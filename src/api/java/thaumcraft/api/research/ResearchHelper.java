package thaumcraft.api.research;

import net.minecraft.entity.player.EntityPlayer;
import thaumcraft.api.ThaumcraftApi;
import thaumcraft.api.internal.EnumWarpType;

public class ResearchHelper {

	public static boolean completeResearch(EntityPlayer player, String researchkey) {
		return ThaumcraftApi.internalMethods.completeResearch(player, researchkey);
	}

	public static boolean isResearchComplete(String username, String[] researchkeys) {
		for (String key:researchkeys) if (!ResearchHelper.isResearchComplete(username, key)) return false;
		return true;
	}

	public static boolean isResearchComplete(String username, String researchkey) {
		return ThaumcraftApi.internalMethods.isResearchComplete(username, researchkey);
	}

	/**
	 * This adds warp to a player. It will automatically be synced clientside
	 * @param player the player using the wand
	 * @param amount how much warp to add. Negative amounts are only valid for temporary warp
	 * @param type the type of warp to be added
	 */
	public static void addWarpToPlayer(EntityPlayer player, int amount, EnumWarpType type) {
		ThaumcraftApi.internalMethods.addWarpToPlayer(player, amount, type);
	}

	/**
	 * This retrieves how much warp the player has
	 * @param player the player using the wand
	 * @param type the type of warp to retrieve
	 * @return how much warp the player has
	 */
	public static int getPlayerWarp(EntityPlayer player, EnumWarpType type) {
		return ThaumcraftApi.internalMethods.getPlayerWarp(player, type);
	}

}
