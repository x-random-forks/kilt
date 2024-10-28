// TRACKED HASH: 62c27db64352c1bb41803757381fc0825f5315fe
package xyz.bluspring.kilt.forgeinjects.client.renderer.texture;

import net.minecraft.client.renderer.texture.SpriteLoader;
import net.minecraft.client.renderer.texture.TextureAtlas;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.client.ForgeHooksClient;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import xyz.bluspring.kilt.injections.client.renderer.texture.TextureAtlasInjection;

import java.util.Collections;
import java.util.Map;
import java.util.Set;

@Mixin(TextureAtlas.class)
public abstract class TextureAtlasInject implements TextureAtlasInjection {
    @Shadow private Map<ResourceLocation, TextureAtlasSprite> texturesByName;

    @Inject(method = "upload", at = @At("TAIL"))
    private void kilt$callTextureStitchPostEvent(SpriteLoader.Preparations preparations, CallbackInfo ci) {
        ForgeHooksClient.onTextureStitchedPost((TextureAtlas) (Object) this);
    }

    @Override
    public Set<ResourceLocation> getTextureLocations() {
        return Collections.unmodifiableSet(texturesByName.keySet());
    }
}