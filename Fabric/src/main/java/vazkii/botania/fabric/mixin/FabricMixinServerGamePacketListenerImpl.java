/*
 * This class is distributed as part of the Botania Mod.
 * Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 *
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 */
package vazkii.botania.fabric.mixin;

import net.minecraft.server.level.ServerPlayer;
import net.minecraft.server.network.ServerGamePacketListenerImpl;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

@Mixin(value = ServerGamePacketListenerImpl.class, priority = 950)
public class FabricMixinServerGamePacketListenerImpl {
	@Shadow
	public ServerPlayer player;

	/* todo 1.19
	@Inject(
		at = @At(value = "INVOKE", target = "Lnet/minecraft/server/players/PlayerList;broadcastMessage(Lnet/minecraft/network/chat/Component;Ljava/util/function/Function;Lnet/minecraft/network/chat/ChatType;Ljava/util/UUID;)V"),
		method = "handleChat(Lnet/minecraft/server/network/TextFilter$FilteredText;)V", cancellable = true
	)
	private void handleCorporeaRequest(TextFilter.FilteredText msg, CallbackInfo ci) {
		if (TileCorporeaIndex.getInputHandler().onChatMessage(player, msg.getRaw())) {
			ci.cancel();
		}
	}
	*/
}
