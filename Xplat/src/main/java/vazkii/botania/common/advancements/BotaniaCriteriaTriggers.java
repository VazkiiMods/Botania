/*
 * This class is distributed as part of the Botania Mod.
 * Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 *
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 */
package vazkii.botania.common.advancements;

import vazkii.botania.mixin.CriteriaTriggersAccessor;

public class BotaniaCriteriaTriggers {
	public static void init() {
		CriteriaTriggersAccessor.botania_register(AlfheimPortalTrigger.INSTANCE);
		CriteriaTriggersAccessor.botania_register(CorporeaRequestTrigger.INSTANCE);
		CriteriaTriggersAccessor.botania_register(GaiaGuardianNoArmorTrigger.INSTANCE);
		CriteriaTriggersAccessor.botania_register(RelicBindTrigger.INSTANCE);
		CriteriaTriggersAccessor.botania_register(UseItemSuccessTrigger.INSTANCE);
		CriteriaTriggersAccessor.botania_register(ManaBlasterTrigger.INSTANCE);
		CriteriaTriggersAccessor.botania_register(LokiPlaceTrigger.INSTANCE);
		CriteriaTriggersAccessor.botania_register(AlfheimPortalBreadTrigger.INSTANCE);
	}
}
