package xyz.bluspring.kilt.forgeinjects.commands.arguments.selector;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.arguments.selector.EntitySelector;
import net.minecraftforge.common.ForgeHooks;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(EntitySelector.class)
public abstract class EntitySelectorInject {
    @WrapOperation(method = "checkPermissions", at = @At(value = "INVOKE", target = "Lnet/minecraft/commands/CommandSourceStack;hasPermission(I)Z"))
    private boolean kilt$checkIfCanUseSelectors(CommandSourceStack instance, int i, Operation<Boolean> original) {
        return original.call(instance, i) || ForgeHooks.canUseEntitySelectors(instance);
    }
}
