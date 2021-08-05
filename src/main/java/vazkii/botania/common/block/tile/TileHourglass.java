/*
 * This class is distributed as part of the Botania Mod.
 * Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 *
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 */
package vazkii.botania.common.block.tile;

import com.mojang.blaze3d.vertex.PoseStack;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.Minecraft;
import net.minecraft.client.resources.language.I18n;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.util.StringUtil;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.entity.TickableBlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;

import vazkii.botania.api.internal.VanillaPacketDispatcher;
import vazkii.botania.api.item.IHourglassTrigger;
import vazkii.botania.common.item.ModItems;

public class TileHourglass extends TileExposedSimpleInventory implements TickableBlockEntity {
	private static final String TAG_TIME = "time";
	private static final String TAG_TIME_FRACTION = "timeFraction";
	private static final String TAG_FLIP = "flip";
	private static final String TAG_FLIP_TICKS = "flipTicks";
	private static final String TAG_LOCK = "lock";
	private static final String TAG_MOVE = "move";

	private int time = 0;
	public float timeFraction = 0F;
	public float lastFraction = 0;
	public boolean flip = false;
	public int flipTicks = 0;
	public boolean lock = false;
	public boolean move = true;

	public TileHourglass(BlockPos pos, BlockState state) {
		super(ModTiles.HOURGLASS, pos, state);
	}

	private boolean isDust() {
		ItemStack stack = getItemHandler().getItem(0);
		return !stack.isEmpty() && stack.getItem() == ModItems.manaPowder;
	}

	@Override
	public void tick() {
		int totalTime = getTotalTime();
		boolean dust = isDust();

		if (totalTime > 0 || dust) {
			if (move && !dust) {
				time++;
			}

			if (time >= totalTime) {
				time = 0;
				flip = !flip;
				flipTicks = 4;
				if (!level.isClientSide) {
					level.setBlock(getBlockPos(), getBlockState().setValue(BlockStateProperties.POWERED, true), 1);
					level.getBlockTicks().scheduleTick(worldPosition, getBlockState().getBlock(), 4);
				}

				for (Direction facing : Direction.values()) {
					BlockPos pos = getBlockPos().relative(facing);
					BlockState state = level.getBlockState(pos);
					if (state.getBlock() instanceof IHourglassTrigger) {
						((IHourglassTrigger) state.getBlock()).onTriggeredByHourglass(level, pos, this);
					}
				}
			}

			lastFraction = timeFraction;
			timeFraction = (float) time / (float) totalTime;
		} else {
			time = 0;
			lastFraction = 0F;
			timeFraction = 0F;
		}

		if (flipTicks > 0) {
			flipTicks--;
		}
	}

	public void onManaCollide() {
		if (!level.isClientSide) {
			if (isDust()) {
				time++;
			} else {
				move = !move;
			}
			VanillaPacketDispatcher.dispatchTEToNearbyPlayers(this);
		}
	}

	public int getTotalTime() {
		ItemStack stack = getItemHandler().getItem(0);
		if (stack.isEmpty()) {
			return 0;
		}

		return getStackItemTime(stack) * stack.getCount();
	}

	public static int getStackItemTime(ItemStack stack) {
		if (stack.isEmpty()) {
			return 0;
		}
		if (stack.getItem() == Blocks.SAND.asItem()) {
			return 20;
		}
		if (stack.getItem() == Blocks.RED_SAND.asItem()) {
			return 200;
		}
		if (stack.getItem() == Blocks.SOUL_SAND.asItem()) {
			return 1200;
		}
		if (stack.getItem() == ModItems.manaPowder) {
			return 1;
		}
		return 0;
	}

	public int getColor() {
		ItemStack stack = getItemHandler().getItem(0);
		if (stack.isEmpty()) {
			return 0;
		}
		if (stack.getItem() == Blocks.SAND.asItem()) {
			return 0xFFEC49;
		}
		if (stack.getItem() == Blocks.RED_SAND.asItem()) {
			return 0xE95800;
		}
		if (stack.getItem() == Blocks.SOUL_SAND.asItem()) {
			return 0x5A412f;
		}
		if (stack.getItem() == ModItems.manaPowder) {
			return 0x03abff;
		}

		return 0;
	}

	@Override
	protected SimpleContainer createItemHandler() {
		return new SimpleContainer(1) {
			@Override
			public boolean canPlaceItem(int index, ItemStack stack) {
				return !stack.isEmpty() && (stack.getItem() == Blocks.SAND.asItem()
						|| stack.getItem() == Blocks.RED_SAND.asItem()
						|| stack.getItem() == Blocks.SOUL_SAND.asItem()
						|| stack.getItem() == ModItems.manaPowder);
			}
		};
	}

	@Override
	public void setChanged() {
		super.setChanged();
		if (level != null && !level.isClientSide) {
			time = 0;
			timeFraction = 0F;
			VanillaPacketDispatcher.dispatchTEToNearbyPlayers(TileHourglass.this);
		}
	}

	@Override
	public void writePacketNBT(CompoundTag tag) {
		super.writePacketNBT(tag);
		tag.putInt(TAG_TIME, time);
		tag.putFloat(TAG_TIME_FRACTION, timeFraction);
		tag.putBoolean(TAG_FLIP, flip);
		tag.putInt(TAG_FLIP_TICKS, flipTicks);
		tag.putBoolean(TAG_MOVE, move);
		tag.putBoolean(TAG_LOCK, lock);
	}

	@Override
	public void readPacketNBT(CompoundTag tag) {
		super.readPacketNBT(tag);
		time = tag.getInt(TAG_TIME);
		timeFraction = tag.getFloat(TAG_TIME_FRACTION);
		flip = tag.getBoolean(TAG_FLIP);
		flipTicks = tag.getInt(TAG_FLIP_TICKS);
		move = tag.getBoolean(TAG_MOVE);
		lock = tag.getBoolean(TAG_LOCK);
	}

	@Environment(EnvType.CLIENT)
	public void renderHUD(PoseStack ms) {
		Minecraft mc = Minecraft.getInstance();
		int x = mc.getWindow().getGuiScaledWidth() / 2 + 10;
		int y = mc.getWindow().getGuiScaledHeight() / 2 - 10;

		ItemStack stack = getItemHandler().getItem(0);
		if (!stack.isEmpty()) {
			mc.getItemRenderer().renderGuiItem(stack, x, y);
			mc.getItemRenderer().renderGuiItemDecorations(mc.font, stack, x, y);

			int time = getTotalTime();
			String timeStr = StringUtil.formatTickDuration(time);
			mc.font.drawShadow(ms, timeStr, x + 20, y, getColor());

			String status = "";
			if (lock) {
				status = "locked";
			}
			if (!move) {
				status = status.isEmpty() ? "stopped" : "lockedStopped";
			}
			if (!status.isEmpty()) {
				mc.font.drawShadow(ms, I18n.get("botaniamisc." + status), x + 20, y + 12, getColor());
			}
		}

	}

}
