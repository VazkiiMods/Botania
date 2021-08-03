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
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;

import javax.annotation.Nullable;

import java.util.List;

/**
 * An item that implements this will allow for various wireframes to be drawn
 * around the player. This will be called while the item is in the inventory,
 * armor slot or bauble slots.
 */
public interface IWireframeCoordinateListProvider {

	/**
	 * Returns a list of BlockPos for the wireframes to draw.
	 * Will not be null.
	 */
	List<BlockPos> getWireframesToDraw(Player player, ItemStack stack);

	/**
	 * Gets a wireframe to draw thicker than the rest.
	 * This is useful to indicate the precedence of some position over the others.
	 * 
	 * @return The position of a single wireframe to draw thicker than all the others.
	 */
	@Nullable
	default BlockPos getSourceWireframe(Player player, ItemStack stack) {
		return null;
	}

}
