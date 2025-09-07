package vazkii.botania.common.block.mana;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.shapes.BooleanOp;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;

import org.jetbrains.annotations.Nullable;

public class SmallManaPoolBlock extends ManaPoolBlock {

	private static final VoxelShape DILUTED_SHAPE_INTERACT = box(0, 0, 0, 16, 6, 16);
	private static final VoxelShape DILUTED_SHAPE_CUTOUT = box(1, 1, 1, 15, 6, 15);
	private static final VoxelShape DILUTED_SHAPE = Shapes.join(DILUTED_SHAPE_INTERACT, DILUTED_SHAPE_CUTOUT, BooleanOp.ONLY_FIRST);

	public SmallManaPoolBlock(int capacity, boolean fabulous, boolean creative, Properties builder) {
		super(capacity, fabulous, creative, builder
		);
	}

	@Override
	public VoxelShape getShape(BlockState state, BlockGetter world, BlockPos pos, CollisionContext ctx) {
		return DILUTED_SHAPE;
	}

	@Override
	public VoxelShape getInnerShape(BlockState state) {
		return DILUTED_SHAPE_CUTOUT;
	}

	@Override
	public VoxelShape getInteractionShape(BlockState state, @Nullable BlockGetter level, @Nullable BlockPos pos) {
		return DILUTED_SHAPE_INTERACT;
	}
}
