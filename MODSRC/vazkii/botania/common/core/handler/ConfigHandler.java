/**
 * This class was created by <Vazkii>. It's distributed as
 * part of the Botania Mod. Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 * 
 * Botania is Open Source and distributed under a
 * Creative Commons Attribution-NonCommercial-ShareAlike 3.0 License
 * (http://creativecommons.org/licenses/by-nc-sa/3.0/deed.en_GB)
 * 
 * File Created @ [Jan 13, 2014, 9:01:32 PM (GMT)]
 */
package vazkii.botania.common.core.handler;

import java.io.File;

import net.minecraftforge.common.Configuration;
import vazkii.botania.common.lib.LibBlockIDs;
import vazkii.botania.common.lib.LibBlockNames;
import vazkii.botania.common.lib.LibItemIDs;
import vazkii.botania.common.lib.LibItemNames;

public final class ConfigHandler {

	private static Configuration config;

	public static void loadConfig(File configFile) {
		config = new Configuration(configFile);

		config.load();

		// Block IDs
		LibBlockIDs.idFlower = loadBlock(LibBlockNames.FLOWER, LibBlockIDs.idFlower);

		// Item IDs
		LibItemIDs.idLexicon = loadItem(LibItemNames.LEXICON, LibItemIDs.idLexicon);
		LibItemIDs.idPetal = loadItem(LibItemNames.PETAL, LibItemIDs.idPetal);
		LibItemIDs.idDye = loadItem(LibItemNames.DYE, LibItemIDs.idDye);
		LibItemIDs.idPestleAndMortar = loadItem(LibItemNames.PESTLE_AND_MORTAR, LibItemIDs.idPestleAndMortar);

		config.save();
	}

	private static int loadItem(String label, int defaultID) {
		return config.getItem(label, defaultID).getInt(defaultID);
	}

	private static int loadBlock(String label, int defaultID) {
		return config.getBlock(label, defaultID).getInt(defaultID);
	}
}
