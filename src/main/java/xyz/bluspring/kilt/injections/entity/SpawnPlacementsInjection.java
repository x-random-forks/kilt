package xyz.bluspring.kilt.injections.entity;

import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.SpawnPlacements;
import net.minecraftforge.event.entity.SpawnPlacementRegisterEvent;
import net.minecraftforge.fml.ModLoader;
import xyz.bluspring.kilt.mixin.SpawnPlacementsDataAccessor;

import java.util.HashMap;

public interface SpawnPlacementsInjection {
    static void fireSpawnPlacementEvent() {
        var map = new HashMap<EntityType<?>, SpawnPlacementRegisterEvent.MergedSpawnPredicate<?>>();

        SpawnPlacements.DATA_BY_TYPE.forEach((entityType, data) -> {
            map.put(entityType, new SpawnPlacementRegisterEvent.MergedSpawnPredicate<>(((SpawnPlacementsDataAccessor) data).getPredicate(), ((SpawnPlacementsDataAccessor) data).getPlacement(), ((SpawnPlacementsDataAccessor) data).getHeightMap()));
        });

        ModLoader.get().postEvent(new SpawnPlacementRegisterEvent(map));

        map.forEach((entityType, merged) -> {
            SpawnPlacements.DATA_BY_TYPE.put(entityType, new SpawnPlacements.Data(merged.getHeightmapType(), merged.getSpawnType(), merged.build()));
        });
    }
}
