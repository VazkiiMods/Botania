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
import net.minecraft.util.collection.DefaultedList;
import net.minecraft.world.World;

import vazkii.botania.common.item.ItemLaputaShard;
import vazkii.botania.common.item.ModItems;

import javax.annotation.Nonnull;

public class LaputaShardUpgradeRecipe extends SpecialCraftingRecipe {
	public static final SpecialRecipeSerializer<LaputaShardUpgradeRecipe> SERIALIZER = new SpecialRecipeSerializer<>(LaputaShardUpgradeRecipe::new);

	public LaputaShardUpgradeRecipe(Identifier id) {
		super(id);
	}

	@Override
	public boolean matches(@Nonnull CraftingInventory inv, @Nonnull World worldIn) {
		boolean foundShard = false;
		boolean foundSpirit = false;
		for (int i = 0; i < inv.size(); i++) {
			ItemStack stack = inv.getStack(i);
			if (stack.isEmpty()) {
				continue;
			}
			if (stack.getItem() == ModItems.laputaShard && !foundShard
					&& ItemLaputaShard.getShardLevel(stack) < 19) {
				foundShard = true;
			} else if (stack.getItem() == ModItems.lifeEssence && !foundSpirit) {
				foundSpirit = true;
			} else {
				return false;
			}
		}
		return foundShard && foundSpirit;
	}

	@Nonnull
	@Override
	public ItemStack getOutput() {
		return new ItemStack(ModItems.laputaShard);
	}

	@Nonnull
	@Override
	public DefaultedList<Ingredient> getPreviewInputs() {
		return DefaultedList.copyOf(Ingredient.EMPTY,
				Ingredient.ofItems(ModItems.laputaShard),
				Ingredient.ofItems(ModItems.lifeEssence));
	}

	@Nonnull
	@Override
	public ItemStack craft(@Nonnull CraftingInventory inv) {
		for (int i = 0; i < inv.size(); i++) {
			ItemStack stack = inv.getStack(i);
			if (stack.getItem() == ModItems.laputaShard) {
				ItemStack result = stack.copy();
				result.getOrCreateTag().putInt(ItemLaputaShard.TAG_LEVEL, ItemLaputaShard.getShardLevel(stack) + 1);
				return result;
			}
		}
		return ItemStack.EMPTY;
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
}
