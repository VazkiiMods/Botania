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
import net.minecraft.client.renderer.tileentity.TileEntityRenderer;
import net.minecraft.client.renderer.tileentity.TileEntityRendererDispatcher;
import net.minecraft.util.ResourceLocation;

import vazkii.botania.client.core.handler.ClientTickHandler;
import vazkii.botania.client.lib.LibResources;
import vazkii.botania.client.model.ModelBrewery;
import vazkii.botania.common.block.tile.TileBrewery;

import javax.annotation.Nullable;

public class RenderTileBrewery extends TileEntityRenderer<TileBrewery> {
	public static final ResourceLocation TEXTURE = new ResourceLocation(LibResources.MODEL_BREWERY);
	final ModelBrewery model = new ModelBrewery();
	public TileBrewery brewery;

	public RenderTileBrewery(TileEntityRendererDispatcher manager) {
		super(manager);
	}

	@Override
	public void render(@Nullable TileBrewery brewery, float f, MatrixStack ms, IRenderTypeBuffer buffers, int light, int overlay) {
		this.brewery = brewery;
		ms.push();

		Minecraft.getInstance().textureManager.bindTexture(TEXTURE);
		ms.scale(1F, -1F, -1F);
		ms.translate(0.5F, -1.5F, -0.5F);

		double time = ClientTickHandler.ticksInGame + f;

		model.render(brewery, time, ms, buffers, light, overlay);
		ms.pop();
	}

}
