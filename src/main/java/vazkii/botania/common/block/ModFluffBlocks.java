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
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.registries.IForgeRegistry;
import net.minecraftforge.registries.ObjectHolder;
import vazkii.botania.api.lexicon.ILexiconable;
import vazkii.botania.common.block.decor.panes.BlockModPane;
import vazkii.botania.common.block.decor.quartz.BlockSpecialQuartzPillar;
import vazkii.botania.common.block.decor.quartz.BlockElfQuartzSlab;
import vazkii.botania.common.block.decor.quartz.BlockElfQuartzStairs;
import vazkii.botania.common.block.decor.slabs.BlockBiomeStoneSlab;
import vazkii.botania.common.block.decor.slabs.BlockModSlab;
import vazkii.botania.common.block.decor.slabs.BlockPavementSlab;
import vazkii.botania.common.block.decor.stairs.BlockBiomeStoneStairs;
import vazkii.botania.common.block.decor.stairs.BlockModStairs;
import vazkii.botania.common.block.decor.stairs.BlockPavementStairs;
import vazkii.botania.common.block.decor.walls.BlockBiomeStoneWall;
import vazkii.botania.common.block.decor.walls.BlockModWall;
import vazkii.botania.common.core.BotaniaCreativeTab;
import vazkii.botania.common.item.block.ItemBlockMod;
import vazkii.botania.common.lexicon.LexiconData;
import vazkii.botania.common.lib.LibBlockNames;
import vazkii.botania.common.lib.LibMisc;

import static vazkii.botania.common.block.ModBlocks.register;
import static vazkii.botania.common.lib.LibBlockNames.STAIR_SUFFIX;
import static vazkii.botania.common.lib.LibBlockNames.SLAB_SUFFIX;
import static vazkii.botania.common.lib.LibBlockNames.WALL_SUFFIX;

@Mod.EventBusSubscriber(modid = LibMisc.MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD)
@ObjectHolder(LibMisc.MOD_ID)
public final class ModFluffBlocks {

	@ObjectHolder(LibBlockNames.LIVING_WOOD + STAIR_SUFFIX) public static Block livingwoodStairs;
	@ObjectHolder(LibBlockNames.LIVING_WOOD + SLAB_SUFFIX) public static Block livingwoodSlab;
	@ObjectHolder(LibBlockNames.LIVING_WOOD + WALL_SUFFIX) public static Block livingwoodWall;
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
	@ObjectHolder(LibBlockNames.METAMORPHIC_PREFIX + "forest_cobblestone" + WALL_SUFFIX) public static Block biomeWallForest;
	@ObjectHolder(LibBlockNames.METAMORPHIC_PREFIX + "forest_bricks") public static Block biomeBrickForest;
	@ObjectHolder(LibBlockNames.METAMORPHIC_PREFIX + "forest_bricks" + SLAB_SUFFIX) public static Block biomeBrickForestSlab;
	@ObjectHolder(LibBlockNames.METAMORPHIC_PREFIX + "forest_bricks" + STAIR_SUFFIX) public static Block biomeBrickForestStairs;
	@ObjectHolder("chiseled_" + LibBlockNames.METAMORPHIC_PREFIX + "forest_bricks") public static Block biomeChiseledBrickForest;

	@ObjectHolder(LibBlockNames.METAMORPHIC_PREFIX + "plains_stone") public static Block biomeStonePlains;
	@ObjectHolder(LibBlockNames.METAMORPHIC_PREFIX + "plains_stone" + SLAB_SUFFIX) public static Block biomeStonePlainsSlab;
	@ObjectHolder(LibBlockNames.METAMORPHIC_PREFIX + "plains_stone" + STAIR_SUFFIX) public static Block biomeStonePlainsStairs;
	@ObjectHolder(LibBlockNames.METAMORPHIC_PREFIX + "plains_cobblestone") public static Block biomeCobblestonePlains;
	@ObjectHolder(LibBlockNames.METAMORPHIC_PREFIX + "plains_cobblestone" + WALL_SUFFIX) public static Block biomeWallPlains;
	@ObjectHolder(LibBlockNames.METAMORPHIC_PREFIX + "plains_bricks") public static Block biomeBrickPlains;
	@ObjectHolder(LibBlockNames.METAMORPHIC_PREFIX + "plains_bricks" + SLAB_SUFFIX) public static Block biomeBrickPlainsSlab;
	@ObjectHolder(LibBlockNames.METAMORPHIC_PREFIX + "plains_bricks" + STAIR_SUFFIX) public static Block biomeBrickPlainsStairs;
	@ObjectHolder("chiseled_" + LibBlockNames.METAMORPHIC_PREFIX + "plains_bricks") public static Block biomeChiseledBrickPlains;

	@ObjectHolder(LibBlockNames.METAMORPHIC_PREFIX + "mountain_stone") public static Block biomeStoneMountain;
	@ObjectHolder(LibBlockNames.METAMORPHIC_PREFIX + "mountain_stone" + SLAB_SUFFIX) public static Block biomeStoneMountainSlab;
	@ObjectHolder(LibBlockNames.METAMORPHIC_PREFIX + "mountain_stone" + STAIR_SUFFIX) public static Block biomeStoneMountainStairs;
	@ObjectHolder(LibBlockNames.METAMORPHIC_PREFIX + "mountain_cobblestone") public static Block biomeCobblestoneMountain;
	@ObjectHolder(LibBlockNames.METAMORPHIC_PREFIX + "mountain_cobblestone" + WALL_SUFFIX) public static Block biomeWallMountain;
	@ObjectHolder(LibBlockNames.METAMORPHIC_PREFIX + "mountain_bricks") public static Block biomeBrickMountain;
	@ObjectHolder(LibBlockNames.METAMORPHIC_PREFIX + "mountain_bricks" + SLAB_SUFFIX) public static Block biomeBrickMountainSlab;
	@ObjectHolder(LibBlockNames.METAMORPHIC_PREFIX + "mountain_bricks" + STAIR_SUFFIX) public static Block biomeBrickMountainStairs;
	@ObjectHolder("chiseled_" + LibBlockNames.METAMORPHIC_PREFIX + "mountain_bricks") public static Block biomeChiseledBrickMountain;

	@ObjectHolder(LibBlockNames.METAMORPHIC_PREFIX + "fungal_stone") public static Block biomeStoneFungal;
	@ObjectHolder(LibBlockNames.METAMORPHIC_PREFIX + "fungal_stone" + SLAB_SUFFIX) public static Block biomeStoneFungalSlab;
	@ObjectHolder(LibBlockNames.METAMORPHIC_PREFIX + "fungal_stone" + STAIR_SUFFIX) public static Block biomeStoneFungalStairs;
	@ObjectHolder(LibBlockNames.METAMORPHIC_PREFIX + "fungal_cobblestone") public static Block biomeCobblestoneFungal;
	@ObjectHolder(LibBlockNames.METAMORPHIC_PREFIX + "fungal_cobblestone" + WALL_SUFFIX) public static Block biomeWallFungal;
	@ObjectHolder(LibBlockNames.METAMORPHIC_PREFIX + "fungal_bricks") public static Block biomeBrickFungal;
	@ObjectHolder(LibBlockNames.METAMORPHIC_PREFIX + "fungal_bricks" + SLAB_SUFFIX) public static Block biomeBrickFungalSlab;
	@ObjectHolder(LibBlockNames.METAMORPHIC_PREFIX + "fungal_bricks" + STAIR_SUFFIX) public static Block biomeBrickFungalStairs;
	@ObjectHolder("chiseled_" + LibBlockNames.METAMORPHIC_PREFIX + "fungal_bricks") public static Block biomeChiseledBrickFungal;

	@ObjectHolder(LibBlockNames.METAMORPHIC_PREFIX + "swamp_stone") public static Block biomeStoneSwamp;
	@ObjectHolder(LibBlockNames.METAMORPHIC_PREFIX + "swamp_stone" + SLAB_SUFFIX) public static Block biomeStoneSwampSlab;
	@ObjectHolder(LibBlockNames.METAMORPHIC_PREFIX + "swamp_stone" + STAIR_SUFFIX) public static Block biomeStoneSwampStairs;
	@ObjectHolder(LibBlockNames.METAMORPHIC_PREFIX + "swamp_cobblestone") public static Block biomeCobblestoneSwamp;
	@ObjectHolder(LibBlockNames.METAMORPHIC_PREFIX + "swamp_cobblestone" + WALL_SUFFIX) public static Block biomeWallSwamp;
	@ObjectHolder(LibBlockNames.METAMORPHIC_PREFIX + "swamp_bricks") public static Block biomeBrickSwamp;
	@ObjectHolder(LibBlockNames.METAMORPHIC_PREFIX + "swamp_bricks" + SLAB_SUFFIX) public static Block biomeBrickSwampSlab;
	@ObjectHolder(LibBlockNames.METAMORPHIC_PREFIX + "swamp_bricks" + STAIR_SUFFIX) public static Block biomeBrickSwampStairs;
	@ObjectHolder("chiseled_" + LibBlockNames.METAMORPHIC_PREFIX + "swamp_bricks") public static Block biomeChiseledBrickSwamp;

	@ObjectHolder(LibBlockNames.METAMORPHIC_PREFIX + "desert_stone") public static Block biomeStoneDesert;
	@ObjectHolder(LibBlockNames.METAMORPHIC_PREFIX + "desert_stone" + SLAB_SUFFIX) public static Block biomeStoneDesertSlab;
	@ObjectHolder(LibBlockNames.METAMORPHIC_PREFIX + "desert_stone" + STAIR_SUFFIX) public static Block biomeStoneDesertStairs;
	@ObjectHolder(LibBlockNames.METAMORPHIC_PREFIX + "desert_cobblestone") public static Block biomeCobblestoneDesert;
	@ObjectHolder(LibBlockNames.METAMORPHIC_PREFIX + "desert_cobblestone" + WALL_SUFFIX) public static Block biomeWallDesert;
	@ObjectHolder(LibBlockNames.METAMORPHIC_PREFIX + "desert_bricks") public static Block biomeBrickDesert;
	@ObjectHolder(LibBlockNames.METAMORPHIC_PREFIX + "desert_bricks" + SLAB_SUFFIX) public static Block biomeBrickDesertSlab;
	@ObjectHolder(LibBlockNames.METAMORPHIC_PREFIX + "desert_bricks" + STAIR_SUFFIX) public static Block biomeBrickDesertStairs;
	@ObjectHolder("chiseled_" + LibBlockNames.METAMORPHIC_PREFIX + "desert_bricks") public static Block biomeChiseledBrickDesert;

	@ObjectHolder(LibBlockNames.METAMORPHIC_PREFIX + "taiga_stone") public static Block biomeStoneTaiga;
	@ObjectHolder(LibBlockNames.METAMORPHIC_PREFIX + "taiga_stone" + SLAB_SUFFIX) public static Block biomeStoneTaigaSlab;
	@ObjectHolder(LibBlockNames.METAMORPHIC_PREFIX + "taiga_stone" + STAIR_SUFFIX) public static Block biomeStoneTaigaStairs;
	@ObjectHolder(LibBlockNames.METAMORPHIC_PREFIX + "taiga_cobblestone") public static Block biomeCobblestoneTaiga;
	@ObjectHolder(LibBlockNames.METAMORPHIC_PREFIX + "taiga_cobblestone" + WALL_SUFFIX) public static Block biomeWallTaiga;
	@ObjectHolder(LibBlockNames.METAMORPHIC_PREFIX + "taiga_bricks") public static Block biomeBrickTaiga;
	@ObjectHolder(LibBlockNames.METAMORPHIC_PREFIX + "taiga_bricks" + SLAB_SUFFIX) public static Block biomeBrickTaigaSlab;
	@ObjectHolder(LibBlockNames.METAMORPHIC_PREFIX + "taiga_bricks" + STAIR_SUFFIX) public static Block biomeBrickTaigaStairs;
	@ObjectHolder("chiseled_" + LibBlockNames.METAMORPHIC_PREFIX + "taiga_bricks") public static Block biomeChiseledBrickTaiga;

	@ObjectHolder(LibBlockNames.METAMORPHIC_PREFIX + "mesa_stone") public static Block biomeStoneMesa;
	@ObjectHolder(LibBlockNames.METAMORPHIC_PREFIX + "mesa_stone" + SLAB_SUFFIX) public static Block biomeStoneMesaSlab;
	@ObjectHolder(LibBlockNames.METAMORPHIC_PREFIX + "mesa_stone" + STAIR_SUFFIX) public static Block biomeStoneMesaStairs;
	@ObjectHolder(LibBlockNames.METAMORPHIC_PREFIX + "mesa_cobblestone") public static Block biomeCobblestoneMesa;
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

	@SubscribeEvent
	public static void registerBlocks(RegistryEvent.Register<Block> evt) {
		IForgeRegistry<Block> r = evt.getRegistry();

		Block base = r.getValue(new ResourceLocation(LibMisc.MOD_ID, LibBlockNames.LIVING_WOOD));
		register(r, new BlockModStairs(base.getDefaultState(), Block.Properties.from(base)), LibBlockNames.LIVING_WOOD + STAIR_SUFFIX);
		register(r, new BlockModSlab(Block.Properties.from(base)), LibBlockNames.LIVING_WOOD + SLAB_SUFFIX);
		register(r, new BlockModWall(Block.Properties.from(base)), LibBlockNames.LIVING_WOOD + WALL_SUFFIX);

		base = r.getValue(new ResourceLocation(LibMisc.MOD_ID, LibBlockNames.LIVING_WOOD_PLANKS));
		register(r, new BlockModStairs(base.getDefaultState(), Block.Properties.from(base)), LibBlockNames.LIVING_WOOD_PLANKS + STAIR_SUFFIX);
		register(r, new BlockModSlab(Block.Properties.from(base)), LibBlockNames.LIVING_WOOD_PLANKS + SLAB_SUFFIX);

		base = r.getValue(new ResourceLocation(LibMisc.MOD_ID, LibBlockNames.LIVING_ROCK));
		register(r, new BlockModStairs(base.getDefaultState(), Block.Properties.from(base)), LibBlockNames.LIVING_ROCK + STAIR_SUFFIX);
		register(r, new BlockModSlab(Block.Properties.from(base)), LibBlockNames.LIVING_ROCK + SLAB_SUFFIX);
		register(r, new BlockModWall(Block.Properties.from(base)), LibBlockNames.LIVING_ROCK + WALL_SUFFIX);
		
		base = r.getValue(new ResourceLocation(LibMisc.MOD_ID, LibBlockNames.LIVING_ROCK_BRICK));
		register(r, new BlockModStairs(base.getDefaultState(), Block.Properties.from(base)), LibBlockNames.LIVING_ROCK_BRICK + STAIR_SUFFIX);
		register(r, new BlockModSlab(Block.Properties.from(base)), LibBlockNames.LIVING_ROCK_BRICK + SLAB_SUFFIX);

		base = r.getValue(new ResourceLocation(LibMisc.MOD_ID, LibBlockNames.DREAM_WOOD));
		register(r, new BlockModStairs(base.getDefaultState(), Block.Properties.from(base)), LibBlockNames.DREAM_WOOD + STAIR_SUFFIX);
		register(r, new BlockModSlab(Block.Properties.from(base)), LibBlockNames.DREAM_WOOD + SLAB_SUFFIX);
		register(r, new BlockModWall(Block.Properties.from(base)), LibBlockNames.DREAM_WOOD + WALL_SUFFIX);

		base = r.getValue(new ResourceLocation(LibMisc.MOD_ID, LibBlockNames.DREAM_WOOD_PLANKS));
		register(r, new BlockModStairs(base.getDefaultState(), Block.Properties.from(base)), LibBlockNames.DREAM_WOOD_PLANKS + STAIR_SUFFIX);
		register(r, new BlockModSlab(Block.Properties.from(base)), LibBlockNames.DREAM_WOOD_PLANKS + SLAB_SUFFIX);
		
		Block.Properties props = Block.Properties.from(Blocks.QUARTZ_BLOCK);
		String[] variants = {
				LibBlockNames.QUARTZ_BLAZE, LibBlockNames.QUARTZ_DARK,
				LibBlockNames.QUARTZ_LAVENDER, LibBlockNames.QUARTZ_MANA,
				LibBlockNames.QUARTZ_RED, LibBlockNames.QUARTZ_SUNNY
		};
		for (String variant : variants) {
			ILexiconable lexicon = (world, pos, player, lex) -> LexiconData.decorativeBlocks;
			
			base = new BlockModLexiconable(props, lexicon);
			register(r, base, variant);
			register(r, new BlockModLexiconable(props, lexicon), "chiseled_" + variant);
			register(r, new BlockSpecialQuartzPillar(props), variant + "_pillar");
			register(r, new BlockModSlab(props), variant + SLAB_SUFFIX);
			register(r, new BlockModStairs(base.getDefaultState(), props), variant + STAIR_SUFFIX);
		}

		// elf quartz special
		ILexiconable elven = (world, pos, player, lex) -> LexiconData.elvenResources;
		base = new BlockModLexiconable(props, elven);
		register(r, base, LibBlockNames.QUARTZ_ELF);
		register(r, new BlockModLexiconable(props, elven), "chiseled_" + LibBlockNames.QUARTZ_ELF);
		register(r, new BlockSpecialQuartzPillar(props), LibBlockNames.QUARTZ_ELF + "_pillar");
		register(r, new BlockElfQuartzSlab(props), LibBlockNames.QUARTZ_ELF + SLAB_SUFFIX);
		register(r, new BlockElfQuartzStairs(base.getDefaultState(), props), LibBlockNames.QUARTZ_ELF + STAIR_SUFFIX);

		props = Block.Properties.create(Material.ROCK).hardnessAndResistance(2, 10).sound(SoundType.STONE);
		
		Block block = new BlockMod(props);
		register(r, block, "white" + LibBlockNames.PAVEMENT_SUFFIX);
		register(r, new BlockPavementStairs(block.getDefaultState(), props), "white" + LibBlockNames.PAVEMENT_SUFFIX + STAIR_SUFFIX);
        register(r, new BlockPavementSlab(props), "white" + LibBlockNames.PAVEMENT_SUFFIX + SLAB_SUFFIX);
		
		block = new BlockMod(props);
		register(r, block, "black" + LibBlockNames.PAVEMENT_SUFFIX);
		register(r, new BlockPavementStairs(block.getDefaultState(), props), "black" + LibBlockNames.PAVEMENT_SUFFIX + STAIR_SUFFIX);
		register(r, new BlockPavementSlab(props), "black" + LibBlockNames.PAVEMENT_SUFFIX + SLAB_SUFFIX);

		block = new BlockMod(props);
		register(r, block, "blue" + LibBlockNames.PAVEMENT_SUFFIX);
		register(r, new BlockPavementStairs(block.getDefaultState(), props), "blue" + LibBlockNames.PAVEMENT_SUFFIX + STAIR_SUFFIX);
		register(r, new BlockPavementSlab(props), "blue" + LibBlockNames.PAVEMENT_SUFFIX + SLAB_SUFFIX);

		block = new BlockMod(props);
		register(r, block, "red" + LibBlockNames.PAVEMENT_SUFFIX);
		register(r, new BlockPavementStairs(block.getDefaultState(), props), "red" + LibBlockNames.PAVEMENT_SUFFIX + STAIR_SUFFIX);
		register(r, new BlockPavementSlab(props), "red" + LibBlockNames.PAVEMENT_SUFFIX + SLAB_SUFFIX);

		block = new BlockMod(props);
		register(r, block, "yellow" + LibBlockNames.PAVEMENT_SUFFIX);
		register(r, new BlockPavementStairs(block.getDefaultState(), props), "yellow" + LibBlockNames.PAVEMENT_SUFFIX + STAIR_SUFFIX);
		register(r, new BlockPavementSlab(props), "yellow" + LibBlockNames.PAVEMENT_SUFFIX + SLAB_SUFFIX);

		block = new BlockMod(props);
		register(r, block, "green" + LibBlockNames.PAVEMENT_SUFFIX);
		register(r, new BlockPavementStairs(block.getDefaultState(), props), "green" + LibBlockNames.PAVEMENT_SUFFIX + STAIR_SUFFIX);
		register(r, new BlockPavementSlab(props), "green" + LibBlockNames.PAVEMENT_SUFFIX + SLAB_SUFFIX);

		props = Block.Properties.create(Material.ROCK).hardnessAndResistance(1.5F, 10).sound(SoundType.STONE);
		ILexiconable marimorph = (world, pos, player, lex) -> LexiconData.marimorphosis;
		variants = new String[] {
				"forest", "plains", "mountain", "fungal",
				"swamp", "desert", "taiga", "mesa"
		};
		for (String variant : variants) {
			base = new BlockModLexiconable(props, marimorph);
			register(r, base, LibBlockNames.METAMORPHIC_PREFIX + variant + "_stone");
			register(r, new BlockBiomeStoneStairs(base.getDefaultState(), props), base.getRegistryName().getPath() + STAIR_SUFFIX);
			register(r, new BlockBiomeStoneSlab(props), base.getRegistryName().getPath() + SLAB_SUFFIX);

			register(r, new BlockModLexiconable(props, marimorph), LibBlockNames.METAMORPHIC_PREFIX + variant + "_cobblestone");
			register(r, new BlockBiomeStoneWall(props), LibBlockNames.METAMORPHIC_PREFIX + variant + "_cobblestone" + WALL_SUFFIX);

			base = new BlockModLexiconable(props, marimorph);
			register(r, base, LibBlockNames.METAMORPHIC_PREFIX + variant + "_bricks");
			register(r, new BlockBiomeStoneStairs(base.getDefaultState(), props), base.getRegistryName().getPath() + STAIR_SUFFIX);
			register(r, new BlockBiomeStoneSlab(props), base.getRegistryName().getPath() + SLAB_SUFFIX);
			register(r, new BlockModLexiconable(props, marimorph), "chiseled_" + LibBlockNames.METAMORPHIC_PREFIX + variant + "_bricks");
		}
		
		base = r.getValue(new ResourceLocation(LibMisc.MOD_ID, LibBlockNames.SHIMMERROCK));
		register(r, new BlockModSlab(Block.Properties.from(base)), LibBlockNames.SHIMMERROCK + SLAB_SUFFIX);
		register(r, new BlockModStairs(base.getDefaultState(), Block.Properties.from(base)), LibBlockNames.SHIMMERROCK + STAIR_SUFFIX);

		base = r.getValue(new ResourceLocation(LibMisc.MOD_ID, LibBlockNames.SHIMMERWOOD_PLANKS));
		register(r, new BlockModSlab(Block.Properties.from(base)), LibBlockNames.SHIMMERWOOD_PLANKS + SLAB_SUFFIX);
		register(r, new BlockModStairs(base.getDefaultState(), Block.Properties.from(base)), LibBlockNames.SHIMMERWOOD_PLANKS + STAIR_SUFFIX);

		base = r.getValue(new ResourceLocation(LibMisc.MOD_ID, LibBlockNames.MANA_GLASS));
		register(r, new BlockModPane(Block.Properties.from(base)), LibBlockNames.MANA_GLASS + "_pane");

		base = r.getValue(new ResourceLocation(LibMisc.MOD_ID, LibBlockNames.ELF_GLASS));
		register(r, new BlockModPane(Block.Properties.from(base)), LibBlockNames.ELF_GLASS + "_pane");

		base = r.getValue(new ResourceLocation(LibMisc.MOD_ID, LibBlockNames.BIFROST));
		register(r, new BlockModPane(Block.Properties.from(base)), LibBlockNames.BIFROST + "_pane");
	}
	
	@SubscribeEvent
	public static void registerItemBlocks(RegistryEvent.Register<Item> evt) {
		IForgeRegistry<Item> r = evt.getRegistry();
		Item.Properties props = new Item.Properties().group(BotaniaCreativeTab.INSTANCE);
		
		r.register(new ItemBlockMod(livingwoodStairs, props).setRegistryName(livingwoodStairs.getRegistryName()));
		r.register(new ItemBlockMod(livingwoodSlab, props).setRegistryName(livingwoodSlab.getRegistryName()));
		r.register(new ItemBlockMod(livingwoodWall, props).setRegistryName(livingwoodWall.getRegistryName()));
		
		r.register(new ItemBlockMod(livingwoodPlankStairs, props).setRegistryName(livingwoodPlankStairs.getRegistryName()));
		r.register(new ItemBlockMod(livingwoodPlankSlab, props).setRegistryName(livingwoodPlankSlab.getRegistryName()));
		
		r.register(new ItemBlockMod(livingrockStairs, props).setRegistryName(livingrockStairs.getRegistryName()));
		r.register(new ItemBlockMod(livingrockSlab, props).setRegistryName(livingrockSlab.getRegistryName()));
		r.register(new ItemBlockMod(livingrockWall, props).setRegistryName(livingrockWall.getRegistryName()));
		
		r.register(new ItemBlockMod(livingrockBrickStairs, props).setRegistryName(livingrockBrickStairs.getRegistryName()));
		r.register(new ItemBlockMod(livingrockBrickSlab, props).setRegistryName(livingrockBrickSlab.getRegistryName()));
		
		r.register(new ItemBlockMod(dreamwoodStairs, props).setRegistryName(dreamwoodStairs.getRegistryName()));
		r.register(new ItemBlockMod(dreamwoodSlab, props).setRegistryName(dreamwoodSlab.getRegistryName()));
		r.register(new ItemBlockMod(dreamwoodWall, props).setRegistryName(dreamwoodWall.getRegistryName()));
		
		r.register(new ItemBlockMod(dreamwoodPlankStairs, props).setRegistryName(dreamwoodPlankStairs.getRegistryName()));
		r.register(new ItemBlockMod(dreamwoodPlankSlab, props).setRegistryName(dreamwoodPlankSlab.getRegistryName()));
		
		r.register(new ItemBlockMod(darkQuartz, props).setRegistryName(darkQuartz.getRegistryName()));
		r.register(new ItemBlockMod(darkQuartzPillar, props).setRegistryName(darkQuartzPillar.getRegistryName()));
		r.register(new ItemBlockMod(darkQuartzChiseled, props).setRegistryName(darkQuartzChiseled.getRegistryName()));
		r.register(new ItemBlockMod(darkQuartzSlab, props).setRegistryName(darkQuartzSlab.getRegistryName()));
		r.register(new ItemBlockMod(darkQuartzStairs, props).setRegistryName(darkQuartzStairs.getRegistryName()));

		r.register(new ItemBlockMod(manaQuartz, props).setRegistryName(manaQuartz.getRegistryName()));
		r.register(new ItemBlockMod(manaQuartzPillar, props).setRegistryName(manaQuartzPillar.getRegistryName()));
		r.register(new ItemBlockMod(manaQuartzChiseled, props).setRegistryName(manaQuartzChiseled.getRegistryName()));
		r.register(new ItemBlockMod(manaQuartzSlab, props).setRegistryName(manaQuartzSlab.getRegistryName()));
		r.register(new ItemBlockMod(manaQuartzStairs, props).setRegistryName(manaQuartzStairs.getRegistryName()));
		
		r.register(new ItemBlockMod(blazeQuartz, props).setRegistryName(blazeQuartz.getRegistryName()));
		r.register(new ItemBlockMod(blazeQuartzPillar, props).setRegistryName(blazeQuartzPillar.getRegistryName()));
		r.register(new ItemBlockMod(blazeQuartzChiseled, props).setRegistryName(blazeQuartzChiseled.getRegistryName()));
		r.register(new ItemBlockMod(blazeQuartzSlab, props).setRegistryName(blazeQuartzSlab.getRegistryName()));
		r.register(new ItemBlockMod(blazeQuartzStairs, props).setRegistryName(blazeQuartzStairs.getRegistryName()));
		
		r.register(new ItemBlockMod(lavenderQuartz, props).setRegistryName(lavenderQuartz.getRegistryName()));
		r.register(new ItemBlockMod(lavenderQuartzPillar, props).setRegistryName(lavenderQuartzPillar.getRegistryName()));
		r.register(new ItemBlockMod(lavenderQuartzChiseled, props).setRegistryName(lavenderQuartzChiseled.getRegistryName()));
		r.register(new ItemBlockMod(lavenderQuartzSlab, props).setRegistryName(lavenderQuartzSlab.getRegistryName()));
		r.register(new ItemBlockMod(lavenderQuartzStairs, props).setRegistryName(lavenderQuartzStairs.getRegistryName()));
		
		r.register(new ItemBlockMod(redQuartz, props).setRegistryName(redQuartz.getRegistryName()));
		r.register(new ItemBlockMod(redQuartzPillar, props).setRegistryName(redQuartzPillar.getRegistryName()));
		r.register(new ItemBlockMod(redQuartzChiseled, props).setRegistryName(redQuartzChiseled.getRegistryName()));
		r.register(new ItemBlockMod(redQuartzSlab, props).setRegistryName(redQuartzSlab.getRegistryName()));
		r.register(new ItemBlockMod(redQuartzStairs, props).setRegistryName(redQuartzStairs.getRegistryName()));
		
		r.register(new ItemBlockMod(elfQuartz, props).setRegistryName(elfQuartz.getRegistryName()));
		r.register(new ItemBlockMod(elfQuartzPillar, props).setRegistryName(elfQuartzPillar.getRegistryName()));
		r.register(new ItemBlockMod(elfQuartzChiseled, props).setRegistryName(elfQuartzChiseled.getRegistryName()));
		r.register(new ItemBlockMod(elfQuartzSlab, props).setRegistryName(elfQuartzSlab.getRegistryName()));
		r.register(new ItemBlockMod(elfQuartzStairs, props).setRegistryName(elfQuartzStairs.getRegistryName()));
		
		r.register(new ItemBlockMod(sunnyQuartz, props).setRegistryName(sunnyQuartz.getRegistryName()));
		r.register(new ItemBlockMod(sunnyQuartzPillar, props).setRegistryName(sunnyQuartzPillar.getRegistryName()));
		r.register(new ItemBlockMod(sunnyQuartzChiseled, props).setRegistryName(sunnyQuartzChiseled.getRegistryName()));
		r.register(new ItemBlockMod(sunnyQuartzSlab, props).setRegistryName(sunnyQuartzSlab.getRegistryName()));
		r.register(new ItemBlockMod(sunnyQuartzStairs, props).setRegistryName(sunnyQuartzStairs.getRegistryName()));
		
		r.register(new ItemBlockMod(biomeStoneForest, props).setRegistryName(biomeStoneForest.getRegistryName()));
		r.register(new ItemBlockMod(biomeStoneForestSlab, props).setRegistryName(biomeStoneForestSlab.getRegistryName()));
		r.register(new ItemBlockMod(biomeStoneForestStairs, props).setRegistryName(biomeStoneForestStairs.getRegistryName()));
		r.register(new ItemBlockMod(biomeBrickForest, props).setRegistryName(biomeBrickForest.getRegistryName()));
		r.register(new ItemBlockMod(biomeBrickForestSlab, props).setRegistryName(biomeBrickForestSlab.getRegistryName()));
		r.register(new ItemBlockMod(biomeBrickForestStairs, props).setRegistryName(biomeBrickForestStairs.getRegistryName()));
		r.register(new ItemBlockMod(biomeCobblestoneForest, props).setRegistryName(biomeCobblestoneForest.getRegistryName()));
		r.register(new ItemBlockMod(biomeWallForest, props).setRegistryName(biomeWallForest.getRegistryName()));
		r.register(new ItemBlockMod(biomeChiseledBrickForest, props).setRegistryName(biomeChiseledBrickForest.getRegistryName()));

		r.register(new ItemBlockMod(biomeStonePlains, props).setRegistryName(biomeStonePlains.getRegistryName()));
		r.register(new ItemBlockMod(biomeStonePlainsSlab, props).setRegistryName(biomeStonePlainsSlab.getRegistryName()));
		r.register(new ItemBlockMod(biomeStonePlainsStairs, props).setRegistryName(biomeStonePlainsStairs.getRegistryName()));
		r.register(new ItemBlockMod(biomeBrickPlains, props).setRegistryName(biomeBrickPlains.getRegistryName()));
		r.register(new ItemBlockMod(biomeBrickPlainsSlab, props).setRegistryName(biomeBrickPlainsSlab.getRegistryName()));
		r.register(new ItemBlockMod(biomeBrickPlainsStairs, props).setRegistryName(biomeBrickPlainsStairs.getRegistryName()));
		r.register(new ItemBlockMod(biomeCobblestonePlains, props).setRegistryName(biomeCobblestonePlains.getRegistryName()));
		r.register(new ItemBlockMod(biomeWallPlains, props).setRegistryName(biomeWallPlains.getRegistryName()));
		r.register(new ItemBlockMod(biomeChiseledBrickPlains, props).setRegistryName(biomeChiseledBrickPlains.getRegistryName()));

		r.register(new ItemBlockMod(biomeStoneMountain, props).setRegistryName(biomeStoneMountain.getRegistryName()));
		r.register(new ItemBlockMod(biomeStoneMountainSlab, props).setRegistryName(biomeStoneMountainSlab.getRegistryName()));
		r.register(new ItemBlockMod(biomeStoneMountainStairs, props).setRegistryName(biomeStoneMountainStairs.getRegistryName()));
		r.register(new ItemBlockMod(biomeBrickMountain, props).setRegistryName(biomeBrickMountain.getRegistryName()));
		r.register(new ItemBlockMod(biomeBrickMountainSlab, props).setRegistryName(biomeBrickMountainSlab.getRegistryName()));
		r.register(new ItemBlockMod(biomeBrickMountainStairs, props).setRegistryName(biomeBrickMountainStairs.getRegistryName()));
		r.register(new ItemBlockMod(biomeCobblestoneMountain, props).setRegistryName(biomeCobblestoneMountain.getRegistryName()));
		r.register(new ItemBlockMod(biomeWallMountain, props).setRegistryName(biomeWallMountain.getRegistryName()));
		r.register(new ItemBlockMod(biomeChiseledBrickMountain, props).setRegistryName(biomeChiseledBrickMountain.getRegistryName()));

		r.register(new ItemBlockMod(biomeStoneFungal, props).setRegistryName(biomeStoneFungal.getRegistryName()));
		r.register(new ItemBlockMod(biomeStoneFungalSlab, props).setRegistryName(biomeStoneFungalSlab.getRegistryName()));
		r.register(new ItemBlockMod(biomeStoneFungalStairs, props).setRegistryName(biomeStoneFungalStairs.getRegistryName()));
		r.register(new ItemBlockMod(biomeBrickFungal, props).setRegistryName(biomeBrickFungal.getRegistryName()));
		r.register(new ItemBlockMod(biomeBrickFungalSlab, props).setRegistryName(biomeBrickFungalSlab.getRegistryName()));
		r.register(new ItemBlockMod(biomeBrickFungalStairs, props).setRegistryName(biomeBrickFungalStairs.getRegistryName()));
		r.register(new ItemBlockMod(biomeCobblestoneFungal, props).setRegistryName(biomeCobblestoneFungal.getRegistryName()));
		r.register(new ItemBlockMod(biomeWallFungal, props).setRegistryName(biomeWallFungal.getRegistryName()));
		r.register(new ItemBlockMod(biomeChiseledBrickFungal, props).setRegistryName(biomeChiseledBrickFungal.getRegistryName()));

		r.register(new ItemBlockMod(biomeStoneSwamp, props).setRegistryName(biomeStoneSwamp.getRegistryName()));
		r.register(new ItemBlockMod(biomeStoneSwampSlab, props).setRegistryName(biomeStoneSwampSlab.getRegistryName()));
		r.register(new ItemBlockMod(biomeStoneSwampStairs, props).setRegistryName(biomeStoneSwampStairs.getRegistryName()));
		r.register(new ItemBlockMod(biomeBrickSwamp, props).setRegistryName(biomeBrickSwamp.getRegistryName()));
		r.register(new ItemBlockMod(biomeBrickSwampSlab, props).setRegistryName(biomeBrickSwampSlab.getRegistryName()));
		r.register(new ItemBlockMod(biomeBrickSwampStairs, props).setRegistryName(biomeBrickSwampStairs.getRegistryName()));
		r.register(new ItemBlockMod(biomeCobblestoneSwamp, props).setRegistryName(biomeCobblestoneSwamp.getRegistryName()));
		r.register(new ItemBlockMod(biomeWallSwamp, props).setRegistryName(biomeWallSwamp.getRegistryName()));
		r.register(new ItemBlockMod(biomeChiseledBrickSwamp, props).setRegistryName(biomeChiseledBrickSwamp.getRegistryName()));

		r.register(new ItemBlockMod(biomeStoneDesert, props).setRegistryName(biomeStoneDesert.getRegistryName()));
		r.register(new ItemBlockMod(biomeStoneDesertSlab, props).setRegistryName(biomeStoneDesertSlab.getRegistryName()));
		r.register(new ItemBlockMod(biomeStoneDesertStairs, props).setRegistryName(biomeStoneDesertStairs.getRegistryName()));
		r.register(new ItemBlockMod(biomeBrickDesert, props).setRegistryName(biomeBrickDesert.getRegistryName()));
		r.register(new ItemBlockMod(biomeBrickDesertSlab, props).setRegistryName(biomeBrickDesertSlab.getRegistryName()));
		r.register(new ItemBlockMod(biomeBrickDesertStairs, props).setRegistryName(biomeBrickDesertStairs.getRegistryName()));
		r.register(new ItemBlockMod(biomeCobblestoneDesert, props).setRegistryName(biomeCobblestoneDesert.getRegistryName()));
		r.register(new ItemBlockMod(biomeWallDesert, props).setRegistryName(biomeWallDesert.getRegistryName()));
		r.register(new ItemBlockMod(biomeChiseledBrickDesert, props).setRegistryName(biomeChiseledBrickDesert.getRegistryName()));

		r.register(new ItemBlockMod(biomeStoneTaiga, props).setRegistryName(biomeStoneTaiga.getRegistryName()));
		r.register(new ItemBlockMod(biomeStoneTaigaSlab, props).setRegistryName(biomeStoneTaigaSlab.getRegistryName()));
		r.register(new ItemBlockMod(biomeStoneTaigaStairs, props).setRegistryName(biomeStoneTaigaStairs.getRegistryName()));
		r.register(new ItemBlockMod(biomeBrickTaiga, props).setRegistryName(biomeBrickTaiga.getRegistryName()));
		r.register(new ItemBlockMod(biomeBrickTaigaSlab, props).setRegistryName(biomeBrickTaigaSlab.getRegistryName()));
		r.register(new ItemBlockMod(biomeBrickTaigaStairs, props).setRegistryName(biomeBrickTaigaStairs.getRegistryName()));
		r.register(new ItemBlockMod(biomeCobblestoneTaiga, props).setRegistryName(biomeCobblestoneTaiga.getRegistryName()));
		r.register(new ItemBlockMod(biomeWallTaiga, props).setRegistryName(biomeWallTaiga.getRegistryName()));
		r.register(new ItemBlockMod(biomeChiseledBrickTaiga, props).setRegistryName(biomeChiseledBrickTaiga.getRegistryName()));

		r.register(new ItemBlockMod(biomeStoneMesa, props).setRegistryName(biomeStoneMesa.getRegistryName()));
		r.register(new ItemBlockMod(biomeStoneMesaSlab, props).setRegistryName(biomeStoneMesaSlab.getRegistryName()));
		r.register(new ItemBlockMod(biomeStoneMesaStairs, props).setRegistryName(biomeStoneMesaStairs.getRegistryName()));
		r.register(new ItemBlockMod(biomeBrickMesa, props).setRegistryName(biomeBrickMesa.getRegistryName()));
		r.register(new ItemBlockMod(biomeBrickMesaSlab, props).setRegistryName(biomeBrickMesaSlab.getRegistryName()));
		r.register(new ItemBlockMod(biomeBrickMesaStairs, props).setRegistryName(biomeBrickMesaStairs.getRegistryName()));
		r.register(new ItemBlockMod(biomeCobblestoneMesa, props).setRegistryName(biomeCobblestoneMesa.getRegistryName()));
		r.register(new ItemBlockMod(biomeWallMesa, props).setRegistryName(biomeWallMesa.getRegistryName()));
		r.register(new ItemBlockMod(biomeChiseledBrickMesa, props).setRegistryName(biomeChiseledBrickMesa.getRegistryName()));
		
		r.register(new ItemBlockMod(whitePavement, props).setRegistryName(whitePavement.getRegistryName()));
		r.register(new ItemBlockMod(blackPavement, props).setRegistryName(blackPavement.getRegistryName()));
		r.register(new ItemBlockMod(bluePavement, props).setRegistryName(bluePavement.getRegistryName()));
		r.register(new ItemBlockMod(yellowPavement, props).setRegistryName(yellowPavement.getRegistryName()));
		r.register(new ItemBlockMod(redPavement, props).setRegistryName(redPavement.getRegistryName()));
		r.register(new ItemBlockMod(greenPavement, props).setRegistryName(greenPavement.getRegistryName()));

		r.register(new ItemBlockMod(whitePavementSlab, props).setRegistryName(whitePavementSlab.getRegistryName()));
		r.register(new ItemBlockMod(blackPavementSlab, props).setRegistryName(blackPavementSlab.getRegistryName()));
		r.register(new ItemBlockMod(bluePavementSlab, props).setRegistryName(bluePavementSlab.getRegistryName()));
		r.register(new ItemBlockMod(yellowPavementSlab, props).setRegistryName(yellowPavementSlab.getRegistryName()));
		r.register(new ItemBlockMod(redPavementSlab, props).setRegistryName(redPavementSlab.getRegistryName()));
		r.register(new ItemBlockMod(greenPavementSlab, props).setRegistryName(greenPavementSlab.getRegistryName()));

		r.register(new ItemBlockMod(whitePavementStair, props).setRegistryName(whitePavementStair.getRegistryName()));
		r.register(new ItemBlockMod(blackPavementStair, props).setRegistryName(blackPavementStair.getRegistryName()));
		r.register(new ItemBlockMod(bluePavementStair, props).setRegistryName(bluePavementStair.getRegistryName()));
		r.register(new ItemBlockMod(yellowPavementStair, props).setRegistryName(yellowPavementStair.getRegistryName()));
		r.register(new ItemBlockMod(redPavementStair, props).setRegistryName(redPavementStair.getRegistryName()));
		r.register(new ItemBlockMod(greenPavementStair, props).setRegistryName(greenPavementStair.getRegistryName()));
		
		r.register(new ItemBlockMod(shimmerrockSlab, props).setRegistryName(shimmerrockSlab.getRegistryName()));
		r.register(new ItemBlockMod(shimmerrockStairs, props).setRegistryName(shimmerrockStairs.getRegistryName()));
		
		r.register(new ItemBlockMod(shimmerwoodPlankSlab, props).setRegistryName(shimmerwoodPlankSlab.getRegistryName()));
		r.register(new ItemBlockMod(shimmerwoodPlankStairs, props).setRegistryName(shimmerwoodPlankStairs.getRegistryName()));
		
		r.register(new ItemBlockMod(managlassPane, props).setRegistryName(managlassPane.getRegistryName()));
		r.register(new ItemBlockMod(alfglassPane, props).setRegistryName(alfglassPane.getRegistryName()));
		r.register(new ItemBlockMod(bifrostPane, props).setRegistryName(bifrostPane.getRegistryName()));
	}

}
