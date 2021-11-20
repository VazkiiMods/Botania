/*
 * This class is distributed as part of the Botania Mod.
 * Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 *
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 */
package vazkii.botania.common.lib;

import net.fabricmc.fabric.api.tag.TagFactory;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.Tag;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;

import static vazkii.botania.common.lib.ResourceLocationHelper.prefix;

public class ModTags {
	public static class Items {
		public static final Tag.Named<Item> DUSTS_MANA = tag("mana_dusts");

		public static final Tag.Named<Item> GEMS_DRAGONSTONE = tag("dragonstone_gems");
		public static final Tag.Named<Item> GEMS_MANA_DIAMOND = tag("mana_diamond_gems");

		public static final Tag.Named<Item> INGOTS_ELEMENTIUM = tag("elementium_ingots");
		public static final Tag.Named<Item> INGOTS_MANASTEEL = tag("manasteel_ingots");
		public static final Tag.Named<Item> INGOTS_TERRASTEEL = tag("terrasteel_ingots");

		public static final Tag.Named<Item> NUGGETS_ELEMENTIUM = tag("elementium_nuggets");
		public static final Tag.Named<Item> NUGGETS_MANASTEEL = tag("manasteel_nuggets");
		public static final Tag.Named<Item> NUGGETS_TERRASTEEL = tag("terrasteel_nuggets");

		public static final Tag.Named<Item> BLOCKS_ELEMENTIUM = tag("elementium_blocks");
		public static final Tag.Named<Item> BLOCKS_MANASTEEL = tag("manasteel_blocks");
		public static final Tag.Named<Item> BLOCKS_QUARTZ = commonTag("quartz_blocks");
		public static final Tag.Named<Item> BLOCKS_TERRASTEEL = tag("terrasteel_blocks");

		public static final Tag.Named<Item> MYSTICAL_FLOWERS = tag("mystical_flowers");
		public static final Tag.Named<Item> DOUBLE_MYSTICAL_FLOWERS = tag("double_mystical_flowers");

		/**
		 * Items in this tag can be specified as contributor headflowers
		 */
		public static final Tag.Named<Item> CONTRIBUTOR_HEADFLOWERS = tag("contributor_headflowers");
		public static final Tag.Named<Item> SPECIAL_FLOWERS = tag("special_flowers");
		public static final Tag.Named<Item> MINI_FLOWERS = tag("mini_flowers");
		public static final Tag.Named<Item> MISC_SPECIAL_FLOWERS = tag("misc_special_flowers");
		public static final Tag.Named<Item> FUNCTIONAL_SPECIAL_FLOWERS = tag("functional_special_flowers");
		public static final Tag.Named<Item> GENERATING_SPECIAL_FLOWERS = tag("generating_special_flowers");

		public static final Tag.Named<Item> FLOATING_FLOWERS = tag("floating_flowers");
		public static final Tag.Named<Item> MUNDANE_FLOATING_FLOWERS = tag("mundane_floating_flowers");
		public static final Tag.Named<Item> SPECIAL_FLOATING_FLOWERS = tag("special_floating_flowers");

		public static final Tag.Named<Item> LENS = tag("lens");

		/**
		 * Items in this tag cannot be pulled by the magnet rings
		 */
		public static final Tag.Named<Item> MAGNET_RING_BLACKLIST = tag("magnet_ring_blacklist");
		/**
		 * Items in this tag cannot be rolled by Looniums
		 */
		public static final Tag.Named<Item> LOONIUM_BLACKLIST = tag("loonium_blacklist");

		/**
		 * Items in this tag are voided by the Elementium Pick
		 */
		public static final Tag.Named<Item> DISPOSABLE = tag("disposable");
		/**
		 * Items in this tag are voided by the Elementium Pick when not shifting
		 */
		public static final Tag.Named<Item> SEMI_DISPOSABLE = tag("semi_disposable");

		public static final Tag.Named<Item> PETALS = tag("petals");
		public static final Tag.Named<Item> PETALS_BLACK = tag("petals/black");
		public static final Tag.Named<Item> PETALS_BLUE = tag("petals/blue");
		public static final Tag.Named<Item> PETALS_BROWN = tag("petals/brown");
		public static final Tag.Named<Item> PETALS_CYAN = tag("petals/cyan");
		public static final Tag.Named<Item> PETALS_GRAY = tag("petals/gray");
		public static final Tag.Named<Item> PETALS_GREEN = tag("petals/green");
		public static final Tag.Named<Item> PETALS_LIGHT_BLUE = tag("petals/light_blue");
		public static final Tag.Named<Item> PETALS_LIGHT_GRAY = tag("petals/light_gray");
		public static final Tag.Named<Item> PETALS_LIME = tag("petals/lime");
		public static final Tag.Named<Item> PETALS_MAGENTA = tag("petals/magenta");
		public static final Tag.Named<Item> PETALS_ORANGE = tag("petals/orange");
		public static final Tag.Named<Item> PETALS_PINK = tag("petals/pink");
		public static final Tag.Named<Item> PETALS_PURPLE = tag("petals/purple");
		public static final Tag.Named<Item> PETALS_RED = tag("petals/red");
		public static final Tag.Named<Item> PETALS_WHITE = tag("petals/white");
		public static final Tag.Named<Item> PETALS_YELLOW = tag("petals/yellow");

		public static final Tag.Named<Item> RUNES = tag("runes");

		public static final Tag.Named<Item> LIVINGWOOD_LOGS = tag("livingwood_logs");
		public static final Tag.Named<Item> DREAMWOOD_LOGS = tag("dreamwood_logs");
		public static final Tag.Named<Item> LIVINGWOOD_LOGS_GLIMMERING = tag("glimmering_livingwood_logs");
		public static final Tag.Named<Item> DREAMWOOD_LOGS_GLIMMERING = tag("glimmering_dreamwood_logs");

		/**
		 * Items in this tag allow wearing players to see bursts through walls and flower radii
		 */
		public static final Tag.Named<Item> BURST_VIEWERS = tag("burst_viewers");
		/**
		 * Items in this tag cannot give mana to the terra pick
		 */
		public static final Tag.Named<Item> TERRA_PICK_BLACKLIST = tag("terra_pick_blacklist");
		public static final Tag.Named<Item> MUSHROOMS = commonTag("mushrooms");
		public static final Tag.Named<Item> RODS = tag("rods");
		/**
		 * Items in this tag may consume mana
		 */
		public static final Tag.Named<Item> MANA_USING_ITEMS = tag("mana_using_items");

		public static Tag.Named<Item> getPetalTag(DyeColor color) {
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

		private static Tag.Named<Item> tag(String name) {
			return TagFactory.ITEM.create(prefix(name));
		}

		private static Tag.Named<Item> commonTag(String name) {
			return TagFactory.ITEM.create(new ResourceLocation("c", name));
		}
	}

	public static class Blocks {
		public static final Tag.Named<Block> MYSTICAL_FLOWERS = tag("mystical_flowers");
		public static final Tag.Named<Block> SHINY_FLOWERS = tag("shiny_flowers");
		public static final Tag.Named<Block> DOUBLE_MYSTICAL_FLOWERS = tag("double_mystical_flowers");

		public static final Tag.Named<Block> SPECIAL_FLOWERS = tag("special_flowers");
		public static final Tag.Named<Block> MINI_FLOWERS = tag("mini_flowers");
		public static final Tag.Named<Block> MISC_SPECIAL_FLOWERS = tag("misc_special_flowers");
		public static final Tag.Named<Block> FUNCTIONAL_SPECIAL_FLOWERS = tag("functional_special_flowers");
		public static final Tag.Named<Block> GENERATING_SPECIAL_FLOWERS = tag("generating_special_flowers");

		public static final Tag.Named<Block> FLOATING_FLOWERS = tag("floating_flowers");
		public static final Tag.Named<Block> MUNDANE_FLOATING_FLOWERS = tag("mundane_floating_flowers");
		public static final Tag.Named<Block> SPECIAL_FLOATING_FLOWERS = tag("special_floating_flowers");

		/**
		 * Blocks in this tag can be used in the Enchanter multiblock
		 */
		public static final Tag.Named<Block> ENCHANTER_FLOWERS = tag("enchanter_flowers");

		public static final Tag.Named<Block> LIVINGWOOD_LOGS = tag("livingwood_logs");
		public static final Tag.Named<Block> DREAMWOOD_LOGS = tag("dreamwood_logs");
		public static final Tag.Named<Block> LIVINGWOOD_LOGS_GLIMMERING = tag("glimmering_livingwood_logs");
		public static final Tag.Named<Block> DREAMWOOD_LOGS_GLIMMERING = tag("glimmering_dreamwood_logs");

		public static final Tag.Named<Block> BLOCKS_ELEMENTIUM = tag("elementium_blocks");
		public static final Tag.Named<Block> BLOCKS_MANASTEEL = tag("manasteel_blocks");
		public static final Tag.Named<Block> BLOCKS_QUARTZ = commonTag("quartz_blocks");
		public static final Tag.Named<Block> BLOCKS_TERRASTEEL = tag("terrasteel_blocks");

		/**
		 * Blocks in this tag are exempt from the Gaia Guardian's block smash attack
		 */
		public static final Tag.Named<Block> GAIA_BREAK_BLACKLIST = tag("gaia_break_blacklist");
		/**
		 * Items resting on blocks in this tag cannot be pulled by magnet rings
		 */
		public static final Tag.Named<Block> MAGNET_RING_BLACKLIST = tag("magnet_ring_blacklist");
		/**
		 * Blocks in this tag cannot be moved by the Laputa Shard
		 */
		public static final Tag.Named<Block> LAPUTA_IMMOBILE = tag("laputa_immobile");

		/**
		 * Blocks in this tag can be removed by the Rod of Terra Firma
		 */
		public static final Tag.Named<Block> TERRAFORMABLE = tag("terraformable");

		/**
		 * Blocks in this tag can have corporea sparks placed on them even though they have no inventory
		 */
		public static final Tag.Named<Block> CORPOREA_SPARK_OVERRIDE = tag("corporea_spark_override");
		/**
		 * Blocks in this tag can sub for livingrock in the terra plate multiblock
		 */
		public static final Tag.Named<Block> TERRA_PLATE_BASE = tag("terra_plate_base");
		public static final Tag.Named<Block> GHOST_RAIL_BARRIER = tag("ghost_rail_barrier");

		public static final Tag.Named<Block> LAPIS_BLOCKS = commonTag("lapis_blocks");

		public static final Tag.Named<Block> ORES = commonTag("ores");
		public static final Tag.Named<Block> MUSHROOMS = commonTag("mushrooms");

		private static Tag.Named<Block> tag(String name) {
			return TagFactory.BLOCK.create(prefix(name));
		}

		private static Tag.Named<Block> commonTag(String name) {
			return TagFactory.BLOCK.create(new ResourceLocation("c", name));
		}
	}

	public static class Entities {
		/**
		 * Entities in this tag cannot be picked up by the Rod of the Shaded Mesa
		 */
		public static final Tag.Named<EntityType<?>> SHADED_MESA_BLACKLIST = tag("shaded_mesa_blacklist");

		public static final Tag.Named<EntityType<?>> COCOON_COMMON = tag("cocoon/common");
		public static final Tag.Named<EntityType<?>> COCOON_RARE = tag("cocoon/rare");
		public static final Tag.Named<EntityType<?>> COCOON_COMMON_AQUATIC = tag("cocoon/common_aquatic");
		public static final Tag.Named<EntityType<?>> COCOON_RARE_AQUATIC = tag("cocoon/rare_aquatic");

		private static Tag.Named<EntityType<?>> tag(String name) {
			return TagFactory.ENTITY_TYPE.create(prefix(name));
		}
	}
}
