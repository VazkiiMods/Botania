/*
 * This class is distributed as part of the Botania Mod.
 * Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 *
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 */
package vazkii.botania.common.network;

import net.minecraft.entity.Entity;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.network.NetworkDirection;
import net.minecraftforge.fml.network.NetworkRegistry;
import net.minecraftforge.fml.network.PacketDistributor;
import net.minecraftforge.fml.network.simple.SimpleChannel;

import static vazkii.botania.common.lib.ResourceLocationHelper.prefix;

public final class PacketHandler {
	private static final String PROTOCOL = "6";
	public static final SimpleChannel HANDLER = NetworkRegistry.newSimpleChannel(
			prefix("chan"),
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
		HANDLER.registerMessage(id++, PacketItemAge.class, PacketItemAge::encode, PacketItemAge::decode, PacketItemAge::handle);
		HANDLER.registerMessage(id++, PacketIndexKeybindRequest.class, PacketIndexKeybindRequest::encode, PacketIndexKeybindRequest::decode, PacketIndexKeybindRequest::handle);
		HANDLER.registerMessage(id++, PacketUpdateItemsRemaining.class, PacketUpdateItemsRemaining::encode, PacketUpdateItemsRemaining::decode, PacketUpdateItemsRemaining::handle);
	}

	/**
	 * Send message to all within 64 blocks that have this chunk loaded
	 */
	public static void sendToNearby(World world, BlockPos pos, Object toSend) {
		if (world instanceof ServerWorld) {
			ServerWorld ws = (ServerWorld) world;

			ws.getChunkManager().threadedAnvilChunkStorage.getPlayersWatchingChunk(new ChunkPos(pos), false)
					.filter(p -> p.squaredDistanceTo(pos.getX(), pos.getY(), pos.getZ()) < 64 * 64)
					.forEach(p -> HANDLER.send(PacketDistributor.PLAYER.with(() -> p), toSend));
		}
	}

	public static void sendToNearby(World world, Entity e, Object toSend) {
		sendToNearby(world, e.getBlockPos(), toSend);
	}

	public static void sendTo(ServerPlayerEntity playerMP, Object toSend) {
		HANDLER.sendTo(toSend, playerMP.networkHandler.getConnection(), NetworkDirection.PLAY_TO_CLIENT);
	}

	public static void sendNonLocal(ServerPlayerEntity playerMP, Object toSend) {
		if (playerMP.server.isDedicated() || !playerMP.getGameProfile().getName().equals(playerMP.server.getUserName())) {
			sendTo(playerMP, toSend);
		}
	}

	public static void sendToServer(Object msg) {
		HANDLER.sendToServer(msg);
	}

	private PacketHandler() {}

}
