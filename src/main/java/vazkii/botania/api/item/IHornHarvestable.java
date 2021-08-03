/*
 * This class is distributed as part of the Botania Mod.
 * Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 *
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 */
package vazkii.botania.api.item;

import net.minecraft.core.BlockPos;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;

/**
 * This interface handles uprooting by the various horns in Botania.
 * Implementations can be registered via BotaniaAPI#registerHornHarvestableBlock
 */
public interface IHornHarvestable {

	/**
	 * Returns true if this block can be uprooted.
	 * Note that the stack param can be empty if it's a drum breaking it.
	 */
	boolean canHornHarvest(Level world, BlockPos pos, ItemStack stack, EnumHornType hornType);

	/**
	 * Returns true if harvestByHorn() should be called. If false it just uses the normal
	 * block breaking method.
	 * Note that the stack param can be empty if it's a drum breaking it.
	 */
	boolean hasSpecialHornHarvest(Level world, BlockPos pos, ItemStack stack, EnumHornType hornType);

	/**
	 * Called to harvest by a horn.
	 * Note that the stack param can be empty if it's a drum breaking it.
	 */
	void harvestByHorn(Level world, BlockPos pos, ItemStack stack, EnumHornType hornType);

	enum EnumHornType {

		/**
		 * Horn of the Wild, for grass and crops
		 */
		WILD,

		/**
		 * Horn of the Canopy, for leaves
		 */
		CANOPY,

		/**
		 * Horn of the Covering, for snow
		 */
		COVERING
	};

}
