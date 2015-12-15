/**
 * This class was created by <Vazkii>. It's distributed as
 * part of the Botania Mod. Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 * 
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 * 
 * File Created @ [Jan 24, 2015, 10:28:03 PM (GMT)]
 */
package vazkii.botania.common.item.rod;

import net.minecraft.block.Block;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import vazkii.botania.api.item.IBlockProvider;
import vazkii.botania.api.mana.IManaUsingItem;
import vazkii.botania.api.mana.ManaItemHandler;
import vazkii.botania.common.item.ItemMod;
import vazkii.botania.common.lib.LibItemNames;

public class ItemCobbleRod extends ItemMod implements IManaUsingItem, IBlockProvider {

	static final int COST = 150;

	public ItemCobbleRod() {
		setMaxStackSize(1);
		setUnlocalizedName(LibItemNames.COBBLE_ROD);
	}

	@Override
	public boolean onItemUse(ItemStack par1ItemStack, EntityPlayer par2EntityPlayer, World par3World, int par4, int par5, int par6, int par7, float par8, float par9, float par10) {
		return ItemDirtRod.place(par1ItemStack, par2EntityPlayer, par3World, par4, par5, par6, par7, par8, par9, par10, Blocks.cobblestone, COST, 0.3F, 0.3F, 0.3F);
	}

	@Override
	public boolean isFull3D() {
		return true;
	}

	@Override
	public boolean usesMana(ItemStack stack) {
		return true;
	}

	@Override
	public boolean provideBlock(EntityPlayer player, ItemStack requestor, ItemStack stack, Block block, int meta, boolean doit) {
		if(block == Blocks.cobblestone && meta == 0)
			return !doit || ManaItemHandler.requestManaExactForTool(requestor, player, COST, true);
		return false;
	}

	@Override
	public int getBlockCount(EntityPlayer player, ItemStack requestor, ItemStack stack, Block block, int meta) {
		if(block == Blocks.cobblestone && meta == 0)
			return -1;
		return 0;
	}

}
