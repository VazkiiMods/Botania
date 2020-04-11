/*
 * This class is distributed as part of the Botania Mod.
 * Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 *
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 */
package vazkii.botania.common.block;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.item.ItemEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.fluid.Fluid;
import net.minecraft.fluid.Fluids;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
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
import net.minecraftforge.items.ItemHandlerHelper;

import vazkii.botania.api.internal.VanillaPacketDispatcher;
import vazkii.botania.api.mana.ManaItemHandler;
import vazkii.botania.common.Botania;
import vazkii.botania.common.block.tile.TileAltar;
import vazkii.botania.common.block.tile.TileSimpleInventory;
import vazkii.botania.common.core.helper.InventoryHelper;
import vazkii.botania.common.item.ModItems;
import vazkii.botania.common.item.rod.ItemWaterRod;

import javax.annotation.Nonnull;

public class BlockAltar extends BlockModWaterloggable {

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

	protected BlockAltar(Variant v, Block.Properties builder) {
		super(builder);
		this.variant = v;
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
	public int getLightValue(@Nonnull BlockState state, IBlockReader world, @Nonnull BlockPos pos) {
		TileEntity te = world.getTileEntity(pos);
		if (te instanceof TileAltar && ((TileAltar) te).getFluid() == Fluids.LAVA) {
			return 15;
		} else {
			return super.getLightValue(state, world, pos);
		}
	}

	@Override
	public ActionResultType onUse(BlockState state, World world, BlockPos pos, PlayerEntity player, Hand hand, BlockRayTraceResult hit) {
		TileAltar tile = (TileAltar) world.getTileEntity(pos);
		ItemStack stack = player.getHeldItem(hand);
		if (player.isSneaking()) {
			InventoryHelper.withdrawFromInventory(tile, player);
			VanillaPacketDispatcher.dispatchTEToNearbyPlayers(tile);
			return ActionResultType.SUCCESS;
		} else if (tile.isEmpty() && tile.getFluid() == Fluids.WATER && stack.isEmpty()) {
			tile.trySetLastRecipe(player);
			return ActionResultType.SUCCESS;
		} else {
			if (!stack.isEmpty() && (isValidWaterContainer(stack) || stack.getItem() == ModItems.waterRod && ManaItemHandler.instance().requestManaExact(stack, player, ItemWaterRod.COST, false))) {
				if (tile.getFluid() == Fluids.EMPTY) {
					if (stack.getItem() == ModItems.waterRod) {
						ManaItemHandler.instance().requestManaExact(stack, player, ItemWaterRod.COST, true);
					} else if (!player.abilities.isCreativeMode) {
						player.setHeldItem(hand, drain(Fluids.WATER, stack));
					}

					tile.setFluid(Fluids.WATER);
					world.updateComparatorOutputLevel(pos, this);
					world.getChunkProvider().getLightManager().checkBlock(pos);
				}

				return ActionResultType.SUCCESS;
			} else if (!stack.isEmpty() && stack.getItem() == Items.LAVA_BUCKET) {
				if (!player.abilities.isCreativeMode) {
					player.setHeldItem(hand, drain(Fluids.LAVA, stack));
				}

				tile.setFluid(Fluids.LAVA);
				world.updateComparatorOutputLevel(pos, this);
				world.getChunkProvider().getLightManager().checkBlock(pos);

				return ActionResultType.SUCCESS;
			} else if (!stack.isEmpty() && stack.getItem() == Items.BUCKET && tile.getFluid() != Fluids.EMPTY && !Botania.gardenOfGlassLoaded) {
				ItemStack bucket = new ItemStack(tile.getFluid().getFilledBucket());
				if (stack.getCount() == 1) {
					player.setHeldItem(hand, bucket);
				} else {
					ItemHandlerHelper.giveItemToPlayer(player, bucket);
					stack.shrink(1);
				}

				tile.setFluid(Fluids.EMPTY);
				world.updateComparatorOutputLevel(pos, this);
				world.getChunkProvider().getLightManager().checkBlock(pos);

				return ActionResultType.SUCCESS;
			}
		}

		return ActionResultType.PASS;
	}

	@Override
	public void fillWithRain(World world, BlockPos pos) {
		if (world.rand.nextInt(20) == 1) {
			TileEntity tile = world.getTileEntity(pos);
			if (tile instanceof TileAltar) {
				TileAltar altar = (TileAltar) tile;
				if (altar.getFluid() == Fluids.EMPTY) {
					altar.setFluid(Fluids.WATER);
				}
				world.updateComparatorOutputLevel(pos, this);
			}
		}
	}

	private boolean isValidWaterContainer(ItemStack stack) {
		if (stack.isEmpty() || stack.getCount() != 1) {
			return false;
		}

		return stack.getCapability(CapabilityFluidHandler.FLUID_HANDLER_ITEM_CAPABILITY).map(handler -> {
			FluidStack simulate = handler.drain(new FluidStack(Fluids.WATER, FluidAttributes.BUCKET_VOLUME), IFluidHandler.FluidAction.SIMULATE);
			return !simulate.isEmpty() && simulate.getFluid() == Fluids.WATER && simulate.getAmount() == FluidAttributes.BUCKET_VOLUME;
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

	@Override
	public boolean hasTileEntity(BlockState state) {
		return true;
	}

	@Nonnull
	@Override
	public TileEntity createTileEntity(@Nonnull BlockState state, @Nonnull IBlockReader world) {
		return new TileAltar();
	}

	@Override
	public void onReplaced(@Nonnull BlockState state, @Nonnull World world, @Nonnull BlockPos pos, @Nonnull BlockState newState, boolean isMoving) {
		if (state.getBlock() != newState.getBlock()) {
			TileSimpleInventory inv = (TileSimpleInventory) world.getTileEntity(pos);
			InventoryHelper.dropInventory(inv, world, state, pos);
			super.onReplaced(state, world, pos, newState, isMoving);
		}
	}

	@Override
	public boolean hasComparatorInputOverride(BlockState state) {
		return true;
	}

	@Override
	public int getComparatorInputOverride(BlockState state, World world, BlockPos pos) {
		TileAltar altar = (TileAltar) world.getTileEntity(pos);
		return altar.getFluid() == Fluids.WATER ? 15 : 0;
	}
}
