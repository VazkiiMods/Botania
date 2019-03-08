package vazkii.botania.common.network;

import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.WorldServer;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.SimpleNetworkWrapper;
import net.minecraftforge.fml.relauncher.Side;
import vazkii.botania.common.lib.LibMisc;

public final class PacketHandler {

	private static final SimpleNetworkWrapper HANDLER = new SimpleNetworkWrapper(LibMisc.NETWORK_CHANNEL);

	public static void init() {
		int id = 0;
		HANDLER.registerMessage(PacketBotaniaEffect.Handler.class, PacketBotaniaEffect.class, id++, Side.CLIENT);
		HANDLER.registerMessage(PacketLeftClick.Handler.class, PacketLeftClick.class, id++, Side.SERVER);
		HANDLER.registerMessage(PacketDodge.Handler.class, PacketDodge.class, id++, Side.SERVER);
		HANDLER.registerMessage(PacketJump.Handler.class, PacketJump.class, id++, Side.SERVER);
		HANDLER.registerMessage(PacketItemAge.Handler.class, PacketItemAge.class, id++, Side.CLIENT);
	}

	/**
	 * Send message to all within 64 blocks that have this chunk loaded
	 */
	public static void sendToNearby(World world, BlockPos pos, IMessage toSend) {
		if(world instanceof WorldServer) {
			WorldServer ws = (WorldServer) world;

			for (EntityPlayer player : ws.playerEntities) {
				EntityPlayerMP playerMP = (EntityPlayerMP) player;

				if (playerMP.getDistanceSq(pos) < 64 * 64
						&& ws.getPlayerChunkMap().isPlayerWatchingChunk(playerMP, pos.getX() >> 4, pos.getZ() >> 4)) {
					HANDLER.sendTo(toSend, playerMP);
				}
			}

		}
	}

	public static void sendToNearby(World world, Entity e, IMessage toSend) {
		sendToNearby(world, new BlockPos(e), toSend);
	}

	public static void sendTo(EntityPlayerMP playerMP, IMessage toSend) {
		HANDLER.sendTo(toSend, playerMP);
	}

	public static void sendToServer(IMessage msg) {
		HANDLER.sendToServer(msg);
	}

	private PacketHandler() {}

}
