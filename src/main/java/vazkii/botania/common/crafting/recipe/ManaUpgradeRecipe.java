/**
 * This class was created by <WireSegal>. It's distributed as
 * part of the Botania Mod. Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 *
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 *
 * File Created @ [Apr 4, 2016, 1:16:22 PM (EST)]
 */
package vazkii.botania.common.crafting.recipe;

import net.minecraft.inventory.InventoryCrafting;
import net.minecraft.item.ItemStack;
import net.minecraftforge.oredict.ShapedOreRecipe;
import vazkii.botania.api.mana.IManaItem;

public class ManaUpgradeRecipe extends ShapedOreRecipe {
	public ManaUpgradeRecipe(ItemStack output, Object... inputs) {
		super(null, output, inputs);
	}

	public static ItemStack output(ItemStack output, InventoryCrafting var1) {
		ItemStack out = output.copy();
		if (!(out.getItem() instanceof IManaItem))
			return out;
		IManaItem outItem = (IManaItem) out.getItem();
		for (int i = 0; i < var1.getSizeInventory(); i++) {
			ItemStack stack = var1.getStackInSlot(i);
			if (!stack.isEmpty()) {
				if (stack.getItem() instanceof IManaItem) {
					IManaItem item = (IManaItem) stack.getItem();
					outItem.addMana(out, item.getMana(stack));
				}
			}
		}
		return out;
	}

	@Override
	public ItemStack getCraftingResult(InventoryCrafting var1) {
		return output(output, var1);
	}
}
