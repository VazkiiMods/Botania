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
import net.minecraft.util.ITickable;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraftforge.fml.common.Loader;
import vazkii.botania.api.mana.IManaReceiver;
import vazkii.botania.common.block.tile.TileMod;
import cofh.api.energy.IEnergyConnection;
import cofh.api.energy.IEnergyReceiver;
import net.minecraftforge.fml.common.Optional;

import java.util.EnumMap;
import java.util.Map;

@Optional.Interface(iface = "cofh.api.energy.IEnergyConnection", modid = "CoFHAPI|energy")
public class TileRFGenerator extends TileMod implements IManaReceiver, IEnergyConnection, ITickable {

	private static final int CONVERSION_RATE = 10;
	private static final int MAX_MANA = 1280 * CONVERSION_RATE;

	private static final String TAG_MANA = "mana";

	int mana = 0;

	// Thanks to skyboy for help with this cuz I'm a noob with RF
	private EnumMap<EnumFacing, IEnergyReceiver> receiverCache;
	private boolean deadCache;

	@Override
	@Optional.Method(modid = "CoFHAPI|energy")
	public void validate() {
		super.validate();
		deadCache = true;
		receiverCache = null;
	}

	@Override
	public void update() {
		if(!worldObj.isRemote && Loader.isModLoaded("CoFHAPI|energy")) {
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
			for(Map.Entry<EnumFacing, IEnergyReceiver> e : receiverCache.entrySet()) {
				// todo 1.8 this iteration was in explicit reverse order. was there a reason?
				IEnergyReceiver tile = e.getValue();
				if(tile == null)
					continue;

				if(tile.receiveEnergy(e.getKey(), energy, true) > 0)
					energy -= tile.receiveEnergy(e.getKey(), energy, false);

				if(energy <= 0)
					return 0;
			}

		return energy;
	}

	@Optional.Method(modid = "CoFHAPI|energy")
	private void reCache() {
		if(deadCache) {
			for(EnumFacing dir : EnumFacing.VALUES)
				onNeighborTileChange(pos.offset(dir));
			deadCache = false;
		}
	}

	@Optional.Method(modid = "CoFHAPI|energy")
	public void onNeighborTileChange(BlockPos pos) {
		TileEntity tile = worldObj.getTileEntity(pos);

		if(pos.getX() < getPos().getX())
			addCache(tile, EnumFacing.EAST);
		else if(pos.getX() > getPos().getX())
			addCache(tile, EnumFacing.WEST);
		else if(pos.getZ() < getPos().getZ())
			addCache(tile, EnumFacing.SOUTH);
		else if(pos.getZ() > getPos().getZ())
			addCache(tile, EnumFacing.NORTH);
		else if(pos.getY() < getPos().getY())
			addCache(tile, EnumFacing.UP);
		else if(pos.getY() > getPos().getY())
			addCache(tile, EnumFacing.DOWN);
	}

	@Optional.Method(modid = "CoFHAPI|energy")
	private void addCache(TileEntity tile, EnumFacing side) {
		if(receiverCache != null)
			receiverCache.remove(side);

		if(tile instanceof IEnergyReceiver && ((IEnergyReceiver)tile).canConnectEnergy(side)) {
			if(receiverCache == null)
				receiverCache = new EnumMap<EnumFacing, IEnergyReceiver>(EnumFacing.class);
			receiverCache.put(side, (IEnergyReceiver)tile);
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
	public boolean canConnectEnergy(EnumFacing from) {
		return true;
	}

}
