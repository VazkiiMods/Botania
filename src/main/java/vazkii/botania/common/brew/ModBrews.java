/*
 * This class is distributed as part of the Botania Mod.
 * Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 *
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 */
package vazkii.botania.common.brew;

import net.minecraft.potion.EffectInstance;
import net.minecraft.potion.Effects;
import net.minecraft.potion.PotionUtils;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.registry.Registry;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.registries.IForgeRegistry;
import net.minecraftforge.registries.RegistryBuilder;

import vazkii.botania.api.brew.Brew;
import vazkii.botania.common.lib.LibBrewNames;
import vazkii.botania.common.lib.LibMisc;

import java.util.Arrays;

import static vazkii.botania.common.lib.ResourceLocationHelper.prefix;

@Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.MOD, modid = LibMisc.MOD_ID)
public class ModBrews {

	public static IForgeRegistry<Brew> registry;
	public static final Brew fallbackBrew = make("fallback", 0, 0);
	public static final Brew speed = make(LibBrewNames.SPEED, 0x59B7FF, 4000, new EffectInstance(Effects.SPEED, 1800, 1));
	public static final Brew strength = make(LibBrewNames.STRENGTH, 0xEE3F3F, 4000, new EffectInstance(Effects.STRENGTH, 1800, 1));
	public static final Brew haste = make(LibBrewNames.HASTE, 0xF4A432, 4000, new EffectInstance(Effects.HASTE, 1800, 1));
	public static final Brew healing = make(LibBrewNames.HEALING, 0xFF5ECC, 6000, new EffectInstance(Effects.INSTANT_HEALTH, 1, 1));
	public static final Brew jumpBoost = make(LibBrewNames.JUMP_BOOST, 0x32F46D, 4000, new EffectInstance(Effects.JUMP_BOOST, 1800, 1));
	public static final Brew regen = make(LibBrewNames.REGEN, 0xFD6488, 7000, new EffectInstance(Effects.REGENERATION, 500, 1));
	public static final Brew regenWeak = make(LibBrewNames.REGEN_WEAK, 0xFD6488, 9000, new EffectInstance(Effects.REGENERATION, 2400, 0));
	public static final Brew resistance = make(LibBrewNames.RESISTANCE, 0xB44E17, 4000, new EffectInstance(Effects.RESISTANCE, 1800, 1));
	public static final Brew fireResistance = make(LibBrewNames.FIRE_RESISTANCE, 0xF86900, 4000, new EffectInstance(Effects.FIRE_RESISTANCE, 9600, 0));
	public static final Brew waterBreathing = make(LibBrewNames.WATER_BREATHING, 0x84A7CF, 4000, new EffectInstance(Effects.WATER_BREATHING, 9600, 0));
	public static final Brew invisibility = make(LibBrewNames.INVISIBILITY, 0xAEAEAE, 8000, new EffectInstance(Effects.INVISIBILITY, 9600, 0)).setNotBloodPendantInfusable();
	public static final Brew nightVision = make(LibBrewNames.NIGHT_VISION, 0x7C4BEB, 4000, new EffectInstance(Effects.NIGHT_VISION, 9600, 0));
	public static final Brew absorption = make(LibBrewNames.ABSORPTION, 0xF2EB23, 7000, new EffectInstance(Effects.ABSORPTION, 1800, 3)).setNotBloodPendantInfusable().setNotIncenseInfusable();

	public static final Brew allure = make(LibBrewNames.ALLURE, 2000, new EffectInstance(ModPotions.allure, 4800, 0));
	public static final Brew soulCross = make(LibBrewNames.SOUL_CROSS, 10000, new EffectInstance(ModPotions.soulCross, 1800, 0));
	public static final Brew featherfeet = make(LibBrewNames.FEATHER_FEET, 7000, new EffectInstance(ModPotions.featherfeet, 1800, 0));
	public static final Brew emptiness = make(LibBrewNames.EMPTINESS, 30000, new EffectInstance(ModPotions.emptiness, 7200, 0));
	public static final Brew bloodthirst = make(LibBrewNames.BLOODTHIRST, 20000, new EffectInstance(ModPotions.bloodthrst, 7200, 0));
	public static final Brew overload = make(LibBrewNames.OVERLOAD, 0x232323, 12000, new EffectInstance(Effects.STRENGTH, 1800, 3), new EffectInstance(Effects.SPEED, 1800, 2), new EffectInstance(Effects.WEAKNESS, 3600, 1), new EffectInstance(Effects.HUNGER, 200, 2));
	public static final Brew clear = make(LibBrewNames.CLEAR, 4000, new EffectInstance(ModPotions.clear, 0, 0));

	public static Brew warpWard;

	@SubscribeEvent
	public static void registerRegistry(RegistryEvent.NewRegistry evt) {
		registry = new RegistryBuilder<Brew>()
				.setName(prefix("brews"))
				.setType(Brew.class)
				.setDefaultKey(prefix("fallback"))
				.disableSaving()
				.create();
	}

	@SubscribeEvent
	public static void register(RegistryEvent.Register<Brew> evt) {
		evt.getRegistry().registerAll(fallbackBrew, speed, strength, haste, healing, jumpBoost, regen, regenWeak, resistance,
				fireResistance, waterBreathing, invisibility, nightVision, absorption, overload, soulCross, featherfeet,
				emptiness, bloodthirst, allure, clear);

		Registry.EFFECTS.getValue(new ResourceLocation("thaumcraft:warpward")).ifPresent(warpWardPotion -> {
			warpWard = make(LibBrewNames.WARP_WARD, 0xFBBDFF, 25000, new EffectInstance(warpWardPotion, 12000, 0)).setNotBloodPendantInfusable();
			evt.getRegistry().register(warpWard);
		});
	}

	private static Brew make(String name, int cost, EffectInstance... effects) {
		return new Brew(PotionUtils.getPotionColorFromEffectList(Arrays.asList(effects)), cost, effects).setRegistryName(prefix(name));
	}

	private static Brew make(String name, int color, int cost, EffectInstance... effects) {
		return new Brew(color, cost, effects).setRegistryName(prefix(name));
	}
}
