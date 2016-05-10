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
        switch (evt.getName().toString()) {
            case "minecraft:chests/spawn_bonus_chest": break;
            case "minecraft:chests/stronghold_corridor": break;
            case "minecraft:chests/simple_dungeon": break;
            case "minecraft:chests/desert_pyramid": break;
            case "minecraft:chests/abandoned_mineshaft": break;
            case "minecraft:chests/jungle_temple": break;
            case "minecraft:chests/village_blacksmith": break;
        }
    }

    private LootEntryTable getInjectEntry(String name) {
        return new LootEntryTable(new ResourceLocation(LibMisc.MOD_ID, "inject/" + name), 1, 0, new LootCondition[0], "botania_inject");
    }

}
