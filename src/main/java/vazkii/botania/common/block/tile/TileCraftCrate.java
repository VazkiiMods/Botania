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

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.InventoryCrafting;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.play.server.SPacketUpdateTileEntity;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.registry.ForgeRegistries;
import vazkii.botania.api.internal.VanillaPacketDispatcher;
import vazkii.botania.common.item.ModItems;

import javax.annotation.Nonnull;
import java.util.List;

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
	private int signal = 0;

	@Override
	public int getSizeInventory() {
		return 10;
	}

	@Override
	protected SimpleItemStackHandler createItemHandler() {
		return new SimpleItemStackHandler(this, true) {
			@Override
			protected int getStackLimit(int slot, @Nonnull ItemStack stack) {
				return 1;
			}

			@Nonnull
			@Override
			public ItemStack insertItem(int slot, @Nonnull ItemStack stack, boolean simulate) {
				if(slot != 9 && !isLocked(slot))
					return super.insertItem(slot, stack, simulate);
				else return stack;
			}
		};
	}

	public boolean isLocked(int slot) {
		return pattern != -1 && !PATTERNS[pattern][slot];
	}

	@Override
	public void update() {
		if (world.isRemote)
			return;

		if(canEject() && isFull() && craft(true))
			ejectAll();

		int newSignal = 0;
		for(; newSignal < 9; newSignal++) // dis for loop be derpy
			if(!isLocked(newSignal) && itemHandler.getStackInSlot(newSignal).isEmpty())
				break;

		if(newSignal != signal) {
			signal = newSignal;
			world.updateComparatorOutputLevel(pos, world.getBlockState(pos).getBlock());
		}
	}

	private boolean craft(boolean fullCheck) {
		if(fullCheck && !isFull())
			return false;

		InventoryCrafting craft = new InventoryCrafting(new Container() {
			@Override
			public boolean canInteractWith(@Nonnull EntityPlayer player) {
				return false;
			}
		}, 3, 3);
		for(int i = 0; i < craft.getSizeInventory(); i++) {
			ItemStack stack = itemHandler.getStackInSlot(i);

			if(stack.isEmpty() || isLocked(i) || stack.getItem() == ModItems.manaResource && stack.getItemDamage() == 11)
				continue;

			craft.setInventorySlotContents(i, stack);
		}

		for(IRecipe recipe : ForgeRegistries.RECIPES)
			if(recipe.matches(craft, world)) {
				itemHandler.setStackInSlot(9, recipe.getCraftingResult(craft));

				List<ItemStack> remainders = recipe.getRemainingItems(craft);
				for(int i = 0; i < craft.getSizeInventory(); i++) {
					ItemStack s = remainders.get(i);
					if(!itemHandler.getStackInSlot(i).isEmpty()
						&& itemHandler.getStackInSlot(i).getItem() == ModItems.manaResource
						&& itemHandler.getStackInSlot(i).getItemDamage() == 11)
						continue;
					itemHandler.setStackInSlot(i, s);
				}
				return true;
			}

		return false;
	}

	boolean isFull() {
		for(int i = 0; i < 9; i++)
			if(!isLocked(i) && itemHandler.getStackInSlot(i).isEmpty())
				return false;

		return true;
	}

	private void ejectAll() {
		for(int i = 0; i < getSizeInventory(); ++i) {
			ItemStack stack = itemHandler.getStackInSlot(i);
			if(!stack.isEmpty())
				eject(stack, false);
			itemHandler.setStackInSlot(i, ItemStack.EMPTY);
		}
		markDirty();
	}

	@Override
	public void writePacketNBT(NBTTagCompound par1nbtTagCompound) {
		super.writePacketNBT(par1nbtTagCompound);
		par1nbtTagCompound.setInteger(TAG_PATTERN, pattern);
	}

	@Override
	public void readPacketNBT(NBTTagCompound par1nbtTagCompound) {
		super.readPacketNBT(par1nbtTagCompound);
		pattern = par1nbtTagCompound.getInteger(TAG_PATTERN);
	}

	@Override
	public boolean onWanded(World world, EntityPlayer player, ItemStack stack) {
		if(!world.isRemote && canEject()) {
			craft(false);
			ejectAll();
		}
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
	public void onDataPacket(NetworkManager manager, SPacketUpdateTileEntity packet) {
		int lastPattern = pattern;
		super.onDataPacket(manager, packet);
		if(pattern != lastPattern)
			world.markBlockRangeForRenderUpdate(pos, pos);
	}

}
