/*
 * This class is distributed as part of the Botania Mod.
 * Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 *
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 */
package vazkii.botania.common.world;

import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.levelgen.feature.ConfiguredFeature;
import net.minecraft.world.level.levelgen.feature.Feature;

import java.util.function.BiConsumer;

import static vazkii.botania.common.lib.ResourceLocationHelper.prefix;

public class BotaniaFeatures {
	public static final ResourceKey<ConfiguredFeature<?, ?>> MYSTICAL_FLOWERS = ResourceKey.create(Registries.CONFIGURED_FEATURE, prefix("mystical_flowers"));
	public static final ResourceKey<ConfiguredFeature<?, ?>> MYSTICAL_MUSHROOMS = ResourceKey.create(Registries.CONFIGURED_FEATURE, prefix("mystical_mushrooms"));

	public static void registerFeatures(BiConsumer<Feature<?>, ResourceLocation> r) {
		r.accept(new MysticalFlowerFeature(MysticalFlowerConfig.CODEC), prefix("mystical_flower"));
	}

}
