/*
 * This class is distributed as part of the Botania Mod.
 * Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 *
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 */
package vazkii.botania.common.advancements;

import net.minecraft.advancement.criterion.Criteria;

public class ModCriteriaTriggers {
	public static void init() {
		Criteria.register(AlfPortalTrigger.INSTANCE);
		Criteria.register(CorporeaRequestTrigger.INSTANCE);
		Criteria.register(DopplegangerNoArmorTrigger.INSTANCE);
		Criteria.register(RelicBindTrigger.INSTANCE);
		Criteria.register(UseItemSuccessTrigger.INSTANCE);
		Criteria.register(ManaGunTrigger.INSTANCE);
		Criteria.register(LokiPlaceTrigger.INSTANCE);
		Criteria.register(AlfPortalBreadTrigger.INSTANCE);
	}
}
