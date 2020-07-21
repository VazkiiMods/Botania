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
import net.minecraft.client.util.math.MatrixStack;

public class ModelTeruTeruBozu extends Model {

	public final ModelPart thread;
	public final ModelPart cloth;
	public final ModelPart happyFace;
	public final ModelPart sadFace;

	public ModelTeruTeruBozu() {
		super(RenderLayer::getEntityCutoutNoCull);
		textureWidth = 64;
		textureHeight = 32;
		sadFace = new ModelPart(this, 32, 0);
		sadFace.setPivot(0.0F, 14.5F, 0.0F);
		sadFace.addCuboid(-4.0F, -6.0F, -4.0F, 8, 8, 8, 0.0F);
		setRotateAngle(sadFace, 0.17453292519943295F, 0.0F, 0.0F);
		happyFace = new ModelPart(this, 0, 0);
		happyFace.setPivot(0.0F, 14.5F, 0.0F);
		happyFace.addCuboid(-4.0F, -6.0F, -4.0F, 8, 8, 8, 0.0F);
		setRotateAngle(happyFace, -0.17453292519943295F, 0.0F, 0.0F);
		thread = new ModelPart(this, 32, 16);
		thread.setPivot(0.0F, 14.0F, 0.0F);
		thread.addCuboid(-3.0F, 2.0F, -3.0F, 6, 1, 6, 0.0F);
		cloth = new ModelPart(this, 0, 16);
		cloth.setPivot(0.0F, 21.5F, -1.0F);
		cloth.addCuboid(-4.0F, -4.0F, -4.0F, 8, 8, 8, 0.0F);
		setRotateAngle(cloth, 0.7853981633974483F, 2.2689280275926285F, 1.5707963267948966F);
	}

	@Override
	public void render(MatrixStack ms, VertexConsumer buffer, int light, int overlay, float r, float g, float b, float a) {
		if (MinecraftClient.getInstance().world.isRaining()) {
			sadFace.render(ms, buffer, light, overlay, r, g, b, a);
		} else {
			happyFace.render(ms, buffer, light, overlay, r, g, b, a);
		}
		thread.render(ms, buffer, light, overlay, r, g, b, a);
		cloth.render(ms, buffer, light, overlay, r, g, b, a);
	}

	public void setRotateAngle(ModelPart modelRenderer, float x, float y, float z) {
		modelRenderer.pitch = x;
		modelRenderer.yaw = y;
		modelRenderer.roll = z;
	}
}
