/**
 * This class was created by <SoundLogic>. It's distributed as
 * part of the Botania Mod. Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 * 
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 * 
 * File Created @ [Apr 18, 2015, 7:30:00 PM (GMT)]
 */
package vazkii.botania.api.mana;

import org.apache.commons.lang3.tuple.Pair;

import net.minecraft.util.math.BlockPos;
import vazkii.botania.api.internal.IManaBurst;
import vazkii.botania.common.entity.EntityManaBurst;

/**
 * Any TileEntity that implements this is considered a Mana Spreader,
 * by which can fire mana bursts as a spreader.
 */
public interface IManaSpreader extends IManaBlock, IPingable, IDirectioned {

	public void setCanShoot(boolean canShoot);

	public int getBurstParticleTick();

	public void setBurstParticleTick(int i);

	public int getLastBurstDeathTick();

	public void setLastBurstDeathTick(int ticksExisted);
	
	public IManaBurst runBurstSimulation();

}
