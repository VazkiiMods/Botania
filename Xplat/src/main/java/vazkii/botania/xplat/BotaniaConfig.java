/*
 * This class is distributed as part of the Botania Mod.
 * Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 *
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 */

package vazkii.botania.xplat;

import org.jetbrains.annotations.Nullable;
import org.jetbrains.annotations.UnknownNullability;

import vazkii.botania.api.BotaniaAPI;
import vazkii.patchouli.api.PatchouliAPI;

import java.util.List;

public class BotaniaConfig {
	public interface ConfigAccess {
		boolean blockBreakParticles();
		boolean blockBreakParticlesTool();
		boolean chargingAnimationEnabled();
		boolean silentSpreaders();
		int spreaderTraceTime();
		boolean enderPickpocketEnabled();
		boolean enchanterEnabled();
		boolean relicsEnabled();
		boolean invertMagnetRing();
		int harvestLevelWeight();
		int harvestLevelBore();
		boolean gogSpawnWithLexicon();
		int gogIslandScaleMultiplier();
		List<String> rannuncarpusIgnoredItems();
		List<String> rannuncarpusExcludedMods();
	}

	public interface ClientConfigAccess {
		boolean lexiconRotatingItems();
		boolean subtlePowerSystem();
		boolean staticWandBeam();
		boolean boundBlockWireframe();
		boolean lexicon3dModel();
		double flowerParticleFrequency();
		boolean elfPortalParticlesEnabled();
		boolean renderAccessories();
		boolean enableSeasonalFeatures();
		boolean enableFancySkybox();
		boolean enableFancySkyboxInNormalWorlds();
		int manaBarHeight();
		boolean staticFloaters();
		boolean debugInfo();
		boolean referencesEnabled();
		boolean splashesEnabled();
		boolean useShaders();
	}

	@Nullable
	private static ConfigAccess config = null;
	@Nullable
	private static ClientConfigAccess clientConfig = null;

	@UnknownNullability
	public static ConfigAccess common() {
		return config;
	}

	@UnknownNullability
	public static ClientConfigAccess client() {
		return clientConfig;
	}

	public static void setCommon(ConfigAccess access) {
		if (config != null) {
			BotaniaAPI.LOGGER.warn("ConfigAccess was replaced! Old {} New {}",
					config.getClass().getName(), access.getClass().getName());
		}
		config = access;
	}

	public static void setClient(ClientConfigAccess access) {
		if (clientConfig != null) {
			BotaniaAPI.LOGGER.warn("ClientConfigAccess was replaced! Old {} New {}",
					clientConfig.getClass().getName(), access.getClass().getName());
		}
		clientConfig = access;
	}

	public static void resetPatchouliFlags() {
		PatchouliAPI.get().setConfigFlag("botania:relics", common().relicsEnabled());
		PatchouliAPI.get().setConfigFlag("botania:mana_enchanter", common().enchanterEnabled());
		PatchouliAPI.get().setConfigFlag("botania:ender_hand_pickpocket", common().enderPickpocketEnabled());
	}
}
