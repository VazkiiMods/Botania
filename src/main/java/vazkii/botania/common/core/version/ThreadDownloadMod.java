/**
 * This class was created by <Vazkii>. It's distributed as
 * part of the Botania Mod. Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 * 
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 * 
 * File Created @ [May 31, 2014, 10:22:44 PM (GMT)]
 */
package vazkii.botania.common.core.version;

import java.awt.Desktop;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;

import net.minecraft.client.Minecraft;
import net.minecraft.util.ChatComponentTranslation;
import net.minecraft.util.ChatStyle;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.util.IChatComponent;
import net.minecraft.util.StatCollector;

public class ThreadDownloadMod extends Thread {

	String fileName;

	byte[] buffer = new byte[10240];

	int totalBytesDownloaded;
	int bytesJustDownloaded;

	InputStream webReader;

	public ThreadDownloadMod(String fileName) {
		setName("Botania Download File Thread");
		this.fileName = fileName;

		setDaemon(true);
		start();
	}

	@Override
	public void run() {
		try {
			IChatComponent component = IChatComponent.Serializer.func_150699_a(String.format(StatCollector.translateToLocal("botania.versioning.startingDownload"), fileName));
			if(Minecraft.getMinecraft().thePlayer != null)
				Minecraft.getMinecraft().thePlayer.addChatMessage(component);

			VersionChecker.startedDownload = true;

			String base = "http://botaniamod.net/";
			String file = fileName.replaceAll(" ", "%20");
			URL url = new URL(base + "dl.php?file=" + file);

			try {
				url.openStream().close(); // Add to DL Counter
			} catch(IOException e) { }

			url = new URL(base + "files/" + file);
			webReader = url.openStream();

			File dir = new File(".", "mods");
			File f = new File(dir, fileName + ".dl");
			f.createNewFile();

			FileOutputStream outputStream = new FileOutputStream(f.getAbsolutePath());

			while((bytesJustDownloaded = webReader.read(buffer)) > 0) {
				outputStream.write(buffer, 0, bytesJustDownloaded);
				buffer = new byte[10240];
				totalBytesDownloaded += bytesJustDownloaded;
			}
			outputStream.close();
			webReader.close();

			File f1 = new File(dir, fileName);
			if(!f1.exists())
				f.renameTo(f1);

			if(Minecraft.getMinecraft().thePlayer != null)
				Minecraft.getMinecraft().thePlayer.addChatMessage(new ChatComponentTranslation("botania.versioning.doneDownloading", fileName).setChatStyle(new ChatStyle().setColor(EnumChatFormatting.GREEN)));

			Desktop.getDesktop().open(dir);
			VersionChecker.downloadedFile = true;

			finalize();
		} catch(Throwable e) {
			e.printStackTrace();
			sendError();
			try {
				finalize();
			} catch(Throwable e1) {
				e1.printStackTrace();
			}
		}
	}

	private void sendError() {
		if(Minecraft.getMinecraft().thePlayer != null)
			Minecraft.getMinecraft().thePlayer.addChatComponentMessage(new ChatComponentTranslation("botania.versioning.error").setChatStyle(new ChatStyle().setColor(EnumChatFormatting.RED)));
	}
}