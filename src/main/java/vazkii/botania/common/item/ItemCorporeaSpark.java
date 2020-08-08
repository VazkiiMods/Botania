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
import net.minecraft.item.ItemUseContext;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.items.CapabilityItemHandler;

import vazkii.botania.api.corporea.CorporeaHelper;
import vazkii.botania.common.entity.EntityCorporeaSpark;
import vazkii.botania.common.entity.ModEntities;
import vazkii.botania.common.lib.ModTags;

import javax.annotation.Nonnull;

public class ItemCorporeaSpark extends Item {

	public ItemCorporeaSpark(Properties props) {
		super(props);
	}

	private boolean canPlaceOn(World world, BlockPos pos) {
		if (world.getBlockState(pos).isIn(ModTags.Blocks.CORPOREA_SPARK_OVERRIDE)) {
			return true;
		}

		TileEntity tile = world.getTileEntity(pos);
		return tile != null
				&& (tile.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY, Direction.UP).isPresent()
						|| tile.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY, null).isPresent());
	}

	@Nonnull
	@Override
	public ActionResultType onItemUse(ItemUseContext ctx) {
		World world = ctx.getWorld();
		BlockPos pos = ctx.getPos();

		if (canPlaceOn(world, pos) && !CorporeaHelper.instance().doesBlockHaveSpark(world, pos)) {
			ctx.getItem().shrink(1);
			if (!world.isRemote) {
				EntityCorporeaSpark spark = ModEntities.CORPOREA_SPARK.create(world);
				if (this == ModItems.corporeaSparkMaster) {
					spark.setMaster(true);
				}
				spark.setPosition(pos.getX() + 0.5, pos.getY() + 1.25, pos.getZ() + 0.5);
				world.addEntity(spark);
			}
			return ActionResultType.SUCCESS;
		}
		return ActionResultType.PASS;
	}
}
