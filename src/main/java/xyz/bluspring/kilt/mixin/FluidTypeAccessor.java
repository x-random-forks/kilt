package xyz.bluspring.kilt.mixin;

import io.github.fabricators_of_create.porting_lib.fluids.FluidType;
import net.minecraft.world.item.Rarity;
import net.minecraft.world.level.pathfinder.BlockPathTypes;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(FluidType.class)
public interface FluidTypeAccessor {
    @Accessor
    String getDescriptionId();

    @Accessor
    double getMotionScale();

    @Accessor
    boolean isCanPushEntity();

    @Accessor
    boolean isCanSwim();

    @Accessor
    boolean isCanDrown();

    @Accessor
    float getFallDistanceModifier();

    @Accessor
    boolean isCanExtinguish();

    @Accessor
    boolean isCanConvertToSource();

    @Accessor
    boolean isSupportsBoating();

    @Accessor
    BlockPathTypes getPathType();

    @Accessor
    BlockPathTypes getAdjacentPathType();

    @Accessor
    boolean isCanHydrate();

    @Accessor
    int getLightLevel();

    @Accessor
    int getDensity();

    @Accessor
    int getTemperature();

    @Accessor
    int getViscosity();

    @Accessor
    Rarity getRarity();
}
