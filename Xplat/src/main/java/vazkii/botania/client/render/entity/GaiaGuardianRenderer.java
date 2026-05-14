/*
 * This class is distributed as part of the Botania Mod.
 * Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 *
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 */
package vazkii.botania.client.render.entity;

import com.mojang.blaze3d.vertex.PoseStack;

import net.minecraft.client.Minecraft;
import net.minecraft.client.model.geom.ModelLayers;
import net.minecraft.client.player.AbstractClientPlayer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.ShaderInstance;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.HumanoidMobRenderer;
import net.minecraft.client.resources.DefaultPlayerSkin;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.PlayerModelPart;

import org.jetbrains.annotations.Nullable;

import vazkii.botania.client.core.helper.CoreShaders;
import vazkii.botania.client.model.GaiaGuardianModel;
import vazkii.botania.client.model.armor.ArmorModels;
import vazkii.botania.common.entity.GaiaGuardianEntity;

public class GaiaGuardianRenderer extends HumanoidMobRenderer<GaiaGuardianEntity, GaiaGuardianModel> {

	public static final float DEFAULT_GRAIN_INTENSITY = 0.05F;
	public static final float DEFAULT_DISFIGURATION = 0.025F;

	private final GaiaGuardianModel normalModel;
	private final GaiaGuardianModel slimModel;

	public GaiaGuardianRenderer(EntityRendererProvider.Context ctx) {
		super(ctx, new GaiaGuardianModel(ctx.bakeLayer(ModelLayers.PLAYER), false), 0F);
		this.normalModel = this.getModel();
		this.slimModel = new GaiaGuardianModel(ctx.bakeLayer(ModelLayers.PLAYER_SLIM), true);
		// Call this here bc no other place with access to Context
		ArmorModels.init(ctx);
	}

	@Override
	public void render(GaiaGuardianEntity dopple, float yaw, float partialTicks, PoseStack ms, MultiBufferSource buffers, int light) {
		int invulTime = dopple.getInvulTime();
		ShaderInstance shader = CoreShaders.doppleganger();
		if (shader != null) {
			float grainIntensity, disfiguration;
			if (invulTime > 0) {
				grainIntensity = invulTime > 20 ? 1F : invulTime * 0.05F;
				disfiguration = grainIntensity * 0.3F;
			} else {
				disfiguration = (0.025F + dopple.hurtTime * ((1F - 0.15F) / 20F)) / 2F;
				grainIntensity = 0.05F + dopple.hurtTime * ((1F - 0.15F) / 10F);
			}
			shader.safeGetUniform("BotaniaGrainIntensity").set(grainIntensity);
			shader.safeGetUniform("BotaniaDisfiguration").set(disfiguration);
		}

		Minecraft minecraft = Minecraft.getInstance();
		AbstractClientPlayer player = minecraft.getCameraEntity() instanceof AbstractClientPlayer clientPlayer
				? clientPlayer
				: minecraft.player;
		if (player != null && DefaultPlayerSkin.get(player.getUUID()).model().id().equals("slim")) {
			this.model = slimModel;
		} else {
			this.model = normalModel;
		}
		this.setModelProperties(player);

		super.render(dopple, yaw, partialTicks, ms, buffers, light);
	}

	private void setModelProperties(@Nullable AbstractClientPlayer clientPlayer) {
		GaiaGuardianModel gaiaGuardianModel = this.getModel();
		gaiaGuardianModel.setAllVisible(true);
		if (clientPlayer == null) {
			gaiaGuardianModel.hat.visible = false;
			gaiaGuardianModel.jacket.visible = false;
			gaiaGuardianModel.leftPants.visible = false;
			gaiaGuardianModel.rightPants.visible = false;
			gaiaGuardianModel.leftSleeve.visible = false;
			gaiaGuardianModel.rightSleeve.visible = false;
		} else {
			gaiaGuardianModel.hat.visible = clientPlayer.isModelPartShown(PlayerModelPart.HAT);
			gaiaGuardianModel.jacket.visible = clientPlayer.isModelPartShown(PlayerModelPart.JACKET);
			gaiaGuardianModel.leftPants.visible = clientPlayer.isModelPartShown(PlayerModelPart.LEFT_PANTS_LEG);
			gaiaGuardianModel.rightPants.visible = clientPlayer.isModelPartShown(PlayerModelPart.RIGHT_PANTS_LEG);
			gaiaGuardianModel.leftSleeve.visible = clientPlayer.isModelPartShown(PlayerModelPart.LEFT_SLEEVE);
			gaiaGuardianModel.rightSleeve.visible = clientPlayer.isModelPartShown(PlayerModelPart.RIGHT_SLEEVE);
		}
	}

	@Override
	public ResourceLocation getTextureLocation(GaiaGuardianEntity entity) {
		Minecraft mc = Minecraft.getInstance();

		if (!(mc.getCameraEntity() instanceof AbstractClientPlayer clientPlayer)) {
			return DefaultPlayerSkin.get(entity.getUUID()).texture();
		}

		return clientPlayer.getSkin().texture();
	}

	@Override
	protected boolean isBodyVisible(GaiaGuardianEntity dopple) {
		return true;
	}

}
