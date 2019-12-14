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
import net.minecraft.item.EnumDyeColor;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.oredict.OreDictionary;
import net.minecraftforge.registries.IForgeRegistry;
import vazkii.botania.api.state.BotaniaStateProps;
import vazkii.botania.api.state.enums.BiomeBrickVariant;
import vazkii.botania.api.state.enums.BiomeStoneVariant;
import vazkii.botania.api.state.enums.QuartzVariant;
import vazkii.botania.common.block.decor.BlockPavement;
import vazkii.botania.common.block.decor.biomestone.BlockBiomeStoneA;
import vazkii.botania.common.block.decor.biomestone.BlockBiomeStoneB;
import vazkii.botania.common.block.decor.panes.BlockAlfglassPane;
import vazkii.botania.common.block.decor.panes.BlockBifrostPane;
import vazkii.botania.common.block.decor.panes.BlockManaglassPane;
import vazkii.botania.common.block.decor.quartz.BlockSpecialQuartz;
import vazkii.botania.common.block.decor.quartz.BlockSpecialQuartzSlab;
import vazkii.botania.common.block.decor.quartz.BlockSpecialQuartzStairs;
import vazkii.botania.common.block.decor.slabs.BlockBiomeStoneSlab;
import vazkii.botania.common.block.decor.slabs.BlockPavementSlab;
import vazkii.botania.common.block.decor.slabs.living.BlockDreamwoodPlankSlab;
import vazkii.botania.common.block.decor.slabs.living.BlockDreamwoodSlab;
import vazkii.botania.common.block.decor.slabs.living.BlockLivingrockBrickSlab;
import vazkii.botania.common.block.decor.slabs.living.BlockLivingrockSlab;
import vazkii.botania.common.block.decor.slabs.living.BlockLivingwoodPlankSlab;
import vazkii.botania.common.block.decor.slabs.living.BlockLivingwoodSlab;
import vazkii.botania.common.block.decor.slabs.living.BlockShimmerrockSlab;
import vazkii.botania.common.block.decor.slabs.living.BlockShimmerwoodPlankSlab;
import vazkii.botania.common.block.decor.stairs.BlockBiomeStoneStairs;
import vazkii.botania.common.block.decor.stairs.BlockPavementStairs;
import vazkii.botania.common.block.decor.stairs.living.BlockDreamwoodPlankStairs;
import vazkii.botania.common.block.decor.stairs.living.BlockDreamwoodStairs;
import vazkii.botania.common.block.decor.stairs.living.BlockLivingrockBrickStairs;
import vazkii.botania.common.block.decor.stairs.living.BlockLivingrockStairs;
import vazkii.botania.common.block.decor.stairs.living.BlockLivingwoodPlankStairs;
import vazkii.botania.common.block.decor.stairs.living.BlockLivingwoodStairs;
import vazkii.botania.common.block.decor.stairs.living.BlockShimmerrockStairs;
import vazkii.botania.common.block.decor.stairs.living.BlockShimmerwoodPlankStairs;
import vazkii.botania.common.block.decor.walls.BlockBiomeStoneWall;
import vazkii.botania.common.block.decor.walls.living.BlockDreamwoodWall;
import vazkii.botania.common.block.decor.walls.living.BlockLivingrockWall;
import vazkii.botania.common.block.decor.walls.living.BlockLivingwoodWall;
import vazkii.botania.common.core.handler.ConfigHandler;
import vazkii.botania.common.item.block.ItemBlockMod;
import vazkii.botania.common.item.block.ItemBlockModSlab;
import vazkii.botania.common.item.block.ItemBlockSpecialQuartz;
import vazkii.botania.common.item.block.ItemBlockWithMetadataAndName;
import vazkii.botania.common.lib.LibBlockNames;
import vazkii.botania.common.lib.LibMisc;
import vazkii.botania.common.lib.LibOreDict;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

@Mod.EventBusSubscriber(modid = LibMisc.MOD_ID)
public final class ModFluffBlocks {

	public static List<Block> slabsToRegister = new ArrayList<>();

	public static final Block livingwoodStairs = new BlockLivingwoodStairs();
	public static final Block livingwoodSlab = new BlockLivingwoodSlab(false);
	public static final Block livingwoodSlabFull = new BlockLivingwoodSlab(true);
	public static final Block livingwoodWall = new BlockLivingwoodWall();
	public static final Block livingwoodPlankStairs = new BlockLivingwoodPlankStairs();
	public static final Block livingwoodPlankSlab = new BlockLivingwoodPlankSlab(false);
	public static final Block livingwoodPlankSlabFull = new BlockLivingwoodPlankSlab(true);
	public static final Block livingrockStairs = new BlockLivingrockStairs();
	public static final Block livingrockSlab = new BlockLivingrockSlab(false);
	public static final Block livingrockSlabFull = new BlockLivingrockSlab(true);
	public static final Block livingrockWall = new BlockLivingrockWall();
	public static final Block livingrockBrickStairs = new BlockLivingrockBrickStairs();
	public static final Block livingrockBrickSlab = new BlockLivingrockBrickSlab(false);
	public static final Block livingrockBrickSlabFull = new BlockLivingrockBrickSlab(true);
	public static final Block dreamwoodStairs = new BlockDreamwoodStairs();
	public static final Block dreamwoodSlab = new BlockDreamwoodSlab(false);
	public static final Block dreamwoodSlabFull = new BlockDreamwoodSlab(true);
	public static final Block dreamwoodWall = new BlockDreamwoodWall();
	public static final Block dreamwoodPlankStairs = new BlockDreamwoodPlankStairs();
	public static final Block dreamwoodPlankSlab = new BlockDreamwoodPlankSlab(false);
	public static final Block dreamwoodPlankSlabFull = new BlockDreamwoodPlankSlab(true);

	public static final Block darkQuartz = new BlockSpecialQuartz(LibBlockNames.QUARTZ_DARK);
	public static final Block darkQuartzSlab = new BlockSpecialQuartzSlab(darkQuartz, false);
	public static final Block darkQuartzSlabFull = new BlockSpecialQuartzSlab(darkQuartz, true);
	public static final Block darkQuartzStairs = new BlockSpecialQuartzStairs(darkQuartz);
	public static final Block manaQuartz = new BlockSpecialQuartz(LibBlockNames.QUARTZ_MANA);
	public static final Block manaQuartzSlab = new BlockSpecialQuartzSlab(manaQuartz, false);
	public static final Block manaQuartzSlabFull = new BlockSpecialQuartzSlab(manaQuartz, true);
	public static final Block manaQuartzStairs = new BlockSpecialQuartzStairs(manaQuartz);
	public static final Block blazeQuartz = new BlockSpecialQuartz(LibBlockNames.QUARTZ_BLAZE);
	public static final Block blazeQuartzSlab = new BlockSpecialQuartzSlab(blazeQuartz, false);
	public static final Block blazeQuartzSlabFull = new BlockSpecialQuartzSlab(blazeQuartz, true);
	public static final Block blazeQuartzStairs = new BlockSpecialQuartzStairs(blazeQuartz);
	public static final Block lavenderQuartz = new BlockSpecialQuartz(LibBlockNames.QUARTZ_LAVENDER);
	public static final Block lavenderQuartzSlab = new BlockSpecialQuartzSlab(lavenderQuartz, false);
	public static final Block lavenderQuartzSlabFull = new BlockSpecialQuartzSlab(lavenderQuartz, true);
	public static final Block lavenderQuartzStairs = new BlockSpecialQuartzStairs(lavenderQuartz);
	public static final Block redQuartz = new BlockSpecialQuartz(LibBlockNames.QUARTZ_RED);
	public static final Block redQuartzSlab = new BlockSpecialQuartzSlab(redQuartz, false);
	public static final Block redQuartzSlabFull = new BlockSpecialQuartzSlab(redQuartz, true);
	public static final Block redQuartzStairs = new BlockSpecialQuartzStairs(redQuartz);
	public static final Block elfQuartz = new BlockSpecialQuartz(LibBlockNames.QUARTZ_ELF);
	public static final Block elfQuartzSlab = new BlockSpecialQuartzSlab(elfQuartz, false);
	public static final Block elfQuartzSlabFull = new BlockSpecialQuartzSlab(elfQuartz, true);
	public static final Block elfQuartzStairs = new BlockSpecialQuartzStairs(elfQuartz);
	public static final Block sunnyQuartz = new BlockSpecialQuartz(LibBlockNames.QUARTZ_SUNNY);
	public static final Block sunnyQuartzSlab = new BlockSpecialQuartzSlab(sunnyQuartz, false);
	public static final Block sunnyQuartzSlabFull = new BlockSpecialQuartzSlab(sunnyQuartz, true);
	public static final Block sunnyQuartzStairs = new BlockSpecialQuartzStairs(sunnyQuartz);

	public static final Block biomeStoneA = new BlockBiomeStoneA();
	public static final Block biomeStoneB = new BlockBiomeStoneB();
	public static final Block pavement = new BlockPavement();

	public static final Block[] biomeStoneStairs = new Block[24];
	public static final Block[] biomeStoneSlabs = new Block[24];
	public static final Block[] biomeStoneFullSlabs = new Block[24];
	public static final Block biomeStoneWall = new BlockBiomeStoneWall();

	public static final Block[] pavementStairs = new Block[BlockPavement.TYPES];
	public static final Block[] pavementSlabs = new Block[BlockPavement.TYPES];
	public static final Block[] pavementFullSlabs = new Block[BlockPavement.TYPES];

	public static final Block shimmerrockSlab = new BlockShimmerrockSlab(false);
	public static final Block shimmerrockSlabFull = new BlockShimmerrockSlab(true);
	public static final Block shimmerrockStairs = new BlockShimmerrockStairs();
	public static final Block shimmerwoodPlankSlab = new BlockShimmerwoodPlankSlab(false);
	public static final Block shimmerwoodPlankSlabFull = new BlockShimmerwoodPlankSlab(true);
	public static final Block shimmerwoodPlankStairs = new BlockShimmerwoodPlankStairs();

	public static final Block managlassPane = new BlockManaglassPane();
	public static final Block alfglassPane = new BlockAlfglassPane();
	public static final Block bifrostPane = new BlockBifrostPane();

	@SubscribeEvent
	public static void register(RegistryEvent.Register<Block> evt) {
		IForgeRegistry<Block> r = evt.getRegistry();
		r.register(livingwoodStairs);
		r.register(livingwoodSlab);
		r.register(livingwoodSlabFull);
		r.register(livingwoodWall);
		
		r.register(livingwoodPlankStairs);
		r.register(livingwoodPlankSlab);
		r.register(livingwoodPlankSlabFull);
		
		r.register(livingrockStairs);
		r.register(livingrockSlab);
		r.register(livingrockSlabFull);
		r.register(livingrockWall);
		
		r.register(livingrockBrickStairs);
		r.register(livingrockBrickSlab);
		r.register(livingrockBrickSlabFull);
		
		r.register(dreamwoodStairs);
		r.register(dreamwoodSlab);
		r.register(dreamwoodSlabFull);
		r.register(dreamwoodWall);
		
		r.register(dreamwoodPlankStairs);
		r.register(dreamwoodPlankSlab);
		r.register(dreamwoodPlankSlabFull);

		if(ConfigHandler.darkQuartzEnabled) {
			r.register(darkQuartz);
			r.register(darkQuartzSlab);
			r.register(darkQuartzSlabFull);
			r.register(darkQuartzStairs);
		}
		
		r.register(manaQuartz);
		r.register(manaQuartzSlab);
		r.register(manaQuartzSlabFull);
		r.register(manaQuartzStairs);
		
		r.register(blazeQuartz);
		r.register(blazeQuartzSlab);
		r.register(blazeQuartzSlabFull);
		r.register(blazeQuartzStairs);
		
		r.register(lavenderQuartz);
		r.register(lavenderQuartzSlab);
		r.register(lavenderQuartzSlabFull);
		r.register(lavenderQuartzStairs);
		
		r.register(redQuartz);
		r.register(redQuartzSlab);
		r.register(redQuartzSlabFull);
		r.register(redQuartzStairs);
		
		r.register(elfQuartz);
		r.register(elfQuartzSlab);
		r.register(elfQuartzSlabFull);
		r.register(elfQuartzStairs);
		
		r.register(sunnyQuartz);
		r.register(sunnyQuartzSlab);
		r.register(sunnyQuartzSlabFull);
		r.register(sunnyQuartzStairs);

		r.register(biomeStoneA);
		r.register(biomeStoneB);
		r.register(pavement);
		
		int count = 0;
		for (BiomeStoneVariant variant : BiomeStoneVariant.values()) {
			biomeStoneStairs[count] = new BlockBiomeStoneStairs(biomeStoneA.getDefaultState().withProperty(BotaniaStateProps.BIOMESTONE_VARIANT, variant));
			biomeStoneSlabs[count] = new BlockBiomeStoneSlab(false, biomeStoneA.getDefaultState().withProperty(BotaniaStateProps.BIOMESTONE_VARIANT, variant), count);
			biomeStoneFullSlabs[count] = new BlockBiomeStoneSlab(true, biomeStoneA.getDefaultState().withProperty(BotaniaStateProps.BIOMESTONE_VARIANT, variant), count);
			r.register(biomeStoneStairs[count]);
			r.register(biomeStoneSlabs[count]);
			r.register(biomeStoneFullSlabs[count]);
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
			r.register(biomeStoneStairs[count]);
			r.register(biomeStoneSlabs[count]);
			r.register(biomeStoneFullSlabs[count]);
			count++;
		}
		
		r.register(biomeStoneWall);

		count = 0;
		for (EnumDyeColor color : ImmutableList.of(EnumDyeColor.WHITE, EnumDyeColor.BLACK, EnumDyeColor.BLUE,
				EnumDyeColor.RED, EnumDyeColor.YELLOW, EnumDyeColor.GREEN)) {
			pavementStairs[count] = new BlockPavementStairs(color);
			pavementSlabs[count] = new BlockPavementSlab(false, color, count);
			pavementFullSlabs[count] = new BlockPavementSlab(true, color, count);
			r.register(pavementStairs[count]);
			r.register(pavementSlabs[count]);
			r.register(pavementFullSlabs[count]);
			count++;
		}

		r.register(shimmerrockSlab);
		r.register(shimmerrockSlabFull);
		r.register(shimmerrockStairs);
		
		r.register(shimmerwoodPlankSlab);
		r.register(shimmerwoodPlankSlabFull);
		r.register(shimmerwoodPlankStairs);

		r.register(managlassPane);
		r.register(alfglassPane);
		r.register(bifrostPane);
	}
	
	@SubscribeEvent
	public static void registerItemBlocks(RegistryEvent.Register<Item> evt) {
		IForgeRegistry<Item> r = evt.getRegistry();
		r.register(new ItemBlockMod(livingwoodStairs).setRegistryName(livingwoodStairs.getRegistryName()));
		r.register(new ItemBlockModSlab(livingwoodSlab).setRegistryName(livingwoodSlab.getRegistryName()));
		r.register(new ItemBlockMod(livingwoodWall).setRegistryName(livingwoodWall.getRegistryName()));
		
		r.register(new ItemBlockMod(livingwoodPlankStairs).setRegistryName(livingwoodPlankStairs.getRegistryName()));
		r.register(new ItemBlockModSlab(livingwoodPlankSlab).setRegistryName(livingwoodPlankSlab.getRegistryName()));
		
		r.register(new ItemBlockMod(livingrockStairs).setRegistryName(livingrockStairs.getRegistryName()));
		r.register(new ItemBlockModSlab(livingrockSlab).setRegistryName(livingrockSlab.getRegistryName()));
		r.register(new ItemBlockMod(livingrockWall).setRegistryName(livingrockWall.getRegistryName()));
		
		r.register(new ItemBlockMod(livingrockBrickStairs).setRegistryName(livingrockBrickStairs.getRegistryName()));
		r.register(new ItemBlockModSlab(livingrockBrickSlab).setRegistryName(livingrockBrickSlab.getRegistryName()));
		
		r.register(new ItemBlockMod(dreamwoodStairs).setRegistryName(dreamwoodStairs.getRegistryName()));
		r.register(new ItemBlockModSlab(dreamwoodSlab).setRegistryName(dreamwoodSlab.getRegistryName()));
		r.register(new ItemBlockMod(dreamwoodWall).setRegistryName(dreamwoodWall.getRegistryName()));
		
		r.register(new ItemBlockMod(dreamwoodPlankStairs).setRegistryName(dreamwoodPlankStairs.getRegistryName()));
		r.register(new ItemBlockModSlab(dreamwoodPlankSlab).setRegistryName(dreamwoodPlankSlab.getRegistryName()));
		
		if(ConfigHandler.darkQuartzEnabled) {
			r.register(new ItemBlockSpecialQuartz(darkQuartz).setRegistryName(darkQuartz.getRegistryName()));
			r.register(new ItemBlockModSlab(darkQuartzSlab).setRegistryName(darkQuartzSlab.getRegistryName()));
			r.register(new ItemBlockMod(darkQuartzStairs).setRegistryName(darkQuartzStairs.getRegistryName()));
		}
		
		r.register(new ItemBlockSpecialQuartz(manaQuartz).setRegistryName(manaQuartz.getRegistryName()));
		r.register(new ItemBlockModSlab(manaQuartzSlab).setRegistryName(manaQuartzSlab.getRegistryName()));
		r.register(new ItemBlockMod(manaQuartzStairs).setRegistryName(manaQuartzStairs.getRegistryName()));
		
		r.register(new ItemBlockSpecialQuartz(blazeQuartz).setRegistryName(blazeQuartz.getRegistryName()));
		r.register(new ItemBlockModSlab(blazeQuartzSlab).setRegistryName(blazeQuartzSlab.getRegistryName()));
		r.register(new ItemBlockMod(blazeQuartzStairs).setRegistryName(blazeQuartzStairs.getRegistryName()));
		
		r.register(new ItemBlockSpecialQuartz(lavenderQuartz).setRegistryName(lavenderQuartz.getRegistryName()));
		r.register(new ItemBlockModSlab(lavenderQuartzSlab).setRegistryName(lavenderQuartzSlab.getRegistryName()));
		r.register(new ItemBlockMod(lavenderQuartzStairs).setRegistryName(lavenderQuartzStairs.getRegistryName()));
		
		r.register(new ItemBlockSpecialQuartz(redQuartz).setRegistryName(redQuartz.getRegistryName()));
		r.register(new ItemBlockModSlab(redQuartzSlab).setRegistryName(redQuartzSlab.getRegistryName()));
		r.register(new ItemBlockMod(redQuartzStairs).setRegistryName(redQuartzStairs.getRegistryName()));
		
		r.register(new ItemBlockSpecialQuartz(elfQuartz).setRegistryName(elfQuartz.getRegistryName()));
		r.register(new ItemBlockModSlab(elfQuartzSlab).setRegistryName(elfQuartzSlab.getRegistryName()));
		r.register(new ItemBlockMod(elfQuartzStairs).setRegistryName(elfQuartzStairs.getRegistryName()));
		
		r.register(new ItemBlockSpecialQuartz(sunnyQuartz).setRegistryName(sunnyQuartz.getRegistryName()));
		r.register(new ItemBlockModSlab(sunnyQuartzSlab).setRegistryName(sunnyQuartzSlab.getRegistryName()));
		r.register(new ItemBlockMod(sunnyQuartzStairs).setRegistryName(sunnyQuartzStairs.getRegistryName()));
		
		r.register(new ItemBlockWithMetadataAndName(biomeStoneA).setRegistryName(biomeStoneA.getRegistryName()));
		r.register(new ItemBlockWithMetadataAndName(biomeStoneB).setRegistryName(biomeStoneB.getRegistryName()));
		r.register(new ItemBlockWithMetadataAndName(pavement).setRegistryName(pavement.getRegistryName()));
		
		for (int i = 0; i < 24; i++) {
			r.register(new ItemBlockMod(biomeStoneStairs[i]).setRegistryName(biomeStoneStairs[i].getRegistryName()));
			r.register(new ItemBlockModSlab(biomeStoneSlabs[i]).setRegistryName(biomeStoneSlabs[i].getRegistryName()));
		}
		
		r.register(new ItemBlockWithMetadataAndName(biomeStoneWall).setRegistryName(biomeStoneWall.getRegistryName()));
		
		for (int i = 0; i < BlockPavement.TYPES; i++) {
			r.register(new ItemBlockMod(pavementStairs[i]).setRegistryName(pavementStairs[i].getRegistryName()));
			r.register(new ItemBlockModSlab(pavementSlabs[i]).setRegistryName(pavementSlabs[i].getRegistryName()));
		}
		
		r.register(new ItemBlockModSlab(shimmerrockSlab).setRegistryName(shimmerrockSlab.getRegistryName()));
		r.register(new ItemBlockMod(shimmerrockStairs).setRegistryName(shimmerrockStairs.getRegistryName()));
		
		r.register(new ItemBlockModSlab(shimmerwoodPlankSlab).setRegistryName(shimmerwoodPlankSlab.getRegistryName()));
		r.register(new ItemBlockMod(shimmerwoodPlankStairs).setRegistryName(shimmerwoodPlankStairs.getRegistryName()));
		
		r.register(new ItemBlockMod(managlassPane).setRegistryName(managlassPane.getRegistryName()));
		r.register(new ItemBlockMod(alfglassPane).setRegistryName(alfglassPane.getRegistryName()));
		r.register(new ItemBlockMod(bifrostPane).setRegistryName(bifrostPane.getRegistryName()));
		registerOreDictionary();
	}

	private static void registerOreDictionary() {
		Block[] quartzBlocks=new Block[] {
				darkQuartz, manaQuartz, blazeQuartz, lavenderQuartz,
				redQuartz, elfQuartz, sunnyQuartz
		};

		for(int i = 0; i < 7; i++) {
			for(int j=0;j< QuartzVariant.values().length;j++) {
				OreDictionary.registerOre(LibOreDict.QUARTZ_BLOCKS[i], new ItemStack(quartzBlocks[i],1,j));
			}
		}

		Block[] quartzSlabs=new Block[] {
				darkQuartzSlab, manaQuartzSlab, blazeQuartzSlab, lavenderQuartzSlab,
				redQuartzSlab,  elfQuartzSlab, sunnyQuartzSlab,
		};

		for(int i = 0; i < 7; i++)
			OreDictionary.registerOre(LibOreDict.QUARTZ_SLABS[i], quartzSlabs[i]);

		Block[] quartzStairs = new Block[] {
			darkQuartzStairs, manaQuartzStairs, blazeQuartzStairs, lavenderQuartzStairs,
			redQuartzStairs, elfQuartzStairs, sunnyQuartzStairs
		};
		for(int i = 0; i < 7; i++)
			OreDictionary.registerOre(LibOreDict.QUARTZ_STAIRS[i], quartzStairs[i]);
	}

}
