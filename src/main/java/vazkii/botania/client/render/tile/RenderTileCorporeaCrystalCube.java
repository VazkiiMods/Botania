/**
 * This class was created by <Vazkii>. It's distributed as
 * part of the Botania Mod. Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 * 
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 * 
 * File Created @ [Apr 30, 2015, 4:10:14 PM (GMT)]
 */
package vazkii.botania.client.render.tile;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.entity.Render;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ResourceLocation;

import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL12;

import vazkii.botania.client.core.handler.ClientTickHandler;
import vazkii.botania.client.lib.LibResources;
import vazkii.botania.client.model.ModelCrystalCube;
import vazkii.botania.common.block.tile.corporea.TileCorporeaCrystalCube;

public class RenderTileCorporeaCrystalCube extends TileEntitySpecialRenderer {

	private static final ResourceLocation texture = new ResourceLocation(LibResources.MODEL_CRYSTAL_CUBE);
	ModelCrystalCube model = new ModelCrystalCube();
	EntityItem entity = null;

	@Override
	public void renderTileEntityAt(TileEntity tileentity, double d0, double d1, double d2, float f) {
		TileCorporeaCrystalCube cube = (TileCorporeaCrystalCube) tileentity;

		if(entity == null)
			entity = new EntityItem(cube.getWorldObj(), cube.xCoord, cube.yCoord, cube.zCoord, new ItemStack(Blocks.stone));

		entity.age = ClientTickHandler.ticksInGame;
		ItemStack stack = cube.getRequestTarget();
		entity.setEntityItemStack(stack);

		double time = ClientTickHandler.ticksInGame + f;
		double worldTicks = tileentity.getWorldObj() == null ? 0 : time;

		Minecraft mc = Minecraft.getMinecraft();
		GL11.glPushMatrix();
		GL11.glEnable(GL12.GL_RESCALE_NORMAL);
		GL11.glEnable(GL11.GL_BLEND);
		GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
		GL11.glColor4f(1F, 1F, 1F, 1F);
		GL11.glTranslated(d0, d1, d2);
		mc.renderEngine.bindTexture(texture);
		GL11.glTranslatef(0.5F, 1.5F, 0.5F);
		GL11.glScalef(1F, -1F, -1F);
		model.renderBase();
		GL11.glTranslatef(0F, (float) Math.sin(worldTicks / 20.0) * 0.05F, 0F);
		if(stack != null) {
			GL11.glPushMatrix();
			float s = stack.getItem() instanceof ItemBlock ? 0.7F : 0.5F;
			GL11.glTranslatef(0F, 0.8F, 0F);
			GL11.glScalef(s, s, s);
			GL11.glRotatef(180F, 0F, 0F, 1F);
			((Render) RenderManager.instance.entityRenderMap.get(EntityItem.class)).doRender(entity, 0, 0, 0, 1F, f);
			GL11.glPopMatrix();
			mc.renderEngine.bindTexture(texture);
		}

		GL11.glColor4f(1F, 1F, 1F, 0.4F);
		model.renderCube();
		GL11.glColor3f(1F, 1F, 1F);

		if(stack != null) {
			int count = cube.getItemCount();
			String countStr = "" + count;
			int color = 0xFFFFFF;
			if(count > 9999) {
				countStr = count / 1000 + "K";
				color = 0xFFFF00;
				if(count > 9999999) {
					countStr = count / 10000000 + "M";
					color = 0x00FF00;
				}
			}
			color |= 0xA0 << 24;
			int colorShade = (color & 16579836) >> 2 | color & -16777216;

			float s = 1F / 64F;
			GL11.glScalef(s, s, s);
			GL11.glDisable(GL11.GL_LIGHTING);
			int l = mc.fontRenderer.getStringWidth(countStr);

			GL11.glTranslatef(0F, 55F, 0F);
			float tr = -16.5F;
			for(int i = 0; i < 4; i++) {
				GL11.glRotatef(90F, 0F, 1F, 0F);
				GL11.glTranslatef(0F, 0F, tr);
				mc.fontRenderer.drawString(countStr, -l / 2, 0, color);
				GL11.glTranslatef(0F, 0F, 0.1F);
				mc.fontRenderer.drawString(countStr, -l / 2 + 1, 1, colorShade);
				GL11.glTranslatef(0F, 0F, -tr - 0.1F);
			}
			GL11.glEnable(GL11.GL_LIGHTING);
		}

		GL11.glScalef(1F, -1F, -1F);
		GL11.glEnable(GL12.GL_RESCALE_NORMAL);
		GL11.glPopMatrix();
	}

}
