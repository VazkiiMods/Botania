/**
 * This class was created by <Vazkii>. It's distributed as
 * part of the Botania Mod. Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 * 
 * Botania is Open Source and distributed under a
 * Creative Commons Attribution-NonCommercial-ShareAlike 3.0 License
 * (http://creativecommons.org/licenses/by-nc-sa/3.0/deed.en_GB)
 * 
 * File Created @ [Jun 20, 2014, 11:43:02 PM (GMT)]
 */
package vazkii.botania.common.block.subtile.functional;

import java.util.List;

import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.AxisAlignedBB;
import net.minecraftforge.common.util.ForgeDirection;
import vazkii.botania.api.subtile.SubTileFunctional;
import vazkii.botania.common.Botania;

public class SubTileDaffomill extends SubTileFunctional {

	private static final String TAG_ORIENTATION = "orientation";
	private static final String TAG_WIND_TICKS = "windTicks";

	int windTicks = 0;
	int orientation = 0;
	
	@Override
	public void onUpdate() {
		super.onUpdate();
		
		ForgeDirection dir = ForgeDirection.getOrientation(orientation + 2);
		if(supertile.getWorldObj().rand.nextInt(4) == 0)
			Botania.proxy.wispFX(supertile.getWorldObj(), supertile.xCoord + Math.random(), supertile.yCoord + Math.random(), supertile.zCoord + Math.random(), 0.05F, 0.05F, 0.05F, 0.25F + (float) Math.random() * 0.15F, dir.offsetX * 0.1F, dir.offsetY * 0.1F, dir.offsetZ * 0.1F);
	
		if(windTicks == 0 && mana > 0) {
			windTicks = 20;
			mana--;
		}
		
		if(windTicks > 0) {
			int x = supertile.xCoord;
			int y = supertile.yCoord;
			int z = supertile.zCoord;
			int w = 2;
			int h = 3;
			int l = 16;
			
			AxisAlignedBB axis = null;
			switch(orientation) {
				case 0 : 
					axis = AxisAlignedBB.getBoundingBox(x - w, y - h, z - l, x + w + 1, y + h, z);
					break;
				case 1 :
					axis = AxisAlignedBB.getBoundingBox(x - w, y - h, z + 1, x + w + 1, y + h, z + l + 1);
					break;
				case 2 :
					axis = AxisAlignedBB.getBoundingBox(x - l, y - h, z - w, x, y + h, z + w + 1);
					break;
				case 3 :
					axis = AxisAlignedBB.getBoundingBox(x + 1, y - h, z - w, x + l + 1, y + h, z + w + 1);
			}
			
			if(axis != null) {
				List<EntityItem> items = supertile.getWorldObj().getEntitiesWithinAABB(EntityItem.class, axis);
				for(EntityItem item : items) {
					item.motionX += (double) dir.offsetX * 0.05;
					item.motionY += (double) dir.offsetY * 0.05;
					item.motionZ += (double) dir.offsetZ * 0.05;
				}
			}
			
			windTicks--;
		}
	}
	
	@Override
	public boolean onWanded(EntityPlayer player, ItemStack wand) {
		if(player == null)
			return false;

		if(player.isSneaking()) {
			orientation = orientation == 3 ? 0 : orientation + 1;
			sync();

			return true;
		}
		else return super.onWanded(player, wand);
	}
	
	@Override
	public int getColor() {
		return 0xD8BA00;
	}
	
	@Override
	public int getMaxMana() {
		return 100;
	}
	
	@Override
	public void writeToPacketNBT(NBTTagCompound cmp) {
		super.writeToPacketNBT(cmp);
		
		cmp.setInteger(TAG_ORIENTATION, orientation);
		cmp.setInteger(TAG_WIND_TICKS, windTicks);
	}
	
	@Override
	public void readFromPacketNBT(NBTTagCompound cmp) {
		super.readFromPacketNBT(cmp);
		
		orientation = cmp.getInteger(TAG_ORIENTATION);
		windTicks = cmp.getInteger(TAG_WIND_TICKS);
	}
	
}
