/**
 * This class was created by <Vazkii>. It's distributed as
 * part of the Botania Mod. Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 *
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 *
 * File Created @ [Feb 18, 2014, 10:05:39 PM (GMT)]
 */
package vazkii.botania.client.model;

import net.minecraft.client.model.ModelBase;
import net.minecraft.client.model.ModelRenderer;

public class ModelPylonNatura extends ModelBase implements IPylonModel {

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
    
	public ModelPylonNatura() {
		
		textureWidth = 64;
		textureHeight = 64;

		//plates
        platef = new ModelRenderer(this, 36, 0);
        platef.setRotationPoint(0.0F, 16.0F, 0.0F);
        platef.addBox(-4.0F, -4.0F, -8.0F, 8, 8, 2, 0.0F);
        plateb = new ModelRenderer(this, 36, 0);
        plateb.setRotationPoint(0.0F, 16.0F, 0.0F);
        plateb.addBox(-4.0F, -4.0F, -8.0F, 8, 8, 2, 0.0F);
        setRotation(plateb, 0.0F, 3.141592653589793F, 0.0F);
        platel = new ModelRenderer(this, 36, 0);
        platel.setRotationPoint(0.0F, 16.0F, 0.0F);
        platel.addBox(-4.0F, -4.0F, -8.0F, 8, 8, 2, 0.0F);
        setRotation(platel, 0.0F, 1.5707963267948966F, 0.0F);
        plater = new ModelRenderer(this, 36, 0);
        plater.setRotationPoint(0.0F, 16.0F, 0.0F);
        plater.addBox(-4.0F, -4.0F, -8.0F, 8, 8, 2, 0.0F);
        setRotation(plater, 0.0F, -1.5707963267948966F, 0.0F);
        
        //shards
        shardrft = new ModelRenderer(this, 16, 32);
        shardrft.setRotationPoint(0.0F, 16.0F, 0.0F);
        shardrft.addBox(2.0F, -13.0F, -5.0F, 3, 7, 3, 0.0F);
        shardlbt = new ModelRenderer(this, 0, 0);
        shardlbt.setRotationPoint(0.0F, 16.0F, 0.0F);
        shardlbt.addBox(-5.0F, -11.0F, 0.0F, 6, 9, 5, 0.0F);
        shardrbt = new ModelRenderer(this, 22, 0);
        shardrbt.setRotationPoint(0.0F, 16.0F, 0.0F);
        shardrbt.addBox(3.0F, -12.0F, 0.0F, 2, 8, 5, 0.0F);
        shardlft = new ModelRenderer(this, 0, 32);
        shardlft.setRotationPoint(0.0F, 16.0F, 0.0F);
        shardlft.addBox(-5.0F, -10.0F, -5.0F, 5, 10, 3, 0.0F);
        
        shardrfb = new ModelRenderer(this, 16, 42);
        shardrfb.setRotationPoint(0.0F, 16.0F, 0.0F);
        shardrfb.addBox(2.0F, -4.0F, -5.0F, 3, 9, 3, 0.0F);
        shardlbb = new ModelRenderer(this, 0, 14);
        shardlbb.setRotationPoint(0.0F, 16.0F, 0.0F);
        shardlbb.addBox(-5.0F, 0.0F, 0.0F, 6, 7, 5, 0.0F);
        shardrbb = new ModelRenderer(this, 22, 13);
        shardrbb.setRotationPoint(0.0F, 16.0F, 0.0F);
        shardrbb.addBox(3.0F, -2.0F, 0.0F, 2, 8, 5, 0.0F);
        shardlfb = new ModelRenderer(this, 0, 45);
        shardlfb.setRotationPoint(0.0F, 16.0F, 0.0F);
        shardlfb.addBox(-5.0F, 2.0F, -5.0F, 5, 6, 3, 0.0F);
	}

	@Override
	public void renderCrystal() {
		float f = 1F / 16F;
		
		shardrft.render(f);
        shardlbt.render(f);
        shardrbt.render(f);
        shardlft.render(f);
        
        shardrfb.render(f);
        shardlbb.render(f);
        shardrbb.render(f);
        shardlfb.render(f);
	}

	@Override
	public void renderRing() {
		float f = 1F / 16F;
		
        platef.render(f);
        plateb.render(f);
        platel.render(f);
        plater.render(f);
	}

	private void setRotation(ModelRenderer model, float x, float y, float z) {
		
		model.rotateAngleX = x;
		model.rotateAngleY = y;
		model.rotateAngleZ = z;
	}
}