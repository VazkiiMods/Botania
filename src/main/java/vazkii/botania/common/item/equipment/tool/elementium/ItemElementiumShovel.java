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
import net.minecraft.block.FallingBlock;
import net.minecraft.block.material.Material;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3i;
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
		Material mat = world.getBlockState(pos).getMaterial();
		if (!ToolCommons.materialsShovel.contains(mat)) {
			return false;
		}

		Block blk = world.getBlockState(pos).getBlock();
		if (blk instanceof FallingBlock) {
			ToolCommons.removeBlocksInIteration(player, stack, world, pos, new Vec3i(0, -12, 0),
					new Vec3i(1, 12, 1),
					state -> state.getBlock() == blk,
					false);
		}

		return false;
	}

}
