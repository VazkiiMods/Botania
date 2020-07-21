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
import net.minecraft.inventory.Inventory;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.recipe.Ingredient;
import net.minecraft.recipe.RecipeSerializer;
import net.minecraft.text.TranslatableText;
import net.minecraft.util.Identifier;
import net.minecraft.util.JsonHelper;
import net.minecraft.world.World;

import vazkii.botania.common.core.helper.ItemNBTHelper;
import vazkii.botania.common.crafting.ModRecipeTypes;
import vazkii.botania.common.crafting.RecipeRuneAltar;

import javax.annotation.Nonnull;

import java.util.ArrayList;
import java.util.List;

public class HeadRecipe extends RecipeRuneAltar {

	public HeadRecipe(Identifier id, ItemStack output, int mana, Ingredient... inputs) {
		super(id, output, mana, inputs);
	}

	@Override
	public boolean matches(Inventory inv, @Nonnull World world) {
		boolean matches = super.matches(inv, world);

		if (matches) {
			for (int i = 0; i < inv.size(); i++) {
				ItemStack stack = inv.getStack(i);
				if (stack.isEmpty()) {
					break;
				}

				if (stack.getItem() == Items.NAME_TAG) {
					String defaultName = new TranslatableText(Items.NAME_TAG.getTranslationKey()).getString();
					if (stack.getName().getString().equals(defaultName)) {
						return false;
					}
				}
			}
		}

		return matches;
	}

	@Nonnull
	@Override
	public ItemStack craft(@Nonnull Inventory inv) {
		ItemStack stack = getOutput().copy();
		for (int i = 0; i < inv.size(); i++) {
			ItemStack ingr = inv.getStack(i);
			if (ingr.getItem() == Items.NAME_TAG) {
				ItemNBTHelper.setString(stack, "SkullOwner", ingr.getName().getString());
				break;
			}
		}
		return stack;
	}

	public static class Serializer implements RecipeSerializer<HeadRecipe> {

		@Nonnull
		@Override
		public HeadRecipe read(@Nonnull Identifier id, @Nonnull JsonObject json) {
			ItemStack output = CraftingHelper.getItemStack(JsonHelper.getObject(json, "output"), true);
			int mana = JsonHelper.getInt(json, "mana");
			JsonArray ingrs = JsonHelper.getArray(json, "ingredients");
			List<Ingredient> inputs = new ArrayList<>();
			for (JsonElement e : ingrs) {
				inputs.add(Ingredient.fromJson(e));
			}
			return new HeadRecipe(id, output, mana, inputs.toArray(new Ingredient[0]));
		}

		@Override
		public HeadRecipe read(@Nonnull Identifier id, @Nonnull PacketByteBuf buf) {
			Ingredient[] inputs = new Ingredient[buf.readVarInt()];
			for (int i = 0; i < inputs.length; i++) {
				inputs[i] = Ingredient.fromPacket(buf);
			}
			ItemStack output = buf.readItemStack();
			int mana = buf.readVarInt();
			return new HeadRecipe(id, output, mana, inputs);
		}

		@Override
		public void write(@Nonnull PacketByteBuf buf, @Nonnull HeadRecipe recipe) {
			ModRecipeTypes.RUNE_SERIALIZER.write(buf, recipe);
		}
	}

}
