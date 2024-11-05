// TRACKED HASH: 803f79832294bb467746850ed2c5d03a86aa08e3
package xyz.bluspring.kilt.forgeinjects.locale;

import com.google.common.collect.ImmutableMap;
import net.minecraft.locale.Language;
import net.minecraftforge.server.LanguageHook;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyVariable;
import xyz.bluspring.kilt.injections.locale.LanguageInjection;

import java.util.HashMap;
import java.util.Map;

@Mixin(Language.class)
public abstract class LanguageInject implements LanguageInjection {
    @ModifyVariable(method = "loadDefault", at = @At("STORE"))
    private static Map<String, String> kilt$captureLanguageMap(Map<String, String> original) {
        var map = new HashMap<>(original);
        LanguageHook.captureLanguageMap(map);

        return map;
    }

    @Override
    public Map<String, String> getLanguageData() {
        return ImmutableMap.of();
    }

    @Mixin(targets = "net.minecraft.locale.Language$1")
    public abstract static class AnonymousLanguageInject implements LanguageInjection {
        @Shadow @Final Map<String, String> val$storage;

        @Override
        public Map<String, String> getLanguageData() {
            return this.val$storage;
        }
    }
}