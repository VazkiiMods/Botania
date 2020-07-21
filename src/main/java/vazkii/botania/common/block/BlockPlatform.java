/*
 * This class is distributed as part of the Botania Mod.
 * Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 *
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 */
package vazkii.botania.common.block;

import net.minecraft.block.*;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemPlacementContext;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.util.shape.VoxelShapes;
import net.minecraft.world.BlockView;
import net.minecraft.world.World;

import vazkii.botania.api.mana.IManaCollisionGhost;
import vazkii.botania.api.wand.IWandable;
import vazkii.botania.common.block.tile.TilePlatform;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public class BlockPlatform extends BlockMod implements IWandable, IManaCollisionGhost, BlockEntityProvider {

	public enum Variant {
		ABSTRUSE,
		SPECTRAL,
		INFRANGIBLE
	}

	public final Variant variant;

	public BlockPlatform(Variant v, Settings builder) {
		super(builder);
		this.variant = v;
	}

	@Nonnull
	@Override
	public VoxelShape getCollisionShape(@Nonnull BlockState state, @Nonnull BlockView world, @Nonnull BlockPos pos, ShapeContext context) {
		Entity e = context.getEntity();
		if (variant == Variant.INFRANGIBLE
				|| variant == Variant.ABSTRUSE
						&& e != null
						&& e.getY() > pos.getY() + 0.9
						&& !context.isDescending()) {
			return super.getCollisionShape(state, world, pos, context);
		} else {
			return VoxelShapes.empty();
		}
	}

	@Override
	public boolean canEntityDestroy(BlockState state, BlockView world, BlockPos pos, Entity entity) {
		return variant != Variant.INFRANGIBLE;
	}

	@Nonnull
	@Override
	public BlockEntity createBlockEntity(@Nonnull BlockView world) {
		return new TilePlatform();
	}

	@Override
	public boolean onUsedByWand(PlayerEntity player, ItemStack stack, World world, BlockPos pos, Direction side) {
		TilePlatform tile = (TilePlatform) world.getBlockEntity(pos);
		return tile.onWanded(player);
	}

	@Override
	public boolean isGhost(BlockState state, World world, BlockPos pos) {
		return true;
	}

	public static boolean isValidBlock(@Nullable BlockState state, World world, BlockPos pos) {
		return state != null && (state.isOpaqueFullCube(world, pos) || state.getRenderType() == BlockRenderType.MODEL);
	}

	@Nonnull
	@Override
	public ActionResult onUse(BlockState state, World world, BlockPos pos, PlayerEntity player, Hand hand, BlockHitResult hit) {
		BlockEntity tile = world.getBlockEntity(pos);
		ItemStack currentStack = player.getStackInHand(hand);
		if (!currentStack.isEmpty()
				&& Block.getBlockFromItem(currentStack.getItem()) != Blocks.AIR
				&& tile instanceof TilePlatform) {
			TilePlatform camo = (TilePlatform) tile;
			ItemPlacementContext ctx = new ItemPlacementContext(world, player, hand, currentStack, hit);
			BlockState changeState = Block.getBlockFromItem(currentStack.getItem()).getPlacementState(ctx);

			if (changeState != null && isValidBlock(changeState, world, pos)
					&& !(changeState.getBlock() instanceof BlockPlatform)
					&& changeState.getMaterial() != Material.AIR) {
				if (!world.isClient) {
					camo.camoState = changeState;
					world.updateListeners(pos, state, state, 3);
				}

				return ActionResult.SUCCESS;
			}
		}

		return ActionResult.PASS;
	}

}
