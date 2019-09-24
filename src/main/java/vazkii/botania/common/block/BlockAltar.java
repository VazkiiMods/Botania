/**
 * This class was created by <Vazkii>. It's distributed as
 * part of the Botania Mod. Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 *
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 *
 * File Created @ [Jan 21, 2014, 7:48:54 PM (GMT)]
 */
package vazkii.botania.common.block;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.item.ItemEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.fluid.Fluid;
import net.minecraft.fluid.Fluids;
import net.minecraft.item.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.BlockRenderLayer;
import net.minecraft.util.Hand;
import net.minecraft.util.IItemProvider;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.util.math.shapes.IBooleanFunction;
import net.minecraft.util.math.shapes.ISelectionContext;
import net.minecraft.util.math.shapes.VoxelShape;
import net.minecraft.util.math.shapes.VoxelShapes;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.IEnviromentBlockReader;
import net.minecraft.world.World;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.fluids.FluidAttributes;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.capability.CapabilityFluidHandler;
import net.minecraftforge.fluids.capability.IFluidHandler;
import net.minecraftforge.fluids.capability.IFluidHandlerItem;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.items.ItemHandlerHelper;
import vazkii.botania.api.internal.VanillaPacketDispatcher;
import vazkii.botania.api.lexicon.ILexiconable;
import vazkii.botania.api.lexicon.LexiconEntry;
import vazkii.botania.api.mana.ManaItemHandler;
import vazkii.botania.common.Botania;
import vazkii.botania.common.block.tile.TileAltar;
import vazkii.botania.common.block.tile.TileSimpleInventory;
import vazkii.botania.common.core.helper.InventoryHelper;
import vazkii.botania.common.item.ModItems;
import vazkii.botania.common.item.rod.ItemWaterRod;
import vazkii.botania.common.lexicon.LexiconData;

import javax.annotation.Nonnull;

public class BlockAltar extends BlockMod implements ILexiconable {

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

	@OnlyIn(Dist.CLIENT)
	@Nonnull
	@Override
	public BlockRenderLayer getRenderLayer() {
		return BlockRenderLayer.CUTOUT;
	}

	@Override
	public void onEntityCollision(BlockState state, World world, BlockPos pos, Entity entity) {
		if(!world.isRemote && entity instanceof ItemEntity) {
			TileAltar tile = (TileAltar) world.getTileEntity(pos);
			if(tile.collideEntityItem((ItemEntity) entity))
				VanillaPacketDispatcher.dispatchTEToNearbyPlayers(tile);
		}
	}

	@Override
	public int getLightValue(@Nonnull BlockState state, IEnviromentBlockReader world, @Nonnull BlockPos pos) {
		if(world.getBlockState(pos).getBlock() != this)
			return world.getBlockState(pos).getLightValue(world, pos);
		TileAltar tile = (TileAltar) world.getTileEntity(pos);
		return tile != null && tile.hasLava ? 15 : 0;
	}

	@Override
	public boolean onBlockActivated(BlockState state, World world, BlockPos pos, PlayerEntity player, Hand hand, BlockRayTraceResult hit) {
		TileAltar tile = (TileAltar) world.getTileEntity(pos);
		ItemStack stack = player.getHeldItem(hand);
		if(player.isSneaking()) {
			InventoryHelper.withdrawFromInventory(tile, player);
			VanillaPacketDispatcher.dispatchTEToNearbyPlayers(tile);
			return true;
		} else if(tile.isEmpty() && tile.hasWater && stack.isEmpty()) {
			tile.trySetLastRecipe(player);
			return true;
		}
		else {
			if(!stack.isEmpty() && (isValidWaterContainer(stack) || stack.getItem() == ModItems.waterRod && ManaItemHandler.requestManaExact(stack, player, ItemWaterRod.COST, false))) {
				if(!tile.hasWater) {
					if(stack.getItem() == ModItems.waterRod)
						ManaItemHandler.requestManaExact(stack, player, ItemWaterRod.COST, true);
					else if(!player.abilities.isCreativeMode)
						player.setHeldItem(hand, drain(Fluids.WATER, stack));

					tile.setWater(true);
					world.updateComparatorOutputLevel(pos, this);
					world.getChunkProvider().getLightManager().checkBlock(pos);
				}

				return true;
			} else if(!stack.isEmpty() && stack.getItem() == Items.LAVA_BUCKET) {
				if(!player.abilities.isCreativeMode)
					player.setHeldItem(hand, drain(Fluids.LAVA, stack));

				tile.setLava(true);
				tile.setWater(false);
				world.updateComparatorOutputLevel(pos, this);
				world.getChunkProvider().getLightManager().checkBlock(pos);

				return true;
			} else if(!stack.isEmpty() && stack.getItem() == Items.BUCKET && (tile.hasWater || tile.hasLava) && !Botania.gardenOfGlassLoaded) {
				ItemStack bucket = tile.hasLava ? new ItemStack(Items.LAVA_BUCKET) : new ItemStack(Items.WATER_BUCKET);
				if(stack.getCount() == 1)
					player.setHeldItem(hand, bucket);
				else {
					ItemHandlerHelper.giveItemToPlayer(player, bucket);
					stack.shrink(1);
				}

				if(tile.hasLava)
					tile.setLava(false);
				else tile.setWater(false);
				world.updateComparatorOutputLevel(pos, this);
				world.getChunkProvider().getLightManager().checkBlock(pos);

				return true;
			}
		}

		return false;
	}

	@Override
	public void fillWithRain(World world, BlockPos pos) {
		if(world.rand.nextInt(20) == 1) {
			TileEntity tile = world.getTileEntity(pos);
			if(tile instanceof TileAltar) {
				TileAltar altar = (TileAltar) tile;
				if(!altar.hasLava && !altar.hasWater)
					altar.setWater(true);
				world.updateComparatorOutputLevel(pos, this);
			}
		}
	}

	private boolean isValidWaterContainer(ItemStack stack) {
		if(stack.isEmpty() || stack.getCount() != 1)
			return false;

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
		TileSimpleInventory inv = (TileSimpleInventory) world.getTileEntity(pos);

		InventoryHelper.dropInventory(inv, world, state, pos);

		super.onReplaced(state, world, pos, newState, isMoving);
	}

	@Override
	public boolean hasComparatorInputOverride(BlockState state) {
		return true;
	}

	@Override
	public int getComparatorInputOverride(BlockState state, World world, BlockPos pos) {
		TileAltar altar = (TileAltar) world.getTileEntity(pos);
		return altar.hasWater ? 15 : 0;
	}

	@Override
	public LexiconEntry getEntry(World world, BlockPos pos, PlayerEntity player, ItemStack lexicon) {
		return LexiconData.apothecary;
	}
}
