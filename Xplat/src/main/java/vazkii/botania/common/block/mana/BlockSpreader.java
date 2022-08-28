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
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.Containers;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
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
import net.minecraft.world.level.material.Fluids;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import vazkii.botania.api.mana.ILens;
import vazkii.botania.api.state.BotaniaStateProps;
import vazkii.botania.common.block.BlockModWaterloggable;
import vazkii.botania.common.block.tile.ModTiles;
import vazkii.botania.common.block.tile.mana.TileSpreader;
import vazkii.botania.common.handler.ModSounds;
import vazkii.botania.common.helper.ColorHelper;
import vazkii.botania.common.item.ItemTwigWand;

public class BlockSpreader extends BlockModWaterloggable implements EntityBlock {
	private static final VoxelShape SHAPE = box(2, 2, 2, 14, 14, 14);
	private static final VoxelShape SHAPE_PADDING = box(1, 1, 1, 15, 15, 15);
	private static final VoxelShape SHAPE_SCAFFOLDING = box(0, 0, 0, 16, 16, 16);

	public enum Variant {
		MANA(160, 1000, 0x20FF20, 0x00FF00, 60, 4f, 1f),
		REDSTONE(160, 1000, 0xFF2020, 0xFF0000, 60, 4f, 1f),
		ELVEN(240, 1000, 0xFF45C4, 0xFF00AE, 80, 4f, 1.25f),
		GAIA(640, 6400, 0x20FF20, 0x00FF00, 120, 20f, 2f);

		public final int burstMana;
		public final int manaCapacity;
		public final int color;
		public final int hudColor;
		public final int preLossTicks;
		public final float lossPerTick;
		public final float motionModifier;

		Variant(int bm, int mc, int c, int hc, int plt, float lpt, float mm) {
			burstMana = bm;
			manaCapacity = mc;
			color = c;
			hudColor = hc;
			preLossTicks = plt;
			lossPerTick = lpt;
			motionModifier = mm;
		}
	}

	public final Variant variant;

	public BlockSpreader(Variant v, Properties builder) {
		super(builder);
		registerDefaultState(defaultBlockState().setValue(BotaniaStateProps.HAS_SCAFFOLDING, false));
		this.variant = v;
	}

	@Override
	protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
		super.createBlockStateDefinition(builder);
		builder.add(BotaniaStateProps.HAS_SCAFFOLDING);
	}

	@NotNull
	@Override
	public VoxelShape getShape(BlockState blockState, BlockGetter blockGetter, BlockPos blockPos, CollisionContext collisionContext) {
		if (blockState.getValue(BotaniaStateProps.HAS_SCAFFOLDING)) {
			return SHAPE_SCAFFOLDING;
		}
		BlockEntity be = blockGetter.getBlockEntity(blockPos);
		return be instanceof TileSpreader spreader && spreader.paddingColor != null ? SHAPE_PADDING : SHAPE;
	}

	@NotNull
	@Override
	public VoxelShape getOcclusionShape(BlockState state, @NotNull BlockGetter world, @NotNull BlockPos pos) {
		return SHAPE;
	}

	@Override
	public float getShadeBrightness(BlockState state, BlockGetter level, BlockPos pos) {
		return 1.0F;
	}

	@Override
	public void setPlacedBy(Level world, BlockPos pos, BlockState state, @Nullable LivingEntity placer, ItemStack stack) {
		Direction orientation = placer == null ? Direction.WEST : Direction.orderedByNearest(placer)[0].getOpposite();
		TileSpreader spreader = (TileSpreader) world.getBlockEntity(pos);

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

	@NotNull
	@Override
	public RenderShape getRenderShape(BlockState state) {
		return RenderShape.ENTITYBLOCK_ANIMATED;
	}

	@Override
	public InteractionResult use(BlockState state, Level world, BlockPos pos, Player player, InteractionHand hand, BlockHitResult hit) {
		BlockEntity tile = world.getBlockEntity(pos);
		if (!(tile instanceof TileSpreader spreader)) {
			return InteractionResult.PASS;
		}

		ItemStack heldItem = player.getItemInHand(hand);
		if (heldItem.getItem() instanceof ItemTwigWand) {
			return InteractionResult.PASS;
		}
		boolean mainHandEmpty = player.getMainHandItem().isEmpty();

		ItemStack lens = spreader.getItemHandler().getItem(0);
		boolean playerHasLens = heldItem.getItem() instanceof ILens;
		boolean lensIsSame = playerHasLens && ItemStack.isSameItemSameTags(heldItem, lens);
		ItemStack wool = spreader.paddingColor != null
				? new ItemStack(ColorHelper.WOOL_MAP.apply(spreader.paddingColor))
				: ItemStack.EMPTY;
		boolean playerHasWool = ColorHelper.isWool(Block.byItem(heldItem.getItem()));
		boolean woolIsSame = playerHasWool && ItemStack.isSameItemSameTags(heldItem, wool);
		boolean playerHasScaffolding = !heldItem.isEmpty() && heldItem.is(Items.SCAFFOLDING);
		boolean shouldInsert = (playerHasLens && !lensIsSame)
				|| (playerHasWool && !woolIsSame)
				|| (playerHasScaffolding && !state.getValue(BotaniaStateProps.HAS_SCAFFOLDING));

		if (shouldInsert) {
			if (playerHasLens) {
				ItemStack toInsert = heldItem.copy();
				toInsert.setCount(1);

				heldItem.shrink(1);
				if (!lens.isEmpty()) {
					player.getInventory().placeItemBackInInventory(lens);
				}

				spreader.getItemHandler().setItem(0, toInsert);
				world.playSound(player, pos, ModSounds.spreaderAddLens, SoundSource.BLOCKS, 1F, 1F);
			} else if (playerHasWool) {
				Block woolBlock = Block.byItem(heldItem.getItem());

				heldItem.shrink(1);
				if (spreader.paddingColor != null) {
					ItemStack spreaderWool = new ItemStack(ColorHelper.WOOL_MAP.apply(spreader.paddingColor));
					player.getInventory().placeItemBackInInventory(spreaderWool);
				}

				spreader.paddingColor = ColorHelper.getWoolColor(woolBlock);
				spreader.setChanged();
				world.playSound(player, pos, ModSounds.spreaderCover, SoundSource.BLOCKS, 1F, 1F);
			} else { // playerHasScaffolding
				world.setBlockAndUpdate(pos, state.setValue(BotaniaStateProps.HAS_SCAFFOLDING, true));
				world.scheduleTick(pos, Fluids.WATER, Fluids.WATER.getTickDelay(world));

				if (!player.getAbilities().instabuild) {
					heldItem.shrink(1);
				}

				world.playSound(player, pos, ModSounds.spreaderScaffold, SoundSource.BLOCKS, 1F, 1F);
			}
			return InteractionResult.SUCCESS;
		}

		if (state.getValue(BotaniaStateProps.HAS_SCAFFOLDING) && player.isSecondaryUseActive()) {
			if (!player.getAbilities().instabuild) {
				ItemStack scaffolding = new ItemStack(Items.SCAFFOLDING);
				player.getInventory().placeItemBackInInventory(scaffolding);
			}
			world.setBlockAndUpdate(pos, state.setValue(BotaniaStateProps.HAS_SCAFFOLDING, false));
			world.scheduleTick(pos, Fluids.WATER, Fluids.WATER.getTickDelay(world));

			world.playSound(player, pos, ModSounds.spreaderUnScaffold, SoundSource.BLOCKS, 1F, 1F);

			return InteractionResult.SUCCESS;
		}
		if (!lens.isEmpty() && (mainHandEmpty || lensIsSame)) {
			player.getInventory().placeItemBackInInventory(lens);
			spreader.getItemHandler().setItem(0, ItemStack.EMPTY);

			world.playSound(player, pos, ModSounds.spreaderRemoveLens, SoundSource.BLOCKS, 1F, 1F);

			return InteractionResult.SUCCESS;
		}
		if (spreader.paddingColor != null && (mainHandEmpty || woolIsSame)) {
			player.getInventory().placeItemBackInInventory(wool);
			spreader.paddingColor = null;
			spreader.setChanged();

			world.playSound(player, pos, ModSounds.spreaderUncover, SoundSource.BLOCKS, 1F, 1F);

			return InteractionResult.SUCCESS;
		}

		return InteractionResult.PASS;
	}

	@Override
	public void onRemove(@NotNull BlockState state, @NotNull Level world, @NotNull BlockPos pos, @NotNull BlockState newState, boolean isMoving) {
		if (!state.is(newState.getBlock())) {
			BlockEntity tile = world.getBlockEntity(pos);
			if (!(tile instanceof TileSpreader spreader)) {
				return;
			}

			if (spreader.paddingColor != null) {
				ItemStack padding = new ItemStack(ColorHelper.WOOL_MAP.apply(spreader.paddingColor));
				Containers.dropItemStack(world, pos.getX(), pos.getY(), pos.getZ(), padding);
			}

			if (state.getValue(BotaniaStateProps.HAS_SCAFFOLDING)) {
				ItemStack scaffolding = new ItemStack(Items.SCAFFOLDING);
				Containers.dropItemStack(world, pos.getX(), pos.getY(), pos.getZ(), scaffolding);
			}

			Containers.dropContents(world, pos, spreader.getItemHandler());

			super.onRemove(state, world, pos, newState, isMoving);
		}
	}

	@NotNull
	@Override
	public BlockEntity newBlockEntity(@NotNull BlockPos pos, @NotNull BlockState state) {
		return new TileSpreader(pos, state);
	}

	@org.jetbrains.annotations.Nullable
	@Override
	public <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level level, BlockState state, BlockEntityType<T> type) {
		return createTickerHelper(type, ModTiles.SPREADER, TileSpreader::commonTick);
	}
}
