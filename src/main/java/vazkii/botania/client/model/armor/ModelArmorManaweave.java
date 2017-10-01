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

public class ModelArmorManaweave extends ModelArmor {
	
	private final ModelRenderer helmAnchor;
	private final ModelRenderer helm;

	private final ModelRenderer bodyAnchor;
	private final ModelRenderer bodyTop;
	private final ModelRenderer bodyBottom;
	
	private final ModelRenderer armLAnchor;
	private final ModelRenderer armL;
	private final ModelRenderer armLpauldron;
	private final ModelRenderer armRAnchor;
	private final ModelRenderer armR;
    private final ModelRenderer armRpauldron;
    
    private final ModelRenderer pantsAnchor;
    private final ModelRenderer legL;
    private final ModelRenderer skirtL;
    private final ModelRenderer legR;
    private final ModelRenderer skirtR;
    
    private final ModelRenderer bootL;
    private final ModelRenderer bootR;
    
	public ModelArmorManaweave(EntityEquipmentSlot slot) {
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
        this.helm.addBox(-4.5F, -9.5F, -4.0F, 9, 11, 10, s);
        this.setRotateAngle(helm, 0.17453292519943295F, 0.0F, 0.0F);
        
        //body
        this.bodyAnchor = new ModelRenderer(this, 0, 0);
        this.bodyAnchor.setRotationPoint(0.0F, 0.0F, 0.0F);
        this.bodyAnchor.addBox(-1.0F, 0.0F, -1.0F, 2, 2, 2, s);
        this.bodyTop = new ModelRenderer(this, 0, 21);
        this.bodyTop.setRotationPoint(0.0F, 0.0F, 0.0F);
        this.bodyTop.addBox(-4.5F, -0.5F, -3.0F, 9, 7, 6, s);
        this.setRotateAngle(bodyTop, 0.0F, 0.0F, 0.0F);
		this.bodyBottom = new ModelRenderer(this, 0, 34);
        this.bodyBottom.setRotationPoint(0.0F, 0.0F, 0.0F);
        this.bodyBottom.addBox(-4.5F, 6.5F, -2.5F, 9, 5, 5, s);
        this.setRotateAngle(bodyBottom, -0F, 0.0F, 0.0F);
        
        //armL
        this.armLAnchor = new ModelRenderer(this, 0, 0);
        this.armLAnchor.mirror = true;
        this.armLAnchor.setRotationPoint(4.0F, 2.0F, 0.0F);
        this.armLAnchor.addBox(0.0F, -1.0F, -1.0F, 2, 2, 2, s);
        this.armL = new ModelRenderer(this, 0, 44);
        this.armL.mirror = true;
        this.armL.setRotationPoint(0.0F, 0.0F, 0.0F);
        this.armL.addBox(-1.5F, -2.5F, -2.49F, 5, 10, 5, s);
        this.armLpauldron = new ModelRenderer(this, 20, 44);
        this.armLpauldron.mirror = true;
        this.armLpauldron.setRotationPoint(0.0F, 0.0F, 0.0F);
        this.armLpauldron.addBox(-1.0F, -3.0F, -3.0F, 6, 5, 6, s);
        this.setRotateAngle(armLpauldron, 0.0F, 0.0F, -0.08726646259971647F);
        
        //armR
        this.armRAnchor = new ModelRenderer(this, 0, 0);
        this.armRAnchor.mirror = true;
        this.armRAnchor.setRotationPoint(-4.0F, 2.0F, 0.0F);
        this.armRAnchor.addBox(-2.0F, -1.0F, -1.0F, 2, 2, 2, s);
        this.armR = new ModelRenderer(this, 0, 44);
        this.armR.setRotationPoint(0.0F, 0.0F, 0.0F);
        this.armR.addBox(-3.5F, -2.5F, -2.51F, 5, 10, 5, s);
        this.armRpauldron = new ModelRenderer(this, 20, 44);
        this.armRpauldron.setRotationPoint(0.0F, 0.0F, 0.0F);
        this.armRpauldron.addBox(-5.0F, -3.0F, -3.0F, 6, 5, 6, s);
        this.setRotateAngle(armRpauldron, 0.0F, 0.0F, 0.08726646259971647F);
        
        //pants
        this.pantsAnchor = new ModelRenderer(this, 0, 0);
        this.pantsAnchor.setRotationPoint(0.0F, 0.0F, 0.0F);
        this.pantsAnchor.addBox(-1.0F, 0.0F, -1.0F, 2, 2, 2, s);
        this.legL = new ModelRenderer(this, 0, 78);
        this.legL.mirror = true;
        this.legL.setRotationPoint(1.9F, 12.0F, 0.0F);
        this.legL.addBox(-2.39F, -0.5F, -2.49F, 5, 6, 5, s);
        this.skirtL = new ModelRenderer(this, 0, 59);
        this.skirtL.mirror = true;
        this.skirtL.setRotationPoint(-0.5F, -2.0F, -2.5F);
        this.skirtL.addBox(-1.0F, 0.0F, -0.5F, 5, 13, 6, s);
        this.setRotateAngle(skirtL, 0.0F, -0.17453292519943295F, -0.2617993877991494F);
        this.legR = new ModelRenderer(this, 0, 78);
        this.legR.setRotationPoint(-1.9F, 12.0F, 0.0F);
        this.legR.addBox(-2.61F, 0.0F, -2.51F, 5, 6, 5, s);
		this.skirtR = new ModelRenderer(this, 0, 59);
        this.skirtR.setRotationPoint(0.5F, -2.0F, -2.5F);
        this.skirtR.addBox(-4.0F, 0.0F, -0.5F, 5, 13, 6, s);
        this.setRotateAngle(skirtR, 0.0F, 0.17453292519943295F, 0.2617993877991494F);
        
        //boot
        this.bootL = new ModelRenderer(this, 0, 89);
        this.bootL.mirror = true;
        this.bootL.setRotationPoint(1.9F, 12.0F, 0.0F);
        this.bootL.addBox(-2.39F, 8.5F, -2.49F, 5, 4, 5, s);
        this.bootR = new ModelRenderer(this, 0, 89);
        this.bootR.setRotationPoint(-1.9F, 12.0F, 0.0F);
        this.bootR.addBox(-2.61F, 8.5F, -2.51F, 5, 4, 5, s);
        
        //hierarchy
        this.helmAnchor.addChild(this.helm);
        
        this.bodyAnchor.addChild(this.bodyTop);
        this.bodyTop.addChild(this.bodyBottom);
        
        this.armLAnchor.addChild(this.armL);
        this.armL.addChild(this.armLpauldron);
        
        this.armRAnchor.addChild(this.armR);
        this.armR.addChild(this.armRpauldron);
        
        this.pantsAnchor.addChild(this.legL);
        this.pantsAnchor.addChild(this.legR);
        this.legL.addChild(this.skirtL);
        this.legR.addChild(this.skirtR);
	}

	@Override
	public void render(@Nonnull Entity entity, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch, float scale) {
		
		helmAnchor.showModel = slot == EntityEquipmentSlot.HEAD;
		bodyAnchor.showModel = slot == EntityEquipmentSlot.CHEST;
		armRAnchor.showModel = slot == EntityEquipmentSlot.CHEST;
		armLAnchor.showModel = slot == EntityEquipmentSlot.CHEST;
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
			bipedRightLeg = legR;
			bipedLeftLeg = legL;
		} else {
			bipedRightLeg = bootR;
			bipedLeftLeg = bootL;
		}

		super.render(entity, limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch, scale);
	}
}