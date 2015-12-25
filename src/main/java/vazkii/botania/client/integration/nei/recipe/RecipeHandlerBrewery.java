package vazkii.botania.client.integration.nei.recipe;

import java.awt.Rectangle;
import java.util.ArrayList;
import java.util.List;

import net.minecraft.item.ItemStack;
import net.minecraft.util.StatCollector;
import net.minecraftforge.oredict.OreDictionary;

import org.lwjgl.opengl.GL11;

import vazkii.botania.api.BotaniaAPI;
import vazkii.botania.api.brew.IBrewContainer;
import vazkii.botania.api.brew.IBrewItem;
import vazkii.botania.api.recipe.RecipeBrew;
import vazkii.botania.client.lib.LibResources;
import vazkii.botania.common.item.ModItems;
import codechicken.lib.gui.GuiDraw;
import codechicken.nei.PositionedStack;
import codechicken.nei.recipe.TemplateRecipeHandler;

public class RecipeHandlerBrewery extends TemplateRecipeHandler {

	public class CachedBreweryRecipe extends CachedRecipe {

		public List<PositionedStack> inputs = new ArrayList<PositionedStack>();
		public PositionedStack output;
		public int mana;

		public CachedBreweryRecipe(RecipeBrew recipe, ItemStack vial) {
			if(recipe == null)
				return;

			setIngredients(recipe.getInputs());
			ItemStack toVial;
			if (vial == null)
				toVial = new ItemStack(ModItems.vial);
			else
				toVial = vial.copy();
			toVial.stackSize = 1;
			inputs.add(new PositionedStack(toVial, 39, 42));

			output = new PositionedStack(recipe.getOutput(toVial), 87, 42);
		}

		public CachedBreweryRecipe(RecipeBrew recipe) {
			this(recipe, null);
		}

		public void setIngredients(List<Object> inputs) {
			int left = 96 - inputs.size() * 18 / 2;

			int i = 0;
			for (Object o : inputs) {
				if (o instanceof String)
					this.inputs.add(new PositionedStack(OreDictionary.getOres((String) o), left + i * 18, 6));
				else
					this.inputs.add(new PositionedStack(o, left + i * 18, 6));

				i++;
			}
		}

		@Override
		public List<PositionedStack> getIngredients() {
			return getCycledIngredients(cycleticks / 20, inputs);
		}

		@Override
		public PositionedStack getResult() {
			return output;
		}

	}

	@Override
	public String getRecipeName() {
		return StatCollector.translateToLocal("botania.nei.brewery");
	}

	@Override
	public String getGuiTexture() {
		return LibResources.GUI_NEI_BREWERY;
	}

	@Override
	public void loadTransferRects() {
		transferRects.add(new RecipeTransferRect(new Rectangle(87, 25, 15, 14), "botania.brewery"));
	}

	@Override
	public void drawBackground(int recipe) {
		GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
		GuiDraw.changeTexture(getGuiTexture());
		GuiDraw.drawTexturedModalRect(0, 0, 0, 0, 166, 65);
	}

	@Override
	public void loadCraftingRecipes(String outputId, Object... results) {
		if(outputId.equals("botania.brewery"))
			for(RecipeBrew recipe : BotaniaAPI.brewRecipes)
				arecipes.add(new CachedBreweryRecipe(recipe));
		else super.loadCraftingRecipes(outputId, results);
	}

	@Override
	public void loadCraftingRecipes(ItemStack result) {
		if(result.getItem() instanceof IBrewItem)
			for(RecipeBrew recipe : BotaniaAPI.brewRecipes) {
				if(recipe == null)
					continue;

				if(((IBrewItem) result.getItem()).getBrew(result) == recipe.getBrew())
					arecipes.add(new CachedBreweryRecipe(recipe));
			}
	}

	@Override
	public void loadUsageRecipes(ItemStack ingredient) {
		if(ingredient.getItem() instanceof IBrewContainer) {
			for(RecipeBrew recipe : BotaniaAPI.brewRecipes) {
				if(recipe == null)
					continue;

				if(recipe.getOutput(ingredient) != null)
					arecipes.add(new CachedBreweryRecipe(recipe, ingredient));
			}
		} else for(RecipeBrew recipe : BotaniaAPI.brewRecipes) {
			if(recipe == null)
				continue;

			CachedBreweryRecipe crecipe = new CachedBreweryRecipe(recipe);
			if(crecipe.contains(crecipe.inputs, ingredient))
				arecipes.add(crecipe);
		}
	}

}
