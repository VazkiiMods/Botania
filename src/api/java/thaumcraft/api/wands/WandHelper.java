package thaumcraft.api.wands;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import thaumcraft.api.ThaumcraftApi;
import thaumcraft.api.aspects.AspectList;

public class WandHelper {

	/**
	 * Use to subtract vis from a wand for most operations
	 * @param wand the wand itemstack
	 * @param player the player using the wand
	 * @param cost the cost of the operation. 
	 * @param doit actually subtract the vis from the wand if true - if false just simulate the result
	 * @param crafting is this a crafting operation or not - if 
	 * false then things like frugal and potency will apply to the costs
	 * @return was the vis successfully subtracted
	 */
	public static boolean consumeVisFromWand(ItemStack wand, EntityPlayer player, 
			AspectList cost, boolean doit, boolean crafting) {
		return ThaumcraftApi.internalMethods.consumeVisFromWand(wand, player, cost, doit, crafting);
	}

	/**
	 * Subtract vis from a wand the player is carrying. The costs are handled like crafting however and things like 
	 * frugal don't effect them
	 * @param player the player using the wand
	 * @param cost the cost of the operation. 
	 * @return was the vis successfully subtracted
	 */
	public static boolean consumeVisFromInventory(EntityPlayer player, AspectList cost) {
		return ThaumcraftApi.internalMethods.consumeVisFromInventory(player, cost);
	}

}
