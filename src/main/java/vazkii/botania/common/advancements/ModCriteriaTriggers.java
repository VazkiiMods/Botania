/*
 * This class is distributed as part of the Botania Mod.
 * Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 *
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 */
package vazkii.botania.common.advancements;

import net.minecraft.advancements.CriteriaTriggers;

public class ModCriteriaTriggers {
	public static void init() {
		CriteriaTriggers.register(AlfPortalTrigger.INSTANCE);
		CriteriaTriggers.register(CorporeaRequestTrigger.INSTANCE);
		CriteriaTriggers.register(DopplegangerNoArmorTrigger.INSTANCE);
		CriteriaTriggers.register(RelicBindTrigger.INSTANCE);
		CriteriaTriggers.register(UseItemSuccessTrigger.INSTANCE);
		CriteriaTriggers.register(ManaGunTrigger.INSTANCE);
		CriteriaTriggers.register(LokiPlaceTrigger.INSTANCE);
		CriteriaTriggers.register(AlfPortalBreadTrigger.INSTANCE);
	}
}
