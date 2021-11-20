/*
 * This class is distributed as part of the Botania Mod.
 * Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 *
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 */
package vazkii.botania.data;

import net.fabricmc.fabric.impl.tag.extension.TagDelegate;
import net.minecraft.core.Registry;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.tags.BlockTagsProvider;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.BlockTags;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.FenceBlock;
import net.minecraft.world.level.block.FenceGateBlock;
import net.minecraft.world.level.block.SlabBlock;
import net.minecraft.world.level.block.StairBlock;
import net.minecraft.world.level.block.WallBlock;
import net.minecraft.world.level.material.Material;

import vazkii.botania.common.block.*;
import vazkii.botania.common.block.decor.BlockFloatingFlower;
import vazkii.botania.common.block.mana.BlockForestDrum;
import vazkii.botania.common.block.mana.BlockPool;
import vazkii.botania.common.block.mana.BlockSpreader;
import vazkii.botania.common.block.string.BlockRedString;
import vazkii.botania.common.lib.LibBlockNames;
import vazkii.botania.common.lib.LibMisc;
import vazkii.botania.common.lib.ModTags;

import javax.annotation.Nonnull;

import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.Set;
import java.util.function.Predicate;

import static vazkii.botania.common.block.ModBlocks.*;
import static vazkii.botania.common.block.ModSubtiles.*;

public class BlockTagProvider extends BlockTagsProvider {
	private static final Predicate<Block> BOTANIA_BLOCK = b -> LibMisc.MOD_ID.equals(Registry.BLOCK.getKey(b).getNamespace());

	public BlockTagProvider(DataGenerator generator) {
		super(generator);
	}

	@Override
	protected void addTags() {
		tag(BlockTags.RAILS).add(ModBlocks.ghostRail);
		tag(BlockTags.SLABS).add(getModBlocks(b -> b instanceof SlabBlock));
		tag(BlockTags.WOODEN_SLABS).add(getModBlocks(b -> b instanceof SlabBlock && b.defaultBlockState().getMaterial() == Material.WOOD));
		tag(BlockTags.STAIRS).add(getModBlocks(b -> b instanceof StairBlock));
		tag(BlockTags.WOODEN_STAIRS).add(getModBlocks(b -> b instanceof StairBlock && b.defaultBlockState().getMaterial() == Material.WOOD));
		tag(BlockTags.WALLS).add(getModBlocks(b -> b instanceof WallBlock));
		tag(BlockTags.FENCES).add(getModBlocks(b -> b instanceof FenceBlock));
		tag(BlockTags.WOODEN_FENCES).add(getModBlocks(b -> b instanceof FenceBlock && b.defaultBlockState().getMaterial() == Material.WOOD));
		tag(BlockTags.FENCE_GATES).add(getModBlocks(b -> b instanceof FenceGateBlock));
		tag(BlockTags.DRAGON_IMMUNE).add(ModBlocks.infrangiblePlatform);
		tag(BlockTags.WITHER_IMMUNE).add(ModBlocks.infrangiblePlatform);

		tag(ModTags.Blocks.MUNDANE_FLOATING_FLOWERS).add(
				Arrays.stream(DyeColor.values())
						.map(ModBlocks::getFloatingFlower)
						.sorted(Comparator.comparing(Registry.BLOCK::getKey))
						.toArray(Block[]::new)
		);

		tag(ModTags.Blocks.SPECIAL_FLOATING_FLOWERS).add(registry.stream().filter(BOTANIA_BLOCK)
				.filter(b -> b instanceof BlockFloatingSpecialFlower)
				.sorted(Comparator.comparing(Registry.BLOCK::getKey))
				.toArray(Block[]::new)
		);

		tag(ModTags.Blocks.FLOATING_FLOWERS).addTag(ModTags.Blocks.MUNDANE_FLOATING_FLOWERS)
				.addTag(ModTags.Blocks.SPECIAL_FLOATING_FLOWERS);

		tag(ModTags.Blocks.MYSTICAL_FLOWERS).add(
				Arrays.stream(DyeColor.values())
						.map(ModBlocks::getFlower)
						.sorted(Comparator.comparing(Registry.BLOCK::getKey))
						.toArray(Block[]::new)
		);

		tag(ModTags.Blocks.SHINY_FLOWERS).add(
				Arrays.stream(DyeColor.values())
						.map(ModBlocks::getShinyFlower)
						.sorted(Comparator.comparing(Registry.BLOCK::getKey))
						.toArray(Block[]::new)
		);

		tag(ModTags.Blocks.DOUBLE_MYSTICAL_FLOWERS).add(
				Arrays.stream(DyeColor.values())
						.map(ModBlocks::getDoubleFlower)
						.sorted(Comparator.comparing(Registry.BLOCK::getKey))
						.toArray(Block[]::new)
		);

		tag(ModTags.Blocks.MISC_SPECIAL_FLOWERS).add(manastar, pureDaisy, bergamute);
		tag(ModTags.Blocks.GENERATING_SPECIAL_FLOWERS).add(
				dandelifeon, endoflame, entropinnyum,
				gourmaryllis, hydroangeas, kekimurus,
				munchdew, narslimmus, rafflowsia, rosaArcana,
				shulkMeNot, spectrolus, thermalily
		);
		tag(ModTags.Blocks.FUNCTIONAL_SPECIAL_FLOWERS).add(
				agricarnation, agricarnationChibi, bellethorn, bellethornChibi,
				bubbell, bubbellChibi, clayconia, clayconiaChibi,
				daffomill, dreadthorn, exoflame, fallenKanade, heiseiDream,
				hopperhock, hopperhockChibi, hyacidus, jadedAmaranthus,
				jiyuulia, jiyuuliaChibi, labellia, loonium, marimorphosis, marimorphosisChibi,
				medumone, orechid, orechidIgnem, pollidisiac, rannuncarpus, rannuncarpusChibi,
				solegnolia, solegnoliaChibi, spectranthemum, tangleberrie, tangleberrieChibi, tigerseye, vinculotus
		);
		tag(ModTags.Blocks.SPECIAL_FLOWERS).addTag(ModTags.Blocks.MISC_SPECIAL_FLOWERS)
				.addTag(ModTags.Blocks.GENERATING_SPECIAL_FLOWERS)
				.addTag(ModTags.Blocks.FUNCTIONAL_SPECIAL_FLOWERS);

		tag(ModTags.Blocks.MINI_FLOWERS).add(
				getModBlocks(b -> b instanceof BlockSpecialFlower && registry.getKey(b).getPath().endsWith("_chibi"))
		);

		tag(ModTags.Blocks.ENCHANTER_FLOWERS).addTag(ModTags.Blocks.MYSTICAL_FLOWERS)
				.addTag(ModTags.Blocks.SHINY_FLOWERS)
				.addTag(ModTags.Blocks.MUNDANE_FLOATING_FLOWERS);

		// Special flowers intentionally excluded due to unwanted behaviors with tree growth and mod compat.
		tag(BlockTags.TALL_FLOWERS).addTag(ModTags.Blocks.DOUBLE_MYSTICAL_FLOWERS);
		tag(BlockTags.SMALL_FLOWERS).addTag(ModTags.Blocks.MYSTICAL_FLOWERS);

		tag(BlockTags.IMPERMEABLE).add(ModBlocks.elfGlass, ModBlocks.manaGlass, ModBlocks.bifrost, ModBlocks.bifrostPerm);
		tag(BlockTags.BEACON_BASE_BLOCKS).add(ModBlocks.manasteelBlock, ModBlocks.terrasteelBlock, ModBlocks.elementiumBlock,
				ModBlocks.manaDiamondBlock, ModBlocks.dragonstoneBlock);

		tag(BlockTags.DIRT).add(getModBlocks(b -> b instanceof BlockAltGrass));
		tag(ModTags.Blocks.BLOCKS_ELEMENTIUM).add(ModBlocks.elementiumBlock);
		tag(ModTags.Blocks.BLOCKS_MANASTEEL).add(ModBlocks.manasteelBlock);
		tag(ModTags.Blocks.BLOCKS_QUARTZ).add(
				ModFluffBlocks.darkQuartz, ModFluffBlocks.manaQuartz, ModFluffBlocks.blazeQuartz,
				ModFluffBlocks.lavenderQuartz, ModFluffBlocks.redQuartz, ModFluffBlocks.elfQuartz, ModFluffBlocks.sunnyQuartz
		);
		tag(ModTags.Blocks.BLOCKS_TERRASTEEL).add(ModBlocks.terrasteelBlock);

		tag(ModTags.Blocks.CORPOREA_SPARK_OVERRIDE).add(
				ModBlocks.corporeaBlock, ModBlocks.corporeaBrick, ModBlocks.corporeaBrickSlab, ModBlocks.corporeaBrickStairs,
				ModBlocks.corporeaBrickWall, ModBlocks.corporeaCrystalCube, ModBlocks.corporeaFunnel, ModBlocks.corporeaIndex,
				ModBlocks.corporeaInterceptor, ModBlocks.corporeaSlab, ModBlocks.corporeaStairs);

		tag(BlockTags.SAND); // We aren't calling vanilla's generation, so need to add a dummy so that using this below doesn't error out.
		tag(ModTags.Blocks.TERRAFORMABLE)
				.add(Blocks.ANDESITE, Blocks.DIORITE, Blocks.GRANITE, Blocks.INFESTED_STONE, Blocks.STONE, Blocks.POLISHED_ANDESITE, Blocks.POLISHED_DIORITE, Blocks.POLISHED_GRANITE)
				.add(Blocks.DIRT, Blocks.GRASS_BLOCK, Blocks.COARSE_DIRT, Blocks.PODZOL, Blocks.MYCELIUM)
				.add(Blocks.GRASS_BLOCK, Blocks.GRAVEL, Blocks.SNOW)
				.addTag(BlockTags.SAND);
		tag(ModTags.Blocks.GAIA_BREAK_BLACKLIST).add(Blocks.BEACON, ModBlocks.manaPylon, ModBlocks.naturaPylon, ModBlocks.gaiaPylon);
		tag(ModTags.Blocks.MAGNET_RING_BLACKLIST).add(ModBlocks.manaPool, ModBlocks.creativePool, ModBlocks.dilutedPool,
				ModBlocks.fabulousPool, ModBlocks.terraPlate, ModBlocks.runeAltar);
		tag(ModTags.Blocks.LAPUTA_IMMOBILE);

		tag(ModTags.Blocks.TERRA_PLATE_BASE).add(ModBlocks.livingrock, ModBlocks.shimmerrock);

		tag(BlockTags.CLIMBABLE).add(ModBlocks.solidVines);

		for (DyeColor color : DyeColor.values()) {
			this.tag(ModTags.Blocks.MUSHROOMS).add(ModBlocks.getMushroom(color));
		}

		tag(BlockTags.PLANKS).add(livingwoodPlanks, livingwoodPlanksMossy, livingwoodFramed, livingwoodPatternFramed,
				dreamwoodPlanks, dreamwoodPlanksMossy, dreamwoodFramed, dreamwoodPatternFramed, shimmerwoodPlanks);

		tag(ModTags.Blocks.LIVINGWOOD_LOGS_GLIMMERING).add(livingwoodGlimmering, livingwoodLogGlimmering,
				livingwoodStrippedGlimmering, livingwoodLogStrippedGlimmering);
		tag(ModTags.Blocks.DREAMWOOD_LOGS_GLIMMERING).add(dreamwoodGlimmering, dreamwoodLogGlimmering,
				dreamwoodStrippedGlimmering, dreamwoodLogStrippedGlimmering);

		tag(ModTags.Blocks.LIVINGWOOD_LOGS)
				.add(livingwoodLog, livingwood, livingwoodLogStripped, livingwoodStripped)
				.addTag(ModTags.Blocks.LIVINGWOOD_LOGS_GLIMMERING);
		tag(ModTags.Blocks.DREAMWOOD_LOGS)
				.add(dreamwoodLog, dreamwood, dreamwoodLogStripped, dreamwoodStripped)
				.addTag(ModTags.Blocks.DREAMWOOD_LOGS_GLIMMERING);
		tag(BlockTags.LOGS_THAT_BURN).addTag(ModTags.Blocks.LIVINGWOOD_LOGS).addTag(ModTags.Blocks.DREAMWOOD_LOGS);

		tag(ModTags.Blocks.GHOST_RAIL_BARRIER).addTag(ModTags.Blocks.DREAMWOOD_LOGS);

		tag(new TagDelegate<>(new ResourceLocation("buzzier_bees:flower_blacklist"), BlockTags::getAllTags))
				.addTag(ModTags.Blocks.MYSTICAL_FLOWERS)
				.addTag(ModTags.Blocks.SPECIAL_FLOWERS);

		registerCommonTags();
		registerMiningTags();
	}

	private void registerMiningTags() {
		tag(BlockTags.MINEABLE_WITH_HOE).add(
				getModBlocks(b -> b == cellBlock
						|| Registry.BLOCK.getKey(b).getPath().contains(LibBlockNames.PETAL_BLOCK_SUFFIX)
				)
		);
		tag(BlockTags.MINEABLE_WITH_SHOVEL).add(
				getModBlocks(b -> b == enchantedSoil
						|| b instanceof BlockFloatingFlower || b instanceof BlockAltGrass)
		);
		var pickaxe = Set.of(
				alchemyCatalyst, conjurationCatalyst,
				manasteelBlock, elementiumBlock, terrasteelBlock, manaDiamondBlock, dragonstoneBlock,
				manaGlass, elfGlass, bifrostPerm,
				ModFluffBlocks.managlassPane, ModFluffBlocks.alfglassPane, ModFluffBlocks.bifrostPane,
				runeAltar, brewery, terraPlate, distributor, manaVoid, manaDetector,
				pistonRelay, tinyPlanet, spawnerClaw,
				rfGenerator, prism, pump, sparkChanger, forestEye, enderEye,
				hourglass, starfield, blazeBlock
		);
		tag(BlockTags.MINEABLE_WITH_PICKAXE).add(
				getModBlocks(b -> pickaxe.contains(b)
						|| b instanceof BlockAltar
						|| b instanceof BlockPylon
						|| b instanceof BlockPool
						|| b instanceof BlockRedString
						|| Registry.BLOCK.getKey(b).getPath().contains(LibBlockNames.AZULEJO_PREFIX)
						|| Registry.BLOCK.getKey(b).getPath().contains("corporea")
						|| Registry.BLOCK.getKey(b).getPath().contains(LibBlockNames.PAVEMENT_SUFFIX)
						|| Registry.BLOCK.getKey(b).getPath().contains("_quartz")
						|| (Registry.BLOCK.getKey(b).getPath().contains(LibBlockNames.METAMORPHIC_PREFIX)
								&& !(b instanceof WallBlock)) // vanilla includes #wall already
						|| (Registry.BLOCK.getKey(b).getPath().contains(LibBlockNames.LIVING_ROCK)
								&& !(b instanceof WallBlock)) // vanilla includes #wall already
						|| Registry.BLOCK.getKey(b).getPath().contains(LibBlockNames.SHIMMERROCK)
				)
		);
		var axe = Set.of(
				alfPortal, turntable, manaBomb, bellows, incensePlate,
				cacophonium, avatar, root, felPumpkin
		);
		tag(BlockTags.MINEABLE_WITH_AXE).add(
				getModBlocks(b -> axe.contains(b)
						|| b instanceof BlockForestDrum
						|| b instanceof BlockOpenCrate
						|| b instanceof BlockPlatform
						|| b instanceof BlockSpreader
						|| Registry.BLOCK.getKey(b).getPath().contains(LibBlockNames.LIVING_WOOD)
						|| Registry.BLOCK.getKey(b).getPath().contains(LibBlockNames.DREAM_WOOD)
						|| Registry.BLOCK.getKey(b).getPath().contains(LibBlockNames.SHIMMERWOOD_PLANKS)
				)
		);
	}

	private void registerCommonTags() {
		tag(ModTags.Blocks.LAPIS_BLOCKS).add(Blocks.LAPIS_BLOCK);

		var vanillaTags = List.of(
				BlockTags.COAL_ORES,
				BlockTags.IRON_ORES,
				BlockTags.GOLD_ORES,
				BlockTags.LAPIS_ORES,
				BlockTags.REDSTONE_ORES,
				BlockTags.DIAMOND_ORES,
				BlockTags.COPPER_ORES,
				BlockTags.EMERALD_ORES
		);
		// We aren't calling vanilla's generation, so need to add dummy calls so that using them below doesn't error out.
		vanillaTags.forEach(this::tag);

		var oreTag = tag(ModTags.Blocks.ORES);
		vanillaTags.forEach(oreTag::addTag);
	}

	@Nonnull
	private Block[] getModBlocks(Predicate<Block> predicate) {
		return registry.stream().filter(BOTANIA_BLOCK.and(predicate))
				.sorted(Comparator.comparing(Registry.BLOCK::getKey))
				.toArray(Block[]::new);
	}

	@Nonnull
	@Override
	public String getName() {
		return "Botania block tags";
	}
}
