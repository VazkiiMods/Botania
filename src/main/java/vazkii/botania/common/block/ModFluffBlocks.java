/*
 * This class is distributed as part of the Botania Mod.
 * Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 *
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 */
package vazkii.botania.common.block;

import net.minecraft.core.Registry;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.FenceBlock;
import net.minecraft.world.level.block.FenceGateBlock;
import net.minecraft.world.level.block.RotatedPillarBlock;
import net.minecraft.world.level.block.SlabBlock;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.WallBlock;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.material.Material;

import vazkii.botania.common.block.decor.panes.BlockModPane;
import vazkii.botania.common.block.decor.stairs.BlockModStairs;
import vazkii.botania.common.item.ModItems;
import vazkii.botania.common.lib.LibBlockNames;

import static vazkii.botania.common.block.ModBlocks.*;
import static vazkii.botania.common.lib.LibBlockNames.*;

public final class ModFluffBlocks {
	public static final Block livingwoodStairs = new BlockModStairs(livingwood.defaultBlockState(), BlockBehaviour.Properties.copy(livingwood));
	public static final Block livingwoodSlab = new SlabBlock(BlockBehaviour.Properties.copy(livingwood));
	public static final Block livingwoodWall = new WallBlock(BlockBehaviour.Properties.copy(livingwood));
	public static final Block livingwoodFence = new FenceBlock(BlockBehaviour.Properties.copy(livingwood));
	public static final Block livingwoodFenceGate = new FenceGateBlock(BlockBehaviour.Properties.copy(livingwood));
	public static final Block livingwoodStrippedStairs = new BlockModStairs(livingwoodStripped.defaultBlockState(), BlockBehaviour.Properties.copy(livingwood));
	public static final Block livingwoodStrippedSlab = new SlabBlock(BlockBehaviour.Properties.copy(livingwood));
	public static final Block livingwoodStrippedWall = new WallBlock(BlockBehaviour.Properties.copy(livingwood));
	public static final Block livingwoodPlankStairs = new BlockModStairs(livingwoodPlanks.defaultBlockState(), BlockBehaviour.Properties.copy(livingwoodPlanks));
	public static final Block livingwoodPlankSlab = new SlabBlock(BlockBehaviour.Properties.copy(livingwoodPlanks));

	public static final Block livingrockStairs = new BlockModStairs(livingrock.defaultBlockState(), BlockBehaviour.Properties.copy(livingrock));
	public static final Block livingrockSlab = new SlabBlock(BlockBehaviour.Properties.copy(livingrock));
	public static final Block livingrockWall = new WallBlock(BlockBehaviour.Properties.copy(livingrock));
	public static final Block livingrockBrickStairs = new BlockModStairs(livingrockBrick.defaultBlockState(), BlockBehaviour.Properties.copy(livingrockBrick));
	public static final Block livingrockBrickSlab = new SlabBlock(BlockBehaviour.Properties.copy(livingrockBrick));
	public static final Block livingrockBrickWall = new WallBlock(BlockBehaviour.Properties.copy(livingrockBrick));
	public static final Block livingrockBrickMossyStairs = new BlockModStairs(livingrockBrickMossy.defaultBlockState(), BlockBehaviour.Properties.copy(livingrockBrickMossy));
	public static final Block livingrockBrickMossySlab = new SlabBlock(BlockBehaviour.Properties.copy(livingrockBrickMossy));
	public static final Block livingrockBrickMossyWall = new WallBlock(BlockBehaviour.Properties.copy(livingrockBrickMossy));

	public static final Block dreamwoodStairs = new BlockModStairs(dreamwood.defaultBlockState(), BlockBehaviour.Properties.copy(dreamwood));
	public static final Block dreamwoodSlab = new SlabBlock(BlockBehaviour.Properties.copy(dreamwood));
	public static final Block dreamwoodWall = new WallBlock(BlockBehaviour.Properties.copy(dreamwood));
	public static final Block dreamwoodFence = new FenceBlock(BlockBehaviour.Properties.copy(dreamwood));
	public static final Block dreamwoodFenceGate = new FenceGateBlock(BlockBehaviour.Properties.copy(dreamwood));
	public static final Block dreamwoodStrippedStairs = new BlockModStairs(dreamwoodStripped.defaultBlockState(), BlockBehaviour.Properties.copy(dreamwood));
	public static final Block dreamwoodStrippedSlab = new SlabBlock(BlockBehaviour.Properties.copy(dreamwood));
	public static final Block dreamwoodStrippedWall = new WallBlock(BlockBehaviour.Properties.copy(dreamwood));
	public static final Block dreamwoodPlankStairs = new BlockModStairs(dreamwoodPlanks.defaultBlockState(), BlockBehaviour.Properties.copy(dreamwoodPlanks));
	public static final Block dreamwoodPlankSlab = new SlabBlock(BlockBehaviour.Properties.copy(dreamwoodPlanks));

	public static final Block darkQuartz = new BlockMod(BlockBehaviour.Properties.copy(Blocks.QUARTZ_BLOCK));
	public static final Block darkQuartzChiseled = new BlockMod(BlockBehaviour.Properties.copy(darkQuartz));
	public static final Block darkQuartzPillar = new RotatedPillarBlock(BlockBehaviour.Properties.copy(darkQuartz));
	public static final Block darkQuartzSlab = new SlabBlock(BlockBehaviour.Properties.copy(darkQuartz));
	public static final Block darkQuartzStairs = new BlockModStairs(darkQuartz.defaultBlockState(), BlockBehaviour.Properties.copy(darkQuartz));

	public static final Block manaQuartz = new BlockMod(BlockBehaviour.Properties.copy(darkQuartz));
	public static final Block manaQuartzChiseled = new BlockMod(BlockBehaviour.Properties.copy(darkQuartz));
	public static final Block manaQuartzPillar = new RotatedPillarBlock(BlockBehaviour.Properties.copy(darkQuartz));
	public static final Block manaQuartzSlab = new SlabBlock(BlockBehaviour.Properties.copy(darkQuartz));
	public static final Block manaQuartzStairs = new BlockModStairs(darkQuartz.defaultBlockState(), BlockBehaviour.Properties.copy(darkQuartz));

	public static final Block blazeQuartz = new BlockMod(BlockBehaviour.Properties.copy(darkQuartz));
	public static final Block blazeQuartzChiseled = new BlockMod(BlockBehaviour.Properties.copy(darkQuartz));
	public static final Block blazeQuartzPillar = new RotatedPillarBlock(BlockBehaviour.Properties.copy(darkQuartz));
	public static final Block blazeQuartzSlab = new SlabBlock(BlockBehaviour.Properties.copy(darkQuartz));
	public static final Block blazeQuartzStairs = new BlockModStairs(darkQuartz.defaultBlockState(), BlockBehaviour.Properties.copy(darkQuartz));

	public static final Block lavenderQuartz = new BlockMod(BlockBehaviour.Properties.copy(darkQuartz));
	public static final Block lavenderQuartzChiseled = new BlockMod(BlockBehaviour.Properties.copy(darkQuartz));
	public static final Block lavenderQuartzPillar = new RotatedPillarBlock(BlockBehaviour.Properties.copy(darkQuartz));
	public static final Block lavenderQuartzSlab = new SlabBlock(BlockBehaviour.Properties.copy(darkQuartz));
	public static final Block lavenderQuartzStairs = new BlockModStairs(darkQuartz.defaultBlockState(), BlockBehaviour.Properties.copy(darkQuartz));

	public static final Block redQuartz = new BlockMod(BlockBehaviour.Properties.copy(darkQuartz));
	public static final Block redQuartzChiseled = new BlockMod(BlockBehaviour.Properties.copy(darkQuartz));
	public static final Block redQuartzPillar = new RotatedPillarBlock(BlockBehaviour.Properties.copy(darkQuartz));
	public static final Block redQuartzSlab = new SlabBlock(BlockBehaviour.Properties.copy(darkQuartz));
	public static final Block redQuartzStairs = new BlockModStairs(darkQuartz.defaultBlockState(), BlockBehaviour.Properties.copy(darkQuartz));

	public static final Block elfQuartz = new BlockMod(BlockBehaviour.Properties.copy(darkQuartz));
	public static final Block elfQuartzChiseled = new BlockMod(BlockBehaviour.Properties.copy(darkQuartz));
	public static final Block elfQuartzPillar = new RotatedPillarBlock(BlockBehaviour.Properties.copy(darkQuartz));
	public static final Block elfQuartzSlab = new SlabBlock(BlockBehaviour.Properties.copy(darkQuartz));
	public static final Block elfQuartzStairs = new BlockModStairs(darkQuartz.defaultBlockState(), BlockBehaviour.Properties.copy(darkQuartz));

	public static final Block sunnyQuartz = new BlockMod(BlockBehaviour.Properties.copy(darkQuartz));
	public static final Block sunnyQuartzChiseled = new BlockMod(BlockBehaviour.Properties.copy(darkQuartz));
	public static final Block sunnyQuartzPillar = new RotatedPillarBlock(BlockBehaviour.Properties.copy(darkQuartz));
	public static final Block sunnyQuartzSlab = new SlabBlock(BlockBehaviour.Properties.copy(darkQuartz));
	public static final Block sunnyQuartzStairs = new BlockModStairs(darkQuartz.defaultBlockState(), BlockBehaviour.Properties.copy(darkQuartz));

	public static final Block whitePavement = new BlockMod(BlockBehaviour.Properties.copy(livingrock));
	public static final Block whitePavementStair = new BlockModStairs(whitePavement.defaultBlockState(), BlockBehaviour.Properties.copy(whitePavement));
	public static final Block whitePavementSlab = new SlabBlock(BlockBehaviour.Properties.copy(whitePavement));

	public static final Block blackPavement = new BlockMod(BlockBehaviour.Properties.copy(whitePavement));
	public static final Block blackPavementStair = new BlockModStairs(whitePavement.defaultBlockState(), BlockBehaviour.Properties.copy(whitePavement));
	public static final Block blackPavementSlab = new SlabBlock(BlockBehaviour.Properties.copy(whitePavement));

	public static final Block bluePavement = new BlockMod(BlockBehaviour.Properties.copy(whitePavement));
	public static final Block bluePavementStair = new BlockModStairs(whitePavement.defaultBlockState(), BlockBehaviour.Properties.copy(whitePavement));
	public static final Block bluePavementSlab = new SlabBlock(BlockBehaviour.Properties.copy(whitePavement));

	public static final Block yellowPavement = new BlockMod(BlockBehaviour.Properties.copy(whitePavement));
	public static final Block yellowPavementStair = new BlockModStairs(whitePavement.defaultBlockState(), BlockBehaviour.Properties.copy(whitePavement));
	public static final Block yellowPavementSlab = new SlabBlock(BlockBehaviour.Properties.copy(whitePavement));

	public static final Block redPavement = new BlockMod(BlockBehaviour.Properties.copy(whitePavement));
	public static final Block redPavementStair = new BlockModStairs(whitePavement.defaultBlockState(), BlockBehaviour.Properties.copy(whitePavement));
	public static final Block redPavementSlab = new SlabBlock(BlockBehaviour.Properties.copy(whitePavement));

	public static final Block greenPavement = new BlockMod(BlockBehaviour.Properties.copy(whitePavement));
	public static final Block greenPavementStair = new BlockModStairs(whitePavement.defaultBlockState(), BlockBehaviour.Properties.copy(whitePavement));
	public static final Block greenPavementSlab = new SlabBlock(BlockBehaviour.Properties.copy(whitePavement));

	public static final Block biomeStoneForest = new BlockMod(BlockBehaviour.Properties.of(Material.STONE).strength(1.5F, 10).sound(SoundType.STONE).requiresCorrectToolForDrops());
	public static final Block biomeStoneForestSlab = new SlabBlock(BlockBehaviour.Properties.copy(biomeStoneForest));
	public static final Block biomeStoneForestStairs = new BlockModStairs(biomeStoneForest.defaultBlockState(), BlockBehaviour.Properties.copy(biomeStoneForest));
	public static final Block biomeCobblestoneForest = new BlockMod(BlockBehaviour.Properties.copy(biomeStoneForest));
	public static final Block biomeCobblestoneForestSlab = new SlabBlock(BlockBehaviour.Properties.copy(biomeStoneForest));
	public static final Block biomeCobblestoneForestStairs = new BlockModStairs(biomeStoneForest.defaultBlockState(), BlockBehaviour.Properties.copy(biomeStoneForest));
	public static final Block biomeCobblestoneForestWall = new WallBlock(BlockBehaviour.Properties.copy(biomeStoneForest));
	public static final Block biomeBrickForest = new BlockMod(BlockBehaviour.Properties.copy(biomeStoneForest));
	public static final Block biomeBrickForestSlab = new SlabBlock(BlockBehaviour.Properties.copy(biomeStoneForest));
	public static final Block biomeBrickForestStairs = new BlockModStairs(biomeStoneForest.defaultBlockState(), BlockBehaviour.Properties.copy(biomeStoneForest));
	public static final Block biomeBrickForestWall = new WallBlock(BlockBehaviour.Properties.copy(biomeStoneForest));
	public static final Block biomeChiseledBrickForest = new BlockMod(BlockBehaviour.Properties.copy(biomeStoneForest));

	public static final Block biomeStonePlains = new BlockMod(BlockBehaviour.Properties.copy(biomeStoneForest));
	public static final Block biomeStonePlainsSlab = new SlabBlock(BlockBehaviour.Properties.copy(biomeStonePlains));
	public static final Block biomeStonePlainsStairs = new BlockModStairs(biomeStonePlains.defaultBlockState(), BlockBehaviour.Properties.copy(biomeStonePlains));
	public static final Block biomeCobblestonePlains = new BlockMod(BlockBehaviour.Properties.copy(biomeStonePlains));
	public static final Block biomeCobblestonePlainsSlab = new SlabBlock(BlockBehaviour.Properties.copy(biomeStonePlains));
	public static final Block biomeCobblestonePlainsStairs = new BlockModStairs(biomeStonePlains.defaultBlockState(), BlockBehaviour.Properties.copy(biomeStonePlains));
	public static final Block biomeCobblestonePlainsWall = new WallBlock(BlockBehaviour.Properties.copy(biomeStonePlains));
	public static final Block biomeBrickPlains = new BlockMod(BlockBehaviour.Properties.copy(biomeStonePlains));
	public static final Block biomeBrickPlainsSlab = new SlabBlock(BlockBehaviour.Properties.copy(biomeStonePlains));
	public static final Block biomeBrickPlainsStairs = new BlockModStairs(biomeStonePlains.defaultBlockState(), BlockBehaviour.Properties.copy(biomeStonePlains));
	public static final Block biomeBrickPlainsWall = new WallBlock(BlockBehaviour.Properties.copy(biomeStonePlains));
	public static final Block biomeChiseledBrickPlains = new BlockMod(BlockBehaviour.Properties.copy(biomeStonePlains));

	public static final Block biomeStoneMountain = new BlockMod(BlockBehaviour.Properties.copy(biomeStoneForest));
	public static final Block biomeStoneMountainSlab = new SlabBlock(BlockBehaviour.Properties.copy(biomeStoneMountain));
	public static final Block biomeStoneMountainStairs = new BlockModStairs(biomeStoneMountain.defaultBlockState(), BlockBehaviour.Properties.copy(biomeStoneMountain));
	public static final Block biomeCobblestoneMountain = new BlockMod(BlockBehaviour.Properties.copy(biomeStoneMountain));
	public static final Block biomeCobblestoneMountainSlab = new SlabBlock(BlockBehaviour.Properties.copy(biomeStoneMountain));
	public static final Block biomeCobblestoneMountainStairs = new BlockModStairs(biomeStoneMountain.defaultBlockState(), BlockBehaviour.Properties.copy(biomeStoneMountain));
	public static final Block biomeCobblestoneMountainWall = new WallBlock(BlockBehaviour.Properties.copy(biomeStoneMountain));
	public static final Block biomeBrickMountain = new BlockMod(BlockBehaviour.Properties.copy(biomeStoneMountain));
	public static final Block biomeBrickMountainSlab = new SlabBlock(BlockBehaviour.Properties.copy(biomeStoneMountain));
	public static final Block biomeBrickMountainStairs = new BlockModStairs(biomeStoneMountain.defaultBlockState(), BlockBehaviour.Properties.copy(biomeStoneMountain));
	public static final Block biomeBrickMountainWall = new WallBlock(BlockBehaviour.Properties.copy(biomeStoneMountain));
	public static final Block biomeChiseledBrickMountain = new BlockMod(BlockBehaviour.Properties.copy(biomeStoneMountain));

	public static final Block biomeStoneFungal = new BlockMod(BlockBehaviour.Properties.copy(biomeStoneForest));
	public static final Block biomeStoneFungalSlab = new SlabBlock(BlockBehaviour.Properties.copy(biomeStoneFungal));
	public static final Block biomeStoneFungalStairs = new BlockModStairs(biomeStoneFungal.defaultBlockState(), BlockBehaviour.Properties.copy(biomeStoneFungal));
	public static final Block biomeCobblestoneFungal = new BlockMod(BlockBehaviour.Properties.copy(biomeStoneFungal));
	public static final Block biomeCobblestoneFungalSlab = new SlabBlock(BlockBehaviour.Properties.copy(biomeStoneFungal));
	public static final Block biomeCobblestoneFungalStairs = new BlockModStairs(biomeStoneFungal.defaultBlockState(), BlockBehaviour.Properties.copy(biomeStoneFungal));
	public static final Block biomeCobblestoneFungalWall = new WallBlock(BlockBehaviour.Properties.copy(biomeStoneFungal));
	public static final Block biomeBrickFungal = new BlockMod(BlockBehaviour.Properties.copy(biomeStoneFungal));
	public static final Block biomeBrickFungalSlab = new SlabBlock(BlockBehaviour.Properties.copy(biomeStoneFungal));
	public static final Block biomeBrickFungalStairs = new BlockModStairs(biomeStoneFungal.defaultBlockState(), BlockBehaviour.Properties.copy(biomeStoneFungal));
	public static final Block biomeBrickFungalWall = new WallBlock(BlockBehaviour.Properties.copy(biomeStoneFungal));
	public static final Block biomeChiseledBrickFungal = new BlockMod(BlockBehaviour.Properties.copy(biomeStoneFungal));

	public static final Block biomeStoneSwamp = new BlockMod(BlockBehaviour.Properties.copy(biomeStoneForest));
	public static final Block biomeStoneSwampSlab = new SlabBlock(BlockBehaviour.Properties.copy(biomeStoneSwamp));
	public static final Block biomeStoneSwampStairs = new BlockModStairs(biomeStoneSwamp.defaultBlockState(), BlockBehaviour.Properties.copy(biomeStoneSwamp));
	public static final Block biomeCobblestoneSwamp = new BlockMod(BlockBehaviour.Properties.copy(biomeStoneSwamp));
	public static final Block biomeCobblestoneSwampSlab = new SlabBlock(BlockBehaviour.Properties.copy(biomeStoneSwamp));
	public static final Block biomeCobblestoneSwampStairs = new BlockModStairs(biomeStoneSwamp.defaultBlockState(), BlockBehaviour.Properties.copy(biomeStoneSwamp));
	public static final Block biomeCobblestoneSwampWall = new WallBlock(BlockBehaviour.Properties.copy(biomeStoneSwamp));
	public static final Block biomeBrickSwamp = new BlockMod(BlockBehaviour.Properties.copy(biomeStoneSwamp));
	public static final Block biomeBrickSwampSlab = new SlabBlock(BlockBehaviour.Properties.copy(biomeStoneSwamp));
	public static final Block biomeBrickSwampStairs = new BlockModStairs(biomeStoneSwamp.defaultBlockState(), BlockBehaviour.Properties.copy(biomeStoneSwamp));
	public static final Block biomeBrickSwampWall = new WallBlock(BlockBehaviour.Properties.copy(biomeStoneSwamp));
	public static final Block biomeChiseledBrickSwamp = new BlockMod(BlockBehaviour.Properties.copy(biomeStoneSwamp));

	public static final Block biomeStoneDesert = new BlockMod(BlockBehaviour.Properties.copy(biomeStoneForest));
	public static final Block biomeStoneDesertSlab = new SlabBlock(BlockBehaviour.Properties.copy(biomeStoneDesert));
	public static final Block biomeStoneDesertStairs = new BlockModStairs(biomeStoneDesert.defaultBlockState(), BlockBehaviour.Properties.copy(biomeStoneDesert));
	public static final Block biomeCobblestoneDesert = new BlockMod(BlockBehaviour.Properties.copy(biomeStoneDesert));
	public static final Block biomeCobblestoneDesertSlab = new SlabBlock(BlockBehaviour.Properties.copy(biomeStoneDesert));
	public static final Block biomeCobblestoneDesertStairs = new BlockModStairs(biomeStoneDesert.defaultBlockState(), BlockBehaviour.Properties.copy(biomeStoneDesert));
	public static final Block biomeCobblestoneDesertWall = new WallBlock(BlockBehaviour.Properties.copy(biomeStoneDesert));
	public static final Block biomeBrickDesert = new BlockMod(BlockBehaviour.Properties.copy(biomeStoneDesert));
	public static final Block biomeBrickDesertSlab = new SlabBlock(BlockBehaviour.Properties.copy(biomeStoneDesert));
	public static final Block biomeBrickDesertStairs = new BlockModStairs(biomeStoneDesert.defaultBlockState(), BlockBehaviour.Properties.copy(biomeStoneDesert));
	public static final Block biomeBrickDesertWall = new WallBlock(BlockBehaviour.Properties.copy(biomeStoneDesert));
	public static final Block biomeChiseledBrickDesert = new BlockMod(BlockBehaviour.Properties.copy(biomeStoneDesert));

	public static final Block biomeStoneTaiga = new BlockMod(BlockBehaviour.Properties.copy(biomeStoneForest));
	public static final Block biomeStoneTaigaSlab = new SlabBlock(BlockBehaviour.Properties.copy(biomeStoneTaiga));
	public static final Block biomeStoneTaigaStairs = new BlockModStairs(biomeStoneTaiga.defaultBlockState(), BlockBehaviour.Properties.copy(biomeStoneTaiga));
	public static final Block biomeCobblestoneTaiga = new BlockMod(BlockBehaviour.Properties.copy(biomeStoneTaiga));
	public static final Block biomeCobblestoneTaigaSlab = new SlabBlock(BlockBehaviour.Properties.copy(biomeStoneTaiga));
	public static final Block biomeCobblestoneTaigaStairs = new BlockModStairs(biomeStoneTaiga.defaultBlockState(), BlockBehaviour.Properties.copy(biomeStoneTaiga));
	public static final Block biomeCobblestoneTaigaWall = new WallBlock(BlockBehaviour.Properties.copy(biomeStoneTaiga));
	public static final Block biomeBrickTaiga = new BlockMod(BlockBehaviour.Properties.copy(biomeStoneTaiga));
	public static final Block biomeBrickTaigaSlab = new SlabBlock(BlockBehaviour.Properties.copy(biomeStoneTaiga));
	public static final Block biomeBrickTaigaStairs = new BlockModStairs(biomeStoneTaiga.defaultBlockState(), BlockBehaviour.Properties.copy(biomeStoneTaiga));
	public static final Block biomeBrickTaigaWall = new WallBlock(BlockBehaviour.Properties.copy(biomeStoneTaiga));
	public static final Block biomeChiseledBrickTaiga = new BlockMod(BlockBehaviour.Properties.copy(biomeStoneTaiga));

	public static final Block biomeStoneMesa = new BlockMod(BlockBehaviour.Properties.copy(biomeStoneForest));
	public static final Block biomeStoneMesaSlab = new SlabBlock(BlockBehaviour.Properties.copy(biomeStoneMesa));
	public static final Block biomeStoneMesaStairs = new BlockModStairs(biomeStoneMesa.defaultBlockState(), BlockBehaviour.Properties.copy(biomeStoneMesa));
	public static final Block biomeCobblestoneMesa = new BlockMod(BlockBehaviour.Properties.copy(biomeStoneMesa));
	public static final Block biomeCobblestoneMesaSlab = new SlabBlock(BlockBehaviour.Properties.copy(biomeStoneMesa));
	public static final Block biomeCobblestoneMesaStairs = new BlockModStairs(biomeStoneMesa.defaultBlockState(), BlockBehaviour.Properties.copy(biomeStoneMesa));
	public static final Block biomeCobblestoneMesaWall = new WallBlock(BlockBehaviour.Properties.copy(biomeStoneMesa));
	public static final Block biomeBrickMesa = new BlockMod(BlockBehaviour.Properties.copy(biomeStoneMesa));
	public static final Block biomeBrickMesaSlab = new SlabBlock(BlockBehaviour.Properties.copy(biomeStoneMesa));
	public static final Block biomeBrickMesaStairs = new BlockModStairs(biomeStoneMesa.defaultBlockState(), BlockBehaviour.Properties.copy(biomeStoneMesa));
	public static final Block biomeBrickMesaWall = new WallBlock(BlockBehaviour.Properties.copy(biomeStoneMesa));
	public static final Block biomeChiseledBrickMesa = new BlockMod(BlockBehaviour.Properties.copy(biomeStoneMesa));

	public static final Block shimmerrockSlab = new SlabBlock(BlockBehaviour.Properties.copy(shimmerrock));
	public static final Block shimmerrockStairs = new BlockModStairs(shimmerrock.defaultBlockState(), BlockBehaviour.Properties.copy(shimmerrock));

	public static final Block shimmerwoodPlankSlab = new SlabBlock(BlockBehaviour.Properties.copy(shimmerwoodPlanks));
	public static final Block shimmerwoodPlankStairs = new BlockModStairs(shimmerwoodPlanks.defaultBlockState(), BlockBehaviour.Properties.copy(shimmerwoodPlanks));

	public static final Block managlassPane = new BlockModPane(BlockBehaviour.Properties.copy(manaGlass));
	public static final Block alfglassPane = new BlockModPane(BlockBehaviour.Properties.copy(elfGlass));
	public static final Block bifrostPane = new BlockModPane(BlockBehaviour.Properties.copy(bifrostPerm));

	public static void registerBlocks() {
		Registry<Block> r = Registry.BLOCK;

		register(r, LibBlockNames.LIVING_WOOD + STAIR_SUFFIX, livingwoodStairs);
		register(r, LibBlockNames.LIVING_WOOD + SLAB_SUFFIX, livingwoodSlab);
		register(r, LibBlockNames.LIVING_WOOD + WALL_SUFFIX, livingwoodWall);
		register(r, LibBlockNames.LIVING_WOOD + FENCE_SUFFIX, livingwoodFence);
		register(r, LibBlockNames.LIVING_WOOD + FENCE_GATE_SUFFIX, livingwoodFenceGate);
		register(r, LibBlockNames.LIVING_WOOD_STRIPPED + STAIR_SUFFIX, livingwoodStrippedStairs);
		register(r, LibBlockNames.LIVING_WOOD_STRIPPED + SLAB_SUFFIX, livingwoodStrippedSlab);
		register(r, LibBlockNames.LIVING_WOOD_STRIPPED + WALL_SUFFIX, livingwoodStrippedWall);

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
		register(r, LibBlockNames.DREAM_WOOD_STRIPPED + STAIR_SUFFIX, dreamwoodStrippedStairs);
		register(r, LibBlockNames.DREAM_WOOD_STRIPPED + SLAB_SUFFIX, dreamwoodStrippedSlab);
		register(r, LibBlockNames.DREAM_WOOD_STRIPPED + WALL_SUFFIX, dreamwoodStrippedWall);

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
		Item.Properties props = ModItems.defaultBuilder();

		register(r, Registry.BLOCK.getKey(livingwoodStairs), new BlockItem(livingwoodStairs, props));
		register(r, Registry.BLOCK.getKey(livingwoodSlab), new BlockItem(livingwoodSlab, props));
		register(r, Registry.BLOCK.getKey(livingwoodWall), new BlockItem(livingwoodWall, props));
		register(r, Registry.BLOCK.getKey(livingwoodFence), new BlockItem(livingwoodFence, props));
		register(r, Registry.BLOCK.getKey(livingwoodFenceGate), new BlockItem(livingwoodFenceGate, props));

		register(r, Registry.BLOCK.getKey(livingwoodStrippedStairs), new BlockItem(livingwoodStrippedStairs, props));
		register(r, Registry.BLOCK.getKey(livingwoodStrippedSlab), new BlockItem(livingwoodStrippedSlab, props));
		register(r, Registry.BLOCK.getKey(livingwoodStrippedWall), new BlockItem(livingwoodStrippedWall, props));

		register(r, Registry.BLOCK.getKey(livingwoodPlankStairs), new BlockItem(livingwoodPlankStairs, props));
		register(r, Registry.BLOCK.getKey(livingwoodPlankSlab), new BlockItem(livingwoodPlankSlab, props));

		register(r, Registry.BLOCK.getKey(livingrockStairs), new BlockItem(livingrockStairs, props));
		register(r, Registry.BLOCK.getKey(livingrockSlab), new BlockItem(livingrockSlab, props));
		register(r, Registry.BLOCK.getKey(livingrockWall), new BlockItem(livingrockWall, props));

		register(r, Registry.BLOCK.getKey(livingrockBrickStairs), new BlockItem(livingrockBrickStairs, props));
		register(r, Registry.BLOCK.getKey(livingrockBrickSlab), new BlockItem(livingrockBrickSlab, props));
		register(r, Registry.BLOCK.getKey(livingrockBrickWall), new BlockItem(livingrockBrickWall, props));

		register(r, Registry.BLOCK.getKey(livingrockBrickMossyStairs), new BlockItem(livingrockBrickMossyStairs, props));
		register(r, Registry.BLOCK.getKey(livingrockBrickMossySlab), new BlockItem(livingrockBrickMossySlab, props));
		register(r, Registry.BLOCK.getKey(livingrockBrickMossyWall), new BlockItem(livingrockBrickMossyWall, props));

		register(r, Registry.BLOCK.getKey(dreamwoodStairs), new BlockItem(dreamwoodStairs, props));
		register(r, Registry.BLOCK.getKey(dreamwoodSlab), new BlockItem(dreamwoodSlab, props));
		register(r, Registry.BLOCK.getKey(dreamwoodWall), new BlockItem(dreamwoodWall, props));
		register(r, Registry.BLOCK.getKey(dreamwoodFence), new BlockItem(dreamwoodFence, props));
		register(r, Registry.BLOCK.getKey(dreamwoodFenceGate), new BlockItem(dreamwoodFenceGate, props));
		register(r, Registry.BLOCK.getKey(dreamwoodStrippedStairs), new BlockItem(dreamwoodStrippedStairs, props));
		register(r, Registry.BLOCK.getKey(dreamwoodStrippedSlab), new BlockItem(dreamwoodStrippedSlab, props));
		register(r, Registry.BLOCK.getKey(dreamwoodStrippedWall), new BlockItem(dreamwoodStrippedWall, props));

		register(r, Registry.BLOCK.getKey(dreamwoodPlankStairs), new BlockItem(dreamwoodPlankStairs, props));
		register(r, Registry.BLOCK.getKey(dreamwoodPlankSlab), new BlockItem(dreamwoodPlankSlab, props));

		register(r, Registry.BLOCK.getKey(darkQuartz), new BlockItem(darkQuartz, props));
		register(r, Registry.BLOCK.getKey(darkQuartzPillar), new BlockItem(darkQuartzPillar, props));
		register(r, Registry.BLOCK.getKey(darkQuartzChiseled), new BlockItem(darkQuartzChiseled, props));
		register(r, Registry.BLOCK.getKey(darkQuartzSlab), new BlockItem(darkQuartzSlab, props));
		register(r, Registry.BLOCK.getKey(darkQuartzStairs), new BlockItem(darkQuartzStairs, props));

		register(r, Registry.BLOCK.getKey(manaQuartz), new BlockItem(manaQuartz, props));
		register(r, Registry.BLOCK.getKey(manaQuartzPillar), new BlockItem(manaQuartzPillar, props));
		register(r, Registry.BLOCK.getKey(manaQuartzChiseled), new BlockItem(manaQuartzChiseled, props));
		register(r, Registry.BLOCK.getKey(manaQuartzSlab), new BlockItem(manaQuartzSlab, props));
		register(r, Registry.BLOCK.getKey(manaQuartzStairs), new BlockItem(manaQuartzStairs, props));

		register(r, Registry.BLOCK.getKey(blazeQuartz), new BlockItem(blazeQuartz, props));
		register(r, Registry.BLOCK.getKey(blazeQuartzPillar), new BlockItem(blazeQuartzPillar, props));
		register(r, Registry.BLOCK.getKey(blazeQuartzChiseled), new BlockItem(blazeQuartzChiseled, props));
		register(r, Registry.BLOCK.getKey(blazeQuartzSlab), new BlockItem(blazeQuartzSlab, props));
		register(r, Registry.BLOCK.getKey(blazeQuartzStairs), new BlockItem(blazeQuartzStairs, props));

		register(r, Registry.BLOCK.getKey(lavenderQuartz), new BlockItem(lavenderQuartz, props));
		register(r, Registry.BLOCK.getKey(lavenderQuartzPillar), new BlockItem(lavenderQuartzPillar, props));
		register(r, Registry.BLOCK.getKey(lavenderQuartzChiseled), new BlockItem(lavenderQuartzChiseled, props));
		register(r, Registry.BLOCK.getKey(lavenderQuartzSlab), new BlockItem(lavenderQuartzSlab, props));
		register(r, Registry.BLOCK.getKey(lavenderQuartzStairs), new BlockItem(lavenderQuartzStairs, props));

		register(r, Registry.BLOCK.getKey(redQuartz), new BlockItem(redQuartz, props));
		register(r, Registry.BLOCK.getKey(redQuartzPillar), new BlockItem(redQuartzPillar, props));
		register(r, Registry.BLOCK.getKey(redQuartzChiseled), new BlockItem(redQuartzChiseled, props));
		register(r, Registry.BLOCK.getKey(redQuartzSlab), new BlockItem(redQuartzSlab, props));
		register(r, Registry.BLOCK.getKey(redQuartzStairs), new BlockItem(redQuartzStairs, props));

		register(r, Registry.BLOCK.getKey(elfQuartz), new BlockItem(elfQuartz, props));
		register(r, Registry.BLOCK.getKey(elfQuartzPillar), new BlockItem(elfQuartzPillar, props));
		register(r, Registry.BLOCK.getKey(elfQuartzChiseled), new BlockItem(elfQuartzChiseled, props));
		register(r, Registry.BLOCK.getKey(elfQuartzSlab), new BlockItem(elfQuartzSlab, props));
		register(r, Registry.BLOCK.getKey(elfQuartzStairs), new BlockItem(elfQuartzStairs, props));

		register(r, Registry.BLOCK.getKey(sunnyQuartz), new BlockItem(sunnyQuartz, props));
		register(r, Registry.BLOCK.getKey(sunnyQuartzPillar), new BlockItem(sunnyQuartzPillar, props));
		register(r, Registry.BLOCK.getKey(sunnyQuartzChiseled), new BlockItem(sunnyQuartzChiseled, props));
		register(r, Registry.BLOCK.getKey(sunnyQuartzSlab), new BlockItem(sunnyQuartzSlab, props));
		register(r, Registry.BLOCK.getKey(sunnyQuartzStairs), new BlockItem(sunnyQuartzStairs, props));

		register(r, Registry.BLOCK.getKey(biomeStoneForest), new BlockItem(biomeStoneForest, props));
		register(r, Registry.BLOCK.getKey(biomeStoneForestSlab), new BlockItem(biomeStoneForestSlab, props));
		register(r, Registry.BLOCK.getKey(biomeStoneForestStairs), new BlockItem(biomeStoneForestStairs, props));
		register(r, Registry.BLOCK.getKey(biomeBrickForest), new BlockItem(biomeBrickForest, props));
		register(r, Registry.BLOCK.getKey(biomeBrickForestSlab), new BlockItem(biomeBrickForestSlab, props));
		register(r, Registry.BLOCK.getKey(biomeBrickForestStairs), new BlockItem(biomeBrickForestStairs, props));
		register(r, Registry.BLOCK.getKey(biomeBrickForestWall), new BlockItem(biomeBrickForestWall, props));
		register(r, Registry.BLOCK.getKey(biomeCobblestoneForest), new BlockItem(biomeCobblestoneForest, props));
		register(r, Registry.BLOCK.getKey(biomeCobblestoneForestSlab), new BlockItem(biomeCobblestoneForestSlab, props));
		register(r, Registry.BLOCK.getKey(biomeCobblestoneForestStairs), new BlockItem(biomeCobblestoneForestStairs, props));
		register(r, Registry.BLOCK.getKey(biomeCobblestoneForestWall), new BlockItem(biomeCobblestoneForestWall, props));
		register(r, Registry.BLOCK.getKey(biomeChiseledBrickForest), new BlockItem(biomeChiseledBrickForest, props));

		register(r, Registry.BLOCK.getKey(biomeStonePlains), new BlockItem(biomeStonePlains, props));
		register(r, Registry.BLOCK.getKey(biomeStonePlainsSlab), new BlockItem(biomeStonePlainsSlab, props));
		register(r, Registry.BLOCK.getKey(biomeStonePlainsStairs), new BlockItem(biomeStonePlainsStairs, props));
		register(r, Registry.BLOCK.getKey(biomeBrickPlains), new BlockItem(biomeBrickPlains, props));
		register(r, Registry.BLOCK.getKey(biomeBrickPlainsSlab), new BlockItem(biomeBrickPlainsSlab, props));
		register(r, Registry.BLOCK.getKey(biomeBrickPlainsStairs), new BlockItem(biomeBrickPlainsStairs, props));
		register(r, Registry.BLOCK.getKey(biomeBrickPlainsWall), new BlockItem(biomeBrickPlainsWall, props));
		register(r, Registry.BLOCK.getKey(biomeCobblestonePlains), new BlockItem(biomeCobblestonePlains, props));
		register(r, Registry.BLOCK.getKey(biomeCobblestonePlainsSlab), new BlockItem(biomeCobblestonePlainsSlab, props));
		register(r, Registry.BLOCK.getKey(biomeCobblestonePlainsStairs), new BlockItem(biomeCobblestonePlainsStairs, props));
		register(r, Registry.BLOCK.getKey(biomeCobblestonePlainsWall), new BlockItem(biomeCobblestonePlainsWall, props));
		register(r, Registry.BLOCK.getKey(biomeChiseledBrickPlains), new BlockItem(biomeChiseledBrickPlains, props));

		register(r, Registry.BLOCK.getKey(biomeStoneMountain), new BlockItem(biomeStoneMountain, props));
		register(r, Registry.BLOCK.getKey(biomeStoneMountainSlab), new BlockItem(biomeStoneMountainSlab, props));
		register(r, Registry.BLOCK.getKey(biomeStoneMountainStairs), new BlockItem(biomeStoneMountainStairs, props));
		register(r, Registry.BLOCK.getKey(biomeBrickMountain), new BlockItem(biomeBrickMountain, props));
		register(r, Registry.BLOCK.getKey(biomeBrickMountainSlab), new BlockItem(biomeBrickMountainSlab, props));
		register(r, Registry.BLOCK.getKey(biomeBrickMountainStairs), new BlockItem(biomeBrickMountainStairs, props));
		register(r, Registry.BLOCK.getKey(biomeBrickMountainWall), new BlockItem(biomeBrickMountainWall, props));
		register(r, Registry.BLOCK.getKey(biomeCobblestoneMountain), new BlockItem(biomeCobblestoneMountain, props));
		register(r, Registry.BLOCK.getKey(biomeCobblestoneMountainSlab), new BlockItem(biomeCobblestoneMountainSlab, props));
		register(r, Registry.BLOCK.getKey(biomeCobblestoneMountainStairs), new BlockItem(biomeCobblestoneMountainStairs, props));
		register(r, Registry.BLOCK.getKey(biomeCobblestoneMountainWall), new BlockItem(biomeCobblestoneMountainWall, props));
		register(r, Registry.BLOCK.getKey(biomeChiseledBrickMountain), new BlockItem(biomeChiseledBrickMountain, props));

		register(r, Registry.BLOCK.getKey(biomeStoneFungal), new BlockItem(biomeStoneFungal, props));
		register(r, Registry.BLOCK.getKey(biomeStoneFungalSlab), new BlockItem(biomeStoneFungalSlab, props));
		register(r, Registry.BLOCK.getKey(biomeStoneFungalStairs), new BlockItem(biomeStoneFungalStairs, props));
		register(r, Registry.BLOCK.getKey(biomeBrickFungal), new BlockItem(biomeBrickFungal, props));
		register(r, Registry.BLOCK.getKey(biomeBrickFungalSlab), new BlockItem(biomeBrickFungalSlab, props));
		register(r, Registry.BLOCK.getKey(biomeBrickFungalStairs), new BlockItem(biomeBrickFungalStairs, props));
		register(r, Registry.BLOCK.getKey(biomeBrickFungalWall), new BlockItem(biomeBrickFungalWall, props));
		register(r, Registry.BLOCK.getKey(biomeCobblestoneFungal), new BlockItem(biomeCobblestoneFungal, props));
		register(r, Registry.BLOCK.getKey(biomeCobblestoneFungalSlab), new BlockItem(biomeCobblestoneFungalSlab, props));
		register(r, Registry.BLOCK.getKey(biomeCobblestoneFungalStairs), new BlockItem(biomeCobblestoneFungalStairs, props));
		register(r, Registry.BLOCK.getKey(biomeCobblestoneFungalWall), new BlockItem(biomeCobblestoneFungalWall, props));
		register(r, Registry.BLOCK.getKey(biomeChiseledBrickFungal), new BlockItem(biomeChiseledBrickFungal, props));

		register(r, Registry.BLOCK.getKey(biomeStoneSwamp), new BlockItem(biomeStoneSwamp, props));
		register(r, Registry.BLOCK.getKey(biomeStoneSwampSlab), new BlockItem(biomeStoneSwampSlab, props));
		register(r, Registry.BLOCK.getKey(biomeStoneSwampStairs), new BlockItem(biomeStoneSwampStairs, props));
		register(r, Registry.BLOCK.getKey(biomeBrickSwamp), new BlockItem(biomeBrickSwamp, props));
		register(r, Registry.BLOCK.getKey(biomeBrickSwampSlab), new BlockItem(biomeBrickSwampSlab, props));
		register(r, Registry.BLOCK.getKey(biomeBrickSwampStairs), new BlockItem(biomeBrickSwampStairs, props));
		register(r, Registry.BLOCK.getKey(biomeBrickSwampWall), new BlockItem(biomeBrickSwampWall, props));
		register(r, Registry.BLOCK.getKey(biomeCobblestoneSwamp), new BlockItem(biomeCobblestoneSwamp, props));
		register(r, Registry.BLOCK.getKey(biomeCobblestoneSwampSlab), new BlockItem(biomeCobblestoneSwampSlab, props));
		register(r, Registry.BLOCK.getKey(biomeCobblestoneSwampStairs), new BlockItem(biomeCobblestoneSwampStairs, props));
		register(r, Registry.BLOCK.getKey(biomeCobblestoneSwampWall), new BlockItem(biomeCobblestoneSwampWall, props));
		register(r, Registry.BLOCK.getKey(biomeChiseledBrickSwamp), new BlockItem(biomeChiseledBrickSwamp, props));

		register(r, Registry.BLOCK.getKey(biomeStoneDesert), new BlockItem(biomeStoneDesert, props));
		register(r, Registry.BLOCK.getKey(biomeStoneDesertSlab), new BlockItem(biomeStoneDesertSlab, props));
		register(r, Registry.BLOCK.getKey(biomeStoneDesertStairs), new BlockItem(biomeStoneDesertStairs, props));
		register(r, Registry.BLOCK.getKey(biomeBrickDesert), new BlockItem(biomeBrickDesert, props));
		register(r, Registry.BLOCK.getKey(biomeBrickDesertSlab), new BlockItem(biomeBrickDesertSlab, props));
		register(r, Registry.BLOCK.getKey(biomeBrickDesertStairs), new BlockItem(biomeBrickDesertStairs, props));
		register(r, Registry.BLOCK.getKey(biomeBrickDesertWall), new BlockItem(biomeBrickDesertWall, props));
		register(r, Registry.BLOCK.getKey(biomeCobblestoneDesert), new BlockItem(biomeCobblestoneDesert, props));
		register(r, Registry.BLOCK.getKey(biomeCobblestoneDesertSlab), new BlockItem(biomeCobblestoneDesertSlab, props));
		register(r, Registry.BLOCK.getKey(biomeCobblestoneDesertStairs), new BlockItem(biomeCobblestoneDesertStairs, props));
		register(r, Registry.BLOCK.getKey(biomeCobblestoneDesertWall), new BlockItem(biomeCobblestoneDesertWall, props));
		register(r, Registry.BLOCK.getKey(biomeChiseledBrickDesert), new BlockItem(biomeChiseledBrickDesert, props));

		register(r, Registry.BLOCK.getKey(biomeStoneTaiga), new BlockItem(biomeStoneTaiga, props));
		register(r, Registry.BLOCK.getKey(biomeStoneTaigaSlab), new BlockItem(biomeStoneTaigaSlab, props));
		register(r, Registry.BLOCK.getKey(biomeStoneTaigaStairs), new BlockItem(biomeStoneTaigaStairs, props));
		register(r, Registry.BLOCK.getKey(biomeBrickTaiga), new BlockItem(biomeBrickTaiga, props));
		register(r, Registry.BLOCK.getKey(biomeBrickTaigaSlab), new BlockItem(biomeBrickTaigaSlab, props));
		register(r, Registry.BLOCK.getKey(biomeBrickTaigaStairs), new BlockItem(biomeBrickTaigaStairs, props));
		register(r, Registry.BLOCK.getKey(biomeBrickTaigaWall), new BlockItem(biomeBrickTaigaWall, props));
		register(r, Registry.BLOCK.getKey(biomeCobblestoneTaiga), new BlockItem(biomeCobblestoneTaiga, props));
		register(r, Registry.BLOCK.getKey(biomeCobblestoneTaigaSlab), new BlockItem(biomeCobblestoneTaigaSlab, props));
		register(r, Registry.BLOCK.getKey(biomeCobblestoneTaigaStairs), new BlockItem(biomeCobblestoneTaigaStairs, props));
		register(r, Registry.BLOCK.getKey(biomeCobblestoneTaigaWall), new BlockItem(biomeCobblestoneTaigaWall, props));
		register(r, Registry.BLOCK.getKey(biomeChiseledBrickTaiga), new BlockItem(biomeChiseledBrickTaiga, props));

		register(r, Registry.BLOCK.getKey(biomeStoneMesa), new BlockItem(biomeStoneMesa, props));
		register(r, Registry.BLOCK.getKey(biomeStoneMesaSlab), new BlockItem(biomeStoneMesaSlab, props));
		register(r, Registry.BLOCK.getKey(biomeStoneMesaStairs), new BlockItem(biomeStoneMesaStairs, props));
		register(r, Registry.BLOCK.getKey(biomeBrickMesa), new BlockItem(biomeBrickMesa, props));
		register(r, Registry.BLOCK.getKey(biomeBrickMesaSlab), new BlockItem(biomeBrickMesaSlab, props));
		register(r, Registry.BLOCK.getKey(biomeBrickMesaStairs), new BlockItem(biomeBrickMesaStairs, props));
		register(r, Registry.BLOCK.getKey(biomeBrickMesaWall), new BlockItem(biomeBrickMesaWall, props));
		register(r, Registry.BLOCK.getKey(biomeCobblestoneMesa), new BlockItem(biomeCobblestoneMesa, props));
		register(r, Registry.BLOCK.getKey(biomeCobblestoneMesaSlab), new BlockItem(biomeCobblestoneMesaSlab, props));
		register(r, Registry.BLOCK.getKey(biomeCobblestoneMesaStairs), new BlockItem(biomeCobblestoneMesaStairs, props));
		register(r, Registry.BLOCK.getKey(biomeCobblestoneMesaWall), new BlockItem(biomeCobblestoneMesaWall, props));
		register(r, Registry.BLOCK.getKey(biomeChiseledBrickMesa), new BlockItem(biomeChiseledBrickMesa, props));

		register(r, Registry.BLOCK.getKey(whitePavement), new BlockItem(whitePavement, props));
		register(r, Registry.BLOCK.getKey(blackPavement), new BlockItem(blackPavement, props));
		register(r, Registry.BLOCK.getKey(bluePavement), new BlockItem(bluePavement, props));
		register(r, Registry.BLOCK.getKey(yellowPavement), new BlockItem(yellowPavement, props));
		register(r, Registry.BLOCK.getKey(redPavement), new BlockItem(redPavement, props));
		register(r, Registry.BLOCK.getKey(greenPavement), new BlockItem(greenPavement, props));

		register(r, Registry.BLOCK.getKey(whitePavementSlab), new BlockItem(whitePavementSlab, props));
		register(r, Registry.BLOCK.getKey(blackPavementSlab), new BlockItem(blackPavementSlab, props));
		register(r, Registry.BLOCK.getKey(bluePavementSlab), new BlockItem(bluePavementSlab, props));
		register(r, Registry.BLOCK.getKey(yellowPavementSlab), new BlockItem(yellowPavementSlab, props));
		register(r, Registry.BLOCK.getKey(redPavementSlab), new BlockItem(redPavementSlab, props));
		register(r, Registry.BLOCK.getKey(greenPavementSlab), new BlockItem(greenPavementSlab, props));

		register(r, Registry.BLOCK.getKey(whitePavementStair), new BlockItem(whitePavementStair, props));
		register(r, Registry.BLOCK.getKey(blackPavementStair), new BlockItem(blackPavementStair, props));
		register(r, Registry.BLOCK.getKey(bluePavementStair), new BlockItem(bluePavementStair, props));
		register(r, Registry.BLOCK.getKey(yellowPavementStair), new BlockItem(yellowPavementStair, props));
		register(r, Registry.BLOCK.getKey(redPavementStair), new BlockItem(redPavementStair, props));
		register(r, Registry.BLOCK.getKey(greenPavementStair), new BlockItem(greenPavementStair, props));

		register(r, Registry.BLOCK.getKey(shimmerrockSlab), new BlockItem(shimmerrockSlab, props));
		register(r, Registry.BLOCK.getKey(shimmerrockStairs), new BlockItem(shimmerrockStairs, props));

		register(r, Registry.BLOCK.getKey(shimmerwoodPlankSlab), new BlockItem(shimmerwoodPlankSlab, props));
		register(r, Registry.BLOCK.getKey(shimmerwoodPlankStairs), new BlockItem(shimmerwoodPlankStairs, props));

		register(r, Registry.BLOCK.getKey(managlassPane), new BlockItem(managlassPane, props));
		register(r, Registry.BLOCK.getKey(alfglassPane), new BlockItem(alfglassPane, props));
		register(r, Registry.BLOCK.getKey(bifrostPane), new BlockItem(bifrostPane, props));
	}

}
