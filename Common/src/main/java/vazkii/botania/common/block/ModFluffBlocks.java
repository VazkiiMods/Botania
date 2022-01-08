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
import net.minecraft.resources.ResourceLocation;
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

import java.util.function.BiConsumer;

import static vazkii.botania.common.block.ModBlocks.*;
import static vazkii.botania.common.lib.LibBlockNames.*;
import static vazkii.botania.common.lib.ResourceLocationHelper.prefix;

public final class ModFluffBlocks {
	public static final Block livingwoodStairs = new BlockModStairs(livingwood.defaultBlockState(), BlockBehaviour.Properties.copy(livingwood));
	public static final Block livingwoodSlab = new SlabBlock(BlockBehaviour.Properties.copy(livingwood));
	public static final Block livingwoodWall = new WallBlock(BlockBehaviour.Properties.copy(livingwood));
	public static final Block livingwoodFence = new FenceBlock(BlockBehaviour.Properties.copy(livingwood));
	public static final Block livingwoodFenceGate = new FenceGateBlock(BlockBehaviour.Properties.copy(livingwood));
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

	public static void registerBlocks(BiConsumer<Block, ResourceLocation> r) {

		r.accept(livingwoodStairs, prefix(LibBlockNames.LIVING_WOOD + STAIR_SUFFIX));
		r.accept(livingwoodSlab, prefix(LibBlockNames.LIVING_WOOD + SLAB_SUFFIX));
		r.accept(livingwoodWall, prefix(LibBlockNames.LIVING_WOOD + WALL_SUFFIX));
		r.accept(livingwoodFence, prefix(LibBlockNames.LIVING_WOOD + FENCE_SUFFIX));
		r.accept(livingwoodFenceGate, prefix(LibBlockNames.LIVING_WOOD + FENCE_GATE_SUFFIX));

		r.accept(livingwoodPlankStairs, prefix(LibBlockNames.LIVING_WOOD_PLANKS + STAIR_SUFFIX));
		r.accept(livingwoodPlankSlab, prefix(LibBlockNames.LIVING_WOOD_PLANKS + SLAB_SUFFIX));

		r.accept(livingrockStairs, prefix(LibBlockNames.LIVING_ROCK + STAIR_SUFFIX));
		r.accept(livingrockSlab, prefix(LibBlockNames.LIVING_ROCK + SLAB_SUFFIX));
		r.accept(livingrockWall, prefix(LibBlockNames.LIVING_ROCK + WALL_SUFFIX));

		r.accept(livingrockBrickStairs, prefix(LibBlockNames.LIVING_ROCK_BRICK + STAIR_SUFFIX));
		r.accept(livingrockBrickSlab, prefix(LibBlockNames.LIVING_ROCK_BRICK + SLAB_SUFFIX));
		r.accept(livingrockBrickWall, prefix(LibBlockNames.LIVING_ROCK_BRICK + WALL_SUFFIX));

		r.accept(livingrockBrickMossyStairs, prefix(LibBlockNames.LIVING_ROCK_BRICK_MOSSY + STAIR_SUFFIX));
		r.accept(livingrockBrickMossySlab, prefix(LibBlockNames.LIVING_ROCK_BRICK_MOSSY + SLAB_SUFFIX));
		r.accept(livingrockBrickMossyWall, prefix(LibBlockNames.LIVING_ROCK_BRICK_MOSSY + WALL_SUFFIX));

		r.accept(dreamwoodStairs, prefix(LibBlockNames.DREAM_WOOD + STAIR_SUFFIX));
		r.accept(dreamwoodSlab, prefix(LibBlockNames.DREAM_WOOD + SLAB_SUFFIX));
		r.accept(dreamwoodWall, prefix(LibBlockNames.DREAM_WOOD + WALL_SUFFIX));
		r.accept(dreamwoodFence, prefix(LibBlockNames.DREAM_WOOD + FENCE_SUFFIX));
		r.accept(dreamwoodFenceGate, prefix(LibBlockNames.DREAM_WOOD + FENCE_GATE_SUFFIX));

		r.accept(dreamwoodPlankStairs, prefix(LibBlockNames.DREAM_WOOD_PLANKS + STAIR_SUFFIX));
		r.accept(dreamwoodPlankSlab, prefix(LibBlockNames.DREAM_WOOD_PLANKS + SLAB_SUFFIX));

		r.accept(darkQuartz, prefix(QUARTZ_DARK));
		r.accept(darkQuartzChiseled, prefix("chiseled_" + QUARTZ_DARK));
		r.accept(darkQuartzPillar, prefix(QUARTZ_DARK + "_pillar"));
		r.accept(darkQuartzSlab, prefix(QUARTZ_DARK + SLAB_SUFFIX));
		r.accept(darkQuartzStairs, prefix(QUARTZ_DARK + STAIR_SUFFIX));

		r.accept(manaQuartz, prefix(QUARTZ_MANA));
		r.accept(manaQuartzChiseled, prefix("chiseled_" + QUARTZ_MANA));
		r.accept(manaQuartzPillar, prefix(QUARTZ_MANA + "_pillar"));
		r.accept(manaQuartzSlab, prefix(QUARTZ_MANA + SLAB_SUFFIX));
		r.accept(manaQuartzStairs, prefix(QUARTZ_MANA + STAIR_SUFFIX));

		r.accept(blazeQuartz, prefix(QUARTZ_BLAZE));
		r.accept(blazeQuartzChiseled, prefix("chiseled_" + QUARTZ_BLAZE));
		r.accept(blazeQuartzPillar, prefix(QUARTZ_BLAZE + "_pillar"));
		r.accept(blazeQuartzSlab, prefix(QUARTZ_BLAZE + SLAB_SUFFIX));
		r.accept(blazeQuartzStairs, prefix(QUARTZ_BLAZE + STAIR_SUFFIX));

		r.accept(lavenderQuartz, prefix(QUARTZ_LAVENDER));
		r.accept(lavenderQuartzChiseled, prefix("chiseled_" + QUARTZ_LAVENDER));
		r.accept(lavenderQuartzPillar, prefix(QUARTZ_LAVENDER + "_pillar"));
		r.accept(lavenderQuartzSlab, prefix(QUARTZ_LAVENDER + SLAB_SUFFIX));
		r.accept(lavenderQuartzStairs, prefix(QUARTZ_LAVENDER + STAIR_SUFFIX));

		r.accept(redQuartz, prefix(QUARTZ_RED));
		r.accept(redQuartzChiseled, prefix("chiseled_" + QUARTZ_RED));
		r.accept(redQuartzPillar, prefix(QUARTZ_RED + "_pillar"));
		r.accept(redQuartzSlab, prefix(QUARTZ_RED + SLAB_SUFFIX));
		r.accept(redQuartzStairs, prefix(QUARTZ_RED + STAIR_SUFFIX));

		r.accept(elfQuartz, prefix(QUARTZ_ELF));
		r.accept(elfQuartzChiseled, prefix("chiseled_" + QUARTZ_ELF));
		r.accept(elfQuartzPillar, prefix(QUARTZ_ELF + "_pillar"));
		r.accept(elfQuartzSlab, prefix(QUARTZ_ELF + SLAB_SUFFIX));
		r.accept(elfQuartzStairs, prefix(QUARTZ_ELF + STAIR_SUFFIX));

		r.accept(sunnyQuartz, prefix(QUARTZ_SUNNY));
		r.accept(sunnyQuartzChiseled, prefix("chiseled_" + QUARTZ_SUNNY));
		r.accept(sunnyQuartzPillar, prefix(QUARTZ_SUNNY + "_pillar"));
		r.accept(sunnyQuartzSlab, prefix(QUARTZ_SUNNY + SLAB_SUFFIX));
		r.accept(sunnyQuartzStairs, prefix(QUARTZ_SUNNY + STAIR_SUFFIX));

		r.accept(whitePavement, prefix("white" + PAVEMENT_SUFFIX));
		r.accept(whitePavementStair, prefix("white" + PAVEMENT_SUFFIX + STAIR_SUFFIX));
		r.accept(whitePavementSlab, prefix("white" + PAVEMENT_SUFFIX + SLAB_SUFFIX));

		r.accept(blackPavement, prefix("black" + PAVEMENT_SUFFIX));
		r.accept(blackPavementStair, prefix("black" + PAVEMENT_SUFFIX + STAIR_SUFFIX));
		r.accept(blackPavementSlab, prefix("black" + PAVEMENT_SUFFIX + SLAB_SUFFIX));

		r.accept(bluePavement, prefix("blue" + PAVEMENT_SUFFIX));
		r.accept(bluePavementStair, prefix("blue" + PAVEMENT_SUFFIX + STAIR_SUFFIX));
		r.accept(bluePavementSlab, prefix("blue" + PAVEMENT_SUFFIX + SLAB_SUFFIX));

		r.accept(yellowPavement, prefix("yellow" + PAVEMENT_SUFFIX));
		r.accept(yellowPavementStair, prefix("yellow" + PAVEMENT_SUFFIX + STAIR_SUFFIX));
		r.accept(yellowPavementSlab, prefix("yellow" + PAVEMENT_SUFFIX + SLAB_SUFFIX));

		r.accept(redPavement, prefix("red" + PAVEMENT_SUFFIX));
		r.accept(redPavementStair, prefix("red" + PAVEMENT_SUFFIX + STAIR_SUFFIX));
		r.accept(redPavementSlab, prefix("red" + PAVEMENT_SUFFIX + SLAB_SUFFIX));

		r.accept(greenPavement, prefix("green" + PAVEMENT_SUFFIX));
		r.accept(greenPavementStair, prefix("green" + PAVEMENT_SUFFIX + STAIR_SUFFIX));
		r.accept(greenPavementSlab, prefix("green" + PAVEMENT_SUFFIX + SLAB_SUFFIX));

		r.accept(biomeStoneForest, prefix(METAMORPHIC_PREFIX + "forest_stone"));
		r.accept(biomeStoneForestSlab, prefix(METAMORPHIC_PREFIX + "forest_stone" + SLAB_SUFFIX));
		r.accept(biomeStoneForestStairs, prefix(METAMORPHIC_PREFIX + "forest_stone" + STAIR_SUFFIX));
		r.accept(biomeCobblestoneForest, prefix(METAMORPHIC_PREFIX + "forest_cobblestone"));
		r.accept(biomeCobblestoneForestSlab, prefix(METAMORPHIC_PREFIX + "forest_cobblestone" + SLAB_SUFFIX));
		r.accept(biomeCobblestoneForestStairs, prefix(METAMORPHIC_PREFIX + "forest_cobblestone" + STAIR_SUFFIX));
		r.accept(biomeCobblestoneForestWall, prefix(METAMORPHIC_PREFIX + "forest_cobblestone" + WALL_SUFFIX));
		r.accept(biomeBrickForest, prefix(METAMORPHIC_PREFIX + "forest_bricks"));
		r.accept(biomeBrickForestSlab, prefix(METAMORPHIC_PREFIX + "forest_bricks" + SLAB_SUFFIX));
		r.accept(biomeBrickForestStairs, prefix(METAMORPHIC_PREFIX + "forest_bricks" + STAIR_SUFFIX));
		r.accept(biomeBrickForestWall, prefix(METAMORPHIC_PREFIX + "forest_bricks" + WALL_SUFFIX));
		r.accept(biomeChiseledBrickForest, prefix("chiseled_" + METAMORPHIC_PREFIX + "forest_bricks"));

		r.accept(biomeStonePlains, prefix(METAMORPHIC_PREFIX + "plains_stone"));
		r.accept(biomeStonePlainsSlab, prefix(METAMORPHIC_PREFIX + "plains_stone" + SLAB_SUFFIX));
		r.accept(biomeStonePlainsStairs, prefix(METAMORPHIC_PREFIX + "plains_stone" + STAIR_SUFFIX));
		r.accept(biomeCobblestonePlains, prefix(METAMORPHIC_PREFIX + "plains_cobblestone"));
		r.accept(biomeCobblestonePlainsSlab, prefix(METAMORPHIC_PREFIX + "plains_cobblestone" + SLAB_SUFFIX));
		r.accept(biomeCobblestonePlainsStairs, prefix(METAMORPHIC_PREFIX + "plains_cobblestone" + STAIR_SUFFIX));
		r.accept(biomeCobblestonePlainsWall, prefix(METAMORPHIC_PREFIX + "plains_cobblestone" + WALL_SUFFIX));
		r.accept(biomeBrickPlains, prefix(METAMORPHIC_PREFIX + "plains_bricks"));
		r.accept(biomeBrickPlainsSlab, prefix(METAMORPHIC_PREFIX + "plains_bricks" + SLAB_SUFFIX));
		r.accept(biomeBrickPlainsStairs, prefix(METAMORPHIC_PREFIX + "plains_bricks" + STAIR_SUFFIX));
		r.accept(biomeBrickPlainsWall, prefix(METAMORPHIC_PREFIX + "plains_bricks" + WALL_SUFFIX));
		r.accept(biomeChiseledBrickPlains, prefix("chiseled_" + METAMORPHIC_PREFIX + "plains_bricks"));

		r.accept(biomeStoneMountain, prefix(METAMORPHIC_PREFIX + "mountain_stone"));
		r.accept(biomeStoneMountainSlab, prefix(METAMORPHIC_PREFIX + "mountain_stone" + SLAB_SUFFIX));
		r.accept(biomeStoneMountainStairs, prefix(METAMORPHIC_PREFIX + "mountain_stone" + STAIR_SUFFIX));
		r.accept(biomeCobblestoneMountain, prefix(METAMORPHIC_PREFIX + "mountain_cobblestone"));
		r.accept(biomeCobblestoneMountainSlab, prefix(METAMORPHIC_PREFIX + "mountain_cobblestone" + SLAB_SUFFIX));
		r.accept(biomeCobblestoneMountainStairs, prefix(METAMORPHIC_PREFIX + "mountain_cobblestone" + STAIR_SUFFIX));
		r.accept(biomeCobblestoneMountainWall, prefix(METAMORPHIC_PREFIX + "mountain_cobblestone" + WALL_SUFFIX));
		r.accept(biomeBrickMountain, prefix(METAMORPHIC_PREFIX + "mountain_bricks"));
		r.accept(biomeBrickMountainSlab, prefix(METAMORPHIC_PREFIX + "mountain_bricks" + SLAB_SUFFIX));
		r.accept(biomeBrickMountainStairs, prefix(METAMORPHIC_PREFIX + "mountain_bricks" + STAIR_SUFFIX));
		r.accept(biomeBrickMountainWall, prefix(METAMORPHIC_PREFIX + "mountain_bricks" + WALL_SUFFIX));
		r.accept(biomeChiseledBrickMountain, prefix("chiseled_" + METAMORPHIC_PREFIX + "mountain_bricks"));

		r.accept(biomeStoneFungal, prefix(METAMORPHIC_PREFIX + "fungal_stone"));
		r.accept(biomeStoneFungalSlab, prefix(METAMORPHIC_PREFIX + "fungal_stone" + SLAB_SUFFIX));
		r.accept(biomeStoneFungalStairs, prefix(METAMORPHIC_PREFIX + "fungal_stone" + STAIR_SUFFIX));
		r.accept(biomeCobblestoneFungal, prefix(METAMORPHIC_PREFIX + "fungal_cobblestone"));
		r.accept(biomeCobblestoneFungalSlab, prefix(METAMORPHIC_PREFIX + "fungal_cobblestone" + SLAB_SUFFIX));
		r.accept(biomeCobblestoneFungalStairs, prefix(METAMORPHIC_PREFIX + "fungal_cobblestone" + STAIR_SUFFIX));
		r.accept(biomeCobblestoneFungalWall, prefix(METAMORPHIC_PREFIX + "fungal_cobblestone" + WALL_SUFFIX));
		r.accept(biomeBrickFungal, prefix(METAMORPHIC_PREFIX + "fungal_bricks"));
		r.accept(biomeBrickFungalSlab, prefix(METAMORPHIC_PREFIX + "fungal_bricks" + SLAB_SUFFIX));
		r.accept(biomeBrickFungalStairs, prefix(METAMORPHIC_PREFIX + "fungal_bricks" + STAIR_SUFFIX));
		r.accept(biomeBrickFungalWall, prefix(METAMORPHIC_PREFIX + "fungal_bricks" + WALL_SUFFIX));
		r.accept(biomeChiseledBrickFungal, prefix("chiseled_" + METAMORPHIC_PREFIX + "fungal_bricks"));

		r.accept(biomeStoneSwamp, prefix(METAMORPHIC_PREFIX + "swamp_stone"));
		r.accept(biomeStoneSwampSlab, prefix(METAMORPHIC_PREFIX + "swamp_stone" + SLAB_SUFFIX));
		r.accept(biomeStoneSwampStairs, prefix(METAMORPHIC_PREFIX + "swamp_stone" + STAIR_SUFFIX));
		r.accept(biomeCobblestoneSwamp, prefix(METAMORPHIC_PREFIX + "swamp_cobblestone"));
		r.accept(biomeCobblestoneSwampSlab, prefix(METAMORPHIC_PREFIX + "swamp_cobblestone" + SLAB_SUFFIX));
		r.accept(biomeCobblestoneSwampStairs, prefix(METAMORPHIC_PREFIX + "swamp_cobblestone" + STAIR_SUFFIX));
		r.accept(biomeCobblestoneSwampWall, prefix(METAMORPHIC_PREFIX + "swamp_cobblestone" + WALL_SUFFIX));
		r.accept(biomeBrickSwamp, prefix(METAMORPHIC_PREFIX + "swamp_bricks"));
		r.accept(biomeBrickSwampSlab, prefix(METAMORPHIC_PREFIX + "swamp_bricks" + SLAB_SUFFIX));
		r.accept(biomeBrickSwampStairs, prefix(METAMORPHIC_PREFIX + "swamp_bricks" + STAIR_SUFFIX));
		r.accept(biomeBrickSwampWall, prefix(METAMORPHIC_PREFIX + "swamp_bricks" + WALL_SUFFIX));
		r.accept(biomeChiseledBrickSwamp, prefix("chiseled_" + METAMORPHIC_PREFIX + "swamp_bricks"));

		r.accept(biomeStoneDesert, prefix(METAMORPHIC_PREFIX + "desert_stone"));
		r.accept(biomeStoneDesertSlab, prefix(METAMORPHIC_PREFIX + "desert_stone" + SLAB_SUFFIX));
		r.accept(biomeStoneDesertStairs, prefix(METAMORPHIC_PREFIX + "desert_stone" + STAIR_SUFFIX));
		r.accept(biomeCobblestoneDesert, prefix(METAMORPHIC_PREFIX + "desert_cobblestone"));
		r.accept(biomeCobblestoneDesertSlab, prefix(METAMORPHIC_PREFIX + "desert_cobblestone" + SLAB_SUFFIX));
		r.accept(biomeCobblestoneDesertStairs, prefix(METAMORPHIC_PREFIX + "desert_cobblestone" + STAIR_SUFFIX));
		r.accept(biomeCobblestoneDesertWall, prefix(METAMORPHIC_PREFIX + "desert_cobblestone" + WALL_SUFFIX));
		r.accept(biomeBrickDesert, prefix(METAMORPHIC_PREFIX + "desert_bricks"));
		r.accept(biomeBrickDesertSlab, prefix(METAMORPHIC_PREFIX + "desert_bricks" + SLAB_SUFFIX));
		r.accept(biomeBrickDesertStairs, prefix(METAMORPHIC_PREFIX + "desert_bricks" + STAIR_SUFFIX));
		r.accept(biomeBrickDesertWall, prefix(METAMORPHIC_PREFIX + "desert_bricks" + WALL_SUFFIX));
		r.accept(biomeChiseledBrickDesert, prefix("chiseled_" + METAMORPHIC_PREFIX + "desert_bricks"));

		r.accept(biomeStoneTaiga, prefix(METAMORPHIC_PREFIX + "taiga_stone"));
		r.accept(biomeStoneTaigaSlab, prefix(METAMORPHIC_PREFIX + "taiga_stone" + SLAB_SUFFIX));
		r.accept(biomeStoneTaigaStairs, prefix(METAMORPHIC_PREFIX + "taiga_stone" + STAIR_SUFFIX));
		r.accept(biomeCobblestoneTaiga, prefix(METAMORPHIC_PREFIX + "taiga_cobblestone"));
		r.accept(biomeCobblestoneTaigaSlab, prefix(METAMORPHIC_PREFIX + "taiga_cobblestone" + SLAB_SUFFIX));
		r.accept(biomeCobblestoneTaigaStairs, prefix(METAMORPHIC_PREFIX + "taiga_cobblestone" + STAIR_SUFFIX));
		r.accept(biomeCobblestoneTaigaWall, prefix(METAMORPHIC_PREFIX + "taiga_cobblestone" + WALL_SUFFIX));
		r.accept(biomeBrickTaiga, prefix(METAMORPHIC_PREFIX + "taiga_bricks"));
		r.accept(biomeBrickTaigaSlab, prefix(METAMORPHIC_PREFIX + "taiga_bricks" + SLAB_SUFFIX));
		r.accept(biomeBrickTaigaStairs, prefix(METAMORPHIC_PREFIX + "taiga_bricks" + STAIR_SUFFIX));
		r.accept(biomeBrickTaigaWall, prefix(METAMORPHIC_PREFIX + "taiga_bricks" + WALL_SUFFIX));
		r.accept(biomeChiseledBrickTaiga, prefix("chiseled_" + METAMORPHIC_PREFIX + "taiga_bricks"));

		r.accept(biomeStoneMesa, prefix(METAMORPHIC_PREFIX + "mesa_stone"));
		r.accept(biomeStoneMesaSlab, prefix(METAMORPHIC_PREFIX + "mesa_stone" + SLAB_SUFFIX));
		r.accept(biomeStoneMesaStairs, prefix(METAMORPHIC_PREFIX + "mesa_stone" + STAIR_SUFFIX));
		r.accept(biomeCobblestoneMesa, prefix(METAMORPHIC_PREFIX + "mesa_cobblestone"));
		r.accept(biomeCobblestoneMesaSlab, prefix(METAMORPHIC_PREFIX + "mesa_cobblestone" + SLAB_SUFFIX));
		r.accept(biomeCobblestoneMesaStairs, prefix(METAMORPHIC_PREFIX + "mesa_cobblestone" + STAIR_SUFFIX));
		r.accept(biomeCobblestoneMesaWall, prefix(METAMORPHIC_PREFIX + "mesa_cobblestone" + WALL_SUFFIX));
		r.accept(biomeBrickMesa, prefix(METAMORPHIC_PREFIX + "mesa_bricks"));
		r.accept(biomeBrickMesaSlab, prefix(METAMORPHIC_PREFIX + "mesa_bricks" + SLAB_SUFFIX));
		r.accept(biomeBrickMesaStairs, prefix(METAMORPHIC_PREFIX + "mesa_bricks" + STAIR_SUFFIX));
		r.accept(biomeBrickMesaWall, prefix(METAMORPHIC_PREFIX + "mesa_bricks" + WALL_SUFFIX));
		r.accept(biomeChiseledBrickMesa, prefix("chiseled_" + METAMORPHIC_PREFIX + "mesa_bricks"));

		r.accept(shimmerrockSlab, prefix(LibBlockNames.SHIMMERROCK + SLAB_SUFFIX));
		r.accept(shimmerrockStairs, prefix(LibBlockNames.SHIMMERROCK + STAIR_SUFFIX));

		r.accept(shimmerwoodPlankSlab, prefix(LibBlockNames.SHIMMERWOOD_PLANKS + SLAB_SUFFIX));
		r.accept(shimmerwoodPlankStairs, prefix(LibBlockNames.SHIMMERWOOD_PLANKS + STAIR_SUFFIX));

		r.accept(managlassPane, prefix(LibBlockNames.MANA_GLASS + "_pane"));
		r.accept(alfglassPane, prefix(LibBlockNames.ELF_GLASS + "_pane"));
		r.accept(bifrostPane, prefix(LibBlockNames.BIFROST + "_pane"));
	}

	public static void registerItemBlocks(BiConsumer<Item, ResourceLocation> r) {
		Item.Properties props = ModItems.defaultBuilder();

		r.accept(new BlockItem(livingwoodStairs, props), Registry.BLOCK.getKey(livingwoodStairs));
		r.accept(new BlockItem(livingwoodSlab, props), Registry.BLOCK.getKey(livingwoodSlab));
		r.accept(new BlockItem(livingwoodWall, props), Registry.BLOCK.getKey(livingwoodWall));
		r.accept(new BlockItem(livingwoodFence, props), Registry.BLOCK.getKey(livingwoodFence));
		r.accept(new BlockItem(livingwoodFenceGate, props), Registry.BLOCK.getKey(livingwoodFenceGate));

		r.accept(new BlockItem(livingwoodPlankStairs, props), Registry.BLOCK.getKey(livingwoodPlankStairs));
		r.accept(new BlockItem(livingwoodPlankSlab, props), Registry.BLOCK.getKey(livingwoodPlankSlab));

		r.accept(new BlockItem(livingrockStairs, props), Registry.BLOCK.getKey(livingrockStairs));
		r.accept(new BlockItem(livingrockSlab, props), Registry.BLOCK.getKey(livingrockSlab));
		r.accept(new BlockItem(livingrockWall, props), Registry.BLOCK.getKey(livingrockWall));

		r.accept(new BlockItem(livingrockBrickStairs, props), Registry.BLOCK.getKey(livingrockBrickStairs));
		r.accept(new BlockItem(livingrockBrickSlab, props), Registry.BLOCK.getKey(livingrockBrickSlab));
		r.accept(new BlockItem(livingrockBrickWall, props), Registry.BLOCK.getKey(livingrockBrickWall));

		r.accept(new BlockItem(livingrockBrickMossyStairs, props), Registry.BLOCK.getKey(livingrockBrickMossyStairs));
		r.accept(new BlockItem(livingrockBrickMossySlab, props), Registry.BLOCK.getKey(livingrockBrickMossySlab));
		r.accept(new BlockItem(livingrockBrickMossyWall, props), Registry.BLOCK.getKey(livingrockBrickMossyWall));

		r.accept(new BlockItem(dreamwoodStairs, props), Registry.BLOCK.getKey(dreamwoodStairs));
		r.accept(new BlockItem(dreamwoodSlab, props), Registry.BLOCK.getKey(dreamwoodSlab));
		r.accept(new BlockItem(dreamwoodWall, props), Registry.BLOCK.getKey(dreamwoodWall));
		r.accept(new BlockItem(dreamwoodFence, props), Registry.BLOCK.getKey(dreamwoodFence));
		r.accept(new BlockItem(dreamwoodFenceGate, props), Registry.BLOCK.getKey(dreamwoodFenceGate));

		r.accept(new BlockItem(dreamwoodPlankStairs, props), Registry.BLOCK.getKey(dreamwoodPlankStairs));
		r.accept(new BlockItem(dreamwoodPlankSlab, props), Registry.BLOCK.getKey(dreamwoodPlankSlab));

		r.accept(new BlockItem(darkQuartz, props), Registry.BLOCK.getKey(darkQuartz));
		r.accept(new BlockItem(darkQuartzPillar, props), Registry.BLOCK.getKey(darkQuartzPillar));
		r.accept(new BlockItem(darkQuartzChiseled, props), Registry.BLOCK.getKey(darkQuartzChiseled));
		r.accept(new BlockItem(darkQuartzSlab, props), Registry.BLOCK.getKey(darkQuartzSlab));
		r.accept(new BlockItem(darkQuartzStairs, props), Registry.BLOCK.getKey(darkQuartzStairs));

		r.accept(new BlockItem(manaQuartz, props), Registry.BLOCK.getKey(manaQuartz));
		r.accept(new BlockItem(manaQuartzPillar, props), Registry.BLOCK.getKey(manaQuartzPillar));
		r.accept(new BlockItem(manaQuartzChiseled, props), Registry.BLOCK.getKey(manaQuartzChiseled));
		r.accept(new BlockItem(manaQuartzSlab, props), Registry.BLOCK.getKey(manaQuartzSlab));
		r.accept(new BlockItem(manaQuartzStairs, props), Registry.BLOCK.getKey(manaQuartzStairs));

		r.accept(new BlockItem(blazeQuartz, props), Registry.BLOCK.getKey(blazeQuartz));
		r.accept(new BlockItem(blazeQuartzPillar, props), Registry.BLOCK.getKey(blazeQuartzPillar));
		r.accept(new BlockItem(blazeQuartzChiseled, props), Registry.BLOCK.getKey(blazeQuartzChiseled));
		r.accept(new BlockItem(blazeQuartzSlab, props), Registry.BLOCK.getKey(blazeQuartzSlab));
		r.accept(new BlockItem(blazeQuartzStairs, props), Registry.BLOCK.getKey(blazeQuartzStairs));

		r.accept(new BlockItem(lavenderQuartz, props), Registry.BLOCK.getKey(lavenderQuartz));
		r.accept(new BlockItem(lavenderQuartzPillar, props), Registry.BLOCK.getKey(lavenderQuartzPillar));
		r.accept(new BlockItem(lavenderQuartzChiseled, props), Registry.BLOCK.getKey(lavenderQuartzChiseled));
		r.accept(new BlockItem(lavenderQuartzSlab, props), Registry.BLOCK.getKey(lavenderQuartzSlab));
		r.accept(new BlockItem(lavenderQuartzStairs, props), Registry.BLOCK.getKey(lavenderQuartzStairs));

		r.accept(new BlockItem(redQuartz, props), Registry.BLOCK.getKey(redQuartz));
		r.accept(new BlockItem(redQuartzPillar, props), Registry.BLOCK.getKey(redQuartzPillar));
		r.accept(new BlockItem(redQuartzChiseled, props), Registry.BLOCK.getKey(redQuartzChiseled));
		r.accept(new BlockItem(redQuartzSlab, props), Registry.BLOCK.getKey(redQuartzSlab));
		r.accept(new BlockItem(redQuartzStairs, props), Registry.BLOCK.getKey(redQuartzStairs));

		r.accept(new BlockItem(elfQuartz, props), Registry.BLOCK.getKey(elfQuartz));
		r.accept(new BlockItem(elfQuartzPillar, props), Registry.BLOCK.getKey(elfQuartzPillar));
		r.accept(new BlockItem(elfQuartzChiseled, props), Registry.BLOCK.getKey(elfQuartzChiseled));
		r.accept(new BlockItem(elfQuartzSlab, props), Registry.BLOCK.getKey(elfQuartzSlab));
		r.accept(new BlockItem(elfQuartzStairs, props), Registry.BLOCK.getKey(elfQuartzStairs));

		r.accept(new BlockItem(sunnyQuartz, props), Registry.BLOCK.getKey(sunnyQuartz));
		r.accept(new BlockItem(sunnyQuartzPillar, props), Registry.BLOCK.getKey(sunnyQuartzPillar));
		r.accept(new BlockItem(sunnyQuartzChiseled, props), Registry.BLOCK.getKey(sunnyQuartzChiseled));
		r.accept(new BlockItem(sunnyQuartzSlab, props), Registry.BLOCK.getKey(sunnyQuartzSlab));
		r.accept(new BlockItem(sunnyQuartzStairs, props), Registry.BLOCK.getKey(sunnyQuartzStairs));

		r.accept(new BlockItem(biomeStoneForest, props), Registry.BLOCK.getKey(biomeStoneForest));
		r.accept(new BlockItem(biomeStoneForestSlab, props), Registry.BLOCK.getKey(biomeStoneForestSlab));
		r.accept(new BlockItem(biomeStoneForestStairs, props), Registry.BLOCK.getKey(biomeStoneForestStairs));
		r.accept(new BlockItem(biomeBrickForest, props), Registry.BLOCK.getKey(biomeBrickForest));
		r.accept(new BlockItem(biomeBrickForestSlab, props), Registry.BLOCK.getKey(biomeBrickForestSlab));
		r.accept(new BlockItem(biomeBrickForestStairs, props), Registry.BLOCK.getKey(biomeBrickForestStairs));
		r.accept(new BlockItem(biomeBrickForestWall, props), Registry.BLOCK.getKey(biomeBrickForestWall));
		r.accept(new BlockItem(biomeCobblestoneForest, props), Registry.BLOCK.getKey(biomeCobblestoneForest));
		r.accept(new BlockItem(biomeCobblestoneForestSlab, props), Registry.BLOCK.getKey(biomeCobblestoneForestSlab));
		r.accept(new BlockItem(biomeCobblestoneForestStairs, props), Registry.BLOCK.getKey(biomeCobblestoneForestStairs));
		r.accept(new BlockItem(biomeCobblestoneForestWall, props), Registry.BLOCK.getKey(biomeCobblestoneForestWall));
		r.accept(new BlockItem(biomeChiseledBrickForest, props), Registry.BLOCK.getKey(biomeChiseledBrickForest));

		r.accept(new BlockItem(biomeStonePlains, props), Registry.BLOCK.getKey(biomeStonePlains));
		r.accept(new BlockItem(biomeStonePlainsSlab, props), Registry.BLOCK.getKey(biomeStonePlainsSlab));
		r.accept(new BlockItem(biomeStonePlainsStairs, props), Registry.BLOCK.getKey(biomeStonePlainsStairs));
		r.accept(new BlockItem(biomeBrickPlains, props), Registry.BLOCK.getKey(biomeBrickPlains));
		r.accept(new BlockItem(biomeBrickPlainsSlab, props), Registry.BLOCK.getKey(biomeBrickPlainsSlab));
		r.accept(new BlockItem(biomeBrickPlainsStairs, props), Registry.BLOCK.getKey(biomeBrickPlainsStairs));
		r.accept(new BlockItem(biomeBrickPlainsWall, props), Registry.BLOCK.getKey(biomeBrickPlainsWall));
		r.accept(new BlockItem(biomeCobblestonePlains, props), Registry.BLOCK.getKey(biomeCobblestonePlains));
		r.accept(new BlockItem(biomeCobblestonePlainsSlab, props), Registry.BLOCK.getKey(biomeCobblestonePlainsSlab));
		r.accept(new BlockItem(biomeCobblestonePlainsStairs, props), Registry.BLOCK.getKey(biomeCobblestonePlainsStairs));
		r.accept(new BlockItem(biomeCobblestonePlainsWall, props), Registry.BLOCK.getKey(biomeCobblestonePlainsWall));
		r.accept(new BlockItem(biomeChiseledBrickPlains, props), Registry.BLOCK.getKey(biomeChiseledBrickPlains));

		r.accept(new BlockItem(biomeStoneMountain, props), Registry.BLOCK.getKey(biomeStoneMountain));
		r.accept(new BlockItem(biomeStoneMountainSlab, props), Registry.BLOCK.getKey(biomeStoneMountainSlab));
		r.accept(new BlockItem(biomeStoneMountainStairs, props), Registry.BLOCK.getKey(biomeStoneMountainStairs));
		r.accept(new BlockItem(biomeBrickMountain, props), Registry.BLOCK.getKey(biomeBrickMountain));
		r.accept(new BlockItem(biomeBrickMountainSlab, props), Registry.BLOCK.getKey(biomeBrickMountainSlab));
		r.accept(new BlockItem(biomeBrickMountainStairs, props), Registry.BLOCK.getKey(biomeBrickMountainStairs));
		r.accept(new BlockItem(biomeBrickMountainWall, props), Registry.BLOCK.getKey(biomeBrickMountainWall));
		r.accept(new BlockItem(biomeCobblestoneMountain, props), Registry.BLOCK.getKey(biomeCobblestoneMountain));
		r.accept(new BlockItem(biomeCobblestoneMountainSlab, props), Registry.BLOCK.getKey(biomeCobblestoneMountainSlab));
		r.accept(new BlockItem(biomeCobblestoneMountainStairs, props), Registry.BLOCK.getKey(biomeCobblestoneMountainStairs));
		r.accept(new BlockItem(biomeCobblestoneMountainWall, props), Registry.BLOCK.getKey(biomeCobblestoneMountainWall));
		r.accept(new BlockItem(biomeChiseledBrickMountain, props), Registry.BLOCK.getKey(biomeChiseledBrickMountain));

		r.accept(new BlockItem(biomeStoneFungal, props), Registry.BLOCK.getKey(biomeStoneFungal));
		r.accept(new BlockItem(biomeStoneFungalSlab, props), Registry.BLOCK.getKey(biomeStoneFungalSlab));
		r.accept(new BlockItem(biomeStoneFungalStairs, props), Registry.BLOCK.getKey(biomeStoneFungalStairs));
		r.accept(new BlockItem(biomeBrickFungal, props), Registry.BLOCK.getKey(biomeBrickFungal));
		r.accept(new BlockItem(biomeBrickFungalSlab, props), Registry.BLOCK.getKey(biomeBrickFungalSlab));
		r.accept(new BlockItem(biomeBrickFungalStairs, props), Registry.BLOCK.getKey(biomeBrickFungalStairs));
		r.accept(new BlockItem(biomeBrickFungalWall, props), Registry.BLOCK.getKey(biomeBrickFungalWall));
		r.accept(new BlockItem(biomeCobblestoneFungal, props), Registry.BLOCK.getKey(biomeCobblestoneFungal));
		r.accept(new BlockItem(biomeCobblestoneFungalSlab, props), Registry.BLOCK.getKey(biomeCobblestoneFungalSlab));
		r.accept(new BlockItem(biomeCobblestoneFungalStairs, props), Registry.BLOCK.getKey(biomeCobblestoneFungalStairs));
		r.accept(new BlockItem(biomeCobblestoneFungalWall, props), Registry.BLOCK.getKey(biomeCobblestoneFungalWall));
		r.accept(new BlockItem(biomeChiseledBrickFungal, props), Registry.BLOCK.getKey(biomeChiseledBrickFungal));

		r.accept(new BlockItem(biomeStoneSwamp, props), Registry.BLOCK.getKey(biomeStoneSwamp));
		r.accept(new BlockItem(biomeStoneSwampSlab, props), Registry.BLOCK.getKey(biomeStoneSwampSlab));
		r.accept(new BlockItem(biomeStoneSwampStairs, props), Registry.BLOCK.getKey(biomeStoneSwampStairs));
		r.accept(new BlockItem(biomeBrickSwamp, props), Registry.BLOCK.getKey(biomeBrickSwamp));
		r.accept(new BlockItem(biomeBrickSwampSlab, props), Registry.BLOCK.getKey(biomeBrickSwampSlab));
		r.accept(new BlockItem(biomeBrickSwampStairs, props), Registry.BLOCK.getKey(biomeBrickSwampStairs));
		r.accept(new BlockItem(biomeBrickSwampWall, props), Registry.BLOCK.getKey(biomeBrickSwampWall));
		r.accept(new BlockItem(biomeCobblestoneSwamp, props), Registry.BLOCK.getKey(biomeCobblestoneSwamp));
		r.accept(new BlockItem(biomeCobblestoneSwampSlab, props), Registry.BLOCK.getKey(biomeCobblestoneSwampSlab));
		r.accept(new BlockItem(biomeCobblestoneSwampStairs, props), Registry.BLOCK.getKey(biomeCobblestoneSwampStairs));
		r.accept(new BlockItem(biomeCobblestoneSwampWall, props), Registry.BLOCK.getKey(biomeCobblestoneSwampWall));
		r.accept(new BlockItem(biomeChiseledBrickSwamp, props), Registry.BLOCK.getKey(biomeChiseledBrickSwamp));

		r.accept(new BlockItem(biomeStoneDesert, props), Registry.BLOCK.getKey(biomeStoneDesert));
		r.accept(new BlockItem(biomeStoneDesertSlab, props), Registry.BLOCK.getKey(biomeStoneDesertSlab));
		r.accept(new BlockItem(biomeStoneDesertStairs, props), Registry.BLOCK.getKey(biomeStoneDesertStairs));
		r.accept(new BlockItem(biomeBrickDesert, props), Registry.BLOCK.getKey(biomeBrickDesert));
		r.accept(new BlockItem(biomeBrickDesertSlab, props), Registry.BLOCK.getKey(biomeBrickDesertSlab));
		r.accept(new BlockItem(biomeBrickDesertStairs, props), Registry.BLOCK.getKey(biomeBrickDesertStairs));
		r.accept(new BlockItem(biomeBrickDesertWall, props), Registry.BLOCK.getKey(biomeBrickDesertWall));
		r.accept(new BlockItem(biomeCobblestoneDesert, props), Registry.BLOCK.getKey(biomeCobblestoneDesert));
		r.accept(new BlockItem(biomeCobblestoneDesertSlab, props), Registry.BLOCK.getKey(biomeCobblestoneDesertSlab));
		r.accept(new BlockItem(biomeCobblestoneDesertStairs, props), Registry.BLOCK.getKey(biomeCobblestoneDesertStairs));
		r.accept(new BlockItem(biomeCobblestoneDesertWall, props), Registry.BLOCK.getKey(biomeCobblestoneDesertWall));
		r.accept(new BlockItem(biomeChiseledBrickDesert, props), Registry.BLOCK.getKey(biomeChiseledBrickDesert));

		r.accept(new BlockItem(biomeStoneTaiga, props), Registry.BLOCK.getKey(biomeStoneTaiga));
		r.accept(new BlockItem(biomeStoneTaigaSlab, props), Registry.BLOCK.getKey(biomeStoneTaigaSlab));
		r.accept(new BlockItem(biomeStoneTaigaStairs, props), Registry.BLOCK.getKey(biomeStoneTaigaStairs));
		r.accept(new BlockItem(biomeBrickTaiga, props), Registry.BLOCK.getKey(biomeBrickTaiga));
		r.accept(new BlockItem(biomeBrickTaigaSlab, props), Registry.BLOCK.getKey(biomeBrickTaigaSlab));
		r.accept(new BlockItem(biomeBrickTaigaStairs, props), Registry.BLOCK.getKey(biomeBrickTaigaStairs));
		r.accept(new BlockItem(biomeBrickTaigaWall, props), Registry.BLOCK.getKey(biomeBrickTaigaWall));
		r.accept(new BlockItem(biomeCobblestoneTaiga, props), Registry.BLOCK.getKey(biomeCobblestoneTaiga));
		r.accept(new BlockItem(biomeCobblestoneTaigaSlab, props), Registry.BLOCK.getKey(biomeCobblestoneTaigaSlab));
		r.accept(new BlockItem(biomeCobblestoneTaigaStairs, props), Registry.BLOCK.getKey(biomeCobblestoneTaigaStairs));
		r.accept(new BlockItem(biomeCobblestoneTaigaWall, props), Registry.BLOCK.getKey(biomeCobblestoneTaigaWall));
		r.accept(new BlockItem(biomeChiseledBrickTaiga, props), Registry.BLOCK.getKey(biomeChiseledBrickTaiga));

		r.accept(new BlockItem(biomeStoneMesa, props), Registry.BLOCK.getKey(biomeStoneMesa));
		r.accept(new BlockItem(biomeStoneMesaSlab, props), Registry.BLOCK.getKey(biomeStoneMesaSlab));
		r.accept(new BlockItem(biomeStoneMesaStairs, props), Registry.BLOCK.getKey(biomeStoneMesaStairs));
		r.accept(new BlockItem(biomeBrickMesa, props), Registry.BLOCK.getKey(biomeBrickMesa));
		r.accept(new BlockItem(biomeBrickMesaSlab, props), Registry.BLOCK.getKey(biomeBrickMesaSlab));
		r.accept(new BlockItem(biomeBrickMesaStairs, props), Registry.BLOCK.getKey(biomeBrickMesaStairs));
		r.accept(new BlockItem(biomeBrickMesaWall, props), Registry.BLOCK.getKey(biomeBrickMesaWall));
		r.accept(new BlockItem(biomeCobblestoneMesa, props), Registry.BLOCK.getKey(biomeCobblestoneMesa));
		r.accept(new BlockItem(biomeCobblestoneMesaSlab, props), Registry.BLOCK.getKey(biomeCobblestoneMesaSlab));
		r.accept(new BlockItem(biomeCobblestoneMesaStairs, props), Registry.BLOCK.getKey(biomeCobblestoneMesaStairs));
		r.accept(new BlockItem(biomeCobblestoneMesaWall, props), Registry.BLOCK.getKey(biomeCobblestoneMesaWall));
		r.accept(new BlockItem(biomeChiseledBrickMesa, props), Registry.BLOCK.getKey(biomeChiseledBrickMesa));

		r.accept(new BlockItem(whitePavement, props), Registry.BLOCK.getKey(whitePavement));
		r.accept(new BlockItem(blackPavement, props), Registry.BLOCK.getKey(blackPavement));
		r.accept(new BlockItem(bluePavement, props), Registry.BLOCK.getKey(bluePavement));
		r.accept(new BlockItem(yellowPavement, props), Registry.BLOCK.getKey(yellowPavement));
		r.accept(new BlockItem(redPavement, props), Registry.BLOCK.getKey(redPavement));
		r.accept(new BlockItem(greenPavement, props), Registry.BLOCK.getKey(greenPavement));

		r.accept(new BlockItem(whitePavementSlab, props), Registry.BLOCK.getKey(whitePavementSlab));
		r.accept(new BlockItem(blackPavementSlab, props), Registry.BLOCK.getKey(blackPavementSlab));
		r.accept(new BlockItem(bluePavementSlab, props), Registry.BLOCK.getKey(bluePavementSlab));
		r.accept(new BlockItem(yellowPavementSlab, props), Registry.BLOCK.getKey(yellowPavementSlab));
		r.accept(new BlockItem(redPavementSlab, props), Registry.BLOCK.getKey(redPavementSlab));
		r.accept(new BlockItem(greenPavementSlab, props), Registry.BLOCK.getKey(greenPavementSlab));

		r.accept(new BlockItem(whitePavementStair, props), Registry.BLOCK.getKey(whitePavementStair));
		r.accept(new BlockItem(blackPavementStair, props), Registry.BLOCK.getKey(blackPavementStair));
		r.accept(new BlockItem(bluePavementStair, props), Registry.BLOCK.getKey(bluePavementStair));
		r.accept(new BlockItem(yellowPavementStair, props), Registry.BLOCK.getKey(yellowPavementStair));
		r.accept(new BlockItem(redPavementStair, props), Registry.BLOCK.getKey(redPavementStair));
		r.accept(new BlockItem(greenPavementStair, props), Registry.BLOCK.getKey(greenPavementStair));

		r.accept(new BlockItem(shimmerrockSlab, props), Registry.BLOCK.getKey(shimmerrockSlab));
		r.accept(new BlockItem(shimmerrockStairs, props), Registry.BLOCK.getKey(shimmerrockStairs));

		r.accept(new BlockItem(shimmerwoodPlankSlab, props), Registry.BLOCK.getKey(shimmerwoodPlankSlab));
		r.accept(new BlockItem(shimmerwoodPlankStairs, props), Registry.BLOCK.getKey(shimmerwoodPlankStairs));

		r.accept(new BlockItem(managlassPane, props), Registry.BLOCK.getKey(managlassPane));
		r.accept(new BlockItem(alfglassPane, props), Registry.BLOCK.getKey(alfglassPane));
		r.accept(new BlockItem(bifrostPane, props), Registry.BLOCK.getKey(bifrostPane));
	}

}
