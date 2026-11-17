/*
 * This class is distributed as part of the Botania Mod.
 * Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 *
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 */
package vazkii.botania.api.mana;

import vazkii.botania.api.internal.ManaBurst;

import java.util.UUID;

/**
 * Any {@link ManaCollector} that also implements this is considered a Mana Spreader,
 * by which can fire mana bursts as a spreader.
 */
public interface ManaSpreader extends ManaCollector {

	void setCanShoot(boolean canShoot);

	int getBurstParticleTick();

	void setBurstParticleTick(int i);

	int getLastBurstDeathTick();

	void setLastBurstDeathTick(int ticksExisted);

	ManaBurst runBurstSimulation();

	/**
	 * @return The rotation around the Y axis (horizontal rotation, or "yaw"), in degrees
	 */
	float getYRot();

	/**
	 * @return The rotation around the yaw-rotated left/right axis (vertical rotation, or "pitch"), in degrees
	 */
	float getXRot();

	/**
	 * Set the Y rotation
	 *
	 * @param rot Y rotation, in degrees
	 */
	void setYRot(float rot);

	/**
	 * Set the X rotation
	 *
	 * @param rot X rotation, in degrees
	 */
	void setXRot(float rot);

	/**
	 * This should be called after rotation setting is done to allow
	 * for the block to re-calculate.
	 */
	void commitRedirection();

	/**
	 * Pings this object back, telling it that the burst passed in is still alive
	 * in the world. The UUID parameter should be the UUID with which the burst
	 * was created, this is used to let the object handle the check for if it's the
	 * correct ID internally. ManaBurst implementations should do this every tick.
	 */
	void pingback(ManaBurst burst, UUID expectedIdentity);

	/**
	 * @return A unique and persistent identifier for this spreader
	 */
	UUID getIdentifier();
}
