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

public class ModelBellows extends Model {

	final ModelRenderer top;
	final ModelRenderer base;
	final ModelRenderer pipe;
	final ModelRenderer funnel;

	public ModelBellows() {
		super(RenderType::getEntityCutout);
		textureWidth = 64;
		textureHeight = 32;

		top = new ModelRenderer(this, 0, 0);
		top.setRotationPoint(0.0F, 16.0F, 0.0F);
		top.addBox(-4.0F, -2.0F, -4.0F, 8, 1, 8, 0.0F);
		base = new ModelRenderer(this, 0, 9);
		base.setRotationPoint(0.0F, 16.0F, 0.0F);
		base.addBox(-5.0F, 6.0F, -5.0F, 10, 2, 10, 0.0F);
		pipe = new ModelRenderer(this, 0, 21);
		pipe.setRotationPoint(0.0F, 16.0F, 0.0F);
		pipe.addBox(-1.0F, 6.0F, -8.0F, 2, 2, 3, 0.0F);

		funnel = new ModelRenderer(this, 40, 0);
		funnel.setRotationPoint(0.0F, 0.0F, 0.0F);
		funnel.addBox(0.0F, 0.0F, 0.0F, 6, 7, 6, 0.0F);
	}

	@Override
	public void render(MatrixStack ms, IVertexBuilder buffer, int light, int overlay, float r, float g, float b, float a) {
		render(ms, buffer, light, overlay, r, g, b, a, 1);
	}

	public void render(MatrixStack ms, IVertexBuilder buffer, int light, int overlay, float r, float g, float b, float alpha, float fract) {
		base.render(ms, buffer, light, overlay, r, g, b, alpha);
		pipe.render(ms, buffer, light, overlay, r, g, b, alpha);

		float mov = (1F - fract) * 0.5F;

		ms.translate(0F, mov, 0F);
		top.render(ms, buffer, light, overlay, r, g, b, alpha);
		ms.translate(0F, -mov, 0F);

		ms.rotate(Vector3f.XP.rotationDegrees(180F));
		ms.translate(-0.19F, -1.375F, -0.19F);
		ms.scale(1F, fract, 1F);
		funnel.render(ms, buffer, light, overlay, r, g, b, alpha);
		ms.scale(1F, 1F / fract, 1F);
	}

}
