/*
 * This class is distributed as part of the Botania Mod.
 * Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 *
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 */
package vazkii.botania.api.item;

import net.minecraft.block.entity.BlockEntity;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.text.Text;

/**
 * Why would you ever want this ._.
 */
public class TinyPotatoRenderEvent extends Event {

	public final BlockEntity tile;
	public final Text name;
	public final float partTicks;
	public final MatrixStack ms;
	public final VertexConsumerProvider buffers;
	public final int light;
	public final int overlay;

	public TinyPotatoRenderEvent(BlockEntity tile, Text name, float partTicks, MatrixStack ms, VertexConsumerProvider buffers, int light, int overlay) {
		this.tile = tile;
		this.name = name;
		this.partTicks = partTicks;
		this.ms = ms;
		this.buffers = buffers;
		this.light = light;
		this.overlay = overlay;
	}
}
