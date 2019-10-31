/**
 * This class was created by <Vazkii>. It's distributed as
 * part of the Botania Mod. Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 *
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 *
 * File Created @ [Jan 25, 2014, 9:42:31 PM (GMT)]
 */
package vazkii.botania.client.render.tile;

import com.mojang.blaze3d.platform.GlStateManager;
import net.minecraft.block.BlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.model.ItemCameraTransforms;
import net.minecraft.client.renderer.texture.AtlasTexture;
import net.minecraft.client.renderer.tileentity.TileEntityRenderer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import vazkii.botania.api.ColorHelper;
import vazkii.botania.client.core.handler.ClientTickHandler;
import vazkii.botania.client.core.proxy.ClientProxy;
import vazkii.botania.client.lib.LibResources;
import vazkii.botania.client.model.ModelSpreader;
import vazkii.botania.common.block.tile.mana.TileSpreader;

import javax.annotation.Nonnull;
import java.awt.*;
import java.util.Random;

public class RenderTileSpreader extends TileEntityRenderer<TileSpreader> {

	private static final ResourceLocation texture = new ResourceLocation(LibResources.MODEL_SPREADER);
	private static final ResourceLocation textureRs = new ResourceLocation(LibResources.MODEL_SPREADER_REDSTONE);
	private static final ResourceLocation textureDw = new ResourceLocation(LibResources.MODEL_SPREADER_DREAMWOOD);
	private static final ResourceLocation textureG = new ResourceLocation(LibResources.MODEL_SPREADER_GAIA);
	
	private static final ResourceLocation textureHalloween = new ResourceLocation(LibResources.MODEL_SPREADER_HALLOWEEN);
	private static final ResourceLocation textureRsHalloween = new ResourceLocation(LibResources.MODEL_SPREADER_REDSTONE_HALLOWEEN);
	private static final ResourceLocation textureDwHalloween = new ResourceLocation(LibResources.MODEL_SPREADER_DREAMWOOD_HALLOWEEN);
	private static final ResourceLocation textureGHalloween = new ResourceLocation(LibResources.MODEL_SPREADER_GAIA_HALLOWEEN);

	private static final ModelSpreader model = new ModelSpreader();

	@Override
	public void render(@Nonnull TileSpreader spreader, double d0, double d1, double d2, float ticks, int digProgress) {
		GlStateManager.pushMatrix();
		GlStateManager.enableRescaleNormal();
		GlStateManager.color4f(1F, 1F, 1F, 1F);
		GlStateManager.translated(d0, d1, d2);

		GlStateManager.translatef(0.5F, 1.5F, 0.5F);
		GlStateManager.rotatef(spreader.rotationX + 90F, 0F, 1F, 0F);
		GlStateManager.translatef(0F, -1F, 0F);
		GlStateManager.rotatef(spreader.rotationY, 1F, 0F, 0F);
		GlStateManager.translatef(0F, 1F, 0F);

		ResourceLocation r = spreader.isRedstone() ? textureRs : spreader.isDreamwood() ? textureDw : spreader.isULTRA_SPREADER() ? textureG : texture;
		if(ClientProxy.dootDoot)
			r = spreader.isRedstone() ? textureRsHalloween : spreader.isDreamwood() ? textureDwHalloween : spreader.isULTRA_SPREADER() ? textureGHalloween : textureHalloween;

		Minecraft.getInstance().textureManager.bindTexture(r);
		GlStateManager.scalef(1F, -1F, -1F);

		double time = ClientTickHandler.ticksInGame + ticks;

		if(spreader.isULTRA_SPREADER()) {
			Color color = Color.getHSBColor((float) ((time * 5 + new Random(spreader.getPos().hashCode()).nextInt(10000)) % 360) / 360F, 0.4F, 0.9F);
			GlStateManager.color3f(color.getRed() / 255F, color.getGreen() / 255F, color.getBlue() / 255F);
		}
		model.render();
		GlStateManager.color3f(1F, 1F, 1F);

		GlStateManager.pushMatrix();
		double worldTicks = spreader.getWorld() == null ? 0 : time;
		GlStateManager.rotatef((float) worldTicks % 360, 0F, 1F, 0F);
		GlStateManager.translatef(0F, (float) Math.sin(worldTicks / 20.0) * 0.05F, 0F);
		model.renderCube();
		GlStateManager.popMatrix();
		GlStateManager.scalef(1F, -1F, -1F);
		ItemStack stack = spreader.getItemHandler().getStackInSlot(0);

		if(!stack.isEmpty()) {
			Minecraft.getInstance().textureManager.bindTexture(AtlasTexture.LOCATION_BLOCKS_TEXTURE);
			stack.getItem();
			GlStateManager.pushMatrix();
			GlStateManager.translatef(0.0F, -1F, -0.4675F);
			GlStateManager.rotatef(180, 0, 0, 1);
			GlStateManager.rotatef(180, 1, 0, 0);
			GlStateManager.scalef(1.0F, 1.0F, 1.0F);
			Minecraft.getInstance().getItemRenderer().renderItem(stack, ItemCameraTransforms.TransformType.NONE);
			GlStateManager.popMatrix();
		}

		if(spreader.paddingColor != null) {
			Minecraft.getInstance().textureManager.bindTexture(AtlasTexture.LOCATION_BLOCKS_TEXTURE);

			BlockState carpet = ColorHelper.CARPET_MAP.get(spreader.paddingColor).getDefaultState();

			GlStateManager.translatef(-0.5F, -0.5F, 0.5F);
			float f = 1 / 16F;

			GlStateManager.translatef(0, -f - 0.001F, 0);
			Minecraft.getInstance().getBlockRendererDispatcher().renderBlockBrightness(carpet, 1.0F);
			GlStateManager.translatef(0, f + 0.001F, 0);
			GlStateManager.rotatef(-90, 0, 1, 0);

			GlStateManager.translatef(-0.001F, 0, 0);
			GlStateManager.rotatef(270, 0, 0, 1);
			Minecraft.getInstance().getBlockRendererDispatcher().renderBlockBrightness(carpet, 1.0F);
			GlStateManager.translatef(0, 0.001F, 0);
			GlStateManager.rotatef(-90, 0, 1, 0);

			GlStateManager.translatef(0, 15 * f + 0.001F, -0.001F);
			Minecraft.getInstance().getBlockRendererDispatcher().renderBlockBrightness(carpet, 1.0F);
			GlStateManager.translatef(0, -0.001F, 0.001F);
			GlStateManager.rotatef(-90, 0, 1, 0);

			GlStateManager.translatef(15 * f + 0.001F, f, 0.001F);
			GlStateManager.rotatef(270, 0, 0, 1);
			Minecraft.getInstance().getBlockRendererDispatcher().renderBlockBrightness(carpet, 1.0F);
			GlStateManager.translatef(-0.001F, 0, -0.001F);
			GlStateManager.rotatef(-90, 0, 1, 0);

			GlStateManager.translatef(-0.001F, -1 + f + 0.001F, -f + 0.001F);
			GlStateManager.rotatef(90, 1, 0, 0);
			Minecraft.getInstance().getBlockRendererDispatcher().renderBlockBrightness(carpet, 1.0F);
		}

		GlStateManager.enableRescaleNormal();
		GlStateManager.popMatrix();
	}
}
