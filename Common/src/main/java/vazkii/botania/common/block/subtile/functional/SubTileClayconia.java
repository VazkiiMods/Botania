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
import net.minecraft.tags.BlockTags;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;

import vazkii.botania.api.subtile.RadiusDescriptor;
import vazkii.botania.api.subtile.TileEntityFunctionalFlower;
import vazkii.botania.common.block.ModSubtiles;
import vazkii.botania.xplat.BotaniaConfig;

import java.util.ArrayList;
import java.util.List;

public class SubTileClayconia extends TileEntityFunctionalFlower {
	private static final int COST = 80;
	private static final int RANGE = 5;
	private static final int RANGE_Y = 3;

	private static final int RANGE_MINI = 2;
	private static final int RANGE_Y_MINI = 1;

	protected SubTileClayconia(BlockEntityType<?> type, BlockPos pos, BlockState state) {
		super(type, pos, state);
	}

	public SubTileClayconia(BlockPos pos, BlockState state) {
		this(ModSubtiles.CLAYCONIA, pos, state);
	}

	@Override
	public void tickFlower() {
		super.tickFlower();

		if (!getLevel().isClientSide && ticksExisted % 5 == 0) {
			if (getMana() >= COST) {
				BlockPos coords = getCoordsToPut();
				if (coords != null) {
					int stateId = Block.getId(getLevel().getBlockState(coords));
					getLevel().removeBlock(coords, false);
					if (BotaniaConfig.common().blockBreakParticles()) {
						getLevel().levelEvent(2001, coords, stateId);
					}
					ItemEntity item = new ItemEntity(getLevel(), coords.getX() + 0.5, coords.getY() + 0.5, coords.getZ() + 0.5, new ItemStack(Items.CLAY_BALL));
					getLevel().addFreshEntity(item);
					addMana(-COST);
				}
			}
		}
	}

	public BlockPos getCoordsToPut() {
		List<BlockPos> possibleCoords = new ArrayList<>();

		int range = getRange();
		int rangeY = getRangeY();

		for (int i = -range; i < range + 1; i++) {
			for (int j = -rangeY; j < rangeY + 1; j++) {
				for (int k = -range; k < range + 1; k++) {
					BlockPos pos = getEffectivePos().offset(i, j, k);
					BlockState state = getLevel().getBlockState(pos);
					if (state.is(BlockTags.SAND)) {
						possibleCoords.add(pos);
					}
				}
			}
		}

		if (possibleCoords.isEmpty()) {
			return null;
		}
		return possibleCoords.get(getLevel().random.nextInt(possibleCoords.size()));
	}

	@Override
	public RadiusDescriptor getRadius() {
		return new RadiusDescriptor.Square(getEffectivePos(), getRange());
	}

	public int getRange() {
		return RANGE;
	}

	public int getRangeY() {
		return RANGE_Y;
	}

	@Override
	public int getColor() {
		return 0x7B8792;
	}

	@Override
	public int getMaxMana() {
		return 640;
	}

	public static class Mini extends SubTileClayconia {
		public Mini(BlockPos pos, BlockState state) {
			super(ModSubtiles.CLAYCONIA_CHIBI, pos, state);
		}

		@Override
		public int getRange() {
			return RANGE_MINI;
		}

		@Override
		public int getRangeY() {
			return RANGE_Y_MINI;
		}
	}
}
