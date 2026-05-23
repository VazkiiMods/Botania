/*
 * This class is distributed as part of the Botania Mod.
 * Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 *
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 */
package vazkii.botania.common.block.block_entity.mana;

import com.mojang.blaze3d.platform.Window;

import net.minecraft.ChatFormatting;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;

import org.apache.commons.lang3.StringUtils;
import org.jetbrains.annotations.Nullable;

import vazkii.botania.api.block.WandHUD;
import vazkii.botania.api.block.Wandable;
import vazkii.botania.api.state.BotaniaStateProperties;
import vazkii.botania.client.core.helper.RenderHelper;
import vazkii.botania.common.block.block_entity.BotaniaBlockEntities;

public class SpreaderTurntableBlockEntity extends BlockEntity implements Wandable {

	public SpreaderTurntableBlockEntity(BlockPos pos, BlockState state) {
		super(BotaniaBlockEntities.TURNTABLE, pos, state);
	}

	public static void commonTick(Level level, BlockPos worldPosition, BlockState state, SpreaderTurntableBlockEntity self) {
		if (state.getValue(BlockStateProperties.POWERED)) {
			return;
		}
		BlockEntity tile = level.getBlockEntity(worldPosition.above());
		if (tile instanceof ManaSpreaderBlockEntity spreader) {
			spreader.rotationX += state.getValue(BotaniaStateProperties.SPEED)
					* (state.getValue(BotaniaStateProperties.BACKWARDS) ? -1 : 1);
			if (spreader.rotationX >= 360F) {
				spreader.rotationX -= 360F;
			} else if (spreader.rotationX < 0F) {
				spreader.rotationX += 360F;
			}
			if (!level.isClientSide()) {
				spreader.checkForReceiver();
			}
		}
	}

	@Override
	public boolean onUsedByWand(@Nullable Player player, ItemStack wand, Direction side) {
		if ((player != null && player.isSecondaryUseActive()) || (player == null && side == Direction.DOWN)) {
			level.setBlockAndUpdate(getBlockPos(), getBlockState().cycle(BotaniaStateProperties.BACKWARDS));
		} else {
			level.setBlockAndUpdate(getBlockPos(), getBlockState().cycle(BotaniaStateProperties.SPEED));
		}
		return true;
	}

	public static class WandHud implements WandHUD {
		private final SpreaderTurntableBlockEntity turntable;

		public WandHud(SpreaderTurntableBlockEntity turntable) {
			this.turntable = turntable;
		}

		@Override
		public void renderHUD(GuiGraphics gui, Window window, Font font, float partialTick) {
			Component speed = Component
					.literal(StringUtils.repeat(
							turntable.getBlockState().getValue(BotaniaStateProperties.BACKWARDS) ? '<' : '>',
							turntable.getBlockState().getValue(BotaniaStateProperties.SPEED)))
					.withStyle(ChatFormatting.BOLD).withStyle(ChatFormatting.WHITE);

			int strWidth = font.width(speed);
			int x = (window.getGuiScaledWidth() - strWidth) / 2;
			int y = window.getGuiScaledHeight() / 2 + 8;

			RenderHelper.renderHUDBox(gui, x - 2, y, x + strWidth + 2, y + 12);
			gui.drawString(font, speed, x, y + 2, 0xFFFFFF);
		}
	}

}
