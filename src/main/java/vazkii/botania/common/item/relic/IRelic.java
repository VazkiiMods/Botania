/**
 * This class was created by <Vazkii>. It's distributed as
 * part of the Botania Mod. Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 * 
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 * 
 * File Created @ [Mar 29, 2015, 7:17:41 PM (GMT)]
 */
package vazkii.botania.common.item.relic;

import net.minecraft.item.ItemStack;
import net.minecraft.stats.Achievement;

public interface IRelic {

	public String getSoulbindUsername(ItemStack stack);
	
	public void setBindAchievement(Achievement achievement);
	
	public Achievement getBindAchievement();
	
}
