/**
 * This class was created by <Vazkii>. It's distributed as
 * part of the Botania Mod. Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 * 
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 * 
 * File Created @ [Mar 22, 2015, 9:00:06 PM (GMT)]
 */
package vazkii.botania.client.render.block;

import net.minecraft.block.Block;
import net.minecraft.block.BlockDoublePlant;
import net.minecraft.client.renderer.RenderBlocks;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.util.IIcon;
import net.minecraft.world.IBlockAccess;
import vazkii.botania.client.lib.LibRenderIDs;
import cpw.mods.fml.client.registry.ISimpleBlockRenderingHandler;

public class RenderDoubleFlower implements ISimpleBlockRenderingHandler {

	@Override
	public void renderInventoryBlock(Block block, int metadata, int modelId, RenderBlocks renderer) {
		// NO-OP
	}

	@Override
	public boolean renderWorldBlock(IBlockAccess world, int p_147774_2_, int p_147774_3_, int p_147774_4_, Block block, int modelId, RenderBlocks renderer) {
		BlockDoublePlant p_147774_1_ = (BlockDoublePlant) block;
		Tessellator tessellator = Tessellator.instance;
		tessellator.setBrightness(p_147774_1_.getMixedBrightnessForBlock(world, p_147774_2_, p_147774_3_, p_147774_4_));
		tessellator.setColorOpaque_F(1F, 1F, 1F);
		long j1 = p_147774_2_ * 3129871 ^ p_147774_4_ * 116129781L;
		j1 = j1 * j1 * 42317861L + j1 * 11L;
		int i1 = world.getBlockMetadata(p_147774_2_, p_147774_3_, p_147774_4_);
		boolean flag1 = BlockDoublePlant.func_149887_c(i1);
		if (flag1)
		{
			if (world.getBlock(p_147774_2_, p_147774_3_ - 1, p_147774_4_) != p_147774_1_)
			{
				return false;
			}

			BlockDoublePlant.func_149890_d(world.getBlockMetadata(p_147774_2_, p_147774_3_ - 1, p_147774_4_));
		}
		else
		{
			BlockDoublePlant.func_149890_d(i1);
		}

		// Only change here, to use xyz rather than side/meta
		IIcon icon = renderer.getBlockIcon(block, world, p_147774_2_, p_147774_3_, p_147774_4_, 0);
		RenderSpecialFlower.drawCrossedSquares(world, block, icon, p_147774_2_, p_147774_3_, p_147774_4_, p_147774_2_, p_147774_3_, p_147774_4_, 1F, renderer);
		return true;
	}

	@Override
	public boolean shouldRender3DInInventory(int modelId) {
		return false;
	}

	@Override
	public int getRenderId() {
		return LibRenderIDs.idDoubleFlower;
	}

}
