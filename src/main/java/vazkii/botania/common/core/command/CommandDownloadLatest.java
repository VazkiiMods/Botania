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

import net.minecraft.command.CommandBase;
import net.minecraft.command.ICommandSender;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.text.Style;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraft.util.text.TextFormatting;
import vazkii.botania.common.core.version.ThreadDownloadMod;
import vazkii.botania.common.core.version.VersionChecker;

import javax.annotation.Nonnull;

public class CommandDownloadLatest extends CommandBase {

	private static final boolean ENABLED = true;

	@Nonnull
	@Override
	public String getName() {
		return "botania-download-latest";
	}

	@Nonnull
	@Override
	public String getUsage(@Nonnull ICommandSender var1) {
		return "/botania-download-latest <version>";
	}

	@Override
	public boolean checkPermission(MinecraftServer server, ICommandSender sender) {
		return server.isSinglePlayer() || super.checkPermission(server, sender);
	}

	@Override
	public void execute(@Nonnull MinecraftServer server, @Nonnull ICommandSender var1, @Nonnull String[] var2) {
		if(!ENABLED)
			var1.sendMessage(new TextComponentTranslation("botania.versioning.disabled").setStyle(new Style().setColor(TextFormatting.RED)));

		else if(var2.length == 1)
			if(VersionChecker.downloadedFile)
				var1.sendMessage(new TextComponentTranslation("botania.versioning.downloadedAlready").setStyle(new Style().setColor(TextFormatting.RED)));
			else if(VersionChecker.startedDownload)
				var1.sendMessage(new TextComponentTranslation("botania.versioning.downloadingAlready").setStyle(new Style().setColor(TextFormatting.RED)));
			else new ThreadDownloadMod("Botania " + var2[0] + ".jar");
	}

}
