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
import net.minecraft.dispenser.DefaultDispenseItemBehavior;
import net.minecraft.dispenser.IBlockSource;
import net.minecraft.dispenser.IDispenseItemBehavior;
import net.minecraft.dispenser.OptionalDispenseBehavior;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.DispenserTileEntity;
import net.minecraft.tileentity.HopperTileEntity;
import net.minecraft.util.Direction;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import vazkii.botania.common.item.ModItems;
import vazkii.botania.common.item.material.ItemEnderAir;

import javax.annotation.Nonnull;

public class BehaviourEnderAirBottling extends OptionalDispenseBehavior {
	private final DefaultDispenseItemBehavior defaultBehaviour = new DefaultDispenseItemBehavior();
	private final IDispenseItemBehavior parent;

	public BehaviourEnderAirBottling(IDispenseItemBehavior parent) {
		this.parent = parent;
	}

	@Override
	protected void playDispenseSound(IBlockSource source) {
		if (this.func_239795_a_()) {
			super.playDispenseSound(source);
		}
	}

	@Override
	protected void spawnDispenseParticles(IBlockSource source, Direction facingIn) {
		if (this.func_239795_a_()) {
			super.spawnDispenseParticles(source, facingIn);
		}
	}

	@Nonnull
	@Override
	protected ItemStack dispenseStack(IBlockSource source, @Nonnull ItemStack stack) {
		World world = source.getWorld();
		BlockPos blockpos = source.getBlockPos().offset(source.getBlockState().get(DispenserBlock.FACING));
		if (world.func_234923_W_() == World.field_234920_i_
				&& world.isAirBlock(blockpos) && world.isAirBlock(blockpos.up())
				&& ItemEnderAir.isClearFromDragonBreath(world, new AxisAlignedBB(blockpos).grow(2.0D))) {
			this.func_239796_a_(true);
			return fillBottle(source, stack, new ItemStack(ModItems.enderAirBottle));
		}
		this.func_239796_a_(false);
		parent.dispense(source, stack);
		return stack;
	}

	private ItemStack fillBottle(IBlockSource source, ItemStack input, ItemStack output) {
		input.shrink(1);
		if (input.isEmpty()) {
			return output.copy();
		} else {
			if (!HopperTileEntity.putStackInInventoryAllSlots(null,
					source.<DispenserTileEntity>getBlockTileEntity(), output.copy(), null).isEmpty()) {
				this.defaultBehaviour.dispense(source, output.copy());
			}
			return input;
		}
	}
}
