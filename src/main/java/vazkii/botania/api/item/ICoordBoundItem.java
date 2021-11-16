/*
 * This class is distributed as part of the Botania Mod.
 * Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 *
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 */
package vazkii.botania.api.item;

import net.minecraft.core.BlockPos;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;

import vazkii.botania.api.block.ITileBound;

import javax.annotation.Nullable;

/**
 * The item equivalent of ITileBound, renders when the
 * item is in hand.
 * 
 * @see ITileBound
 */
public interface ICoordBoundItem {

	@Nullable
	BlockPos getBinding(Level world, ItemStack stack);

}
