/*
 * This class is distributed as part of the Botania Mod.
 * Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 *
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 */
package vazkii.botania.data;

import net.fabricmc.fabric.api.tag.TagFactory;
import net.fabricmc.fabric.api.tool.attribute.v1.FabricToolTags;
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

		this.tag(ModTags.Items.DISPOSABLE).add(Items.DIRT, Items.SAND, Items.GRAVEL, Items.COBBLESTONE, Items.NETHERRACK);
		this.tag(ModTags.Items.SEMI_DISPOSABLE).add(Items.ANDESITE, Items.DIORITE, Items.GRANITE)
				.addOptional(new ResourceLocation("quark", "basalt"))
				.addOptional(new ResourceLocation("quark", "jasper"))
				.addOptional(new ResourceLocation("quark", "limestone"))
				.addOptional(new ResourceLocation("quark", "marble"))
				.addOptional(new ResourceLocation("quark", "slate"));

		this.tag(ModTags.Items.RUNES).add(
				ModItems.runeWater, ModItems.runeFire, ModItems.runeEarth, ModItems.runeAir,
				ModItems.runeSpring, ModItems.runeSummer, ModItems.runeAutumn, ModItems.runeWinter,
				ModItems.runeMana, ModItems.runeLust, ModItems.runeGluttony, ModItems.runeGreed,
				ModItems.runeSloth, ModItems.runeWrath, ModItems.runeEnvy, ModItems.runePride
		);

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

		this.generateAccessoryTags();
	}

	private void generateAccessoryTags() {
		this.tag(accessory("chest/cape")).add(
				ModItems.balanceCloak,
				ModItems.holyCloak,
				ModItems.invisibilityCloak,
				ModItems.unholyCloak
		);
		this.tag(accessory("chest/necklace")).add(
				ModItems.bloodPendant,
				ModItems.cloudPendant,
				ModItems.divaCharm,
				ModItems.goddessCharm,
				ModItems.icePendant,
				ModItems.lavaPendant,
				ModItems.superCloudPendant,
				ModItems.superLavaPendant,
				ModItems.thirdEye
		);
		this.tag(accessory("hand/ring")).add(
				ModItems.auraRing,
				ModItems.auraRingGreater,
				ModItems.dodgeRing,
				ModItems.lokiRing,
				ModItems.magnetRing,
				ModItems.magnetRingGreater,
				ModItems.manaRing,
				ModItems.manaRingGreater,
				ModItems.miningRing,
				ModItems.odinRing,
				ModItems.pixieRing,
				ModItems.reachRing,
				ModItems.swapRing,
				ModItems.thorRing,
				ModItems.waterRing
		);
		this.tag(accessory("head/face")).add(
				ModItems.itemFinder,
				ModItems.monocle,
				ModItems.tinyPlanet
		);
		this.tag(accessory("head/hat")).add(
				ModItems.flightTiara
		);
		this.tag(accessory("legs/belt")).add(
				ModItems.knockbackBelt,
				ModItems.speedUpBelt,
				ModItems.superTravelBelt,
				ModItems.travelBelt
		);

	}

	private static Tag.Named<Item> accessory(String name) {
		return TagFactory.ITEM.create(new ResourceLocation("trinkets", name));
	}

	@Nonnull
	@Override
	public String getName() {
		return "Botania item tags";
	}
}
