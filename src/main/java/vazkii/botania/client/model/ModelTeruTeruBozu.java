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

public class ModelTeruTeruBozu extends ModelBase {
	
    public ModelRenderer thread;
    public ModelRenderer cloth;
    public ModelRenderer happyFace;
    public ModelRenderer sadFace;

    public ModelTeruTeruBozu() {
        this.textureWidth = 64;
        this.textureHeight = 32;
        this.sadFace = new ModelRenderer(this, 32, 0);
        this.sadFace.setRotationPoint(0.0F, 14.5F, 0.0F);
        this.sadFace.addBox(-4.0F, -6.0F, -4.0F, 8, 8, 8, 0.0F);
        this.setRotateAngle(sadFace, 0.17453292519943295F, 0.0F, 0.0F);
        this.happyFace = new ModelRenderer(this, 0, 0);
        this.happyFace.setRotationPoint(0.0F, 14.5F, 0.0F);
        this.happyFace.addBox(-4.0F, -6.0F, -4.0F, 8, 8, 8, 0.0F);
        this.setRotateAngle(happyFace, -0.17453292519943295F, 0.0F, 0.0F);
        this.thread = new ModelRenderer(this, 32, 16);
        this.thread.setRotationPoint(0.0F, 14.0F, 0.0F);
        this.thread.addBox(-3.0F, 2.0F, -3.0F, 6, 1, 6, 0.0F);
        this.cloth = new ModelRenderer(this, 0, 16);
        this.cloth.setRotationPoint(0.0F, 21.5F, -1.0F);
        this.cloth.addBox(-4.0F, -4.0F, -4.0F, 8, 8, 8, 0.0F);
        this.setRotateAngle(cloth, 0.7853981633974483F, 2.2689280275926285F, 1.5707963267948966F);
    }

    @Override
    public void render(Entity entity, float f, float f1, float f2, float f3, float f4, float f5) { 
        this.sadFace.render(f5);
        this.happyFace.render(f5);
        this.thread.render(f5);
        this.cloth.render(f5);
    }

    public void setRotateAngle(ModelRenderer modelRenderer, float x, float y, float z) {
        modelRenderer.rotateAngleX = x;
        modelRenderer.rotateAngleY = y;
        modelRenderer.rotateAngleZ = z;
    }
}
