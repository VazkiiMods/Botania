/*
 * This class is distributed as part of the Botania Mod.
 * Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 *
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 */
package vazkii.botania.common.advancements;

import vazkii.botania.mixin.AccessorCriteriaTriggers;

public class ModCriteriaTriggers {
	public static void init() {
		AccessorCriteriaTriggers.botania_register(AlfPortalTrigger.INSTANCE);
		AccessorCriteriaTriggers.botania_register(CorporeaRequestTrigger.INSTANCE);
		AccessorCriteriaTriggers.botania_register(DopplegangerNoArmorTrigger.INSTANCE);
		AccessorCriteriaTriggers.botania_register(RelicBindTrigger.INSTANCE);
		AccessorCriteriaTriggers.botania_register(UseItemSuccessTrigger.INSTANCE);
		AccessorCriteriaTriggers.botania_register(ManaGunTrigger.INSTANCE);
		AccessorCriteriaTriggers.botania_register(LokiPlaceTrigger.INSTANCE);
		AccessorCriteriaTriggers.botania_register(AlfPortalBreadTrigger.INSTANCE);
	}
}
