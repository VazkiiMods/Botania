/**
 * This class was created by <Vazkii>. It's distributed as
 * part of the Botania Mod. Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 * 
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 * 
 * File Created @ [Nov 17, 2014, 5:48:34 PM (GMT)]
 */
package vazkii.botania.client.render.item;

import net.minecraft.client.renderer.tileentity.TileEntityRendererDispatcher;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraftforge.client.IItemRenderer;

import org.lwjgl.opengl.GL11;

import vazkii.botania.common.block.ModBlocks;
import vazkii.botania.common.block.tile.TileFloatingFlower;
import vazkii.botania.common.item.block.ItemBlockFloatingSpecialFlower;
import vazkii.botania.common.item.block.ItemBlockSpecialFlower;

public class RenderFloatingFlowerItem implements IItemRenderer {

	@Override
	public boolean handleRenderType(ItemStack item, ItemRenderType type) {
		return true;
	}

	@Override
	public boolean shouldUseRenderHelper(ItemRenderType type, ItemStack item, ItemRendererHelper helper) {
		return true;
	}

	@Override
	public void renderItem(ItemRenderType type, ItemStack stack, Object... data) {
		GL11.glPushMatrix();
		GL11.glTranslatef(-0.5F, -0.5F, -0.5F);
		GL11.glEnable(GL11.GL_BLEND);
		GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
		float s = 1.4F;
		GL11.glScalef(s, s, s);
		GL11.glRotatef(-5F, 1F, 0F, 0F);
		Item item = stack.getItem();
		TileFloatingFlower.forcedStack = item instanceof ItemBlockFloatingSpecialFlower ? ItemBlockSpecialFlower.ofType(ItemBlockSpecialFlower.getType(stack)) : new ItemStack(ModBlocks.flower, 1, stack.getItemDamage());

		TileEntityRendererDispatcher.instance.renderTileEntityAt(new TileFloatingFlower(), 0.0D, 0.0D, 0.0D, 0.0F);
		GL11.glDisable(GL11.GL_BLEND);
		GL11.glPopMatrix();
	}

}
