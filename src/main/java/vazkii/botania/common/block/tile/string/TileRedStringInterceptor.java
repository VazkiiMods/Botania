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
import net.minecraft.util.ChunkCoordinates;
import net.minecraft.world.World;

public class TileRedStringInterceptor extends TileRedString {

	public static List<TileRedStringInterceptor> interceptors = new ArrayList();

	@Override
	public void updateEntity() {
		super.updateEntity();

		if(!interceptors.contains(this))
			interceptors.add(this);
	}

	@Override
	public boolean acceptBlock(int x, int y, int z) {
		return worldObj.getTileEntity(x, y, z) != null;
	}

	public boolean removeFromList() {
		return !tileEntityInvalid && worldObj.getTileEntity(xCoord, yCoord, zCoord) == this;
	}

	public static void onInteract(EntityPlayer player, World world, int x, int y, int z) {
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
				ChunkCoordinates coords = inter.getBinding();
				if(coords != null && coords.posX == x && coords.posY == y && coords.posZ == z) {
					if(!world.isRemote) {
						Block block = inter.getBlockType();
						int meta = inter.getBlockMetadata();
						world.setBlockMetadataWithNotify(inter.xCoord, inter.yCoord, inter.zCoord, meta | 8, 1 | 2);
						world.scheduleBlockUpdate(inter.xCoord, inter.yCoord, inter.zCoord, block, block.tickRate(world));
					}

					did = true;
				}
			}
		}

		interceptors.removeAll(remove);
		if(did) {
			if(world.isRemote)
				player.swingItem();
			else world.playSoundEffect(x + 0.5, y + 0.5, z + 0.5, "random.click", 0.3F, 0.6F);
		}
	}

}
