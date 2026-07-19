/*
 * This class is distributed as part of the Botania Mod.
 * Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 *
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 */

package vazkii.botania.neoforge.data;

import net.minecraft.core.HolderLookup;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.PackOutput;
import net.minecraft.data.tags.ItemTagsProvider;
import net.minecraft.data.tags.TagsProvider;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.ItemTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;

import org.jetbrains.annotations.NotNull;

import vazkii.botania.api.BotaniaAPI;
import vazkii.botania.common.item.BotaniaItems;

import java.util.concurrent.CompletableFuture;

public class NeoForgeItemTagProvider extends ItemTagsProvider {
	public NeoForgeItemTagProvider(PackOutput output, CompletableFuture<HolderLookup.Provider> lookupProvider,
			CompletableFuture<TagsProvider.TagLookup<Block>> blockTags) {
		super(output, lookupProvider, blockTags, BotaniaAPI.MODID, null);
	}

	@NotNull
	@Override
	public String getName() {
		return "Botania item tags (NeoForge-specific)";
	}

	@Override
	protected void addTags(HolderLookup.Provider provider) {
		tag(accessory("belt")).add(
				BotaniaItems.TECTONIC_GIRDLE, BotaniaItems.PLANESTRIDERS_SASH, BotaniaItems.GLOBETROTTERS_SASH,
				BotaniaItems.SOJOURNERS_SASH);
		tag(accessory("body")).add(
				BotaniaItems.CLOAK_OF_BALANCE, BotaniaItems.CLOAK_OF_VIRTUE, BotaniaItems.INVISIBILITY_CLOAK,
				BotaniaItems.THIRD_EYE, BotaniaItems.CLOAK_OF_SIN);
		tag(accessory("charm")).add(
				BotaniaItems.CHARM_OF_THE_DIVA, BotaniaItems.BENEVOLENT_GODDESS_CHARM, BotaniaItems.MANASEER_MONOCLE,
				BotaniaItems.TINY_PLANET);
		tag(accessory("head")).add(BotaniaItems.FLUEGEL_TIARA, BotaniaItems.THE_SPECTATOR);
		tag(accessory("necklace")).add(
				BotaniaItems.TAINTED_BLOOD_PENDANT, BotaniaItems.CIRRUS_AMULET, BotaniaItems.SNOWFLAKE_PENDANT,
				BotaniaItems.PYROCLAST_PENDANT, BotaniaItems.NIMBUS_AMULET, BotaniaItems.CRIMSON_PENDANT);
		tag(accessory("ring")).add(
				BotaniaItems.BAND_OF_AURA, BotaniaItems.GREATER_BAND_OF_AURA, BotaniaItems.RING_OF_DEXTEROUS_MOTION,
				BotaniaItems.RING_OF_LOKI, BotaniaItems.RING_OF_MAGNETIZATION,
				BotaniaItems.GREATER_RING_OF_MAGNETIZATION, BotaniaItems.BAND_OF_MANA,
				BotaniaItems.GREATER_BAND_OF_MANA, BotaniaItems.RING_OF_THE_MANTLE, BotaniaItems.RING_OF_ODIN,
				BotaniaItems.GREAT_FAIRY_RING, BotaniaItems.RING_OF_FAR_REACH, BotaniaItems.RING_OF_CORRECTION,
				BotaniaItems.RING_OF_THOR, BotaniaItems.RING_OF_CHORDATA);
		tag(accessory("curio")).add(
				BotaniaItems.COSMETIC_BLACK_BOWTIE, BotaniaItems.COSMETIC_BLACK_TIE,
				BotaniaItems.COSMETIC_RED_GLASSES, BotaniaItems.COSMETIC_PUFFY_SCARF,
				BotaniaItems.COSMETIC_ENGINEER_GOGGLES, BotaniaItems.COSMETIC_EYEPATCH,
				BotaniaItems.COSMETIC_WICKED_EYEPATCH, BotaniaItems.COSMETIC_RED_RIBBONS,
				BotaniaItems.COSMETIC_PINK_FLOWER_BUD, BotaniaItems.COSMETIC_POLKA_DOTTED_BOWS,
				BotaniaItems.COSMETIC_BLUE_BUTTERFLY, BotaniaItems.COSMETIC_CAT_EARS,
				BotaniaItems.COSMETIC_WITCH_PIN, BotaniaItems.COSMETIC_DEVIL_TAIL,
				BotaniaItems.COSMETIC_KAMUI_EYE, BotaniaItems.COSMETIC_GOOGLY_EYES,
				BotaniaItems.COSMETIC_FOUR_LEAFED_CLOVER, BotaniaItems.COSMETIC_CLOCK_EYE,
				BotaniaItems.COSMETIC_UNICORN_HORN, BotaniaItems.COSMETIC_DEVIL_HORNS,
				BotaniaItems.COSMETIC_HYPER_PLUS, BotaniaItems.COSMETIC_BOTANIST_EMBLEM,
				BotaniaItems.COSMETIC_ANCIENT_MASK, BotaniaItems.COSMETIC_EERIE_MASK,
				BotaniaItems.COSMETIC_ALIEN_ANTENNA, BotaniaItems.COSMETIC_ANAGLYPH_GLASSES,
				BotaniaItems.COSMETIC_ORANGE_SHADES, BotaniaItems.COSMETIC_GROUCHO_GLASSES,
				BotaniaItems.COSMETIC_THICK_EYEBROWS, BotaniaItems.COSMETIC_LUSITANIC_SHIELD,
				BotaniaItems.COSMETIC_TINY_POTATO_MASK, BotaniaItems.COSMETIC_QUESTGIVER_MARK,
				BotaniaItems.COSMETIC_THINKING_HAND
		);

		tag(TagKey.create(Registries.ITEM,
				ResourceLocation.fromNamespaceAndPath("quark", "big_harvesting_hoes")))
				.add(BotaniaItems.ELEMENTIUM_HOE);

		tag(TagKey.create(Registries.ITEM, ResourceLocation.fromNamespaceAndPath("quark", "reacharound_able")))
				.add(BotaniaItems.ROD_OF_THE_LANDS, BotaniaItems.ROD_OF_THE_HIGHLANDS, BotaniaItems.ROD_OF_THE_DEPTHS,
						BotaniaItems.BLACK_HOLE_TALISMAN);
	}

	private static TagKey<Item> accessory(String name) {
		return ItemTags.create(ResourceLocation.fromNamespaceAndPath("curios", name));
	}
}
