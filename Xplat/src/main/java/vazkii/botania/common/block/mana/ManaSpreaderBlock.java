/*
 * This class is distributed as part of the Botania Mod.
 * Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 *
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 */
package vazkii.botania.common.block.mana;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.Containers;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.ItemInteractionResult;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
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
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.material.Fluids;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;

import org.jetbrains.annotations.Nullable;

import vazkii.botania.api.mana.BasicLensItem;
import vazkii.botania.api.mana.BurstProperties;
import vazkii.botania.api.state.BotaniaStateProperties;
import vazkii.botania.common.block.BotaniaWaterloggedBlock;
import vazkii.botania.common.block.block_entity.BotaniaBlockEntities;
import vazkii.botania.common.block.block_entity.mana.ManaSpreaderBlockEntity;
import vazkii.botania.common.handler.BotaniaSounds;
import vazkii.botania.common.helper.ColorHelper;
import vazkii.botania.common.item.WandOfTheForestItem;

import static vazkii.botania.api.BotaniaAPI.botaniaRL;

public class ManaSpreaderBlock extends BotaniaWaterloggedBlock implements EntityBlock {
	private static final VoxelShape SHAPE = box(2, 2, 2, 14, 14, 14);
	private static final VoxelShape SHAPE_PADDING = box(1, 1, 1, 15, 15, 15);
	private static final VoxelShape SHAPE_SCAFFOLDING = box(0, 0, 0, 16, 16, 16);
	public static final BooleanProperty HAS_SCAFFOLDING = BotaniaStateProperties.HAS_SCAFFOLDING;

	private static final ResourceLocation DEFAULT_SPREADER_MODEL_ID = botaniaRL("block/mana_spreader");
	private static final ResourceLocation DEFAULT_CORE_MODEL_ID = botaniaRL("block/mana_spreader_core");
	private static final ResourceLocation DEFAULT_SCAFFOLDING_MODEL_ID = botaniaRL("block/mana_spreader_scaffolding");

	public ManaSpreaderBlock(Properties builder) {
		super(builder);
		registerDefaultState(defaultBlockState().setValue(HAS_SCAFFOLDING, false));
	}

	// variant value definitions
	public boolean isRedstoneTriggered() {
		return false;
	}

	public boolean isRainbowRendered() {
		return false;
	}

	public BurstProperties getDefaultBurstProperties() {
		return new BurstProperties(160, 60, 4f, 0f, 1f, 0x20FF20);
	}

	public int getManaCapacity() {
		return 1000;
	}

	public int getHudColor() {
		return 0x00FF00;
	}

	public ResourceLocation getSpreaderModelId() {
		return DEFAULT_SPREADER_MODEL_ID;
	}

	public ResourceLocation getCoreModelId() {
		return DEFAULT_CORE_MODEL_ID;
	}

	public ResourceLocation getScaffoldingModelId() {
		return DEFAULT_SCAFFOLDING_MODEL_ID;
	}

	@Override
	protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
		super.createBlockStateDefinition(builder);
		builder.add(HAS_SCAFFOLDING);
	}

	@Override
	public VoxelShape getShape(BlockState blockState, BlockGetter blockGetter, BlockPos blockPos, CollisionContext collisionContext) {
		if (blockState.getValue(HAS_SCAFFOLDING)) {
			return SHAPE_SCAFFOLDING;
		}
		BlockEntity be = blockGetter.getBlockEntity(blockPos);
		return be instanceof ManaSpreaderBlockEntity spreader && spreader.paddingColor != null ? SHAPE_PADDING : SHAPE;
	}

	@Override
	public VoxelShape getOcclusionShape(BlockState state, BlockGetter world, BlockPos pos) {
		return SHAPE;
	}

	@Override
	public float getShadeBrightness(BlockState state, BlockGetter level, BlockPos pos) {
		return 1.0F;
	}

	@Override
	public void setPlacedBy(Level world, BlockPos pos, BlockState state, @Nullable LivingEntity placer, ItemStack stack) {
		Direction orientation = placer == null ? Direction.WEST : Direction.orderedByNearest(placer)[0].getOpposite();
		ManaSpreaderBlockEntity spreader = (ManaSpreaderBlockEntity) world.getBlockEntity(pos);

		switch (orientation) {
			case DOWN:
				spreader.rotationY = -90F;
				break;
			case UP:
				spreader.rotationY = 90F;
				break;
			case NORTH:
				spreader.rotationX = 270F;
				break;
			case SOUTH:
				spreader.rotationX = 90F;
				break;
			case WEST:
				break;
			case EAST:
				spreader.rotationX = 180F;
				break;
		}
	}

	@Override
	public RenderShape getRenderShape(BlockState state) {
		return RenderShape.ENTITYBLOCK_ANIMATED;
	}

	@Override
	protected ItemInteractionResult useItemOn(ItemStack heldItem, BlockState state, Level world, BlockPos pos, Player player, InteractionHand hand, BlockHitResult hitResult) {
		BlockEntity tile = world.getBlockEntity(pos);
		if (!(tile instanceof ManaSpreaderBlockEntity spreader)) {
			return ItemInteractionResult.PASS_TO_DEFAULT_BLOCK_INTERACTION;
		}

		if (heldItem.getItem() instanceof WandOfTheForestItem) {
			return ItemInteractionResult.PASS_TO_DEFAULT_BLOCK_INTERACTION;
		}
		boolean mainHandEmpty = player.getMainHandItem().isEmpty();

		ItemStack lens = spreader.getItemHandler().getItem(0);
		boolean playerHasLens = heldItem.getItem() instanceof BasicLensItem;
		boolean lensIsSame = playerHasLens && ItemStack.isSameItemSameComponents(heldItem, lens);
		ItemStack wool = spreader.paddingColor != null
				? new ItemStack(ColorHelper.WOOL_MAP.apply(spreader.paddingColor))
				: ItemStack.EMPTY;
		boolean playerHasWool = ColorHelper.isWool(Block.byItem(heldItem.getItem()));
		boolean woolIsSame = playerHasWool && ItemStack.isSameItemSameComponents(heldItem, wool);
		boolean playerHasScaffolding = !heldItem.isEmpty() && heldItem.is(Items.SCAFFOLDING);
		boolean shouldInsert = (playerHasLens && !lensIsSame)
				|| (playerHasWool && !woolIsSame)
				|| (playerHasScaffolding && !state.getValue(HAS_SCAFFOLDING));

		if (shouldInsert) {
			if (playerHasLens) {
				ItemStack toInsert = heldItem.split(1);

				if (!lens.isEmpty()) {
					player.getInventory().placeItemBackInInventory(lens);
				}

				spreader.getItemHandler().setItem(0, toInsert);
				world.playSound(player, pos, BotaniaSounds.spreaderAddLens, SoundSource.BLOCKS, 1F, 1F);
			} else if (playerHasWool) {
				Block woolBlock = Block.byItem(heldItem.getItem());

				heldItem.shrink(1);
				if (spreader.paddingColor != null) {
					ItemStack spreaderWool = new ItemStack(ColorHelper.WOOL_MAP.apply(spreader.paddingColor));
					player.getInventory().placeItemBackInInventory(spreaderWool);
				}

				spreader.paddingColor = ColorHelper.getWoolColor(woolBlock);
				spreader.setChanged();
				world.playSound(player, pos, BotaniaSounds.spreaderCover, SoundSource.BLOCKS, 1F, 1F);
			} else { // playerHasScaffolding
				world.setBlockAndUpdate(pos, state.setValue(HAS_SCAFFOLDING, true));
				world.scheduleTick(pos, Fluids.WATER, Fluids.WATER.getTickDelay(world));

				if (!player.getAbilities().instabuild) {
					heldItem.shrink(1);
				}

				world.playSound(player, pos, BotaniaSounds.spreaderScaffold, SoundSource.BLOCKS, 1F, 1F);
			}
			return ItemInteractionResult.sidedSuccess(world.isClientSide());
		}

		if (state.getValue(HAS_SCAFFOLDING) && player.isSecondaryUseActive()) {
			if (!player.getAbilities().instabuild) {
				ItemStack scaffolding = new ItemStack(Items.SCAFFOLDING);
				player.getInventory().placeItemBackInInventory(scaffolding);
			}
			world.setBlockAndUpdate(pos, state.setValue(HAS_SCAFFOLDING, false));
			world.scheduleTick(pos, Fluids.WATER, Fluids.WATER.getTickDelay(world));

			world.playSound(player, pos, BotaniaSounds.spreaderUnScaffold, SoundSource.BLOCKS, 1F, 1F);

			return ItemInteractionResult.sidedSuccess(world.isClientSide());
		}
		if (!lens.isEmpty() && (mainHandEmpty || lensIsSame)) {
			player.getInventory().placeItemBackInInventory(lens);
			spreader.getItemHandler().setItem(0, ItemStack.EMPTY);

			world.playSound(player, pos, BotaniaSounds.spreaderRemoveLens, SoundSource.BLOCKS, 1F, 1F);

			return ItemInteractionResult.sidedSuccess(world.isClientSide());
		}
		if (spreader.paddingColor != null && (mainHandEmpty || woolIsSame)) {
			player.getInventory().placeItemBackInInventory(wool);
			spreader.paddingColor = null;
			spreader.setChanged();

			world.playSound(player, pos, BotaniaSounds.spreaderUncover, SoundSource.BLOCKS, 1F, 1F);

			return ItemInteractionResult.sidedSuccess(world.isClientSide());
		}

		return ItemInteractionResult.PASS_TO_DEFAULT_BLOCK_INTERACTION;
	}

	@Override
	public void onRemove(BlockState state, Level world, BlockPos pos, BlockState newState, boolean isMoving) {
		if (!state.is(newState.getBlock())) {
			BlockEntity tile = world.getBlockEntity(pos);
			if (!(tile instanceof ManaSpreaderBlockEntity spreader)) {
				return;
			}

			if (spreader.paddingColor != null) {
				ItemStack padding = new ItemStack(ColorHelper.WOOL_MAP.apply(spreader.paddingColor));
				Containers.dropItemStack(world, pos.getX(), pos.getY(), pos.getZ(), padding);
			}

			if (state.getValue(HAS_SCAFFOLDING)) {
				ItemStack scaffolding = new ItemStack(Items.SCAFFOLDING);
				Containers.dropItemStack(world, pos.getX(), pos.getY(), pos.getZ(), scaffolding);
			}

			Containers.dropContents(world, pos, spreader.getItemHandler());

			super.onRemove(state, world, pos, newState, isMoving);
		}
	}

	@Override
	public BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
		return new ManaSpreaderBlockEntity(pos, state);
	}

	@Nullable
	@Override
	public <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level level, BlockState state, BlockEntityType<T> type) {
		return createTickerHelper(type, BotaniaBlockEntities.SPREADER, ManaSpreaderBlockEntity::commonTick);
	}
}
