/*
 * This class is distributed as part of the Botania Mod.
 * Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 *
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 */
package org.violetmoon.botania.api.item;

import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;

import org.jetbrains.annotations.ApiStatus;

import org.violetmoon.botania.api.block.Avatar;
import org.violetmoon.botania.api.capability.ItemApiWithContext;
import org.violetmoon.botania.api.mana.ManaReceiver;
import org.violetmoon.botania.common.component.BotaniaDataComponents;

import static org.violetmoon.botania.api.BotaniaAPI.botaniaRL;

/**
 * An Item that has this capability this can be wielded by an Avatar.
 */
public interface AvatarWieldable {

	ResourceLocation ID = botaniaRL("avatar_wieldable");
	ItemApiWithContext<AvatarWieldable, Avatar> LOOKUP = new ItemApiWithContext<>(ID, AvatarWieldable.class, Avatar.class);

	/**
	 * Called on update of the avatar tile.
	 */
	void onAvatarUpdate(ServerLevel level, BlockPos pos, ManaReceiver receiver);

	/**
	 * Gets the overlay resource to render on top of the avatar tile.
	 */
	ResourceLocation getOverlayResource();

	/**
	 * Sets the current game time as the last time this item was activated by the avatar.
	 */
	default void setLastActivationTime(Level level) {
		rod().set(BotaniaDataComponents.LAST_ACTIVATION_TIME, level.getGameTime());
		avatar().markForPersisting();
	}

	/**
	 * Helper method to return the number of ticks that have passed since the last time this item was activated by the
	 * avatar. If the item has never been activated (or at least never stored an activation time), the number of ticks
	 * corresponds go the current game time.
	 */
	default long getTimeSinceLastActivation(Level level) {
		return level.getGameTime() - rod().getOrDefault(BotaniaDataComponents.LAST_ACTIVATION_TIME, 0L);
	}

	// accessors
	@ApiStatus.OverrideOnly
	ItemStack rod();
	@ApiStatus.OverrideOnly
	Avatar avatar();
}
