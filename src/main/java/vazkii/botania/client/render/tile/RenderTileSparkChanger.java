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
import net.minecraft.client.renderer.model.ItemCameraTransforms;
import net.minecraft.client.renderer.tileentity.TileEntityRenderer;
import net.minecraft.client.renderer.tileentity.TileEntityRendererDispatcher;
import net.minecraft.item.ItemStack;

import net.minecraft.util.math.vector.Vector3f;
import vazkii.botania.common.block.tile.TileSparkChanger;

import javax.annotation.Nonnull;

public class RenderTileSparkChanger extends TileEntityRenderer<TileSparkChanger> {

	public RenderTileSparkChanger(TileEntityRendererDispatcher manager) {
		super(manager);
	}

	@Override
	public void render(@Nonnull TileSparkChanger tileentity, float pticks, MatrixStack ms, IRenderTypeBuffer buffers, int light, int overlay) {
		ms.push();
		ms.rotate(Vector3f.XP.rotationDegrees(90));
		ms.translate(1.0F, -0.125F, -0.25F);
		ItemStack stack = tileentity.getItemHandler().getStackInSlot(0);
		if (!stack.isEmpty()) {
			ms.rotate(Vector3f.YP.rotationDegrees(180));
			ms.translate(0.5F, 0.5F, 0);
			Minecraft.getInstance().getItemRenderer().renderItem(stack, ItemCameraTransforms.TransformType.GROUND, light, overlay, ms, buffers);
		}
		ms.pop();
	}

}
