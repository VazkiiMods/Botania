package vazkii.botania.api.capability;

import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;

import org.jetbrains.annotations.Nullable;

import vazkii.botania.api.BotaniaAPI;

/**
 * Abstract ID for a Botania block capability API without context type.
 * 
 * @param <A> Type of the API.
 */
public final class BlockApiNoContext<A> extends ApiIdBlock<A> {
	public BlockApiNoContext(ResourceLocation id, Class<A> apiClass) {
		super(id, apiClass);
	}

	@Nullable
	public A find(BlockEntity blockEntity) {
		//noinspection DataFlowIssue
		return find(blockEntity.getLevel(), blockEntity.getBlockPos(), blockEntity.getBlockState(), blockEntity);
	}

	@Nullable
	public A find(Level level, BlockPos pos) {
		return find(level, pos, null, null);
	}

	@Nullable
	public A find(Level level, BlockPos pos, @Nullable BlockState state, @Nullable BlockEntity entity) {
		return BotaniaAPI.instance().findBlockApi(this, level, pos, state, entity);
	}
}
