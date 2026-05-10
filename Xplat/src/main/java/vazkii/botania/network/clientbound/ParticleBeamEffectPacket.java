package vazkii.botania.network.clientbound;

import net.minecraft.core.BlockPos;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.world.entity.player.Player;

import vazkii.botania.common.item.WandOfTheForestItem;

import static vazkii.botania.api.BotaniaAPI.botaniaRL;

import io.netty.buffer.ByteBuf;

public record ParticleBeamEffectPacket(BlockPos source, BlockPos target) implements CustomPacketPayload {

	public static final Type<ParticleBeamEffectPacket> ID = new Type<>(botaniaRL("pb"));
	public static final StreamCodec<ByteBuf, ParticleBeamEffectPacket> STREAM_CODEC = StreamCodec.composite(
			BlockPos.STREAM_CODEC, ParticleBeamEffectPacket::source,
			BlockPos.STREAM_CODEC, ParticleBeamEffectPacket::target,
			ParticleBeamEffectPacket::new
	);

	@Override
	public Type<ParticleBeamEffectPacket> type() {
		return ID;
	}

	public static class Handler {
		public static void handle(ParticleBeamEffectPacket packet, Player localPlayer) {
			WandOfTheForestItem.doParticleBeam(localPlayer.level(),
					packet.source().getCenter(), packet.target().getCenter());
		}
	}
}
