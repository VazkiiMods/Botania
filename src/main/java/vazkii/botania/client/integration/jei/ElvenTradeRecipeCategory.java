/*
 * This class is distributed as part of the Botania Mod.
 * Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 *
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 */
package vazkii.botania.client.integration.jei;

import com.google.common.collect.ImmutableList;
import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.systems.RenderSystem;

import mezz.jei.api.constants.VanillaTypes;
import mezz.jei.api.gui.IRecipeLayout;
import mezz.jei.api.gui.drawable.IDrawable;
import mezz.jei.api.helpers.IGuiHelper;
import mezz.jei.api.ingredients.IIngredients;
import mezz.jei.api.recipe.category.IRecipeCategory;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.texture.AtlasTexture;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.client.resources.I18n;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.vector.Matrix4f;

import org.lwjgl.opengl.GL11;

import vazkii.botania.api.recipe.IElvenTradeRecipe;
import vazkii.botania.client.core.handler.MiscellaneousIcons;
import vazkii.botania.common.block.ModBlocks;

import javax.annotation.Nonnull;

import java.util.Arrays;
import java.util.List;

import static vazkii.botania.common.lib.ResourceLocationHelper.prefix;

public class ElvenTradeRecipeCategory implements IRecipeCategory<IElvenTradeRecipe> {

	public static final ResourceLocation UID = prefix("elven_trade");
	private final String localizedName;
	private final IDrawable background;
	private final IDrawable overlay;
	private final IDrawable icon;

	public ElvenTradeRecipeCategory(IGuiHelper guiHelper) {
		localizedName = I18n.format("botania.nei.elvenTrade");
		background = guiHelper.createBlankDrawable(145, 95);
		overlay = guiHelper.createDrawable(prefix("textures/gui/elven_trade_overlay.png"), 0, 15, 140, 90);
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
	public void draw(IElvenTradeRecipe recipe, MatrixStack ms, double mouseX, double mouseY) {
		RenderSystem.enableAlphaTest();
		RenderSystem.enableBlend();
		overlay.draw(ms, 0, 4);
		RenderSystem.disableBlend();
		RenderSystem.disableAlphaTest();

		Minecraft.getInstance().textureManager.bindTexture(AtlasTexture.LOCATION_BLOCKS_TEXTURE);
		TextureAtlasSprite sprite = MiscellaneousIcons.INSTANCE.alfPortalTex.getSprite();
		Tessellator tess = Tessellator.getInstance();
		BufferBuilder wr = tess.getBuffer();
		wr.begin(GL11.GL_QUADS, DefaultVertexFormats.POSITION_TEX);
		int startX = 22;
		int startY = 25;
		int stopX = 70;
		int stopY = 73;
		Matrix4f mat = ms.getLast().getMatrix();
		wr.pos(mat, startX, startY, 0).tex(sprite.getMinU(), sprite.getMinV()).endVertex();
		wr.pos(mat, startX, stopY, 0).tex(sprite.getMinU(), sprite.getMaxV()).endVertex();
		wr.pos(mat, stopX, stopY, 0).tex(sprite.getMaxU(), sprite.getMaxV()).endVertex();
		wr.pos(mat, stopX, startY, 0).tex(sprite.getMaxU(), sprite.getMinV()).endVertex();
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
	}
}
