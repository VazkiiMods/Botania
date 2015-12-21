/**
 * This class was created by <Vazkii>. It's distributed as
 * part of the Botania Mod. Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 * 
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 * 
 * File Created @ [Sep 21, 2015, 4:58:20 PM (GMT)]
 */
package vazkii.botania.common.block.tile.string;

import java.util.ArrayList;
import java.util.List;

import net.minecraft.block.Block;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.ITickable;
import net.minecraft.util.BlockPos;
import net.minecraft.world.World;
import vazkii.botania.api.state.BotaniaStateProps;

public class TileRedStringInterceptor extends TileRedString implements ITickable {

	public static List<TileRedStringInterceptor> interceptors = new ArrayList();

	@Override
	public void update() {
		if(!interceptors.contains(this))
			interceptors.add(this);
	}

	@Override
	public boolean acceptBlock(BlockPos pos) {
		return worldObj.getTileEntity(pos) != null;
	}

	public boolean removeFromList() {
		return !tileEntityInvalid && worldObj.getTileEntity(pos) == this;
	}

	public static void onInteract(EntityPlayer player, World world, BlockPos pos) {
		List<TileRedStringInterceptor> remove = new ArrayList();
		boolean did = false;

		// CMEs are amazing
		List<TileRedStringInterceptor> interceptorsCopy = new ArrayList(interceptors);
		
		for(TileRedStringInterceptor inter : interceptorsCopy) {
			if(!inter.removeFromList()) {
				remove.add(inter);
				continue;
			}

			if(inter.worldObj == world) {
				BlockPos coords = inter.getBinding();
				if(coords != null && coords.equals(pos)) {
					if(!world.isRemote) {
						Block block = inter.getBlockType();
						world.setBlockState(inter.getPos(), world.getBlockState(inter.getPos()).withProperty(BotaniaStateProps.POWERED, true), 1 | 2);
						world.scheduleUpdate(inter.getPos(), block, block.tickRate(world));
					}

					did = true;
				}
			}
		}

		interceptors.removeAll(remove);
		if(did) {
			if(world.isRemote)
				player.swingItem();
			else world.playSoundEffect(pos.getX() + 0.5, pos.getY() + 0.5, pos.getZ() + 0.5, "random.click", 0.3F, 0.6F);
		}
	}

}
