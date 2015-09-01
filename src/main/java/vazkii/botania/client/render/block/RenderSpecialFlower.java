/**
 * This class was created by <Vazkii>. It's distributed as
 * part of the Botania Mod. Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 * 
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 * 
 * File Created @ [Jan 25, 2014, 4:15:46 PM (GMT)]
 */
package vazkii.botania.client.render.block;

import net.minecraft.block.Block;
import net.minecraft.client.renderer.EntityRenderer;
import net.minecraft.client.renderer.RenderBlocks;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.util.IIcon;
import net.minecraft.world.IBlockAccess;
import vazkii.botania.api.lexicon.multiblock.IMultiblockRenderHook;
import vazkii.botania.api.lexicon.multiblock.Multiblock;
import vazkii.botania.api.lexicon.multiblock.component.MultiblockComponent;
import cpw.mods.fml.client.registry.ISimpleBlockRenderingHandler;

public class RenderSpecialFlower implements ISimpleBlockRenderingHandler, IMultiblockRenderHook {

	int id;

	public RenderSpecialFlower(int id) {
		this.id = id;
	}

	@Override
	public void renderInventoryBlock(Block block, int metadata, int modelID, RenderBlocks renderer) {
		// NO-OP
	}

	@Override
	public boolean renderWorldBlock(IBlockAccess blockAccess, int x, int y, int z, Block block, int modelId, RenderBlocks renderer) {
		return renderCrossedSquares(blockAccess, block, x, y, z, renderer);
	}

	// Copied from RenderBlocks
	public static boolean renderCrossedSquares(IBlockAccess blockAccess, Block par1Block, int par2, int par3, int par4, RenderBlocks render) {
		Tessellator tessellator = Tessellator.instance;
		tessellator.setBrightness(par1Block.getMixedBrightnessForBlock(blockAccess, par2, par3, par4));
		float f = 1.0F;
		int l = par1Block.colorMultiplier(blockAccess, par2, par3, par4);
		float f1 = (l >> 16 & 255) / 255.0F;
		float f2 = (l >> 8 & 255) / 255.0F;
		float f3 = (l & 255) / 255.0F;

		if(EntityRenderer.anaglyphEnable) {
			float f4 = (f1 * 30.0F + f2 * 59.0F + f3 * 11.0F) / 100.0F;
			float f5 = (f1 * 30.0F + f2 * 70.0F) / 100.0F;
			float f6 = (f1 * 30.0F + f3 * 70.0F) / 100.0F;
			f1 = f4;
			f2 = f5;
			f3 = f6;
		}

		tessellator.setColorOpaque_F(f * f1, f * f2, f * f3);
		double d1 = par2;
		double d2 = par3;
		double d0 = par4;
		long sh;

		sh = par2 * 3129871 ^ par4 * 116129781L ^ par3;
		sh = sh * sh * 42317861L + sh * 11L;
		d1 += ((sh >> 16 & 15L) / 15.0F - 0.5D) * 0.3D;
		d2 += (sh >> 32 & 15L) / 15.0F * -0.15D;
		d0 += ((sh >> 24 & 15L) / 15.0F - 0.5D) * 0.3D;


		// Only change here, to use xyz rather than side/meta
		IIcon icon = render.getBlockIcon(par1Block, blockAccess, par2, par3, par4, 0);
		drawCrossedSquares(blockAccess, par1Block, icon, par2, par3, par4, d1, d2, d0, 1.0F, render);

		return true;
	}

	// Copied from RenderBlocks
	public static void drawCrossedSquares(IBlockAccess blockAccess, Block par1Block, IIcon icon, int x, int y, int z, double par3, double par5, double par7, float par9, RenderBlocks render) {
		Tessellator tessellator = Tessellator.instance;

		double d3 = icon.getMinU();
		double d4 = icon.getMinV();
		double d5 = icon.getMaxU();
		double d6 = icon.getMaxV();
		double d7 = 0.45D * par9;
		double d8 = par3 + 0.5D - d7;
		double d9 = par3 + 0.5D + d7;
		double d10 = par7 + 0.5D - d7;
		double d11 = par7 + 0.5D + d7;
		tessellator.addVertexWithUV(d8, par5 + par9, d10, d3, d4);
		tessellator.addVertexWithUV(d8, par5 + 0.0D, d10, d3, d6);
		tessellator.addVertexWithUV(d9, par5 + 0.0D, d11, d5, d6);
		tessellator.addVertexWithUV(d9, par5 + par9, d11, d5, d4);
		tessellator.addVertexWithUV(d9, par5 + par9, d11, d3, d4);
		tessellator.addVertexWithUV(d9, par5 + 0.0D, d11, d3, d6);
		tessellator.addVertexWithUV(d8, par5 + 0.0D, d10, d5, d6);
		tessellator.addVertexWithUV(d8, par5 + par9, d10, d5, d4);
		tessellator.addVertexWithUV(d8, par5 + par9, d11, d3, d4);
		tessellator.addVertexWithUV(d8, par5 + 0.0D, d11, d3, d6);
		tessellator.addVertexWithUV(d9, par5 + 0.0D, d10, d5, d6);
		tessellator.addVertexWithUV(d9, par5 + par9, d10, d5, d4);
		tessellator.addVertexWithUV(d9, par5 + par9, d10, d3, d4);
		tessellator.addVertexWithUV(d9, par5 + 0.0D, d10, d3, d6);
		tessellator.addVertexWithUV(d8, par5 + 0.0D, d11, d5, d6);
		tessellator.addVertexWithUV(d8, par5 + par9, d11, d5, d4);
	}

	@Override
	public int getRenderId() {
		return id;
	}

	@Override
	public boolean shouldRender3DInInventory(int modelId) {
		return false;
	}

	@Override
	public void renderBlockForMultiblock(IBlockAccess world, Multiblock mb, Block block, int meta, RenderBlocks renderBlocks, MultiblockComponent comp, float alpha) {
		Tessellator tess = Tessellator.instance;
		tess.startDrawingQuads();
		drawCrossedSquares(world, block, block.getIcon(0, meta), 0, 0, 0, -0.5, -0.5, -0.5, 1F, renderBlocks);
		tess.draw();
	}

	@Override
	public boolean needsTranslate(Block block) {
		return true;
	}

}
