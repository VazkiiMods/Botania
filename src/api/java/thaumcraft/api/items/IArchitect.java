package thaumcraft.api.items;

import java.util.ArrayList;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.world.World;

public interface IArchitect {

	/**
	 * Returns a list of blocks that should be highlighted in world.
	 */
	public ArrayList<BlockPos> getArchitectBlocks(ItemStack stack, World world, 
			BlockPos pos, EnumFacing side, EntityPlayer player);
	
	/**
	 * which axis should be displayed. 
	 */
	public boolean showAxis(ItemStack stack, World world, EntityPlayer player, EnumFacing side, 
			EnumAxis axis);
	
	public enum EnumAxis {
		X, // east / west
		Y, // up / down
		Z; // north / south
	}
}
