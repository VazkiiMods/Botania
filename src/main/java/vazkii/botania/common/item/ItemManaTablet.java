/*
 * This class is distributed as part of the Botania Mod.
 * Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 *
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 */
package vazkii.botania.common.item;

import net.minecraft.ChatFormatting;
import net.minecraft.core.NonNullList;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.util.Mth;
import net.minecraft.world.inventory.tooltip.TooltipComponent;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Rarity;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;

import vazkii.botania.api.mana.IManaItem;
import vazkii.botania.api.mana.ManaBarTooltip;
import vazkii.botania.common.core.helper.ItemNBTHelper;

import javax.annotation.Nonnull;

import java.util.List;
import java.util.Optional;

public class ItemManaTablet extends Item implements IManaItem {

	public static final int MAX_MANA = 500000;

	private static final String TAG_MANA = "mana";
	private static final String TAG_CREATIVE = "creative";
	private static final String TAG_ONE_USE = "oneUse";

	public ItemManaTablet(Properties props) {
		super(props);
	}

	@Override
	public void fillItemCategory(@Nonnull CreativeModeTab tab, @Nonnull NonNullList<ItemStack> stacks) {
		if (allowdedIn(tab)) {
			stacks.add(new ItemStack(this));

			ItemStack fullPower = new ItemStack(this);
			setMana(fullPower, MAX_MANA);
			stacks.add(fullPower);

			ItemStack creative = new ItemStack(this);
			setMana(creative, MAX_MANA);
			setStackCreative(creative);
			stacks.add(creative);
		}
	}

	@Nonnull
	@Override
	public Rarity getRarity(@Nonnull ItemStack stack) {
		return isStackCreative(stack) ? Rarity.EPIC : super.getRarity(stack);
	}

	@Override
	public void appendHoverText(ItemStack stack, Level world, List<Component> stacks, TooltipFlag flags) {
		if (isStackCreative(stack)) {
			stacks.add(new TranslatableComponent("botaniamisc.creative").withStyle(ChatFormatting.GRAY));
		}
	}

	@Override
	public Optional<TooltipComponent> getTooltipImage(ItemStack stack) {
		return Optional.of(ManaBarTooltip.fromManaItem(stack));
	}

	public static void setMana(ItemStack stack, int mana) {
		ItemNBTHelper.setInt(stack, TAG_MANA, mana);
	}

	public static void setStackCreative(ItemStack stack) {
		ItemNBTHelper.setBoolean(stack, TAG_CREATIVE, true);
	}

	public static boolean isStackCreative(ItemStack stack) {
		return ItemNBTHelper.getBoolean(stack, TAG_CREATIVE, false);
	}

	@Override
	public int getMana(ItemStack stack) {
		if (isStackCreative(stack)) {
			return getMaxMana(stack);
		}
		return ItemNBTHelper.getInt(stack, TAG_MANA, 0) * stack.getCount();
	}

	@Override
	public int getMaxMana(ItemStack stack) {
		return (isStackCreative(stack) ? MAX_MANA + 1000 : MAX_MANA) * stack.getCount();
	}

	@Override
	public void addMana(ItemStack stack, int mana) {
		if (!isStackCreative(stack)) {
			setMana(stack, Math.min(getMana(stack) + mana, getMaxMana(stack)) / stack.getCount());
		}
	}

	@Override
	public boolean canReceiveManaFromPool(ItemStack stack, BlockEntity pool) {
		return !ItemNBTHelper.getBoolean(stack, TAG_ONE_USE, false);
	}

	@Override
	public boolean canReceiveManaFromItem(ItemStack stack, ItemStack otherStack) {
		return !isStackCreative(stack);
	}

	@Override
	public boolean canExportManaToPool(ItemStack stack, BlockEntity pool) {
		return true;
	}

	@Override
	public boolean canExportManaToItem(ItemStack stack, ItemStack otherStack) {
		return true;
	}

	@Override
	public boolean isNoExport(ItemStack stack) {
		return false;
	}

	@Override
	public boolean isBarVisible(ItemStack stack) {
		return !isStackCreative(stack);
	}

	@Override
	public int getBarWidth(ItemStack stack) {
		return Math.round(13 * ManaBarTooltip.getFractionForDisplay(this, stack));
	}

	@Override
	public int getBarColor(ItemStack stack) {
		return Mth.hsvToRgb(ManaBarTooltip.getFractionForDisplay(this, stack) / 3.0F, 1.0F, 1.0F);
	}
}
