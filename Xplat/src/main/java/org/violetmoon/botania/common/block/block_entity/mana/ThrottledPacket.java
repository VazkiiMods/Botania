/*
 * This class is distributed as part of the Botania Mod.
 * Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 *
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 */
package vazkii.botania.common.block.block_entity.mana;

import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.phys.Vec3;

import vazkii.botania.api.block_entity.BlockEntityInterface;
import vazkii.botania.common.helper.EntityHelper;
import vazkii.botania.common.helper.PlayerHelper;
import vazkii.botania.common.item.WandOfTheForestItem;

/**
 * A BlockEntity that will only send a few packets rather than one for every client-visible change.
 *
 * @param <T> The implementing BlockEntity's type.
 */
public interface ThrottledPacket<T extends BlockEntity & ThrottledPacket<T>> extends BlockEntityInterface<T> {

	// synchronization methods

	/**
	 * This block entity received a non-critical change that should be synchronized with clients eventually.
	 */
	default void markForPotentialSync() {
		setMarkedForSync(true);
	}

	/**
	 * This block entity received an important change that should be synchronized with clients as soon as possible.
	 */
	default void markForImmediateSync() {
		Level level = getSelf().getLevel();
		if (level != null && !level.isClientSide()) {
			level.sendBlockUpdated(getSelf().getBlockPos(), getSelf().getBlockState(), getSelf().getBlockState(),
					Block.UPDATE_CLIENTS);
			setMarkedForSync(false);
		}
	}

	// internal methods

	/**
	 * The implementing block entity should call this method regularly to synchronize potential pending changes.
	 */
	default void maybeSyncNow() {
		Level level = getSelf().getLevel();
		if (level != null && !level.isClientSide() && isMarkedForSync()
				&& (level.getGameTime() + getSelf().getBlockState().getSeed(getSelf().getBlockPos())) % getSyncInterval() == 0
				&& mayBeRelevantForClients(level)) {
			markForImmediateSync();
		}
	}

	/**
	 * Determines whether a potential synchronization could be relevant to at least some players.
	 */
	default boolean mayBeRelevantForClients(Level level) {
		BlockPos pos = getSelf().getBlockPos();
		Vec3 posCenter = pos.getCenter();
		for (Player player : level.players()) {
			if (player.isAlive()
					&& EntityHelper.isLookingTowards(player, posCenter)
					// is player close enough to potentially interact? (with some leeway)
					&& player.canInteractWithBlock(pos, 2)
					// could player have the wand hud up?
					&& PlayerHelper.hasHeldItemClass(player, WandOfTheForestItem.class)) {
				return true;
			}
		}
		return false;
	}

	/**
	 * Implement to allow retrieving the flag for potential future synchronization.
	 *
	 * @return Whether a client synchronization is pending.
	 */
	boolean isMarkedForSync();

	/**
	 * Implement to store the flag for potential future synchronization.
	 * 
	 * @param markedForSync Whether a client synchronization is pending.
	 */
	void setMarkedForSync(boolean markedForSync);

	/**
	 * Implement to define the intended synchronization interval.
	 * 
	 * @return The interval for low-priority synchronizations.
	 */
	int getSyncInterval();

}
