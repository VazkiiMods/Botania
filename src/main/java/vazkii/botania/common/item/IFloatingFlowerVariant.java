/**
 * This class was created by <WireSegal>. It's distributed as
 * part of the Botania Mod. Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 *
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 *
 * File Created @ [Jan 13, 2016, 6:02:31 PM (GMT)]
 */
package vazkii.botania.common.item;

import net.minecraft.item.ItemStack;
import vazkii.botania.api.item.IFloatingFlower.IslandType;

public interface IFloatingFlowerVariant {
	public IslandType getIslandType(ItemStack stack);
}
