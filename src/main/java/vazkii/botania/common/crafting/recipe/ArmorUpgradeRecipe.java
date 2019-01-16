/**
 * This class was created by <WireSegal>. It's distributed as
 * part of the Botania Mod. Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 *
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 *
 * File Created @ [Apr 4, 2016, 7:49:08 PM (EST)]
 */
package vazkii.botania.common.crafting.recipe;

import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.inventory.InventoryCrafting;
import net.minecraft.item.ItemArmor;
import net.minecraft.item.ItemStack;
import net.minecraftforge.oredict.ShapedOreRecipe;
import vazkii.botania.common.Botania;

public class ArmorUpgradeRecipe extends ShapedOreRecipe {

	public ArmorUpgradeRecipe(ItemStack output, Object... inputs) {
		super(null, output, inputs);
	}

	@Override
	public ItemStack getCraftingResult(InventoryCrafting var1) {
		ItemStack out = output.copy();
		for (int i = 0; i < var1.getSizeInventory(); i++) {
			ItemStack stack = var1.getStackInSlot(i);
			if (!stack.isEmpty() && stack.getItem() instanceof ItemArmor && stack.hasTagCompound()) {
				EnchantmentHelper.setEnchantments(EnchantmentHelper.getEnchantments(stack), out);
				if(Botania.thaumcraftLoaded)
					HelmRevealingRecipe.copyTCData(stack, out);
				break;
			}
		}
		return out;
	}
}
