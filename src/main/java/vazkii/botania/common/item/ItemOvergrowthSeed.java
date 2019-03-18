/**
 * This class was created by <Vazkii>. It's distributed as
 * part of the Botania Mod. Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 *
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 *
 * File Created @ [Jan 26, 2015, 5:59:21 PM (GMT)]
 */
package vazkii.botania.common.item;

import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemUseContext;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import vazkii.botania.common.block.ModBlocks;
import vazkii.botania.common.lib.LibItemNames;

import javax.annotation.Nonnull;

public class ItemOvergrowthSeed extends ItemMod {

	public ItemOvergrowthSeed(Properties props) {
		super(props);
	}

	@Nonnull
	@Override
	public EnumActionResult onItemUse(ItemUseContext ctx) {
		World world = ctx.getWorld();
		BlockPos pos = ctx.getPos();

		// todo 1.13 grass tag
		IBlockState state = world.getBlockState(pos);
		if(state.getBlock() == Blocks.GRASS_BLOCK) {
			if(!world.isRemote) {
				world.playEvent(2001, pos, Block.getStateId(state));
				world.setBlockState(pos, ModBlocks.enchantedSoil.getDefaultState());
				ctx.getItem().shrink(1);
			}
			return EnumActionResult.SUCCESS;
		}
		return EnumActionResult.PASS;
	}

}
