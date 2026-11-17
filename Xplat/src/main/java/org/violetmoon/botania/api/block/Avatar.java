/*
 * This class is distributed as part of the Botania Mod.
 * Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 *
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 */
package vazkii.botania.api.block;

import net.minecraft.core.Direction;
import net.minecraft.world.entity.SlotAccess;
import net.minecraft.world.level.block.entity.BlockEntity;

import vazkii.botania.api.block_entity.BlockEntityInterface;

/**
 * Base interface for the Avatar Block Entity
 */
public interface Avatar extends BlockEntityInterface<BlockEntity> {

	/**
	 * Gets the avatar's single inventory slot.
	 */
	SlotAccess getHeldItemSlot();

	/**
	 * Gets the avatar's facing.
	 */
	Direction getAvatarFacing();

	/**
	 * Gets if this avatar is enabled (is powered by a redstone signal).
	 */
	boolean isEnabled();

}
