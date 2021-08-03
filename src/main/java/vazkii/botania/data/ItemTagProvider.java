/*
 * This class is distributed as part of the Botania Mod.
 * Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 *
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 */
package vazkii.botania.data;

import net.fabricmc.fabric.api.tool.attribute.v1.FabricToolTags;
import net.minecraft.core.Registry;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.tags.ItemTagsProvider;
import net.minecraft.data.tags.TagsProvider;
import net.minecraft.tags.BlockTags;
import net.minecraft.tags.ItemTags;
import net.minecraft.tags.Tag;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;

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
	protected void addTags() {
		this.copy(BlockTags.RAILS, ItemTags.RAILS);
		this.copy(BlockTags.SLABS, ItemTags.SLABS);
		this.copy(BlockTags.STAIRS, ItemTags.STAIRS);
		this.copy(BlockTags.WALLS, ItemTags.WALLS);
		this.copy(BlockTags.FENCES, ItemTags.FENCES);

		this.tag((Tag.Named<Item>) FabricToolTags.SHEARS).add(ModItems.elementiumShears, ModItems.manasteelShears);

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
		this.tag(ModTags.Items.CONTRIBUTOR_HEADFLOWERS)
				.addTag(ModTags.Items.SPECIAL_FLOWERS)
				.add(ModBlocks.motifDaybloom.asItem(), ModBlocks.motifNightshade.asItem());

		this.tag(ItemTags.TALL_FLOWERS).addTag(ModTags.Items.DOUBLE_MYSTICAL_FLOWERS);
		this.tag(ItemTags.SMALL_FLOWERS).addTag(ModTags.Items.MYSTICAL_FLOWERS).addTag(ModTags.Items.SPECIAL_FLOWERS);

		this.tag(ModTags.Items.BURST_VIEWERS).add(ModItems.monocle);
		this.tag(ModTags.Items.TERRA_PICK_BLACKLIST).add(ModItems.auraRing, ModItems.auraRingGreater, ModItems.terrasteelHelm, ModItems.spark);
		TagsProvider.TagAppender<Item> builder = this.tag(ModTags.Items.LENS);
		Registry.ITEM.stream().filter(i -> i instanceof ItemLens && Registry.ITEM.getKey(i).getNamespace().equals(LibMisc.MOD_ID))
				.sorted(Comparator.comparing(Registry.ITEM::getKey))
				.forEach(builder::add);

		this.tag(ItemTags.PIGLIN_LOVED).add(ModBlocks.alchemyCatalyst.asItem(), ModItems.cacophonium, ModItems.divaCharm,
				ModBlocks.hourglass.asItem(), ModBlocks.manaPylon.asItem(), ModItems.monocle);
		this.tag(ItemTags.MUSIC_DISCS).add(ModItems.recordGaia1, ModItems.recordGaia2);

		this.tag(ModTags.Items.DUSTS_MANA).add(ModItems.manaPowder);

		this.tag(ModTags.Items.GEMS_DRAGONSTONE).add(ModItems.dragonstone);
		this.tag(ModTags.Items.GEMS_MANA_DIAMOND).add(ModItems.manaDiamond);

		this.tag(ModTags.Items.INGOTS_ELEMENTIUM).add(ModItems.elementium);
		this.tag(ModTags.Items.INGOTS_MANASTEEL).add(ModItems.manaSteel);
		this.tag(ModTags.Items.INGOTS_TERRASTEEL).add(ModItems.terrasteel);

		this.tag(ModTags.Items.NUGGETS_ELEMENTIUM).add(ModItems.elementiumNugget);
		this.tag(ModTags.Items.NUGGETS_MANASTEEL).add(ModItems.manasteelNugget);
		this.tag(ModTags.Items.NUGGETS_TERRASTEEL).add(ModItems.terrasteelNugget);

		this.copy(ModTags.Blocks.BLOCKS_ELEMENTIUM, ModTags.Items.BLOCKS_ELEMENTIUM);
		this.copy(ModTags.Blocks.BLOCKS_MANASTEEL, ModTags.Items.BLOCKS_MANASTEEL);
		this.copy(ModTags.Blocks.BLOCKS_QUARTZ, ModTags.Items.BLOCKS_QUARTZ);
		this.copy(ModTags.Blocks.BLOCKS_TERRASTEEL, ModTags.Items.BLOCKS_TERRASTEEL);
		// todo 1.16-fabric this.copy(Tags.Blocks.STORAGE_BLOCKS, Tags.Items.STORAGE_BLOCKS);
		this.copy(ModTags.Blocks.LIVINGWOOD, ModTags.Items.LIVINGWOOD);
		this.copy(ModTags.Blocks.LIVINGROCK, ModTags.Items.LIVINGROCK);

		this.tag(ModTags.Items.DISPOSABLE).add(Items.DIRT, Items.SAND, Items.GRAVEL, Items.COBBLESTONE, Items.NETHERRACK);
		this.tag(ModTags.Items.SEMI_DISPOSABLE).add(Items.ANDESITE, Items.DIORITE, Items.GRANITE);
		/* todo 1.16-fabric
			.addOptional(new Identifier("quark", "basalt"))
			.addOptional(new Identifier("quark", "jasper"))
			.addOptional(new Identifier("quark", "limestone"))
			.addOptional(new Identifier("quark", "marble"))
			.addOptional(new Identifier("quark", "slate"));
		
		*/

		List<Tag.Named<Item>> runes = Arrays.asList(
				ModTags.Items.RUNES_WATER, ModTags.Items.RUNES_FIRE, ModTags.Items.RUNES_EARTH, ModTags.Items.RUNES_AIR,
				ModTags.Items.RUNES_SPRING, ModTags.Items.RUNES_SUMMER, ModTags.Items.RUNES_AUTUMN, ModTags.Items.RUNES_WINTER,
				ModTags.Items.RUNES_MANA, ModTags.Items.RUNES_LUST, ModTags.Items.RUNES_GLUTTONY, ModTags.Items.RUNES_GREED,
				ModTags.Items.RUNES_SLOTH, ModTags.Items.RUNES_WRATH, ModTags.Items.RUNES_ENVY, ModTags.Items.RUNES_PRIDE
		);
		TagAppender<Item> allRunes = this.tag(ModTags.Items.RUNES);
		for (Tag.Named<Item> item : runes) {
			this.tag(item).add(registry.get(
					prefix("rune_" + item.getName().getPath().split("/")[1])));
			allRunes.addTag(item);
		}

		TagAppender<Item> allPetals = this.tag(ModTags.Items.PETALS);
		for (DyeColor color : DyeColor.values()) {
			Tag.Named<Item> petalTag = ModTags.Items.getPetalTag(color);
			this.tag(petalTag).add(ModItems.getPetal(color), ModBlocks.getMushroom(color).asItem());
			this.tag(ModTags.Items.MUSHROOMS).add(ModBlocks.getMushroom(color).asItem());
			allPetals.addTag(petalTag);
		}

		this.tag(ModTags.Items.LOONIUM_BLACKLIST).add(ModItems.lexicon, ModItems.overgrowthSeed,
				ModItems.blackLotus, ModItems.blackerLotus).addTag(ItemTags.MUSIC_DISCS);
		this.tag(ModTags.Items.MAGNET_RING_BLACKLIST);
		this.tag(ModTags.Items.RODS).add(
				ModItems.dirtRod,
				ModItems.skyDirtRod,
				ModItems.terraformRod,
				ModItems.cobbleRod,
				ModItems.waterRod,
				ModItems.tornadoRod,
				ModItems.fireRod,
				ModItems.diviningRod,
				ModItems.smeltRod,
				ModItems.exchangeRod,
				ModItems.rainbowRod,
				ModItems.gravityRod,
				ModItems.missileRod);
	}

	@Nonnull
	@Override
	public String getName() {
		return "Botania item tags";
	}
}
