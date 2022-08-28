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
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import vazkii.botania.api.corporea.CorporeaHelper;
import vazkii.botania.common.entity.EntityCorporeaSpark;
import vazkii.botania.common.entity.ModEntities;
import vazkii.botania.common.impl.corporea.DummyCorporeaNode;
import vazkii.botania.common.lib.ModTags;

import java.util.List;

public class ItemCorporeaSpark extends Item {

	public ItemCorporeaSpark(Properties props) {
		super(props);
	}

	@NotNull
	@Override
	public InteractionResult useOn(UseOnContext ctx) {
		return attachSpark(ctx.getLevel(), ctx.getClickedPos(), ctx.getItemInHand()) ? InteractionResult.SUCCESS : InteractionResult.PASS;
	}

	private static boolean canPlace(Level world, EntityCorporeaSpark spark) {
		return world.getBlockState(spark.getAttachPos()).is(ModTags.Blocks.CORPOREA_SPARK_OVERRIDE)
				|| !(spark.getSparkNode() instanceof DummyCorporeaNode);
	}

	public static boolean attachSpark(Level world, BlockPos pos, ItemStack stack) {
		EntityCorporeaSpark spark = ModEntities.CORPOREA_SPARK.create(world);
		if (stack.is(ModItems.corporeaSparkMaster)) {
			spark.setMaster(true);
		}
		if (stack.is(ModItems.corporeaSparkCreative)) {
			spark.setCreative(true);
		}
		spark.setPos(pos.getX() + 0.5, pos.getY() + 1.25, pos.getZ() + 0.5);

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
	public void appendHoverText(ItemStack stack, @Nullable Level level, List<Component> tooltip, TooltipFlag tooltipFlag) {
		if (stack.is(ModItems.corporeaSparkCreative)) {
			tooltip.add(Component.translatable("botaniamisc.creativeSpark").withStyle(ChatFormatting.GRAY));
		}
	}
}
