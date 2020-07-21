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

import net.minecraft.block.BlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.BlockRendererDispatcher;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.RenderTypeLookup;
import net.minecraft.client.renderer.model.IBakedModel;
import net.minecraft.client.renderer.tileentity.TileEntityRenderer;
import net.minecraft.client.renderer.tileentity.TileEntityRendererDispatcher;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.vector.Vector3f;
import net.minecraftforge.client.model.data.IModelData;

import vazkii.botania.api.state.BotaniaStateProps;
import vazkii.botania.client.core.handler.ClientTickHandler;
import vazkii.botania.common.core.handler.ConfigHandler;

import javax.annotation.Nonnull;

import java.util.Random;

public class RenderTileFloatingFlower extends TileEntityRenderer<TileEntity> {

	public RenderTileFloatingFlower(TileEntityRendererDispatcher manager) {
		super(manager);
	}

	@Override
	public void render(@Nonnull TileEntity tile, float t, MatrixStack ms, IRenderTypeBuffer buffers, int light, int overlay) {
		renderFloatingIsland(tile, t, ms, buffers, light, overlay);
	}

	public static void renderFloatingIsland(TileEntity tile, float t, MatrixStack ms, IRenderTypeBuffer buffers, int light, int overlay) {
		if (ConfigHandler.CLIENT.staticFloaters.get()) {
			return;
		}

		IModelData data = tile.getModelData();
		if (!data.hasProperty(BotaniaStateProps.FLOATING_DATA)) {
			return;
		}

		ms.push();

		double worldTime = ClientTickHandler.ticksInGame + t;
		if (tile.getWorld() != null) {
			worldTime += new Random(tile.getPos().hashCode()).nextInt(1000);
		}

		ms.translate(0.5F, 0, 0.5F);
		ms.rotate(Vector3f.YP.rotationDegrees(-((float) worldTime * 0.5F)));
		ms.translate(-0.5, (float) Math.sin(worldTime * 0.05F) * 0.1F, 0.5);

		ms.rotate(Vector3f.XP.rotationDegrees(4F * (float) Math.sin(worldTime * 0.04F)));
		ms.rotate(Vector3f.YP.rotationDegrees(90.0F));

		BlockRendererDispatcher brd = Minecraft.getInstance().getBlockRendererDispatcher();
		BlockState state = tile.getBlockState();

		IBakedModel ibakedmodel = brd.getModelForState(state);
		brd.getBlockModelRenderer().renderModel(ms.getLast(), buffers.getBuffer(RenderTypeLookup.func_239220_a_(state, false)), state, ibakedmodel, 1, 1, 1, light, overlay, data);

		ms.pop();
	}

}
