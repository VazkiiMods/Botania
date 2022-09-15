/*
 * This class is distributed as part of the Botania Mod.
 * Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 *
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 */
package vazkii.botania.common.block.tile.mana;

import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;

import vazkii.botania.api.mana.ManaReceiver;
import vazkii.botania.common.block.tile.ModTiles;
import vazkii.botania.common.block.tile.TileMod;
import vazkii.botania.xplat.IXplatAbstractions;

public class PowerGeneratorBlockEntity extends TileMod implements ManaReceiver {
	private static final int MANA_TO_FE = IXplatAbstractions.INSTANCE.isForge() ? 10 : 3;
	public static final int MAX_ENERGY = 1280 * MANA_TO_FE;

	private static final String TAG_MANA = "mana";
	private int energy = 0;

	public PowerGeneratorBlockEntity(BlockPos pos, BlockState state) {
		super(ModTiles.FLUXFIELD, pos, state);
	}

	public static void serverTick(Level level, BlockPos pos, BlockState state, PowerGeneratorBlockEntity self) {
		int toTransfer = Math.min(self.energy, 160 * MANA_TO_FE);
		int unconsumed = IXplatAbstractions.INSTANCE.transferEnergyToNeighbors(level, pos, toTransfer);
		if (unconsumed != toTransfer) {
			self.energy -= (toTransfer - unconsumed);
			self.setChanged();
		}
	}

	@Override
	public Level getManaReceiverLevel() {
		return getLevel();
	}

	@Override
	public BlockPos getManaReceiverPos() {
		return getBlockPos();
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
		return true;
	}

	@Override
	public void writePacketNBT(CompoundTag cmp) {
		cmp.putInt(TAG_MANA, energy);
	}

	@Override
	public void readPacketNBT(CompoundTag cmp) {
		energy = cmp.getInt(TAG_MANA);
	}

	public int getEnergy() {
		return energy;
	}

}
