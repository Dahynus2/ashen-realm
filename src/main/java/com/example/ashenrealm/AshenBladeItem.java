package com.example.ashenrealm;

import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

/** Espada del jefe: prende fuego y marchita a quien golpea. */
public class AshenBladeItem extends Item {
    public AshenBladeItem(Settings settings) {
        super(settings);
    }

    @Override
    public void postHit(ItemStack stack, LivingEntity target, LivingEntity attacker) {
        super.postHit(stack, target, attacker);
        target.setOnFireFor(4);
        target.addStatusEffect(new StatusEffectInstance(StatusEffects.WITHER, 80, 1), attacker);
    }
}
