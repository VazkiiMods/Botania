/**
 * This class was created by <Vazkii>. It's distributed as
 * part of the Botania Mod. Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 *
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 *
 * File Created @ [Feb 13, 2015, 10:25:32 PM (GMT)]
 */
package vazkii.botania.common.item;

import net.minecraft.item.ItemUseContext;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.items.CapabilityItemHandler;
import vazkii.botania.api.corporea.CorporeaHelper;
import vazkii.botania.common.entity.EntityCorporeaSpark;

import javax.annotation.Nonnull;

public class ItemCorporeaSpark extends ItemMod {

	public ItemCorporeaSpark(Properties props) {
		super(props);
	}

	@Nonnull
	@Override
	public ActionResultType onItemUse(ItemUseContext ctx) {
		World world = ctx.getWorld();
		BlockPos pos = ctx.getPos();

		TileEntity tile = world.getTileEntity(pos);
		if(tile != null
				&& (tile.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY, Direction.UP).isPresent()
					|| tile.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY, null).isPresent())
				&& !CorporeaHelper.doesBlockHaveSpark(world, pos)) {
			ctx.getItem().shrink(1);
			if(!world.isRemote) {
				EntityCorporeaSpark spark = new EntityCorporeaSpark(world);
				if(this == ModItems.corporeaSparkMaster)
					spark.setMaster(true);
				spark.setPosition(pos.getX() + 0.5, pos.getY() + 1.5, pos.getZ() + 0.5);
				world.addEntity(spark);
			}
			return ActionResultType.SUCCESS;
		}
		return ActionResultType.PASS;
	}
}
