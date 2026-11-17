/*
 * This class is distributed as part of the Botania Mod.
 * Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 *
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 */
package vazkii.botania.common.item;

import net.minecraft.ChatFormatting;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.item.DyeItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;

import vazkii.botania.api.corporea.CorporeaHelper;
import vazkii.botania.common.entity.BotaniaEntities;
import vazkii.botania.common.entity.CorporeaSparkEntity;
import vazkii.botania.common.helper.EntityHelper;
import vazkii.botania.common.impl.corporea.DummyCorporeaNode;
import vazkii.botania.common.lib.BotaniaTags;

import java.util.List;

public class CorporeaSparkItem extends Item {

	public CorporeaSparkItem(Properties properties) {
		super(properties);
	}

	@Override
	public InteractionResult useOn(UseOnContext context) {
		ItemStack otherHandStack = ItemStack.EMPTY;
		if (context.getPlayer() != null) {
			otherHandStack = context.getPlayer().getItemInHand(EntityHelper.otherHand(context.getHand()));
			if (context.getPlayer().isCreative()) {
				otherHandStack = otherHandStack.copy();
			}
		}
		return attachSpark(context.getLevel(), context.getClickedPos(), context.getItemInHand(), otherHandStack)
				? InteractionResult.sidedSuccess(context.getLevel().isClientSide())
				: InteractionResult.PASS;
	}

	private static boolean canPlace(Level world, CorporeaSparkEntity spark) {
		return world.getBlockState(spark.getAttachPos()).is(BotaniaTags.Blocks.CORPOREA_SPARK_OVERRIDE)
				|| !(spark.getSparkNode() instanceof DummyCorporeaNode);
	}

	public static boolean attachSpark(Level world, BlockPos pos, ItemStack stack, ItemStack otherHandStack) {
		CorporeaSparkEntity spark = BotaniaEntities.CORPOREA_SPARK.create(world);
		if (stack.is(BotaniaItems.MASTER_CORPOREA_SPARK)) {
			spark.setMaster(true);
		}
		if (stack.is(BotaniaItems.CREATIVE_CORPOREA_SPARK)) {
			spark.setCreative(true);
		}
		spark.setPos(pos.getX() + 0.5, pos.getY() + 1.25, pos.getZ() + 0.5);
		if (otherHandStack.getItem() instanceof DyeItem dye) {
			otherHandStack.shrink(1);
			spark.setNetwork(dye.getDyeColor());
		}

		if (canPlace(world, spark) && !CorporeaHelper.instance().doesBlockHaveSpark(world, pos)) {
			if (!world.isClientSide) {
				world.addFreshEntity(spark);
				stack.shrink(1);
			}
			return true;
		}
		return false;
	}

	@Override
	public void appendHoverText(ItemStack stack, TooltipContext context, List<Component> tooltip, TooltipFlag tooltipFlag) {
		if (stack.is(BotaniaItems.CREATIVE_CORPOREA_SPARK)) {
			tooltip.add(Component.translatable("botaniamisc.creativeSpark").withStyle(ChatFormatting.GRAY));
		}
	}
}
