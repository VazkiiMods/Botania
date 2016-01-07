/**
 * This class was created by <Vazkii>. It's distributed as
 * part of the Botania Mod. Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 * 
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 * 
 * File Created @ [Jul 8, 2014, 10:58:46 PM (GMT)]
 */
package vazkii.botania.client.render.tile;

import java.util.Random;

import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.BlockRendererDispatcher;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.ItemRenderer;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.block.model.ItemCameraTransforms;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.client.resources.model.IBakedModel;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;

import net.minecraft.util.BlockPos;
import net.minecraftforge.client.model.ISmartBlockModel;
import org.lwjgl.opengl.GL11;

import vazkii.botania.client.core.handler.ClientTickHandler;
import vazkii.botania.client.model.FloatingFlowerModel;
import vazkii.botania.client.model.ModelMiniIsland;
import vazkii.botania.common.block.decor.IFloatingFlower;
import vazkii.botania.common.block.tile.TileFloatingFlower;

public class RenderTileFloatingFlower extends TileEntitySpecialRenderer {

	private static final ModelMiniIsland model = new ModelMiniIsland();

	@Override
	public void renderTileEntityAt(TileEntity tile, double d0, double d1, double d2, float t, int digProgress) {
		BlockRendererDispatcher brd = Minecraft.getMinecraft().getBlockRendererDispatcher();
		IFloatingFlower flower = (IFloatingFlower) tile;
		GlStateManager.pushMatrix();
		GlStateManager.color(1F, 1F, 1F, 1F);
		GlStateManager.translate(d0, d1, d2 + 1);

		double worldTime = tile.getWorld() == null ? 0 : (double) (ClientTickHandler.ticksInGame + t);
		if(tile.getWorld() != null)
			worldTime += new Random(tile.getPos().hashCode()).nextInt(1000);

		GlStateManager.translate(0.5F, 0F, 0.5F);
		// todo 1.8.8 fix spin
		//GlStateManager.rotate(-((float) worldTime * 0.5F), 0F, 1F, 0F);
		GlStateManager.translate(-0.5F, 0F, -0.5F);

		if(tile.getWorld() != null) {
			GlStateManager.translate(0F, (float) Math.sin(worldTime * 0.05F) * 0.1F, 0F);
			GlStateManager.rotate(4F * (float) Math.sin(worldTime * 0.04F), 1F, 0F, 0F);
		}

		Minecraft.getMinecraft().renderEngine.bindTexture(TextureMap.locationBlocksTexture);
		GlStateManager.pushMatrix();

		IBlockState state = tile.getWorld().getBlockState(tile.getPos());
		state = state.getBlock().getExtendedState(state, tile.getWorld(), tile.getPos());
		IBakedModel ibakedmodel = FloatingFlowerModel.INSTANCE.handleBlockState(state);

		brd.getBlockModelRenderer().renderModelBrightness(ibakedmodel, state, 1.0F, true);

		GlStateManager.popMatrix();
		GlStateManager.popMatrix();

	}

}
