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
		LibBlockIDs.idAltar = loadBlock(LibBlockNames.ALTAR, LibBlockIDs.idAltar);
		LibBlockIDs.idSpecialFlower = loadBlock(LibBlockNames.SPECIAL_FLOWER, LibBlockIDs.idSpecialFlower);
		LibBlockIDs.idLivingrock = loadBlock(LibBlockNames.LIVING_ROCK, LibBlockIDs.idLivingrock);
		LibBlockIDs.idLivingwood = loadBlock(LibBlockNames.LIVING_WOOD, LibBlockIDs.idLivingwood);
		LibBlockIDs.idSpreader = loadBlock(LibBlockNames.SPREADER, LibBlockIDs.idSpreader);
		LibBlockIDs.idPool = loadBlock(LibBlockNames.POOL, LibBlockIDs.idPool);
		LibBlockIDs.idRuneAltar = loadBlock(LibBlockNames.RUNE_ALTAR, LibBlockIDs.idRuneAltar);
		LibBlockIDs.idUnstableBlock = loadBlock(LibBlockNames.UNSTABLE_BLOCK, LibBlockIDs.idUnstableBlock);

		// Item IDs
		LibItemIDs.idLexicon = loadItem(LibItemNames.LEXICON, LibItemIDs.idLexicon);
		LibItemIDs.idPetal = loadItem(LibItemNames.PETAL, LibItemIDs.idPetal);
		LibItemIDs.idDye = loadItem(LibItemNames.DYE, LibItemIDs.idDye);
		LibItemIDs.idPestleAndMortar = loadItem(LibItemNames.PESTLE_AND_MORTAR, LibItemIDs.idPestleAndMortar);
		LibItemIDs.idTwigWand = loadItem(LibItemNames.TWIG_WAND, LibItemIDs.idTwigWand);
		LibItemIDs.idManaResource = loadItem(LibItemNames.MANA_RESOURCE, LibItemIDs.idManaResource);
		LibItemIDs.idLens = loadItem(LibItemNames.LENS, LibItemIDs.idLens);
		LibItemIDs.idManaPetal = loadItem(LibItemNames.MANA_PETAL, LibItemIDs.idManaPetal);

		config.save();
	}

	private static int loadItem(String label, int defaultID) {
		return config.getItem(label, defaultID).getInt(defaultID);
	}

	private static int loadBlock(String label, int defaultID) {
		return config.getBlock(label, defaultID).getInt(defaultID);
	}
}
