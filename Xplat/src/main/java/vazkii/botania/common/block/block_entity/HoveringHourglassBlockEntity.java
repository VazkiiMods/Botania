/*
 * This class is distributed as part of the Botania Mod.
 * Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 *
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 */
package vazkii.botania.common.block.block_entity;

import com.mojang.blaze3d.platform.Window;

import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.resources.language.I18n;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.particles.DustParticleOptions;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.util.RandomSource;
import net.minecraft.util.StringUtil;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.gameevent.GameEvent;

import org.jetbrains.annotations.Nullable;

import vazkii.botania.api.block.HourglassTrigger;
import vazkii.botania.api.block.WandHUD;
import vazkii.botania.api.block.Wandable;
import vazkii.botania.api.internal.ManaBurst;
import vazkii.botania.api.item.HourglassMaterial;
import vazkii.botania.api.mana.ManaTrigger;
import vazkii.botania.client.core.helper.RenderHelper;
import vazkii.botania.client.integration.shared.LocaleHelper;
import vazkii.botania.common.block.BotaniaBlocks;
import vazkii.botania.common.block.HoveringHourglassBlock;
import vazkii.botania.common.helper.NbtHelper;

public class HoveringHourglassBlockEntity extends ExposedSimpleInventoryBlockEntity implements ManaTrigger, Wandable {
	private static final String TAG_TIME = "time";

	private static final int EVENT_FLIP = 0;
	private static final int EVENT_COUNT_BURST = 1;
	private static final int PARAM_FORCED_FLIP = 1;
	public static final int FLIP_TICKS = 4;

	// both sides
	private int time = 0;

	// client-side, for flip animation
	public long rotationStartTime;
	public float lastRotation = -1;
	public float rotation;

	public HoveringHourglassBlockEntity(BlockPos pos, BlockState state) {
		super(BotaniaBlockEntities.HOURGLASS, pos, state, true);
	}

	public ItemStack getContents() {
		return getItemHandler().getItem(0);
	}

	public void setContents(ItemStack stack) {
		getItemHandler().setItem(0, stack);
		// content was filled or emptied by hand, reset time
		time = getTotalTime();
	}

	public boolean isDust() {
		ItemStack stack = getItemHandler().getItem(0);
		var material = HourglassMaterial.LOOKUP.find(stack);
		return material != null && material.isCounter();
	}

	public static void commonTick(Level level, BlockPos pos, BlockState state, HoveringHourglassBlockEntity self) {
		if (self.time > 0) {
			self.time--;
			if (!level.isClientSide()) {
				if (self.time == 0) {
					level.blockEvent(pos, state.getBlock(), EVENT_FLIP, 0);
				} else if (self.time % 107 == 0) {
					// synchronize remaining time once in a while, just to be sure
					level.sendBlockUpdated(pos, state, state, Block.UPDATE_CLIENTS);
				}
			}
		}
	}

	@Override
	public boolean triggerEvent(int id, int type) {
		switch (id) {
			case EVENT_FLIP -> {
				int totalTime = getTotalTime();
				time = totalTime;
				if (totalTime == 0 && type != PARAM_FORCED_FLIP) {
					// cancel event if all material was removed
					return false;
				}
				level.setBlockAndUpdate(getBlockPos(), getBlockState()
						.cycle(HoveringHourglassBlock.FLIPPED)
						.setValue(HoveringHourglassBlock.POWERED, true));
				if (!level.isClientSide()) {
					level.scheduleTick(getBlockPos(), getBlockState().getBlock(), FLIP_TICKS);

					for (Direction facing : Direction.values()) {
						BlockPos pos = getBlockPos().relative(facing);
						var trigger = HourglassTrigger.LOOKUP.find(level, pos);
						if (trigger != null) {
							trigger.onTriggeredByHourglass(this);
						}
					}
				} else {
					lastRotation = rotation;
					rotationStartTime = level.getGameTime();
					RandomSource random = level.getRandom();
					BlockPos pos = getBlockPos();
					for (int i = 0; i < 5; i++) {
						double x = pos.getX() + 0.5 + (random.nextDouble() - 0.5) * 0.3;
						double y = pos.getY() + 0.5 + (random.nextDouble() - 0.5) * 0.8;
						double z = pos.getZ() + 0.5 + (random.nextDouble() - 0.5) * 0.3;
						level.addParticle(DustParticleOptions.REDSTONE, x, y, z, 0.0, 0.0, 0.0);
					}
				}
				return true;
			}
			case EVENT_COUNT_BURST -> {
				if (time > 0) {
					time--;
					if (time == 0) {
						if (!level.isClientSide()) {
							level.blockEvent(getBlockPos(), getBlockState().getBlock(), EVENT_FLIP, 0);
						}
						return false;
					} else if (!level.isClientSide && time % 10 == 0) {
						// synchronize count once in a while
						level.sendBlockUpdated(getBlockPos(), getBlockState(), getBlockState(), Block.UPDATE_CLIENTS);
					}
					return true;
				}
			}
		}
		return super.triggerEvent(id, type);
	}

	@Override
	public void onBurstCollision(ManaBurst burst) {
		if (level.isClientSide() || burst.isFake()) {
			return;
		}
		if (isDust()) {
			level.blockEvent(getBlockPos(), getBlockState().getBlock(), EVENT_COUNT_BURST, 0);
		} else {
			level.setBlockAndUpdate(getBlockPos(), getBlockState().cycle(HoveringHourglassBlock.ENABLED));
		}
	}

	public void flipEarly() {
		if (!getBlockState().getValue(HoveringHourglassBlock.ENABLED)) {
			// always enable when forced to flip early
			level.setBlockAndUpdate(getBlockPos(), getBlockState().setValue(HoveringHourglassBlock.ENABLED, true));
		}
		level.blockEvent(getBlockPos(), getBlockState().getBlock(), EVENT_FLIP, PARAM_FORCED_FLIP);
	}

	public int getTime() {
		return time;
	}

	public int getTotalTime() {
		ItemStack stack = getItemHandler().getItem(0);
		if (stack.isEmpty()) {
			return 0;
		}

		return getStackItemTime(stack) * stack.getCount();
	}

	public static int getStackItemTime(ItemStack stack) {
		var material = HourglassMaterial.LOOKUP.find(stack);
		return material != null ? material.numberOfTicks() : 0;
	}

	public int getColor() {
		ItemStack stack = getItemHandler().getItem(0);
		var material = HourglassMaterial.LOOKUP.find(stack);
		return material != null ? material.color() : -1;
	}

	@Override
	protected SimpleContainer createItemHandler() {
		return new SimpleContainer(1) {
			@Override
			public boolean canPlaceItem(int index, ItemStack stack) {
				return !stack.isEmpty() && HourglassMaterial.LOOKUP.find(stack) != null;
			}
		};
	}

	@Override
	public void setChanged() {
		if (level == null) {
			return;
		}
		int totalTime = getTotalTime();
		boolean shouldTick = getBlockState().getValue(HoveringHourglassBlock.ENABLED) && totalTime > 0 && !isDust();

		if (getBlockState().getValue(HoveringHourglassBlock.ACTIVE) != shouldTick) {
			if (time > totalTime) {
				// looks like some or all material has been removed, adjust remaining time
				time = totalTime;
			}
			level.setBlockAndUpdate(getBlockPos(), getBlockState().setValue(HoveringHourglassBlock.ACTIVE, shouldTick));
		} else {
			if (time > totalTime) {
				time = totalTime;
			}
			// skip comparator update, but synchronize with clients
			level.blockEntityChanged(getBlockPos());
			level.sendBlockUpdated(getBlockPos(), getBlockState(), getBlockState(), Block.UPDATE_CLIENTS);
		}
	}

	@Override
	protected void saveAdditional(CompoundTag tag, HolderLookup.Provider registries) {
		super.saveAdditional(tag, registries);
		tag.putInt(TAG_TIME, time);
	}

	@Override
	protected void loadAdditional(CompoundTag tag, HolderLookup.Provider registries) {
		super.loadAdditional(tag, registries);
		time = tag.getInt(TAG_TIME);
	}

	@Override
	public CompoundTag getUpdateTag(HolderLookup.Provider registries) {
		var tag = super.getUpdateTag(registries);
		NbtHelper.putVarInt(tag, TAG_TIME, time);
		return tag;
	}

	public static class WandHud implements WandHUD {
		private final HoveringHourglassBlockEntity hourglass;

		public WandHud(HoveringHourglassBlockEntity hourglass) {
			this.hourglass = hourglass;
		}

		@Override
		public void renderHUD(GuiGraphics gui, Window window, Font font, float partialTick) {
			ItemStack stack = hourglass.getItemHandler().getItem(0);
			if (stack.isEmpty()) {
				stack = BotaniaBlocks.hourglass.asItem().getDefaultInstance();
			}
			int x = window.getGuiScaledWidth() / 2 + 8;
			int y = window.getGuiScaledHeight() / 2 - 10;

			String first, second;
			if (hourglass.isDust()) {
				first = LocaleHelper.getIntegerFormat().format(hourglass.time);
				second = LocaleHelper.getIntegerFormat().format(hourglass.getTotalTime());
			} else {
				float tickrate = hourglass.level == null ? 20 : hourglass.level.tickRateManager().tickrate();
				first = StringUtil.formatTickDuration(hourglass.time, tickrate);
				second = StringUtil.formatTickDuration(hourglass.getTotalTime(), tickrate);
			}
			String timer = String.format("%s / %s", first, second);

			boolean locked = hourglass.getBlockState().getValue(HoveringHourglassBlock.LOCKED);
			boolean enabled = hourglass.getBlockState().getValue(HoveringHourglassBlock.ENABLED);
			String status = locked || !enabled
					? I18n.get("botaniamisc." + (enabled ? "locked" : locked ? "lockedStopped" : "stopped"))
					: "";

			int textWidth = Math.max(font.width(timer), font.width(status));

			RenderHelper.renderHUDBox(gui, x, y, x + textWidth + 24, y + 22);

			gui.renderFakeItem(stack, x + 2, y + 3);
			gui.renderItemDecorations(font, stack, x + 2, y + 3);

			gui.drawString(font, timer, x + 22, y + 2, hourglass.getColor());
			if (!status.isEmpty()) {
				gui.drawString(font, status, x + 22, y + 12, hourglass.getColor());
			}
		}
	}

	@Override
	public boolean onUsedByWand(@Nullable Player player, ItemStack stack, Direction side) {
		if (level.setBlock(getBlockPos(), getBlockState().cycle(HoveringHourglassBlock.LOCKED), Block.UPDATE_CLIENTS)) {
			level.gameEvent(player, GameEvent.BLOCK_CHANGE, getBlockPos());
			return true;
		}
		return false;
	}
}
