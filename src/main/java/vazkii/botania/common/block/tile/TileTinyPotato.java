/**
 * This class was created by <Vazkii>. It's distributed as
 * part of the Botania Mod. Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 * 
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 * 
 * File Created @ [Jul 18, 2014, 8:05:08 PM (GMT)]
 */
package vazkii.botania.common.block.tile;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.SoundCategory;
import vazkii.botania.api.sound.BotaniaSoundEvents;


public class TileTinyPotato extends TileMod {

	private static final String TAG_NAME = "name";

	public int jumpTicks = 0;
	public String name = "";
	public int nextDoIt = 0;

	public void interact() {
		jump();
		if(name.toLowerCase().trim().endsWith("shia labeouf") && !worldObj.isRemote && nextDoIt == 0) {
			nextDoIt = 40;
			worldObj.playSound(null, pos, BotaniaSoundEvents.doit, SoundCategory.BLOCKS, 1F, 1F);
		}
	}

	public void jump() {
		if(jumpTicks == 0)
			jumpTicks = 20;
	}

	@Override
	public void update() {
		if(worldObj.rand.nextInt(100) == 0)
			jump();

		if(jumpTicks > 0)
			jumpTicks--;
		if(nextDoIt > 0)
			nextDoIt--;
	}

	@Override
	public void writePacketNBT(NBTTagCompound cmp) {
		cmp.setString(TAG_NAME, name);
	}

	@Override
	public void readPacketNBT(NBTTagCompound cmp) {
		name = cmp.getString(TAG_NAME);
	}
}
