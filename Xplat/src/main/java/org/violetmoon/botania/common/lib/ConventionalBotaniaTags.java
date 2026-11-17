/*
 * This class is distributed as part of the Botania Mod.
 * Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 *
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 *
 */

package vazkii.botania.common.lib;

import net.minecraft.core.Registry;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;

import vazkii.botania.common.helper.ColorHelper;

import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

public class ConventionalBotaniaTags {

	public static <T> TagKey<T> createSuffixedTag(TagKey<T> baseTag, String suffix) {
		return TagKey.create(baseTag.registry(), baseTag.location().withSuffix("/" + suffix));
	}

	private static <T> TagKey<T> conventionalTag(ResourceKey<Registry<T>> registry, String path) {
		return TagKey.create(registry, ResourceLocation.fromNamespaceAndPath("c", path));
	}

	public static class Blocks {
		// conventional block tags in Fabric/Neoforge
		private static final TagKey<Block> COBBLESTONES = conventionalTag(Registries.BLOCK, "cobblestones");
		private static final TagKey<Block> GLASS_BLOCKS = conventionalTag(Registries.BLOCK, "glass_blocks");
		private static final TagKey<Block> GLASS_PANES = conventionalTag(Registries.BLOCK, "glass_panes");
		private static final TagKey<Block> STORAGE_BLOCKS = conventionalTag(Registries.BLOCK, "storage_blocks");

		// Cobblestones
		public static final TagKey<Block> METAMORPHIC_COBBLESTONES = createSuffixedTag(COBBLESTONES, "metamorphic");

		public static final TagKey<Block> FUCHSITE_COBBLESTONES = createSuffixedTag(METAMORPHIC_COBBLESTONES, "fuchsite");
		public static final TagKey<Block> TALC_COBBLESTONES = createSuffixedTag(METAMORPHIC_COBBLESTONES, "talc");
		public static final TagKey<Block> GNEISS_COBBLESTONES = createSuffixedTag(METAMORPHIC_COBBLESTONES, "gneiss");
		public static final TagKey<Block> MYCELITE_COBBLESTONES = createSuffixedTag(METAMORPHIC_COBBLESTONES, "mycelite");
		public static final TagKey<Block> CATACLASITE_COBBLESTONES = createSuffixedTag(METAMORPHIC_COBBLESTONES, "cataclasite");
		public static final TagKey<Block> SOLITE_COBBLESTONES = createSuffixedTag(METAMORPHIC_COBBLESTONES, "solite");
		public static final TagKey<Block> LUNITE_COBBLESTONES = createSuffixedTag(METAMORPHIC_COBBLESTONES, "lunite");
		public static final TagKey<Block> ROSY_TALC_COBBLESTONES = createSuffixedTag(METAMORPHIC_COBBLESTONES, "rosy_talc");

		// Glass
		public static final TagKey<Block> MANA_GLASS_BLOCKS = createSuffixedTag(GLASS_BLOCKS, "mana");
		public static final TagKey<Block> MANA_GLASS_PANES = createSuffixedTag(GLASS_PANES, "mana");

		// Storage blocks
		public static final TagKey<Block> MANASTEEL_STORAGE_BLOCKS = createSuffixedTag(STORAGE_BLOCKS, "manasteel");
		public static final TagKey<Block> TERRASTEEL_STORAGE_BLOCKS = createSuffixedTag(STORAGE_BLOCKS, "terrasteel");
		public static final TagKey<Block> ELEMENTIUM_STORAGE_BLOCKS = createSuffixedTag(STORAGE_BLOCKS, "elementium");
		public static final TagKey<Block> MANA_DIAMOND_STORAGE_BLOCKS = createSuffixedTag(STORAGE_BLOCKS, "mana_diamond");
		public static final TagKey<Block> DRAGONSTONE_STORAGE_BLOCKS = createSuffixedTag(STORAGE_BLOCKS, "dragonstone");
		public static final TagKey<Block> BLAZE_STORAGE_BLOCKS = createSuffixedTag(STORAGE_BLOCKS, "blaze");
		public static final TagKey<Block> PETAL_STORAGE_BLOCKS = createSuffixedTag(STORAGE_BLOCKS, "petal");
		public static final Map<DyeColor, TagKey<Block>> PETAL_STORAGE_BLOCKS_BY_COLOR = ColorHelper.supportedColors()
				.collect(Collectors.toUnmodifiableMap(Function.identity(),
						dyeColor -> createSuffixedTag(PETAL_STORAGE_BLOCKS, dyeColor.getSerializedName())));

	}

	public static class Items {
		// conventional item tags in Fabric/Neoforge
		private static final TagKey<Item> BUCKETS = conventionalTag(Registries.ITEM, "buckets");
		private static final TagKey<Item> COBBLESTONES = conventionalTag(Registries.ITEM, "cobblestones");
		private static final TagKey<Item> DUSTS = conventionalTag(Registries.ITEM, "dusts");
		private static final TagKey<Item> GEMS = conventionalTag(Registries.ITEM, "gems");
		private static final TagKey<Item> GLASS_BLOCKS = conventionalTag(Registries.ITEM, "glass_blocks");
		private static final TagKey<Item> GLASS_PANES = conventionalTag(Registries.ITEM, "glass_panes");
		private static final TagKey<Item> INGOTS = conventionalTag(Registries.ITEM, "ingots");
		private static final TagKey<Item> NUGGETS = conventionalTag(Registries.ITEM, "nuggets");
		private static final TagKey<Item> STORAGE_BLOCKS = conventionalTag(Registries.ITEM, "storage_blocks");
		private static final TagKey<Item> RODS = conventionalTag(Registries.ITEM, "rods");

		// Buckets
		public static final TagKey<Item> EXTRAPOLATING_BUCKETS = createSuffixedTag(BUCKETS, "extrapolating");

		// Cobblestones
		public static final TagKey<Item> METAMORPHIC_COBBLESTONES = createSuffixedTag(COBBLESTONES, "metamorphic");

		public static final TagKey<Item> FUCHSITE_COBBLESTONES = createSuffixedTag(METAMORPHIC_COBBLESTONES, "fuchsite");
		public static final TagKey<Item> TALC_COBBLESTONES = createSuffixedTag(METAMORPHIC_COBBLESTONES, "talc");
		public static final TagKey<Item> GNEISS_COBBLESTONES = createSuffixedTag(METAMORPHIC_COBBLESTONES, "gneiss");
		public static final TagKey<Item> MYCELITE_COBBLESTONES = createSuffixedTag(METAMORPHIC_COBBLESTONES, "mycelite");
		public static final TagKey<Item> CATACLASITE_COBBLESTONES = createSuffixedTag(METAMORPHIC_COBBLESTONES, "cataclasite");
		public static final TagKey<Item> SOLITE_COBBLESTONES = createSuffixedTag(METAMORPHIC_COBBLESTONES, "solite");
		public static final TagKey<Item> LUNITE_COBBLESTONES = createSuffixedTag(METAMORPHIC_COBBLESTONES, "lunite");
		public static final TagKey<Item> ROSY_TALC_COBBLESTONES = createSuffixedTag(METAMORPHIC_COBBLESTONES, "rosy_talc");

		// Dusts
		public static final TagKey<Item> MANA_DUSTS = createSuffixedTag(DUSTS, "mana");
		public static final TagKey<Item> PIXIE_DUSTS = createSuffixedTag(DUSTS, "pixie");

		// Gems
		public static final TagKey<Item> MANA_DIAMOND_GEMS = createSuffixedTag(GEMS, "mana_diamond");
		public static final TagKey<Item> MANA_PEARL_GEMS = createSuffixedTag(GEMS, "mana_pearl");
		public static final TagKey<Item> MANA_QUARTZ_GEMS = createSuffixedTag(GEMS, "mana_quartz");
		public static final TagKey<Item> DARK_QUARTZ_GEMS = createSuffixedTag(GEMS, "smokey_quartz");
		public static final TagKey<Item> BLAZE_QUARTZ_GEMS = createSuffixedTag(GEMS, "blaze_quartz");
		public static final TagKey<Item> LAVENDER_QUARTZ_GEMS = createSuffixedTag(GEMS, "lavender_quartz");
		public static final TagKey<Item> RED_QUARTZ_GEMS = createSuffixedTag(GEMS, "red_quartz");
		public static final TagKey<Item> ELVEN_QUARTZ_GEMS = createSuffixedTag(GEMS, "elven_quartz");
		public static final TagKey<Item> SUNNY_QUARTZ_GEMS = createSuffixedTag(GEMS, "sunny_quartz");
		public static final TagKey<Item> DRAGONSTONE_GEMS = createSuffixedTag(GEMS, "dragonstone");

		// Glass
		public static final TagKey<Item> MANA_GLASS_BLOCKS = createSuffixedTag(GLASS_BLOCKS, "mana");
		public static final TagKey<Item> MANA_GLASS_PANES = createSuffixedTag(GLASS_PANES, "mana");

		// Ingots
		public static final TagKey<Item> MANASTEEL_INGOTS = createSuffixedTag(INGOTS, "manasteel");
		public static final TagKey<Item> TERRASTEEL_INGOTS = createSuffixedTag(INGOTS, "terrasteel");
		public static final TagKey<Item> ELEMENTIUM_INGOTS = createSuffixedTag(INGOTS, "elementium");
		public static final TagKey<Item> GAIA_INGOTS = createSuffixedTag(INGOTS, "gaia");

		// Nuggets
		public static final TagKey<Item> MANASTEEL_NUGGETS = createSuffixedTag(NUGGETS, "manasteel");
		public static final TagKey<Item> TERRASTEEL_NUGGETS = createSuffixedTag(NUGGETS, "terrasteel");
		public static final TagKey<Item> ELEMENTIUM_NUGGETS = createSuffixedTag(NUGGETS, "elementium");

		// Rods
		public static final TagKey<Item> LIVINGWOOD_RODS = createSuffixedTag(RODS, "livingwood");
		public static final TagKey<Item> DREAMWOOD_RODS = createSuffixedTag(RODS, "dreamwood");

		// Storage blocks
		public static final TagKey<Item> MANASTEEL_STORAGE_BLOCKS = createSuffixedTag(STORAGE_BLOCKS, "manasteel");
		public static final TagKey<Item> TERRASTEEL_STORAGE_BLOCKS = createSuffixedTag(STORAGE_BLOCKS, "terrasteel");
		public static final TagKey<Item> ELEMENTIUM_STORAGE_BLOCKS = createSuffixedTag(STORAGE_BLOCKS, "elementium");
		public static final TagKey<Item> MANA_DIAMOND_STORAGE_BLOCKS = createSuffixedTag(STORAGE_BLOCKS, "mana_diamond");
		public static final TagKey<Item> DRAGONSTONE_STORAGE_BLOCKS = createSuffixedTag(STORAGE_BLOCKS, "dragonstone");
		public static final TagKey<Item> BLAZE_STORAGE_BLOCKS = createSuffixedTag(STORAGE_BLOCKS, "blaze");
		public static final TagKey<Item> PETAL_STORAGE_BLOCKS = createSuffixedTag(STORAGE_BLOCKS, "petal");
		public static final Map<DyeColor, TagKey<Item>> PETAL_STORAGE_BLOCKS_BY_COLOR = ColorHelper.supportedColors()
				.collect(Collectors.toUnmodifiableMap(Function.identity(),
						dyeColor -> createSuffixedTag(PETAL_STORAGE_BLOCKS, dyeColor.getSerializedName())));

		// TODO: maybe tags for rods, horns and similar tools?

	}

}
