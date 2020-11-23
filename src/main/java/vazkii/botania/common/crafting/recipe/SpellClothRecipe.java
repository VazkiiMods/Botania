/*
 * This class is distributed as part of the Botania Mod.
 * Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 *
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 */
package vazkii.botania.common.crafting.recipe;

import net.minecraft.inventory.CraftingInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.recipe.RecipeSerializer;
import net.minecraft.recipe.SpecialCraftingRecipe;
import net.minecraft.recipe.SpecialRecipeSerializer;
import net.minecraft.util.Identifier;
import net.minecraft.util.collection.DefaultedList;
import net.minecraft.world.World;

import vazkii.botania.common.item.ModItems;

import javax.annotation.Nonnull;

public class SpellClothRecipe extends SpecialCraftingRecipe {
	public static final SpecialRecipeSerializer<SpellClothRecipe> SERIALIZER = new SpecialRecipeSerializer<>(SpellClothRecipe::new);

	public SpellClothRecipe(Identifier id) {
		super(id);
	}

	@Override
	public boolean matches(@Nonnull CraftingInventory inv, @Nonnull World world) {
		boolean foundCloth = false;
		boolean foundEnchanted = false;

		for (int i = 0; i < inv.size(); i++) {
			ItemStack stack = inv.getStack(i);
			if (!stack.isEmpty()) {
				if (stack.hasEnchantments() && !foundEnchanted && stack.getItem() != ModItems.spellCloth) {
					foundEnchanted = true;
				} else if (stack.getItem() == ModItems.spellCloth && !foundCloth) {
					foundCloth = true;
				} else {
					return false; // Found an invalid item, breaking the recipe
				}
			}
		}

		return foundCloth && foundEnchanted;
	}

	@Nonnull
	@Override
	public ItemStack craft(@Nonnull CraftingInventory inv) {
		ItemStack stackToDisenchant = ItemStack.EMPTY;
		for (int i = 0; i < inv.size(); i++) {
			ItemStack stack = inv.getStack(i);
			if (!stack.isEmpty() && stack.hasEnchantments() && stack.getItem() != ModItems.spellCloth) {
				stackToDisenchant = stack.copy();
				stackToDisenchant.setCount(1);
				break;
			}
		}

		if (stackToDisenchant.isEmpty()) {
			return ItemStack.EMPTY;
		}

		stackToDisenchant.removeSubTag("Enchantments"); // Remove enchantments
		stackToDisenchant.removeSubTag("RepairCost");
		return stackToDisenchant;
	}

	@Override
	public boolean fits(int width, int height) {
		return width * height >= 2;
	}

	@Nonnull
	@Override
	public RecipeSerializer<?> getSerializer() {
		return SERIALIZER;
	}

	@Nonnull
	@Override
	public DefaultedList<ItemStack> getRemainingStacks(@Nonnull CraftingInventory inv) {
		return RecipeUtils.getRemainingItemsSub(inv, s -> {
			if (s.getItem() == ModItems.spellCloth) {
				ItemStack copy = s.copy();
				copy.setDamage(copy.getDamage() + 1);
				return copy;
			}
			return null;
		});
	}
}
