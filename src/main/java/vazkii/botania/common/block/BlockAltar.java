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

import vazkii.botania.api.internal.VanillaPacketDispatcher;
import vazkii.botania.api.item.IPetalApothecary.State;
import vazkii.botania.api.mana.ManaItemHandler;
import vazkii.botania.common.Botania;
import vazkii.botania.common.block.tile.TileAltar;
import vazkii.botania.common.block.tile.TileSimpleInventory;
import vazkii.botania.common.core.helper.InventoryHelper;
import vazkii.botania.common.item.ModItems;
import vazkii.botania.common.item.rod.ItemWaterRod;

import javax.annotation.Nonnull;


import alexiil.mc.lib.attributes.ItemAttributeList;
import alexiil.mc.lib.attributes.Simulation;
import alexiil.mc.lib.attributes.fluid.FluidAttributes;
import alexiil.mc.lib.attributes.fluid.FluidExtractable;
import alexiil.mc.lib.attributes.fluid.FluidInsertable;
import alexiil.mc.lib.attributes.fluid.amount.FluidAmount;
import alexiil.mc.lib.attributes.fluid.filter.ExactFluidFilter;
import alexiil.mc.lib.attributes.fluid.filter.FluidFilter;
import alexiil.mc.lib.attributes.fluid.volume.FluidKey;
import alexiil.mc.lib.attributes.fluid.volume.FluidKeys;
import alexiil.mc.lib.attributes.fluid.volume.FluidVolume;
import alexiil.mc.lib.attributes.misc.LimitedConsumer;
import alexiil.mc.lib.attributes.misc.Ref;
import alexiil.mc.lib.attributes.misc.Reference;

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
		} else {
			if (!stack.isEmpty() && fluid != State.EMPTY && isValidFluidContainerToFill(stack, tile.getFluid().asVanilla()) && !Botania.gardenOfGlassLoaded) {
				if (!player.getAbilities().instabuild) {
					//support bucket stacks
					if (stack.getCount() == 1) {
						player.setItemInHand(hand, fill(tile.getFluid().asVanilla(), stack));
					} else {
						player.getInventory().placeItemBackInInventory(new ItemStack(stack.getItem()));
						stack.shrink(1);
					}
				}

				tile.setFluid(State.EMPTY);
				world.getChunkSource().getLightEngine().checkBlock(pos);

				return InteractionResult.SUCCESS;
			} else if (!stack.isEmpty() && (isValidFluidContainerToDrain(stack, Fluids.WATER) || stack.getItem() == ModItems.waterRod && ManaItemHandler.instance().requestManaExact(stack, player, ItemWaterRod.COST, false))) {
				if (tile.getFluid() == State.EMPTY) {
					if (stack.getItem() == ModItems.waterRod) {
						ManaItemHandler.instance().requestManaExact(stack, player, ItemWaterRod.COST, true);
					} else if (!player.getAbilities().instabuild) {
						player.setItemInHand(hand, drain(Fluids.WATER, stack));
					}

					tile.setFluid(State.WATER);
				}

				return InteractionResult.SUCCESS;
			} else if (!stack.isEmpty() && isValidFluidContainerToDrain(stack, Fluids.LAVA)) {
				if (tile.getFluid() == State.EMPTY) {
					if (!player.getAbilities().instabuild) {
						player.setItemInHand(hand, drain(Fluids.LAVA, stack));
					}

					tile.setFluid(State.LAVA);
				}

				return InteractionResult.SUCCESS;
			}
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

	private boolean isValidFluidContainerToDrain(ItemStack stack, Fluid fluid) {
		if (stack.isEmpty() || stack.getCount() != 1) {
			return false;
		}

		Reference<ItemStack> ref = Reference.simulating(() -> stack, LimitedConsumer.fromConsumer($ -> {}));
		ItemAttributeList<FluidExtractable> extrs = FluidAttributes.EXTRACTABLE.getAll(ref);
		FluidFilter filt = ExactFluidFilter.of(fluid);
		FluidAmount left = FluidAmount.BUCKET;
		for (int i = 0; i < extrs.getCount(); i++) {
			FluidExtractable ex = extrs.get(i);
			FluidVolume vol = ex.attemptExtraction(filt, left, Simulation.SIMULATE);
			left = left.sub(vol.amount());
			if (left.isZero() || left.isNegative()) {
				return true;
			}
		}
		return false;
	}

	private ItemStack drain(Fluid fluid, ItemStack stack) {
		Reference<ItemStack> ref = new Ref<>(stack);
		ItemAttributeList<FluidExtractable> extrs = FluidAttributes.EXTRACTABLE.getAll(ref);
		FluidFilter filt = ExactFluidFilter.of(fluid);
		FluidAmount left = FluidAmount.BUCKET;
		for (int i = 0; i < extrs.getCount(); i++) {
			FluidExtractable ex = extrs.get(i);
			FluidVolume vol = ex.extract(filt, left);
			left = left.sub(vol.amount());
			if (left.isZero() || left.isNegative()) {
				break;
			}
		}
		return ref.get();
	}

	private boolean isValidFluidContainerToFill(ItemStack stack, Fluid fluid) {
		if (stack.isEmpty()) {
			return false;
		}
		//support bucket stacks
		ItemStack container = stack.getCount() > 1 ? new ItemStack(stack.getItem()) : stack;

		Reference<ItemStack> ref = Reference.simulating(() -> container, LimitedConsumer.fromConsumer($ -> {}));
		ItemAttributeList<FluidInsertable> extrs = FluidAttributes.INSERTABLE.getAll(ref);
		FluidKey key = FluidKeys.get(fluid);
		FluidVolume excess = key.withAmount(FluidAmount.BUCKET);
		for (int i = 0; i < extrs.getCount(); i++) {
			FluidInsertable ins = extrs.get(i);
			excess = ins.insert(excess);
			if (excess.isEmpty()) {
				return true;
			}
		}
		return false;
	}

	private ItemStack fill(Fluid fluid, ItemStack stack) {
		Reference<ItemStack> ref = new Ref<>(stack);
		ItemAttributeList<FluidInsertable> extrs = FluidAttributes.INSERTABLE.getAll(ref);
		FluidKey key = FluidKeys.get(fluid);
		FluidVolume excess = key.withAmount(FluidAmount.BUCKET);
		for (int i = 0; i < extrs.getCount(); i++) {
			FluidInsertable ins = extrs.get(i);
			excess = ins.insert(excess);
			if (excess.isEmpty()) {
				break;
			}
		}
		return ref.get();
	}

	@Nonnull
	@Override
	public BlockEntity newBlockEntity(@Nonnull BlockPos pos, @Nonnull BlockState state) {
		return new TileAltar(pos, state);
	}

	@Override
	public void onRemove(@Nonnull BlockState state, @Nonnull Level world, @Nonnull BlockPos pos, @Nonnull BlockState newState, boolean isMoving) {
		if (state.getBlock() != newState.getBlock()) {
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
