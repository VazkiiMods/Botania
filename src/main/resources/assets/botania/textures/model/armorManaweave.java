/*package model;

import net.minecraft.client.model.ModelBase;
import net.minecraft.client.model.ModelRenderer;
import net.minecraft.entity.Entity;

/**
 * armor_manaweave - wiiv
 * Created using Tabula 4.1.1
 */
 /*
public class armorManaweave extends ModelBase {
    public ModelRenderer helm;
    public ModelRenderer body;
    public ModelRenderer armR;
    public ModelRenderer armL;
    public ModelRenderer legR;
    public ModelRenderer legL;
    public ModelRenderer bootR;
    public ModelRenderer bootL;
    public ModelRenderer helm2;
    public ModelRenderer helm3;
    public ModelRenderer helm4;
    public ModelRenderer helmSeam1;
    public ModelRenderer helmSeam2;
    public ModelRenderer helmSeam3;
    public ModelRenderer helmSeam4;
    public ModelRenderer body2;
    public ModelRenderer armRpauldron;
    public ModelRenderer armLpauldron;
    public ModelRenderer skirtR;
    public ModelRenderer skirtL;

    public armorManaweave() {
        this.textureWidth = 64;
        this.textureHeight = 128;
        this.helmSeam3 = new ModelRenderer(this, 26, 61);
        this.helmSeam3.setRotationPoint(0.0F, 0.0F, 0.0F);
        this.helmSeam3.addBox(-0.5F, -9.5F, 5.0F, 1, 11, 1, 0.0F);
        this.helmSeam4 = new ModelRenderer(this, 39, 64);
        this.helmSeam4.setRotationPoint(0.0F, 0.0F, 0.0F);
        this.helmSeam4.addBox(-0.5F, 0.5F, -1.0F, 1, 1, 6, 0.0F);
        this.skirtL = new ModelRenderer(this, 0, 83);
        this.skirtL.mirror = true;
        this.skirtL.setRotationPoint(0.0F, 0.0F, 0.0F);
        this.skirtL.addBox(-2.0F, -2.0F, -3.5F, 5, 13, 7, 0.0F);
        this.setRotateAngle(skirtL, 0.0F, -0.17453292519943295F, -0.2617993877991494F);
        this.armR = new ModelRenderer(this, 24, 83);
        this.armR.setRotationPoint(-5.0F, 2.0F, 0.0F);
        this.armR.addBox(-3.0F, -1.5F, -2.5F, 4, 10, 5, 0.0F);
        this.setRotateAngle(armR, 0.0F, 0.0F, 0.17453292519943295F);
        this.armL = new ModelRenderer(this, 24, 83);
        this.armL.mirror = true;
        this.armL.setRotationPoint(5.0F, 2.0F, -0.0F);
        this.armL.addBox(-1.0F, -1.5F, -2.5F, 4, 10, 5, 0.0F);
        this.setRotateAngle(armL, 0.0F, 0.0F, -0.17453292519943295F);
        this.bootR = new ModelRenderer(this, 0, 103);
        this.bootR.setRotationPoint(-2.0F, 12.0F, 0.0F);
        this.bootR.addBox(-2.0F, 8.0F, -3.0F, 4, 4, 5, 0.0F);
        this.setRotateAngle(bootR, 0.0F, 0.0F, 0.08726646259971647F);
        this.legR = new ModelRenderer(this, 42, 81);
        this.legR.setRotationPoint(-2.0F, 12.0F, 0.0F);
        this.legR.addBox(-2.0F, 0.0F, -2.0F, 4, 6, 4, 0.0F);
        this.setRotateAngle(legR, 0.0F, 0.0F, 0.08726646259971647F);
        this.helm2 = new ModelRenderer(this, 38, 42);
        this.helm2.setRotationPoint(0.0F, 0.0F, 0.0F);
        this.helm2.addBox(-4.49F, -4.0F, -2.5F, 1, 5, 8, 0.0F);
        this.skirtR = new ModelRenderer(this, 0, 83);
        this.skirtR.setRotationPoint(0.0F, 0.0F, 0.0F);
        this.skirtR.addBox(-3.0F, -2.0F, -3.5F, 5, 13, 7, 0.0F);
        this.setRotateAngle(skirtR, 0.0F, 0.17453292519943295F, 0.2617993877991494F);
        this.armRpauldron = new ModelRenderer(this, 0, 72);
        this.armRpauldron.setRotationPoint(0.0F, 0.0F, 0.0F);
        this.armRpauldron.addBox(-4.0F, -2.0F, -3.0F, 3, 5, 6, 0.0F);
        this.setRotateAngle(armRpauldron, 0.0F, 0.0F, 0.17453292519943295F);
        this.bootL = new ModelRenderer(this, 0, 103);
        this.bootL.setRotationPoint(2.0F, 12.0F, 0.0F);
        this.bootL.addBox(-2.0F, 8.0F, -3.0F, 4, 4, 5, 0.0F);
        this.setRotateAngle(bootL, 0.0F, 0.0F, -0.08726646259971647F);
        this.helmSeam1 = new ModelRenderer(this, 30, 61);
        this.helmSeam1.setRotationPoint(0.0F, 0.0F, 0.0F);
        this.helmSeam1.addBox(-0.5F, -10.5F, -5.0F, 1, 7, 2, 0.0F);
        this.helm = new ModelRenderer(this, 0, 32);
        this.helm.setRotationPoint(0.0F, 0.0F, 0.0F);
        this.helm.addBox(-4.5F, -10.0F, -4.5F, 9, 6, 10, 0.0F);
        this.setRotateAngle(helm, 0.08726646259971647F, 0.0F, 0.0F);
        this.armLpauldron = new ModelRenderer(this, 0, 72);
        this.armLpauldron.mirror = true;
        this.armLpauldron.setRotationPoint(0.0F, 0.0F, 0.0F);
        this.armLpauldron.addBox(1.0F, -2.0F, -3.0F, 3, 5, 6, 0.0F);
        this.setRotateAngle(armLpauldron, 0.0F, 0.0F, -0.17453292519943295F);
        this.helm4 = new ModelRenderer(this, 38, 32);
        this.helm4.setRotationPoint(0.0F, 0.0F, 0.0F);
        this.helm4.addBox(-3.5F, -4.0F, 0.49F, 7, 5, 5, 0.0F);
        this.helmSeam2 = new ModelRenderer(this, 36, 61);
        this.helmSeam2.setRotationPoint(0.0F, 0.0F, 0.0F);
        this.helmSeam2.addBox(-0.5F, -10.5F, -3.0F, 1, 1, 9, 0.0F);
        this.legL = new ModelRenderer(this, 42, 81);
        this.legL.mirror = true;
        this.legL.setRotationPoint(2.0F, 12.0F, 0.0F);
        this.legL.addBox(-2.0F, 0.0F, -2.0F, 4, 6, 4, 0.0F);
        this.setRotateAngle(legL, 0.0F, 0.0F, -0.08726646259971647F);
        this.body2 = new ModelRenderer(this, 0, 61);
        this.body2.setRotationPoint(0.0F, 0.0F, 0.0F);
        this.body2.addBox(-4.0F, 6.0F, -2.5F, 8, 6, 5, 0.0F);
        this.body = new ModelRenderer(this, 0, 48);
        this.body.setRotationPoint(0.0F, 0.0F, 0.0F);
        this.body.addBox(-4.5F, 0.0F, -3.0F, 9, 7, 6, 0.0F);
        this.helm3 = new ModelRenderer(this, 38, 42);
        this.helm3.mirror = true;
        this.helm3.setRotationPoint(0.0F, 0.0F, 0.0F);
        this.helm3.addBox(3.49F, -4.0F, -2.5F, 1, 5, 8, 0.0F);
        this.helm.addChild(this.helmSeam3);
        this.helm.addChild(this.helmSeam4);
        this.legL.addChild(this.skirtL);
        this.helm.addChild(this.helm2);
        this.legR.addChild(this.skirtR);
        this.armR.addChild(this.armRpauldron);
        this.helm.addChild(this.helmSeam1);
        this.armL.addChild(this.armLpauldron);
        this.helm.addChild(this.helm4);
        this.helm.addChild(this.helmSeam2);
        this.body.addChild(this.body2);
        this.helm.addChild(this.helm3);
    }

    @Override
    public void render(Entity entity, float f, float f1, float f2, float f3, float f4, float f5) { 
        this.armR.render(f5);
        this.armL.render(f5);
        this.bootR.render(f5);
        this.legR.render(f5);
        this.bootL.render(f5);
        this.helm.render(f5);
        this.legL.render(f5);
        this.body.render(f5);
    }

    /**
     * This is a helper function from Tabula to set the rotation of model parts
     */
	 /*
    public void setRotateAngle(ModelRenderer modelRenderer, float x, float y, float z) {
        modelRenderer.rotateAngleX = x;
        modelRenderer.rotateAngleY = y;
        modelRenderer.rotateAngleZ = z;
    }
}
*/