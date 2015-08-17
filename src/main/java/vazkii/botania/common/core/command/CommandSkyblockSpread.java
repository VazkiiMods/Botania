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

import net.minecraft.command.CommandBase;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.ChunkCoordinates;
import net.minecraft.util.StatCollector;
import vazkii.botania.common.core.helper.MathHelper;
import vazkii.botania.common.world.SkyblockWorldEvents;

public class CommandSkyblockSpread extends CommandBase {

	@Override
	public String getCommandName() {
		return "botania-skyblock-spread";
	}

	@Override
	public String getCommandUsage(ICommandSender p_71518_1_) {
		return "<player> [<range>]";
	}

	@Override
	public int getRequiredPermissionLevel() {
		return 2;
	}

	@Override
	public void processCommand(ICommandSender sender, String[] args) {
		int maxAllowed = 1000000;
		int minAllowed = 250;
		int minDist = 100;

		int maxrange = 200000;
		if(args.length == 2)
			maxrange = parseInt(sender, args[1]);

		if(maxrange > maxAllowed)
			throw new CommandException("botaniamisc.skyblockRangeTooHigh");
		if(maxrange < minAllowed)
			throw new CommandException(StatCollector.translateToLocal("botaniamisc.skyblockRangeTooLow"));

		EntityPlayer player = getPlayer(sender, args[0]);
		if(player != null) {
			ChunkCoordinates spawn = player.worldObj.getSpawnPoint();
			int x, z;

			do {
				x = player.worldObj.rand.nextInt(maxrange) - maxrange / 2 + spawn.posX;
				z = player.worldObj.rand.nextInt(maxrange) - maxrange / 2 + spawn.posZ;
			} while(MathHelper.pointDistancePlane(x, z, spawn.posX, spawn.posZ) < minDist);

			SkyblockWorldEvents.spawnPlayer(player, x, spawn.posY, z, true);
		}
	}

}
