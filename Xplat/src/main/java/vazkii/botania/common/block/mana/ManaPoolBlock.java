/*
 * This class is distributed as part of the Botania Mod.
 * Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 *
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 */
package vazkii.botania.common.block.mana;

import net.minecraft.ChatFormatting;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.item.DyeItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.EntityBlock;
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.BooleanOp;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.EntityCollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import vazkii.botania.common.block.BotaniaWaterloggedBlock;
import vazkii.botania.common.block.block_entity.BotaniaBlockEntities;
import vazkii.botania.common.block.block_entity.mana.ManaPoolBlockEntity;
import vazkii.botania.common.entity.ManaBurstEntity;

import java.util.List;

public class ManaPoolBlock extends BotaniaWaterloggedBlock implements EntityBlock {
	private static final VoxelShape REAL_SHAPE;
	private static final VoxelShape BURST_SHAPE;
	static {
		VoxelShape slab = box(0, 0, 0, 16, 8, 16);
		VoxelShape cutout = box(1, 1, 1, 15, 8, 15);
		VoxelShape cutoutBurst = box(1, 6, 1, 15, 8, 15);
		BURST_SHAPE = Shapes.join(slab, cutoutBurst, BooleanOp.ONLY_FIRST);
		REAL_SHAPE = Shapes.join(slab, cutout, BooleanOp.ONLY_FIRST);
	}

	public enum Variant {
		DEFAULT,
		CREATIVE,
		DILUTED,
		FABULOUS
	}

	public final Variant variant;

	public ManaPoolBlock(Variant v, Properties builder) {
		super(builder);
		this.variant = v;
	}

	@Override
	public void appendHoverText(ItemStack stack, @Nullable BlockGetter world, List<Component> tooltip, TooltipFlag flag) {
		super.appendHoverText(stack, world, tooltip, flag);
		if (variant == ManaPoolBlock.Variant.CREATIVE) {
			for (int i = 0; i < 2; i++) {
				tooltip.add(Component.translatable("botaniamisc.creativePool" + i).withStyle(ChatFormatting.GRAY));
			}
		}
	}

	@NotNull
	@Override
	public VoxelShape getShape(BlockState state, BlockGetter world, BlockPos pos, CollisionContext ctx) {
		return REAL_SHAPE;
	}

	@NotNull
	@Override
	public InteractionResult use(@NotNull BlockState state, Level world, @NotNull BlockPos pos, Player player, @NotNull InteractionHand hand, @NotNull BlockHitResult hit) {
		BlockEntity te = world.getBlockEntity(pos);
		ItemStack stack = player.getItemInHand(hand);
		if (stack.getItem() instanceof DyeItem dye && te instanceof ManaPoolBlockEntity pool) {
			DyeColor color = dye.getDyeColor();
			if (color != pool.getColor()) {
				pool.setColor(color);
				stack.shrink(1);
				return InteractionResult.SUCCESS;
			}
		}
		return super.use(state, world, pos, player, hand, hit);
	}

	@Override
	public VoxelShape getCollisionShape(BlockState state, BlockGetter world, BlockPos pos, CollisionContext context) {
		if (context instanceof EntityCollisionContext econtext
				&& econtext.getEntity() instanceof ManaBurstEntity) {
			// Sometimes the pool's collision box is too thin for bursts shot straight up.
			return BURST_SHAPE;
		} else {
			return super.getCollisionShape(state, world, pos, context);
		}
	}

	@NotNull
	@Override
	public BlockEntity newBlockEntity(@NotNull BlockPos pos, @NotNull BlockState state) {
		return new ManaPoolBlockEntity(pos, state);
	}

	@Nullable
	@Override
	public <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level level, BlockState state, BlockEntityType<T> type) {
		return createTickerHelper(type, BotaniaBlockEntities.POOL, level.isClientSide ? ManaPoolBlockEntity::clientTick : ManaPoolBlockEntity::serverTick);
	}

	@Override
	public void entityInside(BlockState state, Level world, BlockPos pos, Entity entity) {
		if (entity instanceof ItemEntity item) {
			ManaPoolBlockEntity tile = (ManaPoolBlockEntity) world.getBlockEntity(pos);
			tile.collideEntityItem(item);
		}
	}

	@NotNull
	@Override
	public RenderShape getRenderShape(BlockState state) {
		if (variant == Variant.FABULOUS) {
			return RenderShape.ENTITYBLOCK_ANIMATED;
		} else {
			return RenderShape.MODEL;
		}
	}

	@Override
	public boolean hasAnalogOutputSignal(BlockState state) {
		return true;
	}

	@Override
	public int getAnalogOutputSignal(BlockState state, Level world, BlockPos pos) {
		ManaPoolBlockEntity pool = (ManaPoolBlockEntity) world.getBlockEntity(pos);
		return ManaPoolBlockEntity.calculateComparatorLevel(pool.getCurrentMana(), pool.manaCap);
	}
}
