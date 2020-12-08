/*
 * This class is distributed as part of the Botania Mod.
 * Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 *
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 */
package vazkii.botania.common.item;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.client.item.TooltipContext;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.text.Text;
import net.minecraft.text.TranslatableText;
import net.minecraft.util.Formatting;
import net.minecraft.util.Rarity;
import net.minecraft.util.collection.DefaultedList;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;

import vazkii.botania.api.item.IDurabilityExtension;
import vazkii.botania.api.mana.ICreativeManaProvider;
import vazkii.botania.api.mana.IManaItem;
import vazkii.botania.api.mana.IManaTooltipDisplay;
import vazkii.botania.common.core.helper.ItemNBTHelper;

import javax.annotation.Nonnull;

import java.util.List;

public class ItemManaTablet extends Item implements IManaItem, ICreativeManaProvider, IManaTooltipDisplay, IDurabilityExtension {

	public static final int MAX_MANA = 500000;

	private static final String TAG_MANA = "mana";
	private static final String TAG_CREATIVE = "creative";
	private static final String TAG_ONE_USE = "oneUse";

	public ItemManaTablet(Settings props) {
		super(props);
	}

	@Override
	public void appendStacks(@Nonnull ItemGroup tab, @Nonnull DefaultedList<ItemStack> stacks) {
		if (isIn(tab)) {
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
		return isCreative(stack) ? Rarity.EPIC : super.getRarity(stack);
	}

	@Environment(EnvType.CLIENT)
	@Override
	public void appendTooltip(ItemStack stack, World world, List<Text> stacks, TooltipContext flags) {
		if (isStackCreative(stack)) {
			stacks.add(new TranslatableText("botaniamisc.creative").formatted(Formatting.GRAY));
		}
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
		if (!isStackCreative(stack)) {
			setMana(stack, Math.min(getMana(stack) + mana, MAX_MANA));
		}
	}

	@Override
	public boolean canReceiveManaFromPool(ItemStack stack, BlockEntity pool) {
		return !ItemNBTHelper.getBoolean(stack, TAG_ONE_USE, false);
	}

	@Override
	public boolean canReceiveManaFromItem(ItemStack stack, ItemStack otherStack) {
		return !isCreative(stack);
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
	public boolean isCreative(ItemStack stack) {
		return isStackCreative(stack);
	}

	@Override
	public float getManaFractionForDisplay(ItemStack stack) {
		return (float) getMana(stack) / (float) getMaxMana(stack);
	}
	
	@Override
	public boolean showDurability(ItemStack stack) {
		return !isStackCreative(stack);
	}
	
	@Override
	public double getDurability(ItemStack stack) {
		return 1 - getManaFractionForDisplay(stack);
	}
	
	@Override
	public int getDurabilityColor(ItemStack stack) {
		return MathHelper.hsvToRgb(getManaFractionForDisplay(stack) / 3.0F, 1.0F, 1.0F);
	}
}
