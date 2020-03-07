/*
 * This class is distributed as part of the Botania Mod.
 * Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 *
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 */
package vazkii.botania.api.corporea;

public class CorporeaRequest {
	public final ICorporeaRequestMatcher matcher;
	public int count;
	public int foundItems = 0;
	public int extractedItems = 0;

	public CorporeaRequest(ICorporeaRequestMatcher matcher, int count) {
		super();
		this.matcher = matcher;
		this.count = count;
	}

}
