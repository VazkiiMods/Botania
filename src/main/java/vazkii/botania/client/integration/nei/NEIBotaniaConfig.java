package vazkii.botania.client.integration.nei;

import org.lwjgl.input.Keyboard;

import vazkii.botania.client.integration.nei.recipe.RecipeHandlerBrewery;
import vazkii.botania.client.integration.nei.recipe.RecipeHandlerElvenTrade;
import vazkii.botania.client.integration.nei.recipe.RecipeHandlerFloatingFlowers;
import vazkii.botania.client.integration.nei.recipe.RecipeHandlerLexicaBotania;
import vazkii.botania.client.integration.nei.recipe.RecipeHandlerManaPool;
import vazkii.botania.client.integration.nei.recipe.RecipeHandlerPetalApothecary;
import vazkii.botania.client.integration.nei.recipe.RecipeHandlerPureDaisy;
import vazkii.botania.client.integration.nei.recipe.RecipeHandlerRunicAltar;
import vazkii.botania.common.lib.LibMisc;
import codechicken.nei.api.API;
import codechicken.nei.api.IConfigureNEI;
import codechicken.nei.guihook.GuiContainerManager;

public class NEIBotaniaConfig implements IConfigureNEI {

	public static final String CORPOREA_KEY = "gui.botania_corporea_request";
	
	@Override
	public String getName() {
		return LibMisc.MOD_NAME;
	}

	@Override
	public String getVersion() {
		return LibMisc.VERSION;
	}

	@Override
	public void loadConfig() {
		API.registerRecipeHandler(new RecipeHandlerFloatingFlowers());
		API.registerUsageHandler(new RecipeHandlerFloatingFlowers());

		API.registerRecipeHandler(new RecipeHandlerPetalApothecary());
		API.registerUsageHandler(new RecipeHandlerPetalApothecary());

		API.registerRecipeHandler(new RecipeHandlerRunicAltar());
		API.registerUsageHandler(new RecipeHandlerRunicAltar());

		API.registerRecipeHandler(new RecipeHandlerManaPool());
		API.registerUsageHandler(new RecipeHandlerManaPool());

		API.registerRecipeHandler(new RecipeHandlerElvenTrade());
		API.registerUsageHandler(new RecipeHandlerElvenTrade());

		API.registerRecipeHandler(new RecipeHandlerBrewery());
		API.registerUsageHandler(new RecipeHandlerBrewery());

		API.registerRecipeHandler(new RecipeHandlerPureDaisy());
		API.registerUsageHandler(new RecipeHandlerPureDaisy());
		
		API.registerRecipeHandler(new RecipeHandlerLexicaBotania());
		API.registerUsageHandler(new RecipeHandlerLexicaBotania());
		
		API.addKeyBind(CORPOREA_KEY, Keyboard.KEY_C);
		GuiContainerManager.addInputHandler(new NEIInputHandler());
	}

}
