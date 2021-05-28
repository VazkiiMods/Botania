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

/**
 * Any TileEntity that implements this is considered a Mana Spreader,
 * by which can fire mana bursts as a spreader.
 */
public interface IManaSpreader extends IManaBlock, IPingable, IDirectioned {

	void setCanShoot(boolean canShoot);

	int getBurstParticleTick();

	void setBurstParticleTick(int i);

	int getLastBurstDeathTick();

	void setLastBurstDeathTick(int ticksExisted);

	IManaBurst runBurstSimulation();

}
