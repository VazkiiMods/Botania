/**
 * This class was created by <Adubbz>. It's distributed as
 * part of the Botania Mod. Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 * 
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 * 
 * File Created @ [? (GMT)]
 */
package vazkii.botania.client.model;

import net.minecraft.client.model.ModelBase;
import net.minecraft.client.model.ModelRenderer;
import net.minecraft.entity.Entity;
import net.minecraft.util.math.MathHelper;

public class ModelPixie extends ModelBase {

	final ModelRenderer Body;
	final ModelRenderer LeftWing;
	final ModelRenderer RightWing;

	public ModelPixie() {
		textureWidth = 64;
		textureHeight = 32;

		Body = new ModelRenderer(this, 0, 0);
		Body.addBox(0F, 0F, 0F, 4, 4, 4);
		Body.setRotationPoint(-2F, 16F, -2F);
		Body.setTextureSize(64, 32);
		Body.mirror = true;
		setRotation(Body, 0F, 0F, 0F);
		LeftWing = new ModelRenderer(this, 32, 0);
		LeftWing.addBox(0F, 0F, -1F, 0, 4, 7);
		LeftWing.setRotationPoint(2F, 15F, 2F);
		LeftWing.setTextureSize(64, 32);
		LeftWing.mirror = true;
		setRotation(LeftWing, 0F, 0F, 0F);
		RightWing = new ModelRenderer(this, 50, 0);
		RightWing.addBox(0F, 0F, -1F, 0, 4, 7);
		RightWing.setRotationPoint(-2F, 15F, 2F);
		RightWing.setTextureSize(64, 32);
		RightWing.mirror = true;
		setRotation(RightWing, 0F, 0F, 0F);
	}

	@Override
	public void render(Entity entity, float f, float f1, float f2, float f3, float f4, float f5) {
		super.render(entity, f, f1, f2, f3, f4, f5);
		setRotationAngles(f, f1, f2, f3, f4, f5, entity);
		Body.render(f5);
		LeftWing.render(f5);
		RightWing.render(f5);
	}

	private void setRotation(ModelRenderer model, float x, float y, float z) {
		model.rotateAngleX = x;
		model.rotateAngleY = y;
		model.rotateAngleZ = z;
	}

	@Override
	public void setRotationAngles(float f, float f1, float f2, float f3, float f4, float f5, Entity entity) {
		super.setRotationAngles(f, f1, f2, f3, f4, f5, entity);

		RightWing.rotateAngleY = -(MathHelper.cos(f2 * 1.7F) * (float)Math.PI * 0.5F);
		LeftWing.rotateAngleY = MathHelper.cos(f2 * 1.7F) * (float)Math.PI * 0.5F;
	}

}