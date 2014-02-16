/**
 * This class was created by <Vazkii>. It's distributed as
 * part of the Botania Mod. Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 * 
 * Botania is Open Source and distributed under a
 * Creative Commons Attribution-NonCommercial-ShareAlike 3.0 License
 * (http://creativecommons.org/licenses/by-nc-sa/3.0/deed.en_GB)
 * 
 * File Created @ [Feb 2, 2014, 6:34:45 PM (GMT)]
 */
package vazkii.botania.client.render.tile;

import java.awt.Color;

import net.minecraft.block.Block;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.ItemRenderer;
import net.minecraft.client.renderer.RenderBlocks;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.Icon;
import net.minecraftforge.client.ForgeHooksClient;

import org.lwjgl.opengl.GL11;

import vazkii.botania.client.core.helper.RenderHelper;
import vazkii.botania.client.model.ModelSpinningCubes;
import vazkii.botania.common.block.tile.TileRuneAltar;

public class RenderTileRuneAltar extends TileEntitySpecialRenderer {

	ModelSpinningCubes cubes = new ModelSpinningCubes();

	@Override
	public void renderTileEntityAt(TileEntity tileentity, double x, double y, double z, float partticks) {
		TileRuneAltar altar = (TileRuneAltar) tileentity;

		GL11.glPushMatrix();
		GL11.glColor4f(1F, 1F, 1F, 1F);
		GL11.glTranslated(x, y, z);

		int items = 0;
		for(int i = 0; i < altar.getSizeInventory(); i++)
			if(altar.getStackInSlot(i) == null)
				break;
			else items++;
		float[] angles = new float[altar.getSizeInventory()];
		
		float anglePer = 360F / items;
		float totalAngle = 0F;
		for(int i = 0; i < angles.length; i++)
			angles[i] = totalAngle += anglePer;

		for(int i = 0; i < altar.getSizeInventory(); i++) {
			GL11.glPushMatrix();
			GL11.glScalef(0.5F, 0.5F, 0.5F);
			GL11.glTranslatef(1F, 2.5F, 1F);
			GL11.glRotatef(angles[i] + altar.worldObj.getTotalWorldTime(), 0F, 1F, 0F);
			GL11.glTranslatef(2.25F, 0F, 0.5F);
			GL11.glRotatef(90F, 0F, 1F, 0F);
			GL11.glTranslated(0D, 0.15 * Math.sin((tileentity.worldObj.getTotalWorldTime() + i * 10) / 5D), 0F);
			ItemStack stack = altar.getStackInSlot(i);
			Minecraft mc = Minecraft.getMinecraft();
			if(stack != null) {
				mc.renderEngine.bindTexture(stack.getItem() instanceof ItemBlock ? TextureMap.locationBlocksTexture : TextureMap.locationItemsTexture);

				GL11.glScalef(2F, 2F, 2F);
				if(!ForgeHooksClient.renderEntityItem(new EntityItem(altar.worldObj, altar.xCoord, altar.yCoord, altar.zCoord, stack), stack, 0F, 0F, altar.worldObj.rand, mc.renderEngine, new RenderBlocks())) {
					GL11.glScalef(0.5F, 0.5F, 0.5F);
					if(stack.getItem() instanceof ItemBlock && RenderBlocks.renderItemIn3d(Block.blocksList[stack.itemID].getRenderType())) {
						GL11.glScalef(0.5F, 0.5F, 0.5F);
						GL11.glTranslatef(1F, 1.1F, 0F);
						new RenderBlocks().renderBlockAsItem(Block.blocksList[stack.itemID], stack.getItemDamage(), 1F);
						GL11.glTranslatef(-1F, -1.1F, 0F);
						GL11.glScalef(2F, 2F, 2F);
					} else {
						int renderPass = 0;
						do {
							Icon icon = stack.getItem().getIcon(stack, renderPass);
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
					}
				}
			}
			GL11.glPopMatrix();
		}

		GL11.glPushMatrix();
		GL11.glTranslatef(0.5F, 1.8F, 0.5F);
		GL11.glRotatef(180F, 1F, 0F, 1F);
		int repeat = 15;
		cubes.renderSpinningCubes(2, repeat, repeat);
		GL11.glPopMatrix();

		GL11.glTranslatef(0F, 1.2F, 2F);
		float scale = altar.getTargetMana() == 0 ? 0 : (float) altar.getCurrentMana() / (float) altar.getTargetMana() / 75F;

		if(scale != 0) {
			int seed = altar.xCoord ^ altar.yCoord ^ altar.zCoord;
			GL11.glTranslatef(0.5F, 0.7F, 0.5F);
			RenderHelper.renderStar(0x00E4D7, scale, scale, scale, seed);
		}

		GL11.glPopMatrix();
	}
}
