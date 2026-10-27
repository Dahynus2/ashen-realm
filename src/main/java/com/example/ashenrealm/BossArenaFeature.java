package com.example.ashenrealm;

import com.mojang.serialization.Codec;

import net.minecraft.block.Blocks;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.random.Random;
import net.minecraft.world.StructureWorldAccess;
import net.minecraft.world.gen.feature.DefaultFeatureConfig;
import net.minecraft.world.gen.feature.Feature;
import net.minecraft.world.gen.feature.util.FeatureContext;

/** Arena circular con 8 pilares y el altar (obsidiana llorona) en el centro. */
public class BossArenaFeature extends Feature<DefaultFeatureConfig> {
    public BossArenaFeature(Codec<DefaultFeatureConfig> codec) {
        super(codec);
    }

    @Override
    public boolean generate(FeatureContext<DefaultFeatureConfig> context) {
        StructureWorldAccess world = context.getWorld();
        BlockPos origin = context.getOrigin();
        Random random = context.getRandom();

        if (!world.getFluidState(origin.down()).isEmpty()) return false;

        final int radius = 9;
        for (int dx = -radius; dx <= radius; dx++) {
            for (int dz = -radius; dz <= radius; dz++) {
                int d2 = dx * dx + dz * dz;
                if (d2 > radius * radius) continue;

                BlockPos floor = origin.add(dx, -1, dz);
                if (d2 >= 49 && d2 <= 64) {
                    FeatureUtil.set(world, floor, random.nextInt(8) == 0
                            ? Blocks.GILDED_BLACKSTONE.getDefaultState() : FeatureUtil.BLACKSTONE);
                } else {
                    FeatureUtil.set(world, floor, random.nextInt(5) == 0 ? FeatureUtil.CRACKED : FeatureUtil.BRICKS);
                }
                FeatureUtil.support(world, floor.down(), 16);

                for (int dy = 0; dy < 12; dy++) {
                    FeatureUtil.set(world, origin.add(dx, dy, dz), FeatureUtil.AIR);
                }
            }
        }

        // Pilares
        for (int k = 0; k < 8; k++) {
            double angle = k * Math.PI / 4.0;
            int px = (int) Math.round(Math.cos(angle) * 8);
            int pz = (int) Math.round(Math.sin(angle) * 8);
            int h = 5 + random.nextInt(3);
            for (int dy = 0; dy < h; dy++) {
                FeatureUtil.set(world, origin.add(px, dy, pz), FeatureUtil.BRICKS);
            }
            FeatureUtil.set(world, origin.add(px, h, pz), Blocks.SOUL_CAMPFIRE.getDefaultState());
        }

        // Altar
        FeatureUtil.set(world, origin, Blocks.CRYING_OBSIDIAN.getDefaultState());
        return true;
    }
}
