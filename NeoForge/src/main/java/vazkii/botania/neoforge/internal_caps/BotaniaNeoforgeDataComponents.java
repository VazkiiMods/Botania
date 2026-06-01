/*
 * This class is distributed as part of the Botania Mod.
 * Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 *
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 */

package vazkii.botania.neoforge.internal_caps;

import net.minecraft.core.component.DataComponentType;
import net.neoforged.neoforge.fluids.SimpleFluidContent;

import vazkii.botania.common.component.BotaniaDataComponents;

public class BotaniaNeoforgeDataComponents extends BotaniaDataComponents {
	public static final DataComponentType<SimpleFluidContent> BOWL_FLUID = make("bowl_fluid",
			builder -> builder.persistent(SimpleFluidContent.CODEC)
					.networkSynchronized(SimpleFluidContent.STREAM_CODEC));
}
