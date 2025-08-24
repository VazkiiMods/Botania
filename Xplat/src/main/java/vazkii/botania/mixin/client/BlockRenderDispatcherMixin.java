package vazkii.botania.mixin.client;

import com.mojang.blaze3d.vertex.PoseStack;

import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.block.BlockRenderDispatcher;
import net.minecraft.world.level.block.state.BlockState;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import vazkii.botania.client.patchouli.PatchouliUtils;

/**
 * Hack for Patchouli visualizer rendering of floating flowers.
 * TODO: Check if new Patchouli versions can handle rendering non-vanilla block entities in multiblock visualizations.
 */
@Mixin(BlockRenderDispatcher.class)
public class BlockRenderDispatcherMixin {
	@Inject(method = "renderSingleBlock", at = @At("HEAD"))
	void enterRenderSingleBlock(BlockState state, PoseStack poseStack, MultiBufferSource buffer, int packedLight, int packedOverlay, CallbackInfo ci) {
		PatchouliUtils.setInVisualizer(true);
	}

	@Inject(method = "renderSingleBlock", at = @At("RETURN"))
	void exitRenderSingleBlock(BlockState state, PoseStack poseStack, MultiBufferSource buffer, int packedLight, int packedOverlay, CallbackInfo ci) {
		PatchouliUtils.setInVisualizer(false);
	}
}
