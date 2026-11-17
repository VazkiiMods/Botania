/*
 * This class is distributed as part of the Botania Mod.
 * Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 *
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 */

package vazkii.botania.api.configdata;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.resources.PreparableReloadListener;

import org.jetbrains.annotations.Nullable;

public interface ConfigDataManager extends PreparableReloadListener {
	@Nullable
	LooniumStructureConfiguration getEffectiveLooniumStructureConfiguration(ResourceLocation id);

	@Nullable
	GaiaFightConfiguration getGaiaFightConfiguration(ResourceLocation id);
}
