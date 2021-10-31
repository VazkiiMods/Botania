/*
 * This class is distributed as part of the Botania Mod.
 * Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 *
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 */
package vazkii.botania.common.block.tile.mana;

import com.mojang.blaze3d.vertex.PoseStack;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;

import vazkii.botania.api.block.IWandable;
import vazkii.botania.api.internal.VanillaPacketDispatcher;
import vazkii.botania.common.block.tile.ModTiles;
import vazkii.botania.common.block.tile.TileMod;

import javax.annotation.Nullable;

public class TileTurntable extends TileMod implements IWandable {
	private static final String TAG_SPEED = "speed";
	private static final String TAG_BACKWARDS = "backwards";

	private int speed = 1;
	private boolean backwards = false;

	public TileTurntable(BlockPos pos, BlockState state) {
		super(ModTiles.TURNTABLE, pos, state);
	}

	public static void commonTick(Level level, BlockPos worldPosition, BlockState state, TileTurntable self) {
		if (!level.hasNeighborSignal(worldPosition)) {
			BlockEntity tile = level.getBlockEntity(worldPosition.above());
			if (tile instanceof TileSpreader spreader) {
				spreader.rotationX += self.speed * (self.backwards ? -1 : 1);
				if (spreader.rotationX >= 360F) {
					spreader.rotationX -= 360F;
				}
				if (!level.isClientSide) {
					spreader.checkForReceiver();
				}
			}
		}
	}

	@Override
	public void writePacketNBT(CompoundTag cmp) {
		cmp.putInt(TAG_SPEED, speed);
		cmp.putBoolean(TAG_BACKWARDS, backwards);
	}

	@Override
	public void readPacketNBT(CompoundTag cmp) {
		speed = cmp.getInt(TAG_SPEED);
		backwards = cmp.getBoolean(TAG_BACKWARDS);
	}

	@Override
	public boolean onUsedByWand(@Nullable Player player, ItemStack wand, Direction side) {
		if ((player != null && player.isShiftKeyDown()) || (player == null && side == Direction.DOWN)) {
			backwards = !backwards;
		} else {
			speed = speed == 6 ? 1 : speed + 1;
		}
		VanillaPacketDispatcher.dispatchTEToNearbyPlayers(this);
		return true;
	}

	@Environment(EnvType.CLIENT)
	public void renderHUD(PoseStack ms, Minecraft mc) {
		int color = 0xAA006600;

		char motion = backwards ? '<' : '>';
		String speed = ChatFormatting.BOLD + "";
		for (int i = 0; i < this.speed; i++) {
			speed = speed + motion;
		}

		int x = mc.getWindow().getGuiScaledWidth() / 2 - mc.font.width(speed) / 2;
		int y = mc.getWindow().getGuiScaledHeight() / 2 - 15;
		mc.font.drawShadow(ms, speed, x, y, color);
	}

}
