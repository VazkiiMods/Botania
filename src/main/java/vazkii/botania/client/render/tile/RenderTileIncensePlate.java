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
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.block.entity.BlockEntityRenderDispatcher;
import net.minecraft.client.render.block.entity.BlockEntityRenderer;
import net.minecraft.client.render.model.json.ModelTransformation;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.client.util.math.Vector3f;
import net.minecraft.item.ItemStack;
import net.minecraft.state.property.Properties;
import net.minecraft.util.math.Direction;
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
	public void render(@Nonnull TileIncensePlate plate, float ticks, MatrixStack ms, VertexConsumerProvider buffers, int light, int overlay) {
		ItemStack stack = plate.getItemHandler().getStack(0);
		if (stack.isEmpty()) {
			return;
		}

		Direction facing = plate.getCachedState().get(Properties.HORIZONTAL_FACING);

		ms.push();
		ms.translate(0.5F, 1.5F, 0.5F);
		ms.multiply(Vector3f.POSITIVE_Y.getDegreesQuaternion(ROTATIONS.get(facing)));
		float s = 0.6F;
		ms.translate(-0.11F, -1.35F, 0F);
		ms.scale(s, s, s);
		MinecraftClient.getInstance().getItemRenderer().renderItem(stack, ModelTransformation.Mode.GROUND, light, overlay, ms, buffers);
		ms.pop();
	}

}
