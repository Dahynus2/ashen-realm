package com.example.ashenrealm;

import net.fabricmc.fabric.api.object.builder.v1.entity.FabricDefaultAttributeRegistry;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.SpawnGroup;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryKeys;

public class ModEntities {
    private static final RegistryKey<EntityType<?>> ASHEN_WARDEN_KEY =
            RegistryKey.of(RegistryKeys.ENTITY_TYPE, AshenRealm.id("ashen_warden"));

    public static final EntityType<AshenWardenEntity> ASHEN_WARDEN = Registry.register(
            Registries.ENTITY_TYPE, ASHEN_WARDEN_KEY,
            EntityType.Builder.create(AshenWardenEntity::new, SpawnGroup.MONSTER)
                    .dimensions(0.8f, 2.4f)
                    .maxTrackingRange(10)
                    .makeFireImmune()
                    .build(ASHEN_WARDEN_KEY));

    public static void init() {
        FabricDefaultAttributeRegistry.register(ASHEN_WARDEN, AshenWardenEntity.createAttributes());
    }
}
