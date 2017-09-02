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

import net.minecraft.client.renderer.BufferBuilder;
import net.minecraftforge.client.model.animation.FastTESR;
import vazkii.botania.client.core.handler.RedStringRenderer;
import vazkii.botania.common.block.tile.string.TileRedString;

import javax.annotation.Nonnull;

public class RenderTileRedString extends FastTESR<TileRedString> {

	@Override
	public void renderTileEntityFast(@Nonnull TileRedString trs, double x, double y, double z, float partialTicks, int destroyStage, float unused, @Nonnull BufferBuilder worldRenderer) {
		RedStringRenderer.redStringTiles.add(trs);
	}

}

