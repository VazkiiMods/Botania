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

import net.minecraft.client.renderer.entity.model.EntityModel;
import net.minecraft.client.renderer.model.Model;
import net.minecraft.client.renderer.entity.model.RendererModel;
import net.minecraft.client.renderer.model.Model;
import net.minecraft.entity.Entity;
import net.minecraft.util.math.MathHelper;
import vazkii.botania.common.entity.EntityPixie;

public class ModelPixie extends EntityModel<EntityPixie> {

	public RendererModel body;
    public RendererModel leftWingT;
    public RendererModel leftWingB;
    public RendererModel rightWingT;
    public RendererModel rightWingB;

    public ModelPixie() {
    	
        textureWidth = 32;
        textureHeight = 32;
        
        body = new RendererModel(this, 0, 0);
        body.setRotationPoint(0.0F, 16.0F, 0.0F);
        body.addBox(-2.5F, 0.0F, -2.5F, 5, 5, 5, 0.0F);
        
        leftWingT = new RendererModel(this, 0, 4);
        leftWingT.setRotationPoint(2.5F, 18.0F, 0.5F);
        leftWingT.addBox(0.0F, -5.0F, 0.0F, 0, 5, 6, 0.0F);
        setRotateAngle(leftWingT, 0.2617993877991494F, 0.5235987755982988F, 0.2617993877991494F);
        leftWingB = new RendererModel(this, 0, 11);
        leftWingB.setRotationPoint(2.5F, 18.0F, 0.5F);
        leftWingB.addBox(0.0F, 0.0F, 0.0F, 0, 3, 4, 0.0F);
        setRotateAngle(leftWingB, -0.2617993877991494F, 0.2617993877991494F, -0.2617993877991494F);
        rightWingT = new RendererModel(this, 0, 4);
        rightWingT.setRotationPoint(-2.5F, 18.0F, 0.5F);
        rightWingT.addBox(0.0F, -5.0F, 0.0F, 0, 5, 6, 0.0F);
        setRotateAngle(rightWingT, 0.2617993877991494F, -0.5235987755982988F, -0.2617993877991494F);
        rightWingB = new RendererModel(this, 0, 11);
        rightWingB.setRotationPoint(-2.5F, 18.0F, 0.5F);
        rightWingB.addBox(0.0F, 0.0F, 0.0F, 0, 3, 4, 0.0F);
        setRotateAngle(rightWingB, -0.2617993877991494F, -0.2617993877991494F, 0.2617993877991494F);
    }

	@Override
	public void render(EntityPixie entity, float f, float f1, float f2, float f3, float f4, float f5) {
		super.render(entity, f, f1, f2, f3, f4, f5);
		
		setRotationAngles(entity, f, f1, f2, f3, f4, f5);
		
		body.render(f5);
		
		leftWingT.render(f5);
		leftWingB.render(f5);
		rightWingT.render(f5);
		rightWingB.render(f5);
	}
	
	public void setRotateAngle(RendererModel modelRenderer, float x, float y, float z) {
        modelRenderer.rotateAngleX = x;
        modelRenderer.rotateAngleY = y;
        modelRenderer.rotateAngleZ = z;
    }

	@Override
	public void setRotationAngles(EntityPixie entity, float f, float f1, float f2, float f3, float f4, float f5) {
		super.setRotationAngles(entity, f, f1, f2, f3, f4, f5);

		rightWingT.rotateAngleY = -(MathHelper.cos(f2 * 1.7F) * (float)Math.PI * 0.5F);
		leftWingT.rotateAngleY = MathHelper.cos(f2 * 1.7F) * (float)Math.PI * 0.5F;
		rightWingB.rotateAngleY = -(MathHelper.cos(f2 * 1.7F) * (float)Math.PI * 0.25F);
		leftWingB.rotateAngleY = MathHelper.cos(f2 * 1.7F) * (float)Math.PI * 0.25F;
	}

}