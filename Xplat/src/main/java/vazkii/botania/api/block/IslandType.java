package vazkii.botania.api.block;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.level.ItemLike;

import vazkii.botania.api.BotaniaAPI;

public record IslandType(ItemLike item, int modulatedDelay, SoundEvent changeSound) {

	public static ResourceLocation DEFAULT_ID = BotaniaAPI.botaniaRL("grass");

	public ResourceLocation islandModel() {
		return BotaniaAPI.instance().getIslandTypeRegistry().getKey(this).withPrefix("block/islands/");
	}
}
