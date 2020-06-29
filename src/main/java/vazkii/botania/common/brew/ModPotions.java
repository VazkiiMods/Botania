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
import net.minecraftforge.registries.IForgeRegistry;

import vazkii.botania.common.brew.potion.*;
import vazkii.botania.common.lib.LibPotionNames;

import static vazkii.botania.common.block.ModBlocks.register;

public class ModPotions {

	public static final Effect soulCross = new PotionSoulCross();
	public static final Effect featherfeet = new PotionFeatherfeet();
	public static final Effect emptiness = new PotionEmptiness();
	public static final Effect bloodthrst = new PotionBloodthirst();
	public static final Effect allure = new PotionAllure();
	public static final Effect clear = new PotionClear();

	public static void registerPotions(RegistryEvent.Register<Effect> evt) {
		IForgeRegistry<Effect> r = evt.getRegistry();
		register(r, LibPotionNames.SOUL_CROSS, soulCross);
		register(r, LibPotionNames.FEATHER_FEET, featherfeet);
		register(r, LibPotionNames.EMPTINESS, emptiness);
		register(r, LibPotionNames.BLOODTHIRST, bloodthrst);
		register(r, LibPotionNames.ALLURE, allure);
		register(r, LibPotionNames.CLEAR, clear);
	}
}
