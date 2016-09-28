/*package model;

import net.minecraft.client.model.ModelBase;
import net.minecraft.client.model.ModelRenderer;
import net.minecraft.entity.Entity;

/**
 * armor_cloak - wiiv
 * Created using Tabula 4.1.1
 */
/*public class armor_cloak extends ModelBase {
    public ModelRenderer armRpauldron;
    public ModelRenderer armLpauldron;
    public ModelRenderer helm;

    public armor_cloak() {
        this.textureWidth = 64;
        this.textureHeight = 64;
        this.armLpauldron = new ModelRenderer(this, 0, 15);
        this.armLpauldron.mirror = true;
        this.armLpauldron.setRotationPoint(0.0F, 0.0F, -0.0F);
        this.armLpauldron.addBox(-1.0F, 0.0F, -5.0F, 9, 20, 9, 0.0F);
        this.setRotateAngle(armLpauldron, 0.08726646259971647F, -0.2617993877991494F, -0.17453292519943295F);
        this.armRpauldron = new ModelRenderer(this, 0, 15);
        this.armRpauldron.setRotationPoint(0.0F, 0.0F, 0.0F);
        this.armRpauldron.addBox(-8.0F, 0.0F, -5.0F, 9, 20, 9, 0.0F);
        this.setRotateAngle(armRpauldron, 0.08726646259971647F, 0.2617993877991494F, 0.17453292519943295F);
        this.helm = new ModelRenderer(this, 0, 0);
        this.helm.setRotationPoint(0.0F, 0.0F, 0.0F);
        this.helm.addBox(-4.5F, -3.0F, -5.5F, 9, 4, 11, 0.0F);
        this.setRotateAngle(helm, 0.2617993877991494F, 0.0F, 0.0F);
    }

    @Override
    public void render(Entity entity, float f, float f1, float f2, float f3, float f4, float f5) { 
        this.armLpauldron.render(f5);
        this.armRpauldron.render(f5);
        this.helm.render(f5);
    }

    /**
     * This is a helper function from Tabula to set the rotation of model parts
     */
    /*public void setRotateAngle(ModelRenderer modelRenderer, float x, float y, float z) {
        modelRenderer.rotateAngleX = x;
        modelRenderer.rotateAngleY = y;
        modelRenderer.rotateAngleZ = z;
    }
}
*/