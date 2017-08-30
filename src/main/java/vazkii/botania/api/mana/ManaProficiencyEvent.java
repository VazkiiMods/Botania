/**
 * This class was created by <Flaxbeard>. It's distributed as
 * part of the Botania Mod. Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 * 
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 * 
 * File Created @ [Oct 4, 2016, 10:46:12 PM (GMT)]
 */
package vazkii.botania.api.mana;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.common.eventhandler.Event;

public class ManaProficiencyEvent extends Event {

	private final EntityPlayer entityPlayer;
	private ItemStack rod;
	private boolean proficient;

	public ManaProficiencyEvent(EntityPlayer entityPlayer, ItemStack rod, boolean proficient) {
		this.entityPlayer = entityPlayer;
		this.rod = rod;
		this.proficient = proficient;
	}
	
	public EntityPlayer getEntityPlayer() {
		return entityPlayer;
	}
	
	public ItemStack getRod() {
		return rod;
	}
	
	public boolean isProficient() {
		return proficient;
	}
	
	public void setProficient(boolean proficient) {
		this.proficient = proficient;
	}
}
