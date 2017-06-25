/**
 * This class was created by <wiiv>. It's distributed as
 * part of the Botania Mod. Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 *
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 *
 * File Created @ [? (GMT)]
 */
package vazkii.botania.client.model.armor;

import net.minecraft.client.model.ModelRenderer;
import net.minecraft.entity.Entity;
import net.minecraft.inventory.EntityEquipmentSlot;

import javax.annotation.Nonnull;

public class ModelArmorManaweave extends ModelArmor {
	private final ModelRenderer helm;
	public final ModelRenderer body;
	private final ModelRenderer armR;
	private final ModelRenderer armL;
	private final ModelRenderer legR;
	private final ModelRenderer legL;
	private final ModelRenderer bootR;
	private final ModelRenderer bootL;
	private final ModelRenderer helm2;
	private final ModelRenderer helm3;
	private final ModelRenderer helm4;
	private final ModelRenderer helmSeam1;
	private final ModelRenderer helmSeam2;
	private final ModelRenderer helmSeam3;
	private final ModelRenderer helmSeam4;
	private final ModelRenderer body2;
	private final ModelRenderer armRpauldron;
	private final ModelRenderer armLpauldron;
	private final ModelRenderer skirtR;
	private final ModelRenderer skirtL;

	public ModelArmorManaweave(EntityEquipmentSlot slot) {
		super(slot);

		textureWidth = 64;
		textureHeight = 128;
		float s = 0.6F;
		helmSeam3 = new ModelRenderer(this, 26, 61);
		helmSeam3.setRotationPoint(0.0F, 0.0F, 0.0F);
		helmSeam3.addBox(-0.5F, -9.5F, 5.0F, 1, 11, 1, s);
		helmSeam4 = new ModelRenderer(this, 39, 64);
		helmSeam4.setRotationPoint(0.0F, 0.0F, 0.0F);
		helmSeam4.addBox(-0.5F, 0.5F, -1.0F, 1, 1, 6, s);
		skirtL = new ModelRenderer(this, 0, 83);
		skirtL.mirror = true;
		skirtL.setRotationPoint(0.0F, 0.0F, 0.0F);
		skirtL.addBox(-2.0F, -2.0F, -3.5F, 5, 13, 7, s);
		setRotateAngle(skirtL, 0.0F, -0.17453292519943295F, -0.2617993877991494F);
		armR = new ModelRenderer(this, 24, 83);
		armR.setRotationPoint(-5.0F, 2.0F, 0.0F);
		armR.addBox(-3.0F, -1.5F, -2.5F, 4, 10, 5, s);
		setRotateAngle(armR, 0.0F, 0.0F, 0F);
		armL = new ModelRenderer(this, 24, 83);
		armL.mirror = true;
		armL.setRotationPoint(5.0F, 2.0F, -0.0F);
		armL.addBox(-1.0F, -1.5F, -2.5F, 4, 10, 5, s);
		setRotateAngle(armL, 0.0F, 0.0F, 0F);
		bootR = new ModelRenderer(this, 0, 103);
		bootR.setRotationPoint(-2.0F, 12.0F, 0.0F);
		bootR.addBox(-2.0F, 8.0F, -3.0F, 4, 4, 5, s);
		setRotateAngle(bootR, 0.0F, 0.0F, 0F);
		legR = new ModelRenderer(this, 42, 81);
		legR.setRotationPoint(-2.0F, 12.0F, 0.0F);
		legR.addBox(-2.0F, 0.0F, -2.0F, 4, 6, 4, s);
		setRotateAngle(legR, 0.0F, 0.0F, 0F);
		helm2 = new ModelRenderer(this, 38, 42);
		helm2.setRotationPoint(0.0F, 0.0F, 0.0F);
		helm2.addBox(-4.49F, -4.0F, -2.5F, 1, 5, 8, s);
		skirtR = new ModelRenderer(this, 0, 83);
		skirtR.setRotationPoint(0.0F, 0.0F, 0.0F);
		skirtR.addBox(-3.0F, -2.0F, -3.5F, 5, 13, 7, s);
		setRotateAngle(skirtR, 0.0F, 0.17453292519943295F, 0.2617993877991494F);
		armRpauldron = new ModelRenderer(this, 0, 72);
		armRpauldron.setRotationPoint(0.0F, 0.0F, 0.0F);
		armRpauldron.addBox(-4.0F, -2.0F, -3.0F, 3, 5, 6, s);
		setRotateAngle(armRpauldron, 0.0F, 0.0F, 0F);
		bootL = new ModelRenderer(this, 0, 103);
		bootL.setRotationPoint(2.0F, 12.0F, 0.0F);
		bootL.addBox(-2.0F, 8.0F, -3.0F, 4, 4, 5, s);
		setRotateAngle(bootL, 0.0F, 0.0F, 0F);
		helmSeam1 = new ModelRenderer(this, 30, 61);
		helmSeam1.setRotationPoint(0.0F, 0.0F, 0.0F);
		helmSeam1.addBox(-0.5F, -10.5F, -5.0F, 1, 7, 2, s);
		helm = new ModelRenderer(this, 0, 32);
		helm.setRotationPoint(0.0F, 0.0F, 0.0F);
		helm.addBox(-4.5F, -10.0F, -4.5F, 9, 6, 10, s);
		setRotateAngle(helm, 0F, 0.0F, 0.0F);
		armLpauldron = new ModelRenderer(this, 0, 72);
		armLpauldron.mirror = true;
		armLpauldron.setRotationPoint(0.0F, 0.0F, 0.0F);
		armLpauldron.addBox(1.0F, -2.0F, -3.0F, 3, 5, 6, s);
		setRotateAngle(armLpauldron, 0.0F, 0.0F, 0F);
		helm4 = new ModelRenderer(this, 38, 32);
		helm4.setRotationPoint(0.0F, 0.0F, 0.0F);
		helm4.addBox(-3.5F, -4.0F, 0.49F, 7, 5, 5, s);
		helmSeam2 = new ModelRenderer(this, 36, 61);
		helmSeam2.setRotationPoint(0.0F, 0.0F, 0.0F);
		helmSeam2.addBox(-0.5F, -10.5F, -3.0F, 1, 1, 9, s);
		legL = new ModelRenderer(this, 42, 81);
		legL.mirror = true;
		legL.setRotationPoint(2.0F, 12.0F, 0.0F);
		legL.addBox(-2.0F, 0.0F, -2.0F, 4, 6, 4, s);
		setRotateAngle(legL, 0.0F, 0.0F, 0F);
		body2 = new ModelRenderer(this, 0, 61);
		body2.setRotationPoint(0.0F, 0.0F, 0.0F);
		body2.addBox(-4.0F, 6.0F, -2.5F, 8, 6, 5, s);
		body = new ModelRenderer(this, 0, 48);
		body.setRotationPoint(0.0F, 0.0F, 0.0F);
		body.addBox(-4.5F, 0.0F, -3.0F, 9, 7, 6, s);
		helm3 = new ModelRenderer(this, 38, 42);
		helm3.mirror = true;
		helm3.setRotationPoint(0.0F, 0.0F, 0F);
		helm3.addBox(3.49F, -4.0F, -2.5F, 1, 5, 8, s);
		helm.addChild(helmSeam3);
		helm.addChild(helmSeam4);
		legL.addChild(skirtL);
		helm.addChild(helm2);
		legR.addChild(skirtR);
		armR.addChild(armRpauldron);
		helm.addChild(helmSeam1);
		armL.addChild(armLpauldron);
		helm.addChild(helm4);
		helm.addChild(helmSeam2);
		body.addChild(body2);
		helm.addChild(helm3);
	}

	@Override
	public void render(@Nonnull Entity entity, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch, float scale) {
		helm.showModel = slot == EntityEquipmentSlot.HEAD;
		body.showModel = slot == EntityEquipmentSlot.CHEST;
		armR.showModel = slot == EntityEquipmentSlot.CHEST;
		armL.showModel = slot == EntityEquipmentSlot.CHEST;
		legR.showModel = slot == EntityEquipmentSlot.LEGS;
		legL.showModel = slot == EntityEquipmentSlot.LEGS;
		bootL.showModel = slot == EntityEquipmentSlot.FEET;
		bootR.showModel = slot == EntityEquipmentSlot.FEET;
		bipedHeadwear.showModel = false;

		bipedHead = helm;
		bipedBody = body;
		bipedRightArm = armR;
		bipedLeftArm = armL;
		if(slot == EntityEquipmentSlot.LEGS) {
			bipedRightLeg = legR;
			bipedLeftLeg = legL;
		} else {
			bipedRightLeg = bootR;
			bipedLeftLeg = bootL;
		}

		super.render(entity, limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch, scale);
	}
}