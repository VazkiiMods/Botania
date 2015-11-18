package thaumcraft.api.items;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;

/**
 * 
 * @author Azanor
 * 
 * Armor, held items or bauble slot items that implement this interface add warp when equipped or held.
 *
 */

public interface IWarpingGear {
	
	/**
	 * returns how much warp this item adds while worn or held. 
	 */
	public int getWarp(ItemStack itemstack, EntityPlayer player);
	

}
