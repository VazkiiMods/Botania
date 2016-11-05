/**
 * This class was created by <Vazkii>. It's distributed as
 * part of the Botania Mod. Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 *
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 *
 * File Created @ [Nov 1, 2014, 7:00:29 PM (GMT)]
 */
package vazkii.botania.common.brew;

import net.minecraft.potion.Potion;
import vazkii.botania.common.brew.potion.PotionAllure;
import vazkii.botania.common.brew.potion.PotionBloodthirst;
import vazkii.botania.common.brew.potion.PotionClear;
import vazkii.botania.common.brew.potion.PotionEmptiness;
import vazkii.botania.common.brew.potion.PotionFeatherfeet;
import vazkii.botania.common.brew.potion.PotionSoulCross;

public class ModPotions {

	public static Potion soulCross;
	public static Potion featherfeet;
	public static Potion emptiness;
	public static Potion bloodthrst;
	public static Potion allure;
	public static Potion clear;

	public static void init() {
		soulCross = new PotionSoulCross();
		featherfeet = new PotionFeatherfeet();
		emptiness = new PotionEmptiness();
		bloodthrst = new PotionBloodthirst();
		allure = new PotionAllure();
		clear = new PotionClear();
	}

}
