/**
 * This class was created by <Vazkii>. It's distributed as
 * part of the Botania Mod. Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 * 
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 * 
 * File Created @ [Jan 30, 2015, 1:37:26 PM (GMT)]
 */
package vazkii.botania.common.block.subtile.generating;

import java.awt.Color;
import java.util.List;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.client.renderer.entity.RenderItem;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.AxisAlignedBB;

import org.lwjgl.opengl.GL11;

import vazkii.botania.api.lexicon.LexiconEntry;
import vazkii.botania.api.subtile.RadiusDescriptor;
import vazkii.botania.api.subtile.SubTileGenerating;
import vazkii.botania.common.lexicon.LexiconData;

public class SubTileSpectrolus extends SubTileGenerating {

	private static final String TAG_NEXT_COLOR = "nextColor";

	private static final int RANGE = 1;

	int nextColor;

	@Override
	public void onUpdate() {
		super.onUpdate();

		boolean remote = supertile.getWorldObj().isRemote;
		Item wool = Item.getItemFromBlock(Blocks.wool);
		List<EntityItem> items = supertile.getWorldObj().getEntitiesWithinAABB(EntityItem.class, AxisAlignedBB.getBoundingBox(supertile.xCoord - RANGE, supertile.yCoord - RANGE, supertile.zCoord - RANGE, supertile.xCoord + RANGE + 1, supertile.yCoord + RANGE + 1, supertile.zCoord + RANGE + 1));
		int slowdown = getSlowdownFactor();
		
		for(EntityItem item : items) {
			ItemStack stack = item.getEntityItem();
			if(stack != null && stack.getItem() == wool && !item.isDead && item.age >= slowdown) {
				int meta = stack.getItemDamage();
				if(meta == nextColor) {
					if(!remote) {
						mana = Math.min(getMaxMana(), mana + 300);
						nextColor = nextColor == 15 ? 0 : nextColor + 1;
						sync();
					}
					
					for(int i = 0; i < 10; i++) {
						float m = 0.2F;
						float mx = (float) (Math.random() - 0.5) * m;
						float my = (float) (Math.random() - 0.5) * m;
						float mz = (float) (Math.random() - 0.5) * m;
						supertile.getWorldObj().spawnParticle("blockcrack_" + Item.getIdFromItem(stack.getItem()) + "_" + meta, item.posX, item.posY, item.posZ, mx, my, mz);
					}
				}
				
				if(!remote)
					item.setDead();
			}
		}
	}

	@Override
	public RadiusDescriptor getRadius() {
		return new RadiusDescriptor.Square(toChunkCoordinates(), RANGE);
	}

	@Override
	public int getMaxMana() {
		return 8000;
	}

	@Override
	public int getColor() {
		return Color.HSBtoRGB(ticksExisted / 100F, 1F, 1F);
	}

	@Override
	public LexiconEntry getEntry() {
		return LexiconData.spectrolus;
	}

	@Override
	public void renderHUD(Minecraft mc, ScaledResolution res) {
		super.renderHUD(mc, res);

		ItemStack stack = new ItemStack(Blocks.wool, 1, nextColor);
		int color = getColor();

		GL11.glEnable(GL11.GL_BLEND);
		GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
		if(stack != null && stack.getItem() != null) {
			String stackName = stack.getDisplayName();
			int width = 16 + mc.fontRenderer.getStringWidth(stackName) / 2;
			int x = res.getScaledWidth() / 2 - width;
			int y = res.getScaledHeight() / 2 + 30;

			mc.fontRenderer.drawStringWithShadow(stackName, x + 20, y + 5, color);
			RenderHelper.enableGUIStandardItemLighting();
			RenderItem.getInstance().renderItemAndEffectIntoGUI(mc.fontRenderer, mc.renderEngine, stack, x, y);
			RenderHelper.disableStandardItemLighting();
		}

		GL11.glDisable(GL11.GL_LIGHTING);
		GL11.glDisable(GL11.GL_BLEND);
	}

	@Override
	public void writeToPacketNBT(NBTTagCompound cmp) {
		super.writeToPacketNBT(cmp);
		cmp.setInteger(TAG_NEXT_COLOR, nextColor);
	}

	@Override
	public void readFromPacketNBT(NBTTagCompound cmp) {
		super.readFromPacketNBT(cmp);
		nextColor = cmp.getInteger(TAG_NEXT_COLOR);
	}

}
