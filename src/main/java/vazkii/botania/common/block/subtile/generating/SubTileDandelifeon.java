/**
 * This class was created by <Vazkii>. It's distributed as
 * part of the Botania Mod. Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 *
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 *
 * File Created @ [Sep 6, 2015, 3:46:10 PM (GMT)]
 */
package vazkii.botania.common.block.subtile.generating;

import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import vazkii.botania.api.lexicon.LexiconEntry;
import vazkii.botania.api.subtile.RadiusDescriptor;
import vazkii.botania.api.subtile.SubTileGenerating;
import vazkii.botania.common.block.ModBlocks;
import vazkii.botania.common.block.tile.TileCell;
import vazkii.botania.common.lexicon.LexiconData;

import java.util.ArrayList;
import java.util.List;

public class SubTileDandelifeon extends SubTileGenerating {

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

	@Override
	public void onUpdate() {
		super.onUpdate();

		if(!supertile.getWorld().isRemote && redstoneSignal > 0 && ticksExisted % SPEED == 0)
			runSimulation();
	}

	void runSimulation() {
		int[][] table = getCellTable();
		List<int[]> changes = new ArrayList<>();
		new ArrayList();
		boolean wipe = false;

		for(int i = 0; i < table.length; i++)
			for(int j = 0; j < table[0].length; j++) {
				int gen = table[i][j];
				int adj = getAdjCells(table, i, j);

				int newVal = gen;
				if(adj < 2 || adj > 3)
					newVal = -1;
				else {
					if(adj == 3 && gen == -1)
						newVal = getSpawnCellGeneration(table, i, j);
					else if(gen > -1)
						newVal = gen + 1;
				}

				int xdist = Math.abs(i - RANGE);
				int zdist = Math.abs(j - RANGE);
				int allowDist = 1;
				if(xdist <= allowDist && zdist <= allowDist && newVal > -1) {
					gen = newVal;
					newVal = gen == 1 ? -1 : -2;
				}

				if(newVal != gen) {
					changes.add(new int[] { i, j, newVal, gen });
					if(newVal == -2)
						wipe = true;
				}
			}

		BlockPos pos = supertile.getPos();

		for(int[] change : changes) {
			BlockPos pos_ = pos.add(-RANGE + change[0], 0, -RANGE + change[1]);
			int val = change[2];
			if(val != -2 && wipe)
				val = -1;

			int old = change[3];

			setBlockForGeneration(pos_, val, old);
		}
	}

	int[][] getCellTable() {
		int diam = RANGE * 2 + 1;
		int[][] table = new int[diam][diam];

		BlockPos pos = supertile.getPos();

		for(int i = 0; i < diam; i++)
			for(int j = 0; j < diam; j++) {
				BlockPos pos_ = pos.add(-RANGE + i, 0, -RANGE + j);
				table[i][j] = getCellGeneration(pos_);
			}

		return table;
	}

	int getCellGeneration(BlockPos pos) {
		TileEntity tile = supertile.getWorld().getTileEntity(pos);
		if(tile instanceof TileCell)
			return ((TileCell) tile).isSameFlower(supertile) ? ((TileCell) tile).getGeneration() : 0;

			return -1;
	}

	int getAdjCells(int[][] table, int x, int z) {
		int count = 0;
		for(int[] shift : ADJACENT_BLOCKS) {
			int xp = x + shift[0];
			int zp = z + shift[1];
			if(!isOffBounds(table, xp, zp)) {
				int gen = table[xp][zp];
				if(gen >= 0)
					count++;
			}
		}

		return count;
	}

	int getSpawnCellGeneration(int[][] table, int x, int z) {
		int max = -1;
		for(int[] shift : ADJACENT_BLOCKS) {
			int xp = x + shift[0];
			int zp = z + shift[1];
			if(!isOffBounds(table, xp, zp)) {
				int gen = table[xp][zp];
				if(gen > max)
					max = gen;
			}
		}

		return max == -1 ? -1 : max + 1;
	}

	boolean isOffBounds(int[][] table, int x, int z) {
		return x < 0 || z < 0 || x >= table.length || z >= table[0].length;
	}

	void setBlockForGeneration(BlockPos pos, int gen, int prevGen) {
		World world = supertile.getWorld();
		IBlockState stateAt = world.getBlockState(pos);
		Block blockAt = stateAt.getBlock();
		TileEntity tile = world.getTileEntity(pos);
		if(gen == -2) {
			int val = Math.min(MAX_MANA_GENERATIONS, prevGen) * MANA_PER_GEN;
			mana = Math.min(getMaxMana(), mana + val);
			//world.setBlockToAir(x, y, z);
		} else if(blockAt == ModBlocks.cellBlock) {
			if(gen < 0)
				world.setBlockToAir(pos);
			else ((TileCell) tile).setGeneration(supertile, gen);
		} else if(gen >= 0 && blockAt.isAir(stateAt, supertile.getWorld(), pos)) {
			world.setBlockState(pos, ModBlocks.cellBlock.getDefaultState());
			tile = world.getTileEntity(pos);
			((TileCell) tile).setGeneration(supertile, gen);
		}
	}

	@Override
	public boolean acceptsRedstone() {
		return true;
	}

	@Override
	public RadiusDescriptor getRadius() {
		return new RadiusDescriptor.Square(toBlockPos(), RANGE);
	}

	@Override
	public int getMaxMana() {
		return 50000;
	}

	@Override
	public int getColor() {
		return 0x9c0a7e;
	}

	@Override
	public LexiconEntry getEntry() {
		return LexiconData.dandelifeon;
	}

}
