/*
 * This class is distributed as part of the Botania Mod.
 * Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 *
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 */
package vazkii.botania.mixin;

import net.minecraft.world.gen.DimensionSettings;
import net.minecraft.world.gen.settings.DimensionStructuresSettings;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Invoker;

@Mixin(DimensionSettings.Preset.class)
public interface AccessorDimensionSettingsPreset {
	@Invoker("func_236135_a_")
	static DimensionSettings createOverworldSettings(DimensionStructuresSettings structureSettings, boolean amplified, DimensionSettings.Preset preset) {
		throw new IllegalStateException();
	}
}
