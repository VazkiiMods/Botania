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
import net.minecraft.data.DataGenerator;
import net.minecraft.data.server.BlockTagsProvider;
import net.minecraft.tag.BlockTags;
import net.minecraft.util.DyeColor;
import net.minecraft.util.registry.Registry;

import vazkii.botania.common.block.*;
import vazkii.botania.common.lib.LibMisc;
import vazkii.botania.common.lib.ModTags;

import javax.annotation.Nonnull;

import java.util.Arrays;
import java.util.Comparator;
import java.util.function.Predicate;

import static vazkii.botania.common.block.ModSubtiles.*;

public class BlockTagProvider extends BlockTagsProvider {
	private static final Predicate<Block> BOTANIA_BLOCK = b -> LibMisc.MOD_ID.equals(Registry.BLOCK.getId(b).getNamespace());

	public BlockTagProvider(DataGenerator generator) {
		super(generator);
	}

	@Override
	protected void configure() {
		getOrCreateTagBuilder(BlockTags.RAILS).add(ModBlocks.ghostRail);
		getOrCreateTagBuilder(BlockTags.SLABS).add(getModBlocks(b -> b instanceof SlabBlock));
		getOrCreateTagBuilder(BlockTags.STAIRS).add(getModBlocks(b -> b instanceof StairsBlock));
		getOrCreateTagBuilder(BlockTags.WALLS).add(getModBlocks(b -> b instanceof WallBlock));
		getOrCreateTagBuilder(BlockTags.FENCES).add(getModBlocks(b -> b instanceof FenceBlock));
		getOrCreateTagBuilder(BlockTags.FENCE_GATES).add(getModBlocks(b -> b instanceof FenceGateBlock));
		getOrCreateTagBuilder(BlockTags.DRAGON_IMMUNE).add(ModBlocks.infrangiblePlatform);
		getOrCreateTagBuilder(BlockTags.WITHER_IMMUNE).add(ModBlocks.infrangiblePlatform);

		getOrCreateTagBuilder(ModTags.Blocks.MUNDANE_FLOATING_FLOWERS).add(
				Arrays.stream(DyeColor.values())
						.map(ModBlocks::getFloatingFlower)
						.sorted(Comparator.comparing(Registry.BLOCK::getId))
						.toArray(Block[]::new)
		);

		getOrCreateTagBuilder(ModTags.Blocks.SPECIAL_FLOATING_FLOWERS).add(registry.stream().filter(BOTANIA_BLOCK)
				.filter(b -> b instanceof BlockFloatingSpecialFlower)
				.sorted(Comparator.comparing(Registry.BLOCK::getId))
				.toArray(Block[]::new)
		);

		getOrCreateTagBuilder(ModTags.Blocks.FLOATING_FLOWERS).addTag(ModTags.Blocks.MUNDANE_FLOATING_FLOWERS)
				.addTag(ModTags.Blocks.SPECIAL_FLOATING_FLOWERS);

		getOrCreateTagBuilder(ModTags.Blocks.MYSTICAL_FLOWERS).add(
				Arrays.stream(DyeColor.values())
						.map(ModBlocks::getFlower)
						.sorted(Comparator.comparing(Registry.BLOCK::getId))
						.toArray(Block[]::new)
		);

		getOrCreateTagBuilder(ModTags.Blocks.DOUBLE_MYSTICAL_FLOWERS).add(
				Arrays.stream(DyeColor.values())
						.map(ModBlocks::getDoubleFlower)
						.sorted(Comparator.comparing(Registry.BLOCK::getId))
						.toArray(Block[]::new)
		);

		getOrCreateTagBuilder(ModTags.Blocks.MISC_SPECIAL_FLOWERS).add(manastar, pureDaisy, bergamute);
		getOrCreateTagBuilder(ModTags.Blocks.GENERATING_SPECIAL_FLOWERS).add(
				dandelifeon, endoflame, entropinnyum,
				gourmaryllis, hydroangeas, kekimurus,
				munchdew, narslimmus, rafflowsia, rosaArcana,
				shulkMeNot, spectrolus, thermalily
		);
		getOrCreateTagBuilder(ModTags.Blocks.FUNCTIONAL_SPECIAL_FLOWERS).add(
				agricarnation, agricarnationChibi, bellethorn, bellethornChibi,
				bubbell, bubbellChibi, clayconia, clayconiaChibi,
				daffomill, dreadthorn, exoflame, fallenKanade, heiseiDream,
				hopperhock, hopperhockChibi, hyacidus, jadedAmaranthus,
				jiyuulia, loonium, marimorphosis, marimorphosisChibi,
				medumone, orechid, orechidIgnem, pollidisiac, rannuncarpus, rannuncarpusChibi,
				solegnolia, solegnoliaChibi, spectranthemum, tangleberrie, tigerseye, vinculotus
		);
		getOrCreateTagBuilder(ModTags.Blocks.SPECIAL_FLOWERS).addTag(ModTags.Blocks.MISC_SPECIAL_FLOWERS)
				.addTag(ModTags.Blocks.GENERATING_SPECIAL_FLOWERS)
				.addTag(ModTags.Blocks.FUNCTIONAL_SPECIAL_FLOWERS);

		getOrCreateTagBuilder(ModTags.Blocks.MINI_FLOWERS).add(
				getModBlocks(b -> b instanceof BlockSpecialFlower && registry.getId(b).getPath().endsWith("_chibi"))
		);

		getOrCreateTagBuilder(BlockTags.TALL_FLOWERS).addTag(ModTags.Blocks.DOUBLE_MYSTICAL_FLOWERS);
		getOrCreateTagBuilder(BlockTags.SMALL_FLOWERS).addTag(ModTags.Blocks.MYSTICAL_FLOWERS).addTag(ModTags.Blocks.SPECIAL_FLOWERS);

		getOrCreateTagBuilder(BlockTags.IMPERMEABLE).add(ModBlocks.elfGlass, ModBlocks.manaGlass, ModBlocks.bifrost, ModBlocks.bifrostPerm);
		getOrCreateTagBuilder(BlockTags.BEACON_BASE_BLOCKS).add(ModBlocks.manasteelBlock, ModBlocks.terrasteelBlock, ModBlocks.elementiumBlock,
				ModBlocks.manaDiamondBlock, ModBlocks.dragonstoneBlock);

		// todo 1.16-fabric getOrCreateTagBuilder(Tags.Blocks.DIRT).add(getModBlocks(b -> b instanceof BlockAltGrass));
		getOrCreateTagBuilder(ModTags.Blocks.BLOCKS_ELEMENTIUM).add(ModBlocks.elementiumBlock);
		getOrCreateTagBuilder(ModTags.Blocks.BLOCKS_MANASTEEL).add(ModBlocks.manasteelBlock);
		getOrCreateTagBuilder(ModTags.Blocks.BLOCKS_QUARTZ).add(
				ModFluffBlocks.darkQuartz, ModFluffBlocks.manaQuartz, ModFluffBlocks.blazeQuartz,
				ModFluffBlocks.lavenderQuartz, ModFluffBlocks.redQuartz, ModFluffBlocks.elfQuartz, ModFluffBlocks.sunnyQuartz
		);
		getOrCreateTagBuilder(ModTags.Blocks.BLOCKS_TERRASTEEL).add(ModBlocks.terrasteelBlock);

		getOrCreateTagBuilder(ModTags.Blocks.LIVINGROCK).add(ModBlocks.livingrock);
		getOrCreateTagBuilder(ModTags.Blocks.LIVINGWOOD).add(ModBlocks.livingwood);

		getOrCreateTagBuilder(ModTags.Blocks.CORPOREA_SPARK_OVERRIDE).add(
				ModBlocks.corporeaBlock, ModBlocks.corporeaBrick, ModBlocks.corporeaBrickSlab, ModBlocks.corporeaBrickStairs,
				ModBlocks.corporeaBrickWall, ModBlocks.corporeaCrystalCube, ModBlocks.corporeaFunnel, ModBlocks.corporeaIndex,
				ModBlocks.corporeaInterceptor, ModBlocks.corporeaSlab, ModBlocks.corporeaStairs);

		getOrCreateTagBuilder(BlockTags.SAND); // We aren't calling vanilla's generation, so need to add a dummy so that using this below doesn't error out.
		getOrCreateTagBuilder(ModTags.Blocks.TERRAFORMABLE)
				.add(Blocks.ANDESITE, Blocks.DIORITE, Blocks.GRANITE, Blocks.INFESTED_STONE, Blocks.STONE, Blocks.POLISHED_ANDESITE, Blocks.POLISHED_DIORITE, Blocks.POLISHED_GRANITE)
				.add(Blocks.DIRT, Blocks.GRASS_BLOCK, Blocks.COARSE_DIRT, Blocks.PODZOL, Blocks.MYCELIUM)
				.add(Blocks.GRASS_BLOCK, Blocks.GRAVEL, Blocks.SNOW)
				.addTag(BlockTags.SAND);
		getOrCreateTagBuilder(ModTags.Blocks.GAIA_BREAK_BLACKLIST).add(Blocks.BEACON, ModBlocks.manaPylon, ModBlocks.naturaPylon, ModBlocks.gaiaPylon);
		getOrCreateTagBuilder(ModTags.Blocks.MAGNET_RING_BLACKLIST).add(ModBlocks.manaPool, ModBlocks.creativePool, ModBlocks.dilutedPool,
				ModBlocks.fabulousPool, ModBlocks.terraPlate, ModBlocks.runeAltar);

		getOrCreateTagBuilder(ModTags.Blocks.TERRA_PLATE_BASE).add(ModBlocks.livingrock, ModBlocks.shimmerrock);

		getOrCreateTagBuilder(BlockTags.BAMBOO_PLANTABLE_ON).add(ModBlocks.dryGrass, ModBlocks.goldenGrass, ModBlocks.vividGrass,
				ModBlocks.scorchedGrass, ModBlocks.infusedGrass, ModBlocks.mutatedGrass);
	}

	@Nonnull
	private Block[] getModBlocks(Predicate<Block> predicate) {
		return registry.stream().filter(BOTANIA_BLOCK)
				.filter(predicate)
				.sorted(Comparator.comparing(Registry.BLOCK::getId))
				.toArray(Block[]::new);
	}

	@Nonnull
	@Override
	public String getName() {
		return "Botania block tags";
	}
}
