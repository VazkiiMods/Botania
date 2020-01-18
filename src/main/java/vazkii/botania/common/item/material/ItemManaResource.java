/**
 * This class was created by <Vazkii>. It's distributed as
 * part of the Botania Mod. Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 *
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 *
 * File Created @ [Jan 30, 2014, 4:49:16 PM (GMT)]
 */
package vazkii.botania.common.item.material;

import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemUseContext;
import net.minecraft.item.Items;
import net.minecraft.util.ActionResultType;
import vazkii.botania.common.entity.EntityDoppleganger;
import net.minecraft.item.Item;
import vazkii.botania.common.item.ModItems;

import javax.annotation.Nonnull;

public class ItemManaResource extends Item {
	public ItemManaResource(Properties props) {
		super(props);
	}

	@Nonnull
	@Override
	public ActionResultType onItemUse(ItemUseContext ctx) {
		ItemStack stack = ctx.getItem();

		if(this == ModItems.terrasteel || this == ModItems.gaiaIngot)
			return EntityDoppleganger.spawn(ctx.getPlayer(), stack, ctx.getWorld(), ctx.getPos(), this == ModItems.gaiaIngot) ? ActionResultType.SUCCESS : ActionResultType.FAIL;
		else if(this == ModItems.livingroot) {
			return Items.BONE_MEAL.onItemUse(ctx);
		}

		return super.onItemUse(ctx);
	}
}
