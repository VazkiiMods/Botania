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
import net.minecraft.item.crafting.ICraftingRecipe;
import net.minecraft.item.crafting.IRecipeSerializer;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.item.crafting.ShapedRecipe;
import net.minecraft.network.PacketBuffer;
import net.minecraft.util.NonNullList;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;
import net.minecraftforge.registries.ForgeRegistryEntry;

import vazkii.botania.common.block.decor.BlockModMushroom;
import vazkii.botania.common.item.ItemTwigWand;
import vazkii.botania.common.item.ModItems;
import vazkii.botania.common.item.material.ItemPetal;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public class TwigWandRecipe implements ICraftingRecipe {
	public static final IRecipeSerializer<TwigWandRecipe> SERIALIZER = new Serializer();
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
		for (int i = 0; i < inv.getSizeInventory(); i++) {
			ItemStack stack = inv.getStackInSlot(i);
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
	public boolean canFit(int width, int height) {
		return compose.canFit(width, height);
	}

	@Nonnull
	@Override
	public ItemStack getRecipeOutput() {
		return new ItemStack(ModItems.twigWand);
	}

	@Nonnull
	@Override
	public ResourceLocation getId() {
		return compose.getId();
	}

	@Nonnull
	@Override
	public NonNullList<ItemStack> getRemainingItems(CraftingInventory inv) {
		return compose.getRemainingItems(inv);
	}

	@Nonnull
	@Override
	public NonNullList<Ingredient> getIngredients() {
		return compose.getIngredients();
	}

	@Nonnull
	@Override
	public String getGroup() {
		return compose.getGroup();
	}

	@Nonnull
	@Override
	public ItemStack getIcon() {
		return compose.getIcon();
	}

	@Nonnull
	@Override
	public IRecipeSerializer<?> getSerializer() {
		return SERIALIZER;
	}

	private static class Serializer extends ForgeRegistryEntry<IRecipeSerializer<?>> implements IRecipeSerializer<TwigWandRecipe> {
		@Nonnull
		@Override
		public TwigWandRecipe read(@Nonnull ResourceLocation recipeId, @Nonnull JsonObject json) {
			return new TwigWandRecipe(CRAFTING_SHAPED.read(recipeId, json));
		}

		@Nullable
		@Override
		public TwigWandRecipe read(@Nonnull ResourceLocation recipeId, @Nonnull PacketBuffer buffer) {
			return new TwigWandRecipe(CRAFTING_SHAPED.read(recipeId, buffer));
		}

		@Override
		public void write(@Nonnull PacketBuffer buffer, @Nonnull TwigWandRecipe recipe) {
			CRAFTING_SHAPED.write(buffer, recipe.compose);
		}
	}
}
