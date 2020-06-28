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
		Predicate<Block> botania = b -> LibMisc.MOD_ID.equals(Registry.BLOCK.getKey(b).getNamespace());

		func_240522_a_(BlockTags.RAILS).func_240532_a_(ModBlocks.ghostRail);

		func_240522_a_(BlockTags.SLABS).func_240534_a_(registry.stream().filter(botania)
				.filter(b -> b instanceof SlabBlock)
				.sorted(Comparator.comparing(Registry.BLOCK::getKey))
				.toArray(Block[]::new));

		func_240522_a_(BlockTags.STAIRS).func_240534_a_(registry.stream().filter(botania)
				.filter(b -> b instanceof StairsBlock)
				.sorted(Comparator.comparing(Registry.BLOCK::getKey))
				.toArray(Block[]::new));

		func_240522_a_(BlockTags.WALLS).func_240534_a_(registry.stream().filter(botania)
				.filter(b -> b instanceof WallBlock)
				.sorted(Comparator.comparing(Registry.BLOCK::getKey))
				.toArray(Block[]::new));

		func_240522_a_(BlockTags.FENCES).func_240534_a_(registry.stream().filter(botania)
				.filter(b -> b instanceof FenceBlock)
				.sorted(Comparator.comparing(Registry.BLOCK::getKey))
				.toArray(Block[]::new));

		func_240522_a_(ModTags.Blocks.MUNDANE_FLOATING_FLOWERS).func_240534_a_(
				Arrays.stream(DyeColor.values())
						.map(ModBlocks::getFloatingFlower)
						.sorted(Comparator.comparing(Registry.BLOCK::getKey))
						.toArray(Block[]::new)
		);

		func_240522_a_(ModTags.Blocks.SPECIAL_FLOATING_FLOWERS).func_240534_a_(registry.stream().filter(botania)
				.filter(b -> b instanceof BlockFloatingSpecialFlower)
				.sorted(Comparator.comparing(Registry.BLOCK::getKey))
				.toArray(Block[]::new)
		);

		func_240522_a_(ModTags.Blocks.FLOATING_FLOWERS).func_240531_a_(ModTags.Blocks.MUNDANE_FLOATING_FLOWERS)
				.func_240531_a_(ModTags.Blocks.SPECIAL_FLOATING_FLOWERS);

		func_240522_a_(ModTags.Blocks.MYSTICAL_FLOWERS).func_240534_a_(
				Arrays.stream(DyeColor.values())
						.map(ModBlocks::getFlower)
						.sorted(Comparator.comparing(Registry.BLOCK::getKey))
						.toArray(Block[]::new)
		);

		func_240522_a_(ModTags.Blocks.DOUBLE_MYSTICAL_FLOWERS).func_240534_a_(
				Arrays.stream(DyeColor.values())
						.map(ModBlocks::getDoubleFlower)
						.sorted(Comparator.comparing(Registry.BLOCK::getKey))
						.toArray(Block[]::new)
		);

		func_240522_a_(BlockTags.TALL_FLOWERS).func_240531_a_(ModTags.Blocks.DOUBLE_MYSTICAL_FLOWERS);
		func_240522_a_(BlockTags.SMALL_FLOWERS).func_240531_a_(ModTags.Blocks.MYSTICAL_FLOWERS);
		func_240522_a_(BlockTags.FLOWERS).func_240531_a_(ModTags.Blocks.SPECIAL_FLOWERS);

		func_240522_a_(BlockTags.IMPERMEABLE).func_240534_a_(ModBlocks.elfGlass, ModBlocks.manaGlass, ModBlocks.bifrost, ModBlocks.bifrostPerm);
	}

	@Nonnull
	@Override
	public String getName() {
		return "Botania block tags";
	}
}
