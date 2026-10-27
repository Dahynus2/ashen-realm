package com.example.ashenrealm.client;

import com.example.ashenrealm.ModEntities;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.rendering.v1.EntityRendererRegistry;

public class AshenRealmClient implements ClientModInitializer {
    @Override
    public void onInitializeClient() {
        EntityRendererRegistry.register(ModEntities.ASHEN_WARDEN, AshenWardenRenderer::new);
    }
}
