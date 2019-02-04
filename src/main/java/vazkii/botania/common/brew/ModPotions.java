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

	public static final Potion soulCross = new PotionSoulCross().setRegistryName(LibMisc.MOD_ID, LibPotionNames.SOUL_CROSS);
	public static final Potion featherfeet = new PotionFeatherfeet().setRegistryName(LibMisc.MOD_ID, LibPotionNames.FEATHER_FEET);
	public static final Potion emptiness = new PotionEmptiness().setRegistryName(LibMisc.MOD_ID, LibPotionNames.EMPTINESS);
	public static final Potion bloodthrst = new PotionBloodthirst().setRegistryName(LibMisc.MOD_ID, LibPotionNames.BLOODTHIRST);
	public static final Potion allure = new PotionAllure().setRegistryName(LibMisc.MOD_ID, LibPotionNames.ALLURE);
	public static final Potion clear = new PotionClear().setRegistryName(LibMisc.MOD_ID, LibPotionNames.CLEAR);

	@SubscribeEvent
	public static void registerPotions(RegistryEvent.Register<Potion> evt)
	{
		evt.getRegistry().register(soulCross);
		evt.getRegistry().register(featherfeet);
		evt.getRegistry().register(emptiness);
		evt.getRegistry().register(bloodthrst);
		evt.getRegistry().register(allure);
		evt.getRegistry().register(clear);
	}
}
