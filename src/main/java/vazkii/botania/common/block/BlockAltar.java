/*
 * This class is distributed as part of the Botania Mod.
 * Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 *
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 */
package vazkii.botania.common.block;

import net.fabricmc.fabric.api.transfer.v1.context.ContainerItemContext;
import net.fabricmc.fabric.api.transfer.v1.fluid.FluidConstants;
import net.fabricmc.fabric.api.transfer.v1.fluid.FluidStorage;
import net.fabricmc.fabric.api.transfer.v1.fluid.FluidVariant;
import net.fabricmc.fabric.api.transfer.v1.transaction.Transaction;
import net.minecraft.core.BlockPos;
import net.minecraft.world.Containers;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.EntityBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.EnumProperty;
import net.minecraft.world.level.material.Fluid;
import net.minecraft.world.level.material.Fluids;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.BooleanOp;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;

import org.jetbrains.annotations.Nullable;

import vazkii.botania.api.internal.VanillaPacketDispatcher;
import vazkii.botania.api.item.IPetalApothecary.State;
import vazkii.botania.api.mana.ManaItemHandler;
import vazkii.botania.common.Botania;
import vazkii.botania.common.block.tile.ModTiles;
import vazkii.botania.common.block.tile.TileAltar;
import vazkii.botania.common.block.tile.TileSimpleInventory;
import vazkii.botania.common.core.helper.InventoryHelper;
import vazkii.botania.common.item.ModItems;
import vazkii.botania.common.item.rod.ItemWaterRod;

import javax.annotation.Nonnull;

public class BlockAltar extends BlockMod implements EntityBlock {

	public static final EnumProperty<State> FLUID = EnumProperty.create("fluid", State.class);
	private static final VoxelShape BASE = Block.box(0, 0, 0, 16, 2, 16);
	private static final VoxelShape MIDDLE = Block.box(2, 2, 2, 14, 12, 14);
	private static final VoxelShape TOP = Block.box(2, 12, 2, 14, 20, 14);
	private static final VoxelShape TOP_CUTOUT = Block.box(3, 14, 3, 13, 20, 13);
	private static final VoxelShape SHAPE = Shapes.or(Shapes.or(BASE, MIDDLE), Shapes.join(TOP, TOP_CUTOUT, BooleanOp.ONLY_FIRST));

	public enum Variant {
		DEFAULT,
		FOREST,
		PLAINS,
		MOUNTAIN,
		FUNGAL,
		SWAMP,
		DESERT,
		TAIGA,
		MESA,
		MOSSY
	}

	public final Variant variant;

	protected BlockAltar(Variant v, BlockBehaviour.Properties builder) {
		super(builder);
		this.variant = v;
		registerDefaultState(defaultBlockState().setValue(FLUID, State.EMPTY));
	}

	@Override
	protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
		super.createBlockStateDefinition(builder);
		builder.add(FLUID);
	}

	@Nonnull
	@Override
	public VoxelShape getShape(BlockState state, BlockGetter world, BlockPos pos, CollisionContext ctx) {
		return SHAPE;
	}

	@Override
	public void entityInside(BlockState state, Level world, BlockPos pos, Entity entity) {
		if (!world.isClientSide && entity instanceof ItemEntity) {
			TileAltar tile = (TileAltar) world.getBlockEntity(pos);
			if (tile.collideEntityItem((ItemEntity) entity)) {
				VanillaPacketDispatcher.dispatchTEToNearbyPlayers(tile);
			}
		}
	}

	@Override
	public InteractionResult use(BlockState state, Level world, BlockPos pos, Player player, InteractionHand hand, BlockHitResult hit) {
		TileAltar tile = (TileAltar) world.getBlockEntity(pos);
		State fluid = tile.getFluid();
		ItemStack stack = player.getItemInHand(hand);
		if (player.isShiftKeyDown()) {
			InventoryHelper.withdrawFromInventory(tile, player);
			VanillaPacketDispatcher.dispatchTEToNearbyPlayers(tile);
			return InteractionResult.SUCCESS;
		} else if (tile.isEmpty() && fluid == State.WATER && stack.isEmpty()) {
			tile.trySetLastRecipe(player);
			return InteractionResult.SUCCESS;
		} else if (tryWithdrawFluid(player, hand, tile) || tryDepositFluid(player, hand, tile)) {
			return InteractionResult.SUCCESS;
		}

		return InteractionResult.PASS;
	}

	@Override
	public void handlePrecipitation(BlockState state, Level world, BlockPos pos, Biome.Precipitation precipitation) {
		if (world.random.nextInt(20) == 1) {
			if (state.getValue(FLUID) == State.EMPTY) {
				world.setBlockAndUpdate(pos, state.setValue(FLUID, State.WATER));
			}
		}
	}

	private boolean tryWithdrawFluid(Player player, InteractionHand hand, TileAltar altar) {
		Fluid fluid = altar.getFluid().asVanilla();
		if (fluid == Fluids.EMPTY || fluid == Fluids.WATER && Botania.gardenOfGlassLoaded) {
			return false;
		}

		var context = ContainerItemContext.ofPlayerHand(player, hand);
		var fluidStorage = context.find(FluidStorage.ITEM);

		if (fluidStorage == null) {
			return false;
		}

		try (Transaction txn = Transaction.openOuter()) {
			long inserted = fluidStorage.insert(FluidVariant.of(fluid), FluidConstants.BUCKET, txn);
			if (inserted == FluidConstants.BUCKET) {
				if (!player.getAbilities().instabuild) {
					// Only perform inventory side effects in survival
					txn.commit();
				}
				altar.setFluid(State.EMPTY);
				return true;
			}
		}

		return false;
	}

	private boolean tryDepositFluid(Player player, InteractionHand hand, TileAltar altar) {
		if (altar.getFluid() != State.EMPTY) {
			return false;
		}

		ItemStack stack = player.getItemInHand(hand);
		if (!stack.isEmpty()
				&& stack.is(ModItems.waterRod)
				&& ManaItemHandler.instance().requestManaExact(stack, player, ItemWaterRod.COST, false)) {
			ManaItemHandler.instance().requestManaExact(stack, player, ItemWaterRod.COST, true);
			altar.setFluid(State.WATER);
			return true;
		}

		var context = ContainerItemContext.ofPlayerHand(player, hand);
		var fluidStorage = context.find(FluidStorage.ITEM);

		if (fluidStorage == null) {
			return false;
		}

		try (Transaction txn = Transaction.openOuter()) {
			long extracted = fluidStorage.extract(FluidVariant.of(Fluids.WATER), FluidConstants.BUCKET, txn);
			if (extracted == FluidConstants.BUCKET) {
				if (!player.getAbilities().instabuild) {
					// Only perform inventory side effects in survival
					txn.commit();
				}
				altar.setFluid(State.WATER);
				return true;
			}
		}

		try (Transaction txn = Transaction.openOuter()) {
			long extracted = fluidStorage.extract(FluidVariant.of(Fluids.LAVA), FluidConstants.BUCKET, txn);
			if (extracted == FluidConstants.BUCKET) {
				if (!player.getAbilities().instabuild) {
					// Only perform inventory side effects in survival
					txn.commit();
				}
				altar.setFluid(State.LAVA);
				return true;
			}
		}

		return false;
	}

	@Nonnull
	@Override
	public BlockEntity newBlockEntity(@Nonnull BlockPos pos, @Nonnull BlockState state) {
		return new TileAltar(pos, state);
	}

	@Nullable
	@Override
	public <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level level, BlockState state, BlockEntityType<T> type) {
		if (level.isClientSide) {
			return createTickerHelper(type, ModTiles.ALTAR, TileAltar::clientTick);
		} else {
			return createTickerHelper(type, ModTiles.ALTAR, TileAltar::serverTick);
		}
	}

	@Override
	public void onRemove(@Nonnull BlockState state, @Nonnull Level world, @Nonnull BlockPos pos, @Nonnull BlockState newState, boolean isMoving) {
		if (!state.is(newState.getBlock())) {
			BlockEntity be = world.getBlockEntity(pos);
			if (be instanceof TileSimpleInventory) {
				Containers.dropContents(world, pos, ((TileSimpleInventory) be).getItemHandler());
			}
			super.onRemove(state, world, pos, newState, isMoving);
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
}
