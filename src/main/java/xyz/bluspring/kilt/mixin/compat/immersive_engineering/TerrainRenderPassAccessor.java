package xyz.bluspring.kilt.mixin.compat.immersive_engineering;

import me.jellysquid.mods.sodium.client.render.chunk.terrain.TerrainRenderPass;
import net.minecraft.client.renderer.RenderType;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(TerrainRenderPass.class)
public interface TerrainRenderPassAccessor {
    @Accessor
    RenderType getLayer();
}
