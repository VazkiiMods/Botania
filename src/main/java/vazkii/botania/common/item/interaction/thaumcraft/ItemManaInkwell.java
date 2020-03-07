/**
 * This class was created by <Vazkii>. It's distributed as
 * part of the Botania Mod. Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 *
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 *
 * File Created @ [Sep 26, 2014, 5:39:07 PM (GMT)]
 */
package vazkii.botania.common.item.interaction.thaumcraft;

import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.NonNullList;
import net.minecraft.world.World;
import vazkii.botania.api.mana.IManaItem;
import vazkii.botania.common.core.helper.ItemNBTHelper;
import net.minecraft.item.Item;

import javax.annotation.Nonnull;

public class ItemManaInkwell extends Item implements IManaItem {

	private static final int COST_PER_USE = 50;
	private static final int USES = 150;
	protected static final int MAX_MANA = COST_PER_USE * USES;

	private static final String TAG_MANA = "mana";

	public ItemManaInkwell(Properties props) {
		super(props.defaultMaxDamage(USES));
	}

	@Override
	public void fillItemGroup(@Nonnull ItemGroup tab, @Nonnull NonNullList<ItemStack> list) {
		if(isInGroup(tab)) {
			list.add(new ItemStack(this));
			ItemStack full = new ItemStack(this);
			setMana(full, MAX_MANA);
			list.add(full);
		}
	}

	@Override
	public int getDamage(ItemStack stack) {
		float mana = getMana(stack);
		return USES - (int) (mana / getMaxMana(stack) * USES);
	}

	@Override
	public void setDamage(ItemStack stack, int damage) {
		int currentDamage = stack.getDamage();
		if(damage > currentDamage) {
			int cost = (damage - currentDamage) * COST_PER_USE;
			int mana = getMana(stack);
			if(mana >= cost) {
				addMana(stack, -cost);
				return;
			}
		}
		super.setDamage(stack, damage);
	}

	@Override
	public int getEntityLifespan(ItemStack itemStack, World world) {
		return Integer.MAX_VALUE;
	}

	public static void setMana(ItemStack stack, int mana) {
		ItemNBTHelper.setInt(stack, TAG_MANA, mana);
	}

	@Override
	public int getMana(ItemStack stack) {
		return ItemNBTHelper.getInt(stack, TAG_MANA, 0);
	}

	@Override
	public int getMaxMana(ItemStack stack) {
		return MAX_MANA;
	}

	@Override
	public void addMana(ItemStack stack, int mana) {
		setMana(stack, Math.min(getMana(stack) + mana, getMaxMana(stack)));
		stack.setDamage(getDamage(stack));
	}

	@Override
	public boolean canReceiveManaFromPool(ItemStack stack, TileEntity pool) {
		return true;
	}

	@Override
	public boolean canReceiveManaFromItem(ItemStack stack, ItemStack otherStack) {
		return false;
	}

	@Override
	public boolean canExportManaToPool(ItemStack stack, TileEntity pool) {
		return false;
	}

	@Override
	public boolean canExportManaToItem(ItemStack stack, ItemStack otherStack) {
		return false;
	}

	@Override
	public boolean isNoExport(ItemStack stack) {
		return true;
	}

}
