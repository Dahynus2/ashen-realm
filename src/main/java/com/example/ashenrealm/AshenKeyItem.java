package com.example.ashenrealm;

import java.util.Set;

import net.minecraft.block.Blocks;
import net.minecraft.entity.PositionFlag;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.text.Text;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.Heightmap;
import net.minecraft.world.World;

/** Clic derecho: viaja al Reino Cenizo (y de vuelta al Overworld). */
public class AshenKeyItem extends Item {
    public AshenKeyItem(Settings settings) {
        super(settings);
    }

    @Override
    public ActionResult use(World world, PlayerEntity user, Hand hand) {
        if (world.isClient() || !(world instanceof ServerWorld current) || !(user instanceof ServerPlayerEntity player)) {
            return ActionResult.SUCCESS;
        }

        MinecraftServer server = current.getServer();
        boolean inRealm = current.getRegistryKey().equals(ModDimensions.ASHEN_REALM);
        ServerWorld dest = server.getWorld(inRealm ? World.OVERWORLD : ModDimensions.ASHEN_REALM);
        if (dest == null) {
            player.sendMessage(Text.translatable("message.ashenrealm.no_dimension"), true);
            return ActionResult.FAIL;
        }

        BlockPos target = inRealm ? dest.getSpawnPos() : BlockPos.ORIGIN;
        int x = target.getX();
        int z = target.getZ();
        dest.getChunk(x >> 4, z >> 4); // fuerza la carga del chunk
        int y = dest.getTopY(Heightmap.Type.MOTION_BLOCKING, x, z);

        // Plataforma segura bajo los pies si no hay suelo sólido
        BlockPos feet = new BlockPos(x, y, z);
        if (!dest.getBlockState(feet.down()).isSolidBlock(dest, feet.down())) {
            for (int dx = -1; dx <= 1; dx++) {
                for (int dz = -1; dz <= 1; dz++) {
                    dest.setBlockState(feet.add(dx, -1, dz), Blocks.POLISHED_BLACKSTONE_BRICKS.getDefaultState());
                    dest.setBlockState(feet.add(dx, 0, dz), Blocks.AIR.getDefaultState());
                    dest.setBlockState(feet.add(dx, 1, dz), Blocks.AIR.getDefaultState());
                }
            }
        }

        current.spawnParticles(ParticleTypes.PORTAL, player.getX(), player.getY() + 1, player.getZ(), 40, 0.4, 0.8, 0.4, 0.3);
        player.teleport(dest, x + 0.5, y, z + 0.5, Set.<PositionFlag>of(), player.getYaw(), player.getPitch(), true);
        dest.playSound(null, feet, SoundEvents.ENTITY_ENDERMAN_TELEPORT, SoundCategory.PLAYERS, 1.0f, 0.7f);

        ItemStack stack = player.getStackInHand(hand);
        player.getItemCooldownManager().set(stack, 60);
        return ActionResult.SUCCESS;
    }
}
