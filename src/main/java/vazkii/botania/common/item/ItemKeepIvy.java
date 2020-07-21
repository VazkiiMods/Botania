/*
 * This class is distributed as part of the Botania Mod.
 * Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 *
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 */
package vazkii.botania.common.item;

import net.minecraft.entity.ItemEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundTag;
import net.minecraftforge.event.entity.living.LivingDropsEvent;
import net.minecraftforge.event.entity.player.PlayerEvent;

import vazkii.botania.common.core.helper.ItemNBTHelper;

import java.util.ArrayList;
import java.util.List;

public class ItemKeepIvy extends Item {

	public static final String TAG_KEEP = "Botania_keepIvy";

	private static final String TAG_PLAYER_KEPT_DROPS = "Botania_playerKeptDrops";
	private static final String TAG_DROP_COUNT = "dropCount";
	private static final String TAG_DROP_PREFIX = "dropPrefix";

	public ItemKeepIvy(Settings props) {
		super(props);
	}

	public static boolean hasIvy(ItemStack stack) {
		return !stack.isEmpty() && stack.hasTag() && ItemNBTHelper.getBoolean(stack, TAG_KEEP, false);
	}

	// Curios are handled in CurioIntegration#keepCurioDrops
	public static void onPlayerDrops(LivingDropsEvent event) {
		if (!(event.getEntityLiving() instanceof PlayerEntity)) {
			return;
		}

		List<ItemEntity> keeps = new ArrayList<>();
		for (ItemEntity item : event.getDrops()) {
			ItemStack stack = item.getStack();
			if (!stack.isEmpty() && stack.hasTag() && ItemNBTHelper.getBoolean(stack, TAG_KEEP, false)) {
				keeps.add(item);
			}
		}

		if (keeps.size() > 0) {
			event.getDrops().removeAll(keeps);

			CompoundTag cmp = new CompoundTag();
			cmp.putInt(TAG_DROP_COUNT, keeps.size());

			int i = 0;
			for (ItemEntity keep : keeps) {
				ItemStack stack = keep.getStack();
				CompoundTag cmp1 = stack.toTag(new CompoundTag());
				cmp.put(TAG_DROP_PREFIX + i, cmp1);
				i++;
			}

			CompoundTag data = event.getEntityLiving().getPersistentData();
			if (!data.contains(PlayerEntity.PERSISTED_NBT_TAG)) {
				data.put(PlayerEntity.PERSISTED_NBT_TAG, new CompoundTag());
			}

			CompoundTag persist = data.getCompound(PlayerEntity.PERSISTED_NBT_TAG);
			persist.put(TAG_PLAYER_KEPT_DROPS, cmp);
		}
	}

	public static void onPlayerRespawn(PlayerEvent.PlayerRespawnEvent event) {
		CompoundTag data = event.getPlayer().getPersistentData();
		if (data.contains(PlayerEntity.PERSISTED_NBT_TAG)) {
			CompoundTag cmp = data.getCompound(PlayerEntity.PERSISTED_NBT_TAG);
			CompoundTag cmp1 = cmp.getCompound(TAG_PLAYER_KEPT_DROPS);

			int count = cmp1.getInt(TAG_DROP_COUNT);
			for (int i = 0; i < count; i++) {
				CompoundTag cmp2 = cmp1.getCompound(TAG_DROP_PREFIX + i);
				ItemStack stack = ItemStack.fromTag(cmp2);
				if (!stack.isEmpty()) {
					ItemStack copy = stack.copy();
					stack.removeSubTag(TAG_KEEP);
					if (!event.getPlayer().inventory.insertStack(copy)) {
						event.getPlayer().dropStack(copy);
					}
				}
			}

			cmp.remove(TAG_PLAYER_KEPT_DROPS);
		}
	}

}
