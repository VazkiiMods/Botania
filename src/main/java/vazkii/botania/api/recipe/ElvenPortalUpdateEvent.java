/**
 * This class was created by <Vazkii>. It's distributed as
 * part of the Botania Mod. Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 *
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 *
 * File Created @ [Apr 17, 2015, 4:58:30 PM (GMT)]
 */
package vazkii.botania.api.recipe;

import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraftforge.fml.common.eventhandler.Event;

import java.util.List;

/**
 * An event fired when an Elven Portal TE updates. The portal's
 * relevant AABB and other stuff is passed in for convenience. This
 * is fired in MinecraftForge.EVENT_BUS.
 */
public class ElvenPortalUpdateEvent extends Event {

	/**
	 * May be casted to TileAlfPortal if you have botania code access aside from the API.
	 */
	public final TileEntity portalTile;
	public final AxisAlignedBB aabb;
	public final boolean open;
	public final List<ItemStack> stacksInside;

	public ElvenPortalUpdateEvent(TileEntity te, AxisAlignedBB aabb, boolean open, List<ItemStack> stacks) {
		portalTile = te;
		this.aabb = aabb;
		this.open = open;
		stacksInside = stacks;
	}

}
