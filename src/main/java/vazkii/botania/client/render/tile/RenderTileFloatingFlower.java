/**
 * This class was created by <Vazkii>. It's distributed as
 * part of the Botania Mod. Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 *
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 *
 * File Created @ [Jul 8, 2014, 10:58:46 PM (GMT)]
 */
package vazkii.botania.client.render.tile;

import net.minecraft.block.BlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.BlockModelRenderer;
import net.minecraft.client.renderer.BlockRendererDispatcher;
import com.mojang.blaze3d.platform.GlStateManager;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.model.IBakedModel;
import net.minecraft.client.renderer.model.ModelResourceLocation;
import net.minecraft.client.renderer.texture.AtlasTexture;
import net.minecraft.client.renderer.texture.AtlasTexture;
import net.minecraft.client.renderer.tileentity.TileEntityRenderer;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.Direction;
import net.minecraftforge.client.model.data.IModelData;
import net.minecraftforge.common.util.LazyOptional;
import org.lwjgl.opengl.GL11;
import vazkii.botania.api.BotaniaAPI;
import vazkii.botania.api.item.IFloatingFlower;
import vazkii.botania.api.state.BotaniaStateProps;
import vazkii.botania.client.core.handler.ClientTickHandler;
import vazkii.botania.common.core.handler.ConfigHandler;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.Random;

public class RenderTileFloatingFlower extends TileEntityRenderer {

	@Override
	public void render(@Nonnull TileEntity tile, double d0, double d1, double d2, float t, int digProgress) {
		if (ConfigHandler.CLIENT.staticFloaters.get())
			return;

		IModelData data = tile.getModelData();
		if (!data.hasProperty(BotaniaStateProps.FLOATING_DATA))
			return;

		GlStateManager.pushMatrix();
		GlStateManager.color4f(1F, 1F, 1F, 1F);
		GlStateManager.translated(d0, d1, d2);

		double worldTime = ClientTickHandler.ticksInGame + t;
		if(tile.getWorld() != null)
			worldTime += new Random(tile.getPos().hashCode()).nextInt(1000);

		GlStateManager.translatef(0.5F, 0, 0.5F);
		GlStateManager.rotatef(-((float) worldTime * 0.5F), 0F, 1F, 0F);
		GlStateManager.translated(-0.5, (float) Math.sin(worldTime * 0.05F) * 0.1F, 0.5);

		GlStateManager.rotatef(4F * (float) Math.sin(worldTime * 0.04F), 1F, 0F, 0F);
		GlStateManager.rotatef(90.0F, 0.0F, 1.0F, 0.0F);

		Minecraft.getInstance().textureManager.bindTexture(AtlasTexture.LOCATION_BLOCKS_TEXTURE);

		BlockRendererDispatcher brd = Minecraft.getInstance().getBlockRendererDispatcher();
		BlockState state = tile.getWorld().getBlockState(tile.getPos());
		IBakedModel model = brd.getModelForState(state);
		renderModelBrightnessColor(brd.getBlockModelRenderer(), state, model,  data);

		GlStateManager.popMatrix();

	}

	// [VanillaCopy] Like BlockModelRenderer.renderModelBrightnessColor,
	// but no colors and call the getQuads overload with modeldata
	private static void renderModelBrightnessColor(BlockModelRenderer renderer, BlockState state, IBakedModel modelIn, IModelData data) {
		Random random = new Random();

		for(Direction direction : Direction.values()) {
			random.setSeed(42L);
			renderer.renderModelBrightnessColorQuads(1, 1, 1, 1, modelIn.getQuads(state, direction, random, data));
		}

		random.setSeed(42L);
		renderer.renderModelBrightnessColorQuads(1, 1, 1, 1, modelIn.getQuads(state, null, random, data));
	}

}
