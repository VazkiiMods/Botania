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

import vazkii.botania.common.brew.effect.*;
import vazkii.botania.common.lib.LibPotionNames;

import java.util.function.BiConsumer;

import static vazkii.botania.common.lib.ResourceLocationHelper.prefix;

public class BotaniaMobEffects {

	public static final MobEffect soulCross = new SoulCrossMobEffect();
	public static final MobEffect featherfeet = new FeatherfeetMobEffect();
	public static final MobEffect emptiness = new EmptinessMobEffect();
	public static final MobEffect bloodthrst = new BloodthirstMobEffect();
	public static final MobEffect allure = new AllureMobEffect();
	public static final MobEffect clear = new AbsolutionMobEffect();

	public static void registerPotions(BiConsumer<MobEffect, ResourceLocation> r) {
		r.accept(soulCross, prefix(LibPotionNames.SOUL_CROSS));
		r.accept(featherfeet, prefix(LibPotionNames.FEATHER_FEET));
		r.accept(emptiness, prefix(LibPotionNames.EMPTINESS));
		r.accept(bloodthrst, prefix(LibPotionNames.BLOODTHIRST));
		r.accept(allure, prefix(LibPotionNames.ALLURE));
		r.accept(clear, prefix(LibPotionNames.CLEAR));
	}
}
