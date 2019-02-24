/**
 * This class was created by <Vazkii>. It's distributed as
 * part of the Botania Mod. Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 *
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 *
 * File Created @ [Jan 17, 2015, 8:05:07 PM (GMT)]
 */
package vazkii.botania.client.render.tile;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.model.ItemCameraTransforms;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.client.renderer.tileentity.TileEntityRenderer;
import net.minecraft.item.ItemStack;
import vazkii.botania.api.mana.ILens;
import vazkii.botania.client.core.handler.ClientTickHandler;
import vazkii.botania.common.block.tile.mana.TilePrism;

import javax.annotation.Nonnull;

public class RenderTilePrism extends TileEntityRenderer<TilePrism> {

	@Override
	public void render(@Nonnull TilePrism prism, double x, double y, double z, float partTicks, int digProgress) {
		GlStateManager.pushMatrix();
		GlStateManager.translated(x, y, z);
		float pos = (float) Math.sin((ClientTickHandler.ticksInGame + partTicks) * 0.05F) * 0.5F * (1F - 1F / 16F) - 0.5F;

		ItemStack stack = prism.getItemHandler().getStackInSlot(0);

		if(!stack.isEmpty()) {
			Minecraft.getInstance().textureManager.bindTexture(TextureMap.LOCATION_BLOCKS_TEXTURE);
			if(stack.getItem() instanceof ILens) {
				GlStateManager.pushMatrix();
				GlStateManager.rotatef(90F, 1F, 0F, 0F);
				GlStateManager.translatef(0.5F, 0.5F, pos);
				Minecraft.getInstance().getItemRenderer().renderItem(stack, ItemCameraTransforms.TransformType.NONE);
				GlStateManager.popMatrix();
			}
		}
		GlStateManager.popMatrix();
	}

}
