/**
 * This class was created by <Vazkii>. It's distributed as
 * part of the Botania Mod. Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 *
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 *
 * File Created @ [May 29, 2015, 8:21:17 PM (GMT)]
 */
package vazkii.botania.common.block.tile;

import com.mojang.blaze3d.platform.GlStateManager;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.client.resources.I18n;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.tileentity.ITickableTileEntity;
import net.minecraft.tileentity.TileEntityType;
import net.minecraft.util.Direction;
import net.minecraft.util.StringUtils;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.registries.ObjectHolder;
import vazkii.botania.api.internal.VanillaPacketDispatcher;
import vazkii.botania.api.item.IHourglassTrigger;
import vazkii.botania.api.state.BotaniaStateProps;
import vazkii.botania.common.item.ModItems;
import vazkii.botania.common.lib.LibBlockNames;
import vazkii.botania.common.lib.LibMisc;

import javax.annotation.Nonnull;

public class TileHourglass extends TileSimpleInventory implements ITickableTileEntity {
	@ObjectHolder(LibMisc.MOD_ID + ":" + LibBlockNames.HOURGLASS)
	public static TileEntityType<TileHourglass> TYPE;
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

	public TileHourglass() {
		super(TYPE);
	}

	private boolean isDust() {
		ItemStack stack = itemHandler.getStackInSlot(0);
		return !stack.isEmpty() && stack.getItem() == ModItems.manaPowder;
	}

	@Override
	public void tick() {
		int totalTime = getTotalTime();
		boolean dust = isDust();

		if(totalTime > 0 || dust) {
			if(move && !dust)
				time++;

			if(time >= totalTime) {
				time = 0;
				flip = !flip;
				flipTicks = 4;
				if(!world.isRemote) {
					world.setBlockState(getPos(), getBlockState().with(BotaniaStateProps.POWERED, true), 1);
					world.getPendingBlockTicks().scheduleTick(pos, getBlockState().getBlock(), getBlockState().getBlock().tickRate(world));
				}

				for(Direction facing : Direction.values()) {
					BlockPos pos = getPos().offset(facing);
					BlockState state = world.getBlockState(pos);
					if(state.getBlock() instanceof IHourglassTrigger)
						((IHourglassTrigger) state.getBlock()).onTriggeredByHourglass(world, pos, this);
				}
			}

			lastFraction = timeFraction;
			timeFraction = (float) time / (float) totalTime;
		} else {
			time = 0;
			lastFraction = 0F;
			timeFraction = 0F;
		}

		if(flipTicks > 0)
			flipTicks--;
	}

	public void onManaCollide() {
		if(!world.isRemote) {
			if(isDust())
				time++;
			else move = !move;
			VanillaPacketDispatcher.dispatchTEToNearbyPlayers(this);
		}
	}

	public int getTotalTime() {
		ItemStack stack = itemHandler.getStackInSlot(0);
		if(stack.isEmpty())
			return 0;

		return getStackItemTime(stack) * stack.getCount();
	}

	public static int getStackItemTime(ItemStack stack) {
		if(stack.isEmpty())
			return 0;
		if(stack.getItem() == Blocks.SAND.asItem())
			return 20;
		if(stack.getItem() == Blocks.RED_SAND.asItem())
			return 200;
		if(stack.getItem() == Blocks.SOUL_SAND.asItem())
			return 1200;
		if(stack.getItem() == ModItems.manaPowder)
			return 1;
		return 0;
	}

	public int getColor() {
		ItemStack stack = itemHandler.getStackInSlot(0);
		if(stack.isEmpty())
			return 0;
		if(stack.getItem() == Blocks.SAND.asItem())
			return 0xFFEC49;
		if(stack.getItem() == Blocks.RED_SAND.asItem())
			return 0xE95800;
		if(stack.getItem() == Blocks.SOUL_SAND.asItem())
			return 0x5A412f;
		if(stack.getItem() == ModItems.manaPowder)
			return 0x03abff;

		return 0;
	}

	@Override
	protected SimpleItemStackHandler createItemHandler() {
		return new SimpleItemStackHandler(this, true) {
			@Nonnull
			@Override
			public ItemStack insertItem(int slot, @Nonnull ItemStack stack, boolean simulate) {
				if(!stack.isEmpty() && (stack.getItem() == Blocks.SAND.asItem()
						|| stack.getItem() == Blocks.RED_SAND.asItem()
						|| stack.getItem() == Blocks.SOUL_SAND.asItem()
						|| stack.getItem() == ModItems.manaPowder))
					return super.insertItem(slot, stack, simulate);
				else return stack;
			}

			@Override
			public void onContentsChanged(int slot) {
				super.onContentsChanged(slot);
				if(!TileHourglass.this.world.isRemote) {
					time = 0;
					timeFraction = 0F;
					VanillaPacketDispatcher.dispatchTEToNearbyPlayers(TileHourglass.this);
				}
			}
		};
	}

	@Override
	public void writePacketNBT(CompoundNBT par1nbtTagCompound) {
		super.writePacketNBT(par1nbtTagCompound);
		par1nbtTagCompound.putInt(TAG_TIME, time);
		par1nbtTagCompound.putFloat(TAG_TIME_FRACTION, timeFraction);
		par1nbtTagCompound.putBoolean(TAG_FLIP, flip);
		par1nbtTagCompound.putInt(TAG_FLIP_TICKS, flipTicks);
		par1nbtTagCompound.putBoolean(TAG_MOVE, move);
		par1nbtTagCompound.putBoolean(TAG_LOCK, lock);
	}

	@Override
	public void readPacketNBT(CompoundNBT par1nbtTagCompound) {
		super.readPacketNBT(par1nbtTagCompound);
		time = par1nbtTagCompound.getInt(TAG_TIME);
		timeFraction = par1nbtTagCompound.getFloat(TAG_TIME_FRACTION);
		flip = par1nbtTagCompound.getBoolean(TAG_FLIP);
		flipTicks = par1nbtTagCompound.getInt(TAG_FLIP_TICKS);
		move = par1nbtTagCompound.getBoolean(TAG_MOVE);
		lock = par1nbtTagCompound.getBoolean(TAG_LOCK);
	}

	@Override
	public int getSizeInventory() {
		return 1;
	}

	@OnlyIn(Dist.CLIENT)
	public void renderHUD() {
		Minecraft mc = Minecraft.getInstance();
		int x = mc.mainWindow.getScaledWidth() / 2 + 10;
		int y = mc.mainWindow.getScaledHeight() / 2 - 10;

		ItemStack stack = itemHandler.getStackInSlot(0);
		if(!stack.isEmpty()) {
			RenderHelper.enableGUIStandardItemLighting();
			GlStateManager.enableRescaleNormal();
			mc.getItemRenderer().renderItemIntoGUI(stack, x, y);
			mc.getItemRenderer().renderItemOverlays(mc.fontRenderer, stack, x, y);
			GlStateManager.disableRescaleNormal();
			RenderHelper.disableStandardItemLighting();

			int time = getTotalTime();
			String timeStr = StringUtils.ticksToElapsedTime(time);
			mc.fontRenderer.drawStringWithShadow(timeStr, x + 20, y, getColor());

			String status = "";
			if(lock)
				status = "locked";
			if(!move)
				status = status.isEmpty() ? "stopped" : "lockedStopped";
			if(!status.isEmpty())
				mc.fontRenderer.drawStringWithShadow(I18n.format("botaniamisc." + status), x + 20, y + 12, getColor());
		}

	}

}
