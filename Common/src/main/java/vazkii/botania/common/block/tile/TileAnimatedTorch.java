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

import net.minecraft.client.Minecraft;
import net.minecraft.client.resources.language.I18n;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.particles.DustParticleOptions;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;

import vazkii.botania.api.block.IWandHUD;
import vazkii.botania.api.block.IWandable;
import vazkii.botania.api.internal.VanillaPacketDispatcher;
import vazkii.botania.common.block.ModBlocks;

import javax.annotation.Nullable;

import java.util.Arrays;
import java.util.Locale;

public class TileAnimatedTorch extends TileMod implements IWandable {
	private static final String TAG_SIDE = "side";
	private static final String TAG_ROTATING = "rotating";
	private static final String TAG_ROTATION_TICKS = "rotationTicks";
	private static final String TAG_ANGLE_PER_TICK = "anglePerTick";
	private static final String TAG_TORCH_MODE = "torchMode";
	private static final String TAG_NEXT_RANDOM_ROTATION = "nextRandomRotation";

	public static final Direction[] SIDES = new Direction[] {
			Direction.NORTH,
			Direction.EAST,
			Direction.SOUTH,
			Direction.WEST
	};

	public int side;
	public double rotation;
	public boolean rotating;
	public double lastTickRotation;
	public int nextRandomRotation = Mth.floor(Math.random() * 3);
	public int currentRandomRotation;

	private int rotationTicks;
	public double anglePerTick;

	private TorchMode torchMode = TorchMode.TOGGLE;

	public TileAnimatedTorch(BlockPos pos, BlockState state) {
		super(ModTiles.ANIMATED_TORCH, pos, state);
	}

	public void handRotate() {
		if (!level.isClientSide) {
			level.blockEvent(getBlockPos(), ModBlocks.animatedTorch, 0, (side + 1) % 4);
		}
	}

	public void onPlace(@Nullable LivingEntity entity) {
		if (entity != null) {
			side = Arrays.asList(SIDES).indexOf(entity.getDirection().getOpposite());
		}
		level.updateNeighborsAt(getBlockPos().relative(SIDES[side].getOpposite()), getBlockState().getBlock());
	}

	public void toggle() {
		if (!level.isClientSide) {
			level.blockEvent(getBlockPos(), ModBlocks.animatedTorch, 0, torchMode.modeSwitcher.rotate(this, side));
			nextRandomRotation = level.random.nextInt(4);
			VanillaPacketDispatcher.dispatchTEToNearbyPlayers(this);
		}
	}

	@Override
	public boolean onUsedByWand(Player player, ItemStack stack, Direction side) {
		int modeOrdinal = torchMode.ordinal();
		TorchMode[] modes = TorchMode.values();

		torchMode = modes[(modeOrdinal + 1) % modes.length];
		return true;
	}

	@Override
	public boolean triggerEvent(int id, int param) {
		if (id == 0) {
			rotateTo(param);
			return true;
		} else {
			return super.triggerEvent(id, param);
		}
	}

	private void rotateTo(int side) {
		if (rotating) {
			return;
		}

		currentRandomRotation = nextRandomRotation;
		int finalRotation = side * 90;

		double diff = (finalRotation - rotation % 360) % 360;
		if (diff < 0) {
			diff = 360 + diff;
		}

		rotationTicks = 4;
		anglePerTick = diff / rotationTicks;
		this.side = side;
		rotating = true;

		// tell neighbors that signal is off because we are rotating
		level.updateNeighborsAt(getBlockPos(), getBlockState().getBlock());
		for (Direction e : Direction.values()) {
			level.updateNeighborsAt(getBlockPos().relative(e), getBlockState().getBlock());
		}
	}

	public static class WandHud implements IWandHUD {
		private final TileAnimatedTorch torch;

		public WandHud(TileAnimatedTorch torch) {
			this.torch = torch;
		}

		@Override
		public void renderHUD(PoseStack ms, Minecraft mc) {
			int x = mc.getWindow().getGuiScaledWidth() / 2 + 10;
			int y = mc.getWindow().getGuiScaledHeight() / 2 - 8;

			mc.getItemRenderer().renderAndDecorateItem(new ItemStack(Blocks.REDSTONE_TORCH), x, y);
			mc.font.drawShadow(ms, I18n.get("botania.animatedTorch." + torch.torchMode.name().toLowerCase(Locale.ROOT)), x + 18, y + 6, 0xFF4444);
		}
	}

	public static void commonTick(Level level, BlockPos worldPosition, BlockState state, TileAnimatedTorch self) {
		if (self.rotating) {
			self.lastTickRotation = self.rotation;
			self.rotation = (self.rotation + self.anglePerTick) % 360;
			self.rotationTicks--;

			if (self.rotationTicks <= 0) {
				self.rotating = false;
				// done rotating, tell neighbors
				level.updateNeighborsAt(worldPosition, state.getBlock());
				for (Direction e : Direction.values()) {
					level.updateNeighborsAt(worldPosition.relative(e), state.getBlock());
				}
			}

		} else {
			self.rotation = self.side * 90;
		}

		if (level.isClientSide) {
			int amt = self.rotating ? 3 : Math.random() < 0.1 ? 1 : 0;
			double x = worldPosition.getX() + 0.5 + Math.cos((self.rotation + 90) / 180.0 * Math.PI) * 0.35;
			double y = worldPosition.getY() + 0.2;
			double z = worldPosition.getZ() + 0.5 + Math.sin((self.rotation + 90) / 180.0 * Math.PI) * 0.35;

			for (int i = 0; i < amt; i++) {
				level.addParticle(DustParticleOptions.REDSTONE, x, y, z, 0.0D, 0.0D, 0.0D);
			}
		}
	}

	@Override
	public void writePacketNBT(CompoundTag cmp) {
		cmp.putInt(TAG_SIDE, side);
		cmp.putBoolean(TAG_ROTATING, rotating);
		cmp.putInt(TAG_ROTATION_TICKS, rotationTicks);
		cmp.putDouble(TAG_ANGLE_PER_TICK, anglePerTick);
		cmp.putInt(TAG_TORCH_MODE, torchMode.ordinal());
		cmp.putInt(TAG_NEXT_RANDOM_ROTATION, nextRandomRotation);
	}

	@Override
	public void readPacketNBT(CompoundTag cmp) {
		side = cmp.getInt(TAG_SIDE);
		rotating = cmp.getBoolean(TAG_ROTATING);
		if (level != null && !level.isClientSide) {
			rotationTicks = cmp.getInt(TAG_ROTATION_TICKS);
		}
		anglePerTick = cmp.getDouble(TAG_ANGLE_PER_TICK);
		nextRandomRotation = cmp.getInt(TAG_NEXT_RANDOM_ROTATION);

		int modeOrdinal = cmp.getInt(TAG_TORCH_MODE);
		TorchMode[] modes = TorchMode.values();
		torchMode = modes[modeOrdinal % modes.length];
	}

	public enum TorchMode {
		TOGGLE((t, i) -> (i + 2) % 4),
		ROTATE((t, i) -> (i + 1) % 4),
		RANDOM((t, i) -> t.currentRandomRotation);

		TorchMode(RotationHandler modeSwitcher) {
			this.modeSwitcher = modeSwitcher;
		}

		public final RotationHandler modeSwitcher;

		private interface RotationHandler {
			int rotate(TileAnimatedTorch tile, int curr);
		}
	}
}
