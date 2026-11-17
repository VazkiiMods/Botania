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
import net.minecraft.core.Direction;
import net.minecraft.core.dispenser.BlockSource;
import net.minecraft.core.dispenser.DefaultDispenseItemBehavior;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.tags.BlockTags;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.DispenserBlock;
import net.minecraft.world.level.block.LevelEvent;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.RailShape;
import net.minecraft.world.phys.Vec3;

import vazkii.botania.common.entity.ManaPoolMinecartEntity;
import vazkii.botania.xplat.XplatAbstractions;

// [VanillaCopy] MinecartItem.DISPENSE_ITEM_BEHAVIOR
public class ManaPoolMinecartBehavior extends DefaultDispenseItemBehavior {
	private final DefaultDispenseItemBehavior behaviourDefaultDispenseItem = new DefaultDispenseItemBehavior();

	@Override
	public ItemStack execute(BlockSource source, ItemStack stack) {
		Direction direction = source.state().getValue(DispenserBlock.FACING);
		ServerLevel level = source.level();
		Vec3 center = source.center();
		double x = center.x() + (double) direction.getStepX() * 1.125;
		double y = Math.floor(center.y()) + (double) direction.getStepY();
		double z = center.z() + (double) direction.getStepZ() * 1.125;
		BlockPos blockPos = source.pos().relative(direction);
		BlockState blockState = level.getBlockState(blockPos);
		RailShape railShape = XplatAbstractions.INSTANCE.getRailDirection(blockState, level, blockPos, null);
		double yOffset;
		if (blockState.is(BlockTags.RAILS)) {
			if (railShape.isAscending()) {
				yOffset = 0.6;
			} else {
				yOffset = 0.1;
			}
		} else {
			if (!blockState.isAir() || !level.getBlockState(blockPos.below()).is(BlockTags.RAILS)) {
				return this.behaviourDefaultDispenseItem.dispense(source, stack);
			}

			BlockState blockStateBelow = level.getBlockState(blockPos.below());
			RailShape railShapeBelow = XplatAbstractions.INSTANCE.getRailDirection(blockStateBelow, level, blockPos, null);
			if (direction != Direction.DOWN && railShapeBelow.isAscending()) {
				yOffset = -0.4;
			} else {
				yOffset = -0.9;
			}
		}

		ManaPoolMinecartEntity minecartEntity = ManaPoolMinecartEntity.createMinecart(
				level, x, y + yOffset, z, stack, null
		);
		level.addFreshEntity(minecartEntity);
		stack.shrink(1);
		return stack;
	}

	@Override
	protected void playSound(BlockSource source) {
		source.level().levelEvent(LevelEvent.SOUND_DISPENSER_DISPENSE, source.pos(), 0);
	}

}
