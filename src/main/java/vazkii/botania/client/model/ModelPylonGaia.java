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

import net.minecraft.client.renderer.entity.model.RendererModel;
import net.minecraft.client.renderer.model.Model;
import net.minecraft.client.renderer.entity.model.RendererModel;
import net.minecraft.client.renderer.model.Model;

public class ModelPylonGaia extends Model implements IPylonModel {

	private final RendererModel platef;
    private final RendererModel plateb;
    private final RendererModel platel;
    private final RendererModel plater;
	
    private final RendererModel shardlbt;
    private final RendererModel shardrbt;
    private final RendererModel shardlft;
    private final RendererModel shardrft;
    
    private final RendererModel shardlbb;
    private final RendererModel shardrbb;
    private final RendererModel shardlfb;
    private final RendererModel shardrfb;
    
	public ModelPylonGaia() {
		
		textureWidth = 64;
		textureHeight = 64;

		//plates
		platel = new RendererModel(this, 36, 0);
        platel.setRotationPoint(0.0F, 16.0F, 0.0F);
        platel.addBox(-3.0F, -3.0F, -8.0F, 6, 6, 2, 0.0F);
        setRotation(platel, 0.0F, 1.5707963267948966F, 0.0F);
        plater = new RendererModel(this, 36, 0);
        plater.setRotationPoint(0.0F, 16.0F, 0.0F);
        plater.addBox(-3.0F, -3.0F, -8.0F, 6, 6, 2, 0.0F);
        setRotation(plater, 0.0F, -1.5707963267948966F, 0.0F);
        platef = new RendererModel(this, 36, 0);
        platef.setRotationPoint(0.0F, 16.0F, 0.0F);
        platef.addBox(-3.0F, -3.0F, -8.0F, 6, 6, 2, 0.0F);
        plateb = new RendererModel(this, 36, 0);
        plateb.setRotationPoint(0.0F, 16.0F, 0.0F);
        plateb.addBox(-3.0F, -3.0F, -8.0F, 6, 6, 2, 0.0F);
        setRotation(plateb, 0.0F, 3.141592653589793F, 0.0F);
        
        //shards
        shardrft = new RendererModel(this, 16, 32);
        shardrft.setRotationPoint(0.0F, 16.0F, 0.0F);
        shardrft.addBox(2.0F, -6.0F, -5.0F, 3, 2, 3, 0.0F);
        shardlbt = new RendererModel(this, 0, 0);
        shardlbt.setRotationPoint(0.0F, 16.0F, 0.0F);
        shardlbt.addBox(-5.0F, -6.0F, 0.0F, 6, 5, 5, 0.0F);
        shardrbt = new RendererModel(this, 22, 9);
        shardrbt.setRotationPoint(0.0F, 16.0F, 0.0F);
        shardrbt.addBox(3.0F, -5.0F, 0.0F, 2, 4, 5, 0.0F);
        shardlft = new RendererModel(this, 0, 32);
        shardlft.setRotationPoint(0.0F, 16.0F, 0.0F);
        shardlft.addBox(-5.0F, -7.0F, -5.0F, 5, 6, 3, 0.0F);
        
        shardrfb = new RendererModel(this, 16, 37);
        shardrfb.setRotationPoint(0.0F, 16.0F, 0.0F);
        shardrfb.addBox(2.0F, -2.0F, -5.0F, 3, 6, 3, 0.0F);
        shardlbb = new RendererModel(this, 0, 10);
        shardlbb.setRotationPoint(0.0F, 16.0F, 0.0F);
        shardlbb.addBox(-5.0F, 1.0F, 0.0F, 6, 3, 5, 0.0F);
        shardrbb = new RendererModel(this, 22, 0);
        shardrbb.setRotationPoint(0.0F, 16.0F, 0.0F);
        shardrbb.addBox(3.0F, 1.0F, 0.0F, 2, 4, 5, 0.0F);
        shardlfb = new RendererModel(this, 0, 41);
        shardlfb.setRotationPoint(0.0F, 16.0F, 0.0F);
        shardlfb.addBox(-5.0F, 1.0F, -5.0F, 5, 2, 3, 0.0F);
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

	private void setRotation(RendererModel model, float x, float y, float z) {
		
		model.rotateAngleX = x;
		model.rotateAngleY = y;
		model.rotateAngleZ = z;
	}
}