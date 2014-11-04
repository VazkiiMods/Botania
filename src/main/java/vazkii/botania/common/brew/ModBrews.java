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
import vazkii.botania.common.Botania;
import vazkii.botania.common.lib.LibBrewNames;

public class ModBrews {

	public static Brew speed;
	public static Brew strength;
	public static Brew haste;
	public static Brew healing;
	public static Brew jumpBoost;
	public static Brew regen;
	public static Brew resistance;
	public static Brew fireResistance;
	public static Brew waterBreathing;
	public static Brew invisibility;
	public static Brew nightVision;
	public static Brew absorption;

	public static Brew soulCross;
	public static Brew featherfeet;
	public static Brew emptiness;
	public static Brew fulfilment;
	public static Brew livingReactor;
	public static Brew overload;
	public static Brew manashock;
	public static Brew gravitation;
	public static Brew cleansing;

	public static Brew warpWard;

	public static void init() {
		speed = new BrewMod(LibBrewNames.SPEED, 0x59B7FF, 4000, new PotionEffect(Potion.moveSpeed.id, 1800, 1));
		strength = new BrewMod(LibBrewNames.STRENGTH, 0xEE3F3F, 4000, new PotionEffect(Potion.damageBoost.id, 1800, 1));
		haste = new BrewMod(LibBrewNames.HASTE, 0xF4A432, 4000, new PotionEffect(Potion.digSpeed.id, 1800, 1));
		healing = new BrewMod(LibBrewNames.HEALING, 0xFF5ECC, 6000, new PotionEffect(Potion.heal.id, 1, 1));
		jumpBoost = new BrewMod(LibBrewNames.JUMP_BOOST, 0x32F46D, 4000, new PotionEffect(Potion.jump.id, 1800, 1));
		regen = new BrewMod(LibBrewNames.REGEN, 0xFD6488, 7000, new PotionEffect(Potion.regeneration.id, 500, 1));
		resistance = new BrewMod(LibBrewNames.RESISTANCE, 0xB44E17, 4000, new PotionEffect(Potion.resistance.id, 1800, 1));
		fireResistance = new BrewMod(LibBrewNames.FIRE_RESISTANCE, 0xF86900, 4000, new PotionEffect(Potion.fireResistance.id, 9600, 0));
		waterBreathing = new BrewMod(LibBrewNames.WATER_BREATHING, 0x84A7CF, 4000, new PotionEffect(Potion.waterBreathing.id, 9600, 0));
		invisibility = new BrewMod(LibBrewNames.INVISIBILITY, 0xAEAEAE, 4000, new PotionEffect(Potion.invisibility.id, 9600, 0));
		nightVision = new BrewMod(LibBrewNames.NIGHT_VISION, 0x7C4BEB, 4000, new PotionEffect(Potion.nightVision.id, 9600, 0));
		absorption = new BrewMod(LibBrewNames.ABSORPTION, 0xF2EB23, 7000, new PotionEffect(Potion.field_76444_x.id, 1800, 3));

		/*soulCross = new BrewModPotion(LibBrewNames.SOUL_CROSS, 10000, new PotionEffect(ModPotions.soulCross.id, 1800, 0));
		featherfeet = new BrewModPotion(LibBrewNames.FEATHER_FEET, 7000, new PotionEffect(ModPotions.featherfeet.id, 1800, 0));
		emptiness = new BrewModPotion(LibBrewNames.EMPTINESS, 30000, new PotionEffect(ModPotions.emptiness.id, 7200, 0));
		fulfilment = new BrewModPotion(LibBrewNames.FULFILMENT, 20000, new PotionEffect(ModPotions.fulfilment.id, 7200, 0));
		livingReactor = new BrewModPotion(LibBrewNames.LIVING_REACTOR, 9000, new PotionEffect(ModPotions.livingReactor.id, 200, 0));
		overload = new BrewMod(LibBrewNames.OVERLOAD, 0x232323, 12000, new PotionEffect(Potion.damageBoost.id, 1800, 3), new PotionEffect(Potion.moveSpeed.id, 1800, 2), new PotionEffect(Potion.weakness.id, 1800, 2), new PotionEffect(Potion.hunger.id, 200, 0));
		manashock = new BrewModPotion(LibBrewNames.MANASHOCK, 15000, new PotionEffect(ModPotions.manashock.id, 200, 0));
		gravitation = new BrewModPotion(LibBrewNames.MANASHOCK, 12000, new PotionEffect(ModPotions.gravitation.id, 1800, 0));
		cleansing = new BrewModPotion(LibBrewNames.CLEANSING, 16000, new PotionEffect(ModPotions.cleansing.id, 1200, 0));*/
	}

	public static void initTC() {
		Potion warpWardPotion = null;
		for(Potion potion : Potion.potionTypes)
			if(potion != null && potion.getName().equals("potion.warpward")) {
				warpWardPotion = potion;
				break;
			}

		if(warpWardPotion != null)
			warpWard = new BrewMod(LibBrewNames.WARP_WARD, 0xFBBDFF, 25000, new PotionEffect(warpWardPotion.id, 12000, 0));
	}
}
