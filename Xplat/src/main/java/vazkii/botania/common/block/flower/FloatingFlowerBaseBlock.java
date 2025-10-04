package vazkii.botania.common.block.flower;

import net.minecraft.core.BlockPos;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.stats.Stats;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.ItemInteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.EntityBlock;
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.gameevent.GameEvent;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;

import org.jetbrains.annotations.Nullable;

import vazkii.botania.api.BotaniaAPI;
import vazkii.botania.api.block.FloatingFlower;
import vazkii.botania.api.block.FloatingFlowerProvider;
import vazkii.botania.api.block.IslandType;
import vazkii.botania.api.internal.VanillaPacketDispatcher;
import vazkii.botania.api.state.BotaniaStateProperties;
import vazkii.botania.client.patchouli.PatchouliUtils;
import vazkii.botania.common.block.BotaniaWaterloggedBlock;
import vazkii.botania.common.block.block_entity.flower.FloatingFlowerBlockEntity;
import vazkii.botania.common.lib.BotaniaTags;
import vazkii.botania.xplat.BotaniaConfig;
import vazkii.botania.xplat.XplatAbstractions;

import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

public abstract class FloatingFlowerBaseBlock extends BotaniaWaterloggedBlock implements EntityBlock {
	private static final VoxelShape SHAPE = box(1.6, 1.6, 1.6, 14.4, 14.4, 14.4);
	@Nullable
	private static Map<ItemLike, IslandType> ISLAND_TYPE_FOR_ITEM;

	public FloatingFlowerBaseBlock(Properties builder) {
		super(builder);
		registerDefaultState(defaultBlockState().setValue(BotaniaStateProperties.DIMMED, false));
	}

	@Override
	protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
		super.createBlockStateDefinition(builder);
		builder.add(BotaniaStateProperties.DIMMED);
	}

	@Override
	public VoxelShape getShape(BlockState state, BlockGetter world, BlockPos pos, CollisionContext ctx) {
		return SHAPE;
	}

	@Override
	public RenderShape getRenderShape(BlockState state) {
		if (!XplatAbstractions.INSTANCE.isPhysicalClient()) {
			return RenderShape.ENTITYBLOCK_ANIMATED;
		}
		return BotaniaConfig.client().staticFloaters() || PatchouliUtils.isInVisualizer()
				? RenderShape.MODEL
				: RenderShape.ENTITYBLOCK_ANIMATED;
	}

	@Override
	protected ItemInteractionResult useItemOn(ItemStack stack, BlockState state, Level level, BlockPos pos,
			Player player, InteractionHand hand, BlockHitResult hitResult) {
		BlockEntity te = level.getBlockEntity(pos);
		if (te instanceof FloatingFlowerProvider provider && provider.getFloatingData() != null) {
			FloatingFlower flower = provider.getFloatingData();
			IslandType type = getIslandTypeForItem(stack.getItem());
			if (type != null && type != flower.getIslandType()) {
				if (!level.isClientSide) {
					flower.setIslandType(type);
					level.playSound(null, pos, type.changeSound(), SoundSource.BLOCKS, 1, 1);
					player.awardStat(Stats.ITEM_USED.get(stack.getItem()));
					level.gameEvent(GameEvent.BLOCK_CHANGE, pos, GameEvent.Context.of(player, state));
					VanillaPacketDispatcher.dispatchTEToNearbyPlayers(te);
				}

				if (!player.getAbilities().instabuild) {
					stack.shrink(1);
				}
				return ItemInteractionResult.sidedSuccess(level.isClientSide());
			}
		}
		Boolean isDimmed = state.getValue(BotaniaStateProperties.DIMMED);
		if (stack.is(isDimmed ? BotaniaTags.Items.UNDIMS_FLOATING_FLOWERS : BotaniaTags.Items.DIMS_FLOATING_FLOWERS)) {
			level.setBlock(pos, state.setValue(BotaniaStateProperties.DIMMED, !isDimmed), Block.UPDATE_CLIENTS);
			level.playSound(player, pos,
					isDimmed ? SoundEvents.GLOW_INK_SAC_USE : SoundEvents.INK_SAC_USE,
					SoundSource.BLOCKS, 1, 1);
			player.awardStat(Stats.ITEM_USED.get(stack.getItem()));
			level.gameEvent(GameEvent.BLOCK_CHANGE, pos, GameEvent.Context.of(player, state));
			if (!player.getAbilities().instabuild) {
				stack.shrink(1);
			}
			return ItemInteractionResult.sidedSuccess(level.isClientSide());
		}
		return ItemInteractionResult.PASS_TO_DEFAULT_BLOCK_INTERACTION;
	}

	@Nullable
	private static IslandType getIslandTypeForItem(ItemLike item) {
		if (ISLAND_TYPE_FOR_ITEM == null) {
			ISLAND_TYPE_FOR_ITEM = BotaniaAPI.instance().getIslandTypeRegistry().stream()
					.collect(Collectors.toUnmodifiableMap(IslandType::item, Function.identity()));
		}
		return ISLAND_TYPE_FOR_ITEM.get(item);
	}

	@Override
	public BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
		return new FloatingFlowerBlockEntity(pos, state);
	}
}
