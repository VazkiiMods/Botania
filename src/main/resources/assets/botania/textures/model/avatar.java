/*
package model;

import net.minecraft.client.model.ModelBase;
import net.minecraft.client.model.ModelRenderer;
import net.minecraft.entity.Entity;

/**
 * avatar - wiiv
 * Created using Tabula 4.1.1
 */
public class avatar extends ModelBase {
    public ModelRenderer body;
    public ModelRenderer rightarm;
    public ModelRenderer leftarm;
    public ModelRenderer rightleg;
    public ModelRenderer leftleg;
    public ModelRenderer head;

    public avatar() {
        this.textureWidth = 32;
        this.textureHeight = 32;
        this.leftleg = new ModelRenderer(this, 0, 20);
        this.leftleg.mirror = true;
        this.leftleg.setRotationPoint(1.5F, 18.0F, -0.5F);
        this.leftleg.addBox(-1.5F, 0.0F, -1.5F, 3, 6, 3, 0.0F);
        this.rightarm = new ModelRenderer(this, 0, 20);
        this.rightarm.setRotationPoint(-3.0F, 15.0F, -1.0F);
        this.rightarm.addBox(-2.0F, -1.0F, -1.0F, 2, 6, 3, 0.0F);
        this.setRotateAngle(rightarm, 0.0F, -0.0F, 0.08726646259971647F);
        this.leftarm = new ModelRenderer(this, 0, 20);
        this.leftarm.mirror = true;
        this.leftarm.setRotationPoint(3.0F, 15.0F, -1.0F);
        this.leftarm.addBox(0.0F, -1.0F, -1.0F, 2, 6, 3, 0.0F);
        this.setRotateAngle(leftarm, 0.0F, -0.0F, -0.08726646259971647F);
        this.head = new ModelRenderer(this, 0, 0);
        this.head.setRotationPoint(0.0F, 14.0F, 0.0F);
        this.head.addBox(-3.0F, -6.0F, -3.0F, 6, 6, 6, 0.0F);
        this.rightleg = new ModelRenderer(this, 0, 20);
        this.rightleg.setRotationPoint(-1.5F, 18.0F, -0.5F);
        this.rightleg.addBox(-1.5F, 0.0F, -1.5F, 3, 6, 3, 0.0F);
        this.body = new ModelRenderer(this, 0, 12);
        this.body.setRotationPoint(0.0F, 14.0F, 0.0F);
        this.body.addBox(-3.0F, 0.0F, -2.0F, 6, 4, 4, 0.0F);
    }

    @Override
    public void render(Entity entity, float f, float f1, float f2, float f3, float f4, float f5) { 
        this.leftleg.render(f5);
        this.rightarm.render(f5);
        this.leftarm.render(f5);
        this.head.render(f5);
        this.rightleg.render(f5);
        this.body.render(f5);
    }

    /**
     * This is a helper function from Tabula to set the rotation of model parts
     */
    public void setRotateAngle(ModelRenderer modelRenderer, float x, float y, float z) {
        modelRenderer.rotateAngleX = x;
        modelRenderer.rotateAngleY = y;
        modelRenderer.rotateAngleZ = z;
    }
}
*/