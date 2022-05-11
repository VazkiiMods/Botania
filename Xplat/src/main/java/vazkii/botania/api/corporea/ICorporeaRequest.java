/*
 * This class is distributed as part of the Botania Mod.
 * Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 *
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 */
package vazkii.botania.api.corporea;

public interface ICorporeaRequest {

	ICorporeaRequestMatcher getMatcher();

	/**
	 * @return The amount still wanted by the request, -1 if the request wants everything matched by
	 *         {@link #getMatcher}.
	 */
	int getStillNeeded();

	int getFound();

	int getExtracted();

	/**
	 * Inform the request that {@code count} items have been satisfied,
	 * updating the result of {@link #getStillNeeded()}.
	 */
	void trackSatisfied(int count);

	void trackFound(int count);

	void trackExtracted(int count);

}
