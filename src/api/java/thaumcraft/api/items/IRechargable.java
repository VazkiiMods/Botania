package thaumcraft.api.items;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.BlockPos;
import net.minecraft.world.World;
import thaumcraft.api.aspects.Aspect;
import thaumcraft.api.aspects.AspectList;

/**
 * 
 * @author Azanor
 * 
 * Items with this interface can be recharged in wand pedestals and similar devices. 
 * The recharging needs to occur in handleRecharge and the passed in pos will be the 
 * player pos or the pos of the device doing the charging.
 * The recharge pedestal will simply call handleRecharge and let the item do whatever 
 * it would normally to recharge.
 * HandleRecharge should under normal conditions always be called every 5 ticks.
 * 
 */
public interface IRechargable {
	/**
	 * @param world
	 * @param is
	 * @param pos
	 * @param player The passed in player can be null
	 * @param amount how much vis to recharge - modified by things like the node tapper research
	 * @return the last aspect that was recharged
	 */
	public Aspect handleRecharge(World world, ItemStack is, BlockPos pos, EntityPlayer player, int amount);
	
	public AspectList getAspectsInChargable(ItemStack is);
	
}
