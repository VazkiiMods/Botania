package vazkii.botania.client.integration.nei.recipe;

import java.awt.Rectangle;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import net.minecraft.block.Block;
import net.minecraft.item.ItemStack;
import net.minecraft.util.StatCollector;
import net.minecraftforge.oredict.OreDictionary;

import org.lwjgl.opengl.GL11;

import vazkii.botania.api.BotaniaAPI;
import vazkii.botania.api.recipe.RecipePureDaisy;
import vazkii.botania.client.lib.LibResources;
import vazkii.botania.common.item.block.ItemBlockSpecialFlower;
import vazkii.botania.common.lib.LibBlockNames;
import codechicken.lib.gui.GuiDraw;
import codechicken.nei.NEIServerUtils;
import codechicken.nei.PositionedStack;
import codechicken.nei.recipe.TemplateRecipeHandler;

public class RecipeHandlerPureDaisy extends TemplateRecipeHandler {

	public class CachedPureDaisyRecipe extends CachedRecipe {

		public List<PositionedStack> inputs = new ArrayList<PositionedStack>();
		public PositionedStack output;
		public List<PositionedStack> otherStacks = new ArrayList<PositionedStack>();

		public CachedPureDaisyRecipe(RecipePureDaisy recipe) {
			if(recipe == null)
				return;
			inputs.add(new PositionedStack(ItemBlockSpecialFlower.ofType(LibBlockNames.SUBTILE_PUREDAISY), 71, 23));

			if(recipe.getInput() instanceof String)
				inputs.add(new PositionedStack(OreDictionary.getOres((String) recipe.getInput()), 42, 23));
			else inputs.add(new PositionedStack(new ItemStack((Block) recipe.getInput()), 42, 23));

			output = new PositionedStack(new ItemStack(recipe.getOutput()), 101, 23);
		}

		@Override
		public List<PositionedStack> getIngredients() {
			return getCycledIngredients(cycleticks / 20, inputs);
		}

		@Override
		public PositionedStack getResult() {
			return output;
		}

		@Override
		public List<PositionedStack> getOtherStacks() {
			return otherStacks;
		}

		@Override
		public boolean contains(Collection<PositionedStack> ingredients, ItemStack ingredient) {
			if(ingredients == inputs) {
				for(PositionedStack stack : ingredients)
					if(stack.contains(ingredient))
						return true;
			}

			return super.contains(ingredients, ingredient);
		}

	}

	@Override
	public String getRecipeName() {
		return StatCollector.translateToLocal("botania.nei.pureDaisy");
	}

	@Override
	public String getGuiTexture() {
		return LibResources.GUI_NEI_BLANK;
	}

	@Override
	public int recipiesPerPage() {
		return 2;
	}

	@Override
	public void loadTransferRects() {
		transferRects.add(new RecipeTransferRect(new Rectangle(70, 22, 18, 18), "botania.pureDaisy"));
	}

	@Override
	public void drawBackground(int recipe) {
		super.drawBackground(recipe);
		GL11.glEnable(GL11.GL_BLEND);
		GL11.glColor4f(1.0F, 1.0F, 1.0F, 0.5F);
		GuiDraw.changeTexture(LibResources.GUI_PURE_DAISY_OVERLAY);
		GuiDraw.drawTexturedModalRect(45, 10, 0, 0, 65, 44);
	}

	@Override
	public void loadCraftingRecipes(String outputId, Object... results) {
		if(outputId.equals("botania.pureDaisy")) {
			for(RecipePureDaisy recipe : BotaniaAPI.pureDaisyRecipes) {
				if(recipe == null)
					continue;

				arecipes.add(new CachedPureDaisyRecipe(recipe));
			}
		} else super.loadCraftingRecipes(outputId, results);
	}

	@Override
	public void loadCraftingRecipes(ItemStack result) {
		for(RecipePureDaisy recipe : BotaniaAPI.pureDaisyRecipes) {
			if(recipe == null)
				continue;

			if(NEIServerUtils.areStacksSameTypeCrafting(new ItemStack(recipe.getOutput()), result))
				arecipes.add(new CachedPureDaisyRecipe(recipe));
		}
	}

	@Override
	public void loadUsageRecipes(ItemStack ingredient) {
		for(RecipePureDaisy recipe : BotaniaAPI.pureDaisyRecipes) {
			if(recipe == null)
				continue;

			CachedPureDaisyRecipe crecipe = new CachedPureDaisyRecipe(recipe);
			if(crecipe.contains(crecipe.getIngredients(), ingredient) || crecipe.contains(crecipe.getOtherStacks(), ingredient))
				arecipes.add(crecipe);
		}
	}

}
