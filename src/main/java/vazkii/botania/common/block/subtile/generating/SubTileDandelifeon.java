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
import java.util.Random;

import net.minecraft.block.Block;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;
import vazkii.botania.api.lexicon.LexiconEntry;
import vazkii.botania.api.subtile.RadiusDescriptor;
import vazkii.botania.api.subtile.SubTileGenerating;
import vazkii.botania.common.Botania;
import vazkii.botania.common.block.BlockCell;
import vazkii.botania.common.block.ModBlocks;
import vazkii.botania.common.block.tile.TileCell;
import vazkii.botania.common.lexicon.LexiconData;

public class SubTileDandelifeon extends SubTileGenerating {

	private static final int RANGE = 12;
	private static final int SPEED = 10;
	private static final int MAX_GENERATIONS = 60;
	private static final int MANA_PER_GEN = 35;
	private static final int GEN_0_EXCLUSION_RADIUS = 3;
	private static final int MAKE_MANA_RADIUS = 1;

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

	private int[] blockageLocation = null;

	@Override
	public void onUpdate() {
		super.onUpdate();

		if(!supertile.getWorldObj().isRemote && redstoneSignal > 0 && ticksExisted % SPEED == 0)
			runSimulation();
		if(redstoneSignal > 0 && blockageLocation != null)
			spawnBlockageParticles();
	}

	void runSimulation() {
		int[][] table = getCellTable();

		if (blockageLocation != null) {
			if (table[blockageLocation[0]][blockageLocation[1]] == -1 ||
					getAdjCells(table, blockageLocation[0], blockageLocation[1]) != 3) {
				colorBlockageCells(0);
				blockageLocation = null;
				sync();
			} else {
				return;
			}
		}

		List<int[]> changes = new ArrayList<int[]>();

		for(int i = 0; i < table.length; i++)
			for(int j = 0; j < table[0].length; j++) {
				int gen = table[i][j];
				int adj = getAdjCells(table, i, j);

				int newVal = gen;
				if(adj < 2 || adj > 3)
					newVal = -1;
				else {
					if(adj == 3 && gen < 0)
						newVal = getSpawnCellGeneration(table, i, j);
					else if(gen > -1)
						newVal = gen + 1;
				}

				int xdist = Math.abs(i - RANGE);
				int zdist = Math.abs(j - RANGE);
				int allowDist = newVal == 1 && gen == 0 ? GEN_0_EXCLUSION_RADIUS : MAKE_MANA_RADIUS;
				if(xdist <= allowDist && zdist <= allowDist && newVal > -1) {
					gen = newVal;
					newVal = gen == 1 ? -1 : -2;
				}

				if(newVal != gen) {
					if (gen == -3 && newVal > -1) {
						blockageLocation = new int[] { i, j };
						colorBlockageCells(1);
						sync();
						return;
					}
					changes.add(new int[] { i, j, newVal, gen });
				}
			}

		int x = supertile.xCoord;
		int y = supertile.yCoord;
		int z = supertile.zCoord;

		for(int[] change : changes) {
			int px = x - RANGE + change[0];
			int pz = z - RANGE + change[1];
			int val = change[2];
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

	/**
	 * >= 0 - cell
	 * -1 - air
	 * -2 - consume this cell for mana
	 * -3 - not air
	 */
	int getCellGeneration(int x, int y, int z) {
		final World world = supertile.getWorldObj();
		TileEntity tile = world.getTileEntity(x, y, z);
		if(tile instanceof TileCell)
			return ((TileCell) tile).getGeneration();
		if(!world.getBlock(x, y ,z).isAir(world, x, y, z))
			return -3;

		return -1;
	}

	int getAdjCells(int[][] table, int x, int z) {
		int count = 0;
		for(int[] shift : ADJACENT_BLOCKS) {
			int xp = x + shift[0];
			int zp = z + shift[1];
			if(!isOffBounds(table, xp, zp)) {
				int gen = table[xp][zp];
				if(gen > 0 || (gen == 0 && !isInGen0ExclusionZone(xp, zp)))
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
	
	boolean isInGen0ExclusionZone(int x, int z) {
		return Math.abs(x - RANGE) <= GEN_0_EXCLUSION_RADIUS && Math.abs(z - RANGE) <= GEN_0_EXCLUSION_RADIUS;
	}

	void setBlockForGeneration(int x, int y, int z, int gen, int prevGen) {
		World world = supertile.getWorldObj();
		Block blockAt = world.getBlock(x, y, z);
		if(gen == -2) {
			int val = prevGen * MANA_PER_GEN;
			mana = Math.min(getMaxMana(), mana + val);
			world.setBlockToAir(x, y, z);
		} else if(blockAt == ModBlocks.cellBlock) {
			if(gen < 0 || gen > MAX_GENERATIONS)
				world.setBlockToAir(x, y, z);
			else ((TileCell) world.getTileEntity(x, y, z)).setGeneration(supertile, gen);
		} else if(gen >= 0 && blockAt.isAir(supertile.getWorldObj(), x, y, z)) {
			world.setBlock(x, y, z, ModBlocks.cellBlock);
			((TileCell) world.getTileEntity(x, y, z)).setGeneration(supertile, gen);
		}
	}

	void spawnBlockageParticles() {
		if(supertile.getWorldObj().rand.nextInt(4) == 0) {
			Random rand = supertile.getWorldObj().rand;
			int blockageX = supertile.xCoord + (blockageLocation[0] - RANGE);
			int blockageZ = supertile.zCoord + (blockageLocation[1] - RANGE);
			Botania.proxy.wispFX(supertile.getWorldObj(),
					supertile.xCoord + 0.55 + Math.random() * 0.2 - 0.1,
					supertile.yCoord + 0.5,
					supertile.zCoord + 0.55 + Math.random() * 0.2 - 0.1,
					0.6F, 0.03F, 0.03F,
					(float) Math.random() / 3, (float) -Math.random() / 20);
			Botania.proxy.wispFX(supertile.getWorldObj(),
					blockageX + 0.55 + Math.random() * 0.2 - 0.1,
					supertile.yCoord + 1,
					blockageZ + 0.55 + Math.random() * 0.2 - 0.1,
					0.6F, 0.03F, 0.03F,
					(float) (1 + Math.random()) / 5,
					(float) rand.nextGaussian() / 20,
					(float) (0.05 + rand.nextDouble() / 10),
					(float) rand.nextGaussian() / 20);
		}
	}

	void colorBlockageCells(int newMeta) {
		int blockageX = supertile.xCoord + (blockageLocation[0] - RANGE);
		int blockageZ = supertile.zCoord + (blockageLocation[1] - RANGE);
		World world = supertile.getWorldObj();
		for (int[] shift : ADJACENT_BLOCKS) {
			Block block = world.getBlock(blockageX + shift[0], supertile.yCoord, blockageZ + shift[1]);
			if (block instanceof BlockCell) {
				world.setBlockMetadataWithNotify(blockageX + shift[0], supertile.yCoord, blockageZ + shift[1], newMeta, 2);
			}
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

	private static final String TAG_BLOCKAGE = "blockage";
	private static final String TAG_BLOCKAGE_X = "blockageX";
	private static final String TAG_BLOCKAGE_Z = "blockageZ";
	@Override
	public void writeToPacketNBT(NBTTagCompound cmp) {
		super.writeToPacketNBT(cmp);

		if (blockageLocation == null) {
			cmp.setBoolean(TAG_BLOCKAGE, false);
		} else {
			cmp.setBoolean(TAG_BLOCKAGE, true);
			cmp.setInteger(TAG_BLOCKAGE_X, blockageLocation[0]);
			cmp.setInteger(TAG_BLOCKAGE_Z, blockageLocation[1]);
		}
	}

	@Override
	public void readFromPacketNBT(NBTTagCompound cmp) {
		super.readFromPacketNBT(cmp);

		if (cmp.getBoolean(TAG_BLOCKAGE)) {
			blockageLocation = new int[] { cmp.getInteger(TAG_BLOCKAGE_X), cmp.getInteger(TAG_BLOCKAGE_Z) };
		} else {
			blockageLocation = null;
		}
	}
}
