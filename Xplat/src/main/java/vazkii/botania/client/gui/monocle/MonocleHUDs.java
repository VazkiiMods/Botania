package vazkii.botania.client.gui.monocle;

import com.mojang.datafixers.util.Either;

import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.decoration.ItemFrame;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;

import org.jetbrains.annotations.Nullable;

import vazkii.botania.api.BotaniaAPI;
import vazkii.botania.api.block.MonocleHud;
import vazkii.botania.common.block.BotaniaBlocks;
import vazkii.botania.common.entity.BotaniaEntities;

import java.util.List;
import java.util.function.Function;
import java.util.function.Predicate;

public final class MonocleHUDs {
	private static final List<EntityRegistration<? extends Entity>> ENTITY_REGISTRATIONS = List.of(
			entityRegistration(ItemFrame.class, ItemFrameHud::new, ItemFrameHud::fallbackFactory,
					List.of(EntityType.ITEM_FRAME, EntityType.GLOW_ITEM_FRAME))
	);
	private static final List<BlockRegistration> BLOCK_REGISTRATIONS = List.of(
			BlockRegistration.fromList(RepeaterSettingHud::new, Blocks.REPEATER),
			BlockRegistration.fromList(ComparatorSettingHud::new, Blocks.COMPARATOR),
			BlockRegistration.fromList(DaylightDetectorHud::new, Blocks.DAYLIGHT_DETECTOR),
			BlockRegistration.fromList(SculkSensorHud::new, Blocks.SCULK_SENSOR, Blocks.CALIBRATED_SCULK_SENSOR),
			BlockRegistration.fromPredicate(RedstonePowerHud::new, RedstonePowerHud::isApplicable)
	);

	@SuppressWarnings("SameParameterValue")
	private static <E extends Entity> EntityRegistration<E> entityRegistration(
			Class<E> entityClass,
			Function<E, MonocleHud> factory,
			@Nullable Function<Entity, @Nullable MonocleHud> fallbackFactory,
			List<EntityType<? extends E>> entityTypes) {
		return new EntityRegistration<>(entityClass, factory, List.copyOf(entityTypes), fallbackFactory);
	}

	/**
	 * Registers regular Monocle HUD capabilities for entities.
	 *
	 * @param consumer      The registration callback.
	 * @param withFallbacks Whether to include elements that have a fallback factory.
	 */
	public static void registerMonocleHudEntityCaps(BotaniaEntities.ECapConsumer<MonocleHud> consumer,
			boolean withFallbacks) {
		for (EntityRegistration<? extends Entity> entry : ENTITY_REGISTRATIONS) {
			if (entry.fallbackFactory() == null || withFallbacks) {
				consumer.accept(entry::createHud, entry.getEntityTypes());
			}
		}
	}

	/**
	 * Registers Monocle HUD entity capabilities via fallback providers.
	 *
	 * @param consumer The registration callback.
	 */
	public static void registerMonocleHudFallbackEntityCaps(BotaniaEntities.ECapFallbackConsumer<MonocleHud> consumer) {
		for (var entry : ENTITY_REGISTRATIONS) {
			if (entry.fallbackFactory() != null) {
				consumer.accept(entry.fallbackFactory());
			}
		}
	}

	/**
	 * Registers regular Monocle HUD capabilities for blocks.
	 *
	 * @param consumer                   The registration callback.
	 * @param fallbackNotRegisteredCheck An optional predicate that tests whether a particular bloc kwas already
	 *                                   registered for this capability. If provided, registrations with a predicate
	 *                                   instead of a fixed block list are registered as well, determining the list of
	 *                                   blocks at registration time.
	 */
	public static void registerMonocleHudBlockCaps(BotaniaBlocks.BCapConsumer<MonocleHud> consumer,
			@Nullable Predicate<Block> fallbackNotRegisteredCheck) {
		for (BlockRegistration entry : BLOCK_REGISTRATIONS) {
			entry.blockListInfo.ifLeft(blocks -> consumer.accept(entry.factory(), blocks));
		}
		if (fallbackNotRegisteredCheck != null) {
			for (BlockRegistration entry : BLOCK_REGISTRATIONS) {
				entry.blockListInfo.ifRight(predicate -> {
					Block[] blocks = BuiltInRegistries.BLOCK.stream()
							.filter(b -> predicate.test(b.defaultBlockState()))
							.filter(fallbackNotRegisteredCheck)
							.toArray(Block[]::new);
					if (blocks.length > 0) {
						consumer.accept(entry.factory(), blocks);
					} else {
						BotaniaAPI.LOGGER.warn("Nothing to register for {}", entry.factory());
					}
				});
			}
		}
	}

	/**
	 * Registers Monocle HUD block capabilities via fallback providers.
	 *
	 * @param consumer The registration callback.
	 */
	public static void registerMonocleHudFallbackBlockCaps(BotaniaBlocks.BCapFallbackConsumer<MonocleHud> consumer) {
		for (var entry : BLOCK_REGISTRATIONS) {
			entry.blockListInfo().ifRight(predicate -> {
				consumer.accept(state -> predicate.test(state) ? entry.factory().apply(state) : null);
			});
		}
	}

	private record EntityRegistration<E extends Entity>(
			Class<E> entityClass,
			Function<E, MonocleHud> defaultFactory,
			// need this fixed list, since EntityType cannot be inspected for the actual entity class
			List<EntityType<? extends E>> defaultEntities,
			@Nullable Function<Entity, @Nullable MonocleHud> fallbackFactory) {

		public MonocleHud createHud(Entity entity) {
			return defaultFactory.apply(entityClass.cast(entity));
		}

		public EntityType<? extends Entity>[] getEntityTypes() {
			return defaultEntities.toArray(EntityType<?>[]::new);
		}
	}

	private record BlockRegistration(
			Function<BlockState, MonocleHud> factory,
			Either<Block[], Predicate<BlockState>> blockListInfo) {
		public static BlockRegistration fromList(Function<BlockState, MonocleHud> factory, Block... blocks) {
			return new BlockRegistration(factory, Either.left(blocks));
		}

		public static BlockRegistration fromPredicate(Function<BlockState, MonocleHud> factory,
				Predicate<BlockState> blockPredicate) {
			return new BlockRegistration(factory, Either.right(blockPredicate));
		}
	}
}
