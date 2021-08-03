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
import net.minecraft.world.item.ItemStack;

import vazkii.botania.common.block.tile.TileSparkChanger;

import javax.annotation.Nonnull;

public class RenderTileSparkChanger extends BlockEntityRenderer<TileSparkChanger> {

	public RenderTileSparkChanger(BlockEntityRenderDispatcher manager) {
		super(manager);
	}

	@Override
	public void render(@Nonnull TileSparkChanger tileentity, float pticks, PoseStack ms, MultiBufferSource buffers, int light, int overlay) {
		ms.pushPose();
		ms.mulPose(Vector3f.XP.rotationDegrees(90));
		ms.translate(1.0F, -0.125F, -0.25F);
		ItemStack stack = tileentity.getItemHandler().getItem(0);
		if (!stack.isEmpty()) {
			ms.mulPose(Vector3f.YP.rotationDegrees(180));
			ms.translate(0.5F, 0.5F, 0);
			Minecraft.getInstance().getItemRenderer().renderStatic(stack, ItemTransforms.TransformType.GROUND, light, overlay, ms, buffers);
		}
		ms.popPose();
	}

}
