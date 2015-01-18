/**
 * This class was created by <Vazkii>. It's distributed as
 * part of the Botania Mod. Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 * 
 * Botania is Open Source and distributed under a
 * Creative Commons Attribution-NonCommercial-ShareAlike 3.0 License
 * (http://creativecommons.org/licenses/by-nc-sa/3.0/deed.en_GB)
 * 
 * File Created @ [Nov 14, 2014, 6:52:09 PM (GMT)]
 */
package vazkii.botania.client.render.tile;

import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.tileentity.TileEntity;
import vazkii.botania.client.core.handler.RedStringRenderer;
import vazkii.botania.common.block.tile.string.TileRedString;

public class RenderTileRedString extends TileEntitySpecialRenderer {

	@Override
	public void renderTileEntityAt(TileEntity tileentity, double x, double y, double z, float partticks) {
		TileRedString trs = (TileRedString) tileentity;
		RedStringRenderer.redStringTiles.add(trs);
	}
}

