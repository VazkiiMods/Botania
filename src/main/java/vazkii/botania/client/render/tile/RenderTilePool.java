/**
 * This class was created by <Vazkii>. It's distributed as
 * part of the Botania Mod. Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 * 
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 * 
 * File Created @ [Jan 26, 2014, 12:25:11 AM (GMT)]
 */
package vazkii.botania.client.render.tile;

import java.awt.Color;

import net.minecraft.block.Block;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.block.model.BakedQuad;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.client.resources.model.IBakedModel;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ResourceLocation;

import net.minecraftforge.client.model.Attributes;
import net.minecraftforge.client.model.pipeline.LightUtil;
import org.lwjgl.opengl.GL11;

import vazkii.botania.api.mana.IPoolOverlayProvider;
import vazkii.botania.client.core.handler.ClientTickHandler;
import vazkii.botania.client.core.handler.MultiblockRenderHandler;
import vazkii.botania.client.core.handler.MiscellaneousIcons;
import vazkii.botania.client.core.helper.ShaderHelper;
import vazkii.botania.client.lib.LibResources;
import vazkii.botania.client.model.ModelPool;
import vazkii.botania.common.block.tile.mana.TilePool;

public class RenderTilePool extends TileEntitySpecialRenderer<TilePool> {

	private static final ResourceLocation texture = new ResourceLocation(LibResources.MODEL_POOL);
	private static final ResourceLocation textureInf = new ResourceLocation(LibResources.MODEL_INFINITE_POOL);
	private static final ResourceLocation textureDil = new ResourceLocation(LibResources.MODEL_DILUTED_POOL);

	private static final ModelPool model = new ModelPool();

	public static int forceMeta = 0;
	public static boolean forceMana = false;
	public static int forceManaNumber = -1;

	@Override
	public void renderTileEntityAt(TilePool pool, double d0, double d1, double d2, float f, int digProgress) {
		GlStateManager.pushMatrix();
		GlStateManager.enableBlend();
		GlStateManager.blendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
		GlStateManager.enableRescaleNormal();
		float a = MultiblockRenderHandler.rendering ? 0.6F : 1F;

		GlStateManager.color(1F, 1F, 1F, a);
		GlStateManager.translate(d0, d1, d2);
		boolean inf = pool.getWorld() == null ? forceMeta == 1 : pool.getBlockMetadata() == 1;
		boolean dil = pool.getWorld() == null ? forceMeta == 2 : pool.getBlockMetadata() == 2;
		boolean fab = pool.getWorld() == null ? forceMeta == 3 : pool.getBlockMetadata() == 3;


		Minecraft.getMinecraft().renderEngine.bindTexture(TextureMap.locationBlocksTexture);
		int color;
		if (fab) {
			float time = ClientTickHandler.ticksInGame + ClientTickHandler.partialTicks;
			color = Color.getHSBColor(time * 0.005F, 0.6F, 1F).hashCode();
		} else {
			color = pool.getColor().getMapColor().colorValue | 0xFF000000; // Add alpha
		}

		IBakedModel model = Minecraft.getMinecraft().getBlockRendererDispatcher().getBlockModelShapes().getModelForState(pool.getWorld().getBlockState(pool.getPos()));
		Tessellator tess = Tessellator.getInstance();

		tess.getWorldRenderer().begin(GL11.GL_QUADS, Attributes.DEFAULT_BAKED_FORMAT);

		GlStateManager.disableLighting();
		for (BakedQuad quad : model.getGeneralQuads()) {
			LightUtil.renderQuadColor(tess.getWorldRenderer(), quad, color);
		}

		for (EnumFacing e : EnumFacing.VALUES) {
			for (BakedQuad quad: model.getFaceQuads(e)) {
				LightUtil.renderQuadColor(tess.getWorldRenderer(), quad, color);
			}
		}

		tess.draw();

		GlStateManager.translate(0.5F, 1.5F, 0.5F);
		GlStateManager.color(1, 1, 1, a);
		GlStateManager.enableRescaleNormal();

		int mana = pool.getCurrentMana();
		if(forceManaNumber > -1)
			mana = forceManaNumber;
		int cap = pool.manaCap;
		if(cap == -1)
			cap = TilePool.MAX_MANA;

		float waterLevel = (float) mana / (float) cap * 0.4F;
		if(forceMana)
			waterLevel = 0.4F;

		float s = 1F / 16F;
		float v = 1F / 8F;
		float w = -v * 3.5F;

		if(pool.getWorld() != null) {
			Block below = pool.getWorld().getBlockState(pool.getPos().down()).getBlock();
			if(below instanceof IPoolOverlayProvider) {
				TextureAtlasSprite overlay = ((IPoolOverlayProvider) below).getIcon(pool.getWorld(), pool.getPos());
				if(overlay != null) {
					GlStateManager.pushMatrix();
					GlStateManager.enableBlend();
					GlStateManager.blendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
					GlStateManager.disableAlpha();
					GlStateManager.color(1F, 1F, 1F, a * (float) ((Math.sin((ClientTickHandler.ticksInGame + f) / 20.0) + 1) * 0.3 + 0.2));
					GlStateManager.translate(-0.5F, -1F - 0.43F, -0.5F);
					GlStateManager.rotate(90F, 1F, 0F, 0F);
					GlStateManager.scale(s, s, s);

					renderIcon(0, 0, overlay, 16, 16, 240);

					GlStateManager.enableAlpha();
					GlStateManager.disableBlend();
					GlStateManager.popMatrix();
				}
			}
		}

		if(waterLevel > 0) {
			s = 1F / 256F * 14F;
			GlStateManager.pushMatrix();
			GlStateManager.enableBlend();
			GlStateManager.blendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
			GlStateManager.disableAlpha();
			GlStateManager.color(1F, 1F, 1F, a);
			GlStateManager.translate(w, -1F - (0.43F - waterLevel), w);
			GlStateManager.rotate(90F, 1F, 0F, 0F);
			GlStateManager.scale(s, s, s);

			ShaderHelper.useShader(ShaderHelper.manaPool);
			renderIcon(0, 0, MiscellaneousIcons.INSTANCE.manaWater, 16, 16, 240);
			ShaderHelper.releaseShader();

			GlStateManager.enableAlpha();
			GlStateManager.disableBlend();
			GlStateManager.popMatrix();
		}
		GlStateManager.popMatrix();

		forceMeta = 0;
		forceMana = false;
		forceManaNumber = -1;
	}

	public void renderIcon(int par1, int par2, TextureAtlasSprite par3Icon, int par4, int par5, int brightness) {
		Tessellator tessellator = Tessellator.getInstance();
		tessellator.getWorldRenderer().begin(GL11.GL_QUADS, DefaultVertexFormats.POSITION_TEX);
		//tessellator.getWorldRenderer().setBrightness(brightness);
		tessellator.getWorldRenderer().pos(par1 + 0, par2 + par5, 0).tex(par3Icon.getMinU(), par3Icon.getMaxV()).endVertex();
		tessellator.getWorldRenderer().pos(par1 + par4, par2 + par5, 0).tex(par3Icon.getMaxU(), par3Icon.getMaxV()).endVertex();
		tessellator.getWorldRenderer().pos(par1 + par4, par2 + 0, 0).tex(par3Icon.getMaxU(), par3Icon.getMinV()).endVertex();
		tessellator.getWorldRenderer().pos(par1 + 0, par2 + 0, 0).tex(par3Icon.getMinU(), par3Icon.getMinV()).endVertex();
		tessellator.draw();
	}

}
