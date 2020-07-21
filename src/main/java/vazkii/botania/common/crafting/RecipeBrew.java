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
import net.minecraft.inventory.Inventory;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.recipe.Ingredient;
import net.minecraft.recipe.RecipeSerializer;
import net.minecraft.util.Identifier;
import net.minecraft.util.JsonHelper;
import net.minecraft.util.collection.DefaultedList;
import net.minecraft.world.World;

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
	private final Identifier id;
	private final Brew brew;
	private final DefaultedList<Ingredient> inputs;

	public RecipeBrew(Identifier id, Brew brew, Ingredient... inputs) {
		this.id = id;
		this.brew = brew;
		this.inputs = DefaultedList.copyOf(null, inputs);
	}

	@Override
	public boolean matches(Inventory inv, @Nonnull World world) {
		List<Ingredient> inputsMissing = new ArrayList<>(inputs);

		for (int i = 0; i < inv.size(); i++) {
			ItemStack stack = inv.getStack(i);
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
	public DefaultedList<Ingredient> getPreviewInputs() {
		return inputs;
	}

	@Nonnull
	@Override
	public ItemStack getRecipeKindIcon() {
		return new ItemStack(ModBlocks.brewery);
	}

	@Nonnull
	@Override
	public Identifier getId() {
		return id;
	}

	@Nonnull
	@Override
	public RecipeSerializer<?> getSerializer() {
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

	public static class Serializer extends ForgeRegistryEntry<RecipeSerializer<?>> implements RecipeSerializer<RecipeBrew> {
		@Nonnull
		@Override
		public RecipeBrew read(@Nonnull Identifier id, @Nonnull JsonObject json) {
			String brewStr = JsonHelper.getString(json, "brew");
			Identifier brewId = Identifier.tryParse(brewStr);
			if (brewId == null || !BotaniaAPI.instance().getBrewRegistry().containsId(brewId)) {
				throw new JsonParseException("Unknown brew " + brewStr);
			}
			Brew brew = BotaniaAPI.instance().getBrewRegistry().get(brewId);

			JsonArray ingrs = JsonHelper.getArray(json, "ingredients");
			List<Ingredient> inputs = new ArrayList<>();
			for (JsonElement e : ingrs) {
				inputs.add(Ingredient.fromJson(e));
			}
			return new RecipeBrew(id, brew, inputs.toArray(new Ingredient[0]));
		}

		@Override
		public RecipeBrew read(@Nonnull Identifier id, @Nonnull PacketByteBuf buf) {
			int intId = buf.readVarInt();
			Brew brew = BotaniaAPI.instance().getBrewRegistry().get(intId);
			Ingredient[] inputs = new Ingredient[buf.readVarInt()];
			for (int i = 0; i < inputs.length; i++) {
				inputs[i] = Ingredient.fromPacket(buf);
			}
			return new RecipeBrew(id, brew, inputs);
		}

		@Override
		public void write(@Nonnull PacketByteBuf buf, @Nonnull RecipeBrew recipe) {
			int intId = BotaniaAPI.instance().getBrewRegistry().getRawId(recipe.getBrew());
			buf.writeVarInt(intId);
			buf.writeVarInt(recipe.getPreviewInputs().size());
			for (Ingredient input : recipe.getPreviewInputs()) {
				input.write(buf);
			}
		}
	}
}
