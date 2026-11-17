/*
 * This class is distributed as part of the Botania Mod.
 * Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 *
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 */
package org.violetmoon.botania.common.item.equipment.tool.manasteel;

import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.*;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.block.state.BlockState;

import org.violetmoon.botania.api.item.SortableTool;
import org.violetmoon.botania.client.gui.ItemsRemainingRenderHandler;
import org.violetmoon.botania.common.helper.PlayerHelper;
import org.violetmoon.botania.common.item.equipment.tool.ToolCommons;
import org.violetmoon.botania.common.lib.BotaniaTags;

public class ManasteelPickaxeItem extends PickaxeItem implements SortableTool {

	private static final int TIME = 5;

	public ManasteelPickaxeItem(Tier tier, Properties properties) {
		super(tier, properties);
	}

	@Override
	public InteractionResult useOn(UseOnContext context) {
		Player player = context.getPlayer();

		if (player != null) {
			if (context.getHand() == InteractionHand.MAIN_HAND && player.getOffhandItem().getItem() instanceof BlockItem) {
				return InteractionResult.PASS;
			}

			for (int i = 0; i < player.getInventory().getContainerSize(); i++) {
				ItemStack stackAt = player.getInventory().getItem(i);
				if (!stackAt.isEmpty() && stackAt.is(BotaniaTags.Items.TOOL_PLACEABLE_PICKAXE)) {
					ItemStack displayStack = stackAt.copy();
					InteractionResult did = PlayerHelper.substituteUse(context, stackAt);
					if (did.consumesAction()) {
						if (!context.getLevel().isClientSide()) {
							ItemsRemainingRenderHandler.send(player, displayStack, BotaniaTags.Items.TOOL_PLACEABLE_PICKAXE);
						}
						player.getCooldowns().addCooldown(this, TIME);
						return did;
					}
				}
			}
		}
		return InteractionResult.PASS;
	}

	@Override
	public int getSortingPriority(ItemStack stack, BlockState state) {
		return ToolCommons.getToolPriority(stack);
	}
}
