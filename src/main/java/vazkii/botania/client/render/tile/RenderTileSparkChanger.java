/*
 * This class is distributed as part of the Botania Mod.
 * Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 *
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 */
package vazkii.botania.client.render.tile;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.block.entity.BlockEntityRenderDispatcher;
import net.minecraft.client.render.block.entity.BlockEntityRenderer;
import net.minecraft.client.render.model.json.ModelTransformation;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.client.util.math.Vector3f;
import net.minecraft.item.ItemStack;

import vazkii.botania.common.block.tile.TileSparkChanger;

import javax.annotation.Nonnull;

public class RenderTileSparkChanger extends BlockEntityRenderer<TileSparkChanger> {

	public RenderTileSparkChanger(BlockEntityRenderDispatcher manager) {
		super(manager);
	}

	@Override
	public void render(@Nonnull TileSparkChanger tileentity, float pticks, MatrixStack ms, VertexConsumerProvider buffers, int light, int overlay) {
		ms.push();
		ms.multiply(Vector3f.POSITIVE_X.getDegreesQuaternion(90));
		ms.translate(1.0F, -0.125F, -0.25F);
		ItemStack stack = tileentity.getItemHandler().getStack(0);
		if (!stack.isEmpty()) {
			ms.multiply(Vector3f.POSITIVE_Y.getDegreesQuaternion(180));
			ms.translate(0.5F, 0.5F, 0);
			MinecraftClient.getInstance().getItemRenderer().renderItem(stack, ModelTransformation.Mode.GROUND, light, overlay, ms, buffers);
		}
		ms.pop();
	}

}
