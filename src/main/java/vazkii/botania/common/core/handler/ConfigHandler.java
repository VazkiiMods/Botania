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

import net.minecraftforge.common.ForgeConfigSpec;
import org.apache.commons.lang3.tuple.Pair;

public final class ConfigHandler {

	public static class Client {
		public final ForgeConfigSpec.BooleanValue useShaders;
		public final ForgeConfigSpec.BooleanValue lexiconRotatingItems;
		public final ForgeConfigSpec.BooleanValue lexiconJustifiedText;
		public final ForgeConfigSpec.BooleanValue subtlePowerSystem;
		public final ForgeConfigSpec.BooleanValue staticWandBeam;
		public final ForgeConfigSpec.BooleanValue boundBlockWireframe;
		public final ForgeConfigSpec.BooleanValue lexicon3dModel;
		public final ForgeConfigSpec.DoubleValue flowerParticleFrequency;
		public final ForgeConfigSpec.BooleanValue elfPortalParticlesEnabled;
		public final ForgeConfigSpec.BooleanValue renderAccessories;
		public final ForgeConfigSpec.BooleanValue enableSeasonalFeatures;
		public final ForgeConfigSpec.BooleanValue enableArmorModels;
		public final ForgeConfigSpec.BooleanValue enableFancySkybox;
		public final ForgeConfigSpec.BooleanValue enableFancySkyboxInNormalWorlds;

		public final ForgeConfigSpec.IntValue manaBarHeight;
		public final ForgeConfigSpec.IntValue flightBarHeight;
		public final ForgeConfigSpec.IntValue flightBarBreathHeight;
		public final ForgeConfigSpec.IntValue glSecondaryTextureUnit;

		public final ForgeConfigSpec.BooleanValue staticFloaters;
		public final ForgeConfigSpec.BooleanValue debugInfo;
		public final ForgeConfigSpec.BooleanValue matrixMode;
		public final ForgeConfigSpec.BooleanValue referencesEnabled;

		public Client(ForgeConfigSpec.Builder builder) {
			useShaders = builder
					.comment("Set this to false to disable the use of shaders for some of the mod's renders.")
					.define("shaders.enabled", true);
			lexiconRotatingItems = builder
					.comment("Set this to false to disable the rotating items in the petal and rune entries in the Lexica Botania.")
					.define("lexicon.rotating_items", true);
			lexiconJustifiedText = builder
					.comment("Set this to true to enable justified text in the Lexica Botania's text pages.")
					.define("lexicon.justified_text", false);
			subtlePowerSystem = builder
					.comment("Set this to true to set the power system's particles to be a lot more subtle. Good for low-end systems, if the particles are causing lag.")
					.define("subtile_power_system", false);
			staticWandBeam = builder
					.comment("Set this to true to use a static wand beam that shows every single position of the burst, similar to the way it used to work on old Botania versions. Warning: Disabled by default because it may be laggy.")
					.define("static_wand_beam", false);
			boundBlockWireframe = builder
					.comment("Set this to false to disable the wireframe when looking a block bound to something (spreaders, flowers, etc).")
					.define("bound_block_wireframe", true);
			lexicon3dModel = builder
					.comment("Set this to false to disable the animated 3D render for the Lexica Botania.")
					.define("lexicon.render_3d", true);
			flowerParticleFrequency = builder
					.comment("The frequency in which particles spawn from normal (worldgen) mystical flowers")
					.defineInRange("particles.flower_frequency", 0.75, Double.MIN_VALUE, Double.MAX_VALUE);
			elfPortalParticlesEnabled = builder
					.comment("Set this to false to disable the particles in the elven portal.")
					.define("particles.elf_portal", true);
			renderAccessories = builder
					.comment("Set this to false to disable rendering of baubles in the player.")
					.define("render_baubles", true);
			enableSeasonalFeatures = builder
					.comment("Set this to false to disable seasonal features, such as halloween and christmas.")
					.define("seasonal_features", true);
			enableArmorModels = builder
					.comment("Set this to false to disable custom armor models")
					.define("armor_models", true);
			enableFancySkybox = builder
					.comment("Set this to false to disable the fancy skybox in Garden of Glass")
					.define("fancy_skybox.enabled", true);
			enableFancySkyboxInNormalWorlds = builder
					.comment("Set this to true to enable the fancy skybox in non Garden of Glass worlds. (Does not require Garden of Glass loaded to use, needs 'fancy_skybox.enabled' to be true as well)")
					.define("fancy_skybox.normal_worlds", false);
			manaBarHeight = builder
					.comment("The height of the mana display bar in above the XP bar. You can change this if you have a mod that changes where the XP bar is.")
					.defineInRange("hud.mana_bar.height", 29, 0, Integer.MAX_VALUE);
			flightBarHeight = builder
					.comment("The height of the Flugel Tiara flight bar. You can change this if you have a mod that adds a bar in that spot.")
					.defineInRange("hud.flight_bar.height", 49, 0, Integer.MAX_VALUE);
			flightBarBreathHeight = builder
					.comment("The height of the Flugel Tiara flight bar if your breath bar is shown. You can change this if you have a mod that adds a bar in that spot.")
					.defineInRange("hud.flight_bar_breath.height", 59, 0, Integer.MAX_VALUE);
			glSecondaryTextureUnit = builder
					.comment("The GL Texture Unit to use for the secondary sampler passed in to the Lexica Botania's category button shader. DO NOT TOUCH THIS IF YOU DON'T KNOW WHAT YOU'RE DOING")
					.defineInRange("shaders.secondary_unit", 7, 0, Integer.MAX_VALUE);
			staticFloaters = builder
					.comment("Set this to true if you use lots of floating flowers and are experiencing rendering lag. Will disable the floating flowers' animations and render them statically for a major performance boost. Hit F3+A in-world after toggling this.")
					.define("static_floaters", false);
			debugInfo = builder
					.comment("Set to false to disable Botania's messages in the F3 debug screen")
					.define("debug_info", true);
			matrixMode = builder
					.comment("Set this to true if you are the chosen one. For lovers of glitch art and just general mad people.")
					.define("matrix_mode", false);
			referencesEnabled = builder
					.comment("Set this to false to disable the references in the flower tooltips. (You monster D:)")
					.define("references", true);
		}
	}

	public static final Client CLIENT;
	public static final ForgeConfigSpec CLIENT_SPEC;
	static {
		final Pair<Client, ForgeConfigSpec> specPair = new ForgeConfigSpec.Builder().configure(Client::new);
		CLIENT_SPEC = specPair.getRight();
		CLIENT = specPair.getLeft();
	}

	public static class Common {
		public final ForgeConfigSpec.BooleanValue blockBreakParticles;
		public final ForgeConfigSpec.BooleanValue blockBreakParticlesTool;
		public final ForgeConfigSpec.BooleanValue chargingAnimationEnabled;
		public final ForgeConfigSpec.BooleanValue silentSpreaders;
		public final ForgeConfigSpec.IntValue spreaderTraceTime;
		public final ForgeConfigSpec.BooleanValue flowerForceCheck;
		public final ForgeConfigSpec.BooleanValue enderPickpocketEnabled;

		public final ForgeConfigSpec.BooleanValue fallenKanadeEnabled;
		public final ForgeConfigSpec.BooleanValue enchanterEnabled;
		public final ForgeConfigSpec.BooleanValue fluxfieldEnabled;
		public final ForgeConfigSpec.BooleanValue relicsEnabled;
		public final ForgeConfigSpec.BooleanValue ringOfOdinFireResist;
		public final ForgeConfigSpec.BooleanValue invertMagnetRing;
		public final ForgeConfigSpec.BooleanValue enableThaumcraftStablizers;
		public final ForgeConfigSpec.BooleanValue enableThaumcraftAspects;

		public final ForgeConfigSpec.IntValue harvestLevelWeight;
		public final ForgeConfigSpec.IntValue harvestLevelBore;

		public final ForgeConfigSpec.IntValue flowerQuantity;
		public final ForgeConfigSpec.IntValue flowerDensity;
		public final ForgeConfigSpec.IntValue flowerPatchSize;
		public final ForgeConfigSpec.IntValue flowerPatchChance;
		public final ForgeConfigSpec.DoubleValue flowerTallChance;
		public final ForgeConfigSpec.IntValue mushroomQuantity;
		
		public final ForgeConfigSpec.BooleanValue gogSpawnWithLexicon; 

		public Common(ForgeConfigSpec.Builder builder) {
			blockBreakParticles = builder
					.comment("Set this to false to remove the block breaking particles from the flowers and other items in the mod.")
					.define("blockBreakingParticles.enabled", true);
			blockBreakParticlesTool = builder
					.comment("Set this to false to remove the block breaking particles from the Mana Shatterer, as there can be a good amount in higher levels.")
					.define("blockBreakingParticlesTool.enabled", true);
			chargingAnimationEnabled = builder
					.comment("Set this to false to disable the animation when an item is charging on top of a mana pool")
					.define("chargeAnimation.enabled", true);
			silentSpreaders = builder
					.comment("Set this to true to disable the mana spreader shooting sound")
					.define("manaSpreaders.silent", false);
			spreaderTraceTime = builder
					.comment("How many ticks into the future will mana spreaders attempt to predict where mana bursts go? Setting this lower will improve spreader performance, but will cause them to not fire at targets that are too far away.")
					.defineInRange("spreader.traceTime", 400, 1, Integer.MAX_VALUE);
			flowerForceCheck = builder
					.comment("Turn this off ONLY IF you're on an extremely large world with an exaggerated count of Mana Spreaders/Mana Pools and are experiencing TPS lag. This toggles whether flowers are strict with their checking for connecting to pools/spreaders or just check whenever possible.")
					.define("flower.forceCheck", true);
			enderPickpocketEnabled = builder
					.comment("Set to false to disable the ability for the Hand of Ender to pickpocket other players' ender chests")
					.define("enderPickpocket.enabled", true);
			fallenKanadeEnabled = builder
					.comment("Set this to false to disable the Fallen Kanade flower (gives Regeneration). This config option is here for those using Blood Magic. Note: Turning this off will not remove ones already in the world, it'll simply prevent the crafting.")
					.define("fallenKanade.enabled", true);
			enchanterEnabled = builder
					.comment("Set this to false to disable the Mana Enchanter. Since some people find it OP or something. This only disables the entry and creation. Old ones that are already in the world will stay.")
					.define("manaEnchanter.enabled", true);
			fluxfieldEnabled = builder
					.comment("Set this to false to disable the Mana Fluxfield (generates FE from mana). This only disables the entry and creation. Old ones that are already in the world will stay.")
					.define("manaFluxfield.enabled", true);
			relicsEnabled = builder
					.comment("Set this to false to disable the Relic System. This only disables the entries, drops and achievements. Old ones that are already in the world will stay.")
					.define("relics.enabled", true);
			ringOfOdinFireResist = builder
					.comment("Set this to false to make the Ring of Odin not apply fire resistance. Mostly for people who use Witchery transformations.")
					.define("ringOfOdin.fireResist", true);
			invertMagnetRing = builder
					.comment("Set this to true to invert the Ring of Magnetization's controls (from shift to stop to shift to work)")
					.define("magnetRing.invert", false);
			enableThaumcraftStablizers = builder
					.comment("Set this to false to disable Thaumcraft Infusion Stabilizing in botania blocks")
					.define("thaumraftStabilizers.enabled", true);
			enableThaumcraftAspects = builder
					.comment("Set this to false to disable Thaumcraft aspects on Botania items")
					.define("thaumcraftAspects.enabled", true);
			harvestLevelWeight = builder
					.comment("The harvest level of the Mana Lens: Weight. 3 is diamond level. Defaults to 2 (iron level)")
					.defineInRange("harvestLevel.weightLens", 2, 0, Integer.MAX_VALUE);
			harvestLevelBore = builder
					.comment("The harvest level of the Mana Lens: Bore. 3 is diamond level. Defaults to 3")
					.defineInRange("harvestLevel.boreLens", 3, 0, Integer.MAX_VALUE);
			flowerQuantity = builder
					.comment("The quantity of Botania flower patches to generate in the world, defaults to 2, the lower the number the less patches generate.")
					.defineInRange("worldgen.flower.quantity", 2, 0, Integer.MAX_VALUE);
			flowerDensity = builder
					.comment("The density of each Botania flower patch generated, defaults to 2, the lower the number, the less each patch will have.")
					.defineInRange("worldgen.flower.density", 2, 0, Integer.MAX_VALUE);
			flowerPatchSize = builder
					.comment("The size of each Botania flower patch, defaults to 6. The larger this is the farther the each patch can spread")
					.defineInRange("worldgen.flower.patchSize", 6, 0, Integer.MAX_VALUE);
			flowerPatchChance = builder
					.comment("The inverse chance for a Botania flower patch to be generated, defaults to 16. The higher this value is the less patches will exist and the more flower each will have.")
					.defineInRange("worldgen.flower.patchChance", 16, 0, Integer.MAX_VALUE);
			flowerTallChance = builder
					.comment("The chance for a Botania flower generated in a patch to be a tall flower. 0.1 is 10%, 1 is 100%. Defaults to 0.05")
					.defineInRange("worldgen.flower.tallChance", 0.05, 0, 1);
			mushroomQuantity = builder
					.comment("The quantity of Botania mushrooms to generate underground, in the world, defaults to 40, the lower the number the less patches generate.")
					.defineInRange("worldgen.mushroom.quantity", 40, 0, Integer.MAX_VALUE);
			gogSpawnWithLexicon = builder
					.comment("Set this to false to disable spawning with a Lexica Botania in Garden of Glass worlds, if you are modifying the modpack's progression to not start with Botania.")
					.define("gardenOfGlass.spawnWithLexicon", true);
		}
	}

	public static final Common COMMON;
	public static final ForgeConfigSpec COMMON_SPEC;
	static {
		final Pair<Common, ForgeConfigSpec> specPair = new ForgeConfigSpec.Builder().configure(Common::new);
		COMMON_SPEC = specPair.getRight();
		COMMON = specPair.getLeft();
	}
}
