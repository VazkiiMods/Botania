/*
 * This class is distributed as part of the Botania Mod.
 * Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 *
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 */

package org.violetmoon.botania.common.capabilities;

import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.ItemLike;

import org.violetmoon.botania.api.capability.registration.ApiIdRegistration;
import org.violetmoon.botania.api.capability.registration.ApiProviderRegistration;
import org.violetmoon.botania.api.capability.registration.ItemRegistrationNoContext;
import org.violetmoon.botania.api.capability.registration.ItemRegistrationWithContext;
import org.violetmoon.botania.api.item.AvatarWieldable;
import org.violetmoon.botania.api.item.BlockProvider;
import org.violetmoon.botania.api.item.CoordBoundItem;
import org.violetmoon.botania.api.item.HourglassMaterial;
import org.violetmoon.botania.api.item.Relic;
import org.violetmoon.botania.api.mana.ManaItem;
import org.violetmoon.botania.common.component.BotaniaDataComponents;
import org.violetmoon.botania.common.impl.mana.DefaultManaItemImpl;
import org.violetmoon.botania.common.item.BlackHoleTalismanItem;
import org.violetmoon.botania.common.item.BotaniaItems;
import org.violetmoon.botania.common.item.EnderHandItem;
import org.violetmoon.botania.common.item.ManaMirrorItem;
import org.violetmoon.botania.common.item.WandOfTheForestItem;
import org.violetmoon.botania.common.item.relic.DiceOfFateItem;
import org.violetmoon.botania.common.item.relic.EyeOfTheFlugelItem;
import org.violetmoon.botania.common.item.relic.FruitOfGrisaiaItem;
import org.violetmoon.botania.common.item.relic.KeyOfTheKingsLawItem;
import org.violetmoon.botania.common.item.relic.RingOfLokiItem;
import org.violetmoon.botania.common.item.relic.RingOfOdinItem;
import org.violetmoon.botania.common.item.relic.RingOfThorItem;
import org.violetmoon.botania.common.item.rod.BifrostRodItem;
import org.violetmoon.botania.common.item.rod.DepthsRodItem;
import org.violetmoon.botania.common.item.rod.HellsRodItem;
import org.violetmoon.botania.common.item.rod.LandsRodItem;
import org.violetmoon.botania.common.item.rod.PlentifulMantleRodItem;
import org.violetmoon.botania.common.item.rod.SkiesRodItem;
import org.violetmoon.botania.common.item.rod.UnstableReservoirRodItem;

import java.util.List;
import java.util.function.Predicate;

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

	public static void registerFallbackProviders(ApiProviderRegistration registration) {
		registration.register(ManaItem.LOOKUP, List.of(
				itemApi(DefaultManaItemImpl::new, stack -> stack.has(BotaniaDataComponents.MAX_MANA))
		));
	}

	private ItemCapabilities() {}

	public static <A> ItemRegistrationNoContext<A> itemApi(ItemRegistrationNoContext.Provider<A> provider,
			Predicate<ItemStack> predicate) {
		return ItemRegistrationNoContext.forItemPredicate(provider, predicate);
	}

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
