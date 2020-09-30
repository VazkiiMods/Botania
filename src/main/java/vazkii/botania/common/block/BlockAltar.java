/*
 * This class is distributed as part of the Botania Mod.
 * Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 *
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 */
package vazkii.botania.common.block;

import net.minecraft.block.AbstractBlock;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.ITileEntityProvider;
import net.minecraft.entity.Entity;
import net.minecraft.entity.item.ItemEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.fluid.Fluid;
import net.minecraft.fluid.Fluids;
import net.minecraft.item.ItemStack;
import net.minecraft.state.EnumProperty;
import net.minecraft.state.StateContainer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Hand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.util.math.shapes.IBooleanFunction;
import net.minecraft.util.math.shapes.ISelectionContext;
import net.minecraft.util.math.shapes.VoxelShape;
import net.minecraft.util.math.shapes.VoxelShapes;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.World;
import net.minecraftforge.fluids.FluidAttributes;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.capability.CapabilityFluidHandler;
import net.minecraftforge.fluids.capability.IFluidHandler;

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

public class BlockAltar extends BlockMod implements ITileEntityProvider {

	public static final EnumProperty<State> FLUID = EnumProperty.create("fluid", State.class);
	private static final VoxelShape BASE = Block.makeCuboidShape(0, 0, 0, 16, 2, 16);
	private static final VoxelShape MIDDLE = Block.makeCuboidShape(2, 2, 2, 14, 12, 14);
	private static final VoxelShape TOP = Block.makeCuboidShape(2, 12, 2, 14, 20, 14);
	private static final VoxelShape TOP_CUTOUT = Block.makeCuboidShape(3, 14, 3, 13, 20, 13);
	private static final VoxelShape SHAPE = VoxelShapes.or(VoxelShapes.or(BASE, MIDDLE), VoxelShapes.combineAndSimplify(TOP, TOP_CUTOUT, IBooleanFunction.ONLY_FIRST));

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

	protected BlockAltar(Variant v, AbstractBlock.Properties builder) {
		super(builder);
		this.variant = v;
		setDefaultState(getDefaultState().with(FLUID, State.EMPTY));
	}

	@Override
	protected void fillStateContainer(StateContainer.Builder<Block, BlockState> builder) {
		super.fillStateContainer(builder);
		builder.add(FLUID);
	}

	@Nonnull
	@Override
	public VoxelShape getShape(BlockState state, IBlockReader world, BlockPos pos, ISelectionContext ctx) {
		return SHAPE;
	}

	@Override
	public void onEntityCollision(BlockState state, World world, BlockPos pos, Entity entity) {
		if (!world.isRemote && entity instanceof ItemEntity) {
			TileAltar tile = (TileAltar) world.getTileEntity(pos);
			if (tile.collideEntityItem((ItemEntity) entity)) {
				VanillaPacketDispatcher.dispatchTEToNearbyPlayers(tile);
			}
		}
	}

	@Override
	public ActionResultType onBlockActivated(BlockState state, World world, BlockPos pos, PlayerEntity player, Hand hand, BlockRayTraceResult hit) {
		TileAltar tile = (TileAltar) world.getTileEntity(pos);
		State fluid = tile.getFluid();
		ItemStack stack = player.getHeldItem(hand);
		if (player.isSneaking()) {
			InventoryHelper.withdrawFromInventory(tile, player);
			VanillaPacketDispatcher.dispatchTEToNearbyPlayers(tile);
			return ActionResultType.SUCCESS;
		} else if (tile.isEmpty() && fluid == State.WATER && stack.isEmpty()) {
			tile.trySetLastRecipe(player);
			return ActionResultType.SUCCESS;
		} else {
			if (!stack.isEmpty() && fluid != State.EMPTY && isValidFluidContainerToFill(stack, tile.getFluid().asVanilla()) && !Botania.gardenOfGlassLoaded) {
				if (!player.abilities.isCreativeMode) {
					//support bucket stacks
					if (stack.getCount() == 1) {
						player.setHeldItem(hand, fill(tile.getFluid().asVanilla(), stack));
					} else {
						player.inventory.placeItemBackInInventory(player.world, new ItemStack(stack.getItem()));
						stack.shrink(1);
					}
				}

				tile.setFluid(State.EMPTY);
				world.getChunkProvider().getLightManager().checkBlock(pos);

				return ActionResultType.SUCCESS;
			} else if (!stack.isEmpty() && (isValidFluidContainerToDrain(stack, Fluids.WATER) || stack.getItem() == ModItems.waterRod && ManaItemHandler.instance().requestManaExact(stack, player, ItemWaterRod.COST, false))) {
				if (tile.getFluid() == State.EMPTY) {
					if (stack.getItem() == ModItems.waterRod) {
						ManaItemHandler.instance().requestManaExact(stack, player, ItemWaterRod.COST, true);
					} else if (!player.abilities.isCreativeMode) {
						player.setHeldItem(hand, drain(Fluids.WATER, stack));
					}

					tile.setFluid(State.WATER);
				}

				return ActionResultType.SUCCESS;
			} else if (!stack.isEmpty() && isValidFluidContainerToDrain(stack, Fluids.LAVA)) {
				if (tile.getFluid() == State.EMPTY) {
					if (!player.abilities.isCreativeMode) {
						player.setHeldItem(hand, drain(Fluids.LAVA, stack));
					}

					tile.setFluid(State.LAVA);
				}

				return ActionResultType.SUCCESS;
			}
		}

		return ActionResultType.PASS;
	}

	@Override
	public void fillWithRain(World world, BlockPos pos) {
		if (world.rand.nextInt(20) == 1) {
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

		return stack.getCapability(CapabilityFluidHandler.FLUID_HANDLER_ITEM_CAPABILITY).map(handler -> {
			FluidStack simulate = handler.drain(new FluidStack(fluid, FluidAttributes.BUCKET_VOLUME), IFluidHandler.FluidAction.SIMULATE);
			return !simulate.isEmpty() && simulate.getFluid() == fluid && simulate.getAmount() == FluidAttributes.BUCKET_VOLUME;
		}).orElse(false);
	}

	private ItemStack drain(Fluid fluid, ItemStack stack) {
		return stack.getCapability(CapabilityFluidHandler.FLUID_HANDLER_ITEM_CAPABILITY)
				.map(handler -> {
					handler.drain(new FluidStack(fluid, FluidAttributes.BUCKET_VOLUME), IFluidHandler.FluidAction.EXECUTE);
					return handler.getContainer();
				})
				.orElse(stack);
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

		return container.getCapability(CapabilityFluidHandler.FLUID_HANDLER_ITEM_CAPABILITY).map(handler -> {
			int amount = handler.fill(new FluidStack(fluid, FluidAttributes.BUCKET_VOLUME), IFluidHandler.FluidAction.SIMULATE);
			return amount == FluidAttributes.BUCKET_VOLUME;
		}).orElse(false);
	}

	private ItemStack fill(Fluid fluid, ItemStack stack) {
		return stack.getCapability(CapabilityFluidHandler.FLUID_HANDLER_ITEM_CAPABILITY)
				.map(handler -> {
					handler.fill(new FluidStack(fluid, FluidAttributes.BUCKET_VOLUME), IFluidHandler.FluidAction.EXECUTE);
					return handler.getContainer();
				})
				.orElse(stack);
	}

	@Nonnull
	@Override
	public TileEntity createNewTileEntity(@Nonnull IBlockReader world) {
		return new TileAltar();
	}

	@Override
	public void onReplaced(@Nonnull BlockState state, @Nonnull World world, @Nonnull BlockPos pos, @Nonnull BlockState newState, boolean isMoving) {
		if (state.getBlock() != newState.getBlock()) {
			TileEntity te = world.getTileEntity(pos);
			if (te instanceof TileSimpleInventory) {
				net.minecraft.inventory.InventoryHelper.dropInventoryItems(world, pos, ((TileSimpleInventory) te).getItemHandler());
			}
			super.onReplaced(state, world, pos, newState, isMoving);
		}
	}

	@Override
	public boolean hasComparatorInputOverride(BlockState state) {
		return true;
	}

	@Override
	public int getComparatorInputOverride(BlockState state, World world, BlockPos pos) {
		return state.get(FLUID) == State.WATER ? 15 : 0;
	}
}
