/*
 * This class is distributed as part of the Botania Mod.
 * Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 *
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 */
package vazkii.botania.common.item.lens;

import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.HitResult;

import vazkii.botania.api.internal.ManaBurst;
import vazkii.botania.common.block.BotaniaBlocks;
import vazkii.botania.common.block.ForceRelayBlock;
import vazkii.botania.common.helper.EntityHelper;

public class WarpLens extends Lens {

	@Override
	public boolean collideBurst(ManaBurst burst, HitResult pos, boolean isManaBlock, boolean shouldKill, ItemStack stack) {
		Entity entity = burst.entity();
		Level world = entity.level();

		if (world.isClientSide || pos.getType() != HitResult.Type.BLOCK) {
			// On the client, we don't know what the force relay mappings really are,
			// so we can only pretend that we just hit a normal block.
			return shouldKill;
		}

		BlockPos hit = ((BlockHitResult) pos).getBlockPos();
		if (world.getBlockState(hit).is(BotaniaBlocks.pistonRelay)) {
			ForceRelayBlock.WorldData data = ForceRelayBlock.WorldData.get(world);
			BlockPos dest = data.mapping.get(hit);

			if (dest != null) {
				BlockPos sourcePos = entity.blockPosition();
				entity.setPos(dest.getCenter());
				EntityHelper.addTeleportTicketIfFarAway(entity, sourcePos);
				burst.setCollidedAt(dest);

				burst.setWarped(true);

				return false;
			}
		}
		return shouldKill;
	}
}
