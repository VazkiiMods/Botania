/**
 * This class was created by <Vazkii>. It's distributed as
 * part of the Botania Mod. Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 *
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 *
 * File Created @ [Jan 19, 2014, 4:58:19 PM (GMT)]
 */
package vazkii.botania.common.lexicon.page;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screen.Screen;
import com.mojang.blaze3d.platform.GlStateManager;
import net.minecraft.client.renderer.texture.TextureManager;
import net.minecraft.client.resources.I18n;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.item.crafting.ShapedRecipe;
import net.minecraft.item.crafting.ShapelessRecipe;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import org.lwjgl.opengl.GL11;
import vazkii.botania.api.internal.IGuiLexiconEntry;
import vazkii.botania.api.lexicon.LexiconEntry;
import vazkii.botania.api.lexicon.LexiconRecipeMappings;
import vazkii.botania.client.core.helper.RenderHelper;
import vazkii.botania.client.lib.LibResources;

import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import javax.annotation.Resource;

public class PageCraftingRecipe extends PageRecipe {

	private static final ResourceLocation craftingOverlay = new ResourceLocation(LibResources.GUI_CRAFTING_OVERLAY);

	final List<ResourceLocation> recipes;
	int ticksElapsed = 0;
	int recipeAt = 0;

	boolean shapelessRecipe;

	public PageCraftingRecipe(String unlocalizedName, List<ResourceLocation> recipes) {
		super(unlocalizedName);
		this.recipes = recipes;

		for(ResourceLocation res : recipes)
			if(res == null)
				throw new IllegalArgumentException("Null recipes arent allowed");
	}

	public PageCraftingRecipe(String unlocalizedName, ResourceLocation recipe) {
		this(unlocalizedName, Collections.singletonList(recipe));
	}

	@Override
	public void onPageAdded(LexiconEntry entry, int index) {
		/* todo 1.13
		for(ResourceLocation name : recipes) {
			IRecipe recipe = ForgeRegistries.RECIPES.getValue(name);
			if(recipe != null && !recipe.getRecipeOutput().isEmpty()) {
				LexiconRecipeMappings.map(recipe.getRecipeOutput(), entry, index);
			}
		}
		*/
	}

	@Override
	@OnlyIn(Dist.CLIENT)
	public void renderRecipe(IGuiLexiconEntry gui, int mx, int my) {
		shapelessRecipe = false;

		IRecipe recipe = Minecraft.getInstance().world.getRecipeManager().getRecipe(recipes.get(recipeAt));
		renderCraftingRecipe(gui, recipe);

		TextureManager render = Minecraft.getInstance().textureManager;
		render.bindTexture(craftingOverlay);

		GlStateManager.enableBlend();
		GlStateManager.blendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
		GlStateManager.color4f(1F, 1F, 1F, 1F);
		((Screen) gui).blit(gui.getLeft(), gui.getTop(), 0, 0, gui.getWidth(), gui.getHeight());

		int iconX = gui.getLeft() + 115;
		int iconY = gui.getTop() + 12;

		GlStateManager.enableBlend();
		GlStateManager.blendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);

		if(shapelessRecipe) {
			((Screen) gui).blit(iconX, iconY, 240, 0, 16, 16);

			if(mx >= iconX && my >= iconY && mx < iconX + 16 && my < iconY + 16)
				RenderHelper.renderTooltip(mx, my, Collections.singletonList(I18n.format("botaniamisc.shapeless")));

			iconY += 20;
		}

		render.bindTexture(craftingOverlay);
	}

	@Override
	@OnlyIn(Dist.CLIENT)
	public void updateScreen() {
		if(Screen.hasShiftDown())
			return;

		if(ticksElapsed % 20 == 0) {
			recipeAt++;

			if(recipeAt == recipes.size())
				recipeAt = 0;
		}
		++ticksElapsed;
	}

	@OnlyIn(Dist.CLIENT)
	public void renderCraftingRecipe(IGuiLexiconEntry gui, IRecipe recipe) {
		if(recipe == null)
			return;

		if(recipe instanceof ShapedRecipe) {

			int width = ((ShapedRecipe) recipe).getWidth();
			int height = ((ShapedRecipe) recipe).getHeight();

			for(int y = 0; y < height; y++)
				for(int x = 0; x < width; x++) {
					Ingredient input = (Ingredient) recipe.getIngredients().get(y * width + x);
					ItemStack[] stacks = input.getMatchingStacks();
					if(stacks.length > 0) {
						renderItemAtGridPos(gui, 1 + x, 1 + y, stacks[(ticksElapsed / 40) % stacks.length], true);
					}
				}
		} else if(recipe instanceof ShapelessRecipe) {
			shapelessRecipe = true;

			drawGrid : {
				for(int y = 0; y < 3; y++)
					for(int x = 0; x < 3; x++) {
						int index = y * 3 + x;

						if(index >= recipe.getIngredients().size())
							break drawGrid;

						Ingredient input = (Ingredient) recipe.getIngredients().get(index);
						if(input != Ingredient.EMPTY) {
							ItemStack[] stacks = input.getMatchingStacks(); 
							renderItemAtGridPos(gui, 1 + x, 1 + y, stacks[(ticksElapsed / 40) % stacks.length], true);
						}
					}
			}
		}

		renderItemAtGridPos(gui, 2, 0, recipe.getRecipeOutput(), false);
	}

	@Override
	public List<ItemStack> getDisplayedRecipes() {
		/* todo 1.13
		return recipes.stream()
				.map(ForgeRegistries.RECIPES::getValue)
				.filter(Objects::nonNull)
				.map(IRecipe::getRecipeOutput)
				.filter(s -> !s.isEmpty())
				.collect(Collectors.toList());
		*/
		return Collections.emptyList();
	}
}
