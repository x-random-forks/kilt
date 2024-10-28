package xyz.bluspring.kilt.forgeinjects.client.player;

import net.minecraft.client.player.RemotePlayer;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraftforge.common.ForgeHooks;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(RemotePlayer.class)
public abstract class RemotePlayerInject {
    @Inject(method = "hurt", at = @At("HEAD"))
    private void kilt$runPlayerAttackEvent(DamageSource source, float amount, CallbackInfoReturnable<Boolean> cir) {
        ForgeHooks.onPlayerAttack((RemotePlayer) (Object) this, source, amount);
    }
}
