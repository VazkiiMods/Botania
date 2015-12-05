/**
 * This class was created by <Vazkii>. It's distributed as
 * part of the Botania Mod. Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 * 
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
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
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.play.server.S35PacketUpdateTileEntity;
import vazkii.botania.api.internal.VanillaPacketDispatcher;
import vazkii.botania.common.item.ModItems;

public class TileCraftCrate extends TileOpenCrate {

	public static final boolean[][] PATTERNS = new boolean[][] {
		{
			true, false, false,
			false, false, false,
			false, false, false
		}, {
			true, true, false,
			true, true, false,
			false, false, false
		}, {
			true, false, false,
			true, false, false,
			false, false, false
		}, {
			true, true, false,
			false, false, false,
			false, false, false
		}, {
			true, false, false,
			true, false, false,
			true, false, false
		}, {
			true, true, true,
			false, false, false,
			false, false, false
		}, {
			true, true, false,
			true, true, false,
			true, true, false
		}, {
			true, true, true,
			true, true, true,
			false, false, false
		}, {
			true, true, true,
			true, false, true,
			true, true, true
		}
	};

	private static final String TAG_PATTERN = "pattern";

	public int pattern = -1;
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
		return i != 9 && !isLocked(i);
	}

	public boolean isLocked(int slot) {
		return pattern != -1 && !PATTERNS[pattern][slot];
	}

	@Override
	public void updateEntity() {
		if(!worldObj.isRemote && (craft(true) && canEject() || isFull()))
			ejectAll();

		int newSignal = 0;
		for(; newSignal < 9; newSignal++) // dis for loop be derpy
			if(!isLocked(newSignal) && getStackInSlot(newSignal) == null)
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

			if(stack == null || isLocked(i) || stack.getItem() == ModItems.manaResource && stack.getItemDamage() == 11)
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
			if(!isLocked(i) && getStackInSlot(i) == null)
				return false;

		return true;
	}

	void ejectAll() {
		for(int i = 0; i < getSizeInventory(); ++i) {
			ItemStack stack = getStackInSlot(i);
			if(stack != null)
				eject(stack, false);
			setInventorySlotContents(i, null);
			markDirty();
		}
	}

	@Override
	public void writeCustomNBT(NBTTagCompound par1nbtTagCompound) {
		super.writeCustomNBT(par1nbtTagCompound);
		par1nbtTagCompound.setInteger(TAG_PATTERN, pattern);
	}

	@Override
	public void readCustomNBT(NBTTagCompound par1nbtTagCompound) {
		super.readCustomNBT(par1nbtTagCompound);
		pattern = par1nbtTagCompound.getInteger(TAG_PATTERN);
	}

	@Override
	public boolean onWanded(EntityPlayer player, ItemStack stack) {
		craft(false);
		ejectAll();
		return true;
	}

	@Override
	public void markDirty() {
		super.markDirty();
		VanillaPacketDispatcher.dispatchTEToNearbyPlayers(this);
	}

	@Override
	public int getSignal() {
		return signal;
	}

	@Override
	public void onDataPacket(NetworkManager manager, S35PacketUpdateTileEntity packet) {
		int lastPattern = pattern;
		super.onDataPacket(manager, packet);
		if(pattern != lastPattern)
			worldObj.markBlockRangeForRenderUpdate(xCoord,yCoord,zCoord,xCoord,yCoord,zCoord);
	}

}
