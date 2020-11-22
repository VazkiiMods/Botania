/*
 * This class is distributed as part of the Botania Mod.
 * Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 *
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 */
package vazkii.botania.common.crafting;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipeSerializer;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.item.crafting.ShapedRecipe;
import net.minecraft.network.PacketBuffer;
import net.minecraft.util.JSONUtils;
import net.minecraft.util.NonNullList;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;
import net.minecraftforge.registries.ForgeRegistryEntry;

import vazkii.botania.api.recipe.IPetalRecipe;
import vazkii.botania.common.block.ModBlocks;

import javax.annotation.Nonnull;

import java.util.ArrayList;
import java.util.List;

public class RecipePetals implements IPetalRecipe {
	private final ResourceLocation id;
	private final ItemStack output;
	private final NonNullList<Ingredient> inputs;

	public RecipePetals(ResourceLocation id, ItemStack output, Ingredient... inputs) {
		this.id = id;
		this.output = output;
		this.inputs = NonNullList.from(Ingredient.EMPTY, inputs);
	}

	@Override
	public boolean matches(IInventory inv, @Nonnull World world) {
		List<Ingredient> ingredientsMissing = new ArrayList<>(inputs);

		for (int i = 0; i < inv.getSizeInventory(); i++) {
			ItemStack input = inv.getStackInSlot(i);
			if (input.isEmpty()) {
				break;
			}

			int stackIndex = -1;

			for (int j = 0; j < ingredientsMissing.size(); j++) {
				Ingredient ingr = ingredientsMissing.get(j);
				if (ingr.test(input)) {
					stackIndex = j;
					break;
				}
			}

			if (stackIndex != -1) {
				ingredientsMissing.remove(stackIndex);
			} else {
				return false;
			}
		}

		return ingredientsMissing.isEmpty();
	}

	@Nonnull
	@Override
	public final ItemStack getRecipeOutput() {
		return output;
	}

	@Nonnull
	@Override
	public ItemStack getCraftingResult(@Nonnull IInventory inv) {
		return getRecipeOutput().copy();
	}

	@Nonnull
	@Override
	public NonNullList<Ingredient> getIngredients() {
		return inputs;
	}

	@Nonnull
	@Override
	public ItemStack getIcon() {
		return new ItemStack(ModBlocks.defaultAltar);
	}

	@Nonnull
	@Override
	public ResourceLocation getId() {
		return id;
	}

	@Nonnull
	@Override
	public IRecipeSerializer<?> getSerializer() {
		return ModRecipeTypes.PETAL_SERIALIZER;
	}

	public static class Serializer extends ForgeRegistryEntry<IRecipeSerializer<?>> implements IRecipeSerializer<RecipePetals> {
		@Nonnull
		@Override
		public RecipePetals read(@Nonnull ResourceLocation id, @Nonnull JsonObject json) {
			ItemStack output = ShapedRecipe.deserializeItem(JSONUtils.getJsonObject(json, "output"));
			JsonArray ingrs = JSONUtils.getJsonArray(json, "ingredients");
			List<Ingredient> inputs = new ArrayList<>();
			for (JsonElement e : ingrs) {
				inputs.add(Ingredient.deserialize(e));
			}
			return new RecipePetals(id, output, inputs.toArray(new Ingredient[0]));
		}

		@Override
		public RecipePetals read(@Nonnull ResourceLocation id, @Nonnull PacketBuffer buf) {
			Ingredient[] inputs = new Ingredient[buf.readVarInt()];
			for (int i = 0; i < inputs.length; i++) {
				inputs[i] = Ingredient.read(buf);
			}
			ItemStack output = buf.readItemStack();
			return new RecipePetals(id, output, inputs);
		}

		@Override
		public void write(@Nonnull PacketBuffer buf, @Nonnull RecipePetals recipe) {
			buf.writeVarInt(recipe.getIngredients().size());
			for (Ingredient input : recipe.getIngredients()) {
				input.write(buf);
			}
			buf.writeItemStack(recipe.getRecipeOutput(), false);
		}

	}

}
