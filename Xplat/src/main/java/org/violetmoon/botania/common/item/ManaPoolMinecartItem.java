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
import net.minecraft.server.level.ServerLevel;
import net.minecraft.tags.BlockTags;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.RailShape;
import net.minecraft.world.level.gameevent.GameEvent;

import vazkii.botania.common.entity.ManaPoolMinecartEntity;
import vazkii.botania.xplat.XplatAbstractions;

// [VanillaCopy] of MinecartItem, because that is needlessly locked down through the AbstractMinecart.Type enum
public class ManaPoolMinecartItem extends Item {
	// see ManaPoolMinecartBehavior for the corresponding dispenser behavior implementation

	public ManaPoolMinecartItem(Properties builder) {
		super(builder);
	}

	@Override
	public InteractionResult useOn(UseOnContext context) {
		Level level = context.getLevel();
		BlockPos blockPos = context.getClickedPos();
		BlockState blockState = level.getBlockState(blockPos);
		if (!blockState.is(BlockTags.RAILS)) {
			return InteractionResult.FAIL;
		} else {
			ItemStack itemStack = context.getItemInHand();
			if (level instanceof ServerLevel serverlevel) {
				RailShape railShape = XplatAbstractions.INSTANCE.getRailDirection(blockState, level, blockPos, null);
				double yOffset = 0.0;
				if (railShape.isAscending()) {
					yOffset = 0.5;
				}

				ManaPoolMinecartEntity minecartEntity = ManaPoolMinecartEntity.createMinecart(serverlevel,
						blockPos.getX() + 0.5, blockPos.getY() + 0.0625 + yOffset, blockPos.getZ() + 0.5,
						itemStack, context.getPlayer());
				serverlevel.addFreshEntity(minecartEntity);
				serverlevel.gameEvent(GameEvent.ENTITY_PLACE, blockPos,
						GameEvent.Context.of(context.getPlayer(), serverlevel.getBlockState(blockPos.below())));
			}

			itemStack.shrink(1);
			return InteractionResult.sidedSuccess(level.isClientSide());
		}
	}

}
