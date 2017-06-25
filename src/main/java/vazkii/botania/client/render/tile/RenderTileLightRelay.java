/**
 * This class was created by <Vazkii>. It's distributed as
 * part of the Botania Mod. Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 *
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 *
 * File Created @ [Jul 16, 2015, 5:03:57 PM (GMT)]
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
import vazkii.botania.api.state.enums.LuminizerVariant;
import vazkii.botania.client.core.handler.ClientTickHandler;
import vazkii.botania.client.core.handler.MiscellaneousIcons;
import vazkii.botania.client.core.helper.ShaderHelper;
import vazkii.botania.client.core.proxy.ClientProxy;
import vazkii.botania.common.block.ModBlocks;
import vazkii.botania.common.block.tile.TileLightRelay;

import javax.annotation.Nonnull;
import java.util.HashMap;
import java.util.Map;

public class RenderTileLightRelay extends TileEntitySpecialRenderer<TileLightRelay> {

	private static Map<LuminizerVariant, TextureAtlasSprite> sprites = new HashMap();

	@Override
	public void render(@Nonnull TileLightRelay tile, double x, double y, double z, float pticks, int digProgress, float unused) {
		if(!tile.getWorld().isBlockLoaded(tile.getPos(), false) || tile.getWorld().getBlockState(tile.getPos()).getBlock() != ModBlocks.lightRelay)
			return;

		Minecraft mc = Minecraft.getMinecraft();
		if(sprites.isEmpty()) {
			sprites.put(LuminizerVariant.DEFAULT, MiscellaneousIcons.INSTANCE.lightRelayWorldIcon);
			sprites.put(LuminizerVariant.DETECTOR, MiscellaneousIcons.INSTANCE.lightRelayWorldIconRed);
			sprites.put(LuminizerVariant.FORK, MiscellaneousIcons.INSTANCE.lightRelayWorldIconGreen);
			sprites.put(LuminizerVariant.TOGGLE, MiscellaneousIcons.INSTANCE.lightRelayWorldIconPurple);
		}

		TextureAtlasSprite iicon = sprites.get(tile.getWorld().getBlockState(tile.getPos()).getValue(BotaniaStateProps.LUMINIZER_VARIANT));

		GlStateManager.pushMatrix();
		GlStateManager.translate(x + 0.5, y + 0.3, z + 0.5);
		GlStateManager.enableRescaleNormal();
		GlStateManager.enableBlend();
		GlStateManager.blendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
		GlStateManager.alphaFunc(GL11.GL_GREATER, 0.05F);

		double time = ClientTickHandler.ticksInGame + pticks;
		GlStateManager.color(1F, 1F, 1F, 1F);

		float scale = 0.75F;
		GlStateManager.scale(scale, scale, scale);
		Tessellator tessellator = Tessellator.getInstance();

		GlStateManager.pushMatrix();
		float r = 180.0F - mc.getRenderManager().playerViewY;
		GlStateManager.rotate(r, 0F, 1F, 0F);
		GlStateManager.rotate(-mc.getRenderManager().playerViewX, 1F, 0F, 0F);

		float off = 0.25F;
		GlStateManager.translate(0F, off, 0F);
		GlStateManager.rotate((float) time, 0F, 0F, 1F);
		GlStateManager.translate(0F, -off, 0F);

		mc.renderEngine.bindTexture(TextureMap.LOCATION_BLOCKS_TEXTURE);
		ShaderHelper.useShader(ShaderHelper.halo);
		renderIcon(tessellator, iicon);
		ShaderHelper.releaseShader();

		GlStateManager.popMatrix();
		GlStateManager.color(1F, 1F, 1F, 1F);
		GlStateManager.disableBlend();
		GlStateManager.disableRescaleNormal();
		GlStateManager.popMatrix();
	}

	private void renderIcon(Tessellator tess, TextureAtlasSprite icon) {
		float f = icon.getMinU();
		float f1 = icon.getMaxU();
		float f2 = icon.getMinV();
		float f3 = icon.getMaxV();
		float size = f1 - f;
		float pad = size / 8F;
		f += pad;
		f1 -= pad;
		f2 += pad;
		f3 -= pad;

		float f4 = 1.0F;
		float f5 = 0.5F;
		float f6 = 0.25F;

		tess.getBuffer().begin(GL11.GL_QUADS, ClientProxy.POSITION_TEX_LMAP_NORMAL);
		tess.getBuffer().pos(0.0F - f5, 0.0F - f6, 0.0D).tex(f, f3).lightmap(240, 240).normal(0, 1, 0).endVertex();
		tess.getBuffer().pos(f4 - f5, 0.0F - f6, 0.0D).tex(f1, f3).lightmap(240, 240).normal(0, 1, 0).endVertex();
		tess.getBuffer().pos(f4 - f5, f4 - f6, 0.0D).tex(f1, f2).lightmap(240, 240).normal(0, 1, 0).endVertex();
		tess.getBuffer().pos(0.0F - f5, f4 - f6, 0.0D).tex(f, f2).lightmap(240, 240).normal(0, 1, 0).endVertex();
		tess.draw();

	}

}
