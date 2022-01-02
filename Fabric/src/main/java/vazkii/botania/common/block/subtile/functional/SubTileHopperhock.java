/*
 * This class is distributed as part of the Botania Mod.
 * Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 *
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 */
package vazkii.botania.common.block.subtile.functional;

import com.mojang.blaze3d.vertex.PoseStack;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.client.resources.language.I18n;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.Container;
import net.minecraft.world.entity.decoration.ItemFrame;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.ChestBlock;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.entity.HopperBlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.ChestType;
import net.minecraft.world.phys.AABB;

import vazkii.botania.api.block.IWandable;
import vazkii.botania.api.mana.IManaItem;
import vazkii.botania.api.subtile.RadiusDescriptor;
import vazkii.botania.api.subtile.TileEntityFunctionalFlower;
import vazkii.botania.common.block.ModSubtiles;
import vazkii.botania.common.components.EntityComponents;
import vazkii.botania.common.components.ItemFlagsComponent;
import vazkii.botania.common.core.helper.DelayHelper;
import vazkii.botania.common.core.helper.InventoryHelper;

import javax.annotation.Nullable;

import java.util.ArrayList;
import java.util.List;

public class SubTileHopperhock extends TileEntityFunctionalFlower implements IWandable {
	private static final String TAG_FILTER_TYPE = "filterType";
	private static final int RANGE_MANA = 10;
	private static final int RANGE = 6;

	private static final int RANGE_MANA_MINI = 2;
	private static final int RANGE_MINI = 1;

	private int filterType = 0;

	protected SubTileHopperhock(BlockEntityType<?> type, BlockPos pos, BlockState state) {
		super(type, pos, state);
	}

	public SubTileHopperhock(BlockPos pos, BlockState state) {
		this(ModSubtiles.HOPPERHOCK, pos, state);
	}

	@Override
	public void tickFlower() {
		super.tickFlower();

		if (getLevel().isClientSide || redstoneSignal > 0) {
			return;
		}

		boolean pulledAny = false;
		int range = getRange();

		BlockPos pos = getEffectivePos();

		List<ItemEntity> items = getLevel().getEntitiesOfClass(ItemEntity.class, new AABB(pos.offset(-range, -range, -range), pos.offset(range + 1, range + 1, range + 1)));

		for (ItemEntity item : items) {
			if (!DelayHelper.canInteractWith(this, item)) {
				continue;
			}

			// Hopperhocks additionally don't pick up items that have been newly infused (5 ticks),
			// to facilitate multiple infusions
			if (EntityComponents.INTERNAL_ITEM.get(item).getManaInfusionCooldown()
					> ItemFlagsComponent.INITIAL_MANA_INFUSION_COOLDOWN - 5) {
				continue;
			}

			ItemStack stack = item.getItem();
			Container invToPutItemIn = null;
			boolean priorityInv = false;
			int amountToPutIn = 0;
			Direction direction = null;

			for (Direction dir : Direction.values()) {
				BlockPos pos_ = pos.relative(dir);

				Container inv = InventoryHelper.getInventory(getLevel(), pos_, dir.getOpposite());
				if (inv != null) {
					List<ItemStack> filter = getFilterForInventory(pos_, true);
					boolean canAccept = canAcceptItem(stack, filter, filterType);

					ItemStack simulate = InventoryHelper.simulateTransfer(inv, stack, dir.getOpposite());
					int availablePut = stack.getCount() - simulate.getCount();

					canAccept &= availablePut > 0;

					if (canAccept) {
						boolean priority = !filter.isEmpty();

						setInv: {
							if (priorityInv && !priority) {
								break setInv;
							}

							invToPutItemIn = inv;
							priorityInv = priority;
							amountToPutIn = availablePut;
							direction = dir;
						}
					}
				}
			}

			if (invToPutItemIn != null && item.isAlive()) {
				SubTileSpectranthemum.spawnExplosionParticles(item, 3);
				HopperBlockEntity.addItem(null, invToPutItemIn, stack.split(amountToPutIn), direction.getOpposite());
				item.setItem(stack); // Just in case someone subclasses ItemEntity and changes something important.
				pulledAny = true;
			}
		}

		if (pulledAny && getMana() > 0) {
			addMana(-1);
		}
	}

	public boolean canAcceptItem(ItemStack stack, List<ItemStack> filter, int filterType) {
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

					if (matches(stack, filterEntry)) {
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

	public static boolean matches(ItemStack stack, ItemStack filter) {
		Item item = stack.getItem();
		if (item != filter.getItem()) {
			return false;
		}

		if (item instanceof IManaItem manaItem) {
			return getFullness(manaItem, stack) == getFullness(manaItem, filter);
		} else {
			return ItemStack.tagMatches(filter, stack);
		}
	}

	/**
	 * Returns the fullness of the mana item:
	 * 0 if empty, 1 if partially full, 2 if full.
	 */
	public static int getFullness(IManaItem item, ItemStack stack) {
		int mana = item.getMana(stack);
		int fuzz = 10;
		return mana <= fuzz ? 0 : (mana + fuzz < item.getMaxMana(stack) ? 1 : 2);
	}

	public List<ItemStack> getFilterForInventory(BlockPos pos, boolean recursiveForDoubleChests) {
		List<ItemStack> filter = new ArrayList<>();

		if (recursiveForDoubleChests) {
			BlockState chest = getLevel().getBlockState(pos);

			if (chest.hasProperty(ChestBlock.TYPE)) {
				ChestType type = chest.getValue(ChestBlock.TYPE);
				if (type != ChestType.SINGLE) {
					BlockPos other = pos.relative(ChestBlock.getConnectedDirection(chest));
					if (getLevel().getBlockState(other).is(chest.getBlock())) {
						filter.addAll(getFilterForInventory(other, false));
					}
				}
			}
		}

		for (Direction dir : Direction.values()) {
			AABB aabb = new AABB(pos.relative(dir));
			List<ItemFrame> frames = getLevel().getEntitiesOfClass(ItemFrame.class, aabb);
			for (ItemFrame frame : frames) {
				if (frame.getDirection() == dir) {
					filter.add(frame.getItem());
				}
			}
		}

		return filter;
	}

	@Override
	public boolean acceptsRedstone() {
		return true;
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
		return new RadiusDescriptor.Square(getEffectivePos(), getRange());
	}

	public int getRange() {
		return getMana() > 0 ? RANGE_MANA : RANGE;
	}

	@Override
	public void writeToPacketNBT(CompoundTag cmp) {
		super.writeToPacketNBT(cmp);

		cmp.putInt(TAG_FILTER_TYPE, filterType);
	}

	@Override
	public void readFromPacketNBT(CompoundTag cmp) {
		super.readFromPacketNBT(cmp);

		filterType = cmp.getInt(TAG_FILTER_TYPE);
	}

	@Environment(EnvType.CLIENT)
	@Override
	public void renderHUD(PoseStack ms, Minecraft mc) {
		super.renderHUD(ms, mc);

		String filter = I18n.get("botaniamisc.filter" + filterType);
		int x = mc.getWindow().getGuiScaledWidth() / 2 - mc.font.width(filter) / 2;
		int y = mc.getWindow().getGuiScaledHeight() / 2 + 30;

		mc.font.drawShadow(ms, filter, x, y, ChatFormatting.GRAY.getColor());
	}

	@Override
	public int getMaxMana() {
		return 20;
	}

	@Override
	public int getColor() {
		return 0x3F3F3F;
	}

	public static class Mini extends SubTileHopperhock {
		public Mini(BlockPos pos, BlockState state) {
			super(ModSubtiles.HOPPERHOCK_CHIBI, pos, state);
		}

		@Override
		public int getRange() {
			return getMana() > 0 ? RANGE_MANA_MINI : RANGE_MINI;
		}
	}
}
