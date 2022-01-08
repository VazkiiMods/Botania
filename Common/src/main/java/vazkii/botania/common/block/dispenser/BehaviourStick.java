/*
 * This class is distributed as part of the Botania Mod.
 * Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 *
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 */
package vazkii.botania.common.block.dispenser;

import net.minecraft.core.BlockPos;
import net.minecraft.core.BlockSource;
import net.minecraft.core.Direction;
import net.minecraft.core.dispenser.OptionalDispenseItemBehavior;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.DispenserBlock;

import vazkii.botania.common.item.ItemObedienceStick;

import javax.annotation.Nonnull;

public class BehaviourStick extends OptionalDispenseItemBehavior {

	@Nonnull
	@Override
	protected ItemStack execute(BlockSource source, ItemStack stack) {
		Level world = source.getLevel();
		Direction facing = world.getBlockState(source.getPos()).getValue(DispenserBlock.FACING);
		BlockPos pos = source.getPos().relative(facing);

		setSuccess(ItemObedienceStick.applyStick(world, pos));

		return stack;
	}

}
