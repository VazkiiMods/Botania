/*
 * This class is distributed as part of the Botania Mod.
 * Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 *
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 */

package vazkii.botania.common.block;

import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.FireBlock;

import static vazkii.botania.common.block.BotaniaBlocks.*;

public abstract class BotaniaBlockFlammability {
	public static void register() {
		FireBlock b = (FireBlock) Blocks.FIRE;
		// [VanillaCopy] FireBlock
		int logIgnite = 5;
		int logBurn = 5;
		// Planks, slabs, stairs, etc.
		int nonLogIgnite = 5;
		int nonLogBurn = 20;

		b.setFlammable(livingwoodLog, logIgnite, logBurn);
		b.setFlammable(livingwoodLogStripped, logIgnite, logBurn);
		b.setFlammable(livingwoodLogGlimmering, logIgnite, logBurn);
		b.setFlammable(livingwoodLogStrippedGlimmering, logIgnite, logBurn);
		b.setFlammable(livingwood, logIgnite, logBurn);
		b.setFlammable(livingwoodStripped, logIgnite, logBurn);
		b.setFlammable(livingwoodGlimmering, logIgnite, logBurn);
		b.setFlammable(livingwoodStrippedGlimmering, logIgnite, logBurn);
		b.setFlammable(livingwoodPlanks, nonLogIgnite, nonLogBurn);
		b.setFlammable(livingwoodPlanks, nonLogIgnite, nonLogBurn);
		b.setFlammable(livingwoodPlanksMossy, nonLogIgnite, nonLogBurn);
		b.setFlammable(livingwoodFramed, nonLogIgnite, nonLogBurn);
		b.setFlammable(livingwoodPatternFramed, nonLogIgnite, nonLogBurn);

		b.setFlammable(dreamwoodLog, logIgnite, logBurn);
		b.setFlammable(dreamwoodLogStripped, logIgnite, logBurn);
		b.setFlammable(dreamwoodLogGlimmering, logIgnite, logBurn);
		b.setFlammable(dreamwoodLogStrippedGlimmering, logIgnite, logBurn);
		b.setFlammable(dreamwood, logIgnite, logBurn);
		b.setFlammable(dreamwoodStripped, logIgnite, logBurn);
		b.setFlammable(dreamwoodGlimmering, logIgnite, logBurn);
		b.setFlammable(dreamwoodStrippedGlimmering, logIgnite, logBurn);
		b.setFlammable(dreamwoodPlanks, nonLogIgnite, nonLogBurn);
		b.setFlammable(dreamwoodPlanks, nonLogIgnite, nonLogBurn);
		b.setFlammable(dreamwoodPlanksMossy, nonLogIgnite, nonLogBurn);
		b.setFlammable(dreamwoodFramed, nonLogIgnite, nonLogBurn);
		b.setFlammable(dreamwoodPatternFramed, nonLogIgnite, nonLogBurn);

		b.setFlammable(solidVines, 15, 100); // matches vines
		b.setFlammable(cellBlock, 30, 60); // matches leaves
		b.setFlammable(shimmerwoodPlanks, nonLogIgnite, nonLogBurn);

		b.setFlammable(livingwoodStairs, nonLogIgnite, nonLogBurn);
		b.setFlammable(livingwoodSlab, nonLogIgnite, nonLogBurn);
		b.setFlammable(livingwoodWall, nonLogIgnite, nonLogBurn);
		b.setFlammable(livingwoodFence, nonLogIgnite, nonLogBurn);
		b.setFlammable(livingwoodFenceGate, nonLogIgnite, nonLogBurn);
		b.setFlammable(livingwoodStrippedStairs, nonLogIgnite, nonLogBurn);
		b.setFlammable(livingwoodStrippedSlab, nonLogIgnite, nonLogBurn);
		b.setFlammable(livingwoodStrippedWall, nonLogIgnite, nonLogBurn);
		b.setFlammable(livingwoodPlankStairs, nonLogIgnite, nonLogBurn);
		b.setFlammable(livingwoodPlankSlab, nonLogIgnite, nonLogBurn);

		b.setFlammable(dreamwoodStairs, nonLogIgnite, nonLogBurn);
		b.setFlammable(dreamwoodSlab, nonLogIgnite, nonLogBurn);
		b.setFlammable(dreamwoodWall, nonLogIgnite, nonLogBurn);
		b.setFlammable(dreamwoodFence, nonLogIgnite, nonLogBurn);
		b.setFlammable(dreamwoodFenceGate, nonLogIgnite, nonLogBurn);
		b.setFlammable(dreamwoodStrippedStairs, nonLogIgnite, nonLogBurn);
		b.setFlammable(dreamwoodStrippedSlab, nonLogIgnite, nonLogBurn);
		b.setFlammable(dreamwoodStrippedWall, nonLogIgnite, nonLogBurn);
		b.setFlammable(dreamwoodPlankStairs, nonLogIgnite, nonLogBurn);
		b.setFlammable(dreamwoodPlankSlab, nonLogIgnite, nonLogBurn);

		b.setFlammable(shimmerwoodPlankSlab, nonLogIgnite, nonLogBurn);
		b.setFlammable(shimmerwoodPlankStairs, nonLogIgnite, nonLogBurn);
		b.setFlammable(shimmerwoodFence, nonLogIgnite, nonLogBurn);
		b.setFlammable(shimmerwoodFenceGate, nonLogIgnite, nonLogBurn);
	}
}
