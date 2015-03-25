/**
 * This class was created by <Vazkii>. It's distributed as
 * part of the Botania Mod. Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 * 
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 * 
 * File Created @ [Mar 25, 2015, 5:49:28 PM (GMT)]
 */
package vazkii.botania.common.entity;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.boss.EntityWither;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.MathHelper;
import net.minecraft.world.World;

public class EntityPinkWither extends EntityWither {

	public EntityPinkWither(World p_i1701_1_) {
		super(p_i1701_1_);
	}
		
	@Override
	public void onLivingUpdate() {
		super.onLivingUpdate();
		
		if(Math.random() < 0.1)
			for(int j = 0; j < 3; ++j) {
				double d10 = this.func_82214_u(j);
				double d2 = this.func_82208_v(j);
				double d4 = this.func_82213_w(j);
				this.worldObj.spawnParticle("heart", d10 + this.rand.nextGaussian() * 0.30000001192092896D, d2 + this.rand.nextGaussian() * 0.30000001192092896D, d4 + this.rand.nextGaussian() * 0.30000001192092896D, 0.0D, 0.0D, 0.0D);
			}
	}
	
	@Override
	public void setAttackTarget(EntityLivingBase p_70624_1_) {
		// NO-OP
	}
	
	@Override
	protected void attackEntity(Entity p_70785_1_, float p_70785_2_) {
		// NO-OP
	}
	
	@Override
	public boolean attackEntityAsMob(Entity p_70652_1_) {
		return false;
	}
	
	@Override
	protected boolean interact(EntityPlayer player) {
		player.mountEntity(this);
		return true;
	}

	@Override
	protected boolean isAIEnabled() {
		return false;
	}
	
	// COPYPASTA
	
	
    private double func_82214_u(int p_82214_1_)
    {
        if (p_82214_1_ <= 0)
        {
            return this.posX;
        }
        else
        {
            float f = (this.renderYawOffset + (float)(180 * (p_82214_1_ - 1))) / 180.0F * (float)Math.PI;
            float f1 = MathHelper.cos(f);
            return this.posX + (double)f1 * 1.3D;
        }
    }

    private double func_82208_v(int p_82208_1_)
    {
        return p_82208_1_ <= 0 ? this.posY + 3.0D : this.posY + 2.2D;
    }

    private double func_82213_w(int p_82213_1_)
    {
        if (p_82213_1_ <= 0)
        {
            return this.posZ;
        }
        else
        {
            float f = (this.renderYawOffset + (float)(180 * (p_82213_1_ - 1))) / 180.0F * (float)Math.PI;
            float f1 = MathHelper.sin(f);
            return this.posZ + (double)f1 * 1.3D;
        }
    }
}
