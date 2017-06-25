package vazkii.botania.common.core.loot;

import com.google.common.collect.ImmutableList;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.storage.loot.LootEntry;
import net.minecraft.world.storage.loot.LootEntryTable;
import net.minecraft.world.storage.loot.LootPool;
import net.minecraft.world.storage.loot.LootTableList;
import net.minecraft.world.storage.loot.RandomValueRange;
import net.minecraft.world.storage.loot.conditions.LootCondition;
import net.minecraft.world.storage.loot.conditions.LootConditionManager;
import net.minecraft.world.storage.loot.functions.LootFunctionManager;
import net.minecraftforge.event.LootTableLoadEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import vazkii.botania.common.lib.LibMisc;

import java.util.List;

public final class LootHandler {

	private static final List<String> TABLES = ImmutableList.of(
			"gaia_guardian", "gaia_guardian_2",
			"inject/abandoned_mineshaft", "inject/desert_pyramid",
			"inject/jungle_temple", "inject/simple_dungeon",
			"inject/spawn_bonus_chest", "inject/stronghold_corridor",
			"inject/village_blacksmith"
			);

	public LootHandler() {
		for (String s : TABLES) {
			LootTableList.register(new ResourceLocation(LibMisc.MOD_ID, s));
		}

		LootConditionManager.registerCondition(new TrueGuardianKiller.Serializer());
		LootConditionManager.registerCondition(new EnableRelics.Serializer());
		LootFunctionManager.registerFunction(new BindUuid.Serializer());
	}

	@SubscribeEvent
	public void lootLoad(LootTableLoadEvent evt) {
		String prefix = "minecraft:chests/";
		String name = evt.getName().toString();

		if (name.startsWith(prefix)) {
			String file = name.substring(name.indexOf(prefix) + prefix.length());
			switch (file) {
			case "abandoned_mineshaft":
			case "desert_pyramid":
			case "jungle_temple":
			case "simple_dungeon":
			case "spawn_bonus_chest":
			case "stronghold_corridor":
			case "village_blacksmith": evt.getTable().addPool(getInjectPool(file)); break;
			default: break;
			}
		}
	}

	private LootPool getInjectPool(String entryName) {
		return new LootPool(new LootEntry[] { getInjectEntry(entryName, 1) }, new LootCondition[0], new RandomValueRange(1), new RandomValueRange(0, 1), "botania_inject_pool");
	}

	private LootEntryTable getInjectEntry(String name, int weight) {
		return new LootEntryTable(new ResourceLocation(LibMisc.MOD_ID, "inject/" + name), weight, 0, new LootCondition[0], "botania_inject_entry");
	}

}
