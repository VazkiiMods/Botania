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
import net.minecraftforge.common.Property;
import vazkii.botania.common.lib.LibBlockIDs;
import vazkii.botania.common.lib.LibBlockNames;
import vazkii.botania.common.lib.LibItemIDs;
import vazkii.botania.common.lib.LibItemNames;

public final class ConfigHandler {

	private static Configuration config;

	public static boolean lexiconRotatingItems = true;
	public static boolean subtlePowerSystem = false;
	public static boolean staticWandBeam = false;
	public static boolean overrideVanillaBlocks = true;
	public static boolean boundBlockWireframe = true;
	
	public static int flowerQuantity = 3;
	public static int flowerDensity = 32;

	public static void loadConfig(File configFile) {
		config = new Configuration(configFile);

		config.load();

		Property lexiconRotatingItemsProp = config.get(Configuration.CATEGORY_GENERAL, "lexicon.enable.rotatingItems", true);
		lexiconRotatingItemsProp.comment = "Set to false to disable the rotating items in the petal and rune entries in the Lexica Botania.";
		lexiconRotatingItems = lexiconRotatingItemsProp.getBoolean(true);

		Property subtlePowerSystemProp = config.get(Configuration.CATEGORY_GENERAL, "powerSystem.subtle", false);
		subtlePowerSystemProp.comment = "Set to true to set the power system's particles to be a lot more subtle. Good for low-end systems, if the particles are causing lag.";
		subtlePowerSystem = subtlePowerSystemProp.getBoolean(false);

		Property staticWandBeamProp = config.get(Configuration.CATEGORY_GENERAL, "wandBeam.static", false);
		staticWandBeamProp.comment = "Set to true to use a static wand beam that shows every single position of the burst, similar to the way it used to work on old botania versions. Warning: Disabled by default because it may be laggy.";
		staticWandBeam = staticWandBeamProp.getBoolean(false);

		Property overrideVanillaBlocksProp = config.get(Configuration.CATEGORY_GENERAL, "override.vanillaBlocks", true);
		overrideVanillaBlocksProp.comment = "Set to false to remove the overriding of vanilla blocks (snow, red and yellow flowers). Setting this to false will remove the snow on flowers tweak.";
		overrideVanillaBlocks = overrideVanillaBlocksProp.getBoolean(true);

		Property boundBlockWireframeProp = config.get(Configuration.CATEGORY_GENERAL, "boundBlock.wireframe.enabled", true);
		boundBlockWireframeProp.comment = "Set to false to disable the wireframe when looking a block bound to something (spreaders, flowers, etc).";
		boundBlockWireframe = boundBlockWireframeProp.getBoolean(true);
		
		Property propFlowerQuantity = config.get(Configuration.CATEGORY_GENERAL, "worldgen.flower.quantity", 3);
		flowerQuantity = propFlowerQuantity.getInt(3);
		Property propFlowerDensity = config.get(Configuration.CATEGORY_GENERAL, "worldgen.flower.density", 32);
		flowerQuantity = propFlowerDensity.getInt(32);
		
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
		LibBlockIDs.idPylon = loadBlock(LibBlockNames.PYLON, LibBlockIDs.idPylon);
		LibBlockIDs.idPistonRelay = loadBlock(LibBlockNames.PISTON_RELAY, LibBlockIDs.idPistonRelay);
		LibBlockIDs.idDistributor = loadBlock(LibBlockNames.DISTRIBUTOR, LibBlockIDs.idDistributor);
		LibBlockIDs.idManaBeacon = loadBlock(LibBlockNames.MANA_BEACON, LibBlockIDs.idManaBeacon);
		LibBlockIDs.idManaVoid = loadBlock(LibBlockNames.MANA_VOID, LibBlockIDs.idManaVoid);
		LibBlockIDs.idManaDetector = loadBlock(LibBlockNames.MANA_DETECTOR, LibBlockIDs.idManaDetector);
		LibBlockIDs.idEnchanter = loadBlock(LibBlockNames.ENCHANTER, LibBlockIDs.idEnchanter);
		LibBlockIDs.idTurntable = loadBlock(LibBlockNames.TURNTABLE, LibBlockIDs.idTurntable);

		// Item IDs
		LibItemIDs.idLexicon = loadItem(LibItemNames.LEXICON, LibItemIDs.idLexicon);
		LibItemIDs.idPetal = loadItem(LibItemNames.PETAL, LibItemIDs.idPetal);
		LibItemIDs.idDye = loadItem(LibItemNames.DYE, LibItemIDs.idDye);
		LibItemIDs.idPestleAndMortar = loadItem(LibItemNames.PESTLE_AND_MORTAR, LibItemIDs.idPestleAndMortar);
		LibItemIDs.idTwigWand = loadItem(LibItemNames.TWIG_WAND, LibItemIDs.idTwigWand);
		LibItemIDs.idManaResource = loadItem(LibItemNames.MANA_RESOURCE, LibItemIDs.idManaResource);
		LibItemIDs.idLens = loadItem(LibItemNames.LENS, LibItemIDs.idLens);
		LibItemIDs.idManaPetal = loadItem(LibItemNames.MANA_PETAL, LibItemIDs.idManaPetal);
		LibItemIDs.idRune = loadItem(LibItemNames.RUNE, LibItemIDs.idRune);
		LibItemIDs.idSignalFlare = loadItem(LibItemNames.SIGNAL_FLARE, LibItemIDs.idSignalFlare);
		LibItemIDs.idManaTablet = loadItem(LibItemNames.MANA_TABLET, LibItemIDs.idManaTablet);
		LibItemIDs.idManaGun = loadItem(LibItemNames.MANA_GUN, LibItemIDs.idManaGun);
		LibItemIDs.idManaCookie = loadItem(LibItemNames.MANA_COOKIE, LibItemIDs.idManaCookie);

		config.save();
	}

	private static int loadItem(String label, int defaultID) {
		return config.getItem(label, defaultID).getInt(defaultID);
	}

	private static int loadBlock(String label, int defaultID) {
		return config.getBlock(label, defaultID).getInt(defaultID);
	}
}
