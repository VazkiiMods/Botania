/**
 * This class was created by <Vazkii>. It's distributed as
 * part of the Botania Mod. Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 * 
 * Botania is Open Source and distributed under a
 * Creative Commons Attribution-NonCommercial-ShareAlike 3.0 License
 * (http://creativecommons.org/licenses/by-nc-sa/3.0/deed.en_GB)
 * 
 * File Created @ [Mar 5, 2014, 12:59:27 AM (GMT)]
 */
package vazkii.botania.common.block.tile.mana;

import vazkii.botania.api.mana.IManaReceiver;
import vazkii.botania.common.block.tile.TileMod;

public class TileManaVoid extends TileMod implements IManaReceiver {

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
		// All your mana is belong to the void
	}

	@Override
	public boolean canRecieveManaFromBursts() {
		return true;
	}

}
