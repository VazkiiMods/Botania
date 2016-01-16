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

/**
 * A in-game challenge as found in the Lexica Botania challenge sections -
 * contains a simple name for the challenge, the display icon, the difficulty,
 * and a marker for completion.
 */
public class Challenge {

	/**
	 * The unlocalized name of this challenge.
	 */
	public final String unlocalizedName;

	/**
	 * The icon to display in the visuals for this challenge.
	 */
	public final ItemStack icon;

	/**
	 * The percieved difficulty of this challenge.
	 */
	public final EnumChallengeLevel level;

	/**
	 * Whether or not this challenge is considered complete
	 * (true if it is, false otherwise).
	 */
	public boolean complete = false;

	/**
	 * Creates a new challenge with the specified arguments.
	 * @param unlocalizedName The unlocalized name of the challenge.
	 * @param icon The icon of the challenge, represented by the icon of the provided item stack.
	 * @param level The difficulty level of the challenge.
	 */
	public Challenge(String unlocalizedName, ItemStack icon, EnumChallengeLevel level) {
		this.unlocalizedName = unlocalizedName;
		this.icon = icon;
		this.level = level;
	}

	/**
	 * Write this challenge to the provided NBT tag.
	 * @param cmp The tag to write this challenge to.
	 */
	public void writeToNBT(NBTTagCompound cmp) {
		cmp.setBoolean(unlocalizedName, complete);
	}

	/**
	 * Read this challenge from the provided NBT tag.
	 * @param cmp The tag to read this challenge from.
	 */
	public void readFromNBT(NBTTagCompound cmp) {
		complete = cmp.getBoolean(unlocalizedName);
	}

}
