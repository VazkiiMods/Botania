/*
 * This class is distributed as part of the Botania Mod.
 * Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 *
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 */
package vazkii.botania.common.block.subtile.generating;

import vazkii.botania.api.subtile.RadiusDescriptor;
import vazkii.botania.api.subtile.TileEntityGeneratingFlower;
import vazkii.botania.common.block.ModSubtiles;
import vazkii.botania.common.core.handler.ConfigHandler;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.tags.BlockTags;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class SubTileMunchdew extends TileEntityGeneratingFlower {
	public static final String TAG_COOLDOWN = "cooldown";
	private static final String TAG_ATE_ONCE = "ateOnce";

	private static final int RANGE = 8;
	private static final int RANGE_Y = 16;
	public static final int MANA_PER_LEAF = 160;

	private boolean ateOnce = false;
	private int ticksWithoutEating = -1;
	private int cooldown = 0;

	public SubTileMunchdew() {
		super(ModSubtiles.MUNCHDEW);
	}

	@Override
	public void tickFlower() {
		super.tickFlower();

		if (getWorld().isRemote) {
			return;
		}

		if (cooldown > 0) {
			cooldown--;
			ticksWithoutEating = 0;
			ateOnce = false; // don't start ticking ticksWithoutEating again until we eat again
			return;
		}

		eatLeaves: {
			if (getMaxMana() - getMana() >= MANA_PER_LEAF && ticksExisted % 4 == 0) {
				List<BlockPos> coords = new ArrayList<>();
				BlockPos pos = getEffectivePos();

				nextCoord: for (BlockPos pos_ : BlockPos.getAllInBoxMutable(pos.add(-RANGE, 0, -RANGE),
						pos.add(RANGE, RANGE_Y, RANGE))) {
					if (getWorld().getBlockState(pos_).getBlock().isIn(BlockTags.LEAVES)) {
						for (Direction dir : Direction.values()) {
							if (getWorld().isAirBlock(pos_.offset(dir))) {
								coords.add(pos_.toImmutable());
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
				BlockState state = getWorld().getBlockState(breakCoords);
				getWorld().removeBlock(breakCoords, false);
				ticksWithoutEating = 0;
				ateOnce = true;
				if (ConfigHandler.COMMON.blockBreakParticles.get()) {
					getWorld().playEvent(2001, breakCoords, Block.getStateId(state));
				}
				addMana(MANA_PER_LEAF);
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
	public void writeToPacketNBT(CompoundNBT cmp) {
		super.writeToPacketNBT(cmp);

		cmp.putInt(TAG_COOLDOWN, cooldown);
		cmp.putBoolean(TAG_ATE_ONCE, ateOnce);
	}

	@Override
	public void readFromPacketNBT(CompoundNBT cmp) {
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
