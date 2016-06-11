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
	public EnumActionResult onItemUse(ItemStack par1ItemStack, EntityPlayer par2EntityPlayer, World par3World, BlockPos pos, EnumHand hand, EnumFacing side, float par8, float par9, float par10) {
		Block block = par3World.getBlockState(pos).getBlock();
		EnumDyeColor color = EnumDyeColor.byMetadata(par1ItemStack.getItemDamage());
		if(block == Blocks.WOOL && color != par3World.getBlockState(pos).getValue(BlockColored.COLOR)
				|| block == Blocks.CARPET && color != par3World.getBlockState(pos).getValue(BlockCarpet.COLOR)) {
			par3World.setBlockState(pos, par3World.getBlockState(pos).withProperty(block == Blocks.WOOL ? BlockColored.COLOR : BlockCarpet.COLOR, color), 1 | 2);
			par1ItemStack.stackSize--;
			return EnumActionResult.SUCCESS;
		}

		TileEntity tile = par3World.getTileEntity(pos);
		if(tile instanceof IManaPool) {
			IManaPool pool = (IManaPool) tile;
			if(color != pool.getColor()) {
				pool.setColor(color);
				par1ItemStack.stackSize--;
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
				--stack.stackSize;
			}

			return true;
		}
		return false;
	}

}
