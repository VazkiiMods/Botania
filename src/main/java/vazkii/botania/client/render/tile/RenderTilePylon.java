/**
 * This class was created by <Vazkii>. It's distributed as
 * part of the Botania Mod. Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 * 
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 * 
 * File Created @ [Feb 18, 2014, 10:18:36 PM (GMT)]
 */
package vazkii.botania.client.render.tile;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.OpenGlHelper;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.util.ResourceLocation;
import org.lwjgl.opengl.GL11;
import vazkii.botania.api.state.BotaniaStateProps;
import vazkii.botania.api.state.enums.PylonVariant;
import vazkii.botania.client.core.handler.ClientTickHandler;
import vazkii.botania.client.core.handler.MultiblockRenderHandler;
import vazkii.botania.client.core.helper.ShaderHelper;
import vazkii.botania.client.lib.LibResources;
import vazkii.botania.client.model.IPylonModel;
import vazkii.botania.client.model.ModelPylon;
import vazkii.botania.client.model.ModelPylonOld;
import vazkii.botania.common.block.ModBlocks;
import vazkii.botania.common.block.tile.TilePylon;
import vazkii.botania.common.core.handler.ConfigHandler;

import javax.annotation.Nonnull;
import java.util.Random;

public class RenderTilePylon extends TileEntitySpecialRenderer<TilePylon> {

	private static final ResourceLocation textureOld = new ResourceLocation(LibResources.MODEL_PYLON_OLD);
	private static final ResourceLocation textureGreenOld = new ResourceLocation(LibResources.MODEL_PYLON_GREEN_OLD);
	private static final ResourceLocation texturePinkOld = new ResourceLocation(LibResources.MODEL_PYLON_PINK_OLD);

	IPylonModel model;
	public static boolean green = false;
	public static boolean pink = false;

	@Override
	public void renderTileEntityAt(@Nonnull TilePylon pylon, double d0, double d1, double d2, float pticks, int digProgress) {
		if(!pylon.getWorld().isBlockLoaded(pylon.getPos(), false)
				|| pylon.getWorld().getBlockState(pylon.getPos()).getBlock() != ModBlocks.pylon)
			return;
		
		if(model == null)
			model = ConfigHandler.oldPylonModel ? new ModelPylonOld() : new ModelPylon();

			GlStateManager.pushMatrix();
			GlStateManager.enableRescaleNormal();
			GlStateManager.enableBlend();
			GlStateManager.blendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
			float a = MultiblockRenderHandler.rendering ? 0.6F : 1F;
			GlStateManager.color(1F, 1F, 1F, a);
			PylonVariant variant = null;
			if(pylon.getWorld() != null && pylon.getWorld().getBlockState(pylon.getPos()).getBlock() == ModBlocks.pylon) {
				variant = pylon.getWorld().getBlockState(pylon.getPos()).getValue(BotaniaStateProps.PYLON_VARIANT);
				green = variant == PylonVariant.NATURA;
				pink = variant == PylonVariant.GAIA;
			}

			if(ConfigHandler.oldPylonModel)
				Minecraft.getMinecraft().renderEngine.bindTexture(pink ? texturePinkOld : green ? textureGreenOld : textureOld);
			else Minecraft.getMinecraft().renderEngine.bindTexture(TextureMap.LOCATION_BLOCKS_TEXTURE);

			double worldTime = pylon.getWorld() == null ? 0 : (double) (ClientTickHandler.ticksInGame + pticks);

			if(pylon != null)
				worldTime += new Random(pylon.getPos().hashCode()).nextInt(360);

			if(ConfigHandler.oldPylonModel) {
				GlStateManager.translate(d0 + 0.5, d1 + 2.2, d2 + 0.5);
				GlStateManager.scale(1F, -1.5F, -1F);
			} else {
				GlStateManager.translate(d0 + 0.2 + (green ? -0.1 : 0), d1 + 0.05, d2 + 0.8 + (green ? 0.1 : 0));
				float scale = green ? 0.8F : 0.6F;
				GlStateManager.scale(scale, 0.6F, scale);
			}

			if(!green) {
				GlStateManager.pushMatrix();
				if(!ConfigHandler.oldPylonModel)
					GlStateManager.translate(0.5F, 0F, -0.5F);
				GlStateManager.rotate((float) worldTime * 1.5F, 0F, 1F, 0F);
				if(!ConfigHandler.oldPylonModel)
					GlStateManager.translate(-0.5F, 0F, 0.5F);

				model.renderRing(variant);
				GlStateManager.translate(0D, Math.sin(worldTime / 20D) / 20 - 0.025, 0D);
				model.renderGems(variant);
				GlStateManager.popMatrix();
			}

			GlStateManager.pushMatrix();
			GlStateManager.translate(0D, Math.sin(worldTime / 20D) / 17.5, 0D);

			if(!ConfigHandler.oldPylonModel)
				GlStateManager.translate(0.5F, 0F, -0.5F);

			GlStateManager.rotate((float) -worldTime, 0F, 1F, 0F);
			if(!ConfigHandler.oldPylonModel)
				GlStateManager.translate(-0.5F, 0F, 0.5F);


			GlStateManager.disableCull();
			model.renderCrystal(variant);

			GlStateManager.color(1F, 1F, 1F, a);
			if(!ShaderHelper.useShaders()) {
				int light = 15728880;
				int lightmapX = light % 65536;
				int lightmapY = light / 65536;
				OpenGlHelper.setLightmapTextureCoords(OpenGlHelper.lightmapTexUnit, lightmapX, lightmapY);
				float alpha = (float) ((Math.sin(worldTime / 20D) / 2D + 0.5) / (ConfigHandler.oldPylonModel ? 1D : 2D));
				GlStateManager.color(1F, 1F, 1F, a * (alpha + 0.183F));
			}

			GlStateManager.disableAlpha();
			GlStateManager.scale(1.1F, 1.1F, 1.1F);
			if(!ConfigHandler.oldPylonModel)
				GlStateManager.translate(-0.05F, -0.1F, 0.05F);
			else GlStateManager.translate(0F, -0.09F, 0F);

			ShaderHelper.useShader(ShaderHelper.pylonGlow);
			model.renderCrystal(variant);
			ShaderHelper.releaseShader();

			GlStateManager.enableAlpha();
			GlStateManager.enableCull();
			GlStateManager.popMatrix();

			GlStateManager.disableBlend();
			GlStateManager.enableRescaleNormal();
			GlStateManager.popMatrix();
	}


}
