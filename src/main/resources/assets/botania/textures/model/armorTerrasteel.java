/*package model;

import net.minecraft.client.model.ModelBase;
import net.minecraft.client.model.ModelRenderer;
import net.minecraft.entity.Entity;

/**
 * armor_terrasteel - wiiv
 * Created using Tabula 4.1.1
 */
 /*
public class armorTerrasteel extends ModelBase {
    public ModelRenderer helm;
    public ModelRenderer body;
    public ModelRenderer armr;
    public ModelRenderer armL;
    public ModelRenderer belt;
    public ModelRenderer bootR;
    public ModelRenderer bootL;
    public ModelRenderer helm2;
    public ModelRenderer helm3;
    public ModelRenderer helm4;
    public ModelRenderer helmLeaf1;
    public ModelRenderer helmLeaf2;
    public ModelRenderer helmLeaf3;
    public ModelRenderer helmLeaf4;
    public ModelRenderer helmLeaf5;
    public ModelRenderer helmLeaf6;
    public ModelRenderer helmbranch1;
    public ModelRenderer helmbranch2;
    public ModelRenderer helmbranch3;
    public ModelRenderer helmbranch4;
    public ModelRenderer body2;
    public ModelRenderer armRpauldron;
    public ModelRenderer armRbranch1;
    public ModelRenderer armRbranch2;
    public ModelRenderer armLpauldron;
    public ModelRenderer armLbranch1;
    public ModelRenderer armLbranch2;
    public ModelRenderer legR;
    public ModelRenderer legL;
    public ModelRenderer bootR1;
    public ModelRenderer bootRbranch;
    public ModelRenderer bootL2;
    public ModelRenderer bootLbranch;

    public armorTerrasteel() {
        this.textureWidth = 64;
        this.textureHeight = 128;
        this.armr = new ModelRenderer(this, 0, 77);
        this.armr.setRotationPoint(-5.0F, 2.0F, -0.0F);
        this.armr.addBox(-3.0F, 3.0F, -2.0F, 4, 7, 4, 0.0F);
        this.setRotateAngle(armr, 0.0F, 0.0F, 0.17453292519943295F);
        this.body = new ModelRenderer(this, 0, 44);
        this.body.setRotationPoint(0.0F, 0.0F, 0.0F);
        this.body.addBox(-4.5F, 0.0F, -3.5F, 9, 6, 6, 0.0F);
        this.setRotateAngle(body, 0.08726646259971647F, 0.0F, 0.0F);
        this.helm4 = new ModelRenderer(this, 56, 32);
        this.helm4.setRotationPoint(0.0F, 0.0F, 0.0F);
        this.helm4.addBox(-1.0F, -7.5F, -6.5F, 2, 6, 2, 0.0F);
        this.setRotateAngle(helm4, -0.17453292519943295F, 0.0F, 0.0F);
        this.bootR1 = new ModelRenderer(this, 32, 77);
        this.bootR1.setRotationPoint(0.0F, 0.0F, 0.0F);
        this.bootR1.addBox(-2.0F, 6.0F, -2.0F, 4, 2, 4, 0.0F);
        this.helmbranch4 = new ModelRenderer(this, 34, 43);
        this.helmbranch4.mirror = true;
        this.helmbranch4.setRotationPoint(0.0F, 0.0F, 0.0F);
        this.helmbranch4.addBox(-2.0F, -8.0F, -4.0F, 1, 2, 7, 0.0F);
        this.setRotateAngle(helmbranch4, 0.2617993877991494F, 0.0F, 1.0471975511965976F);
        this.bootL = new ModelRenderer(this, 32, 83);
        this.bootL.mirror = true;
        this.bootL.setRotationPoint(1.9F, 12.0F, 0.0F);
        this.bootL.addBox(-2.0F, 8.0F, -3.0F, 4, 4, 5, 0.0F);
        this.setRotateAngle(bootL, 0.0F, 0.0F, -0.17453292519943295F);
        this.bootR = new ModelRenderer(this, 32, 83);
        this.bootR.setRotationPoint(-1.9F, 12.0F, 0.1F);
        this.bootR.addBox(-2.0F, 8.0F, -3.0F, 4, 4, 5, 0.0F);
        this.setRotateAngle(bootR, 0.0F, 0.0F, 0.17453292519943295F);
        this.helmLeaf5 = new ModelRenderer(this, 50, 32);
        this.helmLeaf5.mirror = true;
        this.helmLeaf5.setRotationPoint(0.0F, 0.2F, 0.0F);
        this.helmLeaf5.addBox(-1.0F, -11.0F, -4.5F, 2, 5, 1, 0.0F);
        this.setRotateAngle(helmLeaf5, -0.5235987755982988F, -0.5235987755982988F, 0.5235987755982988F);
        this.bootLbranch = new ModelRenderer(this, 48, 77);
        this.bootLbranch.mirror = true;
        this.bootLbranch.setRotationPoint(0.0F, 0.0F, 0.0F);
        this.bootLbranch.addBox(8.0F, 1.0F, -2.0F, 1, 2, 5, 0.0F);
        this.setRotateAngle(bootLbranch, 0.2617993877991494F, 0.0F, 1.0471975511965976F);
        this.helmLeaf4 = new ModelRenderer(this, 50, 32);
        this.helmLeaf4.mirror = true;
        this.helmLeaf4.setRotationPoint(0.0F, 0.2F, 0.0F);
        this.helmLeaf4.addBox(-1.5F, -9.0F, -6.0F, 2, 3, 1, 0.0F);
        this.setRotateAngle(helmLeaf4, -0.2617993877991494F, -0.2617993877991494F, 0.5235987755982988F);
        this.helmLeaf2 = new ModelRenderer(this, 50, 32);
        this.helmLeaf2.setRotationPoint(0.0F, 0.2F, 0.0F);
        this.helmLeaf2.addBox(-1.0F, -11.0F, -4.5F, 2, 5, 1, 0.0F);
        this.setRotateAngle(helmLeaf2, -0.5235987755982988F, 0.5235987755982988F, -0.5235987755982988F);
        this.helm = new ModelRenderer(this, 0, 32);
        this.helm.setRotationPoint(0.0F, 0.0F, 0.0F);
        this.helm.addBox(-4.0F, -8.0F, -4.5F, 8, 3, 9, 0.0F);
        this.setRotateAngle(helm, 0.08726646259971647F, 0.0F, 0.0F);
        this.helm2 = new ModelRenderer(this, 34, 32);
        this.helm2.setRotationPoint(0.0F, 0.0F, 0.0F);
        this.helm2.addBox(-4.0F, -5.0F, -4.5F, 2, 5, 6, 0.0F);
        this.helm3 = new ModelRenderer(this, 34, 32);
        this.helm3.mirror = true;
        this.helm3.setRotationPoint(0.0F, 0.0F, 0.0F);
        this.helm3.addBox(2.0F, -5.0F, -4.5F, 2, 5, 6, 0.0F);
        this.helmbranch1 = new ModelRenderer(this, 34, 43);
        this.helmbranch1.mirror = true;
        this.helmbranch1.setRotationPoint(0.0F, 0.0F, 0.0F);
        this.helmbranch1.addBox(-2.0F, -10.0F, -1.0F, 1, 2, 7, 0.0F);
        this.setRotateAngle(helmbranch1, 0.5235987755982988F, 0.0F, -0.08726646259971647F);
        this.bootL2 = new ModelRenderer(this, 32, 77);
        this.bootL2.mirror = true;
        this.bootL2.setRotationPoint(0.0F, 0.0F, 0.0F);
        this.bootL2.addBox(-2.0F, 6.0F, -2.0F, 4, 2, 4, 0.0F);
        this.helmbranch2 = new ModelRenderer(this, 34, 43);
        this.helmbranch2.setRotationPoint(0.0F, 0.0F, 0.0F);
        this.helmbranch2.addBox(1.0F, -10.0F, -1.0F, 1, 2, 7, 0.0F);
        this.setRotateAngle(helmbranch2, 0.5235987755982988F, 0.0F, 0.08726646259971647F);
        this.legR = new ModelRenderer(this, 16, 77);
        this.legR.setRotationPoint(-1.9F, 12.0F, 0.0F);
        this.legR.addBox(-2.0F, 0.0F, -2.0F, 4, 6, 4, 0.0F);
        this.setRotateAngle(legR, 0.0F, 0.0F, 0.17453292519943295F);
        this.helmLeaf6 = new ModelRenderer(this, 50, 32);
        this.helmLeaf6.mirror = true;
        this.helmLeaf6.setRotationPoint(0.0F, 0.2F, 0.0F);
        this.helmLeaf6.addBox(-0.5F, -13.0F, -3.0F, 2, 7, 1, 0.0F);
        this.setRotateAngle(helmLeaf6, -0.7853981633974483F, -0.7853981633974483F, 0.7853981633974483F);
        this.armLbranch1 = new ModelRenderer(this, 51, 44);
        this.armLbranch1.mirror = true;
        this.armLbranch1.setRotationPoint(0.0F, 0.0F, -0.0F);
        this.armLbranch1.addBox(2.5F, -5.0F, -1.0F, 1, 5, 2, 0.0F);
        this.setRotateAngle(armLbranch1, 0.0F, 0.0F, 0.7853981633974483F);
        this.bootRbranch = new ModelRenderer(this, 48, 77);
        this.bootRbranch.setRotationPoint(0.0F, 0.0F, 0.0F);
        this.bootRbranch.addBox(-9.0F, 1.0F, -2.0F, 1, 2, 5, 0.0F);
        this.setRotateAngle(bootRbranch, 0.2617993877991494F, 0.0F, -1.0471975511965976F);
        this.armRbranch2 = new ModelRenderer(this, 50, 43);
        this.armRbranch2.setRotationPoint(0.0F, 0.0F, 0.0F);
        this.armRbranch2.addBox(-1.5F, -5.0F, -1.5F, 1, 5, 3, 0.0F);
        this.setRotateAngle(armRbranch2, 0.0F, 0.0F, -0.5235987755982988F);
        this.helmLeaf3 = new ModelRenderer(this, 50, 32);
        this.helmLeaf3.setRotationPoint(0.0F, 0.2F, 0.0F);
        this.helmLeaf3.addBox(-1.5F, -13.0F, -3.0F, 2, 7, 1, 0.0F);
        this.setRotateAngle(helmLeaf3, -0.7853981633974483F, 0.7853981633974483F, -0.7853981633974483F);
        this.armRpauldron = new ModelRenderer(this, 0, 66);
        this.armRpauldron.setRotationPoint(0.0F, 0.0F, 0.0F);
        this.armRpauldron.addBox(-4.0F, -2.0F, -3.0F, 5, 5, 6, 0.0F);
        this.armLbranch2 = new ModelRenderer(this, 50, 43);
        this.armLbranch2.mirror = true;
        this.armLbranch2.setRotationPoint(0.0F, 0.0F, -0.0F);
        this.armLbranch2.addBox(0.5F, -5.0F, -1.5F, 1, 5, 3, 0.0F);
        this.setRotateAngle(armLbranch2, 0.0F, 0.0F, 0.5235987755982988F);
        this.armL = new ModelRenderer(this, 0, 77);
        this.armL.mirror = true;
        this.armL.setRotationPoint(5.0F, 2.0F, -0.0F);
        this.armL.addBox(-1.0F, 3.0F, -2.0F, 4, 7, 4, 0.0F);
        this.setRotateAngle(armL, 0.0F, 0.0F, -0.17453292519943295F);
        this.body2 = new ModelRenderer(this, 0, 57);
        this.body2.setRotationPoint(0.0F, 0.0F, 0.0F);
        this.body2.addBox(-4.0F, 6.0F, -2.5F, 8, 4, 5, 0.0F);
        this.setRotateAngle(body2, -0.08726646259971647F, 0.0F, 0.0F);
        this.helmbranch3 = new ModelRenderer(this, 34, 43);
        this.helmbranch3.setRotationPoint(0.0F, 0.0F, 0.0F);
        this.helmbranch3.addBox(1.0F, -8.0F, -4.0F, 1, 2, 7, 0.0F);
        this.setRotateAngle(helmbranch3, 0.2617993877991494F, 0.0F, -1.0471975511965976F);
        this.armRbranch1 = new ModelRenderer(this, 51, 44);
        this.armRbranch1.setRotationPoint(0.0F, 0.0F, 0.0F);
        this.armRbranch1.addBox(-3.5F, -5.0F, -1.0F, 1, 5, 2, 0.0F);
        this.setRotateAngle(armRbranch1, 0.0F, 0.0F, -0.7853981633974483F);
        this.belt = new ModelRenderer(this, 22, 66);
        this.belt.setRotationPoint(0.0F, 0.0F, 0.0F);
        this.belt.addBox(-4.5F, 9.5F, -3.0F, 9, 3, 6, 0.0F);
        this.helmLeaf1 = new ModelRenderer(this, 50, 32);
        this.helmLeaf1.setRotationPoint(0.0F, 0.2F, 0.0F);
        this.helmLeaf1.addBox(-0.5F, -9.0F, -6.0F, 2, 3, 1, 0.0F);
        this.setRotateAngle(helmLeaf1, -0.2617993877991494F, 0.2617993877991494F, -0.5235987755982988F);
        this.armLpauldron = new ModelRenderer(this, 0, 66);
        this.armLpauldron.mirror = true;
        this.armLpauldron.setRotationPoint(0.0F, 0.0F, -0.0F);
        this.armLpauldron.addBox(-1.0F, -2.0F, -3.0F, 5, 5, 6, 0.0F);
        this.legL = new ModelRenderer(this, 16, 77);
        this.legL.mirror = true;
        this.legL.setRotationPoint(1.9F, 12.0F, 0.0F);
        this.legL.addBox(-2.0F, 0.0F, -2.0F, 4, 6, 4, 0.0F);
        this.setRotateAngle(legL, 0.0F, 0.0F, -0.17453292519943295F);
        this.helm.addChild(this.helm4);
        this.bootR.addChild(this.bootR1);
        this.helm.addChild(this.helmbranch4);
        this.helm.addChild(this.helmLeaf5);
        this.bootL.addChild(this.bootLbranch);
        this.helm.addChild(this.helmLeaf4);
        this.helm.addChild(this.helmLeaf2);
        this.helm.addChild(this.helm2);
        this.helm.addChild(this.helm3);
        this.helm.addChild(this.helmbranch1);
        this.bootL.addChild(this.bootL2);
        this.helm.addChild(this.helmbranch2);
        this.belt.addChild(this.legR);
        this.helm.addChild(this.helmLeaf6);
        this.armLpauldron.addChild(this.armLbranch1);
        this.bootR.addChild(this.bootRbranch);
        this.armRpauldron.addChild(this.armRbranch2);
        this.helm.addChild(this.helmLeaf3);
        this.armr.addChild(this.armRpauldron);
        this.armLpauldron.addChild(this.armLbranch2);
        this.body.addChild(this.body2);
        this.helm.addChild(this.helmbranch3);
        this.armRpauldron.addChild(this.armRbranch1);
        this.helm.addChild(this.helmLeaf1);
        this.armL.addChild(this.armLpauldron);
        this.belt.addChild(this.legL);
    }

    @Override
    public void render(Entity entity, float f, float f1, float f2, float f3, float f4, float f5) { 
        this.armr.render(f5);
        this.body.render(f5);
        this.bootL.render(f5);
        this.bootR.render(f5);
        this.helm.render(f5);
        this.armL.render(f5);
        this.belt.render(f5);
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