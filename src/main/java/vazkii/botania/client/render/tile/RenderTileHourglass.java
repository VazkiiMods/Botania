/**
 * This class was created by <Vazkii>. It's distributed as
 * part of the Botania Mod. Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 * 
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 * 
 * File Created @ [May 29, 2015, 8:19:32 PM (GMT)]
 */
package vazkii.botania.client.render.tile;

import java.util.Random;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ResourceLocation;

import org.lwjgl.opengl.GL11;

import vazkii.botania.client.core.handler.ClientTickHandler;
import vazkii.botania.client.lib.LibResources;
import vazkii.botania.client.model.ModelHourglass;
import vazkii.botania.common.block.tile.TileHourglass;

public class RenderTileHourglass extends TileEntitySpecialRenderer {

	ResourceLocation texture = new ResourceLocation(LibResources.MODEL_HOURGLASS);
	ModelHourglass model = new ModelHourglass();

	@Override
	public void renderTileEntityAt(TileEntity tileentity, double d0, double d1, double d2, float ticks) {
		TileHourglass hourglass = (TileHourglass) tileentity;

		GL11.glPushMatrix();
		GL11.glEnable(GL11.GL_BLEND);
		GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
		GL11.glColor4f(1F, 1F, 1F, 1F);
		GL11.glTranslated(d0, d1, d2);
		Minecraft.getMinecraft().renderEngine.bindTexture(texture);

		int wtime = tileentity.getWorldObj() == null ? 0 : ClientTickHandler.ticksInGame;
		if(wtime != 0)
			wtime += new Random(tileentity.xCoord ^ tileentity.yCoord ^ tileentity.zCoord).nextInt(360);

		float time = wtime == 0 ? 0 : wtime + ticks;
		float x = 0.5F + (float) Math.cos(time * 0.05F) * 0.025F;
		float y = 0.55F + (float) (Math.sin(time * 0.04F) + 1F) * 0.05F;
		float z = 0.5F + (float) Math.sin(time * 0.05F) * 0.025F;
		ItemStack stack = hourglass.getStackInSlot(0);

		float fract1 = stack == null ? 0 : hourglass.timeFraction;
		float fract2 = stack == null ? 0 : 1F - hourglass.timeFraction;
		GL11.glTranslatef(x, y, z);

		float rot = hourglass.flip ? 180F : 1F;
		if(hourglass.flipTicks > 0)
			rot += (hourglass.flipTicks - ticks) * (180F / 4F);
		GL11.glRotatef(rot, 0F, 0F, 1F);

		GL11.glScalef(1F, -1F, -1F);
		model.render(fract1, fract2, hourglass.flip, hourglass.getColor());
		GL11.glScalef(1F, -1F, -1F);
		GL11.glPopMatrix();
	}

}
