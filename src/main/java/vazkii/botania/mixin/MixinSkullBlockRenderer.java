/*
 * This class is distributed as part of the Botania Mod.
 * Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 *
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 */
package vazkii.botania.mixin;

import com.google.common.collect.ImmutableMap;
import com.mojang.blaze3d.vertex.PoseStack;

import net.minecraft.client.model.SkullModelBase;
import net.minecraft.client.model.geom.EntityModelSet;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.blockentity.SkullBlockRenderer;
import net.minecraft.client.resources.DefaultPlayerSkin;
import net.minecraft.core.Direction;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.SkullBlock;

import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;

import vazkii.botania.client.model.ModelGaiaHead;
import vazkii.botania.common.block.BlockGaiaHead;

import java.util.Map;

@Mixin(SkullBlockRenderer.class)
public abstract class MixinSkullBlockRenderer {
	@Shadow
	@Final
	private Map<SkullBlock.Type, SkullModelBase> modelByType;

	@Shadow
	@Final
	private static Map<SkullBlock.Type, ResourceLocation> SKIN_BY_TYPE;

	@Inject(
		method = "createSkullRenderers",
		at = @At(value = "INVOKE", target = "Lcom/google/common/collect/ImmutableMap$Builder;build()Lcom/google/common/collect/ImmutableMap;"),
		locals = LocalCapture.CAPTURE_FAILSOFT
	)
	private static void registerModel(EntityModelSet entityModelSet, CallbackInfoReturnable<Map<SkullBlock.Type, SkullModelBase>> cir,
			ImmutableMap.Builder<SkullBlock.Type, SkullModelBase> builder) {
		builder.put(BlockGaiaHead.GAIA_TYPE, new ModelGaiaHead());

		// todo 1.17 placeholder to avoid crash
		if (!SKIN_BY_TYPE.containsKey(BlockGaiaHead.GAIA_TYPE)) {
			SKIN_BY_TYPE.put(BlockGaiaHead.GAIA_TYPE, DefaultPlayerSkin.getDefaultSkin());
		}
	}

	/**
	 * This hook is necessary instead of just overriding render() in RenderTileGaiaHead as normal
	 * because vanilla ItemStackTileEntityRenderer checks for skull blocks and calls this static method directly.
	 */
	@Inject(at = @At("HEAD"), method = "renderSkull", cancellable = true)
	private static void onRender(Direction direction, float angle, float animationProgress, PoseStack ms,
			MultiBufferSource buffers, int light, SkullModelBase skullModelBase,
			RenderType renderType, CallbackInfo ci) {
		if (skullModelBase instanceof ModelGaiaHead) {
			// todo 1.17 RenderTileGaiaHead.gaiaRender(direction, angle, animationProgress, ms, buffers, light, this.modelByType);
			ci.cancel();
		}
	}
}
