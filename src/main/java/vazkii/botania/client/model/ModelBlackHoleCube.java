/**
 * This class was created by <wiiv>. It's distributed as
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

public class ModelBlackHoleCube extends ModelBase {
	public ModelRenderer shape1;
	public ModelRenderer shape1_1;
	public ModelRenderer shape1_2;
	public ModelRenderer shape1_3;
	public ModelRenderer shape1_4;
	public ModelRenderer shape1_5;
	public ModelRenderer shape1_6;
	public ModelRenderer shape1_7;
	public ModelRenderer shape1_8;
	public ModelRenderer shape1_9;
	public ModelRenderer shape1_10;
	public ModelRenderer shape1_11;

	public ModelBlackHoleCube() {
		textureWidth = 36;
		textureHeight = 2;
		shape1_3 = new ModelRenderer(this, 0, 0);
		shape1_3.setRotationPoint(0.0F, 16.0F, 0.0F);
		shape1_3.addBox(-7.0F, 7.0F, -8.0F, 14, 1, 1, 0.0F);
		setRotateAngle(shape1_3, 0.0F, 1.5707963267948966F, 0.0F);
		shape1_7 = new ModelRenderer(this, 0, 0);
		shape1_7.setRotationPoint(0.0F, 16.0F, 0.0F);
		shape1_7.addBox(-7.0F, -8.0F, -8.0F, 14, 1, 1, 0.0F);
		setRotateAngle(shape1_7, 0.0F, 1.5707963267948966F, 0.0F);
		shape1_9 = new ModelRenderer(this, 0, 0);
		shape1_9.setRotationPoint(0.0F, 16.0F, 0.0F);
		shape1_9.addBox(-7.0F, 7.0F, 7.0F, 14, 1, 1, 0.0F);
		setRotateAngle(shape1_9, 0.0F, 3.141592653589793F, 1.5707963267948966F);
		shape1_11 = new ModelRenderer(this, 0, 0);
		shape1_11.setRotationPoint(0.0F, 16.0F, 0.0F);
		shape1_11.addBox(-7.0F, 7.0F, -8.0F, 14, 1, 1, 0.0F);
		setRotateAngle(shape1_11, 0.0F, 3.141592653589793F, 1.5707963267948966F);
		shape1 = new ModelRenderer(this, 0, 0);
		shape1.setRotationPoint(0.0F, 16.0F, 0.0F);
		shape1.addBox(-8.0F, 7.0F, 7.0F, 16, 1, 1, 0.0F);
		shape1_4 = new ModelRenderer(this, 0, 0);
		shape1_4.setRotationPoint(0.0F, 16.0F, 0.0F);
		shape1_4.addBox(-8.0F, 7.0F, -8.0F, 16, 1, 1, 0.0F);
		shape1_8 = new ModelRenderer(this, 0, 0);
		shape1_8.setRotationPoint(0.0F, 16.0F, 0.0F);
		shape1_8.addBox(-7.0F, -8.0F, -8.0F, 14, 1, 1, 0.0F);
		setRotateAngle(shape1_8, 0.0F, 3.141592653589793F, 1.5707963267948966F);
		shape1_2 = new ModelRenderer(this, 0, 0);
		shape1_2.setRotationPoint(0.0F, 16.0F, 0.0F);
		shape1_2.addBox(-8.0F, -8.0F, -8.0F, 16, 1, 1, 0.0F);
		shape1_1 = new ModelRenderer(this, 0, 0);
		shape1_1.setRotationPoint(0.0F, 16.0F, 0.0F);
		shape1_1.addBox(-8.0F, -8.0F, 7.0F, 16, 1, 1, 0.0F);
		shape1_6 = new ModelRenderer(this, 0, 0);
		shape1_6.setRotationPoint(0.0F, 16.0F, 0.0F);
		shape1_6.addBox(-7.0F, -8.0F, -8.0F, 14, 1, 1, 0.0F);
		setRotateAngle(shape1_6, 0.0F, -1.5707963267948966F, 0.0F);
		shape1_10 = new ModelRenderer(this, 0, 0);
		shape1_10.setRotationPoint(0.0F, 16.0F, 0.0F);
		shape1_10.addBox(-7.0F, -8.0F, 7.0F, 14, 1, 1, 0.0F);
		setRotateAngle(shape1_10, 0.0F, 3.141592653589793F, 1.5707963267948966F);
		shape1_5 = new ModelRenderer(this, 0, 0);
		shape1_5.setRotationPoint(0.0F, 16.0F, 0.0F);
		shape1_5.addBox(-7.0F, 7.0F, -8.0F, 14, 1, 1, 0.0F);
		setRotateAngle(shape1_5, 0.0F, -1.5707963267948966F, 0.0F);
	}

	@Override
	public void render(Entity entity, float f, float f1, float f2, float f3, float f4, float f5) {
		shape1_3.render(f5);
		shape1_7.render(f5);
		shape1_9.render(f5);
		shape1_11.render(f5);
		shape1.render(f5);
		shape1_4.render(f5);
		shape1_8.render(f5);
		shape1_2.render(f5);
		shape1_1.render(f5);
		shape1_6.render(f5);
		shape1_10.render(f5);
		shape1_5.render(f5);
	}


	public void setRotateAngle(ModelRenderer modelRenderer, float x, float y, float z) {
		modelRenderer.rotateAngleX = x;
		modelRenderer.rotateAngleY = y;
		modelRenderer.rotateAngleZ = z;
	}
}
