/*
 * This class is distributed as part of the Botania Mod.
 * Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 *
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 */
package vazkii.botania.client.model;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.vertex.IVertexBuilder;

import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.Vector3f;
import net.minecraft.client.renderer.model.Model;
import net.minecraft.client.renderer.model.ModelRenderer;

public class ModelHourglass extends Model {

	public ModelRenderer top;
	public ModelRenderer glassT;
	public ModelRenderer ring;
	public ModelRenderer glassB;
	public ModelRenderer bottom;

	public ModelRenderer sandT;
	public ModelRenderer sandB;

	public ModelHourglass() {
		super(RenderType::getEntityTranslucent);

		textureWidth = 64;
		textureHeight = 32;

		top = new ModelRenderer(this, 20, 0);
		top.setRotationPoint(0.0F, 0.0F, 0.0F);
		top.addBox(-3.0F, -6.5F, -3.0F, 6, 1, 6, 0.0F);
		glassT = new ModelRenderer(this, 0, 0);
		glassT.setRotationPoint(0.0F, 0.0F, 0.0F);
		glassT.addBox(-2.5F, -5.5F, -2.5F, 5, 5, 5, 0.0F);
		ring = new ModelRenderer(this, 0, 20);
		ring.setRotationPoint(0.0F, 0.0F, 0.0F);
		ring.addBox(-1.5F, -0.5F, -1.5F, 3, 1, 3, 0.0F);
		glassB = new ModelRenderer(this, 0, 10);
		glassB.setRotationPoint(0.0F, 0.0F, 0.0F);
		glassB.addBox(-2.5F, 0.5F, -2.5F, 5, 5, 5, 0.0F);
		bottom = new ModelRenderer(this, 20, 7);
		bottom.setRotationPoint(0.0F, 0.0F, 0.0F);
		bottom.addBox(-3.0F, 5.5F, -3.0F, 6, 1, 6, 0.0F);

		sandT = new ModelRenderer(this, 20, 14);
		sandT.setRotationPoint(0.0F, 0.0F, 0.0F);
		sandT.addBox(0.0F, 0.0F, 0.0F, 4, 4, 4, 0.0F); // -2.0F, -5.0F, -2.0F
		sandB = new ModelRenderer(this, 20, 14);
		sandB.setRotationPoint(0.0F, 0.0F, 0.0F);
		sandB.addBox(0.0F, 0.0F, 0.0F, 4, 4, 4, 0.0F); // -2.0F, 1.0F, -2.05F
	}

	@Override
	public void render(MatrixStack ms, IVertexBuilder buffer, int light, int overlay, float r, float g, float b, float a) {
		render(ms, buffer, light, overlay, r, g, b, a, 0, 1, false);
	}

	public void render(MatrixStack ms, IVertexBuilder buffer, int light, int overlay, float r, float g, float b, float a, float fract1, float fract2, boolean flip) {
		if (flip) {
			float tmp = fract1;
			fract1 = fract2;
			fract2 = tmp;
		}

		float f = 1F / 16F;
		ring.render(ms, buffer, light, overlay, 1, 1, 1, a);
		top.render(ms, buffer, light, overlay, 1, 1, 1, a);
		bottom.render(ms, buffer, light, overlay, 1, 1, 1, a);

		if (fract1 > 0) {
			ms.push();
			if (flip) {
				ms.translate(-2.0F * f, 1.0F * f, -2.0F * f);
			} else {
				ms.rotate(Vector3f.ZP.rotationDegrees(180F));
				ms.translate(-2.0F * f, -5.0F * f, -2.0F * f);
			}
			ms.scale(1F, fract1, 1F);
			sandT.render(ms, buffer, light, overlay, r, g, b, a);
			ms.pop();
		}

		if (fract2 > 0) {
			ms.push();
			if (flip) {
				ms.translate(-2.0F * f, -5.0F * f, -2.0F * f);
			} else {
				ms.rotate(Vector3f.ZP.rotationDegrees(180F));
				ms.translate(-2.0F * f, 1.0F * f, -2.0F * f);
			}
			ms.scale(1F, fract2, 1F);
			sandB.render(ms, buffer, light, overlay, r, g, b, a);
			ms.pop();
		}

		glassT.render(ms, buffer, light, overlay, 1, 1, 1, a);
		glassB.render(ms, buffer, light, overlay, 1, 1, 1, a);
	}

}
