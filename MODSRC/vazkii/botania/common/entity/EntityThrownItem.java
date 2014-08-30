/**
 * This class was created by <Flaxbeard>. It's distributed as
 * part of the Botania Mod. Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 * 
 * Botania is Open Source and distributed under a
 * Creative Commons Attribution-NonCommercial-ShareAlike 3.0 License
 * (http://creativecommons.org/licenses/by-nc-sa/3.0/deed.en_GB)
 * 
 * File Created @ [? (GMT)]
 */
package vazkii.botania.common.entity;

import java.util.List;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.DamageSource;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.util.Vec3;
import net.minecraft.world.World;
import vazkii.botania.common.core.helper.Vector3;

public class EntityThrownItem extends EntityItem {

	public EntityThrownItem(World par1World) {
		super(par1World);
	}
	
	public EntityThrownItem(World p_i1710_1_, double p_i1710_2_,
			double p_i1710_4_, double p_i1710_6_, EntityItem item) {
		super(p_i1710_1_, p_i1710_2_, p_i1710_4_, p_i1710_6_, item.getEntityItem());

		this.delayBeforeCanPickup = item.delayBeforeCanPickup;
		this.motionX = item.motionX;
		this.motionY = item.motionY;
		this.motionZ = item.motionZ;
	}
	
	@Override
	public boolean isEntityInvulnerable() {
		return true;
	}
	
	@Override
	public void onUpdate() {
		super.onUpdate();
        Vec3 vec3 = Vec3.createVectorHelper(this.posX, this.posY, this.posZ);
        Vec3 vec31 = Vec3.createVectorHelper(this.posX + this.motionX, this.posY + this.motionY, this.posZ + this.motionZ);

        MovingObjectPosition movingobjectposition = this.worldObj.rayTraceBlocks(vec3, vec31);
        

		if (!this.worldObj.isRemote)
        {
            Entity entity = null;
            List list = this.worldObj.getEntitiesWithinAABBExcludingEntity(this, this.boundingBox.addCoord(this.motionX*2, this.motionY*2, this.motionZ*2).expand(2.0D, 2.0D, 2.0D));
            double d0 = 0.0D;

            for (int j = 0; j < list.size(); ++j)
            {
                Entity entity1 = (Entity)list.get(j);

                if (entity1.canBeCollidedWith() && (!(entity1 instanceof EntityPlayer) || this.delayBeforeCanPickup == 0))
                {
                    float f = 1.0F;
                    AxisAlignedBB axisalignedbb = entity1.boundingBox.expand((double)f, (double)f, (double)f);
                    MovingObjectPosition movingobjectposition1 = axisalignedbb.calculateIntercept(vec3, vec31);

                    if (movingobjectposition1 != null)
                    {
                        double d1 = vec3.distanceTo(movingobjectposition1.hitVec);

                        if (d1 < d0 || d0 == 0.0D)
                        {
                            entity = entity1;
                            d0 = d1;
                        }
                    }
                }
            }

            if (entity != null)
            {
                movingobjectposition = new MovingObjectPosition(entity);
            }
        }

        if (movingobjectposition != null)
        {
            if (movingobjectposition.typeOfHit == MovingObjectPosition.MovingObjectType.BLOCK && this.worldObj.getBlock(movingobjectposition.blockX, movingobjectposition.blockY, movingobjectposition.blockZ) == Blocks.portal)
            {
                this.setInPortal();
            }
            else
            {
            	if (movingobjectposition.entityHit != null) {
	            	movingobjectposition.entityHit.attackEntityFrom(DamageSource.magic, 2.0F);
	            	if (!this.worldObj.isRemote) {
	    				Entity item = this.getEntityItem().getItem().createEntity(worldObj, this, this.getEntityItem());
	    				if (item == null) {
	    					item = new EntityItem(worldObj, this.posX, this.posY, this.posZ, this.getEntityItem());
	    					worldObj.spawnEntityInWorld(item);
	    					item.motionX = this.motionX*0.25F;
	    					item.motionY = this.motionY*0.25F;
	    					item.motionZ = this.motionZ*0.25F;
	    	
	    				}
	    				else
	    				{
	    					item.motionX = this.motionX*0.25F;
	    					item.motionY = this.motionY*0.25F;
	    					item.motionZ = this.motionZ*0.25F;
	    				}
	    			}
	    			this.setDead();

            	}
            }
        }
		
		Vector3 vec3m = new Vector3(this.motionX, this.motionY, this.motionZ);
		if (vec3m.mag() < 1.0F) {
			if (!this.worldObj.isRemote) {
				System.out.println("3slo");
				Entity item = this.getEntityItem().getItem().createEntity(worldObj, this, this.getEntityItem());
				if (item == null) {
					item = new EntityItem(worldObj, this.posX, this.posY, this.posZ, this.getEntityItem());
					worldObj.spawnEntityInWorld(item);
					item.motionX = this.motionX;
					item.motionY = this.motionY;
					item.motionZ = this.motionZ;
	
				}
				else
				{
					item.motionX = this.motionX;
					item.motionY = this.motionY;
					item.motionZ = this.motionZ;
				}
			}
			this.setDead();
		}
	}
}
