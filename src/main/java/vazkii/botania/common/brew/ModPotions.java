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
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import vazkii.botania.common.brew.potion.PotionAllure;
import vazkii.botania.common.brew.potion.PotionBloodthirst;
import vazkii.botania.common.brew.potion.PotionClear;
import vazkii.botania.common.brew.potion.PotionEmptiness;
import vazkii.botania.common.brew.potion.PotionFeatherfeet;
import vazkii.botania.common.brew.potion.PotionSoulCross;
import vazkii.botania.common.lib.LibMisc;

@Mod.EventBusSubscriber(modid = LibMisc.MOD_ID)
public class ModPotions {

	public static final Potion soulCross = new PotionSoulCross();
	public static final Potion featherfeet = new PotionFeatherfeet();
	public static final Potion emptiness = new PotionEmptiness();
	public static final Potion bloodthrst = new PotionBloodthirst();
	public static final Potion allure = new PotionAllure();
	public static final Potion clear = new PotionClear();

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
