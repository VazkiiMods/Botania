/**
 * This class was created by <Vazkii>. It's distributed as
 * part of the Botania Mod. Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 *
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 *
 * File Created @ [Jun 29, 2015, 4:45:25 PM (GMT)]
 */
package vazkii.botania.client.challenge;

import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;

public class Challenge {

	public final String unlocalizedName;
	public final ItemStack icon;
	public final EnumChallengeLevel level;
	public boolean complete = false;

	public Challenge(String unlocalizedName, ItemStack icon, EnumChallengeLevel level) {
		this.unlocalizedName = unlocalizedName;
		this.icon = icon;
		this.level = level;
	}

	public void writeToNBT(NBTTagCompound cmp) {
		cmp.setBoolean(unlocalizedName, complete);
	}

	public void readFromNBT(NBTTagCompound cmp) {
		complete = cmp.getBoolean(unlocalizedName);
	}

}
