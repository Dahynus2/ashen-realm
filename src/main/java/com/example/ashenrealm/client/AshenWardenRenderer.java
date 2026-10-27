package com.example.ashenrealm.client;

import com.example.ashenrealm.AshenRealm;
import com.example.ashenrealm.AshenWardenEntity;

import net.minecraft.client.render.entity.BipedEntityRenderer;
import net.minecraft.client.render.entity.EntityRendererFactory;
import net.minecraft.client.render.entity.model.BipedEntityModel;
import net.minecraft.client.render.entity.model.EntityModelLayers;
import net.minecraft.client.render.entity.state.BipedEntityRenderState;
import net.minecraft.util.Identifier;

/** Usa el modelo humanoide vanilla con una textura propia. El tamaño lo da el atributo SCALE. */
public class AshenWardenRenderer extends
        BipedEntityRenderer<AshenWardenEntity, BipedEntityRenderState, BipedEntityModel<BipedEntityRenderState>> {

    private static final Identifier TEXTURE = AshenRealm.id("textures/entity/ashen_warden.png");

    public AshenWardenRenderer(EntityRendererFactory.Context context) {
        super(context, new BipedEntityModel<>(context.getPart(EntityModelLayers.ZOMBIE)), 0.7f);
    }

    @Override
    public BipedEntityRenderState createRenderState() {
        return new BipedEntityRenderState();
    }

    @Override
    public Identifier getTexture(BipedEntityRenderState state) {
        return TEXTURE;
    }
}
