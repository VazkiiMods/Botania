/**
 * This class was created by <Vazkii>. It's distributed as
 * part of the Botania Mod. Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 * 
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 * 
 * File Created @ [Aug 29, 2014, 10:01:32 PM (GMT)]
 */
package vazkii.botania.common.block.tile.mana;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraftforge.common.util.ForgeDirection;
import vazkii.botania.api.mana.IManaReceiver;
import vazkii.botania.common.block.tile.TileMod;
import cofh.api.energy.IEnergyConnection;
import cofh.api.energy.IEnergyReceiver;
import cpw.mods.fml.common.Optional;

@Optional.Interface(iface = "cofh.api.energy.IEnergyConnection", modid = "CoFHAPI|energy")
public class TileRFGenerator extends TileMod implements IManaReceiver, IEnergyConnection {

	private static final int CONVERSION_RATE = 10;
	private static final int MAX_MANA = 1280 * CONVERSION_RATE;

	private static final String TAG_MANA = "mana";

	int mana = 0;

	// Thanks to skyboy for help with this cuz I'm a noob with RF
	private IEnergyReceiver[] receiverCache;
	private boolean deadCache;

	@Override
	@Optional.Method(modid = "CoFHAPI|energy")
	public void validate() {
		super.validate();
		deadCache = true;
		receiverCache = null;
	}

	@Override
	@Optional.Method(modid = "CoFHAPI|energy")
	public void updateEntity() {
		super.updateEntity();
		if(!worldObj.isRemote) {
			if(deadCache)
				reCache();

			int transfer = Math.min(mana, 160 * CONVERSION_RATE);
			mana -= transfer;
			mana += transmitEnergy(transfer);
		}
	}

	@Optional.Method(modid = "CoFHAPI|energy")
	protected final int transmitEnergy(int energy) {
		if(receiverCache != null)
			for(int i = receiverCache.length; i-- > 0;) {
				IEnergyReceiver tile = receiverCache[i];
				if(tile == null)
					continue;

				ForgeDirection from = ForgeDirection.VALID_DIRECTIONS[i];
				if(tile.receiveEnergy(from, energy, true) > 0)
					energy -= tile.receiveEnergy(from, energy, false);

				if(energy <= 0)
					return 0;
			}

		return energy;
	}

	@Optional.Method(modid = "CoFHAPI|energy")
	private void reCache() {
		if(deadCache) {
			for(ForgeDirection dir : ForgeDirection.VALID_DIRECTIONS)
				onNeighborTileChange(xCoord + dir.offsetX, yCoord + dir.offsetY, zCoord + dir.offsetZ);
			deadCache = false;
		}
	}

	@Optional.Method(modid = "CoFHAPI|energy")
	public void onNeighborTileChange(int x, int y, int z) {
		TileEntity tile = worldObj.getTileEntity(x, y, z);

		if(x < xCoord)
			addCache(tile, 5);
		else if(x > xCoord)
			addCache(tile, 4);
		else if(z < zCoord)
			addCache(tile, 3);
		else if(z > zCoord)
			addCache(tile, 2);
		else if(y < yCoord)
			addCache(tile, 1);
		else if(y > yCoord)
			addCache(tile, 0);
	}

	@Optional.Method(modid = "CoFHAPI|energy")
	private void addCache(TileEntity tile, int side) {
		if(receiverCache != null)
			receiverCache[side] = null;

		if(tile instanceof IEnergyReceiver && ((IEnergyReceiver)tile).canConnectEnergy(ForgeDirection.VALID_DIRECTIONS[side])) {
			if(receiverCache == null)
				receiverCache = new IEnergyReceiver[6];
			receiverCache[side] = (IEnergyReceiver)tile;
		}
	}

	@Override
	public int getCurrentMana() {
		return mana / CONVERSION_RATE;
	}

	@Override
	public boolean isFull() {
		return mana >= MAX_MANA;
	}

	@Override
	public void recieveMana(int mana) {
		this.mana = Math.min(MAX_MANA, this.mana + mana * CONVERSION_RATE);
	}

	@Override
	public boolean canRecieveManaFromBursts() {
		return true;
	}

	@Override
	public void writeCustomNBT(NBTTagCompound cmp) {
		cmp.setInteger(TAG_MANA, mana);
	}

	@Override
	public void readCustomNBT(NBTTagCompound cmp) {
		mana = cmp.getInteger(TAG_MANA);
	}

	@Override
	public boolean canConnectEnergy(ForgeDirection from) {
		return true;
	}

}
