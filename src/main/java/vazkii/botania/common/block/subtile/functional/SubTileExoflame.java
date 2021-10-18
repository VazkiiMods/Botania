/*
 * This class is distributed as part of the Botania Mod.
 * Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 *
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 */
package vazkii.botania.common.block.subtile.functional;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;

import vazkii.botania.api.block.IExoflameHeatable;
import vazkii.botania.api.subtile.RadiusDescriptor;
import vazkii.botania.api.subtile.TileEntityFunctionalFlower;
import vazkii.botania.common.block.ModSubtiles;
import vazkii.botania.common.components.BlockEntityComponents;

public class SubTileExoflame extends TileEntityFunctionalFlower {
	private static final int RANGE = 5;
	private static final int RANGE_Y = 2;
	private static final int COST = 300;

	public SubTileExoflame(BlockPos pos, BlockState state) {
		super(ModSubtiles.EXOFLAME, pos, state);
	}

	@Override
	public void tickFlower() {
		super.tickFlower();

		if (getLevel().isClientSide || getMana() <= 2) {
			return;
		}

		boolean did = false;

		for (BlockPos pos : BlockPos.betweenClosed(getEffectivePos().offset(-RANGE, -RANGE_Y, -RANGE),
				getEffectivePos().offset(RANGE, RANGE_Y, RANGE))) {
			BlockEntity tile = getLevel().getBlockEntity(pos);
			if (tile != null) {
				IExoflameHeatable heatable = BlockEntityComponents.EXOFLAME_HEATABLE.getNullable(tile);
				if (heatable != null) {
					if (heatable.canSmelt() && getMana() >= COST) {
						if (heatable.getBurnTime() < 2) {
							heatable.boostBurnTime();
							addMana(-COST);
							did = true;
						}

						if (ticksExisted % 2 == 0) {
							heatable.boostCookTime();
						}

						if (getMana() <= 0) {
							break;
						}
					}
				}
			}
		}
		if (did) {
			sync();
		}
	}

	@Override
	public RadiusDescriptor getRadius() {
		return new RadiusDescriptor.Square(getEffectivePos(), RANGE);
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
