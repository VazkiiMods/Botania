package model;

import net.minecraft.client.model.ModelBase;
import net.minecraft.client.model.ModelRenderer;
import net.minecraft.entity.Entity;

/**
 * crystalCube - wiiv
 * Created using Tabula 4.1.1
 */
public class crystalCube extends ModelBase {
    public ModelRenderer cube;
    public ModelRenderer base1;
    public ModelRenderer base2;

    public crystalCube() {
        this.textureWidth = 48;
        this.textureHeight = 32;
        this.cube = new ModelRenderer(this, 0, 0);
        this.cube.setRotationPoint(0.0F, 12.0F, 0.0F);
        this.cube.addBox(-4.0F, -4.0F, -4.0F, 8, 8, 8, 0.0F);
        this.base1 = new ModelRenderer(this, 22, 0);
        this.base1.setRotationPoint(0.0F, 16.0F, 0.0F);
        this.base1.addBox(-3.0F, 7.0F, -3.0F, 6, 1, 6, 0.0F);
        this.base2 = new ModelRenderer(this, 0, 16);
        this.base2.setRotationPoint(0.0F, 0.0F, 0.0F);
        this.base2.addBox(-5.0F, 3.0F, -5.0F, 10, 4, 10, 0.0F);
        this.base1.addChild(this.base2);
    }

    @Override
    public void render(Entity entity, float f, float f1, float f2, float f3, float f4, float f5) { 
        this.cube.render(f5);
        this.base1.render(f5);
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
