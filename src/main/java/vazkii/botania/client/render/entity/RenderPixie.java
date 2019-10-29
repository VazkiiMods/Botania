/**
 * This class was created by <Adubbz>. It's distributed as
 * part of the Botania Mod. Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 *
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 *
 * File Created @ [? (GMT)]
 */
package vazkii.botania.client.render.entity;

import com.mojang.blaze3d.platform.GlStateManager;
import com.mojang.blaze3d.platform.GLX;
import net.minecraft.client.renderer.entity.EntityRendererManager;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.client.renderer.entity.EntityRendererManager;
import net.minecraft.util.ResourceLocation;
import org.lwjgl.opengl.GL11;
import vazkii.botania.api.internal.ShaderCallback;
import vazkii.botania.client.core.helper.ShaderHelper;
import vazkii.botania.client.lib.LibResources;
import vazkii.botania.client.model.ModelPixie;
import vazkii.botania.common.entity.EntityPixie;

import javax.annotation.Nonnull;

public class RenderPixie extends MobRenderer<EntityPixie, ModelPixie> {

	private final ShaderCallback callback = shader -> {
		// Frag Uniforms
		int disfigurationUniform = GLX.glGetUniformLocation(shader, "disfiguration");
		ShaderHelper.FLOAT_BUF.position(0);
		ShaderHelper.FLOAT_BUF.put(0, 0.025F);
		GLX.glUniform1(disfigurationUniform, ShaderHelper.FLOAT_BUF);

		// Vert Uniforms
		int grainIntensityUniform = GLX.glGetUniformLocation(shader, "grainIntensity");
		ShaderHelper.FLOAT_BUF.position(0);
		ShaderHelper.FLOAT_BUF.put(0, 0.05F);
		GLX.glUniform1(grainIntensityUniform, ShaderHelper.FLOAT_BUF);
	};

	public RenderPixie(EntityRendererManager renderManager) {
		super(renderManager, new ModelPixie(), 0.25F);
		//setRenderPassModel(new ModelPixie());
		shadowSize = 0.0F;
	}

	@Nonnull
	@Override
	protected ResourceLocation getEntityTexture(@Nonnull EntityPixie entity) {
		return new ResourceLocation(LibResources.MODEL_PIXIE);
	}

	@Override
	public void doRender(@Nonnull EntityPixie pixie, double par2, double par4, double par6, float par8, float par9) {
		if(pixie.getPixieType() == 1)
			ShaderHelper.useShader(ShaderHelper.doppleganger, callback);
		super.doRender(pixie, par2, par4, par6, par8, par9);
		if(pixie.getPixieType() == 1)
			ShaderHelper.releaseShader();
	}

	protected int setPixieBrightness(EntityPixie par1EntityPixie, int par2, float par3) {
		if (par2 != 0)
			return -1;
		else {
			bindTexture(getEntityTexture(par1EntityPixie));
			float f1 = 1.0F;
			GlStateManager.enableBlend();
			GlStateManager.disableAlphaTest();
			GlStateManager.blendFunc(GL11.GL_ONE, GL11.GL_ONE);

			if (par1EntityPixie.isInvisible())
				GlStateManager.depthMask(false);
			else
				GlStateManager.depthMask(true);

			char c0 = 61680;
			int j = c0 % 65536;
			int k = c0 / 65536;
			GLX.glMultiTexCoord2f(GLX.GL_TEXTURE1, j / 1.0F, k / 1.0F);
			GlStateManager.color4f(1.0F, 1.0F, 1.0F, 1.0F);
			GlStateManager.color4f(1.0F, 1.0F, 1.0F, f1);
			return 1;
		}
	}

	/*@Override
	protected int shouldRenderPass(LivingEntity par1EntityLivingBase, int par2, float par3) {
		return setPixieBrightness((EntityPixie)par1EntityLivingBase, par2, par3);
	}*/
}