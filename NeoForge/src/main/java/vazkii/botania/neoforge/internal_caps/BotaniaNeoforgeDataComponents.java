package vazkii.botania.neoforge.internal_caps;

import net.minecraft.core.component.DataComponentType;
import net.neoforged.neoforge.fluids.SimpleFluidContent;

import vazkii.botania.common.component.BotaniaDataComponents;

public class BotaniaNeoforgeDataComponents extends BotaniaDataComponents {
	public static final DataComponentType<SimpleFluidContent> BOWL_FLUID = make("bowl_fluid",
			builder -> builder.persistent(SimpleFluidContent.CODEC)
					.networkSynchronized(SimpleFluidContent.STREAM_CODEC));
}
