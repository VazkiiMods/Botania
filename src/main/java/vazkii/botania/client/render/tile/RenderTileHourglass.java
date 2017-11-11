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

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import org.lwjgl.opengl.GL11;
import vazkii.botania.client.core.handler.ClientTickHandler;
import vazkii.botania.client.lib.LibResources;
import vazkii.botania.client.model.ModelHourglass;
import vazkii.botania.common.block.tile.TileHourglass;

import javax.annotation.Nullable;
import java.util.Random;

public class RenderTileHourglass extends TileEntitySpecialRenderer<TileHourglass> {

	final ResourceLocation texture = new ResourceLocation(LibResources.MODEL_HOURGLASS);
	final ModelHourglass model = new ModelHourglass();

	@Override
	public void render(@Nullable TileHourglass hourglass, double d0, double d1, double d2, float ticks, int digProgress, float unused) {
		GlStateManager.pushMatrix();
		GlStateManager.enableBlend();
		GlStateManager.blendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
		GlStateManager.color(1F, 1F, 1F, 1F);
		GlStateManager.translate(d0, d1, d2);
		Minecraft.getMinecraft().renderEngine.bindTexture(texture);
		boolean hasWorld = hourglass != null && hourglass.getWorld() != null;
		int wtime = !hasWorld ? 0 : ClientTickHandler.ticksInGame;
		if(wtime != 0)
			wtime += new Random(hourglass.getPos().hashCode()).nextInt(360);

		float time = wtime == 0 ? 0 : wtime + ticks;
		float x = 0.5F + (float) Math.cos(time * 0.05F) * 0.025F;
		float y = 0.55F + (float) (Math.sin(time * 0.04F) + 1F) * 0.05F;
		float z = 0.5F + (float) Math.sin(time * 0.05F) * 0.025F;
		ItemStack stack = hasWorld ? hourglass.getItemHandler().getStackInSlot(0) : ItemStack.EMPTY;

		float activeFraction = stack.isEmpty() ? 0 : hourglass.lastFraction + (hourglass.timeFraction - hourglass.lastFraction) * ticks;
		float fract1 = stack.isEmpty() ? 0 : activeFraction;
		float fract2 = stack.isEmpty() ? 0 : 1F - activeFraction;
		GlStateManager.translate(x, y, z);

		float rot = hasWorld && hourglass.flip ? 180F : 1F;
		if(hasWorld && hourglass.flipTicks > 0)
			rot += (hourglass.flipTicks - ticks) * (180F / 4F);
		GlStateManager.rotate(rot, 0F, 0F, 1F);

		GlStateManager.scale(1F, -1F, -1F);
		model.render(fract1, fract2, hasWorld && hourglass.flip, hasWorld ? hourglass.getColor() : 0);
		GlStateManager.scale(1F, -1F, -1F);
		GlStateManager.popMatrix();
	}

}
