/*
 * This class is distributed as part of the Botania Mod.
 * Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 *
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 */
package vazkii.botania.common.block;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.block.*;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.client.item.TooltipContext;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemPlacementContext;
import net.minecraft.item.ItemStack;
import net.minecraft.text.Text;
import net.minecraft.text.TranslatableText;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Formatting;
import net.minecraft.util.Hand;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.util.shape.VoxelShapes;
import net.minecraft.world.BlockView;
import net.minecraft.world.World;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import net.minecraft.world.WorldView;
import vazkii.botania.api.mana.IManaCollisionGhost;
import vazkii.botania.api.wand.IWandable;
import vazkii.botania.common.block.tile.TilePlatform;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import java.util.List;
import java.util.function.BiPredicate;

public class BlockPlatform extends BlockMod implements IWandable, IManaCollisionGhost, BlockEntityProvider {

	public enum Variant {
		ABSTRUSE(false, (pos, context) -> {
			Entity e = context.getEntity();
			return (e == null || e.getY() > pos.getY() + 0.9 && !context.isDescending());
		}),
		SPECTRAL(false, (pos, context) -> false),
		INFRANGIBLE(true, (pos, context) -> true);

		public final boolean indestructible;
		public final BiPredicate<BlockPos, ShapeContext> collide;

		private Variant(boolean i, BiPredicate<BlockPos, ShapeContext> p) {
			indestructible = i;
			collide = p;
		}
	}

	private final Variant variant;

	public BlockPlatform(@Nonnull Variant v, Settings builder) {
		super(builder);
		this.variant = v;
	}

	@Nonnull
	@Override
	public VoxelShape getOutlineShape(@Nonnull BlockState state, @Nonnull BlockView world, @Nonnull BlockPos pos, @Nonnull ShapeContext context) {
		BlockEntity te = world.getBlockEntity(pos);
		if (te instanceof TilePlatform && ((TilePlatform) te).getCamoState() != null) {
			return ((TilePlatform) te).getCamoState().getOutlineShape(world, pos);
		} else {
			return super.getOutlineShape(state, world, pos, context);
		}
	}

	@Nonnull
	@Override
	public VoxelShape getCollisionShape(@Nonnull BlockState state, @Nonnull BlockView world, @Nonnull BlockPos pos, ShapeContext context) {
		if (variant.collide.test(pos, context)) {
			// NB: Use full shape from super.getOutlineShape instead of camo state. May change later.
			return super.getOutlineShape(state, world, pos, context);
		} else {
			return VoxelShapes.empty();
		}
	}

	@Override
	public boolean canEntityDestroy(BlockState state, BlockView world, BlockPos pos, Entity entity) {
		return variant.indestructible;
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

	@Environment(EnvType.CLIENT)
	@Override
	public void appendTooltip(ItemStack stack, @Nullable BlockView worldIn, List<Text> tooltip, TooltipContext flagIn) {
		if (variant.indestructible) {
			tooltip.add(new TranslatableText("botaniamisc.creative"));
		}
	}

	@Override
	@Environment(EnvType.CLIENT)
	public void appendTooltip(ItemStack stack, @Nullable BlockView worldIn, List<Text> tooltip, TooltipContext flagIn) {
		if (variant.indestructible) {
			tooltip.add(new TranslatableText("botaniamisc.creative").formatted(Formatting.GRAY));
		}
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
					camo.setCamoState(changeState);
				}

				return ActionResult.SUCCESS;
			}
		}

		return ActionResult.PASS;
	}

}
