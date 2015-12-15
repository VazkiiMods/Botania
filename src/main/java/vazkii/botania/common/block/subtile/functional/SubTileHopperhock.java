/**
 * This class was created by <Vazkii>. It's distributed as
 * part of the Botania Mod. Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 * 
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 * 
 * File Created @ [Mar 20, 2014, 5:54:39 PM (GMT)]
 */
package vazkii.botania.common.block.subtile.functional;

import java.util.ArrayList;
import java.util.List;

import net.minecraft.block.Block;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.item.EntityItemFrame;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityChest;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.StatCollector;
import net.minecraftforge.common.util.ForgeDirection;

import org.lwjgl.opengl.GL11;

import vazkii.botania.api.lexicon.LexiconEntry;
import vazkii.botania.api.mana.IManaItem;
import vazkii.botania.api.subtile.RadiusDescriptor;
import vazkii.botania.api.subtile.SubTileFunctional;
import vazkii.botania.common.core.helper.InventoryHelper;
import vazkii.botania.common.lexicon.LexiconData;
import vazkii.botania.common.lib.LibMisc;

public class SubTileHopperhock extends SubTileFunctional {

	private static final String TAG_FILTER_TYPE = "filterType";
	private static final int RANGE_MANA = 10;
	private static final int RANGE = 6;

	private static final int RANGE_MANA_MINI = 2;
	private static final int RANGE_MINI = 1;

	int filterType = 0;

	@Override
	public void onUpdate() {
		super.onUpdate();

		if(redstoneSignal > 0 || supertile.getWorldObj().isRemote)
			return;

		boolean pulledAny = false;
		int range = getRange();

		int x = supertile.xCoord;
		int y = supertile.yCoord;
		int z = supertile.zCoord;

		List<EntityItem> items = supertile.getWorldObj().getEntitiesWithinAABB(EntityItem.class, AxisAlignedBB.getBoundingBox(x - range, y - range, z - range, x + range + 1, y + range + 1, z + range + 1));
		for(EntityItem item : items) {
			if(item.age < 60 || item.age >= 105 && item.age < 110 || item.isDead)
				continue;

			ItemStack stack = item.getEntityItem();

			IInventory invToPutItemIn = null;
			ForgeDirection sideToPutItemIn = ForgeDirection.UNKNOWN;
			boolean priorityInv = false;

			for(ForgeDirection dir : ForgeDirection.VALID_DIRECTIONS) {
				int x_ = x + dir.offsetX;
				int y_ = y + dir.offsetY;
				int z_ = z + dir.offsetZ;

				IInventory inv = InventoryHelper.getInventory(supertile.getWorldObj(), x_, y_, z_);
				if(inv != null) {
					List<ItemStack> filter = getFilterForInventory(inv, x_, y_, z_, true);
					boolean canAccept = canAcceptItem(stack, filter, filterType);
					int stackSize = InventoryHelper.testInventoryInsertion(inv, stack, dir);
					canAccept &= stackSize == stack.stackSize;

					if(canAccept) {
						boolean priority = !filter.isEmpty();

						setInv : {
							if(priorityInv && !priority)
								break setInv;

							invToPutItemIn = inv;
							priorityInv = priority;
							sideToPutItemIn = dir.getOpposite();
						}
					}
				}
			}

			if(invToPutItemIn != null) {
				InventoryHelper.insertItemIntoInventory(invToPutItemIn, stack.copy(), sideToPutItemIn, -1);
				invToPutItemIn.markDirty();
				item.setDead();
				pulledAny = true;
			}
		}

		if(pulledAny && mana > 1)
			mana--;
	}

	public boolean canAcceptItem(ItemStack stack, List<ItemStack> filter, int filterType) {
		if(stack == null)
			return false;

		if(filter.isEmpty())
			return true;

		switch(filterType) {
		case 0 : { // Accept items in frames only
			boolean anyFilter = false;
			for(ItemStack filterEntry : filter) {
				if(filterEntry == null)
					continue;
				anyFilter = true;

				boolean itemEqual = stack.getItem() == filterEntry.getItem();
				boolean damageEqual = stack.getItemDamage() == filterEntry.getItemDamage();
				boolean nbtEqual = ItemStack.areItemStackTagsEqual(filterEntry, stack);

				if(itemEqual && damageEqual && nbtEqual)
					return true;

				if(!stack.getHasSubtypes() && stack.isItemStackDamageable() && stack.getMaxStackSize() == 1 && itemEqual && nbtEqual)
					return true;

				if(stack.getItem() instanceof IManaItem && itemEqual)
					return true;
			}

			return !anyFilter;
		}
		case 1 : return !canAcceptItem(stack, filter, 0); // Accept items not in frames only
		default : return true; // Accept all items
		}
	}

	public List<ItemStack> getFilterForInventory(IInventory inv, int x, int y, int z, boolean recursiveForDoubleChests) {
		List<ItemStack> filter = new ArrayList();

		if(recursiveForDoubleChests) {
			TileEntity tileEntity = supertile.getWorldObj().getTileEntity(x, y, z);
			Block chest = supertile.getWorldObj().getBlock(x, y, z);

			if(tileEntity instanceof TileEntityChest)
				for(ForgeDirection dir : LibMisc.CARDINAL_DIRECTIONS)
					if(supertile.getWorldObj().getBlock(x + dir.offsetX, y, z + dir.offsetZ) == chest) {
						filter.addAll(getFilterForInventory((IInventory) supertile.getWorldObj().getTileEntity(x + dir.offsetX, y, z + dir.offsetZ), x + dir.offsetX, y, z + dir.offsetZ, false));
						break;
					}
		}

		final int[] orientationToDir = new int[] {
				3, 4, 2, 5
		};

		for(ForgeDirection dir : LibMisc.CARDINAL_DIRECTIONS) {
			AxisAlignedBB aabb = AxisAlignedBB.getBoundingBox(x + dir.offsetX, y + dir.offsetY, z + dir.offsetZ, x + dir.offsetX + 1, y + dir.offsetY + 1, z + dir.offsetZ + 1);
			List<EntityItemFrame> frames = supertile.getWorldObj().getEntitiesWithinAABB(EntityItemFrame.class, aabb);
			for(EntityItemFrame frame : frames) {
				int orientation = frame.hangingDirection;
				if(orientationToDir[orientation] == dir.ordinal())
					filter.add(frame.getDisplayedItem());
			}
		}

		return filter;
	}

	@Override
	public boolean acceptsRedstone() {
		return true;
	}

	@Override
	public boolean onWanded(EntityPlayer player, ItemStack wand) {
		if(player == null)
			return false;

		if(player.isSneaking()) {
			filterType = filterType == 2 ? 0 : filterType + 1;
			sync();

			return true;
		}
		else return super.onWanded(player, wand);
	}

	@Override
	public RadiusDescriptor getRadius() {
		return new RadiusDescriptor.Square(toChunkCoordinates(), getRange());
	}

	public int getRange() {
		return mana > 0 ? RANGE_MANA : RANGE;
	}

	@Override
	public void writeToPacketNBT(NBTTagCompound cmp) {
		super.writeToPacketNBT(cmp);

		cmp.setInteger(TAG_FILTER_TYPE, filterType);
	}

	@Override
	public void readFromPacketNBT(NBTTagCompound cmp) {
		super.readFromPacketNBT(cmp);

		filterType = cmp.getInteger(TAG_FILTER_TYPE);
	}

	@Override
	public void renderHUD(Minecraft mc, ScaledResolution res) {
		super.renderHUD(mc, res);

		int color = getColor();
		String filter = StatCollector.translateToLocal("botaniamisc.filter" + filterType);
		GL11.glEnable(GL11.GL_BLEND);
		GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
		int x = res.getScaledWidth() / 2 - mc.fontRenderer.getStringWidth(filter) / 2;
		int y = res.getScaledHeight() / 2 + 30;

		mc.fontRenderer.drawStringWithShadow(filter, x, y, color);
		GL11.glDisable(GL11.GL_BLEND);
	}

	@Override
	public LexiconEntry getEntry() {
		return LexiconData.hopperhock;
	}

	@Override
	public int getMaxMana() {
		return 20;
	}

	@Override
	public int getColor() {
		return 0x3F3F3F;
	}

	public static class Mini extends SubTileHopperhock {
		@Override public int getRange() { return mana > 0 ? RANGE_MANA_MINI : RANGE_MINI; }
	}
}
