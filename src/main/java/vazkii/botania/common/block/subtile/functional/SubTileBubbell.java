/*
 * This class is distributed as part of the Botania Mod.
 * Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 *
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 */
package vazkii.botania.common.block.subtile.functional;

import net.minecraft.block.BlockState;
import net.minecraft.block.Material;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import vazkii.botania.api.subtile.RadiusDescriptor;
import vazkii.botania.api.subtile.TileEntityFunctionalFlower;
import vazkii.botania.common.block.ModBlocks;
import vazkii.botania.common.block.ModSubtiles;
import vazkii.botania.common.block.tile.TileFakeAir;

public class SubTileBubbell extends TileEntityFunctionalFlower {
	private static final int RANGE = 12;
	private static final int RANGE_MINI = 6;
	private static final int COST_PER_TICK = 4;
	private static final String TAG_RANGE = "range";

	int range = 2;

	public SubTileBubbell(BlockEntityType<?> type) {
		super(type);
	}

	public SubTileBubbell() {
		this(ModSubtiles.BUBBELL);
	}

	@Override
	public void tickFlower() {
		super.tickFlower();

		if (getWorld().isClient) {
			return;
		}

		if (ticksExisted % 200 == 0) {
			sync();
		}

		if (getMana() > COST_PER_TICK) {
			addMana(-COST_PER_TICK);

			if (ticksExisted % 10 == 0 && range < getRange()) {
				range++;
			}

			for (BlockPos pos : BlockPos.iterate(getEffectivePos().add(-range, -range, -range), getEffectivePos().add(range, range, range))) {
				if (getEffectivePos().getSquaredDistance(pos) < range * range) {
					BlockState state = getWorld().getBlockState(pos);
					if (state.getMaterial() == Material.WATER) {
						getWorld().setBlockState(pos, ModBlocks.fakeAir.getDefaultState(), 2);
						TileFakeAir air = (TileFakeAir) getWorld().getBlockEntity(pos);
						air.setFlower(this);
					}
				}
			}
		}
	}

	public static boolean isValidBubbell(World world, BlockPos pos) {
		BlockEntity tile = world.getBlockEntity(pos);
		if (tile instanceof SubTileBubbell) {
			return ((SubTileBubbell) tile).getMana() > COST_PER_TICK;
		}

		return false;
	}

	@Override
	public void writeToPacketNBT(CompoundTag cmp) {
		super.writeToPacketNBT(cmp);
		cmp.putInt(TAG_RANGE, range);
	}

	@Override
	public void readFromPacketNBT(CompoundTag cmp) {
		super.readFromPacketNBT(cmp);
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

	public static class Mini extends SubTileBubbell {
		public Mini() {
			super(ModSubtiles.BUBBELL_CHIBI);
		}

		@Override
		public int getRange() {
			return RANGE_MINI;
		}
	}

}
