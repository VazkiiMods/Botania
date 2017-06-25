/**
 * This class was created by <Vazkii>. It's distributed as
 * part of the Botania Mod. Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 *
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 *
 * File Created @ [Aug 21, 2014, 5:24:55 PM (GMT)]
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
import vazkii.botania.api.internal.VanillaPacketDispatcher;
import vazkii.botania.api.mana.IManaGivingItem;
import vazkii.botania.api.mana.spark.ISparkAttachable;
import vazkii.botania.common.entity.EntitySpark;
import vazkii.botania.common.lib.LibItemNames;

import javax.annotation.Nonnull;

public class ItemSpark extends ItemMod implements IManaGivingItem {

	public ItemSpark() {
		super(LibItemNames.SPARK);
	}

	@Nonnull
	@Override
	public EnumActionResult onItemUse(EntityPlayer player, World world, BlockPos pos, EnumHand hand, EnumFacing side, float xv, float yv, float zv) {
		TileEntity tile = world.getTileEntity(pos);
		if(tile instanceof ISparkAttachable) {
			ISparkAttachable attach = (ISparkAttachable) tile;
			ItemStack stack = player.getHeldItem(hand);
			if(attach.canAttachSpark(stack) && attach.getAttachedSpark() == null) {
				if(!world.isRemote) {
					stack.shrink(1);
					EntitySpark spark = new EntitySpark(world);
					spark.setPosition(pos.getX() + 0.5, pos.getY() + 1.5, pos.getZ() + 0.5);
					world.spawnEntity(spark);
					attach.attachSpark(spark);
					VanillaPacketDispatcher.dispatchTEToNearbyPlayers(world, pos);
				}
				return EnumActionResult.SUCCESS;
			}
		}
		return EnumActionResult.PASS;
	}
}
