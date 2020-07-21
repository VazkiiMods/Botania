/*
 * This class is distributed as part of the Botania Mod.
 * Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 *
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 */
package vazkii.botania.common.block.subtile.functional;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.ItemEntity;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemPlacementContext;
import net.minecraft.item.ItemStack;
import net.minecraft.text.Text;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;

import vazkii.botania.api.item.IFlowerPlaceable;
import vazkii.botania.api.subtile.RadiusDescriptor;
import vazkii.botania.api.subtile.TileEntityFunctionalFlower;
import vazkii.botania.common.block.ModSubtiles;
import vazkii.botania.common.core.handler.ConfigHandler;
import vazkii.botania.mixin.AccessorItemEntity;

import javax.annotation.Nonnull;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class SubTileRannuncarpus extends TileEntityFunctionalFlower {
	private static final int RANGE = 2;
	private static final int RANGE_Y = 3;
	private static final int RANGE_PLACE_MANA = 8;
	private static final int RANGE_PLACE = 6;
	private static final int RANGE_PLACE_Y = 6;

	private static final int RANGE_PLACE_MANA_MINI = 3;
	private static final int RANGE_PLACE_MINI = 2;
	private static final int RANGE_PLACE_Y_MINI = 2;

	public SubTileRannuncarpus(BlockEntityType<?> type) {
		super(type);
	}

	public SubTileRannuncarpus() {
		this(ModSubtiles.RANNUNCARPUS);
	}

	@Override
	public void tickFlower() {
		super.tickFlower();

		if (getWorld().isClient || redstoneSignal > 0) {
			return;
		}

		if (ticksExisted % 10 == 0) {
			List<ItemEntity> items = getWorld().getNonSpectatingEntities(ItemEntity.class, new Box(getEffectivePos().add(-RANGE, -RANGE_Y, -RANGE), getEffectivePos().add(RANGE + 1, RANGE_Y + 1, RANGE + 1)));
			List<BlockPos> validPositions = getCandidatePositions();
			int slowdown = getSlowdownFactor();

			for (ItemEntity item : items) {
				int age = ((AccessorItemEntity) item).getAge();
				if (age < 60 + slowdown || !item.isAlive() || item.getStack().isEmpty()) {
					continue;
				}

				ItemStack stack = item.getStack();
				Item stackItem = stack.getItem();
				if (stackItem instanceof BlockItem || stackItem instanceof IFlowerPlaceable) {
					if (!validPositions.isEmpty()) {
						BlockPos coords = validPositions.get(getWorld().random.nextInt(validPositions.size()));
						BlockHitResult ray = new BlockHitResult(Vec3d.ZERO, Direction.UP, coords, false);
						ItemPlacementContext ctx = new RannuncarpusPlaceContext(getWorld(), stack, ray);

						boolean success = false;
						if (stackItem instanceof IFlowerPlaceable) {
							success = ((IFlowerPlaceable) stackItem).tryPlace(this, ctx);
						}
						if (stackItem instanceof BlockItem) {
							success = ((BlockItem) stackItem).place(ctx) == ActionResult.SUCCESS;
						}

						if (success) {
							if (ConfigHandler.COMMON.blockBreakParticles.get()) {
								BlockState state = getWorld().getBlockState(ctx.getBlockPos());
								getWorld().syncWorldEvent(2001, coords, Block.getRawIdFromState(state));
							}
							validPositions.remove(coords);
							if (getMana() > 1) {
								addMana(-1);
							}
							return;
						}
					}
				}
			}
		}
	}

	public BlockState getUnderlyingBlock() {
		return getWorld().getBlockState(getEffectivePos().down(isFloating() ? 1 : 2));
	}

	private List<BlockPos> getCandidatePositions() {
		int rangePlace = getRange();
		int rangePlaceY = getRangeY();
		BlockPos center = getEffectivePos();
		BlockState filter = getUnderlyingBlock();
		List<BlockPos> ret = new ArrayList<>();

		for (BlockPos pos : BlockPos.iterate(center.add(-rangePlace, -rangePlaceY, -rangePlace),
				center.add(rangePlace, rangePlaceY, rangePlace))) {
			BlockState state = getWorld().getBlockState(pos);
			BlockState up = getWorld().getBlockState(pos.up());
			if (filter == state && (up.isAir(getWorld(), pos.up()) || up.getMaterial().isReplaceable())) {
				ret.add(pos.toImmutable());
			}
		}
		return ret;
	}

	@Override
	public boolean acceptsRedstone() {
		return true;
	}

	@Environment(EnvType.CLIENT)
	@Override
	public void renderHUD(MatrixStack ms, MinecraftClient mc) {
		super.renderHUD(ms, mc);

		BlockState filter = getUnderlyingBlock();
		ItemStack recieverStack = new ItemStack(filter.getBlock());
		int color = getColor();

		if (!recieverStack.isEmpty()) {
			Text stackName = recieverStack.getName();
			int width = 16 + mc.textRenderer.getWidth(stackName) / 2;
			int x = mc.getWindow().getScaledWidth() / 2 - width;
			int y = mc.getWindow().getScaledHeight() / 2 + 30;

			mc.textRenderer.drawWithShadow(ms, stackName, x + 20, y + 5, color);
			mc.getItemRenderer().renderInGuiWithOverrides(recieverStack, x, y);
		}

	}

	@Override
	public RadiusDescriptor getRadius() {
		return new RadiusDescriptor.Square(getEffectivePos(), getRange());
	}

	public int getRange() {
		return getMana() > 0 ? RANGE_PLACE_MANA : RANGE_PLACE;
	}

	public int getRangeY() {
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
		public Mini() {
			super(ModSubtiles.RANNUNCARPUS_CHIBI);
		}

		@Override
		public int getRange() {
			return getMana() > 0 ? RANGE_PLACE_MANA_MINI : RANGE_PLACE_MINI;
		}

		@Override
		public int getRangeY() {
			return RANGE_PLACE_Y_MINI;
		}
	}

	// BlockItemUseContext uses a nullable player field without checking it -.-
	private static class RannuncarpusPlaceContext extends ItemPlacementContext {
		private final Direction[] lookDirs;

		public RannuncarpusPlaceContext(World world, ItemStack stack, BlockHitResult rtr) {
			super(world, null, Hand.MAIN_HAND, stack, rtr);
			List<Direction> tmp = Arrays.asList(Direction.values());
			Collections.shuffle(tmp);
			lookDirs = tmp.toArray(new Direction[6]);
		}

		@Nonnull
		@Override
		public Direction getPlayerLookDirection() {
			return getPlacementDirections()[0];
		}

		@Nonnull
		@Override
		public Direction[] getPlacementDirections() {
			return lookDirs;
		}
	}

}
