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

import com.mojang.blaze3d.matrix.MatrixStack;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.text.ITextComponent;
import net.minecraftforge.eventbus.api.Event;

/**
 * Why would you ever want this ._.
 */
public class TinyPotatoRenderEvent extends Event {

	public final TileEntity tile;
	public final ITextComponent name;
	public final float partTicks;
	public final MatrixStack ms;
	public final IRenderTypeBuffer buffers;
	public final int light;
	public final int overlay;

	public TinyPotatoRenderEvent(TileEntity tile, ITextComponent name, float partTicks, MatrixStack ms, IRenderTypeBuffer buffers, int light, int overlay) {
		this.tile = tile;
		this.name = name;
		this.partTicks = partTicks;
		this.ms = ms;
		this.buffers = buffers;
		this.light = light;
		this.overlay = overlay;
	}
}