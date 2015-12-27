/**
 * This class was created by <Vazkii>. It's distributed as
 * part of the Botania Mod. Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 * 
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 * 
 * File Created @ [Oct 31, 2014, 4:53:15 PM (GMT)]
 */
package vazkii.botania.client.render.tile;

import java.awt.Color;

import net.minecraft.block.Block;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.ItemRenderer;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.ForgeHooksClient;

import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL12;

import vazkii.botania.client.core.handler.ClientTickHandler;
import vazkii.botania.client.lib.LibResources;
import vazkii.botania.client.model.ModelBrewery;
import vazkii.botania.common.block.tile.TileBrewery;

public class RenderTileBrewery extends TileEntitySpecialRenderer {

	private static final ResourceLocation texture = new ResourceLocation(LibResources.MODEL_BREWERY);
	ModelBrewery model = new ModelBrewery();
	public TileBrewery brewery;
	public static boolean rotate = true;

	@Override
	public void renderTileEntityAt(TileEntity tileentity, double d0, double d1, double d2, float f, int digProgress) {
		brewery = (TileBrewery) tileentity;
		GlStateManager.pushMatrix();
		GlStateManager.enableRescaleNormal();
		GlStateManager.color(1F, 1F, 1F, 1F);
		GlStateManager.translate(d0, d1, d2);

		Minecraft.getMinecraft().renderEngine.bindTexture(texture);
		GlStateManager.scale(1F, -1F, -1F);
		GlStateManager.translate(0.5F, -1.5F, -0.5F);

		double time = ClientTickHandler.ticksInGame + f;
		if(!rotate)
			time = -1;

		model.render(this, time);
		GlStateManager.enableRescaleNormal();
		GlStateManager.popMatrix();
	}

	public void renderItemStack(ItemStack stack) {
		if(stack != null) {
			Minecraft mc = Minecraft.getMinecraft();
			mc.renderEngine.bindTexture(TextureMap.locationBlocksTexture);

			float s = 0.25F;
			GlStateManager.scale(s, s, s);
			GlStateManager.scale(2F, 2F, 2F);
//			mc.getRenderItem().renderItemModel(stack); // todo 1.8
//			if(!ForgeHooksClient.renderEntityItem(new EntityItem(brewery.getWorld(), brewery.xCoord, brewery.yCoord, brewery.zCoord, stack), stack, 0F, 0F, brewery.getWorld().rand, mc.renderEngine, RenderBlocks.getInstance(), 1)) {
//				GlStateManager.scale(0.5F, 0.5F, 0.5F);
//				if(stack.getItem() instanceof ItemBlock && RenderBlocks.renderItemIn3d(Block.getBlockFromItem(stack.getItem()).getRenderType())) {
//					GlStateManager.scale(0.5F, 0.5F, 0.5F);
//					GlStateManager.translate(1F, 1.1F, 0F);
//					GlStateManager.pushMatrix();
//					RenderBlocks.getInstance().renderBlockAsItem(Block.getBlockFromItem(stack.getItem()), stack.getItemDamage(), 1F);
//					GlStateManager.popMatrix();
//					GlStateManager.translate(-1F, -1.1F, 0F);
//					GlStateManager.scale(2F, 2F, 2F);
//				} else {
//					int renderPass = 0;
//					do {
//						IIcon icon = stack.getItem().getIcon(stack, renderPass);
//						if(icon != null) {
//							Color color = new Color(stack.getItem().getColorFromItemStack(stack, renderPass));
//							GL11.glColor3ub((byte) color.getRed(), (byte) color.getGreen(), (byte) color.getBlue());
//							float f = icon.getMinU();
//							float f1 = icon.getMaxU();
//							float f2 = icon.getMinV();
//							float f3 = icon.getMaxV();
//
//							ItemRenderer.renderItemIn2D(Tessellator.getInstance(), f1, f2, f, f3, icon.getIconWidth(), icon.getIconHeight(), 1F / 16F);
//							GlStateManager.color(1F, 1F, 1F);
//						}
//						renderPass++;
//					} while(renderPass < stack.getItem().getRenderPasses(stack.getItemDamage()));
//				}
//			}
			GlStateManager.scale(1F / s, 1F / s, 1F / s);

			Minecraft.getMinecraft().renderEngine.bindTexture(texture);
		}
	}

}
