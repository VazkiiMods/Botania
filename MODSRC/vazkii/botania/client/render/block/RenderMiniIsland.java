/**
 * This class was created by <Vazkii>. It's distributed as
 * part of the Botania Mod. Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 * 
 * Botania is Open Source and distributed under a
 * Creative Commons Attribution-NonCommercial-ShareAlike 3.0 License
 * (http://creativecommons.org/licenses/by-nc-sa/3.0/deed.en_GB)
 * 
 * File Created @ [Jul 8, 2014, 11:05:38 PM (GMT)]
 */
package vazkii.botania.client.render.block;

import net.minecraft.block.Block;
import net.minecraft.client.renderer.RenderBlocks;
import net.minecraft.client.renderer.tileentity.TileEntityRendererDispatcher;
import net.minecraft.world.IBlockAccess;

import org.lwjgl.opengl.GL11;

import vazkii.botania.client.lib.LibRenderIDs;
import vazkii.botania.client.render.tile.RenderTileMiniIsland;
import vazkii.botania.client.render.tile.RenderTilePool;
import vazkii.botania.common.block.tile.TileMiniIsland;
import vazkii.botania.common.block.tile.mana.TilePool;
import cpw.mods.fml.client.registry.ISimpleBlockRenderingHandler;

public class RenderMiniIsland implements ISimpleBlockRenderingHandler {

	@Override
	public void renderInventoryBlock(Block block, int metadata, int modelId, RenderBlocks renderer) {
		GL11.glPushMatrix();
		GL11.glTranslatef(-0.5F, -0.5F, -0.5F);
		float s = 1.4F;
		GL11.glScalef(s, s, s);
		GL11.glRotatef(-5F, 1F, 0F, 0F);
		RenderTileMiniIsland.forcedMetadata = metadata;
		TileEntityRendererDispatcher.instance.renderTileEntityAt(new TileMiniIsland(), 0.0D, 0.0D, 0.0D, 0.0F);
		GL11.glPopMatrix();
	}

	@Override
	public boolean renderWorldBlock(IBlockAccess world, int x, int y, int z, Block block, int modelId, RenderBlocks renderer) {
		return false;
	}

	@Override
	public boolean shouldRender3DInInventory(int modelId) {
		return true;
	}

	@Override
	public int getRenderId() {
		return LibRenderIDs.idMiniIsland;
	}

}
