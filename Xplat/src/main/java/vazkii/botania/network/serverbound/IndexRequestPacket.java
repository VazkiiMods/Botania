package vazkii.botania.network.serverbound;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerPlayer;
import vazkii.botania.api.BotaniaAPI;
import vazkii.botania.api.corporea.CorporeaRequestMatcher;
import vazkii.botania.common.block.block_entity.corporea.CorporeaIndexBlockEntity;
import vazkii.botania.common.impl.corporea.CorporeaHelperImpl;
import vazkii.botania.common.impl.corporea.matcher.CorporeaConstantMatcher;
import vazkii.botania.network.BotaniaPacket;

import static vazkii.botania.common.lib.ResourceLocationHelper.prefix;

public record IndexRequestPacket(CorporeaRequestMatcher request, int count) implements BotaniaPacket {
	public static final ResourceLocation ID = prefix("idxr");

	public static IndexRequestPacket decode(FriendlyByteBuf buf) {
		var ty = buf.readResourceLocation();
		var deser = CorporeaHelperImpl.corporeaMatcherDeserializers.get(ty);
		if (deser == null || deser.bufDeser() == null) {
			// oh dear, oh god. someone is doing something very wrong. just do our best to bail out.
			return new IndexRequestPacket(CorporeaRequestMatcher.Dummy.INSTANCE, 0);
		}
		var req = deser.bufDeser().apply(buf);
		var count = buf.readVarInt();
		return new IndexRequestPacket(req, count);
	}

	@Override
	public void encode(FriendlyByteBuf buf) {
		var resloc = CorporeaHelperImpl.corporeaMatcherSerializers.get(this.request.getClass());
		if (resloc == null) {
			// oh god ummm uhhhhh
			new IndexRequestPacket(new CorporeaConstantMatcher(false), 0).encode(buf);
			return;
		} else {
			buf.writeResourceLocation(resloc);
		}
		this.request.writeToBuf(buf);
		buf.writeVarInt(this.count);
	}

	@Override
	public ResourceLocation getFabricId() {
		return ID;
	}

	public void handle(MinecraftServer server, ServerPlayer player) {
		server.execute(() -> CorporeaIndexBlockEntity.receiveRequestFromPlayer(player, this.request(), count));
	}
}
