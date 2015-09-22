package model;

import net.minecraft.client.model.ModelBase;
import net.minecraft.client.model.ModelRenderer;
import net.minecraft.entity.Entity;

/**
 * teruTeruBozu - wiiv
 * Created using Tabula 4.1.1
 */
public class teruTeruBozu extends ModelBase {
    public ModelRenderer shape1;
    public ModelRenderer shape1_1;
    public ModelRenderer shape1_2;
    public ModelRenderer shape1_3;

    public teruTeruBozu() {
        this.textureWidth = 48;
        this.textureHeight = 32;
        this.shape1_3 = new ModelRenderer(this, 0, 12);
        this.shape1_3.setRotationPoint(0.0F, 14.5F, 0.0F);
        this.shape1_3.addBox(0.0F, 0.0F, 0.0F, 6, 6, 6, 0.0F);
        this.setRotateAngle(shape1_3, -2.1816615649929116F, -0.9599310885968813F, 1.3962634015954636F);
        this.shape1_1 = new ModelRenderer(this, 0, 24);
        this.shape1_1.setRotationPoint(0.0F, 14.0F, 0.0F);
        this.shape1_1.addBox(-1.0F, 0.0F, -1.0F, 2, 1, 2, 0.0F);
        this.shape1 = new ModelRenderer(this, 0, 0);
        this.shape1.setRotationPoint(0.0F, 14.5F, 0.0F);
        this.shape1.addBox(-3.0F, -6.0F, -3.0F, 6, 6, 6, 0.0F);
        this.setRotateAngle(shape1, -0.17453292519943295F, 0.0F, 0.0F);
        this.shape1_2 = new ModelRenderer(this, 0, 0);
        this.shape1_2.setRotationPoint(0.0F, 14.5F, 0.0F);
        this.shape1_2.addBox(-3.0F, -6.0F, -3.0F, 6, 6, 6, 0.0F);
        this.setRotateAngle(shape1_2, -0.17453292519943295F, 0.0F, 0.0F);
    }

    @Override
    public void render(Entity entity, float f, float f1, float f2, float f3, float f4, float f5) { 
        this.shape1_3.render(f5);
        this.shape1_1.render(f5);
        this.shape1.render(f5);
        this.shape1_2.render(f5);
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
