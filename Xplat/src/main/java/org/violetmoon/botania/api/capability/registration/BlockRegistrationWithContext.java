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

public final class BlockRegistrationWithContext<A, C> {
	private final @Nullable ProviderBlockEntity<A, C> blockEntityProvider;
	private final BlockEntityType<?> @Nullable [] blockEntityTypes;
	private final @Nullable Predicate<BlockEntity> blockEntityPredicate;

	private final @Nullable BlockRegistrationWithContext.ProviderBlock<A, C> blockProvider;
	private final Block @Nullable [] blocks;
	private final @Nullable Predicate<Block> blockPredicate;

	private BlockRegistrationWithContext(@Nullable ProviderBlockEntity<A, C> blockEntityProvider,
			BlockEntityType<?> @Nullable [] blockEntityTypes, @Nullable Predicate<BlockEntity> blockEntityPredicate,
			@Nullable BlockRegistrationWithContext.ProviderBlock<A, C> blockProvider, Block @Nullable [] blocks,
			@Nullable Predicate<Block> blockPredicate) {
		this.blockEntityProvider = blockEntityProvider;
		this.blockEntityTypes = blockEntityTypes;
		this.blockEntityPredicate = blockEntityPredicate;
		this.blockProvider = blockProvider;
		this.blocks = blocks;
		this.blockPredicate = blockPredicate;
	}

	public static <A, C> BlockRegistrationWithContext<A, C> forBlockEntities(ProviderBlockEntity<A, C> provider,
			BlockEntityType<?>... blockEntityTypes) {
		Objects.requireNonNull(provider);
		Objects.requireNonNull(blockEntityTypes);

		if (blockEntityTypes.length == 0) {
			throw new IllegalArgumentException("No block entity types specified");
		}
		return new BlockRegistrationWithContext<>(provider, blockEntityTypes, null, null, null, null);
	}

	public static <A, C> BlockRegistrationWithContext<A, C> forBlockEntityPredicate(ProviderBlockEntity<A, C> provider,
			Predicate<BlockEntity> predicate) {
		Objects.requireNonNull(provider);
		Objects.requireNonNull(predicate);

		return new BlockRegistrationWithContext<>(provider, null, predicate, null, null, null);
	}

	public static <A, C> BlockRegistrationWithContext<A, C> forBlocks(ProviderBlock<A, C> provider, Block... blocks) {
		Objects.requireNonNull(provider);
		Objects.requireNonNull(blocks);

		if (blocks.length == 0) {
			throw new IllegalArgumentException("No blocks specified");
		}
		return new BlockRegistrationWithContext<>(null, null, null, provider, blocks, null);
	}

	public static <A, C> BlockRegistrationWithContext<A, C> forBlockPredicate(ProviderBlock<A, C> provider,
			Predicate<Block> predicate) {
		Objects.requireNonNull(provider);
		Objects.requireNonNull(predicate);

		return new BlockRegistrationWithContext<>(null, null, null, provider, null, predicate);
	}

	public void apply(
			BiConsumer<ProviderBlockEntity<A, C>, BlockEntityType<?>[]> blockEntityConsumer,
			BiConsumer<ProviderBlockEntity<A, C>, Predicate<BlockEntity>> blockEntityPredicateConsumer,
			BiConsumer<ProviderBlock<A, C>, Block[]> blockConsumer,
			BiConsumer<ProviderBlock<A, C>, Predicate<Block>> blockPredicateConsumer) {
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
	public interface ProviderBlockEntity<A, C> {
		@Nullable
		A getApi(BlockEntity entity, C context);

		default ProviderBlockEntity<A, C> withPredicate(Predicate<BlockEntity> predicate) {
			return (entity, context) -> {
				if (!predicate.test(entity)) {
					return null;
				}
				return ProviderBlockEntity.this.getApi(entity, context);
			};
		}
	}

	@FunctionalInterface
	public interface ProviderBlock<A, C> {
		@Nullable
		A getApi(Level level, BlockPos pos, BlockState state, @Nullable BlockEntity blockEntity, C context);

		default ProviderBlock<A, C> withPredicate(Predicate<Block> predicate) {
			return (level, pos, state, blockEntity, context) -> {
				if (!predicate.test(state.getBlock())) {
					return null;
				}
				return ProviderBlock.this.getApi(level, pos, state, blockEntity, context);
			};
		}
	}
}
