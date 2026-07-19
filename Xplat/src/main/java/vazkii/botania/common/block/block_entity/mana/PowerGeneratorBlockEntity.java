/*
 * This class is distributed as part of the Botania Mod.
 * Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 *
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 */
package vazkii.botania.common.block.block_entity.mana;

import net.minecraft.core.BlockPos;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientGamePacketListener;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;

import org.jetbrains.annotations.UnknownNullability;

import vazkii.botania.api.mana.ManaReceiver;
import vazkii.botania.common.block.block_entity.BotaniaBlockEntities;
import vazkii.botania.common.block.mana.ManaSpreaderBlock;
import vazkii.botania.common.helper.NbtHelper;
import vazkii.botania.common.item.lens.PotencyLens;
import vazkii.botania.xplat.XplatAbstractions;

public class PowerGeneratorBlockEntity extends BlockEntity implements ManaReceiver {
	private static final int MANA_TO_ENERGY_UNITS = XplatAbstractions.INSTANCE.isNeoForge() ? 10 : 3;
	private static final int MAX_MANA = PotencyLens.MAX_MANA_FACTOR * ManaSpreaderBlock.BURST_SIZE_GAIA;
	public static final int MAX_ENERGY = MANA_TO_ENERGY_UNITS * MAX_MANA;
	private static final int MAX_ENERGY_TRANSFER = MANA_TO_ENERGY_UNITS * ManaSpreaderBlock.BURST_SIZE_DEFAULT;

	private static final String TAG_ENERGY = "energy";
	private int energy = 0;

	public PowerGeneratorBlockEntity(BlockPos pos, BlockState state) {
		super(BotaniaBlockEntities.MANA_FLUXFIELD, pos, state);
	}

	public static void serverTick(Level level, BlockPos pos, BlockState state, PowerGeneratorBlockEntity self) {
		int toTransfer = Math.min(self.energy, MAX_ENERGY_TRANSFER);
		int unconsumed = XplatAbstractions.INSTANCE.transferEnergyToNeighbors(level, pos, toTransfer);
		if (unconsumed != toTransfer) {
			self.energy -= (toTransfer - unconsumed);
			self.setChanged();
		}
	}

	@Override
	@UnknownNullability
	public Level getManaReceiverLevel() {
		return getLevel();
	}

	@Override
	public BlockPos getManaReceiverPos() {
		return getBlockPos();
	}

	@Override
	public int getCurrentMana() {
		return energy / MANA_TO_ENERGY_UNITS;
	}

	@Override
	public boolean isFull() {
		return energy >= MAX_ENERGY;
	}

	@Override
	public void receiveMana(int mana) {
		this.energy = Math.min(MAX_ENERGY, this.energy + mana * MANA_TO_ENERGY_UNITS);
	}

	@Override
	public boolean canReceiveManaFromBursts() {
		return true;
	}

	@Override
	protected void saveAdditional(CompoundTag cmp, HolderLookup.Provider registries) {
		cmp.putInt(TAG_ENERGY, energy);
	}

	@Override
	protected void loadAdditional(CompoundTag cmp, HolderLookup.Provider registries) {
		energy = cmp.getInt(TAG_ENERGY);
	}

	@Override
	public CompoundTag getUpdateTag(HolderLookup.Provider registries) {
		var tag = super.getUpdateTag(registries);
		NbtHelper.putVarInt(tag, TAG_ENERGY, energy);
		return tag;
	}

	@Override
	public Packet<ClientGamePacketListener> getUpdatePacket() {
		return ClientboundBlockEntityDataPacket.create(this);
	}

	public int getEnergy() {
		return energy;
	}

}
