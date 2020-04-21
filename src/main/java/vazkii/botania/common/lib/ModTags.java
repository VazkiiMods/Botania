/*
 * This class is distributed as part of the Botania Mod.
 * Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 *
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 */
package vazkii.botania.common.lib;

import net.minecraft.block.Block;
import net.minecraft.entity.EntityType;
import net.minecraft.item.DyeColor;
import net.minecraft.item.Item;
import net.minecraft.tags.BlockTags;
import net.minecraft.tags.EntityTypeTags;
import net.minecraft.tags.ItemTags;
import net.minecraft.tags.Tag;
import net.minecraft.util.ResourceLocation;

public class ModTags {
	public static class Items {
		public static final Tag<Item> DUSTS_MANA = forgeTag("dusts/mana");

		public static final Tag<Item> GEMS_DRAGONSTONE = forgeTag("gems/dragonstone");
		public static final Tag<Item> GEMS_MANA_DIAMOND = forgeTag("gems/mana_diamond");

		public static final Tag<Item> INGOTS_ELEMENTIUM = forgeTag("ingots/elementium");
		public static final Tag<Item> INGOTS_MANASTEEL = forgeTag("ingots/manasteel");
		public static final Tag<Item> INGOTS_TERRASTEEL = forgeTag("ingots/terrasteel");

		public static final Tag<Item> NUGGETS_ELEMENTIUM = forgeTag("nuggets/elementium");
		public static final Tag<Item> NUGGETS_MANASTEEL = forgeTag("nuggets/manasteel");
		public static final Tag<Item> NUGGETS_TERRASTEEL = forgeTag("nuggets/terrasteel");

		public static final Tag<Item> BLOCKS_ELEMENTIUM = forgeTag("storage_blocks/elementium");
		public static final Tag<Item> BLOCKS_MANASTEEL = forgeTag("storage_blocks/manasteel");
		public static final Tag<Item> BLOCKS_TERRASTEEL = forgeTag("storage_blocks/terrasteel");
		public static final Tag<Item> BLOCKS_QUARTZ = forgeTag("storage_blocks/quartz");

		public static final Tag<Item> MYSTICAL_FLOWERS = tag("mystical_flowers");
		public static final Tag<Item> DOUBLE_MYSTICAL_FLOWERS = tag("double_mystical_flowers");

		public static final Tag<Item> SPECIAL_FLOWERS = tag("special_flowers");
		public static final Tag<Item> MINI_FLOWERS = tag("mini_flowers");
		public static final Tag<Item> MISC_SPECIAL_FLOWERS = tag("misc_special_flowers");
		public static final Tag<Item> FUNCTIONAL_SPECIAL_FLOWERS = tag("functional_special_flowers");
		public static final Tag<Item> GENERATING_SPECIAL_FLOWERS = tag("generating_special_flowers");

		public static final Tag<Item> FLOATING_FLOWERS = tag("floating_flowers");
		public static final Tag<Item> MUNDANE_FLOATING_FLOWERS = tag("mundane_floating_flowers");
		public static final Tag<Item> SPECIAL_FLOATING_FLOWERS = tag("special_floating_flowers");

		public static final Tag<Item> LENS = tag("lens");
		public static final Tag<Item> LIVINGROCK = tag("livingrock");
		public static final Tag<Item> LIVINGWOOD = tag("livingwood");

		public static final Tag<Item> MAGNET_RING_BLACKLIST = tag("magnet_ring_blacklist");
		public static final Tag<Item> LOONIUM_BLACKLIST = tag("loonium_blacklist");

		public static final Tag<Item> SHEARS = forgeTag("shears");

		public static final Tag<Item> DISPOSABLE = tag("disposable");
		public static final Tag<Item> SEMI_DISPOSABLE = tag("semi_disposable");

		public static final Tag<Item> DYES = tag("dyes");

		public static final Tag<Item> PETALS = tag("petals");
		public static final Tag<Item> PETALS_BLACK = tag("petals/black");
		public static final Tag<Item> PETALS_BLUE = tag("petals/blue");
		public static final Tag<Item> PETALS_BROWN = tag("petals/brown");
		public static final Tag<Item> PETALS_CYAN = tag("petals/cyan");
		public static final Tag<Item> PETALS_GRAY = tag("petals/gray");
		public static final Tag<Item> PETALS_GREEN = tag("petals/green");
		public static final Tag<Item> PETALS_LIGHT_BLUE = tag("petals/light_blue");
		public static final Tag<Item> PETALS_LIGHT_GRAY = tag("petals/light_gray");
		public static final Tag<Item> PETALS_LIME = tag("petals/lime");
		public static final Tag<Item> PETALS_MAGENTA = tag("petals/magenta");
		public static final Tag<Item> PETALS_ORANGE = tag("petals/orange");
		public static final Tag<Item> PETALS_PINK = tag("petals/pink");
		public static final Tag<Item> PETALS_PURPLE = tag("petals/purple");
		public static final Tag<Item> PETALS_RED = tag("petals/red");
		public static final Tag<Item> PETALS_WHITE = tag("petals/white");
		public static final Tag<Item> PETALS_YELLOW = tag("petals/yellow");

		public static final Tag<Item> RUNES = tag("runes");

		public static final Tag<Item> RUNES_AIR = tag("runes/air");
		public static final Tag<Item> RUNES_AUTUMN = tag("runes/autumn");
		public static final Tag<Item> RUNES_EARTH = tag("runes/earth");
		public static final Tag<Item> RUNES_ENVY = tag("runes/envy");
		public static final Tag<Item> RUNES_FIRE = tag("runes/fire");
		public static final Tag<Item> RUNES_GLUTTONY = tag("runes/gluttony");
		public static final Tag<Item> RUNES_GREED = tag("runes/greed");
		public static final Tag<Item> RUNES_LUST = tag("runes/lust");
		public static final Tag<Item> RUNES_MANA = tag("runes/mana");
		public static final Tag<Item> RUNES_PRIDE = tag("runes/pride");
		public static final Tag<Item> RUNES_SLOTH = tag("runes/sloth");
		public static final Tag<Item> RUNES_SPRING = tag("runes/spring");
		public static final Tag<Item> RUNES_SUMMER = tag("runes/summer");
		public static final Tag<Item> RUNES_WATER = tag("runes/water");
		public static final Tag<Item> RUNES_WINTER = tag("runes/winter");
		public static final Tag<Item> RUNES_WRATH = tag("runes/wrath");

		public static Tag<Item> getFlowerTag(DyeColor color) {
			switch (color) {
			default:
			case WHITE:
				return PETALS_WHITE;
			case ORANGE:
				return PETALS_ORANGE;
			case MAGENTA:
				return PETALS_MAGENTA;
			case LIGHT_BLUE:
				return PETALS_LIGHT_BLUE;
			case YELLOW:
				return PETALS_YELLOW;
			case LIME:
				return PETALS_LIME;
			case PINK:
				return PETALS_PINK;
			case GRAY:
				return PETALS_GRAY;
			case LIGHT_GRAY:
				return PETALS_LIGHT_GRAY;
			case CYAN:
				return PETALS_CYAN;
			case PURPLE:
				return PETALS_PURPLE;
			case BLUE:
				return PETALS_BLUE;
			case BROWN:
				return PETALS_BROWN;
			case GREEN:
				return PETALS_GREEN;
			case RED:
				return PETALS_RED;
			case BLACK:
				return PETALS_BLACK;
			}
		}

		private static Tag<Item> tag(String name) {
			return new ItemTags.Wrapper(new ResourceLocation(LibMisc.MOD_ID, name));
		}

		private static Tag<Item> forgeTag(String name) {
			return new ItemTags.Wrapper(new ResourceLocation("forge", name));
		}
	}

	public static class Blocks {
		public static final Tag<Block> MYSTICAL_FLOWERS = tag("mystical_flowers");
		public static final Tag<Block> DOUBLE_MYSTICAL_FLOWERS = tag("double_mystical_flowers");

		public static final Tag<Block> SPECIAL_FLOWERS = tag("special_flowers");
		public static final Tag<Block> MINI_FLOWERS = tag("mini_flowers");
		public static final Tag<Block> MISC_SPECIAL_FLOWERS = tag("misc_special_flowers");
		public static final Tag<Block> FUNCTIONAL_SPECIAL_FLOWERS = tag("functional_special_flowers");
		public static final Tag<Block> GENERATING_SPECIAL_FLOWERS = tag("generating_special_flowers");

		public static final Tag<Block> FLOATING_FLOWERS = tag("floating_flowers");
		public static final Tag<Block> MUNDANE_FLOATING_FLOWERS = tag("mundane_floating_flowers");
		public static final Tag<Block> SPECIAL_FLOATING_FLOWERS = tag("special_floating_flowers");

		public static final Tag<Block> LIVINGROCK = tag("livingrock");
		public static final Tag<Block> LIVINGWOOD = tag("livingwood");

		public static final Tag<Block> BLOCKS_ELEMENTIUM = forgeTag("storage_blocks/elementium");
		public static final Tag<Block> BLOCKS_MANASTEEL = forgeTag("storage_blocks/manasteel");
		public static final Tag<Block> BLOCKS_TERRASTEEL = forgeTag("storage_blocks/terrasteel");
		public static final Tag<Block> BLOCKS_QUARTZ = forgeTag("storage_blocks/quartz");

		public static final Tag<Block> GAIA_BREAK_BLACKLIST = tag("gaia_break_blacklist");
		public static final Tag<Block> MAGNET_RING_BLACKLIST = tag("magnet_ring_blacklist");

		public static final Tag<Block> PEBBLE_SOURCES = tag("pebble_sources");
		public static final Tag<Block> TERRAFORMABLE = tag("terraformable");

		private static Tag<Block> tag(String name) {
			return new BlockTags.Wrapper(new ResourceLocation(LibMisc.MOD_ID, name));
		}

		private static Tag<Block> forgeTag(String name) {
			return new BlockTags.Wrapper(new ResourceLocation("forge", name));
		}
	}

	public static class Entities {
		public static final Tag<EntityType<?>> SHADED_MESA_BLACKLIST = tag("shaded_mesa_blacklist");

		private static Tag<EntityType<?>> tag(String name) {
			return new EntityTypeTags.Wrapper(new ResourceLocation(LibMisc.MOD_ID, name));
		}
	}
}
