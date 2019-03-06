/**
 * This class was created by <Vazkii>. It's distributed as
 * part of the Botania Mod. Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 *
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 *
 * File Created @ [Jan 24, 2015, 8:37:33 PM (GMT)]
 */
package vazkii.botania.common.crafting.recipe;

import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.InventoryCrafting;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.item.crafting.IRecipeHidden;
import net.minecraft.item.crafting.IRecipeSerializer;
import net.minecraft.item.crafting.RecipeSerializers;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;
import net.minecraftforge.registries.IForgeRegistryEntry;
import vazkii.botania.common.item.ItemManaGun;
import vazkii.botania.common.item.ModItems;
import vazkii.botania.common.lib.LibMisc;

import javax.annotation.Nonnull;

public class ManaGunClipRecipe extends IRecipeHidden {
	private static final ResourceLocation TYPE_ID = new ResourceLocation(LibMisc.MOD_ID, "mana_gun_add_clip");
	public static final IRecipeSerializer<ManaGunClipRecipe> SERIALIZER = new RecipeSerializers.SimpleSerializer<>(TYPE_ID.toString(), ManaGunClipRecipe::new);

	public ManaGunClipRecipe(ResourceLocation id) {
		super(id);
	}

	@Override
	public boolean matches(@Nonnull IInventory inv, @Nonnull World world) {
		boolean foundGun = false;
		boolean foundClip = false;

		for(int i = 0; i < inv.getSizeInventory(); i++) {
			ItemStack stack = inv.getStackInSlot(i);
			if(!stack.isEmpty()) {
				if(stack.getItem() instanceof ItemManaGun && !ItemManaGun.hasClip(stack))
					foundGun = true;

				else if(stack.getItem() == ModItems.clip)
					foundClip = true;

				else return false; // Found an invalid item, breaking the recipe
			}
		}

		return foundGun && foundClip;
	}

	@Nonnull
	@Override
	public ItemStack getCraftingResult(@Nonnull IInventory inv) {
		ItemStack gun = ItemStack.EMPTY;

		for(int i = 0; i < inv.getSizeInventory(); i++) {
			ItemStack stack = inv.getStackInSlot(i);
			if(!stack.isEmpty() && stack.getItem() instanceof ItemManaGun)
				gun = stack;
		}

		if(gun.isEmpty())
			return ItemStack.EMPTY;

		ItemStack lens = ItemManaGun.getLens(gun);
		ItemManaGun.setLens(gun, ItemStack.EMPTY);
		ItemStack gunCopy = gun.copy();
		ItemManaGun.setClip(gunCopy, true);
		ItemManaGun.setLensAtPos(gunCopy, lens, 0);
		return gunCopy;
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
