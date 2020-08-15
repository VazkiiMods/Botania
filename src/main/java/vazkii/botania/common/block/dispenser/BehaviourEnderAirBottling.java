/*
 * This class is distributed as part of the Botania Mod.
 * Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 *
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 */
package vazkii.botania.common.block.dispenser;

import net.minecraft.block.DispenserBlock;
import net.minecraft.block.dispenser.DispenserBehavior;
import net.minecraft.block.dispenser.FallibleItemDispenserBehavior;
import net.minecraft.block.dispenser.ItemDispenserBehavior;
import net.minecraft.block.entity.DispenserBlockEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPointer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.Direction;
import net.minecraft.world.World;

import vazkii.botania.common.item.ModItems;
import vazkii.botania.common.item.material.ItemEnderAir;

import javax.annotation.Nonnull;

public class BehaviourEnderAirBottling extends FallibleItemDispenserBehavior {
	private final ItemDispenserBehavior defaultBehaviour = new ItemDispenserBehavior();
	private final DispenserBehavior parent;

	public BehaviourEnderAirBottling(DispenserBehavior parent) {
		this.parent = parent;
	}

	@Override
	protected void playSound(BlockPointer source) {
		if (this.isSuccess()) {
			super.playSound(source);
		}
	}

	@Override
	protected void spawnParticles(BlockPointer source, Direction facingIn) {
		if (this.isSuccess()) {
			super.spawnParticles(source, facingIn);
		}
	}

	@Nonnull
	@Override
	protected ItemStack dispenseSilently(BlockPointer source, @Nonnull ItemStack stack) {
		World world = source.getWorld();
		BlockPos blockpos = source.getBlockPos().offset(source.getBlockState().get(DispenserBlock.FACING));
		if (world.getRegistryKey() == World.END
				&& world.isAir(blockpos) && world.isAir(blockpos.up())
				&& ItemEnderAir.isClearFromDragonBreath(world, new Box(blockpos).expand(2.0D))) {
			this.setSuccess(true);
			return fillBottle(source, stack, new ItemStack(ModItems.enderAirBottle));
		}
		this.setSuccess(false);
		return parent.dispense(source, stack);
	}

	private ItemStack fillBottle(BlockPointer source, ItemStack input, ItemStack output) {
		input.decrement(1);
		if (input.isEmpty()) {
			return output.copy();
		} else {
			if (((DispenserBlockEntity) source.getBlockEntity()).addToFirstFreeSlot(output.copy()) < 0) {
				this.defaultBehaviour.dispense(source, output.copy());
			}
			return input;
		}
	}
}
