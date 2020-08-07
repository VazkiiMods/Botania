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
import net.minecraft.block.material.Material;
import net.minecraft.client.util.ITooltipFlag;
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
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.World;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import vazkii.botania.api.mana.IManaCollisionGhost;
import vazkii.botania.api.wand.IWandable;
import vazkii.botania.common.block.tile.TilePlatform;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import java.util.List;
import java.util.function.BiPredicate;

public class BlockPlatform extends BlockMod implements IWandable, IManaCollisionGhost, ITileEntityProvider {

	public enum Variant {
		ABSTRUSE(false, (pos, context) -> {
			Entity e = context.getEntity();
			return (e != null && e.getPosY() > pos.getY() + 0.9 && !context.func_225581_b_());
		}),
		SPECTRAL(false, (pos, context) -> false),
		INFRANGIBLE(true, (pos, context) -> true);

		public final boolean indestructible;
		public final BiPredicate<BlockPos, ISelectionContext> permeable;

		private Variant(boolean i, BiPredicate<BlockPos, ISelectionContext> p) {
			indestructible = i;
			permeable = p;
		}
	}

	public final Variant variant;

	public BlockPlatform(@Nonnull Variant v, Properties builder) {
		super(builder);
		this.variant = v;
	}

	@Nonnull
	@Override
	public VoxelShape getCollisionShape(@Nonnull BlockState state, @Nonnull IBlockReader world, @Nonnull BlockPos pos, ISelectionContext context) {
		if (variant.permeable.test(pos, context)) {
			return super.getCollisionShape(state, world, pos, context);
		} else {
			return VoxelShapes.empty();
		}
	}

	@Override
	public boolean canEntityDestroy(BlockState state, IBlockReader world, BlockPos pos, Entity entity) {
		return variant.indestructible;
	}

	@Nonnull
	@Override
	public TileEntity createNewTileEntity(@Nonnull IBlockReader world) {
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

	@Override
	@OnlyIn(Dist.CLIENT)
	public void addInformation(ItemStack stack, @Nullable IBlockReader worldIn, List<ITextComponent> tooltip, ITooltipFlag flagIn) {
		if (variant.indestructible) {
			tooltip.add(new TranslationTextComponent("botaniamisc.creative"));
		}
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
