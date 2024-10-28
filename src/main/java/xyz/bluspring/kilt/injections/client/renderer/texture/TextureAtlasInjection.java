package xyz.bluspring.kilt.injections.client.renderer.texture;

import net.minecraft.resources.ResourceLocation;

import java.util.Set;

public interface TextureAtlasInjection {
    Set<ResourceLocation> getTextureLocations();
}
