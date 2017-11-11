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

import net.minecraft.network.play.server.SPacketUpdateTileEntity;
import net.minecraft.server.management.PlayerChunkMapEntry;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.WorldServer;

public final class VanillaPacketDispatcher {

	public static void dispatchTEToNearbyPlayers(TileEntity tile) {
		SPacketUpdateTileEntity packet = tile.getUpdatePacket();

		if(packet != null && tile.getWorld() instanceof WorldServer) {
			PlayerChunkMapEntry chunk = ((WorldServer) tile.getWorld()).getPlayerChunkMap().getEntry(tile.getPos().getX() >> 4, tile.getPos().getZ() >> 4);
			if(chunk != null) {
				chunk.sendPacket(packet);
			}
		}
	}

	public static void dispatchTEToNearbyPlayers(World world, BlockPos pos) {
		TileEntity tile = world.getTileEntity(pos);
		if(tile != null)
			dispatchTEToNearbyPlayers(tile);
	}

	public static float pointDistancePlane(double x1, double y1, double x2, double y2) {
		return (float) Math.hypot(x1 - x2, y1 - y2);
	}

}
