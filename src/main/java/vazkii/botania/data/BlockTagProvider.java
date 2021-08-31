/*
 * This class is distributed as part of the Botania Mod.
 * Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 *
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 */
package vazkii.botania.data;

import net.minecraft.block.*;
import net.minecraft.data.BlockTagsProvider;
import net.minecraft.data.DataGenerator;
import net.minecraft.item.DyeColor;
import net.minecraft.tags.BlockTags;
import net.minecraft.util.registry.Registry;
import net.minecraftforge.common.Tags;
import net.minecraftforge.common.data.ExistingFileHelper;

import vazkii.botania.common.block.*;
import vazkii.botania.common.lib.LibMisc;
import vazkii.botania.common.lib.ModTags;

import javax.annotation.Nonnull;

import java.util.Arrays;
import java.util.Comparator;
import java.util.function.Predicate;

import static vazkii.botania.common.block.ModSubtiles.*;

public class BlockTagProvider extends BlockTagsProvider {
	private static final Predicate<Block> BOTANIA_BLOCK = b -> LibMisc.MOD_ID.equals(Registry.BLOCK.getKey(b).getNamespace());

	public BlockTagProvider(DataGenerator generator, ExistingFileHelper helper) {
		super(generator, LibMisc.MOD_ID, helper);
	}

	@Override
	protected void registerTags() {
		getOrCreateBuilder(BlockTags.RAILS).addItemEntry(ModBlocks.ghostRail);
		getOrCreateBuilder(BlockTags.SLABS).add(getModBlocks(b -> b instanceof SlabBlock));
		getOrCreateBuilder(BlockTags.STAIRS).add(getModBlocks(b -> b instanceof StairsBlock));
		getOrCreateBuilder(BlockTags.WALLS).add(getModBlocks(b -> b instanceof WallBlock));
		getOrCreateBuilder(BlockTags.FENCES).add(getModBlocks(b -> b instanceof FenceBlock));
		getOrCreateBuilder(BlockTags.FENCE_GATES).add(getModBlocks(b -> b instanceof FenceGateBlock));
		getOrCreateBuilder(BlockTags.DRAGON_IMMUNE).add(ModBlocks.infrangiblePlatform);
		getOrCreateBuilder(BlockTags.WITHER_IMMUNE).add(ModBlocks.infrangiblePlatform);

		getOrCreateBuilder(ModTags.Blocks.MUNDANE_FLOATING_FLOWERS).add(
				Arrays.stream(DyeColor.values())
						.map(ModBlocks::getFloatingFlower)
						.sorted(Comparator.comparing(Registry.BLOCK::getKey))
						.toArray(Block[]::new)
		);

		getOrCreateBuilder(ModTags.Blocks.SPECIAL_FLOATING_FLOWERS).add(registry.stream().filter(BOTANIA_BLOCK)
				.filter(b -> b instanceof BlockFloatingSpecialFlower)
				.sorted(Comparator.comparing(Registry.BLOCK::getKey))
				.toArray(Block[]::new)
		);

		getOrCreateBuilder(ModTags.Blocks.FLOATING_FLOWERS).addTag(ModTags.Blocks.MUNDANE_FLOATING_FLOWERS)
				.addTag(ModTags.Blocks.SPECIAL_FLOATING_FLOWERS);

		getOrCreateBuilder(ModTags.Blocks.MYSTICAL_FLOWERS).add(
				Arrays.stream(DyeColor.values())
						.map(ModBlocks::getFlower)
						.sorted(Comparator.comparing(Registry.BLOCK::getKey))
						.toArray(Block[]::new)
		);

		getOrCreateBuilder(ModTags.Blocks.SHINY_FLOWERS).add(
				Arrays.stream(DyeColor.values())
						.map(ModBlocks::getShinyFlower)
						.sorted(Comparator.comparing(Registry.BLOCK::getKey))
						.toArray(Block[]::new)
		);

		getOrCreateBuilder(ModTags.Blocks.DOUBLE_MYSTICAL_FLOWERS).add(
				Arrays.stream(DyeColor.values())
						.map(ModBlocks::getDoubleFlower)
						.sorted(Comparator.comparing(Registry.BLOCK::getKey))
						.toArray(Block[]::new)
		);

		getOrCreateBuilder(ModTags.Blocks.MISC_SPECIAL_FLOWERS).add(manastar, pureDaisy, bergamute);
		getOrCreateBuilder(ModTags.Blocks.GENERATING_SPECIAL_FLOWERS).add(
				dandelifeon, endoflame, entropinnyum,
				gourmaryllis, hydroangeas, kekimurus,
				munchdew, narslimmus, rafflowsia, rosaArcana,
				shulkMeNot, spectrolus, thermalily
		);
		getOrCreateBuilder(ModTags.Blocks.FUNCTIONAL_SPECIAL_FLOWERS).add(
				agricarnation, agricarnationChibi, bellethorn, bellethornChibi,
				bubbell, bubbellChibi, clayconia, clayconiaChibi,
				daffomill, dreadthorn, exoflame, fallenKanade, heiseiDream,
				hopperhock, hopperhockChibi, hyacidus, jadedAmaranthus,
				jiyuulia, labellia, loonium, marimorphosis, marimorphosisChibi,
				medumone, orechid, orechidIgnem, pollidisiac, rannuncarpus, rannuncarpusChibi,
				solegnolia, solegnoliaChibi, spectranthemum, tangleberrie, tigerseye, vinculotus
		);
		getOrCreateBuilder(ModTags.Blocks.SPECIAL_FLOWERS).addTag(ModTags.Blocks.MISC_SPECIAL_FLOWERS)
				.addTag(ModTags.Blocks.GENERATING_SPECIAL_FLOWERS)
				.addTag(ModTags.Blocks.FUNCTIONAL_SPECIAL_FLOWERS);

		getOrCreateBuilder(ModTags.Blocks.MINI_FLOWERS).add(
				getModBlocks(b -> b instanceof BlockSpecialFlower && registry.getKey(b).getPath().endsWith("_chibi"))
		);

		getOrCreateBuilder(ModTags.Blocks.ENCHANTER_FLOWERS).addTag(ModTags.Blocks.MYSTICAL_FLOWERS)
				.addTag(ModTags.Blocks.SHINY_FLOWERS)
				.addTag(ModTags.Blocks.MUNDANE_FLOATING_FLOWERS);

		// Special flowers intentionally excluded due to unwanted behaviors with tree growth and mod compat.
		getOrCreateBuilder(BlockTags.TALL_FLOWERS).addTag(ModTags.Blocks.DOUBLE_MYSTICAL_FLOWERS);
		getOrCreateBuilder(BlockTags.SMALL_FLOWERS).addTag(ModTags.Blocks.MYSTICAL_FLOWERS);

		getOrCreateBuilder(BlockTags.IMPERMEABLE).add(ModBlocks.elfGlass, ModBlocks.manaGlass, ModBlocks.bifrost, ModBlocks.bifrostPerm);
		getOrCreateBuilder(BlockTags.BEACON_BASE_BLOCKS).add(ModBlocks.manasteelBlock, ModBlocks.terrasteelBlock, ModBlocks.elementiumBlock,
				ModBlocks.manaDiamondBlock, ModBlocks.dragonstoneBlock);

		getOrCreateBuilder(Tags.Blocks.DIRT).add(getModBlocks(b -> b instanceof BlockAltGrass));
		getOrCreateBuilder(ModTags.Blocks.BLOCKS_ELEMENTIUM).add(ModBlocks.elementiumBlock);
		getOrCreateBuilder(ModTags.Blocks.BLOCKS_MANASTEEL).add(ModBlocks.manasteelBlock);
		getOrCreateBuilder(ModTags.Blocks.BLOCKS_QUARTZ).add(
				ModFluffBlocks.darkQuartz, ModFluffBlocks.manaQuartz, ModFluffBlocks.blazeQuartz,
				ModFluffBlocks.lavenderQuartz, ModFluffBlocks.redQuartz, ModFluffBlocks.elfQuartz, ModFluffBlocks.sunnyQuartz
		);
		getOrCreateBuilder(ModTags.Blocks.BLOCKS_TERRASTEEL).add(ModBlocks.terrasteelBlock);
		getOrCreateBuilder(Tags.Blocks.STORAGE_BLOCKS).addTag(ModTags.Blocks.BLOCKS_ELEMENTIUM);
		getOrCreateBuilder(Tags.Blocks.STORAGE_BLOCKS).addTag(ModTags.Blocks.BLOCKS_MANASTEEL);
		getOrCreateBuilder(Tags.Blocks.STORAGE_BLOCKS).addTag(ModTags.Blocks.BLOCKS_QUARTZ);
		getOrCreateBuilder(Tags.Blocks.STORAGE_BLOCKS).addTag(ModTags.Blocks.BLOCKS_TERRASTEEL);

		getOrCreateBuilder(ModTags.Blocks.LIVINGROCK).add(ModBlocks.livingrock);
		getOrCreateBuilder(ModTags.Blocks.LIVINGWOOD).add(ModBlocks.livingwood);

		getOrCreateBuilder(ModTags.Blocks.CORPOREA_SPARK_OVERRIDE).add(
				ModBlocks.corporeaBlock, ModBlocks.corporeaBrick, ModBlocks.corporeaBrickSlab, ModBlocks.corporeaBrickStairs,
				ModBlocks.corporeaBrickWall, ModBlocks.corporeaCrystalCube, ModBlocks.corporeaFunnel, ModBlocks.corporeaIndex,
				ModBlocks.corporeaInterceptor, ModBlocks.corporeaSlab, ModBlocks.corporeaStairs);

		getOrCreateBuilder(ModTags.Blocks.TERRAFORMABLE).addTag(Tags.Blocks.STONE).addTag(Tags.Blocks.DIRT).addTag(BlockTags.SAND)
				.add(Blocks.GRASS_BLOCK, Blocks.GRAVEL, Blocks.SNOW);
		getOrCreateBuilder(ModTags.Blocks.GAIA_BREAK_BLACKLIST).add(Blocks.BEACON, ModBlocks.manaPylon, ModBlocks.naturaPylon, ModBlocks.gaiaPylon);
		getOrCreateBuilder(ModTags.Blocks.MAGNET_RING_BLACKLIST).add(ModBlocks.manaPool, ModBlocks.creativePool, ModBlocks.dilutedPool,
				ModBlocks.fabulousPool, ModBlocks.terraPlate, ModBlocks.runeAltar);

		getOrCreateBuilder(ModTags.Blocks.TERRA_PLATE_BASE).add(ModBlocks.livingrock, ModBlocks.shimmerrock);

		getOrCreateBuilder(BlockTags.BAMBOO_PLANTABLE_ON).add(ModBlocks.dryGrass, ModBlocks.goldenGrass, ModBlocks.vividGrass,
				ModBlocks.scorchedGrass, ModBlocks.infusedGrass, ModBlocks.mutatedGrass);
		getOrCreateBuilder(BlockTags.CLIMBABLE).add(ModBlocks.solidVines);

		for (DyeColor color : DyeColor.values()) {
			this.getOrCreateBuilder(ModTags.Blocks.MUSHROOMS).add(ModBlocks.getMushroom(color));
		}

		getOrCreateBuilder(BlockTags.makeWrapperTag("buzzier_bees:flower_blacklist"))
				.addTag(ModTags.Blocks.MYSTICAL_FLOWERS)
				.addTag(ModTags.Blocks.SPECIAL_FLOWERS);
	}

	@Nonnull
	private Block[] getModBlocks(Predicate<Block> predicate) {
		return registry.stream().filter(BOTANIA_BLOCK)
				.filter(predicate)
				.sorted(Comparator.comparing(Registry.BLOCK::getKey))
				.toArray(Block[]::new);
	}

	@Nonnull
	@Override
	public String getName() {
		return "Botania block tags";
	}
}
