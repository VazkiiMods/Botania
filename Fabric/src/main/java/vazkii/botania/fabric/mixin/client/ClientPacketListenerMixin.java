package vazkii.botania.fabric.mixin.client;

import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientPacketListener;
import net.minecraft.network.protocol.game.ClientboundUpdateRecipesPacket;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Slice;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import vazkii.botania.common.block.block_entity.corporea.CorporeaIndexBlockEntity;
import vazkii.botania.common.handler.BotaniaRecipeIngredientsCache;

@Mixin(ClientPacketListener.class)
public class ClientPacketListenerMixin {
	@Inject(at = @At("HEAD"), method = "sendChat", cancellable = true)
	private void onChat(String message, CallbackInfo ci) {
		var player = Minecraft.getInstance().player;
		if (CorporeaIndexBlockEntity.ClientHandler.onChat(player, message)) {
			ci.cancel();
		}
	}

	@Inject(
		method = "handleUpdateRecipes",
		at = @At(value = "RETURN"),
		slice = @Slice(
			from = @At(
				value = "INVOKE",
				target = "Lnet/minecraft/client/multiplayer/SessionSearchTrees;updateRecipes(Lnet/minecraft/client/ClientRecipeBook;Lnet/minecraft/core/RegistryAccess$Frozen;)V"
			)
		)
	)
	private void onRecipesUpdated(ClientboundUpdateRecipesPacket packet, CallbackInfo ci) {
		BotaniaRecipeIngredientsCache.clearClientCache();
	}
}
