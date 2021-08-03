/*
 * This class is distributed as part of the Botania Mod.
 * Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 *
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 */
package vazkii.botania.common.block;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;

import vazkii.botania.api.internal.IManaBurst;
import vazkii.botania.api.mana.IManaTrigger;
import vazkii.botania.common.entity.EntityManaStorm;
import vazkii.botania.common.entity.ModEntities;

public class BlockManaBomb extends BlockMod implements IManaTrigger {

	public BlockManaBomb(Properties builder) {
		super(builder);
	}

	@Override
	public void onBurstCollision(IManaBurst burst, Level world, BlockPos pos) {
		if (!burst.isFake() && !world.isClientSide) {
			world.levelEvent(2001, pos, Block.getId(defaultBlockState()));
			world.removeBlock(pos, false);
			EntityManaStorm storm = ModEntities.MANA_STORM.create(world);
			storm.burstColor = burst.getColor();
			storm.setPos(pos.getX() + 0.5, pos.getY() + 0.5, pos.getZ() + 0.5);
			world.addFreshEntity(storm);
		}
	}
}
