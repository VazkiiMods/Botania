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
import net.minecraft.core.Direction;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobSpawnType;
import net.minecraft.world.entity.monster.Blaze;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.ServerLevelAccessor;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;

import vazkii.botania.mixin.AccessorMob;

import static vazkii.botania.common.lib.ResourceLocationHelper.prefix;

public class BlockFelPumpkin extends BlockMod {
	private static final ResourceLocation LOOT_TABLE = prefix("fel_blaze");

	public BlockFelPumpkin(Properties builder) {
		super(builder);
		registerDefaultState(defaultBlockState().setValue(BlockStateProperties.HORIZONTAL_FACING, Direction.SOUTH));
	}

	@Override
	protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
		builder.add(BlockStateProperties.HORIZONTAL_FACING);
	}

	@Override
	public void onPlace(BlockState state, Level world, BlockPos pos, BlockState oldState, boolean isMoving) {
		super.onPlace(state, world, pos, oldState, isMoving);

		if (!world.isClientSide && world.getBlockState(pos.below()).is(Blocks.IRON_BARS) && world.getBlockState(pos.below(2)).is(Blocks.IRON_BARS)) {
			world.removeBlock(pos, false);
			world.removeBlock(pos.below(), false);
			world.removeBlock(pos.below(2), false);
			Blaze blaze = EntityType.BLAZE.create(world);
			blaze.moveTo(pos.getX() + 0.5D, pos.getY() - 1.95D, pos.getZ() + 0.5D, 0.0F, 0.0F);
			blaze.setPersistenceRequired();
			((AccessorMob) blaze).setLootTable(LOOT_TABLE);
			blaze.finalizeSpawn((ServerLevelAccessor) world, world.getCurrentDifficultyAt(pos), MobSpawnType.EVENT, null, null);
			world.addFreshEntity(blaze);
		}
	}

	@Override
	public BlockState getStateForPlacement(BlockPlaceContext context) {
		return defaultBlockState().setValue(BlockStateProperties.HORIZONTAL_FACING, context.getHorizontalDirection().getOpposite());
	}

}
