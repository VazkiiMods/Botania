/**
 * This class was created by <Hubry>. It's distributed as
 * part of the Botania Mod. Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 *
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 *
 * File Created @ [Dec 23, 2019, 18:51]
 */
package vazkii.botania.common.capability;

import vazkii.botania.api.item.IExoflameHeatable;

public class NoopExoflameHeatable implements IExoflameHeatable {
	@Override
	public boolean canSmelt() {
		return false;
	}

	@Override
	public int getBurnTime() {
		return 0;
	}

	@Override
	public void boostBurnTime() {
	}

	@Override
	public void boostCookTime() {
	}
}
