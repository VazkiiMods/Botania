/*
 * This class is distributed as part of the Botania Mod.
 * Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 *
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 */
package vazkii.botania.client.render.tile;

import vazkii.botania.client.lib.LibResources;
import vazkii.botania.client.model.ModelBellows;
import vazkii.botania.common.block.tile.mana.TileBellows;

import javax.annotation.Nullable;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.block.entity.BlockEntityRenderDispatcher;
import net.minecraft.client.render.block.entity.BlockEntityRenderer;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.client.util.math.Vector3f;
import net.minecraft.state.property.Properties;
import net.minecraft.util.Identifier;

public class RenderTileBellows extends BlockEntityRenderer<TileBellows> {
	private static final Identifier texture = new Identifier(LibResources.MODEL_BELLOWS);
	private static final ModelBellows model = new ModelBellows();

	public RenderTileBellows(BlockEntityRenderDispatcher manager) {
		super(manager);
	}

	@Override
	public void render(@Nullable TileBellows bellows, float f, MatrixStack ms, VertexConsumerProvider buffers, int light, int overlay) {
		ms.push();
		ms.translate(0.5F, 1.5F, 0.5F);
		ms.scale(1F, -1F, -1F);
		float angle = 0;
		if (bellows != null) {
			switch (bellows.getCachedState().get(Properties.HORIZONTAL_FACING)) {
			case SOUTH:
				break;
			case NORTH:
				angle = 180F;
				break;
			case EAST:
				angle = 270F;
				break;
			case WEST:
				angle = 90F;
				break;
			}
		}
		ms.multiply(Vector3f.POSITIVE_Y.getDegreesQuaternion(angle));
		float fract = Math.max(0.1F, 1F - (bellows == null ? 0 : bellows.movePos + bellows.moving * f + 0.1F));
		VertexConsumer buffer = buffers.getBuffer(model.getLayer(texture));
		model.render(ms, buffer, light, overlay, 1, 1, 1, 1, fract);
		ms.pop();
	}

}
