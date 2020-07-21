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

import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.inventory.CraftingInventory;
import net.minecraft.item.ArmorItem;
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

import javax.annotation.Nonnull;

public class ArmorUpgradeRecipe implements CraftingRecipe {
	private final ShapedRecipe compose;

	public ArmorUpgradeRecipe(ShapedRecipe compose) {
		this.compose = compose;
	}

	@Override
	public boolean matches(@Nonnull CraftingInventory inv, @Nonnull World world) {
		return compose.matches(inv, world);
	}

	@Nonnull
	@Override
	public ItemStack getCraftingResult(@Nonnull CraftingInventory inv) {
		ItemStack out = compose.craft(inv);
		for (int i = 0; i < inv.size(); i++) {
			ItemStack stack = inv.getStack(i);
			if (!stack.isEmpty() && stack.getItem() instanceof ArmorItem && stack.hasTag()) {
				EnchantmentHelper.set(EnchantmentHelper.get(stack), out);
				break;
			}
		}
		return out;
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
	public DefaultedList<Ingredient> getPreviewInputs() {
		return compose.getPreviewInputs();
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

	public static final RecipeSerializer<ArmorUpgradeRecipe> SERIALIZER = new Serializer();

	private static class Serializer extends ForgeRegistryEntry<RecipeSerializer<?>> implements RecipeSerializer<ArmorUpgradeRecipe> {
		@Override
		public ArmorUpgradeRecipe read(@Nonnull Identifier recipeId, @Nonnull JsonObject json) {
			return new ArmorUpgradeRecipe(RecipeSerializer.SHAPED.read(recipeId, json));
		}

		@Override
		public ArmorUpgradeRecipe read(@Nonnull Identifier recipeId, @Nonnull PacketByteBuf buffer) {
			return new ArmorUpgradeRecipe(RecipeSerializer.SHAPED.read(recipeId, buffer));
		}

		@Override
		public void write(@Nonnull PacketByteBuf buffer, @Nonnull ArmorUpgradeRecipe recipe) {
			RecipeSerializer.SHAPED.write(buffer, recipe.compose);
		}
	};
}
