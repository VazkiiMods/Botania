/**
 * This class was created by <Vazkii>. It's distributed as
 * part of the Botania Mod. Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 *
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 *
 * File Created @ [Nov 14, 2014, 6:52:09 PM (GMT)]
 */
package vazkii.botania.client.render.tile;

import com.mojang.blaze3d.matrix.MatrixStack;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.tileentity.TileEntityRenderer;
import net.minecraft.client.renderer.tileentity.TileEntityRendererDispatcher;
import vazkii.botania.client.core.handler.RedStringRenderer;
import vazkii.botania.common.block.tile.string.TileRedString;

public class RenderTileRedString extends TileEntityRenderer<TileRedString> {

	public RenderTileRedString(TileEntityRendererDispatcher manager) {
		super(manager);
	}

	@Override
	public void render(TileRedString trs, float partialTicks, MatrixStack ms, IRenderTypeBuffer buffers, int light, int overlay) {
		// todo 1.15 move from RSR into the TESR itself
		RedStringRenderer.redStringTiles.add(trs);
	}

}

