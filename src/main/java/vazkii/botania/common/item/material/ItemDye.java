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
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.world.World;
import vazkii.botania.api.item.IDyablePool;
import vazkii.botania.common.item.Item16Colors;
import vazkii.botania.common.lib.LibItemNames;

public class ItemDye extends Item16Colors {

	public ItemDye() {
		super(LibItemNames.DYE);
	}

	@Override
	public boolean onItemUse(ItemStack par1ItemStack, EntityPlayer par2EntityPlayer, World par3World, BlockPos pos, EnumFacing side, float par8, float par9, float par10) {
		Block block = par3World.getBlockState(pos).getBlock();
		EnumDyeColor color = EnumDyeColor.byMetadata(par1ItemStack.getItemDamage());
		if(block == Blocks.wool && color != par3World.getBlockState(pos).getValue(BlockColored.COLOR)
				|| block == Blocks.carpet && color != par3World.getBlockState(pos).getValue(BlockCarpet.COLOR)) {
			par3World.setBlockState(pos, par3World.getBlockState(pos).withProperty(block == Blocks.wool ? BlockColored.COLOR : BlockCarpet.COLOR, color), 1 | 2);
			par1ItemStack.stackSize--;
			return true;
		}

		TileEntity tile = par3World.getTileEntity(pos);
		if(tile instanceof IDyablePool) {
			IDyablePool dyable = (IDyablePool) tile;
			if(color != dyable.getColor()) {
				dyable.setColor(color);
				par1ItemStack.stackSize--;
				return true;
			}
		}

		return false;
	}

	@Override
	public boolean itemInteractionForEntity(ItemStack p_111207_1_, EntityPlayer p_111207_2_, EntityLivingBase p_111207_3_) {
		if(p_111207_3_ instanceof EntitySheep) {
			EntitySheep entitysheep = (EntitySheep)p_111207_3_;
			EnumDyeColor i = EnumDyeColor.byMetadata(p_111207_1_.getItemDamage());

			if(!entitysheep.getSheared() && entitysheep.getFleeceColor() != i) {
				entitysheep.setFleeceColor(i);
				--p_111207_1_.stackSize;
			}

			return true;
		}
		return false;
	}

}
