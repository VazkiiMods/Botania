/*
 * This class is distributed as part of the Botania Mod.
 * Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 *
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 */
package org.violetmoon.botania.common.block.block_entity.flower.functional;

import net.minecraft.core.BlockPos;
import net.minecraft.core.SectionPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;

import org.violetmoon.botania.api.block.ExoflameHeatable;
import org.violetmoon.botania.api.block_entity.FunctionalFlowerBlockEntity;
import org.violetmoon.botania.api.block_entity.RadiusDescriptor;
import org.violetmoon.botania.common.block.block_entity.BotaniaBlockEntities;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class ExoflameBlockEntity extends FunctionalFlowerBlockEntity {
	private static final int RANGE = 5;
	private static final int RANGE_Y = 2;
	private static final int COST = 300;

	public ExoflameBlockEntity(BlockPos pos, BlockState state) {
		super(BotaniaBlockEntities.EXOFLAME, pos, state);
	}

	@Override
	public void tickFlower() {
		super.tickFlower();

		if (getLevel().isClientSide() || getMana() <= 2) {
			return;
		}

		boolean updateThisTick = shouldUpdateThisTick();
		BlockPos effectivePos = getEffectivePos();

		for (BlockEntity tile : blockEntitiesInRange(effectivePos)) {
			BlockPos pos = tile.getBlockPos();
			ExoflameHeatable heatable = ExoflameHeatable.LOOKUP.find(getLevel(), pos, getLevel().getBlockState(pos), tile);
			if (heatable != null) {
				if (heatable.canSmelt() && getMana() >= COST) {
					if (heatable.getBurnTime() < 2) {
						heatable.boostBurnTime();
						addMana(-COST);
					}

					if (updateThisTick) {
						heatable.boostCookTime();
					}

					if (getMana() <= 0) {
						break;
					}
				}
			}
		}
	}

	/**
	 * The block entities in range, in the order the old loop over every position visited them
	 * (x fastest, then y, then z). That loop called getBlockEntity on all 605 positions every tick; this reads the
	 * block entity maps of the 1-4 chunks in range instead. The order only matters when mana runs out mid-loop.
	 */
	private List<BlockEntity> blockEntitiesInRange(BlockPos center) {
		Level level = getLevel();
		int minX = center.getX() - RANGE;
		int maxX = center.getX() + RANGE;
		int minY = center.getY() - RANGE_Y;
		int maxY = center.getY() + RANGE_Y;
		int minZ = center.getZ() - RANGE;
		int maxZ = center.getZ() + RANGE;
		List<BlockEntity> result = new ArrayList<>();
		for (int cz = SectionPos.blockToSectionCoord(minZ); cz <= SectionPos.blockToSectionCoord(maxZ); cz++) {
			for (int cx = SectionPos.blockToSectionCoord(minX); cx <= SectionPos.blockToSectionCoord(maxX); cx++) {
				for (BlockEntity be : level.getChunk(cx, cz).getBlockEntities().values()) {
					BlockPos pos = be.getBlockPos();
					if (!be.isRemoved() && pos.getX() >= minX && pos.getX() <= maxX && pos.getY() >= minY
							&& pos.getY() <= maxY && pos.getZ() >= minZ && pos.getZ() <= maxZ) {
						result.add(be);
					}
				}
			}
		}
		result.sort(Comparator.<BlockEntity>comparingInt(be -> be.getBlockPos().getZ())
				.thenComparingInt(be -> be.getBlockPos().getY())
				.thenComparingInt(be -> be.getBlockPos().getX()));
		return result;
	}

	@Override
	protected int getUpdateInterval() {
		return 2;
	}

	@Override
	public RadiusDescriptor getRadius() {
		return RadiusDescriptor.Rectangle.square(getEffectivePos(), RANGE);
	}

	@Override
	public int getMaxMana() {
		return 300;
	}

	@Override
	public int getColor() {
		return 0x661600;
	}

}
