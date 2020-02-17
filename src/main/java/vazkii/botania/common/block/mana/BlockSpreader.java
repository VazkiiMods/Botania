/**
 * This class was created by <Vazkii>. It's distributed as
 * part of the Botania Mod. Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 *
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 *
 * File Created @ [Jan 25, 2014, 9:38:23 PM (GMT)]
 */
package vazkii.botania.common.block.mana;

import com.google.common.collect.ImmutableList;
import net.minecraft.block.Block;
import net.minecraft.block.BlockRenderType;
import net.minecraft.block.BlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.item.ItemEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Direction;
import net.minecraft.util.Hand;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.util.math.shapes.ISelectionContext;
import net.minecraft.util.math.shapes.VoxelShape;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.World;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.items.ItemHandlerHelper;
import vazkii.botania.api.ColorHelper;
import vazkii.botania.api.mana.ILens;
import vazkii.botania.api.wand.IWandHUD;
import vazkii.botania.api.wand.IWandable;
import vazkii.botania.api.wand.IWireframeAABBProvider;
import vazkii.botania.common.block.BlockMod;
import vazkii.botania.common.block.tile.mana.TileSpreader;
import vazkii.botania.common.core.helper.InventoryHelper;
import vazkii.botania.common.item.ModItems;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.List;

public class BlockSpreader extends BlockMod implements IWandable, IWandHUD, IWireframeAABBProvider {
	private static final VoxelShape RENDER_SHAPE = makeCuboidShape(1, 1, 1, 15, 15, 15);
	public enum Variant {
		MANA,
		REDSTONE,
		ELVEN,
		GAIA
	}

	public final Variant variant;
	public BlockSpreader(Variant v, Properties builder) {
		super(builder);
		this.variant = v;
	}

	@Nonnull
	@Override
	public VoxelShape getRenderShape(BlockState state, @Nonnull IBlockReader world, @Nonnull BlockPos pos) {
		return RENDER_SHAPE;
	}

	@Override
	public boolean canEntitySpawn(BlockState state, @Nonnull IBlockReader world, @Nonnull BlockPos pos, EntityType<?> type) {
		return false;
	}

	@Override
	public void onBlockPlacedBy(World world, BlockPos pos, BlockState state, LivingEntity placer, ItemStack stack) {
		Direction orientation = Direction.getFacingDirections(placer)[0].getOpposite();
		TileSpreader spreader = (TileSpreader) world.getTileEntity(pos);

		switch(orientation) {
		case DOWN:
			spreader.rotationY = -90F;
			break;
		case UP:
			spreader.rotationY = 90F;
			break;
		case NORTH:
			spreader.rotationX = 270F;
			break;
		case SOUTH:
			spreader.rotationX = 90F;
			break;
		case WEST:
			break;
		default:
			spreader.rotationX = 180F;
			break;
		}
	}

	@Nonnull
	@Override
	public BlockRenderType getRenderType(BlockState state) {
		return BlockRenderType.ENTITYBLOCK_ANIMATED;
	}

	@Override
	public ActionResultType onUse(BlockState state, World world, BlockPos pos, PlayerEntity player, Hand hand, BlockRayTraceResult hit) {
		TileEntity tile = world.getTileEntity(pos);
		if(!(tile instanceof TileSpreader))
			return ActionResultType.PASS;

		TileSpreader spreader = (TileSpreader) tile;
		ItemStack lens = spreader.getItemHandler().getStackInSlot(0);
		ItemStack heldItem = player.getHeldItem(hand);
		boolean isHeldItemLens = !heldItem.isEmpty() && heldItem.getItem() instanceof ILens;
		boolean wool = !heldItem.isEmpty() && ColorHelper.WOOL_MAP.containsValue(Block.getBlockFromItem(heldItem.getItem()).delegate);

		if(!heldItem.isEmpty())
			if(heldItem.getItem() == ModItems.twigWand)
				return ActionResultType.PASS;

		if(lens.isEmpty() && isHeldItemLens) {
			if (!player.abilities.isCreativeMode)
				player.setHeldItem(hand, ItemStack.EMPTY);

			spreader.getItemHandler().setStackInSlot(0, heldItem.copy());
			spreader.markDirty();
		} else if(!lens.isEmpty() && !wool) {
			ItemHandlerHelper.giveItemToPlayer(player, lens);
			spreader.getItemHandler().setStackInSlot(0, ItemStack.EMPTY);
			spreader.markDirty();
		}

		if(wool && spreader.paddingColor == null) {
			Block block = Block.getBlockFromItem(heldItem.getItem());
			spreader.paddingColor = ColorHelper.WOOL_MAP.inverse().get(block.delegate);
			heldItem.shrink(1);
			if(heldItem.isEmpty())
				player.setHeldItem(hand, ItemStack.EMPTY);
		} else if(heldItem.isEmpty() && spreader.paddingColor != null && lens.isEmpty()) {
			ItemStack pad = new ItemStack(ColorHelper.WOOL_MAP.get(spreader.paddingColor).get());
			ItemHandlerHelper.giveItemToPlayer(player, pad);
			spreader.paddingColor = null;
			spreader.markDirty();
		}

		return ActionResultType.SUCCESS;
	}

	@Override
	public void onReplaced(@Nonnull BlockState state, @Nonnull World world, @Nonnull BlockPos pos, @Nonnull BlockState newState, boolean isMoving) {
		if (state.getBlock() != newState.getBlock()) {
			TileEntity tile = world.getTileEntity(pos);
			if(!(tile instanceof TileSpreader))
				return;

			TileSpreader inv = (TileSpreader) tile;

			if(inv.paddingColor != null) {
				ItemStack padding = new ItemStack(ColorHelper.WOOL_MAP.get(inv.paddingColor).get());
				world.addEntity(new ItemEntity(world, pos.getX(), pos.getY(), pos.getZ(), padding));
			}

			InventoryHelper.dropInventory(inv, world, state, pos);

			super.onReplaced(state, world, pos, newState, isMoving);
		}
	}

	@Override
	public boolean onUsedByWand(PlayerEntity player, ItemStack stack, World world, BlockPos pos, Direction side) {
		((TileSpreader) world.getTileEntity(pos)).onWanded(player, stack);
		return true;
	}

	@Override
	public boolean hasTileEntity(BlockState state) {
		return true;
	}

	@Nonnull
	@Override
	public TileEntity createTileEntity(@Nonnull BlockState state, @Nonnull IBlockReader world) {
		return new TileSpreader();
	}

	@OnlyIn(Dist.CLIENT)
	@Override
	public void renderHUD(Minecraft mc, World world, BlockPos pos) {
		((TileSpreader) world.getTileEntity(pos)).renderHUD(mc);
	}

	@Override
	public List<AxisAlignedBB> getWireframeAABB(World world, BlockPos pos) {
		return ImmutableList.of(new AxisAlignedBB(pos).shrink(1.0/16.0));
	}
}
