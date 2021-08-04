/*
 * This class is distributed as part of the Botania Mod.
 * Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 *
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 */
package vazkii.botania.common.crafting.recipe;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.GsonHelper;
import net.minecraft.world.Container;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.ShapedRecipe;
import net.minecraft.world.level.Level;

import vazkii.botania.common.core.helper.ItemNBTHelper;
import vazkii.botania.common.crafting.ModRecipeTypes;
import vazkii.botania.common.crafting.RecipeRuneAltar;

import javax.annotation.Nonnull;

import java.util.ArrayList;
import java.util.List;

public class HeadRecipe extends RecipeRuneAltar {

	public HeadRecipe(ResourceLocation id, ItemStack output, int mana, Ingredient... inputs) {
		super(id, output, mana, inputs);
	}

	@Override
	public boolean matches(Container inv, @Nonnull Level world) {
		boolean matches = super.matches(inv, world);

		if (matches) {
			for (int i = 0; i < inv.getContainerSize(); i++) {
				ItemStack stack = inv.getItem(i);
				if (stack.isEmpty()) {
					break;
				}

				if (stack.getItem() == Items.NAME_TAG) {
					String defaultName = new TranslatableComponent(Items.NAME_TAG.getDescriptionId()).getString();
					if (stack.getHoverName().getString().equals(defaultName)) {
						return false;
					}
				}
			}
		}

		return matches;
	}

	@Nonnull
	@Override
	public ItemStack assemble(@Nonnull Container inv) {
		ItemStack stack = getResultItem().copy();
		for (int i = 0; i < inv.getContainerSize(); i++) {
			ItemStack ingr = inv.getItem(i);
			if (ingr.getItem() == Items.NAME_TAG) {
				ItemNBTHelper.setString(stack, "SkullOwner", ingr.getHoverName().getString());
				break;
			}
		}
		return stack;
	}

	public static class Serializer implements RecipeSerializer<HeadRecipe> {

		@Nonnull
		@Override
		public HeadRecipe fromJson(@Nonnull ResourceLocation id, @Nonnull JsonObject json) {
			ItemStack output = ShapedRecipe.itemStackFromJson(GsonHelper.getAsJsonObject(json, "output"));
			int mana = GsonHelper.getAsInt(json, "mana");
			JsonArray ingrs = GsonHelper.getAsJsonArray(json, "ingredients");
			List<Ingredient> inputs = new ArrayList<>();
			for (JsonElement e : ingrs) {
				inputs.add(Ingredient.fromJson(e));
			}
			return new HeadRecipe(id, output, mana, inputs.toArray(new Ingredient[0]));
		}

		@Override
		public HeadRecipe fromNetwork(@Nonnull ResourceLocation id, @Nonnull FriendlyByteBuf buf) {
			Ingredient[] inputs = new Ingredient[buf.readVarInt()];
			for (int i = 0; i < inputs.length; i++) {
				inputs[i] = Ingredient.fromNetwork(buf);
			}
			ItemStack output = buf.readItem();
			int mana = buf.readVarInt();
			return new HeadRecipe(id, output, mana, inputs);
		}

		@Override
		public void toNetwork(@Nonnull FriendlyByteBuf buf, @Nonnull HeadRecipe recipe) {
			ModRecipeTypes.RUNE_SERIALIZER.toNetwork(buf, recipe);
		}
	}

}
