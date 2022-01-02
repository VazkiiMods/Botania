/*
 * This class is distributed as part of the Botania Mod.
 * Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 *
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 */
package vazkii.botania.common.block.decor;

import net.minecraft.core.BlockPos;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.state.BlockState;

import vazkii.botania.api.block.IHornHarvestable;
import vazkii.botania.common.block.BlockModFlower;
import vazkii.botania.common.impl.DefaultHornHarvestable;

import javax.annotation.Nonnull;

public class BlockShinyFlower extends BlockModFlower {

	public BlockShinyFlower(DyeColor color, Properties builder) {
		super(color, builder);
		IHornHarvestable.API.registerForBlocks((w, p, s, be, c) -> DefaultHornHarvestable.INSTANCE, this);
	}

	@Override
	public boolean isValidBonemealTarget(@Nonnull BlockGetter world, @Nonnull BlockPos pos, @Nonnull BlockState state, boolean fuckifiknow) {
		return false;
	}

}
