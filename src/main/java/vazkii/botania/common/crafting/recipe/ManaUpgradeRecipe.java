/**
 * This class was created by <WireSegal>. It's distributed as
 * part of the Botania Mod. Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 *
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 *
 * File Created @ [Apr 4, 2016, 1:16:22 PM (EST)]
 */
package vazkii.botania.common.crafting.recipe;

import com.google.gson.JsonObject;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.item.crafting.IRecipeSerializer;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.item.crafting.ShapedRecipe;
import net.minecraft.network.PacketBuffer;
import net.minecraft.util.NonNullList;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;
import vazkii.botania.api.mana.IManaItem;
import vazkii.botania.common.lib.LibMisc;

import javax.annotation.Nonnull;

public class ManaUpgradeRecipe implements IRecipe {
	private static final ResourceLocation TYPE_ID = new ResourceLocation(LibMisc.MOD_ID, "mana_upgrade");
	private final ShapedRecipe compose;

	public ManaUpgradeRecipe(ShapedRecipe compose) {
		this.compose = compose;
	}

	public static ItemStack output(ItemStack output, IInventory inv) {
		ItemStack out = output.copy();
		if (!(out.getItem() instanceof IManaItem))
			return out;
		IManaItem outItem = (IManaItem) out.getItem();
		for (int i = 0; i < inv.getSizeInventory(); i++) {
			ItemStack stack = inv.getStackInSlot(i);
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
	public boolean matches(@Nonnull IInventory inv, @Nonnull World world) {
		return compose.matches(inv, world);
	}

	@Nonnull
	@Override
	public ItemStack getCraftingResult(@Nonnull IInventory inv) {
		return output(compose.getCraftingResult(inv), inv);
	}

	@Nonnull
	@Override
	public NonNullList<Ingredient> getIngredients() {
		return compose.getIngredients();
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
	public ResourceLocation getId() {
		return compose.getId();
	}

	@Nonnull
	@Override
	public IRecipeSerializer<?> getSerializer() {
		return SERIALIZER;
	}

	public static final IRecipeSerializer<ManaUpgradeRecipe> SERIALIZER = new IRecipeSerializer<ManaUpgradeRecipe>() {
		@Override
		public ManaUpgradeRecipe read(@Nonnull ResourceLocation recipeId, @Nonnull JsonObject json) {
			return new ManaUpgradeRecipe(RecipeSerializers.CRAFTING_SHAPED.read(recipeId, json));
		}

		@Override
		public ManaUpgradeRecipe read(@Nonnull ResourceLocation recipeId, @Nonnull PacketBuffer buffer) {
			return new ManaUpgradeRecipe(RecipeSerializers.CRAFTING_SHAPED.read(recipeId, buffer));
		}

		@Override
		public void write(@Nonnull PacketBuffer buffer, ManaUpgradeRecipe recipe) {
			RecipeSerializers.CRAFTING_SHAPED.write(buffer, recipe.compose);
		}

		@Nonnull
		@Override
		public ResourceLocation getName() {
			return TYPE_ID;
		}
	};
}
