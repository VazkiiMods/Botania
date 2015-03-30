/**
 * This class was created by <Vazkii>. It's distributed as
 * part of the Botania Mod. Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 * 
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 * 
 * File Created @ [Mar 29, 2015, 10:13:32 PM (GMT)]
 */
package vazkii.botania.common.item.relic;

import java.util.ArrayList;
import java.util.List;

import net.minecraft.client.Minecraft;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.ChunkCoordinates;
import net.minecraft.util.MovingObjectPosition;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.event.entity.player.PlayerInteractEvent.Action;
import vazkii.botania.api.item.IWireframeCoordinateListProvider;
import vazkii.botania.common.core.helper.ItemNBTHelper;
import vazkii.botania.common.item.equipment.tool.ToolCommons;
import vazkii.botania.common.lib.LibItemNames;
import baubles.api.BaubleType;
import baubles.common.container.InventoryBaubles;
import baubles.common.lib.PlayerHandler;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class ItemLokiRing extends ItemRelicBauble implements IWireframeCoordinateListProvider {

	private static final String TAG_CURSOR_LIST = "cursorList";
	private static final String TAG_CURSOR_PREFIX = "cursor";
	private static final String TAG_CURSOR_COUNT = "cursorCount";
	private static final String TAG_X_OFFSET = "xOffset";
	private static final String TAG_Y_OFFSET = "yOffset";
	private static final String TAG_Z_OFFSET = "zOffset";
	private static final String TAG_X_ORIGIN = "xOrigin";
	private static final String TAG_Y_ORIGIN = "yOrigin";
	private static final String TAG_Z_ORIGIN = "zOrigin";

	public ItemLokiRing() {
		super(LibItemNames.LOKI_RING);
		MinecraftForge.EVENT_BUS.register(this);
	}
	
	@SubscribeEvent
	public void onPlayerInteract(PlayerInteractEvent event) {
		EntityPlayer player = event.entityPlayer;
		ItemStack lokiRing = getLokiRing(player);
		if(lokiRing == null || player.worldObj.isRemote)
			return;
		
		ItemStack heldItemStack = player.getCurrentEquippedItem();
		ChunkCoordinates originCoords = getOriginPos(lokiRing);
		MovingObjectPosition lookPos = ToolCommons.raytraceFromEntity(player.worldObj, player, true, 4.5F);
		
		if(heldItemStack == null && event.action == Action.RIGHT_CLICK_BLOCK && player.isSneaking()) {
			if(originCoords.posY == -1 && lookPos != null) {
				setOriginPos(lokiRing, lookPos.blockX, lookPos.blockY, lookPos.blockZ);
				setCursorList(lokiRing, null);
			} else if(lookPos != null) {
				if(originCoords.posX == lookPos.blockX && originCoords.posY == lookPos.blockY && originCoords.posZ == lookPos.blockZ)
					setOriginPos(lokiRing, 0, -1, 0);
				else {
					List<ChunkCoordinates> cursors = getCursorList(lokiRing);
					addCursor : {
						for(ChunkCoordinates cursor : cursors)
							if(cursor.posX == lookPos.blockX && cursor.posY == lookPos.blockY && cursor.posZ == lookPos.blockZ)
								break addCursor;
						
						addCursor(lokiRing, lookPos.blockX - originCoords.posX, lookPos.blockY - originCoords.posY, lookPos.blockZ - originCoords.posZ);
					}
				}
			}
		} else if(heldItemStack != null && event.action == Action.RIGHT_CLICK_BLOCK && lookPos != null) {
			List<ChunkCoordinates> cursors = getCursorList(lokiRing);
			for(ChunkCoordinates cursor : cursors) {
				int x = lookPos.blockX + cursor.posX;
				int y = lookPos.blockY + cursor.posY;
				int z = lookPos.blockZ + cursor.posZ;
				Item item = heldItemStack.getItem();
				item.onItemUse(player.capabilities.isCreativeMode ? heldItemStack.copy() : heldItemStack, player, player.worldObj, x, y, z, lookPos.sideHit, (float) lookPos.hitVec.xCoord - x, (float) lookPos.hitVec.yCoord - y, (float) lookPos.hitVec.zCoord - z);
			}
		}
	}
	
	@Override
	public BaubleType getBaubleType(ItemStack arg0) {
		return BaubleType.RING;
	}

	@Override
	public void onUnequipped(ItemStack stack, EntityLivingBase player) {
		setCursorList(stack, null);
	}
	
	@Override
	@SideOnly(Side.CLIENT)
	public List<ChunkCoordinates> getWireframesToDraw(EntityPlayer player, ItemStack stack) {
		MovingObjectPosition lookPos = Minecraft.getMinecraft().objectMouseOver;

		if(lookPos != null && !player.worldObj.isAirBlock(lookPos.blockX, lookPos.blockY, lookPos.blockZ) && lookPos.entityHit == null) {
			List<ChunkCoordinates> list = getCursorList(stack);
			ChunkCoordinates origin = getOriginPos(stack);
						
			if(origin.posY != -1) {
				for(ChunkCoordinates coords : list) {
					coords.posX += origin.posX;
					coords.posY += origin.posY;
					coords.posZ += origin.posZ;
				}
				list.add(origin);
			} else for(ChunkCoordinates coords : list) {
				coords.posX += lookPos.blockX;
				coords.posY += lookPos.blockY;
				coords.posZ += lookPos.blockZ;
			}
			
			return list;
		}
		
		return null;
	}
	
	private ItemStack getLokiRing(EntityPlayer player) {
		InventoryBaubles baubles = PlayerHandler.getPlayerBaubles(player);
		ItemStack stack1 = baubles.getStackInSlot(1);
		ItemStack stack2 = baubles.getStackInSlot(2);
		return (stack1 != null && stack1.getItem() == this) ? stack1 : (stack2 != null && stack2.getItem() == this) ? stack2 : null;
	}
	
	private ChunkCoordinates getOriginPos(ItemStack stack) {
		int x = ItemNBTHelper.getInt(stack, TAG_X_ORIGIN, 0);
		int y = ItemNBTHelper.getInt(stack, TAG_Y_ORIGIN, -1);
		int z = ItemNBTHelper.getInt(stack, TAG_Z_ORIGIN, 0);
		return new ChunkCoordinates(x, y, z);
	}
	
	private void setOriginPos(ItemStack stack, int x, int y, int z) {
		ItemNBTHelper.setInt(stack, TAG_X_ORIGIN, x);
		ItemNBTHelper.setInt(stack, TAG_Y_ORIGIN, y);
		ItemNBTHelper.setInt(stack, TAG_Z_ORIGIN, z);
	}
	
	private List<ChunkCoordinates> getCursorList(ItemStack stack) {
		NBTTagCompound cmp = ItemNBTHelper.getCompound(stack, TAG_CURSOR_LIST, false);
		List<ChunkCoordinates> cursors = new ArrayList();
		
		int count = cmp.getInteger(TAG_CURSOR_COUNT);
		for(int i = 0; i < count; i++) {
			NBTTagCompound cursorCmp = cmp.getCompoundTag(TAG_CURSOR_PREFIX + i);
			int x = cursorCmp.getInteger(TAG_X_OFFSET);
			int y = cursorCmp.getInteger(TAG_Y_OFFSET);
			int z = cursorCmp.getInteger(TAG_Z_OFFSET);
			cursors.add(new ChunkCoordinates(x, y, z));
		}
		
		return cursors;
	}

	private void setCursorList(ItemStack stack, List<ChunkCoordinates> cursors) {
		NBTTagCompound cmp = new NBTTagCompound();
		if(cursors != null) {
			int i = 0;
			for(ChunkCoordinates cursor : cursors) {
				NBTTagCompound cursorCmp = cursorToCmp(cursor.posX, cursor.posY, cursor.posZ);
				cmp.setTag(TAG_CURSOR_PREFIX + i, cursorCmp);
				i++;
			}
			cmp.setInteger(TAG_CURSOR_COUNT, i);
		}
		
		ItemNBTHelper.setCompound(stack, TAG_CURSOR_LIST, cmp);
	}
	
	private NBTTagCompound cursorToCmp(int x, int y, int z) {
		NBTTagCompound cmp = new NBTTagCompound();
		cmp.setInteger(TAG_X_OFFSET, x);
		cmp.setInteger(TAG_Y_OFFSET, y);
		cmp.setInteger(TAG_Z_OFFSET, z);
		return cmp;
	}
	
	private void addCursor(ItemStack stack, int x, int y, int z) {
		NBTTagCompound cmp = ItemNBTHelper.getCompound(stack, TAG_CURSOR_LIST, false);
		int count = cmp.getInteger(TAG_CURSOR_COUNT);
		cmp.setTag(TAG_CURSOR_PREFIX + count, cursorToCmp(x, y, z));
		cmp.setInteger(TAG_CURSOR_COUNT, count + 1);
		ItemNBTHelper.setCompound(stack, TAG_CURSOR_LIST, cmp);
	}

}

