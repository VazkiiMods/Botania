package vazkii.botania.api.capability;

import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;

import org.jetbrains.annotations.Nullable;

import vazkii.botania.api.BotaniaAPI;

/**
 * Abstract ID for a Botania block capability API with a context type.
 * 
 * @param <A> Type of the API.
 * @param <C> Type of the context.
 */
public final class BlockApiWithContext<A, C> extends ApiIdBlock<A> {
	private final Class<C> contextClass;

	public BlockApiWithContext(ResourceLocation id, Class<A> apiClass, Class<C> contextClass) {
		super(id, apiClass);
		this.contextClass = contextClass;
	}

	@Nullable
	public A find(BlockEntity blockEntity, @Nullable C context) {
		//noinspection DataFlowIssue
		return find(blockEntity.getLevel(), blockEntity.getBlockPos(), blockEntity.getBlockState(), blockEntity, context);
	}

	@Nullable
	public A find(Level level, BlockPos pos, @Nullable C context) {
		return find(level, pos, null, null, context);
	}

	@Nullable
	public A find(Level level, BlockPos pos, @Nullable BlockState state, @Nullable BlockEntity entity, @Nullable C context) {
		return BotaniaAPI.instance().findBlockApi(this, level, pos, state, entity, context);
	}

	public Class<C> getContextClass() {
		return contextClass;
	}
}
