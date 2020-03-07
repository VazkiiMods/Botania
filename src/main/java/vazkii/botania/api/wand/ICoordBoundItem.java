/*
 * This class is distributed as part of the Botania Mod.
 * Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 *
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 */
package vazkii.botania.api.wand;

import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;

/**
 * The item equivalent of ITileBound, renders when the
 * item is in hand.
 * 
 * @see ITileBound
 */
public interface ICoordBoundItem {

	public BlockPos getBinding(ItemStack stack);

}
