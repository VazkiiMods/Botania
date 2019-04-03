/**
 * This class was created by <WireSegal>. It's distributed as
 * part of the Botania Mod. Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 *
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 *
 * File Created @ [Apr 4, 2016, 1:20:34 PM (EST)]
 */
package vazkii.botania.common.crafting.recipe;

import com.google.gson.JsonObject;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.item.crafting.IRecipeSerializer;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.item.crafting.RecipeSerializers;
import net.minecraft.item.crafting.ShapelessRecipe;
import net.minecraft.network.PacketBuffer;
import net.minecraft.util.NonNullList;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;
import vazkii.botania.common.lib.LibMisc;

import javax.annotation.Nonnull;

public class ShapelessManaUpgradeRecipe implements IRecipe {
	private static final ResourceLocation TYPE_ID = new ResourceLocation(LibMisc.MOD_ID, "mana_upgrade_shapeless");

	private final ShapelessRecipe compose;
	public ShapelessManaUpgradeRecipe(ShapelessRecipe compose) {
		this.compose = compose;
	}

	@Override
	public boolean matches(@Nonnull IInventory inv, @Nonnull World world) {
		return compose.matches(inv, world);
	}

	@Nonnull
	@Override
	public ItemStack getCraftingResult(@Nonnull IInventory inv) {
		return ManaUpgradeRecipe.output(compose.getCraftingResult(inv), inv);
	}

	@Override
	public boolean canFit(int width, int height) {
		return compose.canFit(width, height);
	}

	@Nonnull
	@Override
	public ItemStack getRecipeOutput() {
		return compose.getRecipeOutput();
	}

	@Nonnull
	@Override
	public NonNullList<Ingredient> getIngredients() {
		return compose.getIngredients();
	}

	@Nonnull
	@Override
	public ResourceLocation getId() {
		return compose.getId();
	}

	@Nonnull
	@Override
	public IRecipeSerializer<?> getSerializer() {
		return SERIALIZER;
	}

	public static final IRecipeSerializer<ShapelessManaUpgradeRecipe> SERIALIZER = new IRecipeSerializer<ShapelessManaUpgradeRecipe>() {
		@Nonnull
		@Override
		public ShapelessManaUpgradeRecipe read(@Nonnull ResourceLocation recipeId, @Nonnull JsonObject json) {
			return new ShapelessManaUpgradeRecipe(RecipeSerializers.CRAFTING_SHAPELESS.read(recipeId, json));
		}

		@Nonnull
		@Override
		public ShapelessManaUpgradeRecipe read(@Nonnull ResourceLocation recipeId, @Nonnull PacketBuffer buffer) {
			return new ShapelessManaUpgradeRecipe(RecipeSerializers.CRAFTING_SHAPELESS.read(recipeId, buffer));
		}

		@SuppressWarnings("unchecked")
		@Override
		public void write(@Nonnull PacketBuffer buffer, @Nonnull ShapelessManaUpgradeRecipe recipe) {
			IRecipeSerializer<ShapelessRecipe> compose = (IRecipeSerializer<ShapelessRecipe>) recipe.compose.getSerializer();
			compose.write(buffer, recipe.compose);
		}

		@Nonnull
		@Override
		public ResourceLocation getName() {
			return TYPE_ID;
		}
	};
}
