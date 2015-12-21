package vazkii.botania.client.integration.nei;

import net.minecraftforge.fml.common.API;
import vazkii.botania.common.lib.LibMisc;

public class NEIBotaniaConfig implements IConfigureNEI {

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
	}

}
