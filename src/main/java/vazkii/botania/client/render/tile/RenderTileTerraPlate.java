/**
 * This class was created by <Vazkii>. It's distributed as
 * part of the Botania Mod. Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 *
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 *
 * File Created @ [Nov 8, 2014, 7:20:26 PM (GMT)]
 */
package vazkii.botania.client.render.tile;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.OpenGlHelper;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import org.lwjgl.opengl.GL11;
import vazkii.botania.client.core.handler.ClientTickHandler;
import vazkii.botania.client.core.handler.MiscellaneousIcons;
import vazkii.botania.client.core.helper.ShaderHelper;
import vazkii.botania.client.core.proxy.ClientProxy;
import vazkii.botania.common.block.tile.TileTerraPlate;

import javax.annotation.Nonnull;

public class RenderTileTerraPlate extends TileEntitySpecialRenderer<TileTerraPlate> {

	@Override
	public void render(@Nonnull TileTerraPlate plate, double d0, double d1, double d2, float f, int digProgress, float unused) {
		float max = TileTerraPlate.MAX_MANA / 10F;
		float alphaMod = Math.min(max, plate.getCurrentMana()) / max;
		GlStateManager.pushMatrix();
		GlStateManager.translate(d0, d1, d2);

		GlStateManager.rotate(90F, 1F, 0F, 0F);
		GlStateManager.translate(0F, 0F, -3F / 16F - 0.001F);

		GlStateManager.enableBlend();
		GlStateManager.blendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
		GlStateManager.color(1F, 1F, 1F, 1F);
		GlStateManager.disableAlpha();
		float alpha = (float) ((Math.sin((ClientTickHandler.ticksInGame + f) / 8D) + 1D) / 5D + 0.6D) * alphaMod;
		if(ShaderHelper.useShaders())
			GlStateManager.color(1F, 1F, 1F, alpha);
		else {
			int light = 15728880;
			int lightmapX = light % 65536;
			int lightmapY = light / 65536;
			OpenGlHelper.setLightmapTextureCoords(OpenGlHelper.lightmapTexUnit, lightmapX, lightmapY);
			GlStateManager.color(0.6F + (float) ((Math.cos((ClientTickHandler.ticksInGame + f) / 6D) + 1D) / 5D), 0.1F, 0.9F, alpha);
		}

		Minecraft.getMinecraft().renderEngine.bindTexture(TextureMap.LOCATION_BLOCKS_TEXTURE);

		ShaderHelper.useShader(ShaderHelper.terraPlateRune);
		renderIcon(0, 0, MiscellaneousIcons.INSTANCE.terraPlateOverlay, 1, 1, 240);
		ShaderHelper.releaseShader();

		GlStateManager.enableAlpha();
		GlStateManager.disableBlend();
		GlStateManager.color(1F, 1F, 1F, 1F);
		GlStateManager.popMatrix();
	}

	public void renderIcon(int par1, int par2, TextureAtlasSprite par3Icon, int par4, int par5, int brightness) {
		Tessellator tessellator = Tessellator.getInstance();
		tessellator.getBuffer().begin(GL11.GL_QUADS, ClientProxy.POSITION_TEX_LMAP);
		tessellator.getBuffer().pos(par1 + 0, par2 + par5, 0).tex(par3Icon.getMinU(), par3Icon.getMaxV()).lightmap(brightness, brightness).endVertex();
		tessellator.getBuffer().pos(par1 + par4, par2 + par5, 0).tex(par3Icon.getMaxU(), par3Icon.getMaxV()).lightmap(brightness, brightness).endVertex();
		tessellator.getBuffer().pos(par1 + par4, par2 + 0, 0).tex(par3Icon.getMaxU(), par3Icon.getMinV()).lightmap(brightness, brightness).endVertex();
		tessellator.getBuffer().pos(par1 + 0, par2 + 0, 0).tex(par3Icon.getMinU(), par3Icon.getMinV()).lightmap(brightness, brightness).endVertex();
		tessellator.draw();
	}

}
