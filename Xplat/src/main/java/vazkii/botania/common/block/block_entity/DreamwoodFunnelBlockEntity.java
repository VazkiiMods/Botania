package vazkii.botania.common.block.block_entity;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;

import vazkii.botania.common.block.DreamwoodFunnelBlock;

public class DreamwoodFunnelBlockEntity extends OpenCrateBlockEntity {
	public DreamwoodFunnelBlockEntity(BlockPos pos, BlockState state) {
		super(BotaniaBlockEntities.DREAMWOOD_FUNNEL, pos, state);
	}

	@Override
	protected Vec3 getEjectPosition() {
		Direction direction = getBlockState().getValue(DreamwoodFunnelBlock.FACING);

		float itemWidth = EntityType.ITEM.getWidth();
		double offsetX = direction.getAxis() == Direction.Axis.X ? direction.getStepX() * (0.5 + 0.5 * itemWidth) : 0;
		double offsetZ = direction.getAxis() == Direction.Axis.Z ? direction.getStepZ() * (0.5 + 0.5 * itemWidth) : 0;

		float itemHeight = EntityType.ITEM.getHeight();
		double offsetY = direction.getAxis() == Direction.Axis.Y
				? direction.getStepY() * (0.5 + (direction == Direction.UP ? 0 : itemHeight))
				: -0.5 * itemHeight;

		return getBlockPos().getCenter().add(offsetX, offsetY, offsetZ);
	}
}
