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

import net.minecraft.potion.PotionEffect;
import net.minecraftforge.fml.common.registry.GameData;
import vazkii.botania.api.brew.PotionEffectShim;

public class BrewModPotion extends BrewMod {

	public BrewModPotion(String key, int cost, PotionEffectShim... effects) {
		super(key, effects[0].potion.getLiquidColor(), cost, effects);
	}

}
