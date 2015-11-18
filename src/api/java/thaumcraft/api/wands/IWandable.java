package thaumcraft.api.wands;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.world.World;

/**
 *  
 * @author azanor
 * 
 * Add this to a tile entity that you wish wands to interact with in some way. 
 *
 */

public interface IWandable {

	public boolean onWandRightClick(World world, ItemStack wandstack, EntityPlayer player, BlockPos pos, EnumFacing side);
		
	public void onUsingWandTick(ItemStack wandstack, EntityPlayer player, int count);
	
	public void onWandStoppedUsing(ItemStack wandstack, World world, EntityPlayer player, int count);
	
}
