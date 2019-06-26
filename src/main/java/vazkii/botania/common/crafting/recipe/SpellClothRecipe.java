/**
 * This class was created by <Vazkii>. It's distributed as
 * part of the Botania Mod. Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 *
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 *
 * File Created @ [Jan 25, 2015, 6:16:13 PM (GMT)]
 */
package vazkii.botania.common.crafting.recipe;

import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.CraftingInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.item.crafting.SpecialRecipe;
import net.minecraft.item.crafting.IRecipeSerializer;
import net.minecraft.item.crafting.SpecialRecipe;
import net.minecraft.item.crafting.SpecialRecipeSerializer;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;
import net.minecraftforge.registries.IForgeRegistryEntry;
import vazkii.botania.common.item.ModItems;
import vazkii.botania.common.lib.LibMisc;

import javax.annotation.Nonnull;

public class SpellClothRecipe extends SpecialRecipe {
	public static final IRecipeSerializer<SpellClothRecipe> SERIALIZER = new SpecialRecipeSerializer<>(SpellClothRecipe::new);

	public SpellClothRecipe(ResourceLocation id) {
		super(id);
	}

	@Override
	public boolean matches(@Nonnull CraftingInventory inv, @Nonnull World world) {
		boolean foundCloth = false;
		boolean foundEnchanted = false;

		for(int i = 0; i < inv.getSizeInventory(); i++) {
			ItemStack stack = inv.getStackInSlot(i);
			if(!stack.isEmpty()) {
				if(stack.isEnchanted() && !foundEnchanted && stack.getItem() != ModItems.spellCloth)
					foundEnchanted = true;

				else if(stack.getItem() == ModItems.spellCloth && !foundCloth)
					foundCloth = true;

				else return false; // Found an invalid item, breaking the recipe
			}
		}

		return foundCloth && foundEnchanted;
	}

	@Nonnull
	@Override
	public ItemStack getCraftingResult(@Nonnull CraftingInventory inv) {
		ItemStack stackToDisenchant = ItemStack.EMPTY;
		for(int i = 0; i < inv.getSizeInventory(); i++) {
			ItemStack stack = inv.getStackInSlot(i);
			if(!stack.isEmpty() && stack.isEnchanted() && stack.getItem() != ModItems.spellCloth) {
				stackToDisenchant = stack.copy();
				stackToDisenchant.setCount(1);
				break;
			}
		}

		if(stackToDisenchant.isEmpty())
			return ItemStack.EMPTY;

		stackToDisenchant.getTag().remove("ench"); // Remove enchantments
		return stackToDisenchant;
	}

	@Override
	public boolean canFit(int width, int height) {
		return width * height >= 2;
	}

	@Nonnull
	@Override
	public IRecipeSerializer<?> getSerializer() {
		return SERIALIZER;
	}
}
