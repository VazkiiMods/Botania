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

import net.minecraft.block.Block;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.resources.I18n;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.item.EntityItemFrame;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityChest;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.ItemHandlerHelper;
import org.lwjgl.opengl.GL11;
import vazkii.botania.api.corporea.InvWithLocation;
import vazkii.botania.api.lexicon.LexiconEntry;
import vazkii.botania.api.mana.IManaItem;
import vazkii.botania.api.subtile.RadiusDescriptor;
import vazkii.botania.api.subtile.SubTileFunctional;
import vazkii.botania.common.core.helper.InventoryHelper;
import vazkii.botania.common.lexicon.LexiconData;

import java.util.ArrayList;
import java.util.List;

public class SubTileHopperhock extends SubTileFunctional {

	private static final String TAG_FILTER_TYPE = "filterType";
	private static final int RANGE_MANA = 10;
	private static final int RANGE = 6;

	private static final int RANGE_MANA_MINI = 2;
	private static final int RANGE_MINI = 1;

	private int filterType = 0;

	@Override
	public void onUpdate() {
		super.onUpdate();

		if(supertile.getWorld().isRemote || redstoneSignal > 0)
			return;

		boolean pulledAny = false;
		int range = getRange();

		BlockPos pos = supertile.getPos();

		List<EntityItem> items = supertile.getWorld().getEntitiesWithinAABB(EntityItem.class, new AxisAlignedBB(pos.add(-range, -range, -range), pos.add(range + 1, range + 1, range + 1)));
		int slowdown = getSlowdownFactor();

		for(EntityItem item : items) {
			if(item.age < 60 + slowdown || item.age >= 105 && item.age < 110 || item.isDead || item.getItem().isEmpty())
				continue;

			ItemStack stack = item.getItem();
			IItemHandler invToPutItemIn = null;
			boolean priorityInv = false;
			int amountToPutIn = 0;

			for(EnumFacing dir : EnumFacing.VALUES) {
				BlockPos pos_ = pos.offset(dir);

				InvWithLocation inv = InventoryHelper.getInventoryWithLocation(supertile.getWorld(), pos_, dir.getOpposite());
				if(inv != null) {
					List<ItemStack> filter = getFilterForInventory(pos_, true);
					boolean canAccept = canAcceptItem(stack, filter, filterType);

					ItemStack simulate = ItemHandlerHelper.insertItem(inv.handler, stack, true);
					int availablePut = stack.getCount() - simulate.getCount();

					canAccept &= availablePut > 0;

					if(canAccept) {
						boolean priority = !filter.isEmpty();

						setInv : {
							if(priorityInv && !priority)
								break setInv;

							invToPutItemIn = inv.handler;
							priorityInv = priority;
							amountToPutIn = availablePut;
						}
					}
				}
			}

			if(invToPutItemIn != null && !item.isDead) {
				SubTileSpectranthemum.spawnExplosionParticles(item, 3);
				ItemHandlerHelper.insertItem(invToPutItemIn, stack.splitStack(amountToPutIn), false);
				item.setItem(stack); // Just in case someone subclasses EntityItem and changes something important.
				pulledAny = true;
			}
		}

		if(pulledAny && mana > 0)
			mana--;
	}

	public boolean canAcceptItem(ItemStack stack, List<ItemStack> filter, int filterType) {
		if(stack.isEmpty())
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

	public List<ItemStack> getFilterForInventory(BlockPos pos, boolean recursiveForDoubleChests) {
		List<ItemStack> filter = new ArrayList<>();

		if(recursiveForDoubleChests) {
			TileEntity tileEntity = supertile.getWorld().getTileEntity(pos);
			Block chest = supertile.getWorld().getBlockState(pos).getBlock();

			if(tileEntity instanceof TileEntityChest)
				for(EnumFacing dir : EnumFacing.HORIZONTALS)
					if(supertile.getWorld().getBlockState(pos.offset(dir)).getBlock() == chest) {
						filter.addAll(getFilterForInventory(pos.offset(dir), false));
						break;
					}
		}

		for(EnumFacing dir : EnumFacing.HORIZONTALS) {
			AxisAlignedBB aabb = new AxisAlignedBB(pos.offset(dir), pos.offset(dir).add(1, 1, 1));
			List<EntityItemFrame> frames = supertile.getWorld().getEntitiesWithinAABB(EntityItemFrame.class, aabb);
			for(EntityItemFrame frame : frames) {
				if(frame.facingDirection == dir)
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
		return new RadiusDescriptor.Square(toBlockPos(), getRange());
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

	@SideOnly(Side.CLIENT)
	@Override
	public void renderHUD(Minecraft mc, ScaledResolution res) {
		super.renderHUD(mc, res);

		int color = getColor();
		String filter = I18n.format("botaniamisc.filter" + filterType);
		GlStateManager.enableBlend();
		GlStateManager.blendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
		int x = res.getScaledWidth() / 2 - mc.fontRenderer.getStringWidth(filter) / 2;
		int y = res.getScaledHeight() / 2 + 30;

		mc.fontRenderer.drawStringWithShadow(filter, x, y, color);
		GlStateManager.disableBlend();
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
