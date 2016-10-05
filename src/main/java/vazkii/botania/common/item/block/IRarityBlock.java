/**
 * This class was created by <Vazkii>. It's distributed as
 * part of the Botania Mod. Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 *
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 *
 * File Created @ [Mar 31, 2015, 9:00:38 PM (GMT)]
 */
package vazkii.botania.common.item.block;

import net.minecraft.item.EnumRarity;
import net.minecraft.item.ItemStack;

public interface IRarityBlock {

	public EnumRarity getRarity(ItemStack stack);

}
