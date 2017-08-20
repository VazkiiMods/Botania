/**
 * This class was created by <Vazkii>. It's distributed as
 * part of the Botania Mod. Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 * <p>
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 * <p>
 * File Created @ [Feb 18, 2014, 10:18:36 PM (GMT)]
 */
package vazkii.botania.client.render.tile;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.OpenGlHelper;
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
import vazkii.botania.client.model.ModelPylonGaia;
import vazkii.botania.client.model.ModelPylonMana;
import vazkii.botania.client.model.ModelPylonNatura;
import vazkii.botania.common.block.ModBlocks;
import vazkii.botania.common.block.tile.TilePylon;

import javax.annotation.Nonnull;
import java.util.Random;

public class RenderTilePylon extends TileEntitySpecialRenderer<TilePylon> {

	private static final ResourceLocation MANA_TEXTURE = new ResourceLocation(LibResources.MODEL_PYLON_MANA);
	private static final ResourceLocation NATURA_TEXTURE = new ResourceLocation(LibResources.MODEL_PYLON_NATURA);
	private static final ResourceLocation GAIA_TEXTURE = new ResourceLocation(LibResources.MODEL_PYLON_GAIA);

	private final ModelPylonMana manaModel = new ModelPylonMana();
	private final ModelPylonNatura naturaModel = new ModelPylonNatura();
	private final ModelPylonGaia gaiaModel = new ModelPylonGaia();

	@Override
	public void render(@Nonnull TilePylon pylon, double d0, double d1, double d2, float pticks, int digProgress, float unused) {
		if(!pylon.getWorld().isBlockLoaded(pylon.getPos(), false)
				|| pylon.getWorld().getBlockState(pylon.getPos()).getBlock() != ModBlocks.pylon)
			return;

		PylonVariant type = ModBlocks.pylon.getStateFromMeta(pylon.getBlockMetadata()).getValue(BotaniaStateProps.PYLON_VARIANT);
		IPylonModel model;
		switch(type) {
			default:
			case MANA: {
				model = manaModel;
				Minecraft.getMinecraft().renderEngine.bindTexture(MANA_TEXTURE);
				break;
			}
			case NATURA: {
				model = naturaModel;
				Minecraft.getMinecraft().renderEngine.bindTexture(NATURA_TEXTURE);
				break;
			}
			case GAIA: {
				model = gaiaModel;
				Minecraft.getMinecraft().renderEngine.bindTexture(GAIA_TEXTURE);
				break;
			}
		}

		GlStateManager.pushMatrix();
		GlStateManager.enableRescaleNormal();
		GlStateManager.enableBlend();
		GlStateManager.blendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
		float a = MultiblockRenderHandler.rendering ? 0.6F : 1F;
		GlStateManager.color(1F, 1F, 1F, a);

		double worldTime = (double) (ClientTickHandler.ticksInGame + pticks);

		worldTime += new Random(pylon.getPos().hashCode()).nextInt(360);

		GlStateManager.translate(d0 + 0.2 + (type == PylonVariant.NATURA ? -0.1 : 0), d1 + 0.05, d2 + 0.8 + (type == PylonVariant.NATURA ? 0.1 : 0));
		float scale = type == PylonVariant.NATURA ? 0.8F : 0.6F;
		GlStateManager.scale(scale, 0.6F, scale);

		if(type != PylonVariant.NATURA) {
			GlStateManager.pushMatrix();
			GlStateManager.translate(0.5F, 0F, -0.5F);
			GlStateManager.rotate((float) worldTime * 1.5F, 0F, 1F, 0F);
			//GlStateManager.translate(-0.5F, 0F, 0.5F);

			model.renderRing();
			GlStateManager.translate(0D, Math.sin(worldTime / 20D) / 20 - 0.025, 0D);
			GlStateManager.popMatrix();
		}

		GlStateManager.pushMatrix();
		GlStateManager.translate(0D, Math.sin(worldTime / 20D) / 17.5, 0D);

		GlStateManager.translate(0.5F, 0F, -0.5F);
		GlStateManager.rotate((float) -worldTime, 0F, 1F, 0F);
		//GlStateManager.translate(-0.5F, 0F, 0.5F);


		GlStateManager.disableCull();
		model.renderCrystal();

		GlStateManager.color(1F, 1F, 1F, a);
		if(!ShaderHelper.useShaders()) {
			int light = 15728880;
			int lightmapX = light % 65536;
			int lightmapY = light / 65536;
			OpenGlHelper.setLightmapTextureCoords(OpenGlHelper.lightmapTexUnit, lightmapX, lightmapY);
			float alpha = (float) ((Math.sin(worldTime / 20D) / 2D + 0.5) / 2D);
			GlStateManager.color(1F, 1F, 1F, a * (alpha + 0.183F));
		}

		GlStateManager.disableAlpha();
		GlStateManager.scale(1.1F, 1.1F, 1.1F);
		GlStateManager.translate(-0.05F, -0.1F, 0.05F);

		ShaderHelper.useShader(ShaderHelper.pylonGlow);
		model.renderCrystal();
		ShaderHelper.releaseShader();

		GlStateManager.enableAlpha();
		GlStateManager.enableCull();
		GlStateManager.popMatrix();

		GlStateManager.disableBlend();
		GlStateManager.enableRescaleNormal();
		GlStateManager.popMatrix();
	}
}
