/*
 * This class is distributed as part of the Botania Mod.
 * Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 *
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 */
package vazkii.botania.mixin;

import net.minecraft.network.Connection;
import net.minecraft.network.protocol.game.ClientboundUpdateRecipesPacket;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.server.players.PlayerList;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;

import vazkii.botania.common.network.PacketOrechidData;

import java.util.Iterator;

@Mixin(PlayerList.class)
public class MixinPlayerList {
	// Required around this spot as going later will prevent working with JEI on first login.
	// The alternative is Forge's login packets (please no)
	@Inject(
		method = "placeNewPlayer",
		at = @At(value = "NEW", target = "net/minecraft/network/protocol/game/ClientboundUpdateRecipesPacket")
	)
	private void sendOrechidData(Connection netManager, ServerPlayer playerIn, CallbackInfo ci) {
		PacketOrechidData.sendNonLocal(playerIn);
	}

	// Resync after data reload.
	@Inject(
		method = "reloadResources", locals = LocalCapture.CAPTURE_FAILHARD,
		at = @At(
			value = "INVOKE", target = "Lnet/minecraft/stats/ServerRecipeBook;sendInitialRecipeBook(Lnet/minecraft/server/level/ServerPlayer;)V",
			shift = At.Shift.AFTER
		)
	)
	private void resendOrechidData(CallbackInfo ci, ClientboundUpdateRecipesPacket packet,
			Iterator<ServerPlayer> iter, ServerPlayer player) {
		PacketOrechidData.sendNonLocal(player);
	}
}
