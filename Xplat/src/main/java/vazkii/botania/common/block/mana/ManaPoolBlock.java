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
import net.minecraft.world.ItemInteractionResult;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.*;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.EntityBlock;
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.BooleanOp;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.EntityCollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;

import org.jetbrains.annotations.Nullable;

import vazkii.botania.api.state.BotaniaStateProperties.OptionalDyeColor;
import vazkii.botania.common.block.BotaniaWaterloggedBlock;
import vazkii.botania.common.block.block_entity.BotaniaBlockEntities;
import vazkii.botania.common.block.block_entity.mana.ManaPoolBlockEntity;
import vazkii.botania.common.block.decor.BotaniaMushroomBlock;
import vazkii.botania.common.entity.ManaBurstEntity;
import vazkii.botania.common.item.material.MysticalPetalItem;

import java.util.List;
import java.util.Optional;

import static vazkii.botania.api.state.BotaniaStateProperties.OPTIONAL_DYE_COLOR;

public class ManaPoolBlock extends BotaniaWaterloggedBlock implements EntityBlock {
	public static final int MAX_MANA = 1000000;
	public static final int MAX_MANA_DILUTED = 10000;

	public static final VoxelShape NORMAL_SHAPE_INTERACT = box(0, 0, 0, 16, 8, 16);
	public static final VoxelShape NORMAL_SHAPE_CUTOUT = box(2, 2, 2, 14, 16, 14);
	private static final VoxelShape NORMAL_SHAPE = Shapes.join(NORMAL_SHAPE_INTERACT, NORMAL_SHAPE_CUTOUT, BooleanOp.ONLY_FIRST);

	public final boolean creative;
	public final boolean fabulous;
	public final int manaCapacity;

	public ManaPoolBlock(int capacity, boolean fabulous, boolean creative, Properties builder) {
		super(builder);

		this.fabulous = fabulous;
		this.creative = creative;
		this.manaCapacity = capacity;

		registerDefaultState(defaultBlockState().setValue(OPTIONAL_DYE_COLOR, OptionalDyeColor.NONE));
	}

	public boolean isCreative() {
		return creative;
	}

	public boolean isFabulous() {
		return fabulous;
	}

	public int getManaCapacity() {
		return manaCapacity;
	}

	@Override
	protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
		super.createBlockStateDefinition(builder);
		builder.add(OPTIONAL_DYE_COLOR);
	}

	@Override
	public void appendHoverText(ItemStack stack, Item.TooltipContext context, List<Component> tooltip, TooltipFlag flag) {
		super.appendHoverText(stack, context, tooltip, flag);
		if (creative) {
			for (int i = 0; i < 2; i++) {
				tooltip.add(Component.translatable("botaniamisc.creativePool" + i).withStyle(ChatFormatting.GRAY));
			}
		}
	}

	@Override
	public VoxelShape getShape(BlockState state, BlockGetter world, BlockPos pos, CollisionContext ctx) {
		return NORMAL_SHAPE;
	}

	public VoxelShape getInnerShape(BlockState state) {
		return NORMAL_SHAPE_CUTOUT;
	}

	@Override
	public VoxelShape getCollisionShape(BlockState state, BlockGetter world, BlockPos pos, CollisionContext context) {
		// Sometimes the pool's collision box is too thin for bursts shot straight up.
		return context instanceof EntityCollisionContext ecc && ecc.getEntity() instanceof ManaBurstEntity
				? getInteractionShape(state, world, pos)
				: super.getCollisionShape(state, world, pos, context);
	}

	@Override
	public VoxelShape getInteractionShape(BlockState state, @Nullable BlockGetter level, @Nullable BlockPos pos) {
		return NORMAL_SHAPE_INTERACT;
	}

	@Override
	protected ItemInteractionResult useItemOn(ItemStack stack, BlockState state, Level level, BlockPos pos,
			Player player, InteractionHand hand, BlockHitResult hitResult) {
		BlockEntity be = level.getBlockEntity(pos);
		Optional<DyeColor> itemColor = Optional.empty();
		if (stack.getItem() instanceof MysticalPetalItem petalItem) {
			itemColor = Optional.of(petalItem.color);
		}
		if (Block.byItem(stack.getItem()) instanceof BotaniaMushroomBlock mushroomBlock) {
			itemColor = Optional.of(mushroomBlock.color);
		}
		if (itemColor.isPresent() && be instanceof ManaPoolBlockEntity pool) {
			if (!itemColor.equals(pool.getColor())) {
				pool.setColor(itemColor);
				if (!player.getAbilities().instabuild) {
					stack.shrink(1);
				}
				return ItemInteractionResult.sidedSuccess(level.isClientSide());
			}
		}
		if (stack.is(Items.CLAY_BALL) && be instanceof ManaPoolBlockEntity pool && pool.getColor().isPresent()) {
			pool.setColor(Optional.empty());
			if (!player.getAbilities().instabuild) {
				stack.shrink(1);
			}
			return ItemInteractionResult.sidedSuccess(level.isClientSide());
		}
		return ItemInteractionResult.PASS_TO_DEFAULT_BLOCK_INTERACTION;
	}

	@Override
	public BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
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

	@Override
	public RenderShape getRenderShape(BlockState state) {
		if (fabulous) {
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
		return ManaPoolBlockEntity.calculateComparatorLevel(pool.getCurrentMana(), pool.getMaxMana());
	}
}
