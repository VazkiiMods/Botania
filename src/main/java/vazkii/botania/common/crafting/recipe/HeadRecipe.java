/*
 * This class is distributed as part of the Botania Mod.
 * Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 *
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 */
package vazkii.botania.common.crafting.recipe;

import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraftforge.items.IItemHandler;

import vazkii.botania.api.recipe.RecipeRuneAltar;
import vazkii.botania.common.core.helper.ItemNBTHelper;

public class HeadRecipe extends RecipeRuneAltar {

	private String name = "";

	public HeadRecipe(ResourceLocation id, ItemStack output, int mana, Ingredient... inputs) {
		super(id, output, mana, inputs);
	}

	@Override
	public boolean matches(IItemHandler inv) {
		boolean matches = super.matches(inv);

		if (matches) {
			for (int i = 0; i < inv.getSlots(); i++) {
				ItemStack stack = inv.getStackInSlot(i);
				if (stack.isEmpty()) {
					break;
				}

				if (stack.getItem() == Items.NAME_TAG) {
					String defaultName = new TranslationTextComponent(Items.NAME_TAG.getTranslationKey()).getString();
					name = stack.getDisplayName().getString();
					if (name.equals(defaultName)) {
						return false;
					}
				}
			}
		}

		return matches;
	}

	@Override
	public ItemStack getOutput() {
		ItemStack stack = new ItemStack(Items.PLAYER_HEAD);
		if (!name.isEmpty()) {
			ItemNBTHelper.setString(stack, "SkullOwner", name);
		}
		return stack;
	}

}
