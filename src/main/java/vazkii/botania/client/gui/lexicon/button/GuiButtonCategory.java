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

import java.util.Arrays;

import net.minecraft.client.Minecraft;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.StatCollector;

import org.lwjgl.opengl.GL11;

import vazkii.botania.api.lexicon.LexiconCategory;
import vazkii.botania.client.core.helper.RenderHelper;
import vazkii.botania.client.gui.lexicon.GuiLexicon;
import vazkii.botania.client.lib.LibResources;

public class GuiButtonCategory extends GuiButtonLexicon {

	private static final ResourceLocation fallbackResource = new ResourceLocation(LibResources.CATEGORY_INDEX);

	GuiLexicon gui;
	LexiconCategory category;
	float ticksHovered = 0F;

	public GuiButtonCategory(int id, int x, int y, GuiLexicon gui, LexiconCategory category) {
		super(id, x, y, 24, 24, "");
		this.gui = gui;
		this.category = category;
	}

	@Override
	public void drawButton(Minecraft mc, int mx, int my) {
		boolean inside = mx >= xPosition && my >= yPosition && mx < xPosition + width && my < yPosition + height;
		float time = 5F;
		if(inside)
			ticksHovered = Math.min(time, ticksHovered + gui.timeDelta);
		else ticksHovered = Math.max(0F, ticksHovered - gui.timeDelta);

		ResourceLocation resource;
		if(category == null)
			resource = fallbackResource;
		else resource = category.getIcon();
		if(resource == null)
			resource = fallbackResource;

		mc.renderEngine.bindTexture(resource);
		float s = 1F / 48F;
		float defAlpha = 0.3F;
		float alpha = ticksHovered / time * (1F - defAlpha) + defAlpha;

		GL11.glPushMatrix();
		GL11.glColor4f(1F, 1F, 1F, alpha);
		GL11.glEnable(GL11.GL_BLEND);
		GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
		GL11.glScalef(0.5F, 0.5F, 0.5F);
		RenderHelper.drawTexturedModalRect(xPosition * 2, yPosition * 2, zLevel * 2, 0, 0, 48, 48, s, s);
		GL11.glPopMatrix();

		if(inside)
			RenderHelper.renderTooltipGreen(mx, my, Arrays.asList(StatCollector.translateToLocal(getTooltipText())));
	}

	String getTooltipText() {
		if(category == null)
			return "botaniamisc.lexiconIndex";
		return category.getUnlocalizedName();
	}

	public LexiconCategory getCategory() {
		return category;
	}

}
