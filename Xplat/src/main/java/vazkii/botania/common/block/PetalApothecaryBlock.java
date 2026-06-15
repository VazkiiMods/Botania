/*
 * This class is distributed as part of the Botania Mod.
 * Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 *
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 */
package vazkii.botania.common.block;

import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.RandomSource;
import net.minecraft.world.Containers;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.ItemInteractionResult;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.EntityBlock;
import net.minecraft.world.level.block.PointedDripstoneBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.EnumProperty;
import net.minecraft.world.level.gameevent.GameEvent;
import net.minecraft.world.level.material.Fluid;
import net.minecraft.world.level.material.Fluids;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.BooleanOp;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;

import org.jetbrains.annotations.Nullable;

import vazkii.botania.api.block.PetalApothecary;
import vazkii.botania.api.block.PetalApothecary.State;
import vazkii.botania.api.mana.ManaItemHandler;
import vazkii.botania.common.block.block_entity.BotaniaBlockEntities;
import vazkii.botania.common.block.block_entity.PetalApothecaryBlockEntity;
import vazkii.botania.common.block.block_entity.SimpleInventoryBlockEntity;
import vazkii.botania.common.helper.InventoryHelper;
import vazkii.botania.common.item.BotaniaItems;
import vazkii.botania.common.item.rod.SeasRodItem;
import vazkii.botania.xplat.XplatAbstractions;

public class PetalApothecaryBlock extends BotaniaBlock implements EntityBlock {

	public static final EnumProperty<State> FLUID = EnumProperty.create("fluid", State.class);
	private static final VoxelShape BASE = Block.box(2, 0, 2, 14, 2, 14);
	private static final VoxelShape PILLAR = Block.box(4, 2, 4, 12, 11, 12);
	private static final VoxelShape TOP = Block.box(2, 11, 2, 14, 16, 14);
	private static final VoxelShape TOP_CUTOUT = Block.box(3, 12, 3, 13, 16, 13);
	private static final VoxelShape SHAPE_INTERACT = Shapes.or(Shapes.or(BASE, PILLAR), TOP);
	private static final VoxelShape SHAPE = Shapes.join(SHAPE_INTERACT, TOP_CUTOUT, BooleanOp.ONLY_FIRST);

	protected PetalApothecaryBlock(Properties builder) {
		super(builder);
		registerDefaultState(defaultBlockState().setValue(FLUID, State.EMPTY));
	}

	@Override
	protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
		super.createBlockStateDefinition(builder);
		builder.add(FLUID);
	}

	@Override
	public VoxelShape getShape(BlockState state, BlockGetter world, BlockPos pos, CollisionContext ctx) {
		return SHAPE;
	}

	@Override
	public VoxelShape getInteractionShape(BlockState state, BlockGetter level, BlockPos pos) {
		return SHAPE_INTERACT;
	}

	@Override
	public void entityInside(BlockState state, Level world, BlockPos pos, Entity entity) {
		if (!world.isClientSide && entity instanceof ItemEntity itemEntity
				&& world.getBlockEntity(pos) instanceof PetalApothecaryBlockEntity apothecary
				&& apothecary.collideEntityItem(itemEntity)) {
			world.sendBlockUpdated(pos, state, state, Block.UPDATE_CLIENTS);
		}
	}

	@Override
	protected ItemInteractionResult useItemOn(ItemStack stack, BlockState state, Level level, BlockPos pos, Player player, InteractionHand hand, BlockHitResult hitResult) {
		if (stack.isEmpty()) {
			return ItemInteractionResult.PASS_TO_DEFAULT_BLOCK_INTERACTION;
		}

		PetalApothecaryBlockEntity apothecary = level.getBlockEntity(pos, BotaniaBlockEntities.PETAL_APOTHECARY).orElseThrow();
		if (tryWithdrawFluid(player, hand, apothecary, pos) || tryDepositFluid(player, hand, apothecary, pos)) {
			return ItemInteractionResult.sidedSuccess(level.isClientSide());
		}

		return ItemInteractionResult.PASS_TO_DEFAULT_BLOCK_INTERACTION;
	}

	@Override
	protected InteractionResult useWithoutItem(BlockState state, Level level, BlockPos pos, Player player, BlockHitResult hitResult) {
		PetalApothecaryBlockEntity apothecary = level.getBlockEntity(pos, BotaniaBlockEntities.PETAL_APOTHECARY).orElseThrow();

		if (apothecary.canAddLastRecipe()) {
			return apothecary.trySetLastRecipe(player);
		} else if (!apothecary.isEmpty()) {
			InventoryHelper.withdrawFromInventory(apothecary, player);
			level.sendBlockUpdated(pos, state, state, Block.UPDATE_CLIENTS);
			return InteractionResult.sidedSuccess(level.isClientSide());
		}
		return InteractionResult.PASS;
	}

	@Override
	public void handlePrecipitation(BlockState state, Level world, BlockPos pos, Biome.Precipitation precipitation) {
		if (precipitation == Biome.Precipitation.RAIN && world.random.nextInt(20) == 1) {
			if (state.getValue(FLUID) == State.EMPTY) {
				world.setBlockAndUpdate(pos, state.setValue(FLUID, State.WATER));
				world.gameEvent(null, GameEvent.BLOCK_CHANGE, pos);
			}
		}
	}

	public boolean canReceiveStalactiteDrip(BlockState state, Fluid fluid) {
		return state.getValue(FLUID) == State.EMPTY && (fluid == Fluids.WATER || fluid == Fluids.LAVA);
	}

	@Override
	public void tick(BlockState state, ServerLevel level, BlockPos pos, RandomSource random) {
		BlockPos tipPos = PointedDripstoneBlock.findStalactiteTipAboveCauldron(level, pos);
		if (tipPos != null) {
			Fluid fluid = PointedDripstoneBlock.getCauldronFillFluidType(level, tipPos);
			// probability for water is 3x that of lava, because it would only fill a layer in cauldrons,
			// so only accept 1 in 3 water fill attempts
			final boolean isLava = fluid == Fluids.LAVA;
			if ((isLava || fluid == Fluids.WATER && random.nextInt(3) == 0) && this.canReceiveStalactiteDrip(state, fluid)) {
				level.setBlockAndUpdate(pos, state.setValue(FLUID, isLava ? State.LAVA : State.WATER));
			}
		}
	}

	private boolean tryWithdrawFluid(Player player, InteractionHand hand, PetalApothecaryBlockEntity altar, BlockPos pos) {
		Fluid fluid = altar.getFluid().asVanilla();
		if (fluid == Fluids.EMPTY || fluid == Fluids.WATER && XplatAbstractions.INSTANCE.gogLoaded()) {
			return false;
		}

		boolean success = XplatAbstractions.INSTANCE.insertFluidIntoPlayerItem(player, hand, fluid);
		if (success) {
			altar.setFluid(PetalApothecary.State.EMPTY);
			// Usage of vanilla sound events: Subtitle is "Bucket fills"
			if (fluid == Fluids.WATER) {
				player.level().playSound(player, pos, SoundEvents.BUCKET_FILL, SoundSource.BLOCKS, 1F, 1F);
			} else if (fluid == Fluids.LAVA) {
				player.level().playSound(player, pos, SoundEvents.BUCKET_FILL_LAVA, SoundSource.BLOCKS, 1F, 1F);
			}
		}
		return success;
	}

	private boolean tryDepositFluid(Player player, InteractionHand hand, PetalApothecaryBlockEntity altar, BlockPos pos) {
		if (altar.getFluid() != State.EMPTY) {
			return false;
		}

		ItemStack stack = player.getItemInHand(hand);
		if (!stack.isEmpty()
				&& stack.is(BotaniaItems.ROD_OF_THE_SEAS)
				&& ManaItemHandler.instance().requestManaExact(stack, player, SeasRodItem.COST, false)) {
			ManaItemHandler.instance().requestManaExact(stack, player, SeasRodItem.COST, true);
			altar.setFluid(State.WATER);
			player.level().playSound(player, pos, SoundEvents.BUCKET_EMPTY, SoundSource.BLOCKS, 1F, 1F);
			return true;
		}

		if (XplatAbstractions.INSTANCE.extractFluidFromPlayerItem(player, hand, Fluids.WATER)) {
			altar.setFluid(State.WATER);
			// Usage of vanilla sound event: Subtitle is "Bucket empties"
			player.level().playSound(player, pos, SoundEvents.BUCKET_EMPTY, SoundSource.BLOCKS, 1F, 1F);
			return true;
		} else if (XplatAbstractions.INSTANCE.extractFluidFromPlayerItem(player, hand, Fluids.LAVA)) {
			altar.setFluid(State.LAVA);
			// Usage of vanilla sound event: Subtitle is "Bucket empties"
			player.level().playSound(player, pos, SoundEvents.BUCKET_EMPTY_LAVA, SoundSource.BLOCKS, 1F, 1F);
			return true;
		}
		return false;
	}

	@Override
	public BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
		return new PetalApothecaryBlockEntity(pos, state);
	}

	@Nullable
	@Override
	public <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level level, BlockState state, BlockEntityType<T> type) {
		if (level.isClientSide()) {
			return createTickerHelper(type, BotaniaBlockEntities.PETAL_APOTHECARY, PetalApothecaryBlockEntity::clientTick);
		} else {
			return createTickerHelper(type, BotaniaBlockEntities.PETAL_APOTHECARY, PetalApothecaryBlockEntity::serverTick);
		}
	}

	@Override
	public void onRemove(BlockState state, Level world, BlockPos pos, BlockState newState, boolean isMoving) {
		boolean blockChanged = !state.is(newState.getBlock());
		if (blockChanged || newState.getValue(FLUID) != State.WATER) {
			if (world.getBlockEntity(pos) instanceof SimpleInventoryBlockEntity inventory) {
				Containers.dropContents(world, pos, inventory.getItemHandler());
			}
			if (blockChanged) {
				super.onRemove(state, world, pos, newState, isMoving);
			}
		}
	}

	@Override
	public boolean hasAnalogOutputSignal(BlockState state) {
		return true;
	}

	@Override
	public int getAnalogOutputSignal(BlockState state, Level world, BlockPos pos) {
		return state.getValue(FLUID) == State.WATER ? 15 : 0;
	}

	@Override
	public void animateTick(BlockState state, Level level, BlockPos pos, RandomSource random) {
		if (state.getValue(FLUID) == State.LAVA && level.getBlockState(pos.above()).isAir()
				&& random.nextInt(4) == 0) {
			level.addParticle(ParticleTypes.SMOKE,
					pos.getX() + 0.5 + Math.random() * 0.4 - 0.2,
					pos.getY() + 1,
					pos.getZ() + 0.5 + Math.random() * 0.4 - 0.2,
					0, 0.05, 0);
			if (random.nextInt(8) == 0) {
				level.addParticle(ParticleTypes.LAVA,
						pos.getX() + 0.5 + Math.random() * 0.4 - 0.2,
						pos.getY() + 1,
						pos.getZ() + 0.5 + Math.random() * 0.4 - 0.2,
						0, 0.01, 0);
			}
		}
	}
}
