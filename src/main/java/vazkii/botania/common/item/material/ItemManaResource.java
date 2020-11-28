/*
 * This class is distributed as part of the Botania Mod.
 * Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 *
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 */
package vazkii.botania.common.item.material;

import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemUsageContext;
import net.minecraft.item.Items;
import net.minecraft.util.ActionResult;

import vazkii.botania.common.entity.EntityDoppleganger;
import vazkii.botania.common.item.ModItems;

import javax.annotation.Nonnull;

public class ItemManaResource extends Item {
	public ItemManaResource(Settings props) {
		super(props);
	}

	@Nonnull
	@Override
	public ActionResult useOnBlock(ItemUsageContext ctx) {
		ItemStack stack = ctx.getStack();

		if (this == ModItems.terrasteel || this == ModItems.gaiaIngot) {
			return EntityDoppleganger.spawn(ctx.getPlayer(), stack, ctx.getWorld(), ctx.getBlockPos(), this == ModItems.gaiaIngot) ? ActionResult.SUCCESS : ActionResult.FAIL;
		} else if (this == ModItems.livingroot) {
			return Items.BONE_MEAL.useOnBlock(ctx);
		}

		return super.useOnBlock(ctx);
	}
}
