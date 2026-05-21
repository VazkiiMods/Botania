package vazkii.botania.neoforge.network;

import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Player;
import net.neoforged.neoforge.network.event.RegisterPayloadHandlersEvent;
import net.neoforged.neoforge.network.handling.IPayloadHandler;
import net.neoforged.neoforge.network.registration.PayloadRegistrar;

import vazkii.botania.network.clientbound.ArenaIndicatorEffectPacket;
import vazkii.botania.network.clientbound.AvatarSkiesRodEffectPacket;
import vazkii.botania.network.clientbound.AvatarSkiesRodUpdatePacket;
import vazkii.botania.network.clientbound.BlackLotusDissolveEffectPacket;
import vazkii.botania.network.clientbound.DivaCharmEffectPacket;
import vazkii.botania.network.clientbound.EnchanterDestroyEffectPacket;
import vazkii.botania.network.clientbound.FluegelEyeEffectPacket;
import vazkii.botania.network.clientbound.GogWorldPacket;
import vazkii.botania.network.clientbound.GrassSeedsEffectPacket;
import vazkii.botania.network.clientbound.HaloCraftEffectPacket;
import vazkii.botania.network.clientbound.ItemLifeTimePacket;
import vazkii.botania.network.clientbound.ItemSmokeEffectPacket;
import vazkii.botania.network.clientbound.PaintLensEffectPacket;
import vazkii.botania.network.clientbound.ParticleBeamEffectPacket;
import vazkii.botania.network.clientbound.SparkManaFlowEffectPacket;
import vazkii.botania.network.clientbound.SparkNetIndicatorEffectPacket;
import vazkii.botania.network.clientbound.ThundercallerEffectPacket;
import vazkii.botania.network.clientbound.UpdateItemsRemainingPacket;
import vazkii.botania.network.serverbound.DodgePacket;
import vazkii.botania.network.serverbound.IndexKeybindRequestPacket;
import vazkii.botania.network.serverbound.IndexStringRequestPacket;
import vazkii.botania.network.serverbound.JumpPacket;
import vazkii.botania.network.serverbound.LeftClickPacket;

import java.util.function.BiConsumer;

public class ForgePacketHandler {
	public static void registerPayloadHandlers(final RegisterPayloadHandlersEvent event) {
		PayloadRegistrar registrar = event.registrar("1");

		registrar.playToServer(DodgePacket.ID, DodgePacket.STREAM_CODEC, makeServerBoundHandler(DodgePacket::handle));
		registrar.playToServer(IndexKeybindRequestPacket.ID, IndexKeybindRequestPacket.STREAM_CODEC, makeServerBoundHandler(IndexKeybindRequestPacket::handle));
		registrar.playToServer(IndexStringRequestPacket.ID, IndexStringRequestPacket.STREAM_CODEC, makeServerBoundHandler(IndexStringRequestPacket::handle));
		registrar.playToServer(JumpPacket.ID, JumpPacket.STREAM_CODEC, makeServerBoundHandler(JumpPacket::handle));
		registrar.playToServer(LeftClickPacket.ID, LeftClickPacket.STREAM_CODEC, makeServerBoundHandler(LeftClickPacket::handle));

		registrar.playToClient(ArenaIndicatorEffectPacket.ID, ArenaIndicatorEffectPacket.STREAM_CODEC, makeClientBoundHandler(ArenaIndicatorEffectPacket.Handler::handle));
		registrar.playToClient(AvatarSkiesRodUpdatePacket.ID, AvatarSkiesRodUpdatePacket.STREAM_CODEC, makeClientBoundHandler(AvatarSkiesRodUpdatePacket.Handler::handle));
		registrar.playToClient(AvatarSkiesRodEffectPacket.ID, AvatarSkiesRodEffectPacket.STREAM_CODEC, makeClientBoundHandler(AvatarSkiesRodEffectPacket.Handler::handle));
		registrar.playToClient(BlackLotusDissolveEffectPacket.ID, BlackLotusDissolveEffectPacket.STREAM_CODEC, makeClientBoundHandler(BlackLotusDissolveEffectPacket.Handler::handle));
		registrar.playToClient(DivaCharmEffectPacket.ID, DivaCharmEffectPacket.STREAM_CODEC, makeClientBoundHandler(DivaCharmEffectPacket.Handler::handle));
		registrar.playToClient(EnchanterDestroyEffectPacket.ID, EnchanterDestroyEffectPacket.STREAM_CODEC, makeClientBoundHandler(EnchanterDestroyEffectPacket.Handler::handle));
		registrar.playToClient(FluegelEyeEffectPacket.ID, FluegelEyeEffectPacket.STREAM_CODEC, makeClientBoundHandler(FluegelEyeEffectPacket.Handler::handle));
		registrar.playToClient(GogWorldPacket.ID, GogWorldPacket.STREAM_CODEC, makeClientBoundHandler(GogWorldPacket.Handler::handle));
		registrar.playToClient(GrassSeedsEffectPacket.ID, GrassSeedsEffectPacket.STREAM_CODEC, makeClientBoundHandler(GrassSeedsEffectPacket.Handler::handle));
		registrar.playToClient(HaloCraftEffectPacket.ID, HaloCraftEffectPacket.STREAM_CODEC, makeClientBoundHandler(HaloCraftEffectPacket.Handler::handle));
		registrar.playToClient(ItemLifeTimePacket.ID, ItemLifeTimePacket.STREAM_CODEC, makeClientBoundHandler(ItemLifeTimePacket.Handler::handle));
		registrar.playToClient(ItemSmokeEffectPacket.ID, ItemSmokeEffectPacket.STREAM_CODEC, makeClientBoundHandler(ItemSmokeEffectPacket.Handler::handle));
		registrar.playToClient(PaintLensEffectPacket.ID, PaintLensEffectPacket.STREAM_CODEC, makeClientBoundHandler(PaintLensEffectPacket.Handler::handle));
		registrar.playToClient(ParticleBeamEffectPacket.ID, ParticleBeamEffectPacket.STREAM_CODEC, makeClientBoundHandler(ParticleBeamEffectPacket.Handler::handle));
		registrar.playToClient(SparkManaFlowEffectPacket.ID, SparkManaFlowEffectPacket.STREAM_CODEC, makeClientBoundHandler(SparkManaFlowEffectPacket.Handler::handle));
		registrar.playToClient(SparkNetIndicatorEffectPacket.ID, SparkNetIndicatorEffectPacket.STREAM_CODEC, makeClientBoundHandler(SparkNetIndicatorEffectPacket.Handler::handle));
		registrar.playToClient(ThundercallerEffectPacket.ID, ThundercallerEffectPacket.STREAM_CODEC, makeClientBoundHandler(ThundercallerEffectPacket.Handler::handle));
		registrar.playToClient(UpdateItemsRemainingPacket.ID, UpdateItemsRemainingPacket.STREAM_CODEC, makeClientBoundHandler(UpdateItemsRemainingPacket.Handler::handle));
	}

	private static <T extends CustomPacketPayload> IPayloadHandler<T> makeServerBoundHandler(BiConsumer<T, ServerPlayer> handler) {
		return (m, ctx) -> handler.accept(m, (ServerPlayer) ctx.player());
	}

	private static <T extends CustomPacketPayload> IPayloadHandler<T> makeClientBoundHandler(BiConsumer<T, Player> consumer) {
		return (m, ctx) -> consumer.accept(m, ctx.player());
	}
}
