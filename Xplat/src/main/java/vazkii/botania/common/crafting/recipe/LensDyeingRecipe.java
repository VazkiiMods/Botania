/*
 * This class is distributed as part of the Botania Mod.
 * Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 *
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 */
package vazkii.botania.common.crafting.recipe;

import com.google.common.base.Suppliers;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.inventory.CraftingContainer;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.CustomRecipe;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.SimpleRecipeSerializer;
import net.minecraft.world.level.Level;

import vazkii.botania.api.mana.ILens;
import vazkii.botania.common.item.ModItems;
import vazkii.botania.common.item.lens.ItemLens;

import javax.annotation.Nonnull;

import java.util.Arrays;
import java.util.List;
import java.util.function.Supplier;

public class LensDyeingRecipe extends CustomRecipe {
	public static final SimpleRecipeSerializer<LensDyeingRecipe> SERIALIZER = new SimpleRecipeSerializer<>(LensDyeingRecipe::new);

	private final Supplier<List<Ingredient>> dyes = Suppliers.memoize(() -> Arrays.asList(
			Ingredient.of(Items.WHITE_DYE), Ingredient.of(Items.ORANGE_DYE),
			Ingredient.of(Items.MAGENTA_DYE), Ingredient.of(Items.LIGHT_BLUE_DYE),
			Ingredient.of(Items.YELLOW_DYE), Ingredient.of(Items.LIME_DYE),
			Ingredient.of(Items.PINK_DYE), Ingredient.of(Items.GRAY_DYE),
			Ingredient.of(Items.LIGHT_GRAY_DYE), Ingredient.of(Items.CYAN_DYE),
			Ingredient.of(Items.PURPLE_DYE), Ingredient.of(Items.BLUE_DYE),
			Ingredient.of(Items.BROWN_DYE), Ingredient.of(Items.GREEN_DYE),
			Ingredient.of(Items.RED_DYE), Ingredient.of(Items.BLACK_DYE),
			Ingredient.of(ModItems.manaPearl)
	));

	public LensDyeingRecipe(ResourceLocation id) {
		super(id);
	}

	@Nonnull
	@Override
	public RecipeSerializer<?> getSerializer() {
		return SERIALIZER;
	}

	@Override
	public boolean matches(@Nonnull CraftingContainer inv, @Nonnull Level world) {
		boolean foundLens = false;
		boolean foundDye = false;

		for (int i = 0; i < inv.getContainerSize(); i++) {
			ItemStack stack = inv.getItem(i);
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
	public ItemStack assemble(@Nonnull CraftingContainer inv) {
		ItemStack lens = ItemStack.EMPTY;
		int color = -1;

		for (int i = 0; i < inv.getContainerSize(); i++) {
			ItemStack stack = inv.getItem(i);
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
			lensCopy.setCount(1);
			ItemLens.setLensColor(lensCopy, color);

			return lensCopy;
		}

		return ItemStack.EMPTY;
	}

	@Override
	public boolean canCraftInDimensions(int width, int height) {
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
