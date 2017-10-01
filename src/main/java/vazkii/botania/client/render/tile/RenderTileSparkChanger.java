/**
 * This class was created by <Vazkii>. It's distributed as
 * part of the Botania Mod. Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 *
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 *
 * File Created @ [Jun 28, 2015, 10:19:20 PM (GMT)]
 */
package vazkii.botania.client.render.tile;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.block.model.ItemCameraTransforms;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.item.ItemStack;
import vazkii.botania.common.block.tile.TileSparkChanger;

import javax.annotation.Nonnull;

public class RenderTileSparkChanger extends TileEntitySpecialRenderer<TileSparkChanger> {

	@Override
	public void render(@Nonnull TileSparkChanger tileentity, double d0, double d1, double d2, float pticks, int digProgress, float unused) {
		GlStateManager.pushMatrix();
		GlStateManager.translate(d0, d1, d2);
		GlStateManager.rotate(90F, 1F, 0F, 0F);
		GlStateManager.translate(1.0F, -0.125F, -0.25F);
		GlStateManager.color(1F, 1F, 1F, 1F);
		ItemStack stack = tileentity.getItemHandler().getStackInSlot(0);
		if(!stack.isEmpty()) {
			GlStateManager.pushMatrix();
			Minecraft.getMinecraft().renderEngine.bindTexture(TextureMap.LOCATION_BLOCKS_TEXTURE);
			float s = 1.0F;
			GlStateManager.scale(s, s, s);
			GlStateManager.rotate(180F, 0F, 1F, 0F);
			GlStateManager.translate(0.5, 0.5, 0);
			Minecraft.getMinecraft().getRenderItem().renderItem(stack, ItemCameraTransforms.TransformType.GROUND);
			GlStateManager.popMatrix();
		}
		GlStateManager.color(1F, 1F, 1F, 1F);
		GlStateManager.popMatrix();
	}

}
