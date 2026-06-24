/*
 * This class is distributed as part of the Botania Mod.
 * Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 *
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 */
package vazkii.botania.common.brew;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.item.alchemy.PotionContents;

import vazkii.botania.api.brew.Brew;
import vazkii.botania.common.lib.LibBrewNames;

import java.util.Arrays;
import java.util.function.BiConsumer;

import static vazkii.botania.api.BotaniaAPI.botaniaRL;

public class BotaniaBrews {

	public static final Brew FALLBACK = new Brew(0, 0).setNotBloodPendantInfusable().setNotIncenseInfusable();
	public static final Brew FLEETFEET = new Brew(0x59B7FF, 4000, new MobEffectInstance(MobEffects.MOVEMENT_SPEED, 1800, 1));
	public static final Brew VIGOR = new Brew(0xEE3F3F, 4000, new MobEffectInstance(MobEffects.DAMAGE_BOOST, 1800, 1));
	public static final Brew ADRENALINE = new Brew(0xF4A432, 4000, new MobEffectInstance(MobEffects.DIG_SPEED, 1800, 1));
	public static final Brew MENDING = new Brew(0xFF5ECC, 6000, new MobEffectInstance(MobEffects.HEAL, 1, 1));
	public static final Brew UPSURGING = new Brew(0x32F46D, 4000, new MobEffectInstance(MobEffects.JUMP, 1800, 1));
	public static final Brew REVITALIZATION = new Brew(0xFD6488, 7000, new MobEffectInstance(MobEffects.REGENERATION, 500, 1));
	public static final Brew RESTORATION = new Brew(0xFD6488, 9000, new MobEffectInstance(MobEffects.REGENERATION, 2400, 0));
	public static final Brew FORTITUDE = new Brew(0xB44E17, 4000, new MobEffectInstance(MobEffects.DAMAGE_RESISTANCE, 1800, 1));
	public static final Brew MAGMASKIN = new Brew(0xF86900, 4000, new MobEffectInstance(MobEffects.FIRE_RESISTANCE, 9600, 0));
	public static final Brew GILLS = new Brew(0x84A7CF, 4000, new MobEffectInstance(MobEffects.WATER_BREATHING, 9600, 0));
	public static final Brew CLOAKING = new Brew(0xAEAEAE, 8000, new MobEffectInstance(MobEffects.INVISIBILITY, 9600, 0)).setNotBloodPendantInfusable();
	public static final Brew OWLSIGHT = new Brew(0x7C4BEB, 4000, new MobEffectInstance(MobEffects.NIGHT_VISION, 9600, 0));
	public static final Brew SHIELDING = new Brew(0xF2EB23, 7000, new MobEffectInstance(MobEffects.ABSORPTION, 1800, 3)).setNotBloodPendantInfusable().setNotIncenseInfusable();

	public static final Brew MARINE_ALLURE = make(2000, new MobEffectInstance(BotaniaMobEffects.ALLURE, 4800, 0));
	public static final Brew CROSSED_SOULS = make(10000, new MobEffectInstance(BotaniaMobEffects.SOUL_CROSS, 1800, 0));
	public static final Brew FEATHER_FEET = make(7000, new MobEffectInstance(BotaniaMobEffects.FEATHER_FEET, 1800, 0));
	public static final Brew VANITYS_EMPTINESS = make(30000, new MobEffectInstance(BotaniaMobEffects.EMPTINESS, 7200, 0));
	public static final Brew CRIMSON_SHADE = make(20000, new MobEffectInstance(BotaniaMobEffects.BLOODTHIRST, 7200, 0));
	public static final Brew OVERLOAD = new Brew(0x232323, 12000, new MobEffectInstance(MobEffects.DAMAGE_BOOST, 1800, 3), new MobEffectInstance(MobEffects.MOVEMENT_SPEED, 1800, 2), new MobEffectInstance(MobEffects.WEAKNESS, 3600, 1), new MobEffectInstance(MobEffects.HUNGER, 200, 2));
	public static final Brew ABSOLUTION = make(4000, new MobEffectInstance(BotaniaMobEffects.ABSOLUTION, 0, 0));

	public static void submitRegistrations(BiConsumer<Brew, ResourceLocation> r) {
		r.accept(FALLBACK, Brew.DEFAULT_ID);
		r.accept(FLEETFEET, botaniaRL(LibBrewNames.FLEETFEET));
		r.accept(VIGOR, botaniaRL(LibBrewNames.VIGOR));
		r.accept(ADRENALINE, botaniaRL(LibBrewNames.ADRENALINE));
		r.accept(MENDING, botaniaRL(LibBrewNames.MENDING));
		r.accept(UPSURGING, botaniaRL(LibBrewNames.UPSURGING));
		r.accept(REVITALIZATION, botaniaRL(LibBrewNames.REVITALIZATION));
		r.accept(RESTORATION, botaniaRL(LibBrewNames.RESTORATION));
		r.accept(FORTITUDE, botaniaRL(LibBrewNames.FORTITUDE));
		r.accept(MAGMASKIN, botaniaRL(LibBrewNames.MAGMASKIN));
		r.accept(GILLS, botaniaRL(LibBrewNames.GILLS));
		r.accept(CLOAKING, botaniaRL(LibBrewNames.CLOAKING));
		r.accept(OWLSIGHT, botaniaRL(LibBrewNames.OWLSIGHT));
		r.accept(SHIELDING, botaniaRL(LibBrewNames.SHIELDING));
		r.accept(OVERLOAD, botaniaRL(LibBrewNames.OVERLOAD));
		r.accept(CROSSED_SOULS, botaniaRL(LibBrewNames.CROSSED_SOULS));
		r.accept(FEATHER_FEET, botaniaRL(LibBrewNames.FEATHER_FEET));
		r.accept(VANITYS_EMPTINESS, botaniaRL(LibBrewNames.VANITYS_EMPTINESS));
		r.accept(CRIMSON_SHADE, botaniaRL(LibBrewNames.CRIMSON_SHADE));
		r.accept(MARINE_ALLURE, botaniaRL(LibBrewNames.MARINE_ALLURE));
		r.accept(ABSOLUTION, botaniaRL(LibBrewNames.ABSOLUTION));
	}

	private static Brew make(int cost, MobEffectInstance... effects) {
		return new Brew(PotionContents.getColor(Arrays.asList(effects)), cost, effects);
	}

}
