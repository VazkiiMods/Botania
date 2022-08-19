/**
 * This class was created by <Kihira>. It's distributed as
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

public class ModelSkullOverride extends ModelBase {

	private final ModelRenderer bipedHead;
	private final ModelRenderer bipedHeadwear;

	public ModelSkullOverride() {
		textureWidth = 64;
		textureHeight = 32;
		bipedHead = new ModelRenderer(this, 0, 0);
		bipedHead.addBox(-4.0F, -8.0F, -4.0F, 8, 8, 8, 0F);
		bipedHead.setRotationPoint(0.0F, 0.0F, 0.0F);
		bipedHeadwear = new ModelRenderer(this, 32, 0);
		bipedHeadwear.addBox(-4.0F, -8.0F, -4.0F, 8, 8, 8, 0.5F);
		bipedHeadwear.setRotationPoint(0.0F, 0.0F, 0.0F);
	}

	public void renderWithoutRotation(float par1) {
		bipedHead.render(par1);
		bipedHeadwear.render(par1);
	}

	@Override
	public void render(Entity par1Entity, float par2, float par3, float par4, float par5, float par6, float par7) {
		setRotationAngles(par2, par3, par4, par5, par6, par7, par1Entity);
		bipedHead.render(par7);
		bipedHeadwear.render(par7);
	}

	@Override
	public void setRotationAngles(float par1, float par2, float par3, float par4, float par5, float par6, Entity par7Entity) {
		bipedHead.rotateAngleY = bipedHeadwear.rotateAngleY = par4 / (180F / (float)Math.PI);
		bipedHead.rotateAngleX = bipedHeadwear.rotateAngleX =  par5 / (180F / (float)Math.PI);
	}
}