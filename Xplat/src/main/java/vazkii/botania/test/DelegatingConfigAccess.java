package vazkii.botania.test;

import vazkii.botania.xplat.BotaniaConfig;

import java.util.List;

/**
 * Convenience class to allow mocking of one or more config values by overriding
 * the corresponding methods.
 */
public class DelegatingConfigAccess implements BotaniaConfig.ConfigAccess {
	private final BotaniaConfig.ConfigAccess inner;

	public DelegatingConfigAccess(BotaniaConfig.ConfigAccess inner) {
		this.inner = inner;
	}

	public BotaniaConfig.ConfigAccess getInner() {
		return inner;
	}

	@Override
	public boolean blockBreakParticles() {
		return inner.blockBreakParticles();
	}

	@Override
	public boolean blockBreakParticlesTool() {
		return inner.blockBreakParticlesTool();
	}

	@Override
	public boolean chargingAnimationEnabled() {
		return inner.chargingAnimationEnabled();
	}

	@Override
	public boolean silentSpreaders() {
		return inner.silentSpreaders();
	}

	@Override
	public int spreaderTraceTime() {
		return inner.spreaderTraceTime();
	}

	@Override
	public boolean enderPickpocketEnabled() {
		return inner.enderPickpocketEnabled();
	}

	@Override
	public boolean enchanterEnabled() {
		return inner.enchanterEnabled();
	}

	@Override
	public boolean relicsEnabled() {
		return inner.relicsEnabled();
	}

	@Override
	public boolean invertMagnetRing() {
		return inner.invertMagnetRing();
	}

	@Override
	public int harvestLevelWeight() {
		return inner.harvestLevelWeight();
	}

	@Override
	public int harvestLevelBore() {
		return inner.harvestLevelBore();
	}

	@Override
	public boolean gogSpawnWithLexicon() {
		return inner.gogSpawnWithLexicon();
	}

	@Override
	public int gogIslandScaleMultiplier() {
		return inner.gogIslandScaleMultiplier();
	}

	@Override
	public boolean worldgenEnabled() {
		return inner.worldgenEnabled();
	}

	@Override
	public List<String> rannuncarpusItemBlacklist() {
		return inner.rannuncarpusItemBlacklist();
	}

	@Override
	public List<String> rannuncarpusModBlacklist() {
		return inner.rannuncarpusModBlacklist();
	}
}
