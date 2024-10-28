package xyz.bluspring.kilt.forgeinjects.client.renderer.texture;

import net.minecraft.client.renderer.texture.Stitcher;
import org.spongepowered.asm.mixin.Mixin;

@Mixin(Stitcher.class)
public abstract class StitcherInject {
    // TODO: is there much point for Kilt to implement this?
}
