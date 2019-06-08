/**
 * This class was created by <Vazkii>. It's distributed as
 * part of the Botania Mod. Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 *
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 *
 * File Created @ [Nov 1, 2014, 7:00:33 PM (GMT)]
 */
package vazkii.botania.common.brew;

import net.minecraft.potion.Effect;
import net.minecraft.potion.EffectInstance;
import net.minecraft.potion.Effects;
import net.minecraft.potion.Effects;
import net.minecraft.potion.Effect;
import net.minecraft.potion.EffectInstance;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.registries.ForgeRegistries;
import vazkii.botania.api.brew.Brew;
import vazkii.botania.common.lib.LibBrewNames;

public class ModBrews {

	public static Brew speed;
	public static Brew strength;
	public static Brew haste;
	public static Brew healing;
	public static Brew jumpBoost;
	public static Brew regen;
	public static Brew regenWeak;
	public static Brew resistance;
	public static Brew fireResistance;
	public static Brew waterBreathing;
	public static Brew invisibility;
	public static Brew nightVision;
	public static Brew absorption;

	public static Brew allure;
	public static Brew soulCross;
	public static Brew featherfeet;
	public static Brew emptiness;
	public static Brew bloodthirst;
	public static Brew overload;
	public static Brew clear;

	public static Brew warpWard;

	public static void init() {
		speed = new BrewMod(LibBrewNames.SPEED, 0x59B7FF, 4000, new EffectInstance(Effects.SPEED, 1800, 1));
		strength = new BrewMod(LibBrewNames.STRENGTH, 0xEE3F3F, 4000, new EffectInstance(Effects.STRENGTH, 1800, 1));
		haste = new BrewMod(LibBrewNames.HASTE, 0xF4A432, 4000, new EffectInstance(Effects.HASTE, 1800, 1));
		healing = new BrewMod(LibBrewNames.HEALING, 0xFF5ECC, 6000, new EffectInstance(Effects.INSTANT_HEALTH, 1, 1));
		jumpBoost = new BrewMod(LibBrewNames.JUMP_BOOST, 0x32F46D, 4000, new EffectInstance(Effects.JUMP_BOOST, 1800, 1));
		regen = new BrewMod(LibBrewNames.REGEN, 0xFD6488, 7000, new EffectInstance(Effects.REGENERATION, 500, 1));
		regenWeak = new BrewMod(LibBrewNames.REGEN_WEAK, 0xFD6488, 9000, new EffectInstance(Effects.REGENERATION, 2400, 0));
		resistance = new BrewMod(LibBrewNames.RESISTANCE, 0xB44E17, 4000, new EffectInstance(Effects.RESISTANCE, 1800, 1));
		fireResistance = new BrewMod(LibBrewNames.FIRE_RESISTANCE, 0xF86900, 4000, new EffectInstance(Effects.FIRE_RESISTANCE, 9600, 0));
		waterBreathing = new BrewMod(LibBrewNames.WATER_BREATHING, 0x84A7CF, 4000, new EffectInstance(Effects.WATER_BREATHING, 9600, 0));
		invisibility = new BrewMod(LibBrewNames.INVISIBILITY, 0xAEAEAE, 8000, new EffectInstance(Effects.INVISIBILITY, 9600, 0)).setNotBloodPendantInfusable();
		nightVision = new BrewMod(LibBrewNames.NIGHT_VISION, 0x7C4BEB, 4000, new EffectInstance(Effects.NIGHT_VISION, 9600, 0));
		absorption = new BrewMod(LibBrewNames.ABSORPTION, 0xF2EB23, 7000, new EffectInstance(Effects.ABSORPTION, 1800, 3)).setNotBloodPendantInfusable().setNotIncenseInfusable();

		overload = new BrewMod(LibBrewNames.OVERLOAD, 0x232323, 12000, new EffectInstance(Effects.STRENGTH, 1800, 3), new EffectInstance(Effects.SPEED, 1800, 2), new EffectInstance(Effects.WEAKNESS, 3600, 1), new EffectInstance(Effects.HUNGER, 200, 2));
		soulCross = new BrewMod(LibBrewNames.SOUL_CROSS, 10000, new EffectInstance(ModPotions.soulCross, 1800, 0));
		featherfeet = new BrewMod(LibBrewNames.FEATHER_FEET, 7000, new EffectInstance(ModPotions.featherfeet, 1800, 0));
		emptiness = new BrewMod(LibBrewNames.EMPTINESS, 30000, new EffectInstance(ModPotions.emptiness, 7200, 0));
		bloodthirst = new BrewMod(LibBrewNames.BLOODTHIRST, 20000, new EffectInstance(ModPotions.bloodthrst, 7200, 0));
		allure = new BrewMod(LibBrewNames.ALLURE, 2000, new EffectInstance(ModPotions.allure, 4800, 0));
		clear = new BrewMod(LibBrewNames.CLEAR, 4000, new EffectInstance(ModPotions.clear, 0, 0));
	}

	public static void initTC() {
		Effect warpWardPotion = ForgeRegistries.POTIONS.getValue(new ResourceLocation("thaumcraft:warpward"));

		if(warpWardPotion != null)
			warpWard = new BrewMod(LibBrewNames.WARP_WARD, 0xFBBDFF, 25000, new EffectInstance(warpWardPotion, 12000, 0)).setNotBloodPendantInfusable();
	}
}
