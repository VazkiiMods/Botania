/**
 * This class was created by <Vazkii>. It's distributed as
 * part of the Botania Mod. Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 *
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 *
 * File Created @ [Sep 2, 2014, 5:57:35 PM (GMT)]
 */
package vazkii.botania.api.wiki;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.world.World;

/**
 * An interface for a Wiki Provider, these are registered to allow a mod to provide a wiki
 * for all the blocks in them, used for the world interaction with the Lexica Botania.
 * A simple, mostly all-inclusive implementation can be found on SimpleWikiProvider.
 */
public interface IWikiProvider {

	/**
	 * Gets the name of the block being looked at for display.
	 */
	public String getBlockName(World world, RayTraceResult pos, EntityPlayer player);

	/**
	 * Gets the URL to open when the block is clicked.
	 */
	public String getWikiURL(World world, RayTraceResult pos, EntityPlayer player);

	/**
	 * Gets the name of the wiki for display.
	 */
	public String getWikiName(World world, RayTraceResult pos, EntityPlayer player);

}
