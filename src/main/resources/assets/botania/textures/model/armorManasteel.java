/*package model;

import net.minecraft.client.model.ModelBase;
import net.minecraft.client.model.ModelRenderer;
import net.minecraft.entity.Entity;

/**
 * armor_manasteel - wiiv
 * Created using Tabula 4.1.1
 */
 /*
public class armorManasteel extends ModelBase {
    public ModelRenderer helm;
    public ModelRenderer body;
    public ModelRenderer armR;
    public ModelRenderer armL;
    public ModelRenderer belt;
    public ModelRenderer bootR;
    public ModelRenderer bootL;
    public ModelRenderer helm1;
    public ModelRenderer helm2;
    public ModelRenderer helm3;
    public ModelRenderer helm4;
    public ModelRenderer helm5;
    public ModelRenderer helm6;
    public ModelRenderer helm7;
    public ModelRenderer body2;
    public ModelRenderer armRpauldron;
    public ModelRenderer armLpauldron;
    public ModelRenderer legR;
    public ModelRenderer legL;

    public armorManasteel() {
        this.textureWidth = 64;
        this.textureHeight = 128;
        this.armRpauldron = new ModelRenderer(this, 30, 47);
        this.armRpauldron.setRotationPoint(0.0F, 0.0F, 0.0F);
        this.armRpauldron.addBox(-4.0F, -2.0F, -2.5F, 4, 4, 5, 0.0F);
        this.armL = new ModelRenderer(this, 0, 68);
        this.armL.mirror = true;
        this.armL.setRotationPoint(5.0F, 2.0F, -0.0F);
        this.armL.addBox(1.0F, 3.0F, -2.0F, 2, 6, 4, 0.0F);
        this.setRotateAngle(armL, 0.0F, 0.0F, -0.17453292519943295F);
        this.legR = new ModelRenderer(this, 12, 68);
        this.legR.setRotationPoint(-1.9F, 12.0F, 0.0F);
        this.legR.addBox(-2.0F, 0.0F, -2.0F, 4, 8, 4, 0.0F);
        this.setRotateAngle(legR, 0.0F, 0.0F, 0.17453292519943295F);
        this.helm3 = new ModelRenderer(this, 24, 32);
        this.helm3.setRotationPoint(0.0F, 0.0F, 0.0F);
        this.helm3.addBox(-1.0F, -8.5F, -6.5F, 2, 5, 1, 0.0F);
        this.setRotateAngle(helm3, -0.17453292519943295F, 0.0F, 0.0F);
        this.helm7 = new ModelRenderer(this, 24, 32);
        this.helm7.setRotationPoint(0.0F, 0.0F, 0.0F);
        this.helm7.addBox(-1.0F, -8.5F, -6.0F, 2, 3, 1, 0.0F);
        this.setRotateAngle(helm7, -0.3490658503988659F, 0.0F, 0.0F);
        this.bootL = new ModelRenderer(this, 28, 68);
        this.bootL.mirror = true;
        this.bootL.setRotationPoint(2.0F, 12.0F, 0.0F);
        this.bootL.addBox(-2.0F, 8.0F, -3.0F, 4, 4, 5, 0.0F);
        this.setRotateAngle(bootL, 0.0F, 0.0F, -0.17453292519943295F);
        this.helm4 = new ModelRenderer(this, 0, 39);
        this.helm4.setRotationPoint(0.0F, 0.0F, 0.0F);
        this.helm4.addBox(-4.0F, -8.0F, -0.5F, 1, 3, 5, 0.0F);
        this.bootR = new ModelRenderer(this, 28, 68);
        this.bootR.setRotationPoint(-2.0F, 12.0F, 0.0F);
        this.bootR.addBox(-2.0F, 8.0F, -3.0F, 4, 4, 5, 0.0F);
        this.setRotateAngle(bootR, 0.0F, 0.0F, 0.17453292519943295F);
        this.legL = new ModelRenderer(this, 12, 68);
        this.legL.mirror = true;
        this.legL.setRotationPoint(1.9F, 12.0F, 0.0F);
        this.legL.addBox(-2.0F, 0.0F, -2.0F, 4, 8, 4, 0.0F);
        this.setRotateAngle(legL, 0.0F, 0.0F, -0.17453292519943295F);
        this.armR = new ModelRenderer(this, 0, 68);
        this.armR.setRotationPoint(-5.0F, 2.0F, 0.0F);
        this.armR.addBox(-3.0F, 3.0F, -2.0F, 2, 6, 4, 0.0F);
        this.setRotateAngle(armR, 0.0F, 0.0F, 0.17453292519943295F);
        this.helm1 = new ModelRenderer(this, 12, 39);
        this.helm1.setRotationPoint(0.0F, 0.0F, 0.0F);
        this.helm1.addBox(-4.0F, -5.0F, -4.5F, 1, 3, 4, 0.0F);
        this.helm2 = new ModelRenderer(this, 12, 39);
        this.helm2.mirror = true;
        this.helm2.setRotationPoint(0.0F, 0.0F, 0.0F);
        this.helm2.addBox(3.0F, -5.0F, -4.5F, 1, 3, 4, 0.0F);
        this.body2 = new ModelRenderer(this, 0, 59);
        this.body2.setRotationPoint(0.0F, 0.0F, 0.0F);
        this.body2.addBox(-4.0F, 6.0F, -2.5F, 8, 4, 5, 0.0F);
        this.setRotateAngle(body2, -0.08726646259971647F, 0.0F, 0.0F);
        this.body = new ModelRenderer(this, 0, 47);
        this.body.setRotationPoint(0.0F, 0.0F, 0.0F);
        this.body.addBox(-4.5F, 0.0F, -3.5F, 9, 6, 6, 0.0F);
        this.setRotateAngle(body, 0.08726646259971647F, 0.0F, 0.0F);
        this.helm6 = new ModelRenderer(this, 24, 32);
        this.helm6.setRotationPoint(0.0F, 0.0F, 0.0F);
        this.helm6.addBox(-1.0F, -8.5F, -5.5F, 2, 3, 1, 0.0F);
        this.setRotateAngle(helm6, -0.5235987755982988F, 0.0F, 0.0F);
        this.belt = new ModelRenderer(this, 26, 59);
        this.belt.setRotationPoint(0.0F, 0.0F, 0.0F);
        this.belt.addBox(-4.5F, 9.5F, -3.0F, 9, 3, 6, 0.0F);
        this.helm = new ModelRenderer(this, 0, 32);
        this.helm.setRotationPoint(0.0F, 0.0F, 0.0F);
        this.helm.addBox(-4.0F, -8.0F, -4.5F, 8, 3, 4, 0.0F);
        this.setRotateAngle(helm, 0.08726646259971647F, 0.0F, 0.0F);
        this.armLpauldron = new ModelRenderer(this, 30, 47);
        this.armLpauldron.mirror = true;
        this.armLpauldron.setRotationPoint(0.0F, 0.0F, -0.0F);
        this.armLpauldron.addBox(0.0F, -2.0F, -2.5F, 4, 4, 5, 0.0F);
        this.helm5 = new ModelRenderer(this, 0, 39);
        this.helm5.mirror = true;
        this.helm5.setRotationPoint(0.0F, 0.0F, 0.0F);
        this.helm5.addBox(3.0F, -8.0F, -0.5F, 1, 3, 5, 0.0F);
        this.armR.addChild(this.armRpauldron);
        this.belt.addChild(this.legR);
        this.helm.addChild(this.helm3);
        this.helm.addChild(this.helm7);
        this.helm.addChild(this.helm4);
        this.belt.addChild(this.legL);
        this.helm.addChild(this.helm1);
        this.helm.addChild(this.helm2);
        this.body.addChild(this.body2);
        this.helm.addChild(this.helm6);
        this.armL.addChild(this.armLpauldron);
        this.helm.addChild(this.helm5);
    }

    @Override
    public void render(Entity entity, float f, float f1, float f2, float f3, float f4, float f5) { 
        this.armL.render(f5);
        this.bootL.render(f5);
        this.bootR.render(f5);
        this.armR.render(f5);
        this.body.render(f5);
        this.belt.render(f5);
        this.helm.render(f5);
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