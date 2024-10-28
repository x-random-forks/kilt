package xyz.bluspring.kilt.injections.client.renderer.texture;

import io.github.fabricators_of_create.porting_lib.extensions.extensions.AbstractTextureExtensions;

public interface AbstractTextureInjection extends AbstractTextureExtensions {
    void setBlurMipmap(boolean blur, boolean mipmap);
    void restoreLastBlurMipmap();
}
