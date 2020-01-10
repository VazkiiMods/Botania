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

import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.platform.GlStateManager;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.IVertexBuilder;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.Vector3f;
import net.minecraft.client.renderer.tileentity.TileEntityRenderer;
import net.minecraft.client.renderer.tileentity.TileEntityRendererDispatcher;
import net.minecraft.util.ResourceLocation;
import vazkii.botania.api.state.BotaniaStateProps;
import vazkii.botania.client.lib.LibResources;
import vazkii.botania.client.model.ModelBellows;
import vazkii.botania.common.block.ModBlocks;
import vazkii.botania.common.block.tile.mana.TileBellows;

import javax.annotation.Nullable;

public class RenderTileBellows extends TileEntityRenderer<TileBellows> {
	private static final ResourceLocation texture = new ResourceLocation(LibResources.MODEL_BELLOWS);
	private static final ModelBellows model = new ModelBellows();

	public RenderTileBellows(TileEntityRendererDispatcher manager) {
		super(manager);
	}

	@Override
	public void render(@Nullable TileBellows bellows, float f, MatrixStack ms, IRenderTypeBuffer buffers, int light, int overlay) {
		ms.push();
		ms.translate(0.5F, 1.5F, 0.5F);
		ms.scale(1F, -1F, -1F);
		float angle = 0;
		if(bellows != null) {
			switch(bellows.getBlockState().get(BotaniaStateProps.CARDINALS)) {
				case SOUTH: break;
				case NORTH: angle = 180F; break;
				case EAST: angle = 270F; break;
				case WEST: angle = 90F; break;
			}
		}
		ms.multiply(Vector3f.POSITIVE_Y.getDegreesQuaternion(angle));
		float fract = Math.max(0.1F, 1F - (bellows == null ? 0 : bellows.movePos + bellows.moving * f + 0.1F));
		IVertexBuilder buffer = buffers.getBuffer(model.getLayer(texture));
		model.render(ms, buffer, light, overlay, 1, 1, 1, 1, fract);
		ms.pop();
	}

}
