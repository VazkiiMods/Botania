/**
 * This class was created by <Vazkii>. It's distributed as
 * part of the Botania Mod. Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 *
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 *
 * File Created @ [28/09/2016, 17:21:24 (GMT)]
 */
package vazkii.botania.common.block.tile;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.ITickable;
import net.minecraft.util.text.translation.I18n;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import vazkii.botania.api.internal.VanillaPacketDispatcher;
import vazkii.botania.common.block.ModBlocks;

import java.util.Arrays;

public class TileAnimatedTorch extends TileMod implements ITickable {

	public static final String TAG_SIDE = "side";
	public static final String TAG_ROTATING = "rotating";
	public static final String TAG_ROTATION_TICKS = "rotationTicks";
	public static final String TAG_ANGLE_PER_TICK = "anglePerTick";
	public static final String TAG_TORCH_MODE = "torchMode";
	public static final String TAG_NEXT_RANDOM_ROTATION = "nextRandomRotation";

	public static final EnumFacing[] SIDES = new EnumFacing[] {
			EnumFacing.NORTH,
			EnumFacing.EAST,
			EnumFacing.SOUTH,
			EnumFacing.WEST
	};

	public int side;
	public double rotation;
	public boolean rotating;
	public double lastTickRotation;
	public int nextRandomRotation;
	public int currentRandomRotation;

	private int rotationTicks;
	public double anglePerTick;

	private TorchMode torchMode = TorchMode.TOGGLE;

	@Override
	public void onLoad() {
		if(!world.isRemote)
			nextRandomRotation = world.rand.nextInt(4);
	}

	public void handRotate() {
		if(!world.isRemote)
			world.addBlockEvent(getPos(), ModBlocks.animatedTorch, 0, (side + 1) % 4);
	}
	
	public void onPlace(EntityLivingBase entity) {
		if(entity != null) {
			side = Arrays.asList(SIDES).indexOf(entity.getHorizontalFacing().getOpposite());
		}
		world.notifyNeighborsOfStateChange(getPos().offset(SIDES[side].getOpposite()), getBlockType(), false);
	}

	public void toggle() {
		if(!world.isRemote) {
			world.addBlockEvent(getPos(), ModBlocks.animatedTorch, 0, torchMode.modeSwitcher.rotate(this, side));
			nextRandomRotation = world.rand.nextInt(4);
			VanillaPacketDispatcher.dispatchTEToNearbyPlayers(this);
		}
	}

	public void onWanded() {
		int modeOrdinal = torchMode.ordinal();
		TorchMode[] modes = TorchMode.values();

		torchMode = modes[(modeOrdinal + 1) % modes.length];
	}

	@Override
	public boolean receiveClientEvent(int id, int param) {
		if (id == 0) {
			rotateTo(param);
			return true;
		} else {
			return super.receiveClientEvent(id, param);
		}
	}

	private void rotateTo(int side) {
		if(rotating)
			return;

		currentRandomRotation = nextRandomRotation;
		int finalRotation = side * 90;

		double diff = (finalRotation - rotation % 360) % 360;
		if(diff < 0)
			diff = 360 + diff;

		rotationTicks = 4;
		anglePerTick = diff / rotationTicks;
		this.side = side;
		rotating = true;

		// tell neighbors that signal is off because we are rotating
		world.notifyNeighborsOfStateChange(getPos(), getBlockType(), false);
		for(EnumFacing e : EnumFacing.VALUES) {
			world.notifyNeighborsOfStateChange(getPos().offset(e), getBlockType(), false);
		}
	}

	@SideOnly(Side.CLIENT)
	public void renderHUD(Minecraft mc, ScaledResolution res) {
		int x = res.getScaledWidth() / 2 + 10;
		int y = res.getScaledHeight() / 2 - 8;

		mc.getRenderItem().renderItemAndEffectIntoGUI(new ItemStack(Blocks.REDSTONE_TORCH), x, y);
		mc.fontRenderer.drawStringWithShadow(I18n.translateToLocal("botania.animatedTorch." + torchMode.name().toLowerCase()), x + 18, y + 6, 0xFF4444);
	}

	@Override
	public void update() {
		if(rotating) {
			lastTickRotation = rotation;
			rotation = (rotation + anglePerTick) % 360;
			rotationTicks--;

			if(rotationTicks <= 0) {
				rotating = false;
				// done rotating, tell neighbors
				world.notifyNeighborsOfStateChange(getPos(), getBlockType(), false);
				for(EnumFacing e : EnumFacing.VALUES) {
					world.notifyNeighborsOfStateChange(getPos().offset(e), getBlockType(), false);
				}
			}

		} else rotation = side * 90;

		if(world.isRemote) {
			int amt = rotating ? 3 : Math.random() < 0.1 ? 1 : 0;
			double x = getPos().getX() + 0.5 + Math.cos((rotation + 90) / 180.0 * Math.PI) * 0.35;
			double y = getPos().getY() + 0.2;
			double z = getPos().getZ() + 0.5 + Math.sin((rotation + 90) / 180.0 * Math.PI) * 0.35;

			for(int i = 0; i < amt; i++)
				world.spawnParticle(EnumParticleTypes.REDSTONE, x, y, z, 0.0D, 0.0D, 0.0D);
		}
	}

	@Override
	public void writePacketNBT(NBTTagCompound cmp) {
		cmp.setInteger(TAG_SIDE, side);
		cmp.setBoolean(TAG_ROTATING, rotating);
		cmp.setInteger(TAG_ROTATION_TICKS, rotationTicks);
		cmp.setDouble(TAG_ANGLE_PER_TICK, anglePerTick);
		cmp.setInteger(TAG_TORCH_MODE, torchMode.ordinal());
		cmp.setInteger(TAG_NEXT_RANDOM_ROTATION, nextRandomRotation);
	}

	@Override
	public void readPacketNBT(NBTTagCompound cmp) {
		side = cmp.getInteger(TAG_SIDE);
		rotating = cmp.getBoolean(TAG_ROTATING);
		if(world != null && !world.isRemote)
			rotationTicks = cmp.getInteger(TAG_ROTATION_TICKS);
		anglePerTick = cmp.getDouble(TAG_ANGLE_PER_TICK);
		nextRandomRotation = cmp.getInteger(TAG_NEXT_RANDOM_ROTATION);

		int modeOrdinal = cmp.getInteger(TAG_TORCH_MODE);
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
