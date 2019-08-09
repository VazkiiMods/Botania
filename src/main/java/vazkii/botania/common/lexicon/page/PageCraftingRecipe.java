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

import com.mojang.blaze3d.platform.GlStateManager;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.renderer.texture.TextureManager;
import net.minecraft.client.resources.I18n;
import net.minecraft.inventory.CraftingInventory;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.item.crafting.IRecipeType;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.item.crafting.ShapedRecipe;
import net.minecraft.item.crafting.ShapelessRecipe;
import net.minecraft.util.NonNullList;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.fml.server.ServerLifecycleHooks;
import org.lwjgl.opengl.GL11;
import vazkii.botania.api.internal.IGuiLexiconEntry;
import vazkii.botania.api.lexicon.LexiconEntry;
import vazkii.botania.api.lexicon.LexiconRecipeMappings;
import vazkii.botania.client.core.helper.RenderHelper;
import vazkii.botania.client.lib.LibResources;
import vazkii.botania.common.Botania;
import vazkii.botania.common.lib.LibMisc;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.function.Predicate;
import java.util.stream.Collectors;

public class PageCraftingRecipe extends PageRecipe {

	private static final ResourceLocation craftingOverlay = new ResourceLocation(LibResources.GUI_CRAFTING_OVERLAY);
	private static final List<IRecipe<?>> DUMMY = Collections.singletonList(new ShapedRecipe(new ResourceLocation(LibMisc.MOD_ID, "dummy"), "", 1, 1, NonNullList.withSize(1, Ingredient.EMPTY), ItemStack.EMPTY));

	private final Item[] outputItems;
	private final Predicate<IRecipe<?>> recipeFilter;
	
	private List<IRecipe<?>> recipes;
	
	private int ticksElapsed = 0;
	private int recipeAt = 0;

	public PageCraftingRecipe(String unlocalizedName, Item... outputs) {
		this(unlocalizedName, r -> true, outputs);
	}

	public PageCraftingRecipe(String unlocalizedName, Predicate<IRecipe<?>> filter, Item... outputs) {
		super(unlocalizedName);
		this.outputItems = outputs;
		this.recipeFilter = filter;
	}

	@Override
	public void onPageAdded(LexiconEntry entry, int index) {
		for(Item item : outputItems) {
			LexiconRecipeMappings.map(new ItemStack(item), entry, index);
		}
	}

	@Override
	@OnlyIn(Dist.CLIENT)
	public void renderRecipe(IGuiLexiconEntry gui, int mx, int my) {

		IRecipe<?> recipe = getRecipes().get(recipeAt);
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

		if(recipe instanceof ShapelessRecipe) {
			((Screen) gui).blit(iconX, iconY, 240, 0, 16, 16);

			if(mx >= iconX && my >= iconY && mx < iconX + 16 && my < iconY + 16)
				RenderHelper.renderTooltip(mx, my, Collections.singletonList(I18n.format("botaniamisc.shapeless")));
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

			if(recipeAt == getRecipes().size())
				recipeAt = 0;
		}
		++ticksElapsed;
	}

	@OnlyIn(Dist.CLIENT)
	public void renderCraftingRecipe(IGuiLexiconEntry gui, IRecipe<?> recipe) {
		if(recipe == null)
			return;

		if(recipe instanceof ShapedRecipe) {

			int width = ((ShapedRecipe) recipe).getWidth();
			int height = ((ShapedRecipe) recipe).getHeight();

			for(int y = 0; y < height; y++)
				for(int x = 0; x < width; x++) {
					Ingredient input = recipe.getIngredients().get(y * width + x);
					ItemStack[] stacks = input.getMatchingStacks();
					if(stacks.length > 0) {
						renderItemAtGridPos(gui, 1 + x, 1 + y, stacks[(ticksElapsed / 40) % stacks.length], true);
					}
				}
		} else if(recipe instanceof ShapelessRecipe) {

			drawGrid : {
				for(int y = 0; y < 3; y++)
					for(int x = 0; x < 3; x++) {
						int index = y * 3 + x;

						if(index >= recipe.getIngredients().size())
							break drawGrid;

						Ingredient input = recipe.getIngredients().get(index);
						if(input != Ingredient.EMPTY) {
							ItemStack[] stacks = input.getMatchingStacks(); 
							renderItemAtGridPos(gui, 1 + x, 1 + y, stacks[(ticksElapsed / 40) % stacks.length], true);
						}
					}
			}
		}

		renderItemAtGridPos(gui, 2, 0, recipe.getRecipeOutput(), false);
	}

	public List<IRecipe<?>> getRecipes() {
		if(recipes == null) {
			recipes = findRecipes();
		}
		return recipes;
	}

	private List<IRecipe<?>> findRecipes() {
		List<IRecipe<?>> list = new ArrayList<>();
		for(IRecipe<CraftingInventory> recipe : ServerLifecycleHooks.getCurrentServer().getRecipeManager().getRecipes(IRecipeType.CRAFTING).values()) {
			if(recipeFilter.test(recipe)) {
				for(Item output : outputItems) {
					if(recipe.getRecipeOutput().getItem() == output) {
						list.add(recipe);
					}
				}
			}
		}
		if(list.isEmpty()) {
			Botania.LOGGER.info("Could not find mana infusion recipes for items {}, using dummy", (Object) this.outputItems);
			return DUMMY;
		}
		return list;
	}

	@Override
	public List<ItemStack> getDisplayedRecipes() {
		return Arrays.stream(outputItems).map(ItemStack::new).collect(Collectors.toList());
	}
}
