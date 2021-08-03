/*
 * This class is distributed as part of the Botania Mod.
 * Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 *
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 */
package vazkii.botania.common.block.tile;

import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.entity.TickableBlockEntity;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.HitResult;

import vazkii.botania.common.item.equipment.tool.ToolCommons;

import java.util.List;

public class TileEnderEye extends TileMod implements TickableBlockEntity {
	public TileEnderEye() {
		super(ModTiles.ENDER_EYE);
	}

	@Override
	public void tick() {
		if (level.isClientSide) {
			return;
		}

		boolean wasLooking = getBlockState().getValue(BlockStateProperties.POWERED);
		int range = 80;
		List<Player> players = level.getEntitiesOfClass(Player.class, new AABB(worldPosition.offset(-range, -range, -range), worldPosition.offset(range, range, range)));

		boolean looking = false;
		for (Player player : players) {
			ItemStack helm = player.getItemBySlot(EquipmentSlot.HEAD);
			if (!helm.isEmpty() && helm.getItem() == Blocks.PUMPKIN.asItem()) {
				continue;
			}

			BlockHitResult hit = ToolCommons.raytraceFromEntity(player, 64, false);
			if (hit.getType() == HitResult.Type.BLOCK && hit.getBlockPos().equals(getBlockPos())) {
				looking = true;
				break;
			}
		}

		if (looking != wasLooking) {
			level.setBlockAndUpdate(getBlockPos(), getBlockState().setValue(BlockStateProperties.POWERED, looking));
		}
	}

}
