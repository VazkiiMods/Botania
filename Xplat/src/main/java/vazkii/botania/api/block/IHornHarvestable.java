/*
 * This class is distributed as part of the Botania Mod.
 * Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 *
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 */
package vazkii.botania.api.block;

import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;

import javax.annotation.Nullable;

/**
 * Blocks with this capability can express custom logic when being
 * harvested by Botania horns.
 */
@FunctionalInterface
public interface IHornHarvestable {

	@Deprecated(forRemoval = true) // use overload with LivingEntity
	boolean canHornHarvest(Level world, BlockPos pos, ItemStack stack, EnumHornType hornType);

	/**
	 * Returns true if this block can be uprooted.
	 * Note that the stack param can be empty if it's a drum breaking it.
	 */
	default boolean canHornHarvest(Level level, BlockPos pos, ItemStack stack, EnumHornType hornType, @Nullable LivingEntity user) {
		return canHornHarvest(level, pos, stack, hornType);
	}

	@Deprecated(forRemoval = true) // use overload with LivingEntity
	default boolean hasSpecialHornHarvest(Level world, BlockPos pos, ItemStack stack, EnumHornType hornType) {
		return false;
	}

	/**
	 * Returns true if harvestByHorn() should be called. If false it just uses the normal
	 * block breaking method.
	 * Note that the stack param can be empty if it's a drum breaking it.
	 */
	default boolean hasSpecialHornHarvest(Level level, BlockPos pos, ItemStack stack, EnumHornType hornType, @Nullable LivingEntity user) {
		return hasSpecialHornHarvest(level, pos, stack, hornType);
	}

	@Deprecated(forRemoval = true) // Use overload with LivingEntity
	default void harvestByHorn(Level world, BlockPos pos, ItemStack stack, EnumHornType hornType) {}

	/**
	 * Called to harvest by a horn.
	 * Note that the stack param can be empty if it's a drum breaking it.
	 */
	default void harvestByHorn(Level level, BlockPos pos, ItemStack stack, EnumHornType hornType, @Nullable LivingEntity user) {
		harvestByHorn(level, pos, stack, hornType);
	}

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
	}

}
