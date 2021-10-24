/*
 * This class is distributed as part of the Botania Mod.
 * Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 *
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 */
package vazkii.botania.api.mana;

import vazkii.botania.api.internal.IManaBurst;

import java.util.UUID;

/**
 * Any TileEntity that implements this is considered a Mana Spreader,
 * by which can fire mana bursts as a spreader.
 */
public interface IManaSpreader extends IManaBlock {

	void setCanShoot(boolean canShoot);

	int getBurstParticleTick();

	void setBurstParticleTick(int i);

	int getLastBurstDeathTick();

	void setLastBurstDeathTick(int ticksExisted);

	IManaBurst runBurstSimulation();

	/**
	 * @return The X rotation, in degrees
	 */
	float getRotationX();

	/**
	 * @return The Y rotation, in degrees
	 */
	float getRotationY();

	/**
	 * Set the X rotation
	 *
	 * @param rot X rotation, in degrees
	 */
	void setRotationX(float rot);

	/**
	 * Set the Y rotation
	 *
	 * @param rot Y rotation, in degrees
	 */
	void setRotationY(float rot);

	/**
	 * This should be called after rotation setting is done to allow
	 * for the block to re-calculate.
	 */
	void commitRedirection();

	/**
	 * Pings this object back, telling it that the burst passed in is still alive
	 * in the world. The UUID parameter should be the UUID with which the burst
	 * was created, this is used to let the object handle the check for if it's the
	 * correct ID internally. IManaBurst implementations should do this every tick.
	 */
	void pingback(IManaBurst burst, UUID expectedIdentity);

	/**
	 * @return A unique and persistent identifier for this spreader
	 */
	UUID getIdentifier();
}
