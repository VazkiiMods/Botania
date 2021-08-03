/*
 * This class is distributed as part of the Botania Mod.
 * Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 *
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 */
package vazkii.botania.client.model;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Vector3f;

import net.minecraft.client.Minecraft;
import net.minecraft.client.model.Model;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.block.model.ItemTransforms;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;

import vazkii.botania.client.lib.LibResources;
import vazkii.botania.common.block.tile.TileBrewery;

import javax.annotation.Nullable;

public class ModelBrewery extends Model {
	private static final ResourceLocation TEXTURE = new ResourceLocation(LibResources.MODEL_BREWERY);
	final ModelPart top;
	final ModelPart pole;
	final ModelPart bottom;

	final ModelPart plate;

	public ModelBrewery() {
		super(RenderType::entitySolid);

		texWidth = 32;
		texHeight = 16;

		top = new ModelPart(this, 8, 0);
		top.setPos(0.0F, 16.0F, 0.0F);
		top.addBox(-2.0F, -7.0F, -2.0F, 4, 1, 4, 0.0F);
		pole = new ModelPart(this, 0, 0);
		pole.setPos(0.0F, 16.0F, 0.0F);
		pole.addBox(-1.0F, -6.0F, -1.0F, 2, 10, 2, 0.0F);
		bottom = new ModelPart(this, 8, 5);
		bottom.setPos(0.0F, 16.0F, 0.0F);
		bottom.addBox(-2.0F, 4.0F, -2.0F, 4, 1, 4, 0.0F);

		plate = new ModelPart(this, 8, 5);
		plate.setPos(0.0F, 17.0F, 0.0F);
		plate.addBox(5.0F, 0.0F, -2.0F, 4, 1, 4, 0.0F);
	}

	public void render(@Nullable TileBrewery brewery, double time, PoseStack ms, MultiBufferSource buffers, int light, int overlay) {
		float offset = (float) Math.sin(time / 40) * 0.1F + 0.05F;
		boolean hasTile = brewery != null;
		int plates = hasTile ? brewery.inventorySize() - 1 : 7;
		float deg = (float) time / 16F;
		float polerot = -deg * 25F;

		ms.translate(0F, offset, 0F);
		ms.mulPose(Vector3f.YP.rotationDegrees(polerot));
		if (hasTile && !brewery.getItemHandler().getItem(0).isEmpty()) {
			ms.pushPose();
			ms.mulPose(Vector3f.XP.rotationDegrees(180));
			ms.translate(0, -0.45F, 0);
			renderItemStack(brewery.getItemHandler().getItem(0), ms, buffers, light, overlay);
			ms.popPose();
		}

		RenderType layer = renderType(TEXTURE);
		pole.render(ms, buffers.getBuffer(layer), light, overlay);
		top.render(ms, buffers.getBuffer(layer), light, overlay);
		bottom.render(ms, buffers.getBuffer(layer), light, overlay);
		ms.mulPose(Vector3f.YP.rotationDegrees(-polerot));

		float degper = (float) (2F * Math.PI) / plates;
		for (int i = 0; i < plates; i++) {
			plate.yRot = deg;
			float offset1 = (float) Math.sin(time / 20 + i * 40F) * 0.2F - 0.2F;
			if (time == -1) {
				offset1 = 0F;
			}

			ms.translate(0F, offset1, 0F);
			if (hasTile && !brewery.getItemHandler().getItem(i + 1).isEmpty()) {
				float rot = plate.yRot * 180F / (float) Math.PI;
				float transX = 0.3125F;
				float transY = 1.06F;
				float transZ = 0.1245F;
				ms.pushPose();
				ms.mulPose(Vector3f.YP.rotationDegrees(rot));
				ms.translate(transX, transY, transZ);
				ms.mulPose(Vector3f.XP.rotationDegrees(-90F));
				ms.translate(0.125F, 0.125F, 0);
				renderItemStack(brewery.getItemHandler().getItem(i + 1), ms, buffers, light, overlay);
				ms.popPose();
			}
			plate.render(ms, buffers.getBuffer(layer), light, overlay);
			ms.translate(0F, -offset1, 0F);

			deg += degper;
		}
		ms.translate(0F, -offset, 0F);
	}

	private void renderItemStack(ItemStack stack, PoseStack ms, MultiBufferSource buffers, int light, int overlay) {
		if (!stack.isEmpty()) {
			Minecraft mc = Minecraft.getInstance();
			ms.pushPose();

			float s = 0.25F;
			ms.scale(s, s, s);
			mc.getItemRenderer().renderStatic(stack, ItemTransforms.TransformType.GROUND, light, overlay, ms, buffers);
			ms.popPose();
		}
	}

	@Override
	public void renderToBuffer(PoseStack ms, VertexConsumer buffer, int light, int overlay, float r, float g, float b, float a) {
		throw new UnsupportedOperationException("unimplemented, call using other render method");
	}
}
