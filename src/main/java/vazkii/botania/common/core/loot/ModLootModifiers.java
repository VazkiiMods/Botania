package vazkii.botania.common.core.loot;

import net.minecraft.loot.LootConditionType;
import net.minecraft.loot.LootFunctionType;
import net.minecraft.util.registry.Registry;

import static vazkii.botania.common.lib.ResourceLocationHelper.prefix;

public class ModLootModifiers {
	public static final LootConditionType TRUE_GUARDIAN_KILLER = new LootConditionType(new TrueGuardianKiller.Serializer());
	public static final LootConditionType ENABLE_RELICS = new LootConditionType(new EnableRelics.Serializer());
	public static final LootFunctionType BIND_UUID = new LootFunctionType(new BindUuid.Serializer());

	public static void init() {
		Registry.register(Registry.field_239704_ba_, prefix("true_guardian_killer"), TRUE_GUARDIAN_KILLER);
		Registry.register(Registry.field_239704_ba_, prefix("enable_relics"), ENABLE_RELICS);
		Registry.register(Registry.field_239694_aZ_, prefix("bind_uuid"), BIND_UUID);
	}
}
