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

import com.mojang.blaze3d.platform.GlStateManager;
import net.minecraft.block.BlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.texture.AtlasTexture;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.renderer.tileentity.TileEntityRenderer;
import org.lwjgl.opengl.GL11;
import vazkii.botania.api.state.enums.LuminizerVariant;
import vazkii.botania.client.core.handler.ClientTickHandler;
import vazkii.botania.client.core.handler.MiscellaneousIcons;
import vazkii.botania.client.core.helper.ShaderHelper;
import vazkii.botania.client.core.proxy.ClientProxy;
import vazkii.botania.common.block.BlockLightRelay;
import vazkii.botania.common.block.tile.TileLightRelay;

import javax.annotation.Nonnull;
import java.util.EnumMap;
import java.util.Map;

public class RenderTileLightRelay extends TileEntityRenderer<TileLightRelay> {

	private static Map<LuminizerVariant, TextureAtlasSprite> sprites = new EnumMap<>(LuminizerVariant.class);

	@Override
	public void render(@Nonnull TileLightRelay tile, double x, double y, double z, float pticks, int digProgress) {
		if(!tile.getWorld().isBlockLoaded(tile.getPos()))
			return;

		BlockState state = tile.getWorld().getBlockState(tile.getPos());
		if(!(state.getBlock() instanceof BlockLightRelay))
			return;

		Minecraft mc = Minecraft.getInstance();
		if(sprites.isEmpty()) {
			sprites.put(LuminizerVariant.DEFAULT, MiscellaneousIcons.INSTANCE.lightRelayWorldIcon);
			sprites.put(LuminizerVariant.DETECTOR, MiscellaneousIcons.INSTANCE.lightRelayWorldIconRed);
			sprites.put(LuminizerVariant.FORK, MiscellaneousIcons.INSTANCE.lightRelayWorldIconGreen);
			sprites.put(LuminizerVariant.TOGGLE, MiscellaneousIcons.INSTANCE.lightRelayWorldIconPurple);
		}

		TextureAtlasSprite iicon = sprites.get(((BlockLightRelay) state.getBlock()).variant);

		GlStateManager.pushMatrix();
		GlStateManager.translated(x + 0.5, y + 0.3, z + 0.5);
		GlStateManager.enableRescaleNormal();
		GlStateManager.enableBlend();
		GlStateManager.blendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
		GlStateManager.alphaFunc(GL11.GL_GREATER, 0.05F);

		double time = ClientTickHandler.ticksInGame + pticks;
		GlStateManager.color4f(1F, 1F, 1F, 1F);

		float scale = 0.75F;
		GlStateManager.scalef(scale, scale, scale);
		Tessellator tessellator = Tessellator.getInstance();

		GlStateManager.pushMatrix();
		float r = 180.0F - mc.getRenderManager().playerViewY;
		GlStateManager.rotatef(r, 0F, 1F, 0F);
		GlStateManager.rotatef(-mc.getRenderManager().playerViewX, 1F, 0F, 0F);

		float off = 0.25F;
		GlStateManager.translatef(0F, off, 0F);
		GlStateManager.rotatef((float) time, 0F, 0F, 1F);
		GlStateManager.translatef(0F, -off, 0F);

		mc.textureManager.bindTexture(AtlasTexture.LOCATION_BLOCKS_TEXTURE);
		ShaderHelper.useShader(ShaderHelper.BotaniaShader.HALO);
		renderIcon(tessellator, iicon);
		ShaderHelper.releaseShader();

		GlStateManager.popMatrix();
		GlStateManager.color4f(1F, 1F, 1F, 1F);
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
