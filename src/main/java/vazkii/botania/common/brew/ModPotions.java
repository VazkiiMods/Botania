/*
 * This class is distributed as part of the Botania Mod.
 * Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 *
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 */
package vazkii.botania.common.brew;

import net.minecraft.potion.Effect;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import vazkii.botania.common.brew.potion.PotionAllure;
import vazkii.botania.common.brew.potion.PotionBloodthirst;
import vazkii.botania.common.brew.potion.PotionClear;
import vazkii.botania.common.brew.potion.PotionEmptiness;
import vazkii.botania.common.brew.potion.PotionFeatherfeet;
import vazkii.botania.common.brew.potion.PotionSoulCross;
import vazkii.botania.common.lib.LibMisc;
import vazkii.botania.common.lib.LibPotionNames;

@Mod.EventBusSubscriber(modid = LibMisc.MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class ModPotions {

	public static final Effect soulCross = new PotionSoulCross().setRegistryName(LibMisc.MOD_ID, LibPotionNames.SOUL_CROSS);
	public static final Effect featherfeet = new PotionFeatherfeet().setRegistryName(LibMisc.MOD_ID, LibPotionNames.FEATHER_FEET);
	public static final Effect emptiness = new PotionEmptiness().setRegistryName(LibMisc.MOD_ID, LibPotionNames.EMPTINESS);
	public static final Effect bloodthrst = new PotionBloodthirst().setRegistryName(LibMisc.MOD_ID, LibPotionNames.BLOODTHIRST);
	public static final Effect allure = new PotionAllure().setRegistryName(LibMisc.MOD_ID, LibPotionNames.ALLURE);
	public static final Effect clear = new PotionClear().setRegistryName(LibMisc.MOD_ID, LibPotionNames.CLEAR);

	@SubscribeEvent
	public static void registerPotions(RegistryEvent.Register<Effect> evt) {
		evt.getRegistry().register(soulCross);
		evt.getRegistry().register(featherfeet);
		evt.getRegistry().register(emptiness);
		evt.getRegistry().register(bloodthrst);
		evt.getRegistry().register(allure);
		evt.getRegistry().register(clear);
	}
}
