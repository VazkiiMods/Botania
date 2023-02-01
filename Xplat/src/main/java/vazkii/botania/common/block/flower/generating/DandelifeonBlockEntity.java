/*
 * This class is distributed as part of the Botania Mod.
 * Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 *
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 */
package vazkii.botania.common.block.flower.generating;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;

import vazkii.botania.api.block_entity.GeneratingFlowerBlockEntity;
import vazkii.botania.api.block_entity.RadiusDescriptor;
import vazkii.botania.common.block.BotaniaBlocks;
import vazkii.botania.common.block.BotaniaFlowerBlocks;
import vazkii.botania.common.block.block_entity.CellularBlockEntity;

import java.util.ArrayList;
import java.util.List;

public class DandelifeonBlockEntity extends GeneratingFlowerBlockEntity {
	private static final int RANGE = 12;
	private static final int SPEED = 10;
//	private static final int MAX_GENERATIONS = 100;
	private static final int MAX_MANA_GENERATIONS = 100;
	private static final int MANA_PER_GEN = 60;

	private static final int[][] ADJACENT_BLOCKS = new int[][] {
			{ -1, -1 },
			{ -1, +0 },
			{ -1, +1 },
			{ +0, +1 },
			{ +1, +1 },
			{ +1, +0 },
			{ +1, -1 },
			{ +0, -1 }
	};

	public DandelifeonBlockEntity(BlockPos pos, BlockState state) {
		super(BotaniaFlowerBlocks.DANDELIFEON, pos, state);
	}

	@Override
	public void tickFlower() {
		super.tickFlower();

		if (!getLevel().isClientSide) {
			if (getLevel().getGameTime() % SPEED == 0 && getLevel().hasNeighborSignal(getBlockPos())) {
				runSimulation();
			}
		}
	}

	private void runSimulation() {
		var table = new CellTable(RANGE, this);
		List<LifeUpdate> changes = new ArrayList<>();
		boolean wipe = false;

		for (int i = 0; i < table.diam; i++) {
			for (int j = 0; j < table.diam; j++) {
				int cell = table.at(i, j);
				int adj = table.getAdjCells(i, j);

				int cellUpdate;
				if (adj == 3 && cell == Cell.DEAD) {
					// spawn new cell
					cellUpdate = table.getSpawnCellGeneration(i, j);
				} else if ((adj == 2 || adj == 3) && Cell.isLive(cell)) {
					// sustain extant cell
					cellUpdate = cell + 1;
				} else {
					// kill cell
					cellUpdate = -1;
				}

				int xdist = Math.abs(i - RANGE);
				int zdist = Math.abs(j - RANGE);
				int allowDist = 1;
				if (xdist <= allowDist && zdist <= allowDist && cellUpdate > -1) {
					if (cell == 1) {
						// fresh cells shouldn't be placed here at all
						cellUpdate = Cell.DEAD;
					} else {
						// save the new value and consume this cell
						cell = cellUpdate;
						cellUpdate = Cell.CONSUME;
						wipe = true;
					}
				}

				if (cellUpdate != cell) {
					changes.add(new LifeUpdate(i, j, cellUpdate, cell));
				}
			}
		}

		for (var change : changes) {
			BlockPos pos_ = table.center.offset(-RANGE + change.x(), 0, -RANGE + change.z());
			int newLife = change.newLife();
			if (newLife != Cell.CONSUME && wipe) {
				newLife = Cell.DEAD;
			}

			setBlockForGeneration(pos_, Math.min(newLife, MAX_MANA_GENERATIONS), change.oldLife());
		}
	}

	void setBlockForGeneration(BlockPos pos, int cell, int prevCell) {
		Level world = getLevel();
		BlockState stateAt = world.getBlockState(pos);
		BlockEntity tile = world.getBlockEntity(pos);
		if (cell == Cell.CONSUME) {
			int val = prevCell * MANA_PER_GEN;
			world.removeBlock(pos, true);
			addMana(val);
			sync();
		} else if (tile instanceof CellularBlockEntity cellBlock) {
			cellBlock.setGeneration(this, cell);
		} else if (Cell.isLive(cell) && stateAt.isAir()) {
			world.setBlockAndUpdate(pos, BotaniaBlocks.cellBlock.defaultBlockState());
			tile = world.getBlockEntity(pos);
			((CellularBlockEntity) tile).setGeneration(this, cell);
			((CellularBlockEntity) tile).setImmediateGeneration(-1); // so that other flowers know this is 'dead' this tick
		}
	}

	public static final class Cell {
		private Cell() {}

		public static final int CONSUME = -2;
		public static final int DEAD = -1;

		public static boolean isLive(int i) {
			return i >= 0;
		}
	}
	private static class CellTable {
		public final BlockPos center;
		public final int diam;
		private int[][] cells;

		public CellTable(int range, DandelifeonBlockEntity dandie) {
			center = dandie.getEffectivePos();
			diam = range * 2 + 1;
			// store everything in range + one outside
			cells = new int[diam + 2][diam + 2];

			for (int i = -1; i <= diam; i++) {
				for (int j = -1; j <= diam; j++) {
					BlockPos pos = center.offset(-range + i, 0, -range + j);
					cells[i + 1][j + 1] = getCellGeneration(pos, dandie);
				}
			}
		}

		private static int getCellGeneration(BlockPos pos, DandelifeonBlockEntity dandie) {
			BlockEntity tile = dandie.getLevel().getBlockEntity(pos);
			if (tile instanceof CellularBlockEntity cell) {
				return cell.isSameFlower(dandie) ? cell.getGeneration() : (cell.getGeneration() >> 2);
			}

			return Cell.DEAD;
		}

		public boolean inBounds(int x, int z) {
			return x >= 0 && z >= 0 && x < diam && z < diam;
		}

		public int getAdjCells(int x, int z) {
			int count = 0;
			for (int[] shift : ADJACENT_BLOCKS) {
				if (Cell.isLive(this.at(x + shift[0], z + shift[1]))) {
					count++;
				}
			}

			return count;
		}

		public int getSpawnCellGeneration(int x, int z) {
			int max = -1;
			for (int[] shift : ADJACENT_BLOCKS) {
				max = Math.max(max, this.at(x + shift[0], z + shift[1]));
			}

			return max == -1 ? Cell.DEAD : max + 1;
		}

		public int at(int x, int z) {
			return cells[x + 1][z + 1];
		}
	}
	private static record LifeUpdate(int x, int z, int newLife, int oldLife) {}

	@Override
	public RadiusDescriptor getRadius() {
		return RadiusDescriptor.Rectangle.square(getEffectivePos(), RANGE);
	}

	@Override
	public RadiusDescriptor getSecondaryRadius() {
		return RadiusDescriptor.Rectangle.square(getEffectivePos(), 1);
	}

	@Override
	public int getMaxMana() {
		return 50000;
	}

	@Override
	public int getColor() {
		return 0x9c0a7e;
	}

}
