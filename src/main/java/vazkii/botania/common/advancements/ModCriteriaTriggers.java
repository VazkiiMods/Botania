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
		AccessorCriteria.callRegister(AlfPortalTrigger.INSTANCE);
		AccessorCriteria.callRegister(CorporeaRequestTrigger.INSTANCE);
		AccessorCriteria.callRegister(DopplegangerNoArmorTrigger.INSTANCE);
		AccessorCriteria.callRegister(RelicBindTrigger.INSTANCE);
		AccessorCriteria.callRegister(UseItemSuccessTrigger.INSTANCE);
		AccessorCriteria.callRegister(ManaGunTrigger.INSTANCE);
		AccessorCriteria.callRegister(LokiPlaceTrigger.INSTANCE);
		AccessorCriteria.callRegister(AlfPortalBreadTrigger.INSTANCE);
	}
}
