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
import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.WeakHashMap;

import net.minecraft.block.Block;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.item.EntityItemFrame;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityChest;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.StatCollector;

import net.minecraftforge.fml.common.ObfuscationReflectionHelper;
import org.lwjgl.opengl.GL11;

import vazkii.botania.api.lexicon.LexiconEntry;
import vazkii.botania.api.mana.IManaItem;
import vazkii.botania.api.subtile.RadiusDescriptor;
import vazkii.botania.api.subtile.SubTileFunctional;
import vazkii.botania.common.core.helper.InventoryHelper;
import vazkii.botania.common.lexicon.LexiconData;
import vazkii.botania.common.lib.LibMisc;
import vazkii.botania.common.lib.LibObfuscation;

public class SubTileHopperhock extends SubTileFunctional {

	private static final String TAG_FILTER_TYPE = "filterType";
	private static final int RANGE_MANA = 10;
	private static final int RANGE = 6;

	private static final int RANGE_MANA_MINI = 2;
	private static final int RANGE_MINI = 1;

	private static Set<EntityItem> particled = Collections.newSetFromMap(new WeakHashMap());

	int filterType = 0;

	@Override
	public void onUpdate() {
		super.onUpdate();

		if(redstoneSignal > 0)
			return;

		boolean pulledAny = false;
		int range = getRange();

		BlockPos pos = supertile.getPos();

		List<EntityItem> items = supertile.getWorld().getEntitiesWithinAABB(EntityItem.class, new AxisAlignedBB(pos.add(-range, -range, -range), pos.add(range + 1, range + 1, range + 1)));
		int slowdown = getSlowdownFactor();
		
		for(EntityItem item : items) {
			int age = ObfuscationReflectionHelper.getPrivateValue(EntityItem.class, item, LibObfuscation.AGE);
			if(age < (60 + slowdown) || age >= 105 && age < 110 || item.isDead)
				continue;

			ItemStack stack = item.getEntityItem();

			IInventory invToPutItemIn = null;
			EnumFacing sideToPutItemIn = null;
			boolean priorityInv = false;
			int amountToPutIn = 0;

			for(EnumFacing dir : EnumFacing.VALUES) {
				BlockPos pos_ = pos.offset(dir);

				IInventory inv = InventoryHelper.getInventory(supertile.getWorld(), pos_, dir);
				if(inv != null) {
					List<ItemStack> filter = getFilterForInventory(inv, pos_, true);
					boolean canAccept = canAcceptItem(stack, filter, filterType);
					int availablePut = supertile.getWorld().isRemote ? 1 : InventoryHelper.testInventoryInsertion(inv, stack, dir);
					canAccept &= availablePut > 0;

					if(canAccept) {
						boolean priority = !filter.isEmpty();

						setInv : {
							if(priorityInv && !priority)
								break setInv;

							invToPutItemIn = inv;
							priorityInv = priority;
							sideToPutItemIn = dir.getOpposite();
							amountToPutIn = availablePut;
						}
					}
				}
			}

			if(invToPutItemIn != null && !item.isDead) {
				boolean remote = supertile.getWorld().isRemote;
				if(remote) {
					if(!particled.contains(item)) {
						SubTileSpectranthemum.spawnExplosionParticles(item, 3);
						particled.add(item);
					}
				} else {
					InventoryHelper.insertItemIntoInventory(invToPutItemIn, stack.splitStack(amountToPutIn), sideToPutItemIn, -1);
					item.setEntityItemStack(stack); // Just in case someone subclasses EntityItem and changes something important.
					invToPutItemIn.markDirty();
					if(item.getEntityItem().stackSize == 0)
						item.setDead();
					pulledAny = true;
				}
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

	public List<ItemStack> getFilterForInventory(IInventory inv, BlockPos pos, boolean recursiveForDoubleChests) {
		List<ItemStack> filter = new ArrayList();

		if(recursiveForDoubleChests) {
			TileEntity tileEntity = supertile.getWorld().getTileEntity(pos);
			Block chest = supertile.getWorld().getBlockState(pos).getBlock();

			if(tileEntity instanceof TileEntityChest)
				for(EnumFacing dir : LibMisc.CARDINAL_DIRECTIONS)
					if(supertile.getWorld().getBlockState(pos.offset(dir)).getBlock() == chest) {
						filter.addAll(getFilterForInventory((IInventory) supertile.getWorld().getTileEntity(pos.offset(dir)), pos.offset(dir), false));
						break;
					}
		}

		for(EnumFacing dir : LibMisc.CARDINAL_DIRECTIONS) {
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

	@Override
	public void renderHUD(Minecraft mc, ScaledResolution res) {
		super.renderHUD(mc, res);

		int color = getColor();
		String filter = StatCollector.translateToLocal("botaniamisc.filter" + filterType);
		GlStateManager.enableBlend();
		GlStateManager.blendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
		int x = res.getScaledWidth() / 2 - mc.fontRendererObj.getStringWidth(filter) / 2;
		int y = res.getScaledHeight() / 2 + 30;

		mc.fontRendererObj.drawStringWithShadow(filter, x, y, color);
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
