/*
 * This class is distributed as part of the Botania Mod.
 * Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 *
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 */
package vazkii.botania.common.block.tile.string;

import net.minecraft.tileentity.DispenserTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityType;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.registries.ObjectHolder;

import vazkii.botania.common.lib.LibBlockNames;
import vazkii.botania.common.lib.LibMisc;

public class TileRedStringDispenser extends TileRedStringContainer {
	@ObjectHolder(LibMisc.MOD_ID + ":" + LibBlockNames.RED_STRING_DISPENSER) public static TileEntityType<TileRedStringDispenser> TYPE;

	public TileRedStringDispenser() {
		super(TYPE);
	}

	@Override
	public boolean acceptBlock(BlockPos pos) {
		return world.getTileEntity(pos) instanceof DispenserTileEntity;
	}

	public void tickDispenser() {
		BlockPos bind = getBinding();
		if (bind != null) {
			TileEntity tile = world.getTileEntity(bind);
			if (tile instanceof DispenserTileEntity) {
				world.getPendingBlockTicks().scheduleTick(bind, tile.getBlockState().getBlock(), tile.getBlockState().getBlock().tickRate(world));
			}
		}
	}

}
