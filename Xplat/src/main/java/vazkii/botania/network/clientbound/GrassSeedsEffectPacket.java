package vazkii.botania.network.clientbound;

import net.minecraft.core.BlockPos;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.world.entity.player.Player;

import vazkii.botania.common.item.GrassSeedsItem;

import static vazkii.botania.api.BotaniaAPI.botaniaRL;

import io.netty.buffer.ByteBuf;

public record GrassSeedsEffectPacket(BlockPos pos, int color) implements CustomPacketPayload {

	public static final Type<GrassSeedsEffectPacket> ID = new Type<>(botaniaRL("gs"));
	public static final StreamCodec<ByteBuf, GrassSeedsEffectPacket> STREAM_CODEC = StreamCodec.composite(
			BlockPos.STREAM_CODEC, GrassSeedsEffectPacket::pos,
			ByteBufCodecs.INT, GrassSeedsEffectPacket::color,
			GrassSeedsEffectPacket::new
	);

	@Override
	public Type<GrassSeedsEffectPacket> type() {
		return ID;
	}

	public static class Handler {
		public static void handle(GrassSeedsEffectPacket packet, Player localPlayer) {
			GrassSeedsItem.spawnParticles(localPlayer.level(), packet.pos(), packet.color());
		}
	}
}
