/*
 * This class is distributed as part of the Botania Mod.
 * Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 *
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 */
package vazkii.botania.mixin;

import com.mojang.authlib.GameProfile;

import net.minecraft.block.SkullBlock;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.block.entity.SkullBlockEntityRenderer;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.math.Direction;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import vazkii.botania.client.render.tile.RenderTileGaiaHead;
import vazkii.botania.common.block.BlockGaiaHead;

import javax.annotation.Nullable;

@Mixin(SkullBlockEntityRenderer.class)
public abstract class MixinSkullTileEntityRenderer {
	/**
	 * This hook is necessary instead of just overriding render() in RenderTileGaiaHead as normal
	 * because vanilla ItemStackTileEntityRenderer checks for skull blocks and calls this static method directly.
	 */
	@Inject(at = @At("HEAD"), method = "render(Lnet/minecraft/util/math/Direction;FLnet/minecraft/block/SkullBlock$SkullType;Lcom/mojang/authlib/GameProfile;FLnet/minecraft/client/util/math/MatrixStack;Lnet/minecraft/client/render/VertexConsumerProvider;I)V", cancellable = true)
	private static void onRender(@Nullable Direction direction, float angle, SkullBlock.SkullType skullType, @Nullable GameProfile gameProfileIn, float animationProgress, MatrixStack ms, VertexConsumerProvider buffers, int light, CallbackInfo ci) {
		if (skullType == BlockGaiaHead.GAIA_TYPE) {
			RenderTileGaiaHead.gaiaRender(direction, angle, animationProgress, ms, buffers, light);
			ci.cancel();
		}
	}
}
