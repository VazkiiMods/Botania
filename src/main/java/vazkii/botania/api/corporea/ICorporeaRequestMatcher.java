/*
 * This class is distributed as part of the Botania Mod.
 * Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 *
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 */
package vazkii.botania.api.corporea;

import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.StringTextComponent;

/**
 * An interface for a Corporea Request matcher. Accepts an ItemStack and returns whether it fulfills the request.
 */
public interface ICorporeaRequestMatcher {

	/**
	 * Returns whether the given stack matches the request's criteria.
	 */
	default boolean isStackValid(ItemStack stack) {
		return false;
	}

	/**
	 * Serialize to NBT data, for the Corporea Retainer's benefit.
	 */
	default void writeToNBT(CompoundNBT tag) {}

	/**
	 * Returns the pretty name of the requested item, for printing request feedback on Corporea Indexes.
	 */
	default ITextComponent getRequestName() {
		return new StringTextComponent("missingno");
	}

	enum Dummy implements ICorporeaRequestMatcher {
		INSTANCE
	}
}
