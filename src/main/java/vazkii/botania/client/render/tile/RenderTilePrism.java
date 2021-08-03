/*
 * This class is distributed as part of the Botania Mod.
 * Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 *
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 */
package vazkii.botania.client.render.tile;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Vector3f;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.block.model.ItemTransforms;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderDispatcher;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.texture.TextureAtlas;
import net.minecraft.world.item.ItemStack;

import vazkii.botania.api.mana.ILens;
import vazkii.botania.client.core.handler.ClientTickHandler;
import vazkii.botania.common.block.tile.mana.TilePrism;

import javax.annotation.Nonnull;

public class RenderTilePrism extends BlockEntityRenderer<TilePrism> {

	public RenderTilePrism(BlockEntityRenderDispatcher manager) {
		super(manager);
	}

	@Override
	public void render(@Nonnull TilePrism prism, float partTicks, PoseStack ms, MultiBufferSource buffers, int light, int overlay) {
		float pos = (float) Math.sin((ClientTickHandler.ticksInGame + partTicks) * 0.05F) * 0.5F * (1F - 1F / 16F) - 0.5F;

		ItemStack stack = prism.getItemHandler().getItem(0);

		if (!stack.isEmpty()) {
			Minecraft.getInstance().getTextureManager().bind(TextureAtlas.LOCATION_BLOCKS);
			if (stack.getItem() instanceof ILens) {
				ms.pushPose();
				ms.mulPose(Vector3f.XP.rotationDegrees(90));
				ms.translate(0.5F, 0.5F, pos);
				Minecraft.getInstance().getItemRenderer().renderStatic(stack, ItemTransforms.TransformType.NONE, light, overlay, ms, buffers);
				ms.popPose();
			}
		}
	}

}
