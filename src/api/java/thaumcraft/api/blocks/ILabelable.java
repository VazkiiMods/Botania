package thaumcraft.api.blocks;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;

/**
 * 
 * @author Azanor
 * 
 * Tile entities or blocks that extend this interface can have jar labels applied to them
 *
 */
public interface ILabelable {

	/**
	 * This method is used by the block or tileentity to do whatever needs doing.	 
	 * @return if true then label will be subtracted from player inventory
	 */
	public boolean applyLabel(EntityPlayer player, BlockPos pos, EnumFacing side, ItemStack labelstack);
	
}
