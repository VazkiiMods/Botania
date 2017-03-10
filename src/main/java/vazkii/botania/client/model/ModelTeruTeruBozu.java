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

import net.minecraft.client.Minecraft;
import net.minecraft.client.model.ModelBase;
import net.minecraft.client.model.ModelRenderer;

public class ModelTeruTeruBozu extends ModelBase {

	public final ModelRenderer thread;
	public final ModelRenderer cloth;
	public final ModelRenderer happyFace;
	public final ModelRenderer sadFace;

	public ModelTeruTeruBozu() {
		textureWidth = 64;
		textureHeight = 32;
		sadFace = new ModelRenderer(this, 32, 0);
		sadFace.setRotationPoint(0.0F, 14.5F, 0.0F);
		sadFace.addBox(-4.0F, -6.0F, -4.0F, 8, 8, 8, 0.0F);
		setRotateAngle(sadFace, 0.17453292519943295F, 0.0F, 0.0F);
		happyFace = new ModelRenderer(this, 0, 0);
		happyFace.setRotationPoint(0.0F, 14.5F, 0.0F);
		happyFace.addBox(-4.0F, -6.0F, -4.0F, 8, 8, 8, 0.0F);
		setRotateAngle(happyFace, -0.17453292519943295F, 0.0F, 0.0F);
		thread = new ModelRenderer(this, 32, 16);
		thread.setRotationPoint(0.0F, 14.0F, 0.0F);
		thread.addBox(-3.0F, 2.0F, -3.0F, 6, 1, 6, 0.0F);
		cloth = new ModelRenderer(this, 0, 16);
		cloth.setRotationPoint(0.0F, 21.5F, -1.0F);
		cloth.addBox(-4.0F, -4.0F, -4.0F, 8, 8, 8, 0.0F);
		setRotateAngle(cloth, 0.7853981633974483F, 2.2689280275926285F, 1.5707963267948966F);
	}

	public void render() {
		float f5 = 1F / 16F;
		if(Minecraft.getMinecraft().world.isRaining())
			sadFace.render(f5);
		else happyFace.render(f5);
		thread.render(f5);
		cloth.render(f5);
	}

	public void setRotateAngle(ModelRenderer modelRenderer, float x, float y, float z) {
		modelRenderer.rotateAngleX = x;
		modelRenderer.rotateAngleY = y;
		modelRenderer.rotateAngleZ = z;
	}
}
