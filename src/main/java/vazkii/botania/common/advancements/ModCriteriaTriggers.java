/*
 * This class is distributed as part of the Botania Mod.
 * Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 *
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 */
package vazkii.botania.common.advancements;

import vazkii.botania.mixin.AccessorCriteria;

public class ModCriteriaTriggers {
	public static void init() {
		AccessorCriteria.botania_register(AlfPortalTrigger.INSTANCE);
		AccessorCriteria.botania_register(CorporeaRequestTrigger.INSTANCE);
		AccessorCriteria.botania_register(DopplegangerNoArmorTrigger.INSTANCE);
		AccessorCriteria.botania_register(RelicBindTrigger.INSTANCE);
		AccessorCriteria.botania_register(UseItemSuccessTrigger.INSTANCE);
		AccessorCriteria.botania_register(ManaGunTrigger.INSTANCE);
		AccessorCriteria.botania_register(LokiPlaceTrigger.INSTANCE);
		AccessorCriteria.botania_register(AlfPortalBreadTrigger.INSTANCE);
	}
}
