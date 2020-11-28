/*
 * This class is distributed as part of the Botania Mod.
 * Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 *
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 */
package vazkii.botania.common.block.tile.mana;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.util.Tickable;

import vazkii.botania.api.mana.IManaReceiver;
import vazkii.botania.common.block.tile.ModTiles;
import vazkii.botania.common.block.tile.TileMod;
import vazkii.botania.common.core.handler.ConfigHandler;

public class TileRFGenerator extends TileMod implements IManaReceiver, Tickable {
	private static final int MANA_TO_FE = 10;
	private static final int MAX_ENERGY = 1280 * MANA_TO_FE;

	private static final String TAG_MANA = "mana";
	private int energy = 0;

	/*
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
		@Override
		public boolean canExtract() {
			return false;
		}
	
		@Override
		public int extractEnergy(int maxExtract, boolean simulate) {
			return 0;
		}
	
		@Override
		public int receiveEnergy(int maxReceive, boolean simulate) {
			return 0;
		}
	
		@Override
		public boolean canReceive() {
			return false;
		}
	};
	private final LazyOptional<IEnergyStorage> energyCap = LazyOptional.of(() -> energyHandler);
	*/

	public TileRFGenerator() {
		super(ModTiles.FLUXFIELD);
	}

	@Override
	public void tick() {
		if (!world.isClient && ConfigHandler.COMMON.fluxfieldEnabled.getValue()) {
			int transfer = Math.min(energy, 160 * MANA_TO_FE);
			energy -= transfer;
			energy += transmitEnergy(transfer);
		}
	}

	private int transmitEnergy(int energy) {
		/*
		for (Direction e : Direction.values()) {
			BlockPos neighbor = getPos().offset(e);
			if (!world.isChunkLoaded(neighbor)) {
				continue;
			}
		
			BlockEntity te = world.getBlockEntity(neighbor);
			if (te == null) {
				continue;
			}
		
			LazyOptional<IEnergyStorage> storage = LazyOptional.empty();
		
			if (te.getCapability(CapabilityEnergy.ENERGY, e.getOpposite()).isPresent()) {
				storage = te.getCapability(CapabilityEnergy.ENERGY, e.getOpposite());
			} else if (te.getCapability(CapabilityEnergy.ENERGY, null).isPresent()) {
				storage = te.getCapability(CapabilityEnergy.ENERGY, null);
			}
		
			if (storage.isPresent()) {
				energy -= storage.orElseThrow(NullPointerException::new).receiveEnergy(energy, false);
		
				if (energy <= 0) {
					return 0;
				}
			}
		}
		*/

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
	public void receiveMana(int mana) {
		this.energy = Math.min(MAX_ENERGY, this.energy + mana * MANA_TO_FE);
	}

	@Override
	public boolean canReceiveManaFromBursts() {
		return ConfigHandler.COMMON.fluxfieldEnabled.getValue();
	}

	@Override
	public void writePacketNBT(CompoundTag cmp) {
		cmp.putInt(TAG_MANA, energy);
	}

	@Override
	public void readPacketNBT(CompoundTag cmp) {
		energy = cmp.getInt(TAG_MANA);
	}

}
