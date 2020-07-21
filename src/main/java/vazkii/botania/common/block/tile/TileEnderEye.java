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
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.state.property.Properties;
import net.minecraft.util.Tickable;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.hit.HitResult;
import net.minecraft.util.math.Box;
import vazkii.botania.common.item.equipment.tool.ToolCommons;

import java.util.List;

public class TileEnderEye extends TileMod implements Tickable {
	public TileEnderEye() {
		super(ModTiles.ENDER_EYE);
	}

	@Override
	public void tick() {
		if (world.isClient) {
			return;
		}

		boolean wasLooking = getCachedState().get(Properties.POWERED);
		int range = 80;
		List<PlayerEntity> players = world.getNonSpectatingEntities(PlayerEntity.class, new Box(pos.add(-range, -range, -range), pos.add(range, range, range)));

		boolean looking = false;
		for (PlayerEntity player : players) {
			ItemStack helm = player.getEquippedStack(EquipmentSlot.HEAD);
			if (!helm.isEmpty() && helm.getItem() == Item.fromBlock(Blocks.PUMPKIN)) {
				continue;
			}

			BlockHitResult hit = ToolCommons.raytraceFromEntity(player, 64, false);
			if (hit.getType() == HitResult.Type.BLOCK && hit.getBlockPos().equals(getPos())) {
				looking = true;
				break;
			}
		}

		if (looking != wasLooking) {
			world.setBlockState(getPos(), getCachedState().with(Properties.POWERED, looking));
		}
	}

}
