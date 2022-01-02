/*
 * This class is distributed as part of the Botania Mod.
 * Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 *
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 */
package vazkii.botania.api.item;

import net.minecraft.world.item.ItemStack;

/**
 * An item that implements this can have Ancient Wills
 * crafted onto it.
 */
public interface IAncientWillContainer {
	enum AncientWillType {
		AHRIM,
		DHAROK,
		GUTHAN,
		TORAG,
		VERAC,
		KARIL
	}

	void addAncientWill(ItemStack stack, AncientWillType will);

	boolean hasAncientWill(ItemStack stack, AncientWillType will);

}
