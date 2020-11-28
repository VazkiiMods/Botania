/*
 * This class is distributed as part of the Botania Mod.
 * Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 *
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 */
package vazkii.botania.client.render.tile;

import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.block.entity.BlockEntityRenderDispatcher;
import net.minecraft.client.render.block.entity.BlockEntityRenderer;
import net.minecraft.client.util.math.MatrixStack;

import vazkii.botania.client.core.handler.ClientTickHandler;
import vazkii.botania.client.model.ModelBrewery;
import vazkii.botania.common.block.tile.TileBrewery;

import javax.annotation.Nullable;

public class RenderTileBrewery extends BlockEntityRenderer<TileBrewery> {
	final ModelBrewery model = new ModelBrewery();

	public RenderTileBrewery(BlockEntityRenderDispatcher manager) {
		super(manager);
	}

	@Override
	public void render(@Nullable TileBrewery brewery, float f, MatrixStack ms, VertexConsumerProvider buffers, int light, int overlay) {
		ms.push();

		ms.scale(1F, -1F, -1F);
		ms.translate(0.5F, -1.5F, -0.5F);

		double time = ClientTickHandler.ticksInGame + f;

		model.render(brewery, time, ms, buffers, light, overlay);
		ms.pop();
	}

}
