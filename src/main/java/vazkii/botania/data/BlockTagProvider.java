/*
 * This class is distributed as part of the Botania Mod.
 * Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 *
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 */
package vazkii.botania.data;

import com.mojang.datafixers.util.Pair;

import net.minecraft.block.*;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.server.BlockTagsProvider;
import net.minecraft.tag.BlockTags;
import net.minecraft.util.DyeColor;
import net.minecraft.util.registry.Registry;

import vazkii.botania.common.block.BlockFloatingSpecialFlower;
import vazkii.botania.common.block.ModBlocks;
import vazkii.botania.common.block.ModSubtiles;
import vazkii.botania.common.lib.LibMisc;
import vazkii.botania.common.lib.ModTags;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import java.nio.file.Path;
import java.util.Arrays;
import java.util.Comparator;
import java.util.function.Predicate;

import static vazkii.botania.common.block.ModSubtiles.*;

public class BlockTagProvider extends BlockTagsProvider {
	public BlockTagProvider(DataGenerator generator) {
		super(generator);
	}

	@Override
	protected void configure() {
		Predicate<Block> botania = b -> LibMisc.MOD_ID.equals(Registry.BLOCK.getId(b).getNamespace());

		getOrCreateTagBuilder(BlockTags.RAILS).add(ModBlocks.ghostRail);

		getOrCreateTagBuilder(BlockTags.SLABS).add(registry.stream().filter(botania)
				.filter(b -> b instanceof SlabBlock)
				.sorted(Comparator.comparing(Registry.BLOCK::getId))
				.toArray(Block[]::new));

		getOrCreateTagBuilder(BlockTags.STAIRS).add(registry.stream().filter(botania)
				.filter(b -> b instanceof StairsBlock)
				.sorted(Comparator.comparing(Registry.BLOCK::getId))
				.toArray(Block[]::new));

		getOrCreateTagBuilder(BlockTags.WALLS).add(registry.stream().filter(botania)
				.filter(b -> b instanceof WallBlock)
				.sorted(Comparator.comparing(Registry.BLOCK::getId))
				.toArray(Block[]::new));

		getOrCreateTagBuilder(BlockTags.FENCES).add(registry.stream().filter(botania)
				.filter(b -> b instanceof FenceBlock)
				.sorted(Comparator.comparing(Registry.BLOCK::getId))
				.toArray(Block[]::new));

		getOrCreateTagBuilder(ModTags.Blocks.MUNDANE_FLOATING_FLOWERS).add(
				Arrays.stream(DyeColor.values())
						.map(ModBlocks::getFloatingFlower)
						.sorted(Comparator.comparing(Registry.BLOCK::getId))
						.toArray(Block[]::new)
		);

		getOrCreateTagBuilder(ModTags.Blocks.SPECIAL_FLOATING_FLOWERS).add(registry.stream().filter(botania)
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

		getOrCreateTagBuilder(ModTags.Blocks.MISC_SPECIAL_FLOWERS).add(manastar, pureDaisy);
		getOrCreateTagBuilder(ModTags.Blocks.GENERATING_SPECIAL_FLOWERS).add(
				dandelifeon, endoflame, entropinnyum,
				gourmaryllis, hydroangeas, kekimurus,
				munchdew, narslimmus, rafflowsia, rosaArcana,
				shulkMeNot, spectrolus, thermalily
		);
		getOrCreateTagBuilder(ModTags.Blocks.FUNCTIONAL_SPECIAL_FLOWERS).add(
				agricarnation, agricarnationChibi, bellethorn, bellethornChibi,
				bergamute, bubbell, bubbellChibi, clayconia, clayconiaChibi,
				daffomill, dreadthorn, exoflame, fallenKanade, heiseiDream,
				hopperhock, hopperhockChibi, hyacidus, jadedAmaranthus,
				jiyuulia, loonium, marimorphosis, marimorphosisChibi,
				medumone, orechid, orechidIgnem, pollidisiac, rannuncarpus, rannuncarpusChibi,
				solegnolia, solegnoliaChibi, spectranthemum, tangleberrie, tigerseye, vinculotus
		);
		getOrCreateTagBuilder(ModTags.Blocks.SPECIAL_FLOWERS).addTag(ModTags.Blocks.MISC_SPECIAL_FLOWERS)
				.addTag(ModTags.Blocks.GENERATING_SPECIAL_FLOWERS)
				.addTag(ModTags.Blocks.FUNCTIONAL_SPECIAL_FLOWERS);

		getOrCreateTagBuilder(BlockTags.TALL_FLOWERS).addTag(ModTags.Blocks.DOUBLE_MYSTICAL_FLOWERS);
		getOrCreateTagBuilder(BlockTags.SMALL_FLOWERS).addTag(ModTags.Blocks.MYSTICAL_FLOWERS).addTag(ModTags.Blocks.SPECIAL_FLOWERS);

		getOrCreateTagBuilder(BlockTags.IMPERMEABLE).add(ModBlocks.elfGlass, ModBlocks.manaGlass, ModBlocks.bifrost, ModBlocks.bifrostPerm);
		getOrCreateTagBuilder(BlockTags.BEACON_BASE_BLOCKS).add(ModBlocks.manasteelBlock, ModBlocks.terrasteelBlock, ModBlocks.elementiumBlock,
				ModBlocks.manaDiamondBlock, ModBlocks.dragonstoneBlock);
	}

	@Nonnull
	@Override
	public String getName() {
		return "Botania block tags";
	}
}
