/*
 * This class is distributed as part of the Botania Mod.
 * Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 *
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 */
package vazkii.botania.client.model;

import com.google.common.collect.ImmutableList;

import net.minecraft.block.BlockState;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.RenderLayers;
import net.minecraft.client.render.model.BakedModel;
import net.minecraft.client.render.model.BakedQuad;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;

import vazkii.botania.common.block.BlockPlatform;
import vazkii.botania.common.block.ModBlocks;
import vazkii.botania.common.block.tile.TilePlatform;

import javax.annotation.Nonnull;

import java.util.List;
import java.util.Random;

public class PlatformModel extends DelegatedModel {
	public PlatformModel(BakedModel original) {
		super(original);
	}

	@Nonnull
	@Override
	public List<BakedQuad> getQuads(BlockState state, Direction side, @Nonnull Random rand, @Nonnull IModelData data) {
		if (state == null) {
			return ImmutableList.of();
		}

		if (!(state.getBlock() instanceof BlockPlatform)) {
			return MinecraftClient.getInstance().getBlockRenderManager().getModels().getModelManager().getMissingModel().getQuads(state, side, rand);
		}

		RenderLayer layer = MinecraftForgeClient.getRenderLayer();
		if (layer == null) {
			layer = RenderLayer.getSolid(); // workaround for when this isn't set (digging, etc.)
		}

		BlockState heldState = data.getData(TilePlatform.HELD_STATE);
		BlockPos heldPos = data.getData(TilePlatform.HELD_POS);

		if (heldPos == null) {
			return ImmutableList.of();
		}

		MinecraftClient mc = MinecraftClient.getInstance();
		if (heldState == null && layer == RenderLayer.getSolid()) {
			// No camo
			return originalModel.getQuads(state, side, rand, data);
		} else if (heldState != null) {

			// Some people used this to get an invisible block in the past, accommodate that.
			if (heldState.getBlock() == ModBlocks.manaGlass) {
				return ImmutableList.of();
			}

			if (RenderLayers.canRenderInLayer(heldState, layer)) {
				// Steal camo's model
				BakedModel model = mc.getBlockRenderManager().getModels().getModel(heldState);

				return model.getQuads(heldState, side, rand, EmptyModelData.INSTANCE);
			}
		}

		return ImmutableList.of(); // Nothing renders
	}

}
