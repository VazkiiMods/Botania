/**
 * This class was created by <Vazkii>. It's distributed as
 * part of the Botania Mod. Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 *
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 *
 * File Created @ [Aug 22, 2014, 7:45:56 PM (GMT)]
 */
package vazkii.botania.common.crafting.recipe;

import com.google.gson.JsonObject;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.InventoryCrafting;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.item.crafting.IRecipeHidden;
import net.minecraft.item.crafting.IRecipeSerializer;
import net.minecraft.item.crafting.RecipeSerializers;
import net.minecraft.network.PacketBuffer;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;
import net.minecraftforge.registries.IForgeRegistryEntry;
import vazkii.botania.common.item.ModItems;
import vazkii.botania.common.item.equipment.tool.terrasteel.ItemTerraPick;
import vazkii.botania.common.lib.LibMisc;

import javax.annotation.Nonnull;

public class TerraPickTippingRecipe extends IRecipeHidden {
	private static final ResourceLocation TYPE_ID = new ResourceLocation(LibMisc.MOD_ID, "terra_pick_tipping");
	public static final IRecipeSerializer<TerraPickTippingRecipe> SERIALIZER = new RecipeSerializers.SimpleSerializer<>(TYPE_ID.toString(), TerraPickTippingRecipe::new);

	public TerraPickTippingRecipe(ResourceLocation id) {
		super(id);
	}

	@Nonnull
	@Override
	public IRecipeSerializer<?> getSerializer() {
		return SERIALIZER;
	}

	@Override
	public boolean matches(@Nonnull IInventory inv, @Nonnull World world) {
		boolean foundTerraPick = false;
		boolean foundElementiumPick = false;

		for(int i = 0; i < inv.getSizeInventory(); i++) {
			ItemStack stack = inv.getStackInSlot(i);
			if(!stack.isEmpty()) {
				if(stack.getItem() instanceof ItemTerraPick && !ItemTerraPick.isTipped(stack))
					foundTerraPick = true;

				else if(stack.getItem() == ModItems.elementiumPick)
					foundElementiumPick = true;

				else return false; // Found an invalid item, breaking the recipe
			}
		}

		return foundTerraPick && foundElementiumPick;
	}

	@Nonnull
	@Override
	public ItemStack getCraftingResult(@Nonnull IInventory inv) {
		ItemStack terraPick = ItemStack.EMPTY;

		for(int i = 0; i < inv.getSizeInventory(); i++) {
			ItemStack stack = inv.getStackInSlot(i);
			if(!stack.isEmpty() && stack.getItem() instanceof ItemTerraPick)
				terraPick = stack;
		}

		if(terraPick.isEmpty())
			return ItemStack.EMPTY;

		ItemStack terraPickCopy = terraPick.copy();
		ItemTerraPick.setTipped(terraPickCopy);
		return terraPickCopy;
	}

	@Override
	public boolean canFit(int width, int height) {
		return width * height >= 2;
	}
}
