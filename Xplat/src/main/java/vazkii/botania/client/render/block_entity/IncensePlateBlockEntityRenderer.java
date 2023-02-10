/*
 * This class is distributed as part of the Botania Mod.
 * Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 *
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 */
package vazkii.botania.client.render.block_entity;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Vector3f;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.block.model.ItemTransforms;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.core.Direction;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;

import org.jetbrains.annotations.NotNull;

import vazkii.botania.common.block.block_entity.IncensePlateBlockEntity;

public class IncensePlateBlockEntityRenderer implements BlockEntityRenderer<IncensePlateBlockEntity> {
	public IncensePlateBlockEntityRenderer(BlockEntityRendererProvider.Context ctx) {}

	@Override
	public void render(@NotNull IncensePlateBlockEntity plate, float ticks, PoseStack ms, MultiBufferSource buffers, int light, int overlay) {
		ItemStack stack = plate.getItemHandler().getItem(0);
		if (stack.isEmpty()) {
			return;
		}

		Direction facing = plate.getBlockState().getValue(BlockStateProperties.HORIZONTAL_FACING);

		ms.pushPose();
		ms.translate(0.5F, 1.5F, 0.5F);
		int degrees = switch (facing) {
			default -> 0;
			case WEST -> 90;
			case SOUTH -> 180;
			case EAST -> 270;
		};
		ms.mulPose(Vector3f.YP.rotationDegrees(degrees));
		float s = 0.6F;
		ms.translate(-0.11F, -1.35F, 0F);
		ms.scale(s, s, s);
		Minecraft.getInstance().getItemRenderer().renderStatic(stack, ItemTransforms.TransformType.GROUND,
				light, overlay, ms, buffers, 0);
		ms.popPose();
	}

}
