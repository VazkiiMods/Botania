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
			BotaniaTags.Items.SHIMMERING_MUSHROOMS,
			ConventionalItemTags.SEEDS
	);

	public ConventionalItemTagProvider(PackOutput output, CompletableFuture<HolderLookup.Provider> lookupProvider,
			CompletableFuture<TagLookup<Block>> blockTags) {
		super(output, lookupProvider, DummyTagLookup.completedFuture(RELEVANT_TAGS), blockTags);
	}

	@Override
	protected void addTags(HolderLookup.Provider provider) {

		// Cobblestones
		copy(ConventionalBotaniaTags.Blocks.FUCHSITE_COBBLESTONES,
				ConventionalBotaniaTags.Items.FUCHSITE_COBBLESTONES);
		copy(ConventionalBotaniaTags.Blocks.TALC_COBBLESTONES,
				ConventionalBotaniaTags.Items.TALC_COBBLESTONES);
		copy(ConventionalBotaniaTags.Blocks.GNEISS_COBBLESTONES,
				ConventionalBotaniaTags.Items.GNEISS_COBBLESTONES);
		copy(ConventionalBotaniaTags.Blocks.MYCELITE_COBBLESTONES,
				ConventionalBotaniaTags.Items.MYCELITE_COBBLESTONES);
		copy(ConventionalBotaniaTags.Blocks.CATACLASITE_COBBLESTONES,
				ConventionalBotaniaTags.Items.CATACLASITE_COBBLESTONES);
		copy(ConventionalBotaniaTags.Blocks.SOLITE_COBBLESTONES,
				ConventionalBotaniaTags.Items.SOLITE_COBBLESTONES);
		copy(ConventionalBotaniaTags.Blocks.LUNITE_COBBLESTONES,
				ConventionalBotaniaTags.Items.LUNITE_COBBLESTONES);
		copy(ConventionalBotaniaTags.Blocks.ROSY_TALC_COBBLESTONES,
				ConventionalBotaniaTags.Items.ROSY_TALC_COBBLESTONES);
		tag(ConventionalBotaniaTags.Items.METAMORPHIC_COBBLESTONES)
				.addTag(ConventionalBotaniaTags.Items.FUCHSITE_COBBLESTONES)
				.addTag(ConventionalBotaniaTags.Items.TALC_COBBLESTONES)
				.addTag(ConventionalBotaniaTags.Items.GNEISS_COBBLESTONES)
				.addTag(ConventionalBotaniaTags.Items.MYCELITE_COBBLESTONES)
				.addTag(ConventionalBotaniaTags.Items.CATACLASITE_COBBLESTONES)
				.addTag(ConventionalBotaniaTags.Items.SOLITE_COBBLESTONES)
				.addTag(ConventionalBotaniaTags.Items.LUNITE_COBBLESTONES)
				.addTag(ConventionalBotaniaTags.Items.ROSY_TALC_COBBLESTONES);
		tag(ConventionalItemTags.COBBLESTONES).addTag(ConventionalBotaniaTags.Items.METAMORPHIC_COBBLESTONES);
		copy(ConventionalBlockTags.STONES, ConventionalItemTags.STONES);

		// Buckets
		tag(ConventionalBotaniaTags.Items.EXTRAPOLATING_BUCKETS).add(BotaniaItems.EXTRAPOLATED_BUCKET);
		tag(ConventionalItemTags.BUCKETS).addTag(ConventionalBotaniaTags.Items.EXTRAPOLATING_BUCKETS);

		// Dusts
		tag(ConventionalBotaniaTags.Items.MANA_DUSTS).add(BotaniaItems.MANA_POWDER);
		tag(ConventionalBotaniaTags.Items.PIXIE_DUSTS).add(BotaniaItems.PIXIE_DUST);
		tag(ConventionalItemTags.DUSTS)
				.addTag(ConventionalBotaniaTags.Items.MANA_DUSTS)
				.addTag(ConventionalBotaniaTags.Items.PIXIE_DUSTS);

		// Dyed
		ColorHelper.supportedColors().forEach(color -> {
			tag(TagKey.create(ConventionalItemTags.DYED.registry(),
					ConventionalItemTags.DYED.location().withSuffix("/" + color.getSerializedName())))
					.add(BotaniaBlocks.findOptionallyDyedBlock(BotaniaBlocks.MANA_POOL, color).asItem())
					.add(BotaniaBlocks.findOptionallyDyedBlock(BotaniaBlocks.CREATIVE_MANA_POOL, color).asItem())
					.add(BotaniaBlocks.findOptionallyDyedBlock(BotaniaBlocks.DILUTED_MANA_POOL, color).asItem())
					.add(BotaniaBlocks.findOptionallyDyedBlock(BotaniaBlocks.FABULOUS_MANA_POOL, color).asItem());
			// no items for spreaders, so can't just copy block tags
		});

		// Fences and fence gates
		tag(ConventionalItemTags.WOODEN_FENCE_GATES).add(
				BotaniaBlocks.LIVINGWOOD_FENCE_GATE.asItem(),
				BotaniaBlocks.DREAMWOOD_FENCE_GATE.asItem(),
				BotaniaBlocks.SHIMMERWOOD_FENCE_GATE.asItem()
		);
		tag(ConventionalItemTags.WOODEN_FENCES).add(
				BotaniaBlocks.LIVINGWOOD_FENCE.asItem(),
				BotaniaBlocks.DREAMWOOD_FENCE.asItem(),
				BotaniaBlocks.SHIMMERWOOD_FENCE.asItem()
		);

		// Foods and Drinks
		tag(ConventionalItemTags.COOKIE_FOODS).add(BotaniaItems.BISCUIT_OF_TOTALITY);
		tag(ConventionalItemTags.FRUIT_FOODS).add(BotaniaItems.FRUIT_OF_GRISAIA);
		tag(ConventionalItemTags.MAGIC_DRINKS).add(
				BotaniaItems.MANA_IN_A_BOTTLE, BotaniaItems.BREW_VIAL, BotaniaItems.BREW_FLASK
		);
		tag(ConventionalItemTags.WATERY_DRINKS).add(
				BotaniaItems.BREW_VIAL, BotaniaItems.BREW_FLASK
		);
		tag(ConventionalItemTags.DRINK_CONTAINING_BOTTLE).add(
				BotaniaItems.MANA_IN_A_BOTTLE, BotaniaItems.BREW_VIAL, BotaniaItems.BREW_FLASK
		);

		// Gems
		tag(ConventionalBotaniaTags.Items.MANA_DIAMOND_GEMS).add(BotaniaItems.MANA_DIAMOND);
		tag(ConventionalBotaniaTags.Items.MANA_PEARL_GEMS).add(BotaniaItems.MANA_PEARL);
		tag(ConventionalBotaniaTags.Items.DRAGONSTONE_GEMS).add(BotaniaItems.DRAGONSTONE);
		tag(ConventionalBotaniaTags.Items.BLAZE_QUARTZ_GEMS).add(BotaniaItems.BLAZE_QUARTZ);
		tag(ConventionalBotaniaTags.Items.DARK_QUARTZ_GEMS).add(BotaniaItems.DARK_QUARTZ);
		tag(ConventionalBotaniaTags.Items.ELVEN_QUARTZ_GEMS).add(BotaniaItems.ELVEN_QUARTZ);
		tag(ConventionalBotaniaTags.Items.LAVENDER_QUARTZ_GEMS).add(BotaniaItems.LAVENDER_QUARTZ);
		tag(ConventionalBotaniaTags.Items.MANA_QUARTZ_GEMS).add(BotaniaItems.MANA_QUARTZ);
		tag(ConventionalBotaniaTags.Items.RED_QUARTZ_GEMS).add(BotaniaItems.RED_QUARTZ);
		tag(ConventionalBotaniaTags.Items.SUNNY_QUARTZ_GEMS).add(BotaniaItems.SUNNY_QUARTZ);
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
		tag(BotaniaTags.Items.MANA_GEMS)
				.addTag(ConventionalBotaniaTags.Items.MANA_DIAMOND_GEMS)
				.addTag(ConventionalBotaniaTags.Items.MANA_PEARL_GEMS);

		// Glass blocks and panes
		copy(ConventionalBotaniaTags.Blocks.MANA_GLASS_BLOCKS, ConventionalBotaniaTags.Items.MANA_GLASS_BLOCKS);
		tag(ConventionalItemTags.GLASS_BLOCKS).addTag(ConventionalBotaniaTags.Items.MANA_GLASS_BLOCKS);

		copy(ConventionalBotaniaTags.Blocks.MANA_GLASS_PANES, ConventionalBotaniaTags.Items.MANA_GLASS_PANES);
		tag(ConventionalItemTags.GLASS_PANES).addTag(ConventionalBotaniaTags.Items.MANA_GLASS_PANES);

		// Ingots
		tag(ConventionalBotaniaTags.Items.MANASTEEL_INGOTS).add(BotaniaItems.MANASTEEL_INGOT);
		tag(ConventionalBotaniaTags.Items.TERRASTEEL_INGOTS).add(BotaniaItems.TERRASTEEL_INGOT);
		tag(ConventionalBotaniaTags.Items.ELEMENTIUM_INGOTS).add(BotaniaItems.ELEMENTIUM_INGOT);
		tag(ConventionalBotaniaTags.Items.GAIA_INGOTS).add(BotaniaItems.GAIA_INGOT);
		tag(ConventionalItemTags.INGOTS)
				.addTag(ConventionalBotaniaTags.Items.MANASTEEL_INGOTS)
				.addTag(ConventionalBotaniaTags.Items.TERRASTEEL_INGOTS)
				.addTag(ConventionalBotaniaTags.Items.ELEMENTIUM_INGOTS)
				.addTag(ConventionalBotaniaTags.Items.GAIA_INGOTS);

		// Nuggets
		tag(ConventionalBotaniaTags.Items.MANASTEEL_NUGGETS).add(BotaniaItems.MANASTEEL_NUGGET);
		tag(ConventionalBotaniaTags.Items.TERRASTEEL_NUGGETS).add(BotaniaItems.TERRASTEEL_NUGGET);
		tag(ConventionalBotaniaTags.Items.ELEMENTIUM_NUGGETS).add(BotaniaItems.ELEMENTIUM_NUGGET);
		tag(ConventionalItemTags.NUGGETS)
				.addTag(ConventionalBotaniaTags.Items.MANASTEEL_NUGGETS)
				.addTag(ConventionalBotaniaTags.Items.TERRASTEEL_NUGGETS)
				.addTag(ConventionalBotaniaTags.Items.ELEMENTIUM_NUGGETS);

		// Rods (intentionally not wooden rods)
		tag(ConventionalBotaniaTags.Items.LIVINGWOOD_RODS).add(BotaniaItems.LIVINGWOOD_TWIG);
		tag(ConventionalBotaniaTags.Items.DREAMWOOD_RODS).add(BotaniaItems.DREAMWOOD_TWIG);
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
				BotaniaItems.LIVINGWOOD_BOW, BotaniaItems.CRYSTAL_BOW
		);
		tag(ConventionalItemTags.MELEE_WEAPON_TOOLS).add(
				BotaniaItems.MANASTEEL_SWORD, BotaniaItems.MANASTEEL_AXE, BotaniaItems.SOULSCRIBE,
				BotaniaItems.TERRA_BLADE, BotaniaItems.TERRA_TRUNCATOR,
				BotaniaItems.ELEMENTIUM_SWORD, BotaniaItems.ELEMENTIUM_AXE,
				BotaniaItems.THUNDERCALLER, BotaniaItems.STARCALLER
		);
		tag(ConventionalItemTags.MINING_TOOL_TOOLS).add(
				BotaniaItems.MANASTEEL_PICKAXE, BotaniaItems.VITREOUS_PICKAXE,
				BotaniaItems.ELEMENTIUM_PICKAXE, BotaniaItems.TERRA_SHATTERER
		);
		tag(ConventionalItemTags.RANGED_WEAPON_TOOLS).add(
				BotaniaItems.LIVINGWOOD_BOW, BotaniaItems.CRYSTAL_BOW
		);
		tag(ConventionalItemTags.SHEAR_TOOLS).add(
				BotaniaItems.MANASTEEL_SHEARS, BotaniaItems.ELEMENTIUM_SHEARS
		);
		// this is a NeoForge tag, but appears to be missing on Fabric
		tag(TagKey.create(Registries.ITEM, ResourceLocation.fromNamespaceAndPath("c", "tools/wrench")))
				.add(BotaniaItems.WAND_OF_THE_FOREST, BotaniaItems.WAND_OF_THE_ELVEN_FOREST);

		// Miscellaneous
		tag(ConventionalItemTags.MUSHROOMS).addTag(BotaniaTags.Items.SHIMMERING_MUSHROOMS);
		tag(ConventionalItemTags.MUSIC_DISCS).add(
				BotaniaItems.SCATHED_MUSIC_DISC_1, BotaniaItems.SCATHED_MUSIC_DISC_2);
		copy(ConventionalBlockTags.STRIPPED_LOGS, ConventionalItemTags.STRIPPED_LOGS);
		copy(ConventionalBlockTags.STRIPPED_WOODS, ConventionalItemTags.STRIPPED_WOODS);

		tag(BotaniaTags.Items.LOONIUM_EXCLUDED)
				.add(
						BotaniaItems.LEXICA_BOTANIA, BotaniaItems.BLACK_LOTUS, BotaniaItems.BLACKER_LOTUS,
						Items.TRIAL_KEY, Items.OMINOUS_TRIAL_KEY)
				.addTag(ConventionalItemTags.MUSIC_DISCS);

		tag(BotaniaTags.Items.SEED_APOTHECARY_REAGENT)
				.add(Items.NETHER_WART)
				.addTag(ConventionalItemTags.SEEDS);
	}

	@Override
	public String getName() {
		return "Conventional " + super.getName();
	}
}
