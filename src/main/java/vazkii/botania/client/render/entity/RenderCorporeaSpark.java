/**
 * This class was created by <Vazkii>. It's distributed as
 * part of the Botania Mod. Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 * 
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 * 
 * File Created @ [Feb 14, 2015, 1:04:34 AM (GMT)]
 */
package vazkii.botania.client.render.entity;

import net.minecraft.block.Block;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.ItemRenderer;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.entity.passive.EntitySheep;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.util.IIcon;
import net.minecraftforge.common.util.ForgeDirection;

import org.lwjgl.opengl.GL11;

import vazkii.botania.common.entity.EntityCorporeaSpark;
import vazkii.botania.common.item.ItemCorporeaSpark;

public class RenderCorporeaSpark extends RenderSparkBase<EntityCorporeaSpark> {

	@Override
	public IIcon getBaseIcon(EntityCorporeaSpark entity) {
		return entity.isMaster() ? ItemCorporeaSpark.worldIconMaster : ItemCorporeaSpark.worldIcon;
	}

	@Override
	public void colorSpinningIcon(EntityCorporeaSpark entity, float a) {
		int network = Math.min(15, entity.getNetwork());
		GL11.glColor4f(EntitySheep.fleeceColorTable[network][0], EntitySheep.fleeceColorTable[network][1], EntitySheep.fleeceColorTable[network][2], a);
	}

	@Override
	public IIcon getSpinningIcon(EntityCorporeaSpark entity) {
		return ItemCorporeaSpark.iconColorStar;
	}

	@Override
	public void renderCallback(EntityCorporeaSpark entity, float pticks) {
		int time = entity.getItemDisplayTicks();
		if(time == 0)
			return;

		float absTime = Math.abs(time) - pticks;

		GL11.glPushMatrix();
		GL11.glRotated(90F, 1F, 0F, 0F);
		float scalef = 1F / 6F;
		GL11.glScalef(scalef, scalef, scalef);
		GL11.glEnable(GL11.GL_BLEND);
		GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
		GL11.glColor4f(1F, 1F, 1F, absTime / 10);
		GL11.glTranslatef(0F, 0F, -2F + (time < 0 ? -absTime : absTime) / 6);

		ItemStack stack = entity.getDisplayedItem();
		if(stack == null)
			return;

		Item item = stack.getItem();
		boolean block = item instanceof ItemBlock;
		Minecraft.getMinecraft().renderEngine.bindTexture(block ? TextureMap.locationBlocksTexture : TextureMap.locationItemsTexture);
		IIcon icon = block ? Block.getBlockFromItem(item).getBlockTextureFromSide(ForgeDirection.UP.ordinal()) : item.getIcon(stack, 0);

		if(icon != null) {
			float minU = icon.getMinU();
			float maxU = icon.getMaxU();
			float minV = icon.getMinV();
			float maxV = icon.getMaxV();

			int pieces = 8;
			float stepU = (maxU - minU) / pieces;
			float stepV = (maxV - minV) / pieces;
			float gap = 1F + (time > 0 ? 10F - absTime : absTime) * 0.2F;
			int shift = pieces / 2;

			float scale = 1F / pieces * 3F;
			GL11.glScalef(scale, scale, 1F);
			for(int i = -shift; i < shift; i++) {
				GL11.glTranslated(gap * i, 0F, 0F);
				for(int j = -shift; j < shift; j++) {
					GL11.glTranslated(0F, gap * j, 0F);
					ItemRenderer.renderItemIn2D(Tessellator.instance, minU + stepU * (i + shift), minV + stepV * (j + shift + 1), minU + stepU * (i + shift + 1), minV + stepV * (j + shift), icon.getIconWidth() / pieces, icon.getIconHeight() / pieces, 1F / 8F);
					GL11.glTranslated(0F, -gap * j, 0F);
				}
				GL11.glTranslated(-gap * i, 0F, 0F);
			}
		}

		GL11.glDisable(GL11.GL_BLEND);
		GL11.glPopMatrix();
	}

}
