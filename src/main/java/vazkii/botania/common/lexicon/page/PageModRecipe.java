/**
 * This class was created by <Hubry>. It's distributed as
 * part of the Botania Mod. Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 *
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 *
 * File Created @ [sie 08 2019, 11:43]
 */
package vazkii.botania.common.lexicon.page;

import com.mojang.blaze3d.platform.GlStateManager;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.renderer.texture.TextureManager;
import net.minecraft.client.resources.I18n;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.tags.Tag;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import org.lwjgl.opengl.GL11;
import vazkii.botania.api.internal.IGuiLexiconEntry;
import vazkii.botania.api.lexicon.LexiconEntry;
import vazkii.botania.api.lexicon.LexiconRecipeMappings;
import vazkii.botania.api.recipe.IModRecipe;
import vazkii.botania.client.core.handler.ClientTickHandler;
import vazkii.botania.client.lib.LibResources;
import vazkii.botania.common.core.handler.ConfigHandler;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.function.Predicate;
import java.util.stream.Collectors;

public abstract class PageModRecipe<T extends IModRecipe> extends PageRecipe {
	private static final ResourceLocation petalOverlay = new ResourceLocation(LibResources.GUI_PETAL_OVERLAY);
	
	protected final Item[] outputItems;
	private final Predicate<T> recipeFilter;
	
	private List<T> recipes = null;
	private int ticksElapsed = 0;
	private int recipeAt = 0;

	public PageModRecipe(String unlocalizedName, Predicate<T> filter, Item... outputs) {
		super(unlocalizedName);
		this.recipeFilter = filter;
		this.outputItems = outputs;
	}

	public PageModRecipe(String unlocalizedName, Item... outputItems) {
		this(unlocalizedName, r -> true, outputItems);
	}

	public PageModRecipe(String unlocalizedName, Predicate<T> filter, Tag<Item> outputs) {
		this(unlocalizedName, filter, outputs.getAllElements().toArray(new Item[0]));
	}

	@Override
	public void onPageAdded(LexiconEntry entry, int index) {
		for(Item item : outputItems)
			LexiconRecipeMappings.map(new ItemStack(item), entry, index);
	}

	@Override
	@OnlyIn(Dist.CLIENT)
	public void renderRecipe(IGuiLexiconEntry gui, int mx, int my) {
		T recipe = getRecipes().get(recipeAt);
		TextureManager render = Minecraft.getInstance().textureManager;

		renderItemAtGridPos(gui, 3, 0, recipe.getOutput(), false);
		renderItemAtGridPos(gui, 2, 1, getMiddleStack(), false);

		List<Ingredient> inputs = recipe.getInputs();
		int degreePerInput = (int) (360F / inputs.size());
		float currentDegree = ConfigHandler.CLIENT.lexiconRotatingItems.get()
				? Screen.hasShiftDown()
					? ticksElapsed
					: ticksElapsed + ClientTickHandler.partialTicks
				: 0;
		int inputIndex = ticksElapsed / 40;

		for(Ingredient input : inputs) {
			ItemStack[] stacks = input.getMatchingStacks();
			int size = stacks.length;
			ItemStack stack = stacks[size - inputIndex % size - 1];
			// todo 1.13 can't we just use inputIndex % size?

			renderItemAtAngle(gui, currentDegree, stack);

			currentDegree += degreePerInput;
		}

		renderManaBar(gui, recipe, mx, my);

		render.bindTexture(petalOverlay);

		GlStateManager.enableBlend();
		GlStateManager.blendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
		GlStateManager.color4f(1F, 1F, 1F, 1F);
		((Screen) gui).blit(gui.getLeft(), gui.getTop(), 0, 0, gui.getWidth(), gui.getHeight());
		GlStateManager.disableBlend();
	}

	abstract ItemStack getMiddleStack();

	@OnlyIn(Dist.CLIENT)
	public void renderManaBar(IGuiLexiconEntry gui, T recipe, int mx, int my) {
		FontRenderer font = Minecraft.getInstance().fontRenderer;
		GlStateManager.enableBlend();
		GlStateManager.blendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);

		int x = gui.getLeft() + gui.getWidth() / 2 - 50;
		int y = gui.getTop() + 120;

		String stopStr = I18n.format("botaniamisc.shiftToStopSpin");
		font.drawString(stopStr, x + 50 - font.getStringWidth(stopStr) / 2, y + 15, 0x99000000);

		GlStateManager.disableBlend();
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

	public List<T> getRecipes(){
		if(recipes == null) {
			recipes = findRecipes();
		}
		return recipes;
	}

	@Override
	public List<ItemStack> getDisplayedRecipes() {
		return Arrays.stream(outputItems).map(ItemStack::new).collect(Collectors.toList());
	}

	protected abstract List<T> findRecipes();

	protected List<T> findRecipes(Collection<T> recipeList) {
		if(recipes == null) {
			List<T> list = new ArrayList<>();
			for(T recipe : recipeList) {
				if(recipeFilter.test(recipe)) {
					for(Item output : outputItems) {
						if(recipe.getOutput().getItem() == output) {
							list.add(recipe);
						}
					}
				}
			}
			recipes = list;
			
			list.sort((r1, r2) -> {
				Item output1 = r1.getOutput().getItem();
				Item output2 = r2.getOutput().getItem();
				if(output1 == output2)
					return 0;
				for(Item outputItem : outputItems) {
					if(outputItem == output1) {
						return -1;
					} else if(outputItem == output2) {
						return 1;
					}
				}
				return 0;
			});
			return list;
		}
		return recipes;
	}
}
