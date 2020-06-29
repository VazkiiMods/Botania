/*
 * This class is distributed as part of the Botania Mod.
 * Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 *
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 */
package vazkii.botania.common.block.tile;

import net.minecraft.block.Blocks;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.EquipmentSlotType;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.state.properties.BlockStateProperties;
import net.minecraft.tileentity.ITickableTileEntity;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.util.math.RayTraceResult;

import vazkii.botania.common.item.equipment.tool.ToolCommons;

import java.util.List;

public class TileEnderEye extends TileMod implements ITickableTileEntity {
	public TileEnderEye() {
		super(ModTiles.ENDER_EYE);
	}

	@Override
	public void tick() {
		if (world.isRemote) {
			return;
		}

		boolean wasLooking = getBlockState().get(BlockStateProperties.POWERED);
		int range = 80;
		List<PlayerEntity> players = world.getEntitiesWithinAABB(PlayerEntity.class, new AxisAlignedBB(pos.add(-range, -range, -range), pos.add(range, range, range)));

		boolean looking = false;
		for (PlayerEntity player : players) {
			ItemStack helm = player.getItemStackFromSlot(EquipmentSlotType.HEAD);
			if (!helm.isEmpty() && helm.getItem() == Item.getItemFromBlock(Blocks.PUMPKIN)) {
				continue;
			}

			BlockRayTraceResult hit = ToolCommons.raytraceFromEntity(player, 64, false);
			if (hit.getType() == RayTraceResult.Type.BLOCK && hit.getPos().equals(getPos())) {
				looking = true;
				break;
			}
		}

		if (looking != wasLooking) {
			world.setBlockState(getPos(), getBlockState().with(BlockStateProperties.POWERED, looking));
		}
	}

}
