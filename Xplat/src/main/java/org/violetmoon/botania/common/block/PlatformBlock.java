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
import net.minecraft.world.ItemInteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;

import org.jetbrains.annotations.Nullable;

import vazkii.botania.api.mana.ManaCollisionGhost;
import vazkii.botania.common.block.block_entity.PlatformBlockEntity;
import vazkii.botania.common.lib.BotaniaTags;

import java.util.List;

public abstract class PlatformBlock extends BotaniaEntityBlock implements ManaCollisionGhost {

	protected PlatformBlock(Properties properties) {
		super(properties);
	}

	public boolean requireCreativeInteractions() {
		return false;
	}

	public abstract boolean testCollision(BlockPos pos, CollisionContext context);

	@Override
	public VoxelShape getShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext context) {
		if (level.getBlockEntity(pos) instanceof PlatformBlockEntity platform && platform.getCamoState() != null) {
			return platform.getCamoState().getShape(level, pos);
		} else {
			return super.getShape(state, level, pos, context);
		}
	}

	@Override
	public VoxelShape getCollisionShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext context) {
		if (testCollision(pos, context)) {
			// NB: Use full shape from super.getOutlineShape instead of camo state. May change later.
			return super.getShape(state, level, pos, context);
		} else {
			return Shapes.empty();
		}
	}

	@Override
	public VoxelShape getBlockSupportShape(BlockState state, BlockGetter level, BlockPos pos) {
		// Vanilla defaults support shape to collision shape, but spectral platforms don't collide,
		// so set this by hand.
		return Shapes.block();
	}

	@Override
	public float getShadeBrightness(BlockState state, BlockGetter level, BlockPos pos) {
		return 1.0F;
	}

	@Override
	public BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
		return new PlatformBlockEntity(pos, state);
	}

	@Override
	public Behaviour getGhostBehaviour() {
		return Behaviour.SKIP_ALL;
	}

	@Override
	public void appendHoverText(ItemStack stack, @Nullable Item.TooltipContext context, List<Component> tooltipComponents, TooltipFlag tooltipFlag) {
		if (requireCreativeInteractions()) {
			tooltipComponents.add(Component.translatable("botaniamisc.creative").withStyle(ChatFormatting.GRAY));
		}
	}

	@Override
	public ItemInteractionResult useItemOn(ItemStack stack, BlockState state, Level level, BlockPos pos, Player player, InteractionHand hand, BlockHitResult hitResult) {
		ItemStack currentStack = player.getItemInHand(hand);

		if (requireCreativeInteractions() && !player.isCreative()) {
			return ItemInteractionResult.PASS_TO_DEFAULT_BLOCK_INTERACTION;
		}
		if (!currentStack.isEmpty()
				&& Block.byItem(currentStack.getItem()) != Blocks.AIR
				&& level.getBlockEntity(pos) instanceof PlatformBlockEntity camo) {
			BlockPlaceContext ctx = new BlockPlaceContext(player, hand, currentStack, hitResult);
			BlockState changeState = Block.byItem(currentStack.getItem()).getStateForPlacement(ctx);

			if (changeState != null && !changeState.is(BotaniaTags.Blocks.UNSUPPORTED_PLATFORM_DISGUISE)
					&& (changeState.isSolidRender(level, pos) || changeState.getRenderShape() == RenderShape.MODEL)
					&& !(changeState.getBlock() instanceof PlatformBlock)
					&& !changeState.isAir()) {
				if (!level.isClientSide) {
					camo.setCamoState(changeState);
				}

				return ItemInteractionResult.sidedSuccess(level.isClientSide());
			}
		}

		return ItemInteractionResult.PASS_TO_DEFAULT_BLOCK_INTERACTION;
	}

}
