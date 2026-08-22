/*
 * This class is distributed as part of the Botania Mod.
 * Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 *
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 */

package vazkii.botania.common.capabilities;

import net.minecraft.world.item.Items;
import net.minecraft.world.level.ItemLike;

import vazkii.botania.api.capability.registration.ApiIdRegistration;
import vazkii.botania.api.capability.registration.ApiProviderRegistration;
import vazkii.botania.api.capability.registration.ItemRegistrationNoContext;
import vazkii.botania.api.capability.registration.ItemRegistrationWithContext;
import vazkii.botania.api.item.AvatarWieldable;
import vazkii.botania.api.item.BlockProvider;
import vazkii.botania.api.item.CoordBoundItem;
import vazkii.botania.api.item.HourglassMaterial;
import vazkii.botania.api.item.Relic;
import vazkii.botania.api.mana.ManaItem;
import vazkii.botania.common.impl.mana.DefaultManaItemImpl;
import vazkii.botania.common.item.BlackHoleTalismanItem;
import vazkii.botania.common.item.BotaniaItems;
import vazkii.botania.common.item.EnderHandItem;
import vazkii.botania.common.item.ManaMirrorItem;
import vazkii.botania.common.item.WandOfTheForestItem;
import vazkii.botania.common.item.relic.DiceOfFateItem;
import vazkii.botania.common.item.relic.EyeOfTheFlugelItem;
import vazkii.botania.common.item.relic.FruitOfGrisaiaItem;
import vazkii.botania.common.item.relic.KeyOfTheKingsLawItem;
import vazkii.botania.common.item.relic.RingOfLokiItem;
import vazkii.botania.common.item.relic.RingOfOdinItem;
import vazkii.botania.common.item.relic.RingOfThorItem;
import vazkii.botania.common.item.rod.BifrostRodItem;
import vazkii.botania.common.item.rod.DepthsRodItem;
import vazkii.botania.common.item.rod.HellsRodItem;
import vazkii.botania.common.item.rod.LandsRodItem;
import vazkii.botania.common.item.rod.PlentifulMantleRodItem;
import vazkii.botania.common.item.rod.SkiesRodItem;
import vazkii.botania.common.item.rod.UnstableReservoirRodItem;

import java.util.List;

public final class ItemCapabilities {

	public static void registerLookups(ApiIdRegistration registration) {
		registration.register(AvatarWieldable.LOOKUP);
		registration.register(BlockProvider.LOOKUP);
		registration.register(CoordBoundItem.LOOKUP);
		registration.register(HourglassMaterial.LOOKUP);
		registration.register(ManaItem.LOOKUP);
		registration.register(Relic.LOOKUP);
	}

	public static void registerProviders(ApiProviderRegistration registration) {
		registration.register(AvatarWieldable.LOOKUP, List.of(
				itemApi(BifrostRodItem.AvatarBehavior::new, BotaniaItems.ROD_OF_THE_BIFROST),
				itemApi(HellsRodItem.AvatarBehavior::new, BotaniaItems.ROD_OF_THE_HELLS),
				itemApi(LandsRodItem.AvatarBehavior::new, BotaniaItems.ROD_OF_THE_LANDS),
				itemApi(PlentifulMantleRodItem.AvatarBehavior::new, BotaniaItems.ROD_OF_THE_PLENTIFUL_MANTLE),
				itemApi(SkiesRodItem.AvatarBehavior::new, BotaniaItems.ROD_OF_THE_SKIES),
				itemApi(UnstableReservoirRodItem.AvatarBehavior::new, BotaniaItems.ROD_OF_THE_UNSTABLE_RESERVOIR)
		));

		registration.register(BlockProvider.LOOKUP, List.of(
				itemApi(BlackHoleTalismanItem.BlockProviderImpl::new, BotaniaItems.BLACK_HOLE_TALISMAN),
				itemApi(DepthsRodItem.BlockProviderImpl::new, BotaniaItems.ROD_OF_THE_DEPTHS),
				itemApi(EnderHandItem.BlockProviderImpl::new, BotaniaItems.HAND_OF_ENDER),
				itemApi(LandsRodItem.BlockProviderImpl::new,
						BotaniaItems.ROD_OF_THE_LANDS, BotaniaItems.ROD_OF_THE_HIGHLANDS,
						BotaniaItems.ROD_OF_THE_TERRA_FIRMA
				)
		));

		registration.register(CoordBoundItem.LOOKUP, List.of(
				itemApi(EyeOfTheFlugelItem.CoordBoundItemImpl::new, BotaniaItems.EYE_OF_THE_FLUGEL),
				itemApi(ManaMirrorItem.CoordBoundItemImpl::new, BotaniaItems.MANA_MIRROR),
				itemApi(WandOfTheForestItem.CoordBoundItemImpl::new,
						BotaniaItems.WAND_OF_THE_FOREST, BotaniaItems.WAND_OF_THE_ELVEN_FOREST
				)
		));

		registration.register(HourglassMaterial.LOOKUP, List.of(
				itemApi(HourglassMaterial.SAND, Items.SAND),
				itemApi(HourglassMaterial.RED_SAND, Items.RED_SAND),
				itemApi(HourglassMaterial.SOUL_SAND, Items.SOUL_SAND),
				itemApi(HourglassMaterial.MANA_POWDER, BotaniaItems.MANA_POWDER)
		));

		registration.register(ManaItem.LOOKUP, List.of(
				itemApi(DefaultManaItemImpl::new,
						BotaniaItems.MANA_MIRROR, BotaniaItems.BAND_OF_MANA, BotaniaItems.GREATER_BAND_OF_MANA,
						BotaniaItems.MANA_TABLET, BotaniaItems.TERRA_SHATTERER
				)
		));

		registration.register(Relic.LOOKUP, List.of(
				itemApi(DiceOfFateItem::makeRelic, BotaniaItems.DICE_OF_FATE),
				itemApi(EyeOfTheFlugelItem::makeRelic, BotaniaItems.EYE_OF_THE_FLUGEL),
				itemApi(FruitOfGrisaiaItem::makeRelic, BotaniaItems.FRUIT_OF_GRISAIA),
				itemApi(KeyOfTheKingsLawItem::makeRelic, BotaniaItems.KEY_OF_THE_KINGS_LAW),
				itemApi(RingOfLokiItem::makeRelic, BotaniaItems.RING_OF_LOKI),
				itemApi(RingOfOdinItem::makeRelic, BotaniaItems.RING_OF_ODIN),
				itemApi(RingOfThorItem::makeRelic, BotaniaItems.RING_OF_THOR)
		));
	}

	private ItemCapabilities() {}

	public static <A, C> ItemRegistrationWithContext<A, C> itemApi(ItemRegistrationWithContext.Provider<A, C> provider,
			ItemLike... items) {
		return ItemRegistrationWithContext.forItems(provider, items);
	}

	public static <A> ItemRegistrationNoContext<A> itemApi(ItemRegistrationNoContext.Provider<A> provider,
			ItemLike... items) {
		return ItemRegistrationNoContext.forItems(provider, items);
	}

	public static <A> ItemRegistrationNoContext<A> itemApi(A singleton, ItemLike... items) {
		return ItemRegistrationNoContext.forItems(stack -> singleton, items);
	}
}
