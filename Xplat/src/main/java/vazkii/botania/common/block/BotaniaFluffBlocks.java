/*
 * This class is distributed as part of the Botania Mod.
 * Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 *
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 */
package vazkii.botania.common.block;

import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvents;
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

import vazkii.botania.common.block.decor.BotaniaDirectionalBlock;
import vazkii.botania.common.block.decor.panes.BotaniaPaneBlock;
import vazkii.botania.common.block.decor.stairs.BotaniaStairBlock;
import vazkii.botania.common.item.BotaniaItems;
import vazkii.botania.common.lib.LibBlockNames;

import java.util.function.BiConsumer;

import static vazkii.botania.common.block.BotaniaBlocks.*;
import static vazkii.botania.common.lib.LibBlockNames.*;
import static vazkii.botania.common.lib.ResourceLocationHelper.prefix;

public final class BotaniaFluffBlocks {
	public static final Block livingwoodStairs = new BotaniaStairBlock(livingwood.defaultBlockState(), BlockBehaviour.Properties.copy(livingwood));
	public static final Block livingwoodSlab = new SlabBlock(BlockBehaviour.Properties.copy(livingwood));
	public static final Block livingwoodWall = new WallBlock(BlockBehaviour.Properties.copy(livingwood));
	public static final Block livingwoodFence = new FenceBlock(BlockBehaviour.Properties.copy(livingwood));
	public static final Block livingwoodFenceGate = new FenceGateBlock(BlockBehaviour.Properties.copy(livingwood), SoundEvents.FENCE_GATE_CLOSE, SoundEvents.FENCE_GATE_OPEN);
	public static final Block livingwoodStrippedStairs = new BotaniaStairBlock(livingwoodStripped.defaultBlockState(), BlockBehaviour.Properties.copy(livingwood));
	public static final Block livingwoodStrippedSlab = new SlabBlock(BlockBehaviour.Properties.copy(livingwood));
	public static final Block livingwoodStrippedWall = new WallBlock(BlockBehaviour.Properties.copy(livingwood));
	public static final Block livingwoodPlankStairs = new BotaniaStairBlock(livingwoodPlanks.defaultBlockState(), BlockBehaviour.Properties.copy(livingwoodPlanks));
	public static final Block livingwoodPlankSlab = new SlabBlock(BlockBehaviour.Properties.copy(livingwoodPlanks));

	public static final Block livingrockStairs = new BotaniaStairBlock(livingrock.defaultBlockState(), BlockBehaviour.Properties.copy(livingrock));
	public static final Block livingrockSlab = new SlabBlock(BlockBehaviour.Properties.copy(livingrock));
	public static final Block livingrockWall = new WallBlock(BlockBehaviour.Properties.copy(livingrock));
	public static final Block livingrockPolishedStairs = new BotaniaStairBlock(livingrock.defaultBlockState(), BlockBehaviour.Properties.copy(livingrockPolished));
	public static final Block livingrockPolishedSlab = new SlabBlock(BlockBehaviour.Properties.copy(livingrockPolished));
	public static final Block livingrockPolishedWall = new WallBlock(BlockBehaviour.Properties.copy(livingrockPolished));
	public static final Block livingrockBrickStairs = new BotaniaStairBlock(livingrockBrick.defaultBlockState(), BlockBehaviour.Properties.copy(livingrockBrick));
	public static final Block livingrockBrickSlab = new SlabBlock(BlockBehaviour.Properties.copy(livingrockBrick));
	public static final Block livingrockBrickWall = new WallBlock(BlockBehaviour.Properties.copy(livingrockBrick));
	public static final Block livingrockBrickMossyStairs = new BotaniaStairBlock(livingrockBrickMossy.defaultBlockState(), BlockBehaviour.Properties.copy(livingrockBrickMossy));
	public static final Block livingrockBrickMossySlab = new SlabBlock(BlockBehaviour.Properties.copy(livingrockBrickMossy));
	public static final Block livingrockBrickMossyWall = new WallBlock(BlockBehaviour.Properties.copy(livingrockBrickMossy));

	public static final Block dreamwoodStairs = new BotaniaStairBlock(dreamwood.defaultBlockState(), BlockBehaviour.Properties.copy(dreamwood));
	public static final Block dreamwoodSlab = new SlabBlock(BlockBehaviour.Properties.copy(dreamwood));
	public static final Block dreamwoodWall = new WallBlock(BlockBehaviour.Properties.copy(dreamwood));
	public static final Block dreamwoodFence = new FenceBlock(BlockBehaviour.Properties.copy(dreamwood));
	public static final Block dreamwoodFenceGate = new FenceGateBlock(BlockBehaviour.Properties.copy(dreamwood), SoundEvents.FENCE_GATE_CLOSE, SoundEvents.FENCE_GATE_OPEN);
	public static final Block dreamwoodStrippedStairs = new BotaniaStairBlock(dreamwoodStripped.defaultBlockState(), BlockBehaviour.Properties.copy(dreamwood));
	public static final Block dreamwoodStrippedSlab = new SlabBlock(BlockBehaviour.Properties.copy(dreamwood));
	public static final Block dreamwoodStrippedWall = new WallBlock(BlockBehaviour.Properties.copy(dreamwood));
	public static final Block dreamwoodPlankStairs = new BotaniaStairBlock(dreamwoodPlanks.defaultBlockState(), BlockBehaviour.Properties.copy(dreamwoodPlanks));
	public static final Block dreamwoodPlankSlab = new SlabBlock(BlockBehaviour.Properties.copy(dreamwoodPlanks));

	public static final Block darkQuartz = new BotaniaBlock(BlockBehaviour.Properties.copy(Blocks.QUARTZ_BLOCK));
	public static final Block darkQuartzChiseled = new BotaniaBlock(BlockBehaviour.Properties.copy(darkQuartz));
	public static final Block darkQuartzPillar = new RotatedPillarBlock(BlockBehaviour.Properties.copy(darkQuartz));
	public static final Block darkQuartzSlab = new SlabBlock(BlockBehaviour.Properties.copy(darkQuartz));
	public static final Block darkQuartzStairs = new BotaniaStairBlock(darkQuartz.defaultBlockState(), BlockBehaviour.Properties.copy(darkQuartz));

	public static final Block manaQuartz = new BotaniaBlock(BlockBehaviour.Properties.copy(darkQuartz));
	public static final Block manaQuartzChiseled = new BotaniaBlock(BlockBehaviour.Properties.copy(darkQuartz));
	public static final Block manaQuartzPillar = new RotatedPillarBlock(BlockBehaviour.Properties.copy(darkQuartz));
	public static final Block manaQuartzSlab = new SlabBlock(BlockBehaviour.Properties.copy(darkQuartz));
	public static final Block manaQuartzStairs = new BotaniaStairBlock(darkQuartz.defaultBlockState(), BlockBehaviour.Properties.copy(darkQuartz));

	public static final Block blazeQuartz = new BotaniaBlock(BlockBehaviour.Properties.copy(darkQuartz));
	public static final Block blazeQuartzChiseled = new BotaniaBlock(BlockBehaviour.Properties.copy(darkQuartz));
	public static final Block blazeQuartzPillar = new RotatedPillarBlock(BlockBehaviour.Properties.copy(darkQuartz));
	public static final Block blazeQuartzSlab = new SlabBlock(BlockBehaviour.Properties.copy(darkQuartz));
	public static final Block blazeQuartzStairs = new BotaniaStairBlock(darkQuartz.defaultBlockState(), BlockBehaviour.Properties.copy(darkQuartz));

	public static final Block lavenderQuartz = new BotaniaBlock(BlockBehaviour.Properties.copy(darkQuartz));
	public static final Block lavenderQuartzChiseled = new BotaniaBlock(BlockBehaviour.Properties.copy(darkQuartz));
	public static final Block lavenderQuartzPillar = new RotatedPillarBlock(BlockBehaviour.Properties.copy(darkQuartz));
	public static final Block lavenderQuartzSlab = new SlabBlock(BlockBehaviour.Properties.copy(darkQuartz));
	public static final Block lavenderQuartzStairs = new BotaniaStairBlock(darkQuartz.defaultBlockState(), BlockBehaviour.Properties.copy(darkQuartz));

	public static final Block redQuartz = new BotaniaBlock(BlockBehaviour.Properties.copy(darkQuartz));
	public static final Block redQuartzChiseled = new BotaniaBlock(BlockBehaviour.Properties.copy(darkQuartz));
	public static final Block redQuartzPillar = new RotatedPillarBlock(BlockBehaviour.Properties.copy(darkQuartz));
	public static final Block redQuartzSlab = new SlabBlock(BlockBehaviour.Properties.copy(darkQuartz));
	public static final Block redQuartzStairs = new BotaniaStairBlock(darkQuartz.defaultBlockState(), BlockBehaviour.Properties.copy(darkQuartz));

	public static final Block elfQuartz = new BotaniaBlock(BlockBehaviour.Properties.copy(darkQuartz));
	public static final Block elfQuartzChiseled = new BotaniaBlock(BlockBehaviour.Properties.copy(darkQuartz));
	public static final Block elfQuartzPillar = new RotatedPillarBlock(BlockBehaviour.Properties.copy(darkQuartz));
	public static final Block elfQuartzSlab = new SlabBlock(BlockBehaviour.Properties.copy(darkQuartz));
	public static final Block elfQuartzStairs = new BotaniaStairBlock(darkQuartz.defaultBlockState(), BlockBehaviour.Properties.copy(darkQuartz));

	public static final Block sunnyQuartz = new BotaniaBlock(BlockBehaviour.Properties.copy(darkQuartz));
	public static final Block sunnyQuartzChiseled = new BotaniaBlock(BlockBehaviour.Properties.copy(darkQuartz));
	public static final Block sunnyQuartzPillar = new RotatedPillarBlock(BlockBehaviour.Properties.copy(darkQuartz));
	public static final Block sunnyQuartzSlab = new SlabBlock(BlockBehaviour.Properties.copy(darkQuartz));
	public static final Block sunnyQuartzStairs = new BotaniaStairBlock(darkQuartz.defaultBlockState(), BlockBehaviour.Properties.copy(darkQuartz));

	public static final Block whitePavement = new BotaniaBlock(BlockBehaviour.Properties.copy(livingrock));
	public static final Block whitePavementStair = new BotaniaStairBlock(whitePavement.defaultBlockState(), BlockBehaviour.Properties.copy(whitePavement));
	public static final Block whitePavementSlab = new SlabBlock(BlockBehaviour.Properties.copy(whitePavement));

	public static final Block blackPavement = new BotaniaBlock(BlockBehaviour.Properties.copy(whitePavement));
	public static final Block blackPavementStair = new BotaniaStairBlock(whitePavement.defaultBlockState(), BlockBehaviour.Properties.copy(whitePavement));
	public static final Block blackPavementSlab = new SlabBlock(BlockBehaviour.Properties.copy(whitePavement));

	public static final Block bluePavement = new BotaniaBlock(BlockBehaviour.Properties.copy(whitePavement));
	public static final Block bluePavementStair = new BotaniaStairBlock(whitePavement.defaultBlockState(), BlockBehaviour.Properties.copy(whitePavement));
	public static final Block bluePavementSlab = new SlabBlock(BlockBehaviour.Properties.copy(whitePavement));

	public static final Block yellowPavement = new BotaniaBlock(BlockBehaviour.Properties.copy(whitePavement));
	public static final Block yellowPavementStair = new BotaniaStairBlock(whitePavement.defaultBlockState(), BlockBehaviour.Properties.copy(whitePavement));
	public static final Block yellowPavementSlab = new SlabBlock(BlockBehaviour.Properties.copy(whitePavement));

	public static final Block redPavement = new BotaniaBlock(BlockBehaviour.Properties.copy(whitePavement));
	public static final Block redPavementStair = new BotaniaStairBlock(whitePavement.defaultBlockState(), BlockBehaviour.Properties.copy(whitePavement));
	public static final Block redPavementSlab = new SlabBlock(BlockBehaviour.Properties.copy(whitePavement));

	public static final Block greenPavement = new BotaniaBlock(BlockBehaviour.Properties.copy(whitePavement));
	public static final Block greenPavementStair = new BotaniaStairBlock(whitePavement.defaultBlockState(), BlockBehaviour.Properties.copy(whitePavement));
	public static final Block greenPavementSlab = new SlabBlock(BlockBehaviour.Properties.copy(whitePavement));

	public static final Block biomeStoneForest = new BotaniaBlock(BlockBehaviour.Properties.of(Material.STONE).strength(1.5F, 10).sound(SoundType.TUFF).requiresCorrectToolForDrops());
	public static final Block biomeStoneForestSlab = new SlabBlock(BlockBehaviour.Properties.copy(biomeStoneForest));
	public static final Block biomeStoneForestStairs = new BotaniaStairBlock(biomeStoneForest.defaultBlockState(), BlockBehaviour.Properties.copy(biomeStoneForest));
	public static final Block biomeStoneForestWall = new WallBlock(BlockBehaviour.Properties.copy(biomeStoneForest));
	public static final Block biomeCobblestoneForest = new BotaniaBlock(BlockBehaviour.Properties.copy(biomeStoneForest));
	public static final Block biomeCobblestoneForestSlab = new SlabBlock(BlockBehaviour.Properties.copy(biomeStoneForest));
	public static final Block biomeCobblestoneForestStairs = new BotaniaStairBlock(biomeStoneForest.defaultBlockState(), BlockBehaviour.Properties.copy(biomeStoneForest));
	public static final Block biomeCobblestoneForestWall = new WallBlock(BlockBehaviour.Properties.copy(biomeStoneForest));
	public static final Block biomeBrickForest = new BotaniaBlock(BlockBehaviour.Properties.copy(biomeStoneForest));
	public static final Block biomeBrickForestSlab = new SlabBlock(BlockBehaviour.Properties.copy(biomeStoneForest));
	public static final Block biomeBrickForestStairs = new BotaniaStairBlock(biomeStoneForest.defaultBlockState(), BlockBehaviour.Properties.copy(biomeStoneForest));
	public static final Block biomeBrickForestWall = new WallBlock(BlockBehaviour.Properties.copy(biomeStoneForest));
	public static final Block biomeChiseledBrickForest = new BotaniaBlock(BlockBehaviour.Properties.copy(biomeStoneForest));

	public static final Block biomeStonePlains = new BotaniaBlock(BlockBehaviour.Properties.copy(biomeStoneForest).sound(SoundType.CALCITE));
	public static final Block biomeStonePlainsSlab = new SlabBlock(BlockBehaviour.Properties.copy(biomeStonePlains));
	public static final Block biomeStonePlainsStairs = new BotaniaStairBlock(biomeStonePlains.defaultBlockState(), BlockBehaviour.Properties.copy(biomeStonePlains));
	public static final Block biomeStonePlainsWall = new WallBlock(BlockBehaviour.Properties.copy(biomeStonePlains));
	public static final Block biomeCobblestonePlains = new BotaniaBlock(BlockBehaviour.Properties.copy(biomeStonePlains));
	public static final Block biomeCobblestonePlainsSlab = new SlabBlock(BlockBehaviour.Properties.copy(biomeStonePlains));
	public static final Block biomeCobblestonePlainsStairs = new BotaniaStairBlock(biomeStonePlains.defaultBlockState(), BlockBehaviour.Properties.copy(biomeStonePlains));
	public static final Block biomeCobblestonePlainsWall = new WallBlock(BlockBehaviour.Properties.copy(biomeStonePlains));
	public static final Block biomeBrickPlains = new RotatedPillarBlock(BlockBehaviour.Properties.copy(biomeStonePlains));
	public static final Block biomeBrickPlainsSlab = new SlabBlock(BlockBehaviour.Properties.copy(biomeStonePlains));
	public static final Block biomeBrickPlainsStairs = new BotaniaStairBlock(biomeStonePlains.defaultBlockState(), BlockBehaviour.Properties.copy(biomeStonePlains));
	public static final Block biomeBrickPlainsWall = new WallBlock(BlockBehaviour.Properties.copy(biomeStonePlains));
	public static final Block biomeChiseledBrickPlains = new BotaniaBlock(BlockBehaviour.Properties.copy(biomeStonePlains));

	public static final Block biomeStoneMountain = new BotaniaBlock(BlockBehaviour.Properties.copy(biomeStoneForest).sound(SoundType.DEEPSLATE_TILES));
	public static final Block biomeStoneMountainSlab = new SlabBlock(BlockBehaviour.Properties.copy(biomeStoneMountain));
	public static final Block biomeStoneMountainStairs = new BotaniaStairBlock(biomeStoneMountain.defaultBlockState(), BlockBehaviour.Properties.copy(biomeStoneMountain));
	public static final Block biomeStoneMountainWall = new WallBlock(BlockBehaviour.Properties.copy(biomeStoneMountain));
	public static final Block biomeCobblestoneMountain = new BotaniaBlock(BlockBehaviour.Properties.copy(biomeStoneMountain));
	public static final Block biomeCobblestoneMountainSlab = new SlabBlock(BlockBehaviour.Properties.copy(biomeStoneMountain));
	public static final Block biomeCobblestoneMountainStairs = new BotaniaStairBlock(biomeStoneMountain.defaultBlockState(), BlockBehaviour.Properties.copy(biomeStoneMountain));
	public static final Block biomeCobblestoneMountainWall = new WallBlock(BlockBehaviour.Properties.copy(biomeStoneMountain));
	public static final Block biomeBrickMountain = new BotaniaBlock(BlockBehaviour.Properties.copy(biomeStoneMountain));
	public static final Block biomeBrickMountainSlab = new SlabBlock(BlockBehaviour.Properties.copy(biomeStoneMountain));
	public static final Block biomeBrickMountainStairs = new BotaniaStairBlock(biomeStoneMountain.defaultBlockState(), BlockBehaviour.Properties.copy(biomeStoneMountain));
	public static final Block biomeBrickMountainWall = new WallBlock(BlockBehaviour.Properties.copy(biomeStoneMountain));
	public static final Block biomeChiseledBrickMountain = new BotaniaBlock(BlockBehaviour.Properties.copy(biomeStoneMountain));

	public static final Block biomeStoneFungal = new BotaniaBlock(BlockBehaviour.Properties.copy(biomeStoneForest).sound(SoundType.DEEPSLATE_BRICKS));
	public static final Block biomeStoneFungalSlab = new SlabBlock(BlockBehaviour.Properties.copy(biomeStoneFungal));
	public static final Block biomeStoneFungalStairs = new BotaniaStairBlock(biomeStoneFungal.defaultBlockState(), BlockBehaviour.Properties.copy(biomeStoneFungal));
	public static final Block biomeStoneFungalWall = new WallBlock(BlockBehaviour.Properties.copy(biomeStoneFungal));
	public static final Block biomeCobblestoneFungal = new BotaniaBlock(BlockBehaviour.Properties.copy(biomeStoneFungal));
	public static final Block biomeCobblestoneFungalSlab = new SlabBlock(BlockBehaviour.Properties.copy(biomeStoneFungal));
	public static final Block biomeCobblestoneFungalStairs = new BotaniaStairBlock(biomeStoneFungal.defaultBlockState(), BlockBehaviour.Properties.copy(biomeStoneFungal));
	public static final Block biomeCobblestoneFungalWall = new WallBlock(BlockBehaviour.Properties.copy(biomeStoneFungal));
	public static final Block biomeBrickFungal = new BotaniaBlock(BlockBehaviour.Properties.copy(biomeStoneFungal));
	public static final Block biomeBrickFungalSlab = new SlabBlock(BlockBehaviour.Properties.copy(biomeStoneFungal));
	public static final Block biomeBrickFungalStairs = new BotaniaStairBlock(biomeStoneFungal.defaultBlockState(), BlockBehaviour.Properties.copy(biomeStoneFungal));
	public static final Block biomeBrickFungalWall = new WallBlock(BlockBehaviour.Properties.copy(biomeStoneFungal));
	public static final Block biomeChiseledBrickFungal = new BotaniaBlock(BlockBehaviour.Properties.copy(biomeStoneFungal));

	public static final Block biomeStoneSwamp = new BotaniaBlock(BlockBehaviour.Properties.copy(biomeStoneForest).sound(SoundType.DEEPSLATE_TILES));
	public static final Block biomeStoneSwampSlab = new SlabBlock(BlockBehaviour.Properties.copy(biomeStoneSwamp));
	public static final Block biomeStoneSwampStairs = new BotaniaStairBlock(biomeStoneSwamp.defaultBlockState(), BlockBehaviour.Properties.copy(biomeStoneSwamp));
	public static final Block biomeStoneSwampWall = new WallBlock(BlockBehaviour.Properties.copy(biomeStoneSwamp));
	public static final Block biomeCobblestoneSwamp = new BotaniaBlock(BlockBehaviour.Properties.copy(biomeStoneSwamp));
	public static final Block biomeCobblestoneSwampSlab = new SlabBlock(BlockBehaviour.Properties.copy(biomeStoneSwamp));
	public static final Block biomeCobblestoneSwampStairs = new BotaniaStairBlock(biomeStoneSwamp.defaultBlockState(), BlockBehaviour.Properties.copy(biomeStoneSwamp));
	public static final Block biomeCobblestoneSwampWall = new WallBlock(BlockBehaviour.Properties.copy(biomeStoneSwamp));
	public static final Block biomeBrickSwamp = new BotaniaDirectionalBlock(BlockBehaviour.Properties.copy(biomeStoneSwamp));
	public static final Block biomeBrickSwampSlab = new SlabBlock(BlockBehaviour.Properties.copy(biomeStoneSwamp));
	public static final Block biomeBrickSwampStairs = new BotaniaStairBlock(biomeStoneSwamp.defaultBlockState(), BlockBehaviour.Properties.copy(biomeStoneSwamp));
	public static final Block biomeBrickSwampWall = new WallBlock(BlockBehaviour.Properties.copy(biomeStoneSwamp));
	public static final Block biomeChiseledBrickSwamp = new BotaniaDirectionalBlock(BlockBehaviour.Properties.copy(biomeStoneSwamp));

	public static final Block biomeStoneDesert = new BotaniaBlock(BlockBehaviour.Properties.copy(biomeStoneForest).sound(SoundType.DEEPSLATE));
	public static final Block biomeStoneDesertSlab = new SlabBlock(BlockBehaviour.Properties.copy(biomeStoneDesert));
	public static final Block biomeStoneDesertStairs = new BotaniaStairBlock(biomeStoneDesert.defaultBlockState(), BlockBehaviour.Properties.copy(biomeStoneDesert));
	public static final Block biomeStoneDesertWall = new WallBlock(BlockBehaviour.Properties.copy(biomeStoneDesert));
	public static final Block biomeCobblestoneDesert = new BotaniaBlock(BlockBehaviour.Properties.copy(biomeStoneDesert));
	public static final Block biomeCobblestoneDesertSlab = new SlabBlock(BlockBehaviour.Properties.copy(biomeStoneDesert));
	public static final Block biomeCobblestoneDesertStairs = new BotaniaStairBlock(biomeStoneDesert.defaultBlockState(), BlockBehaviour.Properties.copy(biomeStoneDesert));
	public static final Block biomeCobblestoneDesertWall = new WallBlock(BlockBehaviour.Properties.copy(biomeStoneDesert));
	public static final Block biomeBrickDesert = new BotaniaBlock(BlockBehaviour.Properties.copy(biomeStoneDesert));
	public static final Block biomeBrickDesertSlab = new SlabBlock(BlockBehaviour.Properties.copy(biomeStoneDesert));
	public static final Block biomeBrickDesertStairs = new BotaniaStairBlock(biomeStoneDesert.defaultBlockState(), BlockBehaviour.Properties.copy(biomeStoneDesert));
	public static final Block biomeBrickDesertWall = new WallBlock(BlockBehaviour.Properties.copy(biomeStoneDesert));
	public static final Block biomeChiseledBrickDesert = new BotaniaBlock(BlockBehaviour.Properties.copy(biomeStoneDesert));

	public static final Block biomeStoneTaiga = new BotaniaBlock(BlockBehaviour.Properties.copy(biomeStoneForest).sound(SoundType.DEEPSLATE));
	public static final Block biomeStoneTaigaSlab = new SlabBlock(BlockBehaviour.Properties.copy(biomeStoneTaiga));
	public static final Block biomeStoneTaigaStairs = new BotaniaStairBlock(biomeStoneTaiga.defaultBlockState(), BlockBehaviour.Properties.copy(biomeStoneTaiga));
	public static final Block biomeStoneTaigaWall = new WallBlock(BlockBehaviour.Properties.copy(biomeStoneTaiga));
	public static final Block biomeCobblestoneTaiga = new BotaniaBlock(BlockBehaviour.Properties.copy(biomeStoneTaiga));
	public static final Block biomeCobblestoneTaigaSlab = new SlabBlock(BlockBehaviour.Properties.copy(biomeStoneTaiga));
	public static final Block biomeCobblestoneTaigaStairs = new BotaniaStairBlock(biomeStoneTaiga.defaultBlockState(), BlockBehaviour.Properties.copy(biomeStoneTaiga));
	public static final Block biomeCobblestoneTaigaWall = new WallBlock(BlockBehaviour.Properties.copy(biomeStoneTaiga));
	public static final Block biomeBrickTaiga = new BotaniaBlock(BlockBehaviour.Properties.copy(biomeStoneTaiga));
	public static final Block biomeBrickTaigaSlab = new SlabBlock(BlockBehaviour.Properties.copy(biomeStoneTaiga));
	public static final Block biomeBrickTaigaStairs = new BotaniaStairBlock(biomeStoneTaiga.defaultBlockState(), BlockBehaviour.Properties.copy(biomeStoneTaiga));
	public static final Block biomeBrickTaigaWall = new WallBlock(BlockBehaviour.Properties.copy(biomeStoneTaiga));
	public static final Block biomeChiseledBrickTaiga = new BotaniaBlock(BlockBehaviour.Properties.copy(biomeStoneTaiga));

	public static final Block biomeStoneMesa = new BotaniaBlock(BlockBehaviour.Properties.copy(biomeStoneForest).sound(SoundType.CALCITE));
	public static final Block biomeStoneMesaSlab = new SlabBlock(BlockBehaviour.Properties.copy(biomeStoneMesa));
	public static final Block biomeStoneMesaStairs = new BotaniaStairBlock(biomeStoneMesa.defaultBlockState(), BlockBehaviour.Properties.copy(biomeStoneMesa));
	public static final Block biomeStoneMesaWall = new WallBlock(BlockBehaviour.Properties.copy(biomeStoneMesa));
	public static final Block biomeCobblestoneMesa = new BotaniaBlock(BlockBehaviour.Properties.copy(biomeStoneMesa));
	public static final Block biomeCobblestoneMesaSlab = new SlabBlock(BlockBehaviour.Properties.copy(biomeStoneMesa));
	public static final Block biomeCobblestoneMesaStairs = new BotaniaStairBlock(biomeStoneMesa.defaultBlockState(), BlockBehaviour.Properties.copy(biomeStoneMesa));
	public static final Block biomeCobblestoneMesaWall = new WallBlock(BlockBehaviour.Properties.copy(biomeStoneMesa));
	public static final Block biomeBrickMesa = new BotaniaBlock(BlockBehaviour.Properties.copy(biomeStoneMesa));
	public static final Block biomeBrickMesaSlab = new SlabBlock(BlockBehaviour.Properties.copy(biomeStoneMesa));
	public static final Block biomeBrickMesaStairs = new BotaniaStairBlock(biomeStoneMesa.defaultBlockState(), BlockBehaviour.Properties.copy(biomeStoneMesa));
	public static final Block biomeBrickMesaWall = new WallBlock(BlockBehaviour.Properties.copy(biomeStoneMesa));
	public static final Block biomeChiseledBrickMesa = new RotatedPillarBlock(BlockBehaviour.Properties.copy(biomeStoneMesa));

	public static final Block shimmerrockSlab = new SlabBlock(BlockBehaviour.Properties.copy(shimmerrock));
	public static final Block shimmerrockStairs = new BotaniaStairBlock(shimmerrock.defaultBlockState(), BlockBehaviour.Properties.copy(shimmerrock));

	public static final Block shimmerwoodPlankSlab = new SlabBlock(BlockBehaviour.Properties.copy(shimmerwoodPlanks));
	public static final Block shimmerwoodPlankStairs = new BotaniaStairBlock(shimmerwoodPlanks.defaultBlockState(), BlockBehaviour.Properties.copy(shimmerwoodPlanks));

	public static final Block managlassPane = new BotaniaPaneBlock(BlockBehaviour.Properties.copy(manaGlass));
	public static final Block alfglassPane = new BotaniaPaneBlock(BlockBehaviour.Properties.copy(elfGlass));
	public static final Block bifrostPane = new BotaniaPaneBlock(BlockBehaviour.Properties.copy(bifrostPerm));

	public static void registerBlocks(BiConsumer<Block, ResourceLocation> r) {

		r.accept(livingwoodStairs, prefix(LibBlockNames.LIVING_WOOD + STAIR_SUFFIX));
		r.accept(livingwoodSlab, prefix(LibBlockNames.LIVING_WOOD + SLAB_SUFFIX));
		r.accept(livingwoodWall, prefix(LibBlockNames.LIVING_WOOD + WALL_SUFFIX));
		r.accept(livingwoodStrippedStairs, prefix(LibBlockNames.LIVING_WOOD_STRIPPED + STAIR_SUFFIX));
		r.accept(livingwoodStrippedSlab, prefix(LibBlockNames.LIVING_WOOD_STRIPPED + SLAB_SUFFIX));
		r.accept(livingwoodStrippedWall, prefix(LibBlockNames.LIVING_WOOD_STRIPPED + WALL_SUFFIX));
		r.accept(livingwoodPlankStairs, prefix(LibBlockNames.LIVING_WOOD_PLANKS + STAIR_SUFFIX));
		r.accept(livingwoodPlankSlab, prefix(LibBlockNames.LIVING_WOOD_PLANKS + SLAB_SUFFIX));
		r.accept(livingwoodFence, prefix(LibBlockNames.LIVING_WOOD + FENCE_SUFFIX));
		r.accept(livingwoodFenceGate, prefix(LibBlockNames.LIVING_WOOD + FENCE_GATE_SUFFIX));

		r.accept(livingrockStairs, prefix(LibBlockNames.LIVING_ROCK + STAIR_SUFFIX));
		r.accept(livingrockSlab, prefix(LibBlockNames.LIVING_ROCK + SLAB_SUFFIX));
		r.accept(livingrockWall, prefix(LibBlockNames.LIVING_ROCK + WALL_SUFFIX));

		r.accept(livingrockPolishedStairs, prefix(LibBlockNames.LIVING_ROCK_POLISHED + STAIR_SUFFIX));
		r.accept(livingrockPolishedSlab, prefix(LibBlockNames.LIVING_ROCK_POLISHED + SLAB_SUFFIX));
		r.accept(livingrockPolishedWall, prefix(LibBlockNames.LIVING_ROCK_POLISHED + WALL_SUFFIX));

		r.accept(livingrockBrickStairs, prefix(LibBlockNames.LIVING_ROCK_BRICK + STAIR_SUFFIX));
		r.accept(livingrockBrickSlab, prefix(LibBlockNames.LIVING_ROCK_BRICK + SLAB_SUFFIX));
		r.accept(livingrockBrickWall, prefix(LibBlockNames.LIVING_ROCK_BRICK + WALL_SUFFIX));

		r.accept(livingrockBrickMossyStairs, prefix(LibBlockNames.LIVING_ROCK_BRICK_MOSSY + STAIR_SUFFIX));
		r.accept(livingrockBrickMossySlab, prefix(LibBlockNames.LIVING_ROCK_BRICK_MOSSY + SLAB_SUFFIX));
		r.accept(livingrockBrickMossyWall, prefix(LibBlockNames.LIVING_ROCK_BRICK_MOSSY + WALL_SUFFIX));

		r.accept(dreamwoodStairs, prefix(LibBlockNames.DREAM_WOOD + STAIR_SUFFIX));
		r.accept(dreamwoodSlab, prefix(LibBlockNames.DREAM_WOOD + SLAB_SUFFIX));
		r.accept(dreamwoodWall, prefix(LibBlockNames.DREAM_WOOD + WALL_SUFFIX));
		r.accept(dreamwoodStrippedStairs, prefix(LibBlockNames.DREAM_WOOD_STRIPPED + STAIR_SUFFIX));
		r.accept(dreamwoodStrippedSlab, prefix(LibBlockNames.DREAM_WOOD_STRIPPED + SLAB_SUFFIX));
		r.accept(dreamwoodStrippedWall, prefix(LibBlockNames.DREAM_WOOD_STRIPPED + WALL_SUFFIX));
		r.accept(dreamwoodPlankStairs, prefix(LibBlockNames.DREAM_WOOD_PLANKS + STAIR_SUFFIX));
		r.accept(dreamwoodPlankSlab, prefix(LibBlockNames.DREAM_WOOD_PLANKS + SLAB_SUFFIX));
		r.accept(dreamwoodFence, prefix(LibBlockNames.DREAM_WOOD + FENCE_SUFFIX));
		r.accept(dreamwoodFenceGate, prefix(LibBlockNames.DREAM_WOOD + FENCE_GATE_SUFFIX));

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
		r.accept(biomeStoneForestWall, prefix(METAMORPHIC_PREFIX + "forest_stone" + WALL_SUFFIX));
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
		r.accept(biomeStonePlainsWall, prefix(METAMORPHIC_PREFIX + "plains_stone" + WALL_SUFFIX));
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
		r.accept(biomeStoneMountainWall, prefix(METAMORPHIC_PREFIX + "mountain_stone" + WALL_SUFFIX));
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
		r.accept(biomeStoneFungalWall, prefix(METAMORPHIC_PREFIX + "fungal_stone" + WALL_SUFFIX));
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
		r.accept(biomeStoneSwampWall, prefix(METAMORPHIC_PREFIX + "swamp_stone" + WALL_SUFFIX));
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
		r.accept(biomeStoneDesertWall, prefix(METAMORPHIC_PREFIX + "desert_stone" + WALL_SUFFIX));
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
		r.accept(biomeStoneTaigaWall, prefix(METAMORPHIC_PREFIX + "taiga_stone" + WALL_SUFFIX));
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
		r.accept(biomeStoneMesaWall, prefix(METAMORPHIC_PREFIX + "mesa_stone" + WALL_SUFFIX));
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
		Item.Properties props = BotaniaItems.defaultBuilder();

		r.accept(new BlockItem(livingwoodStairs, props), BuiltInRegistries.BLOCK.getKey(livingwoodStairs));
		r.accept(new BlockItem(livingwoodSlab, props), BuiltInRegistries.BLOCK.getKey(livingwoodSlab));
		r.accept(new BlockItem(livingwoodWall, props), BuiltInRegistries.BLOCK.getKey(livingwoodWall));
		r.accept(new BlockItem(livingwoodFence, props), BuiltInRegistries.BLOCK.getKey(livingwoodFence));
		r.accept(new BlockItem(livingwoodFenceGate, props), BuiltInRegistries.BLOCK.getKey(livingwoodFenceGate));

		r.accept(new BlockItem(livingwoodStrippedStairs, props), BuiltInRegistries.BLOCK.getKey(livingwoodStrippedStairs));
		r.accept(new BlockItem(livingwoodStrippedSlab, props), BuiltInRegistries.BLOCK.getKey(livingwoodStrippedSlab));
		r.accept(new BlockItem(livingwoodStrippedWall, props), BuiltInRegistries.BLOCK.getKey(livingwoodStrippedWall));

		r.accept(new BlockItem(livingwoodPlankStairs, props), BuiltInRegistries.BLOCK.getKey(livingwoodPlankStairs));
		r.accept(new BlockItem(livingwoodPlankSlab, props), BuiltInRegistries.BLOCK.getKey(livingwoodPlankSlab));

		r.accept(new BlockItem(livingrockStairs, props), BuiltInRegistries.BLOCK.getKey(livingrockStairs));
		r.accept(new BlockItem(livingrockSlab, props), BuiltInRegistries.BLOCK.getKey(livingrockSlab));
		r.accept(new BlockItem(livingrockWall, props), BuiltInRegistries.BLOCK.getKey(livingrockWall));

		r.accept(new BlockItem(livingrockPolishedStairs, props), BuiltInRegistries.BLOCK.getKey(livingrockPolishedStairs));
		r.accept(new BlockItem(livingrockPolishedSlab, props), BuiltInRegistries.BLOCK.getKey(livingrockPolishedSlab));
		r.accept(new BlockItem(livingrockPolishedWall, props), BuiltInRegistries.BLOCK.getKey(livingrockPolishedWall));

		r.accept(new BlockItem(livingrockBrickStairs, props), BuiltInRegistries.BLOCK.getKey(livingrockBrickStairs));
		r.accept(new BlockItem(livingrockBrickSlab, props), BuiltInRegistries.BLOCK.getKey(livingrockBrickSlab));
		r.accept(new BlockItem(livingrockBrickWall, props), BuiltInRegistries.BLOCK.getKey(livingrockBrickWall));

		r.accept(new BlockItem(livingrockBrickMossyStairs, props), BuiltInRegistries.BLOCK.getKey(livingrockBrickMossyStairs));
		r.accept(new BlockItem(livingrockBrickMossySlab, props), BuiltInRegistries.BLOCK.getKey(livingrockBrickMossySlab));
		r.accept(new BlockItem(livingrockBrickMossyWall, props), BuiltInRegistries.BLOCK.getKey(livingrockBrickMossyWall));

		r.accept(new BlockItem(dreamwoodStairs, props), BuiltInRegistries.BLOCK.getKey(dreamwoodStairs));
		r.accept(new BlockItem(dreamwoodSlab, props), BuiltInRegistries.BLOCK.getKey(dreamwoodSlab));
		r.accept(new BlockItem(dreamwoodWall, props), BuiltInRegistries.BLOCK.getKey(dreamwoodWall));
		r.accept(new BlockItem(dreamwoodFence, props), BuiltInRegistries.BLOCK.getKey(dreamwoodFence));
		r.accept(new BlockItem(dreamwoodFenceGate, props), BuiltInRegistries.BLOCK.getKey(dreamwoodFenceGate));

		r.accept(new BlockItem(dreamwoodStrippedStairs, props), BuiltInRegistries.BLOCK.getKey(dreamwoodStrippedStairs));
		r.accept(new BlockItem(dreamwoodStrippedSlab, props), BuiltInRegistries.BLOCK.getKey(dreamwoodStrippedSlab));
		r.accept(new BlockItem(dreamwoodStrippedWall, props), BuiltInRegistries.BLOCK.getKey(dreamwoodStrippedWall));

		r.accept(new BlockItem(dreamwoodPlankStairs, props), BuiltInRegistries.BLOCK.getKey(dreamwoodPlankStairs));
		r.accept(new BlockItem(dreamwoodPlankSlab, props), BuiltInRegistries.BLOCK.getKey(dreamwoodPlankSlab));

		r.accept(new BlockItem(darkQuartz, props), BuiltInRegistries.BLOCK.getKey(darkQuartz));
		r.accept(new BlockItem(darkQuartzPillar, props), BuiltInRegistries.BLOCK.getKey(darkQuartzPillar));
		r.accept(new BlockItem(darkQuartzChiseled, props), BuiltInRegistries.BLOCK.getKey(darkQuartzChiseled));
		r.accept(new BlockItem(darkQuartzSlab, props), BuiltInRegistries.BLOCK.getKey(darkQuartzSlab));
		r.accept(new BlockItem(darkQuartzStairs, props), BuiltInRegistries.BLOCK.getKey(darkQuartzStairs));

		r.accept(new BlockItem(manaQuartz, props), BuiltInRegistries.BLOCK.getKey(manaQuartz));
		r.accept(new BlockItem(manaQuartzPillar, props), BuiltInRegistries.BLOCK.getKey(manaQuartzPillar));
		r.accept(new BlockItem(manaQuartzChiseled, props), BuiltInRegistries.BLOCK.getKey(manaQuartzChiseled));
		r.accept(new BlockItem(manaQuartzSlab, props), BuiltInRegistries.BLOCK.getKey(manaQuartzSlab));
		r.accept(new BlockItem(manaQuartzStairs, props), BuiltInRegistries.BLOCK.getKey(manaQuartzStairs));

		r.accept(new BlockItem(blazeQuartz, props), BuiltInRegistries.BLOCK.getKey(blazeQuartz));
		r.accept(new BlockItem(blazeQuartzPillar, props), BuiltInRegistries.BLOCK.getKey(blazeQuartzPillar));
		r.accept(new BlockItem(blazeQuartzChiseled, props), BuiltInRegistries.BLOCK.getKey(blazeQuartzChiseled));
		r.accept(new BlockItem(blazeQuartzSlab, props), BuiltInRegistries.BLOCK.getKey(blazeQuartzSlab));
		r.accept(new BlockItem(blazeQuartzStairs, props), BuiltInRegistries.BLOCK.getKey(blazeQuartzStairs));

		r.accept(new BlockItem(lavenderQuartz, props), BuiltInRegistries.BLOCK.getKey(lavenderQuartz));
		r.accept(new BlockItem(lavenderQuartzPillar, props), BuiltInRegistries.BLOCK.getKey(lavenderQuartzPillar));
		r.accept(new BlockItem(lavenderQuartzChiseled, props), BuiltInRegistries.BLOCK.getKey(lavenderQuartzChiseled));
		r.accept(new BlockItem(lavenderQuartzSlab, props), BuiltInRegistries.BLOCK.getKey(lavenderQuartzSlab));
		r.accept(new BlockItem(lavenderQuartzStairs, props), BuiltInRegistries.BLOCK.getKey(lavenderQuartzStairs));

		r.accept(new BlockItem(redQuartz, props), BuiltInRegistries.BLOCK.getKey(redQuartz));
		r.accept(new BlockItem(redQuartzPillar, props), BuiltInRegistries.BLOCK.getKey(redQuartzPillar));
		r.accept(new BlockItem(redQuartzChiseled, props), BuiltInRegistries.BLOCK.getKey(redQuartzChiseled));
		r.accept(new BlockItem(redQuartzSlab, props), BuiltInRegistries.BLOCK.getKey(redQuartzSlab));
		r.accept(new BlockItem(redQuartzStairs, props), BuiltInRegistries.BLOCK.getKey(redQuartzStairs));

		r.accept(new BlockItem(elfQuartz, props), BuiltInRegistries.BLOCK.getKey(elfQuartz));
		r.accept(new BlockItem(elfQuartzPillar, props), BuiltInRegistries.BLOCK.getKey(elfQuartzPillar));
		r.accept(new BlockItem(elfQuartzChiseled, props), BuiltInRegistries.BLOCK.getKey(elfQuartzChiseled));
		r.accept(new BlockItem(elfQuartzSlab, props), BuiltInRegistries.BLOCK.getKey(elfQuartzSlab));
		r.accept(new BlockItem(elfQuartzStairs, props), BuiltInRegistries.BLOCK.getKey(elfQuartzStairs));

		r.accept(new BlockItem(sunnyQuartz, props), BuiltInRegistries.BLOCK.getKey(sunnyQuartz));
		r.accept(new BlockItem(sunnyQuartzPillar, props), BuiltInRegistries.BLOCK.getKey(sunnyQuartzPillar));
		r.accept(new BlockItem(sunnyQuartzChiseled, props), BuiltInRegistries.BLOCK.getKey(sunnyQuartzChiseled));
		r.accept(new BlockItem(sunnyQuartzSlab, props), BuiltInRegistries.BLOCK.getKey(sunnyQuartzSlab));
		r.accept(new BlockItem(sunnyQuartzStairs, props), BuiltInRegistries.BLOCK.getKey(sunnyQuartzStairs));

		r.accept(new BlockItem(biomeStoneForest, props), BuiltInRegistries.BLOCK.getKey(biomeStoneForest));
		r.accept(new BlockItem(biomeStoneForestSlab, props), BuiltInRegistries.BLOCK.getKey(biomeStoneForestSlab));
		r.accept(new BlockItem(biomeStoneForestStairs, props), BuiltInRegistries.BLOCK.getKey(biomeStoneForestStairs));
		r.accept(new BlockItem(biomeStoneForestWall, props), BuiltInRegistries.BLOCK.getKey(biomeStoneForestWall));
		r.accept(new BlockItem(biomeBrickForest, props), BuiltInRegistries.BLOCK.getKey(biomeBrickForest));
		r.accept(new BlockItem(biomeBrickForestSlab, props), BuiltInRegistries.BLOCK.getKey(biomeBrickForestSlab));
		r.accept(new BlockItem(biomeBrickForestStairs, props), BuiltInRegistries.BLOCK.getKey(biomeBrickForestStairs));
		r.accept(new BlockItem(biomeBrickForestWall, props), BuiltInRegistries.BLOCK.getKey(biomeBrickForestWall));
		r.accept(new BlockItem(biomeCobblestoneForest, props), BuiltInRegistries.BLOCK.getKey(biomeCobblestoneForest));
		r.accept(new BlockItem(biomeCobblestoneForestSlab, props), BuiltInRegistries.BLOCK.getKey(biomeCobblestoneForestSlab));
		r.accept(new BlockItem(biomeCobblestoneForestStairs, props), BuiltInRegistries.BLOCK.getKey(biomeCobblestoneForestStairs));
		r.accept(new BlockItem(biomeCobblestoneForestWall, props), BuiltInRegistries.BLOCK.getKey(biomeCobblestoneForestWall));
		r.accept(new BlockItem(biomeChiseledBrickForest, props), BuiltInRegistries.BLOCK.getKey(biomeChiseledBrickForest));

		r.accept(new BlockItem(biomeStonePlains, props), BuiltInRegistries.BLOCK.getKey(biomeStonePlains));
		r.accept(new BlockItem(biomeStonePlainsSlab, props), BuiltInRegistries.BLOCK.getKey(biomeStonePlainsSlab));
		r.accept(new BlockItem(biomeStonePlainsStairs, props), BuiltInRegistries.BLOCK.getKey(biomeStonePlainsStairs));
		r.accept(new BlockItem(biomeStonePlainsWall, props), BuiltInRegistries.BLOCK.getKey(biomeStonePlainsWall));
		r.accept(new BlockItem(biomeBrickPlains, props), BuiltInRegistries.BLOCK.getKey(biomeBrickPlains));
		r.accept(new BlockItem(biomeBrickPlainsSlab, props), BuiltInRegistries.BLOCK.getKey(biomeBrickPlainsSlab));
		r.accept(new BlockItem(biomeBrickPlainsStairs, props), BuiltInRegistries.BLOCK.getKey(biomeBrickPlainsStairs));
		r.accept(new BlockItem(biomeBrickPlainsWall, props), BuiltInRegistries.BLOCK.getKey(biomeBrickPlainsWall));
		r.accept(new BlockItem(biomeCobblestonePlains, props), BuiltInRegistries.BLOCK.getKey(biomeCobblestonePlains));
		r.accept(new BlockItem(biomeCobblestonePlainsSlab, props), BuiltInRegistries.BLOCK.getKey(biomeCobblestonePlainsSlab));
		r.accept(new BlockItem(biomeCobblestonePlainsStairs, props), BuiltInRegistries.BLOCK.getKey(biomeCobblestonePlainsStairs));
		r.accept(new BlockItem(biomeCobblestonePlainsWall, props), BuiltInRegistries.BLOCK.getKey(biomeCobblestonePlainsWall));
		r.accept(new BlockItem(biomeChiseledBrickPlains, props), BuiltInRegistries.BLOCK.getKey(biomeChiseledBrickPlains));

		r.accept(new BlockItem(biomeStoneMountain, props), BuiltInRegistries.BLOCK.getKey(biomeStoneMountain));
		r.accept(new BlockItem(biomeStoneMountainSlab, props), BuiltInRegistries.BLOCK.getKey(biomeStoneMountainSlab));
		r.accept(new BlockItem(biomeStoneMountainStairs, props), BuiltInRegistries.BLOCK.getKey(biomeStoneMountainStairs));
		r.accept(new BlockItem(biomeStoneMountainWall, props), BuiltInRegistries.BLOCK.getKey(biomeStoneMountainWall));
		r.accept(new BlockItem(biomeBrickMountain, props), BuiltInRegistries.BLOCK.getKey(biomeBrickMountain));
		r.accept(new BlockItem(biomeBrickMountainSlab, props), BuiltInRegistries.BLOCK.getKey(biomeBrickMountainSlab));
		r.accept(new BlockItem(biomeBrickMountainStairs, props), BuiltInRegistries.BLOCK.getKey(biomeBrickMountainStairs));
		r.accept(new BlockItem(biomeBrickMountainWall, props), BuiltInRegistries.BLOCK.getKey(biomeBrickMountainWall));
		r.accept(new BlockItem(biomeCobblestoneMountain, props), BuiltInRegistries.BLOCK.getKey(biomeCobblestoneMountain));
		r.accept(new BlockItem(biomeCobblestoneMountainSlab, props), BuiltInRegistries.BLOCK.getKey(biomeCobblestoneMountainSlab));
		r.accept(new BlockItem(biomeCobblestoneMountainStairs, props), BuiltInRegistries.BLOCK.getKey(biomeCobblestoneMountainStairs));
		r.accept(new BlockItem(biomeCobblestoneMountainWall, props), BuiltInRegistries.BLOCK.getKey(biomeCobblestoneMountainWall));
		r.accept(new BlockItem(biomeChiseledBrickMountain, props), BuiltInRegistries.BLOCK.getKey(biomeChiseledBrickMountain));

		r.accept(new BlockItem(biomeStoneFungal, props), BuiltInRegistries.BLOCK.getKey(biomeStoneFungal));
		r.accept(new BlockItem(biomeStoneFungalSlab, props), BuiltInRegistries.BLOCK.getKey(biomeStoneFungalSlab));
		r.accept(new BlockItem(biomeStoneFungalStairs, props), BuiltInRegistries.BLOCK.getKey(biomeStoneFungalStairs));
		r.accept(new BlockItem(biomeStoneFungalWall, props), BuiltInRegistries.BLOCK.getKey(biomeStoneFungalWall));
		r.accept(new BlockItem(biomeBrickFungal, props), BuiltInRegistries.BLOCK.getKey(biomeBrickFungal));
		r.accept(new BlockItem(biomeBrickFungalSlab, props), BuiltInRegistries.BLOCK.getKey(biomeBrickFungalSlab));
		r.accept(new BlockItem(biomeBrickFungalStairs, props), BuiltInRegistries.BLOCK.getKey(biomeBrickFungalStairs));
		r.accept(new BlockItem(biomeBrickFungalWall, props), BuiltInRegistries.BLOCK.getKey(biomeBrickFungalWall));
		r.accept(new BlockItem(biomeCobblestoneFungal, props), BuiltInRegistries.BLOCK.getKey(biomeCobblestoneFungal));
		r.accept(new BlockItem(biomeCobblestoneFungalSlab, props), BuiltInRegistries.BLOCK.getKey(biomeCobblestoneFungalSlab));
		r.accept(new BlockItem(biomeCobblestoneFungalStairs, props), BuiltInRegistries.BLOCK.getKey(biomeCobblestoneFungalStairs));
		r.accept(new BlockItem(biomeCobblestoneFungalWall, props), BuiltInRegistries.BLOCK.getKey(biomeCobblestoneFungalWall));
		r.accept(new BlockItem(biomeChiseledBrickFungal, props), BuiltInRegistries.BLOCK.getKey(biomeChiseledBrickFungal));

		r.accept(new BlockItem(biomeStoneSwamp, props), BuiltInRegistries.BLOCK.getKey(biomeStoneSwamp));
		r.accept(new BlockItem(biomeStoneSwampSlab, props), BuiltInRegistries.BLOCK.getKey(biomeStoneSwampSlab));
		r.accept(new BlockItem(biomeStoneSwampStairs, props), BuiltInRegistries.BLOCK.getKey(biomeStoneSwampStairs));
		r.accept(new BlockItem(biomeStoneSwampWall, props), BuiltInRegistries.BLOCK.getKey(biomeStoneSwampWall));
		r.accept(new BlockItem(biomeBrickSwamp, props), BuiltInRegistries.BLOCK.getKey(biomeBrickSwamp));
		r.accept(new BlockItem(biomeBrickSwampSlab, props), BuiltInRegistries.BLOCK.getKey(biomeBrickSwampSlab));
		r.accept(new BlockItem(biomeBrickSwampStairs, props), BuiltInRegistries.BLOCK.getKey(biomeBrickSwampStairs));
		r.accept(new BlockItem(biomeBrickSwampWall, props), BuiltInRegistries.BLOCK.getKey(biomeBrickSwampWall));
		r.accept(new BlockItem(biomeCobblestoneSwamp, props), BuiltInRegistries.BLOCK.getKey(biomeCobblestoneSwamp));
		r.accept(new BlockItem(biomeCobblestoneSwampSlab, props), BuiltInRegistries.BLOCK.getKey(biomeCobblestoneSwampSlab));
		r.accept(new BlockItem(biomeCobblestoneSwampStairs, props), BuiltInRegistries.BLOCK.getKey(biomeCobblestoneSwampStairs));
		r.accept(new BlockItem(biomeCobblestoneSwampWall, props), BuiltInRegistries.BLOCK.getKey(biomeCobblestoneSwampWall));
		r.accept(new BlockItem(biomeChiseledBrickSwamp, props), BuiltInRegistries.BLOCK.getKey(biomeChiseledBrickSwamp));

		r.accept(new BlockItem(biomeStoneDesert, props), BuiltInRegistries.BLOCK.getKey(biomeStoneDesert));
		r.accept(new BlockItem(biomeStoneDesertSlab, props), BuiltInRegistries.BLOCK.getKey(biomeStoneDesertSlab));
		r.accept(new BlockItem(biomeStoneDesertStairs, props), BuiltInRegistries.BLOCK.getKey(biomeStoneDesertStairs));
		r.accept(new BlockItem(biomeStoneDesertWall, props), BuiltInRegistries.BLOCK.getKey(biomeStoneDesertWall));
		r.accept(new BlockItem(biomeBrickDesert, props), BuiltInRegistries.BLOCK.getKey(biomeBrickDesert));
		r.accept(new BlockItem(biomeBrickDesertSlab, props), BuiltInRegistries.BLOCK.getKey(biomeBrickDesertSlab));
		r.accept(new BlockItem(biomeBrickDesertStairs, props), BuiltInRegistries.BLOCK.getKey(biomeBrickDesertStairs));
		r.accept(new BlockItem(biomeBrickDesertWall, props), BuiltInRegistries.BLOCK.getKey(biomeBrickDesertWall));
		r.accept(new BlockItem(biomeCobblestoneDesert, props), BuiltInRegistries.BLOCK.getKey(biomeCobblestoneDesert));
		r.accept(new BlockItem(biomeCobblestoneDesertSlab, props), BuiltInRegistries.BLOCK.getKey(biomeCobblestoneDesertSlab));
		r.accept(new BlockItem(biomeCobblestoneDesertStairs, props), BuiltInRegistries.BLOCK.getKey(biomeCobblestoneDesertStairs));
		r.accept(new BlockItem(biomeCobblestoneDesertWall, props), BuiltInRegistries.BLOCK.getKey(biomeCobblestoneDesertWall));
		r.accept(new BlockItem(biomeChiseledBrickDesert, props), BuiltInRegistries.BLOCK.getKey(biomeChiseledBrickDesert));

		r.accept(new BlockItem(biomeStoneTaiga, props), BuiltInRegistries.BLOCK.getKey(biomeStoneTaiga));
		r.accept(new BlockItem(biomeStoneTaigaSlab, props), BuiltInRegistries.BLOCK.getKey(biomeStoneTaigaSlab));
		r.accept(new BlockItem(biomeStoneTaigaStairs, props), BuiltInRegistries.BLOCK.getKey(biomeStoneTaigaStairs));
		r.accept(new BlockItem(biomeStoneTaigaWall, props), BuiltInRegistries.BLOCK.getKey(biomeStoneTaigaWall));
		r.accept(new BlockItem(biomeBrickTaiga, props), BuiltInRegistries.BLOCK.getKey(biomeBrickTaiga));
		r.accept(new BlockItem(biomeBrickTaigaSlab, props), BuiltInRegistries.BLOCK.getKey(biomeBrickTaigaSlab));
		r.accept(new BlockItem(biomeBrickTaigaStairs, props), BuiltInRegistries.BLOCK.getKey(biomeBrickTaigaStairs));
		r.accept(new BlockItem(biomeBrickTaigaWall, props), BuiltInRegistries.BLOCK.getKey(biomeBrickTaigaWall));
		r.accept(new BlockItem(biomeCobblestoneTaiga, props), BuiltInRegistries.BLOCK.getKey(biomeCobblestoneTaiga));
		r.accept(new BlockItem(biomeCobblestoneTaigaSlab, props), BuiltInRegistries.BLOCK.getKey(biomeCobblestoneTaigaSlab));
		r.accept(new BlockItem(biomeCobblestoneTaigaStairs, props), BuiltInRegistries.BLOCK.getKey(biomeCobblestoneTaigaStairs));
		r.accept(new BlockItem(biomeCobblestoneTaigaWall, props), BuiltInRegistries.BLOCK.getKey(biomeCobblestoneTaigaWall));
		r.accept(new BlockItem(biomeChiseledBrickTaiga, props), BuiltInRegistries.BLOCK.getKey(biomeChiseledBrickTaiga));

		r.accept(new BlockItem(biomeStoneMesa, props), BuiltInRegistries.BLOCK.getKey(biomeStoneMesa));
		r.accept(new BlockItem(biomeStoneMesaSlab, props), BuiltInRegistries.BLOCK.getKey(biomeStoneMesaSlab));
		r.accept(new BlockItem(biomeStoneMesaStairs, props), BuiltInRegistries.BLOCK.getKey(biomeStoneMesaStairs));
		r.accept(new BlockItem(biomeStoneMesaWall, props), BuiltInRegistries.BLOCK.getKey(biomeStoneMesaWall));
		r.accept(new BlockItem(biomeBrickMesa, props), BuiltInRegistries.BLOCK.getKey(biomeBrickMesa));
		r.accept(new BlockItem(biomeBrickMesaSlab, props), BuiltInRegistries.BLOCK.getKey(biomeBrickMesaSlab));
		r.accept(new BlockItem(biomeBrickMesaStairs, props), BuiltInRegistries.BLOCK.getKey(biomeBrickMesaStairs));
		r.accept(new BlockItem(biomeBrickMesaWall, props), BuiltInRegistries.BLOCK.getKey(biomeBrickMesaWall));
		r.accept(new BlockItem(biomeCobblestoneMesa, props), BuiltInRegistries.BLOCK.getKey(biomeCobblestoneMesa));
		r.accept(new BlockItem(biomeCobblestoneMesaSlab, props), BuiltInRegistries.BLOCK.getKey(biomeCobblestoneMesaSlab));
		r.accept(new BlockItem(biomeCobblestoneMesaStairs, props), BuiltInRegistries.BLOCK.getKey(biomeCobblestoneMesaStairs));
		r.accept(new BlockItem(biomeCobblestoneMesaWall, props), BuiltInRegistries.BLOCK.getKey(biomeCobblestoneMesaWall));
		r.accept(new BlockItem(biomeChiseledBrickMesa, props), BuiltInRegistries.BLOCK.getKey(biomeChiseledBrickMesa));

		r.accept(new BlockItem(whitePavement, props), BuiltInRegistries.BLOCK.getKey(whitePavement));
		r.accept(new BlockItem(blackPavement, props), BuiltInRegistries.BLOCK.getKey(blackPavement));
		r.accept(new BlockItem(bluePavement, props), BuiltInRegistries.BLOCK.getKey(bluePavement));
		r.accept(new BlockItem(yellowPavement, props), BuiltInRegistries.BLOCK.getKey(yellowPavement));
		r.accept(new BlockItem(redPavement, props), BuiltInRegistries.BLOCK.getKey(redPavement));
		r.accept(new BlockItem(greenPavement, props), BuiltInRegistries.BLOCK.getKey(greenPavement));

		r.accept(new BlockItem(whitePavementSlab, props), BuiltInRegistries.BLOCK.getKey(whitePavementSlab));
		r.accept(new BlockItem(blackPavementSlab, props), BuiltInRegistries.BLOCK.getKey(blackPavementSlab));
		r.accept(new BlockItem(bluePavementSlab, props), BuiltInRegistries.BLOCK.getKey(bluePavementSlab));
		r.accept(new BlockItem(yellowPavementSlab, props), BuiltInRegistries.BLOCK.getKey(yellowPavementSlab));
		r.accept(new BlockItem(redPavementSlab, props), BuiltInRegistries.BLOCK.getKey(redPavementSlab));
		r.accept(new BlockItem(greenPavementSlab, props), BuiltInRegistries.BLOCK.getKey(greenPavementSlab));

		r.accept(new BlockItem(whitePavementStair, props), BuiltInRegistries.BLOCK.getKey(whitePavementStair));
		r.accept(new BlockItem(blackPavementStair, props), BuiltInRegistries.BLOCK.getKey(blackPavementStair));
		r.accept(new BlockItem(bluePavementStair, props), BuiltInRegistries.BLOCK.getKey(bluePavementStair));
		r.accept(new BlockItem(yellowPavementStair, props), BuiltInRegistries.BLOCK.getKey(yellowPavementStair));
		r.accept(new BlockItem(redPavementStair, props), BuiltInRegistries.BLOCK.getKey(redPavementStair));
		r.accept(new BlockItem(greenPavementStair, props), BuiltInRegistries.BLOCK.getKey(greenPavementStair));

		r.accept(new BlockItem(shimmerrockSlab, props), BuiltInRegistries.BLOCK.getKey(shimmerrockSlab));
		r.accept(new BlockItem(shimmerrockStairs, props), BuiltInRegistries.BLOCK.getKey(shimmerrockStairs));

		r.accept(new BlockItem(shimmerwoodPlankSlab, props), BuiltInRegistries.BLOCK.getKey(shimmerwoodPlankSlab));
		r.accept(new BlockItem(shimmerwoodPlankStairs, props), BuiltInRegistries.BLOCK.getKey(shimmerwoodPlankStairs));

		r.accept(new BlockItem(managlassPane, props), BuiltInRegistries.BLOCK.getKey(managlassPane));
		r.accept(new BlockItem(alfglassPane, props), BuiltInRegistries.BLOCK.getKey(alfglassPane));
		r.accept(new BlockItem(bifrostPane, props), BuiltInRegistries.BLOCK.getKey(bifrostPane));
	}

}
