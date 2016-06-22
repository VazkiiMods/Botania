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

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableSet;
import net.minecraft.block.Block;
import net.minecraft.item.EnumDyeColor;
import vazkii.botania.api.state.BotaniaStateProps;
import vazkii.botania.api.state.enums.BiomeBrickVariant;
import vazkii.botania.api.state.enums.BiomeStoneVariant;
import vazkii.botania.api.state.enums.FutureStoneVariant;
import vazkii.botania.common.block.decor.Block18Stone;
import vazkii.botania.common.block.decor.BlockPavement;
import vazkii.botania.common.block.decor.biomestone.BlockBiomeStoneA;
import vazkii.botania.common.block.decor.biomestone.BlockBiomeStoneB;
import vazkii.botania.common.block.decor.panes.BlockAlfglassPane;
import vazkii.botania.common.block.decor.panes.BlockBifrostPane;
import vazkii.botania.common.block.decor.panes.BlockManaglassPane;
import vazkii.botania.common.block.decor.quartz.BlockSpecialQuartz;
import vazkii.botania.common.block.decor.quartz.BlockSpecialQuartzSlab;
import vazkii.botania.common.block.decor.quartz.BlockSpecialQuartzStairs;
import vazkii.botania.common.block.decor.slabs.Block18StoneSlab;
import vazkii.botania.common.block.decor.slabs.BlockBiomeStoneSlab;
import vazkii.botania.common.block.decor.slabs.BlockDirtPathSlab;
import vazkii.botania.common.block.decor.slabs.BlockEndStoneSlab;
import vazkii.botania.common.block.decor.slabs.BlockEnderBrickSlab;
import vazkii.botania.common.block.decor.slabs.BlockModSlab;
import vazkii.botania.common.block.decor.slabs.BlockPavementSlab;
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
import vazkii.botania.common.block.decor.slabs.living.BlockShimmerrockSlab;
import vazkii.botania.common.block.decor.slabs.living.BlockShimmerwoodPlankSlab;
import vazkii.botania.common.block.decor.slabs.prismarine.BlockDarkPrismarineSlab;
import vazkii.botania.common.block.decor.slabs.prismarine.BlockPrismarineBrickSlab;
import vazkii.botania.common.block.decor.slabs.prismarine.BlockPrismarineSlab;
import vazkii.botania.common.block.decor.stairs.Block18StoneStairs;
import vazkii.botania.common.block.decor.stairs.BlockBiomeStoneStairs;
import vazkii.botania.common.block.decor.stairs.BlockEndStoneStairs;
import vazkii.botania.common.block.decor.stairs.BlockEnderBrickStairs;
import vazkii.botania.common.block.decor.stairs.BlockPavementStairs;
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
import vazkii.botania.common.block.decor.stairs.living.BlockShimmerrockStairs;
import vazkii.botania.common.block.decor.stairs.living.BlockShimmerwoodPlankStairs;
import vazkii.botania.common.block.decor.stairs.prismarine.BlockDarkPrismarineStairs;
import vazkii.botania.common.block.decor.stairs.prismarine.BlockPrismarineBrickStairs;
import vazkii.botania.common.block.decor.stairs.prismarine.BlockPrismarineStairs;
import vazkii.botania.common.block.decor.walls.Block18StoneWall;
import vazkii.botania.common.block.decor.walls.BlockBiomeStoneWall;
import vazkii.botania.common.block.decor.walls.BlockPrismarineWall;
import vazkii.botania.common.block.decor.walls.BlockReedWall;
import vazkii.botania.common.block.decor.walls.living.BlockDreamwoodWall;
import vazkii.botania.common.block.decor.walls.living.BlockLivingrockWall;
import vazkii.botania.common.block.decor.walls.living.BlockLivingwoodWall;
import vazkii.botania.common.core.handler.ConfigHandler;
import vazkii.botania.common.lib.LibBlockNames;

import java.util.Locale;
import java.util.Set;

public final class ModFluffBlocks {

	public static Block livingwoodStairs;
	public static Block livingwoodSlab;
	public static Block livingwoodSlabFull;
	public static Block livingwoodWall;
	public static Block livingwoodPlankStairs;
	public static Block livingwoodPlankSlab;
	public static Block livingwoodPlankSlabFull;
	public static Block livingrockStairs;
	public static Block livingrockSlab;
	public static Block livingrockSlabFull;
	public static Block livingrockWall;
	public static Block livingrockBrickStairs;
	public static Block livingrockBrickSlab;
	public static Block livingrockBrickSlabFull;
	public static Block dreamwoodStairs;
	public static Block dreamwoodSlab;
	public static Block dreamwoodSlabFull;
	public static Block dreamwoodWall;
	public static Block dreamwoodPlankStairs;
	public static Block dreamwoodPlankSlab;
	public static Block dreamwoodPlankSlabFull;

	public static Block prismarineStairs;
	public static Block prismarineSlab;
	public static Block prismarineSlabFull;
	public static Block prismarineWall;
	public static Block prismarineBrickStairs;
	public static Block prismarineBrickSlab;
	public static Block prismarineBrickSlabFull;
	public static Block darkPrismarineStairs;
	public static Block darkPrismarineSlab;
	public static Block darkPrismarineSlabFull;

	public static Block reedStairs;
	public static Block reedSlab;
	public static Block reedSlabFull;
	public static Block reedWall;
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
	public static Block sunnyQuartz;
	public static Block sunnyQuartzSlab;
	public static Block sunnyQuartzSlabFull;
	public static Block sunnyQuartzStairs;

	public static Block dirtPathSlab;
	public static Block dirtPathSlabFull;

	public static Block biomeStoneA;
	public static Block biomeStoneB;
	public static Block stone;
	public static Block pavement;

	public static final Block[] biomeStoneStairs = new Block[24];
	public static final Block[] biomeStoneSlabs = new Block[24];
	public static final Block[] biomeStoneFullSlabs = new Block[24];
	public static Block biomeStoneWall;

	public static final Block[] stoneStairs = new Block[8];
	public static final Block[] stoneSlabs = new Block[8];
	public static final Block[] stoneFullSlabs = new Block[8];
	public static Block stoneWall;

	public static final Block[] pavementStairs = new Block[BlockPavement.TYPES];
	public static final Block[] pavementSlabs = new Block[BlockPavement.TYPES];
	public static final Block[] pavementFullSlabs = new Block[BlockPavement.TYPES];

	public static Block endStoneSlab;
	public static Block endStoneSlabFull;
	public static Block endStoneStairs;
	public static Block enderBrickSlab;
	public static Block enderBrickSlabFull;
	public static Block enderBrickStairs;

	public static Block shimmerrockSlab;
	public static Block shimmerrockSlabFull;
	public static Block shimmerrockStairs;
	public static Block shimmerwoodPlankSlab;
	public static Block shimmerwoodPlankSlabFull;
	public static Block shimmerwoodPlankStairs;

	public static Block managlassPane;
	public static Block alfglassPane;
	public static Block bifrostPane;

	public static void init() {
		livingwoodStairs = new BlockLivingwoodStairs();
		livingwoodSlab = new BlockLivingwoodSlab(false);
		livingwoodSlabFull = new BlockLivingwoodSlab(true);
		livingwoodWall = new BlockLivingwoodWall();
		livingwoodPlankStairs = new BlockLivingwoodPlankStairs();
		livingwoodPlankSlab = new BlockLivingwoodPlankSlab(false);
		livingwoodPlankSlabFull = new BlockLivingwoodPlankSlab(true);
		livingrockStairs = new BlockLivingrockStairs();
		livingrockSlab = new BlockLivingrockSlab(false);
		livingrockSlabFull = new BlockLivingrockSlab(true);
		livingrockWall = new BlockLivingrockWall();
		livingrockBrickStairs = new BlockLivingrockBrickStairs();
		livingrockBrickSlab = new BlockLivingrockBrickSlab(false);
		livingrockBrickSlabFull = new BlockLivingrockBrickSlab(true);
		dreamwoodStairs = new BlockDreamwoodStairs();
		dreamwoodSlab = new BlockDreamwoodSlab(false);
		dreamwoodSlabFull = new BlockDreamwoodSlab(true);
		dreamwoodWall = new BlockDreamwoodWall();
		dreamwoodPlankStairs = new BlockDreamwoodPlankStairs();
		dreamwoodPlankSlab = new BlockDreamwoodPlankSlab(false);
		dreamwoodPlankSlabFull = new BlockDreamwoodPlankSlab(true);

		prismarineStairs = new BlockPrismarineStairs();
		prismarineSlab = new BlockPrismarineSlab(false);
		prismarineSlabFull = new BlockPrismarineSlab(true);
		prismarineWall = new BlockPrismarineWall();
		prismarineBrickStairs = new BlockPrismarineBrickStairs();
		prismarineBrickSlab = new BlockPrismarineBrickSlab(false);
		prismarineBrickSlabFull = new BlockPrismarineBrickSlab(true);
		darkPrismarineStairs = new BlockDarkPrismarineStairs();
		darkPrismarineSlab = new BlockDarkPrismarineSlab(false);
		darkPrismarineSlabFull = new BlockDarkPrismarineSlab(true);

		reedStairs = new BlockReedStairs();
		reedSlab = new BlockReedSlab(false);
		reedSlabFull = new BlockReedSlab(true);
		reedWall = new BlockReedWall();
		thatchStairs = new BlockThatchStairs();
		thatchSlab = new BlockThatchSlab(false);
		thatchSlabFull = new BlockThatchSlab(true);

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
		stone = new Block18Stone();
		pavement = new BlockPavement();

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
		sunnyQuartz = new BlockSpecialQuartz(LibBlockNames.QUARTZ_SUNNY);
		sunnyQuartzSlab = new BlockSpecialQuartzSlab(sunnyQuartz, false);
		sunnyQuartzSlabFull = new BlockSpecialQuartzSlab(sunnyQuartz, true);
		sunnyQuartzStairs = new BlockSpecialQuartzStairs(sunnyQuartz);

		dirtPathSlab = new BlockDirtPathSlab(false);
		dirtPathSlabFull = new BlockDirtPathSlab(true);

		int count = 0;
		for (BiomeStoneVariant variant : BiomeStoneVariant.values()) {
			biomeStoneStairs[count] = new BlockBiomeStoneStairs(biomeStoneA.getDefaultState().withProperty(BotaniaStateProps.BIOMESTONE_VARIANT, variant));
			biomeStoneSlabs[count] = new BlockBiomeStoneSlab(false, biomeStoneA.getDefaultState().withProperty(BotaniaStateProps.BIOMESTONE_VARIANT, variant), count);
			biomeStoneFullSlabs[count] = new BlockBiomeStoneSlab(true, biomeStoneA.getDefaultState().withProperty(BotaniaStateProps.BIOMESTONE_VARIANT, variant), count);
			count++;
		}

		for (BiomeBrickVariant variant : BiomeBrickVariant.values()) {
			if (variant.getName().toLowerCase(Locale.ROOT).contains("chiseled")) {
				// No chiseled stairs/slabs
				continue;
			}
			biomeStoneStairs[count] = new BlockBiomeStoneStairs(biomeStoneB.getDefaultState().withProperty(BotaniaStateProps.BIOMEBRICK_VARIANT, variant));
			biomeStoneSlabs[count] = new BlockBiomeStoneSlab(false, biomeStoneB.getDefaultState().withProperty(BotaniaStateProps.BIOMEBRICK_VARIANT, variant), count);
			biomeStoneFullSlabs[count] = new BlockBiomeStoneSlab(true, biomeStoneB.getDefaultState().withProperty(BotaniaStateProps.BIOMEBRICK_VARIANT, variant), count);
			count++;
		}
		biomeStoneWall = new BlockBiomeStoneWall();

		Set<FutureStoneVariant> allowed = ImmutableSet.of(FutureStoneVariant.ANDESITE, FutureStoneVariant.ANDESITE_BRICK,
				FutureStoneVariant.BASALT, FutureStoneVariant.BASALT_BRICK, FutureStoneVariant.DIORITE, FutureStoneVariant.DIORITE_BRICK,
				FutureStoneVariant.GRANITE, FutureStoneVariant.GRANITE_BRICK);

		count = 0;
		for(FutureStoneVariant variant : FutureStoneVariant.values()) {
			if (!allowed.contains(variant)) {
				// Filter - only get the base blocks, and the brick versions
				continue;
			}

			stoneStairs[count] = new Block18StoneStairs(variant);
			stoneSlabs[count] = new Block18StoneSlab(false, variant, count);
			stoneFullSlabs[count] = new Block18StoneSlab(true, variant, count);
			count++;
		}
		stoneWall = new Block18StoneWall();

		count = 0;
		for (EnumDyeColor color : ImmutableList.of(EnumDyeColor.WHITE, EnumDyeColor.BLACK, EnumDyeColor.BLUE,
				EnumDyeColor.RED, EnumDyeColor.YELLOW, EnumDyeColor.GREEN)) {
			pavementStairs[count] = new BlockPavementStairs(color);
			pavementSlabs[count] = new BlockPavementSlab(false, color, count);
			pavementFullSlabs[count] = new BlockPavementSlab(true, color, count);
			count++;
		}

		endStoneSlab = new BlockEndStoneSlab(false);
		endStoneSlabFull = new BlockEndStoneSlab(true);
		endStoneStairs = new BlockEndStoneStairs();
		enderBrickSlab = new BlockEnderBrickSlab(false);
		enderBrickSlabFull = new BlockEnderBrickSlab(true);
		enderBrickStairs = new BlockEnderBrickStairs();

		shimmerrockSlab = new BlockShimmerrockSlab(false);
		shimmerrockSlabFull = new BlockShimmerrockSlab(true);
		shimmerrockStairs = new BlockShimmerrockStairs();
		shimmerwoodPlankSlab = new BlockShimmerwoodPlankSlab(false);
		shimmerwoodPlankSlabFull = new BlockShimmerwoodPlankSlab(true);
		shimmerwoodPlankStairs = new BlockShimmerwoodPlankStairs();

		managlassPane = new BlockManaglassPane();
		alfglassPane = new BlockAlfglassPane();
		bifrostPane = new BlockBifrostPane();

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
		((BlockModSlab) sunnyQuartzSlab).register();
		((BlockModSlab) sunnyQuartzSlabFull).register();

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

		((BlockModSlab) endStoneSlab).register();
		((BlockModSlab) endStoneSlabFull).register();
		((BlockModSlab) enderBrickSlab).register();
		((BlockModSlab) enderBrickSlabFull).register();

		((BlockModSlab) shimmerrockSlab).register();
		((BlockModSlab) shimmerrockSlabFull).register();
		((BlockModSlab) shimmerwoodPlankSlab).register();
		((BlockModSlab) shimmerwoodPlankSlabFull).register();

		for(int i = 0; i < 24; i++) {
			((BlockModSlab) biomeStoneSlabs[i]).register();
			((BlockModSlab) biomeStoneFullSlabs[i]).register();
		}

		for(int i = 0; i < 8; i++) {
			((BlockModSlab) stoneSlabs[i]).register();
			((BlockModSlab) stoneFullSlabs[i]).register();
		}

		for(int i = 0; i < pavementSlabs.length; i++) {
			((BlockModSlab) pavementSlabs[i]).register();
			((BlockModSlab) pavementFullSlabs[i]).register();
		}
	}

}
