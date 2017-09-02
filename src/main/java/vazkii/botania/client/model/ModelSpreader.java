/**
 * This class was created by <Vazkii>. It's distributed as
 * part of the Botania Mod. Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 *
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 *
 * File Created @ [Jan 25, 2014, 1:55:05 PM (GMT)]
 */
package vazkii.botania.client.model;

import net.minecraft.client.model.ModelBase;
import net.minecraft.client.model.ModelRenderer;

public class ModelSpreader extends ModelBase {

	final ModelRenderer top;
    final ModelRenderer sideL;
    final ModelRenderer sideR;
    final ModelRenderer bottom;
	final ModelRenderer back;
	
    final ModelRenderer holeT;
	final ModelRenderer holeL;    
	final ModelRenderer holeR;
	final ModelRenderer holeB;
    
    final ModelRenderer inside;

	public ModelSpreader() {
		
		textureWidth = 64;
		textureHeight = 64;
		
		top = new ModelRenderer(this, 0, 0);
        top.setRotationPoint(0.0F, 16.0F, 0.0F);
        top.addBox(-7.0F, -7.0F, -7.0F, 14, 1, 14, 0.0F);
		sideL = new ModelRenderer(this, 0, 30);
        sideL.setRotationPoint(0.0F, 16.0F, 0.0F);
        sideL.addBox(6.0F, -6.0F, -6.0F, 1, 12, 12, 0.0F);
        sideR = new ModelRenderer(this, 0, 30);
        sideR.setRotationPoint(0.0F, 16.0F, 0.0F);
        sideR.addBox(-7.0F, -6.0F, -6.0F, 1, 12, 12, 0.0F);
        back = new ModelRenderer(this, 0, 29);
        back.setRotationPoint(0.0F, 16.0F, 0.0F);
        back.addBox(-7.0F, -6.0F, -7.0F, 14, 12, 1, 0.0F);
        bottom = new ModelRenderer(this, 0, 15);
        bottom.setRotationPoint(0.0F, 16.0F, 0.0F);
        bottom.addBox(-7.0F, 6.0F, -7.0F, 14, 1, 14, 0.0F);
        
        holeT = new ModelRenderer(this, 0, 54);
        holeT.setRotationPoint(0.0F, 10.0F, 6.0F);
        holeT.addBox(-7.0F, 0.0F, 0.0F, 14, 4, 1, 0.0F);
        holeL = new ModelRenderer(this, 30, 54);
        holeL.setRotationPoint(2.0F, 10.0F, 6.0F);
        holeL.addBox(0.0F, 4.0F, 0.0F, 5, 4, 1, 0.0F);
        holeR = new ModelRenderer(this, 30, 59);
        holeR.setRotationPoint(0.0F, 10.0F, 6.0F);
        holeR.addBox(-7.0F, 4.0F, 0.0F, 5, 4, 1, 0.0F);
        holeB = new ModelRenderer(this, 0, 59);
        holeB.setRotationPoint(0.0F, 18.0F, 6.0F);
        holeB.addBox(-7.0F, 0.0F, 0.0F, 14, 4, 1, 0.0F);
        
        inside = new ModelRenderer(this, 30, 30);
        inside.setRotationPoint(0.0F, 16.0F, 0.0F);
        inside.addBox(-3.0F, -3.0F, -3.0F, 6, 6, 6, 0.0F);
	}

	public void render() {
		float f = 1F / 16F;
		
		top.render(f);
        sideL.render(f);
        sideR.render(f);
        back.render(f);
        bottom.render(f);
        
        holeT.render(f);
        holeL.render(f);
        holeR.render(f);
        holeB.render(f);
	}

	public void renderCube() {
		float f = 1F / 16F;
		
		inside.render(f);
	}
}