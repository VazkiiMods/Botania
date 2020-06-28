/*
 * This class is distributed as part of the Botania Mod.
 * Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 *
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 */
package vazkii.botania.common.world;


import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.world.gen.feature.IFeatureConfig;

import vazkii.botania.common.core.handler.ConfigHandler;

public class MysticalFlowerConfig implements IFeatureConfig {
	public static final Codec<MysticalFlowerConfig> CODEC = RecordCodecBuilder.create(instance -> instance.group(
					Codec.INT.fieldOf("flower_patch_size").forGetter(MysticalFlowerConfig::getFlowerPatchSize),
					Codec.INT.fieldOf("mushroom_patch_size").forGetter(MysticalFlowerConfig::getMushroomPatchSize),
					Codec.INT.fieldOf("patch_count").forGetter(MysticalFlowerConfig::getPatchCount),
					Codec.INT.fieldOf("patch_density").forGetter(MysticalFlowerConfig::getPatchDensity),
					Codec.INT.fieldOf("patch_chance").forGetter(MysticalFlowerConfig::getPatchChance),
					Codec.DOUBLE.fieldOf("tall_chance").forGetter(MysticalFlowerConfig::getTallChance)
	).apply(instance, MysticalFlowerConfig::new));

	private final int flowerPatchSize;
	private final int mushroomPatchSize;
	private final int patchCount;
	private final int patchDensity;
	private final int patchChance;
	private final double tallChance;

	public static MysticalFlowerConfig fromConfig() {
		return new MysticalFlowerConfig(
						ConfigHandler.COMMON.flowerPatchSize.get(),
						ConfigHandler.COMMON.mushroomQuantity.get(),
						ConfigHandler.COMMON.flowerPatchSize.get(),
						ConfigHandler.COMMON.flowerDensity.get(),
						ConfigHandler.COMMON.flowerPatchChance.get(),
						ConfigHandler.COMMON.flowerTallChance.get()
		);
	}

	public MysticalFlowerConfig(int flowerPatchSize, int mushroomPatchSize, int patchCount, int patchDensity, int patchChance, double tallChance) {
		this.flowerPatchSize = flowerPatchSize;
		this.mushroomPatchSize = mushroomPatchSize;
		this.patchCount = patchCount;
		this.patchDensity = patchDensity;
		this.patchChance = patchChance;
		this.tallChance = tallChance;
	}

	public int getFlowerPatchSize() {
		return flowerPatchSize;
	}

	public int getPatchCount() {
		return patchCount;
	}

	public int getPatchDensity() {
		return patchDensity;
	}

	public int getPatchChance() {
		return patchChance;
	}

	public double getTallChance() {
		return tallChance;
	}

	public int getMushroomPatchSize() {
		return mushroomPatchSize;
	}
}
