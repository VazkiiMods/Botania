package vazkii.botania.common.block.flower;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Items;

import vazkii.botania.api.block.IslandType;
import vazkii.botania.api.block_entity.SpecialFlowerBlockEntity;
import vazkii.botania.common.item.BotaniaItems;

import java.util.function.BiConsumer;

import static vazkii.botania.api.BotaniaAPI.botaniaRL;

public class BotaniaIslandTypes {

	public static final IslandType GRASS = new IslandType(BotaniaItems.grassSeeds, 0, botaniaRL("block/islands/island_grass"));
	public static final IslandType PODZOL = new IslandType(BotaniaItems.podzolSeeds, SpecialFlowerBlockEntity.PODZOL_DELAY, botaniaRL("block/islands/island_podzol"));
	public static final IslandType MYCELIUM = new IslandType(BotaniaItems.mycelSeeds, SpecialFlowerBlockEntity.MYCELIUM_DELAY, botaniaRL("block/islands/island_mycel"));
	public static final IslandType SNOW = new IslandType(Items.SNOWBALL, 0, botaniaRL("block/islands/island_snow"));
	public static final IslandType DRY = new IslandType(BotaniaItems.drySeeds, 0, botaniaRL("block/islands/island_dry"));
	public static final IslandType GOLDEN = new IslandType(BotaniaItems.goldenSeeds, 0, botaniaRL("block/islands/island_golden"));
	public static final IslandType VIVID = new IslandType(BotaniaItems.vividSeeds, 0, botaniaRL("block/islands/island_vivid"));
	public static final IslandType SCORCHED = new IslandType(BotaniaItems.scorchedSeeds, 0, botaniaRL("block/islands/island_scorched"));
	public static final IslandType INFUSED = new IslandType(BotaniaItems.infusedSeeds, 0, botaniaRL("block/islands/island_infused"));
	public static final IslandType MUTATED = new IslandType(BotaniaItems.mutatedSeeds, 0, botaniaRL("block/islands/island_mutated"));

	public static void registerIslandTypes(BiConsumer<IslandType, ResourceLocation> r) {
		r.accept(GRASS, IslandType.DEFAULT_ID);
		r.accept(PODZOL, botaniaRL("podzol"));
		r.accept(MYCELIUM, botaniaRL("mycelium"));
		r.accept(SNOW, botaniaRL("snow"));
		r.accept(DRY, botaniaRL("dry"));
		r.accept(GOLDEN, botaniaRL("golden"));
		r.accept(VIVID, botaniaRL("vivid"));
		r.accept(SCORCHED, botaniaRL("scorched"));
		r.accept(INFUSED, botaniaRL("infused"));
		r.accept(MUTATED, botaniaRL("mutated"));
	}
}
