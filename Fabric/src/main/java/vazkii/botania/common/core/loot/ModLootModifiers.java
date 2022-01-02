/*
 * This class is distributed as part of the Botania Mod.
 * Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 *
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 */
package vazkii.botania.common.core.loot;

import net.minecraft.core.Registry;
import net.minecraft.world.level.storage.loot.functions.LootItemFunctionType;
import net.minecraft.world.level.storage.loot.predicates.LootItemConditionType;

import static vazkii.botania.common.lib.ResourceLocationHelper.prefix;

public class ModLootModifiers {
	public static final LootItemConditionType TRUE_GUARDIAN_KILLER = new LootItemConditionType(new TrueGuardianKiller.Serializer());
	public static final LootItemConditionType ENABLE_RELICS = new LootItemConditionType(new EnableRelics.Serializer());
	public static final LootItemConditionType KILLED_BY_REAL_PLAYER = new LootItemConditionType(new RealPlayerCondition.Serializer());
	public static final LootItemFunctionType BIND_UUID = new LootItemFunctionType(new BindUuid.Serializer());

	public static void init() {
		Registry.register(Registry.LOOT_CONDITION_TYPE, prefix("true_guardian_killer"), TRUE_GUARDIAN_KILLER);
		Registry.register(Registry.LOOT_CONDITION_TYPE, prefix("enable_relics"), ENABLE_RELICS);
		Registry.register(Registry.LOOT_CONDITION_TYPE, prefix("killed_by_player"), KILLED_BY_REAL_PLAYER);
		Registry.register(Registry.LOOT_FUNCTION_TYPE, prefix("bind_uuid"), BIND_UUID);
	}
}
