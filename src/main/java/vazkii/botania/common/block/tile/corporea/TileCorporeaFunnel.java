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
import net.minecraft.entity.item.ItemFrameEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.Direction;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.text.TextFormatting;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.ItemHandlerHelper;

import vazkii.botania.api.corporea.CorporeaHelper;
import vazkii.botania.api.corporea.ICorporeaRequestMatcher;
import vazkii.botania.api.corporea.ICorporeaRequestor;
import vazkii.botania.api.corporea.ICorporeaSpark;
import vazkii.botania.api.internal.VanillaPacketDispatcher;
import vazkii.botania.common.block.tile.ModTiles;
import vazkii.botania.common.core.helper.InventoryHelper;

import java.util.ArrayList;
import java.util.List;

public class TileCorporeaFunnel extends TileCorporeaBase implements ICorporeaRequestor {
	private static final String TAG_CHECK_NBT = "checkNBT";

	private boolean checkNBT = false;

	public TileCorporeaFunnel() {
		super(ModTiles.CORPOREA_FUNNEL);
	}

	public void doRequest() {
		ICorporeaSpark spark = getSpark();
		if (spark != null && spark.getMaster() != null) {
			List<ItemStack> filter = getFilter();
			if (!filter.isEmpty()) {
				ItemStack stack = filter.get(world.rand.nextInt(filter.size()));

				if (!stack.isEmpty()) {
					doCorporeaRequest(CorporeaHelper.instance().createMatcher(stack, checkNBT), stack.getCount(), spark);
				}
			}
		}
	}

	public List<ItemStack> getFilter() {
		List<ItemStack> filter = new ArrayList<>();

		final int[] rotationToStackSize = new int[] {
				1, 2, 4, 8, 16, 32, 48, 64
		};

		for (Direction dir : Direction.values()) {
			List<ItemFrameEntity> frames = world.getEntitiesWithinAABB(ItemFrameEntity.class, new AxisAlignedBB(pos.offset(dir), pos.offset(dir).add(1, 1, 1)));
			for (ItemFrameEntity frame : frames) {
				Direction orientation = frame.getHorizontalFacing();
				if (orientation == dir) {
					ItemStack stack = frame.getDisplayedItem();
					if (!stack.isEmpty()) {
						ItemStack copy = stack.copy();
						copy.setCount(rotationToStackSize[frame.getRotation()]);
						filter.add(copy);
					}
				}
			}
		}

		return filter;
	}

	public boolean checksNBT() {
		return checkNBT;
	}

	@Override
	public void readPacketNBT(CompoundNBT cmp) {
		super.readPacketNBT(cmp);
		if (cmp.contains(TAG_CHECK_NBT)) {
			checkNBT = cmp.getBoolean(TAG_CHECK_NBT);
		} else {
			// Old funnels will check NBT as they always did, new ones will by default not.
			checkNBT = true;
		}
	}

	@Override
	public void writePacketNBT(CompoundNBT cmp) {
		super.writePacketNBT(cmp);
		cmp.putBoolean(TAG_CHECK_NBT, checkNBT);
	}

	@Override
	public void doCorporeaRequest(ICorporeaRequestMatcher request, int count, ICorporeaSpark spark) {
		IItemHandler inv = getInv();

		List<ItemStack> stacks = CorporeaHelper.instance().requestItem(request, count, spark, true).getStacks();
		spark.onItemsRequested(stacks);
		for (ItemStack reqStack : stacks) {
			if (inv != null && ItemHandlerHelper.insertItemStacked(inv, reqStack, true).isEmpty()) {
				ItemHandlerHelper.insertItemStacked(inv, reqStack, false);
			} else {
				ItemEntity item = new ItemEntity(world, pos.getX() + 0.5, pos.getY() + 1.5, pos.getZ() + 0.5, reqStack);
				world.addEntity(item);
			}
		}
	}

	private IItemHandler getInv() {
		TileEntity te = world.getTileEntity(pos.down());
		IItemHandler ret = InventoryHelper.getInventory(world, pos.down(), Direction.UP);
		if (ret == null) {
			ret = InventoryHelper.getInventory(world, pos.down(), null);
		}
		if (ret != null && !(te instanceof TileCorporeaFunnel)) {
			return ret;
		}

		te = world.getTileEntity(pos.down(2));
		ret = InventoryHelper.getInventory(world, pos.down(2), Direction.UP);
		if (ret == null) {
			ret = InventoryHelper.getInventory(world, pos.down(2), null);
		}
		if (ret != null && !(te instanceof TileCorporeaFunnel)) {
			return ret;
		}

		return null;
	}

	@OnlyIn(Dist.CLIENT)
	public void renderHUD(MatrixStack ms, Minecraft mc) {
		String mode = I18n.format("botaniamisc.funnel." + (checkNBT ? "check_nbt" : "ignore_nbt"));
		int x = mc.getMainWindow().getScaledWidth() / 2 - mc.fontRenderer.getStringWidth(mode) / 2;
		int y = mc.getMainWindow().getScaledHeight() / 2 + 10;
		mc.fontRenderer.drawStringWithShadow(ms, mode, x, y, TextFormatting.GRAY.getColor());
	}

	public boolean onUsedByWand() {
		if (!world.isRemote) {
			checkNBT = !checkNBT;
			markDirty();
			VanillaPacketDispatcher.dispatchTEToNearbyPlayers(this);
		}
		return true;
	}
}
