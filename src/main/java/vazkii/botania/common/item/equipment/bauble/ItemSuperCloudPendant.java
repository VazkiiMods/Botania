/*
 * This class is distributed as part of the Botania Mod.
 * Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 *
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 */
package vazkii.botania.common.item.equipment.bauble;

public class ItemSuperCloudPendant extends ItemCloudPendant {

	public ItemSuperCloudPendant(Properties props) {
		super(props);
	}

	@Override
	public int getMaxAllowedJumps() {
		return 3;
	}
}
