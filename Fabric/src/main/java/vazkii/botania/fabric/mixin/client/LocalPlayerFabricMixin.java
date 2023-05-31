package vazkii.botania.fabric.mixin.client;

import net.minecraft.client.player.LocalPlayer;

import org.spongepowered.asm.mixin.Mixin;

@Mixin(value = LocalPlayer.class, priority = 950)
public class LocalPlayerFabricMixin {
	// TODO 1.19.3: still necessary? moved somewhere else maybe?
	/*
	@Inject(at = @At("HEAD"), method = "chatSigned", cancellable = true)
	public void onChat(String message, @Nullable Component component, CallbackInfo ci) {
		if (CorporeaIndexBlockEntity.ClientHandler.onChat((LocalPlayer) (Object) this, message)) {
			ci.cancel();
		}
	}
	*/
}
