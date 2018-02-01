/**
 * This class was created by <Vazkii>. It's distributed as
 * part of the Botania Mod. Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 *
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 *
 * File Created @ [Jul 30, 2014, 1:10:34 PM (GMT)]
 */
package vazkii.botania.common.block.tile;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.ITickable;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.world.WorldServer;
import vazkii.botania.api.state.BotaniaStateProps;
import vazkii.botania.common.item.equipment.tool.ToolCommons;

import java.util.List;

public class TileEnderEye extends TileMod implements ITickable {

	@Override
	public void update() {
		if (world.isRemote)
			return;

		boolean wasLooking = world.getBlockState(getPos()).getValue(BotaniaStateProps.POWERED);
		int range = 80;
		List<EntityPlayer> players = world.getEntitiesWithinAABB(EntityPlayer.class, new AxisAlignedBB(pos.add(-range, -range, -range), pos.add(range, range, range)));

		boolean looking = false;
		for(EntityPlayer player : players) {
			ItemStack helm = player.getItemStackFromSlot(EntityEquipmentSlot.HEAD);
			if(!helm.isEmpty() && helm.getItem() == Item.getItemFromBlock(Blocks.PUMPKIN))
				continue;

			RayTraceResult pos = ToolCommons.raytraceFromEntity(world, player, true, 64);
			if(pos != null && pos.getBlockPos() != null && pos.getBlockPos().equals(getPos())) {
				looking = true;
				break;
			}
		}

		if(looking != wasLooking && !world.isRemote)
			world.setBlockState(getPos(), world.getBlockState(getPos()).withProperty(BotaniaStateProps.POWERED, looking), 1 | 2);

		if(looking) {
			double x = getPos().getX() - 0.1 + Math.random() * 1.2;
			double y = getPos().getY() - 0.1 + Math.random() * 1.2;
			double z = getPos().getZ() - 0.1 + Math.random() * 1.2;

			((WorldServer) world).spawnParticle(EnumParticleTypes.REDSTONE, false, x, y, z, 0, 1.0D, 0.0D, 0.0D, 1.0D);
		}
	}

}
