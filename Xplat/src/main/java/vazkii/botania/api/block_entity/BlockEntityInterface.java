/*
 * This class is distributed as part of the Botania Mod.
 * Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 *
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 */

package vazkii.botania.api.block_entity;

import net.minecraft.world.level.Level;
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

	/**
	 * Mark the chunk containing this block entity as changed, i.e. let the game save it at the next opportunity.
	 * Functional and generating flowers automatically mark themselves for persisting and synchronization when their
	 * internal buffer fill level changes (compared to the previous tick) after pushing to the bound spreader or pulling
	 * from the bound pool, respectively. (This is one half of the effect of {@code setChanged()}.)
	 */
	default void markForPersisting() {
		Level level = getSelf().getLevel();
		if (level != null && !level.isClientSide()) {
			level.blockEntityChanged(getSelf().getBlockPos());
		}
	}
}
