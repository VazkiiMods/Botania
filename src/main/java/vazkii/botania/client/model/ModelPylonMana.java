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

public class ModelPylonMana extends Model implements IPylonModel {

	private final ModelRenderer platef;
	private final ModelRenderer plateb;
	private final ModelRenderer platel;
	private final ModelRenderer plater;

	private final ModelRenderer shardlf;
	private final ModelRenderer shardrf;
	private final ModelRenderer shardlb;
	private final ModelRenderer shardrb;

	public ModelPylonMana() {
		super(RenderType::getEntityTranslucent);

		textureWidth = 64;
		textureHeight = 64;

		//plates
		platef = new ModelRenderer(this, 36, 0);
		platef.setRotationPoint(0.0F, 16.0F, 0.0F);
		platef.addBox(-3.0F, -4.0F, -8.0F, 6, 8, 2, 0.0F);
		plateb = new ModelRenderer(this, 36, 0);
		plateb.setRotationPoint(0.0F, 16.0F, 0.0F);
		plateb.addBox(-3.0F, -4.0F, -8.0F, 6, 8, 2, 0.0F);
		setRotation(plateb, 0.0F, 3.141592653589793F, 0.0F);
		platel = new ModelRenderer(this, 36, 0);
		platel.setRotationPoint(0.0F, 16.0F, 0.0F);
		platel.addBox(-3.0F, -4.0F, -8.0F, 6, 8, 2, 0.0F);
		setRotation(platel, 0.0F, 1.5707963267948966F, 0.0F);
		plater = new ModelRenderer(this, 36, 0);
		plater.setRotationPoint(0.0F, 16.0F, 0.0F);
		plater.addBox(-3.0F, -4.0F, -8.0F, 6, 8, 2, 0.0F);
		setRotation(plater, 0.0F, -1.5707963267948966F, 0.0F);

		//shards
		shardlf = new ModelRenderer(this, 0, 21);
		shardlf.setRotationPoint(0.0F, 16.0F, 0.0F);
		shardlf.addBox(-5.0F, -9.0F, -5.0F, 5, 16, 3, 0.0F);
		shardrf = new ModelRenderer(this, 16, 21);
		shardrf.setRotationPoint(0.0F, 16.0F, 0.0F);
		shardrf.addBox(2.0F, -12.0F, -5.0F, 3, 16, 3, 0.0F);
		shardlb = new ModelRenderer(this, 0, 0);
		shardlb.setRotationPoint(0.0F, 16.0F, 0.0F);
		shardlb.addBox(-5.0F, -10.0F, 0.0F, 6, 16, 5, 0.0F);
		shardrb = new ModelRenderer(this, 22, 0);
		shardrb.setRotationPoint(0.0F, 16.0F, 0.0F);
		shardrb.addBox(3.0F, -11.0F, 0.0F, 2, 16, 5, 0.0F);
	}

	@Override
	public void renderCrystal(MatrixStack ms, IVertexBuilder buffer, int light, int overlay) {
		shardlf.render(ms, buffer, light, overlay);
		shardrf.render(ms, buffer, light, overlay);
		shardlb.render(ms, buffer, light, overlay);
		shardrb.render(ms, buffer, light, overlay);
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
