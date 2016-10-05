/**
 * This class was created by <Vazkii>. It's distributed as
 * part of the Botania Mod. Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 *
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 *
 * File Created @ [May 31, 2014, 11:43:13 PM (GMT)]
 */
package vazkii.botania.common.core.command;

import javax.annotation.Nonnull;

import net.minecraft.command.CommandBase;
import net.minecraft.command.ICommandSender;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.text.Style;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraft.util.text.TextFormatting;
import vazkii.botania.common.core.version.ThreadDownloadMod;
import vazkii.botania.common.core.version.VersionChecker;

public class CommandDownloadLatest extends CommandBase {

	private static final boolean ENABLED = true;

	@Nonnull
	@Override
	public String getCommandName() {
		return "botania-download-latest";
	}

	@Nonnull
	@Override
	public String getCommandUsage(@Nonnull ICommandSender var1) {
		return "/botania-download-latest <version>";
	}

	@Override
	public void execute(@Nonnull MinecraftServer server, @Nonnull ICommandSender var1, @Nonnull String[] var2) {
		if(!ENABLED)
			var1.addChatMessage(new TextComponentTranslation("botania.versioning.disabled").setStyle(new Style().setColor(TextFormatting.RED)));

		else if(var2.length == 1)
			if(VersionChecker.downloadedFile)
				var1.addChatMessage(new TextComponentTranslation("botania.versioning.downloadedAlready").setStyle(new Style().setColor(TextFormatting.RED)));
			else if(VersionChecker.startedDownload)
				var1.addChatMessage(new TextComponentTranslation("botania.versioning.downloadingAlready").setStyle(new Style().setColor(TextFormatting.RED)));
			else new ThreadDownloadMod("Botania " + var2[0] + ".jar");
	}

}
