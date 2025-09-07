package vazkii.botania.common.block.mana;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.shapes.BooleanOp;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;

import org.jetbrains.annotations.Nullable;

public class BigManaPoolBlock extends ManaPoolBlock {

	private static final VoxelShape CREATIVE_SHAPE_INTERACT = box(0, 0, 0, 16, 10, 16);
	private static final VoxelShape CREATIVE_SHAPE_CUTOUT = box(2, 2, 2, 14, 16, 14);
	private static final VoxelShape CREATIVE_SHAPE = Shapes.join(CREATIVE_SHAPE_INTERACT, CREATIVE_SHAPE_CUTOUT, BooleanOp.ONLY_FIRST);

	public BigManaPoolBlock(int capacity, boolean fabulous, boolean creative, Properties builder) {
		super(capacity, fabulous, creative, builder);
	}

	@Override
	public VoxelShape getShape(BlockState state, BlockGetter world, BlockPos pos, CollisionContext ctx) {
		return CREATIVE_SHAPE;
	}

	@Override
	public VoxelShape getInnerShape(BlockState state) {
		return CREATIVE_SHAPE_CUTOUT;
	}

	@Override
	public VoxelShape getInteractionShape(BlockState state, @Nullable BlockGetter level, @Nullable BlockPos pos) {
		return CREATIVE_SHAPE_INTERACT;
	}
}
