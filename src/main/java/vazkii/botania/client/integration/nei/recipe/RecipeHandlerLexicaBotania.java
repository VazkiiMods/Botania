/**
 * This class was created by <Vazkii>. It's distributed as
 * part of the Botania Mod. Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 * 
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 * 
 * File Created @ [12/12/2015, 23:25:47 (GMT)]
 */
package vazkii.botania.client.integration.nei.recipe;

import java.awt.Rectangle;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.util.StatCollector;
import net.minecraftforge.oredict.OreDictionary;

import org.lwjgl.opengl.GL11;

import vazkii.botania.api.BotaniaAPI;
import vazkii.botania.api.lexicon.KnowledgeType;
import vazkii.botania.api.lexicon.LexiconEntry;
import vazkii.botania.api.lexicon.LexiconRecipeMappings;
import vazkii.botania.api.recipe.RecipeManaInfusion;
import vazkii.botania.client.core.handler.HUDHandler;
import vazkii.botania.client.integration.nei.recipe.RecipeHandlerManaPool.CachedManaPoolRecipe;
import vazkii.botania.client.lib.LibResources;
import vazkii.botania.client.render.tile.RenderTilePool;
import vazkii.botania.common.block.ModBlocks;
import vazkii.botania.common.block.tile.mana.TilePool;
import vazkii.botania.common.core.helper.InventoryHelper;
import vazkii.botania.common.item.ModItems;
import vazkii.botania.common.lexicon.LexiconData;
import vazkii.botania.common.lexicon.page.PageRecipe;
import vazkii.botania.common.lexicon.page.PageText;
import codechicken.lib.gui.GuiDraw;
import codechicken.nei.NEIServerUtils;
import codechicken.nei.PositionedStack;
import codechicken.nei.recipe.TemplateRecipeHandler;
import codechicken.nei.recipe.TemplateRecipeHandler.CachedRecipe;
import codechicken.nei.recipe.TemplateRecipeHandler.RecipeTransferRect;

public class RecipeHandlerLexicaBotania extends TemplateRecipeHandler {

	public class CachedLexicaBotaniaRecipe extends CachedRecipe {

		public LexiconEntry entry;
		public PositionedStack item;
		public List<PositionedStack> otherStacks = new ArrayList<PositionedStack>();

		public CachedLexicaBotaniaRecipe(ItemStack stack, LexiconEntry entry) {
			otherStacks.add(new PositionedStack(new ItemStack(ModItems.lexicon), 51, 5));
			item = new PositionedStack(stack, 91, 5);
			this.entry = entry;
		}

		@Override
		public List<PositionedStack> getIngredients() {
			return otherStacks;
		}

		@Override
		public PositionedStack getResult() {
			return item;
		}

		@Override
		public List<PositionedStack> getOtherStacks() {
			return otherStacks;
		}

		@Override
		public boolean contains(Collection<PositionedStack> ingredients, ItemStack ingredient) {
			return ingredient.getItem() == ModItems.lexicon;
		}

	}

	@Override
	public String getRecipeName() {
		return StatCollector.translateToLocal("botania.nei.lexica");
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
		transferRects.add(new RecipeTransferRect(new Rectangle(50, 4, 18, 18), "botania.lexica"));
	}
	
	@Override
	public void drawBackground(int recipe) {
		super.drawBackground(recipe);
		
		FontRenderer font = Minecraft.getMinecraft().fontRenderer;
		CachedLexicaBotaniaRecipe recipeObj = ((CachedLexicaBotaniaRecipe) arecipes.get(recipe));

		String s = EnumChatFormatting.UNDERLINE + StatCollector.translateToLocal(recipeObj.entry.getUnlocalizedName());
		font.drawString(s, 82 - font.getStringWidth(s) / 2, 30, 4210752);
		
		KnowledgeType type = recipeObj.entry.getKnowledgeType();
		s = type.color + StatCollector.translateToLocal(type.getUnlocalizedName()).replaceAll("\\&.", "");
		font.drawString(s, 82 - font.getStringWidth(s) / 2, 42, 4210752);
		
		s = "\"" + StatCollector.translateToLocal(recipeObj.entry.getTagline()) + "\"";
		PageText.renderText(5, 42, 160, 200, s);
		
		String key = LexiconRecipeMappings.stackToString(recipeObj.item.item);
		String quickInfo = "botania.nei.quickInfo:" + key;
		String quickInfoLocal = StatCollector.translateToLocal(quickInfo);
		
		if(GuiScreen.isShiftKeyDown() && GuiScreen.isCtrlKeyDown() && Minecraft.getMinecraft().gameSettings.advancedItemTooltips)
			s = "name: " + key;
		else if(quickInfo.equals(quickInfoLocal))
			s = StatCollector.translateToLocal("botania.nei.lexicaNoInfo");
		else {
			s = StatCollector.translateToLocal("botania.nei.lexicaSeparator");
			font.drawString(s, 82 - font.getStringWidth(s) / 2, 80, 4210752);
			s = quickInfoLocal;
		}
		
		PageText.renderText(5, 80, 160, 200, s);
	}

	@Override
	public void loadCraftingRecipes(String outputId, Object... results) {
		if(outputId.equals("botania.lexica")) {
			for(LexiconEntry entry : BotaniaAPI.getAllEntries()) {
				List<ItemStack> stacks = entry.getDisplayedRecipes();
				for(ItemStack stack : stacks)
					arecipes.add(new CachedLexicaBotaniaRecipe(stack, entry));
			}
		} else super.loadCraftingRecipes(outputId, results);
	}

	@Override
	public void loadCraftingRecipes(ItemStack result) {
		for(LexiconEntry entry : BotaniaAPI.getAllEntries()) {
			List<ItemStack> stacks = entry.getDisplayedRecipes();
			for(ItemStack stack : stacks) {
				String key1 = LexiconRecipeMappings.stackToString(stack);
				String key2 = LexiconRecipeMappings.stackToString(result);
				if(key1.equals(key2))
					arecipes.add(new CachedLexicaBotaniaRecipe(stack, entry));
			}
		}
	}

	@Override
	public void loadUsageRecipes(ItemStack ingredient) {
		// NO-OP
	}

}
