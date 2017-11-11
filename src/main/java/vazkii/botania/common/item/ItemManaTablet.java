/**
 * This class was created by <Vazkii>. It's distributed as
 * part of the Botania Mod. Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 *
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 *
 * File Created @ [Mar 7, 2014, 7:06:20 PM (GMT)]
 */
package vazkii.botania.common.item;

import net.minecraft.client.resources.I18n;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.NonNullList;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import vazkii.botania.api.mana.ICreativeManaProvider;
import vazkii.botania.api.mana.IManaItem;
import vazkii.botania.api.mana.IManaTooltipDisplay;
import vazkii.botania.common.core.helper.ItemNBTHelper;
import vazkii.botania.common.lib.LibItemNames;

import javax.annotation.Nonnull;
import java.util.List;

public class ItemManaTablet extends ItemMod implements IManaItem, ICreativeManaProvider, IManaTooltipDisplay {

	public static final int MAX_MANA = 500000;

	private static final String TAG_MANA = "mana";
	private static final String TAG_CREATIVE = "creative";
	private static final String TAG_ONE_USE = "oneUse";

	public ItemManaTablet() {
		super(LibItemNames.MANA_TABLET);
		setMaxStackSize(1);
	}

	@Override
	public void getSubItems(@Nonnull CreativeTabs tab, @Nonnull NonNullList<ItemStack> stacks) {
		if(isInCreativeTab(tab)) {
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

	@Override
	public int getDamage(ItemStack stack) {
		// Compatibility shim, so tablets from previous versions of botania
		// stack right in barrels and so forth
		if(super.getDamage(stack) != 0)
			super.setDamage(stack, 0);

		return 0;
	}

	@SideOnly(Side.CLIENT)
	@Override
	public void addInformation(ItemStack par1ItemStack, World world, List<String> stacks, ITooltipFlag flags) {
		if(isStackCreative(par1ItemStack))
			stacks.add(I18n.format("botaniamisc.creative"));
	}

	@Override
	public int getEntityLifespan(ItemStack itemStack, World world) {
		return Integer.MAX_VALUE;
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
		return ItemNBTHelper.getInt(stack, TAG_MANA, 0);
	}

	@Override
	public int getMaxMana(ItemStack stack) {
		return isStackCreative(stack) ? MAX_MANA + 1000 : MAX_MANA;
	}

	@Override
	public void addMana(ItemStack stack, int mana) {
		if(!isStackCreative(stack))
			setMana(stack, Math.min(getMana(stack) + mana, MAX_MANA));
	}

	@Override
	public boolean canReceiveManaFromPool(ItemStack stack, TileEntity pool) {
		return !ItemNBTHelper.getBoolean(stack, TAG_ONE_USE, false);
	}

	@Override
	public boolean canReceiveManaFromItem(ItemStack stack, ItemStack otherStack) {
		return !isCreative(stack);
	}

	@Override
	public boolean canExportManaToPool(ItemStack stack, TileEntity pool) {
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
	public boolean isCreative(ItemStack stack) {
		return isStackCreative(stack);
	}

	@Override
	public float getManaFractionForDisplay(ItemStack stack) {
		return (float) getMana(stack) / (float) getMaxMana(stack);
	}


	@Override
	public boolean showDurabilityBar(ItemStack stack) {
		return !isStackCreative(stack);
	}

	@Override
	public double getDurabilityForDisplay(ItemStack stack) {
		return 1.0 - getManaFractionForDisplay(stack);
	}

	@Override
	public int getRGBDurabilityForDisplay(ItemStack stack) {
		return MathHelper.hsvToRGB(getManaFractionForDisplay(stack) / 3.0F, 1.0F, 1.0F);
	}
}
