/**
 * This class was created by <Vazkii>. It's distributed as
 * part of the Botania Mod. Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 * 
 * Botania is Open Source and distributed under a
 * Creative Commons Attribution-NonCommercial-ShareAlike 3.0 License
 * (http://creativecommons.org/licenses/by-nc-sa/3.0/deed.en_GB)
 * 
 * File Created @ [Apr 20, 2014, 10:58:00 PM (GMT)]
 */
package vazkii.botania.common.item.equipment.bauble;

import java.util.List;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.MovementInput;
import vazkii.botania.api.internal.IManaBurst;
import vazkii.botania.common.core.helper.Vector3;
import vazkii.botania.common.lib.LibItemNames;
import baubles.api.BaubleType;

public class ItemTinyPlanet extends ItemBauble {

	private static final String TAG_ORBIT = "orbit";
	
	public ItemTinyPlanet() {
		super(LibItemNames.TINY_PLANET);
	}

	@Override
	public BaubleType getBaubleType(ItemStack itemstack) {
		return BaubleType.AMULET;
	}
	
	@Override
	public void onWornTick(ItemStack stack, EntityLivingBase player) {
		int range = 8;
		List<Entity> entities = player.worldObj.getEntitiesWithinAABB(IManaBurst.class, AxisAlignedBB.getBoundingBox(player.posX - range, player.posY - range, player.posZ - range, player.posX + range, player.posY + range, player.posZ + range));
		for(Entity entity : entities) {
			IManaBurst burst = (IManaBurst) entity;
			int orbitTime = getEntityOrbitTime(entity);
			if(orbitTime == 0)
				burst.setMinManaLoss(burst.getMinManaLoss() * 4);
				
			float radius = (float) (Math.max(40, orbitTime) - 40) / 40F + 1F;
			int angle = orbitTime % 360;
			
			float xTarget = (float) ((int) player.posX - 0.5F + Math.cos((angle * 10) * Math.PI / 180F) * radius);
			float yTarget = (float) player.posY + 1.2F;
			float zTarget = (float) ((int) player.posZ + 0.5F + Math.sin((angle * 10) * Math.PI / 180F) * radius);
			if(player.worldObj.isRemote)
				yTarget -= 1.62F;
			
			Vector3 targetVec = new Vector3(xTarget, yTarget, zTarget);
			Vector3 currentVec = new Vector3(entity.posX, entity.posY, entity.posZ);
			Vector3 moveVector = targetVec.copy().sub(currentVec);
			
			burst.setMotion(moveVector.x, moveVector.y, moveVector.z);
			
			incrementOrbitTime(entity);
		}
	}
	
	public int getEntityOrbitTime(Entity entity) {
		NBTTagCompound cmp = entity.getEntityData();
		if(cmp.hasKey(TAG_ORBIT))
			return cmp.getInteger(TAG_ORBIT);
		else return 0;
	}
	
	public void incrementOrbitTime(Entity entity) {
		NBTTagCompound cmp = entity.getEntityData();
		int time = getEntityOrbitTime(entity);
		cmp.setInteger(TAG_ORBIT, time + 1);
	}

}
