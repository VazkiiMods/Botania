/**
 * This class was created by <Vazkii>. It's distributed as
 * part of the Botania Mod. Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 *
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 *
 * File Created @ [Jan 19, 2014, 4:10:47 PM (GMT)]
 */
package vazkii.botania.common.item.material;

import net.minecraft.block.Block;
import net.minecraft.block.BlockCarpet;
import net.minecraft.block.BlockColored;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.passive.EntitySheep;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.item.EnumDyeColor;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import vazkii.botania.api.mana.IManaPool;
import vazkii.botania.common.item.Item16Colors;
import vazkii.botania.common.lib.LibItemNames;

import javax.annotation.Nonnull;

public class ItemDye extends Item16Colors {

	public ItemDye() {
		super(LibItemNames.DYE);
	}

	@Nonnull
	@Override
	public EnumActionResult onItemUse(EntityPlayer player, World world, BlockPos pos, EnumHand hand, EnumFacing side, float par8, float par9, float par10) {
		ItemStack stack = player.getHeldItem(hand);
		Block block = world.getBlockState(pos).getBlock();
		EnumDyeColor color = EnumDyeColor.byMetadata(stack.getItemDamage());
		if(block == Blocks.WOOL && color != world.getBlockState(pos).getValue(BlockColored.COLOR)
				|| block == Blocks.CARPET && color != world.getBlockState(pos).getValue(BlockCarpet.COLOR)) {
			world.setBlockState(pos, world.getBlockState(pos).withProperty(block == Blocks.WOOL ? BlockColored.COLOR : BlockCarpet.COLOR, color), 1 | 2);
			stack.shrink(1);
			return EnumActionResult.SUCCESS;
		}

		TileEntity tile = world.getTileEntity(pos);
		if(tile instanceof IManaPool) {
			IManaPool pool = (IManaPool) tile;
			if(color != pool.getColor()) {
				pool.setColor(color);
				stack.shrink(1);
				return EnumActionResult.SUCCESS;
			}
		}

		return EnumActionResult.PASS;
	}

	@Override
	public boolean itemInteractionForEntity(ItemStack stack, EntityPlayer player, EntityLivingBase target, EnumHand hand) {
		if(target instanceof EntitySheep) {
			EntitySheep entitysheep = (EntitySheep)target;
			EnumDyeColor i = EnumDyeColor.byMetadata(stack.getItemDamage());

			if(!entitysheep.getSheared() && entitysheep.getFleeceColor() != i) {
				entitysheep.setFleeceColor(i);
				stack.shrink(1);
			}

			return true;
		}
		return false;
	}

}
