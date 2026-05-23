package vazkii.botania.common.block;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.EntityBlock;
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;

import org.jetbrains.annotations.Nullable;

import vazkii.botania.common.annotations.SoftImplement;
import vazkii.botania.common.block.block_entity.BotaniaBlockEntities;
import vazkii.botania.common.block.block_entity.PylonBlockEntity;

public abstract class PylonBlock extends BotaniaWaterloggedBlock implements EntityBlock {
	private static final VoxelShape SHAPE = box(2, 0, 2, 14, 21, 14);

	public PylonBlock(Properties builder) {
		super(builder);
	}

	protected void clientTick(Level level, BlockPos worldPosition, BlockState state, PylonBlockEntity self) {
		if (level.getRandom().nextBoolean()) {
			PylonBlock variant = ((PylonBlock) state.getBlock());
			variant.addRandomParticle(level, worldPosition);
		}
	}

	@Override
	public VoxelShape getShape(BlockState state, BlockGetter world, BlockPos pos, CollisionContext ctx) {
		return SHAPE;
	}

	@Override
	public RenderShape getRenderShape(BlockState state) {
		return RenderShape.ENTITYBLOCK_ANIMATED;
	}

	@Override
	public BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
		return new PylonBlockEntity(pos, state);
	}

	public abstract void addRandomParticle(Level level, BlockPos pos);

	@Nullable
	@Override
	public <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level level, BlockState state, BlockEntityType<T> type) {
		return level.isClientSide()
				? createTickerHelper(type, BotaniaBlockEntities.PYLON, this::clientTick)
				: null;
	}

	// Note: The enchantment power values are duplicated in the following file. Make sure to keep them in sync:
	// Fabric/src/main/resources/data/quilt/attachments/minecraft/block/enchanting_boosters.json
	@SoftImplement("IBlockExtension")
	public abstract float getEnchantPowerBonus(BlockState state, LevelReader world, BlockPos pos);
}
