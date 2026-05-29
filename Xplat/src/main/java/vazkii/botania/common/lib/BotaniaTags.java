/*
 * This class is distributed as part of the Botania Mod.
 * Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 *
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 */
package vazkii.botania.common.lib;

import net.minecraft.core.component.DataComponentType;
import net.minecraft.core.registries.Registries;
import net.minecraft.tags.TagKey;
import net.minecraft.world.damagesource.DamageType;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BannerPattern;
import net.minecraft.world.level.material.Fluid;

import static vazkii.botania.api.BotaniaAPI.botaniaRL;
import static vazkii.botania.api.BotaniaAPI.gogRL;

public class BotaniaTags {
	public static class Items {
		public static final TagKey<Item> BLOCKS_QUARTZ = tag("quartz_blocks");

		public static final TagKey<Item> MYSTICAL_FLOWERS = tag("mystical_flowers");
		public static final TagKey<Item> SHINY_FLOWERS = tag("shiny_flowers");
		public static final TagKey<Item> DOUBLE_MYSTICAL_FLOWERS = tag("double_mystical_flowers");
		public static final TagKey<Item> SHIMMERING_MUSHROOMS = tag("shimmering_mushrooms");

		/**
		 * Items in this tag can be specified as contributor headflowers
		 */
		public static final TagKey<Item> CONTRIBUTOR_HEADFLOWERS = tag("contributor_headflowers");
		public static final TagKey<Item> SPECIAL_FLOWERS = tag("special_flowers");
		public static final TagKey<Item> MINI_FLOWERS = tag("mini_flowers");
		public static final TagKey<Item> MISC_SPECIAL_FLOWERS = tag("misc_special_flowers");
		public static final TagKey<Item> FUNCTIONAL_SPECIAL_FLOWERS = tag("functional_special_flowers");
		public static final TagKey<Item> GENERATING_SPECIAL_FLOWERS = tag("generating_special_flowers");

		public static final TagKey<Item> FLOATING_FLOWERS = tag("floating_flowers");
		public static final TagKey<Item> MUNDANE_FLOATING_FLOWERS = tag("mundane_floating_flowers");
		public static final TagKey<Item> SPECIAL_FLOATING_FLOWERS = tag("special_floating_flowers");
		public static final TagKey<Item> MISC_SPECIAL_FLOATING_FLOWERS = tag("misc_special_floating_flowers");
		public static final TagKey<Item> FUNCTIONAL_SPECIAL_FLOATING_FLOWERS = tag("functional_special_floating_flowers");
		public static final TagKey<Item> GENERATING_SPECIAL_FLOATING_FLOWERS = tag("generating_special_floating_flowers");

		public static final TagKey<Item> LENS = tag("lens");

		public static final TagKey<Item> LENS_GLUE = tag("lens_glue");

		/**
		 * Items in this tag cannot be pulled by the magnet rings
		 */
		public static final TagKey<Item> MAGNET_RING_IGNORED = tag("magnet_ring_ignored");
		/**
		 * Items in this tag cannot be rolled by Looniums
		 */
		public static final TagKey<Item> LOONIUM_EXCLUDED = tag("loonium_excluded");
		/**
		 * Items that should be equipped in the offhand slot if rolled as Loonium mob equipment,
		 * instead of the default slot for the item.
		 */
		public static final TagKey<Item> LOONIUM_OFFHAND_EQUIPMENT = tag("loonium_offhand_equipment");

		/**
		 * Items in this tag are voided by the Elementium Pick
		 */
		public static final TagKey<Item> DISPOSABLE = tag("disposable");
		/**
		 * Items in this tag are voided by the Elementium Pick when not shifting
		 */
		public static final TagKey<Item> SEMI_DISPOSABLE = tag("semi_disposable");

		public static final TagKey<Item> PETALS = tag("petals");
		public static final TagKey<Item> PETALS_BLACK = tag("petals/black");
		public static final TagKey<Item> PETALS_BLUE = tag("petals/blue");
		public static final TagKey<Item> PETALS_BROWN = tag("petals/brown");
		public static final TagKey<Item> PETALS_CYAN = tag("petals/cyan");
		public static final TagKey<Item> PETALS_GRAY = tag("petals/gray");
		public static final TagKey<Item> PETALS_GREEN = tag("petals/green");
		public static final TagKey<Item> PETALS_LIGHT_BLUE = tag("petals/light_blue");
		public static final TagKey<Item> PETALS_LIGHT_GRAY = tag("petals/light_gray");
		public static final TagKey<Item> PETALS_LIME = tag("petals/lime");
		public static final TagKey<Item> PETALS_MAGENTA = tag("petals/magenta");
		public static final TagKey<Item> PETALS_ORANGE = tag("petals/orange");
		public static final TagKey<Item> PETALS_PINK = tag("petals/pink");
		public static final TagKey<Item> PETALS_PURPLE = tag("petals/purple");
		public static final TagKey<Item> PETALS_RED = tag("petals/red");
		public static final TagKey<Item> PETALS_WHITE = tag("petals/white");
		public static final TagKey<Item> PETALS_YELLOW = tag("petals/yellow");

		public static final TagKey<Item> PETAL_APOTHECARIES = tag("petal_apothecaries");

		// for advancement use
		public static final TagKey<Item> ALL_MANA_POOLS = tag("all_mana_pools");
		public static final TagKey<Item> MANA_POOLS = tag("mana_pools");
		public static final TagKey<Item> CREATIVE_POOLS = tag("creative_pools");
		public static final TagKey<Item> DILUTED_POOLS = tag("diluted_pools");
		public static final TagKey<Item> FABULOUS_POOLS = tag("fabulous_pools");

		// for recipe use
		public static final TagKey<Item> DYED_MANA_POOLS = tag("dyed_mana_pools");
		public static final TagKey<Item> DYED_CREATIVE_POOLS = tag("dyed_creative_pools");
		public static final TagKey<Item> DYED_DILUTED_POOLS = tag("dyed_diluted_pools");
		public static final TagKey<Item> DYED_FABULOUS_POOLS = tag("dyed_fabulous_pools");

		public static final TagKey<Item> MANA_POOL_DYE_REMOVER = tag("mana_pool_dye_remover");

		public static final TagKey<Item> RUNES = tag("runes");
		public static final TagKey<Item> ANCIENT_WILLS = tag("ancient_wills");

		public static final TagKey<Item> LIVINGWOOD_LOGS = tag("livingwood_logs");
		public static final TagKey<Item> DREAMWOOD_LOGS = tag("dreamwood_logs");
		public static final TagKey<Item> LIVINGWOOD_LOGS_GLIMMERING = tag("glimmering_livingwood_logs");
		public static final TagKey<Item> DREAMWOOD_LOGS_GLIMMERING = tag("glimmering_dreamwood_logs");

		/**
		 * Items in this tag allow wearing players to see bursts through walls and flower radii
		 */
		public static final TagKey<Item> BURST_VIEWERS = tag("burst_viewers");
		public static final TagKey<Item> RODS = tag("rods");
		/**
		 * Items in this tag may consume mana
		 */
		public static final TagKey<Item> MANA_USING_ITEMS = tag("mana_using_items");
		public static final TagKey<Item> SEED_APOTHECARY_REAGENT = tag("seed_apothecary_reagent");
		public static final TagKey<Item> MANA_POWDER_SOURCE_DUSTS = tag("mana_powder_source_dusts");

		/**
		 * Block provider items in this tag can be auto-selected via the vanilla "Pick Block" feature.
		 * (Not every block provider makes sense here, e.g. the Hand of Ender cannot place blocks.)
		 */
		public static final TagKey<Item> PICKABLE_BLOCK_PROVIDER = tag("pickable_block_providers");

		public static final TagKey<Item> DIMS_FLOATING_FLOWERS = tag("dims_floating_flowers");
		public static final TagKey<Item> UNDIMS_FLOATING_FLOWERS = tag("undims_floating_flowers");

		/**
		 * Items in this tag are ignored by the Endoflame, even if they are technically fuel items.
		 */
		public static final TagKey<Item> IGNORED_BY_ENDOFLAME = tag("ignored_by_endoflame");

		public static TagKey<Item> getPetalTag(DyeColor color) {
			return switch (color) {
				case WHITE -> PETALS_WHITE;
				case ORANGE -> PETALS_ORANGE;
				case MAGENTA -> PETALS_MAGENTA;
				case LIGHT_BLUE -> PETALS_LIGHT_BLUE;
				case YELLOW -> PETALS_YELLOW;
				case LIME -> PETALS_LIME;
				case PINK -> PETALS_PINK;
				case GRAY -> PETALS_GRAY;
				case LIGHT_GRAY -> PETALS_LIGHT_GRAY;
				case CYAN -> PETALS_CYAN;
				case PURPLE -> PETALS_PURPLE;
				case BLUE -> PETALS_BLUE;
				case BROWN -> PETALS_BROWN;
				case GREEN -> PETALS_GREEN;
				case RED -> PETALS_RED;
				case BLACK -> PETALS_BLACK;
			};
		}

		private static TagKey<Item> tag(String name) {
			return TagKey.create(Registries.ITEM, botaniaRL(name));
		}
	}

	public static class Blocks {
		public static final TagKey<Block> MYSTICAL_FLOWERS = tag("mystical_flowers");
		public static final TagKey<Block> SHINY_FLOWERS = tag("shiny_flowers");
		public static final TagKey<Block> DOUBLE_MYSTICAL_FLOWERS = tag("double_mystical_flowers");
		public static final TagKey<Block> SHIMMERING_MUSHROOMS = tag("shimmering_mushrooms");

		public static final TagKey<Block> SPECIAL_FLOWERS = tag("special_flowers");
		public static final TagKey<Block> MINI_FLOWERS = tag("mini_flowers");
		public static final TagKey<Block> MISC_SPECIAL_FLOWERS = tag("misc_special_flowers");
		public static final TagKey<Block> FUNCTIONAL_SPECIAL_FLOWERS = tag("functional_special_flowers");
		public static final TagKey<Block> GENERATING_SPECIAL_FLOWERS = tag("generating_special_flowers");

		public static final TagKey<Block> FLOATING_FLOWERS = tag("floating_flowers");
		public static final TagKey<Block> MUNDANE_FLOATING_FLOWERS = tag("mundane_floating_flowers");
		public static final TagKey<Block> SPECIAL_FLOATING_FLOWERS = tag("special_floating_flowers");
		public static final TagKey<Block> MISC_SPECIAL_FLOATING_FLOWERS = tag("misc_special_floating_flowers");
		public static final TagKey<Block> FUNCTIONAL_SPECIAL_FLOATING_FLOWERS = tag("functional_special_floating_flowers");
		public static final TagKey<Block> GENERATING_SPECIAL_FLOATING_FLOWERS = tag("generating_special_floating_flowers");

		/**
		 * Blocks in this tag can be used in the Enchanter multiblock
		 */
		public static final TagKey<Block> ENCHANTER_FLOWERS = tag("enchanter_flowers");

		public static final TagKey<Block> LIVINGWOOD_LOGS = tag("livingwood_logs");
		public static final TagKey<Block> DREAMWOOD_LOGS = tag("dreamwood_logs");
		public static final TagKey<Block> LIVINGWOOD_LOGS_GLIMMERING = tag("glimmering_livingwood_logs");
		public static final TagKey<Block> DREAMWOOD_LOGS_GLIMMERING = tag("glimmering_dreamwood_logs");

		public static final TagKey<Block> BLOCKS_QUARTZ = tag("quartz_blocks");

		/**
		 * Blocks in this tag are exempt from the Gaia Guardian's block smash attack
		 */
		public static final TagKey<Block> GAIA_GUARDIAN_IMMUNE = tag("gaia_guardian_immune");
		/**
		 * Items resting on blocks in this tag cannot be pulled by magnet rings
		 */
		public static final TagKey<Block> SHIELDS_FROM_MAGNET_RING = tag("shields_from_magnet_ring");
		/**
		 * Blocks in this tag cannot be moved by the Laputa Shard
		 */
		public static final TagKey<Block> LAPUTA_IMMOBILE = tag("laputa_immobile");
		/**
		 * Blocks in this tag should not be treated like a 2-high double block by the Laputa Shard, even though they
		 * have the {@link net.minecraft.world.level.block.state.properties.BlockStateProperties#DOUBLE_BLOCK_HALF}
		 * property.
		 */
		public static final TagKey<Block> LAPUTA_NO_DOUBLE_BLOCK = tag("laputa_no_double_block");

		/**
		 * Blocks in this tag can be removed by the Rod of Terra Firma
		 */
		public static final TagKey<Block> TERRAFORMABLE = tag("terraformable");

		/**
		 * Blocks in this tag can have corporea sparks placed on them even though they have no inventory
		 */
		public static final TagKey<Block> CORPOREA_SPARK_OVERRIDE = tag("corporea_spark_override");
		/**
		 * Blocks in this tag can sub for livingrock in the terra plate multiblock
		 */
		public static final TagKey<Block> TERRA_PLATE_BASE = tag("terra_plate_base");
		public static final TagKey<Block> GHOST_RAIL_BARRIER = tag("ghost_rail_barrier");

		/**
		 * Blocks in this tag can be turned into end stone by ender air
		 */
		public static final TagKey<Block> ENDER_AIR_CONVERTABLE = tag("ender_air_convertable");

		/**
		 * Blocks in this tag can be turned into metamorphic stones by the marimorphosis
		 */
		public static final TagKey<Block> MARIMORPHOSIS_CONVERTABLE = tag("marimorphosis_convertable");

		/**
		 * Blocks in this tag are affected by the weight lens even if they don't drop themselves with silk touch.
		 */
		public static final TagKey<Block> WEIGHT_LENS_AFFECTED = tag("weight_lens_affected");

		/**
		 * Blocks in this tag can be broken by the Horn/Drum of the Wild,
		 * in addition to any {@code BushBlock}s that are not part of the {@link #SPECIAL_FLOWERS} tag.
		 */
		public static final TagKey<Block> HORN_OF_THE_WILD_BREAKABLE = tag("horn_of_the_wild_breakable");
		/**
		 * Blocks that extend BushBlock (other those tagged {@link #SPECIAL_FLOWERS}) or are tagged
		 * {@link #HORN_OF_THE_WILD_BREAKABLE}, but should not be broken by the Horn/Drum of the Wild.
		 */
		public static final TagKey<Block> HORN_OF_THE_WILD_IMMUNE = tag("horn_of_the_wild_immune");

		/**
		 * Blocks in this tag can be broken by the Horn/Drum of the Canopy, unless they are persistent leaves blocks.
		 */
		public static final TagKey<Block> HORN_OF_THE_CANOPY_BREAKABLE = tag("horn_of_the_canopy_breakable");

		/**
		 * Blocks in this tag can be broken by the Horn of the Covering
		 */
		public static final TagKey<Block> HORN_OF_THE_COVERING_BREAKABLE = tag("horn_of_the_covering_breakable");

		/**
		 * Blocks in this tag are candidates for the Agricarnation's growth boost, assuming they accept random ticks.
		 */
		public static final TagKey<Block> AGRICARNATION_GROWTH_CANDIDATE = tag("agricarnation/growth_candidate");
		/**
		 * Blocks in this tag are ignored by the Agricarnation, even if they look like they are growable plants.
		 */
		public static final TagKey<Block> AGRICARNATION_GROWTH_EXCLUDED = tag("agricarnation/growth_excluded");
		/**
		 * Blocks in this tag will have their growth boosted as if bonemeal was applied, instead of via random ticks.
		 * These plants need to pass the bonemeal success check twice to get a boost, but mana will be consumed even if
		 * that fails.
		 */
		public static final TagKey<Block> AGRICARNATION_APPLY_BONEMEAL = tag("agricarnation/apply_bonemeal");

		/**
		 * Blocks in this tag can not have their state manipulated by a wand of the forest
		 */
		public static final TagKey<Block> UNWANDABLE = tag("unwandable");

		/**
		 * Blocks in this tag can be replaced by the spreading effect of Pasture Seeds and related items.
		 */
		public static final TagKey<Block> PASTURE_SEED_REPLACEABLE = tag("pasture_seed_replaceable");

		/**
		 * Blocks in this tag are considered when checking for unethical TNT sources. Blocks only need to be added
		 * to this tag if their implementation does not extend {@link net.minecraft.world.level.block.TntBlock}.
		 */
		public static final TagKey<Block> UNETHICAL_TNT_CHECK = tag("unethical_tnt_check");

		/**
		 * Blocks in this tag work better for inserting items if they don't receive more than one item at a time.
		 * Example: The vanilla crafter block, which selectively opens inventory slots to distribute matching items.
		 */
		public static final TagKey<Block> SINGLE_ITEM_INSERT = tag("single_item_insert");

		/**
		 * Blocks for which the Vitreous Pickaxe is the appropriate tool.
		 * (should be any pickaxe-mineable blocks plus glass-like blocks)
		 */
		public static final TagKey<Block> MINEABLE_WITH_VITREOUS_PICKAXE = tag("mineable/vitreous_pickaxe");

		/**
		 * Vitreous Pickaxe always mines this block as if the pick had silktouch.
		 * This also overrides mining level requirements.
		 */
		public static final TagKey<Block> VITREOUS_PICKAXE_SILKTOUCHED = tag("vitreous_pickaxe_silktouched");

		/**
		 * Wool-covered spreader blocks. These dampen vibrations and occlude vibration signals.
		 */
		public static final TagKey<Block> COVERED_SPREADERS = tag("covered_spreaders");
		public static final TagKey<Block> COVERED_MANA_SPREADERS = tag("covered_mana_spreaders");
		public static final TagKey<Block> COVERED_PULSE_SPREADERS = tag("covered_redstone_spreaders");
		public static final TagKey<Block> COVERED_ELVEN_SPREADERS = tag("covered_elven_spreaders");
		public static final TagKey<Block> COVERED_GAIA_SPREADERS = tag("covered_gaia_spreaders");

		/**
		 * Blocks consumed by the Munchdew flower.
		 * TODO: Would it accept eating the hats of huge fungi as well? Or the leaves of long vine-like plants?
		 */
		public static final TagKey<Block> MUNCHDEW_CONSUMABLE = tag("munchdew_consumable");

		/**
		 * Blocks in this tag work are likely to cause major issues when used with Abstruse/Spectral Platform blocks.
		 * This tag can only be a last resort option, as it is likely an error in Botania or the block's origin mod
		 * that causes the issues, which should be fixed properly.
		 */
		public static final TagKey<Block> UNSUPPORTED_PLATFORM_DISGUISE = tag("unsupported_platform_disguise");

		public static final TagKey<Block> GOG_PEBBLE_SOURCES =
				TagKey.create(Registries.BLOCK, gogRL("pebble_sources"));

		private static TagKey<Block> tag(String name) {
			return TagKey.create(Registries.BLOCK, botaniaRL(name));
		}
	}

	public static class Entities {
		/**
		 * Entities in this tag cannot be picked up by the Rod of the Shaded Mesa
		 */
		public static final TagKey<EntityType<?>> SHADED_MESA_NO_PICKUP = tag("shaded_mesa_no_pickup");

		public static final TagKey<EntityType<?>> COCOON_COMMON = tag("cocoon/common");
		public static final TagKey<EntityType<?>> COCOON_RARE = tag("cocoon/rare");
		public static final TagKey<EntityType<?>> COCOON_COMMON_AQUATIC = tag("cocoon/common_aquatic");
		public static final TagKey<EntityType<?>> COCOON_RARE_AQUATIC = tag("cocoon/rare_aquatic");

		/**
		 * The Drum of the Gathering fills milk buckets for mobs in this tag.
		 */
		public static final TagKey<EntityType<?>> DRUM_MILKABLE = tag("drum/milkable");

		/**
		 * The Drum of the Gathering will not shear mobs in this tag, even if they could be sheared.
		 */
		public static final TagKey<EntityType<?>> DRUM_NO_SHEARING = tag("drum/no_shearing");

		/**
		 * Mobs in this tag are immune to the effect of the Charm of the Diva
		 */
		public static final TagKey<EntityType<?>> NOT_CHARMABLE = tag("not_charmable");

		/**
		 * Entities in this tag are immune to damage from the Key of the King's Law
		 */
		public static final TagKey<EntityType<?>> KEY_IMMUNE = tag("key_immune");

		/**
		 * Entities in this tag are immune to damage from the portal bread explosion
		 */
		public static final TagKey<EntityType<?>> PORTAL_BREAD_IMMUNE = tag("portal_bread_immune");

		private static TagKey<EntityType<?>> tag(String name) {
			return TagKey.create(Registries.ENTITY_TYPE, botaniaRL(name));
		}
	}

	public static class Fluids {
		/**
		 * Fluids consumed by the Hydroangeas. (Non-source fluids are ignored.)
		 */
		public static final TagKey<Fluid> HYDROANGEAS_CONSUMABLE = tag("hydroangeas_consumable");
		/**
		 * Fluids consumed by the Thermalily. (Non-source fluids are ignored.)
		 */
		public static final TagKey<Fluid> THERMALILY_CONSUMABLE = tag("thermalily_consumable");

		private static TagKey<Fluid> tag(String name) {
			return TagKey.create(Registries.FLUID, botaniaRL(name));
		}
	}

	public static class Biomes {
		public static final TagKey<Biome> MARIMORPHOSIS_DESERT_BONUS = tag("marimorphosis_desert_bonus");
		public static final TagKey<Biome> MARIMORPHOSIS_FOREST_BONUS = tag("marimorphosis_forest_bonus");
		public static final TagKey<Biome> MARIMORPHOSIS_FUNGAL_BONUS = tag("marimorphosis_fungal_bonus");
		public static final TagKey<Biome> MARIMORPHOSIS_MESA_BONUS = tag("marimorphosis_mesa_bonus");
		public static final TagKey<Biome> MARIMORPHOSIS_MOUNTAIN_BONUS = tag("marimorphosis_mountain_bonus");
		public static final TagKey<Biome> MARIMORPHOSIS_PLAINS_BONUS = tag("marimorphosis_plains_bonus");
		public static final TagKey<Biome> MARIMORPHOSIS_SWAMP_BONUS = tag("marimorphosis_swamp_bonus");
		public static final TagKey<Biome> MARIMORPHOSIS_TAIGA_BONUS = tag("marimorphosis_taiga_bonus");

		public static final TagKey<Biome> ORECHID_STONE_COPPER_BONUS = tag("orechid_stone_copper_bonus");
		public static final TagKey<Biome> ORECHID_STONE_EMERALD_BONUS = tag("orechid_stone_emerald_bonus");
		public static final TagKey<Biome> ORECHID_STONE_GOLD_BONUS = tag("orechid_stone_gold_bonus");

		public static final TagKey<Biome> ORECHID_DEEPSLATE_COPPER_BONUS = tag("orechid_deepslate_copper_bonus");
		public static final TagKey<Biome> ORECHID_DEEPSLATE_EMERALD_BONUS = tag("orechid_deepslate_emerald_bonus");

		/**
		 * Biomes in this tag are eligible for the mystical_flower placed_feature.
		 */
		public static final TagKey<Biome> MYSTICAL_FLOWER_SPAWNLIST = tag("mystical_flower_spawnlist");

		/**
		 * Biomes in this tag are blocked from having the mystical_flower placed_feature.
		 * Overrides mystical_flowers_spawnlist tag.
		 */
		public static final TagKey<Biome> MYSTICAL_FLOWER_BLOCKLIST = tag("mystical_flower_blocklist");

		/**
		 * Biomes in this tag are eligible for the mystical_mushroom placed_feature.
		 */
		public static final TagKey<Biome> MYSTICAL_MUSHROOM_SPAWNLIST = tag("mystical_mushroom_spawnlist");

		/**
		 * Biomes in this tag are blocked from having the mystical_mushroom placed_feature.
		 * Overrides mystical_mushroom_spawnlist tag.
		 */
		public static final TagKey<Biome> MYSTICAL_MUSHROOM_BLOCKLIST = tag("mystical_mushroom_blocklist");

		private static TagKey<Biome> tag(String name) {
			return TagKey.create(Registries.BIOME, botaniaRL(name));
		}
	}

	public static class BannerPatterns {
		public static final TagKey<BannerPattern> PATTERN_ITEM_BOTANIA = tag("pattern_item/botania");
		public static final TagKey<BannerPattern> PATTERN_ITEM_MATERIALS = tag("pattern_item/materials");
		public static final TagKey<BannerPattern> PATTERN_ITEM_SPARK_AUGMENTS = tag("pattern_item/spark_augments");
		public static final TagKey<BannerPattern> PATTERN_ITEM_TOOLS = tag("pattern_item/tools");

		private static TagKey<BannerPattern> tag(String name) {
			return TagKey.create(Registries.BANNER_PATTERN, botaniaRL(name));
		}
	}

	public static class DamageTypes {
		public static final TagKey<DamageType> RING_OF_ODIN_IMMUNE = tag("ring_of_odin_immune");

		private static TagKey<DamageType> tag(String name) {
			return TagKey.create(Registries.DAMAGE_TYPE, botaniaRL(name));
		}
	}

	public static class DataComponentTypes {
		public static final TagKey<DataComponentType<?>> GOURMARYLLIS_RELEVANT = tag("gourmaryllis_relevant");

		private static TagKey<DataComponentType<?>> tag(String name) {
			return TagKey.create(Registries.DATA_COMPONENT_TYPE, botaniaRL(name));
		}
	}
}
