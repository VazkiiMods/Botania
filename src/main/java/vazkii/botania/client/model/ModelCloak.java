/*
 * This class is distributed as part of the Botania Mod.
 * Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 *
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 */
package vazkii.botania.client.model;

import net.minecraft.client.model.Model;
import net.minecraft.client.model.ModelPart;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.util.math.MatrixStack;

/**
 * armor_cloak - wiiv
 * Created using Tabula 4.1.1
 */
public class ModelCloak extends Model {

	public ModelPart collar;
	public ModelPart sideL;
	public ModelPart sideR;

	public ModelCloak() {
		super(RenderLayer::getEntityCutout);

		textureWidth = 64;
		textureHeight = 64;
		float s = 0.01F;

		collar = new ModelPart(this, 0, 0);
		collar.setPivot(0.0F, -3.0F, -4.5F);
		collar.addCuboid(-5.5F, 0.0F, -1.5F, 11, 5, 11, s);
		setRotateAngle(collar, 0.08726646259971647F, 0.0F, 0.0F);
		sideL = new ModelPart(this, 0, 16);
		sideL.mirror = true;
		sideL.setPivot(0.0F, 0.0F, 0.0F);
		sideL.addCuboid(-0.5F, -0.5F, -5.5F, 11, 21, 10, s);
		setRotateAngle(sideL, 0.08726646259971647F, -0.08726646259971647F, -0.17453292519943295F);
		sideR = new ModelPart(this, 0, 16);
		sideR.setPivot(0.0F, 0.0F, 0.0F);
		sideR.addCuboid(-10.5F, -0.5F, -5.5F, 11, 21, 10, s);
		setRotateAngle(sideR, 0.08726646259971647F, 0.08726646259971647F, 0.17453292519943295F);

	}

	@Override
	public void render(MatrixStack ms, VertexConsumer buffer, int light, int overlay, float r, float g, float b, float a) {
		collar.render(ms, buffer, light, overlay, r, g, b, a);
		sideL.render(ms, buffer, light, overlay, r, g, b, a);
		sideR.render(ms, buffer, light, overlay, r, g, b, a);
	}

	public void setRotateAngle(ModelPart modelRenderer, float x, float y, float z) {
		modelRenderer.pitch = x;
		modelRenderer.yaw = y;
		modelRenderer.roll = z;
	}
}
