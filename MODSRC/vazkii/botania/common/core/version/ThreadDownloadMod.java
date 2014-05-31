/**
 * This class was created by <Vazkii>. It's distributed as
 * part of the Botania Mod. Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 * 
 * Botania is Open Source and distributed under a
 * Creative Commons Attribution-NonCommercial-ShareAlike 3.0 License
 * (http://creativecommons.org/licenses/by-nc-sa/3.0/deed.en_GB)
 * 
 * File Created @ [May 31, 2014, 10:22:44 PM (GMT)]
 */
package vazkii.botania.common.core.version;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.LinkedList;
import java.util.List;

import net.minecraft.client.Minecraft;
import net.minecraft.util.ChatComponentTranslation;

/**
 * @author Vazkii, TheWhiteWolves
 */
public class ThreadDownloadMod extends Thread {

	public static int amountOfDownloadResourceThreads = 0;
	public static List<String> downloadings = new LinkedList();

	URL downloadUrl;
	String modName;

	long downloadStartTime;
	long downloadFinishTime;

	byte[] buffer = new byte[10240];

	int totalBytesDownloaded;
	int bytesJustDownloaded;

	InputStream webReader;

	public ThreadDownloadMod(String urlToFind, String modName) {
		if (urlToFind == null)
			return;

		setName("Botania Download File Thread " + ++amountOfDownloadResourceThreads);

		this.modName = modName;
		try {
			downloadUrl = getUrlFromFile(urlToFind);
			downloadUrl.openConnection();
			webReader = downloadUrl.openStream();
			downloadings.add(modName);
		} catch (IOException e) {
			e.printStackTrace();
		}
		setDaemon(true);
		start();
	}

	URL getUrlFromFile(String file) {
		URL url;
		try {
			url = new URL(file);
			BufferedReader r = new BufferedReader(new InputStreamReader(url.openStream()));
			String s = r.readLine();
			r.close();
			return new URL(s);
		} catch (IOException e) {
			e.printStackTrace();
			return null;
		}
	}

	@Override
	public void run() {
		try {
			File dir = new File(".", "mods");
			File f = new File(dir, modName + ".jar");
			f.createNewFile();
			System.out.println("[Botania] Starting to download file: " + modName + ".zip to " + f.getAbsolutePath() + ".");
			FileOutputStream outputStream = new FileOutputStream(f.getAbsolutePath());

			downloadStartTime = System.currentTimeMillis();

			while ((bytesJustDownloaded = webReader.read(buffer)) > 0) {
				outputStream.write(buffer, 0, bytesJustDownloaded);
				buffer = new byte[10240];
				totalBytesDownloaded += bytesJustDownloaded;
			}

			downloadFinishTime = System.currentTimeMillis();

			if (Minecraft.getMinecraft().thePlayer != null) 
				Minecraft.getMinecraft().thePlayer.addChatMessage(new ChatComponentTranslation("botania.versioning.doneDownloading", modName, (downloadFinishTime - downloadStartTime) / 1000));

			outputStream.close();
			webReader.close();
			--amountOfDownloadResourceThreads;
			downloadings.remove(modName);
			finalize();
		} catch (Throwable e) {
			e.printStackTrace();
			--amountOfDownloadResourceThreads;
			downloadings.remove(modName);
			try {
				finalize();
			} catch (Throwable e1) {
				e1.printStackTrace();
			}
		}
	}
}