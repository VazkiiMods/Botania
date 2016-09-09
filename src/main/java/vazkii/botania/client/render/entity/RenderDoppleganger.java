/**
 * This class was created by <Vazkii>. It's distributed as
 * part of the Botania Mod. Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 * 
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 * 
 * File Created @ [Jul 12, 2014, 4:07:26 PM (GMT)]
 */
package vazkii.botania.client.render.entity;

import com.mojang.authlib.minecraft.MinecraftProfileTexture;
import net.minecraft.client.Minecraft;
import net.minecraft.client.model.ModelPlayer;
import net.minecraft.client.renderer.entity.RenderBiped;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.client.resources.DefaultPlayerSkin;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.ResourceLocation;
import org.lwjgl.opengl.ARBShaderObjects;
import vazkii.botania.api.internal.ShaderCallback;
import vazkii.botania.client.core.helper.ShaderHelper;
import vazkii.botania.common.entity.EntityDoppleganger;

import javax.annotation.Nonnull;
import java.util.Map;
import java.util.UUID;

public class RenderDoppleganger extends RenderBiped<EntityDoppleganger> {

	public static final float DEFAULT_GRAIN_INTENSITY = 0.05F;
	public static final float DEFAULT_DISFIGURATION = 0.025F;

	public static float grainIntensity = DEFAULT_GRAIN_INTENSITY;
	public static float disfiguration = DEFAULT_DISFIGURATION;

	public static final ShaderCallback callback = shader -> {
        // Frag Uniforms
        int disfigurationUniform = ARBShaderObjects.glGetUniformLocationARB(shader, "disfiguration");
        ARBShaderObjects.glUniform1fARB(disfigurationUniform, disfiguration);

        // Vert Uniforms
        int grainIntensityUniform = ARBShaderObjects.glGetUniformLocationARB(shader, "grainIntensity");
        ARBShaderObjects.glUniform1fARB(grainIntensityUniform, grainIntensity);
    };

	public static final ShaderCallback defaultCallback = shader -> {
        // Frag Uniforms
        int disfigurationUniform = ARBShaderObjects.glGetUniformLocationARB(shader, "disfiguration");
        ARBShaderObjects.glUniform1fARB(disfigurationUniform, DEFAULT_DISFIGURATION);

        // Vert Uniforms
        int grainIntensityUniform = ARBShaderObjects.glGetUniformLocationARB(shader, "grainIntensity");
        ARBShaderObjects.glUniform1fARB(grainIntensityUniform, DEFAULT_GRAIN_INTENSITY);
    };

	public RenderDoppleganger(RenderManager renderManager) {
		super(renderManager, new ModelPlayer(0.0F, false), 0F);
	}

	@Override
	public void doRender(@Nonnull EntityDoppleganger dopple, double par2, double par4, double par6, float par8, float par9) {
		int invulTime = dopple.getInvulTime();
		if(invulTime > 0) {
			grainIntensity = invulTime > 20 ? 1F : invulTime * 0.05F;
			disfiguration = grainIntensity * 0.3F;
		} else {
			disfiguration = (0.025F + dopple.hurtTime * ((1F - 0.15F) / 20F)) / 2F;
			grainIntensity = 0.05F + dopple.hurtTime * ((1F - 0.15F) / 10F);
		}

		ShaderHelper.useShader(ShaderHelper.doppleganger, callback);
		super.doRender(dopple, par2, par4, par6, par8, par9);
		ShaderHelper.releaseShader();
	}

	@Nonnull
	@Override
	protected ResourceLocation getEntityTexture(@Nonnull EntityDoppleganger par1Entity) {
		Minecraft mc = Minecraft.getMinecraft();

		if(!(mc.getRenderViewEntity() instanceof EntityPlayer))
			return DefaultPlayerSkin.getDefaultSkinLegacy();

		EntityPlayer view = (EntityPlayer) mc.getRenderViewEntity();

		// Adapated from TileEntitySkullRenderer.renderTileEntityAt
		Map<MinecraftProfileTexture.Type, MinecraftProfileTexture> map = mc.getSkinManager().loadSkinFromCache(view.getGameProfile());

		if (map.containsKey(MinecraftProfileTexture.Type.SKIN))
		{
			return mc.getSkinManager().loadSkin(map.get(MinecraftProfileTexture.Type.SKIN), MinecraftProfileTexture.Type.SKIN);
		}
		else
		{
			UUID uuid = EntityPlayer.getUUID(view.getGameProfile());
			return DefaultPlayerSkin.getDefaultSkin(uuid);
		}
	}

}
