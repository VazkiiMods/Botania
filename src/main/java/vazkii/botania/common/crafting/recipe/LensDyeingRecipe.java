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
import net.minecraft.recipe.Ingredient;
import net.minecraft.recipe.RecipeSerializer;
import net.minecraft.recipe.SpecialCraftingRecipe;
import net.minecraft.recipe.SpecialRecipeSerializer;
import net.minecraft.util.Identifier;
import net.minecraft.util.Lazy;
import net.minecraft.world.World;

import vazkii.botania.api.mana.ILens;
import vazkii.botania.common.item.ModItems;
import vazkii.botania.common.item.lens.ItemLens;

import javax.annotation.Nonnull;

import java.util.Arrays;
import java.util.List;

public class LensDyeingRecipe extends SpecialCraftingRecipe {
	public static final SpecialRecipeSerializer<LensDyeingRecipe> SERIALIZER = new SpecialRecipeSerializer<>(LensDyeingRecipe::new);

	private final Lazy<List<Ingredient>> dyes = new Lazy<>(() -> Arrays.asList(
			Ingredient.fromTag(Tags.Items.DYES_WHITE), Ingredient.fromTag(Tags.Items.DYES_ORANGE),
			Ingredient.fromTag(Tags.Items.DYES_MAGENTA), Ingredient.fromTag(Tags.Items.DYES_LIGHT_BLUE),
			Ingredient.fromTag(Tags.Items.DYES_YELLOW), Ingredient.fromTag(Tags.Items.DYES_LIME),
			Ingredient.fromTag(Tags.Items.DYES_PINK), Ingredient.fromTag(Tags.Items.DYES_GRAY),
			Ingredient.fromTag(Tags.Items.DYES_LIGHT_GRAY), Ingredient.fromTag(Tags.Items.DYES_CYAN),
			Ingredient.fromTag(Tags.Items.DYES_PURPLE), Ingredient.fromTag(Tags.Items.DYES_BLUE),
			Ingredient.fromTag(Tags.Items.DYES_BROWN), Ingredient.fromTag(Tags.Items.DYES_GREEN),
			Ingredient.fromTag(Tags.Items.DYES_RED), Ingredient.fromTag(Tags.Items.DYES_BLACK),
			Ingredient.ofItems(ModItems.manaPearl)
	));

	public LensDyeingRecipe(Identifier id) {
		super(id);
	}

	@Nonnull
	@Override
	public RecipeSerializer<?> getSerializer() {
		return SERIALIZER;
	}

	@Override
	public boolean matches(@Nonnull CraftingInventory inv, @Nonnull World world) {
		boolean foundLens = false;
		boolean foundDye = false;

		for (int i = 0; i < inv.size(); i++) {
			ItemStack stack = inv.getStack(i);
			if (!stack.isEmpty()) {
				if (stack.getItem() instanceof ILens && !foundLens) {
					foundLens = true;
				} else if (!foundDye) {
					int color = getStackColor(stack);
					if (color > -1) {
						foundDye = true;
					} else {
						return false;
					}
				} else {
					return false;//This means we have an additional item in the recipe after the lens and dye
				}
			}
		}

		return foundLens && foundDye;
	}

	@Nonnull
	@Override
	public ItemStack getCraftingResult(@Nonnull CraftingInventory inv) {
		ItemStack lens = ItemStack.EMPTY;
		int color = -1;

		for (int i = 0; i < inv.size(); i++) {
			ItemStack stack = inv.getStack(i);
			if (!stack.isEmpty()) {
				if (stack.getItem() instanceof ILens && lens.isEmpty()) {
					lens = stack;
				} else {
					color = getStackColor(stack);//We can assume if its not a lens its a dye because we checked it in matches()
				}
			}
		}

		if (lens.getItem() instanceof ILens) {
			ItemStack lensCopy = lens.copy();
			ItemLens.setLensColor(lensCopy, color);

			return lensCopy;
		}

		return ItemStack.EMPTY;
	}

	@Override
	public boolean fits(int width, int height) {
		return width * height >= 2;
	}

	private int getStackColor(ItemStack stack) {
		List<Ingredient> dyes = this.dyes.get();
		for (int i = 0; i < dyes.size(); i++) {
			if (dyes.get(i).test(stack)) {
				return i;
			}
		}

		return -1;
	}
}
