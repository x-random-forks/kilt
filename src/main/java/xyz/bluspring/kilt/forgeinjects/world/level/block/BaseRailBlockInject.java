// TRACKED HASH: 645dd70b9be6b9d17ac805f69fccfcbf748b17c1
package xyz.bluspring.kilt.forgeinjects.world.level.block;

import com.llamalad7.mixinextras.sugar.Local;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.vehicle.AbstractMinecart;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.BaseRailBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.Property;
import net.minecraft.world.level.block.state.properties.RailShape;
import net.minecraftforge.common.extensions.IForgeBaseRailBlock;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(BaseRailBlock.class)
public abstract class BaseRailBlockInject implements IForgeBaseRailBlock {
    @Shadow @Final private boolean isStraight;

    @Shadow public abstract Property<RailShape> getShapeProperty();

    @Redirect(method = {"getShape"}, at = @At(value = "INVOKE", target = "Lnet/minecraft/world/level/block/state/BlockState;getValue(Lnet/minecraft/world/level/block/state/properties/Property;)Ljava/lang/Comparable;"))
    private Comparable<?> kilt$useForgeRailDirection(BlockState instance, Property<?> property, @Local(argsOnly = true) BlockGetter level, @Local(argsOnly = true) BlockPos pos) {
        return getRailDirection(instance, level, pos, (AbstractMinecart) null);
    }

    @Redirect(method = {"neighborChanged", "onRemove"}, at = @At(value = "INVOKE", target = "Lnet/minecraft/world/level/block/state/BlockState;getValue(Lnet/minecraft/world/level/block/state/properties/Property;)Ljava/lang/Comparable;", ordinal = 0))
    private Comparable<?> kilt$useForgeRailDirection(BlockState instance, Property<?> property, @Local(argsOnly = true) Level level, @Local(argsOnly = true, ordinal = 0) BlockPos pos) {
        return getRailDirection(instance, level, pos, (AbstractMinecart) null);
    }

    @Override
    public boolean isFlexibleRail(BlockState state, BlockGetter level, BlockPos pos) {
        return !this.isStraight;
    }

    @Override
    public RailShape getRailDirection(BlockState state, BlockGetter level, BlockPos pos, @Nullable AbstractMinecart cart) {
        return state.getValue(getShapeProperty());
    }
}