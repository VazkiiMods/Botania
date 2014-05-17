/**
 * This class was created by <Vazkii>. It's distributed as
 * part of the Botania Mod. Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 * 
 * Botania is Open Source and distributed under a
 * Creative Commons Attribution-NonCommercial-ShareAlike 3.0 License
 * (http://creativecommons.org/licenses/by-nc-sa/3.0/deed.en_GB)
 * 
 * File Created @ [May 17, 2014, 3:16:36 PM (GMT)]
 */
package vazkii.botania.common.item.equipment.bauble;

import java.util.List;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.item.ItemStack;
import net.minecraft.util.AxisAlignedBB;
import vazkii.botania.common.Botania;
import vazkii.botania.common.core.helper.MathHelper;
import vazkii.botania.common.core.helper.Vector3;
import vazkii.botania.common.lib.LibItemNames;
import baubles.api.BaubleType;

public class ItemMagnetRing extends ItemBauble {

	public ItemMagnetRing() {
		super(LibItemNames.MAGNET_RING);
	}

	@Override
	public void onWornTick(ItemStack stack, EntityLivingBase player) {
		super.onWornTick(stack, player);

		if(!player.isSneaking()) {
			int range = 6;
			double x = player.posX + 0.5;
			double y = player.posY -(player.worldObj.isRemote ? 1.62 : 0) + 0.75;
			double z = player.posZ + 0.5;

			List<EntityItem> items = player.worldObj.getEntitiesWithinAABB(EntityItem.class, AxisAlignedBB.getBoundingBox(x - range, y - range, z - range, x + range, y + range, z + range));
			for(EntityItem item : items) {
				MathHelper.setEntityMotionFromVector(item, new Vector3(x, y, z), 0.45F);
				if(player.worldObj.isRemote) {
					boolean red = player.worldObj.rand.nextBoolean();
					Botania.proxy.sparkleFX(player.worldObj, item.posX, item.posY, item.posZ, red ? 1F : 0F, 0F, red ? 0F : 1F, 1F, 3);
				}
			}
		}
	}

	@Override
	public BaubleType getBaubleType(ItemStack arg0) {
		return BaubleType.RING;
	}


}
