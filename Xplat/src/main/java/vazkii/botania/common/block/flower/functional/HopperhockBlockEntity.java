/*
 * This class is distributed as part of the Botania Mod.
 * Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 *
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 */
package vazkii.botania.common.block.flower.functional;

import com.mojang.blaze3d.platform.Window;

import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.resources.language.I18n;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.AABB;

import org.jetbrains.annotations.Nullable;

import vazkii.botania.api.block.Wandable;
import vazkii.botania.api.block_entity.FunctionalFlowerBlockEntity;
import vazkii.botania.api.block_entity.RadiusDescriptor;
import vazkii.botania.api.state.BotaniaStateProperties;
import vazkii.botania.api.state.enums.HopperhockFilterType;
import vazkii.botania.common.block.block_entity.BotaniaBlockEntities;
import vazkii.botania.common.helper.*;
import vazkii.botania.common.internal_caps.ItemFlagsComponent;
import vazkii.botania.xplat.XplatAbstractions;

import java.util.EnumMap;
import java.util.EnumSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.Predicate;

public class HopperhockBlockEntity extends FunctionalFlowerBlockEntity implements Wandable {
	private static final int RANGE_MANA = 10;
	private static final int RANGE = 6;

	private static final int RANGE_MANA_MINI = 2;
	private static final int RANGE_MINI = 1;

	protected HopperhockBlockEntity(BlockEntityType<?> type, BlockPos pos, BlockState state) {
		super(type, pos, state);
	}

	public HopperhockBlockEntity(BlockPos pos, BlockState state) {
		this(BotaniaBlockEntities.HOPPERHOCK, pos, state);
	}

	@Override
	public void tickFlower() {
		super.tickFlower();

		if (getLevel().isClientSide || isPowered()) {
			return;
		}

		int range = getRange();

		BlockPos inPos = getBlockPos();
		BlockPos outPos = getEffectivePos();

		List<ItemEntity> items = getItemEntities(inPos, range);
		if (items.isEmpty()) {
			return;
		}

		Map<Direction, List<ItemStack>> directionFilters = new EnumMap<>(Direction.class);
		Set<Direction> unfilteredDirections = EnumSet.noneOf(Direction.class);
		findFilterDirections(outPos, unfilteredDirections, directionFilters);
		if (directionFilters.isEmpty() && unfilteredDirections.isEmpty()) {
			return;
		}

		if (moveItems(items, outPos, directionFilters, unfilteredDirections) && getMana() > 0) {
			addMana(-1);
		}
	}

	public HopperhockFilterType getFilterType() {
		return getBlockState().getOptionalValue(BotaniaStateProperties.HOPPERHOCK_FILTER)
				.orElse(HopperhockFilterType.ACCEPT_IN_FRAME);
	}

	private List<ItemEntity> getItemEntities(BlockPos inPos, int range) {
		Predicate<ItemEntity> shouldPickup = item -> {
			if (XplatAbstractions.INSTANCE.preventsRemoteMovement(item)) {
				return false;
			}

			final ItemFlagsComponent flags = XplatAbstractions.INSTANCE.itemFlagsComponent(item);

			// Flat 5 tick delay for newly infused items
			if (flags.spawnedByInWorldRecipe() || overgrowth) {
				return flags.timeCounter >= 5 + getModulatedDelay();
			}
			return DelayHelper.canInteractWith(this, item);
		};
		return getLevel().getEntitiesOfClass(ItemEntity.class, new AABB(inPos).inflate(range), shouldPickup);
	}

	private void findFilterDirections(BlockPos outPos, Set<Direction> unfilteredDirections,
			Map<Direction, List<ItemStack>> directionFilters) {
		for (Direction dir : Direction.values()) {
			BlockPos inventoryPos = outPos.relative(dir);
			Direction sideOfInventory = dir.getOpposite();

			if (XplatAbstractions.INSTANCE.hasInventory(level, inventoryPos, sideOfInventory)) {
				List<ItemStack> filter = getFilterType() != HopperhockFilterType.ACCEPT_ALL
						// don't bother looking for filters if the flower ignores them anyway
						? FilterHelper.getFiltersOnBlock(level, inventoryPos, true)
						: List.of();
				if (filter.isEmpty()) {
					unfilteredDirections.add(dir);
				} else {
					directionFilters.put(dir, filter);
				}
			}
		}
	}

	private boolean moveItems(List<ItemEntity> items, BlockPos outPos, Map<Direction, List<ItemStack>> directionFilters,
			Set<Direction> unfilteredDirections) {
		boolean pulledAny = false;
		for (ItemEntity item : items) {
			ItemStack stack = item.getItem();
			ItemStack originalStack = stack;
			int originalCount = stack.getCount();

			for (Map.Entry<Direction, List<ItemStack>> entry : directionFilters.entrySet()) {
				stack = insertStack(outPos, entry.getKey(), stack, entry.getValue());
			}

			for (Direction dir : unfilteredDirections) {
				stack = insertStack(outPos, dir, stack, List.of());
			}

			if (stack.getCount() < originalCount && item.isAlive()) {
				SpectranthemumBlockEntity.spawnExplosionParticles(item, 3);
				item.setItem(stack);
				if (stack == originalStack) {
					EntityHelper.syncItem(item);
				}
				pulledAny = true;
			}
		}
		return pulledAny;
	}

	private ItemStack insertStack(BlockPos outPos, Direction dir, ItemStack stack, List<ItemStack> filter) {
		BlockPos inventoryPos = outPos.relative(dir);
		Direction sideOfInventory = dir.getOpposite();
		return XplatAbstractions.INSTANCE.hasInventory(level, inventoryPos, sideOfInventory)
				&& canAcceptItem(stack, filter, getFilterType())
						? XplatAbstractions.INSTANCE.insertToInventory(level, inventoryPos, sideOfInventory, stack, false)
						: stack;
	}

	public static boolean canAcceptItem(ItemStack stack, List<ItemStack> filter, HopperhockFilterType filterType) {
		if (stack.isEmpty()) {
			return false;
		}

		if (filter.isEmpty() || filterType == HopperhockFilterType.ACCEPT_ALL) {
			return true;
		}

		for (ItemStack filterEntry : filter) {
			if (filterEntry.isEmpty()) {
				continue;
			}
			if (DataComponentHelper.matchTagAndManaFullness(stack, filterEntry)) {
				return filterType == HopperhockFilterType.ACCEPT_IN_FRAME;
			}
		}

		return filterType == HopperhockFilterType.ACCEPT_NOT_IN_FRAME;
	}

	@Override
	public boolean onUsedByWand(@Nullable Player player, ItemStack wand, Direction side) {
		if (player == null || player.isShiftKeyDown()) {
			level.setBlock(getBlockPos(), getBlockState().cycle(BotaniaStateProperties.HOPPERHOCK_FILTER), Block.UPDATE_CLIENTS);
			return true;
		}
		return false;
	}

	@Override
	public boolean isOvergrowthAffected() {
		return false;
	}

	@Override
	public RadiusDescriptor getRadius() {
		return RadiusDescriptor.Rectangle.square(getEffectivePos(), getRange());
	}

	@Override
	public RadiusDescriptor getSecondaryRadius() {
		return RadiusDescriptor.Rectangle.square(getBlockPos(), 1);
	}

	public int getRange() {
		return getMana() > 0 ? RANGE_MANA : RANGE;
	}

	public static class WandHud extends BindableFlowerWandHud<HopperhockBlockEntity> {
		public WandHud(HopperhockBlockEntity flower) {
			super(flower);
		}

		@Override
		public void renderHUD(GuiGraphics gui, Window window, Font font, float partialTick) {
			String filter = I18n.get("botaniamisc.filter." + flower.getFilterType().getSerializedName());
			int filterWidth = font.width(filter);
			int filterTextStart = (window.getGuiScaledWidth() - filterWidth) / 2;
			int halfMinWidth = (filterWidth + 4) / 2;
			int centerY = window.getGuiScaledHeight() / 2;

			super.renderHUD(gui, window, font, halfMinWidth, halfMinWidth, 40);
			gui.drawString(font, filter, filterTextStart, centerY + 30, flower.getColor());
		}
	}

	@Override
	public int getMaxMana() {
		return 20;
	}

	@Override
	public int getColor() {
		return 0x7F7F7F;
	}

	public static class Mini extends HopperhockBlockEntity {
		public Mini(BlockPos pos, BlockState state) {
			super(BotaniaBlockEntities.HOPPERHOCK_CHIBI, pos, state);
		}

		@Override
		public int getRange() {
			return getMana() > 0 ? RANGE_MANA_MINI : RANGE_MINI;
		}
	}
}
