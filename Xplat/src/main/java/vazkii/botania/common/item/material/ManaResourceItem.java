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

import org.jetbrains.annotations.NotNull;

import vazkii.botania.common.entity.GaiaGuardianEntity;
import vazkii.botania.common.item.BotaniaItems;

public class ManaResourceItem extends Item {
	public ManaResourceItem(Properties props) {
		super(props);
	}

	@NotNull
	@Override
	public InteractionResult useOn(UseOnContext ctx) {
		ItemStack stack = ctx.getItemInHand();

		if (stack.is(BotaniaItems.terrasteel) || stack.is(BotaniaItems.gaiaIngot)) {
			return GaiaGuardianEntity.spawn(ctx.getPlayer(), stack, ctx.getLevel(), ctx.getClickedPos(), stack.is(BotaniaItems.gaiaIngot))
					? InteractionResult.SUCCESS
					: InteractionResult.FAIL;
		} else if (stack.is(BotaniaItems.livingroot)) {
			return Items.BONE_MEAL.useOn(ctx);
		}

		return super.useOn(ctx);
	}
}
