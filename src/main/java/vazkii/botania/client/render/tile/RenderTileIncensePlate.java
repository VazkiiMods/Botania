/*
 * This class is distributed as part of the Botania Mod.
 * Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 *
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 */
package vazkii.botania.client.render.tile;

import com.google.common.collect.ImmutableMap;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Vector3f;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.block.model.ItemTransforms;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderDispatcher;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.core.Direction;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;

import vazkii.botania.common.block.tile.TileIncensePlate;

import javax.annotation.Nonnull;

import java.util.Map;

public class RenderTileIncensePlate extends BlockEntityRenderer<TileIncensePlate> {

	private static final Map<Direction, Integer> ROTATIONS = ImmutableMap.of(Direction.NORTH, 180, Direction.SOUTH, 0, Direction.WEST, 270, Direction.EAST, 90);

	public RenderTileIncensePlate(BlockEntityRenderDispatcher manager) {
		super(manager);
	}

	@SuppressWarnings("deprecation")
	@Override
	public void render(@Nonnull TileIncensePlate plate, float ticks, PoseStack ms, MultiBufferSource buffers, int light, int overlay) {
		ItemStack stack = plate.getItemHandler().getItem(0);
		if (stack.isEmpty()) {
			return;
		}

		Direction facing = plate.getBlockState().getValue(BlockStateProperties.HORIZONTAL_FACING);

		ms.pushPose();
		ms.translate(0.5F, 1.5F, 0.5F);
		ms.mulPose(Vector3f.YP.rotationDegrees(ROTATIONS.get(facing)));
		float s = 0.6F;
		ms.translate(-0.11F, -1.35F, 0F);
		ms.scale(s, s, s);
		Minecraft.getInstance().getItemRenderer().renderStatic(stack, ItemTransforms.TransformType.GROUND, light, overlay, ms, buffers);
		ms.popPose();
	}

}
