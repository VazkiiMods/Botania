/**
 * This class was created by <Vazkii>. It's distributed as
 * part of the Botania Mod. Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 * 
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 * 
 * File Created @ [Jan 21, 2014, 8:02:01 PM (GMT)]
 */
package vazkii.botania.client.render.block;

import net.minecraft.block.Block;
import net.minecraft.client.renderer.RenderBlocks;
import net.minecraft.client.renderer.tileentity.TileEntityRendererDispatcher;
import net.minecraft.world.IBlockAccess;

import org.lwjgl.opengl.GL11;

import vazkii.botania.client.lib.LibRenderIDs;
import vazkii.botania.client.render.tile.RenderTileAltar;
import vazkii.botania.common.block.tile.TileAltar;
import cpw.mods.fml.client.registry.ISimpleBlockRenderingHandler;

public class RenderAltar implements ISimpleBlockRenderingHandler {

	@Override
	public void renderInventoryBlock(Block block, int metadata, int modelID, RenderBlocks renderer) {
		GL11.glPushMatrix();
		GL11.glTranslatef(-0.5F, -0.6F, -0.5F);
		GL11.glScalef(0.9F, 0.9F, 0.9F);
		RenderTileAltar.forceMeta = metadata;
		TileEntityRendererDispatcher.instance.renderTileEntityAt(new TileAltar(), 0.0D, 0.0D, 0.0D, 0.0F);
		GL11.glPopMatrix();
	}

	@Override
	public boolean renderWorldBlock(IBlockAccess world, int x, int y, int z, Block block, int modelId, RenderBlocks renderer) {
		return false;
	}

	@Override
	public int getRenderId() {
		return LibRenderIDs.idAltar;
	}

	@Override
	public boolean shouldRender3DInInventory(int modelId) {
		return true;
	}
}
