/**
 * This class was created by <Vazkii>. It's distributed as
 * part of the Botania Mod. Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 * 
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 * 
 * File Created @ [Nov 14, 2014, 5:29:56 PM (GMT)]
 */
package vazkii.botania.common.block.string;

import net.minecraft.block.state.IBlockState;
import net.minecraft.util.EnumFacing;
import net.minecraft.world.World;
import vazkii.botania.api.state.BotaniaStateProps;
import vazkii.botania.common.block.tile.string.TileRedString;
import vazkii.botania.common.block.tile.string.TileRedStringContainer;
import vazkii.botania.common.lib.LibBlockNames;

public class BlockRedStringContainer extends BlockRedString {

	public BlockRedStringContainer() {
		super(LibBlockNames.RED_STRING_CONTAINER);
		setDefaultState(blockState.getBaseState().withProperty(BotaniaStateProps.FACING, EnumFacing.DOWN));
	}

	@Override
	public TileRedString createTileEntity(World world, IBlockState state) {
		return new TileRedStringContainer();
	}

}
