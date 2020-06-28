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
import com.mojang.blaze3d.matrix.MatrixStack;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.model.ItemCameraTransforms;
import net.minecraft.client.renderer.tileentity.TileEntityRenderer;
import net.minecraft.client.renderer.tileentity.TileEntityRendererDispatcher;
import net.minecraft.item.ItemStack;
import net.minecraft.state.properties.BlockStateProperties;
import net.minecraft.util.Direction;
import net.minecraft.util.math.vector.Vector3f;

import vazkii.botania.common.block.tile.TileIncensePlate;

import javax.annotation.Nonnull;

import java.util.Map;

public class RenderTileIncensePlate extends TileEntityRenderer<TileIncensePlate> {

	private static final Map<Direction, Integer> ROTATIONS = ImmutableMap.of(Direction.NORTH, 180, Direction.SOUTH, 0, Direction.WEST, 270, Direction.EAST, 90);

	public RenderTileIncensePlate(TileEntityRendererDispatcher manager) {
		super(manager);
	}

	@SuppressWarnings("deprecation")
	@Override
	public void render(@Nonnull TileIncensePlate plate, float ticks, MatrixStack ms, IRenderTypeBuffer buffers, int light, int overlay) {
		ItemStack stack = plate.getItemHandler().getStackInSlot(0);
		if (stack.isEmpty()) {
			return;
		}

		Direction facing = plate.getBlockState().get(BlockStateProperties.HORIZONTAL_FACING);

		ms.push();
		ms.translate(0.5F, 1.5F, 0.5F);
		ms.rotate(Vector3f.YP.rotationDegrees(ROTATIONS.get(facing)));
		float s = 0.6F;
		ms.translate(-0.11F, -1.35F, 0F);
		ms.scale(s, s, s);
		Minecraft.getInstance().getItemRenderer().renderItem(stack, ItemCameraTransforms.TransformType.GROUND, light, overlay, ms, buffers);
		ms.pop();
	}

}
