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
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.SimpleRecipeSerializer;
import net.minecraft.world.level.Level;

import org.jetbrains.annotations.NotNull;

import vazkii.botania.common.item.ItemLaputaShard;
import vazkii.botania.common.item.ModItems;

public class LaputaShardUpgradeRecipe extends CustomRecipe {
	public static final SimpleRecipeSerializer<LaputaShardUpgradeRecipe> SERIALIZER = new SimpleRecipeSerializer<>(LaputaShardUpgradeRecipe::new);

	public LaputaShardUpgradeRecipe(ResourceLocation id) {
		super(id);
	}

	@Override
	public boolean matches(@NotNull CraftingContainer inv, @NotNull Level worldIn) {
		boolean foundShard = false;
		boolean foundSpirit = false;
		for (int i = 0; i < inv.getContainerSize(); i++) {
			ItemStack stack = inv.getItem(i);
			if (stack.isEmpty()) {
				continue;
			}
			if (stack.is(ModItems.laputaShard) && !foundShard
					&& ItemLaputaShard.getShardLevel(stack) < 19) {
				foundShard = true;
			} else if (stack.is(ModItems.lifeEssence) && !foundSpirit) {
				foundSpirit = true;
			} else {
				return false;
			}
		}
		return foundShard && foundSpirit;
	}

	@NotNull
	@Override
	public ItemStack getResultItem() {
		return new ItemStack(ModItems.laputaShard);
	}

	@NotNull
	@Override
	public NonNullList<Ingredient> getIngredients() {
		return NonNullList.of(Ingredient.EMPTY,
				Ingredient.of(ModItems.laputaShard),
				Ingredient.of(ModItems.lifeEssence));
	}

	@NotNull
	@Override
	public ItemStack assemble(@NotNull CraftingContainer inv) {
		for (int i = 0; i < inv.getContainerSize(); i++) {
			ItemStack stack = inv.getItem(i);
			if (stack.is(ModItems.laputaShard)) {
				ItemStack result = stack.copy();
				result.getOrCreateTag().putInt(ItemLaputaShard.TAG_LEVEL, ItemLaputaShard.getShardLevel(stack) + 1);
				return result;
			}
		}
		return ItemStack.EMPTY;
	}

	@Override
	public boolean canCraftInDimensions(int width, int height) {
		return width * height >= 2;
	}

	@NotNull
	@Override
	public RecipeSerializer<?> getSerializer() {
		return SERIALIZER;
	}
}
