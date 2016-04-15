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

import net.minecraft.client.model.ModelBiped;
import net.minecraft.client.model.ModelRenderer;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.EnumAction;
import net.minecraft.util.EnumHandSide;

public class ModelArmorManasteel extends ModelBiped {


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

	private final EntityEquipmentSlot slot;

	public ModelArmorManasteel(EntityEquipmentSlot slot) {
		this.slot = slot;

		textureWidth = 64;
		textureHeight = 128;
		float s = 0.2F;
		armRpauldron = new ModelRenderer(this, 30, 47);
		armRpauldron.setRotationPoint(0.0F, 0.0F, 0.0F);
		armRpauldron.addBox(-4.0F, -2.0F, -2.5F, 4, 4, 5, 0.2F);
		armL = new ModelRenderer(this, 0, 68);
		armL.mirror = true;
		armL.setRotationPoint(5.0F, 2.0F, -0.0F);
		armL.addBox(1.0F, 3.0F, -2.0F, 2, 6, 4, s);
		setRotateAngle(armL, 0.0F, 0.0F, -0.17453292519943295F);
		legR = new ModelRenderer(this, 12, 68);
		legR.setRotationPoint(-1.9F, 12.0F, 0.0F);
		legR.addBox(-2.0F, 0.0F, -2.0F, 4, 8, 4, s);
		setRotateAngle(legR, 0.0F, 0.0F, 0F);
		helm3 = new ModelRenderer(this, 24, 32);
		helm3.setRotationPoint(0.0F, 0.0F, 0.0F);
		helm3.addBox(-1.0F, -8.5F, -6.5F, 2, 5, 1, s);
		setRotateAngle(helm3, -0.17453292519943295F, 0.0F, 0.0F);
		helm7 = new ModelRenderer(this, 24, 32);
		helm7.setRotationPoint(0.0F, 0.0F, 0.0F);
		helm7.addBox(-1.0F, -8.5F, -6.0F, 2, 3, 1, s);
		setRotateAngle(helm7, -0.3490658503988659F, 0.0F, 0.0F);
		bootL = new ModelRenderer(this, 28, 68);
		bootL.mirror = true;
		bootL.setRotationPoint(2.0F, 12.0F, 0.0F);
		bootL.addBox(-2.0F, 8.0F, -3.0F, 4, 4, 5, s);
		setRotateAngle(bootL, 0.0F, 0.0F, 0F);
		helm4 = new ModelRenderer(this, 0, 39);
		helm4.setRotationPoint(0.0F, 0.0F, 0.0F);
		helm4.addBox(-4.0F, -8.0F, -0.5F, 1, 3, 5, s);
		bootR = new ModelRenderer(this, 28, 68);
		bootR.setRotationPoint(-2.0F, 12.0F, 0.0F);
		bootR.addBox(-2.0F, 8.0F, -3.0F, 4, 4, 5, s);
		setRotateAngle(bootR, 0.0F, 0.0F, 0F);
		legL = new ModelRenderer(this, 12, 68);
		legL.mirror = true;
		legL.setRotationPoint(1.9F, 12.0F, 0.0F);
		legL.addBox(-2.0F, 0.0F, -2.0F, 4, 8, 4, s);
		setRotateAngle(legL, 0.0F, 0.0F, 0F);
		armR = new ModelRenderer(this, 0, 68);
		armR.setRotationPoint(-5.0F, 2.0F, 0.0F);
		armR.addBox(-3.0F, 3.0F, -2.0F, 2, 6, 4, s);
		setRotateAngle(armR, 0.0F, 0.0F, 0.17453292519943295F);
		helm1 = new ModelRenderer(this, 12, 39);
		helm1.setRotationPoint(0.0F, 0.0F, 0.0F);
		helm1.addBox(-4.0F, -5.0F, -4.5F, 1, 3, 4, s);
		helm2 = new ModelRenderer(this, 12, 39);
		helm2.mirror = true;
		helm2.setRotationPoint(0.0F, 0.0F, 0.0F);
		helm2.addBox(3.0F, -5.0F, -4.5F, 1, 3, 4, s);
		body2 = new ModelRenderer(this, 0, 59);
		body2.setRotationPoint(0.0F, 0.0F, 0.0F);
		body2.addBox(-4.0F, 6.0F, -2.5F, 8, 4, 5, s);
		setRotateAngle(body2, -0.08726646259971647F, 0.0F, 0.0F);
		body = new ModelRenderer(this, 0, 47);
		body.setRotationPoint(0.0F, 0.0F, 0.0F);
		body.addBox(-4.5F, 0.0F, -3.5F, 9, 6, 6, s);
		setRotateAngle(body, 0.08726646259971647F, 0.0F, 0.0F);
		helm6 = new ModelRenderer(this, 24, 32);
		helm6.setRotationPoint(0.0F, 0.0F, 0.0F);
		helm6.addBox(-1.0F, -8.5F, -5.5F, 2, 3, 1, s);
		setRotateAngle(helm6, -0.5235987755982988F, 0.0F, 0.0F);
		belt = new ModelRenderer(this, 26, 59);
		belt.setRotationPoint(0.0F, 0.0F, 0.0F);
		belt.addBox(-4.5F, 9.5F, -3.0F, 9, 3, 6, s);
		helm = new ModelRenderer(this, 0, 32);
		helm.setRotationPoint(0.0F, 0.0F, 0.0F);
		helm.addBox(-4.0F, -8.0F, -4.5F, 8, 3, 4, s);
		setRotateAngle(helm, 0.08726646259971647F, 0.0F, 0.0F);
		armLpauldron = new ModelRenderer(this, 30, 47);
		armLpauldron.mirror = true;
		armLpauldron.setRotationPoint(0.0F, 0.0F, -0.0F);
		armLpauldron.addBox(0.0F, -2.0F, -2.5F, 4, 4, 5, s);
		helm5 = new ModelRenderer(this, 0, 39);
		helm5.mirror = true;
		helm5.setRotationPoint(0.0F, 0.0F, 0.0F);
		helm5.addBox(3.0F, -8.0F, -0.5F, 1, 3, 5, s);

		helm.addChild(helm3);
		helm.addChild(helm7);
		helm.addChild(helm4);
		helm.addChild(helm6);
		helm.addChild(helm1);
		helm.addChild(helm2);
		helm.addChild(helm5);
		body.addChild(body2);
		armL.addChild(armLpauldron);
		armR.addChild(armRpauldron);
		belt.addChild(legR);
		belt.addChild(legL);
	}

	@Override
	public void render(Entity entity, float f, float f1, float f2, float f3, float f4, float f5) {
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

		prepareForRender(entity);
		super.render(entity, f, f1, f2, f3, f4, f5);
	}

	public void prepareForRender(Entity entity) {
		EntityLivingBase living = (EntityLivingBase) entity;
		isSneak = living != null && living.isSneaking();
		isChild = living != null && living.isChild();
		if(living != null) {
			ModelBiped.ArmPose mainPose = ArmPose.EMPTY;
			ModelBiped.ArmPose offPose = ArmPose.EMPTY;

			// Copy from RenderPlayer.setModelVisibilities
			if(living.getHeldItemMainhand() != null) {
				mainPose = ArmPose.ITEM;
				if (living.getItemInUseCount() > 0) {
					EnumAction enumaction = living.getHeldItemMainhand().getItemUseAction();

					if (enumaction == EnumAction.BLOCK) {
						mainPose = ModelBiped.ArmPose.BLOCK;
					} else if (enumaction == EnumAction.BOW) {
						mainPose = ModelBiped.ArmPose.BOW_AND_ARROW;
					}
				}
			}

			if (living.getHeldItemOffhand() != null) {
				offPose = ModelBiped.ArmPose.ITEM;

				if (living.getItemInUseCount() > 0) {
					EnumAction enumaction1 = living.getHeldItemOffhand().getItemUseAction();

					if (enumaction1 == EnumAction.BLOCK) {
						offPose = ModelBiped.ArmPose.BLOCK;
					}
				}
			}

			if(living.getPrimaryHand() == EnumHandSide.RIGHT) {
				rightArmPose = mainPose;
				leftArmPose = offPose;
			} else {
				rightArmPose = offPose;
				leftArmPose = mainPose;
			}
			// End copy RenderPlayer.setModelVisibilities
		}
	}

	public void setRotateAngle(ModelRenderer modelRenderer, float x, float y, float z) {
		modelRenderer.rotateAngleX = x;
		modelRenderer.rotateAngleY = y;
		modelRenderer.rotateAngleZ = z;
	}

}