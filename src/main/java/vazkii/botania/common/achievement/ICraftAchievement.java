/**
 * This class was created by <Vazkii>. It's distributed as
 * part of the Botania Mod. Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 *
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 *
 * File Created @ [Jan 28, 2015, 5:59:07 PM (GMT)]
 */
package vazkii.botania.common.achievement;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.stats.Achievement;

public interface ICraftAchievement {

	public Achievement getAchievementOnCraft(ItemStack stack, EntityPlayer player, IInventory matrix);

}
