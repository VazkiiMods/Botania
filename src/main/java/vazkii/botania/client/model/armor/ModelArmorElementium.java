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
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.EnumAction;
import net.minecraft.item.ItemStack;

public class ModelArmorElementium extends ModelBiped {

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

	int slot;

	public ModelArmorElementium(int slot) {
		this.slot = slot;

		textureWidth = 64;
		textureHeight = 128;
		float s = 0.2F;
		fairy = new ModelRenderer(this, 34, 32);
		fairy.setRotationPoint(0.0F, 0.0F, 0.0F);
		fairy.addBox(-2.0F, -8.5F, -7.0F, 4, 4, 4, s);
		setRotateAngle(fairy, -0.17453292519943295F, 0.0F, 0.0F);
		helm3 = new ModelRenderer(this, 0, 32);
		helm3.setRotationPoint(0.0F, 0.0F, 0.0F);
		helm3.addBox(-1.0F, -5.5F, -5.5F, 2, 3, 1, s);
		setRotateAngle(helm3, -0.17453292519943295F, 0.0F, 0.0F);
		wing1_2 = new ModelRenderer(this, 56, 43);
		wing1_2.mirror = true;
		wing1_2.setRotationPoint(-2.5F, 9.0F, 0.0F);
		wing1_2.addBox(0.5F, -2.0F, 0.0F, 0, 2, 3, s);
		setRotateAngle(wing1_2, 0.2617993877991494F, -0.7853981633974483F, -0.2617993877991494F);
		helm1 = new ModelRenderer(this, 50, 32);
		helm1.setRotationPoint(0.0F, 0.0F, 0.0F);
		helm1.addBox(-4.0F, -5.0F, -4.5F, 1, 5, 4, s);
		legL = new ModelRenderer(this, 12, 79);
		legL.mirror = true;
		legL.setRotationPoint(1.9F, 12.0F, 0.0F);
		legL.addBox(-2.0F, 0.0F, -2.0F, 4, 6, 4, s);
		setRotateAngle(legL, 0.0F, 0.0F, 0F);
		armL = new ModelRenderer(this, 0, 79);
		armL.mirror = true;
		armL.setRotationPoint(5.0F, 2.0F, -0.0F);
		armL.addBox(1.5F, 6.0F, -2.0F, 2, 4, 4, s);
		setRotateAngle(armL, 0.0F, 0.0F, 0F);
		armRpauldron = new ModelRenderer(this, 0, 67);
		armRpauldron.setRotationPoint(0.0F, 0.0F, 0.0F);
		armRpauldron.addBox(-4.0F, -2.5F, -3.0F, 5, 6, 6, s);
		legR = new ModelRenderer(this, 12, 79);
		legR.setRotationPoint(-1.9F, 12.0F, 0.0F);
		legR.addBox(-2.0F, 0.0F, -2.0F, 4, 6, 4, s);
		setRotateAngle(legR, 0.0F, 0.0F, 0F);
		helmWing2 = new ModelRenderer(this, 46, 45);
		helmWing2.mirror = true;
		helmWing2.setRotationPoint(-4.0F, -4.0F, -1.0F);
		helmWing2.addBox(-0.5F, 0.0F, 0.0F, 1, 3, 4, s);
		setRotateAngle(helmWing2, -0.2617993877991494F, -0.2617993877991494F, 0.2617993877991494F);
		bootL1 = new ModelRenderer(this, 12, 79);
		bootL1.mirror = true;
		bootL1.setRotationPoint(0.0F, 0.0F, 0.0F);
		bootL1.addBox(-2.0F, 7.0F, -2.0F, 4, 1, 4, s);
		armR = new ModelRenderer(this, 0, 79);
		armR.setRotationPoint(-5.0F, 2.0F, 0.0F);
		armR.addBox(-3.5F, 6.0F, -2.0F, 2, 4, 4, s);
		setRotateAngle(armR, 0.0F, 0.0F, 0F);
		bootR1 = new ModelRenderer(this, 12, 79);
		bootR1.setRotationPoint(0.0F, 0.0F, 0.0F);
		bootR1.addBox(-2.0F, 7.0F, -2.0F, 4, 1, 4, s);
		bootR = new ModelRenderer(this, 12, 79);
		bootR.setRotationPoint(-1.9F, 12.0F, 0.0F);
		bootR.addBox(-2.0F, 8.0F, -3.0F, 4, 4, 5, s);
		setRotateAngle(bootR, 0.0F, 0.0F, 0F);
		wing2_1 = new ModelRenderer(this, 56, 42);
		wing2_1.setRotationPoint(4.5F, 0.0F, 0.0F);
		wing2_1.addBox(0.0F, 0.0F, -0.5F, 0, 2, 3, s);
		setRotateAngle(wing2_1, 0.08726646259971647F, 0.7853981633974483F, 0.2617993877991494F);
		wing2_2 = new ModelRenderer(this, 56, 44);
		wing2_2.mirror = true;
		wing2_2.setRotationPoint(-2.5F, 9.0F, 0.0F);
		wing2_2.addBox(0.5F, 0.0F, 0.0F, 0, 1, 2, s);
		setRotateAngle(wing2_2, 0.08726646259971647F, -0.7853981633974483F, -0.2617993877991494F);
		bootL = new ModelRenderer(this, 12, 79);
		bootL.mirror = true;
		bootL.setRotationPoint(1.9F, 12.0F, 0.0F);
		bootL.addBox(-2.0F, 8.0F, -3.0F, 4, 4, 5, s);
		setRotateAngle(bootL, 0.0F, 0.0F, 0F);
		body = new ModelRenderer(this, 0, 44);
		body.setRotationPoint(0.0F, 0.0F, 0.0F);
		body.addBox(-4.5F, 0.0F, -4.0F, 9, 5, 7, s);
		setRotateAngle(body, 0.08726646259971647F, 0.0F, 0.0F);
		belt = new ModelRenderer(this, 22, 56);
		belt.setRotationPoint(0.0F, 0.0F, 0.0F);
		belt.addBox(-4.5F, 9.5F, -3.0F, 9, 3, 5, s);
		helm = new ModelRenderer(this, 0, 32);
		helm.setRotationPoint(0.0F, 0.0F, 0.0F);
		helm.addBox(-4.0F, -8.0F, -4.5F, 8, 3, 9, s);
		setRotateAngle(helm, 0.08726646259971647F, 0.0F, 0.0F);
		helmWing4 = new ModelRenderer(this, 46, 45);
		helmWing4.setRotationPoint(4.0F, -4.0F, -1.0F);
		helmWing4.addBox(-0.5F, 0.0F, 0.0F, 1, 3, 4, s);
		setRotateAngle(helmWing4, -0.2617993877991494F, 0.2617993877991494F, -0.2617993877991494F);
		armLpauldron = new ModelRenderer(this, 0, 67);
		armLpauldron.mirror = true;
		armLpauldron.setRotationPoint(0.0F, 0.0F, -0.0F);
		armLpauldron.addBox(-1.0F, -2.5F, -3.0F, 5, 6, 6, s);
		wing1_1 = new ModelRenderer(this, 56, 41);
		wing1_1.setRotationPoint(4.5F, 0.0F, 0.0F);
		wing1_1.addBox(0.0F, -3.0F, -0.5F, 0, 3, 4, s);
		setRotateAngle(wing1_1, 0.2617993877991494F, 0.7853981633974483F, 0.2617993877991494F);
		helm2 = new ModelRenderer(this, 50, 32);
		helm2.mirror = true;
		helm2.setRotationPoint(0.0F, 0.0F, 0.0F);
		helm2.addBox(3.0F, -5.0F, -4.5F, 1, 5, 4, s);
		wing2_3 = new ModelRenderer(this, 56, 44);
		wing2_3.setRotationPoint(2.5F, 9.0F, 0.0F);
		wing2_3.addBox(0.0F, 0.0F, -0.5F, 0, 1, 2, s);
		setRotateAngle(wing2_3, 0.08726646259971647F, 0.7853981633974483F, 0.2617993877991494F);
		wing1 = new ModelRenderer(this, 56, 41);
		wing1.mirror = true;
		wing1.setRotationPoint(-4.5F, 0.0F, 0.0F);
		wing1.addBox(0.5F, -3.0F, 0.0F, 0, 3, 4, s);
		setRotateAngle(wing1, 0.2617993877991494F, -0.7853981633974483F, -0.2617993877991494F);
		body2 = new ModelRenderer(this, 0, 56);
		body2.setRotationPoint(0.0F, 0.0F, 0.0F);
		body2.addBox(-3.0F, 4.0F, -3.0F, 6, 6, 5, s);
		setRotateAngle(body2, -0.08726646259971647F, 0.0F, 0.0F);
		helmWing3 = new ModelRenderer(this, 32, 45);
		helmWing3.setRotationPoint(4.0F, -4.0F, -1.0F);
		helmWing3.addBox(-0.5F, -5.0F, 0.0F, 1, 5, 6, s);
		setRotateAngle(helmWing3, 0.2617993877991494F, 0.5235987755982988F, 0.08726646259971647F);
		helmWing1 = new ModelRenderer(this, 32, 45);
		helmWing1.mirror = true;
		helmWing1.setRotationPoint(-4.0F, -4.0F, -1.0F);
		helmWing1.addBox(-0.5F, -5.0F, 0.0F, 1, 5, 6, s);
		setRotateAngle(helmWing1, 0.2617993877991494F, -0.5235987755982988F, -0.08726646259971647F);
		wing2 = new ModelRenderer(this, 56, 42);
		wing2.mirror = true;
		wing2.setRotationPoint(-4.5F, 0.0F, 0.0F);
		wing2.addBox(0.5F, 0.0F, 0.0F, 0, 2, 3, s);
		setRotateAngle(wing2, 0.08726646259971647F, -0.7853981633974483F, -0.2617993877991494F);
		wing1_3 = new ModelRenderer(this, 56, 43);
		wing1_3.setRotationPoint(2.5F, 9.0F, 0.0F);
		wing1_3.addBox(0.0F, -2.0F, -0.5F, 0, 2, 3, s);
		setRotateAngle(wing1_3, 0.2617993877991494F, 0.7853981633974483F, 0.2617993877991494F);
		helm.addChild(fairy);
		helm.addChild(helm3);
		bootR.addChild(wing1_2);
		helm.addChild(helm1);
		belt.addChild(legL);
		armR.addChild(armRpauldron);
		belt.addChild(legR);
		helm.addChild(helmWing2);
		bootL.addChild(bootL1);
		bootR.addChild(bootR1);
		armLpauldron.addChild(wing2_1);
		bootR.addChild(wing2_2);
		helm.addChild(helmWing4);
		armL.addChild(armLpauldron);
		armLpauldron.addChild(wing1_1);
		helm.addChild(helm2);
		bootL.addChild(wing2_3);
		armRpauldron.addChild(wing1);
		body.addChild(body2);
		helm.addChild(helmWing3);
		helm.addChild(helmWing1);
		armRpauldron.addChild(wing2);
		bootL.addChild(wing1_3);
	}

	@Override
	public void render(Entity entity, float f, float f1, float f2, float f3, float f4, float f5) {
		helm.showModel = slot == 0;
		body.showModel = slot == 1;
		armR.showModel = slot == 1;
		armL.showModel = slot == 1;
		legR.showModel = slot == 2;
		legL.showModel = slot == 2;
		bootL.showModel = slot == 3;
		bootR.showModel = slot == 3;
		bipedHeadwear.showModel = false;

		bipedHead = helm;
		bipedBody = body;
		bipedRightArm = armR;
		bipedLeftArm = armL;
		if(slot == 2) {
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
		isSneak = living != null ? living.isSneaking() : false;
		if(living != null && living instanceof EntityPlayer) {
			EntityPlayer player = (EntityPlayer) living;

			ItemStack itemstack = player.inventory.getCurrentItem();
			heldItemRight = itemstack != null ? 1 : 0;

			aimedBow = false;
			if (itemstack != null && player.getItemInUseCount() > 0) {
				EnumAction enumaction = itemstack.getItemUseAction();

				if (enumaction == EnumAction.block)
					heldItemRight = 3;
				else if (enumaction == EnumAction.bow)
					aimedBow = true;
			}
		}
	}

	public void setRotateAngle(ModelRenderer modelRenderer, float x, float y, float z) {
		modelRenderer.rotateAngleX = x;
		modelRenderer.rotateAngleY = y;
		modelRenderer.rotateAngleZ = z;
	}

}