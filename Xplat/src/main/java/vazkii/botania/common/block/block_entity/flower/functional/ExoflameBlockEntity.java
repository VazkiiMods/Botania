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
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;

import vazkii.botania.api.block.ExoflameHeatable;
import vazkii.botania.api.block_entity.FunctionalFlowerBlockEntity;
import vazkii.botania.api.block_entity.RadiusDescriptor;
import vazkii.botania.common.block.block_entity.BotaniaBlockEntities;
import vazkii.botania.common.helper.MathHelper;
import vazkii.botania.xplat.XplatAbstractions;

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

		if (getLevel().isClientSide || getMana() <= 2) {
			return;
		}

		boolean updateThisTick = shouldUpdateThisTick();
		BlockPos effectivePos = getEffectivePos();

		for (BlockPos pos : MathHelper.aroundPosClosed(effectivePos, RANGE, RANGE_Y)) {
			BlockEntity tile = getLevel().getBlockEntity(pos);
			if (tile != null) {
				ExoflameHeatable heatable = XplatAbstractions.INSTANCE.findExoflameHeatable(getLevel(), pos, getLevel().getBlockState(pos), tile);
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
