/**
 * This class was created by <Vazkii>. It's distributed as
 * part of the Botania Mod. Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 * 
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 * 
 * File Created @ [Nov 2, 2014, 10:37:12 PM (GMT)]
 */
package vazkii.botania.common.brew;

import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;

public class BrewModPotion extends BrewMod {

	public BrewModPotion(String key, int cost, PotionEffect... effects) {
		super(key, Potion.potionTypes[effects[0].getPotionID()].getLiquidColor(), cost, effects);
	}

}
