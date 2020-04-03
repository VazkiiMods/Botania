package vazkii.botania.client.impl;

import com.google.common.collect.Maps;
import net.minecraft.util.ResourceLocation;
import vazkii.botania.api.BotaniaAPIClient;
import vazkii.botania.api.item.IFloatingFlower;

import java.util.Collections;
import java.util.Map;

public class BotaniaAPIClientImpl implements BotaniaAPIClient {
	private final Map<IFloatingFlower.IslandType, ResourceLocation> islandTypeModels = Maps.newHashMap();

	@Override
	public void registerIslandTypeModel(IFloatingFlower.IslandType islandType, ResourceLocation model) {
		islandTypeModels.put(islandType, model);
	}

	@Override
	public Map<IFloatingFlower.IslandType, ResourceLocation> getRegisteredIslandTypeModels() {
		return Collections.unmodifiableMap(islandTypeModels);
	}
}
