/*
 * This class is distributed as part of the Botania Mod.
 * Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 *
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 */
package vazkii.botania.fabric.client;

import net.fabricmc.fabric.api.renderer.v1.model.FabricBakedModel;
import net.fabricmc.fabric.api.renderer.v1.model.ForwardingBakedModel;
import net.fabricmc.fabric.api.renderer.v1.render.RenderContext;
import net.fabricmc.fabric.api.rendering.data.v1.RenderAttachedBlockView;
import net.minecraft.client.Minecraft;
import net.minecraft.client.resources.model.BakedModel;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.BlockAndTintGetter;
import net.minecraft.world.level.block.state.BlockState;

import vazkii.botania.common.block.BlockPlatform;
import vazkii.botania.common.block.ModBlocks;
import vazkii.botania.common.block.tile.TilePlatform;

import java.util.Random;
import java.util.function.Supplier;

public class FabricPlatformModel extends ForwardingBakedModel {
	public FabricPlatformModel(BakedModel original) {
		this.wrapped = original;
	}

	@Override
	public boolean isVanillaAdapter() {
		return false;
	}

	@Override
	public void emitBlockQuads(BlockAndTintGetter blockView, BlockState state, BlockPos pos, Supplier<Random> randomSupplier, RenderContext context) {
		if (!(state.getBlock() instanceof BlockPlatform)) {
			context.fallbackConsumer().accept(Minecraft.getInstance().getBlockRenderer()
					.getBlockModelShaper().getModelManager().getMissingModel());
			return;
		}

		Object data = ((RenderAttachedBlockView) blockView).getBlockEntityRenderAttachment(pos);
		if (data instanceof TilePlatform.PlatformData) {
			BlockPos heldPos = ((TilePlatform.PlatformData) data).pos();
			BlockState heldState = ((TilePlatform.PlatformData) data).state();

			if (heldState == null) {
				// No camo
				super.emitBlockQuads(blockView, state, pos, randomSupplier, context);
			} else {
				// Some people used this to get an invisible block in the past, accommodate that.
				if (heldState.is(ModBlocks.manaGlass)) {
					return;
				}

				BakedModel model = Minecraft.getInstance().getBlockRenderer()
						.getBlockModelShaper().getBlockModel(heldState);
				if (model instanceof FabricBakedModel) {
					// Steal camo's model
					((FabricBakedModel) model).emitBlockQuads(blockView, heldState, heldPos, randomSupplier, context);
				}
			}
		}
	}

}
