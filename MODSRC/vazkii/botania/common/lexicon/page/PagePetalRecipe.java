/**
 * This class was created by <Vazkii>. It's distributed as
 * part of the Botania Mod. Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 * 
 * Botania is Open Source and distributed under a
 * Creative Commons Attribution-NonCommercial-ShareAlike 3.0 License
 * (http://creativecommons.org/licenses/by-nc-sa/3.0/deed.en_GB)
 * 
 * File Created @ [Feb 8, 2014, 1:11:35 PM (GMT)]
 */
package vazkii.botania.common.lexicon.page;

import java.util.Arrays;
import java.util.List;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.renderer.texture.TextureManager;
import net.minecraft.item.ItemStack;
import vazkii.botania.api.internal.IGuiLexiconEntry;
import vazkii.botania.api.recipe.RecipePetals;
import vazkii.botania.common.block.ModBlocks;

public class PagePetalRecipe extends PageRecipe {

	List<RecipePetals> recipes;
	int ticksElapsed = 0;
	int recipeAt = 0;
	
	public PagePetalRecipe(String unlocalizedName, List<RecipePetals> recipes) {
		super(unlocalizedName);
		this.recipes = recipes;
	}
	
	public PagePetalRecipe(String unlocalizedName, RecipePetals recipe) {
		this(unlocalizedName, Arrays.asList(recipe));
	}
	
	@Override
	public void renderRecipe(IGuiLexiconEntry gui, int mx, int my) {
		RecipePetals recipe = recipes.get(recipeAt);
		
		renderItemAtGridPos(gui, 2, 0, recipe.getOutput(), false);
		renderItemAtGridPos(gui, 2, 1, new ItemStack(ModBlocks.altar), false);
		
//		GL11.glEnable(GL11.GL_BLEND);
//		GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
//		String manaUsage = StatCollector.translateToLocal("botaniamisc.manaUsage");
//		font.drawString(manaUsage, gui.getLeft() + gui.getWidth() / 2 - font.getStringWidth(manaUsage) / 2, gui.getTop() + 105, 0x66000000);
//
//		HUDHandler.renderManaBar(gui.getLeft() + gui.getWidth() / 2 - 50, gui.getTop() + 115, 0x0000FF, 0.75F, recipe.getManaToConsume(), TilePool.MAX_MANA / 10);
//		GL11.glDisable(GL11.GL_BLEND);
	}
	
	@Override
	public void updateScreen() {
		if(ticksElapsed % 20 == 0) {
			recipeAt++;

			if(recipeAt == recipes.size())
				recipeAt = 0;
		}
		++ticksElapsed;
	}

}
