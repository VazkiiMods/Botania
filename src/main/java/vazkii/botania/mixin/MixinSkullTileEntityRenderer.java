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
import com.mojang.blaze3d.matrix.MatrixStack;

import net.minecraft.block.SkullBlock;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.tileentity.SkullTileEntityRenderer;
import net.minecraft.util.Direction;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import vazkii.botania.client.render.tile.RenderTileGaiaHead;
import vazkii.botania.common.block.BlockGaiaHead;

import javax.annotation.Nullable;

/**
 * This hook is necessary instead of just overriding render() in RenderTileGaiaHead as normal
 * because vanilla ItemStackTileEntityRenderer checks for skull blocks and calls this static method directly.
 */
@Mixin(SkullTileEntityRenderer.class)
public abstract class MixinSkullTileEntityRenderer {
	@Inject(at = @At("HEAD"), method = "render(Lnet/minecraft/util/Direction;FLnet/minecraft/block/SkullBlock$ISkullType;Lcom/mojang/authlib/GameProfile;FLcom/mojang/blaze3d/matrix/MatrixStack;Lnet/minecraft/client/renderer/IRenderTypeBuffer;I)V", cancellable = true)
	private static void onRender(@Nullable Direction direction, float angle, SkullBlock.ISkullType skullType, @Nullable GameProfile gameProfileIn, float animationProgress, MatrixStack ms, IRenderTypeBuffer buffers, int light, CallbackInfo ci) {
		if (skullType == BlockGaiaHead.GAIA_TYPE) {
			RenderTileGaiaHead.gaiaRender(direction, angle, animationProgress, ms, buffers, light);
			ci.cancel();
		}
	}
}
