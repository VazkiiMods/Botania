/**
 * This class was created by <Vazkii>. It's distributed as
 * part of the Botania Mod. Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 *
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 *
 * File Created @ [Mar 5, 2014, 12:59:27 AM (GMT)]
 */
package vazkii.botania.common.block.tile.mana;

import vazkii.botania.api.mana.IClientManaHandler;
import vazkii.botania.common.Botania;
import vazkii.botania.common.block.tile.TileMod;

public class TileManaVoid extends TileMod implements IClientManaHandler {

	@Override
	public int getCurrentMana() {
		return 0;
	}

	@Override
	public boolean isFull() {
		return false;
	}

	@Override
	public void recieveMana(int mana) {
		if(mana > 0)
			for(int i = 0; i < 10; i++)
				Botania.proxy.sparkleFX(pos.getX() + Math.random(), pos.getY() + Math.random(), pos.getZ() + Math.random(), 0.2F, 0.2F, 0.2F, 0.7F + 0.5F * (float) Math.random(), 5);
	}

	@Override
	public boolean canRecieveManaFromBursts() {
		return true;
	}

}
