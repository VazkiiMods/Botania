/*
 * This class is distributed as part of the Botania Mod.
 * Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 *
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 */
package vazkii.botania.client.model;

import net.fabricmc.fabric.api.renderer.v1.model.FabricBakedModel;
import net.fabricmc.fabric.api.renderer.v1.model.ForwardingBakedModel;
import net.fabricmc.fabric.api.renderer.v1.render.RenderContext;
import net.fabricmc.fabric.api.rendering.data.v1.RenderAttachedBlockView;
import net.minecraft.block.BlockState;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.render.model.BakedModel;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.BlockRenderView;

import vazkii.botania.common.block.BlockPlatform;
import vazkii.botania.common.block.ModBlocks;
import vazkii.botania.common.block.tile.TilePlatform;

import java.util.Random;
import java.util.function.Supplier;

public class PlatformModel extends ForwardingBakedModel {
	public PlatformModel(BakedModel original) {
		this.wrapped = original;
	}

	@Override
	public boolean isVanillaAdapter() {
		return false;
	}

	@Override
	public void emitBlockQuads(BlockRenderView blockView, BlockState state, BlockPos pos, Supplier<Random> randomSupplier, RenderContext context) {
		if (!(state.getBlock() instanceof BlockPlatform)) {
			context.fallbackConsumer().accept(MinecraftClient.getInstance().getBlockRenderManager().getModels().getModelManager().getMissingModel());
			return;
		}

		Object data = ((RenderAttachedBlockView) blockView).getBlockEntityRenderAttachment(pos);
		if (data instanceof TilePlatform.PlatformData) {
			BlockPos heldPos = ((TilePlatform.PlatformData) data).pos;
			BlockState heldState = ((TilePlatform.PlatformData) data).state;

			if (heldState == null) {
				// No camo
				super.emitBlockQuads(blockView, state, pos, randomSupplier, context);
			} else {
				// Some people used this to get an invisible block in the past, accommodate that.
				if (heldState.getBlock() == ModBlocks.manaGlass) {
					return;
				}

				BakedModel model = MinecraftClient.getInstance().getBlockRenderManager().getModels().getModel(heldState);
				if (model instanceof FabricBakedModel) {
					// Steal camo's model
					((FabricBakedModel) model).emitBlockQuads(blockView, heldState, heldPos, randomSupplier, context);
				}
			}
		}
	}

}
