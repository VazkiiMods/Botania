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
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.RenderTypeLookup;
import net.minecraft.client.renderer.model.BakedQuad;
import net.minecraft.client.renderer.model.IBakedModel;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.client.MinecraftForgeClient;
import net.minecraftforge.client.model.BakedModelWrapper;
import net.minecraftforge.client.model.data.EmptyModelData;
import net.minecraftforge.client.model.data.IModelData;

import vazkii.botania.common.block.BlockPlatform;
import vazkii.botania.common.block.ModBlocks;
import vazkii.botania.common.block.tile.TilePlatform;

import javax.annotation.Nonnull;

import java.util.List;
import java.util.Random;

public class PlatformModel extends DelegatedModel {
	public PlatformModel(IBakedModel original) {
		super(original);
	}

	@Nonnull
	@Override
	public List<BakedQuad> getQuads(BlockState state, Direction side, @Nonnull Random rand, @Nonnull IModelData data) {
		if (state == null) {
			return ImmutableList.of();
		}

		if (!(state.getBlock() instanceof BlockPlatform)) {
			return Minecraft.getInstance().getBlockRendererDispatcher().getBlockModelShapes().getModelManager().getMissingModel().getQuads(state, side, rand);
		}

		RenderType layer = MinecraftForgeClient.getRenderLayer();
		if (layer == null) {
			layer = RenderType.getSolid(); // workaround for when this isn't set (digging, etc.)
		}

		BlockState heldState = data.getData(TilePlatform.HELD_STATE);
		BlockPos heldPos = data.getData(TilePlatform.HELD_POS);

		if (heldPos == null) {
			return ImmutableList.of();
		}

		Minecraft mc = Minecraft.getInstance();
		if (heldState == null && layer == RenderType.getSolid()) {
			// No camo
			return originalModel.getQuads(state, side, rand, data);
		} else if (heldState != null) {

			// Some people used this to get an invisible block in the past, accommodate that.
			if (heldState.getBlock() == ModBlocks.manaGlass) {
				return ImmutableList.of();
			}

			if (RenderTypeLookup.canRenderInLayer(heldState, layer)) {
				// Steal camo's model
				IBakedModel model = mc.getBlockRendererDispatcher().getBlockModelShapes().getModel(heldState);

				return model.getQuads(heldState, side, rand, EmptyModelData.INSTANCE);
			}
		}

		return ImmutableList.of(); // Nothing renders
	}

}
