package vazkii.botania.common.impl.corporea;

import vazkii.botania.api.corporea.ICorporeaRequest;
import vazkii.botania.api.corporea.ICorporeaRequestMatcher;

public class CorporeaRequest implements ICorporeaRequest {
	private final ICorporeaRequestMatcher matcher;
	private int stillNeeded;
	private int foundItems = 0;
	private int extractedItems = 0;

	public CorporeaRequest(ICorporeaRequestMatcher matcher, int stillNeeded) {
		this.matcher = matcher;
		this.stillNeeded = stillNeeded;
	}

	@Override
	public ICorporeaRequestMatcher getMatcher() {
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
