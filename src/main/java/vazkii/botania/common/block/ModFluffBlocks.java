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
import net.minecraft.block.material.Material;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.util.registry.Registry;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.registries.IForgeRegistry;
import vazkii.botania.common.block.decor.panes.BlockModPane;
import vazkii.botania.common.block.decor.stairs.BlockModStairs;
import vazkii.botania.common.item.ModItems;
import vazkii.botania.common.lib.LibBlockNames;

import static vazkii.botania.common.block.ModBlocks.*;
import static vazkii.botania.common.lib.LibBlockNames.*;

public final class ModFluffBlocks {
	public static Block livingwoodStairs = new BlockModStairs(livingwood.getDefaultState(), Block.Properties.from(livingwood));
	public static Block livingwoodSlab = new SlabBlock(Block.Properties.from(livingwood));
	public static Block livingwoodWall = new WallBlock(Block.Properties.from(livingwood));
	public static Block livingwoodFence = new FenceBlock(Block.Properties.from(livingwood));
	public static Block livingwoodFenceGate = new FenceGateBlock(Block.Properties.from(livingwood));
	public static Block livingwoodPlankStairs = new BlockModStairs(livingwoodPlanks.getDefaultState(), Block.Properties.from(livingwoodPlanks));
	public static Block livingwoodPlankSlab = new SlabBlock(Block.Properties.from(livingwoodPlanks));
	public static Block livingrockStairs = new BlockModStairs(livingrock.getDefaultState(), Block.Properties.from(livingrock));
	public static Block livingrockSlab = new SlabBlock(Block.Properties.from(livingrock));
	public static Block livingrockWall = new WallBlock(Block.Properties.from(livingrock));
	public static Block livingrockBrickStairs = new BlockModStairs(livingrockBrick.getDefaultState(), Block.Properties.from(livingrockBrick));
	public static Block livingrockBrickSlab = new SlabBlock(Block.Properties.from(livingrockBrick));
	public static Block dreamwoodStairs = new BlockModStairs(dreamwood.getDefaultState(), Block.Properties.from(dreamwood));
	public static Block dreamwoodSlab = new SlabBlock(Block.Properties.from(dreamwood));
	public static Block dreamwoodWall = new WallBlock(Block.Properties.from(dreamwood));
	public static Block dreamwoodFence = new FenceBlock(Block.Properties.from(dreamwood));
	public static Block dreamwoodFenceGate = new FenceGateBlock(Block.Properties.from(dreamwood));
	public static Block dreamwoodPlankStairs = new BlockModStairs(dreamwoodPlanks.getDefaultState(), Block.Properties.from(dreamwoodPlanks));
	public static Block dreamwoodPlankSlab = new SlabBlock(Block.Properties.from(dreamwoodPlanks));

	public static Block darkQuartz = new BlockMod(Block.Properties.from(Blocks.QUARTZ_BLOCK));
	public static Block darkQuartzChiseled = new BlockMod(Block.Properties.from(darkQuartz));
	public static Block darkQuartzPillar = new RotatedPillarBlock(Block.Properties.from(darkQuartz));
	public static Block darkQuartzSlab = new SlabBlock(Block.Properties.from(darkQuartz));
	public static Block darkQuartzStairs = new BlockModStairs(darkQuartz.getDefaultState(), Block.Properties.from(darkQuartz));
	
	public static Block manaQuartz = new BlockMod(Block.Properties.from(darkQuartz));
	public static Block manaQuartzChiseled = new BlockMod(Block.Properties.from(darkQuartz));
	public static Block manaQuartzPillar = new RotatedPillarBlock(Block.Properties.from(darkQuartz));
	public static Block manaQuartzSlab = new SlabBlock(Block.Properties.from(darkQuartz));
	public static Block manaQuartzStairs = new BlockModStairs(darkQuartz.getDefaultState(), Block.Properties.from(darkQuartz));
	
	public static Block blazeQuartz = new BlockMod(Block.Properties.from(darkQuartz));
	public static Block blazeQuartzChiseled = new BlockMod(Block.Properties.from(darkQuartz));
	public static Block blazeQuartzPillar = new RotatedPillarBlock(Block.Properties.from(darkQuartz));
	public static Block blazeQuartzSlab = new SlabBlock(Block.Properties.from(darkQuartz));
	public static Block blazeQuartzStairs = new BlockModStairs(darkQuartz.getDefaultState(), Block.Properties.from(darkQuartz));
	
	public static Block lavenderQuartz = new BlockMod(Block.Properties.from(darkQuartz));
	public static Block lavenderQuartzChiseled = new BlockMod(Block.Properties.from(darkQuartz));
	public static Block lavenderQuartzPillar = new RotatedPillarBlock(Block.Properties.from(darkQuartz));
	public static Block lavenderQuartzSlab = new SlabBlock(Block.Properties.from(darkQuartz));
	public static Block lavenderQuartzStairs = new BlockModStairs(darkQuartz.getDefaultState(), Block.Properties.from(darkQuartz));
	
	public static Block redQuartz = new BlockMod(Block.Properties.from(darkQuartz));
	public static Block redQuartzChiseled = new BlockMod(Block.Properties.from(darkQuartz));
	public static Block redQuartzPillar = new RotatedPillarBlock(Block.Properties.from(darkQuartz));
	public static Block redQuartzSlab = new SlabBlock(Block.Properties.from(darkQuartz));
	public static Block redQuartzStairs = new BlockModStairs(darkQuartz.getDefaultState(), Block.Properties.from(darkQuartz));
	
	public static Block elfQuartz = new BlockMod(Block.Properties.from(darkQuartz));
	public static Block elfQuartzChiseled = new BlockMod(Block.Properties.from(darkQuartz));
	public static Block elfQuartzPillar = new RotatedPillarBlock(Block.Properties.from(darkQuartz));
	public static Block elfQuartzSlab = new SlabBlock(Block.Properties.from(darkQuartz));
	public static Block elfQuartzStairs = new BlockModStairs(darkQuartz.getDefaultState(), Block.Properties.from(darkQuartz));
	
	public static Block sunnyQuartz = new BlockMod(Block.Properties.from(darkQuartz));
	public static Block sunnyQuartzChiseled = new BlockMod(Block.Properties.from(darkQuartz));
	public static Block sunnyQuartzPillar = new RotatedPillarBlock(Block.Properties.from(darkQuartz));
	public static Block sunnyQuartzSlab = new SlabBlock(Block.Properties.from(darkQuartz));
	public static Block sunnyQuartzStairs = new BlockModStairs(darkQuartz.getDefaultState(), Block.Properties.from(darkQuartz));
	
	public static Block whitePavement = new BlockMod(Block.Properties.from(livingrock));
	public static Block whitePavementStair = new BlockModStairs(whitePavement.getDefaultState(), Block.Properties.from(whitePavement));
	public static Block whitePavementSlab = new SlabBlock(Block.Properties.from(whitePavement));
	
	public static Block blackPavement = new BlockMod(Block.Properties.from(whitePavement));
	public static Block blackPavementStair = new BlockModStairs(whitePavement.getDefaultState(), Block.Properties.from(whitePavement));
	public static Block blackPavementSlab = new SlabBlock(Block.Properties.from(whitePavement));
	
	public static Block bluePavement = new BlockMod(Block.Properties.from(whitePavement));
	public static Block bluePavementStair = new BlockModStairs(whitePavement.getDefaultState(), Block.Properties.from(whitePavement));
	public static Block bluePavementSlab = new SlabBlock(Block.Properties.from(whitePavement));
	
	public static Block yellowPavement = new BlockMod(Block.Properties.from(whitePavement));
	public static Block yellowPavementStair = new BlockModStairs(whitePavement.getDefaultState(), Block.Properties.from(whitePavement));
	public static Block yellowPavementSlab = new SlabBlock(Block.Properties.from(whitePavement));
	
	public static Block redPavement = new BlockMod(Block.Properties.from(whitePavement));
	public static Block redPavementStair = new BlockModStairs(whitePavement.getDefaultState(), Block.Properties.from(whitePavement));
	public static Block redPavementSlab = new SlabBlock(Block.Properties.from(whitePavement));
	
	public static Block greenPavement = new BlockMod(Block.Properties.from(whitePavement));
	public static Block greenPavementStair = new BlockModStairs(whitePavement.getDefaultState(), Block.Properties.from(whitePavement));
	public static Block greenPavementSlab = new SlabBlock(Block.Properties.from(whitePavement));
	
	public static Block biomeStoneForest = new BlockMod(Block.Properties.create(Material.ROCK).hardnessAndResistance(1.5F, 10).sound(SoundType.STONE));
	public static Block biomeStoneForestSlab = new SlabBlock(Block.Properties.from(biomeStoneForest));
	public static Block biomeStoneForestStairs = new BlockModStairs(biomeStoneForest.getDefaultState(), Block.Properties.from(biomeStoneForest));
	public static Block biomeCobblestoneForest = new BlockMod(Block.Properties.from(biomeStoneForest));
	public static Block biomeCobblestoneForestSlab = new SlabBlock(Block.Properties.from(biomeStoneForest));
	public static Block biomeCobblestoneForestStairs = new BlockModStairs(biomeStoneForest.getDefaultState(), Block.Properties.from(biomeStoneForest));
	public static Block biomeWallForest = new WallBlock(Block.Properties.from(biomeStoneForest));
	public static Block biomeBrickForest = new BlockMod(Block.Properties.from(biomeStoneForest));
	public static Block biomeBrickForestSlab = new SlabBlock(Block.Properties.from(biomeStoneForest));
	public static Block biomeBrickForestStairs = new BlockModStairs(biomeStoneForest.getDefaultState(), Block.Properties.from(biomeStoneForest));
	public static Block biomeChiseledBrickForest = new BlockMod(Block.Properties.from(biomeStoneForest));
	
	public static Block biomeStonePlains = new BlockMod(Block.Properties.from(biomeStoneForest));
	public static Block biomeStonePlainsSlab = new SlabBlock(Block.Properties.from(biomeStonePlains));
	public static Block biomeStonePlainsStairs = new BlockModStairs(biomeStonePlains.getDefaultState(), Block.Properties.from(biomeStonePlains));
	public static Block biomeCobblestonePlains = new BlockMod(Block.Properties.from(biomeStonePlains));
	public static Block biomeCobblestonePlainsSlab = new SlabBlock(Block.Properties.from(biomeStonePlains));
	public static Block biomeCobblestonePlainsStairs = new BlockModStairs(biomeStonePlains.getDefaultState(), Block.Properties.from(biomeStonePlains));
	public static Block biomeWallPlains = new WallBlock(Block.Properties.from(biomeStonePlains));
	public static Block biomeBrickPlains = new BlockMod(Block.Properties.from(biomeStonePlains));
	public static Block biomeBrickPlainsSlab = new SlabBlock(Block.Properties.from(biomeStonePlains));
	public static Block biomeBrickPlainsStairs = new BlockModStairs(biomeStonePlains.getDefaultState(), Block.Properties.from(biomeStonePlains));
	public static Block biomeChiseledBrickPlains = new BlockMod(Block.Properties.from(biomeStonePlains));
	
	public static Block biomeStoneMountain = new BlockMod(Block.Properties.from(biomeStoneForest));
	public static Block biomeStoneMountainSlab = new SlabBlock(Block.Properties.from(biomeStoneMountain));
	public static Block biomeStoneMountainStairs = new BlockModStairs(biomeStoneMountain.getDefaultState(), Block.Properties.from(biomeStoneMountain));
	public static Block biomeCobblestoneMountain = new BlockMod(Block.Properties.from(biomeStoneMountain));
	public static Block biomeCobblestoneMountainSlab = new SlabBlock(Block.Properties.from(biomeStoneMountain));
	public static Block biomeCobblestoneMountainStairs = new BlockModStairs(biomeStoneMountain.getDefaultState(), Block.Properties.from(biomeStoneMountain));
	public static Block biomeWallMountain = new WallBlock(Block.Properties.from(biomeStoneMountain));
	public static Block biomeBrickMountain = new BlockMod(Block.Properties.from(biomeStoneMountain));
	public static Block biomeBrickMountainSlab = new SlabBlock(Block.Properties.from(biomeStoneMountain));
	public static Block biomeBrickMountainStairs = new BlockModStairs(biomeStoneMountain.getDefaultState(), Block.Properties.from(biomeStoneMountain));
	public static Block biomeChiseledBrickMountain = new BlockMod(Block.Properties.from(biomeStoneMountain));
	
	public static Block biomeStoneFungal = new BlockMod(Block.Properties.from(biomeStoneForest));
	public static Block biomeStoneFungalSlab = new SlabBlock(Block.Properties.from(biomeStoneFungal));
	public static Block biomeStoneFungalStairs = new BlockModStairs(biomeStoneFungal.getDefaultState(), Block.Properties.from(biomeStoneFungal));
	public static Block biomeCobblestoneFungal = new BlockMod(Block.Properties.from(biomeStoneFungal));
	public static Block biomeCobblestoneFungalSlab = new SlabBlock(Block.Properties.from(biomeStoneFungal));
	public static Block biomeCobblestoneFungalStairs = new BlockModStairs(biomeStoneFungal.getDefaultState(), Block.Properties.from(biomeStoneFungal));
	public static Block biomeWallFungal = new WallBlock(Block.Properties.from(biomeStoneFungal));
	public static Block biomeBrickFungal = new BlockMod(Block.Properties.from(biomeStoneFungal));
	public static Block biomeBrickFungalSlab = new SlabBlock(Block.Properties.from(biomeStoneFungal));
	public static Block biomeBrickFungalStairs = new BlockModStairs(biomeStoneFungal.getDefaultState(), Block.Properties.from(biomeStoneFungal));
	public static Block biomeChiseledBrickFungal = new BlockMod(Block.Properties.from(biomeStoneFungal));
	
	public static Block biomeStoneSwamp = new BlockMod(Block.Properties.from(biomeStoneForest));
	public static Block biomeStoneSwampSlab = new SlabBlock(Block.Properties.from(biomeStoneSwamp));
	public static Block biomeStoneSwampStairs = new BlockModStairs(biomeStoneSwamp.getDefaultState(), Block.Properties.from(biomeStoneSwamp));
	public static Block biomeCobblestoneSwamp = new BlockMod(Block.Properties.from(biomeStoneSwamp));
	public static Block biomeCobblestoneSwampSlab = new SlabBlock(Block.Properties.from(biomeStoneSwamp));
	public static Block biomeCobblestoneSwampStairs = new BlockModStairs(biomeStoneSwamp.getDefaultState(), Block.Properties.from(biomeStoneSwamp));
	public static Block biomeWallSwamp = new WallBlock(Block.Properties.from(biomeStoneSwamp));
	public static Block biomeBrickSwamp = new BlockMod(Block.Properties.from(biomeStoneSwamp));
	public static Block biomeBrickSwampSlab = new SlabBlock(Block.Properties.from(biomeStoneSwamp));
	public static Block biomeBrickSwampStairs = new BlockModStairs(biomeStoneSwamp.getDefaultState(), Block.Properties.from(biomeStoneSwamp));
	public static Block biomeChiseledBrickSwamp = new BlockMod(Block.Properties.from(biomeStoneSwamp));
	
	public static Block biomeStoneDesert = new BlockMod(Block.Properties.from(biomeStoneForest));
	public static Block biomeStoneDesertSlab = new SlabBlock(Block.Properties.from(biomeStoneDesert));
	public static Block biomeStoneDesertStairs = new BlockModStairs(biomeStoneDesert.getDefaultState(), Block.Properties.from(biomeStoneDesert));
	public static Block biomeCobblestoneDesert = new BlockMod(Block.Properties.from(biomeStoneDesert));
	public static Block biomeCobblestoneDesertSlab = new SlabBlock(Block.Properties.from(biomeStoneDesert));
	public static Block biomeCobblestoneDesertStairs = new BlockModStairs(biomeStoneDesert.getDefaultState(), Block.Properties.from(biomeStoneDesert));
	public static Block biomeWallDesert = new WallBlock(Block.Properties.from(biomeStoneDesert));
	public static Block biomeBrickDesert = new BlockMod(Block.Properties.from(biomeStoneDesert));
	public static Block biomeBrickDesertSlab = new SlabBlock(Block.Properties.from(biomeStoneDesert));
	public static Block biomeBrickDesertStairs = new BlockModStairs(biomeStoneDesert.getDefaultState(), Block.Properties.from(biomeStoneDesert));
	public static Block biomeChiseledBrickDesert = new BlockMod(Block.Properties.from(biomeStoneDesert));
	
	public static Block biomeStoneTaiga = new BlockMod(Block.Properties.from(biomeStoneForest));
	public static Block biomeStoneTaigaSlab = new SlabBlock(Block.Properties.from(biomeStoneTaiga));
	public static Block biomeStoneTaigaStairs = new BlockModStairs(biomeStoneTaiga.getDefaultState(), Block.Properties.from(biomeStoneTaiga));
	public static Block biomeCobblestoneTaiga = new BlockMod(Block.Properties.from(biomeStoneTaiga));
	public static Block biomeCobblestoneTaigaSlab = new SlabBlock(Block.Properties.from(biomeStoneTaiga));
	public static Block biomeCobblestoneTaigaStairs = new BlockModStairs(biomeStoneTaiga.getDefaultState(), Block.Properties.from(biomeStoneTaiga));
	public static Block biomeWallTaiga = new WallBlock(Block.Properties.from(biomeStoneTaiga));
	public static Block biomeBrickTaiga = new BlockMod(Block.Properties.from(biomeStoneTaiga));
	public static Block biomeBrickTaigaSlab = new SlabBlock(Block.Properties.from(biomeStoneTaiga));
	public static Block biomeBrickTaigaStairs = new BlockModStairs(biomeStoneTaiga.getDefaultState(), Block.Properties.from(biomeStoneTaiga));
	public static Block biomeChiseledBrickTaiga = new BlockMod(Block.Properties.from(biomeStoneTaiga));
	
	public static Block biomeStoneMesa = new BlockMod(Block.Properties.from(biomeStoneForest));
	public static Block biomeStoneMesaSlab = new SlabBlock(Block.Properties.from(biomeStoneMesa));
	public static Block biomeStoneMesaStairs = new BlockModStairs(biomeStoneMesa.getDefaultState(), Block.Properties.from(biomeStoneMesa));
	public static Block biomeCobblestoneMesa = new BlockMod(Block.Properties.from(biomeStoneMesa));
	public static Block biomeCobblestoneMesaSlab = new SlabBlock(Block.Properties.from(biomeStoneMesa));
	public static Block biomeCobblestoneMesaStairs = new BlockModStairs(biomeStoneMesa.getDefaultState(), Block.Properties.from(biomeStoneMesa));
	public static Block biomeWallMesa = new WallBlock(Block.Properties.from(biomeStoneMesa));
	public static Block biomeBrickMesa = new BlockMod(Block.Properties.from(biomeStoneMesa));
	public static Block biomeBrickMesaSlab = new SlabBlock(Block.Properties.from(biomeStoneMesa));
	public static Block biomeBrickMesaStairs = new BlockModStairs(biomeStoneMesa.getDefaultState(), Block.Properties.from(biomeStoneMesa));
	public static Block biomeChiseledBrickMesa = new BlockMod(Block.Properties.from(biomeStoneMesa));

	public static Block shimmerrockSlab = new SlabBlock(Block.Properties.from(shimmerrock));
	public static Block shimmerrockStairs = new BlockModStairs(shimmerrock.getDefaultState(), Block.Properties.from(shimmerrock));
	
	public static Block shimmerwoodPlankSlab = new SlabBlock(Block.Properties.from(shimmerwoodPlanks));
	public static Block shimmerwoodPlankStairs = new BlockModStairs(shimmerwoodPlanks.getDefaultState(), Block.Properties.from(shimmerwoodPlanks));

	public static Block managlassPane = new BlockModPane(Block.Properties.from(manaGlass));
	public static Block alfglassPane = new BlockModPane(Block.Properties.from(elfGlass));
	public static Block bifrostPane = new BlockModPane(Block.Properties.from(bifrostPerm));

	public static void registerBlocks(RegistryEvent.Register<Block> evt) {
		IForgeRegistry<Block> r = evt.getRegistry();

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
		register(r, METAMORPHIC_PREFIX + "forest_cobblestone" + WALL_SUFFIX, biomeWallForest);
		register(r, METAMORPHIC_PREFIX + "forest_bricks", biomeBrickForest);
		register(r, METAMORPHIC_PREFIX + "forest_bricks" + SLAB_SUFFIX, biomeBrickForestSlab);
		register(r, METAMORPHIC_PREFIX + "forest_bricks" + STAIR_SUFFIX, biomeBrickForestStairs);
		register(r, "chiseled_" + METAMORPHIC_PREFIX + "forest_bricks", biomeChiseledBrickForest);
		
		register(r, METAMORPHIC_PREFIX + "plains_stone", biomeStonePlains);
		register(r, METAMORPHIC_PREFIX + "plains_stone" + SLAB_SUFFIX, biomeStonePlainsSlab);
		register(r, METAMORPHIC_PREFIX + "plains_stone" + STAIR_SUFFIX, biomeStonePlainsStairs);
		register(r, METAMORPHIC_PREFIX + "plains_cobblestone", biomeCobblestonePlains);
		register(r, METAMORPHIC_PREFIX + "plains_cobblestone" + SLAB_SUFFIX, biomeCobblestonePlainsSlab);
		register(r, METAMORPHIC_PREFIX + "plains_cobblestone" + STAIR_SUFFIX, biomeCobblestonePlainsStairs);
		register(r, METAMORPHIC_PREFIX + "plains_cobblestone" + WALL_SUFFIX, biomeWallPlains);
		register(r, METAMORPHIC_PREFIX + "plains_bricks", biomeBrickPlains);
		register(r, METAMORPHIC_PREFIX + "plains_bricks" + SLAB_SUFFIX, biomeBrickPlainsSlab);
		register(r, METAMORPHIC_PREFIX + "plains_bricks" + STAIR_SUFFIX, biomeBrickPlainsStairs);
		register(r, "chiseled_" + METAMORPHIC_PREFIX + "plains_bricks", biomeChiseledBrickPlains);
		
		register(r, METAMORPHIC_PREFIX + "mountain_stone", biomeStoneMountain);
		register(r, METAMORPHIC_PREFIX + "mountain_stone" + SLAB_SUFFIX, biomeStoneMountainSlab);
		register(r, METAMORPHIC_PREFIX + "mountain_stone" + STAIR_SUFFIX, biomeStoneMountainStairs);
		register(r, METAMORPHIC_PREFIX + "mountain_cobblestone", biomeCobblestoneMountain);
		register(r, METAMORPHIC_PREFIX + "mountain_cobblestone" + SLAB_SUFFIX, biomeCobblestoneMountainSlab);
		register(r, METAMORPHIC_PREFIX + "mountain_cobblestone" + STAIR_SUFFIX, biomeCobblestoneMountainStairs);
		register(r, METAMORPHIC_PREFIX + "mountain_cobblestone" + WALL_SUFFIX, biomeWallMountain);
		register(r, METAMORPHIC_PREFIX + "mountain_bricks", biomeBrickMountain);
		register(r, METAMORPHIC_PREFIX + "mountain_bricks" + SLAB_SUFFIX, biomeBrickMountainSlab);
		register(r, METAMORPHIC_PREFIX + "mountain_bricks" + STAIR_SUFFIX, biomeBrickMountainStairs);
		register(r, "chiseled_" + METAMORPHIC_PREFIX + "mountain_bricks", biomeChiseledBrickMountain);
		
		register(r, METAMORPHIC_PREFIX + "fungal_stone", biomeStoneFungal);
		register(r, METAMORPHIC_PREFIX + "fungal_stone" + SLAB_SUFFIX, biomeStoneFungalSlab);
		register(r, METAMORPHIC_PREFIX + "fungal_stone" + STAIR_SUFFIX, biomeStoneFungalStairs);
		register(r, METAMORPHIC_PREFIX + "fungal_cobblestone", biomeCobblestoneFungal);
		register(r, METAMORPHIC_PREFIX + "fungal_cobblestone" + SLAB_SUFFIX, biomeCobblestoneFungalSlab);
		register(r, METAMORPHIC_PREFIX + "fungal_cobblestone" + STAIR_SUFFIX, biomeCobblestoneFungalStairs);
		register(r, METAMORPHIC_PREFIX + "fungal_cobblestone" + WALL_SUFFIX, biomeWallFungal);
		register(r, METAMORPHIC_PREFIX + "fungal_bricks", biomeBrickFungal);
		register(r, METAMORPHIC_PREFIX + "fungal_bricks" + SLAB_SUFFIX, biomeBrickFungalSlab);
		register(r, METAMORPHIC_PREFIX + "fungal_bricks" + STAIR_SUFFIX, biomeBrickFungalStairs);
		register(r, "chiseled_" + METAMORPHIC_PREFIX + "fungal_bricks", biomeChiseledBrickFungal);
		
		register(r, METAMORPHIC_PREFIX + "swamp_stone", biomeStoneSwamp);
		register(r, METAMORPHIC_PREFIX + "swamp_stone" + SLAB_SUFFIX, biomeStoneSwampSlab);
		register(r, METAMORPHIC_PREFIX + "swamp_stone" + STAIR_SUFFIX, biomeStoneSwampStairs);
		register(r, METAMORPHIC_PREFIX + "swamp_cobblestone", biomeCobblestoneSwamp);
		register(r, METAMORPHIC_PREFIX + "swamp_cobblestone" + SLAB_SUFFIX, biomeCobblestoneSwampSlab);
		register(r, METAMORPHIC_PREFIX + "swamp_cobblestone" + STAIR_SUFFIX, biomeCobblestoneSwampStairs);
		register(r, METAMORPHIC_PREFIX + "swamp_cobblestone" + WALL_SUFFIX, biomeWallSwamp);
		register(r, METAMORPHIC_PREFIX + "swamp_bricks", biomeBrickSwamp);
		register(r, METAMORPHIC_PREFIX + "swamp_bricks" + SLAB_SUFFIX, biomeBrickSwampSlab);
		register(r, METAMORPHIC_PREFIX + "swamp_bricks" + STAIR_SUFFIX, biomeBrickSwampStairs);
		register(r, "chiseled_" + METAMORPHIC_PREFIX + "swamp_bricks", biomeChiseledBrickSwamp);
		
		register(r, METAMORPHIC_PREFIX + "desert_stone", biomeStoneDesert);
		register(r, METAMORPHIC_PREFIX + "desert_stone" + SLAB_SUFFIX, biomeStoneDesertSlab);
		register(r, METAMORPHIC_PREFIX + "desert_stone" + STAIR_SUFFIX, biomeStoneDesertStairs);
		register(r, METAMORPHIC_PREFIX + "desert_cobblestone", biomeCobblestoneDesert);
		register(r, METAMORPHIC_PREFIX + "desert_cobblestone" + SLAB_SUFFIX, biomeCobblestoneDesertSlab);
		register(r, METAMORPHIC_PREFIX + "desert_cobblestone" + STAIR_SUFFIX, biomeCobblestoneDesertStairs);
		register(r, METAMORPHIC_PREFIX + "desert_cobblestone" + WALL_SUFFIX, biomeWallDesert);
		register(r, METAMORPHIC_PREFIX + "desert_bricks", biomeBrickDesert);
		register(r, METAMORPHIC_PREFIX + "desert_bricks" + SLAB_SUFFIX, biomeBrickDesertSlab);
		register(r, METAMORPHIC_PREFIX + "desert_bricks" + STAIR_SUFFIX, biomeBrickDesertStairs);
		register(r, "chiseled_" + METAMORPHIC_PREFIX + "desert_bricks", biomeChiseledBrickDesert);
		
		register(r, METAMORPHIC_PREFIX + "taiga_stone", biomeStoneTaiga);
		register(r, METAMORPHIC_PREFIX + "taiga_stone" + SLAB_SUFFIX, biomeStoneTaigaSlab);
		register(r, METAMORPHIC_PREFIX + "taiga_stone" + STAIR_SUFFIX, biomeStoneTaigaStairs);
		register(r, METAMORPHIC_PREFIX + "taiga_cobblestone", biomeCobblestoneTaiga);
		register(r, METAMORPHIC_PREFIX + "taiga_cobblestone" + SLAB_SUFFIX, biomeCobblestoneTaigaSlab);
		register(r, METAMORPHIC_PREFIX + "taiga_cobblestone" + STAIR_SUFFIX, biomeCobblestoneTaigaStairs);
		register(r, METAMORPHIC_PREFIX + "taiga_cobblestone" + WALL_SUFFIX, biomeWallTaiga);
		register(r, METAMORPHIC_PREFIX + "taiga_bricks", biomeBrickTaiga);
		register(r, METAMORPHIC_PREFIX + "taiga_bricks" + SLAB_SUFFIX, biomeBrickTaigaSlab);
		register(r, METAMORPHIC_PREFIX + "taiga_bricks" + STAIR_SUFFIX, biomeBrickTaigaStairs);
		register(r, "chiseled_" + METAMORPHIC_PREFIX + "taiga_bricks", biomeChiseledBrickTaiga);
		
		register(r, METAMORPHIC_PREFIX + "mesa_stone", biomeStoneMesa);
		register(r, METAMORPHIC_PREFIX + "mesa_stone" + SLAB_SUFFIX, biomeStoneMesaSlab);
		register(r, METAMORPHIC_PREFIX + "mesa_stone" + STAIR_SUFFIX, biomeStoneMesaStairs);
		register(r, METAMORPHIC_PREFIX + "mesa_cobblestone", biomeCobblestoneMesa);
		register(r, METAMORPHIC_PREFIX + "mesa_cobblestone" + SLAB_SUFFIX, biomeCobblestoneMesaSlab);
		register(r, METAMORPHIC_PREFIX + "mesa_cobblestone" + STAIR_SUFFIX, biomeCobblestoneMesaStairs);
		register(r, METAMORPHIC_PREFIX + "mesa_cobblestone" + WALL_SUFFIX, biomeWallMesa);
		register(r, METAMORPHIC_PREFIX + "mesa_bricks", biomeBrickMesa);
		register(r, METAMORPHIC_PREFIX + "mesa_bricks" + SLAB_SUFFIX, biomeBrickMesaSlab);
		register(r, METAMORPHIC_PREFIX + "mesa_bricks" + STAIR_SUFFIX, biomeBrickMesaStairs);
		register(r, "chiseled_" + METAMORPHIC_PREFIX + "mesa_bricks", biomeChiseledBrickMesa);
		
		register(r, LibBlockNames.SHIMMERROCK + SLAB_SUFFIX, shimmerrockSlab);
		register(r, LibBlockNames.SHIMMERROCK + STAIR_SUFFIX, shimmerrockStairs);

		register(r, LibBlockNames.SHIMMERWOOD_PLANKS + SLAB_SUFFIX, shimmerwoodPlankSlab);
		register(r, LibBlockNames.SHIMMERWOOD_PLANKS + STAIR_SUFFIX, shimmerwoodPlankStairs);

		register(r, LibBlockNames.MANA_GLASS + "_pane", managlassPane);
		register(r, LibBlockNames.ELF_GLASS + "_pane", alfglassPane);
		register(r, LibBlockNames.BIFROST + "_pane", bifrostPane);
	}

	public static void registerItemBlocks(RegistryEvent.Register<Item> evt) {
		IForgeRegistry<Item> r = evt.getRegistry();
		Item.Properties props = ModItems.defaultBuilder();

		register(r, Registry.BLOCK.getKey(livingwoodStairs), new BlockItem(livingwoodStairs, props));
		register(r, Registry.BLOCK.getKey(livingwoodSlab), new BlockItem(livingwoodSlab, props));
		register(r, Registry.BLOCK.getKey(livingwoodWall), new BlockItem(livingwoodWall, props));
		register(r, Registry.BLOCK.getKey(livingwoodFence), new BlockItem(livingwoodFence, props));
		register(r, Registry.BLOCK.getKey(livingwoodFenceGate), new BlockItem(livingwoodFenceGate, props));

		register(r, Registry.BLOCK.getKey(livingwoodPlankStairs), new BlockItem(livingwoodPlankStairs, props));
		register(r, Registry.BLOCK.getKey(livingwoodPlankSlab), new BlockItem(livingwoodPlankSlab, props));

		register(r, Registry.BLOCK.getKey(livingrockStairs), new BlockItem(livingrockStairs, props));
		register(r, Registry.BLOCK.getKey(livingrockSlab), new BlockItem(livingrockSlab, props));
		register(r, Registry.BLOCK.getKey(livingrockWall), new BlockItem(livingrockWall, props));

		register(r, Registry.BLOCK.getKey(livingrockBrickStairs), new BlockItem(livingrockBrickStairs, props));
		register(r, Registry.BLOCK.getKey(livingrockBrickSlab), new BlockItem(livingrockBrickSlab, props));

		register(r, Registry.BLOCK.getKey(dreamwoodStairs), new BlockItem(dreamwoodStairs, props));
		register(r, Registry.BLOCK.getKey(dreamwoodSlab), new BlockItem(dreamwoodSlab, props));
		register(r, Registry.BLOCK.getKey(dreamwoodWall), new BlockItem(dreamwoodWall, props));
		register(r, Registry.BLOCK.getKey(dreamwoodFence), new BlockItem(dreamwoodFence, props));
		register(r, Registry.BLOCK.getKey(dreamwoodFenceGate), new BlockItem(dreamwoodFenceGate, props));

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
		register(r, Registry.BLOCK.getKey(biomeCobblestoneForest), new BlockItem(biomeCobblestoneForest, props));
		register(r, Registry.BLOCK.getKey(biomeCobblestoneForestSlab), new BlockItem(biomeCobblestoneForestSlab, props));
		register(r, Registry.BLOCK.getKey(biomeCobblestoneForestStairs), new BlockItem(biomeCobblestoneForestStairs, props));
		register(r, Registry.BLOCK.getKey(biomeWallForest), new BlockItem(biomeWallForest, props));
		register(r, Registry.BLOCK.getKey(biomeChiseledBrickForest), new BlockItem(biomeChiseledBrickForest, props));

		register(r, Registry.BLOCK.getKey(biomeStonePlains), new BlockItem(biomeStonePlains, props));
		register(r, Registry.BLOCK.getKey(biomeStonePlainsSlab), new BlockItem(biomeStonePlainsSlab, props));
		register(r, Registry.BLOCK.getKey(biomeStonePlainsStairs), new BlockItem(biomeStonePlainsStairs, props));
		register(r, Registry.BLOCK.getKey(biomeBrickPlains), new BlockItem(biomeBrickPlains, props));
		register(r, Registry.BLOCK.getKey(biomeBrickPlainsSlab), new BlockItem(biomeBrickPlainsSlab, props));
		register(r, Registry.BLOCK.getKey(biomeBrickPlainsStairs), new BlockItem(biomeBrickPlainsStairs, props));
		register(r, Registry.BLOCK.getKey(biomeCobblestonePlains), new BlockItem(biomeCobblestonePlains, props));
		register(r, Registry.BLOCK.getKey(biomeCobblestonePlainsSlab), new BlockItem(biomeCobblestonePlainsSlab, props));
		register(r, Registry.BLOCK.getKey(biomeCobblestonePlainsStairs), new BlockItem(biomeCobblestonePlainsStairs, props));
		register(r, Registry.BLOCK.getKey(biomeWallPlains), new BlockItem(biomeWallPlains, props));
		register(r, Registry.BLOCK.getKey(biomeChiseledBrickPlains), new BlockItem(biomeChiseledBrickPlains, props));

		register(r, Registry.BLOCK.getKey(biomeStoneMountain), new BlockItem(biomeStoneMountain, props));
		register(r, Registry.BLOCK.getKey(biomeStoneMountainSlab), new BlockItem(biomeStoneMountainSlab, props));
		register(r, Registry.BLOCK.getKey(biomeStoneMountainStairs), new BlockItem(biomeStoneMountainStairs, props));
		register(r, Registry.BLOCK.getKey(biomeBrickMountain), new BlockItem(biomeBrickMountain, props));
		register(r, Registry.BLOCK.getKey(biomeBrickMountainSlab), new BlockItem(biomeBrickMountainSlab, props));
		register(r, Registry.BLOCK.getKey(biomeBrickMountainStairs), new BlockItem(biomeBrickMountainStairs, props));
		register(r, Registry.BLOCK.getKey(biomeCobblestoneMountain), new BlockItem(biomeCobblestoneMountain, props));
		register(r, Registry.BLOCK.getKey(biomeCobblestoneMountainSlab), new BlockItem(biomeCobblestoneMountainSlab, props));
		register(r, Registry.BLOCK.getKey(biomeCobblestoneMountainStairs), new BlockItem(biomeCobblestoneMountainStairs, props));
		register(r, Registry.BLOCK.getKey(biomeWallMountain), new BlockItem(biomeWallMountain, props));
		register(r, Registry.BLOCK.getKey(biomeChiseledBrickMountain), new BlockItem(biomeChiseledBrickMountain, props));

		register(r, Registry.BLOCK.getKey(biomeStoneFungal), new BlockItem(biomeStoneFungal, props));
		register(r, Registry.BLOCK.getKey(biomeStoneFungalSlab), new BlockItem(biomeStoneFungalSlab, props));
		register(r, Registry.BLOCK.getKey(biomeStoneFungalStairs), new BlockItem(biomeStoneFungalStairs, props));
		register(r, Registry.BLOCK.getKey(biomeBrickFungal), new BlockItem(biomeBrickFungal, props));
		register(r, Registry.BLOCK.getKey(biomeBrickFungalSlab), new BlockItem(biomeBrickFungalSlab, props));
		register(r, Registry.BLOCK.getKey(biomeBrickFungalStairs), new BlockItem(biomeBrickFungalStairs, props));
		register(r, Registry.BLOCK.getKey(biomeCobblestoneFungal), new BlockItem(biomeCobblestoneFungal, props));
		register(r, Registry.BLOCK.getKey(biomeCobblestoneFungalSlab), new BlockItem(biomeCobblestoneFungalSlab, props));
		register(r, Registry.BLOCK.getKey(biomeCobblestoneFungalStairs), new BlockItem(biomeCobblestoneFungalStairs, props));
		register(r, Registry.BLOCK.getKey(biomeWallFungal), new BlockItem(biomeWallFungal, props));
		register(r, Registry.BLOCK.getKey(biomeChiseledBrickFungal), new BlockItem(biomeChiseledBrickFungal, props));

		register(r, Registry.BLOCK.getKey(biomeStoneSwamp), new BlockItem(biomeStoneSwamp, props));
		register(r, Registry.BLOCK.getKey(biomeStoneSwampSlab), new BlockItem(biomeStoneSwampSlab, props));
		register(r, Registry.BLOCK.getKey(biomeStoneSwampStairs), new BlockItem(biomeStoneSwampStairs, props));
		register(r, Registry.BLOCK.getKey(biomeBrickSwamp), new BlockItem(biomeBrickSwamp, props));
		register(r, Registry.BLOCK.getKey(biomeBrickSwampSlab), new BlockItem(biomeBrickSwampSlab, props));
		register(r, Registry.BLOCK.getKey(biomeBrickSwampStairs), new BlockItem(biomeBrickSwampStairs, props));
		register(r, Registry.BLOCK.getKey(biomeCobblestoneSwamp), new BlockItem(biomeCobblestoneSwamp, props));
		register(r, Registry.BLOCK.getKey(biomeCobblestoneSwampSlab), new BlockItem(biomeCobblestoneSwampSlab, props));
		register(r, Registry.BLOCK.getKey(biomeCobblestoneSwampStairs), new BlockItem(biomeCobblestoneSwampStairs, props));
		register(r, Registry.BLOCK.getKey(biomeWallSwamp), new BlockItem(biomeWallSwamp, props));
		register(r, Registry.BLOCK.getKey(biomeChiseledBrickSwamp), new BlockItem(biomeChiseledBrickSwamp, props));

		register(r, Registry.BLOCK.getKey(biomeStoneDesert), new BlockItem(biomeStoneDesert, props));
		register(r, Registry.BLOCK.getKey(biomeStoneDesertSlab), new BlockItem(biomeStoneDesertSlab, props));
		register(r, Registry.BLOCK.getKey(biomeStoneDesertStairs), new BlockItem(biomeStoneDesertStairs, props));
		register(r, Registry.BLOCK.getKey(biomeBrickDesert), new BlockItem(biomeBrickDesert, props));
		register(r, Registry.BLOCK.getKey(biomeBrickDesertSlab), new BlockItem(biomeBrickDesertSlab, props));
		register(r, Registry.BLOCK.getKey(biomeBrickDesertStairs), new BlockItem(biomeBrickDesertStairs, props));
		register(r, Registry.BLOCK.getKey(biomeCobblestoneDesert), new BlockItem(biomeCobblestoneDesert, props));
		register(r, Registry.BLOCK.getKey(biomeCobblestoneDesertSlab), new BlockItem(biomeCobblestoneDesertSlab, props));
		register(r, Registry.BLOCK.getKey(biomeCobblestoneDesertStairs), new BlockItem(biomeCobblestoneDesertStairs, props));
		register(r, Registry.BLOCK.getKey(biomeWallDesert), new BlockItem(biomeWallDesert, props));
		register(r, Registry.BLOCK.getKey(biomeChiseledBrickDesert), new BlockItem(biomeChiseledBrickDesert, props));

		register(r, Registry.BLOCK.getKey(biomeStoneTaiga), new BlockItem(biomeStoneTaiga, props));
		register(r, Registry.BLOCK.getKey(biomeStoneTaigaSlab), new BlockItem(biomeStoneTaigaSlab, props));
		register(r, Registry.BLOCK.getKey(biomeStoneTaigaStairs), new BlockItem(biomeStoneTaigaStairs, props));
		register(r, Registry.BLOCK.getKey(biomeBrickTaiga), new BlockItem(biomeBrickTaiga, props));
		register(r, Registry.BLOCK.getKey(biomeBrickTaigaSlab), new BlockItem(biomeBrickTaigaSlab, props));
		register(r, Registry.BLOCK.getKey(biomeBrickTaigaStairs), new BlockItem(biomeBrickTaigaStairs, props));
		register(r, Registry.BLOCK.getKey(biomeCobblestoneTaiga), new BlockItem(biomeCobblestoneTaiga, props));
		register(r, Registry.BLOCK.getKey(biomeCobblestoneTaigaSlab), new BlockItem(biomeCobblestoneTaigaSlab, props));
		register(r, Registry.BLOCK.getKey(biomeCobblestoneTaigaStairs), new BlockItem(biomeCobblestoneTaigaStairs, props));
		register(r, Registry.BLOCK.getKey(biomeWallTaiga), new BlockItem(biomeWallTaiga, props));
		register(r, Registry.BLOCK.getKey(biomeChiseledBrickTaiga), new BlockItem(biomeChiseledBrickTaiga, props));

		register(r, Registry.BLOCK.getKey(biomeStoneMesa), new BlockItem(biomeStoneMesa, props));
		register(r, Registry.BLOCK.getKey(biomeStoneMesaSlab), new BlockItem(biomeStoneMesaSlab, props));
		register(r, Registry.BLOCK.getKey(biomeStoneMesaStairs), new BlockItem(biomeStoneMesaStairs, props));
		register(r, Registry.BLOCK.getKey(biomeBrickMesa), new BlockItem(biomeBrickMesa, props));
		register(r, Registry.BLOCK.getKey(biomeBrickMesaSlab), new BlockItem(biomeBrickMesaSlab, props));
		register(r, Registry.BLOCK.getKey(biomeBrickMesaStairs), new BlockItem(biomeBrickMesaStairs, props));
		register(r, Registry.BLOCK.getKey(biomeCobblestoneMesa), new BlockItem(biomeCobblestoneMesa, props));
		register(r, Registry.BLOCK.getKey(biomeCobblestoneMesaSlab), new BlockItem(biomeCobblestoneMesaSlab, props));
		register(r, Registry.BLOCK.getKey(biomeCobblestoneMesaStairs), new BlockItem(biomeCobblestoneMesaStairs, props));
		register(r, Registry.BLOCK.getKey(biomeWallMesa), new BlockItem(biomeWallMesa, props));
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
