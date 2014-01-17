/**
 * This class was created by <Vazkii>. It's distributed as
 * part of the Botania Mod. Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 * 
 * Botania is Open Source and distributed under a
 * Creative Commons Attribution-NonCommercial-ShareAlike 3.0 License
 * (http://creativecommons.org/licenses/by-nc-sa/3.0/deed.en_GB)
 * 
 * File Created @ [Jan 13, 2014, 7:45:37 PM (GMT)]
 */
package vazkii.botania.common.core.proxy;

import vazkii.botania.api.BotaniaAPI;
import vazkii.botania.common.Botania;
import vazkii.botania.common.block.ModBlocks;
import vazkii.botania.common.core.handler.ConfigHandler;
import vazkii.botania.common.core.handler.InternalMethodHandler;
import vazkii.botania.common.item.ModItems;
import vazkii.botania.common.lexicon.LexiconData;
import vazkii.botania.common.network.GuiHandler;
import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.event.FMLPostInitializationEvent;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import cpw.mods.fml.common.network.NetworkRegistry;

public class CommonProxy {

	public void preInit(FMLPreInitializationEvent event) {
		BotaniaAPI.internalHandler = new InternalMethodHandler();

		ConfigHandler.loadConfig(event.getSuggestedConfigurationFile());

		ModBlocks.init();
		ModItems.init();
		LexiconData.init();
	}

	public void init(FMLInitializationEvent event) {
		NetworkRegistry.instance().registerGuiHandler(Botania.instance, new GuiHandler());
	}

	public void postInit(FMLPostInitializationEvent event) {

	}


}
