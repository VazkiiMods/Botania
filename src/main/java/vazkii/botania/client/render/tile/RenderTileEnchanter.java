/**
 * This class was created by <Vazkii>. It's distributed as
 * part of the Botania Mod. Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 * 
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 * 
 * File Created @ [Mar 15, 2014, 5:04:42 PM (GMT)]
 */
package vazkii.botania.client.render.tile;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.OpenGlHelper;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.entity.Render;
import net.minecraft.client.renderer.entity.RenderItem;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.IIcon;

import org.lwjgl.opengl.GL11;

import vazkii.botania.client.core.handler.ClientTickHandler;
import vazkii.botania.client.core.helper.ShaderHelper;
import vazkii.botania.common.block.mana.BlockEnchanter;
import vazkii.botania.common.block.tile.TileEnchanter;

public class RenderTileEnchanter extends TileEntitySpecialRenderer {

	RenderItem renderItem = new RenderItem();
	EntityItem item;

	@Override
	public void renderTileEntityAt(TileEntity tileentity, double d0, double d1, double d2, float f) {
		TileEnchanter enchanter = (TileEnchanter) tileentity;
		float alphaMod = 0F;

		if(enchanter.stage == 2)
			alphaMod = Math.min(20, enchanter.stageTicks) / 20F;
		else if(enchanter.stage == 4)
			alphaMod = (20 - enchanter.stageTicks) / 20F;
		else if(enchanter.stage > 2)
			alphaMod = 1F;

		if(enchanter.itemToEnchant != null) {
			if(item == null)
				item = new EntityItem(enchanter.getWorldObj(), enchanter.xCoord, enchanter.yCoord + 1, enchanter.zCoord, enchanter.itemToEnchant);

			item.age = ClientTickHandler.ticksInGame;
			item.setEntityItemStack(enchanter.itemToEnchant);

			GL11.glColor4f(1F, 1F, 1F, 1F);
			GL11.glTranslatef(0.5F, 1.25F, 0.5F);
			((Render) RenderManager.instance.entityRenderMap.get(EntityItem.class)).doRender(item, d0, d1, d2, 1F, f);
			GL11.glTranslatef(-0.5F, -1.25F, -0.5F);
		}

		GL11.glPushMatrix();
		GL11.glTranslated(d0, d1, d2);

		GL11.glRotated(90F, 1F, 0F, 0F);
		GL11.glTranslatef(-2F, -2F, -0.001F);

		GL11.glEnable(GL11.GL_BLEND);
		GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
		GL11.glColor4f(1F, 1F, 1F, 1F);
		GL11.glDisable(GL11.GL_ALPHA_TEST);
		float alpha = (float) ((Math.sin((ClientTickHandler.ticksInGame + f) / 8D) + 1D) / 5D + 0.4D) * alphaMod;

		if(alpha > 0) {
			if(ShaderHelper.useShaders())
				GL11.glColor4f(1F, 1F, 1F, alpha);
			else {
				int light = 15728880;
				int lightmapX = light % 65536;
				int lightmapY = light / 65536;
				OpenGlHelper.setLightmapTextureCoords(OpenGlHelper.lightmapTexUnit, lightmapX, lightmapY);
				GL11.glColor4f(0.6F + (float) ((Math.cos((ClientTickHandler.ticksInGame + f) / 6D) + 1D) / 5D), 0.1F, 0.9F, alpha);
			}

			Minecraft.getMinecraft().renderEngine.bindTexture(TextureMap.locationBlocksTexture);

			if(enchanter.stage == 3 || enchanter.stage == 4) {
				int ticks = enchanter.stageTicks + enchanter.stage3EndTicks;
				int angle = ticks * 2;
				float yTranslation = Math.min(20, ticks) / 20F * 1.15F;
				float scale = ticks < 10 ? 1F : 1F - Math.min(20, ticks - 10) / 20F * 0.75F;

				GL11.glTranslatef(2.5F, 2.5F, -yTranslation);
				GL11.glScalef(scale, scale, 1F);
				GL11.glRotatef(angle, 0F, 0F, 1F);
				GL11.glTranslatef(-2.5F, -2.5F, 0F);
			}

			ShaderHelper.useShader(ShaderHelper.enchanterRune);
			renderIcon(0, 0, BlockEnchanter.overlay, 5, 5, 240);
			ShaderHelper.releaseShader();
		}

		GL11.glEnable(GL11.GL_ALPHA_TEST);
		GL11.glDisable(GL11.GL_BLEND);
		GL11.glColor4f(1F, 1F, 1F, 1F);
		GL11.glPopMatrix();
	}

	public void renderIcon(int par1, int par2, IIcon par3Icon, int par4, int par5, int brightness) {
		Tessellator tessellator = Tessellator.instance;
		tessellator.startDrawingQuads();
		tessellator.setBrightness(brightness);
		tessellator.addVertexWithUV(par1 + 0, par2 + par5, 0, par3Icon.getMinU(), par3Icon.getMaxV());
		tessellator.addVertexWithUV(par1 + par4, par2 + par5, 0, par3Icon.getMaxU(), par3Icon.getMaxV());
		tessellator.addVertexWithUV(par1 + par4, par2 + 0, 0, par3Icon.getMaxU(), par3Icon.getMinV());
		tessellator.addVertexWithUV(par1 + 0, par2 + 0, 0, par3Icon.getMinU(), par3Icon.getMinV());
		tessellator.draw();
	}

}
