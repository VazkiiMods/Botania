/*
 * This class is distributed as part of the Botania Mod.
 * Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 *
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 */
package vazkii.botania.common.item.material;

import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;

import vazkii.botania.api.recipe.CustomApothecaryColor;

public class RuneItem extends Item implements CustomApothecaryColor {

	public RuneItem(Item.Properties builder) {
		super(builder);
	}

	@Override
	public int getParticleColor(ItemStack stack) {
		return 0xA8A8A8;
	}

}
