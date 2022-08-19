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

public class ModelArmorTerrasteel extends ModelBiped {

	public ModelRenderer helm;
	public ModelRenderer body;
	public ModelRenderer armr;
	public ModelRenderer armL;
	public ModelRenderer belt;
	public ModelRenderer bootR;
	public ModelRenderer bootL;
	public ModelRenderer helm2;
	public ModelRenderer helm3;
	public ModelRenderer helm4;
	public ModelRenderer helmLeaf1;
	public ModelRenderer helmLeaf2;
	public ModelRenderer helmLeaf3;
	public ModelRenderer helmLeaf4;
	public ModelRenderer helmLeaf5;
	public ModelRenderer helmLeaf6;
	public ModelRenderer helmbranch1;
	public ModelRenderer helmbranch2;
	public ModelRenderer helmbranch3;
	public ModelRenderer helmbranch4;
	public ModelRenderer body2;
	public ModelRenderer armRpauldron;
	public ModelRenderer armRbranch1;
	public ModelRenderer armRbranch2;
	public ModelRenderer armLpauldron;
	public ModelRenderer armLbranch1;
	public ModelRenderer armLbranch2;
	public ModelRenderer legR;
	public ModelRenderer legL;
	public ModelRenderer bootR1;
	public ModelRenderer bootRbranch;
	public ModelRenderer bootL2;
	public ModelRenderer bootLbranch;

	int slot;

	public ModelArmorTerrasteel(int slot) {
		this.slot = slot;

		textureWidth = 64;
		textureHeight = 128;
		float s = 0.2F;
		armr = new ModelRenderer(this, 0, 77);
		armr.setRotationPoint(-5.0F, 2.0F, -0.0F);
		armr.addBox(-3.0F, 3.0F, -2.0F, 4, 7, 4, s);
		setRotateAngle(armr, 0.0F, 0.0F, 0F);
		body = new ModelRenderer(this, 0, 44);
		body.setRotationPoint(0.0F, 0.0F, 0.0F);
		body.addBox(-4.5F, 0.0F, -3.5F, 9, 6, 6, s);
		setRotateAngle(body, 0F, 0.0F, 0.0F);
		helm4 = new ModelRenderer(this, 56, 32);
		helm4.setRotationPoint(0.0F, 0.0F, 0.0F);
		helm4.addBox(-1.0F, -7.5F, -6.5F, 2, 6, 2, s);
		setRotateAngle(helm4, 0F, 0.0F, 0.0F);
		bootR1 = new ModelRenderer(this, 32, 77);
		bootR1.setRotationPoint(0.0F, 0.0F, 0.0F);
		bootR1.addBox(-2.0F, 6.0F, -2.0F, 4, 2, 4, s);
		helmbranch4 = new ModelRenderer(this, 34, 43);
		helmbranch4.mirror = true;
		helmbranch4.setRotationPoint(0.0F, 0.0F, 0.0F);
		helmbranch4.addBox(-2.0F, -8.0F, -4.0F, 1, 2, 7, s);
		setRotateAngle(helmbranch4, 0.2617993877991494F, 0.0F, 1.0471975511965976F);
		bootL = new ModelRenderer(this, 32, 83);
		bootL.mirror = true;
		bootL.setRotationPoint(1.9F, 12.0F, 0.0F);
		bootL.addBox(-2.0F, 8.0F, -3.0F, 4, 4, 5, s);
		setRotateAngle(bootL, 0.0F, 0.0F, 0F);
		bootR = new ModelRenderer(this, 32, 83);
		bootR.setRotationPoint(-1.9F, 12.0F, 0.1F);
		bootR.addBox(-2.0F, 8.0F, -3.0F, 4, 4, 5, s);
		setRotateAngle(bootR, 0.0F, 0.0F, 0F);
		helmLeaf5 = new ModelRenderer(this, 50, 32);
		helmLeaf5.mirror = true;
		helmLeaf5.setRotationPoint(0.0F, 0.2F, 0.0F);
		helmLeaf5.addBox(-1.0F, -11.0F, -4.5F, 2, 5, 1, s);
		setRotateAngle(helmLeaf5, -0.5235987755982988F, -0.5235987755982988F, 0.5235987755982988F);
		bootLbranch = new ModelRenderer(this, 48, 77);
		bootLbranch.mirror = true;
		bootLbranch.setRotationPoint(0.0F, 0.0F, 0.0F);
		bootLbranch.addBox(8.0F, 1.0F, -2.0F, 1, 2, 5, s);
		setRotateAngle(bootLbranch, 0.2617993877991494F, 0.0F, 1.0471975511965976F);
		helmLeaf4 = new ModelRenderer(this, 50, 32);
		helmLeaf4.mirror = true;
		helmLeaf4.setRotationPoint(0.0F, 0.2F, 0.0F);
		helmLeaf4.addBox(-1.5F, -9.0F, -6.0F, 2, 3, 1, s);
		setRotateAngle(helmLeaf4, -0.2617993877991494F, -0.2617993877991494F, 0.5235987755982988F);
		helmLeaf2 = new ModelRenderer(this, 50, 32);
		helmLeaf2.setRotationPoint(0.0F, 0.2F, 0.0F);
		helmLeaf2.addBox(-1.0F, -11.0F, -4.5F, 2, 5, 1, s);
		setRotateAngle(helmLeaf2, -0.5235987755982988F, 0.5235987755982988F, -0.5235987755982988F);
		helm = new ModelRenderer(this, 0, 32);
		helm.setRotationPoint(0.0F, 0.0F, 0.0F);
		helm.addBox(-4.0F, -8.0F, -4.5F, 8, 3, 9, s);
		setRotateAngle(helm, 0.08726646259971647F, 0.0F, 0.0F);
		helm2 = new ModelRenderer(this, 34, 32);
		helm2.setRotationPoint(0.0F, 0.0F, 0.0F);
		helm2.addBox(-4.0F, -5.0F, -4.5F, 2, 5, 6, s);
		helm3 = new ModelRenderer(this, 34, 32);
		helm3.mirror = true;
		helm3.setRotationPoint(0.0F, 0.0F, 0.0F);
		helm3.addBox(2.0F, -5.0F, -4.5F, 2, 5, 6, s);
		helmbranch1 = new ModelRenderer(this, 34, 43);
		helmbranch1.mirror = true;
		helmbranch1.setRotationPoint(0.0F, 0.0F, 0F);
		helmbranch1.addBox(-2.0F, -10.0F, -1.0F, 1, 2, 7, s);
		setRotateAngle(helmbranch1, 0.5235987755982988F, 0.0F, -0.08726646259971647F);
		bootL2 = new ModelRenderer(this, 32, 77);
		bootL2.mirror = true;
		bootL2.setRotationPoint(0.0F, 0.0F, 0.0F);
		bootL2.addBox(-2.0F, 6.0F, -2.0F, 4, 2, 4, s);
		helmbranch2 = new ModelRenderer(this, 34, 43);
		helmbranch2.setRotationPoint(0.0F, 0.0F, 0.0F);
		helmbranch2.addBox(1.0F, -10.0F, -1.0F, 1, 2, 7, s);
		setRotateAngle(helmbranch2, 0.5235987755982988F, 0.0F, 0.08726646259971647F);
		legR = new ModelRenderer(this, 16, 77);
		legR.setRotationPoint(-1.9F, 12.0F, 0.0F);
		legR.addBox(-2.0F, 0.0F, -2.0F, 4, 6, 4, s);
		setRotateAngle(legR, 0.0F, 0.0F, 0F);
		helmLeaf6 = new ModelRenderer(this, 50, 32);
		helmLeaf6.mirror = true;
		helmLeaf6.setRotationPoint(0.0F, 0.2F, 0.0F);
		helmLeaf6.addBox(-0.5F, -13.0F, -3.0F, 2, 7, 1, s);
		setRotateAngle(helmLeaf6, -0.7853981633974483F, -0.7853981633974483F, 0.7853981633974483F);
		armLbranch1 = new ModelRenderer(this, 51, 44);
		armLbranch1.mirror = true;
		armLbranch1.setRotationPoint(0.0F, 0.0F, -0.0F);
		armLbranch1.addBox(2.5F, -5.0F, -1.0F, 1, 5, 2, s);
		setRotateAngle(armLbranch1, 0.0F, 0.0F, 0.7853981633974483F);
		bootRbranch = new ModelRenderer(this, 48, 77);
		bootRbranch.setRotationPoint(0.0F, 0.0F, 0.0F);
		bootRbranch.addBox(-9.0F, 1.0F, -2.0F, 1, 2, 5, s);
		setRotateAngle(bootRbranch, 0.2617993877991494F, 0.0F, -1.0471975511965976F);
		armRbranch2 = new ModelRenderer(this, 50, 43);
		armRbranch2.setRotationPoint(0.0F, 0.0F, 0.0F);
		armRbranch2.addBox(-1.5F, -5.0F, -1.5F, 1, 5, 3, s);
		setRotateAngle(armRbranch2, 0.0F, 0.0F, -0.5235987755982988F);
		helmLeaf3 = new ModelRenderer(this, 50, 32);
		helmLeaf3.setRotationPoint(0.0F, 0.2F, 0.0F);
		helmLeaf3.addBox(-1.5F, -13.0F, -3.0F, 2, 7, 1, s);
		setRotateAngle(helmLeaf3, -0.7853981633974483F, 0.7853981633974483F, -0.7853981633974483F);
		armRpauldron = new ModelRenderer(this, 0, 66);
		armRpauldron.setRotationPoint(0.0F, 0.0F, 0.0F);
		armRpauldron.addBox(-4.0F, -2.0F, -3.0F, 5, 5, 6, s);
		armLbranch2 = new ModelRenderer(this, 50, 43);
		armLbranch2.mirror = true;
		armLbranch2.setRotationPoint(0.0F, 0.0F, -0.0F);
		armLbranch2.addBox(0.5F, -5.0F, -1.5F, 1, 5, 3, s);
		setRotateAngle(armLbranch2, 0.0F, 0.0F, 0.5235987755982988F);
		armL = new ModelRenderer(this, 0, 77);
		armL.mirror = true;
		armL.setRotationPoint(5.0F, 2.0F, -0.0F);
		armL.addBox(-1.0F, 3.0F, -2.0F, 4, 7, 4, s);
		setRotateAngle(armL, 0.0F, 0.0F, 0F);
		body2 = new ModelRenderer(this, 0, 57);
		body2.setRotationPoint(0.0F, 0.0F, 0.0F);
		body2.addBox(-4.0F, 6.0F, -2.5F, 8, 4, 5, s);
		setRotateAngle(body2, -0.08726646259971647F, 0.0F, 0.0F);
		helmbranch3 = new ModelRenderer(this, 34, 43);
		helmbranch3.setRotationPoint(0.0F, 0.0F, 0.0F);
		helmbranch3.addBox(1.0F, -8.0F, -4.0F, 1, 2, 7, s);
		setRotateAngle(helmbranch3, 0.2617993877991494F, 0.0F, -1.0471975511965976F);
		armRbranch1 = new ModelRenderer(this, 51, 44);
		armRbranch1.setRotationPoint(0.0F, 0.0F, 0.0F);
		armRbranch1.addBox(-3.5F, -5.0F, -1.0F, 1, 5, 2, s);
		setRotateAngle(armRbranch1, 0.0F, 0.0F, -0.7853981633974483F);
		belt = new ModelRenderer(this, 22, 66);
		belt.setRotationPoint(0.0F, 0.0F, 0.0F);
		belt.addBox(-4.5F, 9.5F, -3.0F, 9, 3, 6, s);
		helmLeaf1 = new ModelRenderer(this, 50, 32);
		helmLeaf1.setRotationPoint(0.0F, 0.2F, 0.0F);
		helmLeaf1.addBox(-0.5F, -9.0F, -6.0F, 2, 3, 1, s);
		setRotateAngle(helmLeaf1, -0.2617993877991494F, 0.2617993877991494F, -0.5235987755982988F);
		armLpauldron = new ModelRenderer(this, 0, 66);
		armLpauldron.mirror = true;
		armLpauldron.setRotationPoint(0.0F, 0.0F, -0.0F);
		armLpauldron.addBox(-1.0F, -2.0F, -3.0F, 5, 5, 6, s);
		legL = new ModelRenderer(this, 16, 77);
		legL.mirror = true;
		legL.setRotationPoint(1.9F, 12.0F, 0.0F);
		legL.addBox(-2.0F, 0.0F, -2.0F, 4, 6, 4, s);
		setRotateAngle(legL, 0.0F, 0.0F, 0F);

		helm.addChild(helm4);
		bootR.addChild(bootR1);
		helm.addChild(helmbranch4);
		helm.addChild(helmLeaf5);
		bootL.addChild(bootLbranch);
		helm.addChild(helmLeaf4);
		helm.addChild(helmLeaf2);
		helm.addChild(helm2);
		helm.addChild(helm3);
		helm.addChild(helmbranch1);
		bootL.addChild(bootL2);
		helm.addChild(helmbranch2);
		belt.addChild(legR);
		helm.addChild(helmLeaf6);
		armLpauldron.addChild(armLbranch1);
		bootR.addChild(bootRbranch);
		armRpauldron.addChild(armRbranch2);
		helm.addChild(helmLeaf3);
		armr.addChild(armRpauldron);
		armLpauldron.addChild(armLbranch2);
		body.addChild(body2);
		helm.addChild(helmbranch3);
		armRpauldron.addChild(armRbranch1);
		helm.addChild(helmLeaf1);
		armL.addChild(armLpauldron);
		belt.addChild(legL);
	}

	@Override
	public void render(Entity entity, float f, float f1, float f2, float f3, float f4, float f5) {
		helm.showModel = slot == 0;
		body.showModel = slot == 1;
		armr.showModel = slot == 1;
		armL.showModel = slot == 1;
		legR.showModel = slot == 2;
		legL.showModel = slot == 2;
		bootL.showModel = slot == 3;
		bootR.showModel = slot == 3;
		bipedHeadwear.showModel = false;

		bipedHead = helm;
		bipedBody = body;
		bipedRightArm = armr;
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
