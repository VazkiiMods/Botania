/*package model;

import net.minecraft.client.model.ModelBase;
import net.minecraft.client.model.ModelRenderer;
import net.minecraft.entity.Entity;

/**
 * armor_elementium - wiiv
 * Created using Tabula 4.1.1
 */
 /*
public class armorElementium extends ModelBase {
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
    public ModelRenderer fairy;
    public ModelRenderer helmWing1;
    public ModelRenderer helmWing2;
    public ModelRenderer helmWing3;
    public ModelRenderer helmWing4;
    public ModelRenderer body2;
    public ModelRenderer armRpauldron;
    public ModelRenderer wing1;
    public ModelRenderer wing2;
    public ModelRenderer armLpauldron;
    public ModelRenderer wing1_1;
    public ModelRenderer wing2_1;
    public ModelRenderer legR;
    public ModelRenderer legL;
    public ModelRenderer bootR1;
    public ModelRenderer wing1_2;
    public ModelRenderer wing2_2;
    public ModelRenderer bootL1;
    public ModelRenderer wing1_3;
    public ModelRenderer wing2_3;

    public armorElementium() {
        this.textureWidth = 64;
        this.textureHeight = 128;
        this.fairy = new ModelRenderer(this, 34, 32);
        this.fairy.setRotationPoint(0.0F, 0.0F, 0.0F);
        this.fairy.addBox(-2.0F, -8.5F, -7.0F, 4, 4, 4, 0.0F);
        this.setRotateAngle(fairy, -0.17453292519943295F, 0.0F, 0.0F);
        this.helm3 = new ModelRenderer(this, 0, 32);
        this.helm3.setRotationPoint(0.0F, 0.0F, 0.0F);
        this.helm3.addBox(-1.0F, -5.5F, -5.5F, 2, 3, 1, 0.0F);
        this.setRotateAngle(helm3, -0.17453292519943295F, 0.0F, 0.0F);
        this.wing1_2 = new ModelRenderer(this, 56, 43);
        this.wing1_2.mirror = true;
        this.wing1_2.setRotationPoint(-2.5F, 9.0F, 0.0F);
        this.wing1_2.addBox(0.5F, -2.0F, 0.0F, 0, 2, 3, 0.0F);
        this.setRotateAngle(wing1_2, 0.2617993877991494F, -0.7853981633974483F, -0.2617993877991494F);
        this.helm1 = new ModelRenderer(this, 50, 32);
        this.helm1.setRotationPoint(0.0F, 0.0F, 0.0F);
        this.helm1.addBox(-4.0F, -5.0F, -4.5F, 1, 5, 4, 0.0F);
        this.legL = new ModelRenderer(this, 12, 79);
        this.legL.mirror = true;
        this.legL.setRotationPoint(1.9F, 12.0F, 0.0F);
        this.legL.addBox(-2.0F, 0.0F, -2.0F, 4, 6, 4, 0.0F);
        this.setRotateAngle(legL, 0.0F, 0.0F, -0.17453292519943295F);
        this.armL = new ModelRenderer(this, 0, 79);
        this.armL.mirror = true;
        this.armL.setRotationPoint(5.0F, 2.0F, -0.0F);
        this.armL.addBox(1.5F, 6.0F, -2.0F, 2, 4, 4, 0.2F);
        this.setRotateAngle(armL, 0.0F, 0.0F, -0.17453292519943295F);
        this.armRpauldron = new ModelRenderer(this, 0, 67);
        this.armRpauldron.setRotationPoint(0.0F, 0.0F, 0.0F);
        this.armRpauldron.addBox(-4.0F, -2.5F, -3.0F, 5, 6, 6, 0.0F);
        this.legR = new ModelRenderer(this, 12, 79);
        this.legR.setRotationPoint(-1.9F, 12.0F, 0.0F);
        this.legR.addBox(-2.0F, 0.0F, -2.0F, 4, 6, 4, 0.0F);
        this.setRotateAngle(legR, 0.0F, 0.0F, 0.17453292519943295F);
        this.helmWing2 = new ModelRenderer(this, 46, 45);
        this.helmWing2.mirror = true;
        this.helmWing2.setRotationPoint(-4.0F, -4.0F, -1.0F);
        this.helmWing2.addBox(-0.5F, 0.0F, 0.0F, 1, 3, 4, 0.0F);
        this.setRotateAngle(helmWing2, -0.2617993877991494F, -0.2617993877991494F, 0.2617993877991494F);
        this.bootL1 = new ModelRenderer(this, 12, 79);
        this.bootL1.mirror = true;
        this.bootL1.setRotationPoint(0.0F, 0.0F, 0.0F);
        this.bootL1.addBox(-2.0F, 7.0F, -2.0F, 4, 1, 4, 0.0F);
        this.armR = new ModelRenderer(this, 0, 79);
        this.armR.setRotationPoint(-5.0F, 2.0F, 0.0F);
        this.armR.addBox(-3.5F, 6.0F, -2.0F, 2, 4, 4, 0.2F);
        this.setRotateAngle(armR, 0.0F, 0.0F, 0.17453292519943295F);
        this.bootR1 = new ModelRenderer(this, 12, 79);
        this.bootR1.setRotationPoint(0.0F, 0.0F, 0.0F);
        this.bootR1.addBox(-2.0F, 7.0F, -2.0F, 4, 1, 4, 0.0F);
        this.bootR = new ModelRenderer(this, 12, 79);
        this.bootR.setRotationPoint(-1.9F, 12.0F, 0.0F);
        this.bootR.addBox(-2.0F, 8.0F, -3.0F, 4, 4, 5, 0.0F);
        this.setRotateAngle(bootR, 0.0F, 0.0F, 0.17453292519943295F);
        this.wing2_1 = new ModelRenderer(this, 56, 42);
        this.wing2_1.setRotationPoint(4.5F, 0.0F, 0.0F);
        this.wing2_1.addBox(0.0F, 0.0F, -0.5F, 0, 2, 3, 0.0F);
        this.setRotateAngle(wing2_1, 0.08726646259971647F, 0.7853981633974483F, 0.2617993877991494F);
        this.wing2_2 = new ModelRenderer(this, 56, 44);
        this.wing2_2.mirror = true;
        this.wing2_2.setRotationPoint(-2.5F, 9.0F, 0.0F);
        this.wing2_2.addBox(0.5F, 0.0F, 0.0F, 0, 1, 2, 0.0F);
        this.setRotateAngle(wing2_2, 0.08726646259971647F, -0.7853981633974483F, -0.2617993877991494F);
        this.bootL = new ModelRenderer(this, 12, 79);
        this.bootL.mirror = true;
        this.bootL.setRotationPoint(1.9F, 12.0F, 0.0F);
        this.bootL.addBox(-2.0F, 8.0F, -3.0F, 4, 4, 5, 0.0F);
        this.setRotateAngle(bootL, 0.0F, 0.0F, -0.17453292519943295F);
        this.body = new ModelRenderer(this, 0, 44);
        this.body.setRotationPoint(0.0F, 0.0F, 0.0F);
        this.body.addBox(-4.5F, 0.0F, -4.0F, 9, 5, 7, 0.0F);
        this.setRotateAngle(body, 0.08726646259971647F, 0.0F, 0.0F);
        this.belt = new ModelRenderer(this, 22, 56);
        this.belt.setRotationPoint(0.0F, 0.0F, 0.0F);
        this.belt.addBox(-4.5F, 9.5F, -3.0F, 9, 3, 5, 0.1F);
        this.helm = new ModelRenderer(this, 0, 32);
        this.helm.setRotationPoint(0.0F, 0.0F, 0.0F);
        this.helm.addBox(-4.0F, -8.0F, -4.5F, 8, 3, 9, 0.0F);
        this.setRotateAngle(helm, 0.08726646259971647F, 0.0F, 0.0F);
        this.helmWing4 = new ModelRenderer(this, 46, 45);
        this.helmWing4.setRotationPoint(4.0F, -4.0F, -1.0F);
        this.helmWing4.addBox(-0.5F, 0.0F, 0.0F, 1, 3, 4, 0.0F);
        this.setRotateAngle(helmWing4, -0.2617993877991494F, 0.2617993877991494F, -0.2617993877991494F);
        this.armLpauldron = new ModelRenderer(this, 0, 67);
        this.armLpauldron.mirror = true;
        this.armLpauldron.setRotationPoint(0.0F, 0.0F, -0.0F);
        this.armLpauldron.addBox(-1.0F, -2.5F, -3.0F, 5, 6, 6, 0.0F);
        this.wing1_1 = new ModelRenderer(this, 56, 41);
        this.wing1_1.setRotationPoint(4.5F, 0.0F, 0.0F);
        this.wing1_1.addBox(0.0F, -3.0F, -0.5F, 0, 3, 4, 0.0F);
        this.setRotateAngle(wing1_1, 0.2617993877991494F, 0.7853981633974483F, 0.2617993877991494F);
        this.helm2 = new ModelRenderer(this, 50, 32);
        this.helm2.mirror = true;
        this.helm2.setRotationPoint(0.0F, 0.0F, 0.0F);
        this.helm2.addBox(3.0F, -5.0F, -4.5F, 1, 5, 4, 0.0F);
        this.wing2_3 = new ModelRenderer(this, 56, 44);
        this.wing2_3.setRotationPoint(2.5F, 9.0F, 0.0F);
        this.wing2_3.addBox(0.0F, 0.0F, -0.5F, 0, 1, 2, 0.0F);
        this.setRotateAngle(wing2_3, 0.08726646259971647F, 0.7853981633974483F, 0.2617993877991494F);
        this.wing1 = new ModelRenderer(this, 56, 41);
        this.wing1.mirror = true;
        this.wing1.setRotationPoint(-4.5F, 0.0F, 0.0F);
        this.wing1.addBox(0.5F, -3.0F, 0.0F, 0, 3, 4, 0.0F);
        this.setRotateAngle(wing1, 0.2617993877991494F, -0.7853981633974483F, -0.2617993877991494F);
        this.body2 = new ModelRenderer(this, 0, 56);
        this.body2.setRotationPoint(0.0F, 0.0F, 0.0F);
        this.body2.addBox(-3.0F, 4.0F, -3.0F, 6, 6, 5, 0.0F);
        this.setRotateAngle(body2, -0.08726646259971647F, 0.0F, 0.0F);
        this.helmWing3 = new ModelRenderer(this, 32, 45);
        this.helmWing3.setRotationPoint(4.0F, -4.0F, -1.0F);
        this.helmWing3.addBox(-0.5F, -5.0F, 0.0F, 1, 5, 6, 0.0F);
        this.setRotateAngle(helmWing3, 0.2617993877991494F, 0.5235987755982988F, 0.08726646259971647F);
        this.helmWing1 = new ModelRenderer(this, 32, 45);
        this.helmWing1.mirror = true;
        this.helmWing1.setRotationPoint(-4.0F, -4.0F, -1.0F);
        this.helmWing1.addBox(-0.5F, -5.0F, 0.0F, 1, 5, 6, 0.0F);
        this.setRotateAngle(helmWing1, 0.2617993877991494F, -0.5235987755982988F, -0.08726646259971647F);
        this.wing2 = new ModelRenderer(this, 56, 42);
        this.wing2.mirror = true;
        this.wing2.setRotationPoint(-4.5F, 0.0F, 0.0F);
        this.wing2.addBox(0.5F, 0.0F, 0.0F, 0, 2, 3, 0.0F);
        this.setRotateAngle(wing2, 0.08726646259971647F, -0.7853981633974483F, -0.2617993877991494F);
        this.wing1_3 = new ModelRenderer(this, 56, 43);
        this.wing1_3.setRotationPoint(2.5F, 9.0F, 0.0F);
        this.wing1_3.addBox(0.0F, -2.0F, -0.5F, 0, 2, 3, 0.0F);
        this.setRotateAngle(wing1_3, 0.2617993877991494F, 0.7853981633974483F, 0.2617993877991494F);
        this.helm.addChild(this.fairy);
        this.helm.addChild(this.helm3);
        this.bootR.addChild(this.wing1_2);
        this.helm.addChild(this.helm1);
        this.belt.addChild(this.legL);
        this.armR.addChild(this.armRpauldron);
        this.belt.addChild(this.legR);
        this.helm.addChild(this.helmWing2);
        this.bootL.addChild(this.bootL1);
        this.bootR.addChild(this.bootR1);
        this.armLpauldron.addChild(this.wing2_1);
        this.bootR.addChild(this.wing2_2);
        this.helm.addChild(this.helmWing4);
        this.armL.addChild(this.armLpauldron);
        this.armLpauldron.addChild(this.wing1_1);
        this.helm.addChild(this.helm2);
        this.bootL.addChild(this.wing2_3);
        this.armRpauldron.addChild(this.wing1);
        this.body.addChild(this.body2);
        this.helm.addChild(this.helmWing3);
        this.helm.addChild(this.helmWing1);
        this.armRpauldron.addChild(this.wing2);
        this.bootL.addChild(this.wing1_3);
    }

    @Override
    public void render(Entity entity, float f, float f1, float f2, float f3, float f4, float f5) { 
        this.armL.render(f5);
        this.armR.render(f5);
        this.bootR.render(f5);
        this.bootL.render(f5);
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