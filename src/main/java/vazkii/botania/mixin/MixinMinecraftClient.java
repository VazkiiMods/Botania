/*
 * This class is distributed as part of the Botania Mod.
 * Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 *
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 */
package vazkii.botania.mixin;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientPlayerEntity;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import vazkii.botania.client.core.handler.ClientTickHandler;
import vazkii.botania.common.item.equipment.tool.terrasteel.ItemTerraSword;

import javax.annotation.Nullable;

@Mixin(MinecraftClient.class)
public abstract class MixinMinecraftClient {
	@Shadow
	public abstract boolean isPaused();

	@Shadow
	private float pausedTickDelta;

	@Shadow
	public abstract float getTickDelta();

	@Shadow
	@Nullable
	public ClientPlayerEntity player;

	@Inject(method = "render", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/render/GameRenderer;render(FJZ)V"))
	private void onFrameStart(boolean tick, CallbackInfo ci) {
		ClientTickHandler.renderTick(isPaused() ? pausedTickDelta : getTickDelta());

	}

	@Inject(method = "render", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/render/GameRenderer;render(FJZ)V", shift = At.Shift.AFTER))
	private void onFrameEnd(boolean tick, CallbackInfo ci) {
		ClientTickHandler.calcDelta();
	}

	@Inject(method = "doAttack", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/network/ClientPlayerEntity;resetLastAttackedTicks()V"))
	private void leftClickEmpty(CallbackInfo ci) {
		ItemTerraSword.leftClick(player.getMainHandStack());
	}
}
