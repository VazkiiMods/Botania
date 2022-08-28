package vazkii.botania.fabric.mixin.client;

import net.minecraft.client.player.LocalPlayer;
import net.minecraft.network.chat.Component;

import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import vazkii.botania.common.block.tile.corporea.TileCorporeaIndex;

@Mixin(value = LocalPlayer.class, priority = 950)
public class FabricMixinLocalPlayer {
	@Inject(at = @At("HEAD"), method = "chatSigned", cancellable = true)
	public void onChat(String message, @Nullable Component component, CallbackInfo ci) {
		if (TileCorporeaIndex.ClientHandler.onChat((LocalPlayer) (Object) this, message)) {
			ci.cancel();
		}
	}
}
