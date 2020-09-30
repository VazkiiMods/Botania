/*
 * This class is distributed as part of the Botania Mod.
 * Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 *
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 */
package vazkii.botania.data;

import net.minecraft.data.DataGenerator;
import net.minecraft.data.ItemTagsProvider;
import net.minecraft.item.Item;
import net.minecraft.tags.BlockTags;
import net.minecraft.tags.ItemTags;
import net.minecraft.util.registry.Registry;
import net.minecraftforge.common.Tags;

import vazkii.botania.common.block.ModBlocks;
import vazkii.botania.common.item.ModItems;
import vazkii.botania.common.item.lens.ItemLens;
import vazkii.botania.common.lib.LibMisc;
import vazkii.botania.common.lib.ModTags;

import javax.annotation.Nonnull;

import java.util.Comparator;

public class ItemTagProvider extends ItemTagsProvider {
	public ItemTagProvider(DataGenerator generatorIn, BlockTagProvider blockTagProvider) {
		super(generatorIn, blockTagProvider);
	}

	@Override
	protected void registerTags() {
		this.copy(BlockTags.RAILS, ItemTags.RAILS);
		this.copy(BlockTags.SLABS, ItemTags.SLABS);
		this.copy(BlockTags.STAIRS, ItemTags.STAIRS);
		this.copy(BlockTags.WALLS, ItemTags.WALLS);
		this.copy(BlockTags.FENCES, ItemTags.FENCES);

		this.getOrCreateBuilder(Tags.Items.SHEARS).add(ModItems.elementiumShears, ModItems.manasteelShears);

		this.copy(ModTags.Blocks.MUNDANE_FLOATING_FLOWERS, ModTags.Items.MUNDANE_FLOATING_FLOWERS);
		this.copy(ModTags.Blocks.SPECIAL_FLOATING_FLOWERS, ModTags.Items.SPECIAL_FLOATING_FLOWERS);
		this.copy(ModTags.Blocks.FLOATING_FLOWERS, ModTags.Items.FLOATING_FLOWERS);
		this.copy(ModTags.Blocks.DOUBLE_MYSTICAL_FLOWERS, ModTags.Items.DOUBLE_MYSTICAL_FLOWERS);
		this.copy(ModTags.Blocks.MYSTICAL_FLOWERS, ModTags.Items.MYSTICAL_FLOWERS);

		this.copy(ModTags.Blocks.MISC_SPECIAL_FLOWERS, ModTags.Items.MISC_SPECIAL_FLOWERS);
		this.copy(ModTags.Blocks.GENERATING_SPECIAL_FLOWERS, ModTags.Items.GENERATING_SPECIAL_FLOWERS);
		this.copy(ModTags.Blocks.FUNCTIONAL_SPECIAL_FLOWERS, ModTags.Items.FUNCTIONAL_SPECIAL_FLOWERS);
		this.copy(ModTags.Blocks.SPECIAL_FLOWERS, ModTags.Items.SPECIAL_FLOWERS);
		this.copy(ModTags.Blocks.MINI_FLOWERS, ModTags.Items.MINI_FLOWERS);
		this.getOrCreateBuilder(ModTags.Items.CONTRIBUTOR_HEADFLOWERS)
				.addTag(ModTags.Items.SPECIAL_FLOWERS)
				.add(ModBlocks.motifDaybloom.asItem(), ModBlocks.motifNightshade.asItem());

		this.getOrCreateBuilder(ItemTags.TALL_FLOWERS).addTag(ModTags.Items.DOUBLE_MYSTICAL_FLOWERS);
		this.getOrCreateBuilder(ItemTags.SMALL_FLOWERS).addTag(ModTags.Items.MYSTICAL_FLOWERS).addTag(ModTags.Items.SPECIAL_FLOWERS);

		this.getOrCreateBuilder(ModTags.Items.BURST_VIEWERS).addItemEntry(ModItems.monocle);
		this.getOrCreateBuilder(ModTags.Items.LENS).add(
				Registry.ITEM.stream().filter(i -> i instanceof ItemLens && Registry.ITEM.getKey(i).getNamespace().equals(LibMisc.MOD_ID))
						.sorted(Comparator.comparing(Registry.ITEM::getKey))
						.toArray(Item[]::new)
		);

		this.getOrCreateBuilder(ItemTags.PIGLIN_LOVED).add(ModBlocks.alchemyCatalyst.asItem(), ModItems.cacophonium, ModItems.divaCharm,
				ModBlocks.hourglass.asItem(), ModBlocks.manaPylon.asItem(), ModItems.monocle);
	}

	@Nonnull
	@Override
	public String getName() {
		return "Botania item tags";
	}
}
