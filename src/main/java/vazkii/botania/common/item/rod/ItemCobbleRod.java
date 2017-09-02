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
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import vazkii.botania.api.item.IBlockProvider;
import vazkii.botania.api.mana.IManaUsingItem;
import vazkii.botania.api.mana.ManaItemHandler;
import vazkii.botania.common.item.ItemMod;
import vazkii.botania.common.lib.LibItemNames;

import javax.annotation.Nonnull;

public class ItemCobbleRod extends ItemMod implements IManaUsingItem, IBlockProvider {

	static final int COST = 150;

	public ItemCobbleRod() {
		super(LibItemNames.COBBLE_ROD);
		setMaxStackSize(1);
	}

	@Nonnull
	@Override
	public EnumActionResult onItemUse(EntityPlayer player, World world, BlockPos pos, EnumHand hand, EnumFacing side, float par8, float par9, float par10) {
		return ItemDirtRod.place(player, world, pos, hand, side, par8, par9, par10, Blocks.COBBLESTONE, COST, 0.3F, 0.3F, 0.3F);
	}

	@Override
	public boolean usesMana(ItemStack stack) {
		return true;
	}

	@Override
	public boolean provideBlock(EntityPlayer player, ItemStack requestor, ItemStack stack, Block block, int meta, boolean doit) {
		if(block == Blocks.COBBLESTONE && meta == 0)
			return !doit || ManaItemHandler.requestManaExactForTool(requestor, player, COST, true);
		return false;
	}

	@Override
	public int getBlockCount(EntityPlayer player, ItemStack requestor, ItemStack stack, Block block, int meta) {
		if(block == Blocks.COBBLESTONE && meta == 0)
			return -1;
		return 0;
	}

}
