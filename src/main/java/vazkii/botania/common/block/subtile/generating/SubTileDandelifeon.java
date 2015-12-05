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

import java.util.ArrayList;
import java.util.List;

import net.minecraft.block.Block;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;
import vazkii.botania.api.lexicon.LexiconEntry;
import vazkii.botania.api.subtile.RadiusDescriptor;
import vazkii.botania.api.subtile.SubTileGenerating;
import vazkii.botania.common.block.ModBlocks;
import vazkii.botania.common.block.tile.TileCell;
import vazkii.botania.common.lexicon.LexiconData;

public class SubTileDandelifeon extends SubTileGenerating {

	private static final int RANGE = 12;
	private static final int SPEED = 10;
	private static final int MAX_GENERATIONS = 60;
	private static final int MANA_PER_GEN = 150;

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

		if(!supertile.getWorldObj().isRemote && redstoneSignal > 0 && ticksExisted % SPEED == 0)
			runSimulation();
	}

	void runSimulation() {
		int[][] table = getCellTable();
		List<int[]> changes = new ArrayList();
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

		int x = supertile.xCoord;
		int y = supertile.yCoord;
		int z = supertile.zCoord;

		for(int[] change : changes) {
			int px = x - RANGE + change[0];
			int pz = z - RANGE + change[1];
			int val = change[2];
			if(val != -2 && wipe)
				val = -1;

			int old = change[3];

			setBlockForGeneration(px, y, pz, val, old);
		}
	}

	int[][] getCellTable() {
		int diam = RANGE * 2 + 1;
		int[][] table = new int[diam][diam];

		int x = supertile.xCoord;
		int y = supertile.yCoord;
		int z = supertile.zCoord;

		for(int i = 0; i < diam; i++)
			for(int j = 0; j < diam; j++) {
				int px = x - RANGE + i;
				int pz = z - RANGE + j;
				table[i][j] = getCellGeneration(px, y, pz);
			}

		return table;
	}

	int getCellGeneration(int x, int y, int z) {
		TileEntity tile = supertile.getWorldObj().getTileEntity(x, y, z);
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

	void setBlockForGeneration(int x, int y, int z, int gen, int prevGen) {
		World world = supertile.getWorldObj();
		Block blockAt = world.getBlock(x, y, z);
		TileEntity tile = world.getTileEntity(x, y, z);
		if(gen == -2) {
			int val = prevGen * MANA_PER_GEN;
			mana = Math.min(getMaxMana(), mana + val);
			//world.setBlockToAir(x, y, z);
		} else if(blockAt == ModBlocks.cellBlock) {
			if(gen < 0 || gen > MAX_GENERATIONS)
				world.setBlockToAir(x, y, z);
			else ((TileCell) tile).setGeneration(supertile, gen);
		} else if(gen >= 0 && blockAt.isAir(supertile.getWorldObj(), x, y, z)) {
			world.setBlock(x, y, z, ModBlocks.cellBlock);
			tile = world.getTileEntity(x, y, z);
			((TileCell) tile).setGeneration(supertile, gen);
		}
	}

	@Override
	public boolean acceptsRedstone() {
		return true;
	}

	@Override
	public RadiusDescriptor getRadius() {
		return new RadiusDescriptor.Square(toChunkCoordinates(), RANGE);
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
