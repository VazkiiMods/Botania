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

import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.common.config.Property;

public final class ConfigHandler {

	private static Configuration config;

	public static boolean lexiconRotatingItems = true;
	public static boolean subtlePowerSystem = false;
	public static boolean staticWandBeam = false;
	public static boolean boundBlockWireframe = true;
	public static boolean lexicon3dModel = true;

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

		Property boundBlockWireframeProp = config.get(Configuration.CATEGORY_GENERAL, "boundBlock.wireframe.enabled", true);
		boundBlockWireframeProp.comment = "Set to false to disable the wireframe when looking a block bound to something (spreaders, flowers, etc).";
		boundBlockWireframe = boundBlockWireframeProp.getBoolean(true);

		Property lexicon3dModelProp = config.get(Configuration.CATEGORY_GENERAL, "lexicon.render.3D", true);
		lexicon3dModelProp.comment = "Set to false to disabled the animated 3D render for the lexica botania";
		lexicon3dModel = lexicon3dModelProp.getBoolean(true);

		Property propFlowerQuantity = config.get(Configuration.CATEGORY_GENERAL, "worldgen.flower.quantity", 3);
		flowerQuantity = propFlowerQuantity.getInt(3);
		Property propFlowerDensity = config.get(Configuration.CATEGORY_GENERAL, "worldgen.flower.density", 32);
		flowerDensity = propFlowerDensity.getInt(32);

		
		config.save();
	}

}
