/*
 * This class is distributed as part of the Botania Mod.
 * Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 *
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 */
package vazkii.botania.client.integration.jei.elventrade;

import com.google.common.collect.ImmutableList;
import com.mojang.blaze3d.systems.RenderSystem;

import mezz.jei.api.constants.VanillaTypes;
import mezz.jei.api.gui.IRecipeLayout;
import mezz.jei.api.gui.drawable.IDrawable;
import mezz.jei.api.helpers.IGuiHelper;
import mezz.jei.api.ingredients.IIngredients;
import mezz.jei.api.recipe.category.IRecipeCategory;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.texture.AtlasTexture;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.client.resources.I18n;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.TextFormatting;

import org.lwjgl.opengl.GL11;

import vazkii.botania.client.core.handler.MiscellaneousIcons;
import vazkii.botania.common.block.ModBlocks;
import vazkii.botania.api.recipe.IElvenTradeRecipe;
import vazkii.botania.common.lib.LibMisc;

import javax.annotation.Nonnull;

import java.util.Arrays;
import java.util.List;

public class ElvenTradeRecipeCategory implements IRecipeCategory<IElvenTradeRecipe> {

	public static final ResourceLocation UID = new ResourceLocation(LibMisc.MOD_ID, "elven_trade");
	private final String localizedName;
	private final IDrawable background;
	private final IDrawable overlay;
	private final IDrawable icon;

	public ElvenTradeRecipeCategory(IGuiHelper guiHelper) {
		localizedName = I18n.format("botania.nei.elvenTrade");
		background = guiHelper.createBlankDrawable(145, 95);
		overlay = guiHelper.createDrawable(new ResourceLocation("botania", "textures/gui/elven_trade_overlay.png"), 0, 15, 140, 90);
		icon = guiHelper.createDrawableIngredient(new ItemStack(ModBlocks.alfPortal));
	}

	@Nonnull
	@Override
	public ResourceLocation getUid() {
		return UID;
	}

	@Nonnull
	@Override
	public Class<? extends IElvenTradeRecipe> getRecipeClass() {
		return IElvenTradeRecipe.class;
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

	@Nonnull
	@Override
	public IDrawable getIcon() {
		return icon;
	}

	@Override
	public void setIngredients(IElvenTradeRecipe recipe, IIngredients iIngredients) {
		ImmutableList.Builder<List<ItemStack>> builder = ImmutableList.builder();
		for (Ingredient i : recipe.getIngredients()) {
			builder.add(Arrays.asList(i.getMatchingStacks()));
		}
		iIngredients.setInputLists(VanillaTypes.ITEM, builder.build());
		iIngredients.setOutputs(VanillaTypes.ITEM, ImmutableList.copyOf(recipe.getOutputs()));
	}

	@Override
	public void draw(IElvenTradeRecipe recipe, double mouseX, double mouseY) {
		RenderSystem.enableAlphaTest();
		RenderSystem.enableBlend();
		overlay.draw(0, 4);
		RenderSystem.disableBlend();
		RenderSystem.disableAlphaTest();

		Minecraft.getInstance().textureManager.bindTexture(AtlasTexture.LOCATION_BLOCKS_TEXTURE);
		TextureAtlasSprite sprite = MiscellaneousIcons.INSTANCE.alfPortalTex;
		Tessellator tess = Tessellator.getInstance();
		BufferBuilder wr = tess.getBuffer();
		wr.begin(GL11.GL_QUADS, DefaultVertexFormats.POSITION_TEX);
		int startX = 22;
		int startY = 25;
		int stopX = 70;
		int stopY = 73;
		wr.vertex(startX, startY, 0).texture(sprite.getMinU(), sprite.getMinV()).endVertex();
		wr.vertex(startX, stopY, 0).texture(sprite.getMinU(), sprite.getMaxV()).endVertex();
		wr.vertex(stopX, stopY, 0).texture(sprite.getMaxU(), sprite.getMaxV()).endVertex();
		wr.vertex(stopX, startY, 0).texture(sprite.getMaxU(), sprite.getMinV()).endVertex();
		tess.draw();
	}

	@Override
	public void setRecipe(@Nonnull IRecipeLayout recipeLayout, @Nonnull IElvenTradeRecipe recipe, @Nonnull IIngredients ingredients) {
		int index = 0, posX = 42;
		for (List<ItemStack> o : ingredients.getInputs(VanillaTypes.ITEM)) {
			recipeLayout.getItemStacks().init(index, true, posX, 0);
			recipeLayout.getItemStacks().set(index, o);
			index++;
			posX += 18;
		}

		for (int i = 0; i < ingredients.getOutputs(VanillaTypes.ITEM).size(); i++) {
			List<ItemStack> stacks = ingredients.getOutputs(VanillaTypes.ITEM).get(i);
			recipeLayout.getItemStacks().init(index + i, false, 93 + i % 2 * 20, 41 + i / 2 * 20);
			recipeLayout.getItemStacks().set(index + i, stacks);
		}

		int endIndex = index;
		ResourceLocation recipeId = recipe.getId();
		recipeLayout.getItemStacks().addTooltipCallback((slotIndex, input, ingredient, tooltip) -> {
			if (slotIndex >= endIndex) {
				if (Minecraft.getInstance().gameSettings.advancedItemTooltips || Screen.hasShiftDown()) {
					tooltip.add(TextFormatting.DARK_GRAY + I18n.format("jei.tooltip.recipe.id", recipeId));
				}
			}
		});
	}
}
