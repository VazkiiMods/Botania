package vazkii.botania.common.network;

import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.WorldServer;
import net.minecraftforge.fml.network.NetworkDirection;
import net.minecraftforge.fml.network.NetworkRegistry;
import net.minecraftforge.fml.network.simple.SimpleChannel;
import vazkii.botania.common.lib.LibMisc;

public final class PacketHandler {
	private static final String PROTOCOL = "1";
	private static final SimpleChannel HANDLER = NetworkRegistry.newSimpleChannel(
			new ResourceLocation(LibMisc.MOD_ID, "chan"),
			() -> PROTOCOL,
			PROTOCOL::equals,
			PROTOCOL::equals
	);

	public static void init() {
		int id = 0;
		HANDLER.registerMessage(id++, PacketBotaniaEffect.class, PacketBotaniaEffect::encode, PacketBotaniaEffect::decode, PacketBotaniaEffect.Handler::handle);
		HANDLER.registerMessage(id++, PacketLeftClick.class, PacketLeftClick::encode, PacketLeftClick::decode, PacketLeftClick::handle);
		HANDLER.registerMessage(id++, PacketDodge.class, PacketDodge::encode, PacketDodge::decode, PacketDodge::handle);
		HANDLER.registerMessage(id++, PacketJump.class, PacketJump::encode, PacketJump::decode, PacketJump::handle);
	}

	/**
	 * Send message to all within 64 blocks that have this chunk loaded
	 */
	public static void sendToNearby(World world, BlockPos pos, Object toSend) {
		if(world instanceof WorldServer) {
			WorldServer ws = (WorldServer) world;

			for (EntityPlayer player : ws.playerEntities) {
				EntityPlayerMP playerMP = (EntityPlayerMP) player;

				if (playerMP.getDistanceSq(pos) < 64 * 64
						&& ws.getPlayerChunkMap().isPlayerWatchingChunk(playerMP, pos.getX() >> 4, pos.getZ() >> 4)) {
					HANDLER.sendTo(toSend, playerMP.connection.getNetworkManager(), NetworkDirection.PLAY_TO_CLIENT);
				}
			}

		}
	}

	public static void sendToNearby(World world, Entity e, Object toSend) {
		sendToNearby(world, new BlockPos(e), toSend);
	}

	public static void sendTo(EntityPlayerMP playerMP, Object toSend) {
		HANDLER.sendTo(toSend, playerMP.connection.getNetworkManager(), NetworkDirection.PLAY_TO_CLIENT);
	}

	public static void sendToServer(Object msg) {
		HANDLER.sendToServer(msg);
	}

	private PacketHandler() {}

}
