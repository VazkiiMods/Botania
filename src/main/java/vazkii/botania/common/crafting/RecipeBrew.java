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
import com.google.gson.JsonParseException;

import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.item.crafting.IRecipeSerializer;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.network.PacketBuffer;
import net.minecraft.util.JSONUtils;
import net.minecraft.util.NonNullList;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;
import net.minecraftforge.items.wrapper.RecipeWrapper;
import net.minecraftforge.registries.ForgeRegistryEntry;

import vazkii.botania.api.BotaniaAPI;
import vazkii.botania.api.brew.Brew;
import vazkii.botania.api.brew.IBrewContainer;
import vazkii.botania.api.recipe.IBrewRecipe;
import vazkii.botania.common.block.ModBlocks;

import javax.annotation.Nonnull;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class RecipeBrew implements IBrewRecipe {
	private final ResourceLocation id;
	private final Brew brew;
	private final NonNullList<Ingredient> inputs;

	public RecipeBrew(ResourceLocation id, Brew brew, Ingredient... inputs) {
		this.id = id;
		this.brew = brew;
		this.inputs = NonNullList.from(null, inputs);
	}

	@Override
	public boolean matches(RecipeWrapper inv, @Nonnull World world) {
		List<Ingredient> inputsMissing = new ArrayList<>(inputs);

		for (int i = 0; i < inv.getSizeInventory(); i++) {
			ItemStack stack = inv.getStackInSlot(i);
			if (stack.isEmpty()) {
				break;
			}

			if (stack.getItem() instanceof IBrewContainer) {
				continue;
			}

			boolean matchedOne = false;

			Iterator<Ingredient> iter = inputsMissing.iterator();
			while (iter.hasNext()) {
				Ingredient input = iter.next();
				if (input.test(stack)) {
					iter.remove();
					matchedOne = true;
					break;
				}
			}

			if (!matchedOne) {
				return false;
			}
		}

		return inputsMissing.isEmpty();
	}

	@Nonnull
	@Override
	public NonNullList<Ingredient> getIngredients() {
		return inputs;
	}

	@Nonnull
	@Override
	public ItemStack getIcon() {
		return new ItemStack(ModBlocks.brewery);
	}

	@Nonnull
	@Override
	public ResourceLocation getId() {
		return id;
	}

	@Nonnull
	@Override
	public IRecipeSerializer<?> getSerializer() {
		return ModRecipeTypes.BREW_SERIALIZER;
	}

	@Override
	public Brew getBrew() {
		return brew;
	}

	@Override
	public int getManaUsage() {
		return brew.getManaCost();
	}

	@Override
	public ItemStack getOutput(ItemStack stack) {
		if (stack.isEmpty() || !(stack.getItem() instanceof IBrewContainer)) {
			return new ItemStack(Items.GLASS_BOTTLE); // Fallback...
		}
		IBrewContainer container = (IBrewContainer) stack.getItem();

		return container.getItemForBrew(brew, stack);
	}

	@Override
	public int hashCode() {
		return 31 * brew.hashCode() ^ inputs.hashCode();
	}

	@Override
	public boolean equals(Object o) {
		return o instanceof RecipeBrew
				&& brew == ((RecipeBrew) o).brew
				&& inputs.equals(((RecipeBrew) o).inputs);
	}

	public static class Serializer extends ForgeRegistryEntry<IRecipeSerializer<?>> implements IRecipeSerializer<RecipeBrew> {
		@Nonnull
		@Override
		public RecipeBrew read(@Nonnull ResourceLocation id, @Nonnull JsonObject json) {
			String brewStr = JSONUtils.getString(json, "brew");
			ResourceLocation brewId = ResourceLocation.tryCreate(brewStr);
			if (brewId == null || !BotaniaAPI.instance().getBrewRegistry().containsKey(brewId)) {
				throw new JsonParseException("Unknown brew " + brewStr);
			}
			Brew brew = BotaniaAPI.instance().getBrewRegistry().getValue(brewId);

			JsonArray ingrs = JSONUtils.getJsonArray(json, "ingredients");
			List<Ingredient> inputs = new ArrayList<>();
			for (JsonElement e : ingrs) {
				inputs.add(Ingredient.deserialize(e));
			}
			return new RecipeBrew(id, brew, inputs.toArray(new Ingredient[0]));
		}

		@Override
		public RecipeBrew read(@Nonnull ResourceLocation id, @Nonnull PacketBuffer buf) {
			Brew brew = buf.readRegistryIdUnsafe(BotaniaAPI.instance().getBrewRegistry());
			Ingredient[] inputs = new Ingredient[buf.readVarInt()];
			for (int i = 0; i < inputs.length; i++) {
				inputs[i] = Ingredient.read(buf);
			}
			return new RecipeBrew(id, brew, inputs);
		}

		@Override
		public void write(@Nonnull PacketBuffer buf, @Nonnull RecipeBrew recipe) {
			buf.writeRegistryIdUnsafe(BotaniaAPI.instance().getBrewRegistry(), recipe.getBrew());
			buf.writeVarInt(recipe.getIngredients().size());
			for (Ingredient input : recipe.getIngredients()) {
				input.write(buf);
			}
		}
	}
}
