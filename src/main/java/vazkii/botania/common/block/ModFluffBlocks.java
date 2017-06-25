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

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import com.google.common.collect.ImmutableList;

import net.minecraft.block.Block;
import net.minecraft.item.EnumDyeColor;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.registries.IForgeRegistry;
import vazkii.botania.api.state.BotaniaStateProps;
import vazkii.botania.api.state.enums.BiomeBrickVariant;
import vazkii.botania.api.state.enums.BiomeStoneVariant;
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
import vazkii.botania.common.item.block.ItemBlockModSlab;
import vazkii.botania.common.lib.LibBlockNames;

@Mod.EventBusSubscriber
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

		for(Block b : slabsToRegister)
			GameRegistry.register(new ItemBlockModSlab(b), b.getRegistryName());
	}

}
