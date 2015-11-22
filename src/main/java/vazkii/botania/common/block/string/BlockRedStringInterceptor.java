/**
 * This class was created by <Vazkii>. It's distributed as
 * part of the Botania Mod. Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 * 
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 * 
 * File Created @ [Sep 21, 2015, 4:56:52 PM (GMT)]
 */
package vazkii.botania.common.block.string;

import java.util.Random;

import net.minecraft.block.state.IBlockState;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.event.entity.player.PlayerInteractEvent.Action;
import vazkii.botania.common.block.tile.string.TileRedString;
import vazkii.botania.common.block.tile.string.TileRedStringInterceptor;
import vazkii.botania.common.lib.LibBlockNames;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public class BlockRedStringInterceptor extends BlockRedString {

	public BlockRedStringInterceptor() {
		super(LibBlockNames.RED_STRING_INTERCEPTOR);
		MinecraftForge.EVENT_BUS.register(this);
		// todo 1.8 completely override states from super
	}

	@SubscribeEvent
	public void onInteract(PlayerInteractEvent event) {
		if(event.action == Action.RIGHT_CLICK_BLOCK)
			TileRedStringInterceptor.onInteract(event.entityPlayer, event.world, event.pos);
	}

	@Override
	public boolean canProvidePower() {
		return true;
	}

	@Override
	public int isProvidingWeakPower(IBlockAccess world, BlockPos pos, IBlockState state, EnumFacing side) {
		return (world.getBlockMetadata(x, y, z) & 8) == 0 ? 0 : 15;
	}

	@Override
	public void updateTick(World world, BlockPos pos, IBlockState state, Random update) {
		world.setBlockMetadataWithNotify(x, y, z, world.getBlockMetadata(x, y, z) & -9, 1 | 2);
	}

	@Override
	public int tickRate(World p_149738_1_) {
		return 2;
	}

	@Override
	public TileRedString createNewTileEntity(World world, int meta) {
		return new TileRedStringInterceptor();
	}

}
