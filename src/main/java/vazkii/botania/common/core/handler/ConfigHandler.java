/**
 * This class was created by <Vazkii>. It's distributed as
 * part of the Botania Mod. Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 *
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 *
 * File Created @ [Jan 13, 2014, 9:01:32 PM (GMT)]
 */
package vazkii.botania.common.core.handler;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.text.Style;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraft.util.text.TextFormatting;
import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.common.config.Property;
import net.minecraftforge.fml.client.event.ConfigChangedEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import vazkii.botania.common.Botania;
import vazkii.botania.common.lib.LibMisc;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public final class ConfigHandler {

	public static Configuration config;
	public static ConfigAdaptor adaptor;


	public static boolean useAdaptativeConfig = true;

	public static boolean useShaders = true;
	public static boolean lexiconRotatingItems = true;
	public static boolean lexiconJustifiedText = false;
	public static boolean subtlePowerSystem = false;
	public static boolean staticWandBeam = false;
	public static boolean boundBlockWireframe = true;
	public static boolean lexicon3dModel = true;
	public static double flowerParticleFrequency = 0.75F;
	public static boolean blockBreakParticles = true;
	public static boolean blockBreakParticlesTool = true;
	public static boolean elfPortalParticlesEnabled = true;
	public static boolean chargingAnimationEnabled = true;
	public static boolean useVanillaParticleLimiter = true;
	public static boolean silentSpreaders = false;
	public static boolean renderBaubles = true;
	public static boolean enableSeasonalFeatures = true;
	public static boolean useShiftForQuickLookup = false;
	public static boolean lexicaOfflineMode = false;
	public static boolean enableArmorModels = true;
	public static boolean enableFancySkybox = true;
	public static boolean enableFancySkyboxInNormalWorlds = false;

	public static int manaBarHeight = 29;
	public static int flightBarHeight = 49;
	public static int flightBarBreathHeight = 59;
	public static int glSecondaryTextureUnit = 7;

	public static boolean altFlowerTextures = false;
	public static boolean staticFloaters = false;
	public static boolean debugInfo = true;
	public static boolean matrixMode = false;
	public static boolean referencesEnabled = true;

//	public static boolean versionCheckEnabled = true;
	public static int spreaderPositionShift = 1;
	public static int spreaderTraceTime = 400;
	public static boolean flowerForceCheck = true;
	public static boolean enderPickpocketEnabled = true;

	public static boolean fallenKanadeEnabled = true;
	public static boolean darkQuartzEnabled = true;
	public static boolean enchanterEnabled = true;
	public static boolean fluxfieldEnabled = true;
	public static boolean relicsEnabled = true;
	public static boolean ringOfOdinFireResist = true;
	public static boolean invertMagnetRing = false;
	public static boolean enableThaumcraftStablizers = true;
	public static boolean enableThaumcraftAspects = true;
	public static boolean enableAlbedo = true;
	public static boolean enableShedding = false;

	public static int harvestLevelWeight = 2;
	public static int harvestLevelBore = 3;

	public static int flowerQuantity = 2;
	public static int flowerDensity = 2;
	public static int flowerPatchSize = 6;
	public static int flowerPatchChance = 16;
	public static double flowerTallChance = 0.05;
	public static int mushroomQuantity = 40;

	public static void loadConfig(File configFile) {
		config = new Configuration(configFile);

		config.load();
		load();
	}

	public static void load() {
		String desc;

		desc = "Set this to false to disable the Adaptative Config. Adaptative Config changes any default config values from old versions to the new defaults to make sure you aren't missing out on changes because of old configs. It will not touch any values that were changed manually.";
		useAdaptativeConfig = loadPropBool("adaptativeConfig.enabled", desc, useAdaptativeConfig);
		adaptor = new ConfigAdaptor(useAdaptativeConfig);

		desc = "Set this to false to disable the use of shaders for some of the mod's renders.";
		useShaders = loadPropBool("shaders.enabled", desc, useShaders);

		desc = "Set this to false to disable the rotating items in the petal and rune entries in the Lexica Botania.";
		lexiconRotatingItems = loadPropBool("lexicon.enable.rotatingItems", desc, lexiconRotatingItems);

		desc = "Set this to true to enable justified text in the Lexica Botania's text pages.";
		lexiconJustifiedText = loadPropBool("lexicon.enable.justifiedText", desc, lexiconJustifiedText);

		desc = "Set this to true to set the power system's particles to be a lot more subtle. Good for low-end systems, if the particles are causing lag.";
		subtlePowerSystem = loadPropBool("powerSystem.subtle", desc, subtlePowerSystem);

		desc = "Set this to true to use a static wand beam that shows every single position of the burst, similar to the way it used to work on old Botania versions. Warning: Disabled by default because it may be laggy.";
		staticWandBeam = loadPropBool("wandBeam.static", desc, staticWandBeam);

		desc = "Set this to false to disable the wireframe when looking a block bound to something (spreaders, flowers, etc).";
		boundBlockWireframe = loadPropBool("boundBlock.wireframe.enabled", desc, boundBlockWireframe);

		desc = "Set this to false to disable the animated 3D render for the Lexica Botania.";
		lexicon3dModel = loadPropBool("lexicon.render.3D", desc, lexicon3dModel);

		desc = "The frequency in which particles spawn from normal (worldgen) mystical flowers";
		flowerParticleFrequency = loadPropDouble("flowerParticles.frequency", desc, flowerParticleFrequency);

		desc = "Set this to false to remove the block breaking particles from the flowers and other items in the mod.";
		blockBreakParticles = loadPropBool("blockBreakingParticles.enabled", desc, blockBreakParticles);

		desc = "Set this to false to remove the block breaking particles from the Mana Shatterer, as there can be a good amount in higher levels.";
		blockBreakParticlesTool = loadPropBool("blockBreakingParticlesTool.enabled", desc, blockBreakParticlesTool);

		desc = "Set this to false to disable the particles in the elven portal.";
		elfPortalParticlesEnabled = loadPropBool("elfPortal.particles.enabled", desc, elfPortalParticlesEnabled);

		desc = "Set this to false to disable the animation when an item is charging on top of a mana pool.";
		chargingAnimationEnabled = loadPropBool("chargeAnimation.enabled", desc, chargingAnimationEnabled);

		desc = "Set this to false to always display all particles regardless of the \"Particles\" setting in the Vanilla options menu.";
		useVanillaParticleLimiter = loadPropBool("vanillaParticleConfig.enabled", desc, useVanillaParticleLimiter);

		desc = "Set this to true to disable the mana spreader shooting sound.";
		silentSpreaders = loadPropBool("manaSpreaders.silent", desc, silentSpreaders);

		desc = "Set this to false to disable rendering of baubles in the player.";
		renderBaubles = loadPropBool("baubleRender.enabled", desc, renderBaubles);

		desc = "Set this to false to disable seasonal features, such as halloween and christmas.";
		enableSeasonalFeatures = loadPropBool("seasonalFeatures.enabled", desc, enableSeasonalFeatures);

		desc = "Set this to true to use Shift instead of Ctrl for the inventory lexica botania quick lookup feature.";
		useShiftForQuickLookup = loadPropBool("quickLookup.useShift", desc, useShiftForQuickLookup);

		desc = "Set this to true to disable the wiki lookup feature of the lexica, for offline usage.";
		lexicaOfflineMode = loadPropBool("quickLookup.lexicaOfflineMode", desc, lexicaOfflineMode);

		desc = "Set this to false to disable custom armor models.";
		enableArmorModels = loadPropBool("armorModels.enable", desc, enableArmorModels);

		desc = "Set this to false to disable the fancy skybox in Garden of Glass.";
		enableFancySkybox = loadPropBool("fancySkybox.enable", desc, enableFancySkybox);

		desc = "Set this to true to enable the fancy skybox in non Garden of Glass worlds. (Does not require Garden of Glass loaded to use, needs 'fancySkybox.enable' to be true as well)";
		enableFancySkyboxInNormalWorlds = loadPropBool("fancySkybox.normalWorlds", desc, enableFancySkyboxInNormalWorlds);

		desc = "The height of the mana display bar in above the XP bar. You can change this if you have a mod that changes where the XP bar is.";
		manaBarHeight = loadPropInt("manaBar.height", desc, manaBarHeight);

		desc = "The height of the Flugel Tiara flight bar. You can change this if you have a mod that adds a bar in that spot.";
		flightBarHeight = loadPropInt("flightBar.height", desc, flightBarHeight);

		desc = "The height of the Flugel Tiara flight bar if your breath bar is shown. You can change this if you have a mod that adds a bar in that spot.";
		flightBarBreathHeight = loadPropInt("flightBarBreath.height", desc, flightBarBreathHeight);

		desc = "The GL Texture Unit to use for the secondary sampler passed in to the Lexica Botania's category button shader. DO NOT TOUCH THIS IF YOU DON'T KNOW WHAT YOU'RE DOING";
		glSecondaryTextureUnit = loadPropInt("shaders.secondaryUnit", desc, glSecondaryTextureUnit);

		desc = "Set this to true if you use lots of floating flowers and are experiencing rendering lag. Will disable the floating flowers' animations and render them statically for a major performance boost. Hit F3+A in-world after toggling this.";
		staticFloaters = loadPropBool("staticFloaters.enabled", desc, staticFloaters);

		desc = "Set to false to disable Botania's messages in the F3 debug screen";
		debugInfo = loadPropBool("debugInfo.enabled", desc, debugInfo);

		desc = "Set this to true if you are the chosen one. For lovers of glitch art and just general mad people.";
		matrixMode = loadPropBool("matrixMode.enabled", desc, matrixMode);

		desc = "Set this to false to disable the references in the flower tooltips. (You monster D:)";
		referencesEnabled = loadPropBool("references.enabled", desc, referencesEnabled);

//		desc = "Set this to false to disable checking and alerting when new Botania versions come out. (keywords for noobs: update notification message)";
//		versionCheckEnabled = loadPropBool("versionChecking.enabled", desc, versionCheckEnabled);

		desc = "Do not ever touch this value if not asked to. Possible symptoms of doing so include your head turning backwards, the appearance of Titans near the walls or you being trapped in a game of Sword Art Online.";
		spreaderPositionShift = loadPropInt("spreader.posShift", desc, spreaderPositionShift);

		desc = "How many ticks into the future will mana spreaders attempt to predict where mana bursts go? Setting this lower will improve spreader performance, but will cause them to not fire at targets that are too far away.";
		spreaderTraceTime = loadPropInt("spreader.traceTime", desc, spreaderTraceTime);

		desc = "Turn this off ONLY IF you're on an extremely large world with an exaggerated count of Mana Spreaders/Mana Pools and are experiencing TPS lag. This toggles whether flowers are strict with their checking for connecting to pools/spreaders or just check whenever possible.";
		flowerForceCheck = loadPropBool("flower.forceCheck", desc, flowerForceCheck);

		desc = "Set to false to disable the ability for the Hand of Ender to pickpocket other players' ender chests.";
		enderPickpocketEnabled = loadPropBool("enderPickpocket.enabled", desc, enderPickpocketEnabled);

		desc = "Set this to false to disable the Fallen Kanade flower (gives Regeneration). This config option is here for those using Blood Magic. Note: Turning this off will not remove ones already in the world, it'll simply prevent the crafting.";
		fallenKanadeEnabled = loadPropBool("fallenKanade.enabled", desc, fallenKanadeEnabled);

		desc = "Set this to false to disable the Smokey Quartz blocks. This config option is here for those using Thaumic Tinkerer";
		darkQuartzEnabled = loadPropBool("darkQuartz.enabled", desc, darkQuartzEnabled);

		desc = "Set this to false to disable the Mana Enchanter. Since some people find it OP or something. This only disables the entry and creation. Old ones that are already in the world will stay.";
		enchanterEnabled = loadPropBool("manaEnchanter.enabled", desc, enchanterEnabled);

		desc = "Set this to false to disable the Mana Fluxfield (generates RF from mana). This only disables the entry and creation. Old ones that are already in the world will stay.";
		fluxfieldEnabled = loadPropBool("manaFluxfield.enabled", desc, fluxfieldEnabled);

		desc = "Set this to false to disable the Relic System. This only disables the entries, drops and achievements. Old ones that are already in the world will stay.";
		relicsEnabled = loadPropBool("relics.enabled", desc, relicsEnabled);

		desc = "Set this to false to make the Ring of Odin not apply fire resistance. Mostly for people who use Witchery transformations.";
		ringOfOdinFireResist = loadPropBool("ringOfOdin.fireResist", desc, ringOfOdinFireResist);

		desc = "Set this to true to invert the Ring of Magnetization's controls (from shift to stop to shift to work)";
		invertMagnetRing = loadPropBool("magnetRing.invert", desc, invertMagnetRing);

		desc = "Set this to false to disable Thaumcraft Infusion Stabilizing in botania blocks";
		enableThaumcraftStablizers = loadPropBool("thaumraftStabilizers.enabled", desc, enableThaumcraftStablizers);

		desc = "Set this to false to disable Thaumcraft aspects on Botania items";
		enableThaumcraftAspects = loadPropBool("thaumcraftAspects.enabled", desc, enableThaumcraftAspects);

		desc = "Set this to false to disable Albedo compat for moving colored lights on some Botania entities";
		enableAlbedo = loadPropBool("albedoLights.enabled", desc, enableAlbedo);

		desc = "Set this to true to enable the Shedding feature from 1.7.10. You'll need to load the game to generate the options. No options are enabled by default. This increases load time.";
		enableShedding = loadPropBool("shedding.enable", desc, enableShedding);

		desc = "The harvest level of the Mana Lens: Weight. 3 is diamond level. Defaults to 2 (iron level)";
		harvestLevelWeight = loadPropInt("harvestLevel.weightLens", desc, harvestLevelWeight);

		desc = "The harvest level of the Mana Lens: Bore. 3 is diamond level. Defaults to 3";
		harvestLevelBore = loadPropInt("harvestLevel.boreLens", desc, harvestLevelBore);

		desc = "The quantity of Botania flower patches to generate in the world, defaults to 2, the lower the number the less patches generate.";
		flowerQuantity = loadPropInt("worldgen.flower.quantity", desc, flowerQuantity);

		desc = "The density of each Botania flower patch generated, defaults to 2, the lower the number, the less each patch will have.";
		adaptor.addMappingInt(0, "worldgen.flower.density", 16);
		adaptor.addMappingInt(238, "worldgen.flower.density", 2);
		flowerDensity = loadPropInt("worldgen.flower.density", desc, flowerDensity);

		desc = "The size of each Botania flower patch, defaults to 6. The larger this is the farther the each patch can spread";
		flowerPatchSize = loadPropInt("worldgen.flower.patchSize", desc, flowerPatchSize);

		desc = "The inverse chance for a Botania flower patch to be generated, defaults to 16. The higher this value is the less patches will exist and the more flower each will have.";
		adaptor.addMappingInt(0, "worldgen.flower.patchChance", 4);
		adaptor.addMappingInt(238, "worldgen.flower.patchChance", 16);
		flowerPatchChance = loadPropInt("worldgen.flower.patchChance", desc, flowerPatchChance);

		desc = "The chance for a Botania flower generated in a patch to be a tall flower. 0.1 is 10%, 1 is 100%. Defaults to 0.05";
		adaptor.addMappingDouble(0, "worldgen.flower.tallChance", 0.1);
		adaptor.addMappingDouble(238, "worldgen.flower.tallChance", 0.05);
		flowerTallChance = loadPropDouble("worldgen.flower.tallChance", desc, flowerTallChance);

		desc = "The quantity of Botania mushrooms to generate underground, in the world, defaults to 40, the lower the number the less patches generate.";
		mushroomQuantity = loadPropInt("worldgen.mushroom.quantity", desc, mushroomQuantity);

		if(config.hasChanged())
			config.save();
	}

	public static void loadPostInit() {
		if(enableShedding)
			SheddingHandler.loadFromConfig(config);

		if(config.hasChanged())
			config.save();
	}

	public static int loadPropInt(String propName, String desc, int default_) {
		Property prop = config.get(Configuration.CATEGORY_GENERAL, propName, default_);
		prop.setComment(desc);

		if(adaptor != null)
			adaptor.adaptPropertyInt(prop, prop.getInt(default_));

		return prop.getInt(default_);
	}

	public static double loadPropDouble(String propName, String desc, double default_) {
		Property prop = config.get(Configuration.CATEGORY_GENERAL, propName, default_);
		prop.setComment(desc);

		if(adaptor != null)
			adaptor.adaptPropertyDouble(prop, prop.getDouble(default_));

		return prop.getDouble(default_);
	}

	public static boolean loadPropBool(String propName, String desc, boolean default_) {
		Property prop = config.get(Configuration.CATEGORY_GENERAL, propName, default_);
		prop.setComment(desc);

		if(adaptor != null)
			adaptor.adaptPropertyBool(prop, prop.getBoolean(default_));

		return prop.getBoolean(default_);
	}

	public static class ConfigAdaptor {

		private boolean enabled;
		private int lastBuild;
		private int currentBuild;

		private final Map<String, List<AdaptableValue>> adaptableValues = new HashMap<>();
		private final List<String> changes = new ArrayList<>();

		public ConfigAdaptor(boolean enabled) {
			this.enabled = enabled;

			String lastVersion = Botania.proxy.getLastVersion();
			try {
				lastBuild = Integer.parseInt(lastVersion);
				currentBuild = Integer.parseInt(LibMisc.BUILD);
			} catch(NumberFormatException e) {
				this.enabled = false;
			}
		}

		public <T> void adaptProperty(Property prop, T val) {
			if(!enabled)
				return;

			String name = prop.getName();

			if(!adaptableValues.containsKey(name))
				return;

			AdaptableValue<T> bestValue = null;
			for(AdaptableValue<T> value : adaptableValues.get(name)) {
				if(value.version >= lastBuild) // If version is newer than what we last used we don't care about it
					continue;

				if(bestValue == null || value.version > bestValue.version)
					bestValue = value;
			}

			if(bestValue != null) {
				T expected = bestValue.value;
				T def = (T) prop.getDefault();

				if(areEqualNumbers(val, expected) && !areEqualNumbers(val, def)) {
					prop.setValue(def.toString());
					changes.add(" " + prop.getName() + ": " + val + " -> " + def);
				}
			}
		}

		public <T> void addMapping(int version, String key, T val) {
			if(!enabled)
				return;

			AdaptableValue<T> adapt = new AdaptableValue<>(version, val);
			if(!adaptableValues.containsKey(key)) {
				adaptableValues.put(key, new ArrayList<>());
			}

			List<AdaptableValue> list = adaptableValues.get(key);
			list.add(adapt);
		}

		public boolean areEqualNumbers(Object v1, Object v2) {
			double epsilon = 1.0E-6;
			float v1f = ((Number) v1).floatValue();
			float v2f;

			if(v2 instanceof String)
				v2f = Float.parseFloat((String) v2);
			else v2f = ((Number) v2).floatValue();

			return Math.abs(v1f - v2f) < epsilon;
		}

		public void tellChanges(EntityPlayer player) {
			if(changes.size() == 0)
				return;

			player.sendMessage(new TextComponentTranslation("botaniamisc.adaptativeConfigChanges").setStyle(new Style().setColor(TextFormatting.GOLD)));
			for(String change : changes)
				player.sendMessage(new TextComponentString(change).setStyle(new Style().setColor(TextFormatting.LIGHT_PURPLE)));
		}

		public void addMappingInt(int version, String key, int val) {
			this.addMapping(version, key, val);
		}

		public void addMappingDouble(int version, String key, double val) {
			this.addMapping(version, key, val);
		}

		public void addMappingBool(int version, String key, boolean val) {
			this.addMapping(version, key, val);
		}

		public void adaptPropertyInt(Property prop, int val) {
			this.adaptProperty(prop, val);
		}

		public void adaptPropertyDouble(Property prop, double val) {
			this.adaptProperty(prop, val);
		}

		public void adaptPropertyBool(Property prop, boolean val) {
			this.adaptProperty(prop, val);
		}

		public static class AdaptableValue<T> {

			public final int version;
			public final T value;
			public final Class<? extends T> valueType;

			public AdaptableValue(int version, T value) {
				this.version = version;
				this.value = value;
				valueType = (Class<? extends T>) value.getClass();
			}

		}

	}

	@Mod.EventBusSubscriber(modid = LibMisc.MOD_ID)
	public static class ChangeListener {

		@SubscribeEvent
		public static void onConfigChanged(ConfigChangedEvent.OnConfigChangedEvent eventArgs) {
			if(eventArgs.getModID().equals(LibMisc.MOD_ID))
				load();
		}

	}
}
