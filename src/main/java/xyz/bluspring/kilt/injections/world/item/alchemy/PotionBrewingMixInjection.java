package xyz.bluspring.kilt.injections.world.item.alchemy;

import net.minecraft.core.Holder;
import net.minecraft.world.item.alchemy.PotionBrewing;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraftforge.registries.IForgeRegistry;

public interface PotionBrewingMixInjection<T> {
    Holder.Reference<T> kilt$getFrom();
    Holder.Reference<T> kilt$getTo();

    void kilt$setFrom(Holder.Reference<T> from);
    void kilt$setTo(Holder.Reference<T> to);

    static <T> PotionBrewing.Mix<T> create(IForgeRegistry<T> registry, T from, Ingredient ingredient, T to) {
        var mix = new PotionBrewing.Mix<>(from, ingredient, to);
        ((PotionBrewingMixInjection<T>) mix).kilt$setFrom(registry.getDelegateOrThrow(from));
        ((PotionBrewingMixInjection<T>) mix).kilt$setTo(registry.getDelegateOrThrow(to));

        return mix;
    }
}
