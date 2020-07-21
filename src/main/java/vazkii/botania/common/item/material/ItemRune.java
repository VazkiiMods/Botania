/*
 * This class is distributed as part of the Botania Mod.
 * Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 *
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 */
package vazkii.botania.common.item.material;

import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

import vazkii.botania.api.recipe.ICustomApothecaryColor;

public class ItemRune extends Item implements ICustomApothecaryColor {

	public ItemRune(Item.Settings builder) {
		super(builder);
	}

	@Override
	public int getParticleColor(ItemStack stack) {
		return 0xA8A8A8;
	}

}
