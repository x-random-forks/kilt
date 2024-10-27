package xyz.bluspring.kilt.injections.server.packs.repository;

import net.minecraft.server.packs.PackType;

public interface PackInfoInjection {
    void kilt$setDataFormat(int format);
    void kilt$setResourceFormat(int format);
    void kilt$setHidden(boolean hidden);
    void kilt$markForge();

    int dataFormat();
    int resourceFormat();
    boolean hidden();

    int getFormat(PackType type);
}
