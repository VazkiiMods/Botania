/**
 * This class was created by <Vazkii>. It's distributed as
 * part of the Botania Mod. Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 *
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 *
 * File Created @ [Nov 2, 2014, 5:17:46 PM (GMT)]
 */
package vazkii.botania.common.lexicon.page;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.resources.I18n;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.TextFormatting;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import vazkii.botania.api.BotaniaAPI;
import vazkii.botania.api.brew.Brew;
import vazkii.botania.api.internal.IGuiLexiconEntry;
import vazkii.botania.api.lexicon.ILexicon;
import vazkii.botania.api.lexicon.ITwoNamedPage;
import vazkii.botania.api.recipe.RecipeBrew;
import vazkii.botania.common.core.helper.PlayerHelper;
import vazkii.botania.common.item.ModItems;
import vazkii.botania.common.lib.LibMisc;

import java.util.ArrayList;
import java.util.List;

public class PageBrew extends PageRecipe implements ITwoNamedPage {
	private static final RecipeBrew DUMMY = new RecipeBrew(new ResourceLocation(LibMisc.MOD_ID, "dummy"), BotaniaAPI.fallbackBrew);

	public final RecipeBrew recipe;
	public String text;

	public PageBrew(Brew brew, String unlocalizedName, String bottomText) {
		super(bottomText);
		recipe = BotaniaAPI.brewRecipes.values().stream().filter(recipe -> recipe.getBrew() == brew).findFirst().orElse(DUMMY);
		text = unlocalizedName;
	}

	@Override
	@OnlyIn(Dist.CLIENT)
	public void renderRecipe(IGuiLexiconEntry gui, int mx, int my) {
		int width = gui.getWidth() - 30;
		int height = gui.getHeight();
		int x = gui.getLeft() + 16;
		int y = gui.getTop() + 12;

		Brew brew = recipe.getBrew();
		FontRenderer renderer = Minecraft.getInstance().fontRenderer;
		String s = TextFormatting.BOLD + I18n.format("botaniamisc.brewOf", I18n.format(brew.getUnlocalizedName()));
		renderer.drawString(s, gui.getLeft() + gui.getWidth() / 2 - renderer.getStringWidth(s) / 2, y, 0x222222);
		PageText.renderText(x, y + 22, width, height, text);

		ItemStack book = PlayerHelper.getFirstHeldItemClass(Minecraft.getInstance().player, ILexicon.class);
		if(book != null && ((ILexicon) book.getItem()).isKnowledgeUnlocked(book, BotaniaAPI.elvenKnowledge)) {
			renderItemAtLinePos(gui, 20, 2, y + 12, recipe.getOutput(new ItemStack(ModItems.vial)));
			renderItemAtLinePos(gui, 20, 3, y + 12, recipe.getOutput(new ItemStack(ModItems.flask)));
		} else renderItemAtLinePos(gui, 0, -1, y + 12, recipe.getOutput(new ItemStack(ModItems.vial)));

		int i = 0;
		y = gui.getTop() + gui.getHeight() - 54;
		List<Ingredient> inputs = new ArrayList<>(recipe.getInputs());

		int offset = gui.getWidth() / 2 - inputs.size() * 9;
		for(Ingredient input : inputs) {
			renderItemAtLinePos(gui, offset, i, y, input.getMatchingStacks()[0]);
			i++;
		}

		super.renderRecipe(gui, mx, my);
	}

	@OnlyIn(Dist.CLIENT)
	public void renderItemAtLinePos(IGuiLexiconEntry gui, int offset, int pos, int yPos, ItemStack stack) {
		if(stack.isEmpty())
			return;

		int xPos = gui.getLeft() + (pos == -1 ? gui.getWidth() / 2 - 8 : pos * 18) + offset;

		renderItem(gui, xPos, yPos, stack, false);
	}

	@Override
	public List<ItemStack> getDisplayedRecipes() {
		ArrayList<ItemStack> list = new ArrayList<>();
		list.add(recipe.getOutput(new ItemStack(ModItems.vial)));
		return list;
	}

	@Override
	public void setSecondUnlocalizedName(String name) {
		text = name;
	}

	@Override
	public String getSecondUnlocalizedName() {
		return text;
	}

}
