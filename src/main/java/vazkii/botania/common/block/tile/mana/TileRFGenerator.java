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
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ITickable;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.energy.CapabilityEnergy;
import net.minecraftforge.energy.IEnergyStorage;
import vazkii.botania.api.mana.IManaReceiver;
import vazkii.botania.common.block.tile.TileMod;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public class TileRFGenerator extends TileMod implements IManaReceiver, ITickable {

	private static final int MANA_TO_FE = 10;
	private static final int MAX_ENERGY = 1280 * MANA_TO_FE;

	private static final String TAG_MANA = "mana";
	int energy = 0;

	private final IEnergyStorage energyHandler = new IEnergyStorage() {
		@Override
		public int getEnergyStored() {
			return energy;
		}

		@Override
		public int getMaxEnergyStored() {
			return MAX_ENERGY;
		}

		// todo allow pulling?
		@Override public boolean canExtract() { return false; }
		@Override public int extractEnergy(int maxExtract, boolean simulate) { return 0; }

		@Override public int receiveEnergy(int maxReceive, boolean simulate) { return 0; }
		@Override public boolean canReceive() { return false; }
	};

	@Override
	public boolean hasCapability(@Nonnull Capability<?> cap, @Nullable EnumFacing side) {
		return cap == CapabilityEnergy.ENERGY || super.hasCapability(cap, side);
	}

	@Override
	@Nullable
	public <T> T getCapability(@Nonnull Capability<T> cap, @Nullable EnumFacing side) {
		if(cap == CapabilityEnergy.ENERGY) {
			return CapabilityEnergy.ENERGY.cast(energyHandler);
		} else return super.getCapability(cap, side);
	}

	@Override
	public void update() {
		if(!world.isRemote) {
			int transfer = Math.min(energy, 160 * MANA_TO_FE);
			energy -= transfer;
			energy += transmitEnergy(transfer);
		}
	}

	private int transmitEnergy(int energy) {
		for(EnumFacing e : EnumFacing.VALUES) {
			BlockPos neighbor = getPos().offset(e);
			if(!world.isBlockLoaded(neighbor))
				continue;

			TileEntity te = world.getTileEntity(neighbor);
			if(te == null)
				continue;

			IEnergyStorage storage = null;

			if(te.hasCapability(CapabilityEnergy.ENERGY, e.getOpposite())) {
				storage = te.getCapability(CapabilityEnergy.ENERGY, e.getOpposite());
			} else if(te.hasCapability(CapabilityEnergy.ENERGY, null)) {
				storage = te.getCapability(CapabilityEnergy.ENERGY, null);
			}

			if(storage != null) {
				energy -= storage.receiveEnergy(energy, false);

				if (energy <= 0)
					return 0;
			}
		}

		return energy;
	}

	@Override
	public int getCurrentMana() {
		return energy / MANA_TO_FE;
	}

	@Override
	public boolean isFull() {
		return energy >= MAX_ENERGY;
	}

	@Override
	public void recieveMana(int mana) {
		this.energy = Math.min(MAX_ENERGY, this.energy + mana * MANA_TO_FE);
	}

	@Override
	public boolean canRecieveManaFromBursts() {
		return true;
	}

	@Override
	public void writePacketNBT(NBTTagCompound cmp) {
		cmp.setInteger(TAG_MANA, energy);
	}

	@Override
	public void readPacketNBT(NBTTagCompound cmp) {
		energy = cmp.getInteger(TAG_MANA);
	}

}
