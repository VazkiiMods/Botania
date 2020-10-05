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
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.SpawnReason;
import net.minecraft.entity.mob.BlazeEntity;
import net.minecraft.item.ItemPlacementContext;
import net.minecraft.state.StateManager;
import net.minecraft.state.property.Properties;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.ServerWorldAccess;
import net.minecraft.world.World;
import net.minecraft.world.server.ServerWorld;

import vazkii.botania.mixin.AccessorMobEntity;

import static vazkii.botania.common.lib.ResourceLocationHelper.prefix;

public class BlockFelPumpkin extends BlockMod {
	private static final Identifier LOOT_TABLE = prefix("fel_blaze");

	public BlockFelPumpkin(Settings builder) {
		super(builder);
		setDefaultState(getDefaultState().with(Properties.HORIZONTAL_FACING, Direction.SOUTH));
	}

	@Override
	protected void appendProperties(StateManager.Builder<Block, BlockState> builder) {
		builder.add(Properties.HORIZONTAL_FACING);
	}

	@Override
	public void onBlockAdded(BlockState state, World world, BlockPos pos, BlockState oldState, boolean isMoving) {
		super.onBlockAdded(state, world, pos, oldState, isMoving);

		if (!world.isClient && world.getBlockState(pos.down()).getBlock() == Blocks.IRON_BARS && world.getBlockState(pos.down(2)).getBlock() == Blocks.IRON_BARS) {
			world.setBlockState(pos, Blocks.AIR.getDefaultState());
			world.setBlockState(pos.down(), Blocks.AIR.getDefaultState());
			world.setBlockState(pos.down(2), Blocks.AIR.getDefaultState());
			BlazeEntity blaze = EntityType.BLAZE.create(world);
			blaze.refreshPositionAndAngles(pos.getX() + 0.5D, pos.getY() - 1.95D, pos.getZ() + 0.5D, 0.0F, 0.0F);
			((AccessorMobEntity) blaze).setLootTable(LOOT_TABLE);
			blaze.initialize((ServerWorldAccess) world, world.getLocalDifficulty(pos), SpawnReason.EVENT, null, null);
			world.spawnEntity(blaze);
		}
	}

	@Override
	public BlockState getPlacementState(ItemPlacementContext context) {
		return getDefaultState().with(Properties.HORIZONTAL_FACING, context.getPlayerFacing().getOpposite());
	}

}
