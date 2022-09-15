package vazkii.botania.fabric.mixin.client;

import net.minecraft.client.player.LocalPlayer;
import net.minecraft.network.chat.Component;

import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import vazkii.botania.common.block.block_entity.corporea.CorporeaIndexBlockEntity;

@Mixin(value = LocalPlayer.class, priority = 950)
public class LocalPlayerFabricMixin {
	@Inject(at = @At("HEAD"), method = "chatSigned", cancellable = true)
	public void onChat(String message, @Nullable Component component, CallbackInfo ci) {
		if (CorporeaIndexBlockEntity.ClientHandler.onChat((LocalPlayer) (Object) this, message)) {
			ci.cancel();
		}
	}
}
