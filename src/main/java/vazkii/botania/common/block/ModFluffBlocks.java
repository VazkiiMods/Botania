/**
 * This class was created by <Vazkii>. It's distributed as
 * part of the Botania Mod. Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 * 
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 * 
 * File Created @ [Jan 24, 2015, 2:35:08 PM (GMT)]
 */
package vazkii.botania.common.block;

import net.minecraft.block.Block;
import vazkii.botania.common.block.decor.biomestone.BlockBiomeStoneA;
import vazkii.botania.common.block.decor.biomestone.BlockBiomeStoneB;
import vazkii.botania.common.block.decor.quartz.BlockSpecialQuartz;
import vazkii.botania.common.block.decor.quartz.BlockSpecialQuartzSlab;
import vazkii.botania.common.block.decor.quartz.BlockSpecialQuartzStairs;
import vazkii.botania.common.block.decor.slabs.BlockBiomeStoneSlab;
import vazkii.botania.common.block.decor.slabs.BlockDirtPathSlab;
import vazkii.botania.common.block.decor.slabs.BlockModSlab;
import vazkii.botania.common.block.decor.slabs.BlockReedSlab;
import vazkii.botania.common.block.decor.slabs.BlockThatchSlab;
import vazkii.botania.common.block.decor.slabs.bricks.BlockCustomBrickSlab;
import vazkii.botania.common.block.decor.slabs.bricks.BlockSnowBrickSlab;
import vazkii.botania.common.block.decor.slabs.bricks.BlockSoulBrickSlab;
import vazkii.botania.common.block.decor.slabs.bricks.BlockTileSlab;
import vazkii.botania.common.block.decor.slabs.living.BlockDreamwoodPlankSlab;
import vazkii.botania.common.block.decor.slabs.living.BlockDreamwoodSlab;
import vazkii.botania.common.block.decor.slabs.living.BlockLivingrockBrickSlab;
import vazkii.botania.common.block.decor.slabs.living.BlockLivingrockSlab;
import vazkii.botania.common.block.decor.slabs.living.BlockLivingwoodPlankSlab;
import vazkii.botania.common.block.decor.slabs.living.BlockLivingwoodSlab;
import vazkii.botania.common.block.decor.slabs.prismarine.BlockDarkPrismarineSlab;
import vazkii.botania.common.block.decor.slabs.prismarine.BlockPrismarineBrickSlab;
import vazkii.botania.common.block.decor.slabs.prismarine.BlockPrismarineSlab;
import vazkii.botania.common.block.decor.stairs.BlockLivingStairs;
import vazkii.botania.common.block.decor.stairs.BlockReedStairs;
import vazkii.botania.common.block.decor.stairs.BlockThatchStairs;
import vazkii.botania.common.block.decor.stairs.bricks.BlockCustomBrickStairs;
import vazkii.botania.common.block.decor.stairs.bricks.BlockSnowBrickStairs;
import vazkii.botania.common.block.decor.stairs.bricks.BlockSoulBrickStairs;
import vazkii.botania.common.block.decor.stairs.bricks.BlockTileStairs;
import vazkii.botania.common.block.decor.stairs.living.BlockDreamwoodPlankStairs;
import vazkii.botania.common.block.decor.stairs.living.BlockDreamwoodStairs;
import vazkii.botania.common.block.decor.stairs.living.BlockLivingrockBrickStairs;
import vazkii.botania.common.block.decor.stairs.living.BlockLivingrockStairs;
import vazkii.botania.common.block.decor.stairs.living.BlockLivingwoodPlankStairs;
import vazkii.botania.common.block.decor.stairs.living.BlockLivingwoodStairs;
import vazkii.botania.common.block.decor.stairs.prismarine.BlockDarkPrismarineStairs;
import vazkii.botania.common.block.decor.stairs.prismarine.BlockPrismarineBrickStairs;
import vazkii.botania.common.block.decor.stairs.prismarine.BlockPrismarineStairs;
import vazkii.botania.common.core.handler.ConfigHandler;
import vazkii.botania.common.lib.LibBlockNames;

public final class ModFluffBlocks {

	public static Block livingwoodStairs;
	public static Block livingwoodSlab;
	public static Block livingwoodSlabFull;
	public static Block livingwoodPlankStairs;
	public static Block livingwoodPlankSlab;
	public static Block livingwoodPlankSlabFull;
	public static Block livingrockStairs;
	public static Block livingrockSlab;
	public static Block livingrockSlabFull;
	public static Block livingrockBrickStairs;
	public static Block livingrockBrickSlab;
	public static Block livingrockBrickSlabFull;
	public static Block dreamwoodStairs;
	public static Block dreamwoodSlab;
	public static Block dreamwoodSlabFull;
	public static Block dreamwoodPlankStairs;
	public static Block dreamwoodPlankSlab;
	public static Block dreamwoodPlankSlabFull;

	public static Block prismarineStairs;
	public static Block prismarineSlab;
	public static Block prismarineSlabFull;
	public static Block prismarineBrickStairs;
	public static Block prismarineBrickSlab;
	public static Block prismarineBrickSlabFull;
	public static Block darkPrismarineStairs;
	public static Block darkPrismarineSlab;
	public static Block darkPrismarineSlabFull;

	public static Block reedStairs;
	public static Block reedSlab;
	public static Block reedSlabFull;
	public static Block thatchStairs;
	public static Block thatchSlab;
	public static Block thatchSlabFull;

	public static Block netherBrickStairs;
	public static Block netherBrickSlab;
	public static Block netherBrickSlabFull;
	public static Block soulBrickStairs;
	public static Block soulBrickSlab;
	public static Block soulBrickSlabFull;
	public static Block snowBrickStairs;
	public static Block snowBrickSlab;
	public static Block snowBrickSlabFull;
	public static Block tileStairs;
	public static Block tileSlab;
	public static Block tileSlabFull;

	public static Block darkQuartz;
	public static Block darkQuartzSlab;
	public static Block darkQuartzSlabFull;
	public static Block darkQuartzStairs;
	public static Block manaQuartz;
	public static Block manaQuartzSlab;
	public static Block manaQuartzSlabFull;
	public static Block manaQuartzStairs;
	public static Block blazeQuartz;
	public static Block blazeQuartzSlab;
	public static Block blazeQuartzSlabFull;
	public static Block blazeQuartzStairs;
	public static Block lavenderQuartz;
	public static Block lavenderQuartzSlab;
	public static Block lavenderQuartzSlabFull;
	public static Block lavenderQuartzStairs;
	public static Block redQuartz;
	public static Block redQuartzSlab;
	public static Block redQuartzSlabFull;
	public static Block redQuartzStairs;
	public static Block elfQuartz;
	public static Block elfQuartzSlab;
	public static Block elfQuartzSlabFull;
	public static Block elfQuartzStairs;
	
	public static Block dirtPathSlab;
	public static Block dirtPathSlabFull;
	
	public static Block biomeStoneA;
	public static Block biomeStoneB;
	
	public static Block[] biomeStoneStairs = new Block[24];
	public static Block[] biomeStoneSlabs = new Block[24];
	public static Block[] biomeStoneFullSlabs = new Block[24];
	
	public static void init() {
		livingwoodStairs = new BlockLivingwoodStairs();
		livingwoodSlab = new BlockLivingwoodSlab(false);
		livingwoodSlabFull = new BlockLivingwoodSlab(true);
		livingwoodPlankStairs = new BlockLivingwoodPlankStairs();
		livingwoodPlankSlab = new BlockLivingwoodPlankSlab(false);
		livingwoodPlankSlabFull = new BlockLivingwoodPlankSlab(true);
		livingrockStairs = new BlockLivingrockStairs();
		livingrockSlab = new BlockLivingrockSlab(false);
		livingrockSlabFull = new BlockLivingrockSlab(true);
		livingrockBrickStairs = new BlockLivingrockBrickStairs();
		livingrockBrickSlab = new BlockLivingrockBrickSlab(false);
		livingrockBrickSlabFull = new BlockLivingrockBrickSlab(true);
		dreamwoodStairs = new BlockDreamwoodStairs();
		dreamwoodSlab = new BlockDreamwoodSlab(false);
		dreamwoodSlabFull = new BlockDreamwoodSlab(true);
		dreamwoodPlankStairs = new BlockDreamwoodPlankStairs();
		dreamwoodPlankSlab = new BlockDreamwoodPlankSlab(false);
		dreamwoodPlankSlabFull = new BlockDreamwoodPlankSlab(true);

		reedStairs = new BlockReedStairs();
		reedSlab = new BlockReedSlab(false);
		reedSlabFull = new BlockReedSlab(true);
		thatchStairs = new BlockThatchStairs();
		thatchSlab = new BlockThatchSlab(false);
		thatchSlabFull = new BlockThatchSlab(true);

		prismarineStairs = new BlockPrismarineStairs();
		prismarineSlab = new BlockPrismarineSlab(false);
		prismarineSlabFull = new BlockPrismarineSlab(true);
		prismarineBrickStairs = new BlockPrismarineBrickStairs();
		prismarineBrickSlab = new BlockPrismarineBrickSlab(false);
		prismarineBrickSlabFull = new BlockPrismarineBrickSlab(true);
		darkPrismarineStairs = new BlockDarkPrismarineStairs();
		darkPrismarineSlab = new BlockDarkPrismarineSlab(false);
		darkPrismarineSlabFull = new BlockDarkPrismarineSlab(true);

		netherBrickStairs = new BlockCustomBrickStairs();
		netherBrickSlab = new BlockCustomBrickSlab(false);
		netherBrickSlabFull = new BlockCustomBrickSlab(true);
		soulBrickStairs = new BlockSoulBrickStairs();
		soulBrickSlab = new BlockSoulBrickSlab(false);
		soulBrickSlabFull = new BlockSoulBrickSlab(true);
		snowBrickStairs = new BlockSnowBrickStairs();
		snowBrickSlab = new BlockSnowBrickSlab(false);
		snowBrickSlabFull = new BlockSnowBrickSlab(true);
		tileStairs = new BlockTileStairs();
		tileSlab = new BlockTileSlab(false);
		tileSlabFull = new BlockTileSlab(true);
		
		biomeStoneA = new BlockBiomeStoneA();
		biomeStoneB = new BlockBiomeStoneB();

		if(ConfigHandler.darkQuartzEnabled) {
			darkQuartz = new BlockSpecialQuartz(LibBlockNames.QUARTZ_DARK);
			darkQuartzSlab = new BlockSpecialQuartzSlab(darkQuartz, false);
			darkQuartzSlabFull = new BlockSpecialQuartzSlab(darkQuartz, true);
			darkQuartzStairs = new BlockSpecialQuartzStairs(darkQuartz);
		}

		manaQuartz = new BlockSpecialQuartz(LibBlockNames.QUARTZ_MANA);
		manaQuartzSlab = new BlockSpecialQuartzSlab(manaQuartz, false);
		manaQuartzSlabFull = new BlockSpecialQuartzSlab(manaQuartz, true);
		manaQuartzStairs = new BlockSpecialQuartzStairs(manaQuartz);
		blazeQuartz = new BlockSpecialQuartz(LibBlockNames.QUARTZ_BLAZE);
		blazeQuartzSlab = new BlockSpecialQuartzSlab(blazeQuartz, false);
		blazeQuartzSlabFull = new BlockSpecialQuartzSlab(blazeQuartz, true);
		blazeQuartzStairs = new BlockSpecialQuartzStairs(blazeQuartz);
		lavenderQuartz = new BlockSpecialQuartz(LibBlockNames.QUARTZ_LAVENDER);
		lavenderQuartzSlab = new BlockSpecialQuartzSlab(lavenderQuartz, false);
		lavenderQuartzSlabFull = new BlockSpecialQuartzSlab(lavenderQuartz, true);
		lavenderQuartzStairs = new BlockSpecialQuartzStairs(lavenderQuartz);
		redQuartz = new BlockSpecialQuartz(LibBlockNames.QUARTZ_RED);
		redQuartzSlab = new BlockSpecialQuartzSlab(redQuartz, false);
		redQuartzSlabFull = new BlockSpecialQuartzSlab(redQuartz, true);
		redQuartzStairs = new BlockSpecialQuartzStairs(redQuartz);
		elfQuartz = new BlockSpecialQuartz(LibBlockNames.QUARTZ_ELF);
		elfQuartzSlab = new BlockSpecialQuartzSlab(elfQuartz, false);
		elfQuartzSlabFull = new BlockSpecialQuartzSlab(elfQuartz, true);
		elfQuartzStairs = new BlockSpecialQuartzStairs(elfQuartz);

		dirtPathSlab = new BlockDirtPathSlab(false);
		dirtPathSlabFull = new BlockDirtPathSlab(true);
		
		for(int i = 0; i < 24; i++) {
			int meta = i % 16;
			Block block = i < 16 ? biomeStoneA : biomeStoneB;
			biomeStoneStairs[i] = new BlockLivingStairs(block, meta);
			biomeStoneSlabs[i] = new BlockBiomeStoneSlab(false, block, meta, i);
			biomeStoneFullSlabs[i] = new BlockBiomeStoneSlab(true, block, meta, i);
		}
		
		if(ConfigHandler.darkQuartzEnabled) {
			((BlockModSlab) darkQuartzSlab).register();
			((BlockModSlab) darkQuartzSlabFull).register();
		}
		((BlockModSlab) manaQuartzSlab).register();
		((BlockModSlab) manaQuartzSlabFull).register();
		((BlockModSlab) blazeQuartzSlab).register();
		((BlockModSlab) blazeQuartzSlabFull).register();
		((BlockModSlab) lavenderQuartzSlab).register();
		((BlockModSlab) lavenderQuartzSlabFull).register();
		((BlockModSlab) redQuartzSlab).register();
		((BlockModSlab) redQuartzSlabFull).register();
		((BlockModSlab) elfQuartzSlab).register();
		((BlockModSlab) elfQuartzSlabFull).register();

		((BlockModSlab) livingwoodSlab).register();
		((BlockModSlab) livingwoodSlabFull).register();
		((BlockModSlab) livingwoodPlankSlab).register();
		((BlockModSlab) livingwoodPlankSlabFull).register();
		((BlockModSlab) livingrockSlab).register();
		((BlockModSlab) livingrockSlabFull).register();
		((BlockModSlab) livingrockBrickSlab).register();
		((BlockModSlab) livingrockBrickSlabFull).register();
		((BlockModSlab) dreamwoodSlab).register();
		((BlockModSlab) dreamwoodSlabFull).register();
		((BlockModSlab) dreamwoodPlankSlab).register();
		((BlockModSlab) dreamwoodPlankSlabFull).register();

		((BlockModSlab) reedSlab).register();
		((BlockModSlab) reedSlabFull).register();
		((BlockModSlab) thatchSlab).register();
		((BlockModSlab) thatchSlabFull).register();

		((BlockModSlab) prismarineSlab).register();
		((BlockModSlab) prismarineSlabFull).register();
		((BlockModSlab) prismarineBrickSlab).register();
		((BlockModSlab) prismarineBrickSlabFull).register();
		((BlockModSlab) darkPrismarineSlab).register();
		((BlockModSlab) darkPrismarineSlabFull).register();

		((BlockModSlab) netherBrickSlab).register();
		((BlockModSlab) netherBrickSlabFull).register();
		((BlockModSlab) soulBrickSlab).register();
		((BlockModSlab) soulBrickSlabFull).register();
		((BlockModSlab) snowBrickSlab).register();
		((BlockModSlab) snowBrickSlabFull).register();
		((BlockModSlab) tileSlab).register();
		((BlockModSlab) tileSlabFull).register();
		
		((BlockModSlab) dirtPathSlab).register();
		((BlockModSlab) dirtPathSlabFull).register();
		
		for(int i = 0; i < 24; i++) {
			((BlockModSlab) biomeStoneSlabs[i]).register();
			((BlockModSlab) biomeStoneFullSlabs[i]).register();
		}
	}
	
}
