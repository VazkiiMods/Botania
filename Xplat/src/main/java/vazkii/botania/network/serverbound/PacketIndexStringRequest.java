package vazkii.botania.network.serverbound;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerPlayer;

import vazkii.botania.common.block.tile.corporea.CorporeaIndexBlockEntity;
import vazkii.botania.network.IPacket;

import static vazkii.botania.common.lib.ResourceLocationHelper.prefix;

public record PacketIndexStringRequest(String message) implements IPacket {
	public static final ResourceLocation ID = prefix("idxs");

	public static PacketIndexStringRequest decode(FriendlyByteBuf buf) {
		return new PacketIndexStringRequest(buf.readUtf());
	}

	@Override
	public void encode(FriendlyByteBuf buf) {
		buf.writeUtf(message);
	}

	@Override
	public ResourceLocation getFabricId() {
		return ID;
	}

	public void handle(MinecraftServer server, ServerPlayer player) {
		server.execute(() -> CorporeaIndexBlockEntity.onChatMessage(player, message()));
	}
}
