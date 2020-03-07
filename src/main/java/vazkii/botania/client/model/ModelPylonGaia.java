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
import net.minecraft.client.renderer.model.Model;
import net.minecraft.client.renderer.model.ModelRenderer;

public class ModelPylonGaia extends Model implements IPylonModel {

	private final ModelRenderer platef;
	private final ModelRenderer plateb;
	private final ModelRenderer platel;
	private final ModelRenderer plater;

	private final ModelRenderer shardlbt;
	private final ModelRenderer shardrbt;
	private final ModelRenderer shardlft;
	private final ModelRenderer shardrft;

	private final ModelRenderer shardlbb;
	private final ModelRenderer shardrbb;
	private final ModelRenderer shardlfb;
	private final ModelRenderer shardrfb;

	public ModelPylonGaia() {
		super(RenderType::getEntityTranslucent);

		textureWidth = 64;
		textureHeight = 64;

		//plates
		platel = new ModelRenderer(this, 36, 0);
		platel.setRotationPoint(0.0F, 16.0F, 0.0F);
		platel.addCuboid(-3.0F, -3.0F, -8.0F, 6, 6, 2, 0.0F);
		setRotation(platel, 0.0F, 1.5707963267948966F, 0.0F);
		plater = new ModelRenderer(this, 36, 0);
		plater.setRotationPoint(0.0F, 16.0F, 0.0F);
		plater.addCuboid(-3.0F, -3.0F, -8.0F, 6, 6, 2, 0.0F);
		setRotation(plater, 0.0F, -1.5707963267948966F, 0.0F);
		platef = new ModelRenderer(this, 36, 0);
		platef.setRotationPoint(0.0F, 16.0F, 0.0F);
		platef.addCuboid(-3.0F, -3.0F, -8.0F, 6, 6, 2, 0.0F);
		plateb = new ModelRenderer(this, 36, 0);
		plateb.setRotationPoint(0.0F, 16.0F, 0.0F);
		plateb.addCuboid(-3.0F, -3.0F, -8.0F, 6, 6, 2, 0.0F);
		setRotation(plateb, 0.0F, 3.141592653589793F, 0.0F);

		//shards
		shardrft = new ModelRenderer(this, 16, 32);
		shardrft.setRotationPoint(0.0F, 16.0F, 0.0F);
		shardrft.addCuboid(2.0F, -6.0F, -5.0F, 3, 2, 3, 0.0F);
		shardlbt = new ModelRenderer(this, 0, 0);
		shardlbt.setRotationPoint(0.0F, 16.0F, 0.0F);
		shardlbt.addCuboid(-5.0F, -6.0F, 0.0F, 6, 5, 5, 0.0F);
		shardrbt = new ModelRenderer(this, 22, 9);
		shardrbt.setRotationPoint(0.0F, 16.0F, 0.0F);
		shardrbt.addCuboid(3.0F, -5.0F, 0.0F, 2, 4, 5, 0.0F);
		shardlft = new ModelRenderer(this, 0, 32);
		shardlft.setRotationPoint(0.0F, 16.0F, 0.0F);
		shardlft.addCuboid(-5.0F, -7.0F, -5.0F, 5, 6, 3, 0.0F);

		shardrfb = new ModelRenderer(this, 16, 37);
		shardrfb.setRotationPoint(0.0F, 16.0F, 0.0F);
		shardrfb.addCuboid(2.0F, -2.0F, -5.0F, 3, 6, 3, 0.0F);
		shardlbb = new ModelRenderer(this, 0, 10);
		shardlbb.setRotationPoint(0.0F, 16.0F, 0.0F);
		shardlbb.addCuboid(-5.0F, 1.0F, 0.0F, 6, 3, 5, 0.0F);
		shardrbb = new ModelRenderer(this, 22, 0);
		shardrbb.setRotationPoint(0.0F, 16.0F, 0.0F);
		shardrbb.addCuboid(3.0F, 1.0F, 0.0F, 2, 4, 5, 0.0F);
		shardlfb = new ModelRenderer(this, 0, 41);
		shardlfb.setRotationPoint(0.0F, 16.0F, 0.0F);
		shardlfb.addCuboid(-5.0F, 1.0F, -5.0F, 5, 2, 3, 0.0F);
	}

	@Override
	public void renderCrystal(MatrixStack ms, IVertexBuilder buffer, int light, int overlay) {
		shardrft.render(ms, buffer, light, overlay);
		shardlbt.render(ms, buffer, light, overlay);
		shardrbt.render(ms, buffer, light, overlay);
		shardlft.render(ms, buffer, light, overlay);

		shardrfb.render(ms, buffer, light, overlay);
		shardlbb.render(ms, buffer, light, overlay);
		shardrbb.render(ms, buffer, light, overlay);
		shardlfb.render(ms, buffer, light, overlay);
	}

	@Override
	public void renderRing(MatrixStack ms, IVertexBuilder buffer, int light, int overlay) {
		platef.render(ms, buffer, light, overlay);
		plateb.render(ms, buffer, light, overlay);
		platel.render(ms, buffer, light, overlay);
		plater.render(ms, buffer, light, overlay);
	}

	private void setRotation(ModelRenderer model, float x, float y, float z) {

		model.rotateAngleX = x;
		model.rotateAngleY = y;
		model.rotateAngleZ = z;
	}

	@Override
	public void render(MatrixStack ms, IVertexBuilder buffer, int light, int overlay, float r, float g, float b, float a) {
		throw new UnsupportedOperationException("unimplemented");
	}
}
