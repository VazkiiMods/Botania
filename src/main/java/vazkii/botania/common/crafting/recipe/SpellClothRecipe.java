/*
 * This class is distributed as part of the Botania Mod.
 * Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 *
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 */
package vazkii.botania.common.crafting.recipe;

import net.minecraft.core.NonNullList;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.inventory.CraftingContainer;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.CustomRecipe;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.SimpleRecipeSerializer;
import net.minecraft.world.level.Level;

import vazkii.botania.common.item.ModItems;

import javax.annotation.Nonnull;

public class SpellClothRecipe extends CustomRecipe {
	public static final SimpleRecipeSerializer<SpellClothRecipe> SERIALIZER = new SimpleRecipeSerializer<>(SpellClothRecipe::new);

	public SpellClothRecipe(ResourceLocation id) {
		super(id);
	}

	@Override
	public boolean matches(@Nonnull CraftingContainer inv, @Nonnull Level world) {
		boolean foundCloth = false;
		boolean foundEnchanted = false;

		for (int i = 0; i < inv.getContainerSize(); i++) {
			ItemStack stack = inv.getItem(i);
			if (!stack.isEmpty()) {
				if (stack.isEnchanted() && !foundEnchanted && stack.getItem() != ModItems.spellCloth) {
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
	public ItemStack assemble(@Nonnull CraftingContainer inv) {
		ItemStack stackToDisenchant = ItemStack.EMPTY;
		for (int i = 0; i < inv.getContainerSize(); i++) {
			ItemStack stack = inv.getItem(i);
			if (!stack.isEmpty() && stack.isEnchanted() && stack.getItem() != ModItems.spellCloth) {
				stackToDisenchant = stack.copy();
				stackToDisenchant.setCount(1);
				break;
			}
		}

		if (stackToDisenchant.isEmpty()) {
			return ItemStack.EMPTY;
		}

		stackToDisenchant.removeTagKey("Enchantments"); // Remove enchantments
		stackToDisenchant.removeTagKey("RepairCost");
		return stackToDisenchant;
	}

	@Override
	public boolean canCraftInDimensions(int width, int height) {
		return width * height >= 2;
	}

	@Nonnull
	@Override
	public RecipeSerializer<?> getSerializer() {
		return SERIALIZER;
	}

	@Nonnull
	@Override
	public NonNullList<ItemStack> getRemainingItems(@Nonnull CraftingContainer inv) {
		return RecipeUtils.getRemainingItemsSub(inv, s -> {
			if (s.getItem() == ModItems.spellCloth) {
				ItemStack copy = s.copy();
				copy.setCount(1);
				copy.setDamageValue(copy.getDamageValue() + 1);
				return copy;
			}
			return null;
		});
	}
}
