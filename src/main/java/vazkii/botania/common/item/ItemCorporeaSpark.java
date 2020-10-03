/*
 * This class is distributed as part of the Botania Mod.
 * Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 *
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 */
package vazkii.botania.common.item;

import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemUseContext;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import vazkii.botania.api.corporea.CorporeaHelper;
import vazkii.botania.common.entity.EntityCorporeaSpark;
import vazkii.botania.common.entity.ModEntities;
import vazkii.botania.common.impl.corporea.DummyCorporeaNode;
import vazkii.botania.common.lib.ModTags;

import javax.annotation.Nonnull;

public class ItemCorporeaSpark extends Item {

	public ItemCorporeaSpark(Properties props) {
		super(props);
	}

	@Nonnull
	@Override
	public ActionResultType onItemUse(ItemUseContext ctx) {
		return attachSpark(ctx.getWorld(), ctx.getPos(), ctx.getItem()) ? ActionResultType.SUCCESS : ActionResultType.PASS;
	}

	private static boolean canPlace(World world, EntityCorporeaSpark spark) {
		return world.getBlockState(spark.getAttachPos()).isIn(ModTags.Blocks.CORPOREA_SPARK_OVERRIDE)
				|| !(spark.getSparkNode() instanceof DummyCorporeaNode);
	}

	public static boolean attachSpark(World world, BlockPos pos, ItemStack stack) {
		EntityCorporeaSpark spark = ModEntities.CORPOREA_SPARK.create(world);
		if (stack.getItem() == ModItems.corporeaSparkMaster) {
			spark.setMaster(true);
		}
		spark.setPosition(pos.getX() + 0.5, pos.getY() + 1.25, pos.getZ() + 0.5);

		if (canPlace(world, spark) && !CorporeaHelper.instance().doesBlockHaveSpark(world, pos)) {
			if (!world.isRemote) {
				world.addEntity(spark);
				stack.shrink(1);
			}
			return true;
		}
		return false;
	}
}
