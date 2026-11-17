/*
 * This class is distributed as part of the Botania Mod.
 * Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 *
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 *
 */

package vazkii.botania.fabric.data.fabric;

import net.minecraft.core.HolderLookup;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.PackOutput;
import net.minecraft.data.tags.TagsProvider;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.Block;

import vazkii.botania.common.block.BotaniaBlocks;
import vazkii.botania.common.item.BotaniaItems;
import vazkii.botania.common.item.relic.DiceOfFateItem;
import vazkii.botania.data.ItemTagProvider;

import java.util.concurrent.CompletableFuture;

public class FabricItemTagProvider extends ItemTagProvider {

	private static TagKey<Item> itemTag(ResourceLocation location) {
		return TagKey.create(Registries.ITEM, location);
	}

	public FabricItemTagProvider(PackOutput output, CompletableFuture<HolderLookup.Provider> lookupProvider,
			CompletableFuture<TagsProvider.TagLookup<Block>> blockTags) {
		super(output, lookupProvider, blockTags);
	}

	@Override
	protected void addTags(HolderLookup.Provider provider) {
		generateAccessoryTags();
		generateCompatTags();
	}

	private void generateAccessoryTags() {
		this.tag(accessory("chest/cape")).add(
				BotaniaItems.CLOAK_OF_BALANCE,
				BotaniaItems.CLOAK_OF_VIRTUE,
				BotaniaItems.INVISIBILITY_CLOAK,
				BotaniaItems.CLOAK_OF_SIN
		);
		this.tag(accessory("chest/necklace")).add(
				BotaniaItems.TAINTED_BLOOD_PENDANT,
				BotaniaItems.CIRRUS_AMULET,
				BotaniaItems.CHARM_OF_THE_DIVA,
				BotaniaItems.BENEVOLENT_GODDESS_CHARM,
				BotaniaItems.SNOWFLAKE_PENDANT,
				BotaniaItems.PYROCLAST_PENDANT,
				BotaniaItems.NIMBUS_AMULET,
				BotaniaItems.CRIMSON_PENDANT,
				BotaniaItems.THIRD_EYE
		);
		Item[] rings = {
				BotaniaItems.BAND_OF_AURA,
				BotaniaItems.GREATER_BAND_OF_AURA,
				BotaniaItems.RING_OF_DEXTEROUS_MOTION,
				BotaniaItems.RING_OF_LOKI,
				BotaniaItems.RING_OF_MAGNETIZATION,
				BotaniaItems.GREATER_RING_OF_MAGNETIZATION,
				BotaniaItems.BAND_OF_MANA,
				BotaniaItems.GREATER_BAND_OF_MANA,
				BotaniaItems.RING_OF_THE_MANTLE,
				BotaniaItems.RING_OF_ODIN,
				BotaniaItems.GREAT_FAIRY_RING,
				BotaniaItems.RING_OF_FAR_REACH,
				BotaniaItems.RING_OF_CORRECTION,
				BotaniaItems.RING_OF_THOR,
				BotaniaItems.RING_OF_CHORDATA
		};
		this.tag(accessory("hand/ring")).add(rings);
		this.tag(accessory("offhand/ring")).add(rings);
		this.tag(accessory("head/face")).add(
				BotaniaItems.THE_SPECTATOR,
				BotaniaItems.MANASEER_MONOCLE,
				BotaniaItems.TINY_PLANET
		);
		this.tag(accessory("head/hat")).add(
				BotaniaItems.FLUGEL_TIARA
		);
		this.tag(accessory("legs/belt")).add(
				BotaniaItems.TECTONIC_GIRDLE,
				BotaniaItems.PLANESTRIDERS_SASH,
				BotaniaItems.GLOBETROTTERS_SASH,
				BotaniaItems.SOJOURNERS_SASH
		);
		this.tag(accessory("all")).add(
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
	}

	private void generateCompatTags() {
		this.tag(itemTag(ResourceLocation.fromNamespaceAndPath("modern_industrialization", "replicator_blacklist")))
				.add(DiceOfFateItem.RELIC_STACKS.get().stream().map(ItemStack::getItem).toArray(Item[]::new))
				.add(
						BotaniaItems.DICE_OF_FATE, BotaniaItems.MANA_TABLET, BotaniaItems.BAND_OF_MANA,
						BotaniaItems.GREATER_BAND_OF_MANA, BotaniaItems.BLACKER_LOTUS, BotaniaItems.BLACK_HOLE_TALISMAN,
						BotaniaItems.FLOWER_POUCH, BotaniaItems.PETAL_POUCH, BotaniaItems.TRINKET_CASE,
						BotaniaItems.LIFE_AGGREGATOR, BotaniaItems.TERRA_SHATTERER,
						BotaniaBlocks.TERRASTEEL_BLOCK.asItem());
	}

	private static TagKey<Item> accessory(String name) {
		return itemTag(ResourceLocation.fromNamespaceAndPath("trinkets", name));
	}
}
