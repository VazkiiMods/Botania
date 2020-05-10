/*
 * This class is distributed as part of the Botania Mod.
 * Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 *
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 */
package vazkii.botania.client.render.tile;

import com.mojang.blaze3d.matrix.MatrixStack;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.Vector3f;
import net.minecraft.client.renderer.model.ItemCameraTransforms;
import net.minecraft.client.renderer.texture.AtlasTexture;
import net.minecraft.client.renderer.tileentity.TileEntityRenderer;
import net.minecraft.client.renderer.tileentity.TileEntityRendererDispatcher;
import net.minecraft.item.ItemStack;

import vazkii.botania.api.mana.ILens;
import vazkii.botania.client.core.handler.ClientTickHandler;
import vazkii.botania.common.block.tile.mana.TilePrism;

import javax.annotation.Nonnull;

public class RenderTilePrism extends TileEntityRenderer<TilePrism> {

	public RenderTilePrism(TileEntityRendererDispatcher manager) {
		super(manager);
	}

	@Override
	public void render(@Nonnull TilePrism prism, float partTicks, MatrixStack ms, IRenderTypeBuffer buffers, int light, int overlay) {
		float pos = (float) Math.sin((ClientTickHandler.ticksInGame + partTicks) * 0.05F) * 0.5F * (1F - 1F / 16F) - 0.5F;

		ItemStack stack = prism.getItemHandler().getStackInSlot(0);

		if (!stack.isEmpty()) {
			Minecraft.getInstance().textureManager.bindTexture(AtlasTexture.LOCATION_BLOCKS_TEXTURE);
			if (stack.getItem() instanceof ILens) {
				ms.push();
				ms.rotate(Vector3f.XP.rotationDegrees(90));
				ms.translate(0.5F, 0.5F, pos);
				Minecraft.getInstance().getItemRenderer().renderItem(stack, ItemCameraTransforms.TransformType.NONE, light, overlay, ms, buffers);
				ms.pop();
			}
		}
	}

}
