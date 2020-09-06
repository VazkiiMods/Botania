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

import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ITickable;
import net.minecraftforge.items.IItemHandler;
import vazkii.botania.api.item.IAvatarTile;
import vazkii.botania.api.item.IAvatarWieldable;
import vazkii.botania.api.state.BotaniaStateProps;

import javax.annotation.Nonnull;

public class TileAvatar extends TileSimpleInventory implements IAvatarTile, ITickable {

	private static final int MAX_MANA = 6400;

	private static final String TAG_ENABLED = "enabled";
	private static final String TAG_TICKS_ELAPSED = "ticksElapsed";
	private static final String TAG_MANA = "mana";

	boolean enabled;
	int ticksElapsed;
	int mana;

	@Override
	public void update() {
		enabled = true;
		for(EnumFacing dir : EnumFacing.VALUES) {
			int redstoneSide = world.getRedstonePower(pos.offset(dir), dir);
			if(redstoneSide > 0) {
				enabled = false;
				break;
			}
		}

		ItemStack stack = itemHandler.getStackInSlot(0);
		if(!stack.isEmpty() && stack.getItem() instanceof IAvatarWieldable) {
			IAvatarWieldable wieldable = (IAvatarWieldable) stack.getItem();
			wieldable.onAvatarUpdate(this, stack);
		}

		if(enabled)
			ticksElapsed++;
	}

	@Override
	public void writePacketNBT(NBTTagCompound par1nbtTagCompound) {
		super.writePacketNBT(par1nbtTagCompound);
		par1nbtTagCompound.setBoolean(TAG_ENABLED, enabled);
		par1nbtTagCompound.setInteger(TAG_TICKS_ELAPSED, ticksElapsed);
		par1nbtTagCompound.setInteger(TAG_MANA, mana);
	}

	@Override
	public void readPacketNBT(NBTTagCompound par1nbtTagCompound) {
		super.readPacketNBT(par1nbtTagCompound);
		enabled = par1nbtTagCompound.getBoolean(TAG_ENABLED);
		ticksElapsed = par1nbtTagCompound.getInteger(TAG_TICKS_ELAPSED);
		mana = par1nbtTagCompound.getInteger(TAG_MANA);
	}

	@Override
	public int getSizeInventory() {
		return 1;
	}

	@Override
	protected SimpleItemStackHandler createItemHandler() {
		return new SimpleItemStackHandler(this, false) {
			@Override
			protected int getStackLimit(int slot, @Nonnull ItemStack stack) {
				return 1;
			}
		};
	}

	@Override
	public boolean isFull() {
		return mana >= MAX_MANA;
	}

	@Override
	public void recieveMana(int mana) {
		this.mana = Math.min(3 * MAX_MANA, this.mana + mana);
	}

	@Override
	public boolean canRecieveManaFromBursts() {
		return !itemHandler.getStackInSlot(0).isEmpty();
	}

	@Override
	public int getCurrentMana() {
		return mana;
	}

	@Override
	public IItemHandler getInventory() {
		return getItemHandler();
	}

	@Override
	public EnumFacing getAvatarFacing() {
		return world.getBlockState(getPos()).getValue(BotaniaStateProps.CARDINALS);
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
