/**
 * This class was created by <Vazkii>. It's distributed as
 * part of the Botania Mod. Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 * 
 * Botania is Open Source and distributed under a
 * Creative Commons Attribution-NonCommercial-ShareAlike 3.0 License
 * (http://creativecommons.org/licenses/by-nc-sa/3.0/deed.en_GB)
 * 
 * File Created @ [Jul 26, 2014, 4:50:20 PM (GMT)]
 */
package vazkii.botania.common.block.tile;

import java.util.List;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.InventoryCrafting;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.CraftingManager;
import net.minecraft.item.crafting.IRecipe;
import vazkii.botania.common.item.ModItems;

public class TileCraftCrate extends TileOpenCrate {

	int signal = 0;

	@Override
	public int getSizeInventory() {
		return 10;
	}

	@Override
	public int getInventoryStackLimit() {
		return 1;
	}

	@Override
	public boolean isItemValidForSlot(int i, ItemStack itemstack) {
		return i != 9;
	}

	@Override
	public void updateEntity() {
		if(!worldObj.isRemote && craft(true) && canEject())
			ejectAll();

		int newSignal = 0;
		for(; newSignal < 9; newSignal++) // dis for loop be derpy
			if(getStackInSlot(newSignal) == null)
				break;

		if(newSignal != signal) {
			signal = newSignal;
			worldObj.func_147453_f(xCoord, yCoord, zCoord, worldObj.getBlock(xCoord, yCoord, zCoord));
		}
	}

	boolean craft(boolean fullCheck) {
		if(fullCheck && !isFull())
			return false;

		InventoryCrafting craft = new InventoryCrafting(new Container() {
			@Override
			public boolean canInteractWith(EntityPlayer p_75145_1_) {
				return false;
			}
		}, 3, 3);
		for(int i = 0; i < 9; i++) {
			ItemStack stack = getStackInSlot(i);

			if(stack == null || stack.getItem() == ModItems.manaResource && stack.getItemDamage() == 11)
				continue;

			craft.setInventorySlotContents(i, stack.copy());
		}

		List<IRecipe> recipes = CraftingManager.getInstance().getRecipeList();
		for(IRecipe recipe : recipes)
			if(recipe.matches(craft, worldObj)) {
				setInventorySlotContents(9, recipe.getCraftingResult(craft));

				for(int i = 0; i < 9; i++) {
					ItemStack stack = getStackInSlot(i);
					if(stack == null)
						continue;

					ItemStack container = stack.getItem().getContainerItem(stack);
					setInventorySlotContents(i, container);
				}
				return true;
			}

		return false;
	}

	boolean isFull() {
		for(int i = 0; i < 9; i++)
			if(getStackInSlot(i) == null)
				return false;

		return true;
	}

	void ejectAll() {
		for(int i = 0; i < getSizeInventory(); ++i) {
			ItemStack stack = getStackInSlot(i);
			if(stack != null)
				eject(stack, false);
			setInventorySlotContents(i, null);
		}
	}

	@Override
	public boolean onWanded(EntityPlayer player, ItemStack stack) {
		craft(false);
		ejectAll();
		return true;
	}

	@Override
	public int getSignal() {
		return signal;
	}
}
