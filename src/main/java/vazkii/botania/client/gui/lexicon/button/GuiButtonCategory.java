/**
 * This class was created by <Vazkii>. It's distributed as
 * part of the Botania Mod. Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 *
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 *
 * File Created @ [Oct 18, 2014, 4:00:30 PM (GMT)]
 */
package vazkii.botania.client.gui.lexicon.button;

import net.minecraft.client.Minecraft;
import com.mojang.blaze3d.platform.GlStateManager;
import com.mojang.blaze3d.platform.GLX;
import net.minecraft.client.renderer.texture.TextureManager;
import net.minecraft.client.resources.I18n;
import net.minecraft.util.ResourceLocation;
import org.lwjgl.opengl.ARBMultitexture;
import org.lwjgl.opengl.ARBShaderObjects;
import org.lwjgl.opengl.GL11;
import vazkii.botania.api.internal.ShaderCallback;
import vazkii.botania.api.lexicon.LexiconCategory;
import vazkii.botania.client.core.handler.ClientTickHandler;
import vazkii.botania.client.core.helper.RenderHelper;
import vazkii.botania.client.core.helper.ShaderHelper;
import vazkii.botania.client.gui.lexicon.GuiLexicon;
import vazkii.botania.client.gui.lexicon.GuiLexiconIndex;
import vazkii.botania.client.lib.LibResources;
import vazkii.botania.common.core.handler.ConfigHandler;

import javax.annotation.Nonnull;

public class GuiButtonCategory extends GuiButtonLexicon {

	private static final ResourceLocation fallbackResource = new ResourceLocation(LibResources.CATEGORY_INDEX);
	private static final ResourceLocation stencilResource = new ResourceLocation(LibResources.GUI_STENCIL);

	private static boolean boundStencil = false;

	final GuiLexicon gui;
	private final LexiconCategory category;
	private ResourceLocation resource = null;
	private float ticksHovered = 0F;
	private final float time = 12F;

	private final ShaderCallback shaderCallback = shader -> {
		TextureManager r = Minecraft.getInstance().textureManager;
		int heightMatchUniform = ARBShaderObjects.glGetUniformLocationARB(shader, "heightMatch");
		int imageUniform = ARBShaderObjects.glGetUniformLocationARB(shader, "image");
		int maskUniform = ARBShaderObjects.glGetUniformLocationARB(shader, "mask");

		float heightMatch = ticksHovered / time;
		GLX.glActiveTexture(ARBMultitexture.GL_TEXTURE0_ARB);
		GL11.glBindTexture(GL11.GL_TEXTURE_2D, r.getTexture(resource).getGlTextureId());
		ARBShaderObjects.glUniform1iARB(imageUniform, 0);

		GLX.glActiveTexture(ARBMultitexture.GL_TEXTURE0_ARB + ConfigHandler.CLIENT.glSecondaryTextureUnit.get());

		GlStateManager.enableTexture();
		GL11.glGetInteger(GL11.GL_TEXTURE_BINDING_2D);

		GL11.glBindTexture(GL11.GL_TEXTURE_2D, r.getTexture(stencilResource).getGlTextureId());
		ARBShaderObjects.glUniform1iARB(maskUniform, ConfigHandler.CLIENT.glSecondaryTextureUnit.get());

		ARBShaderObjects.glUniform1fARB(heightMatchUniform, heightMatch);
	};

	public GuiButtonCategory(int x, int y, GuiLexicon gui, LexiconCategory category) {
		super(x, y, 16, 16, "", b -> {
			Minecraft.getInstance().displayGuiScreen(new GuiLexiconIndex(category));
			ClientTickHandler.notifyPageChange();
		});
		this.gui = gui;
		this.category = category;
	}

	@Override
	public void onClick(double mouseX, double mouseY) {
		super.onClick(mouseX, mouseY);
	}

	@Override
	public void render(int mx, int my, float partialTicks) {
		boolean inside = isHovered();
		if(inside)
			ticksHovered = Math.min(time, ticksHovered + gui.timeDelta);
		else ticksHovered = Math.max(0F, ticksHovered - gui.timeDelta);

		if(resource == null) {
			if(category == null)
				resource = fallbackResource;
			else resource = category.getIcon();
			if(resource == null)
				resource = fallbackResource;
		}

		float s = 1F / 32F;

		GlStateManager.pushMatrix();
		GlStateManager.enableBlend();
		GlStateManager.blendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
		GlStateManager.scalef(0.5F, 0.5F, 0.5F);
		GlStateManager.color4f(1F, 1F, 1F, 1F);

		if(!boundStencil) { // Allow for the texture manager to take care of the ResourceLocation before we use it directly with gl
			Minecraft.getInstance().textureManager.bindTexture(stencilResource);
			boundStencil = true;
		}
		Minecraft.getInstance().textureManager.bindTexture(resource);

		int texture = 0;
		boolean shaders = ShaderHelper.useShaders();

		if(shaders) {
			GLX.glActiveTexture(ARBMultitexture.GL_TEXTURE0_ARB + ConfigHandler.CLIENT.glSecondaryTextureUnit.get());
			texture = GL11.glGetInteger(GL11.GL_TEXTURE_BINDING_2D);
		}

		ShaderHelper.useShader(ShaderHelper.categoryButton, shaderCallback);
		RenderHelper.drawTexturedModalRect(x * 2, y * 2, zLevel * 2, 0, 0, 32, 32, s, s);
		ShaderHelper.releaseShader();

		if(shaders) {
			GLX.glActiveTexture(ARBMultitexture.GL_TEXTURE0_ARB + ConfigHandler.CLIENT.glSecondaryTextureUnit.get());
			GL11.glBindTexture(GL11.GL_TEXTURE_2D, texture);
			GLX.glActiveTexture(ARBMultitexture.GL_TEXTURE0_ARB);
		}

		GlStateManager.popMatrix();

		if(inside)
			gui.categoryHighlight = I18n.format(getTooltipText());
	}

	private String getTooltipText() {
		if(category == null)
			return "botaniamisc.lexiconIndex";
		return category.getUnlocalizedName();
	}

	public LexiconCategory getCategory() {
		return category;
	}

}
