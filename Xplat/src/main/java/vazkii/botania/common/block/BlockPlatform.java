/*
 * This class is distributed as part of the Botania Mod.
 * Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 *
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 */
package vazkii.botania.common.block;

import net.minecraft.ChatFormatting;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
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

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import vazkii.botania.api.mana.IManaCollisionGhost;
import vazkii.botania.common.block.tile.TilePlatform;

import java.util.List;
import java.util.function.BiPredicate;

public class BlockPlatform extends BlockMod implements IManaCollisionGhost, EntityBlock {

	public enum Variant {
		ABSTRUSE(false, (pos, context) -> {
			if (context instanceof EntityCollisionContext econtext) {
				Entity e = econtext.getEntity();
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

	public BlockPlatform(@NotNull Variant v, Properties builder) {
		super(builder);
		this.variant = v;
	}

	public Variant getVariant() {
		return variant;
	}

	@NotNull
	@Override
	public VoxelShape getShape(@NotNull BlockState state, @NotNull BlockGetter world, @NotNull BlockPos pos, @NotNull CollisionContext context) {
		BlockEntity te = world.getBlockEntity(pos);
		if (te instanceof TilePlatform platform && platform.getCamoState() != null) {
			return platform.getCamoState().getShape(world, pos);
		} else {
			return super.getShape(state, world, pos, context);
		}
	}

	@NotNull
	@Override
	public VoxelShape getCollisionShape(@NotNull BlockState state, @NotNull BlockGetter world, @NotNull BlockPos pos, CollisionContext context) {
		if (variant.collide.test(pos, context)) {
			// NB: Use full shape from super.getOutlineShape instead of camo state. May change later.
			return super.getShape(state, world, pos, context);
		} else {
			return Shapes.empty();
		}
	}

	@Override
	public float getShadeBrightness(BlockState state, BlockGetter level, BlockPos pos) {
		return 1.0F;
	}

	@NotNull
	@Override
	public BlockEntity newBlockEntity(@NotNull BlockPos pos, @NotNull BlockState state) {
		return new TilePlatform(pos, state);
	}

	@Override
	public Behaviour getGhostBehaviour() {
		return Behaviour.SKIP_ALL;
	}

	public static boolean isValidBlock(@Nullable BlockState state, Level world, BlockPos pos) {
		return state != null && (state.isSolidRender(world, pos) || state.getRenderShape() == RenderShape.MODEL);
	}

	@Override
	public void appendHoverText(ItemStack stack, @Nullable BlockGetter worldIn, List<Component> tooltip, TooltipFlag flagIn) {
		if (variant.indestructible) {
			tooltip.add(Component.translatable("botaniamisc.creative").withStyle(ChatFormatting.GRAY));
		}
	}

	@NotNull
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
