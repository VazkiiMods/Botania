/*
 * This class is distributed as part of the Botania Mod.
 * Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 *
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 */
package vazkii.botania.common.impl.corporea;

import vazkii.botania.api.corporea.CorporeaRequest;
import vazkii.botania.api.corporea.CorporeaRequestMatcher;

public class CorporeaRequestImpl implements CorporeaRequest {
	private final CorporeaRequestMatcher matcher;
	private int stillNeeded;
	private int foundItems = 0;
	private int extractedItems = 0;

	public CorporeaRequestImpl(CorporeaRequestMatcher matcher, int stillNeeded) {
		this.matcher = matcher;
		this.stillNeeded = stillNeeded;
	}

	@Override
	public CorporeaRequestMatcher getMatcher() {
		return matcher;
	}

	@Override
	public int getStillNeeded() {
		return stillNeeded;
	}

	@Override
	public int getFound() {
		return foundItems;
	}

	@Override
	public int getExtracted() {
		return extractedItems;
	}

	@Override
	public void trackSatisfied(int count) {
		if (stillNeeded != -1) {
			stillNeeded -= count;
		}
	}

	@Override
	public void trackFound(int count) {
		foundItems += count;
	}

	@Override
	public void trackExtracted(int count) {
		extractedItems += count;
	}
}
