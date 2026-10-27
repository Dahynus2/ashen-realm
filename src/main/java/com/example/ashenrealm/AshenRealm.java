package com.example.ashenrealm;

import net.fabricmc.api.ModInitializer;
import net.minecraft.util.Identifier;

public class AshenRealm implements ModInitializer {
    public static final String MOD_ID = "ashenrealm";

    public static Identifier id(String path) {
        return Identifier.of(MOD_ID, path);
    }

    @Override
    public void onInitialize() {
        ModItems.init();
        ModEntities.init();
        ModFeatures.init();
        AltarHandler.init();
    }
}
