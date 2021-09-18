/*
 * This class is distributed as part of the Botania Mod.
 * Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 *
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 */
package vazkii.botania.api.internal;

import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.projectile.ThrowableProjectile;
import net.minecraft.world.item.ItemStack;

import javax.annotation.Nullable;

import java.util.UUID;

/**
 * Interface for the Mana Burst entity. This can safely be casted to EntityThrowable.
 */
public interface IManaBurst {

	boolean isFake();

	int getColor();

	void setColor(int color);

	int getMana();

	void setMana(int mana);

	int getStartingMana();

	void setStartingMana(int mana);

	int getMinManaLoss();

	void setMinManaLoss(int minManaLoss);

	float getManaLossPerTick();

	void setManaLossPerTick(float mana);

	float getBurstGravity();

	void setGravity(float gravity);

	BlockPos getBurstSourceBlockPos();

	void setBurstSourceCoords(BlockPos pos);

	ItemStack getSourceLens();

	void setSourceLens(ItemStack lens);

	boolean hasAlreadyCollidedAt(BlockPos pos);

	void setCollidedAt(BlockPos pos);

	int getTicksExisted();

	void setFake(boolean fake);

	void setShooterUUID(UUID uuid);

	UUID getShooterUUID();

	void ping();

	/**
	 * @return True if a warp lens has already warped this burst once
	 */
	boolean hasWarped();

	void setWarped(boolean warped);

	int getOrbitTime();

	void setOrbitTime(int time);

	/**
	 * @return Whether an entity has tripped this burst for the tripwire lens
	 */
	boolean hasTripped();

	void setTripped(boolean tripped);

	/**
	 * @return The position this burst is magnetized towards
	 */
	@Nullable
	BlockPos getMagnetizedPos();

	void setMagnetizePos(@Nullable BlockPos pos);

	/**
	 * @return this Mana Burst as an ThrowableEntity
	 */
	default ThrowableProjectile entity() {
		return (ThrowableProjectile) this;
	}
}
