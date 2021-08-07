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

import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;

import vazkii.botania.client.core.handler.ClientTickHandler;
import vazkii.botania.client.model.ModelBrewery;
import vazkii.botania.common.block.tile.TileBrewery;

import javax.annotation.Nullable;

public class RenderTileBrewery implements BlockEntityRenderer<TileBrewery> {
	final ModelBrewery model = new ModelBrewery();

	public RenderTileBrewery(BlockEntityRendererProvider.Context ctx) {}

	@Override
	public void render(@Nullable TileBrewery brewery, float f, PoseStack ms, MultiBufferSource buffers, int light, int overlay) {
		ms.pushPose();

		ms.scale(1F, -1F, -1F);
		ms.translate(0.5F, -1.5F, -0.5F);

		double time = ClientTickHandler.ticksInGame + f;

		model.render(brewery, time, ms, buffers, light, overlay);
		ms.popPose();
	}

}
