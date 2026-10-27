package com.example.ashenrealm;

import com.mojang.serialization.Codec;

import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.ChestBlock;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.ChestBlockEntity;
import net.minecraft.loot.LootTable;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.random.Random;
import net.minecraft.world.StructureWorldAccess;
import net.minecraft.world.gen.feature.DefaultFeatureConfig;
import net.minecraft.world.gen.feature.Feature;
import net.minecraft.world.gen.feature.util.FeatureContext;

/** Torre en ruinas con un cofre que contiene el Núcleo Cenizo. */
public class AshenRuinFeature extends Feature<DefaultFeatureConfig> {
    private static final RegistryKey<LootTable> LOOT =
            RegistryKey.of(RegistryKeys.LOOT_TABLE, AshenRealm.id("chests/ruin"));

    public AshenRuinFeature(Codec<DefaultFeatureConfig> codec) {
        super(codec);
    }

    @Override
    public boolean generate(FeatureContext<DefaultFeatureConfig> context) {
        StructureWorldAccess world = context.getWorld();
        BlockPos origin = context.getOrigin();
        Random random = context.getRandom();

        if (!world.getFluidState(origin.down()).isEmpty()) return false;

        final int radius = 3;
        final int height = 8;

        for (int dx = -radius; dx <= radius; dx++) {
            for (int dz = -radius; dz <= radius; dz++) {
                BlockPos floor = origin.add(dx, -1, dz);
                FeatureUtil.set(world, floor, FeatureUtil.BRICKS);
                FeatureUtil.support(world, floor.down(), 12);

                boolean edge = Math.abs(dx) == radius || Math.abs(dz) == radius;
                boolean corner = Math.abs(dx) == radius && Math.abs(dz) == radius;

                for (int dy = 0; dy < height; dy++) {
                    BlockPos p = origin.add(dx, dy, dz);
                    if (!edge) {
                        FeatureUtil.set(world, p, FeatureUtil.AIR);
                        continue;
                    }
                    boolean door = dx == 0 && dz == -radius && dy < 2;
                    float skip = corner ? dy * 0.04f : dy * 0.11f; // más derruido arriba
                    if (door || random.nextFloat() < skip) {
                        FeatureUtil.set(world, p, FeatureUtil.AIR);
                    } else {
                        FeatureUtil.set(world, p, random.nextInt(4) == 0 ? FeatureUtil.CRACKED
                                : random.nextInt(6) == 0 ? FeatureUtil.BLACKSTONE : FeatureUtil.BRICKS);
                    }
                }
            }
        }

        // Linternas de almas en las esquinas
        for (int sx : new int[] {-radius + 1, radius - 1}) {
            for (int sz : new int[] {-radius + 1, radius - 1}) {
                FeatureUtil.set(world, origin.add(sx, 0, sz), Blocks.SOUL_CAMPFIRE.getDefaultState());
            }
        }

        // Cofre con botín
        BlockState chest = Blocks.CHEST.getDefaultState().with(ChestBlock.FACING, Direction.NORTH);
        FeatureUtil.set(world, origin, chest);
        BlockEntity be = world.getBlockEntity(origin);
        if (be instanceof ChestBlockEntity chestEntity) {
            chestEntity.setLootTable(LOOT, random.nextLong());
        }
        return true;
    }
}
