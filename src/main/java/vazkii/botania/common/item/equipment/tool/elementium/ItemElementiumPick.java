/*
 * This class is distributed as part of the Botania Mod.
 * Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 *
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 */
package vazkii.botania.common.item.equipment.tool.elementium;

import net.minecraft.block.Block;
import net.minecraft.entity.Entity;
import net.minecraft.item.ItemStack;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.world.BlockEvent.HarvestDropsEvent;

import vazkii.botania.api.BotaniaAPI;
import vazkii.botania.common.item.ModItems;
import vazkii.botania.common.item.equipment.tool.manasteel.ItemManasteelPick;
import vazkii.botania.common.item.equipment.tool.terrasteel.ItemTerraPick;
import vazkii.botania.common.lib.ModTags;

import java.util.List;

public class ItemElementiumPick extends ItemManasteelPick {

	public ItemElementiumPick(Properties props) {
		super(BotaniaAPI.ELEMENTIUM_ITEM_TIER, props, -2.8F);
	}

	public static void filterDisposable(List<ItemStack> drops, Entity e, ItemStack stack) {
		if (!stack.isEmpty() && (stack.getItem() == ModItems.elementiumPick
				|| stack.getItem() == ModItems.terraPick && ItemTerraPick.isTipped(stack))) {
			drops.removeIf(s -> !s.isEmpty() && (isDisposable(s) || isSemiDisposable(s) && !e.isSneaking()));
		}
	}

	public static boolean isDisposable(Block block) {
		return ModTags.Items.DISPOSABLE.contains(block.asItem());
	}

	private static boolean isDisposable(ItemStack stack) {
		if (stack.isEmpty()) {
			return false;
		}

		return ModTags.Items.DISPOSABLE.contains(stack.getItem());
	}

	private static boolean isSemiDisposable(ItemStack stack) {
		return ModTags.Items.SEMI_DISPOSABLE.contains(stack.getItem());
	}
}
