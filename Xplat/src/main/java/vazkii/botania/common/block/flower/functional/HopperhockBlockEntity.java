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
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.AABB;

import org.jetbrains.annotations.Nullable;

import vazkii.botania.api.block.Wandable;
import vazkii.botania.api.block_entity.FunctionalFlowerBlockEntity;
import vazkii.botania.api.block_entity.RadiusDescriptor;
import vazkii.botania.common.block.block_entity.BotaniaBlockEntities;
import vazkii.botania.common.helper.*;
import vazkii.botania.common.internal_caps.ItemFlagsComponent;
import vazkii.botania.xplat.XplatAbstractions;

import java.util.List;
import java.util.function.Predicate;

public class HopperhockBlockEntity extends FunctionalFlowerBlockEntity implements Wandable {
	private static final String TAG_FILTER_TYPE = "filterType";
	private static final int RANGE_MANA = 10;
	private static final int RANGE = 6;

	private static final int RANGE_MANA_MINI = 2;
	private static final int RANGE_MINI = 1;

	private int filterType = 0;

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

		boolean pulledAny = false;
		int range = getRange();

		BlockPos inPos = getBlockPos();
		BlockPos outPos = getEffectivePos();

		Predicate<ItemEntity> shouldPickup = item -> {
			if (XplatAbstractions.INSTANCE.preventsRemoteMovement(item)) {
				return false;
			}

			final ItemFlagsComponent flags = XplatAbstractions.INSTANCE.itemFlagsComponent(item);

			// Flat 5 tick delay for newly infused items
			if (flags.spawnedByInWorldRecipe()) {
				return flags.timeCounter >= 5 + getModulatedDelay();
			}
			return DelayHelper.canInteractWith(this, item);
		};
		List<ItemEntity> items = getLevel().getEntitiesOfClass(ItemEntity.class, new AABB(inPos).inflate(range), shouldPickup);

		for (ItemEntity item : items) {
			ItemStack stack = item.getItem();
			boolean priorityInv = false;
			int amountToPutIn = 0;
			Direction direction = null;

			for (Direction dir : Direction.values()) {
				BlockPos inventoryPos = outPos.relative(dir);
				Direction sideOfInventory = dir.getOpposite();

				if (XplatAbstractions.INSTANCE.hasInventory(level, inventoryPos, sideOfInventory)) {
					List<ItemStack> filter = FilterHelper.getFiltersOnBlock(getLevel(), inventoryPos, true);
					boolean canAccept = canAcceptItem(stack, filter, filterType);

					ItemStack simulate = XplatAbstractions.INSTANCE.insertToInventory(level, inventoryPos, sideOfInventory, stack, true);
					int inserted = stack.getCount() - simulate.getCount();

					canAccept = canAccept && inserted > 0;

					if (canAccept) {
						boolean priority = !filter.isEmpty();

						if (!priorityInv || priority) {
							priorityInv = priority;
							amountToPutIn = inserted;
							direction = dir;
						}
					}
				}
			}

			if (direction != null && item.isAlive()) {
				SpectranthemumBlockEntity.spawnExplosionParticles(item, 3);
				InventoryHelper.checkEmpty(
						XplatAbstractions.INSTANCE.insertToInventory(level, outPos.relative(direction),
								direction.getOpposite(), stack.split(amountToPutIn), false)
				);

				EntityHelper.syncItem(item);
				pulledAny = true;
			}
		}

		if (pulledAny && getMana() > 0) {
			addMana(-1);
		}
	}

	public static boolean canAcceptItem(ItemStack stack, List<ItemStack> filter, int filterType) {
		if (stack.isEmpty()) {
			return false;
		}

		if (filter.isEmpty()) {
			return true;
		}

		switch (filterType) {
			case 0: { // Accept items in frames only
				boolean anyFilter = false;
				for (ItemStack filterEntry : filter) {
					if (filterEntry == null || filterEntry.isEmpty()) {
						continue;
					}
					anyFilter = true;

					if (DataComponentHelper.matchTagAndManaFullness(stack, filterEntry)) {
						return true;
					}
				}

				return !anyFilter;
			}
			case 1:
				return !canAcceptItem(stack, filter, 0); // Accept items not in frames only
			default:
				return true; // Accept all items
		}
	}

	@Override
	public boolean onUsedByWand(@Nullable Player player, ItemStack wand, Direction side) {
		if (player == null || player.isShiftKeyDown()) {
			filterType = filterType == 2 ? 0 : filterType + 1;
			sync();

			return true;
		}
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

	@Override
	public void writeToPacketNBT(CompoundTag cmp, HolderLookup.Provider registries) {
		super.writeToPacketNBT(cmp, registries);

		cmp.putInt(TAG_FILTER_TYPE, filterType);
	}

	@Override
	public void readFromPacketNBT(CompoundTag cmp, HolderLookup.Provider registries) {
		super.readFromPacketNBT(cmp, registries);

		filterType = cmp.getInt(TAG_FILTER_TYPE);
	}

	public static class WandHud extends BindableFlowerWandHud<HopperhockBlockEntity> {
		public WandHud(HopperhockBlockEntity flower) {
			super(flower);
		}

		@Override
		public void renderHUD(GuiGraphics gui, Window window, Font font, float partialTick) {
			String filter = I18n.get("botaniamisc.filter" + flower.filterType);
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
