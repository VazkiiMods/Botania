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
import net.minecraft.server.PlayerManager;
import net.minecraft.server.network.ServerPlayerEntity;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;

import vazkii.botania.common.network.PacketOrechidData;

import java.util.Iterator;

@Mixin(PlayerManager.class)
public class MixinPlayerList {
	// Required around this spot as going later will prevent working with JEI on first login.
	// The alternative is Forge's login packets (please no)
	@Inject(
		method = "onPlayerConnect",
		at = @At(value = "NEW", target = "net/minecraft/network/packet/s2c/play/SynchronizeRecipesS2CPacket")
	)
	private void sendOrechidData(ClientConnection netManager, ServerPlayerEntity playerIn, CallbackInfo ci) {
		PacketOrechidData.sendNonLocal(playerIn);
	}

	// Resync after data reload.
	@Inject(
		method = "onDataPacksReloaded", locals = LocalCapture.CAPTURE_FAILHARD,
		at = @At(
			value = "INVOKE", target = "Lnet/minecraft/server/network/ServerRecipeBook;sendInitRecipesPacket(Lnet/minecraft/server/network/ServerPlayerEntity;)V",
			shift = At.Shift.AFTER
		)
	)
	private void resendOrechidData(CallbackInfo ci, SynchronizeRecipesS2CPacket packet,
			Iterator<ServerPlayerEntity> iter, ServerPlayerEntity player) {
		PacketOrechidData.sendNonLocal(player);
	}
}
