/*
 * This class is distributed as part of the Botania Mod.
 * Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 *
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 */
package vazkii.botania.common.brew;

import net.minecraft.core.Holder;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.effect.MobEffect;

import vazkii.botania.common.brew.effect.*;
import vazkii.botania.common.helper.RegistryHelper;

import java.util.ArrayList;
import java.util.List;

import static vazkii.botania.api.BotaniaAPI.botaniaRL;

public class BotaniaMobEffects {
	private static final List<RegistryHelper.HolderProxy<MobEffect>> toRegister = new ArrayList<>();

	public static final Holder<MobEffect> SOUL_CROSS = create("soul_cross", new SoulCrossMobEffect());
	public static final Holder<MobEffect> FEATHER_FEET = create("feather_feet", new FeatherfeetMobEffect());
	public static final Holder<MobEffect> EMPTINESS = create("emptiness", new EmptinessMobEffect());
	public static final Holder<MobEffect> BLOODTHIRST = create("bloodthirst", new BloodthirstMobEffect());
	public static final Holder<MobEffect> ALLURE = create("allure", new AllureMobEffect());
	public static final Holder<MobEffect> CLEAR = create("clear", new AbsolutionMobEffect());

	private static Holder<MobEffect> create(String name, MobEffect effect) {
		ResourceLocation id = botaniaRL(name);
		RegistryHelper.HolderProxy<MobEffect> proxy = RegistryHelper.holderProxy(Registries.MOB_EFFECT, id, effect);
		toRegister.add(proxy);
		return proxy;
	}

	public static void registerPotions(Registry<MobEffect> registry) {
		toRegister.forEach(proxy -> proxy.register(registry));
	}
}
