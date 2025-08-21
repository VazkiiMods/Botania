package vazkii.botania.common.block;

import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.DustParticleOptions;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.BooleanProperty;

public abstract class LuminizerPoweredBlock extends LuminizerBlock {

	public static final BooleanProperty POWERED = BlockStateProperties.POWERED;

	protected LuminizerPoweredBlock(Properties builder) {
		super(builder);
		registerDefaultState(defaultBlockState().setValue(POWERED, false));
	}

	@Override
	protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
		super.createBlockStateDefinition(builder);
		builder.add(POWERED);
	}

	static void addRedstoneParticle(Level level, BlockPos pos, RandomSource random) {
		double d0 = (double) pos.getX() + 0.5 + (random.nextDouble() - 0.5) * 0.4;
		double d1 = (double) pos.getY() + 0.5 + (random.nextDouble() - 0.5) * 0.4;
		double d2 = (double) pos.getZ() + 0.5 + (random.nextDouble() - 0.5) * 0.4;
		level.addParticle(DustParticleOptions.REDSTONE, d0, d1, d2, 0.0, 0.0, 0.0);
	}
}
