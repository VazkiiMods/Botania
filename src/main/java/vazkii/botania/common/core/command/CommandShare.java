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
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.translation.I18n;

import javax.annotation.Nonnull;

public class CommandShare extends CommandBase {

	@Nonnull
	@Override
	public String getName() {
		return "botania-share";
	}

	@Nonnull
	@Override
	public String getUsage(@Nonnull ICommandSender sender) {
		return "<entry>";
	}

	@Override
	public void execute(@Nonnull MinecraftServer server, @Nonnull ICommandSender sender, @Nonnull String[] args) {
		String json = I18n.translateToLocal("botaniamisc.shareMsg");
		json = json.replaceAll("%name%", sender.getName());
		json = json.replaceAll("%entry%", args[0]);
		json = json.replaceAll("%entryname%", I18n.translateToLocal(args[0]));

		ITextComponent component = ITextComponent.Serializer.jsonToComponent(json);
		server.getPlayerList().sendMessage(component);
	}


	@Override
	public int getRequiredPermissionLevel() {
		return 0;
	}

	@Override
	public boolean checkPermission(MinecraftServer server, ICommandSender sender) {
		return sender instanceof EntityPlayer;
	}
}
