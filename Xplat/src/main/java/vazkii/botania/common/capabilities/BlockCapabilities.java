/*
 * This class is distributed as part of the Botania Mod.
 * Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 *
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 */

package vazkii.botania.common.capabilities;

import com.mojang.datafixers.util.Function3;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.entity.AbstractFurnaceBlockEntity;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;

import vazkii.botania.api.block.EdibleBlockWithEffects;
import vazkii.botania.api.block.ExoflameHeatable;
import vazkii.botania.api.block.HourglassTrigger;
import vazkii.botania.api.block.LifeAggregatorCarryable;
import vazkii.botania.api.block.PhantomInkableBlock;
import vazkii.botania.api.block.WandBindable;
import vazkii.botania.api.block.Wandable;
import vazkii.botania.api.capability.registration.ApiIdRegistration;
import vazkii.botania.api.capability.registration.ApiProviderRegistration;
import vazkii.botania.api.capability.registration.BlockRegistrationNoContext;
import vazkii.botania.api.capability.registration.BlockRegistrationWithContext;
import vazkii.botania.api.mana.ManaCollisionGhost;
import vazkii.botania.api.mana.ManaReceiver;
import vazkii.botania.api.mana.ManaTrigger;
import vazkii.botania.api.mana.spark.ManaSparkAttachable;
import vazkii.botania.common.block.BotaniaBlocks;
import vazkii.botania.common.block.ForceRelayBlock;
import vazkii.botania.common.block.ManastormChargeBlock;
import vazkii.botania.common.block.block_entity.BotaniaBlockEntities;
import vazkii.botania.common.block.block_entity.ManaEnchanterBlockEntity;
import vazkii.botania.common.block.mana.DrumBlock;
import vazkii.botania.common.block.mana.ManaDetectorBlock;
import vazkii.botania.common.block.mana.ManaVoidBlock;
import vazkii.botania.common.handler.ExoflameFurnaceHandler;
import vazkii.botania.common.handler.LifeAggregatorHandler;

import java.util.List;
import java.util.function.BiFunction;
import java.util.function.Function;
import java.util.function.Predicate;

public final class BlockCapabilities {

	public static void registerLookups(ApiIdRegistration registration) {
		registration.register(EdibleBlockWithEffects.LOOKUP);
		registration.register(ExoflameHeatable.LOOKUP);
		registration.register(HourglassTrigger.LOOKUP);
		registration.register(LifeAggregatorCarryable.LOOKUP);
		registration.register(ManaCollisionGhost.LOOKUP);
		registration.register(ManaReceiver.LOOKUP);
		registration.register(ManaSparkAttachable.LOOKUP);
		registration.register(ManaTrigger.LOOKUP);
		registration.register(PhantomInkableBlock.LOOKUP);
		registration.register(Wandable.LOOKUP);
		registration.register(WandBindable.LOOKUP);
	}

	@SuppressWarnings("unchecked")
	public static void registerProviders(ApiProviderRegistration registration) {
		registration.register(EdibleBlockWithEffects.LOOKUP, List.of(
				blockApiSelf(BotaniaBlocks.MUTATED_GRASS_BLOCK, BotaniaBlocks.INFUSED_GRASS_BLOCK)
		));

		// for performance reasons we register the types we know, even though they would be covered by the fallback
		registration.register(ExoflameHeatable.LOOKUP, List.of(
				blockEntityApi(ExoflameFurnaceHandler.FurnaceExoflameHeatable::new,
						BlockEntityType.FURNACE, BlockEntityType.BLAST_FURNACE, BlockEntityType.SMOKER
				)
		));

		registration.register(HourglassTrigger.LOOKUP, List.of(
				blockEntityApiSelf(BotaniaBlockEntities.ANIMATED_TORCH)
		));

		registration.register(LifeAggregatorCarryable.LOOKUP, List.of(
				blockEntityApi(LifeAggregatorHandler.MonsterSpawnerCarryable::new, BlockEntityType.MOB_SPAWNER),
				blockEntityApi(LifeAggregatorHandler.TrialSpawnerCarryable::new, BlockEntityType.TRIAL_SPAWNER)
		));

		registration.register(ManaCollisionGhost.LOOKUP, List.of(
				blockApiSelf(
						BotaniaBlocks.ABSTRUSE_PLATFORM, BotaniaBlocks.INFRANGIBLE_PLATFORM,
						BotaniaBlocks.MANA_DETECTOR, BotaniaBlocks.MANA_PRISM,
						BotaniaBlocks.SPECTRAL_PLATFORM, BotaniaBlocks.TINY_PLANET
				)
		));

		registration.register(ManaReceiver.LOOKUP, List.of(
				blockEntityApiSelfContext(
						BotaniaBlockEntities.AVATAR, BotaniaBlockEntities.BOTANICAL_BREWERY,
						BotaniaBlockEntities.MANA_SPLITTER, BotaniaBlockEntities.MANA_ENCHANTER,
						BotaniaBlockEntities.MANA_POOL, BotaniaBlockEntities.MANA_FLUXFIELD,
						BotaniaBlockEntities.RUNIC_ALTAR, BotaniaBlockEntities.LIFE_IMBUER,
						BotaniaBlockEntities.MANA_SPREADER, BotaniaBlockEntities.TERRESTRIAL_AGGLOMERATION_PLATE
				),
				blockApiContext(ManaVoidBlock.ManaReceiverImpl::new, BotaniaBlocks.MANA_VOID)
		));

		registration.register(ManaSparkAttachable.LOOKUP, List.of(
				blockEntityApiSelf(
						BotaniaBlockEntities.MANA_ENCHANTER, BotaniaBlockEntities.MANA_POOL,
						BotaniaBlockEntities.TERRESTRIAL_AGGLOMERATION_PLATE
				)
		));

		registration.register(ManaTrigger.LOOKUP, List.of(
				blockEntityApiSelf(
						BotaniaBlockEntities.ANIMATED_TORCH, BotaniaBlockEntities.HOVERING_HOURGLASS,
						BotaniaBlockEntities.MANA_PRISM
				),
				blockApi(DrumBlock.ManaTriggerImpl::new,
						BotaniaBlocks.DRUM_OF_THE_CANOPY, BotaniaBlocks.DRUM_OF_THE_GATHERING,
						BotaniaBlocks.DRUM_OF_THE_WILD
				),
				blockApi(ManastormChargeBlock.ManaTriggerImpl::new, BotaniaBlocks.MANASTORM_CHARGE),
				blockApi(ManaDetectorBlock.ManaTriggerImpl::new, BotaniaBlocks.MANA_DETECTOR)
		));

		registration.register(PhantomInkableBlock.LOOKUP, List.of(
				blockEntityApiSelf(
						BotaniaBlockEntities.LUMINIZER, BotaniaBlockEntities.PLATFORM,
						BotaniaBlockEntities.CORPOREA_CRYSTAL_CUBE
				)
		));

		registration.register(Wandable.LOOKUP, List.of(
				blockEntityApiSelfContext(
						BotaniaBlockEntities.ALFHEIM_PORTAL, BotaniaBlockEntities.ANIMATED_TORCH,
						BotaniaBlockEntities.CORPOREA_CRYSTAL_CUBE, BotaniaBlockEntities.CORPOREA_RETAINER,
						BotaniaBlockEntities.CRAFTY_CRATE, BotaniaBlockEntities.MANA_ENCHANTER,
						BotaniaBlockEntities.EYE_OF_THE_ANCIENTS, BotaniaBlockEntities.HOVERING_HOURGLASS,
						BotaniaBlockEntities.PLATFORM, BotaniaBlockEntities.MANA_POOL,
						BotaniaBlockEntities.RUNIC_ALTAR, BotaniaBlockEntities.MANA_SPREADER,
						BotaniaBlockEntities.SPREADER_TURNTABLE, BotaniaBlockEntities.DAFFOMILL,
						BotaniaBlockEntities.HOPPERHOCK, BotaniaBlockEntities.HOPPERHOCK_PETITE,
						BotaniaBlockEntities.POLLIDISIAC, BotaniaBlockEntities.RANNUNCARPUS,
						BotaniaBlockEntities.RANNUNCARPUS_PETITE
				),
				blockApiContext(ForceRelayBlock.ForceRelayWandable::new, BotaniaBlocks.FORCE_RELAY),
				blockApiContext(ManaEnchanterBlockEntity::createLapisBlockWandable, Blocks.LAPIS_BLOCK)
		));

		registration.register(WandBindable.LOOKUP, List.of(
				blockEntityApiSelfContext(
						// generating flowers
						BotaniaBlockEntities.DANDELIFEON, BotaniaBlockEntities.ENDOFLAME,
						BotaniaBlockEntities.ENTROPINNYUM, BotaniaBlockEntities.GOURMARYLLIS,
						BotaniaBlockEntities.HYDROANGEAS, BotaniaBlockEntities.KEKIMURUS,
						BotaniaBlockEntities.MUNCHDEW, BotaniaBlockEntities.NARSLIMMUS,
						BotaniaBlockEntities.RAFFLOWSIA, BotaniaBlockEntities.ROSA_ARCANA,
						BotaniaBlockEntities.SHULK_ME_NOT, BotaniaBlockEntities.SPECTROLUS,
						BotaniaBlockEntities.THERMALILY,

						// functional flowers
						BotaniaBlockEntities.AGRICARNATION, BotaniaBlockEntities.AGRICARNATION_PETITE,
						BotaniaBlockEntities.BELLETHORNE, BotaniaBlockEntities.BELLETHORNE_PETITE,
						BotaniaBlockEntities.BUBBELL, BotaniaBlockEntities.BUBBELL_PETITE,
						BotaniaBlockEntities.CLAYCONIA, BotaniaBlockEntities.CLAYCONIA_PETITE,
						BotaniaBlockEntities.DAFFOMILL, BotaniaBlockEntities.DREADTHORN,
						BotaniaBlockEntities.EXOFLAME, BotaniaBlockEntities.FALLEN_KANADE,
						BotaniaBlockEntities.HEISEI_DREAM, BotaniaBlockEntities.HOPPERHOCK,
						BotaniaBlockEntities.HOPPERHOCK_PETITE, BotaniaBlockEntities.HYACIDUS,
						BotaniaBlockEntities.JADED_AMARANTHUS, BotaniaBlockEntities.JIYUULIA,
						BotaniaBlockEntities.JIYUULIA_PETITE, BotaniaBlockEntities.LABELLIA,
						BotaniaBlockEntities.LOONIUM, BotaniaBlockEntities.MARIMORPHOSIS,
						BotaniaBlockEntities.MARIMORPHOSIS_PETITE, BotaniaBlockEntities.MEDUMONE,
						BotaniaBlockEntities.ORECHID, BotaniaBlockEntities.ORECHID_IGNEM,
						BotaniaBlockEntities.POLLIDISIAC, BotaniaBlockEntities.RANNUNCARPUS,
						BotaniaBlockEntities.RANNUNCARPUS_PETITE, BotaniaBlockEntities.SPECTRANTHEMUM,
						BotaniaBlockEntities.TANGLEBERRIE, BotaniaBlockEntities.TANGLEBERRIE_PETITE,
						BotaniaBlockEntities.TIGERSEYE, BotaniaBlockEntities.VINCULOTUS,

						// other
						BotaniaBlockEntities.LUMINIZER, BotaniaBlockEntities.MANA_SPREADER
				),
				blockApiContext(ForceRelayBlock.ForceRelayBindable::new, BotaniaBlocks.FORCE_RELAY)
		));
	}

	public static void registerFallbackProviders(ApiProviderRegistration registration) {
		registration.register(ExoflameHeatable.LOOKUP, List.of(
				blockEntityApi(ExoflameFurnaceHandler.FurnaceExoflameHeatable::new,
						AbstractFurnaceBlockEntity.class::isInstance)
		));
	}

	private BlockCapabilities() {}

	// helper methods

	public static <A> BlockRegistrationNoContext<A> blockApi(Function<BlockState, A> provider, Block... blocks) {
		return blockApi((level, pos, state, blockEntity) -> provider.apply(state), blocks);
	}

	public static <A> BlockRegistrationNoContext<A> blockApi(Function<BlockState, A> provider, Predicate<Block> predicate) {
		return blockApi((level, pos, state, blockEntity) -> provider.apply(state), predicate);
	}

	public static <A> BlockRegistrationNoContext<A> blockApi(BiFunction<Level, BlockPos, A> provider,
			Block... blocks) {
		return blockApi((level, pos, state, blockEntity) -> provider.apply(level, pos), blocks);
	}

	public static <A> BlockRegistrationNoContext<A> blockApi(Function3<Level, BlockPos, BlockState, A> provider,
			Block... blocks) {
		return blockApi((level, pos, state, blockEntity) -> provider.apply(level, pos, state), blocks);
	}

	public static <A> BlockRegistrationNoContext<A> blockApi(BlockRegistrationNoContext.ProviderBlock<A> provider,
			Block... blocks) {
		return BlockRegistrationNoContext.forBlocks(provider, blocks);
	}

	public static <A> BlockRegistrationNoContext<A> blockApi(BlockRegistrationNoContext.ProviderBlock<A> provider,
			Predicate<Block> predicate) {
		return BlockRegistrationNoContext.forBlockPredicate(provider, predicate);
	}

	@SuppressWarnings("unchecked")
	public static <A> BlockRegistrationNoContext<A> blockApiSelf(Block... blocks) {
		return BlockRegistrationNoContext.forBlocks((level, pos, state, blockEntity) -> (A) state.getBlock(), blocks);
	}

	@SuppressWarnings("unchecked")
	public static <A, E extends BlockEntity> BlockRegistrationNoContext<A> blockEntityApi(Function<E, A> provider,
			BlockEntityType<? extends E>... blockEntityType) {
		return BlockRegistrationNoContext.forBlockEntities(be -> provider.apply((E) be), blockEntityType);
	}

	@SuppressWarnings("unchecked")
	public static <A, E extends BlockEntity> BlockRegistrationNoContext<A> blockEntityApi(Function<E, A> provider,
			Predicate<BlockEntity> predicate) {
		return BlockRegistrationNoContext.forBlockEntityPredicate(be -> provider.apply((E) be), predicate);
	}

	@SuppressWarnings("unchecked")
	public static <A> BlockRegistrationNoContext<A> blockEntityApiSelf(BlockEntityType<?>... blockEntities) {
		return BlockRegistrationNoContext.forBlockEntities(be -> (A) be, blockEntities);
	}

	public static <A, C> BlockRegistrationWithContext<A, C> blockApiContext(BiFunction<Level, BlockPos, A> provider,
			Block... blocks) {
		return blockApiContext((level, pos, state, blockEntity, context) -> provider.apply(level, pos), blocks);
	}

	public static <A, C> BlockRegistrationWithContext<A, C> blockApiContext(
			Function3<Level, BlockPos, BlockState, A> provider, Block... blocks) {
		return blockApiContext((level, pos, state, blockEntity, context) -> provider.apply(level, pos, state), blocks);
	}

	public static <A, C> BlockRegistrationWithContext<A, C> blockApiContext(
			BlockRegistrationWithContext.ProviderBlock<A, C> provider, Block... blocks) {
		return BlockRegistrationWithContext.forBlocks(provider, blocks);
	}

	@SuppressWarnings("unchecked")
	public static <A, C> BlockRegistrationWithContext<A, C> blockEntityApiSelfContext(
			BlockEntityType<?>... blockEntities) {
		return BlockRegistrationWithContext.forBlockEntities((blockEntity, context) -> (A) blockEntity, blockEntities);
	}
}
