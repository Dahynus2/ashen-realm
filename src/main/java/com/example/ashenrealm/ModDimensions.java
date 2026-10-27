package com.example.ashenrealm;

import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.world.World;

public class ModDimensions {
    public static final RegistryKey<World> ASHEN_REALM =
            RegistryKey.of(RegistryKeys.WORLD, AshenRealm.id("ashen_realm"));
}
