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
import net.minecraftforge.common.Tags;

import vazkii.botania.common.block.BlockAltGrass;
import vazkii.botania.common.block.BlockFloatingSpecialFlower;
import vazkii.botania.common.block.BlockSpecialFlower;
import vazkii.botania.common.block.ModBlocks;
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

		getOrCreateTagBuilder(ModTags.Blocks.MINI_FLOWERS).add(
			getModBlocks(b -> b instanceof BlockSpecialFlower && registry.getId(b).getPath().endsWith("_chibi"))
		);

		getOrCreateTagBuilder(BlockTags.TALL_FLOWERS).addTag(ModTags.Blocks.DOUBLE_MYSTICAL_FLOWERS);
		getOrCreateTagBuilder(BlockTags.SMALL_FLOWERS).addTag(ModTags.Blocks.MYSTICAL_FLOWERS).addTag(ModTags.Blocks.SPECIAL_FLOWERS);

		getOrCreateTagBuilder(BlockTags.IMPERMEABLE).add(ModBlocks.elfGlass, ModBlocks.manaGlass, ModBlocks.bifrost, ModBlocks.bifrostPerm);
		getOrCreateTagBuilder(BlockTags.BEACON_BASE_BLOCKS).add(ModBlocks.manasteelBlock, ModBlocks.terrasteelBlock, ModBlocks.elementiumBlock,
				ModBlocks.manaDiamondBlock, ModBlocks.dragonstoneBlock);

		// todo 1.16-fabric getOrCreateTagBuilder(Tags.Blocks.DIRT).add(getModBlocks(b -> b instanceof BlockAltGrass));
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
