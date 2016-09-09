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
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import org.lwjgl.opengl.GL11;
import vazkii.botania.api.state.BotaniaStateProps;
import vazkii.botania.api.state.enums.LuminizerVariant;
import vazkii.botania.client.core.handler.ClientTickHandler;
import vazkii.botania.client.core.handler.MiscellaneousIcons;
import vazkii.botania.client.core.helper.ShaderHelper;
import vazkii.botania.common.block.ModBlocks;
import vazkii.botania.common.block.tile.TileLightRelay;

import javax.annotation.Nonnull;

public class RenderTileLightRelay extends TileEntitySpecialRenderer<TileLightRelay> {

	@Override
	public void renderTileEntityAt(@Nonnull TileLightRelay tile, double x, double y, double z, float pticks, int digProgress) {
		if(!tile.getWorld().isBlockLoaded(tile.getPos(), false)
				|| tile.getWorld().getBlockState(tile.getPos()).getBlock() != ModBlocks.lightRelay)
			return;
		
		Minecraft mc = Minecraft.getMinecraft();
		TextureAtlasSprite iicon = tile.getWorld().getBlockState(tile.getPos())
				.getValue(BotaniaStateProps.LUMINIZER_VARIANT) == LuminizerVariant.DEFAULT
				? MiscellaneousIcons.INSTANCE.lightRelayWorldIcon
				: MiscellaneousIcons.INSTANCE.lightRelayWorldIconRed;

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
		GlStateManager.rotate(((float) time), 0F, 0F, 1F);
		GlStateManager.translate(0F, -off, 0F);

		mc.renderEngine.bindTexture(TextureMap.LOCATION_BLOCKS_TEXTURE);
		ShaderHelper.useShader(ShaderHelper.halo);
		func_77026_a(tessellator, iicon);
		ShaderHelper.releaseShader();

		GlStateManager.popMatrix();
		GlStateManager.color(1F, 1F, 1F, 1F);
		GlStateManager.disableBlend();
		GlStateManager.disableRescaleNormal();
		GlStateManager.popMatrix();
	}

	private void func_77026_a(Tessellator p_77026_1_, TextureAtlasSprite p_77026_2_) {
		float f = p_77026_2_.getMinU();
		float f1 = p_77026_2_.getMaxU();
		float f2 = p_77026_2_.getMinV();
		float f3 = p_77026_2_.getMaxV();
		float size = f1 - f;
		float pad = size / 8F;
		f += pad;
		f1 -= pad;
		f2 += pad;
		f3 -= pad;
		
		float f4 = 1.0F;
		float f5 = 0.5F;
		float f6 = 0.25F;

		p_77026_1_.getBuffer().begin(GL11.GL_QUADS, DefaultVertexFormats.POSITION_TEX_NORMAL);
		//p_77026_1_.getBuffer().setBrightness(240);
		p_77026_1_.getBuffer().pos(0.0F - f5, 0.0F - f6, 0.0D).tex(f, f3).normal(0, 1, 0).endVertex();
		p_77026_1_.getBuffer().pos(f4 - f5, 0.0F - f6, 0.0D).tex(f1, f3).normal(0, 1, 0).endVertex();
		p_77026_1_.getBuffer().pos(f4 - f5, f4 - f6, 0.0D).tex(f1, f2).normal(0, 1, 0).endVertex();
		p_77026_1_.getBuffer().pos(0.0F - f5, f4 - f6, 0.0D).tex(f, f2).normal(0, 1, 0).endVertex();
		p_77026_1_.draw();

	}

}
