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

	@ObjectHolder(LibBlockNames.LIVING_WOOD + STAIR_SUFFIX)
	public static Block livingwoodStairs;
	@ObjectHolder(LibBlockNames.LIVING_WOOD + SLAB_SUFFIX)
	public static Block livingwoodSlab;
	@ObjectHolder(LibBlockNames.LIVING_WOOD + WALL_SUFFIX)
	public static Block livingwoodWall;
	@ObjectHolder(LibBlockNames.LIVING_WOOD + FENCE_SUFFIX)
	public static Block livingwoodFence;
	@ObjectHolder(LibBlockNames.LIVING_WOOD + FENCE_GATE_SUFFIX)
	public static Block livingwoodFenceGate;
	@ObjectHolder(LibBlockNames.LIVING_WOOD_PLANKS + STAIR_SUFFIX)
	public static Block livingwoodPlankStairs;
	@ObjectHolder(LibBlockNames.LIVING_WOOD_PLANKS + SLAB_SUFFIX)
	public static Block livingwoodPlankSlab;
	@ObjectHolder(LibBlockNames.LIVING_ROCK + STAIR_SUFFIX)
	public static Block livingrockStairs;
	@ObjectHolder(LibBlockNames.LIVING_ROCK + SLAB_SUFFIX)
	public static Block livingrockSlab;
	@ObjectHolder(LibBlockNames.LIVING_ROCK + WALL_SUFFIX)
	public static Block livingrockWall;
	@ObjectHolder(LibBlockNames.LIVING_ROCK_BRICK + STAIR_SUFFIX)
	public static Block livingrockBrickStairs;
	@ObjectHolder(LibBlockNames.LIVING_ROCK_BRICK + SLAB_SUFFIX)
	public static Block livingrockBrickSlab;
	@ObjectHolder(LibBlockNames.DREAM_WOOD + STAIR_SUFFIX)
	public static Block dreamwoodStairs;
	@ObjectHolder(LibBlockNames.DREAM_WOOD + SLAB_SUFFIX)
	public static Block dreamwoodSlab;
	@ObjectHolder(LibBlockNames.DREAM_WOOD + WALL_SUFFIX)
	public static Block dreamwoodWall;
	@ObjectHolder(LibBlockNames.DREAM_WOOD + FENCE_SUFFIX)
	public static Block dreamwoodFence;
	@ObjectHolder(LibBlockNames.DREAM_WOOD + FENCE_GATE_SUFFIX)
	public static Block dreamwoodFenceGate;
	@ObjectHolder(LibBlockNames.DREAM_WOOD_PLANKS + STAIR_SUFFIX)
	public static Block dreamwoodPlankStairs;
	@ObjectHolder(LibBlockNames.DREAM_WOOD_PLANKS + SLAB_SUFFIX)
	public static Block dreamwoodPlankSlab;

	@ObjectHolder(LibBlockNames.QUARTZ_DARK)
	public static Block darkQuartz;
	@ObjectHolder("chiseled_" + LibBlockNames.QUARTZ_DARK)
	public static Block darkQuartzChiseled;
	@ObjectHolder(LibBlockNames.QUARTZ_DARK + "_pillar")
	public static Block darkQuartzPillar;
	@ObjectHolder(LibBlockNames.QUARTZ_DARK + SLAB_SUFFIX)
	public static Block darkQuartzSlab;
	@ObjectHolder(LibBlockNames.QUARTZ_DARK + STAIR_SUFFIX)
	public static Block darkQuartzStairs;

	@ObjectHolder(LibBlockNames.QUARTZ_MANA)
	public static Block manaQuartz;
	@ObjectHolder("chiseled_" + LibBlockNames.QUARTZ_MANA)
	public static Block manaQuartzChiseled;
	@ObjectHolder(LibBlockNames.QUARTZ_MANA + "_pillar")
	public static Block manaQuartzPillar;
	@ObjectHolder(LibBlockNames.QUARTZ_MANA + SLAB_SUFFIX)
	public static Block manaQuartzSlab;
	@ObjectHolder(LibBlockNames.QUARTZ_MANA + STAIR_SUFFIX)
	public static Block manaQuartzStairs;

	@ObjectHolder(LibBlockNames.QUARTZ_BLAZE)
	public static Block blazeQuartz;
	@ObjectHolder("chiseled_" + LibBlockNames.QUARTZ_BLAZE)
	public static Block blazeQuartzChiseled;
	@ObjectHolder(LibBlockNames.QUARTZ_BLAZE + "_pillar")
	public static Block blazeQuartzPillar;
	@ObjectHolder(LibBlockNames.QUARTZ_BLAZE + SLAB_SUFFIX)
	public static Block blazeQuartzSlab;
	@ObjectHolder(LibBlockNames.QUARTZ_BLAZE + STAIR_SUFFIX)
	public static Block blazeQuartzStairs;

	@ObjectHolder(LibBlockNames.QUARTZ_LAVENDER)
	public static Block lavenderQuartz;
	@ObjectHolder("chiseled_" + LibBlockNames.QUARTZ_LAVENDER)
	public static Block lavenderQuartzChiseled;
	@ObjectHolder(LibBlockNames.QUARTZ_LAVENDER + "_pillar")
	public static Block lavenderQuartzPillar;
	@ObjectHolder(LibBlockNames.QUARTZ_LAVENDER + SLAB_SUFFIX)
	public static Block lavenderQuartzSlab;
	@ObjectHolder(LibBlockNames.QUARTZ_LAVENDER + STAIR_SUFFIX)
	public static Block lavenderQuartzStairs;

	@ObjectHolder(LibBlockNames.QUARTZ_RED)
	public static Block redQuartz;
	@ObjectHolder("chiseled_" + LibBlockNames.QUARTZ_RED)
	public static Block redQuartzChiseled;
	@ObjectHolder(LibBlockNames.QUARTZ_RED + "_pillar")
	public static Block redQuartzPillar;
	@ObjectHolder(LibBlockNames.QUARTZ_RED + SLAB_SUFFIX)
	public static Block redQuartzSlab;
	@ObjectHolder(LibBlockNames.QUARTZ_RED + STAIR_SUFFIX)
	public static Block redQuartzStairs;

	@ObjectHolder(LibBlockNames.QUARTZ_ELF)
	public static Block elfQuartz;
	@ObjectHolder("chiseled_" + LibBlockNames.QUARTZ_ELF)
	public static Block elfQuartzChiseled;
	@ObjectHolder(LibBlockNames.QUARTZ_ELF + "_pillar")
	public static Block elfQuartzPillar;
	@ObjectHolder(LibBlockNames.QUARTZ_ELF + SLAB_SUFFIX)
	public static Block elfQuartzSlab;
	@ObjectHolder(LibBlockNames.QUARTZ_ELF + STAIR_SUFFIX)
	public static Block elfQuartzStairs;

	@ObjectHolder(LibBlockNames.QUARTZ_SUNNY)
	public static Block sunnyQuartz;
	@ObjectHolder("chiseled_" + LibBlockNames.QUARTZ_SUNNY)
	public static Block sunnyQuartzChiseled;
	@ObjectHolder(LibBlockNames.QUARTZ_SUNNY + "_pillar")
	public static Block sunnyQuartzPillar;
	@ObjectHolder(LibBlockNames.QUARTZ_SUNNY + SLAB_SUFFIX)
	public static Block sunnyQuartzSlab;
	@ObjectHolder(LibBlockNames.QUARTZ_SUNNY + STAIR_SUFFIX)
	public static Block sunnyQuartzStairs;

	@ObjectHolder(LibBlockNames.METAMORPHIC_PREFIX + "forest_stone")
	public static Block biomeStoneForest;
	@ObjectHolder(LibBlockNames.METAMORPHIC_PREFIX + "forest_stone" + SLAB_SUFFIX)
	public static Block biomeStoneForestSlab;
	@ObjectHolder(LibBlockNames.METAMORPHIC_PREFIX + "forest_stone" + STAIR_SUFFIX)
	public static Block biomeStoneForestStairs;
	@ObjectHolder(LibBlockNames.METAMORPHIC_PREFIX + "forest_cobblestone")
	public static Block biomeCobblestoneForest;
	@ObjectHolder(LibBlockNames.METAMORPHIC_PREFIX + "forest_cobblestone" + SLAB_SUFFIX)
	public static Block biomeCobblestoneForestSlab;
	@ObjectHolder(LibBlockNames.METAMORPHIC_PREFIX + "forest_cobblestone" + STAIR_SUFFIX)
	public static Block biomeCobblestoneForestStairs;
	@ObjectHolder(LibBlockNames.METAMORPHIC_PREFIX + "forest_cobblestone" + WALL_SUFFIX)
	public static Block biomeWallForest;
	@ObjectHolder(LibBlockNames.METAMORPHIC_PREFIX + "forest_bricks")
	public static Block biomeBrickForest;
	@ObjectHolder(LibBlockNames.METAMORPHIC_PREFIX + "forest_bricks" + SLAB_SUFFIX)
	public static Block biomeBrickForestSlab;
	@ObjectHolder(LibBlockNames.METAMORPHIC_PREFIX + "forest_bricks" + STAIR_SUFFIX)
	public static Block biomeBrickForestStairs;
	@ObjectHolder("chiseled_" + LibBlockNames.METAMORPHIC_PREFIX + "forest_bricks")
	public static Block biomeChiseledBrickForest;

	@ObjectHolder(LibBlockNames.METAMORPHIC_PREFIX + "plains_stone")
	public static Block biomeStonePlains;
	@ObjectHolder(LibBlockNames.METAMORPHIC_PREFIX + "plains_stone" + SLAB_SUFFIX)
	public static Block biomeStonePlainsSlab;
	@ObjectHolder(LibBlockNames.METAMORPHIC_PREFIX + "plains_stone" + STAIR_SUFFIX)
	public static Block biomeStonePlainsStairs;
	@ObjectHolder(LibBlockNames.METAMORPHIC_PREFIX + "plains_cobblestone")
	public static Block biomeCobblestonePlains;
	@ObjectHolder(LibBlockNames.METAMORPHIC_PREFIX + "plains_cobblestone" + SLAB_SUFFIX)
	public static Block biomeCobblestonePlainsSlab;
	@ObjectHolder(LibBlockNames.METAMORPHIC_PREFIX + "plains_cobblestone" + STAIR_SUFFIX)
	public static Block biomeCobblestonePlainsStairs;
	@ObjectHolder(LibBlockNames.METAMORPHIC_PREFIX + "plains_cobblestone" + WALL_SUFFIX)
	public static Block biomeWallPlains;
	@ObjectHolder(LibBlockNames.METAMORPHIC_PREFIX + "plains_bricks")
	public static Block biomeBrickPlains;
	@ObjectHolder(LibBlockNames.METAMORPHIC_PREFIX + "plains_bricks" + SLAB_SUFFIX)
	public static Block biomeBrickPlainsSlab;
	@ObjectHolder(LibBlockNames.METAMORPHIC_PREFIX + "plains_bricks" + STAIR_SUFFIX)
	public static Block biomeBrickPlainsStairs;
	@ObjectHolder("chiseled_" + LibBlockNames.METAMORPHIC_PREFIX + "plains_bricks")
	public static Block biomeChiseledBrickPlains;

	@ObjectHolder(LibBlockNames.METAMORPHIC_PREFIX + "mountain_stone")
	public static Block biomeStoneMountain;
	@ObjectHolder(LibBlockNames.METAMORPHIC_PREFIX + "mountain_stone" + SLAB_SUFFIX)
	public static Block biomeStoneMountainSlab;
	@ObjectHolder(LibBlockNames.METAMORPHIC_PREFIX + "mountain_stone" + STAIR_SUFFIX)
	public static Block biomeStoneMountainStairs;
	@ObjectHolder(LibBlockNames.METAMORPHIC_PREFIX + "mountain_cobblestone")
	public static Block biomeCobblestoneMountain;
	@ObjectHolder(LibBlockNames.METAMORPHIC_PREFIX + "mountain_cobblestone" + SLAB_SUFFIX)
	public static Block biomeCobblestoneMountainSlab;
	@ObjectHolder(LibBlockNames.METAMORPHIC_PREFIX + "mountain_cobblestone" + STAIR_SUFFIX)
	public static Block biomeCobblestoneMountainStairs;
	@ObjectHolder(LibBlockNames.METAMORPHIC_PREFIX + "mountain_cobblestone" + WALL_SUFFIX)
	public static Block biomeWallMountain;
	@ObjectHolder(LibBlockNames.METAMORPHIC_PREFIX + "mountain_bricks")
	public static Block biomeBrickMountain;
	@ObjectHolder(LibBlockNames.METAMORPHIC_PREFIX + "mountain_bricks" + SLAB_SUFFIX)
	public static Block biomeBrickMountainSlab;
	@ObjectHolder(LibBlockNames.METAMORPHIC_PREFIX + "mountain_bricks" + STAIR_SUFFIX)
	public static Block biomeBrickMountainStairs;
	@ObjectHolder("chiseled_" + LibBlockNames.METAMORPHIC_PREFIX + "mountain_bricks")
	public static Block biomeChiseledBrickMountain;

	@ObjectHolder(LibBlockNames.METAMORPHIC_PREFIX + "fungal_stone")
	public static Block biomeStoneFungal;
	@ObjectHolder(LibBlockNames.METAMORPHIC_PREFIX + "fungal_stone" + SLAB_SUFFIX)
	public static Block biomeStoneFungalSlab;
	@ObjectHolder(LibBlockNames.METAMORPHIC_PREFIX + "fungal_stone" + STAIR_SUFFIX)
	public static Block biomeStoneFungalStairs;
	@ObjectHolder(LibBlockNames.METAMORPHIC_PREFIX + "fungal_cobblestone")
	public static Block biomeCobblestoneFungal;
	@ObjectHolder(LibBlockNames.METAMORPHIC_PREFIX + "fungal_cobblestone" + SLAB_SUFFIX)
	public static Block biomeCobblestoneFungalSlab;
	@ObjectHolder(LibBlockNames.METAMORPHIC_PREFIX + "fungal_cobblestone" + STAIR_SUFFIX)
	public static Block biomeCobblestoneFungalStairs;
	@ObjectHolder(LibBlockNames.METAMORPHIC_PREFIX + "fungal_cobblestone" + WALL_SUFFIX)
	public static Block biomeWallFungal;
	@ObjectHolder(LibBlockNames.METAMORPHIC_PREFIX + "fungal_bricks")
	public static Block biomeBrickFungal;
	@ObjectHolder(LibBlockNames.METAMORPHIC_PREFIX + "fungal_bricks" + SLAB_SUFFIX)
	public static Block biomeBrickFungalSlab;
	@ObjectHolder(LibBlockNames.METAMORPHIC_PREFIX + "fungal_bricks" + STAIR_SUFFIX)
	public static Block biomeBrickFungalStairs;
	@ObjectHolder("chiseled_" + LibBlockNames.METAMORPHIC_PREFIX + "fungal_bricks")
	public static Block biomeChiseledBrickFungal;

	@ObjectHolder(LibBlockNames.METAMORPHIC_PREFIX + "swamp_stone")
	public static Block biomeStoneSwamp;
	@ObjectHolder(LibBlockNames.METAMORPHIC_PREFIX + "swamp_stone" + SLAB_SUFFIX)
	public static Block biomeStoneSwampSlab;
	@ObjectHolder(LibBlockNames.METAMORPHIC_PREFIX + "swamp_stone" + STAIR_SUFFIX)
	public static Block biomeStoneSwampStairs;
	@ObjectHolder(LibBlockNames.METAMORPHIC_PREFIX + "swamp_cobblestone")
	public static Block biomeCobblestoneSwamp;
	@ObjectHolder(LibBlockNames.METAMORPHIC_PREFIX + "swamp_cobblestone" + SLAB_SUFFIX)
	public static Block biomeCobblestoneSwampSlab;
	@ObjectHolder(LibBlockNames.METAMORPHIC_PREFIX + "swamp_cobblestone" + STAIR_SUFFIX)
	public static Block biomeCobblestoneSwampStairs;
	@ObjectHolder(LibBlockNames.METAMORPHIC_PREFIX + "swamp_cobblestone" + WALL_SUFFIX)
	public static Block biomeWallSwamp;
	@ObjectHolder(LibBlockNames.METAMORPHIC_PREFIX + "swamp_bricks")
	public static Block biomeBrickSwamp;
	@ObjectHolder(LibBlockNames.METAMORPHIC_PREFIX + "swamp_bricks" + SLAB_SUFFIX)
	public static Block biomeBrickSwampSlab;
	@ObjectHolder(LibBlockNames.METAMORPHIC_PREFIX + "swamp_bricks" + STAIR_SUFFIX)
	public static Block biomeBrickSwampStairs;
	@ObjectHolder("chiseled_" + LibBlockNames.METAMORPHIC_PREFIX + "swamp_bricks")
	public static Block biomeChiseledBrickSwamp;

	@ObjectHolder(LibBlockNames.METAMORPHIC_PREFIX + "desert_stone")
	public static Block biomeStoneDesert;
	@ObjectHolder(LibBlockNames.METAMORPHIC_PREFIX + "desert_stone" + SLAB_SUFFIX)
	public static Block biomeStoneDesertSlab;
	@ObjectHolder(LibBlockNames.METAMORPHIC_PREFIX + "desert_stone" + STAIR_SUFFIX)
	public static Block biomeStoneDesertStairs;
	@ObjectHolder(LibBlockNames.METAMORPHIC_PREFIX + "desert_cobblestone")
	public static Block biomeCobblestoneDesert;
	@ObjectHolder(LibBlockNames.METAMORPHIC_PREFIX + "desert_cobblestone" + SLAB_SUFFIX)
	public static Block biomeCobblestoneDesertSlab;
	@ObjectHolder(LibBlockNames.METAMORPHIC_PREFIX + "desert_cobblestone" + STAIR_SUFFIX)
	public static Block biomeCobblestoneDesertStairs;
	@ObjectHolder(LibBlockNames.METAMORPHIC_PREFIX + "desert_cobblestone" + WALL_SUFFIX)
	public static Block biomeWallDesert;
	@ObjectHolder(LibBlockNames.METAMORPHIC_PREFIX + "desert_bricks")
	public static Block biomeBrickDesert;
	@ObjectHolder(LibBlockNames.METAMORPHIC_PREFIX + "desert_bricks" + SLAB_SUFFIX)
	public static Block biomeBrickDesertSlab;
	@ObjectHolder(LibBlockNames.METAMORPHIC_PREFIX + "desert_bricks" + STAIR_SUFFIX)
	public static Block biomeBrickDesertStairs;
	@ObjectHolder("chiseled_" + LibBlockNames.METAMORPHIC_PREFIX + "desert_bricks")
	public static Block biomeChiseledBrickDesert;

	@ObjectHolder(LibBlockNames.METAMORPHIC_PREFIX + "taiga_stone")
	public static Block biomeStoneTaiga;
	@ObjectHolder(LibBlockNames.METAMORPHIC_PREFIX + "taiga_stone" + SLAB_SUFFIX)
	public static Block biomeStoneTaigaSlab;
	@ObjectHolder(LibBlockNames.METAMORPHIC_PREFIX + "taiga_stone" + STAIR_SUFFIX)
	public static Block biomeStoneTaigaStairs;
	@ObjectHolder(LibBlockNames.METAMORPHIC_PREFIX + "taiga_cobblestone")
	public static Block biomeCobblestoneTaiga;
	@ObjectHolder(LibBlockNames.METAMORPHIC_PREFIX + "taiga_cobblestone" + SLAB_SUFFIX)
	public static Block biomeCobblestoneTaigaSlab;
	@ObjectHolder(LibBlockNames.METAMORPHIC_PREFIX + "taiga_cobblestone" + STAIR_SUFFIX)
	public static Block biomeCobblestoneTaigaStairs;
	@ObjectHolder(LibBlockNames.METAMORPHIC_PREFIX + "taiga_cobblestone" + WALL_SUFFIX)
	public static Block biomeWallTaiga;
	@ObjectHolder(LibBlockNames.METAMORPHIC_PREFIX + "taiga_bricks")
	public static Block biomeBrickTaiga;
	@ObjectHolder(LibBlockNames.METAMORPHIC_PREFIX + "taiga_bricks" + SLAB_SUFFIX)
	public static Block biomeBrickTaigaSlab;
	@ObjectHolder(LibBlockNames.METAMORPHIC_PREFIX + "taiga_bricks" + STAIR_SUFFIX)
	public static Block biomeBrickTaigaStairs;
	@ObjectHolder("chiseled_" + LibBlockNames.METAMORPHIC_PREFIX + "taiga_bricks")
	public static Block biomeChiseledBrickTaiga;

	@ObjectHolder(LibBlockNames.METAMORPHIC_PREFIX + "mesa_stone")
	public static Block biomeStoneMesa;
	@ObjectHolder(LibBlockNames.METAMORPHIC_PREFIX + "mesa_stone" + SLAB_SUFFIX)
	public static Block biomeStoneMesaSlab;
	@ObjectHolder(LibBlockNames.METAMORPHIC_PREFIX + "mesa_stone" + STAIR_SUFFIX)
	public static Block biomeStoneMesaStairs;
	@ObjectHolder(LibBlockNames.METAMORPHIC_PREFIX + "mesa_cobblestone")
	public static Block biomeCobblestoneMesa;
	@ObjectHolder(LibBlockNames.METAMORPHIC_PREFIX + "mesa_cobblestone" + SLAB_SUFFIX)
	public static Block biomeCobblestoneMesaSlab;
	@ObjectHolder(LibBlockNames.METAMORPHIC_PREFIX + "mesa_cobblestone" + STAIR_SUFFIX)
	public static Block biomeCobblestoneMesaStairs;
	@ObjectHolder(LibBlockNames.METAMORPHIC_PREFIX + "mesa_cobblestone" + WALL_SUFFIX)
	public static Block biomeWallMesa;
	@ObjectHolder(LibBlockNames.METAMORPHIC_PREFIX + "mesa_bricks")
	public static Block biomeBrickMesa;
	@ObjectHolder(LibBlockNames.METAMORPHIC_PREFIX + "mesa_bricks" + SLAB_SUFFIX)
	public static Block biomeBrickMesaSlab;
	@ObjectHolder(LibBlockNames.METAMORPHIC_PREFIX + "mesa_bricks" + STAIR_SUFFIX)
	public static Block biomeBrickMesaStairs;
	@ObjectHolder("chiseled_" + LibBlockNames.METAMORPHIC_PREFIX + "mesa_bricks")
	public static Block biomeChiseledBrickMesa;

	@ObjectHolder("white" + LibBlockNames.PAVEMENT_SUFFIX)
	public static Block whitePavement;
	@ObjectHolder("black" + LibBlockNames.PAVEMENT_SUFFIX)
	public static Block blackPavement;
	@ObjectHolder("blue" + LibBlockNames.PAVEMENT_SUFFIX)
	public static Block bluePavement;
	@ObjectHolder("yellow" + LibBlockNames.PAVEMENT_SUFFIX)
	public static Block yellowPavement;
	@ObjectHolder("red" + LibBlockNames.PAVEMENT_SUFFIX)
	public static Block redPavement;
	@ObjectHolder("green" + LibBlockNames.PAVEMENT_SUFFIX)
	public static Block greenPavement;

	@ObjectHolder("white" + LibBlockNames.PAVEMENT_SUFFIX + SLAB_SUFFIX)
	public static Block whitePavementSlab;
	@ObjectHolder("black" + LibBlockNames.PAVEMENT_SUFFIX + SLAB_SUFFIX)
	public static Block blackPavementSlab;
	@ObjectHolder("blue" + LibBlockNames.PAVEMENT_SUFFIX + SLAB_SUFFIX)
	public static Block bluePavementSlab;
	@ObjectHolder("yellow" + LibBlockNames.PAVEMENT_SUFFIX + SLAB_SUFFIX)
	public static Block yellowPavementSlab;
	@ObjectHolder("red" + LibBlockNames.PAVEMENT_SUFFIX + SLAB_SUFFIX)
	public static Block redPavementSlab;
	@ObjectHolder("green" + LibBlockNames.PAVEMENT_SUFFIX + SLAB_SUFFIX)
	public static Block greenPavementSlab;

	@ObjectHolder("white" + LibBlockNames.PAVEMENT_SUFFIX + STAIR_SUFFIX)
	public static Block whitePavementStair;
	@ObjectHolder("black" + LibBlockNames.PAVEMENT_SUFFIX + STAIR_SUFFIX)
	public static Block blackPavementStair;
	@ObjectHolder("blue" + LibBlockNames.PAVEMENT_SUFFIX + STAIR_SUFFIX)
	public static Block bluePavementStair;
	@ObjectHolder("yellow" + LibBlockNames.PAVEMENT_SUFFIX + STAIR_SUFFIX)
	public static Block yellowPavementStair;
	@ObjectHolder("red" + LibBlockNames.PAVEMENT_SUFFIX + STAIR_SUFFIX)
	public static Block redPavementStair;
	@ObjectHolder("green" + LibBlockNames.PAVEMENT_SUFFIX + STAIR_SUFFIX)
	public static Block greenPavementStair;

	@ObjectHolder(LibBlockNames.SHIMMERROCK + SLAB_SUFFIX)
	public static Block shimmerrockSlab;
	@ObjectHolder(LibBlockNames.SHIMMERROCK + STAIR_SUFFIX)
	public static Block shimmerrockStairs;
	@ObjectHolder(LibBlockNames.SHIMMERWOOD_PLANKS + SLAB_SUFFIX)
	public static Block shimmerwoodPlankSlab;
	@ObjectHolder(LibBlockNames.SHIMMERWOOD_PLANKS + STAIR_SUFFIX)
	public static Block shimmerwoodPlankStairs;

	@ObjectHolder(LibBlockNames.MANA_GLASS + "_pane")
	public static Block managlassPane;
	@ObjectHolder(LibBlockNames.ELF_GLASS + "_pane")
	public static Block alfglassPane;
	@ObjectHolder(LibBlockNames.BIFROST + "_pane")
	public static Block bifrostPane;

	public static void registerBlocks(RegistryEvent.Register<Block> evt) {
		IForgeRegistry<Block> r = evt.getRegistry();

		Block base = r.getValue(prefix(LibBlockNames.LIVING_WOOD));
		register(r, LibBlockNames.LIVING_WOOD + STAIR_SUFFIX, new BlockModStairs(base.getDefaultState(), Block.Properties.from(base)));
		register(r, LibBlockNames.LIVING_WOOD + SLAB_SUFFIX, new SlabBlock(Block.Properties.from(base)));
		register(r, LibBlockNames.LIVING_WOOD + WALL_SUFFIX, new WallBlock(Block.Properties.from(base)));
		register(r, LibBlockNames.LIVING_WOOD + FENCE_SUFFIX, new FenceBlock(Block.Properties.from(base)));
		register(r, LibBlockNames.LIVING_WOOD + FENCE_GATE_SUFFIX, new FenceGateBlock(Block.Properties.from(base)));

		base = r.getValue(prefix(LibBlockNames.LIVING_WOOD_PLANKS));
		register(r, LibBlockNames.LIVING_WOOD_PLANKS + STAIR_SUFFIX, new BlockModStairs(base.getDefaultState(), Block.Properties.from(base)));
		register(r, LibBlockNames.LIVING_WOOD_PLANKS + SLAB_SUFFIX, new SlabBlock(Block.Properties.from(base)));

		base = r.getValue(prefix(LibBlockNames.LIVING_ROCK));
		register(r, LibBlockNames.LIVING_ROCK + STAIR_SUFFIX, new BlockModStairs(base.getDefaultState(), Block.Properties.from(base)));
		register(r, LibBlockNames.LIVING_ROCK + SLAB_SUFFIX, new SlabBlock(Block.Properties.from(base)));
		register(r, LibBlockNames.LIVING_ROCK + WALL_SUFFIX, new WallBlock(Block.Properties.from(base)));

		base = r.getValue(prefix(LibBlockNames.LIVING_ROCK_BRICK));
		register(r, LibBlockNames.LIVING_ROCK_BRICK + STAIR_SUFFIX, new BlockModStairs(base.getDefaultState(), Block.Properties.from(base)));
		register(r, LibBlockNames.LIVING_ROCK_BRICK + SLAB_SUFFIX, new SlabBlock(Block.Properties.from(base)));

		base = r.getValue(prefix(LibBlockNames.DREAM_WOOD));
		register(r, LibBlockNames.DREAM_WOOD + STAIR_SUFFIX, new BlockModStairs(base.getDefaultState(), Block.Properties.from(base)));
		register(r, LibBlockNames.DREAM_WOOD + SLAB_SUFFIX, new SlabBlock(Block.Properties.from(base)));
		register(r, LibBlockNames.DREAM_WOOD + WALL_SUFFIX, new WallBlock(Block.Properties.from(base)));
		register(r, LibBlockNames.DREAM_WOOD + FENCE_SUFFIX, new FenceBlock(Block.Properties.from(base)));
		register(r, LibBlockNames.DREAM_WOOD + FENCE_GATE_SUFFIX, new FenceGateBlock(Block.Properties.from(base)));

		base = r.getValue(prefix(LibBlockNames.DREAM_WOOD_PLANKS));
		register(r, LibBlockNames.DREAM_WOOD_PLANKS + STAIR_SUFFIX, new BlockModStairs(base.getDefaultState(), Block.Properties.from(base)));
		register(r, LibBlockNames.DREAM_WOOD_PLANKS + SLAB_SUFFIX, new SlabBlock(Block.Properties.from(base)));

		Block.Properties props = Block.Properties.from(Blocks.QUARTZ_BLOCK);
		for (String variant : LibBlockNames.QUARTZ_VARIANTS) {

			base = new BlockMod(props);
			register(r, variant, base);
			register(r, "chiseled_" + variant, new BlockMod(props));
			register(r, variant + "_pillar", new RotatedPillarBlock(props));
			register(r, variant + SLAB_SUFFIX, new SlabBlock(props));
			register(r, variant + STAIR_SUFFIX, new BlockModStairs(base.getDefaultState(), props));
		}

		props = Block.Properties.create(Material.ROCK).hardnessAndResistance(2, 10).sound(SoundType.STONE);
		for (String color : LibBlockNames.PAVEMENT_VARIANTS) {
			Block block = new BlockMod(props);
			register(r, color + LibBlockNames.PAVEMENT_SUFFIX, block);
			register(r, color + LibBlockNames.PAVEMENT_SUFFIX + STAIR_SUFFIX, new BlockModStairs(block.getDefaultState(), props));
			register(r, color + LibBlockNames.PAVEMENT_SUFFIX + SLAB_SUFFIX, new SlabBlock(props));
		}

		props = Block.Properties.create(Material.ROCK).hardnessAndResistance(1.5F, 10).sound(SoundType.STONE);
		for (String variant : LibBlockNames.METAMORPHIC_VARIANTS) {
			base = new BlockMod(props);
			register(r, LibBlockNames.METAMORPHIC_PREFIX + variant + "_stone", base);
			register(r, Registry.BLOCK.getKey(base).getPath() + STAIR_SUFFIX, new BlockModStairs(base.getDefaultState(), props));
			register(r, Registry.BLOCK.getKey(base).getPath() + SLAB_SUFFIX, new SlabBlock(props));

			base = new BlockMod(props);
			register(r, LibBlockNames.METAMORPHIC_PREFIX + variant + "_cobblestone", base);
			register(r, Registry.BLOCK.getKey(base).getPath() + STAIR_SUFFIX, new BlockModStairs(base.getDefaultState(), props));
			register(r, Registry.BLOCK.getKey(base).getPath() + SLAB_SUFFIX, new SlabBlock(props));
			register(r, LibBlockNames.METAMORPHIC_PREFIX + variant + "_cobblestone" + WALL_SUFFIX, new WallBlock(props));

			base = new BlockMod(props);
			register(r, LibBlockNames.METAMORPHIC_PREFIX + variant + "_bricks", base);
			register(r, Registry.BLOCK.getKey(base).getPath() + STAIR_SUFFIX, new BlockModStairs(base.getDefaultState(), props));
			register(r, Registry.BLOCK.getKey(base).getPath() + SLAB_SUFFIX, new SlabBlock(props));
			register(r, "chiseled_" + LibBlockNames.METAMORPHIC_PREFIX + variant + "_bricks", new BlockMod(props));
		}

		base = r.getValue(prefix(LibBlockNames.SHIMMERROCK));
		register(r, LibBlockNames.SHIMMERROCK + SLAB_SUFFIX, new SlabBlock(Block.Properties.from(base)));
		register(r, LibBlockNames.SHIMMERROCK + STAIR_SUFFIX, new BlockModStairs(base.getDefaultState(), Block.Properties.from(base)));

		base = r.getValue(prefix(LibBlockNames.SHIMMERWOOD_PLANKS));
		register(r, LibBlockNames.SHIMMERWOOD_PLANKS + SLAB_SUFFIX, new SlabBlock(Block.Properties.from(base)));
		register(r, LibBlockNames.SHIMMERWOOD_PLANKS + STAIR_SUFFIX, new BlockModStairs(base.getDefaultState(), Block.Properties.from(base)));

		base = r.getValue(prefix(LibBlockNames.MANA_GLASS));
		register(r, LibBlockNames.MANA_GLASS + "_pane", new BlockModPane(Block.Properties.from(base)));

		base = r.getValue(prefix(LibBlockNames.ELF_GLASS));
		register(r, LibBlockNames.ELF_GLASS + "_pane", new BlockModPane(Block.Properties.from(base)));

		base = r.getValue(prefix(LibBlockNames.BIFROST_PERM));
		register(r, LibBlockNames.BIFROST + "_pane", new BlockModPane(Block.Properties.from(base)));
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
