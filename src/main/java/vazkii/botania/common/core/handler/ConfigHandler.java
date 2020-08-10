/*
 * This class is distributed as part of the Botania Mod.
 * Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 *
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 */
package vazkii.botania.common.core.handler;

import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.ForgeConfigSpec;

import org.apache.commons.lang3.tuple.Pair;

import java.util.Collections;
import java.util.List;

public final class ConfigHandler {

	public static class Client {
		public final ForgeConfigSpec.BooleanValue useShaders;
		public final ForgeConfigSpec.BooleanValue lexiconRotatingItems;
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

		public final ForgeConfigSpec.BooleanValue staticFloaters;
		public final ForgeConfigSpec.BooleanValue debugInfo;
		public final ForgeConfigSpec.BooleanValue referencesEnabled;
		public final ForgeConfigSpec.BooleanValue splashesEnabled;

		public Client(ForgeConfigSpec.Builder builder) {
			builder.push("rendering");
			useShaders = builder
					.comment("Set this to false to disable the use of shaders for some of the mod's renders. (Requires game restart)")
					.define("shaders", true);
			boundBlockWireframe = builder
					.comment("Set this to false to disable the wireframe when looking a block bound to something (spreaders, flowers, etc).")
					.define("boundBlockWireframe", true);
			renderAccessories = builder
					.comment("Set this to false to disable rendering of accessories in the player.")
					.define("accessories", true);
			enableArmorModels = builder
					.comment("Set this to false to disable custom armor models")
					.define("armorModels", true);
			manaBarHeight = builder
					.comment("The height of the mana display bar in above the XP bar. You can change this if you have a mod that changes where the XP bar is.")
					.defineInRange("manaBarHeight", 29, 0, Integer.MAX_VALUE);
			staticFloaters = builder
					.comment("Set this to true if you use lots of floating flowers and are experiencing rendering lag. Will disable the floating flowers' animations and render them statically for a major performance boost. Hit F3+A in-world after toggling this.")
					.define("staticFloaters", false);

			builder.push("fancySkybox");
			enableFancySkybox = builder
					.comment("Set this to false to disable the fancy skybox in Garden of Glass")
					.define("enabled", true);
			enableFancySkyboxInNormalWorlds = builder
					.comment("Set this to true to enable the fancy skybox in non Garden of Glass worlds. (Does not require Garden of Glass loaded to use, needs 'fancySkybox.enabled' to be true as well)")
					.define("normalWorlds", false);
			builder.pop();

			builder.push("lexicon");
			lexiconRotatingItems = builder
					.comment("Set this to false to disable the rotating items in the petal and rune entries in the Lexica Botania.")
					.define("rotatingItems", true);
			lexicon3dModel = builder
					.comment("Set this to false to disable the animated 3D render for the Lexica Botania.")
					.define("render_3d", true);
			builder.pop(2);

			builder.push("particles");
			subtlePowerSystem = builder
					.comment("Set this to true to set the power system's particles to be a lot more subtle. Good for low-end systems, if the particles are causing lag.")
					.define("powerSystem", false);
			staticWandBeam = builder
					.comment("Set this to true to use a static wand beam that shows every single position of the burst, similar to the way it used to work on old Botania versions. Warning: Disabled by default because it may be laggy.")
					.define("staticWandBeam", false);
			flowerParticleFrequency = builder
					.comment("The frequency in which particles spawn from normal (worldgen) mystical flowers")
					.defineInRange("flowerFrequency", 0.75, Double.MIN_VALUE, Double.MAX_VALUE);
			elfPortalParticlesEnabled = builder
					.comment("Set this to false to disable the particles in the elven portal.")
					.define("elvenPortal", true);
			builder.pop();

			enableSeasonalFeatures = builder
					.comment("Set this to false to disable seasonal features, such as halloween and christmas.")
					.define("seasonalFeatures", true);
			debugInfo = builder
					.comment("Set to false to disable Botania's messages in the F3 debug screen")
					.define("debugInfo", true);
			referencesEnabled = builder
					.comment("Set this to false to disable the references in the flower tooltips. (You monster D:)")
					.define("references", true);
			splashesEnabled = builder
					.comment("Set this to false to disable Botania's splashes in the main menu.")
					.define("splashes", true);
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

		public final ForgeConfigSpec.BooleanValue enchanterEnabled;
		public final ForgeConfigSpec.BooleanValue fluxfieldEnabled;
		public final ForgeConfigSpec.BooleanValue relicsEnabled;
		public final ForgeConfigSpec.BooleanValue invertMagnetRing;

		public final ForgeConfigSpec.IntValue harvestLevelWeight;
		public final ForgeConfigSpec.IntValue harvestLevelBore;

		public final ForgeConfigSpec.IntValue flowerQuantity;
		public final ForgeConfigSpec.IntValue flowerDensity;
		public final ForgeConfigSpec.IntValue flowerPatchSize;
		public final ForgeConfigSpec.IntValue flowerPatchChance;
		public final ForgeConfigSpec.DoubleValue flowerTallChance;
		public final ForgeConfigSpec.IntValue mushroomQuantity;

		public final ForgeConfigSpec.BooleanValue gogSpawnWithLexicon;
		public final ForgeConfigSpec.IntValue gogIslandScaleMultiplier;

		public final ForgeConfigSpec.ConfigValue<List<? extends String>> orechidPriorityMods;

		public Common(ForgeConfigSpec.Builder builder) {
			builder.push("blockBreakingParticles");
			blockBreakParticles = builder
					.comment("Set this to false to remove the block breaking particles from the flowers and other items in the mod.")
					.define("enabled", true);
			blockBreakParticlesTool = builder
					.comment("Set this to false to remove the block breaking particles from the Terra Shatterer, as there can be a good amount in higher levels.")
					.define("toolEnabled", true);
			builder.pop();

			builder.push("manaSpreaders");
			silentSpreaders = builder
					.comment("Set this to true to disable the mana spreader shooting sound")
					.define("silent", false);
			spreaderTraceTime = builder
					.comment("How many ticks into the future will mana spreaders attempt to predict where mana bursts go? Setting this lower will improve spreader performance, but will cause them to not fire at targets that are too far away.")
					.defineInRange("traceTime", 400, 1, Integer.MAX_VALUE);
			builder.pop();

			builder.push("harvestLevels");
			harvestLevelWeight = builder
					.comment("The harvest level of the Mana Lens: Weight. 3 is diamond level. Defaults to 2 (iron level)")
					.defineInRange("weightLens", 2, 0, Integer.MAX_VALUE);
			harvestLevelBore = builder
					.comment("The harvest level of the Mana Lens: Bore. 3 is diamond level. Defaults to 3")
					.defineInRange("boreLens", 3, 0, Integer.MAX_VALUE);
			builder.pop();

			builder.push("worldgen");
			flowerQuantity = builder
					.comment("The quantity of Botania flower patches to generate in the world, defaults to 2, the lower the number the less patches generate.")
					.defineInRange("flower.quantity", 2, 0, Integer.MAX_VALUE);
			flowerDensity = builder
					.comment("The density of each Botania flower patch generated, defaults to 2, the lower the number, the less each patch will have.")
					.defineInRange("flower.density", 2, 0, Integer.MAX_VALUE);
			flowerPatchSize = builder
					.comment("The size of each Botania flower patch, defaults to 6. The larger this is the farther the each patch can spread")
					.defineInRange("flower.patchSize", 6, 0, Integer.MAX_VALUE);
			flowerPatchChance = builder
					.comment("The inverse chance for a Botania flower patch to be generated, defaults to 16. The higher this value is the less patches will exist and the more flower each will have.")
					.defineInRange("flower.patchChance", 16, 0, Integer.MAX_VALUE);
			flowerTallChance = builder
					.comment("The chance for a Botania flower generated in a patch to be a tall flower. 0.1 is 10%, 1 is 100%. Defaults to 0.05")
					.defineInRange("flower.tallChance", 0.05, 0, 1);
			mushroomQuantity = builder
					.comment("The quantity of Botania mushrooms to generate underground, in the world, defaults to 40, the lower the number the less patches generate.")
					.defineInRange("mushroom.quantity", 40, 0, Integer.MAX_VALUE);
			builder.pop();

			chargingAnimationEnabled = builder
					.comment("Set this to false to disable the animation when an item is charging on top of a mana pool")
					.define("chargeAnimation", true);
			flowerForceCheck = builder
					.comment("Turn this off ONLY IF you're on an extremely large world with an exaggerated count of Mana Spreaders/Mana Pools and are experiencing TPS lag. This toggles whether flowers are strict with their checking for connecting to pools/spreaders or just check whenever possible.")
					.define("flowerBindingForceCheck", true);
			enderPickpocketEnabled = builder
					.comment("Set to false to disable the ability for the Hand of Ender to pickpocket other players' ender chests")
					.define("enderPickpocket", true);
			enchanterEnabled = builder
					.comment("Set this to false to disable the Mana Enchanter. Since some people find it OP or something. This only disables the entry and creation. Old ones that are already in the world will stay.")
					.define("manaEnchanter", true);
			fluxfieldEnabled = builder
					.comment("Set this to false to disable the Mana Fluxfield (generates FE from mana). This only disables the entry and creation. Old ones that are already in the world will stay.")
					.define("manaFluxfield", true);
			relicsEnabled = builder
					.comment("Set this to false to disable the Relic System. This only disables the entries, drops and achievements. Old ones that are already in the world will stay.")
					.define("relics", true);
			invertMagnetRing = builder
					.comment("Set this to true to invert the Ring of Magnetization's controls (from shift to stop to shift to work)")
					.define("invertMagnetRing", false);
			gogSpawnWithLexicon = builder
					.comment("Set this to false to disable spawning with a Lexica Botania in Garden of Glass worlds, if you are modifying the modpack's progression to not start with Botania.")
					.define("gardenOfGlass.spawnWithLexicon", true);
			gogIslandScaleMultiplier = builder
					.comment("The multiplier for island distances for multiplayer Garden of Glass worlds.\n" +
							"Islands are placed on a grid with 256 blocks between points, with the spawn island always being placed on 256, 256.\n" +
							"By default, the scale is 8, putting each island on points separated by 2048 blocks.\n" +
							"Values below 4 (1024 block spacing) are not recommended due to Nether portal collisions.")
					.defineInRange("gardenOfGlass.islandScaleMultiplier", 8, 1, 512);
			orechidPriorityMods = builder
					.comment("List of modids to prioritize when choosing a random ore from the tag.\n" +
							"By default, the chosen ore is randomly picked from all ores in the ore's tag.\n" +
							"Ores from mods present on this list will be picked over mods listed lower or not listed at all.")
					.defineList("orechidPriorityMods", Collections.emptyList(), s -> s instanceof String && ResourceLocation.isResouceNameValid(s + ":test"));
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
