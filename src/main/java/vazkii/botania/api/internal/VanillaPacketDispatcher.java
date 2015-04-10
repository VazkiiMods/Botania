/**
 * This class was created by <Vazkii>. It's distributed as
 * part of the Botania Mod. Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 * 
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 * 
 * File Created @ [Apr 9, 2015, 9:38:44 PM (GMT)]
 */
package vazkii.botania.api.internal;

import java.util.List;

import vazkii.botania.common.core.helper.MathHelper;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;
import net.minecraft.world.chunk.Chunk;

public final class VanillaPacketDispatcher {

	public static void dispatchTEToNearbyPlayers(TileEntity tile) {
		World world = tile.getWorldObj();
		List<EntityPlayer> players = world.playerEntities;
		for(EntityPlayer player : players)
			if(MathHelper.pointDistancePlane(player.posX, player.posZ, tile.xCoord + 0.5, tile.zCoord + 0.5) < 64 && player instanceof EntityPlayerMP)
				((EntityPlayerMP) player).playerNetServerHandler.sendPacket(tile.getDescriptionPacket());
	}
	
	public static void dispatchTEToNearbyPlayers(World world, int x, int y, int z) {
		TileEntity tile = world.getTileEntity(x, y, z);
		if(tile != null)
			dispatchTEToNearbyPlayers(tile);
	}
	
}
