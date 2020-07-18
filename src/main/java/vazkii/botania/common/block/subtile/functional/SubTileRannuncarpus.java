/*
 * This class is distributed as part of the Botania Mod.
 * Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 *
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 */
package vazkii.botania.common.block.subtile.functional;

import com.mojang.blaze3d.matrix.MatrixStack;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.item.ItemEntity;
import net.minecraft.item.BlockItem;
import net.minecraft.item.BlockItemUseContext;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntityType;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Direction;
import net.minecraft.util.Hand;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.world.World;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

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

	public SubTileRannuncarpus(TileEntityType<?> type) {
		super(type);
	}

	public SubTileRannuncarpus() {
		this(ModSubtiles.RANNUNCARPUS);
	}

	@Override
	public void tickFlower() {
		super.tickFlower();

		if (getWorld().isRemote || redstoneSignal > 0) {
			return;
		}

		if (ticksExisted % 10 == 0) {
			List<ItemEntity> items = getWorld().getEntitiesWithinAABB(ItemEntity.class, new AxisAlignedBB(getEffectivePos().add(-RANGE, -RANGE_Y, -RANGE), getEffectivePos().add(RANGE + 1, RANGE_Y + 1, RANGE + 1)));
			List<BlockPos> validPositions = getCandidatePositions();
			int slowdown = getSlowdownFactor();

			for (ItemEntity item : items) {
				int age = ((AccessorItemEntity) item).getAge();
				if (age < 60 + slowdown || !item.isAlive() || item.getItem().isEmpty()) {
					continue;
				}

				ItemStack stack = item.getItem();
				Item stackItem = stack.getItem();
				if (stackItem instanceof BlockItem || stackItem instanceof IFlowerPlaceable) {
					if (!validPositions.isEmpty()) {
						BlockPos coords = validPositions.get(getWorld().rand.nextInt(validPositions.size()));
						BlockRayTraceResult ray = new BlockRayTraceResult(Vector3d.ZERO, Direction.UP, coords, false);
						BlockItemUseContext ctx = new RannuncarpusPlaceContext(getWorld(), stack, ray);

						boolean success = false;
						if (stackItem instanceof IFlowerPlaceable) {
							success = ((IFlowerPlaceable) stackItem).tryPlace(this, ctx);
						}
						if (stackItem instanceof BlockItem) {
							success = ((BlockItem) stackItem).tryPlace(ctx) == ActionResultType.SUCCESS;
						}

						if (success) {
							if (ConfigHandler.COMMON.blockBreakParticles.get()) {
								BlockState state = getWorld().getBlockState(ctx.getPos());
								getWorld().playEvent(2001, coords, Block.getStateId(state));
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

		for (BlockPos pos : BlockPos.getAllInBoxMutable(center.add(-rangePlace, -rangePlaceY, -rangePlace),
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

	@OnlyIn(Dist.CLIENT)
	@Override
	public void renderHUD(MatrixStack ms, Minecraft mc) {
		super.renderHUD(ms, mc);

		BlockState filter = getUnderlyingBlock();
		ItemStack recieverStack = new ItemStack(filter.getBlock());
		int color = getColor();

		if (!recieverStack.isEmpty()) {
			ITextComponent stackName = recieverStack.getDisplayName();
			int width = 16 + mc.fontRenderer.func_238414_a_(stackName) / 2;
			int x = mc.getMainWindow().getScaledWidth() / 2 - width;
			int y = mc.getMainWindow().getScaledHeight() / 2 + 30;

			mc.fontRenderer.func_238407_a_(ms, stackName, x + 20, y + 5, color);
			mc.getItemRenderer().renderItemAndEffectIntoGUI(recieverStack, x, y);
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
	private static class RannuncarpusPlaceContext extends BlockItemUseContext {
		private final Direction[] lookDirs;

		public RannuncarpusPlaceContext(World world, ItemStack stack, BlockRayTraceResult rtr) {
			super(world, null, Hand.MAIN_HAND, stack, rtr);
			List<Direction> tmp = Arrays.asList(Direction.values());
			Collections.shuffle(tmp);
			lookDirs = tmp.toArray(new Direction[6]);
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
	}

}
