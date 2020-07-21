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
import net.minecraft.client.util.math.Vector3f;

public class ModelBellows extends Model {

	final ModelPart top;
	final ModelPart base;
	final ModelPart pipe;
	final ModelPart funnel;

	public ModelBellows() {
		super(RenderLayer::getEntityCutout);
		textureWidth = 64;
		textureHeight = 32;

		top = new ModelPart(this, 0, 0);
		top.setPivot(0.0F, 16.0F, 0.0F);
		top.addCuboid(-4.0F, -2.0F, -4.0F, 8, 1, 8, 0.0F);
		base = new ModelPart(this, 0, 9);
		base.setPivot(0.0F, 16.0F, 0.0F);
		base.addCuboid(-5.0F, 6.0F, -5.0F, 10, 2, 10, 0.0F);
		pipe = new ModelPart(this, 0, 21);
		pipe.setPivot(0.0F, 16.0F, 0.0F);
		pipe.addCuboid(-1.0F, 6.0F, -8.0F, 2, 2, 3, 0.0F);

		funnel = new ModelPart(this, 40, 0);
		funnel.setPivot(0.0F, 0.0F, 0.0F);
		funnel.addCuboid(0.0F, 0.0F, 0.0F, 6, 7, 6, 0.0F);
	}

	@Override
	public void render(MatrixStack ms, VertexConsumer buffer, int light, int overlay, float r, float g, float b, float a) {
		render(ms, buffer, light, overlay, r, g, b, a, 1);
	}

	public void render(MatrixStack ms, VertexConsumer buffer, int light, int overlay, float r, float g, float b, float alpha, float fract) {
		base.render(ms, buffer, light, overlay, r, g, b, alpha);
		pipe.render(ms, buffer, light, overlay, r, g, b, alpha);

		float mov = (1F - fract) * 0.5F;

		ms.translate(0F, mov, 0F);
		top.render(ms, buffer, light, overlay, r, g, b, alpha);
		ms.translate(0F, -mov, 0F);

		ms.multiply(Vector3f.POSITIVE_X.getDegreesQuaternion(180F));
		ms.translate(-0.19F, -1.375F, -0.19F);
		ms.scale(1F, fract, 1F);
		funnel.render(ms, buffer, light, overlay, r, g, b, alpha);
		ms.scale(1F, 1F / fract, 1F);
	}

}
