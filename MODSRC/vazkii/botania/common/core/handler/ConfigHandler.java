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

	public static boolean useShaders = true;
	public static boolean lexiconRotatingItems = true;
	public static boolean subtlePowerSystem = false;
	public static boolean staticWandBeam = false;
	public static boolean boundBlockWireframe = true;
	public static boolean lexicon3dModel = true;
	public static boolean oldPylonModel = false;
	public static double flowerParticleFrequency = 0.75F;
	public static boolean blockBreakParticles = true;
	public static boolean blockBreakParticlesTool = true;

	public static boolean versionCheckEnabled = true;
	public static int spreaderPositionShift = 1;

	public static int flowerQuantity = 3;
	public static int flowerDensity = 32;

	public static void loadConfig(File configFile) {
		config = new Configuration(configFile);

		config.load();
		String desc;

		desc = "Set to false to disable the use of shaders for some of the mod's renders.";
		useShaders = loadPropBool("shaders.enabled", desc, useShaders);

		desc = "Set to false to disable the rotating items in the petal and rune entries in the Lexica Botania.";
		lexiconRotatingItems = loadPropBool("lexicon.enable.rotatingItems", desc, lexiconRotatingItems);

		desc = "Set to true to set the power system's particles to be a lot more subtle. Good for low-end systems, if the particles are causing lag.";
		subtlePowerSystem = loadPropBool("powerSystem.subtle", desc, subtlePowerSystem);

		desc = "Set to true to use a static wand beam that shows every single position of the burst, similar to the way it used to work on old botania versions. Warning: Disabled by default because it may be laggy.";
		staticWandBeam = loadPropBool("wandBeam.static", desc, staticWandBeam);

		desc = "Set to false to disable the wireframe when looking a block bound to something (spreaders, flowers, etc).";
		boundBlockWireframe = loadPropBool("boundBlock.wireframe.enabled", desc, boundBlockWireframe);

		desc = "Set to false to disabled the animated 3D render for the lexica botania";
		lexicon3dModel = loadPropBool("lexicon.render.3D", desc, lexicon3dModel);

		desc = "Set to true to use the old (non-.obj, pre beta18) pylon model";
		oldPylonModel = loadPropBool("pylonModel.old", desc, oldPylonModel);

		desc = "The frequency in which particles spawn from normal (worldgen) mystical flowers";
		flowerParticleFrequency = loadPropDouble("flowerParticles.frequency", desc, flowerParticleFrequency);

		desc = "Set to false to remove the block breaking particles from the flowers and other items in the mod.";
		blockBreakParticles = loadPropBool("blockBreakingParticles.enabled", desc, blockBreakParticles);

		desc = "Set to false to remove the block breaking particles from the Mana Shatterer, as there can be a good amount in higher levels.";
		blockBreakParticlesTool = loadPropBool("blockBreakingParticlesTool.enabled", desc, blockBreakParticlesTool);

		desc = "Set to false to disable checking and alerting when new Botania versions come out.";
		versionCheckEnabled = loadPropBool("versionChecking.enabled", desc, versionCheckEnabled);
		
		desc = "Do not ever touch this value if not asked to. Possible symptoms of doing so include your head turning backwards, the appearance of Titans near the walls or you being trapped in a game of Sword Art Online.";
		spreaderPositionShift = loadPropInt("spreader.posShift", desc, spreaderPositionShift);

		flowerQuantity = loadPropInt("worldgen.flower.quantity", null, 3);
		flowerDensity = loadPropInt("worldgen.flower.density", null, 32);

		config.save();
	}

	public static int loadPropInt(String propName, String desc, int default_) {
		Property prop = config.get(Configuration.CATEGORY_GENERAL, propName, default_);
		prop.comment = desc;
		return prop.getInt(default_);
	}

	public static double loadPropDouble(String propName, String desc, double default_) {
		Property prop = config.get(Configuration.CATEGORY_GENERAL, propName, default_);
		prop.comment = desc;
		return prop.getDouble(default_);
	}

	public static boolean loadPropBool(String propName, String desc, boolean default_) {
		Property prop = config.get(Configuration.CATEGORY_GENERAL, propName, default_);
		prop.comment = desc;
		return prop.getBoolean(default_);
	}
}
