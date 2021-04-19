/*
 * This class is distributed as part of the Botania Mod.
 * Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 *
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 */
package vazkii.botania.common.item.equipment.tool.elementium;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.FallingBlock;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.vector.Vector3i;
import net.minecraft.world.World;

import vazkii.botania.api.BotaniaAPI;
import vazkii.botania.common.item.equipment.tool.ToolCommons;
import vazkii.botania.common.item.equipment.tool.manasteel.ItemManasteelShovel;

public class ItemElementiumShovel extends ItemManasteelShovel {

	public ItemElementiumShovel(Properties props) {
		super(BotaniaAPI.instance().getElementiumItemTier(), props);
	}

	@Override
	public boolean onBlockStartBreak(ItemStack stack, BlockPos pos, PlayerEntity player) {
		World world = player.world;
		BlockState blockState = world.getBlockState(pos);
		if (this.getDestroySpeed(stack, blockState) <= 1.0F) {
			return false;
		}

		Block blk = blockState.getBlock();
		if (blk instanceof FallingBlock) {
			ToolCommons.removeBlocksInIteration(player, stack, world, pos, new Vector3i(0, -12, 0),
					new Vector3i(1, 12, 1),
					state -> state.getBlock() == blk);
		}

		return false;
	}

}
