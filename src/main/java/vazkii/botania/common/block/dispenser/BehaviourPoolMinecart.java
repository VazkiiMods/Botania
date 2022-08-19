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

import net.minecraft.block.Block;
import net.minecraft.block.BlockDispenser;
import net.minecraft.block.BlockRailBase;
import net.minecraft.block.material.Material;
import net.minecraft.dispenser.BehaviorDefaultDispenseItem;
import net.minecraft.dispenser.IBlockSource;
import net.minecraft.entity.item.EntityMinecart;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import net.minecraft.world.World;
import vazkii.botania.common.entity.EntityPoolMinecart;

public class BehaviourPoolMinecart extends BehaviorDefaultDispenseItem {

	@Override
	public ItemStack dispenseStack(IBlockSource p_82487_1_, ItemStack p_82487_2_) {
		EnumFacing enumfacing = BlockDispenser.func_149937_b(p_82487_1_.getBlockMetadata());
		World world = p_82487_1_.getWorld();
		double d0 = p_82487_1_.getX() + enumfacing.getFrontOffsetX() * 1.125F;
		double d1 = p_82487_1_.getY() + enumfacing.getFrontOffsetY() * 1.125F;
		double d2 = p_82487_1_.getZ() + enumfacing.getFrontOffsetZ() * 1.125F;
		int i = p_82487_1_.getXInt() + enumfacing.getFrontOffsetX();
		int j = p_82487_1_.getYInt() + enumfacing.getFrontOffsetY();
		int k = p_82487_1_.getZInt() + enumfacing.getFrontOffsetZ();
		Block block = world.getBlock(i, j, k);
		double d3;

		if(BlockRailBase.func_150051_a(block))
			d3 = 0.0D;
		else {
			if(block.getMaterial() != Material.air || !BlockRailBase.func_150051_a(world.getBlock(i, j - 1, k)))
				return super.dispenseStack(p_82487_1_, p_82487_2_);

			d3 = -1.0D;
		}

		EntityMinecart entityminecart = new EntityPoolMinecart(world, d0, d1 + d3, d2);

		if(p_82487_2_.hasDisplayName())
			entityminecart.setMinecartName(p_82487_2_.getDisplayName());

		world.spawnEntityInWorld(entityminecart);
		p_82487_2_.splitStack(1);
		return p_82487_2_;
	}

	@Override
	protected void playDispenseSound(IBlockSource p_82485_1_) {
		p_82485_1_.getWorld().playAuxSFX(1000, p_82485_1_.getXInt(), p_82485_1_.getYInt(), p_82485_1_.getZInt(), 0);
	}

}
