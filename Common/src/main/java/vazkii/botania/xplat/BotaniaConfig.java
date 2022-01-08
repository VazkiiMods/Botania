package vazkii.botania.xplat;

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
		boolean fluxfieldEnabled();
		boolean relicsEnabled();
		boolean invertMagnetRing();
		int harvestLevelWeight();
		int harvestLevelBore();
		boolean gogSpawnWithLexicon();
		int gogIslandScaleMultiplier();
		boolean worldgenEnabled();
		List<String> rannuncarpusItemBlacklist();
		List<String> rannuncarpusModBlacklist();
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
	}

	private static ConfigAccess config = null;
	private static ClientConfigAccess clientConfig = null;

	public static ConfigAccess common() {
		return config;
	}

	public static ClientConfigAccess client() {
		return clientConfig;
	}

	public static void setCommon(ConfigAccess access) {
		if (config != null) {
			throw new IllegalStateException("ConfigAccess was already set to "
					+ config.getClass().getName());
		}
		config = access;
	}

	public static void setClient(ClientConfigAccess access) {
		if (clientConfig != null) {
			throw new IllegalStateException("ClientConfigAccess was already set to "
					+ clientConfig.getClass().getName());
		}
		clientConfig = access;
	}
}
