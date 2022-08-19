/**
 * This class was created by <Vazkii>. It's distributed as
 * part of the Botania Mod. Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 * 
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 * 
 * File Created @ [Nov 16, 2014, 7:10:46 PM (GMT)]
 */
package vazkii.botania.common.block.string;

import java.util.Random;

import net.minecraft.block.IGrowable;
import net.minecraft.world.World;
import vazkii.botania.common.block.tile.string.TileRedString;
import vazkii.botania.common.block.tile.string.TileRedStringFertilizer;
import vazkii.botania.common.lib.LibBlockNames;

public class BlockRedStringFertilizer extends BlockRedString implements IGrowable {

	public BlockRedStringFertilizer() {
		super(LibBlockNames.RED_STRING_FERTILIZER);
	}

	@Override
	public boolean func_149851_a(World world, int x, int y, int z, boolean something) {
		return ((TileRedStringFertilizer) world.getTileEntity(x, y, z)).func_149851_a(world, something);
	}

	@Override
	public boolean func_149852_a(World world, Random rand, int x, int y, int z) {
		return ((TileRedStringFertilizer) world.getTileEntity(x, y, z)).func_149852_a(world, rand);
	}

	@Override
	public void func_149853_b(World world, Random rand, int x, int y, int z) {
		((TileRedStringFertilizer) world.getTileEntity(x, y, z)).func_149853_b(world, rand);
	}

	@Override
	public TileRedString createNewTileEntity(World world, int meta) {
		return new TileRedStringFertilizer();
	}

}
