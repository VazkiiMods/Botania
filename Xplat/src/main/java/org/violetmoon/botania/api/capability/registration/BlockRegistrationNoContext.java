/*
 * This class is distributed as part of the Botania Mod.
 * Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 *
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 */

package vazkii.botania.api.capability.registration;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;

import org.jetbrains.annotations.Nullable;

import java.util.Objects;
import java.util.function.BiConsumer;
import java.util.function.Predicate;

public final class BlockRegistrationNoContext<A> {
	private final @Nullable ProviderBlockEntity<A> blockEntityProvider;
	private final BlockEntityType<?> @Nullable [] blockEntityTypes;
	private final @Nullable Predicate<BlockEntity> blockEntityPredicate;

	private final @Nullable BlockRegistrationNoContext.ProviderBlock<A> blockProvider;
	private final Block @Nullable [] blocks;
	private final @Nullable Predicate<Block> blockPredicate;

	private BlockRegistrationNoContext(@Nullable ProviderBlockEntity<A> blockEntityProvider,
			BlockEntityType<?> @Nullable [] blockEntityTypes, @Nullable Predicate<BlockEntity> blockEntityPredicate,
			@Nullable BlockRegistrationNoContext.ProviderBlock<A> blockProvider, Block @Nullable [] blocks, @Nullable Predicate<Block> blockPredicate) {
		this.blockEntityProvider = blockEntityProvider;
		this.blockEntityTypes = blockEntityTypes;
		this.blockEntityPredicate = blockEntityPredicate;
		this.blockProvider = blockProvider;
		this.blocks = blocks;
		this.blockPredicate = blockPredicate;
	}

	public static <A> BlockRegistrationNoContext<A> forBlockEntities(
			ProviderBlockEntity<A> provider, BlockEntityType<?>... blockEntityTypes) {
		Objects.requireNonNull(provider);
		Objects.requireNonNull(blockEntityTypes);

		if (blockEntityTypes.length == 0) {
			throw new IllegalArgumentException("No block entity types specified");
		}
		return new BlockRegistrationNoContext<>(provider, blockEntityTypes, null, null, null, null);
	}

	public static <A> BlockRegistrationNoContext<A> forBlockEntityPredicate(ProviderBlockEntity<A> provider,
			Predicate<BlockEntity> predicate) {
		Objects.requireNonNull(provider);
		Objects.requireNonNull(predicate);

		return new BlockRegistrationNoContext<>(provider, null, predicate, null, null, null);
	}

	public static <A> BlockRegistrationNoContext<A> forBlocks(ProviderBlock<A> provider, Block... blocks) {
		Objects.requireNonNull(provider);
		Objects.requireNonNull(blocks);

		if (blocks.length == 0) {
			throw new IllegalArgumentException("No blocks specified");
		}
		return new BlockRegistrationNoContext<>(null, null, null, provider, blocks, null);
	}

	public static <A> BlockRegistrationNoContext<A> forBlockPredicate(ProviderBlock<A> provider,
			Predicate<Block> predicate) {
		Objects.requireNonNull(provider);
		Objects.requireNonNull(predicate);

		return new BlockRegistrationNoContext<>(null, null, null, provider, null, predicate);
	}

	public void apply(
			BiConsumer<ProviderBlockEntity<A>, BlockEntityType<?>[]> blockEntityConsumer,
			BiConsumer<ProviderBlockEntity<A>, Predicate<BlockEntity>> blockEntityPredicateConsumer,
			BiConsumer<ProviderBlock<A>, Block[]> blockConsumer,
			BiConsumer<ProviderBlock<A>, Predicate<Block>> blockPredicateConsumer) {
		if (this.blockEntityProvider != null) {
			if (this.blockEntityTypes != null) {
				blockEntityConsumer.accept(this.blockEntityProvider, this.blockEntityTypes);
			} else if (this.blockEntityPredicate != null) {
				blockEntityPredicateConsumer.accept(this.blockEntityProvider, this.blockEntityPredicate);
			}
		} else if (this.blockProvider != null) {
			if (this.blocks != null) {
				blockConsumer.accept(this.blockProvider, this.blocks);
			} else if (this.blockPredicate != null) {
				blockPredicateConsumer.accept(this.blockProvider, this.blockPredicate);
			}
		}
	}

	@FunctionalInterface
	public interface ProviderBlockEntity<A> {
		@Nullable
		A getApi(BlockEntity entity);

		@Nullable
		default <C> A getApi(BlockEntity entity, @SuppressWarnings("unused") @Nullable C context) {
			return getApi(entity);
		}

		default ProviderBlockEntity<A> withPredicate(Predicate<BlockEntity> predicate) {
			return (entity) -> {
				if (!predicate.test(entity)) {
					return null;
				}
				return ProviderBlockEntity.this.getApi(entity);
			};
		}
	}

	@FunctionalInterface
	public interface ProviderBlock<A> {
		@Nullable
		A getApi(Level level, BlockPos pos, BlockState state, @Nullable BlockEntity blockEntity);

		@Nullable
		default <C> A getApi(Level level, BlockPos pos, BlockState state, @Nullable BlockEntity blockEntity,
				@SuppressWarnings("unused") @Nullable C context) {
			return getApi(level, pos, state, blockEntity);
		}

		default ProviderBlock<A> withPredicate(Predicate<Block> predicate) {
			return (level, pos, state, blockEntity) -> {
				if (!predicate.test(state.getBlock())) {
					return null;
				}
				return ProviderBlock.this.getApi(level, pos, state, blockEntity);
			};
		}
	}
}
