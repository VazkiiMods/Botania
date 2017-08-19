package vazkii.botania.client.model;

import net.minecraft.client.model.ModelBase;
import net.minecraft.client.model.ModelRenderer;

/**
 * armor_cloak - wiiv
 * Created using Tabula 4.1.1
 */
public class ModelCloak extends ModelBase {

	public ModelRenderer collar;
	public ModelRenderer sideL;
	public ModelRenderer sideR;
	
	public ModelCloak() {
		
		textureWidth = 64;
		textureHeight = 64;
		float s = 0.01F;
		
		collar = new ModelRenderer(this, 0, 0);
        collar.setRotationPoint(0.0F, -3.0F, -4.5F);
        collar.addBox(-5.5F, 0.0F, -1.5F, 11, 5, 11, s);
        setRotateAngle(collar, 0.08726646259971647F, 0.0F, 0.0F);
		sideL = new ModelRenderer(this, 0, 16);
        sideL.mirror = true;
        sideL.setRotationPoint(0.0F, 0.0F, 0.0F);
        sideL.addBox(-0.5F, -0.5F, -5.5F, 11, 21, 10, s);
        setRotateAngle(sideL, 0.08726646259971647F, -0.08726646259971647F, -0.17453292519943295F);
        sideR = new ModelRenderer(this, 0, 16);
        sideR.setRotationPoint(0.0F, 0.0F, 0.0F);
        sideR.addBox(-10.5F, -0.5F, -5.5F, 11, 21, 10, s);
        setRotateAngle(sideR, 0.08726646259971647F, 0.08726646259971647F, 0.17453292519943295F);
		
	}

	public void render(float f5) {
		collar.render(f5);
		sideL.render(f5);
		sideR.render(f5);
	}

	public void setRotateAngle(ModelRenderer modelRenderer, float x, float y, float z) {
		modelRenderer.rotateAngleX = x;
		modelRenderer.rotateAngleY = y;
		modelRenderer.rotateAngleZ = z;
	}
}
