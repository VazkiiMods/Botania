/*
 * This class is distributed as part of the Botania Mod.
 * Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 *
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 */
package vazkii.botania.client.render.tile;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.vertex.IVertexBuilder;

import net.minecraft.block.BlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.Quaternion;
import net.minecraft.client.renderer.RenderTypeLookup;
import net.minecraft.client.renderer.Vector3f;
import net.minecraft.client.renderer.model.IBakedModel;
import net.minecraft.client.renderer.model.ItemCameraTransforms;
import net.minecraft.client.renderer.tileentity.TileEntityRenderer;
import net.minecraft.client.renderer.tileentity.TileEntityRendererDispatcher;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.MathHelper;

import vazkii.botania.api.ColorHelper;
import vazkii.botania.client.core.handler.ClientTickHandler;
import vazkii.botania.client.core.handler.MiscellaneousIcons;
import vazkii.botania.common.block.mana.BlockSpreader;
import vazkii.botania.common.block.tile.mana.TileSpreader;

import javax.annotation.Nonnull;

import java.util.Random;

public class RenderTileSpreader extends TileEntityRenderer<TileSpreader> {

	public RenderTileSpreader(TileEntityRendererDispatcher manager) {
		super(manager);
	}

	@Override
	public void render(@Nonnull TileSpreader spreader, float partialTicks, MatrixStack ms, IRenderTypeBuffer buffers, int light, int overlay) {
		ms.push();

		ms.translate(0.5F, 0.5, 0.5F);

		Quaternion transform = Vector3f.POSITIVE_Y.getDegreesQuaternion(spreader.rotationX + 90F);
		transform.multiply(Vector3f.POSITIVE_X.getDegreesQuaternion(spreader.rotationY));
		ms.multiply(transform);

		ms.translate(-0.5F, -0.5F, -0.5F);

		double time = ClientTickHandler.ticksInGame + partialTicks;

		float r = 1, g = 1, b = 1;
		if (spreader.getVariant() == BlockSpreader.Variant.GAIA) {
			int color = MathHelper.hsvToRGB((float) ((time * 5 + new Random(spreader.getPos().hashCode()).nextInt(10000)) % 360) / 360F, 0.4F, 0.9F);
			r = (color >> 16 & 0xFF) / 255F;
			g = (color >> 8 & 0xFF) / 255F;
			b = (color & 0xFF) / 255F;
		}

		IVertexBuilder buffer = buffers.getBuffer(RenderTypeLookup.getEntityBlockLayer(spreader.getBlockState()));
		IBakedModel bakedModel = Minecraft.getInstance().getBlockRendererDispatcher().getModelForState(spreader.getBlockState());
		Minecraft.getInstance().getBlockRendererDispatcher().getBlockModelRenderer()
				.render(ms.peek(), buffer, spreader.getBlockState(),
						bakedModel, r, g, b, light, overlay);

		ms.push();
		ms.translate(0.5, 0.5, 0.5);
		ms.multiply(Vector3f.POSITIVE_Y.getDegreesQuaternion((float) time % 360));
		ms.translate(-0.5, -0.5, -0.5);
		ms.translate(0F, (float) Math.sin(time / 20.0) * 0.05F, 0F);
		IBakedModel cube = getInsideModel(spreader);
		Minecraft.getInstance().getBlockRendererDispatcher().getBlockModelRenderer()
				.render(ms.peek(), buffer, spreader.getBlockState(),
						cube, 1, 1, 1, light, overlay);
		ms.pop();

		ms.translate(0.5, 1.5, 0.5);
		ItemStack stack = spreader.getItemHandler().getStackInSlot(0);

		if (!stack.isEmpty()) {
			ms.push();
			ms.translate(0.0F, -1F, -0.4675F);
			ms.multiply(Vector3f.POSITIVE_Z.getDegreesQuaternion(180));
			ms.multiply(Vector3f.POSITIVE_X.getDegreesQuaternion(180));
			ms.scale(1.0F, 1.0F, 1.0F);
			Minecraft.getInstance().getItemRenderer().renderItem(stack, ItemCameraTransforms.TransformType.NONE, light, overlay, ms, buffers);
			ms.pop();
		}

		if (spreader.paddingColor != null) {
			BlockState carpet = ColorHelper.CARPET_MAP.get(spreader.paddingColor).get().getDefaultState();
			IBakedModel model = Minecraft.getInstance().getBlockRendererDispatcher().getModelForState(carpet);
			buffer = buffers.getBuffer(RenderTypeLookup.getEntityBlockLayer(carpet));

			float f = 1 / 16F;

			// back
			ms.push();
			ms.multiply(Vector3f.POSITIVE_X.getDegreesQuaternion(90));
			ms.translate(-0.5F, 0.5F - f, 0.5F);
			Minecraft.getInstance().getBlockRendererDispatcher().getBlockModelRenderer().render(ms.peek(), buffer, carpet, model, 1, 1, 1, light, overlay);
			ms.pop();

			// left
			ms.push();
			ms.multiply(Vector3f.POSITIVE_Z.getDegreesQuaternion(-90));
			ms.translate(0.5F, 0.5F, -0.5F - f);
			Minecraft.getInstance().getBlockRendererDispatcher().getBlockModelRenderer().render(ms.peek(), buffer, carpet, model, 1, 1, 1, light, overlay);
			ms.pop();

			// right
			ms.push();
			ms.multiply(Vector3f.POSITIVE_Z.getDegreesQuaternion(90));
			ms.translate(-1.5F, 0.5F, -0.5F - f);
			Minecraft.getInstance().getBlockRendererDispatcher().getBlockModelRenderer().render(ms.peek(), buffer, carpet, model, 1, 1, 1, light, overlay);
			ms.pop();

			// top
			ms.push();
			ms.translate(-0.5F, -0.5F, -0.5F - f);
			Minecraft.getInstance().getBlockRendererDispatcher().getBlockModelRenderer().render(ms.peek(), buffer, carpet, model, 1, 1, 1, light, overlay);
			ms.pop();

			// bottom
			ms.push();
			ms.multiply(Vector3f.POSITIVE_Y.getDegreesQuaternion(180));
			ms.translate(-0.5F, -1.5F - f, -0.5F + f);
			Minecraft.getInstance().getBlockRendererDispatcher().getBlockModelRenderer().render(ms.peek(), buffer, carpet, model, 1, 1, 1, light, overlay);
			ms.pop();
		}

		ms.pop();
	}

	private IBakedModel getInsideModel(TileSpreader tile) {
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
