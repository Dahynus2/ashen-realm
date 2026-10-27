package com.example.ashenrealm;

import net.fabricmc.fabric.api.event.player.UseBlockCallback;
import net.minecraft.block.Blocks;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LightningEntity;
import net.minecraft.entity.SpawnReason;
import net.minecraft.item.ItemStack;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.text.Text;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Box;

/**
 * Altar = Obsidiana llorona dentro del Reino Cenizo.
 * Clic derecho con un Núcleo Cenizo para invocar al jefe.
 */
public class AltarHandler {
    public static void init() {
        UseBlockCallback.EVENT.register((player, world, hand, hit) -> {
            if (world.isClient() || hand != Hand.MAIN_HAND) return ActionResult.PASS;

            ItemStack stack = player.getStackInHand(hand);
            if (!stack.isOf(ModItems.ASHEN_CORE)) return ActionResult.PASS;
            if (!world.getRegistryKey().equals(ModDimensions.ASHEN_REALM)) return ActionResult.PASS;

            BlockPos pos = hit.getBlockPos();
            if (!world.getBlockState(pos).isOf(Blocks.CRYING_OBSIDIAN)) return ActionResult.PASS;

            ServerWorld sw = (ServerWorld) world;
            boolean alive = !sw.getEntitiesByClass(AshenWardenEntity.class, new Box(pos).expand(48), Entity::isAlive).isEmpty();
            if (alive) {
                player.sendMessage(Text.translatable("message.ashenrealm.boss_alive"), true);
                return ActionResult.FAIL;
            }

            AshenWardenEntity boss = ModEntities.ASHEN_WARDEN.create(sw, SpawnReason.TRIGGERED);
            if (boss == null) return ActionResult.FAIL;
            boss.refreshPositionAndAngles(pos.getX() + 0.5, pos.getY() + 1, pos.getZ() + 0.5, 0f, 0f);
            boss.setPersistent();
            sw.spawnEntity(boss);

            LightningEntity bolt = EntityType.LIGHTNING_BOLT.create(sw, SpawnReason.TRIGGERED);
            if (bolt != null) {
                bolt.refreshPositionAndAngles(pos.getX() + 0.5, pos.getY() + 1, pos.getZ() + 0.5, 0f, 0f);
                bolt.setCosmetic(true);
                sw.spawnEntity(bolt);
            }

            if (!player.isCreative()) stack.decrement(1);
            player.sendMessage(Text.translatable("message.ashenrealm.boss_summoned"), true);
            return ActionResult.SUCCESS;
        });
    }
}
