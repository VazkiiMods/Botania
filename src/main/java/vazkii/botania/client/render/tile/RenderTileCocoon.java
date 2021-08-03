/*
 * This class is distributed as part of the Botania Mod.
 * Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 *
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 */
package vazkii.botania.client.render.tile;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Vector3f;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.ItemBlockRenderTypes;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderDispatcher;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.resources.model.BakedModel;
import net.minecraft.world.level.block.state.BlockState;

import vazkii.botania.common.block.tile.TileCocoon;

import javax.annotation.Nonnull;

public class RenderTileCocoon extends BlockEntityRenderer<TileCocoon> {

	public RenderTileCocoon(BlockEntityRenderDispatcher manager) {
		super(manager);
	}

	@Override
	public void render(@Nonnull TileCocoon cocoon, float partialTicks, PoseStack ms, MultiBufferSource buffers, int light, int overlay) {
		float rot = 0F;
		float modval = 60F - (float) cocoon.timePassed / (float) TileCocoon.TOTAL_TIME * 30F;
		if (cocoon.timePassed % modval < 10) {
			float mod = (cocoon.timePassed + partialTicks) % modval;
			float v = mod / 5 * (float) Math.PI * 2;
			rot = (float) Math.sin(v) * (float) Math.log(cocoon.timePassed + partialTicks);
		}

		ms.pushPose();
		ms.translate(0.5, 0, 0);
		ms.mulPose(Vector3f.XP.rotationDegrees(rot));
		ms.translate(-0.5, 0, 0);
		BlockState state = cocoon.getBlockState();
		BakedModel model = Minecraft.getInstance().getBlockRenderer().getBlockModelShaper().getBlockModel(state);
		VertexConsumer buffer = buffers.getBuffer(ItemBlockRenderTypes.getChunkRenderType(state));
		Minecraft.getInstance().getBlockRenderer().getModelRenderer().renderModel(ms.last(), buffer, state, model, 1, 1, 1, light, overlay);
		ms.popPose();
	}
}
