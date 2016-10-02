package vazkii.botania.client.model;

import net.minecraft.client.model.ModelBase;
import net.minecraft.client.model.ModelRenderer;

/**
 * armor_cloak - wiiv
 * Created using Tabula 4.1.1
 */
public class ModelCloak extends ModelBase {

	public ModelRenderer armRpauldron;
	public ModelRenderer armLpauldron;
	public ModelRenderer helm;

	public ModelCloak() {
		textureWidth = 64;
		textureHeight = 64;
		armLpauldron = new ModelRenderer(this, 0, 15);
		armLpauldron.mirror = true;
		armLpauldron.setRotationPoint(0.0F, 0.0F, -0.0F);
		armLpauldron.addBox(-1.0F, 0.0F, -5.0F, 9, 20, 9, 0.0F);
		setRotateAngle(armLpauldron, 0.08726646259971647F, -0.2617993877991494F, -0.17453292519943295F);
		armRpauldron = new ModelRenderer(this, 0, 15);
		armRpauldron.setRotationPoint(0.0F, 0.0F, 0.0F);
		armRpauldron.addBox(-8.0F, 0.0F, -5.0F, 9, 20, 9, 0.0F);
		setRotateAngle(armRpauldron, 0.08726646259971647F, 0.2617993877991494F, 0.17453292519943295F);
		helm = new ModelRenderer(this, 0, 0);
		helm.setRotationPoint(0.0F, 0.0F, 0.0F);
		helm.addBox(-4.5F, -3.0F, -5.5F, 9, 4, 11, 0.0F);
		setRotateAngle(helm, 0.2617993877991494F, 0.0F, 0.0F);
	}

	public void render(float f5) {
		armLpauldron.render(f5);
		armRpauldron.render(f5);
		helm.render(f5);
	}

	public void setRotateAngle(ModelRenderer modelRenderer, float x, float y, float z) {
		modelRenderer.rotateAngleX = x;
		modelRenderer.rotateAngleY = y;
		modelRenderer.rotateAngleZ = z;
	}
}
