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

import net.minecraft.client.renderer.entity.model.RendererModel;
import net.minecraft.client.renderer.entity.model.RendererModel;
import net.minecraft.entity.Entity;
import net.minecraft.inventory.EquipmentSlotType;
import net.minecraft.inventory.EquipmentSlotType;

public class ModelArmorElementium extends ModelArmor {
	
	private final RendererModel helmAnchor;
	private final RendererModel helm;
    private final RendererModel helmFairy;
    private final RendererModel helmWing1r;
    private final RendererModel helmWing2l;
	private final RendererModel helmWing1l;
    private final RendererModel helmWing2r;
	
	private final RendererModel bodyAnchor;
	private final RendererModel bodyTop;
	private final RendererModel bodyBottom;
	
	private final RendererModel armLAnchor;
	private final RendererModel armL;
	private final RendererModel armLpauldron;
	private final RendererModel armLwing1;
    private final RendererModel armLwing2;
    
	private final RendererModel armRAnchor;
	private final RendererModel armR;
	private final RendererModel armRpauldron;
    private final RendererModel armRwing1;
    private final RendererModel armRwing2;
	
	private final RendererModel pantsAnchor;
	private final RendererModel belt;
    private final RendererModel legL;
    private final RendererModel legR;
    
    private final RendererModel bootL;
    private final RendererModel bootLwing1;
    private final RendererModel bootLwing2;
	private final RendererModel bootR;
	private final RendererModel bootRwing1;
    private final RendererModel bootRwing2;
    
	public ModelArmorElementium(EquipmentSlotType slot) {
		super(slot);

		this.textureWidth = 64;
        this.textureHeight = 128;
		float s = 0.01F;
		
		//helm
        this.helmAnchor = new RendererModel(this, 0, 0);
        this.helmAnchor.setRotationPoint(0.0F, 0.0F, 0.0F);
        this.helmAnchor.addBox(-1.0F, -2.0F, 0.0F, 2, 2, 2, s);
        this.helm = new RendererModel(this, 0, 0);
        this.helm.setRotationPoint(0.0F, 0.0F, 0.0F);
        this.helm.addBox(-4.5F, -9.0F, -4.5F, 9, 9, 9, s);
        this.helmFairy = new RendererModel(this, 36, 11);
        this.helmFairy.setRotationPoint(0.0F, 0.0F, 0.0F);
        this.helmFairy.addBox(-2.5F, -10.0F, -5.5F, 5, 5, 5, s);
        this.helmWing1l = new RendererModel(this, 36, 0);
        this.helmWing1l.mirror = true;
        this.helmWing1l.setRotationPoint(4.5F, -6.0F, -0.5F);
        this.helmWing1l.addBox(-1.0F, -5.0F, 0.0F, 1, 5, 6, s);
        this.setRotateAngle(helmWing1l, 0.2617993877991494F, 0.5235987755982988F, 0.2617993877991494F);
        this.helmWing2l = new RendererModel(this, 50, 0);
        this.helmWing2l.setRotationPoint(4.5F, -6.0F, -0.5F);
        this.helmWing2l.addBox(-1.0F, 0.0F, 0.0F, 1, 3, 4, s);
        this.setRotateAngle(helmWing2l, -0.2617993877991494F, 0.2617993877991494F, -0.2617993877991494F);
        this.helmWing1r = new RendererModel(this, 36, 0);
        this.helmWing1r.setRotationPoint(-4.5F, -6.0F, -0.5F);
        this.helmWing1r.addBox(0.0F, -5.0F, 0.0F, 1, 5, 6, s);
        this.setRotateAngle(helmWing1r, 0.2617993877991494F, -0.5235987755982988F, -0.2617993877991494F);
        this.helmWing2r = new RendererModel(this, 50, 0);
        this.helmWing2r.mirror = true;
        this.helmWing2r.setRotationPoint(-4.5F, -6.0F, -0.5F);
        this.helmWing2r.addBox(0.0F, 0.0F, 0.0F, 1, 3, 4, s);
        this.setRotateAngle(helmWing2r, -0.2617993877991494F, -0.2617993877991494F, 0.2617993877991494F);
        
        //body
        this.bodyAnchor = new RendererModel(this, 0, 0);
        this.bodyAnchor.setRotationPoint(0.0F, 0.0F, 0.0F);
        this.bodyAnchor.addBox(-1.0F, 0.0F, -1.0F, 2, 2, 2, s);
        this.bodyTop = new RendererModel(this, 0, 19);
        this.bodyTop.setRotationPoint(0.0F, 0.0F, 0.0F);
        this.bodyTop.addBox(-4.5F, 0.0F, -3.0F, 9, 6, 6, s);
        this.bodyBottom = new RendererModel(this, 0, 31);
        this.bodyBottom.setRotationPoint(0.0F, 0.0F, 0.0F);
        this.bodyBottom.addBox(-3.5F, 5.0F, -2.5F, 7, 3, 5, s);
        
     	//armL
        this.armLAnchor = new RendererModel(this, 0, 0);
        this.armLAnchor.mirror = true;
        this.armLAnchor.setRotationPoint(4.0F, 2.0F, 0.0F);
        this.armLAnchor.addBox(0.0F, -1.0F, -1.0F, 2, 2, 2, s);
        this.armL = new RendererModel(this, 24, 40);
        this.armL.mirror = true;
        this.armL.setRotationPoint(0.0F, 0.0F, -0.0F);
        this.armL.addBox(0.5F, 4.5F, -2.49F, 3, 6, 5, s);
        this.armLpauldron = new RendererModel(this, 0, 40);
        this.armLpauldron.mirror = true;
        this.armLpauldron.setRotationPoint(0.0F, 0.0F, 0.0F);
        this.armLpauldron.addBox(-0.5F, -3.0F, -3.0F, 6, 7, 6, s);
        this.armLwing1 = new RendererModel(this, 40, 35);
        this.armLwing1.setRotationPoint(6.0F, -1.0F, 0.0F);
        this.armLwing1.addBox(0.0F, -4.0F, 0.0F, 0, 4, 5, s);
        this.setRotateAngle(armLwing1, 0.2617993877991494F, 0.5235987755982988F, 0.2617993877991494F);
        this.armLwing2 = new RendererModel(this, 40, 40);
        this.armLwing2.setRotationPoint(6.0F, -1.0F, 0.0F);
        this.armLwing2.addBox(0.0F, 0.0F, 0.0F, 0, 3, 4, s);
        this.setRotateAngle(armLwing2, -0.2617993877991494F, 0.2617993877991494F, -0.2617993877991494F);
        
        //armR
        this.armRAnchor = new RendererModel(this, 0, 0);
        this.armRAnchor.mirror = true;
        this.armRAnchor.setRotationPoint(-4.0F, 2.0F, 0.0F);
        this.armRAnchor.addBox(-2.0F, -1.0F, -1.0F, 2, 2, 2, s);
        this.armR = new RendererModel(this, 24, 40);
        this.armR.setRotationPoint(0.0F, 0.0F, 0.0F);
        this.armR.addBox(-3.5F, 4.5F, -2.51F, 3, 6, 5, s);
        this.armRpauldron = new RendererModel(this, 0, 40);
        this.armRpauldron.setRotationPoint(0.0F, 0.0F, 0.0F);
        this.armRpauldron.addBox(-5.5F, -3.0F, -3.0F, 6, 7, 6, s);
        this.setRotateAngle(armRpauldron, 0.0F, 0.0F, 0.0017453292519943296F);
        this.armRwing1 = new RendererModel(this, 40, 35);
        this.armRwing1.mirror = true;
        this.armRwing1.setRotationPoint(-6.5F, -1.0F, 0.0F);
        this.armRwing1.addBox(0.0F, -4.0F, 0.0F, 0, 4, 5, s);
        this.setRotateAngle(armRwing1, 0.2617993877991494F, -0.5235987755982988F, -0.2617993877991494F);
        this.armRwing2 = new RendererModel(this, 40, 40);
        this.armRwing2.mirror = true;
        this.armRwing2.setRotationPoint(-6.5F, -1.0F, 0.0F);
        this.armRwing2.addBox(0.0F, 0.0F, 0.0F, 0, 3, 4, s);
        this.setRotateAngle(armRwing2, -0.2617993877991494F, -0.2617993877991494F, 0.2617993877991494F);
        
        //pants
        this.pantsAnchor = new RendererModel(this, 0, 0);
        this.pantsAnchor.setRotationPoint(0.0F, 0.0F, 0.0F);
        this.pantsAnchor.addBox(-1.0F, 0.0F, -1.0F, 2, 2, 2, s);
        this.belt = new RendererModel(this, 0, 53);
        this.belt.setRotationPoint(0.0F, 0.0F, 0.0F);
        this.belt.addBox(-4.5F, 8.0F, -3.0F, 9, 5, 6, s);
        this.legL = new RendererModel(this, 0, 64);
        this.legL.mirror = true;
        this.legL.setRotationPoint(1.9F, 12.0F, 0.0F);
        this.legL.addBox(-2.39F, 0.0F, -2.49F, 5, 6, 5, s);
        this.legR = new RendererModel(this, 0, 64);
        this.legR.setRotationPoint(-1.9F, 12.0F, 0.0F);
        this.legR.addBox(-2.61F, 0.0F, -2.51F, 5, 6, 5, s);
        
        //boots
        this.bootL = new RendererModel(this, 0, 75);
        this.bootL.mirror = true;
        this.bootL.setRotationPoint(1.9F, 12.0F, 0.0F);
        this.bootL.addBox(-2.39F, 8.5F, -2.49F, 5, 5, 5, s);
        this.bootLwing1 = new RendererModel(this, 40, 40);
        this.bootLwing1.setRotationPoint(2.5F, 8.5F, 0.0F);
        this.bootLwing1.addBox(0.0F, -3.0F, 0.0F, 0, 3, 4, s);
        this.setRotateAngle(bootLwing1, 0.2617993877991494F, 0.5235987755982988F, 0.2617993877991494F);
		this.bootLwing2 = new RendererModel(this, 40, 44);
        this.bootLwing2.setRotationPoint(2.5F, 8.5F, 0.0F);
        this.bootLwing2.addBox(0.0F, 0.0F, 0.0F, 0, 2, 3, s);
        this.setRotateAngle(bootLwing2, -0.2617993877991494F, 0.2617993877991494F, -0.2617993877991494F);
		this.bootR = new RendererModel(this, 0, 75);
        this.bootR.setRotationPoint(-1.9F, 12.0F, 0.0F);
        this.bootR.addBox(-2.61F, 8.5F, -2.51F, 5, 5, 5, s);
		this.bootRwing1 = new RendererModel(this, 40, 40);
        this.bootRwing1.mirror = true;
        this.bootRwing1.setRotationPoint(-2.6F, 8.5F, 0.0F);
        this.bootRwing1.addBox(0.0F, -3.0F, 0.0F, 0, 3, 4, s);
        this.setRotateAngle(bootRwing1, 0.2617993877991494F, -0.5235987755982988F, -0.2617993877991494F);
		this.bootRwing2 = new RendererModel(this, 40, 44);
        this.bootRwing2.mirror = true;
        this.bootRwing2.setRotationPoint(-2.5F, 8.5F, 0.0F);
        this.bootRwing2.addBox(0.0F, 0.0F, 0.0F, 0, 2, 3, s);
        this.setRotateAngle(bootRwing2, -0.2617993877991494F, -0.2617993877991494F, 0.2617993877991494F);
        
        //hierarchy
        this.helmAnchor.addChild(this.helm);
        this.helm.addChild(this.helmFairy);
        this.helm.addChild(this.helmWing1l);
        this.helm.addChild(this.helmWing2l);
        this.helm.addChild(this.helmWing1r);
        this.helm.addChild(this.helmWing2r);
        
        this.bodyAnchor.addChild(this.bodyTop);
        this.bodyTop.addChild(this.bodyBottom);
        this.armLAnchor.addChild(this.armL);
        this.armL.addChild(this.armLpauldron);
        this.armLpauldron.addChild(this.armLwing1);
        this.armLpauldron.addChild(this.armLwing2);
        this.armRAnchor.addChild(this.armR);
        this.armR.addChild(this.armRpauldron);
        this.armRpauldron.addChild(this.armRwing1);
        this.armRpauldron.addChild(this.armRwing2);
        
        this.pantsAnchor.addChild(this.belt);
        this.belt.addChild(this.legL);
        this.belt.addChild(this.legR);;
        
        this.bootL.addChild(bootLwing1);
        this.bootL.addChild(bootLwing2);
        this.bootR.addChild(bootRwing1);
        this.bootR.addChild(bootRwing2);
	}

	@Override
	public void render(@Nonnull Entity entity, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch, float scale) {
		
		helmAnchor.showModel = slot == EquipmentSlotType.HEAD;
		bodyAnchor.showModel = slot == EquipmentSlotType.CHEST;
		armRAnchor.showModel = slot == EquipmentSlotType.CHEST;
		armLAnchor.showModel = slot == EquipmentSlotType.CHEST;
		legR.showModel = slot == EquipmentSlotType.LEGS;
		legL.showModel = slot == EquipmentSlotType.LEGS;
		bootL.showModel = slot == EquipmentSlotType.FEET;
		bootR.showModel = slot == EquipmentSlotType.FEET;
		bipedHeadwear.showModel = false;

		bipedHead = helmAnchor;
		bipedBody = bodyAnchor;
		bipedRightArm = armRAnchor;
		bipedLeftArm = armLAnchor;
		if(slot == EquipmentSlotType.LEGS) {
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