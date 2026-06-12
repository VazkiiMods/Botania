/*
 * This class is distributed as part of the Botania Mod.
 * Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 *
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 */

package vazkii.botania.api.block_entity;

import net.minecraft.world.level.block.entity.BlockEntity;

import org.jetbrains.annotations.ApiStatus;

/**
 * A helper interface that is extended by interfaces that should only be implemented by a BlockEntity.
 *
 * @param <T> The implementing BlockEntity's type.
 */
public interface BlockEntityInterface<T extends BlockEntity> {
	/**
	 * Helper method to potentially necessary typecasting nonsense related to implementing interfaces.
	 * Implementing classes should specify their own type as the return type and return {@code this} unconditionally.
	 */
	@ApiStatus.OverrideOnly
	T getSelf();
}
