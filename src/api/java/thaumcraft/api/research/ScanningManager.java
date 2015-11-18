package thaumcraft.api.research;

import java.util.ArrayList;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.StatCollector;

public class ScanningManager {
	
	static ArrayList<IScanThing> things = new ArrayList<IScanThing>();
	
	/**
	 * Add things to scan
	 * @example 
	 * <i>ScanManager.addScannableThing(new ScanItem("HIPSTER",new ItemStack(Items.apple,1,OreDictionary.WILDCARD_VALUE)));</i><br>
	 * This will unlock the <b>HIPSTER</b> research if you scan any kind of apple.
	 */	
	public static void addScannableThing(IScanThing obj) {		
		things.add(obj);		
	}
	
	/**
	 * 
	 * @param player
	 * @param object this could in theory be anything, but vanilla tc scanning tools only pass in Entity, BlockPos, Itemstack or null
	 */
	public static void scanTheThing(EntityPlayer player, Object object) {
		boolean found = false;
		for (IScanThing thing:things) {
			if (thing.checkThing(player, object)) {				
				if (ResearchHelper.completeResearch(player, thing.getResearchKey())) {					
					found=true;
				}
			}			
		}
		if (!found) {
			player.addChatMessage(new ChatComponentText("\u00a75\u00a7o"+StatCollector.translateToLocal("tc.unknownobject")));
		}
	}
	
	/**
	 * @param player
	 * @param object
	 * @return true if the object can be scanned for research the player has not yet discovered
	 */
	public static boolean isThingStillScannable(EntityPlayer player, Object object) {		
		for (IScanThing thing:things) {
			if (thing.checkThing(player, object)) {				
				if (!ResearchHelper.isResearchComplete(player.getCommandSenderName(), thing.getResearchKey())) {
					return true;
				}
			}			
		}
		return false;
	}
		
}
