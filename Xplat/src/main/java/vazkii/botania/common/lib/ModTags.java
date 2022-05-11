/*
 * This class is distributed as part of the Botania Mod.
 * Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 *
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 */
package vazkii.botania.common.lib;

import net.minecraft.core.Registry;
import net.minecraft.tags.TagKey;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;

import static vazkii.botania.common.lib.ResourceLocationHelper.prefix;

public class ModTags {
	public static class Items {
		public static final TagKey<Item> DUSTS_MANA = tag("mana_dusts");

		public static final TagKey<Item> GEMS_DRAGONSTONE = tag("dragonstone_gems");
		public static final TagKey<Item> GEMS_MANA_DIAMOND = tag("mana_diamond_gems");

		public static final TagKey<Item> INGOTS_ELEMENTIUM = tag("elementium_ingots");
		public static final TagKey<Item> INGOTS_MANASTEEL = tag("manasteel_ingots");
		public static final TagKey<Item> INGOTS_TERRASTEEL = tag("terrasteel_ingots");

		public static final TagKey<Item> NUGGETS_ELEMENTIUM = tag("elementium_nuggets");
		public static final TagKey<Item> NUGGETS_MANASTEEL = tag("manasteel_nuggets");
		public static final TagKey<Item> NUGGETS_TERRASTEEL = tag("terrasteel_nuggets");

		public static final TagKey<Item> BLOCKS_ELEMENTIUM = tag("elementium_blocks");
		public static final TagKey<Item> BLOCKS_MANASTEEL = tag("manasteel_blocks");
		public static final TagKey<Item> BLOCKS_TERRASTEEL = tag("terrasteel_blocks");

		public static final TagKey<Item> MYSTICAL_FLOWERS = tag("mystical_flowers");
		public static final TagKey<Item> DOUBLE_MYSTICAL_FLOWERS = tag("double_mystical_flowers");

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

		public static final TagKey<Item> LENS = tag("lens");

		/**
		 * Items in this tag cannot be pulled by the magnet rings
		 */
		public static final TagKey<Item> MAGNET_RING_BLACKLIST = tag("magnet_ring_blacklist");
		/**
		 * Items in this tag cannot be rolled by Looniums
		 */
		public static final TagKey<Item> LOONIUM_BLACKLIST = tag("loonium_blacklist");

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

		public static final TagKey<Item> RUNES = tag("runes");

		public static final TagKey<Item> LIVINGWOOD_LOGS = tag("livingwood_logs");
		public static final TagKey<Item> DREAMWOOD_LOGS = tag("dreamwood_logs");
		public static final TagKey<Item> LIVINGWOOD_LOGS_GLIMMERING = tag("glimmering_livingwood_logs");
		public static final TagKey<Item> DREAMWOOD_LOGS_GLIMMERING = tag("glimmering_dreamwood_logs");

		/**
		 * Items in this tag allow wearing players to see bursts through walls and flower radii
		 */
		public static final TagKey<Item> BURST_VIEWERS = tag("burst_viewers");
		/**
		 * Items in this tag cannot give mana to the terra pick
		 */
		public static final TagKey<Item> TERRA_PICK_BLACKLIST = tag("terra_pick_blacklist");
		public static final TagKey<Item> RODS = tag("rods");
		/**
		 * Items in this tag may consume mana
		 */
		public static final TagKey<Item> MANA_USING_ITEMS = tag("mana_using_items");

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
			return TagKey.create(Registry.ITEM_REGISTRY, prefix(name));
		}
	}

	public static class Blocks {
		public static final TagKey<Block> MYSTICAL_FLOWERS = tag("mystical_flowers");
		public static final TagKey<Block> SHINY_FLOWERS = tag("shiny_flowers");
		public static final TagKey<Block> DOUBLE_MYSTICAL_FLOWERS = tag("double_mystical_flowers");

		public static final TagKey<Block> SPECIAL_FLOWERS = tag("special_flowers");
		public static final TagKey<Block> MINI_FLOWERS = tag("mini_flowers");
		public static final TagKey<Block> MISC_SPECIAL_FLOWERS = tag("misc_special_flowers");
		public static final TagKey<Block> FUNCTIONAL_SPECIAL_FLOWERS = tag("functional_special_flowers");
		public static final TagKey<Block> GENERATING_SPECIAL_FLOWERS = tag("generating_special_flowers");

		public static final TagKey<Block> FLOATING_FLOWERS = tag("floating_flowers");
		public static final TagKey<Block> MUNDANE_FLOATING_FLOWERS = tag("mundane_floating_flowers");
		public static final TagKey<Block> SPECIAL_FLOATING_FLOWERS = tag("special_floating_flowers");

		/**
		 * Blocks in this tag can be used in the Enchanter multiblock
		 */
		public static final TagKey<Block> ENCHANTER_FLOWERS = tag("enchanter_flowers");

		public static final TagKey<Block> LIVINGWOOD_LOGS = tag("livingwood_logs");
		public static final TagKey<Block> DREAMWOOD_LOGS = tag("dreamwood_logs");
		public static final TagKey<Block> LIVINGWOOD_LOGS_GLIMMERING = tag("glimmering_livingwood_logs");
		public static final TagKey<Block> DREAMWOOD_LOGS_GLIMMERING = tag("glimmering_dreamwood_logs");

		public static final TagKey<Block> BLOCKS_ELEMENTIUM = tag("elementium_blocks");
		public static final TagKey<Block> BLOCKS_MANASTEEL = tag("manasteel_blocks");
		public static final TagKey<Block> BLOCKS_TERRASTEEL = tag("terrasteel_blocks");

		/**
		 * Blocks in this tag are exempt from the Gaia Guardian's block smash attack
		 */
		public static final TagKey<Block> GAIA_BREAK_BLACKLIST = tag("gaia_break_blacklist");
		/**
		 * Items resting on blocks in this tag cannot be pulled by magnet rings
		 */
		public static final TagKey<Block> MAGNET_RING_BLACKLIST = tag("magnet_ring_blacklist");
		/**
		 * Blocks in this tag cannot be moved by the Laputa Shard
		 */
		public static final TagKey<Block> LAPUTA_IMMOBILE = tag("laputa_immobile");

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
		 * Blocks in this tag are affected by the weight lens even if they don't drop themselves with silk touch.
		 */
		public static final TagKey<Block> WEIGHT_LENS_WHITELIST = tag("weight_lens_whitelist");

		private static TagKey<Block> tag(String name) {
			return TagKey.create(Registry.BLOCK_REGISTRY, prefix(name));
		}
	}

	public static class Entities {
		/**
		 * Entities in this tag cannot be picked up by the Rod of the Shaded Mesa
		 */
		public static final TagKey<EntityType<?>> SHADED_MESA_BLACKLIST = tag("shaded_mesa_blacklist");

		public static final TagKey<EntityType<?>> COCOON_COMMON = tag("cocoon/common");
		public static final TagKey<EntityType<?>> COCOON_RARE = tag("cocoon/rare");
		public static final TagKey<EntityType<?>> COCOON_COMMON_AQUATIC = tag("cocoon/common_aquatic");
		public static final TagKey<EntityType<?>> COCOON_RARE_AQUATIC = tag("cocoon/rare_aquatic");

		private static TagKey<EntityType<?>> tag(String name) {
			return TagKey.create(Registry.ENTITY_TYPE_REGISTRY, prefix(name));
		}
	}
}
