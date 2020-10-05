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

import net.minecraft.world.gen.feature.FeatureConfig;

public class MysticalMushroomConfig implements FeatureConfig {
	public static final Codec<MysticalMushroomConfig> CODEC = RecordCodecBuilder.create(instance -> instance.group(
			Codec.INT.fieldOf("patch_size").forGetter(MysticalMushroomConfig::getMushroomPatchSize)
	).apply(instance, MysticalMushroomConfig::new));

	private final int mushroomPatchSize;

	public MysticalMushroomConfig(int mushroomPatchSize) {
		this.mushroomPatchSize = mushroomPatchSize;
	}

	public int getMushroomPatchSize() {
		return mushroomPatchSize;
	}
}
