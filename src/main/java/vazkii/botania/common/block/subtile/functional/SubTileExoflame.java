/*
 * This class is distributed as part of the Botania Mod.
 * Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 *
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 */
package vazkii.botania.common.block.subtile.functional;

import net.minecraft.block.entity.BlockEntity;
import net.minecraft.util.math.BlockPos;

import vazkii.botania.api.item.IExoflameHeatable;
import vazkii.botania.api.subtile.RadiusDescriptor;
import vazkii.botania.api.subtile.TileEntityFunctionalFlower;
import vazkii.botania.common.block.ModSubtiles;
import vazkii.botania.common.components.BlockEntityComponents;

public class SubTileExoflame extends TileEntityFunctionalFlower {
	private static final int RANGE = 5;
	private static final int RANGE_Y = 2;
	private static final int COST = 300;

	public SubTileExoflame() {
		super(ModSubtiles.EXOFLAME);
	}

	@Override
	public void tickFlower() {
		super.tickFlower();

		if (getWorld().isClient || getMana() <= 2) {
			return;
		}

		boolean did = false;

		for (BlockPos pos : BlockPos.iterate(getEffectivePos().add(-RANGE, -RANGE_Y, -RANGE),
				getEffectivePos().add(RANGE, RANGE_Y, RANGE))) {
			BlockEntity tile = getWorld().getBlockEntity(pos);
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
