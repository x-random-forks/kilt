package xyz.bluspring.kilt.injections.world.level;

import net.minecraftforge.common.util.BlockSnapshot;

import java.util.ArrayList;

public interface LevelInjection {
    default ArrayList<BlockSnapshot> getCapturedBlockSnapshots() {
        throw new IllegalStateException();
    }

    void kilt$setCapturingSnapshots(boolean value);
    void kilt$setRestoringSnapshots(boolean value);
}
