/*
 * This class is distributed as part of the Botania Mod.
 * Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 *
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 */
package vazkii.botania.common.block.subtile.functional;

import com.google.common.collect.ImmutableList;
import com.mojang.blaze3d.vertex.PoseStack;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.client.resources.language.I18n;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.Registry;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.Vec3;

import vazkii.botania.api.item.IFlowerPlaceable;
import vazkii.botania.api.subtile.RadiusDescriptor;
import vazkii.botania.api.subtile.TileEntityFunctionalFlower;
import vazkii.botania.common.block.ModSubtiles;
import vazkii.botania.common.core.handler.ConfigHandler;
import vazkii.botania.mixin.AccessorItemEntity;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import java.util.*;

public class SubTileRannuncarpus extends TileEntityFunctionalFlower {
	private static final int PICKUP_RANGE = 2;
	private static final int PICKUP_RANGE_Y = 3;
	private static final int RANGE_PLACE_MANA = 8;
	private static final int RANGE_PLACE = 6;
	private static final int RANGE_PLACE_Y = 6;

	private static final int RANGE_PLACE_MANA_MINI = 3;
	private static final int RANGE_PLACE_MINI = 2;
	private static final int RANGE_PLACE_Y_MINI = 2;
	private static final String TAG_STATE_SENSITIVE = "stateSensitive";
	private boolean stateSensitive = false;

	protected SubTileRannuncarpus(BlockEntityType<?> type, BlockPos pos, BlockState state) {
		super(type, pos, state);
	}

	public SubTileRannuncarpus(BlockPos pos, BlockState state) {
		this(ModSubtiles.RANNUNCARPUS, pos, state);
	}

	@Override
	public void tickFlower() {
		super.tickFlower();

		if (getLevel().isClientSide || redstoneSignal > 0) {
			return;
		}

		if (ticksExisted % 10 == 0) {
			List<ItemEntity> items = getLevel().getEntitiesOfClass(ItemEntity.class, new AABB(getEffectivePos().offset(-PICKUP_RANGE, -PICKUP_RANGE_Y, -PICKUP_RANGE), getEffectivePos().offset(PICKUP_RANGE + 1, PICKUP_RANGE_Y + 1, PICKUP_RANGE + 1)));
			int slowdown = getSlowdownFactor();

			for (ItemEntity item : items) {
				int age = ((AccessorItemEntity) item).getAge();
				if (age < 60 + slowdown || !item.isAlive() || item.getItem().isEmpty()) {
					continue;
				}

				ItemStack stack = item.getItem();
				Item stackItem = stack.getItem();
				ResourceLocation id = Registry.ITEM.getKey(stackItem);
				if (ConfigHandler.blacklistedRannuncarpusModIds.contains(id.getNamespace())
						|| ConfigHandler.blacklistedRannuncarpusItems.contains(id)) {
					continue;
				}

				if (stackItem instanceof BlockItem || stackItem instanceof IFlowerPlaceable) {
					BlockPos coords = getCandidatePosition(getLevel().random);
					if (coords == null) {
						continue;
					}
					BlockHitResult ray = new BlockHitResult(new Vec3(coords.getX() + 0.5, coords.getY() + 1, coords.getZ() + 0.5), Direction.UP, coords, false);
					BlockPlaceContext ctx = new RannuncarpusPlaceContext(getLevel(), stack, ray, worldPosition);

					boolean success = false;
					if (stackItem instanceof IFlowerPlaceable) {
						success = ((IFlowerPlaceable) stackItem).tryPlace(this, ctx);
					}
					if (stackItem instanceof BlockItem) {
						success = ((BlockItem) stackItem).place(ctx).consumesAction();
					}

					if (success) {
						if (ConfigHandler.COMMON.blockBreakParticles.getValue()) {
							BlockState state = getLevel().getBlockState(ctx.getClickedPos());
							getLevel().levelEvent(2001, coords, Block.getId(state));
						}
						if (getMana() > 1) {
							addMana(-1);
						}
						return;
					}
				}
			}
		}
	}

	public BlockState getUnderlyingBlock() {
		return getLevel().getBlockState(getEffectivePos().below(isFloating() ? 1 : 2));
	}

	@Nullable
	private BlockPos getCandidatePosition(Random rand) {
		int rangePlace = getPlaceRange();
		int rangePlaceY = getVerticalPlaceRange();
		BlockPos center = getEffectivePos();
		BlockState filter = getUnderlyingBlock();
		List<BlockPos> ret = new ArrayList<>();

		for (BlockPos pos : BlockPos.betweenClosed(center.offset(-rangePlace, -rangePlaceY, -rangePlace),
				center.offset(rangePlace, rangePlaceY, rangePlace))) {
			BlockState state = getLevel().getBlockState(pos);
			BlockState up = getLevel().getBlockState(pos.above());

			boolean matches;
			if (stateSensitive) {
				matches = state == filter;
			} else {
				matches = state.is(filter.getBlock());
			}

			if (matches && (up.isAir() || up.getMaterial().isReplaceable())) {
				ret.add(pos.immutable());
			}
		}

		return ret.isEmpty() ? null : ret.get(rand.nextInt(ret.size()));
	}

	@Override
	public boolean acceptsRedstone() {
		return true;
	}

	@Override
	public void readFromPacketNBT(CompoundTag cmp) {
		super.readFromPacketNBT(cmp);
		if (cmp.contains(TAG_STATE_SENSITIVE)) {
			stateSensitive = cmp.getBoolean(TAG_STATE_SENSITIVE);
		} else {
			// old flowers stay state sensitive, new flowers are state insensitive
			stateSensitive = true;
		}
	}

	@Override
	public void writeToPacketNBT(CompoundTag cmp) {
		super.writeToPacketNBT(cmp);
		cmp.putBoolean(TAG_STATE_SENSITIVE, stateSensitive);
	}

	@Override
	public boolean onWanded(Player player, ItemStack wand) {
		if (player == null || player.isShiftKeyDown()) {
			stateSensitive = !stateSensitive;
			setChanged();
			sync();
			return true;
		}
		return super.onWanded(player, wand);
	}

	@Environment(EnvType.CLIENT)
	@Override
	public void renderHUD(PoseStack ms, Minecraft mc) {
		super.renderHUD(ms, mc);

		BlockState filter = getUnderlyingBlock();
		ItemStack recieverStack = new ItemStack(filter.getBlock());
		int color = getColor();

		if (!recieverStack.isEmpty()) {
			Component stackName = recieverStack.getHoverName();
			int width = 16 + mc.font.width(stackName) / 2;
			int x = mc.getWindow().getGuiScaledWidth() / 2 - width;
			int y = mc.getWindow().getGuiScaledHeight() / 2 + 30;

			mc.font.drawShadow(ms, stackName, x + 20, y + 5, color);
			mc.getItemRenderer().renderAndDecorateItem(recieverStack, x, y);

			String mode = I18n.get("botaniamisc.rannuncarpus." + (stateSensitive ? "state_sensitive" : "state_insensitive"));
			x = mc.getWindow().getGuiScaledWidth() / 2 - mc.font.width(mode) / 2;
			y = mc.getWindow().getGuiScaledHeight() / 2 + 50;
			mc.font.drawShadow(ms, mode, x, y, ChatFormatting.WHITE.getColor());
		}

	}

	@Override
	public RadiusDescriptor getRadius() {
		return new RadiusDescriptor.Square(getEffectivePos(), getPlaceRange());
	}

	@Override
	public RadiusDescriptor getSecondaryRadius() {
		if (getPlaceRange() == PICKUP_RANGE) {
			return null;
		}
		return new RadiusDescriptor.Square(getEffectivePos(), PICKUP_RANGE);
	}

	public int getPlaceRange() {
		return getMana() > 0 ? RANGE_PLACE_MANA : RANGE_PLACE;
	}

	public int getVerticalPlaceRange() {
		return RANGE_PLACE_Y;
	}

	@Override
	public int getMaxMana() {
		return 20;
	}

	@Override
	public int getColor() {
		return 0xFFB27F;
	}

	public static class Mini extends SubTileRannuncarpus {
		public Mini(BlockPos pos, BlockState state) {
			super(ModSubtiles.RANNUNCARPUS_CHIBI, pos, state);
		}

		@Override
		public int getPlaceRange() {
			return getMana() > 0 ? RANGE_PLACE_MANA_MINI : RANGE_PLACE_MINI;
		}

		@Override
		public int getVerticalPlaceRange() {
			return RANGE_PLACE_Y_MINI;
		}
	}

	// BlockItemUseContext uses a nullable player field without checking it -.-
	private static class RannuncarpusPlaceContext extends BlockPlaceContext {
		private final Direction[] lookDirs;
		private final float placementYaw;

		public RannuncarpusPlaceContext(Level world, ItemStack stack, BlockHitResult rtr, BlockPos flowerPos) {
			super(world, null, InteractionHand.MAIN_HAND, stack, rtr);
			int dx = rtr.getBlockPos().getX() - flowerPos.getX();
			int dy = rtr.getBlockPos().getY() - flowerPos.getY();
			int dz = rtr.getBlockPos().getZ() - flowerPos.getZ();

			Direction xClosest = dx >= 0 ? Direction.EAST : Direction.WEST;
			Direction yClosest = dy >= 0 ? Direction.UP : Direction.DOWN;
			Direction zClosest = dz >= 0 ? Direction.SOUTH : Direction.NORTH;

			List<Direction> directions = sortThree(xClosest, yClosest, zClosest, Math.abs(dx), Math.abs(dy), Math.abs(dz));

			Direction first = directions.get(0);
			Direction second = directions.get(1);
			Direction third = directions.get(2);

			lookDirs = new Direction[] {
					first,
					second,
					third,
					third.getOpposite(),
					second.getOpposite(),
					first.getOpposite()
			};

			placementYaw = (float) (-Mth.atan2(dx, dz) * 180 / Math.PI);
		}

		/**
		 * Arrange a, b and c such that their corresponding ints (a -> aInt) are in descending order.
		 */
		private static <T> List<T> sortThree(T a, T b, T c, int aInt, int bInt, int cInt) {
			if (aInt >= bInt) {
				if (bInt >= cInt) {
					return ImmutableList.of(a, b, c);
				} else {
					return cInt >= aInt ? ImmutableList.of(c, a, b) : ImmutableList.of(a, c, b);
				}
			} else if (bInt >= cInt) {
				return cInt >= aInt ? ImmutableList.of(b, c, a) : ImmutableList.of(b, a, c);
			} else {
				return ImmutableList.of(c, b, a);
			}
		}

		@Nonnull
		@Override
		public Direction getNearestLookingDirection() {
			return getNearestLookingDirections()[0];
		}

		@Nonnull
		@Override
		public Direction[] getNearestLookingDirections() {
			return lookDirs;
		}

		@Nonnull
		@Override
		public Direction getHorizontalDirection() {
			return getNearestLookingDirection().getAxis().isHorizontal() ? getNearestLookingDirection() : getNearestLookingDirections()[1];
		}

		@Override
		public float getRotation() {
			return placementYaw;
		}
	}

}
