/**
 * This class was created by <Vazkii>. It's distributed as
 * part of the Botania Mod. Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 * 
 * Botania is Open Source and distributed under a
 * Creative Commons Attribution-NonCommercial-ShareAlike 3.0 License
 * (http://creativecommons.org/licenses/by-nc-sa/3.0/deed.en_GB)
 * 
 * File Created @ [Nov 1, 2014, 7:00:33 PM (GMT)]
 */
package vazkii.botania.common.brew;

import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;
import vazkii.botania.api.brew.Brew;
import vazkii.botania.common.lib.LibBrewNames;

public class ModBrews {

	public static Brew speed;
	public static Brew strength;

	public static void init() {
		speed = new BrewMod(LibBrewNames.SPEED, 0x59B7FF, 2000, new PotionEffect(Potion.moveSpeed.id, 1800, 1));
		strength = new BrewMod(LibBrewNames.STRENGTH, 0XEE3F3F, 2000, new PotionEffect(Potion.damageBoost.id, 1800, 1));
	}
	
}
