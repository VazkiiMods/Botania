/**
 * This class was created by <Vazkii>. It's distributed as
 * part of the Botania Mod. Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 * 
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 * 
 * File Created @ [May 15, 2015, 4:27:27 PM (GMT)]
 */
package vazkii.botania.client.render.tile;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.block.model.ItemCameraTransforms;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ResourceLocation;

import org.lwjgl.opengl.GL11;

import vazkii.botania.api.state.BotaniaStateProps;
import vazkii.botania.client.lib.LibResources;
import vazkii.botania.client.model.ModelIncensePlate;
import vazkii.botania.common.block.tile.TileIncensePlate;

public class RenderTileIncensePlate extends TileEntitySpecialRenderer<TileIncensePlate> {

	private static final float[] ROTATIONS = new float[] {
		180F, 0F, 90F, 270F
	};

	ResourceLocation texture = new ResourceLocation(LibResources.MODEL_INCENSE_PLATE);
	ModelIncensePlate model = new ModelIncensePlate();

	@Override
	public void renderTileEntityAt(TileIncensePlate plate, double d0, double d1, double d2, float ticks, int digProgress) {
		if (plate != null && plate.getWorld() != null && !plate.getWorld().isBlockLoaded(plate.getPos(), false)) {
			return;
		}
		
		EnumFacing facing = plate.getWorld().getBlockState(plate.getPos()).getValue(BotaniaStateProps.CARDINALS);

		GlStateManager.pushMatrix();
		GlStateManager.enableRescaleNormal();
		GlStateManager.enableBlend();
		GlStateManager.blendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
		GlStateManager.color(1F, 1F, 1F, 1F);
		GlStateManager.translate(d0, d1, d2);
		// Minecraft.getMinecraft().renderEngine.bindTexture(texture);
		GlStateManager.translate(0.5F, 1.5F, 0.5F);
		GlStateManager.scale(1F, -1F, -1F);
		GlStateManager.rotate(ROTATIONS[Math.max(Math.min(ROTATIONS.length, facing.getIndex() - 2), 0)], 0F, 1F, 0F);
		// model.render();
		GlStateManager.scale(1F, -1F, -1F);

		ItemStack stack = plate.getStackInSlot(0);
		if(stack != null) {
			GlStateManager.pushMatrix();
			Minecraft.getMinecraft().renderEngine.bindTexture(TextureMap.locationBlocksTexture);
			float s = 0.4F;
			GlStateManager.translate(-0.075F, -1.25F, 0F);
			GlStateManager.scale(s, s, s);
			//GlStateManager.rotate(180F, 0F, 1F, 0F);
			Minecraft.getMinecraft().getRenderItem().renderItem(stack, ItemCameraTransforms.TransformType.GROUND);
			GlStateManager.popMatrix();
		}
		GlStateManager.color(1F, 1F, 1F);
		GlStateManager.enableRescaleNormal();
		GlStateManager.popMatrix();
	}

}
