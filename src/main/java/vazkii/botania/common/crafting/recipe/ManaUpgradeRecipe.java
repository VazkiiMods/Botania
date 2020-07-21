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
import net.minecraft.inventory.Inventory;
import net.minecraft.item.ItemStack;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.recipe.CraftingRecipe;
import net.minecraft.recipe.Ingredient;
import net.minecraft.recipe.RecipeSerializer;
import net.minecraft.recipe.ShapedRecipe;
import net.minecraft.util.Identifier;
import net.minecraft.util.collection.DefaultedList;
import net.minecraft.world.World;

import vazkii.botania.api.mana.IManaItem;

import javax.annotation.Nonnull;

public class ManaUpgradeRecipe implements CraftingRecipe {
	private final ShapedRecipe compose;

	public ManaUpgradeRecipe(ShapedRecipe compose) {
		this.compose = compose;
	}

	public static ItemStack output(ItemStack output, Inventory inv) {
		ItemStack out = output.copy();
		if (!(out.getItem() instanceof IManaItem)) {
			return out;
		}
		IManaItem outItem = (IManaItem) out.getItem();
		for (int i = 0; i < inv.size(); i++) {
			ItemStack stack = inv.getStack(i);
			if (!stack.isEmpty()) {
				if (stack.getItem() instanceof IManaItem) {
					IManaItem item = (IManaItem) stack.getItem();
					outItem.addMana(out, item.getMana(stack));
				}
			}
		}
		return out;
	}

	@Override
	public boolean matches(@Nonnull CraftingInventory inv, @Nonnull World world) {
		return compose.matches(inv, world);
	}

	@Nonnull
	@Override
	public ItemStack craft(@Nonnull CraftingInventory inv) {
		return output(compose.craft(inv), inv);
	}

	@Nonnull
	@Override
	public DefaultedList<Ingredient> getPreviewInputs() {
		return compose.getPreviewInputs();
	}

	@Override
	public boolean fits(int width, int height) {
		return compose.fits(width, height);
	}

	@Nonnull
	@Override
	public ItemStack getOutput() {
		return compose.getOutput();
	}

	@Nonnull
	@Override
	public Identifier getId() {
		return compose.getId();
	}

	@Nonnull
	@Override
	public RecipeSerializer<?> getSerializer() {
		return SERIALIZER;
	}

	public static final RecipeSerializer<ManaUpgradeRecipe> SERIALIZER = new Serializer();

	private static class Serializer implements RecipeSerializer<ManaUpgradeRecipe> {
		@Override
		public ManaUpgradeRecipe read(@Nonnull Identifier recipeId, @Nonnull JsonObject json) {
			return new ManaUpgradeRecipe(RecipeSerializer.SHAPED.read(recipeId, json));
		}

		@Override
		public ManaUpgradeRecipe read(@Nonnull Identifier recipeId, @Nonnull PacketByteBuf buffer) {
			return new ManaUpgradeRecipe(RecipeSerializer.SHAPED.read(recipeId, buffer));
		}

		@Override
		public void write(@Nonnull PacketByteBuf buffer, ManaUpgradeRecipe recipe) {
			RecipeSerializer.SHAPED.write(buffer, recipe.compose);
		}
	};
}
