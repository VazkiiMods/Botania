/*
 * This class is distributed as part of the Botania Mod.
 * Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 *
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 */
package vazkii.botania.common.item.equipment.tool.elementium;

import net.minecraft.world.entity.Entity;
import net.minecraft.world.item.ItemStack;

import vazkii.botania.api.BotaniaAPI;
import vazkii.botania.common.item.ModItems;
import vazkii.botania.common.item.equipment.tool.manasteel.ManasteelPickaxeItem;
import vazkii.botania.common.item.equipment.tool.terrasteel.TerraShattererItem;
import vazkii.botania.common.lib.ModTags;

public class ElementiumPickaxeItem extends ManasteelPickaxeItem {

	public ElementiumPickaxeItem(Properties props) {
		super(BotaniaAPI.instance().getElementiumItemTier(), props, -2.8F);
	}

	public static boolean shouldFilterOut(Entity e, ItemStack tool, ItemStack drop) {
		if (!tool.isEmpty() && (tool.is(ModItems.elementiumPick)
				|| tool.is(ModItems.terraPick) && TerraShattererItem.isTipped(tool))) {
			return !drop.isEmpty() && (isDisposable(drop) || isSemiDisposable(drop) && !e.isShiftKeyDown());
		}
		return false;
	}

	private static boolean isDisposable(ItemStack stack) {
		if (stack.isEmpty()) {
			return false;
		}

		return stack.is(ModTags.Items.DISPOSABLE);
	}

	private static boolean isSemiDisposable(ItemStack stack) {
		return stack.is(ModTags.Items.SEMI_DISPOSABLE);
	}
}
