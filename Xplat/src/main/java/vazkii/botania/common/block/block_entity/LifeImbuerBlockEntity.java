/*
 * This class is distributed as part of the Botania Mod.
 * Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 *
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 */
package vazkii.botania.common.block.block_entity;

import net.minecraft.core.BlockPos;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;

import org.jetbrains.annotations.UnknownNullability;

import vazkii.botania.api.mana.ManaReceiver;
import vazkii.botania.api.state.BotaniaStateProperties;
import vazkii.botania.client.fx.WispParticleData;
import vazkii.botania.common.internal_caps.SlowDespawn;

public class LifeImbuerBlockEntity extends BlockEntity implements ManaReceiver {
	private static final String TAG_MANA = "mana";
	private static final int MAX_MANA = 160;
	private static final int MANA_PER_TICK = 6;

	private int mana = 0;

	public LifeImbuerBlockEntity(BlockPos pos, BlockState state) {
		super(BotaniaBlockEntities.LIFE_IMBUER, pos, state);
	}

	public boolean tryConsumeMana(boolean nearPlayer) {
		boolean canOperate = mana >= MANA_PER_TICK && level instanceof ServerLevel serverLevel
				&& serverLevel.getBlockState(getBlockPos().below()).is(Blocks.SPAWNER)
				&& serverLevel.isPositionEntityTicking(getBlockPos());

		if (canOperate && !nearPlayer) {
			receiveMana(-MANA_PER_TICK);
			if (!isActive()) {
				level.setBlock(getBlockPos(), getBlockState().setValue(BotaniaStateProperties.ACTIVE, true), Block.UPDATE_CLIENTS);
			}
		} else if (isActive()) {
			level.setBlock(getBlockPos(), getBlockState().setValue(BotaniaStateProperties.ACTIVE, false), Block.UPDATE_CLIENTS);
		}
		return canOperate;
	}

	public static void applySlowDespawn(ServerLevel level, BlockPos pos, Mob mob) {
		if (!level.getBlockState(pos).is(Blocks.SPAWNER)) {
			return;
		}

		SlowDespawn.MARKER.addFor(mob);
	}

	public boolean clientTickActive() {
		if (!isActive()) {
			return false;
		}
		if (Math.random() > 0.5) {
			WispParticleData data = WispParticleData.wisp((float) Math.random() / 3F,
					0.6F - (float) Math.random() * 0.3F, 0.1F, 0.6F - (float) Math.random() * 0.3F, 2F);
			level.addParticle(data,
					getBlockPos().getX() + 0.3 + Math.random() * 0.5, getBlockPos().getY() - 0.3 + Math.random() * 0.25, getBlockPos().getZ() + Math.random(),
					0, -(-0.025F - 0.005F * (float) Math.random()), 0);
		}
		return true;
	}

	private boolean isActive() {
		return getBlockState().getValue(BotaniaStateProperties.ACTIVE);
	}

	@Override
	protected void saveAdditional(CompoundTag cmp, HolderLookup.Provider registries) {
		cmp.putInt(TAG_MANA, mana);
	}

	@Override
	protected void loadAdditional(CompoundTag cmp, HolderLookup.Provider registries) {
		mana = cmp.getInt(TAG_MANA);
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
		return mana;
	}

	@Override
	public boolean isFull() {
		return mana >= MAX_MANA;
	}

	@Override
	public void receiveMana(int mana) {
		this.mana = Math.min(3 * MAX_MANA, this.mana + mana);
		level.blockEntityChanged(worldPosition);
	}

	@Override
	public boolean canReceiveManaFromBursts() {
		return true;
	}

}
