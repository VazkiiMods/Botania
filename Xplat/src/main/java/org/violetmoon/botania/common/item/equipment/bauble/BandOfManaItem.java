/*
 * This class is distributed as part of the Botania Mod.
 * Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 *
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 */
package org.violetmoon.botania.common.item.equipment.bauble;

import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.tooltip.TooltipComponent;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;

import org.violetmoon.botania.api.mana.ManaBarTooltip;
import org.violetmoon.botania.api.mana.ManaItem;
import org.violetmoon.botania.common.annotations.SoftImplement;
import org.violetmoon.botania.common.component.BotaniaDataComponents;
import org.violetmoon.botania.common.helper.DataComponentHelper;
import org.violetmoon.botania.common.impl.mana.DefaultManaItemImpl;
import org.violetmoon.botania.common.item.CustomCreativeTabContents;
import org.violetmoon.botania.common.item.ManaTabletItem;

import java.util.Optional;

public class BandOfManaItem extends BaubleItem implements CustomCreativeTabContents {

	public static final int DEFAULT_MAX_MANA = ManaTabletItem.DEFAULT_MAX_MANA;
	public static final int DEFAULT_GREATER_MAX_MANA = DEFAULT_MAX_MANA * 4;

	public BandOfManaItem(Properties properties) {
		super(properties);
	}

	@Override
	public void addToCreativeTab(Item me, CreativeModeTab.Output output) {
		output.accept(me);

		ItemStack full = new ItemStack(me);
		setMana(full, me.components().getOrDefault(BotaniaDataComponents.MAX_MANA, DEFAULT_MAX_MANA));
		output.accept(full);
	}

	@Override
	public Optional<TooltipComponent> getTooltipImage(ItemStack stack) {
		return Optional.of(ManaBarTooltip.fromManaItem(stack));
	}

	protected static void setMana(ItemStack stack, int mana) {
		DataComponentHelper.setIntNonZero(stack, BotaniaDataComponents.MANA, mana);
	}

	@Override
	public boolean isBarVisible(ItemStack stack) {
		return ManaItem.LOOKUP.find(stack) != null;
	}

	@Override
	public int getBarWidth(ItemStack stack) {
		return DefaultManaItemImpl.getManaItemBarWidth(stack);
	}

	@Override
	public int getBarColor(ItemStack stack) {
		return DefaultManaItemImpl.getManaItemBarColor(stack);
	}

	@SoftImplement("IItemExtension")
	public boolean shouldCauseReequipAnimation(ItemStack oldStack, ItemStack newStack, boolean slotChanged) {
		return slotChanged;
	}

	@SoftImplement("FabricItem")
	public boolean allowComponentsUpdateAnimation(Player player, InteractionHand hand, ItemStack oldStack, ItemStack newStack) {
		return false;
	}
}
