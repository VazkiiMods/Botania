/**
 * This class was created by <Vazkii>. It's distributed as
 * part of the Botania Mod. Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 *
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 *
 * File Created @ [Mar 31, 2015, 9:11:23 PM (GMT)]
 */
package vazkii.botania.common.item;

import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.entity.player.PlayerDropsEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.PlayerEvent.PlayerRespawnEvent;
import vazkii.botania.common.core.helper.ItemNBTHelper;
import vazkii.botania.common.lib.LibItemNames;

import java.util.ArrayList;
import java.util.List;

public class ItemKeepIvy extends ItemMod {

	public static final String TAG_KEEP = "Botania_keepIvy";

	private static final String TAG_PLAYER_KEPT_DROPS = "Botania_playerKeptDrops";
	private static final String TAG_DROP_COUNT = "dropCount";
	private static final String TAG_DROP_PREFIX = "dropPrefix";

	public ItemKeepIvy() {
		super(LibItemNames.KEEP_IVY);
		MinecraftForge.EVENT_BUS.register(ItemKeepIvy.class);
	}

	@SubscribeEvent
	public static void onPlayerDrops(PlayerDropsEvent event) {
		List<EntityItem> keeps = new ArrayList<>();
		for(EntityItem item : event.getDrops()) {
			ItemStack stack = item.getItem();
			if(!stack.isEmpty() && stack.hasTagCompound() && ItemNBTHelper.getBoolean(stack, TAG_KEEP, false))
				keeps.add(item);
		}

		if(keeps.size() > 0) {
			event.getDrops().removeAll(keeps);

			NBTTagCompound cmp = new NBTTagCompound();
			cmp.setInteger(TAG_DROP_COUNT, keeps.size());

			int i = 0;
			for(EntityItem keep : keeps) {
				ItemStack stack = keep.getItem();
				NBTTagCompound cmp1 = stack.writeToNBT(new NBTTagCompound());
				cmp.setTag(TAG_DROP_PREFIX + i, cmp1);
				i++;
			}

			NBTTagCompound data = event.getEntityPlayer().getEntityData();
			if(!data.hasKey(EntityPlayer.PERSISTED_NBT_TAG))
				data.setTag(EntityPlayer.PERSISTED_NBT_TAG, new NBTTagCompound());

			NBTTagCompound persist = data.getCompoundTag(EntityPlayer.PERSISTED_NBT_TAG);
			persist.setTag(TAG_PLAYER_KEPT_DROPS, cmp);
		}
	}

	@SubscribeEvent
	public static void onPlayerRespawn(PlayerRespawnEvent event) {
		NBTTagCompound data = event.player.getEntityData();
		if(data.hasKey(EntityPlayer.PERSISTED_NBT_TAG)) {
			NBTTagCompound cmp = data.getCompoundTag(EntityPlayer.PERSISTED_NBT_TAG);
			NBTTagCompound cmp1 = cmp.getCompoundTag(TAG_PLAYER_KEPT_DROPS);

			int count = cmp1.getInteger(TAG_DROP_COUNT);
			for(int i = 0; i < count; i++) {
				NBTTagCompound cmp2 = cmp1.getCompoundTag(TAG_DROP_PREFIX + i);
				ItemStack stack = new ItemStack(cmp2);
				if(!stack.isEmpty()) {
					ItemStack copy = stack.copy();
					ItemNBTHelper.setBoolean(copy, TAG_KEEP, false);
					event.player.inventory.addItemStackToInventory(copy);
				}
			}

			cmp.setTag(TAG_PLAYER_KEPT_DROPS, new NBTTagCompound());
		}
	}

}
