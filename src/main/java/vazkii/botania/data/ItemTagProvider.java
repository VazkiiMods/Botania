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
import net.minecraft.data.TagsProvider;
import net.minecraft.item.DyeColor;
import net.minecraft.item.Item;
import net.minecraft.tags.BlockTags;
import net.minecraft.tags.ItemTags;
import net.minecraft.util.registry.Registry;

import vazkii.botania.common.item.ModItems;
import vazkii.botania.common.lib.ModTags;

import javax.annotation.Nonnull;

import java.util.Arrays;
import java.util.Comparator;

public class ItemTagProvider extends ItemTagsProvider {
	public ItemTagProvider(DataGenerator generatorIn, BlockTagProvider blockTagProvider) {
		super(generatorIn, blockTagProvider);
	}

	@Override
	protected void registerTags() {
		this.func_240521_a_(BlockTags.RAILS, ItemTags.RAILS);
		this.func_240521_a_(BlockTags.SLABS, ItemTags.SLABS);
		this.func_240521_a_(BlockTags.STAIRS, ItemTags.STAIRS);
		this.func_240521_a_(BlockTags.WALLS, ItemTags.WALLS);
		this.func_240521_a_(BlockTags.FENCES, ItemTags.FENCES);

		this.func_240522_a_(ModTags.Items.SHEARS).func_240534_a_(ModItems.elementiumShears, ModItems.manasteelShears);

		this.func_240521_a_(ModTags.Blocks.MUNDANE_FLOATING_FLOWERS, ModTags.Items.MUNDANE_FLOATING_FLOWERS);
		this.func_240521_a_(ModTags.Blocks.SPECIAL_FLOATING_FLOWERS, ModTags.Items.SPECIAL_FLOATING_FLOWERS);
		this.func_240521_a_(ModTags.Blocks.FLOATING_FLOWERS, ModTags.Items.FLOATING_FLOWERS);
		this.func_240521_a_(ModTags.Blocks.DOUBLE_MYSTICAL_FLOWERS, ModTags.Items.DOUBLE_MYSTICAL_FLOWERS);
		this.func_240521_a_(ModTags.Blocks.MYSTICAL_FLOWERS, ModTags.Items.MYSTICAL_FLOWERS);

		this.func_240521_a_(ModTags.Blocks.MISC_SPECIAL_FLOWERS, ModTags.Items.MISC_SPECIAL_FLOWERS);
		this.func_240521_a_(ModTags.Blocks.GENERATING_SPECIAL_FLOWERS, ModTags.Items.GENERATING_SPECIAL_FLOWERS);
		this.func_240521_a_(ModTags.Blocks.FUNCTIONAL_SPECIAL_FLOWERS, ModTags.Items.FUNCTIONAL_SPECIAL_FLOWERS);
		this.func_240521_a_(ModTags.Blocks.SPECIAL_FLOWERS, ModTags.Items.SPECIAL_FLOWERS);

		this.func_240522_a_(ItemTags.TALL_FLOWERS).func_240531_a_(ModTags.Items.DOUBLE_MYSTICAL_FLOWERS);
		this.func_240522_a_(ItemTags.SMALL_FLOWERS).func_240531_a_(ModTags.Items.MYSTICAL_FLOWERS);
		this.func_240522_a_(ItemTags.FLOWERS).func_240531_a_(ModTags.Items.SPECIAL_FLOWERS);

		this.func_240522_a_(ModTags.Items.BURST_VIEWERS).func_240532_a_(ModItems.monocle);
	}

	@Nonnull
	@Override
	public String getName() {
		return "Botania item tags";
	}
}
