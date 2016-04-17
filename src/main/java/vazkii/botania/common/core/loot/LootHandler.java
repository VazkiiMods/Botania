package vazkii.botania.common.core.loot;

import net.minecraft.util.ResourceLocation;
import net.minecraft.world.storage.loot.LootTableList;
import net.minecraft.world.storage.loot.conditions.LootConditionManager;
import net.minecraft.world.storage.loot.functions.LootFunctionManager;
import vazkii.botania.common.lib.LibMisc;

public final class LootHandler {

    public static void init() {
        LootTableList.register(new ResourceLocation(LibMisc.MOD_ID, "gaia_guardian"));
        LootTableList.register(new ResourceLocation(LibMisc.MOD_ID, "gaia_guardian_2"));
        LootConditionManager.registerCondition(new TrueGuardianKiller.Serializer());
        LootConditionManager.registerCondition(new EnableRelics.Serializer());
        LootFunctionManager.registerFunction(new BindUuid.Serializer());
    }

    private LootHandler() {}

}
