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

import javax.annotation.Nonnull;

import net.minecraft.client.model.ModelRenderer;
import net.minecraft.entity.Entity;
import net.minecraft.inventory.EntityEquipmentSlot;

public class ModelArmorTerrasteel extends ModelArmor {

	private final ModelRenderer helmAnchor;
	private final ModelRenderer helm;
	private final ModelRenderer helmFront;
	private final ModelRenderer helmLeaf1l;
	private final ModelRenderer helmLeaf2l;
	private final ModelRenderer helmLeaf3l;
	private final ModelRenderer helmLeaf1r;
	private final ModelRenderer helmLeaf2r;
	private final ModelRenderer helmLeaf3r;
	private final ModelRenderer helmbranch1l;
	private final ModelRenderer helmbranch2l;
	private final ModelRenderer helmbranch1r;
	private final ModelRenderer helmbranch2r;

	private final ModelRenderer bodyAnchor;
	private final ModelRenderer bodyTop;
	private final ModelRenderer bodyBottom;
	
	private final ModelRenderer armLAnchor;
	private final ModelRenderer armL;
	private final ModelRenderer armLpauldron;
	private final ModelRenderer armLbranch1;
	private final ModelRenderer armLbranch2;
	
	private final ModelRenderer armRAnchor;
	private final ModelRenderer armR;
	private final ModelRenderer armRpauldron;
	private final ModelRenderer armRbranch1;
	private final ModelRenderer armRbranch2;
	
	private final ModelRenderer pantsAnchor;
	private final ModelRenderer belt;
	private final ModelRenderer legL;
	private final ModelRenderer legR;
	
	private final ModelRenderer bootL;
	private final ModelRenderer bootLtop;
	private final ModelRenderer bootLbranch1;
	private final ModelRenderer bootLbranch2;
	
	private final ModelRenderer bootR;
	private final ModelRenderer bootRtop;
	private final ModelRenderer bootRbranch1;
	private final ModelRenderer bootRbranch2;
	
	public ModelArmorTerrasteel(EntityEquipmentSlot slot) {
		super(slot);

		textureWidth = 64;
		textureHeight = 128;
		float s = 0.01F;
		
		//helm
        this.helmAnchor = new ModelRenderer(this, 0, 0);
        this.helmAnchor.setRotationPoint(0.0F, 0.0F, 0.0F);
        this.helmAnchor.addBox(-1.0F, -2.0F, 0.0F, 2, 2, 2, s);
        this.helm = new ModelRenderer(this, 0, 0);
        this.helm.setRotationPoint(0.0F, 0.0F, 0.0F);
        this.helm.addBox(-4.5F, -9.0F, -4.5F, 9, 11, 9, s);
        this.helmFront = new ModelRenderer(this, 36, 0);
        this.helmFront.setRotationPoint(0.0F, 0.0F, 0.0F);
        this.helmFront.addBox(-1.5F, -10.0F, -5.5F, 3, 8, 7, s);
        this.helmLeaf1l = new ModelRenderer(this, 56, 14);
        this.helmLeaf1l.mirror = true;
        this.helmLeaf1l.setRotationPoint(4.5F, -6.0F, -2.5F);
        this.helmLeaf1l.addBox(0.0F, -3.0F, -1.0F, 2, 3, 1, s);
        this.setRotateAngle(helmLeaf1l, -0.2617993877991494F, -0.2617993877991494F, 0.5235987755982988F);
        this.helmLeaf2l = new ModelRenderer(this, 56, 8);
        this.helmLeaf2l.mirror = true;
        this.helmLeaf2l.setRotationPoint(4.5F, -6.0F, -0.5F);
        this.helmLeaf2l.addBox(0.0F, -5.0F, -1.0F, 2, 5, 1, s);
        this.setRotateAngle(helmLeaf2l, -0.5235987755982988F, -0.5235987755982988F, 0.7853981633974483F);
        this.helmLeaf3l = new ModelRenderer(this, 56, 0);
        this.helmLeaf3l.mirror = true;
        this.helmLeaf3l.setRotationPoint(4.5F, -6.0F, 2.5F);
        this.helmLeaf3l.addBox(0.0F, -7.0F, -1.0F, 2, 7, 1, s);
        this.setRotateAngle(helmLeaf3l, -0.7853981633974483F, -0.7853981633974483F, 0.7853981633974483F);
        this.helmLeaf1r = new ModelRenderer(this, 56, 14);
        this.helmLeaf1r.setRotationPoint(-4.5F, -6.0F, -2.5F);
        this.helmLeaf1r.addBox(-2.0F, -3.0F, -1.0F, 2, 3, 1, s);
        this.setRotateAngle(helmLeaf1r, -0.2617993877991494F, 0.2617993877991494F, -0.5235987755982988F);
        this.helmLeaf2r = new ModelRenderer(this, 56, 8);
        this.helmLeaf2r.setRotationPoint(-4.5F, -6.0F, -0.5F);
        this.helmLeaf2r.addBox(-2.0F, -5.0F, -1.0F, 2, 5, 1, s);
        this.setRotateAngle(helmLeaf2r, -0.5235987755982988F, 0.5235987755982988F, -0.7853981633974483F);
        this.helmLeaf3r = new ModelRenderer(this, 56, 0);
        this.helmLeaf3r.setRotationPoint(-4.5F, -6.0F, 2.5F);
        this.helmLeaf3r.addBox(-2.0F, -7.0F, -1.0F, 2, 7, 1, s);
        this.setRotateAngle(helmLeaf3r, -0.7853981633974483F, 0.7853981633974483F, -0.7853981633974483F);
        this.helmbranch1l = new ModelRenderer(this, 36, 15);
        this.helmbranch1l.mirror = true;
        this.helmbranch1l.setRotationPoint(2.5F, -9.0F, -3.5F);
        this.helmbranch1l.addBox(-1.0F, -1.0F, 0.0F, 2, 2, 7, s);
        this.setRotateAngle(helmbranch1l, 0.5235987755982988F, 0.2617993877991494F, 0.0F);
        this.helmbranch2l = new ModelRenderer(this, 36, 15);
        this.helmbranch2l.mirror = true;
        this.helmbranch2l.setRotationPoint(4.5F, -3.0F, -2.5F);
        this.helmbranch2l.addBox(-1.0F, -1.0F, 0.0F, 2, 2, 7, s);
        this.setRotateAngle(helmbranch2l, 0.08726646259971647F, 0.2617993877991494F, 0.0F);
        this.helmbranch1r = new ModelRenderer(this, 36, 15);
        this.helmbranch1r.setRotationPoint(-2.5F, -9.0F, -3.5F);
        this.helmbranch1r.addBox(-1.0F, -1.0F, 0.0F, 2, 2, 7, s);
        this.setRotateAngle(helmbranch1r, 0.5235987755982988F, -0.2617993877991494F, 0.0F);
        this.helmbranch2r = new ModelRenderer(this, 36, 15);
        this.helmbranch2r.setRotationPoint(-4.5F, -3.0F, -2.5F);
        this.helmbranch2r.addBox(-1.0F, -1.0F, 0.0F, 2, 2, 7, s);
        this.setRotateAngle(helmbranch2r, 0.08726646259971647F, -0.2617993877991494F, 0.0F);
        
        //body
        this.bodyAnchor = new ModelRenderer(this, 0, 0);
        this.bodyAnchor.setRotationPoint(0.0F, 0.0F, 0.0F);
        this.bodyAnchor.addBox(-1.0F, 0.0F, -1.0F, 2, 2, 2, s);
        this.bodyTop = new ModelRenderer(this, 0, 20);
        this.bodyTop.setRotationPoint(0.0F, 0.0F, 0.0F);
        this.bodyTop.addBox(-5.5F, 0.0F, -3.0F, 11, 6, 6, s);
        this.bodyBottom = new ModelRenderer(this, 0, 32);
        this.bodyBottom.setRotationPoint(0.0F, 0.0F, 0.0F);
        this.bodyBottom.addBox(-4.5F, 5.0F, -2.5F, 9, 3, 5, s);
        
        //armL
        this.armLAnchor = new ModelRenderer(this, 0, 0);
        this.armLAnchor.mirror = true;
        this.armLAnchor.setRotationPoint(4.0F, 2.0F, 0.0F);
        this.armLAnchor.addBox(0.0F, -1.0F, -1.0F, 2, 2, 2, s);
		this.armL = new ModelRenderer(this, 0, 52);
        this.armL.mirror = true;
        this.armL.setRotationPoint(0.0F, 0.0F, 0.0F);
        this.armL.addBox(-1.5F, 3.0F, -2.49F, 5, 8, 5, s);
        this.armLpauldron = new ModelRenderer(this, 0, 40);
        this.armLpauldron.mirror = true;
        this.armLpauldron.setRotationPoint(1.5F, 0.0F, 0.0F);
        this.armLpauldron.addBox(-1.0F, -3.0F, -3.0F, 6, 6, 6, s);
        this.armLbranch1 = new ModelRenderer(this, 36, 15);
        this.armLbranch1.mirror = true;
        this.armLbranch1.setRotationPoint(4.0F, -3.0F, -1.0F);
        this.armLbranch1.addBox(-1.0F, -1.0F, 0.0F, 2, 2, 7, s);
        this.setRotateAngle(armLbranch1, 0.5235987755982988F, 0.5235987755982988F, 0.0F);
        this.armLbranch2 = new ModelRenderer(this, 36, 24);
        this.armLbranch2.mirror = true;
        this.armLbranch2.setRotationPoint(5.0F, -2.0F, 0.0F);
        this.armLbranch2.addBox(-1.0F, 0.0F, 0.0F, 2, 2, 5, s);
        this.setRotateAngle(armLbranch2, 0.08726646259971647F, 0.7853981633974483F, 0.0F);
        
        //armR
        this.armRAnchor = new ModelRenderer(this, 0, 0);
        this.armRAnchor.mirror = true;
        this.armRAnchor.setRotationPoint(-4.0F, 2.0F, 0.0F);
        this.armRAnchor.addBox(-2.0F, -1.0F, -1.0F, 2, 2, 2, s);
		this.armR = new ModelRenderer(this, 0, 52);
        this.armR.setRotationPoint(0.0F, 0.0F, 0.0F);
        this.armR.addBox(-3.5F, 3.0F, -2.51F, 5, 8, 5, s);
        this.armRpauldron = new ModelRenderer(this, 0, 40);
        this.armRpauldron.setRotationPoint(-1.5F, 0.0F, 0.0F);
        this.armRpauldron.addBox(-5.0F, -3.0F, -3.0F, 6, 6, 6, s);
        this.armRbranch1 = new ModelRenderer(this, 36, 15);
        this.armRbranch1.setRotationPoint(-4.0F, -3.0F, -1.0F);
        this.armRbranch1.addBox(-1.0F, -1.0F, 0.0F, 2, 2, 7, s);
        this.setRotateAngle(armRbranch1, 0.5235987755982988F, -0.5235987755982988F, 0.0F);
        this.armRbranch2 = new ModelRenderer(this, 36, 24);
        this.armRbranch2.setRotationPoint(-5.0F, -2.0F, 0.0F);
        this.armRbranch2.addBox(-1.0F, 0.0F, 0.0F, 2, 2, 5, s);
        this.setRotateAngle(armRbranch2, 0.08726646259971647F, -0.7853981633974483F, 0.0F);
       
        //pants
        this.pantsAnchor = new ModelRenderer(this, 0, 0);
        this.pantsAnchor.setRotationPoint(0.0F, 0.0F, 0.0F);
        this.pantsAnchor.addBox(-1.0F, 0.0F, -1.0F, 2, 2, 2, s);
        this.belt = new ModelRenderer(this, 0, 65);
        this.belt.setRotationPoint(0.0F, 0.0F, 0.0F);
        this.belt.addBox(-4.5F, 8.0F, -3.0F, 9, 5, 6, s);
		this.legL = new ModelRenderer(this, 0, 76);
        this.legL.mirror = true;
        this.legL.setRotationPoint(1.9F, 12.0F, 0.0F);
        this.legL.addBox(-2.39F, -0.01F, -2.49F, 5, 6, 5, s);
		this.legR = new ModelRenderer(this, 0, 76);
        this.legR.setRotationPoint(-1.9F, 12.0F, 0.0F);
        this.legR.addBox(-2.61F, -0.01F, -2.51F, 5, 6, 5, s);
        
        //boots
        this.bootL = new ModelRenderer(this, 0, 94);
        this.bootL.mirror = true;
        this.bootL.setRotationPoint(1.9F, 12.0F, 0.0F);
        this.bootL.addBox(-2.39F, 8.5F, -2.49F, 5, 4, 5, s);
        this.bootLtop = new ModelRenderer(this, 0, 87);
        this.bootLtop.mirror = true;
        this.bootLtop.setRotationPoint(0.0F, 0.0F, 0.0F);
        this.bootLtop.addBox(-2.39F, 6.0F, -2.49F, 6, 2, 5, s);
        this.bootLbranch1 = new ModelRenderer(this, 36, 15);
        this.bootLbranch1.mirror = true;
        this.bootLbranch1.setRotationPoint(3.5F, 6.0F, 0.0F);
        this.bootLbranch1.addBox(-1.0F, -1.0F, 0.0F, 2, 2, 7,s);
        this.setRotateAngle(bootLbranch1, 0.2617993877991494F, 0.2617993877991494F, -0.08726646259971647F);
        this.bootLbranch2 = new ModelRenderer(this, 36, 24);
        this.bootLbranch2.mirror = true;
        this.bootLbranch2.setRotationPoint(2.5F, 9.0F, 0.0F);
        this.bootLbranch2.addBox(-1.0F, -1.0F, 0.0F, 2, 2, 5, s);
        this.setRotateAngle(bootLbranch2, 0.08726646259971647F, 0.7853981633974483F, 0.0F);
        this.bootR = new ModelRenderer(this, 0, 94);
        this.bootR.setRotationPoint(-1.9F, 12.0F, 0.0F);
        this.bootR.addBox(-2.61F, 8.5F, -2.51F, 5, 4, 5, s);
		this.bootRtop = new ModelRenderer(this, 0, 87);
        this.bootRtop.setRotationPoint(0.0F, 0.0F, 0.0F);
        this.bootRtop.addBox(-3.61F, 6.0F, -2.51F, 6, 2, 5, s);
        this.bootRbranch1 = new ModelRenderer(this, 36, 15);
        this.bootRbranch1.setRotationPoint(-3.5F, 6.0F, 0.0F);
        this.bootRbranch1.addBox(-1.0F, -1.0F, 0.0F, 2, 2, 7, s);
        this.setRotateAngle(bootRbranch1, 0.2617993877991494F, -0.2617993877991494F, 0.08726646259971647F);
        this.bootRbranch2 = new ModelRenderer(this, 36, 24);
        this.bootRbranch2.setRotationPoint(-2.5F, 9.0F, 0.5F);
        this.bootRbranch2.addBox(-1.0F, -1.0F, 0.0F, 2, 2, 5, s);
        this.setRotateAngle(bootRbranch2, 0.08726646259971647F, -0.7853981633974483F, 0.0F);
		
        //hierarchy
        this.helmAnchor.addChild(this.helm);
        this.helm.addChild(this.helmFront);
        this.helm.addChild(this.helmLeaf1l);
        this.helm.addChild(this.helmLeaf2l);
        this.helm.addChild(this.helmLeaf3l);
        this.helm.addChild(this.helmLeaf1r);
        this.helm.addChild(this.helmLeaf2r);
        this.helm.addChild(this.helmLeaf3r);
        this.helm.addChild(this.helmbranch1l);
        this.helm.addChild(this.helmbranch2l);
        this.helm.addChild(this.helmbranch1r);
        this.helm.addChild(this.helmbranch2r);
        
        this.bodyAnchor.addChild(this.bodyTop);
        this.bodyTop.addChild(this.bodyBottom);
        this.armLAnchor.addChild(this.armL);
        this.armL.addChild(this.armLpauldron);
        this.armLpauldron.addChild(this.armLbranch1);
        this.armLpauldron.addChild(this.armLbranch2);
        this.armRAnchor.addChild(this.armR);
        this.armR.addChild(this.armRpauldron);
        this.armRpauldron.addChild(this.armRbranch1);
        this.armRpauldron.addChild(this.armRbranch2);
        
        this.pantsAnchor.addChild(this.belt);
        this.belt.addChild(this.legL);
        this.belt.addChild(this.legR);
        
        this.bootL.addChild(bootLtop);
        this.bootLtop.addChild(bootLbranch1);
        this.bootLtop.addChild(bootLbranch2);
        this.bootR.addChild(bootRtop);
        this.bootRtop.addChild(bootRbranch1);
        this.bootRtop.addChild(bootRbranch2);
	}

	@Override
	public void render(@Nonnull Entity entity, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch, float scale) {
		
		helmAnchor.showModel = slot == EntityEquipmentSlot.HEAD;
		bodyAnchor.showModel = slot == EntityEquipmentSlot.CHEST;
		armLAnchor.showModel = slot == EntityEquipmentSlot.CHEST;
		armRAnchor.showModel = slot == EntityEquipmentSlot.CHEST;
		legR.showModel = slot == EntityEquipmentSlot.LEGS;
		legL.showModel = slot == EntityEquipmentSlot.LEGS;
		bootL.showModel = slot == EntityEquipmentSlot.FEET;
		bootR.showModel = slot == EntityEquipmentSlot.FEET;
		
		bipedHeadwear.showModel = false;

		bipedHead = helmAnchor;
		bipedBody = bodyAnchor;
		bipedRightArm = armRAnchor;
		bipedLeftArm = armLAnchor;
		if(slot == EntityEquipmentSlot.LEGS) {
			bipedBody = pantsAnchor;
			bipedRightLeg = legR;
			bipedLeftLeg = legL;
		} else {
			bipedRightLeg = bootR;
			bipedLeftLeg = bootL;
		}

		super.render(entity, limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch, scale);
	}
}
