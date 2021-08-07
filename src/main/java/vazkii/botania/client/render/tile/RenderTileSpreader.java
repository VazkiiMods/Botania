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
import com.mojang.math.Quaternion;
import com.mojang.math.Vector3f;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.ItemBlockRenderTypes;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.block.model.ItemTransforms;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.client.resources.model.BakedModel;
import net.minecraft.util.Mth;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.state.BlockState;

import vazkii.botania.client.core.handler.ClientTickHandler;
import vazkii.botania.client.core.handler.MiscellaneousIcons;
import vazkii.botania.common.block.mana.BlockSpreader;
import vazkii.botania.common.block.tile.mana.TileSpreader;
import vazkii.botania.common.core.helper.ColorHelper;

import javax.annotation.Nonnull;

import java.util.Random;

public class RenderTileSpreader implements BlockEntityRenderer<TileSpreader> {

	public RenderTileSpreader(BlockEntityRendererProvider.Context ctx) {}

	@Override
	public void render(@Nonnull TileSpreader spreader, float partialTicks, PoseStack ms, MultiBufferSource buffers, int light, int overlay) {
		ms.pushPose();

		ms.translate(0.5F, 0.5, 0.5F);

		Quaternion transform = Vector3f.YP.rotationDegrees(spreader.rotationX + 90F);
		transform.mul(Vector3f.XP.rotationDegrees(spreader.rotationY));
		ms.mulPose(transform);

		ms.translate(-0.5F, -0.5F, -0.5F);

		double time = ClientTickHandler.ticksInGame + partialTicks;

		float r = 1, g = 1, b = 1;
		if (spreader.getVariant() == BlockSpreader.Variant.GAIA) {
			int color = Mth.hsvToRgb((float) ((time * 5 + new Random(spreader.getBlockPos().hashCode()).nextInt(10000)) % 360) / 360F, 0.4F, 0.9F);
			r = (color >> 16 & 0xFF) / 255F;
			g = (color >> 8 & 0xFF) / 255F;
			b = (color & 0xFF) / 255F;
		}

		VertexConsumer buffer = buffers.getBuffer(ItemBlockRenderTypes.getRenderType(spreader.getBlockState(), false));
		BakedModel bakedModel = Minecraft.getInstance().getBlockRenderer().getBlockModel(spreader.getBlockState());
		Minecraft.getInstance().getBlockRenderer().getModelRenderer()
				.renderModel(ms.last(), buffer, spreader.getBlockState(),
						bakedModel, r, g, b, light, overlay);

		ms.pushPose();
		ms.translate(0.5, 0.5, 0.5);
		ms.mulPose(Vector3f.YP.rotationDegrees((float) time % 360));
		ms.translate(-0.5, -0.5, -0.5);
		ms.translate(0F, (float) Math.sin(time / 20.0) * 0.05F, 0F);
		BakedModel cube = getInsideModel(spreader);
		Minecraft.getInstance().getBlockRenderer().getModelRenderer()
				.renderModel(ms.last(), buffer, spreader.getBlockState(),
						cube, 1, 1, 1, light, overlay);
		ms.popPose();

		ms.translate(0.5, 1.5, 0.5);
		ItemStack stack = spreader.getItemHandler().getItem(0);

		if (!stack.isEmpty()) {
			ms.pushPose();
			ms.translate(0.0F, -1F, -0.4675F);
			ms.mulPose(Vector3f.ZP.rotationDegrees(180));
			ms.mulPose(Vector3f.XP.rotationDegrees(180));
			ms.scale(1.0F, 1.0F, 1.0F);
			Minecraft.getInstance().getItemRenderer().renderStatic(stack, ItemTransforms.TransformType.NONE, light, overlay, ms, buffers);
			ms.popPose();
		}

		if (spreader.paddingColor != null) {
			BlockState carpet = ColorHelper.CARPET_MAP.apply(spreader.paddingColor).defaultBlockState();
			BakedModel model = Minecraft.getInstance().getBlockRenderer().getBlockModel(carpet);
			buffer = buffers.getBuffer(ItemBlockRenderTypes.getRenderType(carpet, false));

			float f = 1 / 16F;

			// back
			ms.pushPose();
			ms.mulPose(Vector3f.XP.rotationDegrees(90));
			ms.translate(-0.5F, 0.5F - f, 0.5F);
			Minecraft.getInstance().getBlockRenderer().getModelRenderer().renderModel(ms.last(), buffer, carpet, model, 1, 1, 1, light, overlay);
			ms.popPose();

			// left
			ms.pushPose();
			ms.mulPose(Vector3f.ZP.rotationDegrees(-90));
			ms.translate(0.5F, 0.5F, -0.5F - f);
			Minecraft.getInstance().getBlockRenderer().getModelRenderer().renderModel(ms.last(), buffer, carpet, model, 1, 1, 1, light, overlay);
			ms.popPose();

			// right
			ms.pushPose();
			ms.mulPose(Vector3f.ZP.rotationDegrees(90));
			ms.translate(-1.5F, 0.5F, -0.5F - f);
			Minecraft.getInstance().getBlockRenderer().getModelRenderer().renderModel(ms.last(), buffer, carpet, model, 1, 1, 1, light, overlay);
			ms.popPose();

			// top
			ms.pushPose();
			ms.translate(-0.5F, -0.5F, -0.5F - f);
			Minecraft.getInstance().getBlockRenderer().getModelRenderer().renderModel(ms.last(), buffer, carpet, model, 1, 1, 1, light, overlay);
			ms.popPose();

			// bottom
			ms.pushPose();
			ms.mulPose(Vector3f.YP.rotationDegrees(180));
			ms.translate(-0.5F, -1.5F - f, -0.5F + f);
			Minecraft.getInstance().getBlockRenderer().getModelRenderer().renderModel(ms.last(), buffer, carpet, model, 1, 1, 1, light, overlay);
			ms.popPose();
		}

		ms.popPose();
	}

	private BakedModel getInsideModel(TileSpreader tile) {
		switch (tile.getVariant()) {
		case GAIA:
			return MiscellaneousIcons.INSTANCE.gaiaSpreaderInside;
		case REDSTONE:
			return MiscellaneousIcons.INSTANCE.redstoneSpreaderInside;
		case ELVEN:
			return MiscellaneousIcons.INSTANCE.elvenSpreaderInside;
		default:
		case MANA:
			return MiscellaneousIcons.INSTANCE.manaSpreaderInside;
		}
	}
}
