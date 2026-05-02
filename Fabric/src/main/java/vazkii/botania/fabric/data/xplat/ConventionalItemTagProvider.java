/*
 * This class is distributed as part of the Botania Mod.
 * Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 *
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 *
 */

package vazkii.botania.fabric.data.xplat;

import net.fabricmc.fabric.api.tag.convention.v2.ConventionalBlockTags;
import net.fabricmc.fabric.api.tag.convention.v2.ConventionalItemTags;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.PackOutput;
import net.minecraft.data.tags.ItemTagsProvider;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.block.Block;

import vazkii.botania.common.block.BotaniaBlocks;
import vazkii.botania.common.helper.ColorHelper;
import vazkii.botania.common.item.BotaniaItems;
import vazkii.botania.common.lib.BotaniaTags;
import vazkii.botania.common.lib.ConventionalBotaniaTags;
import vazkii.botania.data.util.DummyTagLookup;

import java.util.Set;
import java.util.concurrent.CompletableFuture;

public class ConventionalItemTagProvider extends ItemTagsProvider {
	private static final Set<TagKey<Item>> RELEVANT_TAGS = Set.of(
			BotaniaTags.Items.SHIMMERING_MUSHROOMS
	);

	public ConventionalItemTagProvider(PackOutput packOutput, CompletableFuture<HolderLookup.Provider> lookupProvider, CompletableFuture<TagLookup<Block>> blockTagProvider) {
		super(packOutput, lookupProvider, DummyTagLookup.completedFuture(RELEVANT_TAGS), blockTagProvider);
	}

	@Override
	protected void addTags(HolderLookup.Provider provider) {

		// Cobblestones
		copy(ConventionalBotaniaTags.Blocks.METAMORPHIC_FOREST_COBBLESTONES,
				ConventionalBotaniaTags.Items.METAMORPHIC_FOREST_COBBLESTONES);
		copy(ConventionalBotaniaTags.Blocks.METAMORPHIC_PLAINS_COBBLESTONES,
				ConventionalBotaniaTags.Items.METAMORPHIC_PLAINS_COBBLESTONES);
		copy(ConventionalBotaniaTags.Blocks.METAMORPHIC_MOUNTAIN_COBBLESTONES,
				ConventionalBotaniaTags.Items.METAMORPHIC_MOUNTAIN_COBBLESTONES);
		copy(ConventionalBotaniaTags.Blocks.METAMORPHIC_FUNGAL_COBBLESTONES,
				ConventionalBotaniaTags.Items.METAMORPHIC_FUNGAL_COBBLESTONES);
		copy(ConventionalBotaniaTags.Blocks.METAMORPHIC_SWAMP_COBBLESTONES,
				ConventionalBotaniaTags.Items.METAMORPHIC_SWAMP_COBBLESTONES);
		copy(ConventionalBotaniaTags.Blocks.METAMORPHIC_DESERT_COBBLESTONES,
				ConventionalBotaniaTags.Items.METAMORPHIC_DESERT_COBBLESTONES);
		copy(ConventionalBotaniaTags.Blocks.METAMORPHIC_TAIGA_COBBLESTONES,
				ConventionalBotaniaTags.Items.METAMORPHIC_TAIGA_COBBLESTONES);
		copy(ConventionalBotaniaTags.Blocks.METAMORPHIC_MESA_COBBLESTONES,
				ConventionalBotaniaTags.Items.METAMORPHIC_MESA_COBBLESTONES);
		tag(ConventionalBotaniaTags.Items.METAMORPHIC_COBBLESTONES)
				.addTag(ConventionalBotaniaTags.Items.METAMORPHIC_FOREST_COBBLESTONES)
				.addTag(ConventionalBotaniaTags.Items.METAMORPHIC_PLAINS_COBBLESTONES)
				.addTag(ConventionalBotaniaTags.Items.METAMORPHIC_MOUNTAIN_COBBLESTONES)
				.addTag(ConventionalBotaniaTags.Items.METAMORPHIC_FUNGAL_COBBLESTONES)
				.addTag(ConventionalBotaniaTags.Items.METAMORPHIC_SWAMP_COBBLESTONES)
				.addTag(ConventionalBotaniaTags.Items.METAMORPHIC_DESERT_COBBLESTONES)
				.addTag(ConventionalBotaniaTags.Items.METAMORPHIC_TAIGA_COBBLESTONES)
				.addTag(ConventionalBotaniaTags.Items.METAMORPHIC_MESA_COBBLESTONES);
		tag(ConventionalItemTags.COBBLESTONES).addTag(ConventionalBotaniaTags.Items.METAMORPHIC_COBBLESTONES);
		copy(ConventionalBlockTags.STONES, ConventionalItemTags.STONES);

		// Buckets
		tag(ConventionalBotaniaTags.Items.EXTRAPOLATING_BUCKETS).add(BotaniaItems.openBucket);
		tag(ConventionalItemTags.BUCKETS)
				.addTag(ConventionalBotaniaTags.Items.EXTRAPOLATING_BUCKETS);

		// Dusts
		tag(ConventionalBotaniaTags.Items.MANA_DUSTS).add(BotaniaItems.manaPowder);
		tag(ConventionalBotaniaTags.Items.PIXIE_DUSTS).add(BotaniaItems.pixieDust);
		tag(ConventionalItemTags.DUSTS)
				.addTag(ConventionalBotaniaTags.Items.MANA_DUSTS)
				.addTag(ConventionalBotaniaTags.Items.PIXIE_DUSTS);

		// Dyed
		ColorHelper.supportedColors().forEach(color -> {
			var blockTag = TagKey.create(ConventionalBlockTags.DYED.registry(),
					ConventionalBlockTags.DYED.location().withSuffix("/" + color.getSerializedName()));
			var itemTag = TagKey.create(ConventionalItemTags.DYED.registry(),
					ConventionalItemTags.DYED.location().withSuffix("/" + color.getSerializedName()));
			copy(blockTag, itemTag);
		});

		// Fences and fence gates
		tag(ConventionalItemTags.WOODEN_FENCE_GATES).add(
				BotaniaBlocks.livingwoodFenceGate.asItem(), BotaniaBlocks.dreamwoodFenceGate.asItem(),
				BotaniaBlocks.shimmerwoodFenceGate.asItem()
		);
		tag(ConventionalItemTags.WOODEN_FENCES).add(
				BotaniaBlocks.livingwoodFence.asItem(), BotaniaBlocks.dreamwoodFence.asItem(),
				BotaniaBlocks.shimmerwoodFence.asItem()
		);

		// Foods and Drinks
		tag(ConventionalItemTags.COOKIE_FOODS).add(BotaniaItems.manaCookie);
		tag(ConventionalItemTags.FRUIT_FOODS).add(BotaniaItems.infiniteFruit);
		tag(ConventionalItemTags.MAGIC_DRINKS).add(
				BotaniaItems.manaBottle, BotaniaItems.brewVial, BotaniaItems.brewFlask
		);
		tag(ConventionalItemTags.WATERY_DRINKS).add(
				BotaniaItems.brewVial, BotaniaItems.brewFlask
		);
		tag(ConventionalItemTags.DRINK_CONTAINING_BOTTLE).add(
				BotaniaItems.manaBottle, BotaniaItems.brewVial, BotaniaItems.brewFlask
		);

		// Gems
		tag(ConventionalBotaniaTags.Items.MANA_DIAMOND_GEMS).add(BotaniaItems.manaDiamond);
		tag(ConventionalBotaniaTags.Items.MANA_PEARL_GEMS).add(BotaniaItems.manaPearl);
		tag(ConventionalBotaniaTags.Items.DRAGONSTONE_GEMS).add(BotaniaItems.dragonstone);
		tag(ConventionalBotaniaTags.Items.BLAZE_QUARTZ_GEMS).add(BotaniaItems.blazeQuartz);
		tag(ConventionalBotaniaTags.Items.DARK_QUARTZ_GEMS).add(BotaniaItems.darkQuartz);
		tag(ConventionalBotaniaTags.Items.ELVEN_QUARTZ_GEMS).add(BotaniaItems.elfQuartz);
		tag(ConventionalBotaniaTags.Items.LAVENDER_QUARTZ_GEMS).add(BotaniaItems.lavenderQuartz);
		tag(ConventionalBotaniaTags.Items.MANA_QUARTZ_GEMS).add(BotaniaItems.manaQuartz);
		tag(ConventionalBotaniaTags.Items.RED_QUARTZ_GEMS).add(BotaniaItems.redQuartz);
		tag(ConventionalBotaniaTags.Items.SUNNY_QUARTZ_GEMS).add(BotaniaItems.sunnyQuartz);
		tag(ConventionalItemTags.GEMS)
				.addTag(ConventionalBotaniaTags.Items.MANA_DIAMOND_GEMS)
				.addTag(ConventionalBotaniaTags.Items.MANA_PEARL_GEMS)
				.addTag(ConventionalBotaniaTags.Items.DRAGONSTONE_GEMS)
				.addTag(ConventionalBotaniaTags.Items.BLAZE_QUARTZ_GEMS)
				.addTag(ConventionalBotaniaTags.Items.DARK_QUARTZ_GEMS)
				.addTag(ConventionalBotaniaTags.Items.ELVEN_QUARTZ_GEMS)
				.addTag(ConventionalBotaniaTags.Items.LAVENDER_QUARTZ_GEMS)
				.addTag(ConventionalBotaniaTags.Items.MANA_QUARTZ_GEMS)
				.addTag(ConventionalBotaniaTags.Items.RED_QUARTZ_GEMS)
				.addTag(ConventionalBotaniaTags.Items.SUNNY_QUARTZ_GEMS);

		// Glass blocks and panes
		copy(ConventionalBotaniaTags.Blocks.MANA_GLASS_BLOCKS, ConventionalBotaniaTags.Items.MANA_GLASS_BLOCKS);
		tag(ConventionalItemTags.GLASS_BLOCKS).addTag(ConventionalBotaniaTags.Items.MANA_GLASS_BLOCKS);

		copy(ConventionalBotaniaTags.Blocks.MANA_GLASS_PANES, ConventionalBotaniaTags.Items.MANA_GLASS_PANES);
		tag(ConventionalItemTags.GLASS_PANES).addTag(ConventionalBotaniaTags.Items.MANA_GLASS_PANES);

		// Ingots
		tag(ConventionalBotaniaTags.Items.MANASTEEL_INGOTS).add(BotaniaItems.manaSteel);
		tag(ConventionalBotaniaTags.Items.TERRASTEEL_INGOTS).add(BotaniaItems.terrasteel);
		tag(ConventionalBotaniaTags.Items.ELEMENTIUM_INGOTS).add(BotaniaItems.elementium);
		tag(ConventionalBotaniaTags.Items.GAIA_INGOTS).add(BotaniaItems.gaiaIngot);
		tag(ConventionalItemTags.INGOTS)
				.addTag(ConventionalBotaniaTags.Items.MANASTEEL_INGOTS)
				.addTag(ConventionalBotaniaTags.Items.TERRASTEEL_INGOTS)
				.addTag(ConventionalBotaniaTags.Items.ELEMENTIUM_INGOTS)
				.addTag(ConventionalBotaniaTags.Items.GAIA_INGOTS);

		// Nuggets
		tag(ConventionalBotaniaTags.Items.MANASTEEL_NUGGETS).add(BotaniaItems.manasteelNugget);
		tag(ConventionalBotaniaTags.Items.TERRASTEEL_NUGGETS).add(BotaniaItems.terrasteelNugget);
		tag(ConventionalBotaniaTags.Items.ELEMENTIUM_NUGGETS).add(BotaniaItems.elementiumNugget);
		tag(ConventionalItemTags.NUGGETS)
				.addTag(ConventionalBotaniaTags.Items.MANASTEEL_NUGGETS)
				.addTag(ConventionalBotaniaTags.Items.TERRASTEEL_NUGGETS)
				.addTag(ConventionalBotaniaTags.Items.ELEMENTIUM_NUGGETS);

		// Rods (intentionally not wooden rods)
		tag(ConventionalBotaniaTags.Items.LIVINGWOOD_RODS).add(BotaniaItems.livingwoodTwig);
		tag(ConventionalBotaniaTags.Items.DREAMWOOD_RODS).add(BotaniaItems.dreamwoodTwig);
		tag(ConventionalItemTags.RODS)
				.addTag(ConventionalBotaniaTags.Items.LIVINGWOOD_RODS)
				.addTag(ConventionalBotaniaTags.Items.DREAMWOOD_RODS);

		// Storage blocks
		copy(ConventionalBotaniaTags.Blocks.MANASTEEL_STORAGE_BLOCKS,
				ConventionalBotaniaTags.Items.MANASTEEL_STORAGE_BLOCKS);
		copy(ConventionalBotaniaTags.Blocks.TERRASTEEL_STORAGE_BLOCKS,
				ConventionalBotaniaTags.Items.TERRASTEEL_STORAGE_BLOCKS);
		copy(ConventionalBotaniaTags.Blocks.ELEMENTIUM_STORAGE_BLOCKS,
				ConventionalBotaniaTags.Items.ELEMENTIUM_STORAGE_BLOCKS);
		copy(ConventionalBotaniaTags.Blocks.MANA_DIAMOND_STORAGE_BLOCKS,
				ConventionalBotaniaTags.Items.MANA_DIAMOND_STORAGE_BLOCKS);
		copy(ConventionalBotaniaTags.Blocks.DRAGONSTONE_STORAGE_BLOCKS,
				ConventionalBotaniaTags.Items.DRAGONSTONE_STORAGE_BLOCKS);
		copy(ConventionalBotaniaTags.Blocks.BLAZE_STORAGE_BLOCKS,
				ConventionalBotaniaTags.Items.BLAZE_STORAGE_BLOCKS);
		ColorHelper.supportedColors().forEach(dyeColor -> {
			var blockTag = ConventionalBotaniaTags.Blocks.PETAL_STORAGE_BLOCKS_BY_COLOR.get(dyeColor);
			var itemTag = ConventionalBotaniaTags.Items.PETAL_STORAGE_BLOCKS_BY_COLOR.get(dyeColor);
			copy(blockTag, itemTag);
			tag(ConventionalBotaniaTags.Items.PETAL_STORAGE_BLOCKS).addTag(itemTag);
		});
		tag(ConventionalItemTags.STORAGE_BLOCKS)
				.addTag(ConventionalBotaniaTags.Items.MANASTEEL_STORAGE_BLOCKS)
				.addTag(ConventionalBotaniaTags.Items.TERRASTEEL_STORAGE_BLOCKS)
				.addTag(ConventionalBotaniaTags.Items.ELEMENTIUM_STORAGE_BLOCKS)
				.addTag(ConventionalBotaniaTags.Items.MANA_DIAMOND_STORAGE_BLOCKS)
				.addTag(ConventionalBotaniaTags.Items.DRAGONSTONE_STORAGE_BLOCKS)
				.addTag(ConventionalBotaniaTags.Items.BLAZE_STORAGE_BLOCKS)
				.addTag(ConventionalBotaniaTags.Items.PETAL_STORAGE_BLOCKS);

		// Tools
		tag(ConventionalItemTags.BOW_TOOLS).add(
				BotaniaItems.livingwoodBow, BotaniaItems.crystalBow
		);
		tag(ConventionalItemTags.MELEE_WEAPON_TOOLS).add(
				BotaniaItems.manasteelSword, BotaniaItems.manasteelAxe, BotaniaItems.enderDagger,
				BotaniaItems.terraSword, BotaniaItems.terraAxe,
				BotaniaItems.elementiumSword, BotaniaItems.elementiumAxe,
				BotaniaItems.thunderSword, BotaniaItems.starSword
		);
		tag(ConventionalItemTags.MINING_TOOL_TOOLS).add(
				BotaniaItems.manasteelPick, BotaniaItems.glassPick,
				BotaniaItems.elementiumPick, BotaniaItems.terraPick
		);
		tag(ConventionalItemTags.RANGED_WEAPON_TOOLS).add(
				BotaniaItems.livingwoodBow, BotaniaItems.crystalBow
		);
		tag(ConventionalItemTags.SHEAR_TOOLS).add(
				BotaniaItems.manasteelShears, BotaniaItems.elementiumShears
		);
		// this is a NeoForge tag, but appears to be missing on Fabric
		tag(TagKey.create(Registries.ITEM, ResourceLocation.fromNamespaceAndPath("c", "tools/wrench")))
				.add(BotaniaItems.twigWand, BotaniaItems.dreamwoodWand);

		// Miscellaneous
		tag(ConventionalItemTags.MUSHROOMS).addTag(BotaniaTags.Items.SHIMMERING_MUSHROOMS);
		tag(ConventionalItemTags.MUSIC_DISCS).add(BotaniaItems.recordGaia1, BotaniaItems.recordGaia2);
		copy(ConventionalBlockTags.STRIPPED_LOGS, ConventionalItemTags.STRIPPED_LOGS);
		copy(ConventionalBlockTags.STRIPPED_WOODS, ConventionalItemTags.STRIPPED_WOODS);

		tag(BotaniaTags.Items.LOONIUM_EXCLUDED)
				.add(BotaniaItems.lexicon, BotaniaItems.blackLotus, BotaniaItems.blackerLotus, Items.TRIAL_KEY, Items.OMINOUS_TRIAL_KEY)
				.addTag(ConventionalItemTags.MUSIC_DISCS);
	}

	@Override
	public String getName() {
		return "Conventional " + super.getName();
	}
}
