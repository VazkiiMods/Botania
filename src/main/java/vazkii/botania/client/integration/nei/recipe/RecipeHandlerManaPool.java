package vazkii.botania.client.integration.nei.recipe;

import java.awt.Rectangle;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.StatCollector;
import net.minecraftforge.oredict.OreDictionary;

import org.lwjgl.opengl.GL11;

import vazkii.botania.api.BotaniaAPI;
import vazkii.botania.api.recipe.RecipeManaInfusion;
import vazkii.botania.client.core.handler.HUDHandler;
import vazkii.botania.client.lib.LibResources;
import vazkii.botania.client.render.tile.RenderTilePool;
import vazkii.botania.common.block.ModBlocks;
import vazkii.botania.common.block.tile.mana.TilePool;
import codechicken.lib.gui.GuiDraw;
import codechicken.nei.NEIServerUtils;
import codechicken.nei.PositionedStack;
import codechicken.nei.recipe.TemplateRecipeHandler;

public class RecipeHandlerManaPool extends TemplateRecipeHandler {

	public class CachedManaPoolRecipe extends CachedRecipe {

		public List<PositionedStack> inputs = new ArrayList<PositionedStack>();
		public PositionedStack output;
		public List<PositionedStack> otherStacks = new ArrayList<PositionedStack>();
		public int mana;

		public CachedManaPoolRecipe(RecipeManaInfusion recipe) {
			if(recipe == null)
				return;
			inputs.add(new PositionedStack(new ItemStack(ModBlocks.pool, 1, recipe.getOutput().getItem() == Item.getItemFromBlock(ModBlocks.pool) ? 2 : 0), 71, 37));

			if(recipe.getInput() instanceof String)
				inputs.add(new PositionedStack(OreDictionary.getOres((String) recipe.getInput()), 42, 37));
			else inputs.add(new PositionedStack(recipe.getInput(), 42, 37));

			if(recipe.isAlchemy())
				otherStacks.add(new PositionedStack(new ItemStack(ModBlocks.alchemyCatalyst), 10, 37));
			else if (recipe.isConjuration())
				otherStacks.add(new PositionedStack(new ItemStack(ModBlocks.conjurationCatalyst), 10, 37));

			output = new PositionedStack(recipe.getOutput(), 101, 37);
			mana = recipe.getManaToConsume();
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
				boolean skippedPool = false;

				for(PositionedStack stack : ingredients) {
					if(!skippedPool) {
						skippedPool = true;
						continue;
					}

					if(stack.contains(ingredient))
						return true;
				}
			}

			return super.contains(ingredients, ingredient);
		}

	}

	@Override
	public String getRecipeName() {
		return StatCollector.translateToLocal("botania.nei.manaPool");
	}

	@Override
	public String getGuiTexture() {
		return LibResources.GUI_NEI_BLANK;
	}

	@Override
	public int recipiesPerPage() {
		return 1;
	}

	@Override
	public void loadTransferRects() {
		transferRects.add(new RecipeTransferRect(new Rectangle(70, 36, 18, 18), "botania.manaPool"));
	}

	@Override
	public void drawBackground(int recipe) {
		super.drawBackground(recipe);
		GL11.glEnable(GL11.GL_BLEND);
		GL11.glColor4f(1.0F, 1.0F, 1.0F, 0.5F);
		GuiDraw.changeTexture(LibResources.GUI_MANA_INFUSION_OVERLAY);
		GuiDraw.drawTexturedModalRect(45, 20, 38, 35, 92, 50);
		HUDHandler.renderManaBar(32, 80, 0x0000FF, 0.75F, ((CachedManaPoolRecipe) arecipes.get(recipe)).mana, TilePool.MAX_MANA / 10);
		RenderTilePool.forceMana = true;
	}

	@Override
	public void loadCraftingRecipes(String outputId, Object... results) {
		if(outputId.equals("botania.manaPool")) {
			for(RecipeManaInfusion recipe : BotaniaAPI.manaInfusionRecipes) {
				if(recipe == null)
					continue;

				arecipes.add(new CachedManaPoolRecipe(recipe));
			}
		} else super.loadCraftingRecipes(outputId, results);
	}

	@Override
	public void loadCraftingRecipes(ItemStack result) {
		for(RecipeManaInfusion recipe : BotaniaAPI.manaInfusionRecipes) {
			if(recipe == null)
				continue;

			if(NEIServerUtils.areStacksSameTypeCrafting(recipe.getOutput(), result))
				arecipes.add(new CachedManaPoolRecipe(recipe));
		}
	}

	@Override
	public void loadUsageRecipes(ItemStack ingredient) {
		for(RecipeManaInfusion recipe : BotaniaAPI.manaInfusionRecipes) {
			if(recipe == null)
				continue;

			CachedManaPoolRecipe crecipe = new CachedManaPoolRecipe(recipe);
			if(crecipe.contains(crecipe.getIngredients(), ingredient) || crecipe.contains(crecipe.getOtherStacks(), ingredient))
				arecipes.add(crecipe);
		}
	}

}
