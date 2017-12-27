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

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.items.CapabilityItemHandler;
import vazkii.botania.api.corporea.CorporeaHelper;
import vazkii.botania.common.entity.EntityCorporeaSpark;

import javax.annotation.Nonnull;

public class ItemCorporeaSpark extends ItemMod {

	public ItemCorporeaSpark(String name) {
		super(name);
	}

	@Nonnull
	@Override
	public EnumActionResult onItemUse(EntityPlayer player, World world, BlockPos pos, EnumHand hand, EnumFacing side, float xv, float yv, float zv) {
		TileEntity tile = world.getTileEntity(pos);
		if(tile != null && (tile.hasCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY, EnumFacing.UP) || tile.hasCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY, null))
				&& !CorporeaHelper.doesBlockHaveSpark(world, pos)) {
			ItemStack stack = player.getHeldItem(hand);
			stack.shrink(1);
			if(!world.isRemote) {
				EntityCorporeaSpark spark = new EntityCorporeaSpark(world);
				if(this == ModItems.corporeaSparkMaster)
					spark.setMaster(true);
				spark.setPosition(pos.getX() + 0.5, pos.getY() + 1.5, pos.getZ() + 0.5);
				world.spawnEntity(spark);
			}
			return EnumActionResult.SUCCESS;
		}
		return EnumActionResult.PASS;
	}

}
