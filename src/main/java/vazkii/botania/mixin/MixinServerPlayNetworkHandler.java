/*
 * This class is distributed as part of the Botania Mod.
 * Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 *
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 */
package vazkii.botania.mixin;

import net.minecraft.network.packet.c2s.play.ChatMessageC2SPacket;
import net.minecraft.server.network.ServerPlayNetworkHandler;
import net.minecraft.server.network.ServerPlayerEntity;

import org.apache.commons.lang3.StringUtils;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import vazkii.botania.common.block.tile.corporea.TileCorporeaIndex;

import java.util.function.Supplier;

@Mixin(ServerPlayNetworkHandler.class)
public class MixinServerPlayNetworkHandler {
	@Shadow
	public ServerPlayerEntity player;

	@Inject(
		at = @At(value = "INVOKE", target = "Lnet/minecraft/server/PlayerManager;broadcastChatMessage(Lnet/minecraft/text/Text;Lnet/minecraft/network/MessageType;Ljava/util/UUID;)V"),
		method = "onGameMessage", cancellable = true
	)
	private void handleCorporeaRequest(ChatMessageC2SPacket packet, CallbackInfo ci) {
		// lazy-renormalize the message text instead of capturing the already-normalized local
		Supplier<String> lazyMsg = () -> StringUtils.normalizeSpace(packet.getChatMessage());
		if (TileCorporeaIndex.getInputHandler().onChatMessage(player, lazyMsg)) {
			ci.cancel();
		}
	}
}
