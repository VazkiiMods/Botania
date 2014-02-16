/**
 * This class was created by <Vazkii>. It's distributed as
 * part of the Botania Mod. Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 * 
 * Botania is Open Source and distributed under a
 * Creative Commons Attribution-NonCommercial-ShareAlike 3.0 License
 * (http://creativecommons.org/licenses/by-nc-sa/3.0/deed.en_GB)
 * 
 * File Created @ [Feb 16, 2014, 12:37:40 AM (GMT)]
 */
package vazkii.botania.common.block.subtile.functional;

import java.util.List;

import cpw.mods.fml.common.network.PacketDispatcher;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.monster.IMob;
import net.minecraft.util.AxisAlignedBB;
import vazkii.botania.common.block.subtile.SubTileFunctional;

public class SubTileHeiseiDream extends SubTileFunctional {

	@Override
	public void onUpdate() {
		super.onUpdate();
		
		final int range = 5;
		final int cost = 100;
		
		List<IMob> mobs = supertile.worldObj.getEntitiesWithinAABB(IMob.class, AxisAlignedBB.getBoundingBox(supertile.xCoord - range, supertile.yCoord - range, supertile.zCoord - range, supertile.xCoord + range, supertile.yCoord + range, supertile.zCoord + range));
		if(mobs.size() > 1 && mana >= cost)
			for(IMob mob : mobs) {
				if(mob instanceof EntityLiving) {
					EntityLiving entity = (EntityLiving) mob;
					EntityLivingBase target = entity.getAttackTarget();
					if(target == null || !(target instanceof IMob)) {
						IMob newTarget;
						do newTarget = mobs.get(supertile.worldObj.rand.nextInt(mobs.size()));
						while(newTarget == mob);
						
						if(newTarget instanceof EntityLivingBase) {
							entity.setAttackTarget((EntityLivingBase) newTarget);
							System.out.println("target set "  + newTarget);
							mana -= cost;
							PacketDispatcher.sendPacketToAllInDimension(supertile.getDescriptionPacket(), supertile.worldObj.provider.dimensionId);
							break;
						}
					}	
				}
			}
	}
	
	@Override
	public int getColor() {
		return 0xFF219D;
	}
	
	@Override
	public int getMaxMana() {
		return 1000;
	}
	
}
