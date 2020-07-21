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
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.render.BufferBuilder;
import net.minecraft.client.render.Tessellator;
import net.minecraft.client.render.VertexFormats;
import net.minecraft.client.resource.language.I18n;
import net.minecraft.client.texture.Sprite;
import net.minecraft.client.texture.SpriteAtlasTexture;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.item.ItemStack;
import net.minecraft.recipe.Ingredient;
import net.minecraft.text.TranslatableText;
import net.minecraft.util.Formatting;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.Matrix4f;
import org.lwjgl.opengl.GL11;

import vazkii.botania.api.recipe.IElvenTradeRecipe;
import vazkii.botania.client.core.handler.MiscellaneousIcons;
import vazkii.botania.common.block.ModBlocks;
import vazkii.botania.common.lib.LibMisc;

import javax.annotation.Nonnull;

import java.util.Arrays;
import java.util.List;

import static vazkii.botania.common.lib.ResourceLocationHelper.prefix;

public class ElvenTradeRecipeCategory implements IRecipeCategory<IElvenTradeRecipe> {

	public static final Identifier UID = prefix("elven_trade");
	private final String localizedName;
	private final IDrawable background;
	private final IDrawable overlay;
	private final IDrawable icon;

	public ElvenTradeRecipeCategory(IGuiHelper guiHelper) {
		localizedName = I18n.translate("botania.nei.elvenTrade");
		background = guiHelper.createBlankDrawable(145, 95);
		overlay = guiHelper.createDrawable(prefix("textures/gui/elven_trade_overlay.png"), 0, 15, 140, 90);
		icon = guiHelper.createDrawableIngredient(new ItemStack(ModBlocks.alfPortal));
	}

	@Nonnull
	@Override
	public Identifier getUid() {
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
		for (Ingredient i : recipe.getPreviewInputs()) {
			builder.add(Arrays.asList(i.getMatchingStacksClient()));
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

		MinecraftClient.getInstance().getTextureManager().bindTexture(SpriteAtlasTexture.BLOCK_ATLAS_TEX);
		Sprite sprite = MiscellaneousIcons.INSTANCE.alfPortalTex;
		Tessellator tess = Tessellator.getInstance();
		BufferBuilder wr = tess.getBuffer();
		wr.begin(GL11.GL_QUADS, VertexFormats.POSITION_TEXTURE);
		int startX = 22;
		int startY = 25;
		int stopX = 70;
		int stopY = 73;
		Matrix4f mat = ms.peek().getModel();
		wr.vertex(mat, startX, startY, 0).texture(sprite.getMinU(), sprite.getMinV()).next();
		wr.vertex(mat, startX, stopY, 0).texture(sprite.getMinU(), sprite.getMaxV()).next();
		wr.vertex(mat, stopX, stopY, 0).texture(sprite.getMaxU(), sprite.getMaxV()).next();
		wr.vertex(mat, stopX, startY, 0).texture(sprite.getMaxU(), sprite.getMinV()).next();
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
		Identifier recipeId = recipe.getId();
		recipeLayout.getItemStacks().addTooltipCallback((slotIndex, input, ingredient, tooltip) -> {
			if (slotIndex >= endIndex) {
				if (MinecraftClient.getInstance().options.advancedItemTooltips || Screen.hasShiftDown()) {
					tooltip.add(new TranslatableText("jei.tooltip.recipe.id", recipeId).formatted(Formatting.DARK_GRAY));
				}
			}
		});
	}
}
