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
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.registries.IForgeRegistry;
import net.minecraftforge.registries.ObjectHolder;

import vazkii.botania.common.block.decor.panes.BlockModPane;
import vazkii.botania.common.block.decor.stairs.BlockModStairs;
import vazkii.botania.common.item.ModItems;
import vazkii.botania.common.lib.LibBlockNames;
import vazkii.botania.common.lib.LibMisc;

import static vazkii.botania.common.block.ModBlocks.register;
import static vazkii.botania.common.lib.LibBlockNames.*;
import static vazkii.botania.common.lib.ResourceLocationHelper.prefix;

@ObjectHolder(LibMisc.MOD_ID)
public final class ModFluffBlocks {

	@ObjectHolder(LibBlockNames.LIVING_WOOD + STAIR_SUFFIX) public static Block livingwoodStairs;
	@ObjectHolder(LibBlockNames.LIVING_WOOD + SLAB_SUFFIX) public static Block livingwoodSlab;
	@ObjectHolder(LibBlockNames.LIVING_WOOD + WALL_SUFFIX) public static Block livingwoodWall;
	@ObjectHolder(LibBlockNames.LIVING_WOOD + FENCE_SUFFIX) public static Block livingwoodFence;
	@ObjectHolder(LibBlockNames.LIVING_WOOD + FENCE_GATE_SUFFIX) public static Block livingwoodFenceGate;
	@ObjectHolder(LibBlockNames.LIVING_WOOD_PLANKS + STAIR_SUFFIX) public static Block livingwoodPlankStairs;
	@ObjectHolder(LibBlockNames.LIVING_WOOD_PLANKS + SLAB_SUFFIX) public static Block livingwoodPlankSlab;
	@ObjectHolder(LibBlockNames.LIVING_ROCK + STAIR_SUFFIX) public static Block livingrockStairs;
	@ObjectHolder(LibBlockNames.LIVING_ROCK + SLAB_SUFFIX) public static Block livingrockSlab;
	@ObjectHolder(LibBlockNames.LIVING_ROCK + WALL_SUFFIX) public static Block livingrockWall;
	@ObjectHolder(LibBlockNames.LIVING_ROCK_BRICK + STAIR_SUFFIX) public static Block livingrockBrickStairs;
	@ObjectHolder(LibBlockNames.LIVING_ROCK_BRICK + SLAB_SUFFIX) public static Block livingrockBrickSlab;
	@ObjectHolder(LibBlockNames.DREAM_WOOD + STAIR_SUFFIX) public static Block dreamwoodStairs;
	@ObjectHolder(LibBlockNames.DREAM_WOOD + SLAB_SUFFIX) public static Block dreamwoodSlab;
	@ObjectHolder(LibBlockNames.DREAM_WOOD + WALL_SUFFIX) public static Block dreamwoodWall;
	@ObjectHolder(LibBlockNames.DREAM_WOOD + FENCE_SUFFIX) public static Block dreamwoodFence;
	@ObjectHolder(LibBlockNames.DREAM_WOOD + FENCE_GATE_SUFFIX) public static Block dreamwoodFenceGate;
	@ObjectHolder(LibBlockNames.DREAM_WOOD_PLANKS + STAIR_SUFFIX) public static Block dreamwoodPlankStairs;
	@ObjectHolder(LibBlockNames.DREAM_WOOD_PLANKS + SLAB_SUFFIX) public static Block dreamwoodPlankSlab;

	@ObjectHolder(LibBlockNames.QUARTZ_DARK) public static Block darkQuartz;
	@ObjectHolder("chiseled_" + LibBlockNames.QUARTZ_DARK) public static Block darkQuartzChiseled;
	@ObjectHolder(LibBlockNames.QUARTZ_DARK + "_pillar") public static Block darkQuartzPillar;
	@ObjectHolder(LibBlockNames.QUARTZ_DARK + SLAB_SUFFIX) public static Block darkQuartzSlab;
	@ObjectHolder(LibBlockNames.QUARTZ_DARK + STAIR_SUFFIX) public static Block darkQuartzStairs;

	@ObjectHolder(LibBlockNames.QUARTZ_MANA) public static Block manaQuartz;
	@ObjectHolder("chiseled_" + LibBlockNames.QUARTZ_MANA) public static Block manaQuartzChiseled;
	@ObjectHolder(LibBlockNames.QUARTZ_MANA + "_pillar") public static Block manaQuartzPillar;
	@ObjectHolder(LibBlockNames.QUARTZ_MANA + SLAB_SUFFIX) public static Block manaQuartzSlab;
	@ObjectHolder(LibBlockNames.QUARTZ_MANA + STAIR_SUFFIX) public static Block manaQuartzStairs;

	@ObjectHolder(LibBlockNames.QUARTZ_BLAZE) public static Block blazeQuartz;
	@ObjectHolder("chiseled_" + LibBlockNames.QUARTZ_BLAZE) public static Block blazeQuartzChiseled;
	@ObjectHolder(LibBlockNames.QUARTZ_BLAZE + "_pillar") public static Block blazeQuartzPillar;
	@ObjectHolder(LibBlockNames.QUARTZ_BLAZE + SLAB_SUFFIX) public static Block blazeQuartzSlab;
	@ObjectHolder(LibBlockNames.QUARTZ_BLAZE + STAIR_SUFFIX) public static Block blazeQuartzStairs;

	@ObjectHolder(LibBlockNames.QUARTZ_LAVENDER) public static Block lavenderQuartz;
	@ObjectHolder("chiseled_" + LibBlockNames.QUARTZ_LAVENDER) public static Block lavenderQuartzChiseled;
	@ObjectHolder(LibBlockNames.QUARTZ_LAVENDER + "_pillar") public static Block lavenderQuartzPillar;
	@ObjectHolder(LibBlockNames.QUARTZ_LAVENDER + SLAB_SUFFIX) public static Block lavenderQuartzSlab;
	@ObjectHolder(LibBlockNames.QUARTZ_LAVENDER + STAIR_SUFFIX) public static Block lavenderQuartzStairs;

	@ObjectHolder(LibBlockNames.QUARTZ_RED) public static Block redQuartz;
	@ObjectHolder("chiseled_" + LibBlockNames.QUARTZ_RED) public static Block redQuartzChiseled;
	@ObjectHolder(LibBlockNames.QUARTZ_RED + "_pillar") public static Block redQuartzPillar;
	@ObjectHolder(LibBlockNames.QUARTZ_RED + SLAB_SUFFIX) public static Block redQuartzSlab;
	@ObjectHolder(LibBlockNames.QUARTZ_RED + STAIR_SUFFIX) public static Block redQuartzStairs;

	@ObjectHolder(LibBlockNames.QUARTZ_ELF) public static Block elfQuartz;
	@ObjectHolder("chiseled_" + LibBlockNames.QUARTZ_ELF) public static Block elfQuartzChiseled;
	@ObjectHolder(LibBlockNames.QUARTZ_ELF + "_pillar") public static Block elfQuartzPillar;
	@ObjectHolder(LibBlockNames.QUARTZ_ELF + SLAB_SUFFIX) public static Block elfQuartzSlab;
	@ObjectHolder(LibBlockNames.QUARTZ_ELF + STAIR_SUFFIX) public static Block elfQuartzStairs;

	@ObjectHolder(LibBlockNames.QUARTZ_SUNNY) public static Block sunnyQuartz;
	@ObjectHolder("chiseled_" + LibBlockNames.QUARTZ_SUNNY) public static Block sunnyQuartzChiseled;
	@ObjectHolder(LibBlockNames.QUARTZ_SUNNY + "_pillar") public static Block sunnyQuartzPillar;
	@ObjectHolder(LibBlockNames.QUARTZ_SUNNY + SLAB_SUFFIX) public static Block sunnyQuartzSlab;
	@ObjectHolder(LibBlockNames.QUARTZ_SUNNY + STAIR_SUFFIX) public static Block sunnyQuartzStairs;

	@ObjectHolder(LibBlockNames.METAMORPHIC_PREFIX + "forest_stone") public static Block biomeStoneForest;
	@ObjectHolder(LibBlockNames.METAMORPHIC_PREFIX + "forest_stone" + SLAB_SUFFIX) public static Block biomeStoneForestSlab;
	@ObjectHolder(LibBlockNames.METAMORPHIC_PREFIX + "forest_stone" + STAIR_SUFFIX) public static Block biomeStoneForestStairs;
	@ObjectHolder(LibBlockNames.METAMORPHIC_PREFIX + "forest_cobblestone") public static Block biomeCobblestoneForest;
	@ObjectHolder(LibBlockNames.METAMORPHIC_PREFIX + "forest_cobblestone" + SLAB_SUFFIX) public static Block biomeCobblestoneForestSlab;
	@ObjectHolder(LibBlockNames.METAMORPHIC_PREFIX + "forest_cobblestone" + STAIR_SUFFIX) public static Block biomeCobblestoneForestStairs;
	@ObjectHolder(LibBlockNames.METAMORPHIC_PREFIX + "forest_cobblestone" + WALL_SUFFIX) public static Block biomeWallForest;
	@ObjectHolder(LibBlockNames.METAMORPHIC_PREFIX + "forest_bricks") public static Block biomeBrickForest;
	@ObjectHolder(LibBlockNames.METAMORPHIC_PREFIX + "forest_bricks" + SLAB_SUFFIX) public static Block biomeBrickForestSlab;
	@ObjectHolder(LibBlockNames.METAMORPHIC_PREFIX + "forest_bricks" + STAIR_SUFFIX) public static Block biomeBrickForestStairs;
	@ObjectHolder("chiseled_" + LibBlockNames.METAMORPHIC_PREFIX + "forest_bricks") public static Block biomeChiseledBrickForest;

	@ObjectHolder(LibBlockNames.METAMORPHIC_PREFIX + "plains_stone") public static Block biomeStonePlains;
	@ObjectHolder(LibBlockNames.METAMORPHIC_PREFIX + "plains_stone" + SLAB_SUFFIX) public static Block biomeStonePlainsSlab;
	@ObjectHolder(LibBlockNames.METAMORPHIC_PREFIX + "plains_stone" + STAIR_SUFFIX) public static Block biomeStonePlainsStairs;
	@ObjectHolder(LibBlockNames.METAMORPHIC_PREFIX + "plains_cobblestone") public static Block biomeCobblestonePlains;
	@ObjectHolder(LibBlockNames.METAMORPHIC_PREFIX + "plains_cobblestone" + SLAB_SUFFIX) public static Block biomeCobblestonePlainsSlab;
	@ObjectHolder(LibBlockNames.METAMORPHIC_PREFIX + "plains_cobblestone" + STAIR_SUFFIX) public static Block biomeCobblestonePlainsStairs;
	@ObjectHolder(LibBlockNames.METAMORPHIC_PREFIX + "plains_cobblestone" + WALL_SUFFIX) public static Block biomeWallPlains;
	@ObjectHolder(LibBlockNames.METAMORPHIC_PREFIX + "plains_bricks") public static Block biomeBrickPlains;
	@ObjectHolder(LibBlockNames.METAMORPHIC_PREFIX + "plains_bricks" + SLAB_SUFFIX) public static Block biomeBrickPlainsSlab;
	@ObjectHolder(LibBlockNames.METAMORPHIC_PREFIX + "plains_bricks" + STAIR_SUFFIX) public static Block biomeBrickPlainsStairs;
	@ObjectHolder("chiseled_" + LibBlockNames.METAMORPHIC_PREFIX + "plains_bricks") public static Block biomeChiseledBrickPlains;

	@ObjectHolder(LibBlockNames.METAMORPHIC_PREFIX + "mountain_stone") public static Block biomeStoneMountain;
	@ObjectHolder(LibBlockNames.METAMORPHIC_PREFIX + "mountain_stone" + SLAB_SUFFIX) public static Block biomeStoneMountainSlab;
	@ObjectHolder(LibBlockNames.METAMORPHIC_PREFIX + "mountain_stone" + STAIR_SUFFIX) public static Block biomeStoneMountainStairs;
	@ObjectHolder(LibBlockNames.METAMORPHIC_PREFIX + "mountain_cobblestone") public static Block biomeCobblestoneMountain;
	@ObjectHolder(LibBlockNames.METAMORPHIC_PREFIX + "mountain_cobblestone" + SLAB_SUFFIX) public static Block biomeCobblestoneMountainSlab;
	@ObjectHolder(LibBlockNames.METAMORPHIC_PREFIX + "mountain_cobblestone" + STAIR_SUFFIX) public static Block biomeCobblestoneMountainStairs;
	@ObjectHolder(LibBlockNames.METAMORPHIC_PREFIX + "mountain_cobblestone" + WALL_SUFFIX) public static Block biomeWallMountain;
	@ObjectHolder(LibBlockNames.METAMORPHIC_PREFIX + "mountain_bricks") public static Block biomeBrickMountain;
	@ObjectHolder(LibBlockNames.METAMORPHIC_PREFIX + "mountain_bricks" + SLAB_SUFFIX) public static Block biomeBrickMountainSlab;
	@ObjectHolder(LibBlockNames.METAMORPHIC_PREFIX + "mountain_bricks" + STAIR_SUFFIX) public static Block biomeBrickMountainStairs;
	@ObjectHolder("chiseled_" + LibBlockNames.METAMORPHIC_PREFIX + "mountain_bricks") public static Block biomeChiseledBrickMountain;

	@ObjectHolder(LibBlockNames.METAMORPHIC_PREFIX + "fungal_stone") public static Block biomeStoneFungal;
	@ObjectHolder(LibBlockNames.METAMORPHIC_PREFIX + "fungal_stone" + SLAB_SUFFIX) public static Block biomeStoneFungalSlab;
	@ObjectHolder(LibBlockNames.METAMORPHIC_PREFIX + "fungal_stone" + STAIR_SUFFIX) public static Block biomeStoneFungalStairs;
	@ObjectHolder(LibBlockNames.METAMORPHIC_PREFIX + "fungal_cobblestone") public static Block biomeCobblestoneFungal;
	@ObjectHolder(LibBlockNames.METAMORPHIC_PREFIX + "fungal_cobblestone" + SLAB_SUFFIX) public static Block biomeCobblestoneFungalSlab;
	@ObjectHolder(LibBlockNames.METAMORPHIC_PREFIX + "fungal_cobblestone" + STAIR_SUFFIX) public static Block biomeCobblestoneFungalStairs;
	@ObjectHolder(LibBlockNames.METAMORPHIC_PREFIX + "fungal_cobblestone" + WALL_SUFFIX) public static Block biomeWallFungal;
	@ObjectHolder(LibBlockNames.METAMORPHIC_PREFIX + "fungal_bricks") public static Block biomeBrickFungal;
	@ObjectHolder(LibBlockNames.METAMORPHIC_PREFIX + "fungal_bricks" + SLAB_SUFFIX) public static Block biomeBrickFungalSlab;
	@ObjectHolder(LibBlockNames.METAMORPHIC_PREFIX + "fungal_bricks" + STAIR_SUFFIX) public static Block biomeBrickFungalStairs;
	@ObjectHolder("chiseled_" + LibBlockNames.METAMORPHIC_PREFIX + "fungal_bricks") public static Block biomeChiseledBrickFungal;

	@ObjectHolder(LibBlockNames.METAMORPHIC_PREFIX + "swamp_stone") public static Block biomeStoneSwamp;
	@ObjectHolder(LibBlockNames.METAMORPHIC_PREFIX + "swamp_stone" + SLAB_SUFFIX) public static Block biomeStoneSwampSlab;
	@ObjectHolder(LibBlockNames.METAMORPHIC_PREFIX + "swamp_stone" + STAIR_SUFFIX) public static Block biomeStoneSwampStairs;
	@ObjectHolder(LibBlockNames.METAMORPHIC_PREFIX + "swamp_cobblestone") public static Block biomeCobblestoneSwamp;
	@ObjectHolder(LibBlockNames.METAMORPHIC_PREFIX + "swamp_cobblestone" + SLAB_SUFFIX) public static Block biomeCobblestoneSwampSlab;
	@ObjectHolder(LibBlockNames.METAMORPHIC_PREFIX + "swamp_cobblestone" + STAIR_SUFFIX) public static Block biomeCobblestoneSwampStairs;
	@ObjectHolder(LibBlockNames.METAMORPHIC_PREFIX + "swamp_cobblestone" + WALL_SUFFIX) public static Block biomeWallSwamp;
	@ObjectHolder(LibBlockNames.METAMORPHIC_PREFIX + "swamp_bricks") public static Block biomeBrickSwamp;
	@ObjectHolder(LibBlockNames.METAMORPHIC_PREFIX + "swamp_bricks" + SLAB_SUFFIX) public static Block biomeBrickSwampSlab;
	@ObjectHolder(LibBlockNames.METAMORPHIC_PREFIX + "swamp_bricks" + STAIR_SUFFIX) public static Block biomeBrickSwampStairs;
	@ObjectHolder("chiseled_" + LibBlockNames.METAMORPHIC_PREFIX + "swamp_bricks") public static Block biomeChiseledBrickSwamp;

	@ObjectHolder(LibBlockNames.METAMORPHIC_PREFIX + "desert_stone") public static Block biomeStoneDesert;
	@ObjectHolder(LibBlockNames.METAMORPHIC_PREFIX + "desert_stone" + SLAB_SUFFIX) public static Block biomeStoneDesertSlab;
	@ObjectHolder(LibBlockNames.METAMORPHIC_PREFIX + "desert_stone" + STAIR_SUFFIX) public static Block biomeStoneDesertStairs;
	@ObjectHolder(LibBlockNames.METAMORPHIC_PREFIX + "desert_cobblestone") public static Block biomeCobblestoneDesert;
	@ObjectHolder(LibBlockNames.METAMORPHIC_PREFIX + "desert_cobblestone" + SLAB_SUFFIX) public static Block biomeCobblestoneDesertSlab;
	@ObjectHolder(LibBlockNames.METAMORPHIC_PREFIX + "desert_cobblestone" + STAIR_SUFFIX) public static Block biomeCobblestoneDesertStairs;
	@ObjectHolder(LibBlockNames.METAMORPHIC_PREFIX + "desert_cobblestone" + WALL_SUFFIX) public static Block biomeWallDesert;
	@ObjectHolder(LibBlockNames.METAMORPHIC_PREFIX + "desert_bricks") public static Block biomeBrickDesert;
	@ObjectHolder(LibBlockNames.METAMORPHIC_PREFIX + "desert_bricks" + SLAB_SUFFIX) public static Block biomeBrickDesertSlab;
	@ObjectHolder(LibBlockNames.METAMORPHIC_PREFIX + "desert_bricks" + STAIR_SUFFIX) public static Block biomeBrickDesertStairs;
	@ObjectHolder("chiseled_" + LibBlockNames.METAMORPHIC_PREFIX + "desert_bricks") public static Block biomeChiseledBrickDesert;

	@ObjectHolder(LibBlockNames.METAMORPHIC_PREFIX + "taiga_stone") public static Block biomeStoneTaiga;
	@ObjectHolder(LibBlockNames.METAMORPHIC_PREFIX + "taiga_stone" + SLAB_SUFFIX) public static Block biomeStoneTaigaSlab;
	@ObjectHolder(LibBlockNames.METAMORPHIC_PREFIX + "taiga_stone" + STAIR_SUFFIX) public static Block biomeStoneTaigaStairs;
	@ObjectHolder(LibBlockNames.METAMORPHIC_PREFIX + "taiga_cobblestone") public static Block biomeCobblestoneTaiga;
	@ObjectHolder(LibBlockNames.METAMORPHIC_PREFIX + "taiga_cobblestone" + SLAB_SUFFIX) public static Block biomeCobblestoneTaigaSlab;
	@ObjectHolder(LibBlockNames.METAMORPHIC_PREFIX + "taiga_cobblestone" + STAIR_SUFFIX) public static Block biomeCobblestoneTaigaStairs;
	@ObjectHolder(LibBlockNames.METAMORPHIC_PREFIX + "taiga_cobblestone" + WALL_SUFFIX) public static Block biomeWallTaiga;
	@ObjectHolder(LibBlockNames.METAMORPHIC_PREFIX + "taiga_bricks") public static Block biomeBrickTaiga;
	@ObjectHolder(LibBlockNames.METAMORPHIC_PREFIX + "taiga_bricks" + SLAB_SUFFIX) public static Block biomeBrickTaigaSlab;
	@ObjectHolder(LibBlockNames.METAMORPHIC_PREFIX + "taiga_bricks" + STAIR_SUFFIX) public static Block biomeBrickTaigaStairs;
	@ObjectHolder("chiseled_" + LibBlockNames.METAMORPHIC_PREFIX + "taiga_bricks") public static Block biomeChiseledBrickTaiga;

	@ObjectHolder(LibBlockNames.METAMORPHIC_PREFIX + "mesa_stone") public static Block biomeStoneMesa;
	@ObjectHolder(LibBlockNames.METAMORPHIC_PREFIX + "mesa_stone" + SLAB_SUFFIX) public static Block biomeStoneMesaSlab;
	@ObjectHolder(LibBlockNames.METAMORPHIC_PREFIX + "mesa_stone" + STAIR_SUFFIX) public static Block biomeStoneMesaStairs;
	@ObjectHolder(LibBlockNames.METAMORPHIC_PREFIX + "mesa_cobblestone") public static Block biomeCobblestoneMesa;
	@ObjectHolder(LibBlockNames.METAMORPHIC_PREFIX + "mesa_cobblestone" + SLAB_SUFFIX) public static Block biomeCobblestoneMesaSlab;
	@ObjectHolder(LibBlockNames.METAMORPHIC_PREFIX + "mesa_cobblestone" + STAIR_SUFFIX) public static Block biomeCobblestoneMesaStairs;
	@ObjectHolder(LibBlockNames.METAMORPHIC_PREFIX + "mesa_cobblestone" + WALL_SUFFIX) public static Block biomeWallMesa;
	@ObjectHolder(LibBlockNames.METAMORPHIC_PREFIX + "mesa_bricks") public static Block biomeBrickMesa;
	@ObjectHolder(LibBlockNames.METAMORPHIC_PREFIX + "mesa_bricks" + SLAB_SUFFIX) public static Block biomeBrickMesaSlab;
	@ObjectHolder(LibBlockNames.METAMORPHIC_PREFIX + "mesa_bricks" + STAIR_SUFFIX) public static Block biomeBrickMesaStairs;
	@ObjectHolder("chiseled_" + LibBlockNames.METAMORPHIC_PREFIX + "mesa_bricks") public static Block biomeChiseledBrickMesa;

	@ObjectHolder("white" + LibBlockNames.PAVEMENT_SUFFIX) public static Block whitePavement;
	@ObjectHolder("black" + LibBlockNames.PAVEMENT_SUFFIX) public static Block blackPavement;
	@ObjectHolder("blue" + LibBlockNames.PAVEMENT_SUFFIX) public static Block bluePavement;
	@ObjectHolder("yellow" + LibBlockNames.PAVEMENT_SUFFIX) public static Block yellowPavement;
	@ObjectHolder("red" + LibBlockNames.PAVEMENT_SUFFIX) public static Block redPavement;
	@ObjectHolder("green" + LibBlockNames.PAVEMENT_SUFFIX) public static Block greenPavement;

	@ObjectHolder("white" + LibBlockNames.PAVEMENT_SUFFIX + SLAB_SUFFIX) public static Block whitePavementSlab;
	@ObjectHolder("black" + LibBlockNames.PAVEMENT_SUFFIX + SLAB_SUFFIX) public static Block blackPavementSlab;
	@ObjectHolder("blue" + LibBlockNames.PAVEMENT_SUFFIX + SLAB_SUFFIX) public static Block bluePavementSlab;
	@ObjectHolder("yellow" + LibBlockNames.PAVEMENT_SUFFIX + SLAB_SUFFIX) public static Block yellowPavementSlab;
	@ObjectHolder("red" + LibBlockNames.PAVEMENT_SUFFIX + SLAB_SUFFIX) public static Block redPavementSlab;
	@ObjectHolder("green" + LibBlockNames.PAVEMENT_SUFFIX + SLAB_SUFFIX) public static Block greenPavementSlab;

	@ObjectHolder("white" + LibBlockNames.PAVEMENT_SUFFIX + STAIR_SUFFIX) public static Block whitePavementStair;
	@ObjectHolder("black" + LibBlockNames.PAVEMENT_SUFFIX + STAIR_SUFFIX) public static Block blackPavementStair;
	@ObjectHolder("blue" + LibBlockNames.PAVEMENT_SUFFIX + STAIR_SUFFIX) public static Block bluePavementStair;
	@ObjectHolder("yellow" + LibBlockNames.PAVEMENT_SUFFIX + STAIR_SUFFIX) public static Block yellowPavementStair;
	@ObjectHolder("red" + LibBlockNames.PAVEMENT_SUFFIX + STAIR_SUFFIX) public static Block redPavementStair;
	@ObjectHolder("green" + LibBlockNames.PAVEMENT_SUFFIX + STAIR_SUFFIX) public static Block greenPavementStair;

	@ObjectHolder(LibBlockNames.SHIMMERROCK + SLAB_SUFFIX) public static Block shimmerrockSlab;
	@ObjectHolder(LibBlockNames.SHIMMERROCK + STAIR_SUFFIX) public static Block shimmerrockStairs;
	@ObjectHolder(LibBlockNames.SHIMMERWOOD_PLANKS + SLAB_SUFFIX) public static Block shimmerwoodPlankSlab;
	@ObjectHolder(LibBlockNames.SHIMMERWOOD_PLANKS + STAIR_SUFFIX) public static Block shimmerwoodPlankStairs;

	@ObjectHolder(LibBlockNames.MANA_GLASS + "_pane") public static Block managlassPane;
	@ObjectHolder(LibBlockNames.ELF_GLASS + "_pane") public static Block alfglassPane;
	@ObjectHolder(LibBlockNames.BIFROST + "_pane") public static Block bifrostPane;

	public static void registerBlocks(RegistryEvent.Register<Block> evt) {
		IForgeRegistry<Block> r = evt.getRegistry();

		Block base = r.getValue(prefix(LibBlockNames.LIVING_WOOD));
		register(r, new BlockModStairs(base.getDefaultState(), Block.Properties.from(base)), LibBlockNames.LIVING_WOOD + STAIR_SUFFIX);
		register(r, new SlabBlock(Block.Properties.from(base)), LibBlockNames.LIVING_WOOD + SLAB_SUFFIX);
		register(r, new WallBlock(Block.Properties.from(base)), LibBlockNames.LIVING_WOOD + WALL_SUFFIX);
		register(r, new FenceBlock(Block.Properties.from(base)), LibBlockNames.LIVING_WOOD + FENCE_SUFFIX);
		register(r, new FenceGateBlock(Block.Properties.from(base)), LibBlockNames.LIVING_WOOD + FENCE_GATE_SUFFIX);

		base = r.getValue(prefix(LibBlockNames.LIVING_WOOD_PLANKS));
		register(r, new BlockModStairs(base.getDefaultState(), Block.Properties.from(base)), LibBlockNames.LIVING_WOOD_PLANKS + STAIR_SUFFIX);
		register(r, new SlabBlock(Block.Properties.from(base)), LibBlockNames.LIVING_WOOD_PLANKS + SLAB_SUFFIX);

		base = r.getValue(prefix(LibBlockNames.LIVING_ROCK));
		register(r, new BlockModStairs(base.getDefaultState(), Block.Properties.from(base)), LibBlockNames.LIVING_ROCK + STAIR_SUFFIX);
		register(r, new SlabBlock(Block.Properties.from(base)), LibBlockNames.LIVING_ROCK + SLAB_SUFFIX);
		register(r, new WallBlock(Block.Properties.from(base)), LibBlockNames.LIVING_ROCK + WALL_SUFFIX);

		base = r.getValue(prefix(LibBlockNames.LIVING_ROCK_BRICK));
		register(r, new BlockModStairs(base.getDefaultState(), Block.Properties.from(base)), LibBlockNames.LIVING_ROCK_BRICK + STAIR_SUFFIX);
		register(r, new SlabBlock(Block.Properties.from(base)), LibBlockNames.LIVING_ROCK_BRICK + SLAB_SUFFIX);

		base = r.getValue(prefix(LibBlockNames.DREAM_WOOD));
		register(r, new BlockModStairs(base.getDefaultState(), Block.Properties.from(base)), LibBlockNames.DREAM_WOOD + STAIR_SUFFIX);
		register(r, new SlabBlock(Block.Properties.from(base)), LibBlockNames.DREAM_WOOD + SLAB_SUFFIX);
		register(r, new WallBlock(Block.Properties.from(base)), LibBlockNames.DREAM_WOOD + WALL_SUFFIX);
		register(r, new FenceBlock(Block.Properties.from(base)), LibBlockNames.DREAM_WOOD + FENCE_SUFFIX);
		register(r, new FenceGateBlock(Block.Properties.from(base)), LibBlockNames.DREAM_WOOD + FENCE_GATE_SUFFIX);

		base = r.getValue(prefix(LibBlockNames.DREAM_WOOD_PLANKS));
		register(r, new BlockModStairs(base.getDefaultState(), Block.Properties.from(base)), LibBlockNames.DREAM_WOOD_PLANKS + STAIR_SUFFIX);
		register(r, new SlabBlock(Block.Properties.from(base)), LibBlockNames.DREAM_WOOD_PLANKS + SLAB_SUFFIX);

		Block.Properties props = Block.Properties.from(Blocks.QUARTZ_BLOCK);
		for (String variant : LibBlockNames.QUARTZ_VARIANTS) {

			base = new BlockMod(props);
			register(r, base, variant);
			register(r, new BlockMod(props), "chiseled_" + variant);
			register(r, new RotatedPillarBlock(props), variant + "_pillar");
			register(r, new SlabBlock(props), variant + SLAB_SUFFIX);
			register(r, new BlockModStairs(base.getDefaultState(), props), variant + STAIR_SUFFIX);
		}

		props = Block.Properties.create(Material.ROCK).hardnessAndResistance(2, 10).sound(SoundType.STONE);
		for (String color : LibBlockNames.PAVEMENT_VARIANTS) {
			Block block = new BlockMod(props);
			register(r, block, color + LibBlockNames.PAVEMENT_SUFFIX);
			register(r, new BlockModStairs(block.getDefaultState(), props), color + LibBlockNames.PAVEMENT_SUFFIX + STAIR_SUFFIX);
			register(r, new SlabBlock(props), color + LibBlockNames.PAVEMENT_SUFFIX + SLAB_SUFFIX);
		}

		props = Block.Properties.create(Material.ROCK).hardnessAndResistance(1.5F, 10).sound(SoundType.STONE);
		for (String variant : LibBlockNames.METAMORPHIC_VARIANTS) {
			base = new BlockMod(props);
			register(r, base, LibBlockNames.METAMORPHIC_PREFIX + variant + "_stone");
			register(r, new BlockModStairs(base.getDefaultState(), props), base.getRegistryName().getPath() + STAIR_SUFFIX);
			register(r, new SlabBlock(props), base.getRegistryName().getPath() + SLAB_SUFFIX);

			base = new BlockMod(props);
			register(r, base, LibBlockNames.METAMORPHIC_PREFIX + variant + "_cobblestone");
			register(r, new BlockModStairs(base.getDefaultState(), props), base.getRegistryName().getPath() + STAIR_SUFFIX);
			register(r, new SlabBlock(props), base.getRegistryName().getPath() + SLAB_SUFFIX);
			register(r, new WallBlock(props), LibBlockNames.METAMORPHIC_PREFIX + variant + "_cobblestone" + WALL_SUFFIX);

			base = new BlockMod(props);
			register(r, base, LibBlockNames.METAMORPHIC_PREFIX + variant + "_bricks");
			register(r, new BlockModStairs(base.getDefaultState(), props), base.getRegistryName().getPath() + STAIR_SUFFIX);
			register(r, new SlabBlock(props), base.getRegistryName().getPath() + SLAB_SUFFIX);
			register(r, new BlockMod(props), "chiseled_" + LibBlockNames.METAMORPHIC_PREFIX + variant + "_bricks");
		}

		base = r.getValue(prefix(LibBlockNames.SHIMMERROCK));
		register(r, new SlabBlock(Block.Properties.from(base)), LibBlockNames.SHIMMERROCK + SLAB_SUFFIX);
		register(r, new BlockModStairs(base.getDefaultState(), Block.Properties.from(base)), LibBlockNames.SHIMMERROCK + STAIR_SUFFIX);

		base = r.getValue(prefix(LibBlockNames.SHIMMERWOOD_PLANKS));
		register(r, new SlabBlock(Block.Properties.from(base)), LibBlockNames.SHIMMERWOOD_PLANKS + SLAB_SUFFIX);
		register(r, new BlockModStairs(base.getDefaultState(), Block.Properties.from(base)), LibBlockNames.SHIMMERWOOD_PLANKS + STAIR_SUFFIX);

		base = r.getValue(prefix(LibBlockNames.MANA_GLASS));
		register(r, new BlockModPane(Block.Properties.from(base)), LibBlockNames.MANA_GLASS + "_pane");

		base = r.getValue(prefix(LibBlockNames.ELF_GLASS));
		register(r, new BlockModPane(Block.Properties.from(base)), LibBlockNames.ELF_GLASS + "_pane");

		base = r.getValue(prefix(LibBlockNames.BIFROST_PERM));
		register(r, new BlockModPane(Block.Properties.from(base)), LibBlockNames.BIFROST + "_pane");
	}

	public static void registerItemBlocks(RegistryEvent.Register<Item> evt) {
		IForgeRegistry<Item> r = evt.getRegistry();
		Item.Properties props = ModItems.defaultBuilder();

		r.register(new BlockItem(livingwoodStairs, props).setRegistryName(livingwoodStairs.getRegistryName()));
		r.register(new BlockItem(livingwoodSlab, props).setRegistryName(livingwoodSlab.getRegistryName()));
		r.register(new BlockItem(livingwoodWall, props).setRegistryName(livingwoodWall.getRegistryName()));
		r.register(new BlockItem(livingwoodFence, props).setRegistryName(livingwoodFence.getRegistryName()));
		r.register(new BlockItem(livingwoodFenceGate, props).setRegistryName(livingwoodFenceGate.getRegistryName()));

		r.register(new BlockItem(livingwoodPlankStairs, props).setRegistryName(livingwoodPlankStairs.getRegistryName()));
		r.register(new BlockItem(livingwoodPlankSlab, props).setRegistryName(livingwoodPlankSlab.getRegistryName()));

		r.register(new BlockItem(livingrockStairs, props).setRegistryName(livingrockStairs.getRegistryName()));
		r.register(new BlockItem(livingrockSlab, props).setRegistryName(livingrockSlab.getRegistryName()));
		r.register(new BlockItem(livingrockWall, props).setRegistryName(livingrockWall.getRegistryName()));

		r.register(new BlockItem(livingrockBrickStairs, props).setRegistryName(livingrockBrickStairs.getRegistryName()));
		r.register(new BlockItem(livingrockBrickSlab, props).setRegistryName(livingrockBrickSlab.getRegistryName()));

		r.register(new BlockItem(dreamwoodStairs, props).setRegistryName(dreamwoodStairs.getRegistryName()));
		r.register(new BlockItem(dreamwoodSlab, props).setRegistryName(dreamwoodSlab.getRegistryName()));
		r.register(new BlockItem(dreamwoodWall, props).setRegistryName(dreamwoodWall.getRegistryName()));
		r.register(new BlockItem(dreamwoodFence, props).setRegistryName(dreamwoodFence.getRegistryName()));
		r.register(new BlockItem(dreamwoodFenceGate, props).setRegistryName(dreamwoodFenceGate.getRegistryName()));

		r.register(new BlockItem(dreamwoodPlankStairs, props).setRegistryName(dreamwoodPlankStairs.getRegistryName()));
		r.register(new BlockItem(dreamwoodPlankSlab, props).setRegistryName(dreamwoodPlankSlab.getRegistryName()));

		r.register(new BlockItem(darkQuartz, props).setRegistryName(darkQuartz.getRegistryName()));
		r.register(new BlockItem(darkQuartzPillar, props).setRegistryName(darkQuartzPillar.getRegistryName()));
		r.register(new BlockItem(darkQuartzChiseled, props).setRegistryName(darkQuartzChiseled.getRegistryName()));
		r.register(new BlockItem(darkQuartzSlab, props).setRegistryName(darkQuartzSlab.getRegistryName()));
		r.register(new BlockItem(darkQuartzStairs, props).setRegistryName(darkQuartzStairs.getRegistryName()));

		r.register(new BlockItem(manaQuartz, props).setRegistryName(manaQuartz.getRegistryName()));
		r.register(new BlockItem(manaQuartzPillar, props).setRegistryName(manaQuartzPillar.getRegistryName()));
		r.register(new BlockItem(manaQuartzChiseled, props).setRegistryName(manaQuartzChiseled.getRegistryName()));
		r.register(new BlockItem(manaQuartzSlab, props).setRegistryName(manaQuartzSlab.getRegistryName()));
		r.register(new BlockItem(manaQuartzStairs, props).setRegistryName(manaQuartzStairs.getRegistryName()));

		r.register(new BlockItem(blazeQuartz, props).setRegistryName(blazeQuartz.getRegistryName()));
		r.register(new BlockItem(blazeQuartzPillar, props).setRegistryName(blazeQuartzPillar.getRegistryName()));
		r.register(new BlockItem(blazeQuartzChiseled, props).setRegistryName(blazeQuartzChiseled.getRegistryName()));
		r.register(new BlockItem(blazeQuartzSlab, props).setRegistryName(blazeQuartzSlab.getRegistryName()));
		r.register(new BlockItem(blazeQuartzStairs, props).setRegistryName(blazeQuartzStairs.getRegistryName()));

		r.register(new BlockItem(lavenderQuartz, props).setRegistryName(lavenderQuartz.getRegistryName()));
		r.register(new BlockItem(lavenderQuartzPillar, props).setRegistryName(lavenderQuartzPillar.getRegistryName()));
		r.register(new BlockItem(lavenderQuartzChiseled, props).setRegistryName(lavenderQuartzChiseled.getRegistryName()));
		r.register(new BlockItem(lavenderQuartzSlab, props).setRegistryName(lavenderQuartzSlab.getRegistryName()));
		r.register(new BlockItem(lavenderQuartzStairs, props).setRegistryName(lavenderQuartzStairs.getRegistryName()));

		r.register(new BlockItem(redQuartz, props).setRegistryName(redQuartz.getRegistryName()));
		r.register(new BlockItem(redQuartzPillar, props).setRegistryName(redQuartzPillar.getRegistryName()));
		r.register(new BlockItem(redQuartzChiseled, props).setRegistryName(redQuartzChiseled.getRegistryName()));
		r.register(new BlockItem(redQuartzSlab, props).setRegistryName(redQuartzSlab.getRegistryName()));
		r.register(new BlockItem(redQuartzStairs, props).setRegistryName(redQuartzStairs.getRegistryName()));

		r.register(new BlockItem(elfQuartz, props).setRegistryName(elfQuartz.getRegistryName()));
		r.register(new BlockItem(elfQuartzPillar, props).setRegistryName(elfQuartzPillar.getRegistryName()));
		r.register(new BlockItem(elfQuartzChiseled, props).setRegistryName(elfQuartzChiseled.getRegistryName()));
		r.register(new BlockItem(elfQuartzSlab, props).setRegistryName(elfQuartzSlab.getRegistryName()));
		r.register(new BlockItem(elfQuartzStairs, props).setRegistryName(elfQuartzStairs.getRegistryName()));

		r.register(new BlockItem(sunnyQuartz, props).setRegistryName(sunnyQuartz.getRegistryName()));
		r.register(new BlockItem(sunnyQuartzPillar, props).setRegistryName(sunnyQuartzPillar.getRegistryName()));
		r.register(new BlockItem(sunnyQuartzChiseled, props).setRegistryName(sunnyQuartzChiseled.getRegistryName()));
		r.register(new BlockItem(sunnyQuartzSlab, props).setRegistryName(sunnyQuartzSlab.getRegistryName()));
		r.register(new BlockItem(sunnyQuartzStairs, props).setRegistryName(sunnyQuartzStairs.getRegistryName()));

		r.register(new BlockItem(biomeStoneForest, props).setRegistryName(biomeStoneForest.getRegistryName()));
		r.register(new BlockItem(biomeStoneForestSlab, props).setRegistryName(biomeStoneForestSlab.getRegistryName()));
		r.register(new BlockItem(biomeStoneForestStairs, props).setRegistryName(biomeStoneForestStairs.getRegistryName()));
		r.register(new BlockItem(biomeBrickForest, props).setRegistryName(biomeBrickForest.getRegistryName()));
		r.register(new BlockItem(biomeBrickForestSlab, props).setRegistryName(biomeBrickForestSlab.getRegistryName()));
		r.register(new BlockItem(biomeBrickForestStairs, props).setRegistryName(biomeBrickForestStairs.getRegistryName()));
		r.register(new BlockItem(biomeCobblestoneForest, props).setRegistryName(biomeCobblestoneForest.getRegistryName()));
		r.register(new BlockItem(biomeCobblestoneForestSlab, props).setRegistryName(biomeCobblestoneForestSlab.getRegistryName()));
		r.register(new BlockItem(biomeCobblestoneForestStairs, props).setRegistryName(biomeCobblestoneForestStairs.getRegistryName()));
		r.register(new BlockItem(biomeWallForest, props).setRegistryName(biomeWallForest.getRegistryName()));
		r.register(new BlockItem(biomeChiseledBrickForest, props).setRegistryName(biomeChiseledBrickForest.getRegistryName()));

		r.register(new BlockItem(biomeStonePlains, props).setRegistryName(biomeStonePlains.getRegistryName()));
		r.register(new BlockItem(biomeStonePlainsSlab, props).setRegistryName(biomeStonePlainsSlab.getRegistryName()));
		r.register(new BlockItem(biomeStonePlainsStairs, props).setRegistryName(biomeStonePlainsStairs.getRegistryName()));
		r.register(new BlockItem(biomeBrickPlains, props).setRegistryName(biomeBrickPlains.getRegistryName()));
		r.register(new BlockItem(biomeBrickPlainsSlab, props).setRegistryName(biomeBrickPlainsSlab.getRegistryName()));
		r.register(new BlockItem(biomeBrickPlainsStairs, props).setRegistryName(biomeBrickPlainsStairs.getRegistryName()));
		r.register(new BlockItem(biomeCobblestonePlains, props).setRegistryName(biomeCobblestonePlains.getRegistryName()));
		r.register(new BlockItem(biomeCobblestonePlainsSlab, props).setRegistryName(biomeCobblestonePlainsSlab.getRegistryName()));
		r.register(new BlockItem(biomeCobblestonePlainsStairs, props).setRegistryName(biomeCobblestonePlainsStairs.getRegistryName()));
		r.register(new BlockItem(biomeWallPlains, props).setRegistryName(biomeWallPlains.getRegistryName()));
		r.register(new BlockItem(biomeChiseledBrickPlains, props).setRegistryName(biomeChiseledBrickPlains.getRegistryName()));

		r.register(new BlockItem(biomeStoneMountain, props).setRegistryName(biomeStoneMountain.getRegistryName()));
		r.register(new BlockItem(biomeStoneMountainSlab, props).setRegistryName(biomeStoneMountainSlab.getRegistryName()));
		r.register(new BlockItem(biomeStoneMountainStairs, props).setRegistryName(biomeStoneMountainStairs.getRegistryName()));
		r.register(new BlockItem(biomeBrickMountain, props).setRegistryName(biomeBrickMountain.getRegistryName()));
		r.register(new BlockItem(biomeBrickMountainSlab, props).setRegistryName(biomeBrickMountainSlab.getRegistryName()));
		r.register(new BlockItem(biomeBrickMountainStairs, props).setRegistryName(biomeBrickMountainStairs.getRegistryName()));
		r.register(new BlockItem(biomeCobblestoneMountain, props).setRegistryName(biomeCobblestoneMountain.getRegistryName()));
		r.register(new BlockItem(biomeCobblestoneMountainSlab, props).setRegistryName(biomeCobblestoneMountainSlab.getRegistryName()));
		r.register(new BlockItem(biomeCobblestoneMountainStairs, props).setRegistryName(biomeCobblestoneMountainStairs.getRegistryName()));
		r.register(new BlockItem(biomeWallMountain, props).setRegistryName(biomeWallMountain.getRegistryName()));
		r.register(new BlockItem(biomeChiseledBrickMountain, props).setRegistryName(biomeChiseledBrickMountain.getRegistryName()));

		r.register(new BlockItem(biomeStoneFungal, props).setRegistryName(biomeStoneFungal.getRegistryName()));
		r.register(new BlockItem(biomeStoneFungalSlab, props).setRegistryName(biomeStoneFungalSlab.getRegistryName()));
		r.register(new BlockItem(biomeStoneFungalStairs, props).setRegistryName(biomeStoneFungalStairs.getRegistryName()));
		r.register(new BlockItem(biomeBrickFungal, props).setRegistryName(biomeBrickFungal.getRegistryName()));
		r.register(new BlockItem(biomeBrickFungalSlab, props).setRegistryName(biomeBrickFungalSlab.getRegistryName()));
		r.register(new BlockItem(biomeBrickFungalStairs, props).setRegistryName(biomeBrickFungalStairs.getRegistryName()));
		r.register(new BlockItem(biomeCobblestoneFungal, props).setRegistryName(biomeCobblestoneFungal.getRegistryName()));
		r.register(new BlockItem(biomeCobblestoneFungalSlab, props).setRegistryName(biomeCobblestoneFungalSlab.getRegistryName()));
		r.register(new BlockItem(biomeCobblestoneFungalStairs, props).setRegistryName(biomeCobblestoneFungalStairs.getRegistryName()));
		r.register(new BlockItem(biomeWallFungal, props).setRegistryName(biomeWallFungal.getRegistryName()));
		r.register(new BlockItem(biomeChiseledBrickFungal, props).setRegistryName(biomeChiseledBrickFungal.getRegistryName()));

		r.register(new BlockItem(biomeStoneSwamp, props).setRegistryName(biomeStoneSwamp.getRegistryName()));
		r.register(new BlockItem(biomeStoneSwampSlab, props).setRegistryName(biomeStoneSwampSlab.getRegistryName()));
		r.register(new BlockItem(biomeStoneSwampStairs, props).setRegistryName(biomeStoneSwampStairs.getRegistryName()));
		r.register(new BlockItem(biomeBrickSwamp, props).setRegistryName(biomeBrickSwamp.getRegistryName()));
		r.register(new BlockItem(biomeBrickSwampSlab, props).setRegistryName(biomeBrickSwampSlab.getRegistryName()));
		r.register(new BlockItem(biomeBrickSwampStairs, props).setRegistryName(biomeBrickSwampStairs.getRegistryName()));
		r.register(new BlockItem(biomeCobblestoneSwamp, props).setRegistryName(biomeCobblestoneSwamp.getRegistryName()));
		r.register(new BlockItem(biomeCobblestoneSwampSlab, props).setRegistryName(biomeCobblestoneSwampSlab.getRegistryName()));
		r.register(new BlockItem(biomeCobblestoneSwampStairs, props).setRegistryName(biomeCobblestoneSwampStairs.getRegistryName()));
		r.register(new BlockItem(biomeWallSwamp, props).setRegistryName(biomeWallSwamp.getRegistryName()));
		r.register(new BlockItem(biomeChiseledBrickSwamp, props).setRegistryName(biomeChiseledBrickSwamp.getRegistryName()));

		r.register(new BlockItem(biomeStoneDesert, props).setRegistryName(biomeStoneDesert.getRegistryName()));
		r.register(new BlockItem(biomeStoneDesertSlab, props).setRegistryName(biomeStoneDesertSlab.getRegistryName()));
		r.register(new BlockItem(biomeStoneDesertStairs, props).setRegistryName(biomeStoneDesertStairs.getRegistryName()));
		r.register(new BlockItem(biomeBrickDesert, props).setRegistryName(biomeBrickDesert.getRegistryName()));
		r.register(new BlockItem(biomeBrickDesertSlab, props).setRegistryName(biomeBrickDesertSlab.getRegistryName()));
		r.register(new BlockItem(biomeBrickDesertStairs, props).setRegistryName(biomeBrickDesertStairs.getRegistryName()));
		r.register(new BlockItem(biomeCobblestoneDesert, props).setRegistryName(biomeCobblestoneDesert.getRegistryName()));
		r.register(new BlockItem(biomeCobblestoneDesertSlab, props).setRegistryName(biomeCobblestoneDesertSlab.getRegistryName()));
		r.register(new BlockItem(biomeCobblestoneDesertStairs, props).setRegistryName(biomeCobblestoneDesertStairs.getRegistryName()));
		r.register(new BlockItem(biomeWallDesert, props).setRegistryName(biomeWallDesert.getRegistryName()));
		r.register(new BlockItem(biomeChiseledBrickDesert, props).setRegistryName(biomeChiseledBrickDesert.getRegistryName()));

		r.register(new BlockItem(biomeStoneTaiga, props).setRegistryName(biomeStoneTaiga.getRegistryName()));
		r.register(new BlockItem(biomeStoneTaigaSlab, props).setRegistryName(biomeStoneTaigaSlab.getRegistryName()));
		r.register(new BlockItem(biomeStoneTaigaStairs, props).setRegistryName(biomeStoneTaigaStairs.getRegistryName()));
		r.register(new BlockItem(biomeBrickTaiga, props).setRegistryName(biomeBrickTaiga.getRegistryName()));
		r.register(new BlockItem(biomeBrickTaigaSlab, props).setRegistryName(biomeBrickTaigaSlab.getRegistryName()));
		r.register(new BlockItem(biomeBrickTaigaStairs, props).setRegistryName(biomeBrickTaigaStairs.getRegistryName()));
		r.register(new BlockItem(biomeCobblestoneTaiga, props).setRegistryName(biomeCobblestoneTaiga.getRegistryName()));
		r.register(new BlockItem(biomeCobblestoneTaigaSlab, props).setRegistryName(biomeCobblestoneTaigaSlab.getRegistryName()));
		r.register(new BlockItem(biomeCobblestoneTaigaStairs, props).setRegistryName(biomeCobblestoneTaigaStairs.getRegistryName()));
		r.register(new BlockItem(biomeWallTaiga, props).setRegistryName(biomeWallTaiga.getRegistryName()));
		r.register(new BlockItem(biomeChiseledBrickTaiga, props).setRegistryName(biomeChiseledBrickTaiga.getRegistryName()));

		r.register(new BlockItem(biomeStoneMesa, props).setRegistryName(biomeStoneMesa.getRegistryName()));
		r.register(new BlockItem(biomeStoneMesaSlab, props).setRegistryName(biomeStoneMesaSlab.getRegistryName()));
		r.register(new BlockItem(biomeStoneMesaStairs, props).setRegistryName(biomeStoneMesaStairs.getRegistryName()));
		r.register(new BlockItem(biomeBrickMesa, props).setRegistryName(biomeBrickMesa.getRegistryName()));
		r.register(new BlockItem(biomeBrickMesaSlab, props).setRegistryName(biomeBrickMesaSlab.getRegistryName()));
		r.register(new BlockItem(biomeBrickMesaStairs, props).setRegistryName(biomeBrickMesaStairs.getRegistryName()));
		r.register(new BlockItem(biomeCobblestoneMesa, props).setRegistryName(biomeCobblestoneMesa.getRegistryName()));
		r.register(new BlockItem(biomeCobblestoneMesaSlab, props).setRegistryName(biomeCobblestoneMesaSlab.getRegistryName()));
		r.register(new BlockItem(biomeCobblestoneMesaStairs, props).setRegistryName(biomeCobblestoneMesaStairs.getRegistryName()));
		r.register(new BlockItem(biomeWallMesa, props).setRegistryName(biomeWallMesa.getRegistryName()));
		r.register(new BlockItem(biomeChiseledBrickMesa, props).setRegistryName(biomeChiseledBrickMesa.getRegistryName()));

		r.register(new BlockItem(whitePavement, props).setRegistryName(whitePavement.getRegistryName()));
		r.register(new BlockItem(blackPavement, props).setRegistryName(blackPavement.getRegistryName()));
		r.register(new BlockItem(bluePavement, props).setRegistryName(bluePavement.getRegistryName()));
		r.register(new BlockItem(yellowPavement, props).setRegistryName(yellowPavement.getRegistryName()));
		r.register(new BlockItem(redPavement, props).setRegistryName(redPavement.getRegistryName()));
		r.register(new BlockItem(greenPavement, props).setRegistryName(greenPavement.getRegistryName()));

		r.register(new BlockItem(whitePavementSlab, props).setRegistryName(whitePavementSlab.getRegistryName()));
		r.register(new BlockItem(blackPavementSlab, props).setRegistryName(blackPavementSlab.getRegistryName()));
		r.register(new BlockItem(bluePavementSlab, props).setRegistryName(bluePavementSlab.getRegistryName()));
		r.register(new BlockItem(yellowPavementSlab, props).setRegistryName(yellowPavementSlab.getRegistryName()));
		r.register(new BlockItem(redPavementSlab, props).setRegistryName(redPavementSlab.getRegistryName()));
		r.register(new BlockItem(greenPavementSlab, props).setRegistryName(greenPavementSlab.getRegistryName()));

		r.register(new BlockItem(whitePavementStair, props).setRegistryName(whitePavementStair.getRegistryName()));
		r.register(new BlockItem(blackPavementStair, props).setRegistryName(blackPavementStair.getRegistryName()));
		r.register(new BlockItem(bluePavementStair, props).setRegistryName(bluePavementStair.getRegistryName()));
		r.register(new BlockItem(yellowPavementStair, props).setRegistryName(yellowPavementStair.getRegistryName()));
		r.register(new BlockItem(redPavementStair, props).setRegistryName(redPavementStair.getRegistryName()));
		r.register(new BlockItem(greenPavementStair, props).setRegistryName(greenPavementStair.getRegistryName()));

		r.register(new BlockItem(shimmerrockSlab, props).setRegistryName(shimmerrockSlab.getRegistryName()));
		r.register(new BlockItem(shimmerrockStairs, props).setRegistryName(shimmerrockStairs.getRegistryName()));

		r.register(new BlockItem(shimmerwoodPlankSlab, props).setRegistryName(shimmerwoodPlankSlab.getRegistryName()));
		r.register(new BlockItem(shimmerwoodPlankStairs, props).setRegistryName(shimmerwoodPlankStairs.getRegistryName()));

		r.register(new BlockItem(managlassPane, props).setRegistryName(managlassPane.getRegistryName()));
		r.register(new BlockItem(alfglassPane, props).setRegistryName(alfglassPane.getRegistryName()));
		r.register(new BlockItem(bifrostPane, props).setRegistryName(bifrostPane.getRegistryName()));
	}

}
