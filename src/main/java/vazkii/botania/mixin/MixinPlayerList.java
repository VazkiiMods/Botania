/*
 * This class is distributed as part of the Botania Mod.
 * Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 *
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 */
package vazkii.botania.mixin;

import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.play.server.SUpdateRecipesPacket;
import net.minecraft.server.management.PlayerList;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;

import vazkii.botania.common.network.PacketHandler;
import vazkii.botania.common.network.PacketOrechidData;

import java.util.Iterator;

@Mixin(PlayerList.class)
public class MixinPlayerList {
	// Required around this spot as going later will prevent working with JEI on first login.
	// The alternative is Forge's login packets (please no)
	@Inject(
		method = "initializeConnectionToPlayer",
		at = @At(value = "NEW", target = "net/minecraft/network/play/server/SUpdateRecipesPacket")
	)
	private void sendOrechidData(NetworkManager netManager, ServerPlayerEntity playerIn, CallbackInfo ci) {
		PacketHandler.sendNonLocal(playerIn, PacketOrechidData.create());
	}

	// Resync after data reload.
	@Inject(
		method = "reloadResources", locals = LocalCapture.CAPTURE_FAILHARD,
		at = @At(
			value = "INVOKE", target = "Lnet/minecraft/item/crafting/ServerRecipeBook;init(Lnet/minecraft/entity/player/ServerPlayerEntity;)V",
			shift = At.Shift.AFTER
		)
	)
	private void resendOrechidData(CallbackInfo ci, SUpdateRecipesPacket packet,
			Iterator<ServerPlayerEntity> iter, ServerPlayerEntity player) {
		PacketHandler.sendNonLocal(player, PacketOrechidData.create());
	}
}
