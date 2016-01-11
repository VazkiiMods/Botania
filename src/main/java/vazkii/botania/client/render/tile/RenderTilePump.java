/**
 * This class was created by <Vazkii>. It's distributed as
 * part of the Botania Mod. Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 * 
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 * 
 * File Created @ [Mar 18, 2015, 3:15:38 PM (GMT)]
 */
package vazkii.botania.client.render.tile;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ResourceLocation;

import vazkii.botania.api.state.BotaniaStateProps;
import vazkii.botania.client.lib.LibResources;
import vazkii.botania.client.model.ModelPump;
import vazkii.botania.common.block.tile.mana.TilePump;

public class RenderTilePump extends TileEntitySpecialRenderer<TilePump> {

	private static final float[] ROTATIONS = new float[] {
		180F, 0F, 90F, 270F
	};

	private static final ResourceLocation texture = new ResourceLocation(LibResources.MODEL_PUMP);
	private static final ModelPump model = new ModelPump();

	@Override
	public void renderTileEntityAt(TilePump pump, double d0, double d1, double d2, float f, int digProgress) {
		if (pump != null && pump.getWorld() != null && !pump.getWorld().isBlockLoaded(pump.getPos(), false)) {
			return;
		}
		
		GlStateManager.pushMatrix();
		GlStateManager.enableRescaleNormal();
		GlStateManager.color(1F, 1F, 1F, 1F);
		GlStateManager.translate(d0, d1, d2);

		Minecraft.getMinecraft().renderEngine.bindTexture(texture);
		EnumFacing facing = pump != null && pump.getWorld() != null ? pump.getWorld().getBlockState(pump.getPos()).getValue(BotaniaStateProps.CARDINALS) : EnumFacing.SOUTH;

		GlStateManager.translate(0.5F, 0.5F, 0.5F);
		GlStateManager.scale(1F, -1F, -1F);
		GlStateManager.rotate(ROTATIONS[Math.max(Math.min(ROTATIONS.length - 1, facing.getIndex() - 2), 0)], 0F, 1F, 0F);
		model.render(Math.max(0F, Math.min(8F, pump == null ? 8 : pump.innerRingPos + pump.moving * f)));
		GlStateManager.color(1F, 1F, 1F);
		GlStateManager.scale(1F, -1F, -1F);
		GlStateManager.enableRescaleNormal();
		GlStateManager.popMatrix();
	}

}
