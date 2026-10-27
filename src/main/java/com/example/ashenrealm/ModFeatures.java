package com.example.ashenrealm;

import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.world.gen.feature.DefaultFeatureConfig;
import net.minecraft.world.gen.feature.Feature;

public class ModFeatures {
    public static final Feature<DefaultFeatureConfig> ASHEN_RUIN = Registry.register(
            Registries.FEATURE, AshenRealm.id("ashen_ruin"), new AshenRuinFeature(DefaultFeatureConfig.CODEC));

    public static final Feature<DefaultFeatureConfig> BOSS_ARENA = Registry.register(
            Registries.FEATURE, AshenRealm.id("boss_arena"), new BossArenaFeature(DefaultFeatureConfig.CODEC));

    public static void init() {
    }
}
