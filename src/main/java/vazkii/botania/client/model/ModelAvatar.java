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

import net.minecraft.client.renderer.entity.model.RendererModel;
import net.minecraft.client.renderer.model.Model;

public class ModelAvatar extends Model {

	public final RendererModel body;
	public final RendererModel rightarm;
	public final RendererModel leftarm;
	public final RendererModel rightleg;
	public final RendererModel leftleg;
	public final RendererModel head;

	public ModelAvatar() {
		textureWidth = 32;
		textureHeight = 32;
		leftleg = new RendererModel(this, 0, 20);
		leftleg.mirror = true;
		leftleg.setRotationPoint(1.5F, 18.0F, -0.5F);
		leftleg.addBox(-1.5F, 0.0F, -1.5F, 3, 6, 3, 0.0F);
		rightarm = new RendererModel(this, 0, 20);
		rightarm.setRotationPoint(-3.0F, 15.0F, -1.0F);
		rightarm.addBox(-2.0F, -1.0F, -1.0F, 2, 6, 3, 0.0F);
		setRotateAngle(rightarm, 0.0F, -0.0F, 0.08726646259971647F);
		leftarm = new RendererModel(this, 0, 20);
		leftarm.mirror = true;
		leftarm.setRotationPoint(3.0F, 15.0F, -1.0F);
		leftarm.addBox(0.0F, -1.0F, -1.0F, 2, 6, 3, 0.0F);
		setRotateAngle(leftarm, 0.0F, -0.0F, -0.08726646259971647F);
		head = new RendererModel(this, 0, 0);
		head.setRotationPoint(0.0F, 14.0F, 0.0F);
		head.addBox(-3.0F, -6.0F, -3.0F, 6, 6, 6, 0.0F);
		rightleg = new RendererModel(this, 0, 20);
		rightleg.setRotationPoint(-1.5F, 18.0F, -0.5F);
		rightleg.addBox(-1.5F, 0.0F, -1.5F, 3, 6, 3, 0.0F);
		body = new RendererModel(this, 0, 12);
		body.setRotationPoint(0.0F, 14.0F, 0.0F);
		body.addBox(-3.0F, 0.0F, -2.0F, 6, 4, 4, 0.0F);
	}

	public void render() {
		float f5 = 1F / 15F;
		leftleg.render(f5);
		rightarm.render(f5);
		leftarm.render(f5);
		head.render(f5);
		rightleg.render(f5);
		body.render(f5);
	}

	public void setRotateAngle(RendererModel modelRenderer, float x, float y, float z) {
		modelRenderer.rotateAngleX = x;
		modelRenderer.rotateAngleY = y;
		modelRenderer.rotateAngleZ = z;
	}

}