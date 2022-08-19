/**
 * This class was created by <Vazkii>. It's distributed as
 * part of the Botania Mod. Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 * 
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 * 
 * File Created @ [Nov 16, 2014, 10:19:28 PM (GMT)]
 */
package vazkii.botania.common.block.string;

import net.minecraft.world.World;
import vazkii.botania.common.block.tile.string.TileRedString;
import vazkii.botania.common.block.tile.string.TileRedStringComparator;
import vazkii.botania.common.lib.LibBlockNames;

public class BlockRedStringComparator extends BlockRedString {

	public BlockRedStringComparator() {
		super(LibBlockNames.RED_STRING_COMPARATOR);
	}

	@Override
	public boolean hasComparatorInputOverride() {
		return true;
	}

	@Override
	public int getComparatorInputOverride(World world, int x, int y, int z, int side) {
		return ((TileRedStringComparator) world.getTileEntity(x, y, z)).getComparatorValue();
	}

	@Override
	public TileRedString createNewTileEntity(World world, int meta) {
		return new TileRedStringComparator();
	}

}
