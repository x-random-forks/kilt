package xyz.bluspring.kilt.forgeinjects.client.renderer.texture;

import com.llamalad7.mixinextras.sugar.Local;
import com.llamalad7.mixinextras.sugar.ref.LocalIntRef;
import com.mojang.blaze3d.platform.NativeImage;
import net.minecraft.client.renderer.texture.MipmapGenerator;
import net.minecraftforge.client.ForgeHooksClient;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyArgs;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import org.spongepowered.asm.mixin.injection.invoke.arg.Args;

@Mixin(MipmapGenerator.class)
public abstract class MipmapGeneratorInject {
    @Inject(method = "generateMipLevels", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/renderer/texture/MipmapGenerator;hasTransparentPixel(Lcom/mojang/blaze3d/platform/NativeImage;)Z", shift = At.Shift.AFTER))
    private static void kilt$checkMaxMipmapLevel(CallbackInfoReturnable<NativeImage[]> cir, @Local(ordinal = 1) NativeImage[] images, @Local(argsOnly = true) LocalIntRef mipLevel) {
        mipLevel.set(ForgeHooksClient.getMaxMipmapLevel(images[0].getWidth(), images[1].getHeight()));
    }

    @ModifyArgs(method = "generateMipLevels", at = @At(value = "NEW", target = "(IIZ)Lcom/mojang/blaze3d/platform/NativeImage;"))
    private static void kilt$guardInvalidTextureSize(Args args) {
        args.set(0, Math.max(1, args.get(0)));
        args.set(1, Math.max(1, args.get(1)));
    }
}
