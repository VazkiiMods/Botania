/**
 * This class was created by <williewillus>. It's distributed as
 * part of the Botania Mod. Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 * <p/>
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 */
package vazkii.botania.client.integration.jei.elventrade;

import mezz.jei.api.IGuiHelper;
import mezz.jei.api.gui.IDrawable;
import mezz.jei.api.gui.IRecipeLayout;
import mezz.jei.api.ingredients.IIngredients;
import mezz.jei.api.ingredients.VanillaTypes;
import mezz.jei.api.recipe.IRecipeCategory;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.client.resources.I18n;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import org.lwjgl.opengl.GL11;
import vazkii.botania.client.core.handler.MiscellaneousIcons;
import vazkii.botania.common.lib.LibMisc;

import javax.annotation.Nonnull;
import java.util.List;

public class ElvenTradeRecipeCategory implements IRecipeCategory<ElvenTradeRecipeWrapper> {

	public static final String UID = "botania.elvenTrade";
	private final String localizedName;
	private final IDrawable background;
	private final IDrawable overlay;

	public ElvenTradeRecipeCategory(IGuiHelper guiHelper) {
		localizedName = I18n.format("botania.nei.elvenTrade");
		background = guiHelper.createBlankDrawable(145, 95);
		overlay = guiHelper.createDrawable(new ResourceLocation("botania", "textures/gui/elvenTradeOverlay.png"), 0, 15, 140, 90);
	}

	@Nonnull
	@Override
	public String getUid() {
		return UID;
	}

	@Nonnull
	@Override
	public String getTitle() {
		return localizedName;
	}

	@Nonnull
	@Override
	public IDrawable getBackground() {
		return background;
	}

	@Override
	public void drawExtras(@Nonnull Minecraft minecraft) {
		GlStateManager.enableAlpha();
		GlStateManager.enableBlend();
		overlay.draw(minecraft, 0, 4);
		GlStateManager.disableBlend();
		GlStateManager.disableAlpha();

		minecraft.renderEngine.bindTexture(TextureMap.LOCATION_BLOCKS_TEXTURE);
		TextureAtlasSprite sprite = MiscellaneousIcons.INSTANCE.alfPortalTex;
		Tessellator tess = Tessellator.getInstance();
		BufferBuilder wr = tess.getBuffer();
		wr.begin(GL11.GL_QUADS, DefaultVertexFormats.POSITION_TEX);
		int startX = 22;
		int startY = 25;
		int stopX = 70;
		int stopY = 73;
		wr.pos(startX, startY, 0).tex(sprite.getMinU(), sprite.getMinV()).endVertex();
		wr.pos(startX, stopY, 0).tex(sprite.getMinU(), sprite.getMaxV()).endVertex();
		wr.pos(stopX, stopY, 0).tex(sprite.getMaxU(), sprite.getMaxV()).endVertex();
		wr.pos(stopX, startY, 0).tex(sprite.getMaxU(), sprite.getMinV()).endVertex();
		tess.draw();
	}

	@Override
	public void setRecipe(@Nonnull IRecipeLayout recipeLayout, @Nonnull ElvenTradeRecipeWrapper recipeWrapper, @Nonnull IIngredients ingredients) {
		int index = 0, posX = 42;
		for(List<ItemStack> o : ingredients.getInputs(VanillaTypes.ITEM)) {
			recipeLayout.getItemStacks().init(index, true, posX, 0);
			recipeLayout.getItemStacks().set(index, o);
			index++;
			posX += 18;
		}

		for(int i = 0; i < ingredients.getOutputs(VanillaTypes.ITEM).size(); i++) {
			List<ItemStack> stacks = ingredients.getOutputs(VanillaTypes.ITEM).get(i);
			recipeLayout.getItemStacks().init(index + i, false, 93 + i % 2 * 20, 41 + i / 2 * 20);
			recipeLayout.getItemStacks().set(index + i, stacks);
		}
	}

	@Nonnull
	@Override
	public String getModName() {
		return LibMisc.MOD_NAME;
	}

}
