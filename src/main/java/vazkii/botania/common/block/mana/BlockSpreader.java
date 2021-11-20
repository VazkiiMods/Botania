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
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.EntityBlock;
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;

import vazkii.botania.api.mana.ILens;
import vazkii.botania.common.block.BlockModWaterloggable;
import vazkii.botania.common.block.tile.ModTiles;
import vazkii.botania.common.block.tile.mana.TileSpreader;
import vazkii.botania.common.core.handler.ModSounds;
import vazkii.botania.common.core.helper.ColorHelper;
import vazkii.botania.common.item.ModItems;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public class BlockSpreader extends BlockModWaterloggable implements EntityBlock {
	private static final VoxelShape SHAPE = box(2, 2, 2, 14, 14, 14);

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
		this.variant = v;
	}

	@Nonnull
	@Override
	public VoxelShape getShape(BlockState blockState, BlockGetter blockGetter, BlockPos blockPos, CollisionContext collisionContext) {
		return SHAPE;
	}

	@Nonnull
	@Override
	public VoxelShape getOcclusionShape(BlockState state, @Nonnull BlockGetter world, @Nonnull BlockPos pos) {
		return SHAPE;
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

	@Nonnull
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

		ItemStack lens = spreader.getItemHandler().getItem(0);
		ItemStack heldItem = player.getItemInHand(hand);
		boolean isHeldItemLens = !heldItem.isEmpty() && heldItem.getItem() instanceof ILens;
		boolean wool = !heldItem.isEmpty() && ColorHelper.isWool(Block.byItem(heldItem.getItem()));

		if (!heldItem.isEmpty()) {
			if (heldItem.is(ModItems.twigWand)) {
				return InteractionResult.PASS;
			}
		}

		if (lens.isEmpty() && isHeldItemLens) {
			ItemStack toInsert;
			if (!player.getAbilities().instabuild) {
				toInsert = heldItem.split(1);
			} else {
				toInsert = heldItem.copy();
				toInsert.setCount(1);
			}

			spreader.getItemHandler().setItem(0, toInsert);

			return InteractionResult.SUCCESS;
		} else if (!lens.isEmpty() && !wool) {
			player.getInventory().placeItemBackInInventory(lens);
			spreader.getItemHandler().setItem(0, ItemStack.EMPTY);

			return InteractionResult.SUCCESS;
		}

		if (wool && spreader.paddingColor == null) {
			Block block = Block.byItem(heldItem.getItem());
			spreader.paddingColor = ColorHelper.getWoolColor(block);

			if (!player.getAbilities().instabuild) {
				heldItem.shrink(1);
				if (heldItem.isEmpty()) {
					player.setItemInHand(hand, ItemStack.EMPTY);
				}
			}

			world.playSound(player, pos, ModSounds.spreaderCover, SoundSource.BLOCKS, 1F, 1F);

			return InteractionResult.SUCCESS;
		} else if (wool || heldItem.isEmpty() && spreader.paddingColor != null && lens.isEmpty()) {
			ItemStack pad = new ItemStack(ColorHelper.WOOL_MAP.apply(spreader.paddingColor));
			player.getInventory().placeItemBackInInventory(pad);
			spreader.paddingColor = null;
			spreader.setChanged();

			world.playSound(player, pos, ModSounds.spreaderUncover, SoundSource.BLOCKS, 1F, 1F);

			return InteractionResult.SUCCESS;
		}

		return InteractionResult.PASS;
	}

	@Override
	public void onRemove(@Nonnull BlockState state, @Nonnull Level world, @Nonnull BlockPos pos, @Nonnull BlockState newState, boolean isMoving) {
		if (!state.is(newState.getBlock())) {
			BlockEntity tile = world.getBlockEntity(pos);
			if (!(tile instanceof TileSpreader inv)) {
				return;
			}

			if (inv.paddingColor != null) {
				ItemStack padding = new ItemStack(ColorHelper.WOOL_MAP.apply(inv.paddingColor));
				world.addFreshEntity(new ItemEntity(world, pos.getX(), pos.getY(), pos.getZ(), padding));
			}

			Containers.dropContents(world, pos, inv.getItemHandler());

			super.onRemove(state, world, pos, newState, isMoving);
		}
	}

	@Nonnull
	@Override
	public BlockEntity newBlockEntity(@Nonnull BlockPos pos, @Nonnull BlockState state) {
		return new TileSpreader(pos, state);
	}

	@org.jetbrains.annotations.Nullable
	@Override
	public <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level level, BlockState state, BlockEntityType<T> type) {
		return createTickerHelper(type, ModTiles.SPREADER, TileSpreader::commonTick);
	}
}
