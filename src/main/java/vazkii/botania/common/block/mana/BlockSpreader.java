/*
 * This class is distributed as part of the Botania Mod.
 * Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 *
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 */
package vazkii.botania.common.block.mana;

import com.google.common.collect.ImmutableList;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.block.Block;
import net.minecraft.block.BlockEntityProvider;
import net.minecraft.block.BlockRenderType;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.ItemEntity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.ItemScatterer;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.Direction;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.world.BlockView;
import net.minecraft.world.World;

import vazkii.botania.api.mana.ILens;
import vazkii.botania.api.wand.IWandHUD;
import vazkii.botania.api.wand.IWandable;
import vazkii.botania.api.wand.IWireframeAABBProvider;
import vazkii.botania.common.block.BlockModWaterloggable;
import vazkii.botania.common.block.tile.mana.TileSpreader;
import vazkii.botania.common.core.helper.ColorHelper;
import vazkii.botania.common.item.ModItems;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import java.util.List;

public class BlockSpreader extends BlockModWaterloggable implements BlockEntityProvider, IWandable, IWandHUD, IWireframeAABBProvider {
	private static final VoxelShape RENDER_SHAPE = createCuboidShape(1, 1, 1, 15, 15, 15);

	public enum Variant {
		MANA(160, 1000, 0x20FF20, 0x00FF00, 60, 4f, 1f),
		REDSTONE(160, 1000, 0xFF2020, 0xFF0000, 60, 4f, 1f),
		ELVEN(240, 1000, 0xFF45C4, 0xFF00AE, 80, 4f, 1.25f),
		GAIA(640, 6400, 0x20FF20, 0x00FF00, 120, 20f, 2f);

		public final int burstMana;
		public final int manaCapacity;
		public final int color;
		public final int hudColor;
		public final int preLossTicks;
		public final float lossPerTick;
		public final float motionModifier;

		Variant(int bm, int mc, int c, int hc, int plt, float lpt, float mm) {
			burstMana = bm;
			manaCapacity = mc;
			color = c;
			hudColor = hc;
			preLossTicks = plt;
			lossPerTick = lpt;
			motionModifier = mm;
		}
	}

	public final Variant variant;

	public BlockSpreader(Variant v, Settings builder) {
		super(builder);
		this.variant = v;
	}

	@Nonnull
	@Override
	public VoxelShape getCullingShape(BlockState state, @Nonnull BlockView world, @Nonnull BlockPos pos) {
		return RENDER_SHAPE;
	}

	@Override
	public void onPlaced(World world, BlockPos pos, BlockState state, @Nullable LivingEntity placer, ItemStack stack) {
		Direction orientation = placer == null ? Direction.WEST : Direction.getEntityFacingOrder(placer)[0].getOpposite();
		TileSpreader spreader = (TileSpreader) world.getBlockEntity(pos);

		switch (orientation) {
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
		case EAST:
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
	public ActionResult onUse(BlockState state, World world, BlockPos pos, PlayerEntity player, Hand hand, BlockHitResult hit) {
		BlockEntity tile = world.getBlockEntity(pos);
		if (!(tile instanceof TileSpreader)) {
			return ActionResult.PASS;
		}

		TileSpreader spreader = (TileSpreader) tile;
		ItemStack lens = spreader.getItemHandler().getStack(0);
		ItemStack heldItem = player.getStackInHand(hand);
		boolean isHeldItemLens = !heldItem.isEmpty() && heldItem.getItem() instanceof ILens;
		boolean wool = !heldItem.isEmpty() && ColorHelper.isWool(Block.getBlockFromItem(heldItem.getItem()));

		if (!heldItem.isEmpty()) {
			if (heldItem.getItem() == ModItems.twigWand) {
				return ActionResult.PASS;
			}
		}

		if (lens.isEmpty() && isHeldItemLens) {
			if (!player.abilities.creativeMode) {
				player.setStackInHand(hand, ItemStack.EMPTY);
			}

			spreader.getItemHandler().setStack(0, heldItem.copy());
		} else if (!lens.isEmpty() && !wool) {
			player.inventory.offerOrDrop(player.world, lens);
			spreader.getItemHandler().setStack(0, ItemStack.EMPTY);
		}

		if (wool && spreader.paddingColor == null) {
			Block block = Block.getBlockFromItem(heldItem.getItem());
			spreader.paddingColor = ColorHelper.getWoolColor(block);
			heldItem.decrement(1);
			if (heldItem.isEmpty()) {
				player.setStackInHand(hand, ItemStack.EMPTY);
			}
		} else if (heldItem.isEmpty() && spreader.paddingColor != null && lens.isEmpty()) {
			ItemStack pad = new ItemStack(ColorHelper.WOOL_MAP.apply(spreader.paddingColor));
			player.inventory.offerOrDrop(player.world, pad);
			spreader.paddingColor = null;
			spreader.markDirty();
		}

		return ActionResult.SUCCESS;
	}

	@Override
	public void onStateReplaced(@Nonnull BlockState state, @Nonnull World world, @Nonnull BlockPos pos, @Nonnull BlockState newState, boolean isMoving) {
		if (state.getBlock() != newState.getBlock()) {
			BlockEntity tile = world.getBlockEntity(pos);
			if (!(tile instanceof TileSpreader)) {
				return;
			}

			TileSpreader inv = (TileSpreader) tile;

			if (inv.paddingColor != null) {
				ItemStack padding = new ItemStack(ColorHelper.WOOL_MAP.apply(inv.paddingColor));
				world.spawnEntity(new ItemEntity(world, pos.getX(), pos.getY(), pos.getZ(), padding));
			}

			ItemScatterer.spawn(world, pos, inv.getItemHandler());

			super.onStateReplaced(state, world, pos, newState, isMoving);
		}
	}

	@Override
	public boolean onUsedByWand(PlayerEntity player, ItemStack stack, World world, BlockPos pos, Direction side) {
		((TileSpreader) world.getBlockEntity(pos)).onWanded(player, stack);
		return true;
	}

	@Nonnull
	@Override
	public BlockEntity createBlockEntity(@Nonnull BlockView world) {
		return new TileSpreader();
	}

	@Environment(EnvType.CLIENT)
	@Override
	public void renderHUD(MatrixStack ms, MinecraftClient mc, World world, BlockPos pos) {
		((TileSpreader) world.getBlockEntity(pos)).renderHUD(ms, mc);
	}

	@Override
	public List<Box> getWireframeAABB(World world, BlockPos pos) {
		return ImmutableList.of(new Box(pos).contract(1.0 / 16.0));
	}
}
