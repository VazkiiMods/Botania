/**
 * This class was created by <Alwinfy>. It's distributed as
 * part of the Botania Mod. Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 *
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 *
 * File Created @ [Dec 22, 2019, 11:13:14 PM (GMT)]
 */
package vazkii.botania.api.corporea;

import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.text.ITextComponent;

/**
 * An interface for a Corporea Request matcher. Accepts an ItemStack and returns whether it fulfills the request.
 * Needs to be registered over in {@link vazkii.botania.common.block.tile.corporea.TileCorporeaRetainer#addCorporeaRequestMatcher}.
 * Needs one additional (static) method for deserialization, which can't really go here because Java doesn't like static abstract's.
 */
public interface ICorporeaRequestMatcher {

	/**
	 * Returns whether the given stack matches the request's criteria.
	 */
	boolean isStackValid(ItemStack stack);

	/**
	 * Serialize to NBT data, for the Corporea Retainer's benefit.
	 */
	void writeToNBT(CompoundNBT tag);

	/**
	 * Returns the pretty name of the requested item, for printing request feedback on Corporea Indexes.
	 */
	ITextComponent getRequestName();
}
