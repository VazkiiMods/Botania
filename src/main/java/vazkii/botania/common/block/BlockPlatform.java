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
import net.minecraft.block.BlockRenderType;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.material.Material;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.BlockItemUseContext;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Direction;
import net.minecraft.util.Hand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.util.math.shapes.ISelectionContext;
import net.minecraft.util.math.shapes.VoxelShape;
import net.minecraft.util.math.shapes.VoxelShapes;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.World;

import vazkii.botania.api.mana.IManaCollisionGhost;
import vazkii.botania.api.wand.IWandable;
import vazkii.botania.common.block.tile.TilePlatform;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public class BlockPlatform extends BlockMod implements IWandable, IManaCollisionGhost {

	public enum Variant {
		ABSTRUSE,
		SPECTRAL,
		INFRANGIBLE
	}

	public final Variant variant;

	public BlockPlatform(Variant v, Properties builder) {
		super(builder);
		this.variant = v;
	}

	@Nonnull
	@Override
	public VoxelShape getCollisionShape(@Nonnull BlockState state, @Nonnull IBlockReader world, @Nonnull BlockPos pos, ISelectionContext context) {
		Entity e = context.getEntity();
		if (variant == Variant.INFRANGIBLE
				|| variant == Variant.ABSTRUSE
						&& e != null
						&& e.getPosY() > pos.getY() + 0.9
						&& !context.func_225581_b_()) {
			return super.getCollisionShape(state, world, pos, context);
		} else {
			return VoxelShapes.empty();
		}
	}

	@Override
	public boolean canEntityDestroy(BlockState state, IBlockReader world, BlockPos pos, Entity entity) {
		return variant != Variant.INFRANGIBLE;
	}

	@Override
	public boolean hasTileEntity(BlockState state) {
		return true;
	}

	@Nonnull
	@Override
	public TileEntity createTileEntity(@Nonnull BlockState state, @Nonnull IBlockReader world) {
		return new TilePlatform();
	}

	@Override
	public boolean onUsedByWand(PlayerEntity player, ItemStack stack, World world, BlockPos pos, Direction side) {
		TilePlatform tile = (TilePlatform) world.getTileEntity(pos);
		return tile.onWanded(player);
	}

	@Override
	public boolean isGhost(BlockState state, World world, BlockPos pos) {
		return true;
	}

	public static boolean isValidBlock(@Nullable BlockState state, World world, BlockPos pos) {
		return state != null && (state.isOpaqueCube(world, pos) || state.getRenderType() == BlockRenderType.MODEL);
	}

	@Nonnull
	@Override
	public ActionResultType onBlockActivated(BlockState state, World world, BlockPos pos, PlayerEntity player, Hand hand, BlockRayTraceResult hit) {
		TileEntity tile = world.getTileEntity(pos);
		ItemStack currentStack = player.getHeldItem(hand);
		if (!currentStack.isEmpty()
				&& Block.getBlockFromItem(currentStack.getItem()) != Blocks.AIR
				&& tile instanceof TilePlatform) {
			TilePlatform camo = (TilePlatform) tile;
			BlockItemUseContext ctx = new BlockItemUseContext(world, player, hand, currentStack, hit);
			BlockState changeState = Block.getBlockFromItem(currentStack.getItem()).getStateForPlacement(ctx);

			if (changeState != null && isValidBlock(changeState, world, pos)
					&& !(changeState.getBlock() instanceof BlockPlatform)
					&& changeState.getMaterial() != Material.AIR) {
				if (!world.isRemote) {
					camo.camoState = changeState;
					world.notifyBlockUpdate(pos, state, state, 3);
				}

				return ActionResultType.SUCCESS;
			}
		}

		return ActionResultType.PASS;
	}

}
