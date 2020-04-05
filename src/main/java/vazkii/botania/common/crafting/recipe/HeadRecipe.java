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

import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.item.crafting.IRecipeSerializer;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.network.PacketBuffer;
import net.minecraft.util.JSONUtils;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.world.World;
import net.minecraftforge.common.crafting.CraftingHelper;
import net.minecraftforge.items.wrapper.RecipeWrapper;
import net.minecraftforge.registries.ForgeRegistryEntry;

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
	public boolean matches(RecipeWrapper inv, @Nonnull World world) {
		boolean matches = super.matches(inv, world);

		if (matches) {
			for (int i = 0; i < inv.getSizeInventory(); i++) {
				ItemStack stack = inv.getStackInSlot(i);
				if (stack.isEmpty()) {
					break;
				}

				if (stack.getItem() == Items.NAME_TAG) {
					String defaultName = new TranslationTextComponent(Items.NAME_TAG.getTranslationKey()).getString();
					if (stack.getDisplayName().getString().equals(defaultName)) {
						return false;
					}
				}
			}
		}

		return matches;
	}

	@Nonnull
	@Override
	public ItemStack getCraftingResult(@Nonnull RecipeWrapper inv) {
		ItemStack stack = getRecipeOutput().copy();
		for (int i = 0; i < inv.getSizeInventory(); i++) {
			ItemStack ingr = inv.getStackInSlot(i);
			if (ingr.getItem() == Items.NAME_TAG) {
				ItemNBTHelper.setString(stack, "SkullOwner", ingr.getDisplayName().getString());
				break;
			}
		}
		return stack;
	}

	public static class Serializer extends ForgeRegistryEntry<IRecipeSerializer<?>> implements IRecipeSerializer<HeadRecipe> {

		// todo 1.15 reduce this duplication with RecipeRuneAltar, probably by introducing an abstract superclass
		@Nonnull
		@Override
		public HeadRecipe read(@Nonnull ResourceLocation id, @Nonnull JsonObject json) {
			ItemStack output = CraftingHelper.getItemStack(JSONUtils.getJsonObject(json, "output"), true);
			int mana = JSONUtils.getInt(json, "mana");
			JsonArray ingrs = JSONUtils.getJsonArray(json, "ingredients");
			List<Ingredient> inputs = new ArrayList<>();
			for (JsonElement e : ingrs) {
				inputs.add(Ingredient.deserialize(e));
			}
			return new HeadRecipe(id, output, mana, inputs.toArray(new Ingredient[0]));
		}

		@Override
		public HeadRecipe read(@Nonnull ResourceLocation id, @Nonnull PacketBuffer buf) {
			Ingredient[] inputs = new Ingredient[buf.readVarInt()];
			for (int i = 0; i < inputs.length; i++) {
				inputs[i] = Ingredient.read(buf);
			}
			ItemStack output = buf.readItemStack();
			int mana = buf.readVarInt();
			return new HeadRecipe(id, output, mana, inputs);
		}

		@Override
		public void write(@Nonnull PacketBuffer buf, @Nonnull HeadRecipe recipe) {
			ModRecipeTypes.RUNE_SERIALIZER.write(buf, recipe);
		}
	}

}
