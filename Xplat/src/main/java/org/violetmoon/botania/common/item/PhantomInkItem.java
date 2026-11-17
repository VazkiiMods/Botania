/*
 * This class is distributed as part of the Botania Mod.
 * Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 *
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 */

package vazkii.botania.common.item;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;

import vazkii.botania.api.block.PhantomInkableBlock;

public class PhantomInkItem extends Item {

	public PhantomInkItem(Properties properties) {
		super(properties);
	}

	@Override
	public InteractionResult useOn(UseOnContext ctx) {
		Player player = ctx.getPlayer();

		if (player == null) {
			return InteractionResult.PASS;
		}

		Level level = ctx.getLevel();
		BlockPos pos = ctx.getClickedPos();
		PhantomInkableBlock inkable = PhantomInkableBlock.LOOKUP.find(level, pos);
		if (inkable != null) {
			ItemStack stack = ctx.getItemInHand();
			Direction side = ctx.getClickedFace();
			return inkable.onPhantomInked(player, stack, side) ? InteractionResult.SUCCESS : InteractionResult.PASS;
		}
		return InteractionResult.PASS;
	}
}
