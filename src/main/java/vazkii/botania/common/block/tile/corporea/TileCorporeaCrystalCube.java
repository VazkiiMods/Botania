/*
 * This class is distributed as part of the Botania Mod.
 * Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 *
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 */
package vazkii.botania.common.block.tile.corporea;

import com.mojang.blaze3d.matrix.MatrixStack;
import net.minecraft.client.Minecraft;
import net.minecraft.client.resources.I18n;
import net.minecraft.entity.item.ItemEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.tileentity.ITickableTileEntity;

import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.World;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import vazkii.botania.api.corporea.CorporeaHelper;
import vazkii.botania.api.corporea.ICorporeaRequestMatcher;
import vazkii.botania.api.corporea.ICorporeaRequestor;
import vazkii.botania.api.corporea.ICorporeaSpark;
import vazkii.botania.api.internal.VanillaPacketDispatcher;
import vazkii.botania.common.block.tile.ModTiles;

import java.util.List;

public class TileCorporeaCrystalCube extends TileCorporeaBase implements ICorporeaRequestor, ITickableTileEntity {
	private static final String TAG_REQUEST_TARGET = "requestTarget";
	private static final String TAG_ITEM_COUNT = "itemCount";
	private static final String TAG_LOCK = "lock";
	private static final String TAG_CHECK_NBT = "checkNBT";

	private ItemStack requestTarget = ItemStack.EMPTY;
	private ItemStack requestTargetNoNBT = ItemStack.EMPTY;
	private int itemCount = 0;
	private int ticks = 0;
	private int compValue = 0;
	public boolean locked = false;
	private boolean checkNBT = false;

	public TileCorporeaCrystalCube() {
		super(ModTiles.CORPOREA_CRYSTAL_CUBE);
	}

	@Override
	public void tick() {
		++ticks;
		if (ticks % 20 == 0) {
			updateCount();
		}
	}

	public void setRequestTarget(ItemStack stack) {
		if (!stack.isEmpty() && !locked) {
			ItemStack copy = stack.copy();
			copy.setCount(1);
			requestTarget = copy;
			requestTargetNoNBT = requestTarget.copy();
			requestTargetNoNBT.setTag(null);
			updateCount();
			if (!world.isRemote) {
				VanillaPacketDispatcher.dispatchTEToNearbyPlayers(this);
			}
		}
	}

	public ItemStack getRequestTarget() {
		return checkNBT ? requestTarget : requestTargetNoNBT;
	}

	public int getItemCount() {
		return itemCount;
	}

	public void doRequest(boolean fullStack) {
		if (world.isRemote) {
			return;
		}

		ICorporeaSpark spark = getSpark();
		if (spark != null && spark.getMaster() != null && !requestTarget.isEmpty()) {
			int count = fullStack ? requestTarget.getMaxStackSize() : 1;
			doCorporeaRequest(CorporeaHelper.instance().createMatcher(requestTarget, checkNBT), count, spark);
		}
	}

	private void updateCount() {
		if (world.isRemote) {
			return;
		}

		int sum = 0;
		ICorporeaSpark spark = getSpark();
		if (spark != null && spark.getMaster() != null && !requestTarget.isEmpty()) {
			List<ItemStack> stacks = CorporeaHelper.instance().requestItem(CorporeaHelper.instance().createMatcher(requestTarget, checkNBT), -1, spark, false).getStacks();
			for (ItemStack stack : stacks) {
				sum += stack.getCount();
			}
		}

		setCount(sum);
	}

	private void setCount(int count) {
		int oldCount = this.itemCount;
		this.itemCount = count;
		if (this.itemCount != oldCount) {
			int oldCompValue = this.compValue;
			this.compValue = CorporeaHelper.instance().signalStrengthForRequestSize(itemCount);
			if (this.compValue != oldCompValue && this.world != null) {
				this.world.updateComparatorOutputLevel(this.pos, getBlockState().getBlock());
			}
			VanillaPacketDispatcher.dispatchTEToNearbyPlayers(this);
		}
	}

	@Override
	public void writePacketNBT(CompoundNBT tag) {
		super.writePacketNBT(tag);
		CompoundNBT cmp = new CompoundNBT();
		if (!requestTarget.isEmpty()) {
			cmp = requestTarget.write(cmp);
		}
		tag.put(TAG_REQUEST_TARGET, cmp);
		tag.putInt(TAG_ITEM_COUNT, itemCount);
		tag.putBoolean(TAG_LOCK, locked);
		tag.putBoolean(TAG_CHECK_NBT, checkNBT);
	}

	@Override
	public void readPacketNBT(CompoundNBT tag) {
		super.readPacketNBT(tag);
		CompoundNBT cmp = tag.getCompound(TAG_REQUEST_TARGET);
		requestTarget = ItemStack.read(cmp);
		requestTargetNoNBT = requestTarget.copy();
		requestTargetNoNBT.setTag(null);
		setCount(tag.getInt(TAG_ITEM_COUNT));
		locked = tag.getBoolean(TAG_LOCK);
		if (tag.contains(TAG_CHECK_NBT)) {
			checkNBT = tag.getBoolean(TAG_CHECK_NBT);
		} else {
			// Old crystal cubes will check NBT as they always did, new ones will by default not.
			checkNBT = true;
		}
	}

	public boolean checksNBT() {
		return checkNBT;
	}

	public boolean onUsedByWand(PlayerEntity player, ItemStack stack, World world, BlockPos pos, Direction side) {
		if (player == null || player.isSneaking()) {
			checkNBT = !checkNBT;
			updateCount();
			markDirty();
			if (!world.isRemote) {
				VanillaPacketDispatcher.dispatchTEToNearbyPlayers(this);
			}
		}
		return true;
	}

	@OnlyIn(Dist.CLIENT)
	public void renderHUD(MatrixStack ms, Minecraft mc) {
		String mode = I18n.format("botaniamisc.crystal_cube." + (checkNBT ? "check_nbt" : "ignore_nbt"));
		int x = mc.getMainWindow().getScaledWidth() / 2 - mc.fontRenderer.getStringWidth(mode) / 2;
		int y = mc.getMainWindow().getScaledHeight() / 2 + 25;
		mc.fontRenderer.drawStringWithShadow(ms, mode, x, y, TextFormatting.GRAY.getColor());
	}

	public int getComparatorValue() {
		return compValue;
	}

	@Override
	public void doCorporeaRequest(ICorporeaRequestMatcher request, int count, ICorporeaSpark spark) {
		if (!requestTarget.isEmpty()) {
			List<ItemStack> stacks = CorporeaHelper.instance().requestItem(request, count, spark, true).getStacks();
			spark.onItemsRequested(stacks);
			boolean did = false;
			int sum = 0;
			for (ItemStack reqStack : stacks) {
				ItemEntity item = new ItemEntity(world, pos.getX() + 0.5, pos.getY() + 1.5, pos.getZ() + 0.5, reqStack);
				world.addEntity(item);
				if (requestTarget.isItemEqual(reqStack)) {
					sum += reqStack.getCount();
					did = true;
				}
			}

			if (did) {
				setCount(getItemCount() - sum);
			}
		}
	}
}
