/*
 * This class is distributed as part of the Botania Mod.
 * Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 *
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 */
package vazkii.botania.data;

import net.minecraft.core.Registry;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.tags.ItemTagsProvider;
import net.minecraft.data.tags.TagsProvider;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.BlockTags;
import net.minecraft.tags.ItemTags;
import net.minecraft.tags.Tag;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;

import vazkii.botania.common.block.ModBlocks;
import vazkii.botania.common.item.lens.ItemLens;
import vazkii.botania.common.lib.LibMisc;
import vazkii.botania.common.lib.ModTags;

import javax.annotation.Nonnull;

import java.util.Comparator;

import static vazkii.botania.common.item.ModItems.*;

public class ItemTagProvider extends ItemTagsProvider {
	public ItemTagProvider(DataGenerator generatorIn, BlockTagProvider blockTagProvider) {
		super(generatorIn, blockTagProvider);
	}

	@Override
	protected void addTags() {
		this.copy(BlockTags.RAILS, ItemTags.RAILS);
		this.copy(BlockTags.SLABS, ItemTags.SLABS);
		this.copy(BlockTags.PLANKS, ItemTags.PLANKS);
		this.copy(BlockTags.WOODEN_SLABS, ItemTags.WOODEN_SLABS);
		this.copy(BlockTags.STAIRS, ItemTags.STAIRS);
		this.copy(BlockTags.WOODEN_STAIRS, ItemTags.WOODEN_STAIRS);
		this.copy(BlockTags.WALLS, ItemTags.WALLS);
		this.copy(BlockTags.FENCES, ItemTags.FENCES);
		this.copy(BlockTags.WOODEN_FENCES, ItemTags.WOODEN_FENCES);

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

		this.tag(ModTags.Items.BURST_VIEWERS).add(monocle);
		this.tag(ModTags.Items.TERRA_PICK_BLACKLIST).add(auraRing, auraRingGreater, terrasteelHelm, spark);
		TagsProvider.TagAppender<Item> builder = this.tag(ModTags.Items.LENS);
		Registry.ITEM.stream().filter(i -> i instanceof ItemLens && Registry.ITEM.getKey(i).getNamespace().equals(LibMisc.MOD_ID))
				.sorted(Comparator.comparing(Registry.ITEM::getKey))
				.forEach(builder::add);

		this.tag(ItemTags.PIGLIN_LOVED).add(ModBlocks.alchemyCatalyst.asItem(), divaCharm,
				ModBlocks.hourglass.asItem(), ModBlocks.manaPylon.asItem(), monocle);
		this.tag(ItemTags.MUSIC_DISCS).add(recordGaia1, recordGaia2);
		this.tag(ItemTags.CLUSTER_MAX_HARVESTABLES).add(manasteelPick, elementiumPick, terraPick, glassPick);

		this.tag(ModTags.Items.DUSTS_MANA).add(manaPowder);

		this.tag(ModTags.Items.GEMS_DRAGONSTONE).add(dragonstone);
		this.tag(ModTags.Items.GEMS_MANA_DIAMOND).add(manaDiamond);

		this.tag(ModTags.Items.INGOTS_ELEMENTIUM).add(elementium);
		this.tag(ModTags.Items.INGOTS_MANASTEEL).add(manaSteel);
		this.tag(ModTags.Items.INGOTS_TERRASTEEL).add(terrasteel);

		this.tag(ModTags.Items.NUGGETS_ELEMENTIUM).add(elementiumNugget);
		this.tag(ModTags.Items.NUGGETS_MANASTEEL).add(manasteelNugget);
		this.tag(ModTags.Items.NUGGETS_TERRASTEEL).add(terrasteelNugget);

		this.copy(ModTags.Blocks.BLOCKS_ELEMENTIUM, ModTags.Items.BLOCKS_ELEMENTIUM);
		this.copy(ModTags.Blocks.BLOCKS_MANASTEEL, ModTags.Items.BLOCKS_MANASTEEL);
		this.copy(ModTags.Blocks.BLOCKS_TERRASTEEL, ModTags.Items.BLOCKS_TERRASTEEL);

		this.copy(BlockTags.LOGS_THAT_BURN, ItemTags.LOGS_THAT_BURN);
		this.copy(ModTags.Blocks.LIVINGWOOD_LOGS, ModTags.Items.LIVINGWOOD_LOGS);
		this.copy(ModTags.Blocks.LIVINGWOOD_LOGS_GLIMMERING, ModTags.Items.LIVINGWOOD_LOGS_GLIMMERING);
		this.copy(ModTags.Blocks.DREAMWOOD_LOGS, ModTags.Items.DREAMWOOD_LOGS);
		this.copy(ModTags.Blocks.DREAMWOOD_LOGS_GLIMMERING, ModTags.Items.DREAMWOOD_LOGS_GLIMMERING);

		this.tag(ModTags.Items.DISPOSABLE).add(Items.DIRT, Items.SAND, Items.GRAVEL, Items.COBBLESTONE, Items.NETHERRACK);
		this.tag(ModTags.Items.SEMI_DISPOSABLE).add(Items.ANDESITE, Items.DIORITE, Items.GRANITE)
				.addOptional(new ResourceLocation("quark", "basalt"))
				.addOptional(new ResourceLocation("quark", "jasper"))
				.addOptional(new ResourceLocation("quark", "limestone"))
				.addOptional(new ResourceLocation("quark", "marble"))
				.addOptional(new ResourceLocation("quark", "slate"));

		this.tag(ModTags.Items.RUNES).add(
				runeWater, runeFire, runeEarth, runeAir,
				runeSpring, runeSummer, runeAutumn, runeWinter,
				runeMana, runeLust, runeGluttony, runeGreed,
				runeSloth, runeWrath, runeEnvy, runePride
		);

		TagAppender<Item> allPetals = this.tag(ModTags.Items.PETALS);
		for (DyeColor color : DyeColor.values()) {
			Tag.Named<Item> petalTag = ModTags.Items.getPetalTag(color);
			this.tag(petalTag).add(getPetal(color), ModBlocks.getMushroom(color).asItem());
			allPetals.addTag(petalTag);
		}

		this.tag(ModTags.Items.LOONIUM_BLACKLIST).add(lexicon, overgrowthSeed,
				blackLotus, blackerLotus).addTag(ItemTags.MUSIC_DISCS);
		this.tag(ModTags.Items.MAGNET_RING_BLACKLIST);
		this.tag(ModTags.Items.RODS).add(
				dirtRod,
				skyDirtRod,
				terraformRod,
				cobbleRod,
				waterRod,
				tornadoRod,
				fireRod,
				diviningRod,
				smeltRod,
				exchangeRod,
				rainbowRod,
				gravityRod,
				missileRod);
		this.tag(ModTags.Items.MANA_USING_ITEMS).add(
				bloodPendant,
				cobbleRod,
				crystalBow,
				dirtRod,
				divaCharm,
				diviningRod,
				elementiumAxe,
				elementiumBoots,
				elementiumChest,
				elementiumHelm,
				elementiumHoe,
				elementiumLegs,
				elementiumPick,
				elementiumShears,
				elementiumShovel,
				elementiumSword,
				enderHand,
				exchangeRod,
				fireRod,
				flightTiara,
				flugelEye,
				glassPick,
				goddessCharm,
				gravityRod,
				infiniteFruit,
				invisibilityCloak,
				kingKey,
				livingwoodBow,
				lokiRing,
				manasteelAxe,
				manasteelBoots,
				manasteelChest,
				manasteelHelm,
				manasteelHoe,
				manasteelLegs,
				manasteelPick,
				manasteelShears,
				manasteelShovel,
				manasteelSword,
				manaweaveBoots,
				manaweaveChest,
				manaweaveHelm,
				manaweaveLegs,
				miningRing,
				missileRod,
				rainbowRod,
				skyDirtRod,
				smeltRod,
				starSword,
				superTravelBelt,
				terraAxe,
				terraPick,
				terraSword,
				terraformRod,
				terrasteelBoots,
				terrasteelChest,
				terrasteelHelm,
				terrasteelLegs,
				thirdEye,
				thunderSword,
				tornadoRod,
				travelBelt,
				waterRing,
				waterRod
		);
		this.tag(ItemTags.FREEZE_IMMUNE_WEARABLES).add(icePendant);
	}

	@Nonnull
	@Override
	public String getName() {
		return "Botania item tags";
	}
}
