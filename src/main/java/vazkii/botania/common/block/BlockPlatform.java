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
import net.minecraft.ChatFormatting;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.EntityBlock;
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.Material;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.EntityCollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;

import vazkii.botania.api.mana.IManaCollisionGhost;
import vazkii.botania.common.block.tile.TilePlatform;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import java.util.List;
import java.util.function.BiPredicate;

public class BlockPlatform extends BlockMod implements IManaCollisionGhost, EntityBlock {

	public enum Variant {
		ABSTRUSE(false, (pos, context) -> {
			if (context instanceof EntityCollisionContext econtext) {
				Entity e = econtext.getEntity().orElse(null);
				return (e == null || e.getY() > pos.getY() + 0.9 && !context.isDescending());
			} else {
				return true;
			}
		}),
		SPECTRAL(false, (pos, context) -> false),
		INFRANGIBLE(true, (pos, context) -> true);

		public final boolean indestructible;
		public final BiPredicate<BlockPos, CollisionContext> collide;

		Variant(boolean i, BiPredicate<BlockPos, CollisionContext> p) {
			indestructible = i;
			collide = p;
		}
	}

	private final Variant variant;

	public BlockPlatform(@Nonnull Variant v, Properties builder) {
		super(builder);
		this.variant = v;
	}

	public Variant getVariant() {
		return variant;
	}

	@Nonnull
	@Override
	public VoxelShape getShape(@Nonnull BlockState state, @Nonnull BlockGetter world, @Nonnull BlockPos pos, @Nonnull CollisionContext context) {
		BlockEntity te = world.getBlockEntity(pos);
		if (te instanceof TilePlatform && ((TilePlatform) te).getCamoState() != null) {
			return ((TilePlatform) te).getCamoState().getShape(world, pos);
		} else {
			return super.getShape(state, world, pos, context);
		}
	}

	@Nonnull
	@Override
	public VoxelShape getCollisionShape(@Nonnull BlockState state, @Nonnull BlockGetter world, @Nonnull BlockPos pos, CollisionContext context) {
		if (variant.collide.test(pos, context)) {
			// NB: Use full shape from super.getOutlineShape instead of camo state. May change later.
			return super.getShape(state, world, pos, context);
		} else {
			return Shapes.empty();
		}
	}

	@Nonnull
	@Override
	public BlockEntity newBlockEntity(@Nonnull BlockPos pos, @Nonnull BlockState state) {
		return new TilePlatform(pos, state);
	}

	@Override
	public boolean isGhost(BlockState state, Level world, BlockPos pos) {
		return true;
	}

	public static boolean isValidBlock(@Nullable BlockState state, Level world, BlockPos pos) {
		return state != null && (state.isSolidRender(world, pos) || state.getRenderShape() == RenderShape.MODEL);
	}

	@Override
	@Environment(EnvType.CLIENT)
	public void appendHoverText(ItemStack stack, @Nullable BlockGetter worldIn, List<Component> tooltip, TooltipFlag flagIn) {
		if (variant.indestructible) {
			tooltip.add(new TranslatableComponent("botaniamisc.creative").withStyle(ChatFormatting.GRAY));
		}
	}

	@Nonnull
	@Override
	public InteractionResult use(BlockState state, Level world, BlockPos pos, Player player, InteractionHand hand, BlockHitResult hit) {
		BlockEntity tile = world.getBlockEntity(pos);
		ItemStack currentStack = player.getItemInHand(hand);

		if (variant.indestructible && !player.isCreative()) {
			return InteractionResult.PASS;
		}
		if (!currentStack.isEmpty()
				&& Block.byItem(currentStack.getItem()) != Blocks.AIR
				&& tile instanceof TilePlatform camo) {
			BlockPlaceContext ctx = new BlockPlaceContext(player, hand, currentStack, hit);
			BlockState changeState = Block.byItem(currentStack.getItem()).getStateForPlacement(ctx);

			if (isValidBlock(changeState, world, pos)
					&& !(changeState.getBlock() instanceof BlockPlatform)
					&& changeState.getMaterial() != Material.AIR) {
				if (!world.isClientSide) {
					camo.setCamoState(changeState);
				}

				return InteractionResult.SUCCESS;
			}
		}

		return InteractionResult.PASS;
	}

}
