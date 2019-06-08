/**
 * This class was created by <Vazkii>. It's distributed as
 * part of the Botania Mod. Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 *
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 *
 * File Created @ [Jan 21, 2014, 7:55:47 PM (GMT)]
 */
package vazkii.botania.client.render.tile;

import net.minecraft.block.Blocks;
import net.minecraft.client.Minecraft;
import com.mojang.blaze3d.platform.GlStateManager;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.model.ItemCameraTransforms;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.renderer.texture.AtlasTexture;
import net.minecraft.client.renderer.tileentity.TileEntityRenderer;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.item.ItemStack;
import org.lwjgl.opengl.GL11;
import vazkii.botania.client.core.handler.ClientTickHandler;
import vazkii.botania.common.block.tile.TileAltar;

import javax.annotation.Nonnull;

public class RenderTileAltar extends TileEntityRenderer<TileAltar> {

	@Override
	public void render(@Nonnull TileAltar altar, double d0, double d1, double d2, float pticks, int digProgress) {
		if(!altar.getWorld().isBlockLoaded(altar.getPos(), false))
			return;

		GlStateManager.pushMatrix();
		GlStateManager.enableRescaleNormal();
		GlStateManager.color4f(1F, 1F, 1F, 1F);

		GlStateManager.translated(d0 + 0.5, d1 + 1.5, d2 + 0.5);
		GlStateManager.enableRescaleNormal();

		boolean water = altar.hasWater();
		boolean lava = altar.hasLava();
		if(water || lava) {
			GlStateManager.pushMatrix();
			float s = 1F / 256F * 10F;
			float v = 1F / 8F;
			float w = -v * 2.5F;

			if(water) {
				int petals = 0;
				for(int i = 0; i < altar.getSizeInventory(); i++)
					if(!altar.getItemHandler().getStackInSlot(i).isEmpty())
						petals++;
					else break;

				if(petals > 0) {
					final float modifier = 6F;
					final float rotationModifier = 0.25F;
					final float radiusBase = 1.2F;
					final float radiusMod = 0.1F;

					double ticks = (ClientTickHandler.ticksInGame + pticks) * 0.5;
					float offsetPerPetal = 360 / petals;

					GlStateManager.pushMatrix();
					GlStateManager.translatef(-0.05F, -0.5F, 0F);
					GlStateManager.scalef(v, v, v);
					for(int i = 0; i < petals; i++) {
						float offset = offsetPerPetal * i;
						float deg = (int) (ticks / rotationModifier % 360F + offset);
						float rad = deg * (float) Math.PI / 180F;
						float radiusX = (float) (radiusBase + radiusMod * Math.sin(ticks / modifier));
						float radiusZ = (float) (radiusBase + radiusMod * Math.cos(ticks / modifier));
						float x =  (float) (radiusX * Math.cos(rad));
						float z = (float) (radiusZ * Math.sin(rad));
						float y = (float) Math.cos((ticks + 50 * i) / 5F) / 10F;

						GlStateManager.pushMatrix();
						GlStateManager.translatef(x, y, z);
						float xRotate = (float) Math.sin(ticks * rotationModifier) / 2F;
						float yRotate = (float) Math.max(0.6F, Math.sin(ticks * 0.1F) / 2F + 0.5F);
						float zRotate = (float) Math.cos(ticks * rotationModifier) / 2F;

						v /= 2F;
						GlStateManager.translatef(v, v, v);
						GlStateManager.rotatef(deg, xRotate, yRotate, zRotate);
						GlStateManager.translatef(-v, -v, -v);
						v *= 2F;

						GlStateManager.color4f(1F, 1F, 1F, 1F);

						ItemStack stack = altar.getItemHandler().getStackInSlot(i);
						Minecraft.getInstance().textureManager.bindTexture(AtlasTexture.LOCATION_BLOCKS_TEXTURE);
						Minecraft.getInstance().getItemRenderer().renderItem(stack, ItemCameraTransforms.TransformType.GROUND);
						GlStateManager.popMatrix();
					}

					GlStateManager.popMatrix();
				}
			}


			Minecraft.getInstance().textureManager.bindTexture(AtlasTexture.LOCATION_BLOCKS_TEXTURE);
			int brightness = lava ? 240 : -1;
			float alpha = lava ? 1F : 0.7F;

			GlStateManager.enableBlend();
			GlStateManager.blendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
			GlStateManager.disableAlphaTest();
			if(lava)
				GlStateManager.disableLighting();
			GlStateManager.color4f(1F, 1F, 1F, alpha);
			GlStateManager.translatef(w, -0.3F, w);
			GlStateManager.rotatef(90F, 1F, 0F, 0F);
			GlStateManager.scalef(s, s, s);

			TextureAtlasSprite sprite = lava ? Minecraft.getInstance().getModelManager().getBlockModelShapes().getModel(Blocks.LAVA.getDefaultState()).getParticleTexture()
					: Minecraft.getInstance().getModelManager().getBlockModelShapes().getModel(Blocks.WATER.getDefaultState()).getParticleTexture();
			renderIcon(0, 0, sprite, 16, 16, brightness);
			if(lava)
				GlStateManager.enableLighting();
			GlStateManager.enableAlphaTest();
			GlStateManager.disableBlend();
			GlStateManager.popMatrix();
		}
		GlStateManager.popMatrix();
	}

	private void renderIcon(int x, int y, TextureAtlasSprite sprite, int width, int height, int brightness) {
		Tessellator tessellator = Tessellator.getInstance();
		tessellator.getBuffer().begin(GL11.GL_QUADS, DefaultVertexFormats.POSITION_TEX);
		//if(brightness != -1)
		//tessellator.getBuffer().putBrightness4(brightness, brightness, brightness, brightness);
		tessellator.getBuffer().pos(x + 0, y + height, 0).tex(sprite.getMinU(), sprite.getMaxV()).endVertex();
		tessellator.getBuffer().pos(x + width, y + height, 0).tex(sprite.getMaxU(), sprite.getMaxV()).endVertex();
		tessellator.getBuffer().pos(x + width, y + 0, 0).tex(sprite.getMaxU(), sprite.getMinV()).endVertex();
		tessellator.getBuffer().pos(x + 0, y + 0, 0).tex(sprite.getMinU(), sprite.getMinV()).endVertex();
		tessellator.draw();
	}

}
