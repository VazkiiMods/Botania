/**
 * This class was created by <Vazkii>. It's distributed as
 * part of the Botania Mod. Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 * 
 * Botania is Open Source and distributed under a
 * Creative Commons Attribution-NonCommercial-ShareAlike 3.0 License
 * (http://creativecommons.org/licenses/by-nc-sa/3.0/deed.en_GB)
 * 
 * File Created @ [May 31, 2014, 10:31:46 PM (GMT)]
 */
package vazkii.botania.common.core.version;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;

import net.minecraftforge.common.MinecraftForge;

public class ThreadVersionChecker extends Thread {

	public ThreadVersionChecker() {
		setName("Botania Version Checker Thread");
		setDaemon(true);
		start();
	}

	@Override
	public void run() {
		try {
			URL url = new URL("https://raw.githubusercontent.com/Vazkii/Botania/master/version/" + MinecraftForge.MC_VERSION + ".txt");
			BufferedReader r = new BufferedReader(new InputStreamReader(url.openStream()));
			VersionChecker.onlineVersion = r.readLine();
			r.close();
		} catch(Exception e) {
			e.printStackTrace();
		}
		VersionChecker.doneChecking = true;
	}
}
