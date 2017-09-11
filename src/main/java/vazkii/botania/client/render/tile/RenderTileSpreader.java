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

import java.awt.Color;
import java.util.Random;

import javax.annotation.Nonnull;

import net.minecraft.block.BlockCarpet;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.block.model.ItemCameraTransforms;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.init.Blocks;
import net.minecraft.item.EnumDyeColor;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import vazkii.botania.client.core.handler.ClientTickHandler;
import vazkii.botania.client.core.proxy.ClientProxy;
import vazkii.botania.client.lib.LibResources;
import vazkii.botania.client.model.ModelSpreader;
import vazkii.botania.common.block.ModBlocks;
import vazkii.botania.common.block.tile.mana.TileSpreader;

public class RenderTileSpreader extends TileEntitySpecialRenderer<TileSpreader> {

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
	public void render(@Nonnull TileSpreader spreader, double d0, double d1, double d2, float ticks, int digProgress, float unused) {
		if(!spreader.getWorld().isBlockLoaded(spreader.getPos(), false)
				|| spreader.getWorld().getBlockState(spreader.getPos()).getBlock() != ModBlocks.spreader)
			return;
		
		GlStateManager.pushMatrix();
		GlStateManager.enableRescaleNormal();
		GlStateManager.color(1F, 1F, 1F, 1F);
		GlStateManager.translate(d0, d1, d2);

		GlStateManager.translate(0.5F, 1.5F, 0.5F);
		GlStateManager.rotate(spreader.rotationX + 90F, 0F, 1F, 0F);
		GlStateManager.translate(0F, -1F, 0F);
		GlStateManager.rotate(spreader.rotationY, 1F, 0F, 0F);
		GlStateManager.translate(0F, 1F, 0F);

		ResourceLocation r = spreader.isRedstone() ? textureRs : spreader.isDreamwood() ? textureDw : spreader.isULTRA_SPREADER() ? textureG : texture;
		if(ClientProxy.dootDoot)
			r = spreader.isRedstone() ? textureRsHalloween : spreader.isDreamwood() ? textureDwHalloween : spreader.isULTRA_SPREADER() ? textureGHalloween : textureHalloween;

		Minecraft.getMinecraft().renderEngine.bindTexture(r);
		GlStateManager.scale(1F, -1F, -1F);

		double time = ClientTickHandler.ticksInGame + ticks;

		if(spreader.isULTRA_SPREADER()) {
			Color color = Color.getHSBColor((float) ((time * 5 + new Random(spreader.getPos().hashCode()).nextInt(10000)) % 360) / 360F, 0.4F, 0.9F);
			GlStateManager.color(color.getRed() / 255F, color.getGreen() / 255F, color.getBlue() / 255F);
		}
		model.render();
		GlStateManager.color(1F, 1F, 1F);

		GlStateManager.pushMatrix();
		double worldTicks = spreader.getWorld() == null ? 0 : time;
		GlStateManager.rotate((float) worldTicks % 360, 0F, 1F, 0F);
		GlStateManager.translate(0F, (float) Math.sin(worldTicks / 20.0) * 0.05F, 0F);
		model.renderCube();
		GlStateManager.popMatrix();
		GlStateManager.scale(1F, -1F, -1F);
		ItemStack stack = spreader.getItemHandler().getStackInSlot(0);

		if(!stack.isEmpty()) {
			Minecraft.getMinecraft().renderEngine.bindTexture(TextureMap.LOCATION_BLOCKS_TEXTURE);
			stack.getItem();
			GlStateManager.pushMatrix();
			GlStateManager.translate(0.0F, -1F, -0.4675F);
			GlStateManager.rotate(180, 0, 0, 1);
			GlStateManager.rotate(180, 1, 0, 0);
			GlStateManager.scale(1.0F, 1.0F, 1.0F);
			Minecraft.getMinecraft().getRenderItem().renderItem(stack, ItemCameraTransforms.TransformType.NONE);
			GlStateManager.popMatrix();
		}

		if(spreader.paddingColor != -1) {
			Minecraft.getMinecraft().renderEngine.bindTexture(TextureMap.LOCATION_BLOCKS_TEXTURE);

			IBlockState carpet = Blocks.CARPET.getDefaultState().withProperty(BlockCarpet.COLOR, EnumDyeColor.byMetadata(spreader.paddingColor));

			GlStateManager.translate(-0.5F, -0.5F, 0.5F);
			float f = 1 / 16F;

			GlStateManager.translate(0, -f - 0.001, 0);
			Minecraft.getMinecraft().getBlockRendererDispatcher().renderBlockBrightness(carpet, 1.0F);
			GlStateManager.translate(0, f + 0.001, 0);
			GlStateManager.rotate(-90, 0, 1, 0);

			GlStateManager.translate(-0.001, 0, 0);
			GlStateManager.rotate(270, 0, 0, 1);
			Minecraft.getMinecraft().getBlockRendererDispatcher().renderBlockBrightness(carpet, 1.0F);
			GlStateManager.translate(0, 0.001, 0);
			GlStateManager.rotate(-90, 0, 1, 0);

			GlStateManager.translate(0, 15 * f + 0.001, -0.001);
			Minecraft.getMinecraft().getBlockRendererDispatcher().renderBlockBrightness(carpet, 1.0F);
			GlStateManager.translate(0, -0.001, 0.001);
			GlStateManager.rotate(-90, 0, 1, 0);

			GlStateManager.translate(15 * f + 0.001, f, 0.001);
			GlStateManager.rotate(270, 0, 0, 1);
			Minecraft.getMinecraft().getBlockRendererDispatcher().renderBlockBrightness(carpet, 1.0F);
			GlStateManager.translate(-0.001, 0, -0.001);
			GlStateManager.rotate(-90, 0, 1, 0);

			GlStateManager.translate(-0.001, -1 + f + 0.001, -f + 0.001);
			GlStateManager.rotate(90, 1, 0, 0);
			Minecraft.getMinecraft().getBlockRendererDispatcher().renderBlockBrightness(carpet, 1.0F);
		}

		GlStateManager.enableRescaleNormal();
		GlStateManager.popMatrix();
	}

}
