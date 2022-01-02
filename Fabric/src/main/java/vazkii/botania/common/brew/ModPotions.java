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
import net.minecraft.world.effect.MobEffect;

import vazkii.botania.common.brew.potion.*;
import vazkii.botania.common.lib.LibPotionNames;

import java.util.function.BiConsumer;

import static vazkii.botania.common.block.ModBlocks.register;

public class ModPotions {

	public static final MobEffect soulCross = new PotionSoulCross();
	public static final MobEffect featherfeet = new PotionFeatherfeet();
	public static final MobEffect emptiness = new PotionEmptiness();
	public static final MobEffect bloodthrst = new PotionBloodthirst();
	public static final MobEffect allure = new PotionAllure();
	public static final MobEffect clear = new PotionClear();

	public static void registerPotions(BiConsumer<MobEffect, ResourceLocation> r) {
		register(r, LibPotionNames.SOUL_CROSS, soulCross);
		register(r, LibPotionNames.FEATHER_FEET, featherfeet);
		register(r, LibPotionNames.EMPTINESS, emptiness);
		register(r, LibPotionNames.BLOODTHIRST, bloodthrst);
		register(r, LibPotionNames.ALLURE, allure);
		register(r, LibPotionNames.CLEAR, clear);
	}
}
