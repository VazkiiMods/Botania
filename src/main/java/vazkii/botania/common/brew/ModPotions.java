/*
 * This class is distributed as part of the Botania Mod.
 * Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 *
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 */
package vazkii.botania.common.brew;

import net.minecraft.entity.effect.StatusEffect;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.registries.IForgeRegistry;

import vazkii.botania.common.brew.potion.*;
import vazkii.botania.common.lib.LibPotionNames;

import static vazkii.botania.common.block.ModBlocks.register;

public class ModPotions {

	public static final StatusEffect soulCross = new PotionSoulCross();
	public static final StatusEffect featherfeet = new PotionFeatherfeet();
	public static final StatusEffect emptiness = new PotionEmptiness();
	public static final StatusEffect bloodthrst = new PotionBloodthirst();
	public static final StatusEffect allure = new PotionAllure();
	public static final StatusEffect clear = new PotionClear();

	public static void registerPotions(RegistryEvent.Register<StatusEffect> evt) {
		IForgeRegistry<StatusEffect> r = evt.getRegistry();
		register(r, LibPotionNames.SOUL_CROSS, soulCross);
		register(r, LibPotionNames.FEATHER_FEET, featherfeet);
		register(r, LibPotionNames.EMPTINESS, emptiness);
		register(r, LibPotionNames.BLOODTHIRST, bloodthrst);
		register(r, LibPotionNames.ALLURE, allure);
		register(r, LibPotionNames.CLEAR, clear);
	}
}
