/*
 * This class is distributed as part of the Botania Mod.
 * Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 *
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 */
package vazkii.botania.common.block;

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
import alexiil.mc.lib.attributes.misc.Ref;
import alexiil.mc.lib.attributes.misc.Reference;
import net.minecraft.block.*;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.entity.Entity;
import net.minecraft.entity.ItemEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.fluid.Fluid;
import net.minecraft.fluid.Fluids;
import net.minecraft.item.ItemStack;
import net.minecraft.state.StateManager;
import net.minecraft.state.property.EnumProperty;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.ItemScatterer;
import net.minecraft.util.function.BooleanBiFunction;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.util.shape.VoxelShapes;
import net.minecraft.world.BlockView;
import net.minecraft.world.World;

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

public class BlockAltar extends BlockMod implements BlockEntityProvider {

	public static final EnumProperty<State> FLUID = EnumProperty.of("fluid", State.class);
	private static final VoxelShape BASE = Block.createCuboidShape(0, 0, 0, 16, 2, 16);
	private static final VoxelShape MIDDLE = Block.createCuboidShape(2, 2, 2, 14, 12, 14);
	private static final VoxelShape TOP = Block.createCuboidShape(2, 12, 2, 14, 20, 14);
	private static final VoxelShape TOP_CUTOUT = Block.createCuboidShape(3, 14, 3, 13, 20, 13);
	private static final VoxelShape SHAPE = VoxelShapes.union(VoxelShapes.union(BASE, MIDDLE), VoxelShapes.combineAndSimplify(TOP, TOP_CUTOUT, BooleanBiFunction.ONLY_FIRST));

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

	protected BlockAltar(Variant v, AbstractBlock.Settings builder) {
		super(builder);
		this.variant = v;
		setDefaultState(getDefaultState().with(FLUID, State.EMPTY));
	}

	@Override
	protected void appendProperties(StateManager.Builder<Block, BlockState> builder) {
		super.appendProperties(builder);
		builder.add(FLUID);
	}

	@Nonnull
	@Override
	public VoxelShape getOutlineShape(BlockState state, BlockView world, BlockPos pos, ShapeContext ctx) {
		return SHAPE;
	}

	@Override
	public void onEntityCollision(BlockState state, World world, BlockPos pos, Entity entity) {
		if (!world.isClient && entity instanceof ItemEntity) {
			TileAltar tile = (TileAltar) world.getBlockEntity(pos);
			if (tile.collideEntityItem((ItemEntity) entity)) {
				VanillaPacketDispatcher.dispatchTEToNearbyPlayers(tile);
			}
		}
	}

	@Override
	public ActionResult onUse(BlockState state, World world, BlockPos pos, PlayerEntity player, Hand hand, BlockHitResult hit) {
		TileAltar tile = (TileAltar) world.getBlockEntity(pos);
		State fluid = tile.getFluid();
		ItemStack stack = player.getStackInHand(hand);
		if (player.isSneaking()) {
			InventoryHelper.withdrawFromInventory(tile, player);
			VanillaPacketDispatcher.dispatchTEToNearbyPlayers(tile);
			return ActionResult.SUCCESS;
		} else if (tile.isEmpty() && fluid == State.WATER && stack.isEmpty()) {
			tile.trySetLastRecipe(player);
			return ActionResult.SUCCESS;
		} else {
			if (!stack.isEmpty() && fluid != State.EMPTY && isValidFluidContainerToFill(stack, tile.getFluid().asVanilla()) && !Botania.gardenOfGlassLoaded) {
				if (!player.abilities.creativeMode) {
					//support bucket stacks
					if (stack.getCount() == 1) {
						player.setStackInHand(hand, fill(tile.getFluid().asVanilla(), stack));
					} else {
						player.inventory.offerOrDrop(player.world, new ItemStack(stack.getItem()));
						stack.decrement(1);
					}
				}

				tile.setFluid(State.EMPTY);
				world.getChunkManager().getLightingProvider().checkBlock(pos);

				return ActionResult.SUCCESS;
			} else if (!stack.isEmpty() && (isValidFluidContainerToDrain(stack, Fluids.WATER) || stack.getItem() == ModItems.waterRod && ManaItemHandler.instance().requestManaExact(stack, player, ItemWaterRod.COST, false))) {
				if (tile.getFluid() == State.EMPTY) {
					if (stack.getItem() == ModItems.waterRod) {
						ManaItemHandler.instance().requestManaExact(stack, player, ItemWaterRod.COST, true);
					} else if (!player.abilities.creativeMode) {
						player.setStackInHand(hand, drain(Fluids.WATER, stack));
					}

					tile.setFluid(State.WATER);
				}

				return ActionResult.SUCCESS;
			} else if (!stack.isEmpty() && isValidFluidContainerToDrain(stack, Fluids.LAVA)) {
				if (tile.getFluid() == State.EMPTY) {
					if (!player.abilities.creativeMode) {
						player.setStackInHand(hand, drain(Fluids.LAVA, stack));
					}

					tile.setFluid(State.LAVA);
				}

				return ActionResult.SUCCESS;
			}
		}

		return ActionResult.PASS;
	}

	@Override
	public void rainTick(World world, BlockPos pos) {
		if (world.random.nextInt(20) == 1) {
			BlockState state = world.getBlockState(pos);
			if (state.get(FLUID) == State.EMPTY) {
				world.setBlockState(pos, state.with(FLUID, State.WATER));
			}
		}
	}

	private boolean isValidFluidContainerToDrain(ItemStack stack, Fluid fluid) {
		if (stack.isEmpty() || stack.getCount() != 1) {
			return false;
		}

		ItemAttributeList<FluidExtractable> extrs = FluidAttributes.EXTRACTABLE.getAll(stack);
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
		ItemStack container = stack;
		if (stack.getCount() > 1) {
			container = new ItemStack(stack.getItem());
		}

		ItemAttributeList<FluidInsertable> extrs = FluidAttributes.INSERTABLE.getAll(container);
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
	public BlockEntity createBlockEntity(@Nonnull BlockView world) {
		return new TileAltar();
	}

	@Override
	public void onStateReplaced(@Nonnull BlockState state, @Nonnull World world, @Nonnull BlockPos pos, @Nonnull BlockState newState, boolean isMoving) {
		if (state.getBlock() != newState.getBlock()) {
			BlockEntity be = world.getBlockEntity(pos);
			if (be instanceof TileSimpleInventory) {
				ItemScatterer.spawn(world, pos, ((TileSimpleInventory) be).getItemHandler());
			}
			super.onStateReplaced(state, world, pos, newState, isMoving);
		}
	}

	@Override
	public boolean hasComparatorOutput(BlockState state) {
		return true;
	}

	@Override
	public int getComparatorOutput(BlockState state, World world, BlockPos pos) {
		return state.get(FLUID) == State.WATER ? 15 : 0;
	}
}
