/**
 * This class was created by <Vazkii>. It's distributed as
 * part of the Botania Mod. Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 * 
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 * 
 * File Created @ [Jul 30, 2014, 12:37:24 PM (GMT)]
 */
package vazkii.botania.common.core.handler;

import net.minecraft.entity.EntityList;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityMobSpawner;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.event.entity.player.PlayerInteractEvent.Action;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public final class SpawnerChangingHandler {

	@SubscribeEvent
	public void onInteract(PlayerInteractEvent event) {
		/*if(event.entityPlayer == null || event.entityPlayer.capabilities == null || event.world == null) todo 1.9
			return; // Cauldron breaks stuff

		if(event.entityPlayer.capabilities.isCreativeMode && !event.world.isRemote && event.action == Action.RIGHT_CLICK_BLOCK && !event.entityPlayer.isSneaking()) {
			ItemStack stack = event.entityPlayer.getCurrentEquippedItem();
			if(stack != null && stack.getItem() == Items.spawn_egg) {
				TileEntity tile = event.world.getTileEntity(event.pos);
				if(tile instanceof TileEntityMobSpawner) {
					TileEntityMobSpawner spawner = (TileEntityMobSpawner) tile;
					spawner.getSpawnerBaseLogic().setEntityName(EntityList.getStringFromID(stack.getItemDamage()));
					event.world.markBlockForUpdate(event.pos);
					event.setCanceled(true);
				}
			}
		}*/
	}

}
