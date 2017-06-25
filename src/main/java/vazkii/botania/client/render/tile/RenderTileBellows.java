/**
 * This class was created by <Vazkii>. It's distributed as
 * part of the Botania Mod. Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 *
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 *
 * File Created @ [Aug 28, 2015, 5:30:41 PM (GMT)]
 */
package vazkii.botania.client.render.tile;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.util.ResourceLocation;
import vazkii.botania.client.lib.LibResources;
import vazkii.botania.client.model.ModelBellows;
import vazkii.botania.common.block.ModBlocks;
import vazkii.botania.common.block.tile.mana.TileBellows;

import javax.annotation.Nullable;

public class RenderTileBellows extends TileEntitySpecialRenderer<TileBellows> {

	private static final float[] ROTATIONS = new float[] {
			180F, 0F, 90F, 270F
	};

	private static final ResourceLocation texture = new ResourceLocation(LibResources.MODEL_BELLOWS);
	private static final ModelBellows model = new ModelBellows();

	@Override
	public void render(@Nullable TileBellows bellows, double d0, double d1, double d2, float f, int digProgress, float unused) {
		if (bellows != null)
			if (!bellows.getWorld().isBlockLoaded(bellows.getPos(), false)
					|| bellows.getWorld().getBlockState(bellows.getPos()).getBlock() != ModBlocks.bellows)
				return;

		GlStateManager.pushMatrix();
		GlStateManager.enableRescaleNormal();
		GlStateManager.color(1F, 1F, 1F, 1F);
		GlStateManager.translate(d0, d1, d2);

		Minecraft.getMinecraft().renderEngine.bindTexture(texture);
		int meta = bellows != null && bellows.getWorld() != null ? bellows.getBlockMetadata() : 0;

		GlStateManager.translate(0.5F, 1.5F, 0.5F);
		GlStateManager.scale(1F, -1F, -1F);
		GlStateManager.rotate(ROTATIONS[Math.max(Math.min(ROTATIONS.length, meta - 2), 0)], 0F, 1F, 0F);
		model.render(Math.max(0.1F, 1F - (bellows == null ? 0 : bellows.movePos + bellows.moving * f + 0.1F)));
		GlStateManager.color(1F, 1F, 1F);
		GlStateManager.scale(1F, -1F, -1F);
		GlStateManager.enableRescaleNormal();
		GlStateManager.popMatrix();
	}

}
