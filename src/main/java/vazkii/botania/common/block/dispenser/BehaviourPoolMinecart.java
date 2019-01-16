/**
 * This class was created by <Vazkii>. It's distributed as
 * part of the Botania Mod. Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 *
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 *
 * File Created @ [Mar 18, 2015, 12:22:58 AM (GMT)]
 */
package vazkii.botania.common.block.dispenser;

import net.minecraft.block.BlockDispenser;
import net.minecraft.block.BlockRailBase;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.dispenser.BehaviorDefaultDispenseItem;
import net.minecraft.dispenser.IBlockSource;
import net.minecraft.entity.item.EntityMinecart;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import vazkii.botania.common.entity.EntityPoolMinecart;

import javax.annotation.Nonnull;

public class BehaviourPoolMinecart extends BehaviorDefaultDispenseItem {

	@Nonnull
	@Override
	public ItemStack dispenseStack(IBlockSource source, ItemStack stack) {
		World world = source.getWorld();
		EnumFacing enumfacing = world.getBlockState(source.getBlockPos()).getValue(BlockDispenser.FACING);
		double d0 = source.getX() + enumfacing.getXOffset() * 1.125F;
		double d1 = source.getY() + enumfacing.getYOffset() * 1.125F;
		double d2 = source.getZ() + enumfacing.getZOffset() * 1.125F;
		BlockPos pos = source.getBlockPos().offset(enumfacing);
		IBlockState state = world.getBlockState(pos);
		double d3;

		if(BlockRailBase.isRailBlock(state))
			d3 = 0.0D;
		else {
			if(state.getMaterial() != Material.AIR || !BlockRailBase.isRailBlock(world.getBlockState(pos.down())))
				return super.dispenseStack(source, stack);

			d3 = -1.0D;
		}

		EntityMinecart entityminecart = new EntityPoolMinecart(world, d0, d1 + d3, d2);

		if(stack.hasDisplayName())
			entityminecart.setCustomNameTag(stack.getDisplayName());

		world.spawnEntity(entityminecart);
		stack.splitStack(1);
		return stack;
	}

	@Override
	protected void playDispenseSound(IBlockSource source) {
		source.getWorld().playEvent(1000, source.getBlockPos(), 0);
	}

}
