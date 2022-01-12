package vazkii.botania.forge.network;

import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerPlayer;
import net.minecraftforge.network.NetworkEvent;
import net.minecraftforge.network.NetworkRegistry;
import net.minecraftforge.network.simple.SimpleChannel;

import vazkii.botania.network.TriConsumer;
import vazkii.botania.network.clientbound.*;
import vazkii.botania.network.serverbound.PacketDodge;
import vazkii.botania.network.serverbound.PacketIndexKeybindRequest;
import vazkii.botania.network.serverbound.PacketJump;
import vazkii.botania.network.serverbound.PacketLeftClick;

import java.util.function.BiConsumer;
import java.util.function.Consumer;
import java.util.function.Supplier;

import static vazkii.botania.common.lib.ResourceLocationHelper.prefix;

public class ForgePacketHandler {
	public static final SimpleChannel CHANNEL = NetworkRegistry.newSimpleChannel(
			prefix("main"),
			// We don't use this
			() -> "0",
			"0"::equals,
			"0"::equals);

	public static void init() {
		int i = 0;
		// Serverbound
		CHANNEL.registerMessage(i++, PacketDodge.class, PacketDodge::encode, PacketDodge::decode,
				makeServerBoundHandler(PacketDodge::handle));
		CHANNEL.registerMessage(i++, PacketIndexKeybindRequest.class, PacketIndexKeybindRequest::encode, PacketIndexKeybindRequest::decode,
				makeServerBoundHandler(PacketIndexKeybindRequest::handle));
		CHANNEL.registerMessage(i++, PacketJump.class, PacketJump::encode, PacketJump::decode,
				makeServerBoundHandler(PacketJump::handle));
		CHANNEL.registerMessage(i++, PacketLeftClick.class, PacketLeftClick::encode, PacketLeftClick::decode,
				makeServerBoundHandler(PacketLeftClick::handle));

		// Clientbound
		CHANNEL.registerMessage(i++, PacketAvatarTornadoRod.class, PacketAvatarTornadoRod::encode, PacketAvatarTornadoRod::decode,
				makeClientBoundHandler(PacketAvatarTornadoRod.Handler::handle));
		CHANNEL.registerMessage(i++, PacketBotaniaEffect.class, PacketBotaniaEffect::encode, PacketBotaniaEffect::decode,
				makeClientBoundHandler(PacketBotaniaEffect.Handler::handle));
		CHANNEL.registerMessage(i++, PacketGogWorld.class, PacketGogWorld::encode, PacketGogWorld::decode,
				makeClientBoundHandler(PacketGogWorld.Handler::handle));
		CHANNEL.registerMessage(i++, PacketItemAge.class, PacketItemAge::encode, PacketItemAge::decode,
				makeClientBoundHandler(PacketItemAge.Handler::handle));
		CHANNEL.registerMessage(i++, PacketSpawnDoppleganger.class, PacketSpawnDoppleganger::encode, PacketSpawnDoppleganger::decode,
				makeClientBoundHandler(PacketSpawnDoppleganger.Handler::handle));
		CHANNEL.registerMessage(i++, PacketUpdateItemsRemaining.class, PacketUpdateItemsRemaining::encode, PacketUpdateItemsRemaining::decode,
				makeClientBoundHandler(PacketUpdateItemsRemaining.Handler::handle));
	}

	private static <T> BiConsumer<T, Supplier<NetworkEvent.Context>> makeServerBoundHandler(TriConsumer<T, MinecraftServer, ServerPlayer> handler) {
		return (m, ctx) -> handler.accept(m, ctx.get().getSender().getServer(), ctx.get().getSender());
	}

	private static <T> BiConsumer<T, Supplier<NetworkEvent.Context>> makeClientBoundHandler(Consumer<T> consumer) {
		return (m, _ctx) -> consumer.accept(m);
	}
}
