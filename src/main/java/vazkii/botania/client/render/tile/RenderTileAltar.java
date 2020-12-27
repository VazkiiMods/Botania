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

import java.awt.Color;

import net.minecraft.block.Block;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.ItemRenderer;
import net.minecraft.client.renderer.RenderBlocks;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.entity.RenderItem;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.IIcon;
import net.minecraft.util.ResourceLocation;

import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL12;

import vazkii.botania.client.core.handler.ClientTickHandler;
import vazkii.botania.client.lib.LibResources;
import vazkii.botania.client.model.ModelAltar;
import vazkii.botania.common.block.tile.TileAltar;

public class RenderTileAltar extends TileEntitySpecialRenderer {

	private static final ResourceLocation[] textures = new ResourceLocation[] {
		new ResourceLocation(LibResources.MODEL_ALTAR),
		new ResourceLocation(String.format(LibResources.MODEL_ALTAR_META, 0)),
		new ResourceLocation(String.format(LibResources.MODEL_ALTAR_META, 1)),
		new ResourceLocation(String.format(LibResources.MODEL_ALTAR_META, 2)),
		new ResourceLocation(String.format(LibResources.MODEL_ALTAR_META, 3)),
		new ResourceLocation(String.format(LibResources.MODEL_ALTAR_META, 4)),
		new ResourceLocation(String.format(LibResources.MODEL_ALTAR_META, 5)),
		new ResourceLocation(String.format(LibResources.MODEL_ALTAR_META, 6)),
		new ResourceLocation(String.format(LibResources.MODEL_ALTAR_META, 7))
	};

	private static final ResourceLocation textureMossy = new ResourceLocation(LibResources.MODEL_ALTAR_MOSSY);

	ModelAltar model = new ModelAltar();
	RenderItem renderItem = new RenderItem();
	RenderBlocks renderBlocks = new RenderBlocks();
	public static int forceMeta = -1;

	@Override
	public void renderTileEntityAt(TileEntity tileentity, double d0, double d1, double d2, float pticks) {
		TileAltar altar = (TileAltar) tileentity;

		GL11.glPushMatrix();
		GL11.glEnable(GL12.GL_RESCALE_NORMAL);
		GL11.glColor4f(1F, 1F, 1F, 1F);
		Minecraft.getMinecraft().renderEngine.bindTexture(altar.isMossy ? textureMossy : textures[Math.min(textures.length - 1, forceMeta == -1 ? tileentity.getBlockMetadata() : forceMeta)]);

		GL11.glTranslated(d0 + 0.5, d1 + 1.5, d2 + 0.5);
		GL11.glScalef(1F, -1F, -1F);
		model.render();
		GL11.glScalef(1F, -1F, -1F);
		GL11.glEnable(GL12.GL_RESCALE_NORMAL);

		boolean water = altar.hasWater();
		boolean lava = altar.hasLava();
		if(water || lava) {
			GL11.glPushMatrix();
			float s = 1F / 256F * 10F;
			float v = 1F / 8F;
			float w = -v * 2.5F;

			if(water) {
				int petals = 0;
				for(int i = 0; i < altar.getSizeInventory(); i++)
					if(altar.getStackInSlot(i) != null)
						petals++;
					else break;

				if(petals > 0) {
					Minecraft minecraft = Minecraft.getMinecraft();
					final float modifier = 6F;
					final float rotationModifier = 0.25F;
					final float radiusBase = 1.2F;
					final float radiusMod = 0.1F;

					double ticks = (ClientTickHandler.ticksInGame + pticks) * 0.5;
					float offsetPerPetal = 360 / petals;

					GL11.glPushMatrix();
					GL11.glTranslatef(-0.05F, -0.5F, 0F);
					GL11.glScalef(v, v, v);
					for(int i = 0; i < petals; i++) {
						float offset = offsetPerPetal * i;
						float deg = (int) (ticks / rotationModifier % 360F + offset);
						float rad = deg * (float) Math.PI / 180F;
						float radiusX = (float) (radiusBase + radiusMod * Math.sin(ticks / modifier));
						float radiusZ = (float) (radiusBase + radiusMod * Math.cos(ticks / modifier));
						float x =  (float) (radiusX * Math.cos(rad));
						float z = (float) (radiusZ * Math.sin(rad));
						float y = (float) Math.cos((ticks + 50 * i) / 5F) / 10F;

						GL11.glPushMatrix();
						GL11.glTranslatef(x, y, z);
						float xRotate = (float) Math.sin(ticks * rotationModifier) / 2F;
						float yRotate = (float) Math.max(0.6F, Math.sin(ticks * 0.1F) / 2F + 0.5F);
						float zRotate = (float) Math.cos(ticks * rotationModifier) / 2F;

						v /= 2F;
						GL11.glTranslatef(v, v, v);
						GL11.glRotatef(deg, xRotate, yRotate, zRotate);
						GL11.glTranslatef(-v, -v, -v);
						v *= 2F;

						GL11.glColor4f(1F, 1F, 1F, 1F);

						ItemStack stack = altar.getStackInSlot(i);
						minecraft.renderEngine.bindTexture(stack.getItem() instanceof ItemBlock ? TextureMap.locationBlocksTexture : TextureMap.locationItemsTexture);

						if(stack.getItem() instanceof ItemBlock && RenderBlocks.renderItemIn3d(Block.getBlockFromItem(stack.getItem()).getRenderType())) {
							GL11.glScalef(0.5F, 0.5F, 0.5F);
							GL11.glTranslatef(1F, 1.1F, 0F);
							renderBlocks.renderBlockAsItem(Block.getBlockFromItem(stack.getItem()), stack.getItemDamage(), 1F);
							GL11.glTranslatef(-1F, -1.1F, 0F);
							GL11.glScalef(2F, 2F, 2F);
						} else {
							IIcon icon = stack.getItem().getIcon(stack, 0);
							if (icon != null) {
								Color color = new Color(stack.getItem().getColorFromItemStack(stack, 0));
								GL11.glColor3ub((byte) color.getRed(), (byte) color.getGreen(), (byte) color.getBlue());
								float f = icon.getMinU();
								float f1 = icon.getMaxU();
								float f2 = icon.getMinV();
								float f3 = icon.getMaxV();
								ItemRenderer.renderItemIn2D(Tessellator.instance, f1, f2, f, f3, icon.getIconWidth(), icon.getIconHeight(), 1F / 16F);
								GL11.glColor3f(1F, 1F, 1F);
							}
						}

						GL11.glPopMatrix();
					}

					GL11.glPopMatrix();
				}
			}

			Minecraft.getMinecraft().renderEngine.bindTexture(TextureMap.locationBlocksTexture);
			Block block = lava ? Blocks.lava : Blocks.water;
			int brightness = lava ? 240 : -1;
			float alpha = lava ? 1F : 0.7F;

			GL11.glEnable(GL11.GL_BLEND);
			GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
			GL11.glDisable(GL11.GL_ALPHA_TEST);
			if(lava)
				GL11.glDisable(GL11.GL_LIGHTING);
			GL11.glColor4f(1F, 1F, 1F, alpha);
			GL11.glTranslatef(w, -0.3F, w);
			GL11.glRotatef(90F, 1F, 0F, 0F);
			GL11.glScalef(s, s, s);

			renderIcon(0, 0, block.getIcon(0, 0), 16, 16, brightness);
			if(lava)
				GL11.glEnable(GL11.GL_LIGHTING);
			GL11.glEnable(GL11.GL_ALPHA_TEST);
			GL11.glDisable(GL11.GL_BLEND);
			GL11.glPopMatrix();
		}
		GL11.glPopMatrix();

		forceMeta = -1;
	}

	public void renderIcon(int par1, int par2, IIcon par3Icon, int par4, int par5, int brightness) {
		Tessellator tessellator = Tessellator.instance;
		tessellator.startDrawingQuads();
		if(brightness != -1)
			tessellator.setBrightness(brightness);
		tessellator.addVertexWithUV(par1 + 0, par2 + par5, 0, par3Icon.getMinU(), par3Icon.getMaxV());
		tessellator.addVertexWithUV(par1 + par4, par2 + par5, 0, par3Icon.getMaxU(), par3Icon.getMaxV());
		tessellator.addVertexWithUV(par1 + par4, par2 + 0, 0, par3Icon.getMaxU(), par3Icon.getMinV());
		tessellator.addVertexWithUV(par1 + 0, par2 + 0, 0, par3Icon.getMinU(), par3Icon.getMinV());
		tessellator.draw();
	}

}
