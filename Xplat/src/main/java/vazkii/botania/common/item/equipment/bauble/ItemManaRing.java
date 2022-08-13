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
import net.minecraft.world.inventory.tooltip.TooltipComponent;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.entity.BlockEntity;

import vazkii.botania.api.mana.IManaItem;
import vazkii.botania.api.mana.ManaBarTooltip;
import vazkii.botania.common.helper.ItemNBTHelper;
import vazkii.botania.xplat.IXplatAbstractions;

import javax.annotation.Nonnull;

import java.util.Optional;

public class ItemManaRing extends ItemBauble {

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
			setMana(full, MAX_MANA);
			stacks.add(full);
		}
	}

	@Override
	public Optional<TooltipComponent> getTooltipImage(ItemStack stack) {
		return Optional.of(ManaBarTooltip.fromManaItem(stack));
	}

	protected static void setMana(ItemStack stack, int mana) {
		if (mana > 0) {
			ItemNBTHelper.setInt(stack, TAG_MANA, mana);
		} else {
			ItemNBTHelper.removeEntry(stack, TAG_MANA);
		}
	}

	public static class ManaItem implements IManaItem {
		protected final ItemStack stack;

		public ManaItem(ItemStack stack) {
			this.stack = stack;
		}

		@Override
		public int getMana() {
			return ItemNBTHelper.getInt(stack, TAG_MANA, 0) * stack.getCount();
		}

		@Override
		public int getMaxMana() {
			return MAX_MANA * stack.getCount();
		}

		@Override
		public void addMana(int mana) {
			setMana(stack, Math.min(getMana() + mana, getMaxMana()) / stack.getCount());
		}

		@Override
		public boolean canReceiveManaFromPool(BlockEntity pool) {
			return true;
		}

		@Override
		public boolean canReceiveManaFromItem(ItemStack otherStack) {
			return true;
		}

		@Override
		public boolean canExportManaToPool(BlockEntity pool) {
			return true;
		}

		@Override
		public boolean canExportManaToItem(ItemStack otherStack) {
			return true;
		}

		@Override
		public boolean isNoExport() {
			return false;
		}
	}

	@Override
	public boolean isBarVisible(ItemStack stack) {
		return true;
	}

	@Override
	public int getBarWidth(ItemStack stack) {
		var manaItem = IXplatAbstractions.INSTANCE.findManaItem(stack);
		return Math.round(13 * ManaBarTooltip.getFractionForDisplay(manaItem));
	}

	@Override
	public int getBarColor(ItemStack stack) {
		var manaItem = IXplatAbstractions.INSTANCE.findManaItem(stack);
		return Mth.hsvToRgb(ManaBarTooltip.getFractionForDisplay(manaItem) / 3.0F, 1.0F, 1.0F);
	}
}
