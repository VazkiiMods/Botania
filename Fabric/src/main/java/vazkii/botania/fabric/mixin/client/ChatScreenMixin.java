package vazkii.botania.fabric.mixin.client;

import net.minecraft.client.gui.screens.ChatScreen;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import vazkii.botania.common.block.block_entity.corporea.CorporeaIndexBlockEntity;

@Mixin(value = ChatScreen.class)
public abstract class ChatScreenMixin extends Screen {
	private ChatScreenMixin(Component title) {
		super(title);
		throw new IllegalStateException();
	}

	@Inject(at = @At("HEAD"), method = "handleChatInput", cancellable = true)
	public void onChat(String input, boolean addToRecentChat, CallbackInfoReturnable<Boolean> cir) {
		if (CorporeaIndexBlockEntity.ClientHandler.onChat(this.minecraft.player, input)) {
			cir.setReturnValue(true);
		}
	}
}
