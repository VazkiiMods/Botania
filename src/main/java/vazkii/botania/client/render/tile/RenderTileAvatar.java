/**
 * This class was created by <Vazkii>. It's distributed as
 * part of the Botania Mod. Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 * 
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 * 
 * File Created @ [Oct 24, 2015, 3:51:35 PM (GMT)]
 */
package vazkii.botania.client.render.tile;

import java.awt.Color;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.ItemRenderer;
import net.minecraft.client.renderer.OpenGlHelper;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.IIcon;
import net.minecraft.util.ResourceLocation;

import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL12;

import vazkii.botania.api.item.IAvatarWieldable;
import vazkii.botania.client.core.handler.ClientTickHandler;
import vazkii.botania.client.lib.LibResources;
import vazkii.botania.client.model.ModelAvatar;
import vazkii.botania.common.block.tile.TileAvatar;

public class RenderTileAvatar extends TileEntitySpecialRenderer {

	private static final float[] ROTATIONS = new float[] {
		180F, 0F, 90F, 270F
	};

	private static final ResourceLocation texture = new ResourceLocation(LibResources.MODEL_AVATAR);
	private static final ModelAvatar model = new ModelAvatar();

	@Override
	public void renderTileEntityAt(TileEntity tileentity, double d0, double d1, double d2, float pticks) {
		TileAvatar avatar = (TileAvatar) tileentity;

		GL11.glPushMatrix();
		GL11.glEnable(GL12.GL_RESCALE_NORMAL);
		GL11.glColor4f(1F, 1F, 1F, 1F);
		GL11.glTranslated(d0, d1, d2);

		Minecraft.getMinecraft().renderEngine.bindTexture(texture);
		int meta = avatar.getWorldObj() != null ? avatar.getBlockMetadata() : 0;

		GL11.glTranslatef(0.5F, 1.6F, 0.5F);
		GL11.glScalef(1F, -1F, -1F);
		GL11.glRotatef(ROTATIONS[Math.max(Math.min(ROTATIONS.length - 1, meta - 2), 0)], 0F, 1F, 0F);
		model.render();

		ItemStack stack = avatar.getStackInSlot(0);
		if(stack != null) {
			GL11.glPushMatrix();
			Minecraft.getMinecraft().renderEngine.bindTexture(TextureMap.locationItemsTexture);
			float s = 0.4F;
			GL11.glScalef(s, s, s);
			GL11.glRotatef(90F, 0F, 1F, 0F);
			GL11.glRotatef(180F, 0F, 0F, 1F);
			GL11.glTranslated(-1.2F, -3.5F, -0.65F);
			GL11.glRotatef(20F, 0F, 0F, 1F);

			int renderPass = 0;
			do {
				IIcon icon = stack.getItem().getIcon(stack, renderPass);
				if(icon != null) {
					Color color = new Color(stack.getItem().getColorFromItemStack(stack, renderPass));
					GL11.glColor3ub((byte) color.getRed(), (byte) color.getGreen(), (byte) color.getBlue());
					float f = icon.getMinU();
					float f1 = icon.getMaxU();
					float f2 = icon.getMinV();
					float f3 = icon.getMaxV();
					ItemRenderer.renderItemIn2D(Tessellator.instance, f1, f2, f, f3, icon.getIconWidth(), icon.getIconHeight(), 1F / 16F);
					GL11.glColor3f(1F, 1F, 1F);
				}
				renderPass++;
			} while(renderPass < stack.getItem().getRenderPasses(stack.getItemDamage()));
			GL11.glPopMatrix();

			IAvatarWieldable wieldable = (IAvatarWieldable) stack.getItem();
			Minecraft.getMinecraft().renderEngine.bindTexture(wieldable.getOverlayResource(avatar, stack));
			s = 1.01F;

			GL11.glPushMatrix();
			GL11.glEnable(GL11.GL_BLEND);
			GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
			GL11.glScalef(s, s, s);
			GL11.glTranslatef(0F, -0.01F, 0F);
			int light = 15728880;
			int lightmapX = light % 65536;
			int lightmapY = light / 65536;
			OpenGlHelper.setLightmapTextureCoords(OpenGlHelper.lightmapTexUnit, lightmapX, lightmapY);
			float alpha = (float) Math.sin(ClientTickHandler.ticksInGame / 20D) / 2F + 0.5F;
			GL11.glColor4f(1F, 1F, 1F, alpha + 0.183F);
			model.render();
			GL11.glPopMatrix();
		}
		GL11.glColor3f(1F, 1F, 1F);
		GL11.glScalef(1F, -1F, -1F);
		GL11.glEnable(GL12.GL_RESCALE_NORMAL);
		GL11.glPopMatrix();
	}

}
