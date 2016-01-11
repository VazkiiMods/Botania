/**
 * This class was created by <Vazkii>. It's distributed as
 * part of the Botania Mod. Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 * 
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 * 
 * File Created @ [Jul 25, 2015, 8:02:41 PM (GMT)]
 */
package vazkii.botania.api.item;

import net.minecraft.tileentity.TileEntity;
import net.minecraftforge.fml.common.eventhandler.Event;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

/**
 * Why would you ever want this ._.
 */
@SideOnly(Side.CLIENT)
public class TinyPotatoRenderEvent extends Event {

	public final TileEntity tile;
	public final String name;
	public final double x, y, z;
	public final float partTicks;
	public final int destroyStage;

	public TinyPotatoRenderEvent(TileEntity tile, String name, double x, double y, double z, float partTicks, int destroyStage) {
		this.tile = tile;
		this.name = name;
		this.x = x;
		this.y = y;
		this.z = z;
		this.partTicks = partTicks;
		this.destroyStage = destroyStage;
	}
}