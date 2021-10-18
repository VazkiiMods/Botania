/*
 * This class is distributed as part of the Botania Mod.
 * Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 *
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 */
package vazkii.botania.common.item.equipment.bauble;

import net.minecraft.core.NonNullList;
import net.minecraft.util.Mth;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.entity.BlockEntity;

import vazkii.botania.api.mana.IManaItem;
import vazkii.botania.api.mana.IManaTooltipDisplay;
import vazkii.botania.common.core.helper.ItemNBTHelper;

import javax.annotation.Nonnull;

public class ItemManaRing extends ItemBauble implements IManaItem, IManaTooltipDisplay {

	protected static final int MAX_MANA = 500000;

	private static final String TAG_MANA = "mana";

	public ItemManaRing(Properties props) {
		super(props);
	}

	@Override
	public void fillItemCategory(@Nonnull CreativeModeTab tab, @Nonnull NonNullList<ItemStack> stacks) {
		if (allowdedIn(tab)) {
			stacks.add(new ItemStack(this));

			ItemStack full = new ItemStack(this);
			setMana(full, getMaxMana(full));
			stacks.add(full);
		}
	}

	public static void setMana(ItemStack stack, int mana) {
		ItemNBTHelper.setInt(stack, TAG_MANA, mana);
	}

	@Override
	public int getMana(ItemStack stack) {
		return ItemNBTHelper.getInt(stack, TAG_MANA, 0) * stack.getCount();
	}

	@Override
	public int getMaxMana(ItemStack stack) {
		return MAX_MANA * stack.getCount();
	}

	@Override
	public void addMana(ItemStack stack, int mana) {
		setMana(stack, Math.min(getMana(stack) + mana, getMaxMana(stack)) / stack.getCount());
	}

	@Override
	public boolean canReceiveManaFromPool(ItemStack stack, BlockEntity pool) {
		return true;
	}

	@Override
	public boolean canReceiveManaFromItem(ItemStack stack, ItemStack otherStack) {
		return true;
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
	public float getManaFractionForDisplay(ItemStack stack) {
		return (float) getMana(stack) / (float) getMaxMana(stack);
	}

	@Override
	public boolean isBarVisible(ItemStack stack) {
		return true;
	}

	@Override
	public int getBarWidth(ItemStack stack) {
		return Math.round(13 * getManaFractionForDisplay(stack));
	}

	@Override
	public int getBarColor(ItemStack stack) {
		return Mth.hsvToRgb(getManaFractionForDisplay(stack) / 3.0F, 1.0F, 1.0F);
	}
}
