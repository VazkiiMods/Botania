package vazkii.botania.fabric.mixin.client;

import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientPacketListener;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import vazkii.botania.common.block.block_entity.corporea.CorporeaIndexBlockEntity;

@Mixin(ClientPacketListener.class)
public class ClientPacketListenerMixin {
	@Inject(at = @At("HEAD"), method = "sendChat", cancellable = true)
	private void onChat(String message, CallbackInfo ci) {
		var player = Minecraft.getInstance().player;
		if (CorporeaIndexBlockEntity.ClientHandler.onChat(player, message)) {
			ci.cancel();
		}
	}
}
