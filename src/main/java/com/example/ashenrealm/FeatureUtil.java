package com.example.ashenrealm;

import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.StructureWorldAccess;

final class FeatureUtil {
    static final BlockState BRICKS = Blocks.POLISHED_BLACKSTONE_BRICKS.getDefaultState();
    static final BlockState CRACKED = Blocks.CRACKED_POLISHED_BLACKSTONE_BRICKS.getDefaultState();
    static final BlockState BLACKSTONE = Blocks.BLACKSTONE.getDefaultState();
    static final BlockState AIR = Blocks.AIR.getDefaultState();

    private FeatureUtil() {}

    static void set(StructureWorldAccess world, BlockPos pos, BlockState state) {
        world.setBlockState(pos, state, 2);
    }

    /** Rellena hacia abajo hasta encontrar suelo sólido. */
    static void support(StructureWorldAccess world, BlockPos start, int maxDepth) {
        BlockPos.Mutable m = start.mutableCopy();
        for (int i = 0; i < maxDepth; i++) {
            if (world.getBlockState(m).isSolidBlock(world, m)) break;
            world.setBlockState(m, BLACKSTONE, 2);
            m.move(Direction.DOWN);
        }
    }
}
