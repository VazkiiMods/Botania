/*
 * This class is distributed as part of the Botania Mod.
 * Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 *
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 */
package vazkii.botania.client.model;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.model.Model;
import net.minecraft.client.model.ModelPart;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.model.json.ModelTransformation;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.client.util.math.Vector3f;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Identifier;

import vazkii.botania.client.lib.LibResources;
import vazkii.botania.common.block.tile.TileBrewery;

import javax.annotation.Nullable;

public class ModelBrewery extends Model {
	private static final Identifier TEXTURE = new Identifier(LibResources.MODEL_BREWERY);
	final ModelPart top;
	final ModelPart pole;
	final ModelPart bottom;

	final ModelPart plate;

	public ModelBrewery() {
		super(RenderLayer::getEntitySolid);

		textureWidth = 32;
		textureHeight = 16;

		top = new ModelPart(this, 8, 0);
		top.setPivot(0.0F, 16.0F, 0.0F);
		top.addCuboid(-2.0F, -7.0F, -2.0F, 4, 1, 4, 0.0F);
		pole = new ModelPart(this, 0, 0);
		pole.setPivot(0.0F, 16.0F, 0.0F);
		pole.addCuboid(-1.0F, -6.0F, -1.0F, 2, 10, 2, 0.0F);
		bottom = new ModelPart(this, 8, 5);
		bottom.setPivot(0.0F, 16.0F, 0.0F);
		bottom.addCuboid(-2.0F, 4.0F, -2.0F, 4, 1, 4, 0.0F);

		plate = new ModelPart(this, 8, 5);
		plate.setPivot(0.0F, 17.0F, 0.0F);
		plate.addCuboid(5.0F, 0.0F, -2.0F, 4, 1, 4, 0.0F);
	}

	public void render(@Nullable TileBrewery brewery, double time, MatrixStack ms, VertexConsumerProvider buffers, int light, int overlay) {
		float offset = (float) Math.sin(time / 40) * 0.1F + 0.05F;
		boolean hasTile = brewery != null;
		int plates = hasTile ? brewery.inventorySize() - 1 : 7;
		float deg = (float) time / 16F;
		float polerot = -deg * 25F;

		ms.translate(0F, offset, 0F);
		ms.multiply(Vector3f.POSITIVE_Y.getDegreesQuaternion(polerot));
		if (hasTile && !brewery.getItemHandler().getStack(0).isEmpty()) {
			ms.push();
			ms.multiply(Vector3f.POSITIVE_X.getDegreesQuaternion(180));
			ms.translate(0, -0.45F, 0);
			renderItemStack(brewery.getItemHandler().getStack(0), ms, buffers, light, overlay);
			ms.pop();
		}

		RenderLayer layer = getLayer(TEXTURE);
		pole.render(ms, buffers.getBuffer(layer), light, overlay);
		top.render(ms, buffers.getBuffer(layer), light, overlay);
		bottom.render(ms, buffers.getBuffer(layer), light, overlay);
		ms.multiply(Vector3f.POSITIVE_Y.getDegreesQuaternion(-polerot));

		float degper = (float) (2F * Math.PI) / plates;
		for (int i = 0; i < plates; i++) {
			plate.yaw = deg;
			float offset1 = (float) Math.sin(time / 20 + i * 40F) * 0.2F - 0.2F;
			if (time == -1) {
				offset1 = 0F;
			}

			ms.translate(0F, offset1, 0F);
			if (hasTile && !brewery.getItemHandler().getStack(i + 1).isEmpty()) {
				float rot = plate.yaw * 180F / (float) Math.PI;
				float transX = 0.3125F;
				float transY = 1.06F;
				float transZ = 0.1245F;
				ms.push();
				ms.multiply(Vector3f.POSITIVE_Y.getDegreesQuaternion(rot));
				ms.translate(transX, transY, transZ);
				ms.multiply(Vector3f.POSITIVE_X.getDegreesQuaternion(-90F));
				ms.translate(0.125F, 0.125F, 0);
				renderItemStack(brewery.getItemHandler().getStack(i + 1), ms, buffers, light, overlay);
				ms.pop();
			}
			plate.render(ms, buffers.getBuffer(layer), light, overlay);
			ms.translate(0F, -offset1, 0F);

			deg += degper;
		}
		ms.translate(0F, -offset, 0F);
	}

	private void renderItemStack(ItemStack stack, MatrixStack ms, VertexConsumerProvider buffers, int light, int overlay) {
		if (!stack.isEmpty()) {
			MinecraftClient mc = MinecraftClient.getInstance();
			ms.push();

			float s = 0.25F;
			ms.scale(s, s, s);
			mc.getItemRenderer().renderItem(stack, ModelTransformation.Mode.GROUND, light, overlay, ms, buffers);
			ms.pop();
		}
	}

	@Override
	public void render(MatrixStack ms, VertexConsumer buffer, int light, int overlay, float r, float g, float b, float a) {
		throw new UnsupportedOperationException("unimplemented, call using other render method");
	}
}
