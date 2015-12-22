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
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.ItemRenderer;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.entity.passive.EntitySheep;
import net.minecraft.item.EnumDyeColor;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.util.IIcon;
import net.minecraftforge.common.util.ForgeDirection;

import org.lwjgl.opengl.GL11;

import vazkii.botania.common.entity.EntityCorporeaSpark;
import vazkii.botania.common.item.ItemCorporeaSpark;

public class RenderCorporeaSpark extends RenderSparkBase<EntityCorporeaSpark> {

	public RenderCorporeaSpark(RenderManager p_i46185_1_) {
		super(p_i46185_1_);
	}

	@Override
	public TextureAtlasSprite getBaseIcon(EntityCorporeaSpark entity) {
		return entity.isMaster() ? ItemCorporeaSpark.worldIconMaster : ItemCorporeaSpark.worldIcon;
	}

	@Override
	public void colorSpinningIcon(EntityCorporeaSpark entity, float a) {
		int network = Math.min(15, entity.getNetwork());
		int hex = EnumDyeColor.byMetadata(network).getMapColor().colorValue;

		GlStateManager.color(EntitySheep.fleeceColorTable[network][0], EntitySheep.fleeceColorTable[network][1], EntitySheep.fleeceColorTable[network][2], a);
	}

	@Override
	public TextureAtlasSprite getSpinningIcon(EntityCorporeaSpark entity) {
		return ItemCorporeaSpark.iconColorStar;
	}

	@Override
	public void renderCallback(EntityCorporeaSpark entity, float pticks) {
		int time = entity.getItemDisplayTicks();
		if(time == 0)
			return;

		float absTime = Math.abs(time) - pticks;

		GlStateManager.pushMatrix();
		GlStateManager.rotate(90F, 1F, 0F, 0F);
		float scalef = 1F / 6F;
		GlStateManager.scale(scalef, scalef, scalef);
		GlStateManager.enableBlend();
		GlStateManager.blendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
		GlStateManager.color(1F, 1F, 1F, absTime / 10);
		GlStateManager.translate(0F, 0F, -2F + (time < 0 ? -absTime : absTime) / 6);

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
			GlStateManager.scale(scale, scale, 1F);
			for(int i = -shift; i < shift; i++) {
				GlStateManager.translate(gap * i, 0F, 0F);
				for(int j = -shift; j < shift; j++) {
					GlStateManager.translate(0F, gap * j, 0F);
					ItemRenderer.renderItemIn2D(Tessellator.getInstance(), minU + stepU * (i + shift), minV + stepV * (j + shift + 1), minU + stepU * (i + shift + 1), minV + stepV * (j + shift), icon.getIconWidth() / pieces, icon.getIconHeight() / pieces, 1F / 8F);
					GlStateManager.translate(0F, -gap * j, 0F);
				}
				GlStateManager.translate(-gap * i, 0F, 0F);
			}
		}

		GlStateManager.disableBlend();
		GlStateManager.popMatrix();
	}

}
