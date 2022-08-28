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
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.GsonHelper;
import net.minecraft.world.Container;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.ShapedRecipe;
import net.minecraft.world.level.Level;

import org.jetbrains.annotations.NotNull;

import vazkii.botania.common.crafting.ModRecipeTypes;
import vazkii.botania.common.crafting.RecipeRuneAltar;
import vazkii.botania.common.crafting.RecipeSerializerBase;
import vazkii.botania.common.helper.ItemNBTHelper;

import java.util.ArrayList;
import java.util.List;

public class HeadRecipe extends RecipeRuneAltar {

	public HeadRecipe(ResourceLocation id, ItemStack output, int mana, Ingredient... inputs) {
		super(id, output, mana, inputs);
	}

	@Override
	public boolean matches(Container inv, @NotNull Level world) {
		boolean matches = super.matches(inv, world);

		if (matches) {
			for (int i = 0; i < inv.getContainerSize(); i++) {
				ItemStack stack = inv.getItem(i);
				if (stack.isEmpty()) {
					break;
				}

				if (stack.is(Items.NAME_TAG)) {
					String defaultName = Component.translatable(Items.NAME_TAG.getDescriptionId()).getString();
					if (stack.getHoverName().getString().equals(defaultName)) {
						return false;
					}
				}
			}
		}

		return matches;
	}

	@NotNull
	@Override
	public ItemStack assemble(@NotNull Container inv) {
		ItemStack stack = getResultItem().copy();
		for (int i = 0; i < inv.getContainerSize(); i++) {
			ItemStack ingr = inv.getItem(i);
			if (ingr.is(Items.NAME_TAG)) {
				ItemNBTHelper.setString(stack, "SkullOwner", ingr.getHoverName().getString());
				break;
			}
		}
		return stack;
	}

	public static class Serializer extends RecipeSerializerBase<HeadRecipe> {

		@NotNull
		@Override
		public HeadRecipe fromJson(@NotNull ResourceLocation id, @NotNull JsonObject json) {
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
		public HeadRecipe fromNetwork(@NotNull ResourceLocation id, @NotNull FriendlyByteBuf buf) {
			Ingredient[] inputs = new Ingredient[buf.readVarInt()];
			for (int i = 0; i < inputs.length; i++) {
				inputs[i] = Ingredient.fromNetwork(buf);
			}
			ItemStack output = buf.readItem();
			int mana = buf.readVarInt();
			return new HeadRecipe(id, output, mana, inputs);
		}

		@Override
		public void toNetwork(@NotNull FriendlyByteBuf buf, @NotNull HeadRecipe recipe) {
			ModRecipeTypes.RUNE_SERIALIZER.toNetwork(buf, recipe);
		}
	}

}
