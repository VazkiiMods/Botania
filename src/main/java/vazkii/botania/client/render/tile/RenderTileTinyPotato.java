/**
 * This class was created by <Vazkii>. It's distributed as
 * part of the Botania Mod. Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 * 
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 * 
 * File Created @ [Jul 18, 2014, 10:48:46 PM (GMT)]
 */
package vazkii.botania.client.render.tile;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.ItemRenderer;
import net.minecraft.client.renderer.OpenGlHelper;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.IIcon;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.MinecraftForge;

import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL12;

import vazkii.botania.api.item.TinyPotatoRenderEvent;
import vazkii.botania.client.lib.LibResources;
import vazkii.botania.client.model.ModelTinyPotato;
import vazkii.botania.common.block.tile.TileTinyPotato;
import vazkii.botania.common.item.ModItems;
import vazkii.botania.common.item.material.ItemManaResource;

public class RenderTileTinyPotato extends TileEntitySpecialRenderer {

	private static final ResourceLocation texture = new ResourceLocation(LibResources.MODEL_TINY_POTATO);
	private static final ModelTinyPotato model = new ModelTinyPotato();

	@Override
	public void renderTileEntityAt(TileEntity var1, double d0, double d1, double d2, float var8) {
		TileTinyPotato potato = (TileTinyPotato) var1;
		GL11.glPushMatrix();
		GL11.glEnable(GL12.GL_RESCALE_NORMAL);
		GL11.glColor4f(1F, 1F, 1F, 1F);
		GL11.glTranslated(d0, d1, d2);

		Minecraft mc = Minecraft.getMinecraft();
		mc.renderEngine.bindTexture(texture);

		GL11.glTranslatef(0.5F, 1.5F, 0.5F);
		GL11.glScalef(1F, -1F, -1F);
		int meta = potato.getWorldObj() == null ? 3 : potato.getBlockMetadata();
		float rotY = meta * 90F - 180F;
		GL11.glRotatef(rotY, 0F, 1F, 0F);

		float jump = potato.jumpTicks;
		if(jump > 0)
			jump -= var8;

		float up = (float) -Math.abs(Math.sin(jump / 10 * Math.PI)) * 0.2F;
		float rotZ = (float) Math.sin(jump / 10 * Math.PI) * 2;

		GL11.glTranslatef(0F, up, 0F);
		GL11.glRotatef(rotZ, 0F, 0F, 1F);

		model.render();

		GL11.glPushMatrix();
		String name = potato.name.toLowerCase();

		mc.renderEngine.bindTexture(TextureMap.locationItemsTexture);
		float scale = 1F / 4F;
		GL11.glTranslatef(0F, 1F, 0F);
		GL11.glScalef(scale, scale, scale);
		if(name.equals("phi") || name.equals("vazkii")) {
			GL11.glTranslatef(0.45F, 0F, 0.4F);
			GL11.glRotatef(90F, 0F, 1F, 0F);
			GL11.glRotatef(20F, 1F, 0F, 1F);
			renderIcon(((ItemManaResource) ModItems.manaResource).phiFlowerIcon);
		} else if(name.equals("skull kid")) {
			GL11.glScalef(1.25F, 1.25F, 1.25F);
			GL11.glRotatef(180F, 0F, 0F, 1F);
			GL11.glTranslatef(-0.5F, -1.2F, -0.4F);
			renderIcon(ModItems.cosmetic.getIconFromDamage(23));
		} else if(name.equals("kamina")) {
			GL11.glScalef(1.25F, 1.25F, 1.25F);
			GL11.glRotatef(180F, 0F, 0F, 1F);
			GL11.glTranslatef(-0.5F, -1.1F, -0.4F);
			renderIcon(ModItems.cosmetic.getIconFromDamage(26));
		} else if(name.equals("haighyorkie")) {
			GL11.glScalef(1.25F, 1.25F, 1.25F);
			GL11.glRotatef(180F, 0F, 0F, 1F);
			GL11.glRotatef(90F, 0F, 1F, 0F);
			GL11.glTranslatef(-0.5F, -1.2F, -0.4F);
			renderIcon(((ItemManaResource) ModItems.manaResource).goldfishIcon);
		} else if(name.equals("chitoge")) {
			GL11.glScalef(1.25F, 1.25F, 1.25F);
			GL11.glRotatef(180F, 0F, 0F, 1F);
			GL11.glTranslatef(-0.5F, -0.7F, 0.1F);
			renderIcon(ModItems.cosmetic.getIconFromDamage(7));
		} else if(name.equals("direwolf20")) {
			GL11.glRotatef(180F, 0F, 0F, 1F);
			GL11.glTranslatef(-0.5F, -2.2F, -0.5F);
			renderIcon(ModItems.cosmetic.getIconFromDamage(0));
		} else if(name.equals("doctor")) {
			GL11.glScalef(1.25F, 1.25F, 1.25F);
			GL11.glRotatef(180F, 0F, 0F, 1F);
			GL11.glTranslatef(-0.5F, -1.15F, -0.4F);
			renderIcon(ModItems.cosmetic.getIconFromDamage(25));
		} else if(name.equals("snoo")) {
			GL11.glScalef(1.25F, 1.25F, 1.25F);
			GL11.glRotatef(180F, 0F, 0F, 1F);
			GL11.glTranslatef(-0.5F, -0.7F, 0.1F);
			GL11.glRotatef(20F, 0F, 0F, 1F);
			renderIcon(ModItems.cosmetic.getIconFromDamage(24));
		}
		GL11.glPopMatrix();

		MinecraftForge.EVENT_BUS.post(new TinyPotatoRenderEvent(potato, potato.name, d0, d1, d2, var8));

		GL11.glRotatef(-rotZ, 0F, 0F, 1F);
		GL11.glRotatef(-rotY, 0F, 1F, 0F);
		GL11.glColor3f(1F, 1F, 1F);
		GL11.glScalef(1F, -1F, -1F);

		MovingObjectPosition pos = mc.objectMouseOver;
		if(!name.isEmpty() && pos != null && pos.blockX == potato.xCoord && pos.blockY == potato.yCoord && pos.blockZ == potato.zCoord) {
			GL11.glPushMatrix();
			GL11.glTranslatef(0F, -0.6F, 0F);
			GL11.glRotatef(-RenderManager.instance.playerViewY, 0.0F, 1.0F, 0.0F);
			GL11.glRotatef(RenderManager.instance.playerViewX, 1.0F, 0.0F, 0.0F);
			float f = 1.6F;
			float f1 = 0.016666668F * f;
			GL11.glScalef(-f1, -f1, f1);
			GL11.glDisable(GL11.GL_LIGHTING);
			GL11.glTranslatef(0.0F, 0F / f1, 0.0F);
			GL11.glDepthMask(false);
			GL11.glEnable(GL11.GL_BLEND);
			OpenGlHelper.glBlendFunc(770, 771, 1, 0);
			Tessellator tessellator = Tessellator.instance;
			GL11.glDisable(GL11.GL_TEXTURE_2D);
			tessellator.startDrawingQuads();
			int i = mc.fontRenderer.getStringWidth(potato.name) / 2;
			tessellator.setColorRGBA_F(0.0F, 0.0F, 0.0F, 0.25F);
			tessellator.addVertex(-i - 1, -1.0D, 0.0D);
			tessellator.addVertex(-i - 1, 8.0D, 0.0D);
			tessellator.addVertex(i + 1, 8.0D, 0.0D);
			tessellator.addVertex(i + 1, -1.0D, 0.0D);
			tessellator.draw();
			GL11.glEnable(GL11.GL_TEXTURE_2D);
			GL11.glDepthMask(true);
			mc.fontRenderer.drawString(potato.name, -mc.fontRenderer.getStringWidth(potato.name) / 2, 0, 0xFFFFFF);
			GL11.glEnable(GL11.GL_LIGHTING);
			GL11.glDisable(GL11.GL_BLEND);
			GL11.glColor4f(1F, 1F, 1F, 1F);
			GL11.glScalef(1F / -f1, 1F / -f1, 1F / f1);
			GL11.glPopMatrix();
		}

		GL11.glPopMatrix();
	}

	public void renderIcon(IIcon icon) {
		float f = icon.getMinU();
		float f1 = icon.getMaxU();
		float f2 = icon.getMinV();
		float f3 = icon.getMaxV();
		ItemRenderer.renderItemIn2D(Tessellator.instance, f1, f2, f, f3, icon.getIconWidth(), icon.getIconHeight(), 1F / 16F);
	}
}