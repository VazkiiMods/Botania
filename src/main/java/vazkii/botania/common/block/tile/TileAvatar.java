/**
 * This class was created by <Vazkii>. It's distributed as
 * part of the Botania Mod. Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 * 
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 * 
 * File Created @ [Oct 24, 2015, 3:17:44 PM (GMT)]
 */
package vazkii.botania.common.block.tile;

import net.minecraft.inventory.ISidedInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.ITickable;
import net.minecraft.util.EnumFacing;
import vazkii.botania.api.item.IAvatarTile;
import vazkii.botania.api.item.IAvatarWieldable;
import vazkii.botania.common.lib.LibBlockNames;

public class TileAvatar extends TileSimpleInventory implements IAvatarTile, ISidedInventory, ITickable {

	private static final int MAX_MANA = 6400;

	private static final String TAG_ENABLED = "enabled";
	private static final String TAG_TICKS_ELAPSED = "ticksElapsed";
	private static final String TAG_MANA = "ticksElapsed";

	boolean enabled;
	int ticksElapsed;
	int mana;

	@Override
	public void update() {

		enabled = true;
		for(EnumFacing dir : EnumFacing.VALUES) {
			int redstoneSide = worldObj.getRedstonePower(pos.offset(dir), dir);
			if(redstoneSide > 0) {
				enabled = false;
				break;
			}
		}

		ItemStack stack = getStackInSlot(0);
		if(stack != null && stack.getItem() instanceof IAvatarWieldable) {
			IAvatarWieldable wieldable = (IAvatarWieldable) stack.getItem();
			wieldable.onAvatarUpdate(this, stack);
		}

		if(enabled)
			ticksElapsed++;
	}

	@Override
	public void writeCustomNBT(NBTTagCompound par1nbtTagCompound) {
		super.writeCustomNBT(par1nbtTagCompound);
		par1nbtTagCompound.setBoolean(TAG_ENABLED, enabled);
		par1nbtTagCompound.setInteger(TAG_TICKS_ELAPSED, ticksElapsed);
		par1nbtTagCompound.setInteger(TAG_MANA, mana);
	}

	@Override
	public void readCustomNBT(NBTTagCompound par1nbtTagCompound) {
		super.readCustomNBT(par1nbtTagCompound);
		enabled = par1nbtTagCompound.getBoolean(TAG_ENABLED);
		ticksElapsed = par1nbtTagCompound.getInteger(TAG_TICKS_ELAPSED);
		mana = par1nbtTagCompound.getInteger(TAG_MANA);
	}

	@Override
	public int getSizeInventory() {
		return 1;
	}

	@Override
	public int getInventoryStackLimit() {
		return 1;
	}

	@Override
	public boolean isItemValidForSlot(int i, ItemStack itemstack) {
		return itemstack != null && itemstack.getItem() instanceof IAvatarTile;
	}

	@Override
	public int[] getSlotsForFace(EnumFacing p_94128_1_) {
		return new int[0];
	}

	@Override
	public boolean canInsertItem(int p_102007_1_, ItemStack p_102007_2_, EnumFacing p_102007_3_) {
		return false;
	}

	@Override
	public boolean canExtractItem(int p_102008_1_, ItemStack p_102008_2_, EnumFacing p_102008_3_) {
		return false;
	}

	@Override
	public String getName() {
		return LibBlockNames.AVATAR;
	}

	@Override
	public boolean isFull() {
		return mana >= MAX_MANA;
	}

	@Override
	public void recieveMana(int mana) {
		this.mana = Math.min(MAX_MANA, this.mana + mana);
	}

	@Override
	public boolean canRecieveManaFromBursts() {
		return getStackInSlot(0) != null;
	}

	@Override
	public int getCurrentMana() {
		return mana;
	}

	@Override
	public int getElapsedFunctionalTicks() {
		return ticksElapsed;
	}

	@Override
	public boolean isEnabled() {
		return enabled;
	}

}
