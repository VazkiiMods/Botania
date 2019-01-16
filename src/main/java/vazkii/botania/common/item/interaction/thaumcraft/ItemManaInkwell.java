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

import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.NonNullList;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.Optional;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import thaumcraft.api.items.IScribeTools;
import vazkii.botania.api.mana.IManaItem;
import vazkii.botania.common.core.helper.ItemNBTHelper;
import vazkii.botania.common.item.ItemMod;
import vazkii.botania.common.lib.LibItemNames;

import javax.annotation.Nonnull;

@Optional.Interface(modid = "thaumcraft", iface = "thaumcraft.api.items.IScribeTools")
public class ItemManaInkwell extends ItemMod implements IManaItem, IScribeTools {

	private static final int COST_PER_USE = 50;
	private static final int USES = 150;
	protected static final int MAX_MANA = COST_PER_USE * USES;

	private static final String TAG_MANA = "mana";

	public ItemManaInkwell() {
		super(LibItemNames.MANA_INKWELL);
		setMaxDamage(USES);
		setMaxStackSize(1);
		setNoRepair();
	}

	@Override
	public void getSubItems(@Nonnull CreativeTabs tab, @Nonnull NonNullList<ItemStack> list) {
		if(isInCreativeTab(tab)) {
			list.add(new ItemStack(this, 1, USES));
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
		int currentDamage = stack.getItemDamage();
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
		stack.setItemDamage(getDamage(stack));
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
