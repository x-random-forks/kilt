package xyz.bluspring.kilt

import com.google.gson.Gson
import com.google.gson.GsonBuilder
import dev.architectury.event.CompoundEventResult
import dev.architectury.event.EventResult
import dev.architectury.event.events.common.EntityEvent
import dev.architectury.event.events.common.InteractionEvent
import dev.architectury.event.events.common.TickEvent.ServerLevelTick
import io.github.fabricators_of_create.porting_lib.entity.events.LivingEntityEvents
import io.github.fabricators_of_create.porting_lib.event.common.ExplosionEvents
import net.fabricmc.api.ModInitializer
import net.fabricmc.fabric.api.entity.event.v1.EntitySleepEvents
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerLifecycleEvents
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerWorldEvents
import net.minecraft.core.BlockPos
import net.minecraft.world.InteractionResult
import net.minecraft.world.level.ChunkPos
import net.minecraft.world.phys.BlockHitResult
import net.minecraftforge.common.ForgeHooks
import net.minecraftforge.common.MinecraftForge
import net.minecraftforge.event.ForgeEventFactory
import net.minecraftforge.event.entity.EntityJoinLevelEvent
import net.minecraftforge.event.entity.living.LivingDropsEvent
import net.minecraftforge.event.level.LevelEvent
import net.minecraftforge.eventbus.api.Event
import net.minecraftforge.server.ServerLifecycleHooks
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import xyz.bluspring.kilt.client.KiltClient
import xyz.bluspring.kilt.interop.transfer.TransferInterop
import xyz.bluspring.kilt.loader.KiltLoader
import xyz.bluspring.kilt.mixin.MinecraftServerAccessor
import xyz.bluspring.kilt.util.DeltaTimeProfiler
import java.util.*

class Kilt : ModInitializer {
    override fun onInitialize() {
        registerFabricEvents()

        TransferInterop.init()
        DeltaTimeProfiler.popAll()
    }

    @Suppress("removal")
    private fun registerFabricEvents() {
        InteractionEvent.RIGHT_CLICK_BLOCK.register { player, hand, pos, direction ->
            val event = ForgeHooks.onRightClickBlock(player, hand, pos, BlockHitResult(pos.center, direction, pos, false))
            eventBusToArchitectury(event.result)
        }

        InteractionEvent.RIGHT_CLICK_ITEM.register { player, hand ->
            val result = ForgeHooks.onItemRightClick(player, hand) ?: return@register CompoundEventResult.pass()

            when (result) {
                InteractionResult.PASS -> CompoundEventResult.pass()
                InteractionResult.FAIL -> CompoundEventResult.interruptFalse(player.getItemInHand(hand))
                InteractionResult.SUCCESS -> CompoundEventResult.interruptTrue(player.getItemInHand(hand))
                else -> CompoundEventResult.interruptDefault(player.getItemInHand(hand))
            }
        }

        InteractionEvent.INTERACT_ENTITY.register { player, entity, hand ->
            val result = ForgeHooks.onInteractEntity(player, entity, hand) ?: return@register EventResult.pass()
            vanillaToArchitectury(result)
        }

        InteractionEvent.LEFT_CLICK_BLOCK.register { player, hand, pos, direction ->
            val event = ForgeHooks.onLeftClickBlock(player, pos, direction)
            eventBusToArchitectury(event.result)
        }

        InteractionEvent.CLIENT_LEFT_CLICK_AIR.register { player, hand ->
            ForgeHooks.onEmptyLeftClick(player)
        }

        InteractionEvent.CLIENT_RIGHT_CLICK_AIR.register { player, hand ->
            ForgeHooks.onEmptyClick(player, hand)
        }

        LivingEntityEvents.DROPS.register { entity, source, drops, level, recentlyHit ->
            MinecraftForge.EVENT_BUS.post(LivingDropsEvent(entity, source, drops, level, recentlyHit))
        }

        LivingEntityEvents.LivingTickEvent.TICK.register { event ->
            if (ForgeHooks.onLivingTick(event.entity))
                event.isCanceled = true
        }

        EntitySleepEvents.ALLOW_SLEEPING.register { player, pos ->
            ForgeEventFactory.onPlayerSleepInBed(player, Optional.of(pos))
        }

        EntitySleepEvents.ALLOW_SETTING_SPAWN.register { player, pos ->
            ForgeEventFactory.onPlayerSpawnSet(player, player.level().dimension(), pos, false)
        }

        EntitySleepEvents.ALLOW_SLEEP_TIME.register { player, pos, _ ->
            val ret = ForgeEventFactory.fireSleepingTimeCheck(player, Optional.of(pos))

            if (ret)
                InteractionResult.SUCCESS
            else
                InteractionResult.FAIL
        }

        ServerLifecycleEvents.SERVER_STARTED.register {
            ServerLifecycleHooks.handleServerStarted(it)
        }

        ServerLifecycleEvents.SERVER_STOPPING.register {
            ServerLifecycleHooks.handleServerStopping(it)
        }

        ServerLifecycleEvents.SERVER_STOPPED.register {
            ServerLifecycleHooks.expectServerStopped()
            ServerLifecycleHooks.handleServerStopped(it)
        }

        ExplosionEvents.START.register { level, explosion ->
            ForgeEventFactory.onExplosionStart(level, explosion)
        }

        ExplosionEvents.DETONATE.register { level, explosion, entities, diameter ->
            ForgeEventFactory.onExplosionDetonate(level, explosion, entities, diameter)
        }

        EntityEvent.ENTER_SECTION.register { entity, sectionX, sectionY, sectionZ, prevX, prevY, prevZ ->
            ForgeHooks.onEntityEnterSection(entity, ChunkPos.asLong(BlockPos(sectionX, sectionY, sectionZ)), ChunkPos.asLong(
                BlockPos(prevX, prevY, prevZ)
            ))
        }

        EntityEvent.ANIMAL_TAME.register { animal, player ->
            if (ForgeEventFactory.onAnimalTame(animal, player))
                EventResult.interruptDefault()
            else
                EventResult.pass()
        }

        EntityEvent.LIVING_HURT.register { entity, source, amount ->
            val newAmount = ForgeHooks.onLivingHurt(entity, source, amount)

            // TODO: set amount
            if (newAmount != 0F) {
                EventResult.interruptDefault()
            } else {
                EventResult.pass()
            }
        }

        EntityEvent.ADD.register { entity, level ->
            if (MinecraftForge.EVENT_BUS.post(EntityJoinLevelEvent(entity, level)))
                EventResult.interruptDefault()
            else
                EventResult.pass()
        }

        ServerLevelTick.SERVER_PRE.register {
            ForgeEventFactory.onPreServerTick((it as MinecraftServerAccessor)::callHaveTime, it)
        }

        ServerLevelTick.SERVER_POST.register {
            ForgeEventFactory.onPostServerTick((it as MinecraftServerAccessor)::callHaveTime, it)
        }

        ServerLevelTick.SERVER_LEVEL_PRE.register {
            ForgeEventFactory.onPreLevelTick(it, (it.server as MinecraftServerAccessor)::callHaveTime)
        }

        ServerLevelTick.SERVER_LEVEL_POST.register {
            ForgeEventFactory.onPostLevelTick(it, (it.server as MinecraftServerAccessor)::callHaveTime)
        }

        ServerLevelTick.PLAYER_PRE.register {
            ForgeEventFactory.onPlayerPreTick(it)
        }

        ServerLevelTick.PLAYER_POST.register {
            ForgeEventFactory.onPlayerPostTick(it)
        }

        ServerWorldEvents.LOAD.register { server, level ->
            MinecraftForge.EVENT_BUS.post(LevelEvent.Load(level))
        }

        ServerWorldEvents.UNLOAD.register { server, level ->
            MinecraftForge.EVENT_BUS.post(LevelEvent.Unload(level))
        }

        LivingEntityEvents.LOOTING_LEVEL.register { source, target, level, _ ->
            ForgeHooks.getLootingLevel(target, source, level)
        }

        LivingEntityEvents.DROPS.register { target, source, drops, level, recentlyHit ->
            ForgeHooks.onLivingDrops(target, source, drops, level, recentlyHit)
        }
    }

    companion object {
        const val MOD_ID = "kilt"

        lateinit var instance: Kilt
        val logger: Logger = LoggerFactory.getLogger(Kilt::class.java)
        val loader: KiltLoader = KiltLoader.INSTANCE
        val gson: Gson = GsonBuilder().setPrettyPrinting().create()

        fun load(onServer: Boolean) {
            loader.initMods()

            // config load should be here
            var loaded = false

            if (!onServer) {
                KiltClient.lateRegisterEvents()
            }
        }

        fun eventBusToArchitectury(result: Event.Result): EventResult {
            return when (result) {
                Event.Result.ALLOW -> EventResult.interruptTrue()
                Event.Result.DEFAULT -> EventResult.pass()
                Event.Result.DENY -> EventResult.interruptFalse()
                else -> EventResult.pass()
            }
        }

        fun vanillaToArchitectury(result: InteractionResult): EventResult {
            return when (result) {
                InteractionResult.PASS -> EventResult.pass()
                InteractionResult.FAIL -> EventResult.interruptFalse()
                InteractionResult.SUCCESS -> EventResult.interruptTrue()
                else -> EventResult.interruptDefault()
            }
        }
    }
}