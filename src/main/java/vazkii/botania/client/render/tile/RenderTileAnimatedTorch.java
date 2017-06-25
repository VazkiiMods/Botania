/**
 * This class was created by <Vazkii>. It's distributed as
 * part of the Botania Mod. Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 *
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 *
 * File Created @ [28/09/2016, 17:55:36 (GMT)]
 */
package vazkii.botania.client.render.tile;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.block.model.ItemCameraTransforms;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import vazkii.botania.client.core.handler.ClientTickHandler;
import vazkii.botania.common.block.tile.TileAnimatedTorch;

import java.util.Random;

public class RenderTileAnimatedTorch extends TileEntitySpecialRenderer<TileAnimatedTorch> {

	@Override
	public void render(TileAnimatedTorch te, double x, double y, double z, float partialTicks, int destroyStage, float unused) {
		Minecraft mc = Minecraft.getMinecraft();
		GlStateManager.pushMatrix();
		GlStateManager.translate(x, y, z);

		boolean hasWorld = te != null && te.getWorld() != null;
		int wtime = !hasWorld ? 0 : ClientTickHandler.ticksInGame;
		if(wtime != 0)
			wtime += new Random(te.getPos().hashCode()).nextInt(360);

		float time = wtime == 0 ? 0 : wtime + partialTicks;
		float xt = 0.5F + (float) Math.cos(time * 0.05F) * 0.025F;
		float yt = 0.1F + (float) (Math.sin(time * 0.04F) + 1F) * 0.05F;
		float zt = 0.5F + (float) Math.sin(time * 0.05F) * 0.025F;
		GlStateManager.translate(xt, yt, zt);

		GlStateManager.scale(2F, 2F, 2F);
		GlStateManager.rotate(90F, 1F, 0F, 0F);
		float rotation = (float) te.rotation;
		if(te.rotating)
			rotation += te.anglePerTick * partialTicks;

		GlStateManager.rotate(rotation, 0F, 0F, 1F);
		mc.getRenderItem().renderItem(new ItemStack(Blocks.REDSTONE_TORCH), ItemCameraTransforms.TransformType.GROUND);
		GlStateManager.popMatrix();
	}

}
