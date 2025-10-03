package vazkii.botania.api.block;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.ItemLike;

import vazkii.botania.api.BotaniaAPI;

public record IslandType(ItemLike item, int modulatedDelay, ResourceLocation islandModel) {
	public static ResourceLocation DEFAULT_ID = BotaniaAPI.botaniaRL("grass");
}
