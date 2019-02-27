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
import net.minecraft.block.Block;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.item.EnumDyeColor;
import net.minecraft.item.Item;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.registries.IForgeRegistry;
import net.minecraftforge.registries.ObjectHolder;
import vazkii.botania.api.state.BotaniaStateProps;
import vazkii.botania.api.state.enums.BiomeBrickVariant;
import vazkii.botania.api.state.enums.BiomeStoneVariant;
import vazkii.botania.common.block.decor.biomestone.BlockBiomeStoneA;
import vazkii.botania.common.block.decor.biomestone.BlockBiomeStoneB;
import vazkii.botania.common.block.decor.panes.BlockModPane;
import vazkii.botania.common.block.decor.quartz.BlockSpecialQuartz;
import vazkii.botania.common.block.decor.quartz.BlockSpecialQuartzSlab;
import vazkii.botania.common.block.decor.quartz.BlockSpecialQuartzStairs;
import vazkii.botania.common.block.decor.slabs.BlockBiomeStoneSlab;
import vazkii.botania.common.block.decor.slabs.BlockModSlab;
import vazkii.botania.common.block.decor.slabs.BlockPavementSlab;
import vazkii.botania.common.block.decor.stairs.BlockBiomeStoneStairs;
import vazkii.botania.common.block.decor.stairs.BlockModStairs;
import vazkii.botania.common.block.decor.stairs.BlockPavementStairs;
import vazkii.botania.common.block.decor.walls.BlockBiomeStoneWall;
import vazkii.botania.common.block.decor.walls.BlockModWall;
import vazkii.botania.common.item.block.ItemBlockMod;
import vazkii.botania.common.item.block.ItemBlockWithMetadataAndName;
import vazkii.botania.common.lib.LibBlockNames;
import vazkii.botania.common.lib.LibMisc;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import static vazkii.botania.common.block.ModBlocks.register;
import static vazkii.botania.common.lib.LibBlockNames.STAIR_SUFFIX;
import static vazkii.botania.common.lib.LibBlockNames.SLAB_SUFFIX;
import static vazkii.botania.common.lib.LibBlockNames.WALL_SUFFIX;

@Mod.EventBusSubscriber(modid = LibMisc.MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD)
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

	public static final Block darkQuartz = new BlockSpecialQuartz(LibBlockNames.QUARTZ_DARK);
	public static final Block darkQuartzSlab = new BlockSpecialQuartzSlab(darkQuartz, false);
	public static final Block darkQuartzStairs = new BlockSpecialQuartzStairs(darkQuartz);
	public static final Block manaQuartz = new BlockSpecialQuartz(LibBlockNames.QUARTZ_MANA);
	public static final Block manaQuartzSlab = new BlockSpecialQuartzSlab(manaQuartz, false);
	public static final Block manaQuartzStairs = new BlockSpecialQuartzStairs(manaQuartz);
	public static final Block blazeQuartz = new BlockSpecialQuartz(LibBlockNames.QUARTZ_BLAZE);
	public static final Block blazeQuartzSlab = new BlockSpecialQuartzSlab(blazeQuartz, false);
	public static final Block blazeQuartzStairs = new BlockSpecialQuartzStairs(blazeQuartz);
	public static final Block lavenderQuartz = new BlockSpecialQuartz(LibBlockNames.QUARTZ_LAVENDER);
	public static final Block lavenderQuartzSlab = new BlockSpecialQuartzSlab(lavenderQuartz, false);
	public static final Block lavenderQuartzStairs = new BlockSpecialQuartzStairs(lavenderQuartz);
	public static final Block redQuartz = new BlockSpecialQuartz(LibBlockNames.QUARTZ_RED);
	public static final Block redQuartzSlab = new BlockSpecialQuartzSlab(redQuartz, false);
	public static final Block redQuartzStairs = new BlockSpecialQuartzStairs(redQuartz);
	public static final Block elfQuartz = new BlockSpecialQuartz(LibBlockNames.QUARTZ_ELF);
	public static final Block elfQuartzSlab = new BlockSpecialQuartzSlab(elfQuartz, false);
	public static final Block elfQuartzStairs = new BlockSpecialQuartzStairs(elfQuartz);
	public static final Block sunnyQuartz = new BlockSpecialQuartz(LibBlockNames.QUARTZ_SUNNY);
	public static final Block sunnyQuartzSlab = new BlockSpecialQuartzSlab(sunnyQuartz, false);
	public static final Block sunnyQuartzStairs = new BlockSpecialQuartzStairs(sunnyQuartz);

	public static final Block biomeStoneA = new BlockBiomeStoneA();
	public static final Block biomeStoneB = new BlockBiomeStoneB();
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

	public static final Block[] biomeStoneStairs = new Block[24];
	public static final Block[] biomeStoneSlabs = new Block[24];
	public static final Block biomeStoneWall = new BlockBiomeStoneWall();

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

		Block livingwood = r.getValue(new ResourceLocation(LibMisc.MOD_ID, LibBlockNames.LIVING_WOOD));
		register(r, new BlockModStairs(livingwood.getDefaultState(), Block.Properties.from(livingwood)), LibBlockNames.LIVING_WOOD + STAIR_SUFFIX);
		register(r, new BlockModSlab(Block.Properties.from(livingwood)), LibBlockNames.LIVING_WOOD + SLAB_SUFFIX);
		register(r, new BlockModWall(Block.Properties.from(livingwood)), LibBlockNames.LIVING_WOOD + WALL_SUFFIX);

		Block livingwoodPlank = r.getValue(new ResourceLocation(LibMisc.MOD_ID, LibBlockNames.LIVING_WOOD_PLANKS));
		register(r, new BlockModStairs(livingwoodPlank.getDefaultState(), Block.Properties.from(livingwoodPlank)), LibBlockNames.LIVING_WOOD_PLANKS + STAIR_SUFFIX);
		register(r, new BlockModSlab(Block.Properties.from(livingwoodPlank)), LibBlockNames.LIVING_WOOD_PLANKS + SLAB_SUFFIX);

		Block livingrock = r.getValue(new ResourceLocation(LibMisc.MOD_ID, LibBlockNames.LIVING_ROCK));
		register(r, new BlockModStairs(livingrock.getDefaultState(), Block.Properties.from(livingrock)), LibBlockNames.LIVING_ROCK + STAIR_SUFFIX);
		register(r, new BlockModSlab(Block.Properties.from(livingrock)), LibBlockNames.LIVING_ROCK + SLAB_SUFFIX);
		register(r, new BlockModWall(Block.Properties.from(livingrock)), LibBlockNames.LIVING_ROCK + WALL_SUFFIX);
		
		Block livingrockBrick = r.getValue(new ResourceLocation(LibMisc.MOD_ID, LibBlockNames.LIVING_ROCK_BRICK));
		register(r, new BlockModStairs(livingrockBrick.getDefaultState(), Block.Properties.from(livingrockBrick)), LibBlockNames.LIVING_ROCK_BRICK + STAIR_SUFFIX);
		register(r, new BlockModSlab(Block.Properties.from(livingrockBrick)), LibBlockNames.LIVING_ROCK_BRICK + SLAB_SUFFIX);

		Block dreamwood = r.getValue(new ResourceLocation(LibMisc.MOD_ID, LibBlockNames.DREAM_WOOD));
		register(r, new BlockModStairs(dreamwood.getDefaultState(), Block.Properties.from(dreamwood)), LibBlockNames.DREAM_WOOD + STAIR_SUFFIX);
		register(r, new BlockModSlab(Block.Properties.from(dreamwood)), LibBlockNames.DREAM_WOOD + SLAB_SUFFIX);
		register(r, new BlockModWall(Block.Properties.from(dreamwood)), LibBlockNames.DREAM_WOOD + WALL_SUFFIX);

		Block dreamwoodPlank = r.getValue(new ResourceLocation(LibMisc.MOD_ID, LibBlockNames.DREAM_WOOD_PLANKS));
		register(r, new BlockModStairs(dreamwoodPlank.getDefaultState(), Block.Properties.from(dreamwoodPlank)), LibBlockNames.DREAM_WOOD_PLANKS + STAIR_SUFFIX);
		register(r, new BlockModSlab(Block.Properties.from(dreamwoodPlank)), LibBlockNames.DREAM_WOOD_PLANKS + SLAB_SUFFIX);

		r.register(darkQuartz);
		r.register(darkQuartzSlab);
		r.register(darkQuartzStairs);

		r.register(manaQuartz);
		r.register(manaQuartzSlab);
		r.register(manaQuartzStairs);
		
		r.register(blazeQuartz);
		r.register(blazeQuartzSlab);
		r.register(blazeQuartzStairs);
		
		r.register(lavenderQuartz);
		r.register(lavenderQuartzSlab);
		r.register(lavenderQuartzStairs);
		
		r.register(redQuartz);
		r.register(redQuartzSlab);
		r.register(redQuartzStairs);
		
		r.register(elfQuartz);
		r.register(elfQuartzSlab);
		r.register(elfQuartzStairs);
		
		r.register(sunnyQuartz);
		r.register(sunnyQuartzSlab);
		r.register(sunnyQuartzStairs);

		r.register(biomeStoneA);
		r.register(biomeStoneB);

		Block.Properties props = Block.Properties.create(Material.ROCK).hardnessAndResistance(2, 10).sound(SoundType.STONE);
		
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
		
		int count = 0;
		for (BiomeStoneVariant variant : BiomeStoneVariant.values()) {
			biomeStoneStairs[count] = new BlockBiomeStoneStairs(biomeStoneA.getDefaultState().with(BotaniaStateProps.BIOMESTONE_VARIANT, variant));
			biomeStoneSlabs[count] = new BlockBiomeStoneSlab(false, biomeStoneA.getDefaultState().with(BotaniaStateProps.BIOMESTONE_VARIANT, variant), count);
			r.register(biomeStoneStairs[count]);
			r.register(biomeStoneSlabs[count]);
			count++;
		}

		for (BiomeBrickVariant variant : BiomeBrickVariant.values()) {
			if (variant.getName().toLowerCase(Locale.ROOT).contains("chiseled")) {
				// No chiseled stairs/slabs
				continue;
			}
			biomeStoneStairs[count] = new BlockBiomeStoneStairs(biomeStoneB.getDefaultState().with(BotaniaStateProps.BIOMEBRICK_VARIANT, variant));
			biomeStoneSlabs[count] = new BlockBiomeStoneSlab(false, biomeStoneB.getDefaultState().with(BotaniaStateProps.BIOMEBRICK_VARIANT, variant), count);
			r.register(biomeStoneStairs[count]);
			r.register(biomeStoneSlabs[count]);
			count++;
		}
		
		r.register(biomeStoneWall);

		Block shimmerrock = r.getValue(new ResourceLocation(LibMisc.MOD_ID, LibBlockNames.SHIMMERROCK));
		register(r, new BlockModSlab(Block.Properties.from(shimmerrock)), LibBlockNames.SHIMMERROCK + SLAB_SUFFIX);
		register(r, new BlockModStairs(shimmerrock.getDefaultState(), Block.Properties.from(shimmerrock)), LibBlockNames.SHIMMERROCK + STAIR_SUFFIX);

		Block shimmerwood = r.getValue(new ResourceLocation(LibMisc.MOD_ID, LibBlockNames.SHIMMERWOOD_PLANKS));
		register(r, new BlockModSlab(Block.Properties.from(shimmerwood)), LibBlockNames.SHIMMERWOOD_PLANKS + SLAB_SUFFIX);
		register(r, new BlockModStairs(shimmerwood.getDefaultState(), Block.Properties.from(shimmerwood)), LibBlockNames.SHIMMERWOOD_PLANKS + STAIR_SUFFIX);

		Block manaGlass = r.getValue(new ResourceLocation(LibMisc.MOD_ID, LibBlockNames.MANA_GLASS));
		register(r, new BlockModPane(Block.Properties.from(manaGlass)), LibBlockNames.MANA_GLASS + "_pane");

		Block elfGlass = r.getValue(new ResourceLocation(LibMisc.MOD_ID, LibBlockNames.ELF_GLASS));
		register(r, new BlockModPane(Block.Properties.from(elfGlass)), LibBlockNames.ELF_GLASS + "_pane");

		Block bifrost = r.getValue(new ResourceLocation(LibMisc.MOD_ID, LibBlockNames.BIFROST));
		register(r, new BlockModPane(Block.Properties.from(bifrost)), LibBlockNames.BIFROST + "_pane");
	}
	
	@SubscribeEvent
	public static void registerItemBlocks(RegistryEvent.Register<Item> evt) {
		IForgeRegistry<Item> r = evt.getRegistry();
		r.register(new ItemBlockMod(livingwoodStairs).setRegistryName(livingwoodStairs.getRegistryName()));
		r.register(new ItemBlockMod(livingwoodSlab).setRegistryName(livingwoodSlab.getRegistryName()));
		r.register(new ItemBlockMod(livingwoodWall).setRegistryName(livingwoodWall.getRegistryName()));
		
		r.register(new ItemBlockMod(livingwoodPlankStairs).setRegistryName(livingwoodPlankStairs.getRegistryName()));
		r.register(new ItemBlockMod(livingwoodPlankSlab).setRegistryName(livingwoodPlankSlab.getRegistryName()));
		
		r.register(new ItemBlockMod(livingrockStairs).setRegistryName(livingrockStairs.getRegistryName()));
		r.register(new ItemBlockMod(livingrockSlab).setRegistryName(livingrockSlab.getRegistryName()));
		r.register(new ItemBlockMod(livingrockWall).setRegistryName(livingrockWall.getRegistryName()));
		
		r.register(new ItemBlockMod(livingrockBrickStairs).setRegistryName(livingrockBrickStairs.getRegistryName()));
		r.register(new ItemBlockMod(livingrockBrickSlab).setRegistryName(livingrockBrickSlab.getRegistryName()));
		
		r.register(new ItemBlockMod(dreamwoodStairs).setRegistryName(dreamwoodStairs.getRegistryName()));
		r.register(new ItemBlockMod(dreamwoodSlab).setRegistryName(dreamwoodSlab.getRegistryName()));
		r.register(new ItemBlockMod(dreamwoodWall).setRegistryName(dreamwoodWall.getRegistryName()));
		
		r.register(new ItemBlockMod(dreamwoodPlankStairs).setRegistryName(dreamwoodPlankStairs.getRegistryName()));
		r.register(new ItemBlockMod(dreamwoodPlankSlab).setRegistryName(dreamwoodPlankSlab.getRegistryName()));
		
		r.register(new ItemBlockMod(darkQuartz).setRegistryName(darkQuartz.getRegistryName()));
		r.register(new ItemBlockMod(darkQuartzSlab).setRegistryName(darkQuartzSlab.getRegistryName()));
		r.register(new ItemBlockMod(darkQuartzStairs).setRegistryName(darkQuartzStairs.getRegistryName()));

		r.register(new ItemBlockMod(manaQuartz).setRegistryName(manaQuartz.getRegistryName()));
		r.register(new ItemBlockMod(manaQuartzSlab).setRegistryName(manaQuartzSlab.getRegistryName()));
		r.register(new ItemBlockMod(manaQuartzStairs).setRegistryName(manaQuartzStairs.getRegistryName()));
		
		r.register(new ItemBlockMod(blazeQuartz).setRegistryName(blazeQuartz.getRegistryName()));
		r.register(new ItemBlockMod(blazeQuartzSlab).setRegistryName(blazeQuartzSlab.getRegistryName()));
		r.register(new ItemBlockMod(blazeQuartzStairs).setRegistryName(blazeQuartzStairs.getRegistryName()));
		
		r.register(new ItemBlockMod(lavenderQuartz).setRegistryName(lavenderQuartz.getRegistryName()));
		r.register(new ItemBlockMod(lavenderQuartzSlab).setRegistryName(lavenderQuartzSlab.getRegistryName()));
		r.register(new ItemBlockMod(lavenderQuartzStairs).setRegistryName(lavenderQuartzStairs.getRegistryName()));
		
		r.register(new ItemBlockMod(redQuartz).setRegistryName(redQuartz.getRegistryName()));
		r.register(new ItemBlockMod(redQuartzSlab).setRegistryName(redQuartzSlab.getRegistryName()));
		r.register(new ItemBlockMod(redQuartzStairs).setRegistryName(redQuartzStairs.getRegistryName()));
		
		r.register(new ItemBlockMod(elfQuartz).setRegistryName(elfQuartz.getRegistryName()));
		r.register(new ItemBlockMod(elfQuartzSlab).setRegistryName(elfQuartzSlab.getRegistryName()));
		r.register(new ItemBlockMod(elfQuartzStairs).setRegistryName(elfQuartzStairs.getRegistryName()));
		
		r.register(new ItemBlockMod(sunnyQuartz).setRegistryName(sunnyQuartz.getRegistryName()));
		r.register(new ItemBlockMod(sunnyQuartzSlab).setRegistryName(sunnyQuartzSlab.getRegistryName()));
		r.register(new ItemBlockMod(sunnyQuartzStairs).setRegistryName(sunnyQuartzStairs.getRegistryName()));
		
		r.register(new ItemBlockWithMetadataAndName(biomeStoneA).setRegistryName(biomeStoneA.getRegistryName()));
		r.register(new ItemBlockWithMetadataAndName(biomeStoneB).setRegistryName(biomeStoneB.getRegistryName()));
		r.register(new ItemBlockWithMetadataAndName(pavement).setRegistryName(pavement.getRegistryName()));
		
		for (int i = 0; i < 24; i++) {
			r.register(new ItemBlockMod(biomeStoneStairs[i]).setRegistryName(biomeStoneStairs[i].getRegistryName()));
			r.register(new ItemBlockMod(biomeStoneSlabs[i]).setRegistryName(biomeStoneSlabs[i].getRegistryName()));
		}
		
		r.register(new ItemBlockWithMetadataAndName(biomeStoneWall).setRegistryName(biomeStoneWall.getRegistryName()));
		
		for (int i = 0; i < BlockPavement.TYPES; i++) {
			r.register(new ItemBlockMod(pavementStairs[i]).setRegistryName(pavementStairs[i].getRegistryName()));
			r.register(new ItemBlockMod(pavementSlabs[i]).setRegistryName(pavementSlabs[i].getRegistryName()));
		}
		
		r.register(new ItemBlockMod(shimmerrockSlab).setRegistryName(shimmerrockSlab.getRegistryName()));
		r.register(new ItemBlockMod(shimmerrockStairs).setRegistryName(shimmerrockStairs.getRegistryName()));
		
		r.register(new ItemBlockMod(shimmerwoodPlankSlab).setRegistryName(shimmerwoodPlankSlab.getRegistryName()));
		r.register(new ItemBlockMod(shimmerwoodPlankStairs).setRegistryName(shimmerwoodPlankStairs.getRegistryName()));
		
		r.register(new ItemBlockMod(managlassPane).setRegistryName(managlassPane.getRegistryName()));
		r.register(new ItemBlockMod(alfglassPane).setRegistryName(alfglassPane.getRegistryName()));
		r.register(new ItemBlockMod(bifrostPane).setRegistryName(bifrostPane.getRegistryName()));
	}

}
