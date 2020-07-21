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
import net.minecraft.item.Item;
import net.minecraft.item.ItemUsageContext;
import net.minecraft.util.ActionResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.World;

import vazkii.botania.api.corporea.CorporeaHelper;
import vazkii.botania.common.entity.EntityCorporeaSpark;
import vazkii.botania.common.entity.ModEntities;

import javax.annotation.Nonnull;

public class ItemCorporeaSpark extends Item {

	public ItemCorporeaSpark(Settings props) {
		super(props);
	}

	@Nonnull
	@Override
	public ActionResult useOnBlock(ItemUsageContext ctx) {
		World world = ctx.getWorld();
		BlockPos pos = ctx.getBlockPos();

		BlockEntity tile = world.getBlockEntity(pos);
		if (tile != null
				&& (tile.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY, Direction.UP).isPresent()
						|| tile.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY, null).isPresent())
				&& !CorporeaHelper.instance().doesBlockHaveSpark(world, pos)) {
			ctx.getStack().decrement(1);
			if (!world.isClient) {
				EntityCorporeaSpark spark = ModEntities.CORPOREA_SPARK.create(world);
				if (this == ModItems.corporeaSparkMaster) {
					spark.setMaster(true);
				}
				spark.updatePosition(pos.getX() + 0.5, pos.getY() + 1.5, pos.getZ() + 0.5);
				world.spawnEntity(spark);
			}
			return ActionResult.SUCCESS;
		}
		return ActionResult.PASS;
	}
}
