/*
 * This class is distributed as part of the Botania Mod.
 * Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 *
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 */
package vazkii.botania.common.block.tile.mana;

import com.mojang.blaze3d.matrix.MatrixStack;

import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.tileentity.ITickableTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.Direction;
import net.minecraft.util.text.TextFormatting;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import vazkii.botania.api.internal.VanillaPacketDispatcher;
import vazkii.botania.common.block.tile.ModTiles;
import vazkii.botania.common.block.tile.TileMod;

public class TileTurntable extends TileMod implements ITickableTileEntity {
	private static final String TAG_SPEED = "speed";
	private static final String TAG_BACKWARDS = "backwards";

	private int speed = 1;
	private boolean backwards = false;

	public TileTurntable() {
		super(ModTiles.TURNTABLE);
	}

	@Override
	public void tick() {
		boolean redstone = false;

		for (Direction dir : Direction.values()) {
			int redstoneSide = world.getRedstonePower(pos.offset(dir), dir);
			if (redstoneSide > 0) {
				redstone = true;
			}
		}

		if (!redstone) {
			TileEntity tile = world.getTileEntity(pos.up());
			if (tile instanceof TileSpreader) {
				TileSpreader spreader = (TileSpreader) tile;
				spreader.rotationX += speed * (backwards ? -1 : 1);
				if (spreader.rotationX >= 360F) {
					spreader.rotationX -= 360F;
				}
				if (!world.isRemote) {
					spreader.checkForReceiver();
				}
			}
		}
	}

	@Override
	public void writePacketNBT(CompoundNBT cmp) {
		cmp.putInt(TAG_SPEED, speed);
		cmp.putBoolean(TAG_BACKWARDS, backwards);
	}

	@Override
	public void readPacketNBT(CompoundNBT cmp) {
		speed = cmp.getInt(TAG_SPEED);
		backwards = cmp.getBoolean(TAG_BACKWARDS);
	}

	public void onWanded(PlayerEntity player, ItemStack wand, Direction side) {
		if ((player != null && player.isSneaking()) || (player == null && side == Direction.DOWN)) {
			backwards = !backwards;
		} else {
			speed = speed == 6 ? 1 : speed + 1;
		}
		VanillaPacketDispatcher.dispatchTEToNearbyPlayers(this);
	}

	@OnlyIn(Dist.CLIENT)
	public void renderHUD(MatrixStack ms, Minecraft mc) {
		int color = 0xAA006600;

		char motion = backwards ? '<' : '>';
		String speed = TextFormatting.BOLD + "";
		for (int i = 0; i < this.speed; i++) {
			speed = speed + motion;
		}

		int x = mc.getMainWindow().getScaledWidth() / 2 - mc.fontRenderer.getStringWidth(speed) / 2;
		int y = mc.getMainWindow().getScaledHeight() / 2 - 15;
		mc.fontRenderer.func_238405_a_(ms, speed, x, y, color);
	}

}
