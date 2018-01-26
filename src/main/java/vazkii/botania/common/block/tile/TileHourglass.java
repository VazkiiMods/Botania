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

import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.client.resources.I18n;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ITickable;
import net.minecraft.util.StringUtils;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import vazkii.botania.api.internal.VanillaPacketDispatcher;
import vazkii.botania.api.item.IHourglassTrigger;
import vazkii.botania.api.state.BotaniaStateProps;
import vazkii.botania.common.item.ModItems;

import javax.annotation.Nonnull;

public class TileHourglass extends TileSimpleInventory implements ITickable {

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

	private boolean isDust() {
		ItemStack stack = itemHandler.getStackInSlot(0);
		return !stack.isEmpty() && stack.getItem() == ModItems.manaResource && stack.getMetadata() == 23;
	}

	@Override
	public void update() {
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
					world.setBlockState(getPos(), world.getBlockState(getPos()).withProperty(BotaniaStateProps.POWERED, true), 1);
					world.scheduleUpdate(pos, getBlockType(), getBlockType().tickRate(world));
				}

				for(EnumFacing facing : EnumFacing.VALUES) {
					BlockPos pos = getPos().offset(facing);
					IBlockState state = world.getBlockState(pos);
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
		if(stack.getItem() == Item.getItemFromBlock(Blocks.SAND))
			return stack.getItemDamage() == 1 ? 200 : 20;
		if(stack.getItem() == Item.getItemFromBlock(Blocks.SOUL_SAND))
			return 1200;
		if(stack.getItem() == ModItems.manaResource)
			return 1;
		return 0;
	}

	public int getColor() {
		ItemStack stack = itemHandler.getStackInSlot(0);
		if(stack.isEmpty())
			return 0;
		if(stack.getItem() == Item.getItemFromBlock(Blocks.SAND))
			return stack.getItemDamage() == 1 ? 0xE95800 : 0xFFEC49;
		if(stack.getItem() == Item.getItemFromBlock(Blocks.SOUL_SAND))
			return 0x5A412f;
		if(stack.getItem() == ModItems.manaResource)
			return 0x03abff;

		return 0;
	}

	@Override
	protected SimpleItemStackHandler createItemHandler() {
		return new SimpleItemStackHandler(this, true) {
			@Nonnull
			@Override
			public ItemStack insertItem(int slot, @Nonnull ItemStack stack, boolean simulate) {
				if(!stack.isEmpty() && (stack.getItem() == Item.getItemFromBlock(Blocks.SAND) || stack.getItem() == Item.getItemFromBlock(Blocks.SOUL_SAND)) || stack.getItem() == ModItems.manaResource && stack.getItemDamage() == 23)
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
	public void writePacketNBT(NBTTagCompound par1nbtTagCompound) {
		super.writePacketNBT(par1nbtTagCompound);
		par1nbtTagCompound.setInteger(TAG_TIME, time);
		par1nbtTagCompound.setFloat(TAG_TIME_FRACTION, timeFraction);
		par1nbtTagCompound.setBoolean(TAG_FLIP, flip);
		par1nbtTagCompound.setInteger(TAG_FLIP_TICKS, flipTicks);
		par1nbtTagCompound.setBoolean(TAG_MOVE, move);
		par1nbtTagCompound.setBoolean(TAG_LOCK, lock);
	}

	@Override
	public void readPacketNBT(NBTTagCompound par1nbtTagCompound) {
		super.readPacketNBT(par1nbtTagCompound);
		time = par1nbtTagCompound.getInteger(TAG_TIME);
		timeFraction = par1nbtTagCompound.getFloat(TAG_TIME_FRACTION);
		flip = par1nbtTagCompound.getBoolean(TAG_FLIP);
		flipTicks = par1nbtTagCompound.getInteger(TAG_FLIP_TICKS);
		move = par1nbtTagCompound.getBoolean(TAG_MOVE);
		lock = par1nbtTagCompound.getBoolean(TAG_LOCK);
	}

	@Override
	public int getSizeInventory() {
		return 1;
	}

	@SideOnly(Side.CLIENT)
	public void renderHUD(ScaledResolution res) {
		Minecraft mc = Minecraft.getMinecraft();
		int x = res.getScaledWidth() / 2 + 10;
		int y = res.getScaledHeight() / 2 - 10;

		ItemStack stack = itemHandler.getStackInSlot(0);
		if(!stack.isEmpty()) {
			RenderHelper.enableGUIStandardItemLighting();
			GlStateManager.enableRescaleNormal();
			mc.getRenderItem().renderItemIntoGUI(stack, x, y);
			mc.getRenderItem().renderItemOverlays(mc.fontRenderer, stack, x, y);
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
