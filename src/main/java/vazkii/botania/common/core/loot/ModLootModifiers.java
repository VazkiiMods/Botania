/*
 * This class is distributed as part of the Botania Mod.
 * Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 *
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 */
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
		Registry.register(Registry.LOOT_CONDITION_TYPE, prefix("true_guardian_killer"), TRUE_GUARDIAN_KILLER);
		Registry.register(Registry.LOOT_CONDITION_TYPE, prefix("enable_relics"), ENABLE_RELICS);
		Registry.register(Registry.LOOT_FUNCTION_TYPE, prefix("bind_uuid"), BIND_UUID);
	}
}
