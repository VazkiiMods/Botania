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
import net.minecraft.core.dispenser.DefaultDispenseItemBehavior;
import net.minecraft.core.dispenser.DispenseItemBehavior;
import net.minecraft.core.dispenser.OptionalDispenseItemBehavior;
import net.minecraft.world.entity.EntitySelector;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.DispenserBlock;
import net.minecraft.world.level.block.entity.DispenserBlockEntity;
import net.minecraft.world.phys.AABB;

import vazkii.botania.common.entity.EntityEnderAir;
import vazkii.botania.common.item.ModItems;
import vazkii.botania.common.item.material.ItemEnderAir;

import javax.annotation.Nonnull;

public class BehaviourEnderAirBottling extends OptionalDispenseItemBehavior {
	private final DefaultDispenseItemBehavior defaultBehaviour = new DefaultDispenseItemBehavior();
	private final DispenseItemBehavior parent;

	public BehaviourEnderAirBottling(DispenseItemBehavior parent) {
		this.parent = parent;
	}

	@Override
	protected void playSound(BlockSource source) {
		if (this.isSuccess()) {
			super.playSound(source);
		}
	}

	@Override
	protected void playAnimation(BlockSource source, Direction facingIn) {
		if (this.isSuccess()) {
			super.playAnimation(source, facingIn);
		}
	}

	private boolean pickupInEnd(Level world, BlockPos facingPos) {
		return world.dimension() == Level.END
				&& world.isEmptyBlock(facingPos) && world.isEmptyBlock(facingPos.above())
				&& ItemEnderAir.isClearFromDragonBreath(world, new AABB(facingPos).inflate(2.0D));
	}

	private boolean pickupFromEntity(Level level, BlockPos facingPos) {
		var entities = level.getEntitiesOfClass(EntityEnderAir.class, new AABB(facingPos), EntitySelector.ENTITY_STILL_ALIVE);
		if (!entities.isEmpty()) {
			entities.get(0).discard();
			return true;
		}
		return false;
	}

	@Nonnull
	@Override
	protected ItemStack execute(BlockSource source, @Nonnull ItemStack stack) {
		Level world = source.getLevel();
		BlockPos blockpos = source.getPos().relative(source.getBlockState().getValue(DispenserBlock.FACING));
		if (pickupInEnd(world, blockpos) || pickupFromEntity(world, blockpos)) {
			this.setSuccess(true);
			return fillBottle(source, stack, new ItemStack(ModItems.enderAirBottle));
		}
		this.setSuccess(false);
		return parent.dispense(source, stack);
	}

	private ItemStack fillBottle(BlockSource source, ItemStack input, ItemStack output) {
		input.shrink(1);
		if (input.isEmpty()) {
			return output.copy();
		} else {
			if (((DispenserBlockEntity) source.getEntity()).addItem(output.copy()) < 0) {
				this.defaultBehaviour.dispense(source, output.copy());
			}
			return input;
		}
	}
}
