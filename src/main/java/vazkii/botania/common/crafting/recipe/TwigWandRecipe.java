/*
 * This class is distributed as part of the Botania Mod.
 * Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 *
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 */
package vazkii.botania.common.crafting.recipe;

import com.google.gson.JsonObject;

import net.minecraft.inventory.CraftingInventory;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.recipe.CraftingRecipe;
import net.minecraft.recipe.Ingredient;
import net.minecraft.recipe.RecipeSerializer;
import net.minecraft.recipe.ShapedRecipe;
import net.minecraft.util.Identifier;
import net.minecraft.util.collection.DefaultedList;
import net.minecraft.world.World;
import net.minecraftforge.registries.ForgeRegistryEntry;

import vazkii.botania.common.block.decor.BlockModMushroom;
import vazkii.botania.common.item.ItemTwigWand;
import vazkii.botania.common.item.ModItems;
import vazkii.botania.common.item.material.ItemPetal;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public class TwigWandRecipe implements CraftingRecipe {
	public static final RecipeSerializer<TwigWandRecipe> SERIALIZER = new Serializer();
	private final ShapedRecipe compose;

	public TwigWandRecipe(ShapedRecipe compose) {
		this.compose = compose;
	}

	@Override
	public boolean matches(@Nonnull CraftingInventory inv, @Nonnull World worldIn) {
		return compose.matches(inv, worldIn);
	}

	@Nonnull
	@Override
	public ItemStack getCraftingResult(CraftingInventory inv) {
		int first = -1;
		for (int i = 0; i < inv.size(); i++) {
			ItemStack stack = inv.getStack(i);
			Item item = stack.getItem();

			int colorId;
			if (item instanceof ItemPetal) {
				colorId = ((ItemPetal) item).color.getId();
			} else if (item instanceof BlockItem && ((BlockItem) item).getBlock() instanceof BlockModMushroom) {
				colorId = ((BlockModMushroom) ((BlockItem) item).getBlock()).color.getId();
			} else {
				continue;
			}
			if (first == -1) {
				first = colorId;
			} else {
				return ItemTwigWand.forColors(first, colorId);
			}
		}
		return ItemTwigWand.forColors(first != -1 ? first : 0, 0);
	}

	@Override
	public boolean fits(int width, int height) {
		return compose.fits(width, height);
	}

	@Nonnull
	@Override
	public ItemStack getOutput() {
		return new ItemStack(ModItems.twigWand);
	}

	@Nonnull
	@Override
	public Identifier getId() {
		return compose.getId();
	}

	@Nonnull
	@Override
	public DefaultedList<ItemStack> getRemainingItems(CraftingInventory inv) {
		return compose.getRemainingStacks(inv);
	}

	@Nonnull
	@Override
	public DefaultedList<Ingredient> getPreviewInputs() {
		return compose.getPreviewInputs();
	}

	@Nonnull
	@Override
	public String getGroup() {
		return compose.getGroup();
	}

	@Nonnull
	@Override
	public ItemStack getRecipeKindIcon() {
		return compose.getRecipeKindIcon();
	}

	@Nonnull
	@Override
	public RecipeSerializer<?> getSerializer() {
		return SERIALIZER;
	}

	private static class Serializer extends ForgeRegistryEntry<RecipeSerializer<?>> implements RecipeSerializer<TwigWandRecipe> {
		@Nonnull
		@Override
		public TwigWandRecipe read(@Nonnull Identifier recipeId, @Nonnull JsonObject json) {
			return new TwigWandRecipe(SHAPED.read(recipeId, json));
		}

		@Nullable
		@Override
		public TwigWandRecipe read(@Nonnull Identifier recipeId, @Nonnull PacketByteBuf buffer) {
			return new TwigWandRecipe(SHAPED.read(recipeId, buffer));
		}

		@Override
		public void write(@Nonnull PacketByteBuf buffer, @Nonnull TwigWandRecipe recipe) {
			SHAPED.write(buffer, recipe.compose);
		}
	}
}
