/*
 * This class is distributed as part of the Botania Mod.
 * Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 *
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 */

package org.violetmoon.botania.neoforge.network;

import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Player;
import net.neoforged.neoforge.network.event.RegisterPayloadHandlersEvent;
import net.neoforged.neoforge.network.handling.IPayloadHandler;
import net.neoforged.neoforge.network.registration.PayloadRegistrar;

import org.violetmoon.botania.network.clientbound.ArenaIndicatorEffectPacket;
import org.violetmoon.botania.network.clientbound.AvatarSkiesRodEffectPacket;
import org.violetmoon.botania.network.clientbound.AvatarSkiesRodUpdatePacket;
import org.violetmoon.botania.network.clientbound.BlackLotusDissolveEffectPacket;
import org.violetmoon.botania.network.clientbound.DivaCharmEffectPacket;
import org.violetmoon.botania.network.clientbound.EnchanterDestroyEffectPacket;
import org.violetmoon.botania.network.clientbound.FlowerTakeItemEffectPacket;
import org.violetmoon.botania.network.clientbound.FlugelEyeEffectPacket;
import org.violetmoon.botania.network.clientbound.GogWorldPacket;
import org.violetmoon.botania.network.clientbound.GrassSeedsEffectPacket;
import org.violetmoon.botania.network.clientbound.HaloCraftEffectPacket;
import org.violetmoon.botania.network.clientbound.ItemLifeTimePacket;
import org.violetmoon.botania.network.clientbound.ItemSmokeEffectPacket;
import org.violetmoon.botania.network.clientbound.PaintLensEffectPacket;
import org.violetmoon.botania.network.clientbound.ParticleBeamEffectPacket;
import org.violetmoon.botania.network.clientbound.RodOfThePlentifulMantleEffectPacket;
import org.violetmoon.botania.network.clientbound.RodOfTheUnstableReservoirEffectPacket;
import org.violetmoon.botania.network.clientbound.SparkManaFlowEffectPacket;
import org.violetmoon.botania.network.clientbound.SparkNetIndicatorEffectPacket;
import org.violetmoon.botania.network.clientbound.ThundercallerEffectPacket;
import org.violetmoon.botania.network.clientbound.UpdateItemsRemainingPacket;
import org.violetmoon.botania.network.serverbound.DodgePacket;
import org.violetmoon.botania.network.serverbound.IndexKeybindRequestPacket;
import org.violetmoon.botania.network.serverbound.IndexStringRequestPacket;
import org.violetmoon.botania.network.serverbound.JumpPacket;
import org.violetmoon.botania.network.serverbound.LeftClickPacket;

import java.util.function.BiConsumer;
import java.util.function.Supplier;

public class NeoForgePacketHandler {
	public static void registerPayloadHandlers(final RegisterPayloadHandlersEvent event) {
		PayloadRegistrar registrar = event.registrar("1");

		registrar.playToServer(DodgePacket.ID, DodgePacket.STREAM_CODEC, makeServerBoundHandler(DodgePacket::handle));
		registrar.playToServer(IndexKeybindRequestPacket.ID, IndexKeybindRequestPacket.STREAM_CODEC, makeServerBoundHandler(IndexKeybindRequestPacket::handle));
		registrar.playToServer(IndexStringRequestPacket.ID, IndexStringRequestPacket.STREAM_CODEC, makeServerBoundHandler(IndexStringRequestPacket::handle));
		registrar.playToServer(JumpPacket.ID, JumpPacket.STREAM_CODEC, makeServerBoundHandler(JumpPacket::handle));
		registrar.playToServer(LeftClickPacket.ID, LeftClickPacket.STREAM_CODEC, makeServerBoundHandler(LeftClickPacket::handle));

		registrar.playToClient(ArenaIndicatorEffectPacket.ID, ArenaIndicatorEffectPacket.STREAM_CODEC, makeClientBoundHandler(() -> ArenaIndicatorEffectPacket.Handler::handle));
		registrar.playToClient(AvatarSkiesRodUpdatePacket.ID, AvatarSkiesRodUpdatePacket.STREAM_CODEC, makeClientBoundHandler(() -> AvatarSkiesRodUpdatePacket.Handler::handle));
		registrar.playToClient(AvatarSkiesRodEffectPacket.ID, AvatarSkiesRodEffectPacket.STREAM_CODEC, makeClientBoundHandler(() -> AvatarSkiesRodEffectPacket.Handler::handle));
		registrar.playToClient(BlackLotusDissolveEffectPacket.ID, BlackLotusDissolveEffectPacket.STREAM_CODEC, makeClientBoundHandler(() -> BlackLotusDissolveEffectPacket.Handler::handle));
		registrar.playToClient(DivaCharmEffectPacket.ID, DivaCharmEffectPacket.STREAM_CODEC, makeClientBoundHandler(() -> DivaCharmEffectPacket.Handler::handle));
		registrar.playToClient(EnchanterDestroyEffectPacket.ID, EnchanterDestroyEffectPacket.STREAM_CODEC, makeClientBoundHandler(() -> EnchanterDestroyEffectPacket.Handler::handle));
		registrar.playToClient(FlowerTakeItemEffectPacket.ID, FlowerTakeItemEffectPacket.STREAM_CODEC, makeClientBoundHandler(() -> FlowerTakeItemEffectPacket.Handler::handle));
		registrar.playToClient(FlowerTakeItemEffectPacket.FIRE_ID, FlowerTakeItemEffectPacket.STREAM_CODEC_FIRE, makeClientBoundHandler(() -> FlowerTakeItemEffectPacket.Handler::handle));
		registrar.playToClient(FlugelEyeEffectPacket.ID, FlugelEyeEffectPacket.STREAM_CODEC, makeClientBoundHandler(() -> FlugelEyeEffectPacket.Handler::handle));
		registrar.playToClient(GogWorldPacket.ID, GogWorldPacket.STREAM_CODEC, makeClientBoundHandler(() -> GogWorldPacket.Handler::handle));
		registrar.playToClient(GrassSeedsEffectPacket.ID, GrassSeedsEffectPacket.STREAM_CODEC, makeClientBoundHandler(() -> GrassSeedsEffectPacket.Handler::handle));
		registrar.playToClient(HaloCraftEffectPacket.ID, HaloCraftEffectPacket.STREAM_CODEC, makeClientBoundHandler(() -> HaloCraftEffectPacket.Handler::handle));
		registrar.playToClient(ItemLifeTimePacket.ID, ItemLifeTimePacket.STREAM_CODEC, makeClientBoundHandler(() -> ItemLifeTimePacket.Handler::handle));
		registrar.playToClient(ItemSmokeEffectPacket.ID, ItemSmokeEffectPacket.STREAM_CODEC, makeClientBoundHandler(() -> ItemSmokeEffectPacket.Handler::handle));
		registrar.playToClient(PaintLensEffectPacket.ID, PaintLensEffectPacket.STREAM_CODEC, makeClientBoundHandler(() -> PaintLensEffectPacket.Handler::handle));
		registrar.playToClient(ParticleBeamEffectPacket.ID, ParticleBeamEffectPacket.STREAM_CODEC, makeClientBoundHandler(() -> ParticleBeamEffectPacket.Handler::handle));
		registrar.playToClient(RodOfThePlentifulMantleEffectPacket.ID, RodOfThePlentifulMantleEffectPacket.STREAM_CODEC, makeClientBoundHandler(() -> RodOfThePlentifulMantleEffectPacket.Handler::handle));
		registrar.playToClient(RodOfTheUnstableReservoirEffectPacket.ID, RodOfTheUnstableReservoirEffectPacket.STREAM_CODEC, makeClientBoundHandler(() -> RodOfTheUnstableReservoirEffectPacket.Handler::handle));
		registrar.playToClient(SparkManaFlowEffectPacket.ID, SparkManaFlowEffectPacket.STREAM_CODEC, makeClientBoundHandler(() -> SparkManaFlowEffectPacket.Handler::handle));
		registrar.playToClient(SparkNetIndicatorEffectPacket.ID, SparkNetIndicatorEffectPacket.STREAM_CODEC, makeClientBoundHandler(() -> SparkNetIndicatorEffectPacket.Handler::handle));
		registrar.playToClient(ThundercallerEffectPacket.ID, ThundercallerEffectPacket.STREAM_CODEC, makeClientBoundHandler(() -> ThundercallerEffectPacket.Handler::handle));
		registrar.playToClient(UpdateItemsRemainingPacket.ID, UpdateItemsRemainingPacket.STREAM_CODEC, makeClientBoundHandler(() -> UpdateItemsRemainingPacket.Handler::handle));
	}

	private static <T extends CustomPacketPayload> IPayloadHandler<T> makeServerBoundHandler(BiConsumer<T, ServerPlayer> handler) {
		return (m, ctx) -> handler.accept(m, (ServerPlayer) ctx.player());
	}

	private static <T extends CustomPacketPayload> IPayloadHandler<T> makeClientBoundHandler(Supplier<BiConsumer<T, Player>> consumerGetter) {
		return (m, ctx) -> consumerGetter.get().accept(m, ctx.player());
	}
}
