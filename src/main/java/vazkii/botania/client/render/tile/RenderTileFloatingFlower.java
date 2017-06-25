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

import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.BlockRendererDispatcher;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.block.model.IBakedModel;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.tileentity.TileEntity;
import vazkii.botania.client.core.handler.ClientTickHandler;
import vazkii.botania.common.core.handler.ConfigHandler;

import javax.annotation.Nonnull;
import java.util.Random;

public class RenderTileFloatingFlower extends TileEntitySpecialRenderer {

	@Override
	public void render(@Nonnull TileEntity tile, double d0, double d1, double d2, float t, int digProgress, float unused) {
		if(ConfigHandler.staticFloaters)
			return;

		if (tile != null)
			if (!tile.getWorld().isBlockLoaded(tile.getPos(), false))
				return;


		BlockRendererDispatcher brd = Minecraft.getMinecraft().getBlockRendererDispatcher();
		GlStateManager.pushMatrix();
		GlStateManager.color(1F, 1F, 1F, 1F);
		GlStateManager.translate(d0, d1, d2);

		double worldTime = ClientTickHandler.ticksInGame + t;
		if(tile.getWorld() != null)
			worldTime += new Random(tile.getPos().hashCode()).nextInt(1000);

		GlStateManager.translate(0.5F, 0, 0.5F);
		GlStateManager.rotate(-((float) worldTime * 0.5F), 0F, 1F, 0F);
		GlStateManager.translate(-0.5, (float) Math.sin(worldTime * 0.05F) * 0.1F, 0.5);

		GlStateManager.rotate(4F * (float) Math.sin(worldTime * 0.04F), 1F, 0F, 0F);

		Minecraft.getMinecraft().renderEngine.bindTexture(TextureMap.LOCATION_BLOCKS_TEXTURE);

		IBlockState state = tile.getWorld().getBlockState(tile.getPos());
		state = state.getBlock().getExtendedState(state, tile.getWorld(), tile.getPos());
		IBakedModel model = brd.getBlockModelShapes().getModelManager().getModel(new ModelResourceLocation("botania:floatingSpecialFlower", "inventory"));
		brd.getBlockModelRenderer().renderModelBrightness(model, state, 1.0F, true);

		GlStateManager.popMatrix();

	}

}
