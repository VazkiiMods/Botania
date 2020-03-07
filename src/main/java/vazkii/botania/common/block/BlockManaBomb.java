/*
 * This class is distributed as part of the Botania Mod.
 * Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 *
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 */
package vazkii.botania.common.block;

import net.minecraft.block.Block;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import vazkii.botania.api.internal.IManaBurst;
import vazkii.botania.api.mana.IManaTrigger;
import vazkii.botania.common.entity.EntityManaStorm;

public class BlockManaBomb extends BlockMod implements IManaTrigger {

	public BlockManaBomb(Properties builder) {
		super(builder);
	}

	@Override
	public void onBurstCollision(IManaBurst burst, World world, BlockPos pos) {
		if (!burst.isFake() && !world.isRemote) {
			world.playEvent(2001, pos, Block.getStateId(getDefaultState()));
			world.removeBlock(pos, false);
			EntityManaStorm storm = new EntityManaStorm(world);
			storm.setPosition(pos.getX() + 0.5, pos.getY() + 0.5, pos.getZ() + 0.5);
			world.addEntity(storm);
		}
	}
}
