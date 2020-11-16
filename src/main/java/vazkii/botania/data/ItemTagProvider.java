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
import net.minecraft.item.DyeColor;
import net.minecraft.item.Item;
import net.minecraft.item.Items;
import net.minecraft.tags.BlockTags;
import net.minecraft.tags.ITag;
import net.minecraft.tags.ItemTags;
import net.minecraft.util.registry.Registry;
import net.minecraftforge.common.Tags;

import vazkii.botania.common.block.ModBlocks;
import vazkii.botania.common.item.ModItems;
import vazkii.botania.common.item.lens.ItemLens;
import vazkii.botania.common.lib.LibMisc;
import vazkii.botania.common.lib.ModTags;

import javax.annotation.Nonnull;

import java.util.Arrays;
import java.util.Comparator;
import java.util.List;

import static vazkii.botania.common.lib.ResourceLocationHelper.prefix;

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
		this.getOrCreateBuilder(ModTags.Items.TERRA_PICK_BLACKLIST).add(ModItems.auraRing, ModItems.auraRingGreater, ModItems.terrasteelHelm);
		this.getOrCreateBuilder(ModTags.Items.LENS).add(
				Registry.ITEM.stream().filter(i -> i instanceof ItemLens && Registry.ITEM.getKey(i).getNamespace().equals(LibMisc.MOD_ID))
						.sorted(Comparator.comparing(Registry.ITEM::getKey))
						.toArray(Item[]::new)
		);

		this.getOrCreateBuilder(ItemTags.PIGLIN_LOVED).add(ModBlocks.alchemyCatalyst.asItem(), ModItems.cacophonium, ModItems.divaCharm,
				ModBlocks.hourglass.asItem(), ModBlocks.manaPylon.asItem(), ModItems.monocle);
		this.getOrCreateBuilder(ItemTags.MUSIC_DISCS).add(ModItems.recordGaia1, ModItems.recordGaia2);

		this.getOrCreateBuilder(ModTags.Items.DUSTS_MANA).add(ModItems.manaPowder);
		this.getOrCreateBuilder(Tags.Items.DUSTS).addTag(ModTags.Items.DUSTS_MANA);

		this.getOrCreateBuilder(ModTags.Items.GEMS_DRAGONSTONE).add(ModItems.dragonstone);
		this.getOrCreateBuilder(ModTags.Items.GEMS_MANA_DIAMOND).add(ModItems.manaDiamond);
		this.getOrCreateBuilder(Tags.Items.GEMS).addTag(ModTags.Items.GEMS_DRAGONSTONE);
		this.getOrCreateBuilder(Tags.Items.GEMS).addTag(ModTags.Items.GEMS_MANA_DIAMOND);

		this.getOrCreateBuilder(ModTags.Items.INGOTS_ELEMENTIUM).add(ModItems.elementium);
		this.getOrCreateBuilder(ModTags.Items.INGOTS_MANASTEEL).add(ModItems.manaSteel);
		this.getOrCreateBuilder(ModTags.Items.INGOTS_TERRASTEEL).add(ModItems.terrasteel);
		this.getOrCreateBuilder(Tags.Items.INGOTS).addTag(ModTags.Items.INGOTS_ELEMENTIUM);
		this.getOrCreateBuilder(Tags.Items.INGOTS).addTag(ModTags.Items.INGOTS_MANASTEEL);
		this.getOrCreateBuilder(Tags.Items.INGOTS).addTag(ModTags.Items.INGOTS_TERRASTEEL);

		this.getOrCreateBuilder(ModTags.Items.NUGGETS_ELEMENTIUM).add(ModItems.elementiumNugget);
		this.getOrCreateBuilder(ModTags.Items.NUGGETS_MANASTEEL).add(ModItems.manasteelNugget);
		this.getOrCreateBuilder(ModTags.Items.NUGGETS_TERRASTEEL).add(ModItems.terrasteelNugget);
		this.getOrCreateBuilder(Tags.Items.NUGGETS).addTag(ModTags.Items.NUGGETS_ELEMENTIUM);
		this.getOrCreateBuilder(Tags.Items.NUGGETS).addTag(ModTags.Items.NUGGETS_MANASTEEL);
		this.getOrCreateBuilder(Tags.Items.NUGGETS).addTag(ModTags.Items.NUGGETS_TERRASTEEL);

		this.copy(ModTags.Blocks.BLOCKS_ELEMENTIUM, ModTags.Items.BLOCKS_ELEMENTIUM);
		this.copy(ModTags.Blocks.BLOCKS_MANASTEEL, ModTags.Items.BLOCKS_MANASTEEL);
		this.copy(ModTags.Blocks.BLOCKS_QUARTZ, ModTags.Items.BLOCKS_QUARTZ);
		this.copy(ModTags.Blocks.BLOCKS_TERRASTEEL, ModTags.Items.BLOCKS_TERRASTEEL);
		this.copy(Tags.Blocks.STORAGE_BLOCKS, Tags.Items.STORAGE_BLOCKS);
		this.copy(ModTags.Blocks.LIVINGWOOD, ModTags.Items.LIVINGWOOD);
		this.copy(ModTags.Blocks.LIVINGROCK, ModTags.Items.LIVINGROCK);

		this.getOrCreateBuilder(ModTags.Items.DISPOSABLE).add(Items.DIRT, Items.SAND, Items.GRAVEL, Items.COBBLESTONE, Items.NETHERRACK);
		this.getOrCreateBuilder(ModTags.Items.SEMI_DISPOSABLE).add(Items.ANDESITE, Items.DIORITE, Items.GRANITE);

		List<ITag.INamedTag<Item>> runes = Arrays.asList(
				ModTags.Items.RUNES_WATER, ModTags.Items.RUNES_FIRE, ModTags.Items.RUNES_EARTH, ModTags.Items.RUNES_AIR,
				ModTags.Items.RUNES_SPRING, ModTags.Items.RUNES_SUMMER, ModTags.Items.RUNES_AUTUMN, ModTags.Items.RUNES_WINTER,
				ModTags.Items.RUNES_MANA, ModTags.Items.RUNES_LUST, ModTags.Items.RUNES_GLUTTONY, ModTags.Items.RUNES_GREED,
				ModTags.Items.RUNES_SLOTH, ModTags.Items.RUNES_WRATH, ModTags.Items.RUNES_ENVY, ModTags.Items.RUNES_PRIDE
		);
		Builder<Item> allRunes = this.getOrCreateBuilder(ModTags.Items.RUNES);
		for (ITag.INamedTag<Item> item : runes) {
			this.getOrCreateBuilder(item).add(registry.getOrDefault(
					prefix("rune_" + item.getName().getPath().split("/")[1])));
			allRunes.addTag(item);
		}

		Builder<Item> allPetals = this.getOrCreateBuilder(ModTags.Items.PETALS);
		for (DyeColor color : DyeColor.values()) {
			ITag.INamedTag<Item> petalTag = ModTags.Items.getPetalTag(color);
			this.getOrCreateBuilder(petalTag).add(ModItems.getPetal(color), ModBlocks.getMushroom(color).asItem());
			allPetals.addTag(petalTag);
		}

		this.getOrCreateBuilder(ModTags.Items.LOONIUM_BLACKLIST).add(ModItems.lexicon, ModItems.overgrowthSeed,
				ModItems.blackLotus, ModItems.blackerLotus).addTag(ItemTags.MUSIC_DISCS);
		this.getOrCreateBuilder(ModTags.Items.MAGNET_RING_BLACKLIST);
	}

	@Nonnull
	@Override
	public String getName() {
		return "Botania item tags";
	}
}
