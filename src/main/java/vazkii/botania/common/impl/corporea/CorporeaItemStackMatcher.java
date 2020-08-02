/*
 * This class is distributed as part of the Botania Mod.
 * Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 *
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 */
package vazkii.botania.common.impl.corporea;

import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.text.ITextComponent;

import vazkii.botania.api.corporea.ICorporeaRequestMatcher;
import vazkii.botania.common.core.helper.ItemNBTHelper;

public class CorporeaItemStackMatcher implements ICorporeaRequestMatcher {
	private static final String TAG_REQUEST_STACK = "requestStack";
	private static final String TAG_REQUEST_CHECK_NBT = "requestCheckNBT";

	private final ItemStack match;
	private final boolean checkNBT;

	public CorporeaItemStackMatcher(ItemStack match, boolean checkNBT) {
		this.match = match;
		this.checkNBT = checkNBT;
	}

	@Override
	public boolean test(ItemStack stack) {
		return !stack.isEmpty() && !match.isEmpty() && stack.isItemEqual(match) && (!checkNBT || ItemNBTHelper.matchTag(match.getTag(), stack.getTag()));
	}

	public static CorporeaItemStackMatcher createFromNBT(CompoundNBT tag) {
		return new CorporeaItemStackMatcher(ItemStack.read(tag.getCompound(TAG_REQUEST_STACK)), tag.getBoolean(TAG_REQUEST_CHECK_NBT));
	}

	@Override
	public void writeToNBT(CompoundNBT tag) {
		CompoundNBT cmp = match.write(new CompoundNBT());
		tag.put(TAG_REQUEST_STACK, cmp);
		tag.putBoolean(TAG_REQUEST_CHECK_NBT, checkNBT);
	}

	@Override
	public ITextComponent getRequestName() {
		return match.getTextComponent();
	}
}
