/**
 * This class was created by <Vazkii>. It's distributed as
 * part of the Botania Mod. Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 *
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 *
 * File Created @ [Jun 9, 2014, 9:55:07 PM (GMT)]
 */
package vazkii.botania.client.render.tile;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import org.lwjgl.opengl.GL11;
import vazkii.botania.api.state.BotaniaStateProps;
import vazkii.botania.api.state.enums.AlfPortalState;
import vazkii.botania.client.core.handler.ClientTickHandler;
import vazkii.botania.client.core.handler.MiscellaneousIcons;
import vazkii.botania.client.core.proxy.ClientProxy;
import vazkii.botania.common.block.ModBlocks;
import vazkii.botania.common.block.tile.TileAlfPortal;

import javax.annotation.Nonnull;

public class RenderTileAlfPortal extends TileEntitySpecialRenderer<TileAlfPortal> {

	@Override
	public void render(@Nonnull TileAlfPortal portal, double d0, double d1, double d2, float f, int digProgress, float unused) {
		if (!portal.getWorld().isBlockLoaded(portal.getPos(), false)
				|| portal.getWorld().getBlockState(portal.getPos()).getBlock() != ModBlocks.alfPortal)
			return;

		AlfPortalState state = portal.getWorld().getBlockState(portal.getPos()).getValue(BotaniaStateProps.ALFPORTAL_STATE);

		if(state == AlfPortalState.OFF)
			return;

		GlStateManager.pushMatrix();
		GlStateManager.translate(d0, d1, d2);
		GlStateManager.translate(-1F, 1F, 0.25F);

		GlStateManager.enableBlend();
		GlStateManager.blendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
		GlStateManager.color(1F, 1F, 1F, 1F);
		GlStateManager.disableAlpha();
		GlStateManager.enableCull();
		float alpha = (float) Math.min(1F, (Math.sin((ClientTickHandler.ticksInGame + f) / 8D) + 1D) / 7D + 0.6D) * (Math.min(60, portal.ticksOpen) / 60F) * 0.5F;
		GlStateManager.color(1F, 1F, 1F, alpha);

		Minecraft.getMinecraft().renderEngine.bindTexture(TextureMap.LOCATION_BLOCKS_TEXTURE);

		if(state == AlfPortalState.ON_X) {
			GlStateManager.translate(1.25F, 0F, 1.75F);
			GlStateManager.rotate(90F, 0F, 1F, 0F);
		}

		GlStateManager.disableCull();
		GlStateManager.disableLighting();
		renderIcon(0, 0, MiscellaneousIcons.INSTANCE.alfPortalTex, 3, 3, 240);

		GlStateManager.blendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
		GlStateManager.translate(0F, 0F, 0.5F);
		renderIcon(0, 0, MiscellaneousIcons.INSTANCE.alfPortalTex, 3, 3, 240);

		GlStateManager.enableCull();
		GlStateManager.enableAlpha();
		GlStateManager.disableBlend();
		GlStateManager.enableLighting();
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
