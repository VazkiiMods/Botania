/*
 * This class is distributed as part of the Botania Mod.
 * Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 *
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 */
package vazkii.botania.api.internal;

import net.minecraft.block.entity.BlockEntity;
import net.minecraft.network.packet.s2c.play.BlockEntityUpdateS2CPacket;
import net.minecraft.server.world.ServerChunkManager;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.ChunkPos;

public final class VanillaPacketDispatcher {

	public static void dispatchTEToNearbyPlayers(BlockEntity tile) {
		if (tile.getWorld() instanceof ServerWorld) {
			BlockEntityUpdateS2CPacket packet = tile.toUpdatePacket();
			if (packet != null) {
				BlockPos pos = tile.getPos();
				((ServerChunkManager) tile.getWorld().getChunkManager()).threadedAnvilChunkStorage
						.getPlayersWatchingChunk(new ChunkPos(pos), false)
						.forEach(e -> e.networkHandler.sendPacket(packet));
			}
		}
	}

}
