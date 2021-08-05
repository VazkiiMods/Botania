/*
 * This class is distributed as part of the Botania Mod.
 * Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 *
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 */
package vazkii.botania.common.block.subtile.generating;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.tags.BlockTags;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;

import vazkii.botania.api.subtile.RadiusDescriptor;
import vazkii.botania.api.subtile.TileEntityGeneratingFlower;
import vazkii.botania.common.block.ModSubtiles;
import vazkii.botania.common.core.handler.ConfigHandler;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class SubTileMunchdew extends TileEntityGeneratingFlower {
	public static final String TAG_COOLDOWN = "cooldown";
	private static final String TAG_ATE_ONCE = "ateOnce";

	private static final int RANGE = 8;
	private static final int RANGE_Y = 16;

	private boolean ateOnce = false;
	private int ticksWithoutEating = -1;
	private int cooldown = 0;

	public SubTileMunchdew(BlockPos pos, BlockState state) {
		super(ModSubtiles.MUNCHDEW, pos, state);
	}

	@Override
	public void tickFlower() {
		super.tickFlower();

		if (getLevel().isClientSide) {
			return;
		}

		if (cooldown > 0) {
			cooldown--;
			ticksWithoutEating = 0;
			ateOnce = false; // don't start ticking ticksWithoutEating again until we eat again
			return;
		}

		int manaPerLeaf = 160;
		eatLeaves: {
			if (getMaxMana() - getMana() >= manaPerLeaf && ticksExisted % 4 == 0) {
				List<BlockPos> coords = new ArrayList<>();
				BlockPos pos = getEffectivePos();

				nextCoord: for (BlockPos pos_ : BlockPos.betweenClosed(pos.offset(-RANGE, 0, -RANGE),
						pos.offset(RANGE, RANGE_Y, RANGE))) {
					if (getLevel().getBlockState(pos_).getBlock().is(BlockTags.LEAVES)) {
						for (Direction dir : Direction.values()) {
							if (getLevel().isEmptyBlock(pos_.relative(dir))) {
								coords.add(pos_.immutable());
								break nextCoord;
							}
						}
					}
				}

				if (coords.isEmpty()) {
					break eatLeaves;
				}

				Collections.shuffle(coords);
				BlockPos breakCoords = coords.get(0);
				BlockState state = getLevel().getBlockState(breakCoords);
				getLevel().removeBlock(breakCoords, false);
				ticksWithoutEating = 0;
				ateOnce = true;
				if (ConfigHandler.COMMON.blockBreakParticles.getValue()) {
					getLevel().levelEvent(2001, breakCoords, Block.getId(state));
				}
				addMana(manaPerLeaf);
			}
		}

		if (ateOnce) {
			ticksWithoutEating++;
			if (ticksWithoutEating >= 5) {
				cooldown = 1600;
				sync();
			}
		}
	}

	@Override
	public RadiusDescriptor getRadius() {
		return new RadiusDescriptor.Square(getEffectivePos(), RANGE);
	}

	@Override
	public void writeToPacketNBT(CompoundTag cmp) {
		super.writeToPacketNBT(cmp);

		cmp.putInt(TAG_COOLDOWN, cooldown);
		cmp.putBoolean(TAG_ATE_ONCE, ateOnce);
	}

	@Override
	public void readFromPacketNBT(CompoundTag cmp) {
		super.readFromPacketNBT(cmp);

		cooldown = cmp.getInt(TAG_COOLDOWN);
		ateOnce = cmp.getBoolean(TAG_ATE_ONCE);
	}

	@Override
	public int getColor() {
		return 0x79C42F;
	}

	@Override
	public int getMaxMana() {
		return 10000;
	}

}
