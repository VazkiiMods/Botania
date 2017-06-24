/**
 * This class was created by <WireSegal>. It's distributed as
 * part of the Botania Mod. Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 *
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 *
 * File Created @ [Apr 4, 2016, 1:20:34 PM (EST)]
 */
package vazkii.botania.common.crafting.recipe;

import net.minecraft.inventory.InventoryCrafting;
import net.minecraft.item.ItemStack;
import net.minecraftforge.oredict.ShapelessOreRecipe;

public class ShapelessManaUpgradeRecipe extends ShapelessOreRecipe {
	public ShapelessManaUpgradeRecipe(ItemStack output, Object... inputs) {
		super(null, output, inputs);
	}

	@Override
	public ItemStack getCraftingResult(InventoryCrafting var1) {
		return ManaUpgradeRecipe.output(output, var1);
	}
}
