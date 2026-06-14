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
import vazkii.botania.common.item.relic.DiceOfFateItem;
import vazkii.botania.data.ItemTagProvider;

import java.util.concurrent.CompletableFuture;

import static vazkii.botania.common.item.BotaniaItems.*;

public class FabricItemTagProvider extends ItemTagProvider {

	private static TagKey<Item> itemTag(ResourceLocation location) {
		return TagKey.create(Registries.ITEM, location);
	}

	public FabricItemTagProvider(PackOutput packOutput, CompletableFuture<HolderLookup.Provider> lookupProvider, CompletableFuture<TagsProvider.TagLookup<Block>> blockTagProvider) {
		super(packOutput, lookupProvider, blockTagProvider);
	}

	@Override
	protected void addTags(HolderLookup.Provider provider) {
		generateAccessoryTags();
		generateCompatTags();
	}

	private void generateAccessoryTags() {
		this.tag(accessory("chest/cape")).add(
				balanceCloak,
				holyCloak,
				invisibilityCloak,
				unholyCloak
		);
		this.tag(accessory("chest/necklace")).add(
				bloodPendant,
				cloudPendant,
				divaCharm,
				goddessCharm,
				icePendant,
				lavaPendant,
				superCloudPendant,
				superLavaPendant,
				thirdEye
		);
		Item[] rings = {
				auraRing,
				auraRingGreater,
				dodgeRing,
				lokiRing,
				magnetRing,
				magnetRingGreater,
				manaRing,
				manaRingGreater,
				miningRing,
				odinRing,
				pixieRing,
				reachRing,
				swapRing,
				thorRing,
				waterRing
		};
		this.tag(accessory("hand/ring")).add(rings);
		this.tag(accessory("offhand/ring")).add(rings);
		this.tag(accessory("head/face")).add(
				itemFinder,
				monocle,
				tinyPlanet
		);
		this.tag(accessory("head/hat")).add(
				flightTiara
		);
		this.tag(accessory("legs/belt")).add(
				knockbackBelt,
				speedUpBelt,
				superTravelBelt,
				travelBelt
		);
		this.tag(accessory("all")).add(
				blackBowtie, blackTie,
				redGlasses, puffyScarf,
				engineerGoggles, eyepatch,
				wickedEyepatch, redRibbons,
				pinkFlowerBud, polkaDottedBows,
				blueButterfly, catEars,
				witchPin, devilTail,
				kamuiEye, googlyEyes,
				fourLeafClover, clockEye,
				unicornHorn, devilHorns,
				hyperPlus, botanistEmblem,
				ancientMask, eerieMask,
				alienAntenna, anaglyphGlasses,
				orangeShades, grouchoGlasses,
				thickEyebrows, lusitanicShield,
				tinyPotatoMask, questgiverMark,
				thinkingHand
		);
	}

	private void generateCompatTags() {
		this.tag(itemTag(ResourceLocation.fromNamespaceAndPath("modern_industrialization", "replicator_blacklist")))
				.add(DiceOfFateItem.RELIC_STACKS.get().stream().map(ItemStack::getItem).toArray(Item[]::new))
				.add(dice, manaTablet, manaRing, manaRingGreater, blackerLotus, blackHoleTalisman, flowerBag,
						spawnerMover, terraPick, BotaniaBlocks.TERRASTEEL_BLOCK.asItem());
	}

	private static TagKey<Item> accessory(String name) {
		return itemTag(ResourceLocation.fromNamespaceAndPath("trinkets", name));
	}
}
