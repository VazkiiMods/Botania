/*
 * This class is distributed as part of the Botania Mod.
 * Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 *
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 */
package vazkii.botania.common.item;

import net.minecraft.block.entity.BlockEntity;
import net.minecraft.inventory.Inventory;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemUsageContext;
import net.minecraft.util.ActionResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.World;

import vazkii.botania.api.corporea.CorporeaHelper;
import vazkii.botania.common.entity.EntityCorporeaSpark;
import vazkii.botania.common.entity.ModEntities;
import vazkii.botania.common.lib.ModTags;

import javax.annotation.Nonnull;

public class ItemCorporeaSpark extends Item {

	public ItemCorporeaSpark(Settings props) {
		super(props);
	}

	@Nonnull
	@Override
	public ActionResult useOnBlock(ItemUsageContext ctx) {
		return attachSpark(ctx.getWorld(), ctx.getBlockPos(), ctx.getStack()) ? ActionResult.SUCCESS : ActionResult.PASS;
	}

	private static boolean canPlaceOn(World world, BlockPos pos) {
		if (world.getBlockState(pos).isIn(ModTags.Blocks.CORPOREA_SPARK_OVERRIDE)) {
			return true;
		}

		BlockEntity tile = world.getBlockEntity(pos);
		return tile instanceof Inventory; // todo 1.16-fabric query node detectors
	}

	public static boolean attachSpark(World world, BlockPos pos, ItemStack stack) {
		if (canPlaceOn(world, pos) && !CorporeaHelper.instance().doesBlockHaveSpark(world, pos)) {
			if (!world.isClient) {
				EntityCorporeaSpark spark = ModEntities.CORPOREA_SPARK.create(world);
				if (stack.getItem() == ModItems.corporeaSparkMaster) {
					spark.setMaster(true);
				}
				spark.updatePosition(pos.getX() + 0.5, pos.getY() + 1.25, pos.getZ() + 0.5);
				world.spawnEntity(spark);
				stack.decrement(1);
			}
			return true;
		}
		return false;
	}
}
