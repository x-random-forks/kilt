package xyz.bluspring.kilt.forgeinjects.commands.arguments.selector;

import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.brigadier.suggestion.SuggestionsBuilder;
import net.minecraft.commands.arguments.selector.EntitySelector;
import net.minecraft.commands.arguments.selector.EntitySelectorParser;
import net.minecraftforge.common.command.EntitySelectorManager;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(EntitySelectorParser.class)
public abstract class EntitySelectorParserInject {
    @Inject(method = "parse", at = @At(value = "INVOKE", target = "Lnet/minecraft/commands/arguments/selector/EntitySelectorParser;parseSelector()V"), cancellable = true)
    private void kilt$tryUseForgeSelector(CallbackInfoReturnable<EntitySelector> cir) throws CommandSyntaxException {
        var forgeSelector = EntitySelectorManager.parseSelector((EntitySelectorParser) (Object) this);
        if (forgeSelector != null)
            cir.setReturnValue(forgeSelector);
    }

    @Inject(method = "fillSelectorSuggestions", at = @At("TAIL"))
    private static void kilt$fillForgeSelectorSuggestions(SuggestionsBuilder builder, CallbackInfo ci) {
        EntitySelectorManager.fillSelectorSuggestions(builder);
    }
}
