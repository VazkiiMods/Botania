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

public class ModelAvatar extends Model {

	public final ModelPart body;
	public final ModelPart rightarm;
	public final ModelPart leftarm;
	public final ModelPart rightleg;
	public final ModelPart leftleg;
	public final ModelPart head;

	public ModelAvatar() {
		super(RenderLayer::getEntitySolid);
		textureWidth = 32;
		textureHeight = 32;
		leftleg = new ModelPart(this, 0, 20);
		leftleg.mirror = true;
		leftleg.setPivot(1.5F, 18.0F, -0.5F);
		leftleg.addCuboid(-1.5F, 0.0F, -1.5F, 3, 6, 3, 0.0F);
		rightarm = new ModelPart(this, 0, 20);
		rightarm.setPivot(-3.0F, 15.0F, -1.0F);
		rightarm.addCuboid(-2.0F, -1.0F, -1.0F, 2, 6, 3, 0.0F);
		setRotateAngle(rightarm, 0.0F, -0.0F, 0.08726646259971647F);
		leftarm = new ModelPart(this, 0, 20);
		leftarm.mirror = true;
		leftarm.setPivot(3.0F, 15.0F, -1.0F);
		leftarm.addCuboid(0.0F, -1.0F, -1.0F, 2, 6, 3, 0.0F);
		setRotateAngle(leftarm, 0.0F, -0.0F, -0.08726646259971647F);
		head = new ModelPart(this, 0, 0);
		head.setPivot(0.0F, 14.0F, 0.0F);
		head.addCuboid(-3.0F, -6.0F, -3.0F, 6, 6, 6, 0.0F);
		rightleg = new ModelPart(this, 0, 20);
		rightleg.setPivot(-1.5F, 18.0F, -0.5F);
		rightleg.addCuboid(-1.5F, 0.0F, -1.5F, 3, 6, 3, 0.0F);
		body = new ModelPart(this, 0, 12);
		body.setPivot(0.0F, 14.0F, 0.0F);
		body.addCuboid(-3.0F, 0.0F, -2.0F, 6, 4, 4, 0.0F);
	}

	@Override
	public void render(MatrixStack ms, VertexConsumer buffer, int light, int overlay, float r, float g, float b, float a) {
		leftleg.render(ms, buffer, light, overlay, r, g, b, a);
		rightarm.render(ms, buffer, light, overlay, r, g, b, a);
		leftarm.render(ms, buffer, light, overlay, r, g, b, a);
		head.render(ms, buffer, light, overlay, r, g, b, a);
		rightleg.render(ms, buffer, light, overlay, r, g, b, a);
		body.render(ms, buffer, light, overlay, r, g, b, a);
	}

	public void setRotateAngle(ModelPart modelRenderer, float x, float y, float z) {
		modelRenderer.pitch = x;
		modelRenderer.yaw = y;
		modelRenderer.roll = z;
	}

}
