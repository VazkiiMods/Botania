/**
 * This class was created by <Vazkii>. It's distributed as
 * part of the Botania Mod. Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 *
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 *
 * File Created @ [Jul 23, 2014, 1:06:51 AM (GMT)]
 */
package vazkii.botania.common.item.rod;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;
import vazkii.botania.api.mana.ManaItemHandler;
import vazkii.botania.common.Botania;
import vazkii.botania.common.core.helper.Vector3;
import vazkii.botania.common.lib.LibItemNames;

import javax.annotation.Nonnull;

public class ItemSkyDirtRod extends ItemDirtRod {

	public ItemSkyDirtRod() {
		super(LibItemNames.SKY_DIRT_ROD);
	}

	@Nonnull
	@Override
	public ActionResult<ItemStack> onItemRightClick(World world, EntityPlayer player, @Nonnull EnumHand hand) {
		ItemStack stack = player.getHeldItem(hand);
		if(!world.isRemote && ManaItemHandler.requestManaExactForTool(stack, player, COST * 2, false)) {
			Vector3 playerVec = Vector3.fromEntityCenter(player);
			Vector3 lookVec = new Vector3(player.getLookVec()).multiply(3);
			Vector3 placeVec = playerVec.add(lookVec);

			int x = MathHelper.floor(placeVec.x);
			int y = MathHelper.floor(placeVec.y) + 1;
			int z = MathHelper.floor(placeVec.z);

			int entities = world.getEntitiesWithinAABB(EntityLivingBase.class, new AxisAlignedBB(x, y, z, x + 1, y + 1, z + 1)).size();

			if(entities == 0) {
				ItemStack stackToPlace = new ItemStack(Blocks.DIRT);
				player.setHeldItem(hand, stackToPlace);
				stackToPlace.onItemUse(player, world, new BlockPos(x, y, z), hand, EnumFacing.DOWN, 0F, 0F, 0F);
				player.setHeldItem(hand, stack);

				if(stackToPlace.isEmpty()) {
					ManaItemHandler.requestManaExactForTool(stack, player, COST * 2, true);
					for(int i = 0; i < 6; i++)
						Botania.proxy.sparkleFX(x + Math.random(), y + Math.random(), z + Math.random(), 0.35F, 0.2F, 0.05F, 1F, 5);
				}
			}
		}
		if(world.isRemote)
			player.swingArm(hand);

		return ActionResult.newResult(EnumActionResult.SUCCESS, stack);
	}

}
