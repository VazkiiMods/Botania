/*
 * This class is distributed as part of the Botania Mod.
 * Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 *
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 */
package vazkii.botania.common.item;

import net.minecraft.world.item.ItemStack;

import vazkii.botania.api.block.IFloatingFlower.IslandType;

public interface IFloatingFlowerVariant {
	IslandType getIslandType(ItemStack stack);
}
