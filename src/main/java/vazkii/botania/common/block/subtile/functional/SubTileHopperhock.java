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
import com.mojang.blaze3d.systems.RenderSystem;

import net.minecraft.block.BlockState;
import net.minecraft.block.ChestBlock;
import net.minecraft.client.Minecraft;
import net.minecraft.client.resources.I18n;
import net.minecraft.entity.item.ItemEntity;
import net.minecraft.entity.item.ItemFrameEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.state.properties.ChestType;
import net.minecraft.tileentity.TileEntityType;
import net.minecraft.util.Direction;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextFormatting;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.ItemHandlerHelper;

import vazkii.botania.api.corporea.InvWithLocation;
import vazkii.botania.api.mana.IManaItem;
import vazkii.botania.api.subtile.RadiusDescriptor;
import vazkii.botania.api.subtile.TileEntityFunctionalFlower;
import vazkii.botania.common.block.ModSubtiles;
import vazkii.botania.common.core.helper.InventoryHelper;
import vazkii.botania.mixin.AccessorItemEntity;

import java.util.ArrayList;
import java.util.List;

public class SubTileHopperhock extends TileEntityFunctionalFlower {
	private static final String TAG_FILTER_TYPE = "filterType";
	private static final int RANGE_MANA = 10;
	private static final int RANGE = 6;

	private static final int RANGE_MANA_MINI = 2;
	private static final int RANGE_MINI = 1;

	private int filterType = 0;

	public SubTileHopperhock(TileEntityType<?> type) {
		super(type);
	}

	public SubTileHopperhock() {
		this(ModSubtiles.HOPPERHOCK);
	}

	@Override
	public void tickFlower() {
		super.tickFlower();

		if (getWorld().isRemote || redstoneSignal > 0) {
			return;
		}

		boolean pulledAny = false;
		int range = getRange();

		BlockPos pos = getEffectivePos();

		List<ItemEntity> items = getWorld().getEntitiesWithinAABB(ItemEntity.class, new AxisAlignedBB(pos.add(-range, -range, -range), pos.add(range + 1, range + 1, range + 1)));
		int slowdown = getSlowdownFactor();

		for (ItemEntity item : items) {
			int age = ((AccessorItemEntity) item).getAge();
			if (age < 60 + slowdown || age >= 105 && age < 110 || !item.isAlive() || item.getItem().isEmpty()) {
				continue;
			}

			ItemStack stack = item.getItem();
			IItemHandler invToPutItemIn = null;
			boolean priorityInv = false;
			int amountToPutIn = 0;

			for (Direction dir : Direction.values()) {
				BlockPos pos_ = pos.offset(dir);

				InvWithLocation inv = InventoryHelper.getInventoryWithLocation(getWorld(), pos_, dir.getOpposite());
				if (inv != null) {
					List<ItemStack> filter = getFilterForInventory(pos_, true);
					boolean canAccept = canAcceptItem(stack, filter, filterType);

					ItemStack simulate = ItemHandlerHelper.insertItem(inv.getHandler(), stack, true);
					int availablePut = stack.getCount() - simulate.getCount();

					canAccept &= availablePut > 0;

					if (canAccept) {
						boolean priority = !filter.isEmpty();

						setInv: {
							if (priorityInv && !priority) {
								break setInv;
							}

							invToPutItemIn = inv.getHandler();
							priorityInv = priority;
							amountToPutIn = availablePut;
						}
					}
				}
			}

			if (invToPutItemIn != null && item.isAlive()) {
				SubTileSpectranthemum.spawnExplosionParticles(item, 3);
				ItemHandlerHelper.insertItem(invToPutItemIn, stack.split(amountToPutIn), false);
				item.setItem(stack); // Just in case someone subclasses EntityItem and changes something important.
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
				if (filterEntry == null) {
					continue;
				}
				anyFilter = true;

				boolean itemEqual = stack.getItem() == filterEntry.getItem();
				boolean nbtEqual = ItemStack.areItemStackTagsEqual(filterEntry, stack);

				if (itemEqual && nbtEqual) {
					return true;
				}

				if (stack.getItem() instanceof IManaItem && itemEqual) {
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

	public List<ItemStack> getFilterForInventory(BlockPos pos, boolean recursiveForDoubleChests) {
		List<ItemStack> filter = new ArrayList<>();

		if (recursiveForDoubleChests) {
			BlockState chest = getWorld().getBlockState(pos);

			if (chest.func_235901_b_(ChestBlock.TYPE)) {
				ChestType type = chest.get(ChestBlock.TYPE);
				if (type != ChestType.SINGLE) {
					BlockPos other = pos.offset(ChestBlock.getDirectionToAttached(chest));
					if (getWorld().getBlockState(other).getBlock() == chest.getBlock()) {
						filter.addAll(getFilterForInventory(other, false));
					}
				}
			}
		}

		for (Direction dir : Direction.values()) {
			AxisAlignedBB aabb = new AxisAlignedBB(pos.offset(dir));
			List<ItemFrameEntity> frames = getWorld().getEntitiesWithinAABB(ItemFrameEntity.class, aabb);
			for (ItemFrameEntity frame : frames) {
				if (frame.getHorizontalFacing() == dir) {
					filter.add(frame.getDisplayedItem());
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
	public boolean onWanded(PlayerEntity player, ItemStack wand) {
		if (player == null || player.isSneaking()) {
			filterType = filterType == 2 ? 0 : filterType + 1;
			sync();

			return true;
		} else {
			return super.onWanded(player, wand);
		}
	}

	@Override
	public RadiusDescriptor getRadius() {
		return new RadiusDescriptor.Square(getEffectivePos(), getRange());
	}

	public int getRange() {
		return getMana() > 0 ? RANGE_MANA : RANGE;
	}

	@Override
	public void writeToPacketNBT(CompoundNBT cmp) {
		super.writeToPacketNBT(cmp);

		cmp.putInt(TAG_FILTER_TYPE, filterType);
	}

	@Override
	public void readFromPacketNBT(CompoundNBT cmp) {
		super.readFromPacketNBT(cmp);

		filterType = cmp.getInt(TAG_FILTER_TYPE);
	}

	@OnlyIn(Dist.CLIENT)
	@Override
	public void renderHUD(MatrixStack ms, Minecraft mc) {
		super.renderHUD(ms, mc);

		String filter = I18n.format("botaniamisc.filter" + filterType);
		int x = mc.getMainWindow().getScaledWidth() / 2 - mc.fontRenderer.getStringWidth(filter) / 2;
		int y = mc.getMainWindow().getScaledHeight() / 2 + 30;

		mc.fontRenderer.drawStringWithShadow(ms, filter, x, y, TextFormatting.GRAY.getColor());
		RenderSystem.disableLighting();
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
		public Mini() {
			super(ModSubtiles.HOPPERHOCK_CHIBI);
		}

		@Override
		public int getRange() {
			return getMana() > 0 ? RANGE_MANA_MINI : RANGE_MINI;
		}
	}
}
