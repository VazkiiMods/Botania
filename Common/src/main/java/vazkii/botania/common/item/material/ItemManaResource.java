/*
 * This class is distributed as part of the Botania Mod.
 * Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 *
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 */
package vazkii.botania.common.item.material;

import net.minecraft.world.InteractionResult;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.context.UseOnContext;

import vazkii.botania.common.entity.EntityDoppleganger;
import vazkii.botania.common.item.ModItems;

import javax.annotation.Nonnull;

public class ItemManaResource extends Item {
	public ItemManaResource(Properties props) {
		super(props);
	}

	@Nonnull
	@Override
	public InteractionResult useOn(UseOnContext ctx) {
		ItemStack stack = ctx.getItemInHand();

		if (stack.is(ModItems.terrasteel) || stack.is(ModItems.gaiaIngot)) {
			return EntityDoppleganger.spawn(ctx.getPlayer(), stack, ctx.getLevel(), ctx.getClickedPos(), stack.is(ModItems.gaiaIngot))
					? InteractionResult.SUCCESS
					: InteractionResult.FAIL;
		} else if (stack.is(ModItems.livingroot)) {
			return Items.BONE_MEAL.useOn(ctx);
		}

		return super.useOn(ctx);
	}
}
