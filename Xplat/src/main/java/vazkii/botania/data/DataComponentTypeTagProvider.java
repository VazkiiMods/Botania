package vazkii.botania.data;

import net.minecraft.core.HolderLookup;
import net.minecraft.core.component.DataComponentType;
import net.minecraft.core.component.DataComponents;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.PackOutput;
import net.minecraft.data.tags.IntrinsicHolderTagsProvider;
import net.minecraft.resources.ResourceKey;

import vazkii.botania.common.lib.BotaniaTags;

import java.util.concurrent.CompletableFuture;

public class DataComponentTypeTagProvider extends IntrinsicHolderTagsProvider<DataComponentType<?>> {

	public DataComponentTypeTagProvider(PackOutput packOutput, CompletableFuture<HolderLookup.Provider> lookupProvider) {
		super(packOutput, Registries.DATA_COMPONENT_TYPE, lookupProvider, DataComponentTypeTagProvider::getResourceKey);
	}

	@Override
	protected void addTags(HolderLookup.Provider provider) {
		this.tag(BotaniaTags.DataComponentTypes.GOURMARYLLIS_RELEVANT).add(
				DataComponents.FOOD,
				DataComponents.SUSPICIOUS_STEW_EFFECTS
		);
	}

	private static ResourceKey<DataComponentType<?>> getResourceKey(DataComponentType<?> dataComponentType) {
		return BuiltInRegistries.DATA_COMPONENT_TYPE.getResourceKey(dataComponentType).orElseThrow();
	}
}
