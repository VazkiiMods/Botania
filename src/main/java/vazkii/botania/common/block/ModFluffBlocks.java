/*
 * This class is distributed as part of the Botania Mod.
 * Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 *
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 */
package vazkii.botania.common.block;

import net.minecraft.block.*;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.sound.BlockSoundGroup;
import net.minecraft.util.registry.Registry;

import vazkii.botania.common.block.decor.panes.BlockModPane;
import vazkii.botania.common.block.decor.stairs.BlockModStairs;
import vazkii.botania.common.item.ModItems;
import vazkii.botania.common.lib.LibBlockNames;

import static vazkii.botania.common.block.ModBlocks.*;
import static vazkii.botania.common.lib.LibBlockNames.*;

public final class ModFluffBlocks {
	public static final Block livingwoodStairs = new BlockModStairs(livingwood.getDefaultState(), AbstractBlock.Settings.copy(livingwood));
	public static final Block livingwoodSlab = new SlabBlock(AbstractBlock.Settings.copy(livingwood));
	public static final Block livingwoodWall = new WallBlock(AbstractBlock.Settings.copy(livingwood));
	public static final Block livingwoodFence = new FenceBlock(AbstractBlock.Settings.copy(livingwood));
	public static final Block livingwoodFenceGate = new FenceGateBlock(AbstractBlock.Settings.copy(livingwood));
	public static final Block livingwoodPlankStairs = new BlockModStairs(livingwoodPlanks.getDefaultState(), AbstractBlock.Settings.copy(livingwoodPlanks));
	public static final Block livingwoodPlankSlab = new SlabBlock(AbstractBlock.Settings.copy(livingwoodPlanks));

	public static final Block livingrockStairs = new BlockModStairs(livingrock.getDefaultState(), AbstractBlock.Settings.copy(livingrock));
	public static final Block livingrockSlab = new SlabBlock(AbstractBlock.Settings.copy(livingrock));
	public static final Block livingrockWall = new WallBlock(AbstractBlock.Settings.copy(livingrock));
	public static final Block livingrockBrickStairs = new BlockModStairs(livingrockBrick.getDefaultState(), AbstractBlock.Settings.copy(livingrockBrick));
	public static final Block livingrockBrickSlab = new SlabBlock(AbstractBlock.Settings.copy(livingrockBrick));
	public static final Block livingrockBrickWall = new WallBlock(AbstractBlock.Settings.copy(livingrockBrick));
	public static final Block livingrockBrickMossyStairs = new BlockModStairs(livingrockBrickMossy.getDefaultState(), AbstractBlock.Settings.copy(livingrockBrickMossy));
	public static final Block livingrockBrickMossySlab = new SlabBlock(AbstractBlock.Settings.copy(livingrockBrickMossy));
	public static final Block livingrockBrickMossyWall = new WallBlock(AbstractBlock.Settings.copy(livingrockBrickMossy));

	public static final Block dreamwoodStairs = new BlockModStairs(dreamwood.getDefaultState(), AbstractBlock.Settings.copy(dreamwood));
	public static final Block dreamwoodSlab = new SlabBlock(AbstractBlock.Settings.copy(dreamwood));
	public static final Block dreamwoodWall = new WallBlock(AbstractBlock.Settings.copy(dreamwood));
	public static final Block dreamwoodFence = new FenceBlock(AbstractBlock.Settings.copy(dreamwood));
	public static final Block dreamwoodFenceGate = new FenceGateBlock(AbstractBlock.Settings.copy(dreamwood));
	public static final Block dreamwoodPlankStairs = new BlockModStairs(dreamwoodPlanks.getDefaultState(), AbstractBlock.Settings.copy(dreamwoodPlanks));
	public static final Block dreamwoodPlankSlab = new SlabBlock(AbstractBlock.Settings.copy(dreamwoodPlanks));

	public static final Block darkQuartz = new BlockMod(AbstractBlock.Settings.copy(Blocks.QUARTZ_BLOCK));
	public static final Block darkQuartzChiseled = new BlockMod(AbstractBlock.Settings.copy(darkQuartz));
	public static final Block darkQuartzPillar = new PillarBlock(AbstractBlock.Settings.copy(darkQuartz));
	public static final Block darkQuartzSlab = new SlabBlock(AbstractBlock.Settings.copy(darkQuartz));
	public static final Block darkQuartzStairs = new BlockModStairs(darkQuartz.getDefaultState(), AbstractBlock.Settings.copy(darkQuartz));

	public static final Block manaQuartz = new BlockMod(AbstractBlock.Settings.copy(darkQuartz));
	public static final Block manaQuartzChiseled = new BlockMod(AbstractBlock.Settings.copy(darkQuartz));
	public static final Block manaQuartzPillar = new PillarBlock(AbstractBlock.Settings.copy(darkQuartz));
	public static final Block manaQuartzSlab = new SlabBlock(AbstractBlock.Settings.copy(darkQuartz));
	public static final Block manaQuartzStairs = new BlockModStairs(darkQuartz.getDefaultState(), AbstractBlock.Settings.copy(darkQuartz));

	public static final Block blazeQuartz = new BlockMod(AbstractBlock.Settings.copy(darkQuartz));
	public static final Block blazeQuartzChiseled = new BlockMod(AbstractBlock.Settings.copy(darkQuartz));
	public static final Block blazeQuartzPillar = new PillarBlock(AbstractBlock.Settings.copy(darkQuartz));
	public static final Block blazeQuartzSlab = new SlabBlock(AbstractBlock.Settings.copy(darkQuartz));
	public static final Block blazeQuartzStairs = new BlockModStairs(darkQuartz.getDefaultState(), AbstractBlock.Settings.copy(darkQuartz));

	public static final Block lavenderQuartz = new BlockMod(AbstractBlock.Settings.copy(darkQuartz));
	public static final Block lavenderQuartzChiseled = new BlockMod(AbstractBlock.Settings.copy(darkQuartz));
	public static final Block lavenderQuartzPillar = new PillarBlock(AbstractBlock.Settings.copy(darkQuartz));
	public static final Block lavenderQuartzSlab = new SlabBlock(AbstractBlock.Settings.copy(darkQuartz));
	public static final Block lavenderQuartzStairs = new BlockModStairs(darkQuartz.getDefaultState(), AbstractBlock.Settings.copy(darkQuartz));

	public static final Block redQuartz = new BlockMod(AbstractBlock.Settings.copy(darkQuartz));
	public static final Block redQuartzChiseled = new BlockMod(AbstractBlock.Settings.copy(darkQuartz));
	public static final Block redQuartzPillar = new PillarBlock(AbstractBlock.Settings.copy(darkQuartz));
	public static final Block redQuartzSlab = new SlabBlock(AbstractBlock.Settings.copy(darkQuartz));
	public static final Block redQuartzStairs = new BlockModStairs(darkQuartz.getDefaultState(), AbstractBlock.Settings.copy(darkQuartz));

	public static final Block elfQuartz = new BlockMod(AbstractBlock.Settings.copy(darkQuartz));
	public static final Block elfQuartzChiseled = new BlockMod(AbstractBlock.Settings.copy(darkQuartz));
	public static final Block elfQuartzPillar = new PillarBlock(AbstractBlock.Settings.copy(darkQuartz));
	public static final Block elfQuartzSlab = new SlabBlock(AbstractBlock.Settings.copy(darkQuartz));
	public static final Block elfQuartzStairs = new BlockModStairs(darkQuartz.getDefaultState(), AbstractBlock.Settings.copy(darkQuartz));

	public static final Block sunnyQuartz = new BlockMod(AbstractBlock.Settings.copy(darkQuartz));
	public static final Block sunnyQuartzChiseled = new BlockMod(AbstractBlock.Settings.copy(darkQuartz));
	public static final Block sunnyQuartzPillar = new PillarBlock(AbstractBlock.Settings.copy(darkQuartz));
	public static final Block sunnyQuartzSlab = new SlabBlock(AbstractBlock.Settings.copy(darkQuartz));
	public static final Block sunnyQuartzStairs = new BlockModStairs(darkQuartz.getDefaultState(), AbstractBlock.Settings.copy(darkQuartz));

	public static final Block whitePavement = new BlockMod(AbstractBlock.Settings.copy(livingrock));
	public static final Block whitePavementStair = new BlockModStairs(whitePavement.getDefaultState(), AbstractBlock.Settings.copy(whitePavement));
	public static final Block whitePavementSlab = new SlabBlock(AbstractBlock.Settings.copy(whitePavement));

	public static final Block blackPavement = new BlockMod(AbstractBlock.Settings.copy(whitePavement));
	public static final Block blackPavementStair = new BlockModStairs(whitePavement.getDefaultState(), AbstractBlock.Settings.copy(whitePavement));
	public static final Block blackPavementSlab = new SlabBlock(AbstractBlock.Settings.copy(whitePavement));

	public static final Block bluePavement = new BlockMod(AbstractBlock.Settings.copy(whitePavement));
	public static final Block bluePavementStair = new BlockModStairs(whitePavement.getDefaultState(), AbstractBlock.Settings.copy(whitePavement));
	public static final Block bluePavementSlab = new SlabBlock(AbstractBlock.Settings.copy(whitePavement));

	public static final Block yellowPavement = new BlockMod(AbstractBlock.Settings.copy(whitePavement));
	public static final Block yellowPavementStair = new BlockModStairs(whitePavement.getDefaultState(), AbstractBlock.Settings.copy(whitePavement));
	public static final Block yellowPavementSlab = new SlabBlock(AbstractBlock.Settings.copy(whitePavement));

	public static final Block redPavement = new BlockMod(AbstractBlock.Settings.copy(whitePavement));
	public static final Block redPavementStair = new BlockModStairs(whitePavement.getDefaultState(), AbstractBlock.Settings.copy(whitePavement));
	public static final Block redPavementSlab = new SlabBlock(AbstractBlock.Settings.copy(whitePavement));

	public static final Block greenPavement = new BlockMod(AbstractBlock.Settings.copy(whitePavement));
	public static final Block greenPavementStair = new BlockModStairs(whitePavement.getDefaultState(), AbstractBlock.Settings.copy(whitePavement));
	public static final Block greenPavementSlab = new SlabBlock(AbstractBlock.Settings.copy(whitePavement));

	public static final Block biomeStoneForest = new BlockMod(AbstractBlock.Settings.of(Material.STONE).strength(1.5F, 10).sounds(BlockSoundGroup.STONE));
	public static final Block biomeStoneForestSlab = new SlabBlock(AbstractBlock.Settings.copy(biomeStoneForest));
	public static final Block biomeStoneForestStairs = new BlockModStairs(biomeStoneForest.getDefaultState(), AbstractBlock.Settings.copy(biomeStoneForest));
	public static final Block biomeCobblestoneForest = new BlockMod(AbstractBlock.Settings.copy(biomeStoneForest));
	public static final Block biomeCobblestoneForestSlab = new SlabBlock(AbstractBlock.Settings.copy(biomeStoneForest));
	public static final Block biomeCobblestoneForestStairs = new BlockModStairs(biomeStoneForest.getDefaultState(), AbstractBlock.Settings.copy(biomeStoneForest));
	public static final Block biomeCobblestoneForestWall = new WallBlock(AbstractBlock.Settings.copy(biomeStoneForest));
	public static final Block biomeBrickForest = new BlockMod(AbstractBlock.Settings.copy(biomeStoneForest));
	public static final Block biomeBrickForestSlab = new SlabBlock(AbstractBlock.Settings.copy(biomeStoneForest));
	public static final Block biomeBrickForestStairs = new BlockModStairs(biomeStoneForest.getDefaultState(), AbstractBlock.Settings.copy(biomeStoneForest));
	public static final Block biomeBrickForestWall = new WallBlock(AbstractBlock.Settings.copy(biomeStoneForest));
	public static final Block biomeChiseledBrickForest = new BlockMod(AbstractBlock.Settings.copy(biomeStoneForest));

	public static final Block biomeStonePlains = new BlockMod(AbstractBlock.Settings.copy(biomeStoneForest));
	public static final Block biomeStonePlainsSlab = new SlabBlock(AbstractBlock.Settings.copy(biomeStonePlains));
	public static final Block biomeStonePlainsStairs = new BlockModStairs(biomeStonePlains.getDefaultState(), AbstractBlock.Settings.copy(biomeStonePlains));
	public static final Block biomeCobblestonePlains = new BlockMod(AbstractBlock.Settings.copy(biomeStonePlains));
	public static final Block biomeCobblestonePlainsSlab = new SlabBlock(AbstractBlock.Settings.copy(biomeStonePlains));
	public static final Block biomeCobblestonePlainsStairs = new BlockModStairs(biomeStonePlains.getDefaultState(), AbstractBlock.Settings.copy(biomeStonePlains));
	public static final Block biomeCobblestonePlainsWall = new WallBlock(AbstractBlock.Settings.copy(biomeStonePlains));
	public static final Block biomeBrickPlains = new BlockMod(AbstractBlock.Settings.copy(biomeStonePlains));
	public static final Block biomeBrickPlainsSlab = new SlabBlock(AbstractBlock.Settings.copy(biomeStonePlains));
	public static final Block biomeBrickPlainsStairs = new BlockModStairs(biomeStonePlains.getDefaultState(), AbstractBlock.Settings.copy(biomeStonePlains));
	public static final Block biomeBrickPlainsWall = new WallBlock(AbstractBlock.Settings.copy(biomeStonePlains));
	public static final Block biomeChiseledBrickPlains = new BlockMod(AbstractBlock.Settings.copy(biomeStonePlains));

	public static final Block biomeStoneMountain = new BlockMod(AbstractBlock.Settings.copy(biomeStoneForest));
	public static final Block biomeStoneMountainSlab = new SlabBlock(AbstractBlock.Settings.copy(biomeStoneMountain));
	public static final Block biomeStoneMountainStairs = new BlockModStairs(biomeStoneMountain.getDefaultState(), AbstractBlock.Settings.copy(biomeStoneMountain));
	public static final Block biomeCobblestoneMountain = new BlockMod(AbstractBlock.Settings.copy(biomeStoneMountain));
	public static final Block biomeCobblestoneMountainSlab = new SlabBlock(AbstractBlock.Settings.copy(biomeStoneMountain));
	public static final Block biomeCobblestoneMountainStairs = new BlockModStairs(biomeStoneMountain.getDefaultState(), AbstractBlock.Settings.copy(biomeStoneMountain));
	public static final Block biomeCobblestoneMountainWall = new WallBlock(AbstractBlock.Settings.copy(biomeStoneMountain));
	public static final Block biomeBrickMountain = new BlockMod(AbstractBlock.Settings.copy(biomeStoneMountain));
	public static final Block biomeBrickMountainSlab = new SlabBlock(AbstractBlock.Settings.copy(biomeStoneMountain));
	public static final Block biomeBrickMountainStairs = new BlockModStairs(biomeStoneMountain.getDefaultState(), AbstractBlock.Settings.copy(biomeStoneMountain));
	public static final Block biomeBrickMountainWall = new WallBlock(AbstractBlock.Settings.copy(biomeStoneMountain));
	public static final Block biomeChiseledBrickMountain = new BlockMod(AbstractBlock.Settings.copy(biomeStoneMountain));

	public static final Block biomeStoneFungal = new BlockMod(AbstractBlock.Settings.copy(biomeStoneForest));
	public static final Block biomeStoneFungalSlab = new SlabBlock(AbstractBlock.Settings.copy(biomeStoneFungal));
	public static final Block biomeStoneFungalStairs = new BlockModStairs(biomeStoneFungal.getDefaultState(), AbstractBlock.Settings.copy(biomeStoneFungal));
	public static final Block biomeCobblestoneFungal = new BlockMod(AbstractBlock.Settings.copy(biomeStoneFungal));
	public static final Block biomeCobblestoneFungalSlab = new SlabBlock(AbstractBlock.Settings.copy(biomeStoneFungal));
	public static final Block biomeCobblestoneFungalStairs = new BlockModStairs(biomeStoneFungal.getDefaultState(), AbstractBlock.Settings.copy(biomeStoneFungal));
	public static final Block biomeCobblestoneFungalWall = new WallBlock(AbstractBlock.Settings.copy(biomeStoneFungal));
	public static final Block biomeBrickFungal = new BlockMod(AbstractBlock.Settings.copy(biomeStoneFungal));
	public static final Block biomeBrickFungalSlab = new SlabBlock(AbstractBlock.Settings.copy(biomeStoneFungal));
	public static final Block biomeBrickFungalStairs = new BlockModStairs(biomeStoneFungal.getDefaultState(), AbstractBlock.Settings.copy(biomeStoneFungal));
	public static final Block biomeBrickFungalWall = new WallBlock(AbstractBlock.Settings.copy(biomeStoneFungal));
	public static final Block biomeChiseledBrickFungal = new BlockMod(AbstractBlock.Settings.copy(biomeStoneFungal));

	public static final Block biomeStoneSwamp = new BlockMod(AbstractBlock.Settings.copy(biomeStoneForest));
	public static final Block biomeStoneSwampSlab = new SlabBlock(AbstractBlock.Settings.copy(biomeStoneSwamp));
	public static final Block biomeStoneSwampStairs = new BlockModStairs(biomeStoneSwamp.getDefaultState(), AbstractBlock.Settings.copy(biomeStoneSwamp));
	public static final Block biomeCobblestoneSwamp = new BlockMod(AbstractBlock.Settings.copy(biomeStoneSwamp));
	public static final Block biomeCobblestoneSwampSlab = new SlabBlock(AbstractBlock.Settings.copy(biomeStoneSwamp));
	public static final Block biomeCobblestoneSwampStairs = new BlockModStairs(biomeStoneSwamp.getDefaultState(), AbstractBlock.Settings.copy(biomeStoneSwamp));
	public static final Block biomeCobblestoneSwampWall = new WallBlock(AbstractBlock.Settings.copy(biomeStoneSwamp));
	public static final Block biomeBrickSwamp = new BlockMod(AbstractBlock.Settings.copy(biomeStoneSwamp));
	public static final Block biomeBrickSwampSlab = new SlabBlock(AbstractBlock.Settings.copy(biomeStoneSwamp));
	public static final Block biomeBrickSwampStairs = new BlockModStairs(biomeStoneSwamp.getDefaultState(), AbstractBlock.Settings.copy(biomeStoneSwamp));
	public static final Block biomeBrickSwampWall = new WallBlock(AbstractBlock.Settings.copy(biomeStoneSwamp));
	public static final Block biomeChiseledBrickSwamp = new BlockMod(AbstractBlock.Settings.copy(biomeStoneSwamp));

	public static final Block biomeStoneDesert = new BlockMod(AbstractBlock.Settings.copy(biomeStoneForest));
	public static final Block biomeStoneDesertSlab = new SlabBlock(AbstractBlock.Settings.copy(biomeStoneDesert));
	public static final Block biomeStoneDesertStairs = new BlockModStairs(biomeStoneDesert.getDefaultState(), AbstractBlock.Settings.copy(biomeStoneDesert));
	public static final Block biomeCobblestoneDesert = new BlockMod(AbstractBlock.Settings.copy(biomeStoneDesert));
	public static final Block biomeCobblestoneDesertSlab = new SlabBlock(AbstractBlock.Settings.copy(biomeStoneDesert));
	public static final Block biomeCobblestoneDesertStairs = new BlockModStairs(biomeStoneDesert.getDefaultState(), AbstractBlock.Settings.copy(biomeStoneDesert));
	public static final Block biomeCobblestoneDesertWall = new WallBlock(AbstractBlock.Settings.copy(biomeStoneDesert));
	public static final Block biomeBrickDesert = new BlockMod(AbstractBlock.Settings.copy(biomeStoneDesert));
	public static final Block biomeBrickDesertSlab = new SlabBlock(AbstractBlock.Settings.copy(biomeStoneDesert));
	public static final Block biomeBrickDesertStairs = new BlockModStairs(biomeStoneDesert.getDefaultState(), AbstractBlock.Settings.copy(biomeStoneDesert));
	public static final Block biomeBrickDesertWall = new WallBlock(AbstractBlock.Settings.copy(biomeStoneDesert));
	public static final Block biomeChiseledBrickDesert = new BlockMod(AbstractBlock.Settings.copy(biomeStoneDesert));

	public static final Block biomeStoneTaiga = new BlockMod(AbstractBlock.Settings.copy(biomeStoneForest));
	public static final Block biomeStoneTaigaSlab = new SlabBlock(AbstractBlock.Settings.copy(biomeStoneTaiga));
	public static final Block biomeStoneTaigaStairs = new BlockModStairs(biomeStoneTaiga.getDefaultState(), AbstractBlock.Settings.copy(biomeStoneTaiga));
	public static final Block biomeCobblestoneTaiga = new BlockMod(AbstractBlock.Settings.copy(biomeStoneTaiga));
	public static final Block biomeCobblestoneTaigaSlab = new SlabBlock(AbstractBlock.Settings.copy(biomeStoneTaiga));
	public static final Block biomeCobblestoneTaigaStairs = new BlockModStairs(biomeStoneTaiga.getDefaultState(), AbstractBlock.Settings.copy(biomeStoneTaiga));
	public static final Block biomeCobblestoneTaigaWall = new WallBlock(AbstractBlock.Settings.copy(biomeStoneTaiga));
	public static final Block biomeBrickTaiga = new BlockMod(AbstractBlock.Settings.copy(biomeStoneTaiga));
	public static final Block biomeBrickTaigaSlab = new SlabBlock(AbstractBlock.Settings.copy(biomeStoneTaiga));
	public static final Block biomeBrickTaigaStairs = new BlockModStairs(biomeStoneTaiga.getDefaultState(), AbstractBlock.Settings.copy(biomeStoneTaiga));
	public static final Block biomeBrickTaigaWall = new WallBlock(AbstractBlock.Settings.copy(biomeStoneTaiga));
	public static final Block biomeChiseledBrickTaiga = new BlockMod(AbstractBlock.Settings.copy(biomeStoneTaiga));

	public static final Block biomeStoneMesa = new BlockMod(AbstractBlock.Settings.copy(biomeStoneForest));
	public static final Block biomeStoneMesaSlab = new SlabBlock(AbstractBlock.Settings.copy(biomeStoneMesa));
	public static final Block biomeStoneMesaStairs = new BlockModStairs(biomeStoneMesa.getDefaultState(), AbstractBlock.Settings.copy(biomeStoneMesa));
	public static final Block biomeCobblestoneMesa = new BlockMod(AbstractBlock.Settings.copy(biomeStoneMesa));
	public static final Block biomeCobblestoneMesaSlab = new SlabBlock(AbstractBlock.Settings.copy(biomeStoneMesa));
	public static final Block biomeCobblestoneMesaStairs = new BlockModStairs(biomeStoneMesa.getDefaultState(), AbstractBlock.Settings.copy(biomeStoneMesa));
	public static final Block biomeCobblestoneMesaWall = new WallBlock(AbstractBlock.Settings.copy(biomeStoneMesa));
	public static final Block biomeBrickMesa = new BlockMod(AbstractBlock.Settings.copy(biomeStoneMesa));
	public static final Block biomeBrickMesaSlab = new SlabBlock(AbstractBlock.Settings.copy(biomeStoneMesa));
	public static final Block biomeBrickMesaStairs = new BlockModStairs(biomeStoneMesa.getDefaultState(), AbstractBlock.Settings.copy(biomeStoneMesa));
	public static final Block biomeBrickMesaWall = new WallBlock(AbstractBlock.Settings.copy(biomeStoneMesa));
	public static final Block biomeChiseledBrickMesa = new BlockMod(AbstractBlock.Settings.copy(biomeStoneMesa));

	public static final Block shimmerrockSlab = new SlabBlock(AbstractBlock.Settings.copy(shimmerrock));
	public static final Block shimmerrockStairs = new BlockModStairs(shimmerrock.getDefaultState(), AbstractBlock.Settings.copy(shimmerrock));

	public static final Block shimmerwoodPlankSlab = new SlabBlock(AbstractBlock.Settings.copy(shimmerwoodPlanks));
	public static final Block shimmerwoodPlankStairs = new BlockModStairs(shimmerwoodPlanks.getDefaultState(), AbstractBlock.Settings.copy(shimmerwoodPlanks));

	public static final Block managlassPane = new BlockModPane(AbstractBlock.Settings.copy(manaGlass));
	public static final Block alfglassPane = new BlockModPane(AbstractBlock.Settings.copy(elfGlass));
	public static final Block bifrostPane = new BlockModPane(AbstractBlock.Settings.copy(bifrostPerm));

	public static void registerBlocks() {
		Registry<Block> r = Registry.BLOCK;

		register(r, LibBlockNames.LIVING_WOOD + STAIR_SUFFIX, livingwoodStairs);
		register(r, LibBlockNames.LIVING_WOOD + SLAB_SUFFIX, livingwoodSlab);
		register(r, LibBlockNames.LIVING_WOOD + WALL_SUFFIX, livingwoodWall);
		register(r, LibBlockNames.LIVING_WOOD + FENCE_SUFFIX, livingwoodFence);
		register(r, LibBlockNames.LIVING_WOOD + FENCE_GATE_SUFFIX, livingwoodFenceGate);

		register(r, LibBlockNames.LIVING_WOOD_PLANKS + STAIR_SUFFIX, livingwoodPlankStairs);
		register(r, LibBlockNames.LIVING_WOOD_PLANKS + SLAB_SUFFIX, livingwoodPlankSlab);

		register(r, LibBlockNames.LIVING_ROCK + STAIR_SUFFIX, livingrockStairs);
		register(r, LibBlockNames.LIVING_ROCK + SLAB_SUFFIX, livingrockSlab);
		register(r, LibBlockNames.LIVING_ROCK + WALL_SUFFIX, livingrockWall);

		register(r, LibBlockNames.LIVING_ROCK_BRICK + STAIR_SUFFIX, livingrockBrickStairs);
		register(r, LibBlockNames.LIVING_ROCK_BRICK + SLAB_SUFFIX, livingrockBrickSlab);
		register(r, LibBlockNames.LIVING_ROCK_BRICK + WALL_SUFFIX, livingrockBrickWall);

		register(r, LibBlockNames.LIVING_ROCK_BRICK_MOSSY + STAIR_SUFFIX, livingrockBrickMossyStairs);
		register(r, LibBlockNames.LIVING_ROCK_BRICK_MOSSY + SLAB_SUFFIX, livingrockBrickMossySlab);
		register(r, LibBlockNames.LIVING_ROCK_BRICK_MOSSY + WALL_SUFFIX, livingrockBrickMossyWall);

		register(r, LibBlockNames.DREAM_WOOD + STAIR_SUFFIX, dreamwoodStairs);
		register(r, LibBlockNames.DREAM_WOOD + SLAB_SUFFIX, dreamwoodSlab);
		register(r, LibBlockNames.DREAM_WOOD + WALL_SUFFIX, dreamwoodWall);
		register(r, LibBlockNames.DREAM_WOOD + FENCE_SUFFIX, dreamwoodFence);
		register(r, LibBlockNames.DREAM_WOOD + FENCE_GATE_SUFFIX, dreamwoodFenceGate);

		register(r, LibBlockNames.DREAM_WOOD_PLANKS + STAIR_SUFFIX, dreamwoodPlankStairs);
		register(r, LibBlockNames.DREAM_WOOD_PLANKS + SLAB_SUFFIX, dreamwoodPlankSlab);

		register(r, QUARTZ_DARK, darkQuartz);
		register(r, "chiseled_" + QUARTZ_DARK, darkQuartzChiseled);
		register(r, QUARTZ_DARK + "_pillar", darkQuartzPillar);
		register(r, QUARTZ_DARK + SLAB_SUFFIX, darkQuartzSlab);
		register(r, QUARTZ_DARK + STAIR_SUFFIX, darkQuartzStairs);

		register(r, QUARTZ_MANA, manaQuartz);
		register(r, "chiseled_" + QUARTZ_MANA, manaQuartzChiseled);
		register(r, QUARTZ_MANA + "_pillar", manaQuartzPillar);
		register(r, QUARTZ_MANA + SLAB_SUFFIX, manaQuartzSlab);
		register(r, QUARTZ_MANA + STAIR_SUFFIX, manaQuartzStairs);

		register(r, QUARTZ_BLAZE, blazeQuartz);
		register(r, "chiseled_" + QUARTZ_BLAZE, blazeQuartzChiseled);
		register(r, QUARTZ_BLAZE + "_pillar", blazeQuartzPillar);
		register(r, QUARTZ_BLAZE + SLAB_SUFFIX, blazeQuartzSlab);
		register(r, QUARTZ_BLAZE + STAIR_SUFFIX, blazeQuartzStairs);

		register(r, QUARTZ_LAVENDER, lavenderQuartz);
		register(r, "chiseled_" + QUARTZ_LAVENDER, lavenderQuartzChiseled);
		register(r, QUARTZ_LAVENDER + "_pillar", lavenderQuartzPillar);
		register(r, QUARTZ_LAVENDER + SLAB_SUFFIX, lavenderQuartzSlab);
		register(r, QUARTZ_LAVENDER + STAIR_SUFFIX, lavenderQuartzStairs);

		register(r, QUARTZ_RED, redQuartz);
		register(r, "chiseled_" + QUARTZ_RED, redQuartzChiseled);
		register(r, QUARTZ_RED + "_pillar", redQuartzPillar);
		register(r, QUARTZ_RED + SLAB_SUFFIX, redQuartzSlab);
		register(r, QUARTZ_RED + STAIR_SUFFIX, redQuartzStairs);

		register(r, QUARTZ_ELF, elfQuartz);
		register(r, "chiseled_" + QUARTZ_ELF, elfQuartzChiseled);
		register(r, QUARTZ_ELF + "_pillar", elfQuartzPillar);
		register(r, QUARTZ_ELF + SLAB_SUFFIX, elfQuartzSlab);
		register(r, QUARTZ_ELF + STAIR_SUFFIX, elfQuartzStairs);

		register(r, QUARTZ_SUNNY, sunnyQuartz);
		register(r, "chiseled_" + QUARTZ_SUNNY, sunnyQuartzChiseled);
		register(r, QUARTZ_SUNNY + "_pillar", sunnyQuartzPillar);
		register(r, QUARTZ_SUNNY + SLAB_SUFFIX, sunnyQuartzSlab);
		register(r, QUARTZ_SUNNY + STAIR_SUFFIX, sunnyQuartzStairs);

		register(r, "white" + PAVEMENT_SUFFIX, whitePavement);
		register(r, "white" + PAVEMENT_SUFFIX + STAIR_SUFFIX, whitePavementStair);
		register(r, "white" + PAVEMENT_SUFFIX + SLAB_SUFFIX, whitePavementSlab);

		register(r, "black" + PAVEMENT_SUFFIX, blackPavement);
		register(r, "black" + PAVEMENT_SUFFIX + STAIR_SUFFIX, blackPavementStair);
		register(r, "black" + PAVEMENT_SUFFIX + SLAB_SUFFIX, blackPavementSlab);

		register(r, "blue" + PAVEMENT_SUFFIX, bluePavement);
		register(r, "blue" + PAVEMENT_SUFFIX + STAIR_SUFFIX, bluePavementStair);
		register(r, "blue" + PAVEMENT_SUFFIX + SLAB_SUFFIX, bluePavementSlab);

		register(r, "yellow" + PAVEMENT_SUFFIX, yellowPavement);
		register(r, "yellow" + PAVEMENT_SUFFIX + STAIR_SUFFIX, yellowPavementStair);
		register(r, "yellow" + PAVEMENT_SUFFIX + SLAB_SUFFIX, yellowPavementSlab);

		register(r, "red" + PAVEMENT_SUFFIX, redPavement);
		register(r, "red" + PAVEMENT_SUFFIX + STAIR_SUFFIX, redPavementStair);
		register(r, "red" + PAVEMENT_SUFFIX + SLAB_SUFFIX, redPavementSlab);

		register(r, "green" + PAVEMENT_SUFFIX, greenPavement);
		register(r, "green" + PAVEMENT_SUFFIX + STAIR_SUFFIX, greenPavementStair);
		register(r, "green" + PAVEMENT_SUFFIX + SLAB_SUFFIX, greenPavementSlab);

		register(r, METAMORPHIC_PREFIX + "forest_stone", biomeStoneForest);
		register(r, METAMORPHIC_PREFIX + "forest_stone" + SLAB_SUFFIX, biomeStoneForestSlab);
		register(r, METAMORPHIC_PREFIX + "forest_stone" + STAIR_SUFFIX, biomeStoneForestStairs);
		register(r, METAMORPHIC_PREFIX + "forest_cobblestone", biomeCobblestoneForest);
		register(r, METAMORPHIC_PREFIX + "forest_cobblestone" + SLAB_SUFFIX, biomeCobblestoneForestSlab);
		register(r, METAMORPHIC_PREFIX + "forest_cobblestone" + STAIR_SUFFIX, biomeCobblestoneForestStairs);
		register(r, METAMORPHIC_PREFIX + "forest_cobblestone" + WALL_SUFFIX, biomeCobblestoneForestWall);
		register(r, METAMORPHIC_PREFIX + "forest_bricks", biomeBrickForest);
		register(r, METAMORPHIC_PREFIX + "forest_bricks" + SLAB_SUFFIX, biomeBrickForestSlab);
		register(r, METAMORPHIC_PREFIX + "forest_bricks" + STAIR_SUFFIX, biomeBrickForestStairs);
		register(r, METAMORPHIC_PREFIX + "forest_bricks" + WALL_SUFFIX, biomeBrickForestWall);
		register(r, "chiseled_" + METAMORPHIC_PREFIX + "forest_bricks", biomeChiseledBrickForest);

		register(r, METAMORPHIC_PREFIX + "plains_stone", biomeStonePlains);
		register(r, METAMORPHIC_PREFIX + "plains_stone" + SLAB_SUFFIX, biomeStonePlainsSlab);
		register(r, METAMORPHIC_PREFIX + "plains_stone" + STAIR_SUFFIX, biomeStonePlainsStairs);
		register(r, METAMORPHIC_PREFIX + "plains_cobblestone", biomeCobblestonePlains);
		register(r, METAMORPHIC_PREFIX + "plains_cobblestone" + SLAB_SUFFIX, biomeCobblestonePlainsSlab);
		register(r, METAMORPHIC_PREFIX + "plains_cobblestone" + STAIR_SUFFIX, biomeCobblestonePlainsStairs);
		register(r, METAMORPHIC_PREFIX + "plains_cobblestone" + WALL_SUFFIX, biomeCobblestonePlainsWall);
		register(r, METAMORPHIC_PREFIX + "plains_bricks", biomeBrickPlains);
		register(r, METAMORPHIC_PREFIX + "plains_bricks" + SLAB_SUFFIX, biomeBrickPlainsSlab);
		register(r, METAMORPHIC_PREFIX + "plains_bricks" + STAIR_SUFFIX, biomeBrickPlainsStairs);
		register(r, METAMORPHIC_PREFIX + "plains_bricks" + WALL_SUFFIX, biomeBrickPlainsWall);
		register(r, "chiseled_" + METAMORPHIC_PREFIX + "plains_bricks", biomeChiseledBrickPlains);

		register(r, METAMORPHIC_PREFIX + "mountain_stone", biomeStoneMountain);
		register(r, METAMORPHIC_PREFIX + "mountain_stone" + SLAB_SUFFIX, biomeStoneMountainSlab);
		register(r, METAMORPHIC_PREFIX + "mountain_stone" + STAIR_SUFFIX, biomeStoneMountainStairs);
		register(r, METAMORPHIC_PREFIX + "mountain_cobblestone", biomeCobblestoneMountain);
		register(r, METAMORPHIC_PREFIX + "mountain_cobblestone" + SLAB_SUFFIX, biomeCobblestoneMountainSlab);
		register(r, METAMORPHIC_PREFIX + "mountain_cobblestone" + STAIR_SUFFIX, biomeCobblestoneMountainStairs);
		register(r, METAMORPHIC_PREFIX + "mountain_cobblestone" + WALL_SUFFIX, biomeCobblestoneMountainWall);
		register(r, METAMORPHIC_PREFIX + "mountain_bricks", biomeBrickMountain);
		register(r, METAMORPHIC_PREFIX + "mountain_bricks" + SLAB_SUFFIX, biomeBrickMountainSlab);
		register(r, METAMORPHIC_PREFIX + "mountain_bricks" + STAIR_SUFFIX, biomeBrickMountainStairs);
		register(r, METAMORPHIC_PREFIX + "mountain_bricks" + WALL_SUFFIX, biomeBrickMountainWall);
		register(r, "chiseled_" + METAMORPHIC_PREFIX + "mountain_bricks", biomeChiseledBrickMountain);

		register(r, METAMORPHIC_PREFIX + "fungal_stone", biomeStoneFungal);
		register(r, METAMORPHIC_PREFIX + "fungal_stone" + SLAB_SUFFIX, biomeStoneFungalSlab);
		register(r, METAMORPHIC_PREFIX + "fungal_stone" + STAIR_SUFFIX, biomeStoneFungalStairs);
		register(r, METAMORPHIC_PREFIX + "fungal_cobblestone", biomeCobblestoneFungal);
		register(r, METAMORPHIC_PREFIX + "fungal_cobblestone" + SLAB_SUFFIX, biomeCobblestoneFungalSlab);
		register(r, METAMORPHIC_PREFIX + "fungal_cobblestone" + STAIR_SUFFIX, biomeCobblestoneFungalStairs);
		register(r, METAMORPHIC_PREFIX + "fungal_cobblestone" + WALL_SUFFIX, biomeCobblestoneFungalWall);
		register(r, METAMORPHIC_PREFIX + "fungal_bricks", biomeBrickFungal);
		register(r, METAMORPHIC_PREFIX + "fungal_bricks" + SLAB_SUFFIX, biomeBrickFungalSlab);
		register(r, METAMORPHIC_PREFIX + "fungal_bricks" + STAIR_SUFFIX, biomeBrickFungalStairs);
		register(r, METAMORPHIC_PREFIX + "fungal_bricks" + WALL_SUFFIX, biomeBrickFungalWall);
		register(r, "chiseled_" + METAMORPHIC_PREFIX + "fungal_bricks", biomeChiseledBrickFungal);

		register(r, METAMORPHIC_PREFIX + "swamp_stone", biomeStoneSwamp);
		register(r, METAMORPHIC_PREFIX + "swamp_stone" + SLAB_SUFFIX, biomeStoneSwampSlab);
		register(r, METAMORPHIC_PREFIX + "swamp_stone" + STAIR_SUFFIX, biomeStoneSwampStairs);
		register(r, METAMORPHIC_PREFIX + "swamp_cobblestone", biomeCobblestoneSwamp);
		register(r, METAMORPHIC_PREFIX + "swamp_cobblestone" + SLAB_SUFFIX, biomeCobblestoneSwampSlab);
		register(r, METAMORPHIC_PREFIX + "swamp_cobblestone" + STAIR_SUFFIX, biomeCobblestoneSwampStairs);
		register(r, METAMORPHIC_PREFIX + "swamp_cobblestone" + WALL_SUFFIX, biomeCobblestoneSwampWall);
		register(r, METAMORPHIC_PREFIX + "swamp_bricks", biomeBrickSwamp);
		register(r, METAMORPHIC_PREFIX + "swamp_bricks" + SLAB_SUFFIX, biomeBrickSwampSlab);
		register(r, METAMORPHIC_PREFIX + "swamp_bricks" + STAIR_SUFFIX, biomeBrickSwampStairs);
		register(r, METAMORPHIC_PREFIX + "swamp_bricks" + WALL_SUFFIX, biomeBrickSwampWall);
		register(r, "chiseled_" + METAMORPHIC_PREFIX + "swamp_bricks", biomeChiseledBrickSwamp);

		register(r, METAMORPHIC_PREFIX + "desert_stone", biomeStoneDesert);
		register(r, METAMORPHIC_PREFIX + "desert_stone" + SLAB_SUFFIX, biomeStoneDesertSlab);
		register(r, METAMORPHIC_PREFIX + "desert_stone" + STAIR_SUFFIX, biomeStoneDesertStairs);
		register(r, METAMORPHIC_PREFIX + "desert_cobblestone", biomeCobblestoneDesert);
		register(r, METAMORPHIC_PREFIX + "desert_cobblestone" + SLAB_SUFFIX, biomeCobblestoneDesertSlab);
		register(r, METAMORPHIC_PREFIX + "desert_cobblestone" + STAIR_SUFFIX, biomeCobblestoneDesertStairs);
		register(r, METAMORPHIC_PREFIX + "desert_cobblestone" + WALL_SUFFIX, biomeCobblestoneDesertWall);
		register(r, METAMORPHIC_PREFIX + "desert_bricks", biomeBrickDesert);
		register(r, METAMORPHIC_PREFIX + "desert_bricks" + SLAB_SUFFIX, biomeBrickDesertSlab);
		register(r, METAMORPHIC_PREFIX + "desert_bricks" + STAIR_SUFFIX, biomeBrickDesertStairs);
		register(r, METAMORPHIC_PREFIX + "desert_bricks" + WALL_SUFFIX, biomeBrickDesertWall);
		register(r, "chiseled_" + METAMORPHIC_PREFIX + "desert_bricks", biomeChiseledBrickDesert);

		register(r, METAMORPHIC_PREFIX + "taiga_stone", biomeStoneTaiga);
		register(r, METAMORPHIC_PREFIX + "taiga_stone" + SLAB_SUFFIX, biomeStoneTaigaSlab);
		register(r, METAMORPHIC_PREFIX + "taiga_stone" + STAIR_SUFFIX, biomeStoneTaigaStairs);
		register(r, METAMORPHIC_PREFIX + "taiga_cobblestone", biomeCobblestoneTaiga);
		register(r, METAMORPHIC_PREFIX + "taiga_cobblestone" + SLAB_SUFFIX, biomeCobblestoneTaigaSlab);
		register(r, METAMORPHIC_PREFIX + "taiga_cobblestone" + STAIR_SUFFIX, biomeCobblestoneTaigaStairs);
		register(r, METAMORPHIC_PREFIX + "taiga_cobblestone" + WALL_SUFFIX, biomeCobblestoneTaigaWall);
		register(r, METAMORPHIC_PREFIX + "taiga_bricks", biomeBrickTaiga);
		register(r, METAMORPHIC_PREFIX + "taiga_bricks" + SLAB_SUFFIX, biomeBrickTaigaSlab);
		register(r, METAMORPHIC_PREFIX + "taiga_bricks" + STAIR_SUFFIX, biomeBrickTaigaStairs);
		register(r, METAMORPHIC_PREFIX + "taiga_bricks" + WALL_SUFFIX, biomeBrickTaigaWall);
		register(r, "chiseled_" + METAMORPHIC_PREFIX + "taiga_bricks", biomeChiseledBrickTaiga);

		register(r, METAMORPHIC_PREFIX + "mesa_stone", biomeStoneMesa);
		register(r, METAMORPHIC_PREFIX + "mesa_stone" + SLAB_SUFFIX, biomeStoneMesaSlab);
		register(r, METAMORPHIC_PREFIX + "mesa_stone" + STAIR_SUFFIX, biomeStoneMesaStairs);
		register(r, METAMORPHIC_PREFIX + "mesa_cobblestone", biomeCobblestoneMesa);
		register(r, METAMORPHIC_PREFIX + "mesa_cobblestone" + SLAB_SUFFIX, biomeCobblestoneMesaSlab);
		register(r, METAMORPHIC_PREFIX + "mesa_cobblestone" + STAIR_SUFFIX, biomeCobblestoneMesaStairs);
		register(r, METAMORPHIC_PREFIX + "mesa_cobblestone" + WALL_SUFFIX, biomeCobblestoneMesaWall);
		register(r, METAMORPHIC_PREFIX + "mesa_bricks", biomeBrickMesa);
		register(r, METAMORPHIC_PREFIX + "mesa_bricks" + SLAB_SUFFIX, biomeBrickMesaSlab);
		register(r, METAMORPHIC_PREFIX + "mesa_bricks" + STAIR_SUFFIX, biomeBrickMesaStairs);
		register(r, METAMORPHIC_PREFIX + "mesa_bricks" + WALL_SUFFIX, biomeBrickMesaWall);
		register(r, "chiseled_" + METAMORPHIC_PREFIX + "mesa_bricks", biomeChiseledBrickMesa);

		register(r, LibBlockNames.SHIMMERROCK + SLAB_SUFFIX, shimmerrockSlab);
		register(r, LibBlockNames.SHIMMERROCK + STAIR_SUFFIX, shimmerrockStairs);

		register(r, LibBlockNames.SHIMMERWOOD_PLANKS + SLAB_SUFFIX, shimmerwoodPlankSlab);
		register(r, LibBlockNames.SHIMMERWOOD_PLANKS + STAIR_SUFFIX, shimmerwoodPlankStairs);

		register(r, LibBlockNames.MANA_GLASS + "_pane", managlassPane);
		register(r, LibBlockNames.ELF_GLASS + "_pane", alfglassPane);
		register(r, LibBlockNames.BIFROST + "_pane", bifrostPane);
	}

	public static void registerItemBlocks() {
		Registry<Item> r = Registry.ITEM;
		Item.Settings props = ModItems.defaultBuilder();

		register(r, Registry.BLOCK.getId(livingwoodStairs), new BlockItem(livingwoodStairs, props));
		register(r, Registry.BLOCK.getId(livingwoodSlab), new BlockItem(livingwoodSlab, props));
		register(r, Registry.BLOCK.getId(livingwoodWall), new BlockItem(livingwoodWall, props));
		register(r, Registry.BLOCK.getId(livingwoodFence), new BlockItem(livingwoodFence, props));
		register(r, Registry.BLOCK.getId(livingwoodFenceGate), new BlockItem(livingwoodFenceGate, props));

		register(r, Registry.BLOCK.getId(livingwoodPlankStairs), new BlockItem(livingwoodPlankStairs, props));
		register(r, Registry.BLOCK.getId(livingwoodPlankSlab), new BlockItem(livingwoodPlankSlab, props));

		register(r, Registry.BLOCK.getId(livingrockStairs), new BlockItem(livingrockStairs, props));
		register(r, Registry.BLOCK.getId(livingrockSlab), new BlockItem(livingrockSlab, props));
		register(r, Registry.BLOCK.getId(livingrockWall), new BlockItem(livingrockWall, props));

		register(r, Registry.BLOCK.getId(livingrockBrickStairs), new BlockItem(livingrockBrickStairs, props));
		register(r, Registry.BLOCK.getId(livingrockBrickSlab), new BlockItem(livingrockBrickSlab, props));
		register(r, Registry.BLOCK.getId(livingrockBrickWall), new BlockItem(livingrockBrickWall, props));

		register(r, Registry.BLOCK.getId(livingrockBrickMossyStairs), new BlockItem(livingrockBrickMossyStairs, props));
		register(r, Registry.BLOCK.getId(livingrockBrickMossySlab), new BlockItem(livingrockBrickMossySlab, props));
		register(r, Registry.BLOCK.getId(livingrockBrickMossyWall), new BlockItem(livingrockBrickMossyWall, props));

		register(r, Registry.BLOCK.getId(dreamwoodStairs), new BlockItem(dreamwoodStairs, props));
		register(r, Registry.BLOCK.getId(dreamwoodSlab), new BlockItem(dreamwoodSlab, props));
		register(r, Registry.BLOCK.getId(dreamwoodWall), new BlockItem(dreamwoodWall, props));
		register(r, Registry.BLOCK.getId(dreamwoodFence), new BlockItem(dreamwoodFence, props));
		register(r, Registry.BLOCK.getId(dreamwoodFenceGate), new BlockItem(dreamwoodFenceGate, props));

		register(r, Registry.BLOCK.getId(dreamwoodPlankStairs), new BlockItem(dreamwoodPlankStairs, props));
		register(r, Registry.BLOCK.getId(dreamwoodPlankSlab), new BlockItem(dreamwoodPlankSlab, props));

		register(r, Registry.BLOCK.getId(darkQuartz), new BlockItem(darkQuartz, props));
		register(r, Registry.BLOCK.getId(darkQuartzPillar), new BlockItem(darkQuartzPillar, props));
		register(r, Registry.BLOCK.getId(darkQuartzChiseled), new BlockItem(darkQuartzChiseled, props));
		register(r, Registry.BLOCK.getId(darkQuartzSlab), new BlockItem(darkQuartzSlab, props));
		register(r, Registry.BLOCK.getId(darkQuartzStairs), new BlockItem(darkQuartzStairs, props));

		register(r, Registry.BLOCK.getId(manaQuartz), new BlockItem(manaQuartz, props));
		register(r, Registry.BLOCK.getId(manaQuartzPillar), new BlockItem(manaQuartzPillar, props));
		register(r, Registry.BLOCK.getId(manaQuartzChiseled), new BlockItem(manaQuartzChiseled, props));
		register(r, Registry.BLOCK.getId(manaQuartzSlab), new BlockItem(manaQuartzSlab, props));
		register(r, Registry.BLOCK.getId(manaQuartzStairs), new BlockItem(manaQuartzStairs, props));

		register(r, Registry.BLOCK.getId(blazeQuartz), new BlockItem(blazeQuartz, props));
		register(r, Registry.BLOCK.getId(blazeQuartzPillar), new BlockItem(blazeQuartzPillar, props));
		register(r, Registry.BLOCK.getId(blazeQuartzChiseled), new BlockItem(blazeQuartzChiseled, props));
		register(r, Registry.BLOCK.getId(blazeQuartzSlab), new BlockItem(blazeQuartzSlab, props));
		register(r, Registry.BLOCK.getId(blazeQuartzStairs), new BlockItem(blazeQuartzStairs, props));

		register(r, Registry.BLOCK.getId(lavenderQuartz), new BlockItem(lavenderQuartz, props));
		register(r, Registry.BLOCK.getId(lavenderQuartzPillar), new BlockItem(lavenderQuartzPillar, props));
		register(r, Registry.BLOCK.getId(lavenderQuartzChiseled), new BlockItem(lavenderQuartzChiseled, props));
		register(r, Registry.BLOCK.getId(lavenderQuartzSlab), new BlockItem(lavenderQuartzSlab, props));
		register(r, Registry.BLOCK.getId(lavenderQuartzStairs), new BlockItem(lavenderQuartzStairs, props));

		register(r, Registry.BLOCK.getId(redQuartz), new BlockItem(redQuartz, props));
		register(r, Registry.BLOCK.getId(redQuartzPillar), new BlockItem(redQuartzPillar, props));
		register(r, Registry.BLOCK.getId(redQuartzChiseled), new BlockItem(redQuartzChiseled, props));
		register(r, Registry.BLOCK.getId(redQuartzSlab), new BlockItem(redQuartzSlab, props));
		register(r, Registry.BLOCK.getId(redQuartzStairs), new BlockItem(redQuartzStairs, props));

		register(r, Registry.BLOCK.getId(elfQuartz), new BlockItem(elfQuartz, props));
		register(r, Registry.BLOCK.getId(elfQuartzPillar), new BlockItem(elfQuartzPillar, props));
		register(r, Registry.BLOCK.getId(elfQuartzChiseled), new BlockItem(elfQuartzChiseled, props));
		register(r, Registry.BLOCK.getId(elfQuartzSlab), new BlockItem(elfQuartzSlab, props));
		register(r, Registry.BLOCK.getId(elfQuartzStairs), new BlockItem(elfQuartzStairs, props));

		register(r, Registry.BLOCK.getId(sunnyQuartz), new BlockItem(sunnyQuartz, props));
		register(r, Registry.BLOCK.getId(sunnyQuartzPillar), new BlockItem(sunnyQuartzPillar, props));
		register(r, Registry.BLOCK.getId(sunnyQuartzChiseled), new BlockItem(sunnyQuartzChiseled, props));
		register(r, Registry.BLOCK.getId(sunnyQuartzSlab), new BlockItem(sunnyQuartzSlab, props));
		register(r, Registry.BLOCK.getId(sunnyQuartzStairs), new BlockItem(sunnyQuartzStairs, props));

		register(r, Registry.BLOCK.getId(biomeStoneForest), new BlockItem(biomeStoneForest, props));
		register(r, Registry.BLOCK.getId(biomeStoneForestSlab), new BlockItem(biomeStoneForestSlab, props));
		register(r, Registry.BLOCK.getId(biomeStoneForestStairs), new BlockItem(biomeStoneForestStairs, props));
		register(r, Registry.BLOCK.getId(biomeBrickForest), new BlockItem(biomeBrickForest, props));
		register(r, Registry.BLOCK.getId(biomeBrickForestSlab), new BlockItem(biomeBrickForestSlab, props));
		register(r, Registry.BLOCK.getId(biomeBrickForestStairs), new BlockItem(biomeBrickForestStairs, props));
		register(r, Registry.BLOCK.getId(biomeBrickForestWall), new BlockItem(biomeBrickForestWall, props));
		register(r, Registry.BLOCK.getId(biomeCobblestoneForest), new BlockItem(biomeCobblestoneForest, props));
		register(r, Registry.BLOCK.getId(biomeCobblestoneForestSlab), new BlockItem(biomeCobblestoneForestSlab, props));
		register(r, Registry.BLOCK.getId(biomeCobblestoneForestStairs), new BlockItem(biomeCobblestoneForestStairs, props));
		register(r, Registry.BLOCK.getId(biomeCobblestoneForestWall), new BlockItem(biomeCobblestoneForestWall, props));
		register(r, Registry.BLOCK.getId(biomeChiseledBrickForest), new BlockItem(biomeChiseledBrickForest, props));

		register(r, Registry.BLOCK.getId(biomeStonePlains), new BlockItem(biomeStonePlains, props));
		register(r, Registry.BLOCK.getId(biomeStonePlainsSlab), new BlockItem(biomeStonePlainsSlab, props));
		register(r, Registry.BLOCK.getId(biomeStonePlainsStairs), new BlockItem(biomeStonePlainsStairs, props));
		register(r, Registry.BLOCK.getId(biomeBrickPlains), new BlockItem(biomeBrickPlains, props));
		register(r, Registry.BLOCK.getId(biomeBrickPlainsSlab), new BlockItem(biomeBrickPlainsSlab, props));
		register(r, Registry.BLOCK.getId(biomeBrickPlainsStairs), new BlockItem(biomeBrickPlainsStairs, props));
		register(r, Registry.BLOCK.getId(biomeBrickPlainsWall), new BlockItem(biomeBrickPlainsWall, props));
		register(r, Registry.BLOCK.getId(biomeCobblestonePlains), new BlockItem(biomeCobblestonePlains, props));
		register(r, Registry.BLOCK.getId(biomeCobblestonePlainsSlab), new BlockItem(biomeCobblestonePlainsSlab, props));
		register(r, Registry.BLOCK.getId(biomeCobblestonePlainsStairs), new BlockItem(biomeCobblestonePlainsStairs, props));
		register(r, Registry.BLOCK.getId(biomeCobblestonePlainsWall), new BlockItem(biomeCobblestonePlainsWall, props));
		register(r, Registry.BLOCK.getId(biomeChiseledBrickPlains), new BlockItem(biomeChiseledBrickPlains, props));

		register(r, Registry.BLOCK.getId(biomeStoneMountain), new BlockItem(biomeStoneMountain, props));
		register(r, Registry.BLOCK.getId(biomeStoneMountainSlab), new BlockItem(biomeStoneMountainSlab, props));
		register(r, Registry.BLOCK.getId(biomeStoneMountainStairs), new BlockItem(biomeStoneMountainStairs, props));
		register(r, Registry.BLOCK.getId(biomeBrickMountain), new BlockItem(biomeBrickMountain, props));
		register(r, Registry.BLOCK.getId(biomeBrickMountainSlab), new BlockItem(biomeBrickMountainSlab, props));
		register(r, Registry.BLOCK.getId(biomeBrickMountainStairs), new BlockItem(biomeBrickMountainStairs, props));
		register(r, Registry.BLOCK.getId(biomeBrickMountainWall), new BlockItem(biomeBrickMountainWall, props));
		register(r, Registry.BLOCK.getId(biomeCobblestoneMountain), new BlockItem(biomeCobblestoneMountain, props));
		register(r, Registry.BLOCK.getId(biomeCobblestoneMountainSlab), new BlockItem(biomeCobblestoneMountainSlab, props));
		register(r, Registry.BLOCK.getId(biomeCobblestoneMountainStairs), new BlockItem(biomeCobblestoneMountainStairs, props));
		register(r, Registry.BLOCK.getId(biomeCobblestoneMountainWall), new BlockItem(biomeCobblestoneMountainWall, props));
		register(r, Registry.BLOCK.getId(biomeChiseledBrickMountain), new BlockItem(biomeChiseledBrickMountain, props));

		register(r, Registry.BLOCK.getId(biomeStoneFungal), new BlockItem(biomeStoneFungal, props));
		register(r, Registry.BLOCK.getId(biomeStoneFungalSlab), new BlockItem(biomeStoneFungalSlab, props));
		register(r, Registry.BLOCK.getId(biomeStoneFungalStairs), new BlockItem(biomeStoneFungalStairs, props));
		register(r, Registry.BLOCK.getId(biomeBrickFungal), new BlockItem(biomeBrickFungal, props));
		register(r, Registry.BLOCK.getId(biomeBrickFungalSlab), new BlockItem(biomeBrickFungalSlab, props));
		register(r, Registry.BLOCK.getId(biomeBrickFungalStairs), new BlockItem(biomeBrickFungalStairs, props));
		register(r, Registry.BLOCK.getId(biomeBrickFungalWall), new BlockItem(biomeBrickFungalWall, props));
		register(r, Registry.BLOCK.getId(biomeCobblestoneFungal), new BlockItem(biomeCobblestoneFungal, props));
		register(r, Registry.BLOCK.getId(biomeCobblestoneFungalSlab), new BlockItem(biomeCobblestoneFungalSlab, props));
		register(r, Registry.BLOCK.getId(biomeCobblestoneFungalStairs), new BlockItem(biomeCobblestoneFungalStairs, props));
		register(r, Registry.BLOCK.getId(biomeCobblestoneFungalWall), new BlockItem(biomeCobblestoneFungalWall, props));
		register(r, Registry.BLOCK.getId(biomeChiseledBrickFungal), new BlockItem(biomeChiseledBrickFungal, props));

		register(r, Registry.BLOCK.getId(biomeStoneSwamp), new BlockItem(biomeStoneSwamp, props));
		register(r, Registry.BLOCK.getId(biomeStoneSwampSlab), new BlockItem(biomeStoneSwampSlab, props));
		register(r, Registry.BLOCK.getId(biomeStoneSwampStairs), new BlockItem(biomeStoneSwampStairs, props));
		register(r, Registry.BLOCK.getId(biomeBrickSwamp), new BlockItem(biomeBrickSwamp, props));
		register(r, Registry.BLOCK.getId(biomeBrickSwampSlab), new BlockItem(biomeBrickSwampSlab, props));
		register(r, Registry.BLOCK.getId(biomeBrickSwampStairs), new BlockItem(biomeBrickSwampStairs, props));
		register(r, Registry.BLOCK.getId(biomeBrickSwampWall), new BlockItem(biomeBrickSwampWall, props));
		register(r, Registry.BLOCK.getId(biomeCobblestoneSwamp), new BlockItem(biomeCobblestoneSwamp, props));
		register(r, Registry.BLOCK.getId(biomeCobblestoneSwampSlab), new BlockItem(biomeCobblestoneSwampSlab, props));
		register(r, Registry.BLOCK.getId(biomeCobblestoneSwampStairs), new BlockItem(biomeCobblestoneSwampStairs, props));
		register(r, Registry.BLOCK.getId(biomeCobblestoneSwampWall), new BlockItem(biomeCobblestoneSwampWall, props));
		register(r, Registry.BLOCK.getId(biomeChiseledBrickSwamp), new BlockItem(biomeChiseledBrickSwamp, props));

		register(r, Registry.BLOCK.getId(biomeStoneDesert), new BlockItem(biomeStoneDesert, props));
		register(r, Registry.BLOCK.getId(biomeStoneDesertSlab), new BlockItem(biomeStoneDesertSlab, props));
		register(r, Registry.BLOCK.getId(biomeStoneDesertStairs), new BlockItem(biomeStoneDesertStairs, props));
		register(r, Registry.BLOCK.getId(biomeBrickDesert), new BlockItem(biomeBrickDesert, props));
		register(r, Registry.BLOCK.getId(biomeBrickDesertSlab), new BlockItem(biomeBrickDesertSlab, props));
		register(r, Registry.BLOCK.getId(biomeBrickDesertStairs), new BlockItem(biomeBrickDesertStairs, props));
		register(r, Registry.BLOCK.getId(biomeBrickDesertWall), new BlockItem(biomeBrickDesertWall, props));
		register(r, Registry.BLOCK.getId(biomeCobblestoneDesert), new BlockItem(biomeCobblestoneDesert, props));
		register(r, Registry.BLOCK.getId(biomeCobblestoneDesertSlab), new BlockItem(biomeCobblestoneDesertSlab, props));
		register(r, Registry.BLOCK.getId(biomeCobblestoneDesertStairs), new BlockItem(biomeCobblestoneDesertStairs, props));
		register(r, Registry.BLOCK.getId(biomeCobblestoneDesertWall), new BlockItem(biomeCobblestoneDesertWall, props));
		register(r, Registry.BLOCK.getId(biomeChiseledBrickDesert), new BlockItem(biomeChiseledBrickDesert, props));

		register(r, Registry.BLOCK.getId(biomeStoneTaiga), new BlockItem(biomeStoneTaiga, props));
		register(r, Registry.BLOCK.getId(biomeStoneTaigaSlab), new BlockItem(biomeStoneTaigaSlab, props));
		register(r, Registry.BLOCK.getId(biomeStoneTaigaStairs), new BlockItem(biomeStoneTaigaStairs, props));
		register(r, Registry.BLOCK.getId(biomeBrickTaiga), new BlockItem(biomeBrickTaiga, props));
		register(r, Registry.BLOCK.getId(biomeBrickTaigaSlab), new BlockItem(biomeBrickTaigaSlab, props));
		register(r, Registry.BLOCK.getId(biomeBrickTaigaStairs), new BlockItem(biomeBrickTaigaStairs, props));
		register(r, Registry.BLOCK.getId(biomeBrickTaigaWall), new BlockItem(biomeBrickTaigaWall, props));
		register(r, Registry.BLOCK.getId(biomeCobblestoneTaiga), new BlockItem(biomeCobblestoneTaiga, props));
		register(r, Registry.BLOCK.getId(biomeCobblestoneTaigaSlab), new BlockItem(biomeCobblestoneTaigaSlab, props));
		register(r, Registry.BLOCK.getId(biomeCobblestoneTaigaStairs), new BlockItem(biomeCobblestoneTaigaStairs, props));
		register(r, Registry.BLOCK.getId(biomeCobblestoneTaigaWall), new BlockItem(biomeCobblestoneTaigaWall, props));
		register(r, Registry.BLOCK.getId(biomeChiseledBrickTaiga), new BlockItem(biomeChiseledBrickTaiga, props));

		register(r, Registry.BLOCK.getId(biomeStoneMesa), new BlockItem(biomeStoneMesa, props));
		register(r, Registry.BLOCK.getId(biomeStoneMesaSlab), new BlockItem(biomeStoneMesaSlab, props));
		register(r, Registry.BLOCK.getId(biomeStoneMesaStairs), new BlockItem(biomeStoneMesaStairs, props));
		register(r, Registry.BLOCK.getId(biomeBrickMesa), new BlockItem(biomeBrickMesa, props));
		register(r, Registry.BLOCK.getId(biomeBrickMesaSlab), new BlockItem(biomeBrickMesaSlab, props));
		register(r, Registry.BLOCK.getId(biomeBrickMesaStairs), new BlockItem(biomeBrickMesaStairs, props));
		register(r, Registry.BLOCK.getId(biomeBrickMesaWall), new BlockItem(biomeBrickMesaWall, props));
		register(r, Registry.BLOCK.getId(biomeCobblestoneMesa), new BlockItem(biomeCobblestoneMesa, props));
		register(r, Registry.BLOCK.getId(biomeCobblestoneMesaSlab), new BlockItem(biomeCobblestoneMesaSlab, props));
		register(r, Registry.BLOCK.getId(biomeCobblestoneMesaStairs), new BlockItem(biomeCobblestoneMesaStairs, props));
		register(r, Registry.BLOCK.getId(biomeCobblestoneMesaWall), new BlockItem(biomeCobblestoneMesaWall, props));
		register(r, Registry.BLOCK.getId(biomeChiseledBrickMesa), new BlockItem(biomeChiseledBrickMesa, props));

		register(r, Registry.BLOCK.getId(whitePavement), new BlockItem(whitePavement, props));
		register(r, Registry.BLOCK.getId(blackPavement), new BlockItem(blackPavement, props));
		register(r, Registry.BLOCK.getId(bluePavement), new BlockItem(bluePavement, props));
		register(r, Registry.BLOCK.getId(yellowPavement), new BlockItem(yellowPavement, props));
		register(r, Registry.BLOCK.getId(redPavement), new BlockItem(redPavement, props));
		register(r, Registry.BLOCK.getId(greenPavement), new BlockItem(greenPavement, props));

		register(r, Registry.BLOCK.getId(whitePavementSlab), new BlockItem(whitePavementSlab, props));
		register(r, Registry.BLOCK.getId(blackPavementSlab), new BlockItem(blackPavementSlab, props));
		register(r, Registry.BLOCK.getId(bluePavementSlab), new BlockItem(bluePavementSlab, props));
		register(r, Registry.BLOCK.getId(yellowPavementSlab), new BlockItem(yellowPavementSlab, props));
		register(r, Registry.BLOCK.getId(redPavementSlab), new BlockItem(redPavementSlab, props));
		register(r, Registry.BLOCK.getId(greenPavementSlab), new BlockItem(greenPavementSlab, props));

		register(r, Registry.BLOCK.getId(whitePavementStair), new BlockItem(whitePavementStair, props));
		register(r, Registry.BLOCK.getId(blackPavementStair), new BlockItem(blackPavementStair, props));
		register(r, Registry.BLOCK.getId(bluePavementStair), new BlockItem(bluePavementStair, props));
		register(r, Registry.BLOCK.getId(yellowPavementStair), new BlockItem(yellowPavementStair, props));
		register(r, Registry.BLOCK.getId(redPavementStair), new BlockItem(redPavementStair, props));
		register(r, Registry.BLOCK.getId(greenPavementStair), new BlockItem(greenPavementStair, props));

		register(r, Registry.BLOCK.getId(shimmerrockSlab), new BlockItem(shimmerrockSlab, props));
		register(r, Registry.BLOCK.getId(shimmerrockStairs), new BlockItem(shimmerrockStairs, props));

		register(r, Registry.BLOCK.getId(shimmerwoodPlankSlab), new BlockItem(shimmerwoodPlankSlab, props));
		register(r, Registry.BLOCK.getId(shimmerwoodPlankStairs), new BlockItem(shimmerwoodPlankStairs, props));

		register(r, Registry.BLOCK.getId(managlassPane), new BlockItem(managlassPane, props));
		register(r, Registry.BLOCK.getId(alfglassPane), new BlockItem(alfglassPane, props));
		register(r, Registry.BLOCK.getId(bifrostPane), new BlockItem(bifrostPane, props));
	}

}
