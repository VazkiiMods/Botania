/**
 * This class was created by <Vazkii>. It's distributed as
 * part of the Botania Mod. Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 *
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 *
 * File Created @ [Feb 5, 2014, 1:41:14 PM (GMT)]
 */
package vazkii.botania.api.recipe;

import net.minecraft.item.ItemStack;

public class RecipeRuneAltar extends RecipePetals {

	private final int mana;

	public RecipeRuneAltar(ItemStack output, int mana, Object... inputs) {
		super(output, inputs);
		this.mana = mana;
	}

	public int getManaUsage() {
		return mana;
	}

}
