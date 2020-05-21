/*
 * This class is distributed as part of the Botania Mod.
 * Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 *
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 */
package vazkii.botania.data;

import net.minecraft.block.Block;
import net.minecraft.block.FenceBlock;
import net.minecraft.block.SlabBlock;
import net.minecraft.block.StairsBlock;
import net.minecraft.block.WallBlock;
import net.minecraft.data.BlockTagsProvider;
import net.minecraft.data.DataGenerator;
import net.minecraft.item.DyeColor;
import net.minecraft.tags.BlockTags;

import vazkii.botania.common.block.BlockFloatingSpecialFlower;
import vazkii.botania.common.block.ModBlocks;
import vazkii.botania.common.lib.LibMisc;
import vazkii.botania.common.lib.ModTags;

import javax.annotation.Nonnull;

import java.util.Arrays;
import java.util.Comparator;
import java.util.function.Predicate;

public class BlockTagProvider extends BlockTagsProvider {
	public BlockTagProvider(DataGenerator generator) {
		super(generator);
	}

	@Override
	protected void registerTags() {
		Predicate<Block> botania = b -> LibMisc.MOD_ID.equals(b.getRegistryName().getNamespace());

		getBuilder(BlockTags.RAILS).add(ModBlocks.ghostRail);

		getBuilder(BlockTags.SLABS).add(registry.stream().filter(botania)
				.filter(b -> b instanceof SlabBlock)
				.sorted(Comparator.comparing(Block::getRegistryName))
				.toArray(Block[]::new));

		getBuilder(BlockTags.STAIRS).add(registry.stream().filter(botania)
				.filter(b -> b instanceof StairsBlock)
				.sorted(Comparator.comparing(Block::getRegistryName))
				.toArray(Block[]::new));

		getBuilder(BlockTags.WALLS).add(registry.stream().filter(botania)
				.filter(b -> b instanceof WallBlock)
				.sorted(Comparator.comparing(Block::getRegistryName))
				.toArray(Block[]::new));

		getBuilder(BlockTags.FENCES).add(registry.stream().filter(botania)
				.filter(b -> b instanceof FenceBlock)
				.sorted(Comparator.comparing(Block::getRegistryName))
				.toArray(Block[]::new));

		getBuilder(ModTags.Blocks.MUNDANE_FLOATING_FLOWERS).add(
				Arrays.stream(DyeColor.values())
						.map(ModBlocks::getFloatingFlower)
						.sorted(Comparator.comparing(Block::getRegistryName))
						.toArray(Block[]::new)
		);

		getBuilder(ModTags.Blocks.SPECIAL_FLOATING_FLOWERS).add(registry.stream().filter(botania)
				.filter(b -> b instanceof BlockFloatingSpecialFlower)
				.sorted(Comparator.comparing(Block::getRegistryName))
				.toArray(Block[]::new)
		);

		getBuilder(ModTags.Blocks.FLOATING_FLOWERS).add(ModTags.Blocks.MUNDANE_FLOATING_FLOWERS, ModTags.Blocks.SPECIAL_FLOATING_FLOWERS);

		getBuilder(ModTags.Blocks.MYSTICAL_FLOWERS).add(
				Arrays.stream(DyeColor.values())
						.map(ModBlocks::getFlower)
						.sorted(Comparator.comparing(Block::getRegistryName))
						.toArray(Block[]::new)
		);

		getBuilder(ModTags.Blocks.DOUBLE_MYSTICAL_FLOWERS).add(
				Arrays.stream(DyeColor.values())
						.map(ModBlocks::getDoubleFlower)
						.sorted(Comparator.comparing(Block::getRegistryName))
						.toArray(Block[]::new)
		);

		getBuilder(BlockTags.TALL_FLOWERS).add(ModTags.Blocks.DOUBLE_MYSTICAL_FLOWERS);
		getBuilder(BlockTags.SMALL_FLOWERS).add(ModTags.Blocks.MYSTICAL_FLOWERS);
		getBuilder(BlockTags.FLOWERS).add(ModTags.Blocks.SPECIAL_FLOWERS);

		getBuilder(BlockTags.IMPERMEABLE).add(ModBlocks.elfGlass, ModBlocks.manaGlass, ModBlocks.bifrost, ModBlocks.bifrostPerm);
	}

	@Nonnull
	@Override
	public String getName() {
		return "Botania block tags";
	}
}
