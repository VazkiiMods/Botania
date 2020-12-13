/*
 * This class is distributed as part of the Botania Mod.
 * Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 *
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 */
package vazkii.botania.common.lib;

import net.fabricmc.fabric.api.tag.TagRegistry;
import net.minecraft.block.Block;
import net.minecraft.entity.EntityType;
import net.minecraft.item.Item;
import net.minecraft.tag.BlockTags;
import net.minecraft.tag.EntityTypeTags;
import net.minecraft.tag.ItemTags;
import net.minecraft.tag.Tag;
import net.minecraft.util.DyeColor;
import net.minecraft.util.Identifier;

import java.util.List;
import java.util.function.Function;

import static vazkii.botania.common.lib.ResourceLocationHelper.prefix;

public class ModTags {
	private static <T> Tag.Identified<T> getOrRegister(List<? extends Tag.Identified<T>> list,
			Function<Identifier, Tag.Identified<T>> register,
			Identifier loc) {
		for (Tag.Identified<T> existing : list) {
			if (existing.getId().equals(loc)) {
				return existing;
			}
		}

		return register.apply(loc);
	}

	public static class Items {
		public static final Tag.Identified<Item> DUSTS_MANA = tag("mana_dusts");

		public static final Tag.Identified<Item> GEMS_DRAGONSTONE = tag("dragonstone_gems");
		public static final Tag.Identified<Item> GEMS_MANA_DIAMOND = tag("mana_diamond_gems");

		public static final Tag.Identified<Item> INGOTS_ELEMENTIUM = tag("elementium_ingots");
		public static final Tag.Identified<Item> INGOTS_MANASTEEL = tag("manasteel_ingots");
		public static final Tag.Identified<Item> INGOTS_TERRASTEEL = tag("terrasteel_ingots");

		public static final Tag.Identified<Item> NUGGETS_ELEMENTIUM = tag("elementium_nuggets");
		public static final Tag.Identified<Item> NUGGETS_MANASTEEL = tag("manasteel_nuggets");
		public static final Tag.Identified<Item> NUGGETS_TERRASTEEL = tag("terrasteel_nuggets");

		public static final Tag.Identified<Item> BLOCKS_ELEMENTIUM = tag("elementium_blocks");
		public static final Tag.Identified<Item> BLOCKS_MANASTEEL = tag("manasteel_blocks");
		public static final Tag.Identified<Item> BLOCKS_QUARTZ = commonTag("quartz_blocks");
		public static final Tag.Identified<Item> BLOCKS_TERRASTEEL = tag("terrasteel_blocks");

		public static final Tag.Identified<Item> MYSTICAL_FLOWERS = tag("mystical_flowers");
		public static final Tag.Identified<Item> DOUBLE_MYSTICAL_FLOWERS = tag("double_mystical_flowers");

		public static final Tag.Identified<Item> CONTRIBUTOR_HEADFLOWERS = tag("contributor_headflowers");
		public static final Tag.Identified<Item> SPECIAL_FLOWERS = tag("special_flowers");
		public static final Tag.Identified<Item> MINI_FLOWERS = tag("mini_flowers");
		public static final Tag.Identified<Item> MISC_SPECIAL_FLOWERS = tag("misc_special_flowers");
		public static final Tag.Identified<Item> FUNCTIONAL_SPECIAL_FLOWERS = tag("functional_special_flowers");
		public static final Tag.Identified<Item> GENERATING_SPECIAL_FLOWERS = tag("generating_special_flowers");

		public static final Tag.Identified<Item> FLOATING_FLOWERS = tag("floating_flowers");
		public static final Tag.Identified<Item> MUNDANE_FLOATING_FLOWERS = tag("mundane_floating_flowers");
		public static final Tag.Identified<Item> SPECIAL_FLOATING_FLOWERS = tag("special_floating_flowers");

		public static final Tag.Identified<Item> LENS = tag("lens");
		public static final Tag.Identified<Item> LIVINGROCK = tag("livingrock");
		public static final Tag.Identified<Item> LIVINGWOOD = tag("livingwood");

		public static final Tag.Identified<Item> MAGNET_RING_BLACKLIST = tag("magnet_ring_blacklist");
		public static final Tag.Identified<Item> LOONIUM_BLACKLIST = tag("loonium_blacklist");

		public static final Tag.Identified<Item> DISPOSABLE = tag("disposable");
		public static final Tag.Identified<Item> SEMI_DISPOSABLE = tag("semi_disposable");

		public static final Tag.Identified<Item> PETALS = tag("petals");
		public static final Tag.Identified<Item> PETALS_BLACK = tag("petals/black");
		public static final Tag.Identified<Item> PETALS_BLUE = tag("petals/blue");
		public static final Tag.Identified<Item> PETALS_BROWN = tag("petals/brown");
		public static final Tag.Identified<Item> PETALS_CYAN = tag("petals/cyan");
		public static final Tag.Identified<Item> PETALS_GRAY = tag("petals/gray");
		public static final Tag.Identified<Item> PETALS_GREEN = tag("petals/green");
		public static final Tag.Identified<Item> PETALS_LIGHT_BLUE = tag("petals/light_blue");
		public static final Tag.Identified<Item> PETALS_LIGHT_GRAY = tag("petals/light_gray");
		public static final Tag.Identified<Item> PETALS_LIME = tag("petals/lime");
		public static final Tag.Identified<Item> PETALS_MAGENTA = tag("petals/magenta");
		public static final Tag.Identified<Item> PETALS_ORANGE = tag("petals/orange");
		public static final Tag.Identified<Item> PETALS_PINK = tag("petals/pink");
		public static final Tag.Identified<Item> PETALS_PURPLE = tag("petals/purple");
		public static final Tag.Identified<Item> PETALS_RED = tag("petals/red");
		public static final Tag.Identified<Item> PETALS_WHITE = tag("petals/white");
		public static final Tag.Identified<Item> PETALS_YELLOW = tag("petals/yellow");

		public static final Tag.Identified<Item> RUNES = tag("runes");

		public static final Tag.Identified<Item> RUNES_AIR = tag("runes/air");
		public static final Tag.Identified<Item> RUNES_AUTUMN = tag("runes/autumn");
		public static final Tag.Identified<Item> RUNES_EARTH = tag("runes/earth");
		public static final Tag.Identified<Item> RUNES_ENVY = tag("runes/envy");
		public static final Tag.Identified<Item> RUNES_FIRE = tag("runes/fire");
		public static final Tag.Identified<Item> RUNES_GLUTTONY = tag("runes/gluttony");
		public static final Tag.Identified<Item> RUNES_GREED = tag("runes/greed");
		public static final Tag.Identified<Item> RUNES_LUST = tag("runes/lust");
		public static final Tag.Identified<Item> RUNES_MANA = tag("runes/mana");
		public static final Tag.Identified<Item> RUNES_PRIDE = tag("runes/pride");
		public static final Tag.Identified<Item> RUNES_SLOTH = tag("runes/sloth");
		public static final Tag.Identified<Item> RUNES_SPRING = tag("runes/spring");
		public static final Tag.Identified<Item> RUNES_SUMMER = tag("runes/summer");
		public static final Tag.Identified<Item> RUNES_WATER = tag("runes/water");
		public static final Tag.Identified<Item> RUNES_WINTER = tag("runes/winter");
		public static final Tag.Identified<Item> RUNES_WRATH = tag("runes/wrath");

		public static final Tag.Identified<Item> BURST_VIEWERS = tag("burst_viewers");
		public static final Tag.Identified<Item> TERRA_PICK_BLACKLIST = tag("terra_pick_blacklist");

		public static Tag.Identified<Item> getPetalTag(DyeColor color) {
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

		private static Tag.Identified<Item> tag(String name) {
			return TagRegistry.create(prefix(name), ItemTags::getTagGroup);
		}

		private static Tag.Identified<Item> commonTag(String name) {
			return TagRegistry.create(new Identifier("c", name), ItemTags::getTagGroup);
		}
	}

	public static class Blocks {
		public static final Tag.Identified<Block> MYSTICAL_FLOWERS = tag("mystical_flowers");
		public static final Tag.Identified<Block> DOUBLE_MYSTICAL_FLOWERS = tag("double_mystical_flowers");

		public static final Tag.Identified<Block> SPECIAL_FLOWERS = tag("special_flowers");
		public static final Tag.Identified<Block> MINI_FLOWERS = tag("mini_flowers");
		public static final Tag.Identified<Block> MISC_SPECIAL_FLOWERS = tag("misc_special_flowers");
		public static final Tag.Identified<Block> FUNCTIONAL_SPECIAL_FLOWERS = tag("functional_special_flowers");
		public static final Tag.Identified<Block> GENERATING_SPECIAL_FLOWERS = tag("generating_special_flowers");

		public static final Tag.Identified<Block> FLOATING_FLOWERS = tag("floating_flowers");
		public static final Tag.Identified<Block> MUNDANE_FLOATING_FLOWERS = tag("mundane_floating_flowers");
		public static final Tag.Identified<Block> SPECIAL_FLOATING_FLOWERS = tag("special_floating_flowers");

		public static final Tag.Identified<Block> LIVINGROCK = tag("livingrock");
		public static final Tag.Identified<Block> LIVINGWOOD = tag("livingwood");

		public static final Tag.Identified<Block> BLOCKS_ELEMENTIUM = tag("elementium_blocks");
		public static final Tag.Identified<Block> BLOCKS_MANASTEEL = tag("manasteel_blocks");
		public static final Tag.Identified<Block> BLOCKS_QUARTZ = commonTag("quartz_blocks");
		public static final Tag.Identified<Block> BLOCKS_TERRASTEEL = tag("terrasteel_blocks");

		public static final Tag.Identified<Block> GAIA_BREAK_BLACKLIST = tag("gaia_break_blacklist");
		public static final Tag.Identified<Block> MAGNET_RING_BLACKLIST = tag("magnet_ring_blacklist");

		public static final Tag.Identified<Block> TERRAFORMABLE = tag("terraformable");

		public static final Tag.Identified<Block> CORPOREA_SPARK_OVERRIDE = tag("corporea_spark_override");
		public static final Tag.Identified<Block> TERRA_PLATE_BASE = tag("terra_plate_base");

		public static final Tag.Identified<Block> LAPIS_BLOCKS = commonTag("lapis_blocks");

		private static Tag.Identified<Block> tag(String name) {
			return TagRegistry.create(prefix(name), BlockTags::getTagGroup);
		}

		private static Tag.Identified<Block> commonTag(String name) {
			return TagRegistry.create(new Identifier("c", name), BlockTags::getTagGroup);
		}
	}

	public static class Entities {
		public static final Tag.Identified<EntityType<?>> SHADED_MESA_BLACKLIST = tag("shaded_mesa_blacklist");

		public static final Tag.Identified<EntityType<?>> COCOON_COMMON = tag("cocoon/common");
		public static final Tag.Identified<EntityType<?>> COCOON_RARE = tag("cocoon/rare");
		public static final Tag.Identified<EntityType<?>> COCOON_COMMON_AQUATIC = tag("cocoon/common_aquatic");
		public static final Tag.Identified<EntityType<?>> COCOON_RARE_AQUATIC = tag("cocoon/rare_aquatic");

		private static Tag.Identified<EntityType<?>> tag(String name) {
			return TagRegistry.create(prefix(name), EntityTypeTags::getTagGroup);
		}
	}
}
