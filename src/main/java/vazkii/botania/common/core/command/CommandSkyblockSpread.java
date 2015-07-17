/**
 * This class was created by <Vazkii>. It's distributed as
 * part of the Botania Mod. Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 * 
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 * 
 * File Created @ [Jul 17, 2015, 5:13:06 PM (GMT)]
 */
package vazkii.botania.common.core.command;

import vazkii.botania.common.world.SkyblockWorldEvents;
import net.minecraft.command.CommandBase;
import net.minecraft.command.ICommandSender;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.ChunkCoordinates;

public class CommandSkyblockSpread extends CommandBase {

	@Override
	public String getCommandName() {
		return "botania-skyblock-spread";
	}

	@Override
	public String getCommandUsage(ICommandSender p_71518_1_) {
		return "<player>";
	}
	
	@Override
    public int getRequiredPermissionLevel() {
        return 2;
    }

	@Override
	public void processCommand(ICommandSender p_71515_1_, String[] p_71515_2_) {
		int maxrange = 2000000;
		EntityPlayer player = getPlayer(p_71515_1_, p_71515_2_[0]);
		if(player != null) {
			ChunkCoordinates spawn = player.worldObj.getSpawnPoint();
			int x = player.worldObj.rand.nextInt(maxrange) - maxrange / 2 + spawn.posX;
			int z = player.worldObj.rand.nextInt(maxrange) - maxrange / 2 + spawn.posZ;
			SkyblockWorldEvents.spawnPlayer(player, x, spawn.posY, z);
		}
	}

}
