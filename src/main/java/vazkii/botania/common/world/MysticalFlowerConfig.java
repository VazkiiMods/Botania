/*
 * This class is distributed as part of the Botania Mod.
 * Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 *
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 */
package vazkii.botania.common.world;


import com.mojang.serialization.Dynamic;
import com.mojang.serialization.DynamicOps;
import net.minecraft.world.gen.feature.IFeatureConfig;

import vazkii.botania.common.core.handler.ConfigHandler;

import javax.annotation.Nonnull;

public class MysticalFlowerConfig implements IFeatureConfig {
	public int getPatchSize() {
		return ConfigHandler.COMMON.flowerPatchSize.get();
	}

	public int getPatchCount() {
		return ConfigHandler.COMMON.flowerQuantity.get();
	}

	public int getPatchDensity() {
		return ConfigHandler.COMMON.flowerDensity.get();
	}

	public int getPatchChance() {
		return ConfigHandler.COMMON.flowerPatchChance.get();
	}

	public double getTallChance() {
		return ConfigHandler.COMMON.flowerTallChance.get();
	}

	public int getMushroomPatchSize() {
		return ConfigHandler.COMMON.mushroomQuantity.get();
	}

	@Nonnull
	@Override
	public <T> Dynamic<T> serialize(@Nonnull DynamicOps<T> ops) {
		return new Dynamic<>(ops);
	}

	public static MysticalFlowerConfig deserialize(Dynamic<?> dynamic) {
		return new MysticalFlowerConfig();
	}
}
