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
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.passive.EntitySheep;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;
import vazkii.botania.api.internal.VanillaPacketDispatcher;
import vazkii.botania.api.item.IDyablePool;
import vazkii.botania.api.item.IManaDissolvable;
import vazkii.botania.api.mana.IManaPool;
import vazkii.botania.common.item.Item16Colors;
import vazkii.botania.common.lib.LibItemNames;

public class ItemDye extends Item16Colors {

	public ItemDye() {
		super(LibItemNames.DYE);
	}

	@Override
	public boolean onItemUse(ItemStack par1ItemStack, EntityPlayer par2EntityPlayer, World par3World, int par4, int par5, int par6, int par7, float par8, float par9, float par10) {
		Block block = par3World.getBlock(par4, par5, par6);
		int meta = par1ItemStack.getItemDamage();
		if(meta != par3World.getBlockMetadata(par4, par5, par6) && (block == Blocks.wool || block == Blocks.carpet)) {
			par3World.setBlockMetadataWithNotify(par4, par5, par6, meta, 1 | 2);
			par1ItemStack.stackSize--;
			return true;
		}
		
		TileEntity tile = par3World.getTileEntity(par4, par5, par6);
		if(tile instanceof IDyablePool) {
			IDyablePool dyable = (IDyablePool) tile;
			int itemMeta = par1ItemStack.getItemDamage();
			if(meta != dyable.getColor()) {
				dyable.setColor(meta);
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
			int i = p_111207_1_.getItemDamage();

			if(!entitysheep.getSheared() && entitysheep.getFleeceColor() != i) {
				entitysheep.setFleeceColor(i);
				--p_111207_1_.stackSize;
			}

			return true;
		}
		return false;
	}

}
