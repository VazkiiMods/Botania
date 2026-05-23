/*
 * This class is distributed as part of the Botania Mod.
 * Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 *
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 */
package vazkii.botania.common.block.block_entity.flower.functional;

import net.minecraft.core.BlockPos;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;

import vazkii.botania.api.block_entity.FunctionalFlowerBlockEntity;
import vazkii.botania.api.block_entity.RadiusDescriptor;
import vazkii.botania.common.block.BotaniaBlocks;
import vazkii.botania.common.block.block_entity.BotaniaBlockEntities;
import vazkii.botania.common.block.block_entity.FakeAirBlockEntity;
import vazkii.botania.common.helper.MathHelper;

public class BubbellBlockEntity extends FunctionalFlowerBlockEntity {
	private static final int RANGE = 12;
	private static final int RANGE_MINI = 6;
	private static final int COST_PER_TICK = 4;
	private static final String TAG_RANGE = "range";

	int range = 2;

	protected BubbellBlockEntity(BlockEntityType<?> type, BlockPos pos, BlockState state) {
		super(type, pos, state);
	}

	public BubbellBlockEntity(BlockPos pos, BlockState state) {
		this(BotaniaBlockEntities.BUBBELL, pos, state);
	}

	@Override
	public void tickFlower() {
		super.tickFlower();

		if (getLevel().isClientSide() || getMana() < COST_PER_TICK + 1) {
			return;
		}

		addMana(-COST_PER_TICK);

		if (range < getRange() && shouldUpdateThisTick()) {
			range++;
		}

		int rangeSqr = range * range;
		BlockPos effectivePos = getEffectivePos();
		for (BlockPos pos : MathHelper.aroundPosClosed(effectivePos, range)) {
			if (effectivePos.distSqr(pos) < rangeSqr) {
				BlockState state = getLevel().getBlockState(pos);
				if (state.is(Blocks.WATER)) {
					getLevel().setBlock(pos, BotaniaBlocks.fakeAir.defaultBlockState(), Block.UPDATE_CLIENTS);
					if (getLevel().getBlockEntity(pos) instanceof FakeAirBlockEntity air) {
						air.setFlower(this);
					}
				}
			}
		}
	}

	@Override
	protected int getUpdateInterval() {
		return 10;
	}

	public static boolean isValidBubbell(Level world, BlockPos pos) {
		if (world.getBlockEntity(pos) instanceof BubbellBlockEntity bubbell) {
			return bubbell.getMana() > COST_PER_TICK;
		}

		return false;
	}

	@Override
	public void saveAdditional(CompoundTag cmp, HolderLookup.Provider registries) {
		super.saveAdditional(cmp, registries);
		cmp.putInt(TAG_RANGE, range);
	}

	@Override
	public void loadAdditional(CompoundTag cmp, HolderLookup.Provider registries) {
		super.loadAdditional(cmp, registries);
		range = cmp.getInt(TAG_RANGE);
	}

	@Override
	public int getMaxMana() {
		return 2000;
	}

	@Override
	public int getColor() {
		return 0x0DCF89;
	}

	public int getRange() {
		return RANGE;
	}

	@Override
	public RadiusDescriptor getRadius() {
		return new RadiusDescriptor.Circle(getEffectivePos(), range);
	}

	public static class Mini extends BubbellBlockEntity {
		public Mini(BlockPos pos, BlockState state) {
			super(BotaniaBlockEntities.BUBBELL_CHIBI, pos, state);
		}

		@Override
		public int getRange() {
			return RANGE_MINI;
		}
	}

}
