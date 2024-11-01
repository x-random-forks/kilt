package xyz.bluspring.kilt.mixin.compat.forge.immersiveengineering;

import com.moulberry.mixinconstraints.annotations.IfModLoaded;
import me.jellysquid.mods.sodium.client.render.chunk.terrain.TerrainRenderPass;
import net.minecraft.client.renderer.RenderType;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@IfModLoaded("sodium")
@Mixin(TerrainRenderPass.class)
public interface TerrainRenderPassAccessor {
    @Accessor
    RenderType getLayer();
}
