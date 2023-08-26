package vazkii.botania.common.block;

import net.minecraft.world.level.block.Blocks;

import vazkii.botania.mixin.FireBlockAccessor;

import static vazkii.botania.common.block.BotaniaBlocks.*;

public abstract class BotaniaBlockFlammability {
	public static void register() {
		FireBlockAccessor b = (FireBlockAccessor) Blocks.FIRE;
		// [VanillaCopy] FireBlock
		int logIgnite = 5;
		int logBurn = 5;
		// Planks, slabs, stairs, etc.
		int nonLogIgnite = 5;
		int nonLogBurn = 20;

		b.botania_register(livingwoodLog, logIgnite, logBurn);
		b.botania_register(livingwoodLogStripped, logIgnite, logBurn);
		b.botania_register(livingwoodLogGlimmering, logIgnite, logBurn);
		b.botania_register(livingwoodLogStrippedGlimmering, logIgnite, logBurn);
		b.botania_register(livingwood, logIgnite, logBurn);
		b.botania_register(livingwoodStripped, logIgnite, logBurn);
		b.botania_register(livingwoodGlimmering, logIgnite, logBurn);
		b.botania_register(livingwoodStrippedGlimmering, logIgnite, logBurn);
		b.botania_register(livingwoodPlanks, nonLogIgnite, nonLogBurn);
		b.botania_register(livingwoodPlanks, nonLogIgnite, nonLogBurn);
		b.botania_register(livingwoodPlanksMossy, nonLogIgnite, nonLogBurn);
		b.botania_register(livingwoodFramed, nonLogIgnite, nonLogBurn);
		b.botania_register(livingwoodPatternFramed, nonLogIgnite, nonLogBurn);

		b.botania_register(dreamwoodLog, logIgnite, logBurn);
		b.botania_register(dreamwoodLogStripped, logIgnite, logBurn);
		b.botania_register(dreamwoodLogGlimmering, logIgnite, logBurn);
		b.botania_register(dreamwoodLogStrippedGlimmering, logIgnite, logBurn);
		b.botania_register(dreamwood, logIgnite, logBurn);
		b.botania_register(dreamwoodStripped, logIgnite, logBurn);
		b.botania_register(dreamwoodGlimmering, logIgnite, logBurn);
		b.botania_register(dreamwoodStrippedGlimmering, logIgnite, logBurn);
		b.botania_register(dreamwoodPlanks, nonLogIgnite, nonLogBurn);
		b.botania_register(dreamwoodPlanks, nonLogIgnite, nonLogBurn);
		b.botania_register(dreamwoodPlanksMossy, nonLogIgnite, nonLogBurn);
		b.botania_register(dreamwoodFramed, nonLogIgnite, nonLogBurn);
		b.botania_register(dreamwoodPatternFramed, nonLogIgnite, nonLogBurn);

		b.botania_register(solidVines, 15, 100); // matches vines
		b.botania_register(cellBlock, 30, 60); // matches leaves
		b.botania_register(shimmerwoodPlanks, nonLogIgnite, nonLogBurn);

		b.botania_register(livingwoodStairs, nonLogIgnite, nonLogBurn);
		b.botania_register(livingwoodSlab, nonLogIgnite, nonLogBurn);
		b.botania_register(livingwoodWall, nonLogIgnite, nonLogBurn);
		b.botania_register(livingwoodFence, nonLogIgnite, nonLogBurn);
		b.botania_register(livingwoodFenceGate, nonLogIgnite, nonLogBurn);
		b.botania_register(livingwoodStrippedStairs, nonLogIgnite, nonLogBurn);
		b.botania_register(livingwoodStrippedSlab, nonLogIgnite, nonLogBurn);
		b.botania_register(livingwoodStrippedWall, nonLogIgnite, nonLogBurn);
		b.botania_register(livingwoodPlankStairs, nonLogIgnite, nonLogBurn);
		b.botania_register(livingwoodPlankSlab, nonLogIgnite, nonLogBurn);

		b.botania_register(dreamwoodStairs, nonLogIgnite, nonLogBurn);
		b.botania_register(dreamwoodSlab, nonLogIgnite, nonLogBurn);
		b.botania_register(dreamwoodWall, nonLogIgnite, nonLogBurn);
		b.botania_register(dreamwoodFence, nonLogIgnite, nonLogBurn);
		b.botania_register(dreamwoodFenceGate, nonLogIgnite, nonLogBurn);
		b.botania_register(dreamwoodStrippedStairs, nonLogIgnite, nonLogBurn);
		b.botania_register(dreamwoodStrippedSlab, nonLogIgnite, nonLogBurn);
		b.botania_register(dreamwoodStrippedWall, nonLogIgnite, nonLogBurn);
		b.botania_register(dreamwoodPlankStairs, nonLogIgnite, nonLogBurn);
		b.botania_register(dreamwoodPlankSlab, nonLogIgnite, nonLogBurn);

		b.botania_register(shimmerwoodPlankSlab, nonLogIgnite, nonLogBurn);
		b.botania_register(shimmerwoodPlankStairs, nonLogIgnite, nonLogBurn);
	}
}
