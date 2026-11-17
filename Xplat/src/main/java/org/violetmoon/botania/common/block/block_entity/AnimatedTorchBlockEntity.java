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
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;

import org.jetbrains.annotations.Nullable;

import vazkii.botania.api.block.HourglassTrigger;
import vazkii.botania.api.block.WandHUD;
import vazkii.botania.api.block.Wandable;
import vazkii.botania.api.internal.ManaBurst;
import vazkii.botania.api.mana.ManaTrigger;
import vazkii.botania.api.state.enums.TorchMode;
import vazkii.botania.client.core.helper.RenderHelper;
import vazkii.botania.common.block.AnimatedTorchBlock;

public class AnimatedTorchBlockEntity extends BlockEntity implements ManaTrigger, Wandable, HourglassTrigger {
	private static final int EVENT_TRIGGER_ROTATE = 0;
	private static final String TAG_ROTATION_TICKS = "rotationTicks";
	public static final int ROTATION_TICKS = 4;

	// server-side
	private int rotationTicks;

	// client-side
	public long rotationStartTime;
	public float lastRotation = -1;
	public float rotation;

	public AnimatedTorchBlockEntity(BlockPos pos, BlockState state) {
		super(BotaniaBlockEntities.ANIMATED_TORCH, pos, state);
	}

	public void handRotate() {
		if (!level.isClientSide()) {
			triggerRotation(TorchMode.ROTATE);
		}
	}

	private void triggerRotation(TorchMode rotate) {
		BlockState state = getBlockState();
		level.blockEvent(getBlockPos(), state.getBlock(), EVENT_TRIGGER_ROTATE,
				rotate.getNewFacing(state.getValue(AnimatedTorchBlock.FACING)).ordinal()
		);
	}

	public void toggle() {
		if (!level.isClientSide()) {
			triggerRotation(getBlockState().getValue(AnimatedTorchBlock.MODE));
		}
	}

	@Override
	public boolean onUsedByWand(@Nullable Player player, ItemStack stack, Direction side) {
		return level.setBlock(getBlockPos(), getBlockState().cycle(AnimatedTorchBlock.MODE), Block.UPDATE_ALL);
	}

	@Override
	public void onBurstCollision(ManaBurst burst) {
		if (!burst.isFake()) {
			toggle();
		}
	}

	public static void serverRotatingTick(Level level, BlockPos pos, BlockState state, AnimatedTorchBlockEntity self) {
		self.rotationTicks--;
		if (self.rotationTicks <= 0) {
			level.setBlock(pos, state.setValue(AnimatedTorchBlock.TRIGGERED, false), Block.UPDATE_ALL);
		} else {
			level.blockEntityChanged(pos);
		}
	}

	@Override
	public boolean triggerEvent(int id, int param) {
		if (id != EVENT_TRIGGER_ROTATE) {
			return super.triggerEvent(id, param);
		}
		Direction[] directions = Direction.values();
		if (param < 0 || param >= directions.length) {
			return false;
		}
		Direction newFacing = directions[param];
		if (!AnimatedTorchBlock.FACING.getPossibleValues().contains(newFacing)) {
			return false;
		}
		if (!level.isClientSide()) {
			rotationTicks = ROTATION_TICKS;
		} else {
			lastRotation = rotation;
			rotationStartTime = level.getGameTime();
		}
		level.setBlock(
				getBlockPos(),
				getBlockState()
						.setValue(AnimatedTorchBlock.FACING, newFacing)
						.setValue(AnimatedTorchBlock.TRIGGERED, true),
				Block.UPDATE_ALL
		);
		return true;
	}

	@Override
	public void onTriggeredByHourglass(BlockEntity hourglass) {
		toggle();
	}

	public static class WandHud implements WandHUD {
		private final AnimatedTorchBlockEntity torch;

		public WandHud(AnimatedTorchBlockEntity torch) {
			this.torch = torch;
		}

		@Override
		public void renderHUD(GuiGraphics gui, Window window, Font font, float partialTick) {
			int x = window.getGuiScaledWidth() / 2 + 8;
			int y = window.getGuiScaledHeight() / 2 - 10;

			String str = I18n.get("botania.animatedTorch."
					+ torch.getBlockState().getValue(AnimatedTorchBlock.MODE).getSerializedName());
			RenderHelper.renderHUDBox(gui, x, y, x + 18 + font.width(str), y + 20);
			gui.renderFakeItem(new ItemStack(Blocks.REDSTONE_TORCH), x, y + 2);
			gui.drawString(font, str, x + 16, y + 6, 0xFF4444);
		}
	}

	@Override
	protected void saveAdditional(CompoundTag cmp, HolderLookup.Provider registries) {
		cmp.putInt(TAG_ROTATION_TICKS, rotationTicks);
	}

	@Override
	protected void loadAdditional(CompoundTag cmp, HolderLookup.Provider registries) {
		if (level != null && !level.isClientSide()) {
			rotationTicks = cmp.getInt(TAG_ROTATION_TICKS);
		}
	}
}
