/**
 * This class was created by <Vazkii>. It's distributed as
 * part of the Botania Mod. Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 *
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 *
 * File Created @ [Nov 1, 2014, 8:52:00 PM (GMT)]
 */
package vazkii.botania.api.recipe;

import com.google.common.collect.ImmutableList;
import net.minecraft.item.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.network.PacketBuffer;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.items.IItemHandler;
import vazkii.botania.api.BotaniaAPI;
import vazkii.botania.api.brew.Brew;
import vazkii.botania.api.brew.IBrewContainer;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class RecipeBrew {
	private final ResourceLocation id;
	private final Brew brew;
	private final ImmutableList<Ingredient> inputs;

	public RecipeBrew(ResourceLocation id, Brew brew, Ingredient... inputs) {
		this.id = id;
		this.brew = brew;
		this.inputs = ImmutableList.copyOf(inputs);
	}

	public boolean matches(IItemHandler inv) {
		List<Ingredient> inputsMissing = new ArrayList<>(inputs);

		for(int i = 0; i < inv.getSlots(); i++) {
			ItemStack stack = inv.getStackInSlot(i);
			if(stack.isEmpty())
				break;

			if(stack.getItem() instanceof IBrewContainer)
				continue;

			boolean matchedOne = false;

			Iterator<Ingredient> iter = inputsMissing.iterator();
			while (iter.hasNext()) {
				Ingredient input = iter.next();
				if(input.test(stack)) {
					iter.remove();
					matchedOne = true;
					break;
				}
			}

			if(!matchedOne)
				return false;
		}

		return inputsMissing.isEmpty();
	}

	public ResourceLocation getId() {
		return id;
	}

	public List<Ingredient> getInputs() {
		return new ArrayList<>(inputs);
	}

	public Brew getBrew() {
		return brew;
	}

	public int getManaUsage() {
		return brew.getManaCost();
	}

	public ItemStack getOutput(ItemStack stack) {
		if(stack.isEmpty() || !(stack.getItem() instanceof IBrewContainer))
			return new ItemStack(Items.GLASS_BOTTLE); // Fallback...
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

	public void write(PacketBuffer buf) {
		buf.writeResourceLocation(id);
		buf.writeString(brew.getKey());
		buf.writeVarInt(inputs.size());
		for (Ingredient input : inputs) {
			input.write(buf);
		}
	}

	public static RecipeBrew read(PacketBuffer buf) {
		ResourceLocation id = buf.readResourceLocation();
		Brew brew = BotaniaAPI.getBrewFromKey(buf.readString());
		Ingredient[] inputs = new Ingredient[buf.readVarInt()];
		for (int i = 0; i < inputs.length; i++) {
			inputs[i] = Ingredient.read(buf);
		}
		return new RecipeBrew(id, brew, inputs);
	}

}
