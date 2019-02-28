/**
 * This class was created by <Vazkii>. It's distributed as
 * part of the Botania Mod. Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 *
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 *
 * File Created @ [Nov 14, 2014, 11:00:16 PM (GMT)]
 */
package vazkii.botania.common.block.tile.string;

import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityDispenser;
import net.minecraft.tileentity.TileEntityType;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.registries.ObjectHolder;
import vazkii.botania.common.lib.LibBlockNames;
import vazkii.botania.common.lib.LibMisc;

import javax.annotation.Nonnull;

public class TileRedStringDispenser extends TileRedStringContainer {
	@ObjectHolder(LibMisc.MOD_ID + ":" + LibBlockNames.RED_STRING_DISPENSER)
	public static TileEntityType<TileRedStringDispenser> TYPE;

	public TileRedStringDispenser() {
		super(TYPE);
	}

	@Override
	public boolean acceptBlock(BlockPos pos) {
		return world.getTileEntity(pos) instanceof TileEntityDispenser;
	}

	public void tickDispenser() {
		BlockPos bind = getBinding();
		if(bind != null) {
			TileEntity tile = world.getTileEntity(bind);
			if(tile instanceof TileEntityDispenser)
				world.getPendingBlockTicks().scheduleTick(bind, tile.getBlockState().getBlock(), tile.getBlockState().getBlock().tickRate(world));
		}
	}

}
