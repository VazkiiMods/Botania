/**
 * This class was created by <Vazkii>. It's distributed as
 * part of the Botania Mod. Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 *
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 *
 * File Created @ [Sep 23, 2014, 8:06:49 PM (GMT)]
 */
package vazkii.botania.common.core.command;

import net.minecraft.command.CommandBase;
import net.minecraft.command.ICommandSender;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.IChatComponent;
import net.minecraft.util.StatCollector;

public class CommandShare extends CommandBase {

	@Override
	public String getCommandName() {
		return "botania-share";
	}

	@Override
	public String getCommandUsage(ICommandSender p_71518_1_) {
		return "<entry>";
	}

	@Override
	public void processCommand(ICommandSender sender, String[] args) {
		String json = StatCollector.translateToLocal("botaniamisc.shareMsg");
		json = json.replaceAll("%name%", sender.getCommandSenderName());
		json = json.replaceAll("%entry%", args[0]);
		json = json.replaceAll("%entryname%", StatCollector.translateToLocal(args[0]));

		IChatComponent component = IChatComponent.Serializer.func_150699_a(json);
		MinecraftServer.getServer().getConfigurationManager().sendChatMsg(component);
	}


	@Override
	public int getRequiredPermissionLevel() {
		return 0;
	}

	@Override
	public boolean canCommandSenderUseCommand(ICommandSender p_71519_1_) {
		return p_71519_1_ instanceof EntityPlayer;
	}
}
