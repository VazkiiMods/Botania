/*
 * This class is distributed as part of the Botania Mod.
 * Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 *
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 */
package vazkii.botania.mixin;

import net.minecraft.network.ClientConnection;
import net.minecraft.network.packet.s2c.play.SynchronizeRecipesS2CPacket;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.PlayerManager;
import net.minecraft.server.network.ServerPlayerEntity;

import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyVariable;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;

import vazkii.botania.api.BotaniaAPI;
import vazkii.botania.common.item.ItemKeepIvy;
import vazkii.botania.common.item.equipment.bauble.ItemFlightTiara;
import vazkii.botania.common.item.equipment.bauble.ItemTravelBelt;
import vazkii.botania.common.network.PacketOrechidOreWeights;

import java.util.Iterator;

@Mixin(PlayerManager.class)
public class MixinPlayerManager {
	@Shadow
	@Final
	private MinecraftServer server;

	@Inject(at = @At("HEAD"), method = "remove")
	private void onLogout(ServerPlayerEntity player, CallbackInfo ci) {
		ItemTravelBelt.playerLoggedOut(player);
		ItemFlightTiara.playerLoggedOut(player);
	}

	@ModifyVariable(at = @At("RETURN"), method = "respawnPlayer", ordinal = 2)
	private ServerPlayerEntity onRespawn(ServerPlayerEntity newPlayer, ServerPlayerEntity oldPlayer, boolean fromDeath) {
		if (!fromDeath) {
			ItemKeepIvy.onPlayerRespawn(newPlayer);
		}
		return newPlayer;
	}

	/**
	 * Even though this is completely unnecessary in the context of Botania itself, some may want this data on the
	 * client. For example, practically anyone with REI installed.
	 */
	@Inject(method = "onDataPacksReloaded", at = @At(value = "INVOKE", target = "Lnet/minecraft/server/network/ServerRecipeBook;sendInitRecipesPacket(Lnet/minecraft/server/network/ServerPlayerEntity;)V", shift = At.Shift.AFTER), locals = LocalCapture.CAPTURE_FAILHARD)
	public void botania_dispatchOreWeights(CallbackInfo ctx, SynchronizeRecipesS2CPacket w, Iterator<ServerPlayerEntity> var5, ServerPlayerEntity player) {
		if (!this.server.isSinglePlayer()) {
			PacketOrechidOreWeights.send(player, BotaniaAPI.instance().getOreWeights());
			PacketOrechidOreWeights.send(player, BotaniaAPI.instance().getNetherOreWeights());
		}
	}

	/**
	 * In the most inconvenient fashion, datapacks don't reload when someone joins the server.
	 */
	@Inject(method = "onPlayerConnect", at = @At(value = "INVOKE", target = "Lnet/minecraft/server/PlayerManager;sendCommandTree(Lnet/minecraft/server/network/ServerPlayerEntity;)V", shift = At.Shift.BEFORE))
	public void botania_dispatchOreWeights(ClientConnection connection, ServerPlayerEntity player, CallbackInfo ctx) {
		if (!this.server.isSinglePlayer()) {
			PacketOrechidOreWeights.send(player, BotaniaAPI.instance().getOreWeights());
			PacketOrechidOreWeights.send(player, BotaniaAPI.instance().getNetherOreWeights());
		}
	}
}
